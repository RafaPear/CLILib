package pt.rafap.clilib.cmdUtils.commands.functions

import pt.rafap.clilib.cmdUtils.Command
import pt.rafap.clilib.cmdUtils.CommandInfo
import pt.rafap.clilib.tools.cmdParser
import pt.rafap.clilib.tools.eval
import pt.rafap.clilib.tools.tExt.joinToString
import pt.rafap.clilib.tools.validateArgs

object IfCmd : Command {
    override val info = CommandInfo(
        description = "Create an if statement",
        longDescription = "Create an if statement with the given condition. The commands will be executed if the condition is true.",
        usage = "if <condition> [commands]",
        aliases = listOf("if"),
        minArgs = 1,
        maxArgs = -1,
        commands = listOf(
            "-h", "--help"
        )
    )

    private var isIf = true

    private data class IfResult(val state: Boolean, val ifResult: Boolean)

    override fun run(args: List<String>): Boolean {
        if (!validateArgs(args, this)) return false

        try {
            return parseIf(args)
        } catch (e: Exception) {
            println("Error: ${e.message}")
            return false
        }
    }

    private fun parseIf(args: List<String>): Boolean {
        var result = doIf(args)
        if (result.ifResult) {
            return result.state
        }

        if (args.contains("elseif")) {
            val newArgs = args.dropWhile { it != "elseif" }.drop(1)
            result = doIf(newArgs)
            if (result.ifResult) {
                return result.state
            }
        }

        if (args.contains("else")) {
            val newArgs = args.dropWhile { it != "else" }.drop(1)
            result = doElse(newArgs)
            return result.state
        }

        isIf = true

        return true
    }

    private fun doIf(args: List<String>): IfResult {
        val condition = args[0]

        if (eval(condition)) {
            val newCmd = args.drop(1).joinToString(" ").substringBefore('}').removePrefix("{")
            return IfResult(cmdParser(newCmd), true)
        }
        return IfResult(state = true, ifResult = false)
    }

    private fun doElse(args: List<String>): IfResult {
        val newCmd = args.joinToString(" ").substringBefore('}').removePrefix("{")
        return IfResult(cmdParser(newCmd), true)
    }
}