package pt.rafap.clilib.datastore

object InputHistory {
    private val history = mutableListOf<String>()
    private var currentIndex = -1

    /**
     * Adds a command to the history.
     *
     * @param command The command to add.
     */
    fun add(command: String) {
        if (command.isNotBlank()) {
            history.add(command)
            currentIndex = history.size // Reset index to the end
        }
    }

    /**
     * Resets the current index to the end of the history.
     */
    fun resetIndex() {
        currentIndex = history.size
    }

    /**
     * Retrieves the previous command in history.
     *
     * @return The previous command or null if no previous command exists.
     */
    fun previous(): String? {
        if (currentIndex > 0) {
            currentIndex--
            return history[currentIndex]
        }
        return null
    }

    /**
     * Retrieves the next command in history.
     *
     * @return The next command or null if no next command exists.
     */
    fun next(): String? {
        if (currentIndex < history.size - 1) {
            currentIndex++
            return history[currentIndex]
        }
        // If there are no more commands, reset the index to the end
        if (currentIndex == history.size - 1)
            currentIndex = history.size

        return null
    }

    /**
     * Retrieves the entire history.
     *
     * @return A list of all commands in history.
     */
    fun getHistory(): List<String> {
        return history.toList()
    }

    /**
     * Clears the input history.
     */
    fun clear() {
        history.clear()
        currentIndex = -1
    }
}