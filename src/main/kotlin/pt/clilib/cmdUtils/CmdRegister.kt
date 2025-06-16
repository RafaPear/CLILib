package pt.clilib.cmdUtils

import pt.clilib.tools.RESET
import pt.clilib.tools.YELLOW

object CmdRegister {
    private val aliasMap = mutableMapOf<String, Command>()
    private val commands = linkedSetOf<Command>()

    fun register(command: Command) {
        val existing = command.aliases.firstOrNull { aliasMap.containsKey(it.lowercase()) }
        if (existing != null) {
            println("${YELLOW}Warning: Command '$existing' is already registered. Skipping registration.$RESET")
            return
        }
        command.aliases.forEach { aliasMap[it.lowercase()] = command }
        commands.add(command)
    }

    fun unregister(command: Command) {
        aliasMap.entries.removeIf { it.value == command }
        commands.remove(command)
    }

    fun registerAll(commands: List<Command>) {
        commands.forEach { register(it) }
    }

    fun unregisterAll(commands: List<Command>) {
        commands.forEach { unregister(it) }
    }

    fun all(): List<Command> = commands.toList()

    fun find(alias: String): Command? = aliasMap[alias.lowercase()]

    fun findSimilar(prefix: String): String? =
        aliasMap.keys.firstOrNull { it.startsWith(prefix.lowercase()) }
}

