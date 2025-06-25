package pt.clilib.tools

import pt.clilib.datastore.Buffer
import pt.clilib.datastore.KeyBuffer
import pt.clilib.tools.Environment.formatedPrompt

object TUI {

    val buffer = Buffer()

    fun clearAll() {
        buffer.clear()
        clearKeyBuffer()
        clearUpdatePrompt()
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
        updatePrompt()
        print("\u001B[0K") // Clear the line
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

    fun clearLine() {
        print("\u001B[0K") // Clear the current line
    }

    fun clearLineBelow() {
        print("\n\u001B[0K") // Clear the line below the cursor
    }
    /*private fun clearAndRedrawPrompt() {
            print("\u001B[2J") // Clear the screen
            print("\u001B[H") // Move cursor to the top left corner
            print(formatedPrompt) // Redraw the formatedPrompt
        }*/
}