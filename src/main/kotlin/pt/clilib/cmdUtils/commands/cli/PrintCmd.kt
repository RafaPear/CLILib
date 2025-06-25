package pt.clilib.cmdUtils.commands.cli

import pt.clilib.cmdUtils.Command
import pt.clilib.cmdUtils.CommandInfo
import pt.clilib.tools.*
import pt.clilib.datastore.Colors.GREEN

/**
 * Imprime no terminal todos os argumentos passados ao comando.
 */
object PrintCmd : Command {
    override val info = CommandInfo(
        description = "Print argument",
        longDescription = "Print the provided arguments to the terminal.",
        usage = "print <words>",
        aliases = listOf("print"),
        minArgs = 0,
        maxArgs = -1,
        requiresFile = false,
        fileExtension = ""
    )

    override fun run(args: List<String>): Boolean {
        if (!validateArgs(args, this)) return false
        println(args.joinToString(" ").colorize(GREEN))
        return true
    }
}