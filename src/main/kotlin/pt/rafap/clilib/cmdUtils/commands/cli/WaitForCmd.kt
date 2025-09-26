package pt.rafap.clilib.cmdUtils.commands.cli

import pt.rafap.clilib.cmdUtils.Command
import pt.rafap.clilib.cmdUtils.CommandInfo
import pt.rafap.clilib.datastore.Colors.WHITE
import pt.rafap.clilib.datastore.Colors.YELLOW
import pt.rafap.clilib.datastore.KeyCodes
import pt.rafap.clilib.tools.TUI
import pt.rafap.clilib.tools.validateArgs

/**
 * Wait for the user to press Enter (or another key) before continuing.
 *
 * CommandInfo:
 * - description: Wait for user input
 * - longDescription: Pause execution until the user presses Enter; an optional message can be displayed while waiting.
 * - usage: waitfor <message>
 * - aliases: waitfor, wfu
 * - minArgs: 0
 * - maxArgs: -1
 */

object WaitForCmd : Command {
    override val info = CommandInfo(
        description = "Wait for user input",
        longDescription = "Pause execution until the user presses Enter; optionally display a message while waiting.",
        usage = "waitfor <message>",
        aliases = listOf("waitforuser", "waitfor", "wfu"),
        minArgs = 0,
        maxArgs = -1
    )

    override fun run(args: List<String>): Boolean {
        if (!validateArgs(args, this)) return false
        if (args.isNotEmpty()) {
            println("${YELLOW}${args.joinToString(" ")}${WHITE}")
        } else {
            println("${YELLOW}Press Enter to continue...${WHITE}")
        }
        var key = TUI.consumeKey()
        while (key != KeyCodes.ENTER) { // Wait for Enter key
            key = TUI.consumeKey()
        }
        println()
        return true
    }
}