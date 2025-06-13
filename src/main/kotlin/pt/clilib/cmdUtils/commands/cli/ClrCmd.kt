package pt.clilib.cmdUtils.commands.cli

import pt.clilib.cmdUtils.Command
import pt.clilib.tools.*

/**
* Limpa o ecrã do terminal imprimindo várias linhas vazias.
*/
object ClrCmd : Command {
    override val description = "Clear the screen"
    override val longDescription = "Clear the screen by printing multiple empty lines."
    override val usage = "clr"
    override val aliases = listOf("clr")
    override val minArgs = 0
    override val maxArgs = 0
    override val requiresFile = false
    override val fileExtension = ""

    override fun run(args: List<String>): Boolean {
        if (!validateArgs(args, this)) return false
        println("[[CLEAR]]")
        clearPrompt()
        return true
    }
}