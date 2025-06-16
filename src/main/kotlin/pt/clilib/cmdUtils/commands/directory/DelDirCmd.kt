package pt.clilib.cmdUtils.commands.directory

import pt.clilib.cmdUtils.Command
import pt.clilib.cmdUtils.CommandInfo
import pt.clilib.tools.*

object DelDirCmd : Command {
    override val info = CommandInfo(
        description = "Delete a directory",
        longDescription = "Deletes the specified directory from the filesystem.",
        usage = "deldir <directory_path>",
        aliases = listOf("deldir", "rmdir"),
        minArgs = 1,
        maxArgs = 1,
        requiresFile = false
    )

    override fun run(args: List<String>): Boolean {
        if (!validateArgs(args, this)) return false
        val dirPath = args[0]
        val dir = java.io.File(dirPath)
        return if (dir.exists() && dir.isDirectory) {
            if (dir.deleteRecursively()) {
                println("${GREEN}Directory deleted successfully: $dirPath$RESET")
                true
            } else {
                println("${RED}Error deleting directory: $dirPath$RESET")
                false
            }
        } else {
            println("${YELLOW}Directory not found or is not a directory: $dirPath$RESET")
            false
        }
    }
}