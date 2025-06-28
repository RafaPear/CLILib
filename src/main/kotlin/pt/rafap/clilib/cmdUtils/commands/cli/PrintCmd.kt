package pt.rafap.clilib.cmdUtils.commands.cli

import pt.rafap.clilib.cmdUtils.Command
import pt.rafap.clilib.cmdUtils.CommandInfo
import pt.rafap.clilib.datastore.Colors.GREEN
import pt.rafap.clilib.tools.colorize
import pt.rafap.clilib.tools.validateArgs

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