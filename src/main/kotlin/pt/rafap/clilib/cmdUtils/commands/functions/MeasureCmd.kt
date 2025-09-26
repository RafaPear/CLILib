package pt.rafap.clilib.cmdUtils.commands.functions

import pt.rafap.clilib.cmdUtils.Command
import pt.rafap.clilib.cmdUtils.CommandInfo
import pt.rafap.clilib.datastore.Colors.GREEN
import pt.rafap.clilib.datastore.Colors.WHITE
import pt.rafap.clilib.datastore.Colors.YELLOW
import pt.rafap.clilib.registers.VarRegister
import pt.rafap.clilib.tools.cmdParser
import pt.rafap.clilib.tools.validateArgs
import kotlin.time.measureTime

/**
 * Measure the execution time of a command.
 *
 * CommandInfo:
 * - description: Measure command execution time
 * - longDescription: Executes a command and measures the elapsed time; stores the timing result for later inspection.
 * - usage: measure <command>
 * - aliases: measure, m, time
 * - minArgs: 1
 * - maxArgs: -1
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