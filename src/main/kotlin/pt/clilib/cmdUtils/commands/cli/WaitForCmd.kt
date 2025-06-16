package pt.clilib.cmdUtils.commands.cli

import pt.clilib.cmdUtils.Command
import pt.clilib.cmdUtils.CommandInfo
import pt.clilib.tools.*

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
                println("${YELLOW}${args.joinToString(" ")}${RESET}")
            } else {
                println("${YELLOW}Press Enter to continue...${RESET}")
            }
            readLine()
            return true
        }
}