package pt.clilib.cmdUtils.commands.varOp

import pt.clilib.cmdUtils.Command
import pt.clilib.tools.*
import pt.clilib.tools.ArithmeticOperator
import pt.clilib.tools.performVarOperation

object SubVarCmd : Command {
    override val description = "Subtract two variables"
    override val longDescription = "Subtracts the value of one variable from another and creates a new variable with the result."
    override val usage = "sub <var1> <var2> <resultVar>"
    override val aliases = listOf("sub", "minus")
    override val minArgs = 2
    override val maxArgs = 3

    override fun run(args: List<String>): Boolean {
        return performVarOperation(args, this, ArithmeticOperator.SUBTRACT)
    }
}