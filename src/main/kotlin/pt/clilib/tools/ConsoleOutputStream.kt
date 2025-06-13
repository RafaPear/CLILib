package pt.clilib.tools

import java.io.*
import java.nio.charset.StandardCharsets
import javax.swing.SwingUtilities

internal class ConsoleOutputStream(private val terminal: TerminalWindow) : OutputStream() {
    private val buffer = ByteArrayOutputStream()

    override fun write(b: Int) {
        buffer.write(b)
        // se for fim de linha, converte tudo até agora
        if (b == '\n'.code) flush()
    }

    override fun write(b: ByteArray, off: Int, len: Int) {
        buffer.write(b, off, len)
        // busca se no chunk aparece '\n'
        if (b.copyOfRange(off, off + len).contains('\n'.code.toByte())) flush()
    }

    override fun flush() {
        val bytes = buffer.toByteArray()
        if (bytes.isNotEmpty()) {
            val text = String(bytes, StandardCharsets.UTF_8)
            SwingUtilities.invokeLater {
                terminal.append(text)
            }
            buffer.reset()
        }
    }



    override fun close() {
        flush()
    }
}
