package pt.clilib.cmdUtils.commands.varOp

import pt.clilib.VarRegister
import pt.clilib.LAST_CMD_KEY
import pt.clilib.cmdUtils.Command
import pt.clilib.tools.*

// Adds two variables together, or creates a new variable with the result.
object AddVarCmd : Command{
    override val description = "Add two variables together"
    override val longDescription = "Adds the values of two variables and creates a new variable with the result."
    override val usage = "add <var1> <var2> <resultVar>"
    override val aliases = listOf("add", "plus")
    override val minArgs = 2
    override val maxArgs = 3

    override fun run(args: List<String>): Boolean {
        if (!validateArgs(args, this)) return false
        val var1 = VarRegister.get(args[0])
        val var2 = VarRegister.get(args[1])
        if (var1 == null || var2 == null) {
            println("${RED}Error: One or both variables do not exist.${RESET}")
            return false
        }
        else if (var1::class != var2::class) {
            println("${RED}Error: Variables must be of the same type to add them.${RESET}")
            return false
        }
        val result = when (var1) {
            is Int -> var1 + var2 as Int
            is Double -> var1 + var2 as Double
            is Float -> var1 + var2 as Float
            is Long -> var1 + var2 as Long
            else -> {
                println("${RED}Error: Unsupported variable type for addition.${RESET}")
                return false
            }
        }
        VarRegister.setLastCmdDump(result)
        if (args.size < 3) {
            return true
        }
        else {
            if (args[2] == LAST_CMD_KEY) {
                VarRegister.setLastCmdDump(result)
            } else {
                VarRegister.register(args[2], result)
            }
        }
        return true
    }
}