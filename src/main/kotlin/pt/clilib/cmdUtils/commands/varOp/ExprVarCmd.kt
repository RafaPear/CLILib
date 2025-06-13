package pt.clilib.cmdUtils.commands.varOp

import pt.clilib.VarRegister
import pt.clilib.cmdUtils.Command
import pt.clilib.tools.*

object ExprVarCmd : Command {
    override val description = "Evaluate an expression"
    override val longDescription = "Evaluates a mathematical expression."
    override val usage = "expr <expression>"
    override val aliases = listOf("expr", "evaluate")
    override val minArgs = 1
    override val maxArgs = -1

    override fun run(args: List<String>): Boolean {
        if (!validateArgs(args, this)) return false
        val expression = args.joinToString("")

        try {
            val result = ExprParser().parse(expression)
            VarRegister.setLastCmdDump(result)
            println("${GREEN}Result: $result${RESET}")
        } catch (e: Exception) {
            println("${RED}Error evaluating expression: ${e.message}${RESET}")
            return false
        }
        return true
    }
}