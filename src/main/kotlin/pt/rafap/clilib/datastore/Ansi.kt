package pt.rafap.clilib.datastore

/**
 * Centralized ANSI escape sequences and color configuration.
 * Values can be overridden via environment variables.
 */
object Ansi {
    private fun env(name: String, default: String): String = System.getenv(name) ?: default

    // Core
    val ESC: String = env("CLILIB_ANSI_ESC", "\u001B[")

    // Cursor control
    val SAVE_CURSOR: String = env("CLILIB_ANSI_SAVE_CURSOR", ESC + "s")
    val RESTORE_CURSOR: String = env("CLILIB_ANSI_RESTORE_CURSOR", ESC + "u")
    val CURSOR_HOME: String = env("CLILIB_ANSI_CURSOR_HOME", ESC + "H")
    val HIDE_CURSOR: String = env("CLILIB_ANSI_HIDE_CURSOR", ESC + "?25l")
    val SHOW_CURSOR: String = env("CLILIB_ANSI_SHOW_CURSOR", ESC + "?25h")

    // Screen clearing
    val CLEAR_SCREEN: String = env("CLILIB_ANSI_CLEAR_SCREEN", ESC + "2J")

    // Line clearing
    val CLEAR_LINE_TO_END: String = env("CLILIB_ANSI_CLEAR_LINE_TO_END", ESC + "0K")
    val CLEAR_LINE_TO_START: String = env("CLILIB_ANSI_CLEAR_LINE_TO_START", ESC + "1K")
    val CLEAR_LINE: String = env("CLILIB_ANSI_CLEAR_LINE", ESC + "2K")

    // Deletion/insert
    val DELETE_CHAR_AT_CURSOR: String = env("CLILIB_ANSI_DELETE_CHAR", ESC + "P")
}
