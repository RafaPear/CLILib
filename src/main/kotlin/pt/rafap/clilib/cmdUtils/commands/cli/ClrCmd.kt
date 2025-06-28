package pt.rafap.clilib.cmdUtils.commands.cli

import pt.rafap.clilib.cmdUtils.Command
import pt.rafap.clilib.cmdUtils.CommandInfo
import pt.rafap.clilib.tools.clearPrompt
import pt.rafap.clilib.tools.validateArgs

/**
* Limpa o ecrã do terminal imprimindo várias linhas vazias.
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