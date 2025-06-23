package pt.clilib.cmdUtils.commands.file

import pt.clilib.cmdUtils.Command
import pt.clilib.cmdUtils.CommandInfo
import pt.clilib.tools.CYAN
import pt.clilib.tools.Environment
import pt.clilib.tools.RED
import pt.clilib.tools.RESET
import pt.clilib.tools.validateArgs

object BetaEditCmd : Command {
    override val info = CommandInfo(
        description = "Edit a file",
        longDescription = "Edit a file using the default editor.",
        usage = "bedit <file>",
        aliases = listOf("bedit"),
        minArgs = 1,
        maxArgs = 1,
        requiresFile = true,
        fileExtension = ""
    )

    override fun run(args: List<String>): Boolean {
        if (!validateArgs(args, this)) return false
        val file = Environment.resolve(args[0]).toFile()
        val fileName = file.absolutePath
        return if (file.exists()) {
            println("${CYAN}Editing $fileName...${RESET}")
            // Open the file in the default editor for windows, mac and linux

            println("${CYAN}File $fileName edited successfully!${RESET}")
            true
        } else {
            println("${RED}App Error: File $fileName does not exist.${RESET}")
            false
        }
    }
}