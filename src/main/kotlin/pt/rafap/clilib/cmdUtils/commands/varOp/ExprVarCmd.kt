package pt.rafap.clilib.cmdUtils.commands.varOp

import pt.rafap.clilib.cmdUtils.Command
import pt.rafap.clilib.cmdUtils.CommandInfo
import pt.rafap.clilib.datastore.Colors.RED
import pt.rafap.clilib.datastore.Colors.WHITE
import pt.rafap.clilib.registers.VarRegister
import pt.rafap.clilib.tools.ExprParser
import pt.rafap.clilib.tools.validateArgs

/**
 * Evaluate a mathematical expression and store the result.
 *
 * CommandInfo:
 * - description: Evaluate expression
 * - longDescription: Parses and evaluates an arithmetic expression and stores the numeric result in the last command dump variable.
 * - usage: var expr <expression>
 * - aliases: expr, evaluate
 * - minArgs: 1
 * - maxArgs: -1
 */
object ExprVarCmd : Command {
    override val info = CommandInfo(
        description = "Evaluate expression",
        longDescription = "Parses and evaluates a mathematical expression and stores the result for later use.",
        usage = "var expr <expression>",
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
            println("${RED}Error evaluating expression: ${e.message}${WHITE}")
            return false
        }
        return true
    }
}