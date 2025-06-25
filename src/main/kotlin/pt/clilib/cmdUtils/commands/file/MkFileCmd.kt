package pt.clilib.cmdUtils.commands.file

import pt.clilib.cmdUtils.Command
import pt.clilib.cmdUtils.CommandInfo
import pt.clilib.tools.*
import pt.clilib.datastore.Colors.CYAN
import pt.clilib.datastore.Colors.RED
import pt.clilib.datastore.Colors.WHITE
import java.io.File

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