package pt.clilib.cmdUtils.commands.functions

import pt.clilib.cmdUtils.Command
import pt.clilib.cmdUtils.CommandInfo
import pt.clilib.tools.cmdParser
import pt.clilib.tools.eval
import pt.clilib.tools.replaceVars
import pt.clilib.tools.validateArgs

object IfCmd : Command {
    override val info = CommandInfo(
        description = "Create an if statement",
        longDescription = "Create an if statement with the given condition. The commands will be executed if the condition is true.",
        usage = "if <condition> [commands]",
        aliases = listOf("if"),
        minArgs = 1,
        maxArgs = -1,
        commands = listOf(
            "-h", "--help"
        )
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