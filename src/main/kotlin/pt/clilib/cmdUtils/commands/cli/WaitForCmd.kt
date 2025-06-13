package pt.clilib.cmdUtils.commands.cli

import pt.clilib.cmdUtils.Command
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

internal object WaitForCmd : Command {
        override val description = "Wait for user input"
        override val longDescription = "Wait for user input before proceeding."
        override val usage = "wfu [message]"
        override val aliases = listOf("waitforuser", "wfu")
        override val minArgs = 0
        override val maxArgs = -1

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