package pt.rafap.clilib.cmdUtils.commands.cli

import pt.rafap.clilib.cmdUtils.Command
import pt.rafap.clilib.cmdUtils.CommandInfo
import pt.rafap.clilib.datastore.Colors.CYAN
import pt.rafap.clilib.datastore.Colors.RED
import pt.rafap.clilib.datastore.Colors.WHITE
import pt.rafap.clilib.registers.CmdRegister
import pt.rafap.clilib.tools.cmdParser
import pt.rafap.clilib.tools.readJsonFile
import pt.rafap.clilib.tools.validateArgs

/**
 * Create simple commands from JSON templates.
 *
 * CommandInfo:
 * - description: Create command from JSON
 * - longDescription: Reads a JSON file describing a simple command and registers it at runtime.
 * - usage: mkcmd <file.json>
 * - aliases: mkcmd
 * - minArgs: 1
 * - maxArgs: 1
 */
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
                println("${RED}App Error: Invalid JSON file. Please check required fields.${WHITE}")
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
            println("${CYAN}Command '${aliases.firstOrNull() ?: "<unknown>"}' created successfully!${WHITE}")
            true
        }
    }
}