package cmdUtils.commands.cli
import tools.*
import cmdUtils.Command
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
    override val description = "Exit the application"
    override val usage = "exit"
    override val aliases = listOf("e", "exit")

    override fun run(args: List<String>): Boolean {
        if (!validateArgs(args, this)) return false
        println("${YELLOW}Exiting...$RESET")
        exitProcess(0)
    }
}
