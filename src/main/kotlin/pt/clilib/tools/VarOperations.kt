package pt.clilib.tools

import pt.clilib.LAST_CMD_KEY
import pt.clilib.VarRegister
import pt.clilib.cmdUtils.Command

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

    val var1 = VarRegister.get(args[0])
    val var2 = VarRegister.get(args[1])
    if (var1 == null || var2 == null) {
        println("${RED}Error: One or both variables do not exist.${RESET}")
        return false
    }
    if (var1::class != var2::class || var1 !is Number || var2 !is Number) {
        println("${RED}Error: Variables must be numbers of the same type.${RESET}")
        return false
    }

    val result = when (var1) {
        is Int -> when (operator) {
            ArithmeticOperator.ADD -> var1 + var2 as Int
            ArithmeticOperator.SUBTRACT -> var1 - var2 as Int
            ArithmeticOperator.MULTIPLY -> var1 * var2 as Int
            ArithmeticOperator.DIVIDE -> {
                if (var2 == 0) {
                    println("${RED}Error: Division by zero is not allowed.${RESET}")
                    return false
                }
                var1 / var2 as Int
            }
        }
        is Double -> when (operator) {
            ArithmeticOperator.ADD -> var1 + var2 as Double
            ArithmeticOperator.SUBTRACT -> var1 - var2 as Double
            ArithmeticOperator.MULTIPLY -> var1 * var2 as Double
            ArithmeticOperator.DIVIDE -> {
                if (var2 == 0.0) {
                    println("${RED}Error: Division by zero is not allowed.${RESET}")
                    return false
                }
                var1 / var2 as Double
            }
        }
        is Float -> when (operator) {
            ArithmeticOperator.ADD -> var1 + var2 as Float
            ArithmeticOperator.SUBTRACT -> var1 - var2 as Float
            ArithmeticOperator.MULTIPLY -> var1 * var2 as Float
            ArithmeticOperator.DIVIDE -> {
                if (var2 == 0f) {
                    println("${RED}Error: Division by zero is not allowed.${RESET}")
                    return false
                }
                var1 / var2 as Float
            }
        }
        is Long -> when (operator) {
            ArithmeticOperator.ADD -> var1 + var2 as Long
            ArithmeticOperator.SUBTRACT -> var1 - var2 as Long
            ArithmeticOperator.MULTIPLY -> var1 * var2 as Long
            ArithmeticOperator.DIVIDE -> {
                if (var2 == 0L) {
                    println("${RED}Error: Division by zero is not allowed.${RESET}")
                    return false
                }
                var1 / var2 as Long
            }
        }
        else -> {
            println("${RED}Error: Unsupported variable type for operation.${RESET}")
            return false
        }
    }

    VarRegister.setLastCmdDump(result)
    if (args.size == 3) {
        if (args[2] == LAST_CMD_KEY) {
            VarRegister.setLastCmdDump(result)
        } else {
            VarRegister.register(args[2], result)
        }
    }
    return true
}
