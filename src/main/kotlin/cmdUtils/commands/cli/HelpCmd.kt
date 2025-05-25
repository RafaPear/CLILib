package cmdUtils.commands.cli

import cmdUtils.CmdRegister
import cmdUtils.Command
import tools.*

/**
 * Comando `HELP`
 *
 * Mostra todos os comandos disponíveis na aplicação, bem como a sua descrição e modo de uso.
 *
 * Uso: `HELP`
 *
 * Não necessita de argumentos.
 */
object HelpCmd : Command {
    override val description = "Show help information"
    override val longDescription = "Show all available cmdUtils.commands and their usage."
    override val usage = "help [command]"
    override val aliases = listOf("h", "help")
    override val minArgs = 0
    override val maxArgs = 1

    override fun run(args: List<String>): Boolean {
        if (!validateArgs(args, this)) return false
        val entries = CmdRegister.all()
        if (args.isNotEmpty()) {
            entries.forEach { cmd ->
                if (args[0].lowercase() in cmd.aliases) {
                    // Print help but description is long description and formats in a better readable way
                    println("${CYAN}${cmd.usage}$RESET - ${YELLOW}Alias: ${cmd.aliases.joinToString(", ")}$RESET")
                    println("${YELLOW}${cmd.longDescription}$RESET")

                    return true
                }
            }
            println("${RED}App Error: Unknown command ${args[0]}$RESET")
        }
        else {
            println("${YELLOW}Arguments with [] are optional and arguments with <> are required.$RESET")
            println("${CYAN}Available cmdUtils.commands:$RESET")
            entries.forEach { cmd ->
                println("${CYAN}${cmd.usage}$RESET - ${YELLOW}Alias: ${cmd.aliases.joinToString(", ")}$RESET - ${cmd.description}")
            }
        }
        return true
    }
}
