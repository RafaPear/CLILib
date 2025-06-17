package pt.clilib.cmdUtils.commands.directory

import pt.clilib.cmdUtils.Command
import pt.clilib.cmdUtils.CommandInfo
import pt.clilib.tools.*
import java.io.File

/**
 * Lista os ficheiros e pastas do diretório atual ou de um diretório relativo passado como argumento.
 */
object LsCmd : Command {
    override val info = CommandInfo(
        description = "List files in the current directory",
        longDescription = "List files in the current directory or a specified relative directory.",
        usage = "ls [directory]",
        aliases = listOf("ls"),
        minArgs = 0,
        maxArgs = 1
    )

    override fun run(args: List<String>): Boolean {
        if (!validateArgs(args, this)) return false
        val path = if (args.isNotEmpty()) Environment.resolve(args[0]) else Environment.root
        val target = path.toFile()
        val displayPath = path.toString()
        if (!target.exists() || !target.isDirectory) {
            println("${RED}App Error: Directory does not exist or is invalid: $displayPath$RESET")
            return false
        }
        println("${GREEN}Files in ${displayPath.ifEmpty { "current" }} directory:$RESET")
        printFlatDirectoryTree(target)
        return true
    }
    /**
     * Função que imprime uma árvore de diretórios plana a partir de um diretório especificado.
     * A função lista os arquivos e subdiretórios dentro do diretório, ordenando-os por nome.
     *
     * @param dir O diretório a ser listado.
     */
    private fun printFlatDirectoryTree(dir: File) {
        if (!dir.exists()) return
        val children = dir.listFiles()?.sortedBy { it.name.lowercase() } ?: return
        for (file in children) {
            // Usa escapes Unicode para evitar problemas de encoding
            val branch = "\u2514\u2500\u2500 "  // └──
            if (file.isDirectory) {
                println("$branch${BLUE}${file.name}/$RESET")
            } else {
                println("$branch${GREEN}${file.name}$RESET")
            }
        }
    }
}