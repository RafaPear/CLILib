package pt.clilib.cmdUtils.commands.functions

import pt.clilib.registers.VarRegister
import pt.clilib.cmdUtils.Command
import pt.clilib.cmdUtils.CommandInfo
import pt.clilib.tools.*
import pt.clilib.datastore.Colors.GREEN
import pt.clilib.datastore.Colors.WHITE
import pt.clilib.datastore.Colors.YELLOW
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
    override val info = CommandInfo(
        description = "Measure the time taken by a command",
        longDescription = "Measure the time taken to execute a command. Useful for performance testing.",
        usage = "measure <command>",
        aliases = listOf("measure", "m", "time"),
        minArgs = 1,
        maxArgs = -1
    )

    override fun run(args: List<String>): Boolean {
        if (!validateArgs(args, this)) return false
        val newArgs = args.joinToString(" ")

        println("${YELLOW}Measuring time for command: ${newArgs}${WHITE}")

        val time = measureTime { cmdParser(newArgs) }
        VarRegister.setLastCmdDump(time)

        println("${GREEN}Time taken: ${time.inWholeMilliseconds} ms${WHITE} \n")
        return true
    }
}