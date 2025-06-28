package pt.rafap.clilib.cmdUtils.commands.varOp

import pt.rafap.clilib.cmdUtils.Command
import pt.rafap.clilib.cmdUtils.CommandInfo
import pt.rafap.clilib.tools.ArithmeticOperator
import pt.rafap.clilib.tools.performVarOperation

// Adds two variables together, or creates a new variable with the result.
object AddVarCmd : Command {
    override val info = CommandInfo(
        description = "Add two variables together",
        longDescription = "Adds the values of two variables and creates a new variable with the result.",
        usage = "add <var1> <var2> <resultVar>",
        aliases = listOf("add", "plus"),
        minArgs = 2,
        maxArgs = 3
    )

    override fun run(args: List<String>): Boolean {
        return performVarOperation(args, this, ArithmeticOperator.ADD)
    }
}