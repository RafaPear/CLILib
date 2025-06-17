package pt.clilib.tools

import java.awt.*
import java.awt.event.*
import javax.swing.*
import javax.swing.text.*
import java.util.regex.Pattern
import pt.clilib.cmdUtils.CmdRegister
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener
import kotlin.concurrent.thread


class TerminalWindow(
    title: String = "CLI",
    bgColor: Color = Color.BLACK,
    fgColor: Color = Color.WHITE,
    private val prompt: String = "> ",
    initText: String = "",
) : JFrame(title) {

    private val textPane = JTextPane()
    private val doc: StyledDocument = textPane.styledDocument
    private val scrollPane = JScrollPane(textPane)
    private val commandHistory = mutableListOf<String>()
    private var historyIndex = -1
    private var inputStartOffset = 0
    private var fontSize = 14
    private val fontFamily = "JetBrains Mono"
    // --------------  depois das tuas variáveis ---------------
    private val modelo = DefaultListModel<Sugestao>()
    private val lista  = JList(modelo) .apply {
        selectionMode = ListSelectionModel.SINGLE_SELECTION
        cellRenderer = criarRenderer()
        background = Color(30, 30, 30)
        selectionBackground = Color(60, 60, 60)
        // elimina a moldura feia da lista
        border = BorderFactory.createEmptyBorder()
    }
    private val popup = JPopupMenu().apply {
        border = BorderFactory.createLineBorder(Color(80, 80, 80))
        add(JScrollPane(lista).apply {
            border = BorderFactory.createEmptyBorder()
            preferredSize = Dimension(260, 120)
            horizontalScrollBarPolicy = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
        })
    }
    private data class Sugestao(val alias: String, val usage: String, val desc: String) {
        override fun toString() = alias   // o modelo usa isto (importante!)
    }
               // já herda o tipo Sugestao

    private var cmdThread = thread {}
    private var runningCmd: Boolean = false

    init {
        textPane.font = Font(fontFamily, Font.PLAIN, fontSize)
        textPane.background = bgColor
        textPane.foreground = fgColor
        textPane.caretColor = fgColor
        textPane.isEditable = true

        val im = lista.getInputMap(JComponent.WHEN_FOCUSED)
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "choose")
        // im.put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB,   0), "choose")   // opcional
        val am = lista.actionMap
        am.put("choose", object : AbstractAction() {
            override fun actionPerformed(e: ActionEvent?) = aceitarPopup()
        })

        // duplo-clique também confirma
        lista.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                if (e.clickCount == 2) aceitarPopup()
            }
        })


        setupStyles()
        setupZoomBindings(textPane)

        textPane.addKeyListener(object : KeyAdapter() {
            override fun keyTyped(e: KeyEvent) {
                if (!Character.isISOControl(e.keyChar) && !runningCmd) {
                    val end = maxOf(textPane.selectionStart+1, textPane.selectionEnd)
                    if (inputStartOffset > 0 && end < inputStartOffset) {  // < em vez de <=
                        e.consume()
                    }
                }
            }

            override fun keyPressed(e: KeyEvent) {
                if (e.keyCode == KeyEvent.VK_BACK_SPACE && !runningCmd) {
                    val end = maxOf(textPane.selectionStart-1, textPane.selectionEnd-1)
                    if (inputStartOffset > 0 && end < inputStartOffset) {  // < em vez de <=
                        e.consume()
                        return
                    }
                }

                if (e.keyCode == KeyEvent.VK_TAB && !runningCmd) {
                    e.consume()
                    if (popup.isVisible) {
                        val nxt = (lista.selectedIndex + 1) % modelo.size
                        lista.selectedIndex = nxt
                        lista.ensureIndexIsVisible(nxt)
                    } else {
                        autocomplete()
                    }
                    return
                }

                // ENTER  →  se popup aberto confirma, senão processa comando
                if (e.keyCode == KeyEvent.VK_ENTER && !e.isShiftDown) {
                    if (popup.isVisible) {
                        e.consume(); aceitarPopup(); return
                    }
                    else if (!runningCmd) {
                        e.consume(); processCommand(); return
                    } else {
                        // Se já está a correr um comando, ignora o ENTER
                        e.consume()
                        append("\n", "default")
                        System.out.flush()
                        SwingUtilities.invokeLater {
                            drawPrompt()  // desenha o prompt novamente
                            scrollToBottom()  // garante que o cursor está visível
                        }
                        runningCmd = false
                        return
                    }
                }

                // ESC fecha popup
                if (e.keyCode == KeyEvent.VK_ESCAPE && popup.isVisible && !runningCmd) {
                    popup.isVisible = false
                    e.consume(); return
                }

                if (popup.isVisible && (e.keyCode == KeyEvent.VK_UP || e.keyCode == KeyEvent.VK_DOWN) && !runningCmd) {
                    e.consume()
                    val delta = if (e.keyCode == KeyEvent.VK_UP) -1 else 1
                    val next = (lista.selectedIndex + delta + modelo.size) % modelo.size
                    lista.selectedIndex = next
                    lista.ensureIndexIsVisible(next)
                    return
                }

                if (e.keyCode == KeyEvent.VK_UP && !popup.isVisible && !runningCmd)    { e.consume(); showPreviousCommand(); return }
                if (e.keyCode == KeyEvent.VK_DOWN && !popup.isVisible && !runningCmd)  { e.consume(); showNextCommand();     return }
                if (e.keyCode == KeyEvent.VK_F11)   { e.consume(); toggleFullScreen();    return }
                if (e.keyCode == KeyEvent.VK_BACK_SPACE && !runningCmd) { return }
            }
        })


        /*textPane.addCaretListener {
            if (textPane.caretPosition < inputStartOffset) {
                textPane.caretPosition = doc.length
            }
        }*/

        doc.addDocumentListener(object : DocumentListener {
            override fun insertUpdate(e: DocumentEvent?) {
                e ?: return
                // só scrolla se a alteração ocorreu ANTES do prompt (output)
                if (e.offset < inputStartOffset) scrollToBottom()
            }
            override fun removeUpdate(e: DocumentEvent?) {
                e ?: return
                // idem para remoções
                if (e.offset < inputStartOffset) scrollToBottom()
            }
            override fun changedUpdate(e: DocumentEvent?) {}
        })


        scrollPane.verticalScrollBar.unitIncrement = 16
        contentPane.add(scrollPane)
        defaultCloseOperation = EXIT_ON_CLOSE
        setSize(800, 500)
        isVisible = true


        // escreve a mensagem de boas-vindas ANTES de mostrar o prompt
        append(initText, "default")

        drawPrompt()

        Environment.window
    }

    /** Renderer simples, com bom contraste */
    /** Renderer estilizado para mostrar alias, usage e descrição */
    private fun criarRenderer(): ListCellRenderer<in Sugestao> =
        object : DefaultListCellRenderer() {

            private fun hex(c: Color) = "#%02x%02x%02x".format(c.red, c.green, c.blue)

            private val bg      = Color(25, 25, 25)      // fundo normal
            private val selBG   = Color(0, 120, 170)     // fundo seleccionado
            private val aliasC  = Color.WHITE            // alias a branco puro
            private val usageC  = Color(200, 200, 200)   // usage cinza claro
            private val descC   = Color(150, 150, 150)   // descrição cinza médio

            override fun getListCellRendererComponent(
                list: JList<*>?, value: Any?, index: Int,
                selected: Boolean, focus: Boolean
            ): Component {
                val s = value as Sugestao
                val comp = super.getListCellRendererComponent(list, "", index, selected, focus) as JLabel

                comp.text = """
                <html>
                    <div>
                        <span style='color:${hex(aliasC)}; font-weight:bold'>${s.usage.replace('<', '(').replace('>',')')}</span>
                        &nbsp;&nbsp;<br>
                        <span style='color:${hex(descC)}; font-size:90%'>${s.desc}</span>
                    </div>
                </html>
            """.trimIndent()

                comp.background = if (selected) selBG else bg
                comp.border = BorderFactory.createEmptyBorder(4, 8, 4, 8)
                comp.font = textPane.font.deriveFont(fontSize.toFloat())   // respeita zoom
                comp.isOpaque = true
                return comp
            }
        }

    /** Mostra popup com todas as hipóteses */
    private fun mostrarPopup(op: List<Sugestao>) {
        modelo.clear(); op.forEach(modelo::addElement)
        lista.selectedIndex = 0
        val p = textPane.modelToView2D(textPane.caretPosition)
        popup.show(textPane, p.x.toInt(), p.y.toInt() + textPane.font.size)
        SwingUtilities.invokeLater { lista.requestFocusInWindow() }
    }

    /** Aplica a opção seleccionada e fecha popup */
    private fun aceitarPopup() {
        val sel = lista.selectedValue ?: return
        replaceInput(sel.alias)          // insere apenas o nome do comando
        popup.isVisible = false
        SwingUtilities.invokeLater { lista.requestFocusInWindow() }
    }

    private fun setupStyles() {
        // estilo base com cor, família e tamanho
        val defaultStyle = textPane.addStyle("default", null).apply {
            StyleConstants.setForeground(this, Color.WHITE)
            StyleConstants.setFontFamily(this, fontFamily)
            StyleConstants.setFontSize(this, fontSize)
        }

        // input herda o default mas reafirma a cor branca
        textPane.addStyle("input", defaultStyle).apply {
            StyleConstants.setForeground(this, Color.WHITE)
        }

        // restantes cores ANSI
        mapOf(
            "prompt" to Color.GREEN,
            "red"    to Color(255, 80, 80),
            "green"  to Color(0, 220, 0),
            "yellow" to Color.YELLOW,
            "blue"   to Color.CYAN,
            "magenta" to Color.MAGENTA,
            "cyan"   to Color(0, 220, 220),
            "gray"   to Color(150, 150, 150)
        ).forEach { (name, color) ->
            textPane.addStyle(name, defaultStyle).also {
                StyleConstants.setForeground(it, color)
            }
        }
    }

    private fun setupZoomBindings(tp: JTextPane) {
        val im = tp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
        val am = tp.actionMap

        val zoomInPlus  = KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, InputEvent.CTRL_DOWN_MASK)
        val zoomInEquals  = KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, InputEvent.CTRL_DOWN_MASK)
        val zoomOutMinus = KeyStroke.getKeyStroke(KeyEvent.VK_MINUS,  InputEvent.CTRL_DOWN_MASK)

        im.put(zoomInPlus,  "zoomIn")
        im.put(zoomInEquals,  "zoomIn")
        im.put(zoomOutMinus, "zoomOut")

        am.put("zoomIn", object : AbstractAction() {
            override fun actionPerformed(e: ActionEvent?) { adjustFontSize(+1) }
        })
        am.put("zoomOut", object : AbstractAction() {
            override fun actionPerformed(e: ActionEvent?) { adjustFontSize(-1) }
        })
    }

    private fun adjustFontSize(delta: Int) {
        fontSize = (fontSize + delta).coerceIn(10, 42)

        // 1) actualiza o estilo “default” (texto existente e futuro em append)
        val defaultStyle = textPane.getStyle("default")
        StyleConstants.setFontSize(defaultStyle, fontSize)

        // 2) actualiza o estilo “input” (é usado para typing directo)
        val inputStyle = textPane.getStyle("input")
        StyleConstants.setFontSize(inputStyle, fontSize)

        // 3) reaplica ao documento todo o texto existente
        val applyAttr = SimpleAttributeSet().apply {
            StyleConstants.setFontSize(this, fontSize)
            StyleConstants.setFontFamily(this, fontFamily)
        }
        doc.setCharacterAttributes(0, doc.length, applyAttr, false)

        textPane.revalidate()
        textPane.repaint()
    }

    private fun scrollToBottom() {
        SwingUtilities.invokeLater { textPane.caretPosition = doc.length }
    }

    fun drawPrompt() {
        append(prompt, "prompt")
        inputStartOffset = doc.length
        // Faz com que tudo o que vier a seguir a partir daqui seja com o estilo “input”
        val inputStyle = textPane.getStyle("input")
        textPane.caretPosition = doc.length
        textPane.setCharacterAttributes(inputStyle, true)
        scrollToBottom()
    }

    private fun processCommand() {
        val input = doc.getText(inputStartOffset, doc.length - inputStartOffset).trim()
        append("\n", "default")

        if (input.isNotBlank()) {
            Environment.window = this  // garante que o ambiente está actualizado
            commandHistory.add(input)
            historyIndex = commandHistory.size
            if (!input.lowercase().contains("window") && !runningCmd) {
                runningCmd = true
                cmdThread = thread {
                    cmdParser(input)
                    SwingUtilities.invokeLater {
                        drawPrompt()  // desenha o prompt novamente
                        scrollToBottom()  // garante que o cursor está visível
                    }
                    runningCmd = false
                }
            }
            else {
                println("${RED}Command 'window' cannot be executed from within a window.$RESET")
                System.out.flush()
                SwingUtilities.invokeLater {
                    drawPrompt()  // desenha o prompt novamente
                    scrollToBottom()  // garante que o cursor está visível
                }
            }
        } else{
            System.out.flush()
            SwingUtilities.invokeLater {
                drawPrompt()  // desenha o prompt novamente
                scrollToBottom()  // garante que o cursor está visível
            }
        }

    }



    private fun showPreviousCommand() {
        // Garante que historyIndex nunca ultrapassa o tamanho
        historyIndex = historyIndex.coerceAtMost(commandHistory.size)
        if (historyIndex > 0) {
            historyIndex--
            replaceInput(commandHistory[historyIndex])
        }
    }

    private fun showNextCommand() {
        // Garante que historyIndex nunca fica abaixo de zero
        historyIndex = historyIndex.coerceAtLeast(0)
        if (historyIndex < commandHistory.size - 1) {
            historyIndex++
            replaceInput(commandHistory[historyIndex])
        } else if (historyIndex == commandHistory.size - 1) {
            // Se estamos no último comando, passar para linha vazia
            historyIndex = commandHistory.size
            replaceInput("")
        }
    }


    private fun clearInput() { replaceInput("") }

    private fun replaceInput(newText: String) {
        doc.remove(inputStartOffset, doc.length - inputStartOffset)
        doc.insertString(inputStartOffset, newText, doc.getStyle("input"))
        textPane.caretPosition = doc.length
        // Garante que DIGITAR a seguir mantém o estilo “input”
        textPane.setCharacterAttributes(textPane.getStyle("input"), true)
    }


    private fun autocomplete() {
        val input = doc.getText(inputStartOffset, doc.length - inputStartOffset).trim()

        val todas = CmdRegister.all().flatMap { cmd ->
            cmd.aliases.map { alias -> Sugestao(alias, cmd.usage, cmd.description) }
        }

        val candidatas = todas.filter { it.alias.startsWith(input) }.sortedBy { it.alias }

        when (candidatas.size) {
            0 -> Toolkit.getDefaultToolkit().beep()
            1 -> replaceInput(candidatas.first().alias)
            else -> mostrarPopup(candidatas)
        }
    }

    private fun toggleFullScreen() {
        val full = graphicsConfiguration.device.fullScreenWindow == this
        dispose()
        if (full) {
            graphicsConfiguration.device.fullScreenWindow = null
            isUndecorated = false
            extendedState = JFrame.NORMAL
        } else {
            isUndecorated = true
            graphicsConfiguration.device.fullScreenWindow = this
            extendedState = JFrame.MAXIMIZED_BOTH
        }
        isVisible = true
    }

    fun append(text: String, style: String = "default") {
        val matcher = ansiPattern.matcher(text)
        var last = 0
        var cur = style
        while (matcher.find()) {
            val pre = text.substring(last, matcher.start())
            doc.insertString(doc.length, pre, doc.getStyle(cur))
            cur = ansiToStyle(matcher.group(1))
            last = matcher.end()
        }
        if (last < text.length)
            doc.insertString(doc.length, text.substring(last), doc.getStyle(cur))
    }

    private fun ansiToStyle(code: String) = when (code) {
        "31" -> "red";    "32" -> "green"
        "33" -> "yellow"; "34" -> "blue"
        "35" -> "magenta";"36" -> "cyan"
        "90" -> "gray";   else -> "default"
    }

    companion object {
        private val ansiPattern = Pattern.compile("\u001B\\[(\\d{1,2})m")
    }
}
