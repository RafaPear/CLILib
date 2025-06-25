package pt.clilib.tools

import pt.clilib.datastore.Buffer
import pt.clilib.tools.Environment.prompt

object TUI {

    val buffer = Buffer()

    fun clearUpdatePrompt() {
        updatePrompt()
        print("\u001B[0K") // Clear the line
    }

    fun updatePrompt() {
        print("\r$prompt${buffer.content()}")
        val posFromEnd = buffer.content().length - buffer.cursorPosition()
        if (posFromEnd > 0) {
            print("\u001B[${posFromEnd}D") // Move the cursor to the correct position
        }
    }

    fun clearToLineEnd() {
        print("\u001B[0K") // Clear the current line
    }

    fun clearToLineStart() {
        print("\u001B[1K") // Clear from the cursor to the start of the line
    }

    fun clearLine() {
        print("\u001B[2K") // Clear the current line
    }

    fun clearLineBelow() {
        print("\n\u001B[0K") // Clear the line below the cursor
    }
    /*private fun clearAndRedrawPrompt() {
            print("\u001B[2J") // Clear the screen
            print("\u001B[H") // Move cursor to the top left corner
            print(prompt) // Redraw the prompt
        }*/
}