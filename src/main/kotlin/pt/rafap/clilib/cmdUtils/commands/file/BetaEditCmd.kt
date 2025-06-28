package pt.rafap.clilib.cmdUtils.commands.file

import pt.rafap.clilib.cmdUtils.Command
import pt.rafap.clilib.cmdUtils.CommandInfo
import pt.rafap.clilib.datastore.Colors.CYAN
import pt.rafap.clilib.datastore.Colors.GREEN
import pt.rafap.clilib.datastore.Colors.WHITE
import pt.rafap.clilib.datastore.Colors.YELLOW
import pt.rafap.clilib.datastore.KeyBuffer
import pt.rafap.clilib.datastore.KeyCodes
import pt.rafap.clilib.tools.Environment
import pt.rafap.clilib.tools.clearPrompt
import pt.rafap.clilib.tools.validateArgs

/**
 * Simple terminal text editor used for quick file edits.
 * Enter text line by line. Use ':wq' to save and exit or ':q' to exit without saving.
 */
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
        println("${CYAN}Entering beta editor. Type ':wq' to save and exit, ':q' to quit without saving.${WHITE}")
        lines.forEachIndexed { i, line -> println(line) }
        var buffer = ""
        var editBuffer = ""
        var isEditing = true
        clearPrompt()
        KeyBuffer.clear() // Clear the key buffer before starting
        print("bed> ")
        while (true) {

            val temp = KeyBuffer.consume() ?: 0

            when (temp) {
                KeyCodes.BACKSPACE -> {
                    if (buffer.isNotEmpty()) {
                        buffer = buffer.dropLast(1)
                        print("\rbed> $buffer ")
                    } else {
                        print("\rbed> ")
                    }
                }
                KeyCodes.CTRL_C -> {
                    println("${YELLOW}Exiting without saving.${WHITE}")
                    return true
                }
                KeyCodes.ENTER -> {
                    if (buffer.isNotEmpty()) {
                        lines.add(buffer)
                        println("${lines.size}: $buffer")
                        buffer = ""
                    }
                    print("bed> ")
                }
                KeyCodes.ESCAPE -> {
                    isEditing = false
                }
                'I'.code -> { // 'I' key to toggle editing mode
                    isEditing = !isEditing
                    if (isEditing) {
                        print("\rbed> $buffer ")
                    } else {
                        print("\rbed> ${editBuffer} ")
                    }
                }
                else -> {
                    if (temp in 32..126) { // Printable ASCII characters
                        if (isEditing) {
                            editBuffer = ""
                            buffer += temp.toChar()
                            print("\rbed> $buffer ")
                        }
                        else {
                            // Handle escape sequences or other special keys if needed
                            when (editBuffer) {
                                ":wq" -> {
                                    file.parentFile?.mkdirs()
                                    file.writeText(lines.joinToString("\n"))
                                    println("${GREEN}File saved to ${file.absolutePath}$WHITE")
                                    return true
                                }
                                ":q" -> {
                                    println("${YELLOW}Exiting without saving.${WHITE}")
                                    return true
                                }
                                else -> {
                                    editBuffer += temp.toChar()
                                    print("bed> ")
                                }
                            }
                        }
                    }
                }
            }

            /*when (KeyBuffer) {
                ":wq" -> {
                    file.parentFile?.mkdirs()
                    file.writeText(lines.joinToString("\n"))
                    println("${GREEN}File saved to ${file.absolutePath}$RESET")
                    return true
                }
                ":q" -> {
                    println("${YELLOW}Exiting without saving.${RESET}")
                    return true
                }
                else -> lines.add(input)
            }*/
        }
    }
}

