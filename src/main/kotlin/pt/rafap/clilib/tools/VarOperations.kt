package pt.rafap.clilib.tools

import pt.rafap.clilib.cmdUtils.Command
import pt.rafap.clilib.datastore.Colors.RED
import pt.rafap.clilib.datastore.Colors.WHITE
import pt.rafap.clilib.datastore.Colors.YELLOW
import pt.rafap.clilib.datatypes.Operator
import pt.rafap.clilib.registers.VarRegister

/**
 * Performs an arithmetic operation on two registered variables.
 * The result is stored in the last command dump and optionally
 * in a new variable if [args] contains a third parameter.
 */
fun performVarOperation(
    args: List<String>,
    command: Command,
    operator: Operator<Any>
): Boolean {
    if (!validateArgs(args, command)) return false

    val var1 = VarRegister.get(args[0])
    val var2 = VarRegister.get(args[1])

    if (var1 == null || var2 == null) {
        println("${RED}Error: One or both variables are not registered.${WHITE}")
        return false
    }

    val result = try {
        operator.invoke(var1, var2)
    } catch (e: Exception) {
        println("${RED}Error performing operation: ${e.message}${WHITE}")
        return false
    }

    // Check if the result is null, which can happen if the
    // operation is not valid for the types of var1 and var2.
    // Can also happen if the operation is not defined for the types.
    if (result == null) {
        println("${RED}Error: Operation resulted in null. Check the types of the variables.${WHITE}\n" +
                "${YELLOW}Hint: Ensure that the operation ${operator::class.simpleName} is valid for the types '${var1::class.simpleName}' and '${var2::class.simpleName}'.${WHITE}"
        )
        return false
    }

    VarRegister.setLastCmdDump(result)
    if (args.size == 3 && args[2] != LAST_CMD_KEY) {
        VarRegister.register(args[2], result)
    }

    return true
}
