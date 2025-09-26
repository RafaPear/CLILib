package pt.rafap.clilib.cmdUtils.commands.cli

import pt.rafap.clilib.cmdUtils.Command
import pt.rafap.clilib.cmdUtils.CommandInfo
import pt.rafap.clilib.tools.clearPrompt
import pt.rafap.clilib.tools.validateArgs

/**
 * Clears the terminal screen.
 *
 * CommandInfo:
 * - description: Clear terminal
 * - longDescription: Clears the visible content of the terminal/console.
 * - usage: clr
 * - aliases: clear, cls, clr
 * - minArgs: 0
 * - maxArgs: 0
 */
object ClrCmd : Command {
    override val info = CommandInfo(
        description = "Clear the screen",
        longDescription = "Clear the screen by printing multiple empty lines.",
        usage = "clr",
        aliases = listOf("clr"),
        minArgs = 0,
        maxArgs = 0,
        requiresFile = false,
        fileExtension = ""
    )

    override fun run(args: List<String>): Boolean {
        if (!validateArgs(args, this)) return false
        clearPrompt()
        return true
    }
}