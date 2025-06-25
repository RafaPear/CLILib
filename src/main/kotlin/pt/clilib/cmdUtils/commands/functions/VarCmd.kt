package pt.clilib.cmdUtils.commands.functions

import pt.clilib.registers.VarRegister
import pt.clilib.cmdUtils.Command
import pt.clilib.cmdUtils.CommandInfo
import pt.clilib.tools.*
import pt.clilib.datastore.Colors.RED
import pt.clilib.datastore.Colors.WHITE
import pt.clilib.datastore.Colors.YELLOW
import pt.clilib.tools.isValidIdentifier


object VarCmd : Command {
    override val info = CommandInfo(
        description = "Create or modify a variable",
        longDescription = "Create or modify a variable with the given name and value.",
        usage = "var <name> [args]",
        aliases = listOf("var"),
        minArgs = 1,
        maxArgs = -1,
        commands = listOf(
            "-d", "--delete",
            "-l", "--list",
            "-h", "--help"
        )
    )


    // Argument commands:
    // Name is mandatory, value is optional.
    // -d, --delete: Delete the variable.
    // -l, --list: List all variables.
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
            // Default behavior: register the variable with the given name and value.
            val newArgs = args.drop(1).joinToString(" ")

            if (!isValidIdentifier(args[0])) {
                println("${RED}Error: Invalid variable name '${args[0]}'.${WHITE}")
                return false
            }

            if(cmdParser(newArgs.lowercase(), supress = true)) {
                if (assignLastCmdDump(args[0])) return true
            }

            // Previous failed because there is no command to run. Assign the value directly. Preserve the type smartly as an Int or its derivatives (depending on the size and structure), String os Char.
            val value = newArgs.trim()

            if (!assignValue(args[0], value)) {
                if (!assignLastCmdDump(args[0])) {
                    println("${RED}Error: Unable to assign value to variable '${args[0]}'. Please provide a valid value.${WHITE}")
                    return false
                }
            }
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
                if (args[1] == LAST_CMD_KEY) {
                    println("${YELLOW}Warning: '$LAST_CMD_KEY' cannot be removed.${WHITE}")
                    return false
                }
                VarRegister.unregister(args[1])
            }
            "-l", "--list" -> {
                println("Registered variables: ${VarRegister.all()}")
            }
            "-h", "--help" -> {
                println("Usage: var <name> [args]")
                println("Commands:")
                commands.forEach { println("  $it") }
            }
            else -> {
                println("${RED}Error: Unknown command '${args[0]}'.${WHITE}")
                return false
            }
        }
        return true
    }

    private fun assignValue(name: String, value: String): Boolean {
        if (name == LAST_CMD_KEY) {
            println("${YELLOW}Warning: '$LAST_CMD_KEY' cannot be modified via CLI${WHITE}")
            return false
        }
        val newValue = when {
            value.isEmpty() -> null
            value.all { it.isDigit() } -> value.toIntOrNull() ?: value.toLongOrNull() ?: value.toDoubleOrNull() ?: value.toFloatOrNull()
            value.length == 1 -> value.first() // Char
            else -> value // String
        }

        if (newValue == null) return false

        if (VarRegister.isRegistered(name)) {
            VarRegister.modify(name, newValue)
        } else {
            VarRegister.register(name, newValue)
        }
        return true
    }

    private fun assignLastCmdDump(name: String) : Boolean {
        val dump = VarRegister.lastCmdDump() ?: return false

        if (name == LAST_CMD_KEY) {
            println("${YELLOW}Warning: '$LAST_CMD_KEY' cannot be modified via CLI${WHITE}")
            return false
        }

        if (VarRegister.isRegistered(name)) {
            VarRegister.modify(name, dump)
        } else {
            VarRegister.register(name, dump)
        }
        return true
    }
}