package pt.rafap.clilib.cmdUtils.commands.cli

import pt.rafap.clilib.cmdUtils.Command
import pt.rafap.clilib.cmdUtils.CommandInfo
import pt.rafap.clilib.datastore.Colors.CYAN
import pt.rafap.clilib.datastore.Colors.RED
import pt.rafap.clilib.datastore.Colors.WHITE
import pt.rafap.clilib.registers.VarRegister
import pt.rafap.clilib.tools.tExt.joinToString
import pt.rafap.clilib.tools.validateArgs

/**
 * Manage an internal temporary text buffer used by commands.
 *
 * CommandInfo:
 * - description: Manage internal buffer
 * - longDescription: Store, clear and display temporary buffer content that other commands can consume.
 * - usage: buffer [--dump|--clear]
 * - aliases: buffer, buf
 * - minArgs: 0
 * - maxArgs: 1
 */
object BufferCmd : Command {
    override val info = CommandInfo(
        description = "Manipulate the command buffer",
        longDescription = "This command allows you to manipulate the last command's output buffer.",
        usage = "buffer [--clear|--dump]",
        aliases = listOf("buffer", "buf"),
        minArgs = 0,
        maxArgs = 1
    )

    override fun run(args: List<String>): Boolean {
        if (!validateArgs(args, this)) return false

        // Use Colors
        when (args.getOrNull(0)?.lowercase()) {
            "--clear", "-c" -> {
                VarRegister.setLastCmdDump(null)
                println("${CYAN}Buffer cleared.${WHITE}")
            }
            "--dump", "-d" -> {
                val dump = VarRegister.lastCmdDump()
                if (dump == null) {
                    println("${CYAN}Buffer is empty.${WHITE}")
                } else {
                    println("${CYAN}Buffer content:${WHITE}")
                    println(dump.joinToString())
                }
            }
            "--help", "-h" -> {
                displayHelp()
            }
            else -> {
                println("${RED}Invalid argument. Use '--clear' to clear the buffer or '--dump' to display its content.${WHITE}")
                displayHelp()
            }
        }
        return true
    }

    private fun displayHelp() {
        println("${CYAN}Usage: buffer [--clear|--dump]${WHITE}")
        println("${CYAN}Options:${WHITE}")
        println("  --clear, -c  Clear the command buffer.")
        println("  --dump, -d   Display the content of the command buffer.")
        println("  --help, -h   Show this help message.")
    }
}
