package pt.clilib.cmdUtils.commands.cli

import pt.clilib.cmdUtils.CmdRegister
import pt.clilib.cmdUtils.Command
import pt.clilib.cmdUtils.CommandInfo
import pt.clilib.tools.CYAN
import pt.clilib.tools.RED
import pt.clilib.tools.RESET
import pt.clilib.tools.cmdParser
import pt.clilib.tools.readJsonFile
import pt.clilib.tools.validateArgs

object MkCmd : Command {
    override val info = CommandInfo(
        description = "Create a simple command from a JSON file",
        longDescription = "Allows creating simple commands that only print a fixed message defined in a JSON file.",
        usage = "mkcmd <file.json>",
        aliases = listOf("mkcmd"),
        minArgs = 1,
        maxArgs = 1,
        requiresFile = true,
        fileExtension = ".json"
    )

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
                override val info = CommandInfo(
                    description = description,
                    longDescription = longDescription,
                    usage = usage,
                    aliases = aliases,
                    minArgs = minArgs,
                    maxArgs = maxArgs,
                    requiresFile = requiresFile,
                    fileExtension = fileExtension
                )

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