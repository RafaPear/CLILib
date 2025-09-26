package pt.rafap.clilib.cmdUtils.commands.varOp

import pt.rafap.clilib.cmdUtils.Command
import pt.rafap.clilib.cmdUtils.CommandInfo
import pt.rafap.clilib.datastore.Operators
import pt.rafap.clilib.tools.performVarOperation

object SubVarCmd : Command {
    override val info = CommandInfo(
        description = "Subtract two variables",
        longDescription = "Subtracts the value of one variable from another and creates a new variable with the result.",
        usage = "sub <var1> <var2> <resultVar>",
        aliases = listOf("sub", "minus"),
        minArgs = 2,
        maxArgs = 3
    )

    override fun run(args: List<String>): Boolean {
        return performVarOperation(args, this, Operators.SUB())
    }
}