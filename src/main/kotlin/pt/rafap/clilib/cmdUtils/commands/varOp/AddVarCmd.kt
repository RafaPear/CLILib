package pt.rafap.clilib.cmdUtils.commands.varOp

import pt.rafap.clilib.cmdUtils.Command
import pt.rafap.clilib.cmdUtils.CommandInfo
import pt.rafap.clilib.datastore.Operators
import pt.rafap.clilib.tools.performVarOperation

/**
 * Add two variables and store the result in a target variable.
 *
 * CommandInfo:
 * - description: Add two variables
 * - longDescription: Adds the values of two variables or values and stores the result in a target variable.
 * - usage: var add <var1> <var2> <resultVar>
 * - aliases: add, plus
 * - minArgs: 2
 * - maxArgs: 3
 */
object AddVarCmd : Command {
    override val info = CommandInfo(
        description = "Add two variables",
        longDescription = "Adds the values of two variables and stores result in a target variable.",
        usage = "var add <var1> <var2> <resultVar>",
        aliases = listOf("add", "plus"),
        minArgs = 2,
        maxArgs = 3
    )

    override fun run(args: List<String>): Boolean {
        return performVarOperation(args, this, Operators.ADD())
    }
}