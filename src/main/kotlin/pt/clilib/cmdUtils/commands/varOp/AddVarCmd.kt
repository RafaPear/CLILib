package pt.clilib.cmdUtils.commands.varOp

import pt.clilib.cmdUtils.Command
import pt.clilib.tools.*
import pt.clilib.tools.ArithmeticOperator
import pt.clilib.tools.performVarOperation

// Adds two variables together, or creates a new variable with the result.
object AddVarCmd : Command{
    override val description = "Add two variables together"
    override val longDescription = "Adds the values of two variables and creates a new variable with the result."
    override val usage = "add <var1> <var2> <resultVar>"
    override val aliases = listOf("add", "plus")
    override val minArgs = 2
    override val maxArgs = 3

    override fun run(args: List<String>): Boolean {
        return performVarOperation(args, this, ArithmeticOperator.ADD)
    }
}