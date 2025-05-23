package commands

import cmdRegister.Command
import tools.*

/**
 * Comando `WAIT`
 *
 * Faz uma pausa na execução do programa por um determinado número de milissegundos.
 *
 * Uso: `wait <milliseconds>`
 *
 * Aceita um argumento que é o número de milissegundos a esperar.
 */

object WaitCmd : Command {
    override val description = "Wait for a specified time"
    override val longDescription = "Wait for a specified time in milliseconds."
    override val usage = "wait <milliseconds>"
    override val aliases = listOf("wait", "w")
    override val minArgs = 1
    override val maxArgs = 1

    override fun run(args: List<String>): Boolean {
        if (!validateArgs(args, this)) return false
        try {
            Thread.sleep(args[0].toLong())
        } catch (e: Exception) {
            println("${RED}App Error: Failed to wait. ${e.message}${RESET}")
            return false
        }
        return true
    }
}