package pt.rafap.clilib.cmdUtils.commands.directory

import pt.rafap.clilib.cmdUtils.Command
import pt.rafap.clilib.cmdUtils.CommandInfo
import pt.rafap.clilib.datastore.Colors.GREEN
import pt.rafap.clilib.datastore.Colors.RED
import pt.rafap.clilib.datastore.Colors.WHITE
import pt.rafap.clilib.datastore.Colors.YELLOW
import pt.rafap.clilib.tools.validateArgs

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
                println("${GREEN}Directory deleted successfully: $dirPath$WHITE")
                true
            } else {
                println("${RED}Error deleting directory: $dirPath$WHITE")
                false
            }
        } else {
            println("${YELLOW}Directory not found or is not a directory: $dirPath$WHITE")
            false
        }
    }
}