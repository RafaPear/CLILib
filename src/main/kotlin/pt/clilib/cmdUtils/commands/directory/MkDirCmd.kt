package pt.clilib.cmdUtils.commands.directory

import pt.clilib.cmdUtils.Command
import pt.clilib.cmdUtils.CommandInfo
import pt.clilib.tools.*
import java.io.File

object MkDirCmd : Command {
    override val info = CommandInfo(
        description = "Create a directory",
        longDescription = "Create a directory with the specified name.",
        usage = "mkdir <directory>",
        aliases = listOf("mkdir"),
        minArgs = 1,
        maxArgs = 1,
        requiresFile = false,
        fileExtension = ""
    )

    override fun run(args: List<String>): Boolean {
        if (!validateArgs(args, this)) return false
        val dirName = args[0]
        val dir = File(dirName)
        return if (dir.exists()) {
            println("${RED}App Error: Directory $dirName already exists.$RESET")
            false
        } else {
            dir.mkdirs()
            println("${CYAN}Directory $dirName created successfully!$RESET")
            true
        }
    }
}