package cmdUtils

object CmdRegister {
    private val commands = mutableListOf<Command>()

    fun register(command: Command) {
        commands.add(command)
    }

    fun unregister(command: Command) {
        commands.remove(command)
    }

    fun registerAll(commands: List<Command>) {
        commands.forEach {
            // Print all commands being registered and contents
            println("Registering command: ${it.aliases.joinToString(", ")} - ${it.description}")
            println("Long Description: ${it.longDescription}")
            println("Usage: ${it.usage}")
            println("Min Args: ${it.minArgs}, Max Args: ${it.maxArgs}")
            println("Requires File: ${it.requiresFile}, File Extension: ${it.fileExtension}")
            println("Commands: ${it.commands.joinToString(", ")}")
            println("--------------------------------------------------")
            register(it)
        }
    }

    fun unregisterAll(commands: List<Command>) {
        commands.forEach { unregister(it) }
    }

    fun all(): List<Command> = commands

    fun find(alias: String): Command? {
        return commands.find { alias.lowercase() in it.aliases.map(String::lowercase) }
    }
}
