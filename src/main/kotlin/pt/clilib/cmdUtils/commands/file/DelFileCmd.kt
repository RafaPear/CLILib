package pt.clilib.cmdUtils.commands.file

import pt.clilib.cmdUtils.Command
import pt.clilib.cmdUtils.CommandInfo
import pt.clilib.tools.*

object DelFileCmd : Command {
    override val info = CommandInfo(
        description = "Delete a file",
        longDescription = "Deletes the specified file from the filesystem.",
        usage = "delfile <file_path>",
        aliases = listOf("delfile", "rmfile"),
        minArgs = 1,
        maxArgs = 1,
        requiresFile = false
    )

    override fun run(args: List<String>): Boolean {
        if (!validateArgs(args, this)) return false
        val filePath = args[0]
        val file = java.io.File(filePath)
        return if (file.exists()) {
            if (file.delete()) {
                println("${GREEN}File deleted successfully: $filePath$RESET")
                true
            } else {
                println("${RED}Error deleting file: $filePath$RESET")
                false
            }
        } else {
            println("${YELLOW}File not found: $filePath$RESET")
            false
        }
    }
}