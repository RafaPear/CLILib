package pt.clilib.tools

import pt.clilib.datastore.Buffer
import pt.clilib.datastore.Colors.BLUE
import pt.clilib.datastore.Colors.GRAY
import pt.clilib.datastore.Colors.GREEN
import pt.clilib.datastore.Colors.RED
import pt.clilib.datastore.Colors.RESET
import pt.clilib.datastore.InputHistory
import pt.clilib.datastore.KeyBuffer
import pt.clilib.datastore.KeyCodes
import pt.clilib.registers.CmdRegister
import pt.clilib.tools.Environment.prompt
import pt.clilib.tools.TUI.buffer
import pt.clilib.tools.TUI.clearLineBelow
import pt.clilib.tools.TUI.clearUpdatePrompt
import pt.clilib.tools.TUI.updatePrompt

object Terminal {

    fun doTerminalInteraction() {
        if (!isRunningInTerminal()) {
            println("${RED}App Error: Not running in terminal.${RESET}")
            return
        }

        KeyBuffer.clear() // Clear the key buffer before starting
        clearUpdatePrompt()
        while (true) {
            Thread.sleep(1) // Adjust the sleep time as needed
            val key = KeyBuffer.consume()

            when (key) {
                null -> { continue }
                KeyCodes.ENTER -> { doEnter() }
                KeyCodes.BACKSPACE -> { doBackspace() }
                KeyCodes.TAB -> { doTab() }
                KeyCodes.ARROW_UP -> { doArrowUp() }
                KeyCodes.ARROW_DOWN -> { doArrowDown() }
                KeyCodes.ARROW_LEFT -> { doArrowLeft() }
                KeyCodes.ARROW_RIGHT -> { doArrowRight() }
                in 0x20..0x7E -> { // Printable ASCII characters
                    buffer.insert(key.toChar()) ; updatePrompt()
                }
                KeyCodes.CTRL_C, KeyCodes.ESCAPE -> {
                    println("${RED}Exiting CLI...${RESET}")
                    break
                }
                else -> {
                    // Handle other keys if needed
                    println("\n${RED}Unsupported key: ${key}${RESET}")
                    updatePrompt()
                }
            }
        }
    }

    private fun doArrowRight() {
        if (buffer.moveCursorRight(1) < buffer.size()) {
            updatePrompt()
        }
    }

    private fun doArrowLeft() {
        if (buffer.moveCursorLeft(1) > 0) {
            updatePrompt()
        }
    }

    private fun doArrowDown() {
        val next = InputHistory.next()
        if (next != null) {
            buffer.useTempBuffer()
            buffer.clear()
            buffer.add(next)
        } else {
            buffer.useMainBuffer()
        }
        clearUpdatePrompt()
    }

    private fun doArrowUp() {
        val prev = InputHistory.previous()
        if (prev != null) {
            buffer.useTempBuffer()
            buffer.clear()
            buffer.add(prev)
        }
        clearUpdatePrompt()
    }

    private fun doBackspace() {
        if (buffer.isUsingTempBuffer()){
            buffer.switchToMainBuffer()
        }

        if (!buffer.isEmpty()) {
            buffer.remove(1)
            updatePrompt()
            print("\u001B[P") // Move cursor back one position
        } else {
            updatePrompt()
        }
    }

    private fun doEnter() {
        if (buffer.isUsingTempBuffer()){
            buffer.clear()
            buffer.switchToMainBuffer()
            InputHistory.resetIndex()
        } else InputHistory.add(buffer.content())

        clearLineBelow()
        cmdParser(buffer.content())
        print("\r$prompt")
        buffer.clear()
        KeyBuffer.clear()
    }

    private fun doTab(){
        print("\u001B[s") // Save cursor position
        print("\n\u001B[0K")

        val similar = CmdRegister.findAllSimilar(buffer.content())
        if (similar.isNotEmpty()) {
            print("${GRAY}${similar.joinToString(", ")}$RESET")
        } else if (buffer.content().isNotBlank()) {
            // Find folders and files in the current directory. Colorize them differently
            val currentDir = Environment.root.toFile()
            val files = currentDir.listFiles()?.filter { it.isFile }?.map { it.name.colorize(BLUE) } ?: emptyList()
            val dirs = currentDir.listFiles()?.filter { it.isDirectory }?.map { "${it.name}/".colorize(GREEN) } ?: emptyList()
            if (files.isNotEmpty() || dirs.isNotEmpty()) {
                val items = (files + dirs).sorted()
                print("${GRAY}${items.joinToString(", ")}$RESET")
            } else {
                print("${RED}No files or directories found.${RESET}")
            }
        }

        print("\u001B[u") // Restore cursor position
        print("\r$prompt${buffer.content()}")
    }
}