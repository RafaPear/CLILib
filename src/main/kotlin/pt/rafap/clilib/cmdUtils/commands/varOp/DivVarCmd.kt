package pt.rafap.clilib.cmdUtils.commands.varOp

import pt.rafap.clilib.cmdUtils.Command
import pt.rafap.clilib.cmdUtils.CommandInfo
import pt.rafap.clilib.datastore.Operators
import pt.rafap.clilib.tools.performVarOperation

/**
 * Divide two variables and store the result in a target variable.
 *
 * CommandInfo:
 * - description: Divide two variables
 * - longDescription: Divides the value of one variable by another and stores the result in a target variable.
 * - usage: var div <var1> <var2> <resultVar>
 * - aliases: div, divide
 * - minArgs: 2
 * - maxArgs: 3
 */
object DivVarCmd : Command {
    override val info = CommandInfo(
        description = "Divide two variables",
        longDescription = "Divides the value of one variable by another and stores the result in a target variable.",
        usage = "var div <var1> <var2> <resultVar>",
        aliases = listOf("div", "divide"),
        minArgs = 2,
        maxArgs = 3
    )

    override fun run(args: List<String>): Boolean {
        return performVarOperation(args, this, Operators.DIV())
    }
}