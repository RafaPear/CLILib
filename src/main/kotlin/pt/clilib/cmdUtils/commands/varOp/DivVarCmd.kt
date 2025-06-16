package pt.clilib.cmdUtils.commands.varOp

import pt.clilib.cmdUtils.Command
import pt.clilib.tools.*
import pt.clilib.tools.ArithmeticOperator
import pt.clilib.tools.performVarOperation

object DivVarCmd : Command {
    override val description = "Divide two variables"
    override val longDescription = "Divides the value of one variable by another and creates a new variable with the result."
    override val usage = "div <var1> <var2> <resultVar>"
    override val aliases = listOf("div", "divide")
    override val minArgs = 2
    override val maxArgs = 3

    override fun run(args: List<String>): Boolean {
        return performVarOperation(args, this, ArithmeticOperator.DIVIDE)
    }
}