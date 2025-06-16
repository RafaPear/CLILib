package pt.clilib.cmdUtils.commands.functions

import pt.clilib.cmdUtils.Command
import pt.clilib.cmdUtils.CommandInfo
import pt.clilib.tools.RED
import pt.clilib.tools.RESET
import pt.clilib.tools.cmdParser
import pt.clilib.tools.eval
import pt.clilib.tools.replaceVars
import pt.clilib.tools.validateArgs

object WhileCmd : Command {
    override val info = CommandInfo(
        description = "Create a while loop",
        longDescription = "Create a while loop with the given condition. The loop will continue until the condition is false.",
        usage = "while <condition> [commands]",
        aliases = listOf("while"),
        minArgs = 1,
        maxArgs = -1,
        commands = listOf(
            "-b", "--break",
            "-h", "--help"
        )
    )

    override fun run(args: List<String>): Boolean {
        if (!validateArgs(args, this)) return false
        try {
            val newCmd = args.drop(1)
                .joinToString(" ")
                .removeSurrounding("{", "}")

            while (eval(args[0].replaceVars(true))) {
                if(!cmdParser(newCmd)) {
                    println("${RED}Error executing loop${RESET}")
                    return false
                }
            }
        }
        catch (e: Exception){
            println("${RED}Error: ${e.message}${RESET}")
            return false
        }
        return true
    }

}