package pt.rafap.clilib.cmdUtils.commands.varOp

import pt.rafap.clilib.cmdUtils.Command
import pt.rafap.clilib.cmdUtils.CommandInfo
import pt.rafap.clilib.tools.ArithmeticOperator
import pt.rafap.clilib.tools.performVarOperation

object MultVarCmd : Command {
    override val info = CommandInfo(
        description = "Multiply two variables",
        longDescription = "Multiplies the value of one variable by another and creates a new variable with the result.",
        usage = "mult <var1> <var2> <resultVar>",
        aliases = listOf("mult", "times"),
        minArgs = 2,
        maxArgs = 3
    )

    override fun run(args: List<String>): Boolean {
        return performVarOperation(args, this, ArithmeticOperator.MULTIPLY)
    }
}