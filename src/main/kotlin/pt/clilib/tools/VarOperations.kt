package pt.clilib.tools

import pt.clilib.registers.VarRegister
import pt.clilib.cmdUtils.Command
import pt.clilib.datastore.Colors.RED
import pt.clilib.datastore.Colors.WHITE

/**
 * Supported arithmetic operations for variable commands.
 */
internal enum class ArithmeticOperator { ADD, SUBTRACT, MULTIPLY, DIVIDE }

/**
 * Performs an arithmetic operation on two registered variables.
 * The result is stored in the last command dump and optionally
 * in a new variable if [args] contains a third parameter.
 */
internal fun performVarOperation(
    args: List<String>,
    command: Command,
    operator: ArithmeticOperator
): Boolean {
    if (!validateArgs(args, command)) return false

    val var1 = VarRegister.get(args[0]) as? Number
    val var2 = VarRegister.get(args[1]) as? Number

    if (var1 == null || var2 == null) {
        println("${RED}Error: One or both variables do not exist or are not numeric.${WHITE}")
        return false
    }

    val a = var1.toDouble()
    val b = var2.toDouble()

    val result = when (operator) {
        ArithmeticOperator.ADD -> a + b
        ArithmeticOperator.SUBTRACT -> a - b
        ArithmeticOperator.MULTIPLY -> a * b
        ArithmeticOperator.DIVIDE -> {
            if (b == 0.0) {
                println("${RED}Error: Division by zero is not allowed.${WHITE}")
                return false
            }
            a / b
        }
    }

    VarRegister.setLastCmdDump(result)
    if (args.size == 3 && args[2] != LAST_CMD_KEY) {
        VarRegister.register(args[2], result)
    }

    return true
}
