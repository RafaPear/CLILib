package cmdUtils.commands.varOp

import cmdUtils.Command
import tools.*

object SubVarCmd : Command {
    override val description = "Subtract two variables"
    override val longDescription = "Subtracts the value of one variable from another and creates a new variable with the result."
    override val usage = "sub <var1> <var2> <resultVar>"
    override val aliases = listOf("sub", "minus")
    override val minArgs = 2
    override val maxArgs = 3

    override fun run(args: List<String>): Boolean {
        if (!validateArgs(args, this)) return false
        val var1 = VarRegister.get(args[0])
        val var2 = VarRegister.get(args[1])
        if (var1 == null || var2 == null) {
            println("${RED}Error: One or both variables do not exist.${RESET}")
            return false
        } else if (var1::class != var2::class) {
            println("${RED}Error: Variables must be of the same type to subtract them.${RESET}")
            return false
        }
        val result = when (var1) {
            is Int -> var1 - var2 as Int
            is Double -> var1 - var2 as Double
            is Float -> var1 - var2 as Float
            is Long -> var1 - var2 as Long
            else -> {
                println("${RED}Error: Unsupported variable type for subtraction.${RESET}")
                return false
            }
        }
        lastCmdDump = result
        if (args.size < 3) {
            println("${GREEN}Result: $result${RESET}")
            return true
        } else {
            VarRegister.register(args[2], result)
            println("${GREEN}Subtracted ${args[1]} from ${args[0]} to create ${args[2]} with value $result.${RESET}")
        }
        return true
    }
}