package cmdUtils.commands.file

import cmdUtils.Command
import tools.*

object DelFileCmd : Command {
    override val description = "Delete a file"
    override val longDescription = "Deletes the specified file from the filesystem."
    override val usage = "delfile <file_path>"
    override val aliases = listOf("delfile", "rmfile")
    override val minArgs = 1
    override val maxArgs = 1
    override val requiresFile = false

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