package pt.clilib.cmdUtils.commands.cli

import pt.clilib.cmdUtils.Command
import pt.clilib.cmdUtils.CommandInfo
import pt.clilib.tools.*
import java.io.PrintStream
import pt.clilib.tools.validateArgs
import pt.clilib.tools.Environment

object WindowCmd : Command {
    override val info = CommandInfo(
        description = "Open a new terminal window",
        longDescription = "Open a new terminal window with the same session.",
        usage = "window",
        aliases = listOf("window"),
        minArgs = 0,
        maxArgs = 0,
        requiresFile = false,
        fileExtension = ""
    )

    override fun run(args: List<String>): Boolean {
        if (!validateArgs(args, this)) return false
        val terminal = TerminalWindow(
            title = "CLI App - $version",
            prompt = "${GRAY}${Environment.prompt} >> ${RESET}"
        )
        // redireciona saída
        val stream = ConsoleOutputStream(terminal)
        // autoFlush = false
        System.setOut(PrintStream(stream, false, "UTF-8"))
        System.setErr(PrintStream(stream, false, "UTF-8"))
        return true
    }
}