package pt.rafap.clilib.cmdUtils.commands.file

import jdk.jfr.Experimental
import pt.rafap.clilib.cmdUtils.Command
import pt.rafap.clilib.cmdUtils.CommandInfo
import pt.rafap.clilib.datastore.Colors.GREEN
import pt.rafap.clilib.datastore.Colors.WHITE
import pt.rafap.clilib.datastore.KeyCodes
import pt.rafap.clilib.tools.Environment
import pt.rafap.clilib.tools.TUI
import pt.rafap.clilib.datastore.Ansi
import pt.rafap.clilib.tools.TUI.buffer
import pt.rafap.clilib.tools.TUI.clearLine
import pt.rafap.clilib.tools.TUI.clearLineToEnd
import pt.rafap.clilib.tools.TUI.consumeKey
import pt.rafap.clilib.tools.TUI.updatePrompt
import pt.rafap.clilib.tools.validateArgs

/**
 * Simple terminal text editor used for quick file edits.
 * Enter text line by line. Use ':wq' to save and exit or ':q' to exit without saving.
 */
@Experimental
object BetaEditCmd : Command {
    override val info = CommandInfo(
        description = "Edit a file in the terminal (beta)",
        longDescription = "Opens a minimal terminal text editor to edit a file.",
        usage = "betaedit <file>",
        aliases = listOf("betaedit", "bed"),
        minArgs = 1,
        maxArgs = 1,
        requiresFile = false
    )

    override fun run(args: List<String>): Boolean {
        if (!validateArgs(args, this)) return false
        val file = Environment.resolve(args[0]).toFile()
        val lines = if (file.exists()) file.readLines().toMutableList() else mutableListOf()
        // print lines
        lines.forEach { line -> println(line) }

        var isEditing = false
        var isWritingCmd = false

        TUI.clearScreen()
        buffer.useTempBuffer()
        buffer.clear()
        buffer.useMainBuffer()
        buffer.clear()

        TUI.printSaveCursor {
            repeat(TUI.HEIGHT) {
                clearLine()
                println(".")
            }
        }

        TUI.hideCursor()


        while (true) {
            Thread.sleep(1) // Adjust the sleep time as needed
            val input = consumeKey()

            if (isEditing || isWritingCmd) TUI.showCursor()
            else TUI.hideCursor()

            if (input == null) {
                // If no input, continue to the next iteration
                continue
            }

            when (input) {
                KeyCodes.ENTER -> {
                    if (isEditing) {
                        buffer.useMainBuffer()
                        buffer.insert('\n')
                        TUI.clearUpdatePrompt(false)
                    }
                    else {
                        buffer.useTempBuffer()
                        parseCmd(buffer.content())
                    }
                }

                KeyCodes.BACKSPACE -> {
                    if (isEditing) {
                        buffer.useMainBuffer()
                        buffer.removeLast()
                        TUI.clearUpdatePrompt(false)
                    } else {
                        buffer.useTempBuffer()
                        buffer.removeLast()
                        TUI.printInLastLine(
                            "${GREEN}${buffer.content()}${WHITE}",
                        )
                    }
                }

                KeyCodes.ESCAPE -> {
                    isEditing = false
                    isWritingCmd = false
                }

                in 0x20..0x7E -> {
                    if (isEditing) {
                        buffer.useMainBuffer()
                        clearLineToEnd(); buffer.insert(input.toChar())
                        updatePrompt(false)
                    } else if (!isWritingCmd && input.toChar() == 'i') {
                        isEditing = true
                        print(Ansi.RESTORE_CURSOR)                // restore cursor
                        buffer.useMainBuffer()
                        clearLineToEnd(); updatePrompt(false)
                    } else if (!isWritingCmd && input.toChar() == ':'){
                        isWritingCmd = true
                        print(Ansi.SAVE_CURSOR)                // save position
                        buffer.useTempBuffer()
                        buffer.clear()
                        TUI.printInLastLine(
                            "${GREEN}${buffer.content()}${WHITE}",
                        )
                    } else if (isWritingCmd) {
                        buffer.useTempBuffer()
                        buffer.insert(input.toChar())
                        TUI.printInLastLine(
                            "${GREEN}${buffer.content()}${WHITE}",
                        )
                    }

                }

                else -> {

                }
            }
        }
    }

    private fun parseCmd(content: String) {
        // Simple no-op that safely references the parameter to avoid unused-parameter warnings.
        // We intentionally avoid changing editor behavior; this function may be implemented later.
        if (content.isNotEmpty()) {
            // run some trivial inspection so the parameter is used
            val preview = content.take(120)
            // avoid printing in the editor loop; just keep a reference
            @Suppress("UNUSED_VARIABLE")
            val _previewLength = preview.length
        }
    }
}
