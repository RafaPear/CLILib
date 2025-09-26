package pt.rafap.clilib.cmdUtils.commands.file

import pt.rafap.clilib.cmdUtils.Command
import pt.rafap.clilib.cmdUtils.CommandInfo
import pt.rafap.clilib.datastore.Colors.GREEN
import pt.rafap.clilib.datastore.Colors.RED
import pt.rafap.clilib.datastore.Colors.WHITE
import pt.rafap.clilib.datastore.Colors.YELLOW
import pt.rafap.clilib.tools.validateArgs

/**
 * Deletes a file from the filesystem.
 *
 * CommandInfo:
 * - description: Delete a file
 * - longDescription: Deletes the specified file from the filesystem.
 * - usage: delfile <file_path>
 * - aliases: delfile, rmfile
 * - minArgs: 1
 * - maxArgs: 1
 */
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
                println("${GREEN}File deleted successfully: $filePath$WHITE")
                true
            } else {
                println("${RED}Error deleting file: $filePath$WHITE")
                false
            }
        } else {
            println("${YELLOW}File not found: $filePath$WHITE")
            false
        }
    }
}