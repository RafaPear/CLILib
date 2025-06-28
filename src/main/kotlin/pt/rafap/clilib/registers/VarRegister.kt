package pt.rafap.clilib.registers

import pt.rafap.clilib.tools.LAST_CMD_KEY

internal object VarRegister {
    private val functions = mutableMapOf<String, Any?>(LAST_CMD_KEY to null)
    /**
     * Registers a variable with a given name and value.
     *
     * @param name The name of the variable.
     * @param value The value of the variable.
     */
    fun register(name: String, value: Any) {
        if (name.isBlank()) {
            throw IllegalArgumentException("Variable name cannot be blank.")
        }
        if (name == LAST_CMD_KEY) {
            println("Warning: '${LAST_CMD_KEY}' is reserved and cannot be registered via CLI")
            return
        }
        functions[name] = value
    }

    /**
     * Retrieves the value of a variable by its name.
     *
     * @param name The name of the variable.
     * @return The value of the variable, or null if not found.
     */
    fun get(name: String): Any? {
        if (name.isBlank()) {
            throw IllegalArgumentException("Variable name cannot be blank.")
        }
        return functions[name]
    }

    /**
     * Checks if a variable with the given name is registered.
     *
     * @param name The name of the variable.
     * @return True if the variable is registered, false otherwise.
     */
    fun isRegistered(name: String): Boolean {
        if (name.isBlank()) {
            throw IllegalArgumentException("Variable name cannot be blank.")
        }
        return functions.containsKey(name)
    }

    /**
     * Unregisters a variable by its name.
     *
     * @param name The name of the variable to unregister.
     */
    fun unregister(name: String) {
        if (name.isBlank()) {
            throw IllegalArgumentException("Variable name cannot be blank.")
        }
        if (name == LAST_CMD_KEY) {
            println("Warning: '${LAST_CMD_KEY}' cannot be removed from the register")
            return
        }
        if (functions.remove(name) != null) {
            println("Unregistered variable: $name")
        } else {
            println("Variable $name not found.")
        }
    }

    /**
     * Returns a map of all registered variables.
     *
     * @return A map containing all variable names and their values.
     */
    fun all(): Map<String, Any?> {
        return functions.toMap()
    }

    fun modify(name: String, value: Any) {
        if (name.isBlank()) {
            throw IllegalArgumentException("Variable name cannot be blank.")
        }
        if (name == LAST_CMD_KEY) {
            println("Warning: '${LAST_CMD_KEY}' cannot be modified via CLI")
            return
        }
        if (functions.containsKey(name)) {
            functions[name] = value
        } else {
            println("Variable $name not found. Use register to create it first.")
        }
    }

    fun lastCmdDump(): Any? = functions[LAST_CMD_KEY]

    fun setLastCmdDump(value: Any?) {
        functions[LAST_CMD_KEY] = value
    }
}