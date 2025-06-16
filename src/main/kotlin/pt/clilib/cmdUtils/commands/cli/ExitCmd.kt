package pt.clilib.cmdUtils.commands.cli
import pt.clilib.tools.*
import pt.clilib.cmdUtils.Command
import pt.clilib.cmdUtils.CommandInfo
import kotlin.system.exitProcess

/**
 * Comando `EXIT`
 *
 * Encerra a aplicação CLI.
 *
 * Uso: `EXIT`
 *
 * Não recebe argumentos.
 */
object ExitCmd : Command {
    override val info = CommandInfo(
        description = "Exit the application",
        usage = "exit",
        aliases = listOf("e", "exit")
    )

    override fun run(args: List<String>): Boolean {
        if (!validateArgs(args, this)) return false
        println("${YELLOW}Exiting...$RESET")
        exitProcess(0)
    }
}
