package pt.clilib.cmdUtils.commands.functions

import pt.clilib.cmdUtils.Command
import pt.clilib.tools.cmdParser
import pt.clilib.tools.eval
import pt.clilib.tools.replaceVars
import pt.clilib.tools.validateArgs

object IfCmd : Command {
    override val description = "Create an if statement"
    override val longDescription = "Create an if statement with the given condition. The commands will be executed if the condition is true."
    override val usage = "if <condition> [commands]"
    override val aliases = listOf("if")
    override val minArgs = 1
    override val maxArgs = -1
    override val commands = listOf(
        "-h", "--help"
    )

    override fun run(args: List<String>): Boolean {
        if (!validateArgs(args, this)) return false
        try {
            val condition = args[0].replaceVars(true)
            if (eval(condition)) {
                val newCmd = args.drop(1).joinToString(" ").removeSurrounding("{", "}")
                return cmdParser(newCmd)
            }
        } catch (e: Exception) {
            println("Error: ${e.message}")
            return false
        }
        return true
    }
}