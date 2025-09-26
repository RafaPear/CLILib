package pt.rafap.clilib.cmdUtils.commands.file

import pt.rafap.clilib.cmdUtils.Command
import pt.rafap.clilib.cmdUtils.CommandInfo
import pt.rafap.clilib.datastore.Colors.CYAN
import pt.rafap.clilib.datastore.Colors.RED
import pt.rafap.clilib.datastore.Colors.WHITE
import pt.rafap.clilib.tools.validateArgs
import java.io.File

/**
 * Creates a file with the specified name.
 *
 * CommandInfo:
 * - description: Create a file
 * - longDescription: Create a file with the specified name.
 * - usage: mkfile <file>
 * - aliases: mkfile
 * - minArgs: 1
 * - maxArgs: 1
 */
object MkFileCmd : Command {
    override val info = CommandInfo(
        description = "Create a file",
        longDescription = "Create a file with the specified name.",
        usage = "mkfile <file>",
        aliases = listOf("mkfile"),
        minArgs = 1,
        maxArgs = 1,
        requiresFile = false,
        fileExtension = ""
    )

    override fun run(args: List<String>): Boolean {
        if (!validateArgs(args, this)) return false
        val fileName = args[0]
        val file = File(fileName)
        return if (file.exists()) {
            println("${RED}App Error: File $fileName already exists.$WHITE")
            false
        } else {
            file.createNewFile()
            println("${CYAN}File $fileName created successfully!$WHITE")
            true
        }
    }
}