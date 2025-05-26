package cmdUtils.commands.directory

import cmdUtils.Command
import tools.*

object DelDirCmd : Command {
    override val description = "Delete a directory"
    override val longDescription = "Deletes the specified directory from the filesystem."
    override val usage = "deldir <directory_path>"
    override val aliases = listOf("deldir", "rmdir")
    override val minArgs = 1
    override val maxArgs = 1
    override val requiresFile = false

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