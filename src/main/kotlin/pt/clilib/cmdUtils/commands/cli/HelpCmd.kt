package pt.clilib.cmdUtils.commands.cli

import pt.clilib.registers.CmdRegister
import pt.clilib.cmdUtils.Command
import pt.clilib.cmdUtils.CommandInfo
import pt.clilib.tools.*
import pt.clilib.datastore.Colors.CYAN
import pt.clilib.datastore.Colors.RED
import pt.clilib.datastore.Colors.WHITE
import pt.clilib.datastore.Colors.YELLOW

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
    override val info = CommandInfo(
        description = "Show help information",
        longDescription = "Show all available cmdUtils.commands and their usage.",
        usage = "help [command]",
        aliases = listOf("h", "help"),
        minArgs = 0,
        maxArgs = 1
    )

    override fun run(args: List<String>): Boolean {
        if (!validateArgs(args, this)) return false
        val entries = CmdRegister.all()
        if (args.isNotEmpty()) {
            entries.forEach { cmd ->
                if (args[0].lowercase() in cmd.aliases.map { it.lowercase() }) {
                    // Print help but description is long description and formats in a better readable way
                    println("${CYAN}${cmd.usage}$WHITE - ${YELLOW}Alias: ${cmd.aliases.joinToString(", ")}$WHITE")
                    println("${YELLOW}${cmd.longDescription}$WHITE")

                    return true
                }
            }
            println("${RED}App Error: Unknown command ${args[0]}$WHITE")
        }
        else {
            println("${YELLOW}Arguments with [] are optional and arguments with <> are required.$WHITE")
            println("${CYAN}Available cmdUtils.commands:$WHITE")
            entries.forEach { cmd ->
                println("${CYAN}${cmd.usage}$WHITE - ${YELLOW}Alias: ${cmd.aliases.joinToString(", ")}$WHITE - ${cmd.description}")
            }
        }
        return true
    }
}
