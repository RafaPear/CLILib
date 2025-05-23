package commands.cli

import cmdRegister.Command
import tools.CYAN
import tools.RESET
import tools.YELLOW
import tools.validateArgs
import tools.version

/**
 * Mostra a versão da aplicação e os créditos dos autores.
 */
object VersionCmd : Command {

    override val description = "Show version information and credits"
    override val usage = "version"
    override val aliases = listOf("version")

    override fun run(args: List<String>): Boolean {
        if (!validateArgs(args, this)) return false

        drawBox(
            """
                    {Course Letters} - Version ${version}
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
        resetColor: String = RESET
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