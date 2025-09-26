package pt.rafap.clilib.cmdUtils.commands.cli

import pt.rafap.clilib.cmdUtils.Command
import pt.rafap.clilib.cmdUtils.CommandInfo
import pt.rafap.clilib.datastore.Colors.RED
import pt.rafap.clilib.datastore.Colors.WHITE
import pt.rafap.clilib.tools.validateArgs

/**
 * Pause execution for a specified duration.
 *
 * CommandInfo:
 * - description: Pause execution
 * - longDescription: Suspends the CLI for a specified amount of time (milliseconds).
 * - usage: wait <milliseconds>
 * - aliases: wait, sleep
 * - minArgs: 1
 * - maxArgs: 1
 */
object WaitCmd : Command {
    override val info = CommandInfo(
        description = "Pause execution",
        longDescription = "Suspend CLI execution for a specified time in milliseconds.",
        usage = "wait <milliseconds>",
        aliases = listOf("wait", "sleep"),
        minArgs = 1,
        maxArgs = 1
    )

    override fun run(args: List<String>): Boolean {
        if (!validateArgs(args, this)) return false
        try {
            Thread.sleep(args[0].toLong())
        } catch (e: Exception) {
            println("${RED}App Error: Failed to wait. ${e.message}${WHITE}")
            return false
        }
        return true
    }
}