package commands.cli

import cmdRegister.CmdRegister
import cmdRegister.Command
import tools.CYAN
import tools.RED
import tools.RESET
import tools.cmdParser
import tools.readJsonFile
import tools.validateArgs

object MkCmd : Command {
    override val description = "Cria um comando simples a partir de um ficheiro JSON"
    override val longDescription = "Permite criar comandos simples que apenas imprimem uma mensagem fixa, definida num ficheiro JSON."
    override val usage = "mkcmd <ficheiro.json>"
    override val aliases = listOf("mkcmd")
    override val minArgs = 1
    override val maxArgs = 1
    override val requiresFile = true
    override val fileExtension = ".json"

    override fun run(args: List<String>): Boolean {
        if (!validateArgs(args, this)) return false
        return readJsonFile(args[0]) { json ->
            val description = json.optString("description")
            val longDescription = json.optString("longDescription", description)
            val usage = json.optString("usage")
            val aliases = json.optJSONArray("aliases")?.let { arr ->
                List(arr.length()) { arr.getString(it) }
            } ?: emptyList()
            val minArgs = json.optInt("minArgs", 0)
            val maxArgs = json.optInt("maxArgs", 0)
            val requiresFile = json.optBoolean("requiresFile", false)
            val fileExtension = json.optString("fileExtension", "")
            val run = json.optString("run")

            if (description.isEmpty() || usage.isEmpty() || aliases.isEmpty() || run.isEmpty()) {
                println("${RED}App Error: Ficheiro JSON inválido. Verifique os campos obrigatórios.${RESET}")
                return@readJsonFile false
            }
            val customCmd = object : Command {
                override val description = description
                override val longDescription = longDescription
                override val usage = usage
                override val aliases = aliases
                override val minArgs = minArgs
                override val maxArgs = maxArgs
                override val requiresFile = requiresFile
                override val fileExtension = fileExtension

                override fun run(args: List<String>): Boolean {
                    if (!validateArgs(args, this)) return false
                    if (args.isNotEmpty()) {
                        cmdParser(run, args)
                    } else
                        cmdParser(run)
                    return true
                }
            }
            CmdRegister.register(customCmd)
            println("${CYAN}Comando '${aliases.first()}' criado com sucesso!${RESET}")
            true
        }
    }
}