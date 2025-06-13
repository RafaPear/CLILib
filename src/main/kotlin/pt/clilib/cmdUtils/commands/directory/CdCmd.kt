package pt.clilib.cmdUtils.commands.directory

import pt.clilib.cmdUtils.Command
import pt.clilib.tools.*
import java.io.File

/**
 * Muda o diretório atual para outro especificado ou para o diretório anterior com "..".
 */
internal object CdCmd : Command {
    override val description = "Change directory"
    override val longDescription = "Change the current directory to a specified relative directory or to the parent directory with '..'."
    override val usage = "cd <directory>"
    override val aliases = listOf("cd")
    override val minArgs = 1
    override val maxArgs = 1
    override val requiresFile = false
    override val fileExtension = ""

    override fun run(args: List<String>): Boolean {
        if (!validateArgs(args, this)) return false
        try {
            val path = args[0]
            if (path == "..") {
                root = root.dropLast(1).dropLastWhile { it != '/' && it != '\\' }
            } else if (File("$root$path").isDirectory()) {
                root += "$path\\"
            } else {
                println("${RED}App Error: Path does not exist or is invalid: $path$RESET")
                return false
            }
        } catch (e: Exception) {
            println("${RED}App Error: Failed to change directory. ${e.message}$RESET")
            return false
        }
        return true
    }
}