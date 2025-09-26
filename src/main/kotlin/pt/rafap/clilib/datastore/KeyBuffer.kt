package pt.rafap.clilib.datastore

import pt.rafap.clilib.datatypes.LinkedList
import pt.rafap.clilib.ext.RawConsoleInput
import pt.rafap.clilib.tools.isRunningInTerminal
import kotlin.concurrent.thread

/**
* KeyBuffer is a placeholder for a class that could be used to manage key inputs
*
* */
object KeyBuffer {
    private var buffer = LinkedList<Int>()
    val size
        get() = SIZE

    private var SIZE = 0

    /**
     * Initializes the KeyBuffer.
     */
    init {
        clear()
        thread {
            while (true) {
                //Thread.sleep(50) // Adjust the sleep time as needed
                if (!isRunningInTerminal()) {
                    break // Exit the thread if not running in terminal
                }
                try {
                    val input = RawConsoleInput.read(true)
                    append(input)
                } catch (e: Exception) {
                    break
                }
            }
        }
    }

    /**
     * Appends a key to the buffer.
     *
     * @param key The key to append to the buffer.
     */
    fun append(key: Int) {
        buffer.add(key)
        SIZE++
    }

    /**
     * Checks if the buffer contains a specific key.
     *
     * @param key The key to check for in the buffer.
     * @return True if the key is in the buffer, false otherwise.
     */
    fun contains(key: Int): Boolean = buffer.contains(key)


    fun contains(keys: String): Boolean {
        if (keys.isEmpty()) return false
        // Checks if the buffer contains all keys in the string in successive order
        val keyList = keys.map { it.code }
        var currentNode = buffer.head
        for (key in keyList) {
            var found = false
            while (currentNode != null) {
                if (currentNode.value == key) {
                    found = true
                    break
                }
                currentNode = currentNode.next
            }
            if (!found) return false // If any key is not found, return false
        }
        return true
    }

    /**
     * Clears the buffer.
     */
    fun clear() = buffer.clear()

    fun consume(): Int? {
        val value = buffer.head?.value
        if (value != null) {
            buffer.remove(value)
            return value
        }
        return null
    }

    fun consumeAll(): List<Int> {
        val values = mutableListOf<Int>()
        while (!buffer.isEmpty()) {
            val value = consume()
            if (value != null) {
                values.add(value)
            }
        }
        return values
    }

    /**
     * Checks if the buffer is empty.
     *
     * @return True if the buffer is empty, false otherwise.
     */
    fun isEmpty(): Boolean = buffer.isEmpty()
}