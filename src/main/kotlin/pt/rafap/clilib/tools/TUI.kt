package pt.rafap.clilib.tools

import pt.rafap.clilib.datastore.KeyBuffer
import pt.rafap.clilib.datatypes.Buffer
import pt.rafap.clilib.tools.Environment.formatedPrompt

object TUI {

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
        print("\u001B[2J")
        print("\u001B[H")
    }

    fun moveCursor(row: Int, col: Int) {
        print("\u001B[${row};${col}H")
    }

    fun hideCursor() { print("\u001B[?25l") }
    fun showCursor() { print("\u001B[?25h") }

    fun printPrompt() {
        print("\r$formatedPrompt${buffer.content()}")
    }

    fun clearUpdatePrompt() {
        clearLine()
        updatePrompt()
    }

    fun updatePrompt() {
        print("\r$formatedPrompt${buffer.content()}")
        if (!buffer.isEmpty()) {
            val posFromEnd = buffer.content().length - 1 - buffer.cursorPosition()
            if (posFromEnd > 0) {
                print("\u001B[${posFromEnd}D") // Move the cursor to the correct position
            }
        }
    }

    fun printBelow(text: String) {
        val ESC = "\u001B["      // sequência CSI
        print("${ESC}s")                // guarda posição
        clearLineToEnd()
        print("${ESC}999C")             // move muito para a direita (CSI 999 C)
        print("${ESC}${text.length}D")   // recua exactamente o tamanho da mensagem
        print(text)
        print("${ESC}u")                // restaura
    }


    fun printDebug(text: String) {
        if (Environment.debug)
            printBelow(text)
    }

    fun clearLineToEnd() {
        print("\u001B[0K") // Clear the current line
    }

    fun clearLineToStart() {
        print("\u001B[1K") // Clear the line from the start to the cursor
    }

    fun clearLine() {
        print("\u001B[2K") // Clear the entire line
    }

    fun clearLineBelow() {
        print("\u001B[s") // Save cursor position
        print("\u001B[E\u001B[2K") // Clear the line below the cursor
        print("\u001B[u") // Restore cursor position
    }
    /*private fun clearAndRedrawPrompt() {
            print("\u001B[2J") // Clear the screen
            print("\u001B[H") // Move cursor to the top left corner
            print(formatedPrompt) // Redraw the formatedPrompt
        }*/
}