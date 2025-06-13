package pt.clilib.cmdUtils.commands.file

import pt.clilib.cmdUtils.Command
import pt.clilib.tools.CYAN
import pt.clilib.tools.RED
import pt.clilib.tools.RESET
import pt.clilib.tools.root
import pt.clilib.tools.validateArgs
import java.io.File

internal object EditCmd : Command {
    override val description = "Edit a file"
    override val longDescription = "Edit a file using the default editor."
    override val usage = "edit <file>"
    override val aliases = listOf("edit")
    override val minArgs = 1
    override val maxArgs = 1
    override val requiresFile = true
    override val fileExtension = ""

    override fun run(args: List<String>): Boolean {
        if (!validateArgs(args, this)) return false
        val fileName = root + args[0]
        val file = File(fileName)
        return if (file.exists()) {
            println("${CYAN}Editing $fileName...${RESET}")
            // Open the file in the default editor for windows, mac and linux
            val os = System.getProperty("os.name").lowercase()
            val process = when {
                os.contains("win") -> ProcessBuilder("notepad", fileName).inheritIO().start()
                os.contains("mac") -> ProcessBuilder("open", "-a", "TextEdit", fileName).inheritIO().start()
                os.contains("nix") || os.contains("nux") -> ProcessBuilder("xdg-open", fileName).inheritIO().start()
                else -> {
                    println("${RED}App Error: Unsupported OS.${RESET}")
                    return false
                }
            }
            process.waitFor()
            println("${CYAN}File $fileName edited successfully!${RESET}")
            true
        } else {
            println("${RED}App Error: O arquivo $fileName não existe.${RESET}")
            false
        }
    }
}