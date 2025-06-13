package pt.clilib.cmdUtils.commands.directory

import pt.clilib.cmdUtils.Command
import pt.clilib.tools.*
import java.io.File

/**
 * Lista os ficheiros e pastas do diretório atual ou de um diretório relativo passado como argumento.
 */
object LsCmd : Command {
    override val description = "List files in the current directory"
    override val longDescription = "List files in the current directory or a specified relative directory."
    override val usage = "ls [directory]"
    override val aliases = listOf("ls")
    override val minArgs = 0
    override val maxArgs = 1

    override fun run(args: List<String>): Boolean {
        if (!validateArgs(args, this)) return false
        val path = if (args.isNotEmpty()) "$root${args[0]}" else root
        val target = File(path)
        if (!target.exists() || !target.isDirectory) {
            println("${RED}App Error: Directory does not exist or is invalid: $path$RESET")
            return false
        }
        println("${GREEN}Files in ${path.ifEmpty { "current" }} directory:$RESET")
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