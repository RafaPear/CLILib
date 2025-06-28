package pt.rafap.clilib.cmdUtils.commands.file

import pt.rafap.clilib.cmdUtils.Command
import pt.rafap.clilib.cmdUtils.CommandInfo
import pt.rafap.clilib.datastore.Colors.CYAN
import pt.rafap.clilib.datastore.Colors.RED
import pt.rafap.clilib.datastore.Colors.WHITE
import pt.rafap.clilib.tools.Environment
import pt.rafap.clilib.tools.validateArgs

object EditCmd : Command {
    override val info = CommandInfo(
        description = "Edit a file",
        longDescription = "Edit a file using the default editor.",
        usage = "edit <file>",
        aliases = listOf("edit"),
        minArgs = 1,
        maxArgs = 1,
        requiresFile = true,
        fileExtension = ""
    )

    override fun run(args: List<String>): Boolean {
        if (!validateArgs(args, this)) return false
        val file = Environment.resolve(args[0]).toFile()
        val fileName = file.absolutePath
        return if (file.exists()) {
            println("${CYAN}Editing $fileName...${WHITE}")
            // Open the file in the default editor for windows, mac and linux
            val os = System.getProperty("os.name").lowercase()
            val process = when {
                os.contains("win") -> ProcessBuilder("notepad", fileName).inheritIO().start()
                os.contains("mac") -> ProcessBuilder("open", "-a", "TextEdit", fileName).inheritIO().start()
                os.contains("nix") || os.contains("nux") -> ProcessBuilder("xdg-open", fileName).inheritIO().start()
                else -> {
                    println("${RED}App Error: Unsupported OS.${WHITE}")
                    return false
                }
            }
            process.waitFor()
            println("${CYAN}File $fileName edited successfully!${WHITE}")
            true
        } else {
            println("${RED}App Error: File $fileName does not exist.${WHITE}")
            false
        }
    }
}