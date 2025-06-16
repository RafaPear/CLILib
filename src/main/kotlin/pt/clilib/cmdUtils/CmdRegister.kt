package pt.clilib.cmdUtils

import pt.clilib.tools.RESET
import pt.clilib.tools.YELLOW

object CmdRegister {
    private val commands = mutableListOf<Command>()

    fun register(command: Command) {
        val cmdAliases = commands.map { it.aliases }.flatten()
        if(!command.aliases.any { alias -> alias in cmdAliases })
            commands.add(command)
        else
            println("${YELLOW}Warning: Command '${command.aliases.first()}' is already registered. Skipping registration.$RESET")
    }

    fun unregister(command: Command) {
        commands.remove(command)
    }

    fun registerAll(commands: List<Command>) {
        commands.forEach { register(it) }
    }

    fun unregisterAll(commands: List<Command>) {
        commands.forEach { unregister(it) }
    }

    fun all(): List<Command> = commands

    fun find(alias: String): Command? {
        return commands.find { alias.lowercase() in it.aliases.map(String::lowercase) }
    }
}

