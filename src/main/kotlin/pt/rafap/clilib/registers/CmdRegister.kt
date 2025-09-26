package pt.rafap.clilib.registers

import pt.rafap.clilib.cmdUtils.Command
import pt.rafap.clilib.datastore.Colors

/**
 * Global registry of CLI commands.
 * Allows registering, unregistering and looking up commands by alias.
 */
object CmdRegister {
    private val aliasMap = mutableMapOf<String, Command>()
    private val commands = linkedSetOf<Command>()

    /**
     * Registers a [command] and all of its aliases.
     *
     * @param command Command instance to register.
     */
    fun register(command: Command) {
        val existing = command.aliases.firstOrNull { aliasMap.containsKey(it.lowercase()) }
        if (existing != null) {
            println("${Colors.YELLOW}Warning: Command '$existing' is already registered. Skipping registration.${Colors.WHITE}")
            return
        }
        else
            println("${Colors.GREEN}Registering command: ${command.aliases[0]}${Colors.WHITE}")
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

    fun findAllSimilar(prefix: String): List<String> =
        if (prefix.isNotBlank())
            aliasMap.keys
                .filter {
                    it.startsWith(prefix.lowercase())
                }
                .sortedBy {
                    it.length
                }
        else
            emptyList()


}