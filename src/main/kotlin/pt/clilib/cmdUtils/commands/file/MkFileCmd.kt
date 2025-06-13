package pt.clilib.cmdUtils.commands.file

import pt.clilib.cmdUtils.Command
import pt.clilib.tools.*
import java.io.File

object MkFileCmd : Command {
    override val description = "Cria um arquivo"
    override val longDescription = "Cria um arquivo com o nome especificado."
    override val usage = "mkfile <arquivo>"
    override val aliases = listOf("mkfile")
    override val minArgs = 1
    override val maxArgs = 1
    override val requiresFile = false
    override val fileExtension = ""

    override fun run(args: List<String>): Boolean {
        if (!validateArgs(args, this)) return false
        val fileName = args[0]
        val file = File(fileName)
        return if (file.exists()) {
            println("${RED}App Error: O arquivo $fileName já existe.$RESET")
            false
        } else {
            file.createNewFile()
            println("${CYAN}Arquivo $fileName criado com sucesso!$RESET")
            true
        }
    }
}