package pt.rafap.clilib.cmdUtils.commands.functions

import pt.rafap.clilib.cmdUtils.Command
import pt.rafap.clilib.cmdUtils.CommandInfo
import pt.rafap.clilib.datastore.Colors.BLUE
import pt.rafap.clilib.datastore.Colors.RED
import pt.rafap.clilib.datastore.Colors.WHITE
import pt.rafap.clilib.datastore.Colors.YELLOW
import pt.rafap.clilib.tools.Environment
import pt.rafap.clilib.tools.cmdParser
import pt.rafap.clilib.tools.commentCode
import pt.rafap.clilib.tools.validateArgs

/**
 * Comando `LOADSCRIPT`
 *
 * Carrega e executa um ficheiro de script com uma série de comandos.
 *
 * Uso: `loadscript <script_file>`
 *
 * Aceita um argumento que é o nome do ficheiro de script a ser carregado.
 */

object LoadScriptCmd : Command {

        override val info = CommandInfo(
            description = "Load a script file",
            longDescription = "Load and execute a script file containing a series of commands.",
            usage = "loadscript <script_file>",
            aliases = listOf("loadscript", "lscript"),
            minArgs = 1,
            maxArgs = 1,
            requiresFile = true,
            fileExtension = ".ppc"
        )

        override fun run(args: List<String>): Boolean {
            if (!validateArgs(args, this)) return false
            val prevRoot = Environment.root
            println("${YELLOW}Loading script: ${args[0]}${WHITE}")
            try {
                val file = Environment.resolve(args[0]).toFile()
                val lines = file.readLines()
                Environment.changeRoot(System.getProperty("user.dir"))
                for (line in lines) {
                    if (!line.trim().startsWith(commentCode)) {
                        if (!cmdParser(line)) {
                            println("${RED}Script Error: Failed to execute command in script: ${BLUE}$line${WHITE}")
                            break
                        }
                    }
                }
            } catch (e: Exception) {
                println("${RED}App Error: Failed to load script. ${e.message}${WHITE}")
                return false
            }
            Environment.root = prevRoot
            return true
        }
}