package pt.clilib.registers

import pt.clilib.tools.LAST_CMD_KEY

internal object FunRegister {
    private val functions = mutableMapOf<String, String>()

    /**
     * Registers a function with a given name and value.
     *
     * @param name The name of the function.
     * @param value The value of the function.
     */
    fun register(name: String, value: String) {
        if (name.isBlank()) {
            throw IllegalArgumentException("Function name cannot be blank.")
        }
        if (name == LAST_CMD_KEY) {
            println("Warning: '${LAST_CMD_KEY}' is reserved and cannot be registered via CLI")
            return
        }
        functions[name] = value
    }

    /**
     * Retrieves the value of a function by its name.
     *
     * @param name The name of the function.
     * @return The value of the function, or null if not found.
     */
    fun get(name: String): Any? {
        if (name.isBlank()) {
            throw IllegalArgumentException("Function name cannot be blank.")
        }
        return functions[name]
    }

    /**
     * Checks if a function with the given name is registered.
     *
     * @param name The name of the function.
     * @return True if the function is registered, false otherwise.
     */
    fun isRegistered(name: String): Boolean {
        if (name.isBlank()) {
            throw IllegalArgumentException("Function name cannot be blank.")
        }
        return functions.containsKey(name)
    }

    /**
     * Unregisters a function by its name.
     *
     * @param name The name of the function to unregister.
     */
    fun unregister(name: String) {
        if (name.isBlank()) {
            throw IllegalArgumentException("Function name cannot be blank.")
        }
        if (name == LAST_CMD_KEY) {
            println("Warning: '${LAST_CMD_KEY}' cannot be removed from the register")
            return
        }
        if (functions.remove(name) != null) {
            println("Unregistered function: $name")
        } else {
            println("Function $name not found.")
        }
    }

    /**
     * Returns a map of all registered functions.
     *
     * @return A map containing all function names and their values.
     */
    fun all(): Map<String, String> {
        return functions.toMap()
    }

    fun modify(name: String, value: String) {
        if (name.isBlank()) {
            throw IllegalArgumentException("Function name cannot be blank.")
        }
        if (name == LAST_CMD_KEY) {
            println("Warning: '${LAST_CMD_KEY}' cannot be modified via CLI")
            return
        }
        if (functions.containsKey(name)) {
            functions[name] = value
        } else {
            println("Function $name not found. Use register to create it first.")
        }
    }
}