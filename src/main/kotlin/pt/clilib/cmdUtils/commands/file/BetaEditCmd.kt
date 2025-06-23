package pt.clilib.cmdUtils.commands.file

import pt.clilib.cmdUtils.Command
import pt.clilib.cmdUtils.CommandInfo
import pt.clilib.tools.*
import java.io.File

/**
 * Simple terminal text editor used for quick file edits.
 * Enter text line by line. Use ':wq' to save and exit or ':q' to exit without saving.
 */
object BetaEditCmd : Command {
    override val info = CommandInfo(
        description = "Edit a file in the terminal (beta)",
        longDescription = "Opens a minimal terminal text editor to edit a file.",
        usage = "betaedit <file>",
        aliases = listOf("betaedit"),
        minArgs = 1,
        maxArgs = 1,
        requiresFile = false
    )

    override fun run(args: List<String>): Boolean {
        if (!validateArgs(args, this)) return false
        val file = Environment.resolve(args[0]).toFile()
        val lines = if (file.exists()) file.readLines().toMutableList() else mutableListOf()
        println(
            "${CYAN}Entering beta editor.${RESET}\n" +
            "Type ':help' for available commands."
        )

        fun printLines() {
            lines.forEachIndexed { i, line -> println("${i + 1}: $line") }
        }

        printLines()

        while (true) {
            print("betaedit> ")
            val input = readLine() ?: return false
            val tokens = input.split(" ", limit = 3)
            when (tokens[0]) {
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
                ":p", ":print" -> printLines()
                ":d", ":del" -> {
                    if (tokens.size >= 2) {
                        val idx = tokens[1].toIntOrNull()?.minus(1)
                        if (idx != null && idx in lines.indices) {
                            lines.removeAt(idx)
                        } else {
                            println("${RED}Invalid line number${RESET}")
                        }
                    }
                }
                ":i", ":insert" -> {
                    if (tokens.size >= 3) {
                        val idx = tokens[1].toIntOrNull()?.minus(1)
                        if (idx != null && idx in 0..lines.size) {
                            lines.add(idx, tokens[2])
                        } else {
                            println("${RED}Invalid line number${RESET}")
                        }
                    }
                }
                ":r", ":replace" -> {
                    if (tokens.size >= 3) {
                        val idx = tokens[1].toIntOrNull()?.minus(1)
                        if (idx != null && idx in lines.indices) {
                            lines[idx] = tokens[2]
                        } else {
                            println("${RED}Invalid line number${RESET}")
                        }
                    }
                }
                ":help" -> {
                    println(
                        "Commands:\n" +
                        ":p               - print file contents\n" +
                        ":i <n> <text>    - insert line before n\n" +
                        ":d <n>           - delete line n\n" +
                        ":r <n> <text>    - replace line n\n" +
                        ":wq              - save and quit\n" +
                        ":q               - quit without saving"
                    )
                }
                else -> lines.add(input)
            }
        }
    }
}

