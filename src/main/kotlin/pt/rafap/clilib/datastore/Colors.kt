package pt.rafap.clilib.datastore

/**
 * ANSI color codes configurable via environment variables.
 * Fallbacks default to common ANSI sequences.
 */
object Colors {
    private fun env(name: String, default: String): String = System.getenv(name) ?: default

    val BLUE   : String = env("CLILIB_COLOR_BLUE", "\u001B[34m")
    val GREEN  : String = env("CLILIB_COLOR_GREEN", "\u001B[32m")
    val RED    : String = env("CLILIB_COLOR_RED", "\u001B[31m")
    val YELLOW : String = env("CLILIB_COLOR_YELLOW", "\u001B[33m")
    val CYAN   : String = env("CLILIB_COLOR_CYAN", "\u001B[36m")
    val GRAY   : String = env("CLILIB_COLOR_GRAY", "\u001B[90m")
    val ORANGE : String = env("CLILIB_COLOR_ORANGE", "\u001B[38214m")
    val MAGENTA: String = env("CLILIB_COLOR_MAGENTA", "\u001B[35m")
    val WHITE  : String = env("CLILIB_COLOR_WHITE", "\u001B[37m")
    val BLACK  : String = env("CLILIB_COLOR_BLACK", "\u001B[30m")
    val BOLD   : String = env("CLILIB_COLOR_BOLD", "\u001B[1m")
}
