package pt.clilib.cmdUtils.commands.cli

import pt.clilib.cmdUtils.Command
import pt.clilib.cmdUtils.CommandInfo
import pt.clilib.tools.*

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