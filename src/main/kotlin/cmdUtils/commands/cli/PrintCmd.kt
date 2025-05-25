package cmdUtils.commands.cli

import cmdUtils.Command
import tools.*

/**
 * Imprime no terminal todos os argumentos passados ao comando.
 */
object PrintCmd : Command {
    override val description = "Print argument"
    override val longDescription = "Print the provided arguments to the terminal."
    override val usage = "print <words>"
    override val aliases = listOf("print")
    override val minArgs = 0
    override val maxArgs = -1
    override val requiresFile = false
    override val fileExtension = ""

    override fun run(args: List<String>): Boolean {
        if (!validateArgs(args, this)) return false
        println("${GREEN}${args.joinToString(" ")}${RESET}")
        return true
    }
}