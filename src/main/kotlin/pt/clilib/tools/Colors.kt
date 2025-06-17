package pt.clilib.tools

// File that stores the ANSI color codes used in the project
// ANSI Colors

internal enum class AnsiColor(val code: String) {
    RESET("\u001B[0m"),
    BLUE("\u001B[34m"),
    GREEN("\u001B[32m"),
    RED("\u001B[31m"),
    YELLOW("\u001B[33m"),
    CYAN("\u001B[36m"),
    GRAY("\u001B[90m"),
    ORANGE("\u001B[38;5;214m"),
    MAGENTA("\u001B[35m"),
    WHITE("\u001B[37m"),
    BLACK("\u001B[30m"),
    BOLD("\u001B[1m");
}

internal val RESET = AnsiColor.RESET.code
internal val BLUE = AnsiColor.BLUE.code
internal val GREEN = AnsiColor.GREEN.code
internal val RED = AnsiColor.RED.code
internal val YELLOW = AnsiColor.YELLOW.code
internal val CYAN = AnsiColor.CYAN.code
internal val GRAY = AnsiColor.GRAY.code
internal val ORANGE = AnsiColor.ORANGE.code
internal val MAGENTA = AnsiColor.MAGENTA.code
internal val WHITE = AnsiColor.WHITE.code
internal val BLACK = AnsiColor.BLACK.code
internal val BOLD = AnsiColor.BOLD.code

internal fun String.colorize(color: AnsiColor): String = "${color.code}$this${AnsiColor.RESET.code}"

