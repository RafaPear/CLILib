package pt.rafap.clilib.cmdUtils.commands.cli

import pt.rafap.clilib.cmdUtils.Command
import pt.rafap.clilib.cmdUtils.CommandInfo
import pt.rafap.clilib.datastore.Colors.CYAN
import pt.rafap.clilib.datastore.Colors.WHITE
import pt.rafap.clilib.tools.openExternalTerminal
import pt.rafap.clilib.tools.validateArgs

object WindowCmd : Command {
    override val info = CommandInfo(
        description = "Open a new terminal window",
        longDescription = "Open a new terminal window with the same session.",
        usage = "window",
        aliases = listOf("window"),
        minArgs = 0,
        maxArgs = 0
    )

    override fun run(args: List<String>): Boolean {
        if (!validateArgs(args, this)) return false
        println("${CYAN}Opening a new terminal window...${WHITE}")
        return openExternalTerminal()
    }
}
