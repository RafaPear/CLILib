package pt.rafap.clilib.cmdUtils.commands.functions

import pt.rafap.clilib.cmdUtils.Command
import pt.rafap.clilib.cmdUtils.CommandInfo
import pt.rafap.clilib.datastore.Colors.RED
import pt.rafap.clilib.datastore.Colors.WHITE
import pt.rafap.clilib.tools.cmdParser
import pt.rafap.clilib.tools.eval
import pt.rafap.clilib.tools.replaceVars
import pt.rafap.clilib.tools.validateArgs

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
                    println("${RED}Error executing loop${{WHITE}}")
                    return false
                }
            }
        }
        catch (e: Exception){
            println("${RED}Error: ${e.message}${{WHITE}}")
            return false
        }
        return true
    }

}