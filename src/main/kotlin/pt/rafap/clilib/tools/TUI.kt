package pt.rafap.clilib.tools

import org.jline.nativ.JLineLibrary
import org.jline.nativ.JLineNativeLoader
import org.jline.utils.Colors.s
import pt.rafap.clilib.datastore.Ansi
import pt.rafap.clilib.datastore.KeyBuffer
import pt.rafap.clilib.datatypes.Buffer
import pt.rafap.clilib.tools.Environment.formatedPrompt

object TUI {

    fun getSizes(): Pair<Int, Int> {
        val signals: Array<String> = arrayOf(
            Ansi.SAVE_CURSOR,                  // save cursor position
            Ansi.ESC + "5000;5000H",          // move to col 5000 row 5000
            Ansi.ESC + "6n",                 // request cursor position
            Ansi.RESTORE_CURSOR,               // restore cursor position
        )
        for (s in signals) {
            print(s)
        }
        val sb = StringBuilder()
        val buff = ByteArray(1)
        while (System.`in`.read(buff, 0, 1) != -1) {
            sb.append(Char(buff[0].toUShort()))
            if ('R'.code.toByte() == buff[0]) {
                break
            }
        }
        val size = sb.toString()
        val rows = size.substring(size.indexOf(Ansi.ESC) + 2, size.indexOf(';'.code.toChar())).toInt()
        val cols = size.substring(size.indexOf(';'.code.toChar()) + 1, size.indexOf('R'.code.toChar())).toInt()
        return Pair(rows, cols)
    }

    // Get the terminal lines and colls dimensions dinamically using ANSI escape codes
    val HEIGHT: Int
        get() = getSizes().first

    val buffer = Buffer()

    fun clearAll() {
        buffer.clear()
        clearKeyBuffer()
    }

    /* Key buffer helper methods */
    fun consumeKey(): Int? = KeyBuffer.consume()
    fun consumeAllKeys(): List<Int> = KeyBuffer.consumeAll()
    fun clearKeyBuffer() = KeyBuffer.clear()
    fun keyBufferIsEmpty(): Boolean = KeyBuffer.isEmpty()
    fun keyBufferContains(key: Int): Boolean = KeyBuffer.contains(key)
    fun keyBufferContains(keys: String): Boolean = KeyBuffer.contains(keys)

    /* Terminal control methods */
    fun clearScreen() {
        print(Ansi.CLEAR_SCREEN)
        print(Ansi.CURSOR_HOME)
    }

    fun moveCursor(row: Int, col: Int) {
        print(Ansi.ESC + "${row};${col}H")
    }

    fun hideCursor() { print(Ansi.HIDE_CURSOR) }
    fun showCursor() { print(Ansi.SHOW_CURSOR) }

    fun printPrompt() {
        print("\r$formatedPrompt${buffer.content()}")
    }

    fun clearUpdatePrompt(printPrompt: Boolean = true) {
        clearLine()
        updatePrompt(printPrompt)
    }

    fun updatePrompt(printPrompt: Boolean = true) {
        if (printPrompt) {
            print("\r$formatedPrompt${buffer.content()}")
        }
        else {
            print("\r${buffer.content()}")
        }
        if (!buffer.isEmpty()) {
            val posFromEnd = buffer.content().length - 1 - buffer.cursorPosition()
            if (posFromEnd > 0) {
                print(Ansi.ESC + "${posFromEnd}D") // Move the cursor to the correct position
            }
        }
    }

    fun printBelow(text: String) {
        print(Ansi.SAVE_CURSOR)                // guarda posição
        clearLine()
        print(Ansi.ESC + "999C")             // move muito para a direita (CSI 999 C)
        print(Ansi.ESC + "${text.length}D")   // recua exactamente o tamanho da mensagem
        print(text)
        print(Ansi.RESTORE_CURSOR)             // restaura
    }

    fun printSaveCursor(func: () -> Unit) {
        print(Ansi.SAVE_CURSOR)                // guarda posição
        func()                                 // executa a função passada
        print(Ansi.RESTORE_CURSOR)             // restaura
    }


    fun printDebug(text: String) {
        if (Environment.debug)
            printBelow(text)
    }

    fun clearLineToEnd() {
        print(Ansi.CLEAR_LINE_TO_END) // Clear the current line
    }

    fun clearLineToStart() {
        print(Ansi.CLEAR_LINE_TO_START) // Clear the line from the start to the cursor
    }

    fun clearLine() {
        print(Ansi.CLEAR_LINE) // Clear the entire line
    }

    fun clearLineBelow() {
        print(Ansi.SAVE_CURSOR) // Save cursor position
        print(Ansi.ESC + "E" + Ansi.CLEAR_LINE) // Clear the line below the cursor
        print(Ansi.RESTORE_CURSOR) // Restore cursor position
    }


    fun printInLastLine(text: String) {
        print(Ansi.ESC + "999D")            // move muito para a esquerda (CSI 999 D)
        print(Ansi.ESC + "999B")             // move muito para a baixo (CSI 999 B)
        clearLine()
        print(text)
    }
    /*private fun clearAndRedrawPrompt() {
            print(Ansi.CLEAR_SCREEN) // Clear the screen
            print(Ansi.CURSOR_HOME) // Move cursor to the top left corner
            print(formatedPrompt) // Redraw the formatedPrompt
        }*/
}