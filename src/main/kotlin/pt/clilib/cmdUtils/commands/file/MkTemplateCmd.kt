package pt.clilib.cmdUtils.commands.file

import pt.clilib.cmdUtils.Command
import pt.clilib.cmdUtils.CommandInfo
import pt.clilib.tools.*
import java.io.File

object MkTemplateCmd : Command {
    override val info = CommandInfo(
        description = "Create a template JSON file for custom commands",
        longDescription = "Generate a JSON example used to create simple commands via mkcmd.",
        usage = "mkcmdtemplate <file.json>",
        aliases = listOf("mkcmdtemplate", "mkcmdtpl"),
        minArgs = 1,
        maxArgs = 1,
        requiresFile = false
    )

    override fun run(args: List<String>): Boolean {
        if (!validateArgs(args, this)) return false
        val fileName = if (args[0].endsWith(".json")) args[0] else args[0] + ".json"
        val file = Environment.resolve(fileName).toFile()
        if (file.exists()) {
            println("${RED}App Error: File $fileName already exists.$RESET")
            return false
        }
        val template = """
        {
          "description": "Command description",
          "longDescription": "Long command description.",
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
        println("${CYAN}Template created at: $fileName$RESET")
        return true
    }
}