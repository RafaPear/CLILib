package commands.file

import cmdRegister.Command
import tools.*
import java.io.File

object MkTemplateCmd : Command {
    override val description = "Cria um ficheiro JSON de template para comandos customizados"
    override val longDescription = "Gera um ficheiro JSON de exemplo para criar comandos simples via mkcmd."
    override val usage = "mkcmdtemplate <nome_do_ficheiro.json>"
    override val aliases = listOf("mkcmdtemplate", "mkcmdtpl")
    override val minArgs = 1
    override val maxArgs = 1
    override val requiresFile = false

    override fun run(args: List<String>): Boolean {
        if (!validateArgs(args, this)) return false
        val fileName = if (args[0].endsWith(".json")) args[0] else args[0] + ".json"
        val file = File(root + fileName)
        if (file.exists()) {
            println("${RED}App Error: O ficheiro $fileName já existe.$RESET")
            return false
        }
        val template = """
        {
          "description": "Descrição do comando",
          "longDescription": "Descrição longa do comando.",
          "usage": "nome_do_comando",
          "aliases": ["nome_do_comando", "alias"],
          "minArgs": 1,
          "maxArgs": 1,
          "requiresFile": false,
          "fileExtension": "",
          "run": "print arg[0] exemplo de comando criado a partir de JSON."
        }
        """.trimIndent()
        file.writeText(template)
        println("${CYAN}Template criado em: $fileName$RESET")
        return true
    }
}