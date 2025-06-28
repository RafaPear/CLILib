package pt.rafap.clilib.cmdUtils

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
