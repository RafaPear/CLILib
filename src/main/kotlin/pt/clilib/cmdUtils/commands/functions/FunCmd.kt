package pt.clilib.cmdUtils.commands.functions

import pt.clilib.FunRegister
import pt.clilib.LAST_CMD_KEY
import pt.clilib.cmdUtils.CmdRegister
import pt.clilib.cmdUtils.Command
import pt.clilib.tools.RED
import pt.clilib.tools.RESET
import pt.clilib.tools.YELLOW
import pt.clilib.tools.cmdParser
import pt.clilib.tools.validateArgs

object FunCmd : Command {
    override val description = "Create a function"
    override val longDescription =
        "Create a function with the given name and commands. The function can be called later."
    override val usage = "fun <name> {commands}"
    override val aliases = listOf("fun", "function")
    override val minArgs = 1
    override val maxArgs = -1
    override val commands = listOf(
        "-h", "--help"
    )

    // Argument commands:
    // Name is mandatory, value is optional.
    // -d, --delete: Delete the function.
    // -l, --list: List all functions.
    // -h, --help: Show help for the command.

    override fun run(args: List<String>): Boolean {
        if (!validateArgs(args, this)) return false
        // Check if args contains any command stated in commands list. Else proceed with the default behavior.
        var isArgCmd = false
        if (args[0].startsWith("-")) {
            isArgCmd = true
        }

        if (isArgCmd) {
            parseCommands(args)
        } else {
            // Default behavior: register the function with the given name and value.
            val newArgs = args.drop(1).joinToString(" ")

            if (args[0][0].isDigit()){
                println("${RED}Error: Function name '${args[0]}' must start with a letter.${RESET}")
                return false
            }
            args[0].map {
                if(!(it.isLetter() || it.isDigit() || it == '_')) {
                    println("${RED}Error: Function name '${args[0]}' must start with a letter and can only contain letters, digits, and underscores.${RESET}")
                    return false
                }
            }

            val value = newArgs.trim()

            val functionCommand =
                object : Command{
                    override val description = "Function '${args[0]}'"
                    override val longDescription = "Function '${args[0]}' with commands: $value"
                    override val usage = "fun ${args[0]} {commands}"
                    override val aliases = listOf(args[0])
                    override val minArgs = 0
                    override val maxArgs = -1
                    override val commands = emptyList<String>()

                    private val body = value.removeSurrounding("{", "}")

                    override fun run(args: List<String>): Boolean {
                        return cmdParser(body, args, supress = false)
                    }
                }

            FunRegister.register(args[0], value)
            CmdRegister.register(functionCommand)
        }
        return true
    }

    private fun parseCommands(args: List<String> ): Boolean {
        when (args[0]) {
            "-d", "--delete" -> {
                if (args.size != 2) {
                    println("${RED}Error: Invalid number of arguments for delete command.${RESET}")
                    return false
                }
                FunRegister.unregister(args[1])
            }
            "-l", "--list" -> {
                println("Registered functions: ${FunRegister.all()}")
            }
            "-h", "--help" -> {
                println("Usage: var <name> [args]")
                println("Commands:")
                VarCmd.commands.forEach { println("  $it") }
            }
            else -> {
                println("${RED}Error: Unknown command '${args[0]}'.${RESET}")
                return false
            }
        }
        return true
    }
}