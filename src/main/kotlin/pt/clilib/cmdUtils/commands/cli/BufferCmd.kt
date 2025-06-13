package pt.clilib.cmdUtils.commands.cli

import jdk.internal.org.jline.utils.Colors
import pt.clilib.cmdUtils.Command
import pt.clilib.tools.CYAN
import pt.clilib.tools.RED
import pt.clilib.tools.RESET
import pt.clilib.tools.joinToString
import pt.clilib.tools.lastCmdDump
import pt.clilib.tools.validateArgs

// Command that manipulates the lastCmdDump variable buffer, which is used to store the last command's output.
object BufferCmd : Command {
    override val description = "Manipulate the command buffer"
    override val longDescription = "This command allows you to manipulate the last command's output buffer."
    override val usage = "buffer [--clear|--dump]"
    override val aliases = listOf("buffer", "buf")
    override val minArgs = 0
    override val maxArgs = 1

    override fun run(args: List<String>): Boolean {
        if (!validateArgs(args, this)) return false

        // Use Colors
        when (args.getOrNull(0)?.lowercase()) {
            "--clear", "-c" -> {
                lastCmdDump = null
                println("${CYAN}Buffer cleared.$RESET")
            }
            "--dump", "-d" -> {
                if (lastCmdDump == null) {
                    println("${CYAN}Buffer is empty.$RESET")
                } else {
                    println("${CYAN}Buffer content:$RESET")
                    println(lastCmdDump.joinToString())
                }
            }
            "--help", "-h" -> {
                displayHelp()
            }
            else -> {
                println("${RED}Invalid argument. Use '--clear' to clear the buffer or '--dump' to display its content.$RESET")
                displayHelp()
            }
        }
        return true
    }

    private fun displayHelp() {
        println("${CYAN}Usage: buffer [--clear|--dump]$RESET")
        println("${CYAN}Options:$RESET")
        println("  --clear, -c  Clear the command buffer.")
        println("  --dump, -d   Display the content of the command buffer.")
        println("  --help, -h   Show this help message.")
    }
}
