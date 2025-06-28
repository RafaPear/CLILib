package pt.rafap.clilib.cmdUtils.commands.cli

import pt.rafap.clilib.cmdUtils.Command
import pt.rafap.clilib.cmdUtils.CommandInfo
import pt.rafap.clilib.datastore.Colors.WHITE
import pt.rafap.clilib.datastore.Colors.YELLOW
import pt.rafap.clilib.tools.validateArgs

/**
 * Comando `WAITFORUSER`
 *
 * Faz uma pausa na execução do programa até que o utilizador pressione Enter.
 *
 * Uso: `waitforuser [message]`
 *
 * Aceita um argumento opcional que é a mensagem a ser exibida antes de esperar pela entrada do utilizador.
 */

object WaitForCmd : Command {
        override val info = CommandInfo(
            description = "Wait for user input",
            longDescription = "Wait for user input before proceeding.",
            usage = "wfu [message]",
            aliases = listOf("waitforuser", "wfu"),
            minArgs = 0,
            maxArgs = -1
        )

        override fun run(args: List<String>): Boolean {
            if (!validateArgs(args, this)) return false
            if (args.isNotEmpty()) {
                println("${YELLOW}${args.joinToString(" ")}${WHITE}")
            } else {
                println("${YELLOW}Press Enter to continue...${WHITE}")
            }
            readLine()
            println()
            return true
        }
}