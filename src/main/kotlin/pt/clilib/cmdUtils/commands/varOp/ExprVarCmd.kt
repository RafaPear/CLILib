package pt.clilib.cmdUtils.commands.varOp

import pt.clilib.VarRegister
import pt.clilib.cmdUtils.Command
import pt.clilib.cmdUtils.CommandInfo
import pt.clilib.tools.*

object ExprVarCmd : Command {
    override val info = CommandInfo(
        description = "Evaluate an expression",
        longDescription = "Evaluates a mathematical expression.",
        usage = "expr <expression>",
        aliases = listOf("expr", "evaluate"),
        minArgs = 1,
        maxArgs = -1
    )

    override fun run(args: List<String>): Boolean {
        if (!validateArgs(args, this)) return false
        val expression = args.joinToString("")

        try {
            val result: Double = ExprParser().parse(expression)
            if (!result.isNaN()) {
                VarRegister.setLastCmdDump(result.toInt())
            } else {
                VarRegister.setLastCmdDump(result)
            }
        } catch (e: Exception) {
            println("${RED}Error evaluating expression: ${e.message}${RESET}")
            return false
        }
        return true
    }
}