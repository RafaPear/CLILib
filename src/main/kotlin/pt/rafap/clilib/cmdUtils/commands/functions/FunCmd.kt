package pt.rafap.clilib.cmdUtils.commands.functions

import pt.rafap.clilib.cmdUtils.Command
import pt.rafap.clilib.cmdUtils.CommandInfo
import pt.rafap.clilib.datastore.Colors.RED
import pt.rafap.clilib.datastore.Colors.WHITE
import pt.rafap.clilib.registers.CmdRegister
import pt.rafap.clilib.registers.FunRegister
import pt.rafap.clilib.tools.cmdParser
import pt.rafap.clilib.tools.isValidIdentifier
import pt.rafap.clilib.tools.validateArgs

/**
 * Define and manage user-defined functions (macros) composed of CLI commands.
 *
 * CommandInfo:
 * - description: Create or manage functions
 * - longDescription: Define a named function (a sequence of CLI commands) that can be executed later; also supports listing and removing functions.
 * - usage: fun <name> {commands}  | fun -l | fun -d <name>
 * - aliases: fun, function
 * - minArgs: 0
 * - maxArgs: -1
 */

object FunCmd : Command {
    override val info = CommandInfo(
        description = "Create a function",
        longDescription = "Create a function with the given name and commands. The function can be called later.",
        usage = "fun <name> {commands}",
        aliases = listOf("fun", "function"),
        minArgs = 1,
        maxArgs = -1,
        commands = listOf(
            "-h", "--help"
        )
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

            if (!isValidIdentifier(args[0])) {
                println("${RED}Error: Invalid function name '${args[0]}'.${WHITE}")
                return false
            }

            val value = newArgs.trim()

            val functionCommand =
                object : Command {
                    override val info = CommandInfo(
                        description = "Function '${args[0]}'",
                        longDescription = "Function '${args[0]}' with commands: $value",
                        usage = "${args[0]} {commands}",
                        aliases = listOf(args[0]),
                        minArgs = 0,
                        maxArgs = -1,
                        commands = emptyList()
                    )

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
                    println("${RED}Error: Invalid number of arguments for delete command.${WHITE}")
                    return false
                }
                FunRegister.unregister(args[1])
            }
            "-l", "--list" -> {
                println("Registered functions: ${FunRegister.all()}")
            }
            "-h", "--help" -> {
                println("Usage: fun <name> {commands}")
                println("Commands:")
                println("  -d, --delete   Delete the function by name")
                println("  -l, --list     List registered functions")
                println("  -h, --help     Show this help message")
            }
            else -> {
                println("${RED}Error: Unknown command '${args[0]}'.${WHITE}")
                return false
            }
        }
        return true
    }
}