package pt.rafap.clilib.cmdUtils

/**
 * Immutable metadata describing a CLI command.
 *
 * @param description Short, one-line description shown in help listings.
 * @param usage Usage string demonstrating how to call the command.
 * @param aliases List of aliases that can be used to invoke the command.
 * @param longDescription Longer, detailed description shown in extended help. Defaults to [description].
 * @param minArgs Minimum number of arguments the command accepts.
 * @param maxArgs Maximum number of arguments the command accepts; use -1 for unlimited.
 * @param commands Optional list of sub-commands or fixed positional tokens supported by the command.
 * @param requiresFile Whether the command expects a file path argument.
 * @param fileExtension Expected file extension when [requiresFile] is true (empty means any).
 */

data class CommandInfo(
    val description: String,
    val usage: String,
    val aliases: List<String>,
    val longDescription: String = description,
    val minArgs: Int = 0,
    val maxArgs: Int = 0,
    val commands: List<String> = emptyList(),
    val requiresFile: Boolean = false,
    val fileExtension: String = ""
)
