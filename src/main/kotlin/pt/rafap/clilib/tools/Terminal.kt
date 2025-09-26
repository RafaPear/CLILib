package pt.rafap.clilib.tools

import pt.rafap.clilib.datastore.Colors.GRAY
import pt.rafap.clilib.datastore.Colors.RED
import pt.rafap.clilib.datastore.Colors.WHITE
import pt.rafap.clilib.datastore.InputHistory
import pt.rafap.clilib.datastore.KeyCodes
import pt.rafap.clilib.datastore.Ansi
import pt.rafap.clilib.registers.CmdRegister
import pt.rafap.clilib.tools.TUI.buffer
import pt.rafap.clilib.tools.TUI.clearAll
import pt.rafap.clilib.tools.TUI.clearKeyBuffer
import pt.rafap.clilib.tools.TUI.clearLineToEnd
import pt.rafap.clilib.tools.TUI.clearUpdatePrompt
import pt.rafap.clilib.tools.TUI.consumeKey
import pt.rafap.clilib.tools.TUI.printBelow
import pt.rafap.clilib.tools.TUI.printDebug
import pt.rafap.clilib.tools.TUI.printPrompt
import pt.rafap.clilib.tools.TUI.updatePrompt

/**
 * Terminal interaction engine: processes real-time keyboard input,
 * provides history navigation, autocomplete, and dispatches commands to the parser.
 */
object Terminal {

    /** Cycle index for TAB autocomplete suggestions. */
    var tabCycle = -1

    /**
     * Starts the terminal interaction loop. Requires [isRunningInTerminal] to be true.
     * Reads keys, updates the buffer and executes commands when the user presses ENTER.
     */
    fun doTerminalInteraction() {
        if (!isRunningInTerminal()) {
            println("${RED}App Error: Not running in terminal.${WHITE}")
            return
        }
        TUI.clearScreen()

        clearKeyBuffer() // Clear the key buffer before starting
        printPrompt() // Print the initial prompt
        while (true) {
            Thread.sleep(1) // Adjust the sleep time as needed
            val key = consumeKey()

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
                    clearLineToEnd() ; buffer.insert(key.toChar()) ; updatePrompt()
                }
                KeyCodes.CTRL_C, KeyCodes.ESCAPE -> {
                    println("${RED}Exiting CLI...${WHITE}")
                    break
                }
                else -> {
                    // Handle other keys if needed
                    println("\n${RED}Unsupported key: ${key}${WHITE}")
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
            clearUpdatePrompt()
        } else {
            buffer.useMainBuffer()
            clearUpdatePrompt()
        }
        printDebug("Next input: $next, current buffer: '${buffer.content()}', isTemp: ${buffer.isUsingTempBuffer()}")
    }

    private fun doArrowUp() {
        val prev = InputHistory.previous()
        if (prev != null) {
            buffer.useTempBuffer()
            buffer.clear()
            buffer.add(prev)
        }
        printDebug("Previous input: $prev, current buffer: '${buffer.content()}', isTemp: ${buffer.isUsingTempBuffer()}")
        clearUpdatePrompt()
    }

    private fun doBackspace() {
        if (buffer.isUsingTempBuffer()){
            buffer.switchToMainBuffer()
        }

        if (!buffer.isEmpty()) {
            buffer.remove(1)
            clearUpdatePrompt()
            print(Ansi.DELETE_CHAR_AT_CURSOR) // Delete char at cursor
        } else {
            updatePrompt()
        }
    }

    private fun doEnter() {
        clearLineToEnd()
        if (tabCycle != -1) {
            doTab(true) // Cycle through tab suggestions
            return
        }
        if (buffer.isUsingTempBuffer()){
            buffer.switchToMainBuffer()
            InputHistory.resetIndex()
        } else {
            InputHistory.add(buffer.content())
        }

        println()
        cmdParser(buffer.content())
        clearAll()
        clearUpdatePrompt()
    }

    private fun doTab(get: Boolean = false) {

        val similar = CmdRegister.findAllSimilar(buffer.content()).toMutableList()
        similar += ""
        if (similar.dropLast(1).isNotEmpty()) {
            printTab(similar, get)
        } else if (buffer.content().isNotBlank()) {

            /* isola só o token depois do último espaço (ex.: "ls S" → "S") */
            val fullBuf = buffer.content()
            val lastSpace = fullBuf.lastIndexOf(' ')
            val token    = if (lastSpace >= 0) fullBuf.substring(lastSpace + 1) else fullBuf

            val all = findSimilarFiles(token) // Lista de ficheiros e pastas

            printDebug("Tab completion: found ${all.size} matches")

            if (all.isNotEmpty()) {
                printTab(all, get, token.length, buffer.content()) // Print the first item or cycle through them
            } else {
                printBelow("${RED}No files or directories found.${WHITE}")
            }
        }

        updatePrompt()
    }

    private fun printTab(lst: List<String>, get: Boolean = false, size : Int = buffer.size(), extra: String = "") {
        if (get) {
            buffer.clear()
            buffer.add(extra.dropLast(size) + lst[tabCycle]) // Drop the leading space
            tabCycle = -1
        } else {
            tabCycle++
            if (tabCycle >= lst.size) {
                tabCycle = 0 // Reset the cycle
            }
            clearUpdatePrompt()
            print("${GRAY}${lst[tabCycle].drop(size)}$WHITE")
        }
    }

    private fun findSimilarFiles(searchTerm: String): List<String> {
        val currentDir = Environment.root.toFile()
        if (!currentDir.exists() || !currentDir.isDirectory) {
            printBelow("${RED}Current directory does not exist or is not a directory.$WHITE")
            return emptyList()
        }

        printDebug("Tab completion: searching '${searchTerm}' inside '${currentDir.path}'")

        /* listas “cruas” (sem cor) para podermos completar o buffer sem códigos ANSI */
        val rawFiles = currentDir.listFiles()?.filter { it.isFile      && it.name.startsWith(searchTerm, true) } ?: emptyList()
        val rawDirs  = currentDir.listFiles()?.filter { it.isDirectory && it.name.startsWith(searchTerm, true) } ?: emptyList()

        val files = rawFiles.map { it.name }.sortedBy { it.lowercase() }
        val dirs  = rawDirs .map { "${it.name}/" }.sortedBy { it.lowercase() }
        val all   = files + dirs                                                     // display-list para printTab
        return all
    }
}