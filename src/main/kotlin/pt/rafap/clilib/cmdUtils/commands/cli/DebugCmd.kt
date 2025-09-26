package pt.rafap.clilib.cmdUtils.commands.cli

import pt.rafap.clilib.cmdUtils.Command
import pt.rafap.clilib.cmdUtils.CommandInfo
import pt.rafap.clilib.datastore.Colors.WHITE
import pt.rafap.clilib.datastore.Colors.YELLOW
import pt.rafap.clilib.tools.Environment
import pt.rafap.clilib.tools.validateArgs

/**
 * Enables or disables debug mode.
 *
 * CommandInfo:
 * - description: Control debug mode
 * - longDescription: Enable or disable debug output to help during development.
 * - usage: debug [on|off|toggle]
 * - aliases: debug
 * - minArgs: 0
 * - maxArgs: 1
 */
object DebugCmd : Command {
    override val info = CommandInfo(
        description = "Debug command for testing purposes",
        usage = "debug",
        aliases = listOf("dbg", "debug"),
        minArgs = 0,
        maxArgs = 1
    )

    override fun run(args: List<String>): Boolean {
        if (!validateArgs(args, this)) return false
        if (args.isNotEmpty()) {
            try {
                val arg = args[0].toInt()
                Environment.debug =
                    when (arg) {
                        0 -> false
                        1 -> true
                        else -> Environment.debug
                    }
                return true
            }
            catch (e: NumberFormatException) { }
            try {
                val arg = args[0].toBoolean()
                Environment.debug = arg
                return true
            }
            catch (e: IllegalArgumentException) {
                println("${YELLOW}Invalid argument for debug command. Use 0 or 1, or true/false.$WHITE")
                return false
            }
        } else {
            println("${YELLOW}Debug mode is currently ${if (Environment.debug) "enabled" else "disabled"}.$WHITE")
        }
        // Add debugging logic here
        return true
    }
}