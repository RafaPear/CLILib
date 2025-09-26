package pt.rafap.clilib.cmdUtils.commands.directory

import pt.rafap.clilib.cmdUtils.Command
import pt.rafap.clilib.cmdUtils.CommandInfo
import pt.rafap.clilib.datastore.Colors.RED
import pt.rafap.clilib.datastore.Colors.WHITE
import pt.rafap.clilib.tools.Environment
import pt.rafap.clilib.tools.validateArgs

/**
 * Changes the current directory to the specified relative directory, or to the parent directory with "..".
 *
 * CommandInfo:
 * - description: Change directory
 * - longDescription: Change the current directory to a specified relative directory or to the parent directory with '..'.
 * - usage: cd <directory>
 * - aliases: cd
 * - minArgs: 1
 * - maxArgs: 1
 */
object CdCmd : Command {
    override val info = CommandInfo(
        description = "Change directory",
        longDescription = "Change the current directory to a specified relative directory or to the parent directory with '..'.",
        usage = "cd <directory>",
        aliases = listOf("cd"),
        minArgs = 1,
        maxArgs = 1,
        requiresFile = false,
        fileExtension = ""
    )

    override fun run(args: List<String>): Boolean {
        if (!validateArgs(args, this)) return false
        try {
            val pathArg = args[0]
            if (pathArg == "..") {
                Environment.root = Environment.root.parent ?: Environment.root
            } else {
                val target = Environment.resolve(pathArg)
                if (target.toFile().isDirectory) {
                    Environment.root = target
                } else {
                    println("${RED}App Error: Path does not exist or is invalid: $pathArg$WHITE")
                    return false
                }
            }
        } catch (e: Exception) {
            println("${RED}App Error: Failed to change directory. ${e.message}$WHITE")
            return false
        }
        return true
    }
}