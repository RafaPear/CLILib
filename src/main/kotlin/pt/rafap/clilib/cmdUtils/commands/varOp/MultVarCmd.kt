package pt.rafap.clilib.cmdUtils.commands.varOp

import pt.rafap.clilib.cmdUtils.Command
import pt.rafap.clilib.cmdUtils.CommandInfo
import pt.rafap.clilib.datastore.Operators
import pt.rafap.clilib.tools.performVarOperation

/**
 * Multiply two variables and store the result in a target variable.
 *
 * CommandInfo:
 * - description: Multiply two variables
 * - longDescription: Multiplies the values of two variables and stores the result in a target variable.
 * - usage: var mul <var1> <var2> <resultVar>
 * - aliases: mul, times
 * - minArgs: 2
 * - maxArgs: 3
 */
object MultVarCmd : Command {
    override val info = CommandInfo(
        description = "Multiply two variables",
        longDescription = "Multiplies the values of two variables and stores the result in a target variable.",
        usage = "var mul <var1> <var2> <resultVar>",
        aliases = listOf("mult", "times", "mul"),
        minArgs = 2,
        maxArgs = 3
    )

    override fun run(args: List<String>): Boolean {
        return performVarOperation(args, this, Operators.MUL())
    }
}