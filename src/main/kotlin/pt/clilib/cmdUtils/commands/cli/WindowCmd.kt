package pt.clilib.cmdUtils.commands.cli

import pt.clilib.cmdUtils.Command
import pt.clilib.cmdUtils.CommandInfo
import pt.clilib.tools.*
import pt.clilib.datastore.Colors.CYAN
import pt.clilib.datastore.Colors.RESET

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
        println("${CYAN}Opening a new terminal window...${RESET}")
        return openExternalTerminal()
    }
}
