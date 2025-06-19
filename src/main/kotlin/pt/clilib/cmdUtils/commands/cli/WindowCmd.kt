package pt.clilib.cmdUtils.commands.cli

import pt.clilib.cmdUtils.Command
import pt.clilib.cmdUtils.CommandInfo
import pt.clilib.tools.*

object WindowCmd : Command {
    override val info = CommandInfo(
        description = "Open a new terminal window",
        longDescription = "Open a new terminal window with the same session.",
        usage = "window",
        aliases = listOf("window"),
        minArgs = 0,
        maxArgs = 0,
        requiresFile = false,
        fileExtension = ""
    )

    override fun run(args: List<String>): Boolean {
        if (!validateArgs(args, this)) return false
        return openExternalTerminal()
    }
}
