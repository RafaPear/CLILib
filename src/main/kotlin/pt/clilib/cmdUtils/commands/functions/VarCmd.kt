package pt.clilib.cmdUtils.commands.functions

import pt.clilib.VarRegister
import pt.clilib.cmdUtils.Command
import pt.clilib.tools.*

internal object VarCmd : Command {
    override val description = "Create or modify a variable"
    override val longDescription = "Create or modify a variable with the given name and value. "
    override val usage = "var <name> [args]"
    override val aliases = listOf("var")
    override val minArgs = 1
    override val maxArgs = -1
    override val commands = listOf(
        "-d", "--delete",
        "-l", "--list",
        "-s", "--set",
        "-h", "--help"
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
        for (arg in args) {
            if (arg.contains("-")) {
                isArgCmd = true
                break
            }
        }

        if (isArgCmd) {
            when (args[0]) {
                "-d", "--delete" -> {
                    if (args.size != 2) {
                        println("${RED}Error: Invalid number of arguments for delete command.${RESET}")
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
                    println("${RED}Error: Unknown command '${args[0]}'.${RESET}")
                    return false
                }
            }
        } else {
            // Default behavior: register the variable with the given name and value.
            val newArgs = args.drop(1).joinToString(" ")
            // Previous failed because there is no comand to run. Assign the value directly. Preserve the type smartly as an Int or its derivatives (depending on the size and structure), String os Char.
            val value = newArgs.trim()
            val registeredValue = when {
                value.isEmpty() -> null
                value.all { it.isDigit() } -> value.toIntOrNull() ?: value.toLongOrNull() ?: value.toDoubleOrNull() ?: value.toFloatOrNull()
                value.length == 1 -> value.first() // Char
                else -> value // String
            }
            if (VarRegister.isRegistered(args[0])) {
                if (registeredValue != null) {
                    VarRegister.modify(args[0], registeredValue)
                    println("${GREEN}Variable '${args[0]}' modified with value: $registeredValue${RESET}")
                } else {
                    if (lastCmdDump != null) {
                        VarRegister.modify(args[0], lastCmdDump!!)
                        println("${GREEN}Variable '${args[0]}' modified with value: $lastCmdDump${RESET}")
                    } else {
                        println("${RED}Error: Unable to determine value for variable '${args[0]}'. Please provide a valid value.${RESET}")
                        return false
                    }
                }
            }
            else {
                if (registeredValue != null) {
                    VarRegister.register(args[0], registeredValue)
                    println("${GREEN}Variable '${args[0]}' registered with value: $registeredValue${RESET}")
                } else {
                    if (lastCmdDump != null) {
                        VarRegister.register(args[0], lastCmdDump!!)
                        println("${GREEN}Variable '${args[0]}' registered with value: $lastCmdDump${RESET}")
                    } else {
                        println("${RED}Error: Unable to determine value for variable '${args[0]}'. Please provide a valid value.${RESET}")
                        return false
                    }
                }
            }
        }
        return true
    }
}