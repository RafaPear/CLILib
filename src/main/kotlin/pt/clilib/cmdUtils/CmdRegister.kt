package pt.clilib.cmdUtils

internal object CmdRegister {
    private val commands = mutableListOf<Command>()

    fun register(command: Command) {
        commands.add(command)
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

