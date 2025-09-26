package pt.rafap.clilib.cmdUtils.commands.cli

import pt.rafap.clilib.cmdUtils.Command
import pt.rafap.clilib.cmdUtils.CommandInfo
import pt.rafap.clilib.datastore.Colors.GREEN
import pt.rafap.clilib.tools.tExt.colorize
import pt.rafap.clilib.tools.validateArgs

/**
 * Prints formatted text to the terminal.
 *
 * CommandInfo:
 * - description: Print text to terminal
 * - longDescription: Displays plain or formatted text, honoring colors and environment variables.
 * - usage: print <text>
 * - aliases: print, echo
 * - minArgs: 1
 * - maxArgs: 99
 */
object PrintCmd : Command {
    override val info = CommandInfo(
        description = "Print argument",
        longDescription = "Print the provided arguments to the terminal.",
        usage = "print <words>",
        aliases = listOf("print"),
        minArgs = 0,
        maxArgs = -1,
        requiresFile = false,
        fileExtension = ""
    )

    override fun run(args: List<String>): Boolean {
        if (!validateArgs(args, this)) return false
        println(args.joinToString(" ").colorize(GREEN))
        return true
    }
}