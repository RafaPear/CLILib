package pt.clilib.cmdUtils.commands.file

import pt.clilib.cmdUtils.Command
import pt.clilib.cmdUtils.CommandInfo
import pt.clilib.tools.*
import java.io.File

/**
 * Minimal terminal-based text editor.
 * Commands:
 *  :p           print lines
 *  :w           save file
 *  :wq          save and quit
 *  :q           quit without saving
 *  :h           show help
 *  i <n>        insert line after n (0 to insert at start)
 *  d <n>        delete line n
 *  <n> <text>   replace line n
 *  any other input appends a line
 */
object BetaEditCmd : Command {
    override val info = CommandInfo(
        description = "Edit a file in the terminal (beta)",
        longDescription = "Opens a simple line editor for quick file edits.",
        usage = "betaedit <file>",
        aliases = listOf("betaedit"),
        minArgs = 1,
        maxArgs = 1,
        requiresFile = false
    )

    override fun run(args: List<String>): Boolean {
        if (!validateArgs(args, this)) return false
        val file = Environment.resolve(args[0]).toFile()
        val lines = if (file.exists()) file.readLines().toMutableList() else mutableListOf<String>()

        fun printLines() {
            lines.forEachIndexed { i, line -> println("${'$'}{i + 1}: ${'$'}line") }
        }

        println("${CYAN}Beta editor started. Type ':h' for help.${RESET}")
        printLines()
        var modified = false
        while (true) {
            print("betaedit> ")
            val input = readLine() ?: break
            when {
                input == ":h" -> {
                    println(
                        """Commands:\n" +
                                ":p           print lines\n" +
                                ":w           save file\n" +
                                ":wq          save and quit\n" +
                                ":q           quit without saving\n" +
                                "i <n>        insert line after n (0 for start)\n" +
                                "d <n>        delete line n\n" +
                                "<n> <text>   replace line n"""
                    )
                }
                input == ":p" -> printLines()
                input == ":wq" -> {
                    file.parentFile?.mkdirs()
                    file.writeText(lines.joinToString("\n"))
                    println("${GREEN}File saved to ${'$'}{file.absolutePath}${RESET}")
                    return true
                }
                input == ":w" -> {
                    file.parentFile?.mkdirs()
                    file.writeText(lines.joinToString("\n"))
                    println("${GREEN}File saved.${RESET}")
                    modified = false
                }
                input == ":q" -> {
                    if (modified) println("${YELLOW}Exiting without saving changes.${RESET}")
                    return true
                }
                input.startsWith("i ") -> {
                    val num = input.substringAfter("i ").trim().toIntOrNull()
                    if (num == null || num !in 0..lines.size) {
                        println("${RED}Invalid line number.${RESET}")
                    } else {
                        print("insert> ")
                        val text = readLine() ?: ""
                        lines.add(num, text)
                        modified = true
                    }
                }
                input.startsWith("d ") -> {
                    val num = input.substringAfter("d ").trim().toIntOrNull()
                    if (num == null || num !in 1..lines.size) {
                        println("${RED}Invalid line number.${RESET}")
                    } else {
                        lines.removeAt(num - 1)
                        modified = true
                    }
                }
                input.matches(Regex("^\\d+\\s+.*")) -> {
                    val idx = input.substringBefore(" ").toIntOrNull()
                    if (idx == null || idx !in 1..lines.size) {
                        println("${RED}Invalid line number.${RESET}")
                    } else {
                        val text = input.substringAfter(" ")
                        lines[idx - 1] = text
                        modified = true
                    }
                }
                else -> {
                    lines.add(input)
                    modified = true
                }
            }
        }
        return true
    }
}

