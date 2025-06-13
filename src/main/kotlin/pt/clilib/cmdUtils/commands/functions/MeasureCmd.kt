package pt.clilib.cmdUtils.commands.functions

import pt.clilib.VarRegister
import pt.clilib.cmdUtils.Command
import pt.clilib.tools.*
import kotlin.time.measureTime

/**
 * Comando `MEASURE`
 *
 * Mede o tempo de execução de um comando.
 *
 * Uso: `measure <command>`
 *
 * Aceita qualquer comando como argumento. Útil para testes de desempenho.
 */
object MeasureCmd : Command {
    override val description = "Measure the time taken by a command"
    override val longDescription = "Measure the time taken to execute a command. Useful for performance testing."
    override val usage = "measure <command>"
    override val aliases = listOf("measure", "m", "time")
    override val minArgs = 1
    override val maxArgs = -1

    override fun run(args: List<String>): Boolean {
        if (!validateArgs(args, this)) return false
        val newArgs = args.joinToString(" ")

        println("${YELLOW}Measuring time for command: ${newArgs}${RESET}")

        val time = measureTime { cmdParser(newArgs) }
        VarRegister.setLastCmdDump(time)

        println("${GREEN}Time taken: ${time.inWholeMilliseconds} ms${RESET} \n")
        return true
    }
}