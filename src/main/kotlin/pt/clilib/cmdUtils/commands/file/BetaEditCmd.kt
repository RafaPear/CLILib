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
        aliases = listOf("betaedit", "bed"),
        minArgs = 1,
        maxArgs = 1,
        requiresFile = false
    )

    override fun run(args: List<String>): Boolean {
        if (!validateArgs(args, this)) return false
        val file = Environment.resolve(args[0]).toFile()
        val lines = if (file.exists()) file.readLines().toMutableList() else mutableListOf()
        println("${CYAN}Entering beta editor. Type ':wq' to save and exit, ':q' to quit without saving.${RESET}")
        lines.forEachIndexed { i, line -> println("${i + 1}: $line") }
        while (true) {
            print("betaedit> ")
            val input = readLine() ?: return false
            when (input) {
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
            }
        }
    }
}

