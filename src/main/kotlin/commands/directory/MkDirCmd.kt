package commands.directory

import cmdRegister.Command
import tools.*
import java.io.File

object MkDirCmd : Command {
    override val description = "Cria um diretório"
    override val longDescription = "Cria um diretório com o nome especificado."
    override val usage = "mkdir <diretório>"
    override val aliases = listOf("mkdir")
    override val minArgs = 1
    override val maxArgs = 1
    override val requiresFile = false
    override val fileExtension = ""

    override fun run(args: List<String>): Boolean {
        if (!validateArgs(args, this)) return false
        val dirName = args[0]
        val dir = File(dirName)
        return if (dir.exists()) {
            println("${RED}App Error: O diretório $dirName já existe.$RESET")
            false
        } else {
            dir.mkdirs()
            println("${CYAN}Diretório $dirName criado com sucesso!$RESET")
            true
        }
    }
}