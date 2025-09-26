package pt.rafap.clilib.cmdUtils.commands.cli
import pt.rafap.clilib.cmdUtils.Command
import pt.rafap.clilib.cmdUtils.CommandInfo
import pt.rafap.clilib.datastore.Colors.WHITE
import pt.rafap.clilib.datastore.Colors.YELLOW
import pt.rafap.clilib.tools.validateArgs
import kotlin.system.exitProcess

/**
 * Exits the CLI application.
 *
 * CommandInfo:
 * - description: Exit the application
 * - longDescription: Finalizes the CLI session and terminates the program.
 * - usage: exit
 * - aliases: exit, quit
 * - minArgs: 0
 * - maxArgs: 0
 */
object ExitCmd : Command {
    override val info = CommandInfo(
        description = "Exit the application",
        usage = "exit",
        aliases = listOf("e", "exit")
    )

    override fun run(args: List<String>): Boolean {
        if (!validateArgs(args, this)) return false
        println("${YELLOW}Exiting...$WHITE")
        exitProcess(0)
    }
}
