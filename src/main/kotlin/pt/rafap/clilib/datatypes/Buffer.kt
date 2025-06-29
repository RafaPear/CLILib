package pt.rafap.clilib.datatypes

import pt.rafap.clilib.tools.TUI.printDebug

/**
 * Buffer is a simple text buffer class that allows for basic text manipulation.
 * It supports operations like adding text, removing text, and navigating through the buffer.
 */
class Buffer {
    /**
     * Holds the mutable state for a buffer instance (main or temporary).
     */
    private data class BufferState(
        var buffer: String = "",
        var cursor: Int = 0,
        var size: Int = 0
    )

    private val mainBuffer = BufferState()
    private val tempBuffer = BufferState()

    /**
     * When true, all operations target the temporary buffer; otherwise they target the main buffer.
     */
    private var usingTempBuffer = false

    // ──────────────────────────────────────────────────────────────────────────────
    // Convenience accessor to the *current* buffer state, depending on the flag.
    // ──────────────────────────────────────────────────────────────────────────────
    private val current: BufferState
        get() = if (usingTempBuffer) tempBuffer else mainBuffer

    // All property accesses delegate to the *current* BufferState instance.
    private var buffer: String
        get() = current.buffer
        set(value) { current.buffer = value }

    private var cursor: Int
        get() = current.cursor
        set(value) { current.cursor = value }

    private var size: Int
        get() = current.size
        set(value) { current.size = value }

    // ──────────────────────────────────────────────────────────────────────────────
    //  Public API – buffer selection
    // ──────────────────────────────────────────────────────────────────────────────
    fun isUsingTempBuffer(): Boolean = usingTempBuffer

    /**
     * Switch to the main buffer, discarding any changes made in the temporary buffer.
     */
    fun useMainBuffer() {
        usingTempBuffer = false
    }

    /**
     * Switch to the temporary buffer, allowing edits to be made without affecting the main buffer.
     */
    fun useTempBuffer() {
        usingTempBuffer = true
    }

    /**
     * Switch immediately to the temporary buffer, **copying** the contents of the
     * main buffer so that edits are isolated.
     */
    fun switchToTempBuffer() {
        tempBuffer.buffer = mainBuffer.buffer
        tempBuffer.cursor = mainBuffer.cursor
        tempBuffer.size = mainBuffer.size
        usingTempBuffer = true
    }

    /**
     * Commit the temporary buffer back into the main buffer and continue working
     * on the main one.
     */
    fun switchToMainBuffer() {
        mainBuffer.buffer = tempBuffer.buffer
        mainBuffer.cursor = tempBuffer.cursor
        mainBuffer.size = tempBuffer.size
        usingTempBuffer = false

    }

    // ──────────────────────────────────────────────────────────────────────────────
    //  Editing operations
    // ──────────────────────────────────────────────────────────────────────────────
    fun add(text: String) {
        if (text.isBlank()) return // Não adiciona texto vazio
        buffer += text
        size += text.length
        cursor = size - 1 // Cursor aponta para o último carácter inserido
        printDebug("Cursor: $cursor, Size: $size")
    }

    fun add(text: Char) {
        if (text.isWhitespace()) return // Não adiciona texto vazio
        buffer += text
        size += 1
        cursor = size - 1
        printDebug("Cursor: $cursor, Size: $size")
    }

    fun insert(text: String) {
        if (cursor < 0 || cursor >= size) {
            // Cursor fora de limites ⇒ append
            add(text)
        } else {
            if (text.isBlank()) return // Não adiciona texto vazio
            buffer = buffer.substring(0, cursor + 1) + text + buffer.substring(cursor + 1)
            size += text.length
            cursor += text.length
        }
        printDebug("Cursor: $cursor, Size: $size")
    }

    fun insert(text: Char) {
        if (cursor < 0 || cursor >= size) {
            add(text)
        } else {
            buffer = buffer.substring(0, cursor + 1) + text + buffer.substring(cursor + 1)
            size += 1
            cursor += 1
        }
        printDebug("Cursor: $cursor, Size: $size")
    }

    /**
     * Remove `length` caracteres a partir do cursor actual.
     * @return Texto removido ou null se a operação não for válida.
     */
    fun remove(length: Int): String? {
        if (length <= 0 || cursor < 0 || cursor > size) return null

        val end = (cursor + length).coerceAtMost(size)
        val removed = buffer.substring(cursor, end)
        buffer = buffer.removeRange(cursor, end)
        size -= removed.length
        cursor -= removed.length // Mantém cursor dentro dos limites
        printDebug("Cursor: $cursor, Size: $size") // Move o cursor para o início da linha
        return removed
    }

    /**
     * Remove o último carácter do buffer.
     */
    fun removeLast(): Char? {
        if (size == 0) return null
        val removed = buffer.last()
        buffer = buffer.dropLast(1)
        size -= 1
        cursor = if (size == 0) -1 else size - 1
        printDebug("Cursor: $cursor, Size: $size")
        return removed
    }

    // ──────────────────────────────────────────────────────────────────────────────
    //  Navegação do cursor
    // ──────────────────────────────────────────────────────────────────────────────
    fun moveCursorLeft(steps: Int = 1): Int {
        cursor = (cursor - steps).coerceAtLeast(0)
        printDebug("Cursor: $cursor, Size: $size")
        return cursor
    }

    fun moveCursorRight(steps: Int = 1): Int {
        cursor = if (size == 0) {
            -1
        } else {
            (cursor + steps).coerceAtMost(size - 1)
        }
        printDebug("Cursor: $cursor, Size: $size")
        return cursor
    }

    fun moveCursorToStart() {
        cursor = if (size == 0) -1 else 0
        printDebug("Cursor: $cursor, Size: $size")
    }

    fun moveCursorToEnd() {
        cursor = if (size == 0) -1 else size - 1
        printDebug("Cursor: $cursor, Size: $size")
    }

    // ──────────────────────────────────────────────────────────────────────────────
    //  Misc.
    // ──────────────────────────────────────────────────────────────────────────────
    fun clear() {
        buffer = ""
        cursor = -1
        size = 0
        printDebug("Cursor: $cursor, Size: $size")
    }

    fun content(): String = buffer

    fun cursorPosition(): Int = cursor

    fun size(): Int = size // Renomeado para evitar conflito com a propriedade privada

    fun isEmpty(): Boolean = size == 0
}