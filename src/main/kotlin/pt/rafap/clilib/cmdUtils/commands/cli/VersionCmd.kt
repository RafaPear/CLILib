package pt.rafap.clilib.cmdUtils.commands.cli

import pt.rafap.clilib.cmdUtils.Command
import pt.rafap.clilib.cmdUtils.CommandInfo
import pt.rafap.clilib.datastore.Colors.CYAN
import pt.rafap.clilib.datastore.Colors.WHITE
import pt.rafap.clilib.datastore.Colors.YELLOW
import pt.rafap.clilib.tools.validateArgs
import pt.rafap.clilib.tools.version

/**
 * Shows the application's version.
 *
 * CommandInfo:
 * - description: Show application version
 * - longDescription: Displays current CLI version and build information.
 * - usage: version
 * - aliases: version, v
 * - minArgs: 0
 * - maxArgs: 0
 */
object VersionCmd : Command {

    override val info = CommandInfo(
        description = "Show version information and credits",
        usage = "version",
        aliases = listOf("version")
    )

    override fun run(args: List<String>): Boolean {
        if (!validateArgs(args, this)) return false

        drawBox(
            """
                    {Course Letters} - Version $version
                    Developed by: {Developers}
                    For the ambit of: {Course}
                """.trimIndent())
        println()
        return true
    }


    private fun drawBox(
        text: String,
        borderColor: String = CYAN,
        textColor: String = YELLOW,
        resetColor: String = WHITE
    ) {
        val lines = text.lines()
        val maxLength = lines.maxOf { it.length }
        val horizontalBorder = "═".repeat(maxLength + 2)  // +2 para os espaços à esquerda e direita

        println("$borderColor╔$horizontalBorder╗$resetColor")
        for (line in lines) {
            val padding = " ".repeat(maxLength - line.length + 1) // +1 espaço extra à direita
            println("$borderColor║ $resetColor$textColor$line$padding$resetColor$borderColor║$resetColor")
        }
        println("$borderColor╚$horizontalBorder╝$resetColor")
    }
}