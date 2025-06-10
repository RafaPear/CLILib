package cmdUtils.commands.cli

import cmdUtils.Command
import tools.BLUE
import tools.ConsoleOutputStream
import tools.RESET
import tools.TerminalWindow
import java.io.PrintStream
import tools.*
import java.awt.Color

object WindowCmd : Command {
    override val description = "Open a new terminal window"
    override val longDescription = "Open a new terminal window with the same session."
    override val usage = "window"
    override val aliases = listOf("window")
    override val minArgs = 0
    override val maxArgs = 0
    override val requiresFile = false
    override val fileExtension = ""

    override fun run(args: List<String>): Boolean {
        if (!validateArgs(args, this)) return false
        val terminal = TerminalWindow(
            title = "CLI App",
            bgColor = Color.BLACK,
            fgColor = Color.WHITE,
            prompt = "${tools.GRAY}${root} >> ${tools.RESET}"
        )
        // redireciona saída
        val stream = ConsoleOutputStream(terminal)
        // autoFlush = false
        System.setOut(PrintStream(stream, false, "UTF-8"))
        System.setErr(PrintStream(stream, false, "UTF-8"))
        return true
    }
}