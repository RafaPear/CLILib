package pt.clilib.datastore

/**
 * Buffer is a simple text buffer class that allows for basic text manipulation.
 * It supports operations like adding text, removing text, and navigating through the buffer.
 * */
class Buffer {
    /**
     * The internal buffer that stores the text.
     */
    private data class BufferState(
        var buffer: String = "",
        var cursor: Int = -1,
        var size: Int = 0
    )
    private val mainBuffer = BufferState()
    private val tempBuffer = BufferState()

    private var usingTempBuffer = false

    private var buffer = mainBuffer.buffer
        get() = if (usingTempBuffer) tempBuffer.buffer else mainBuffer.buffer
        set(value) {
            if (usingTempBuffer) {
                tempBuffer.buffer = value
            } else {
                mainBuffer.buffer = value
            }
            field = value
        }

    private var size = mainBuffer.size
        get() = if (usingTempBuffer) tempBuffer.size else mainBuffer.size
        set(value) {
            if (usingTempBuffer) {
                tempBuffer.size = value
            } else {
                mainBuffer.size = value
            }
            field = value
        }

    private var cursor = mainBuffer.cursor
        get() = if (usingTempBuffer) tempBuffer.cursor else mainBuffer.cursor
        set(value) {
            if (usingTempBuffer) {
                tempBuffer.cursor = value
            } else {
                mainBuffer.cursor = value
            }
            field = value
        }

    fun isUsingTempBuffer(): Boolean {
        return usingTempBuffer
    }

    fun useTempBuffer() {
        usingTempBuffer = true
    }

    fun useMainBuffer() {
        usingTempBuffer = false
    }
    /**
     * Switches the current buffer to the temporary buffer.
     */
    fun switchToTempBuffer() {
        usingTempBuffer = true
        tempBuffer.buffer = mainBuffer.buffer
        tempBuffer.cursor = mainBuffer.cursor
        tempBuffer.size = mainBuffer.size
    }

    /**
     * Switches the current buffer to the main buffer.
     */
    fun switchToMainBuffer() {
        usingTempBuffer = false
        mainBuffer.buffer = tempBuffer.buffer
        mainBuffer.cursor = tempBuffer.cursor
        mainBuffer.size = tempBuffer.size
    }



    /**
     * Adds text to the buffer.
     *
     * @param text The text to add.
     */
    fun add(text: String) {
        buffer += text
        size += text.length
        cursor = size - 1 // Move cursor to the end of the added text
    }

    /**
     * Adds text to the buffer.
     *
     * @param text The text to add.
     */
    fun add(text: Char) {
        buffer += text
        size++
        cursor++ // Move cursor to the end of the added text
    }

    /**
     * Inserts text at the current cursor position.
     *
     * @param text The text to insert.
     */
    fun insert(text: String) {
        if (cursor < 0 || cursor >= size) {
            add(text) // If cursor is out of bounds, append text
        } else {
            buffer = buffer.substring(0, cursor + 1) + text + buffer.substring(cursor + 1)
            size += text.length
            cursor += text.length // Move cursor to the end of the inserted text
        }
    }

    /**
     * Inserts text at the current cursor position.
     *
     * @param text The text to insert.
     */
    fun insert(text: Char) {
        if (cursor < 0 || cursor >= size) {
            add(text) // If cursor is out of bounds, append text
        } else {
            buffer = buffer.substring(0, cursor + 1) + text + buffer.substring(cursor + 1)
            size++
            cursor++ // Move cursor to the end of the inserted text
        }
    }

    /**
     * Removes text from the buffer starting at the current cursor position.
     *
     * @param length The number of characters to remove.
     * @return The removed text or null if the length is greater than the remaining text.
     */
    fun remove(length: Int): String? {
        if (length <= 0 || cursor < 0 || cursor >= size) {
            return null // Invalid length or cursor position
        }
        val end = (cursor + length).coerceAtMost(size)
        val removedText = buffer.substring(cursor, end)
        buffer = buffer.substring(0, cursor) + buffer.substring(end)
        size -= removedText.length
        cursor = (cursor - 1).coerceAtLeast(0) // Move cursor back after removal
        return removedText
    }

    /**
     * Removes the last character from the buffer.
     *
     * @return The removed character or null if the buffer is empty.
     */
    fun removeLast(): Char? {
        return if (size > 0) {
            val removedChar = buffer.last()
            buffer = buffer.dropLast(1)
            size--
            cursor = size - 1 // Move cursor to the end of the new buffer
            removedChar
        } else {
            null
        }
    }

    fun moveCursorLeft(steps: Int = 1): Int {
        if (cursor - steps >= 0) {
            cursor -= steps
        } else {
            cursor = 0 // Prevent moving cursor out of bounds
        }
        return cursor
    }

    fun moveCursorRight(steps: Int = 1): Int {
        if (cursor + steps < size) {
            cursor += steps
        } else {
            cursor = size - 1 // Prevent moving cursor out of bounds
        }
        return cursor
    }

    /**
     * Moves the cursor to the beginning of the buffer.
     */
    fun moveCursorToStart() {
        cursor = 0
    }

    /**
     * Moves the cursor to the end of the buffer.
     */
    fun moveCursorToEnd() {
        cursor = size - 1
    }

    /**
     * Clears the buffer.
     */
    fun clear() {
        buffer = ""
        cursor = -1
        size = 0
    }

    /**
     * Returns the current content of the buffer.
     *
     * @return The content of the buffer.
     */
    fun content(): String {
        return buffer
    }

    /**
     * Returns the current cursor position.
     *
     * @return The cursor position.
     */
    fun cursorPosition(): Int {
        return cursor
    }

    /**
     * Returns the size of the buffer.
     *
     * @return The size of the buffer.
     */
    fun size(): Int {
        return size
    }

    /**
     * Checks if the buffer is empty.
     *
     * @return True if the buffer is empty, false otherwise.
     */
    fun isEmpty(): Boolean {
        return size == 0
    }
}