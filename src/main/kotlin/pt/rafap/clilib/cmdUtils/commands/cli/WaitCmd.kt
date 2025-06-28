package pt.rafap.clilib.cmdUtils.commands.cli

import pt.rafap.clilib.cmdUtils.Command
import pt.rafap.clilib.cmdUtils.CommandInfo
import pt.rafap.clilib.datastore.Colors.RED
import pt.rafap.clilib.datastore.Colors.WHITE
import pt.rafap.clilib.tools.validateArgs

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
    override val info = CommandInfo(
        description = "Wait for a specified time",
        longDescription = "Wait for a specified time in milliseconds.",
        usage = "wait <milliseconds>",
        aliases = listOf("wait", "w"),
        minArgs = 1,
        maxArgs = 1
    )

    override fun run(args: List<String>): Boolean {
        if (!validateArgs(args, this)) return false
        try {
            Thread.sleep(args[0].toLong())
        } catch (e: Exception) {
            println("${RED}App Error: Failed to wait. ${e.message}${WHITE}")
            return false
        }
        return true
    }
}