package commands

import tools.*
import cmdRegister.Command
import java.io.File

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

        override val description = "Load a script file"
        override val longDescription = "Load and execute a script file containing a series of commands."
        override val usage = "loadscript <script_file>"
        override val aliases = listOf("loadscript", "lscript")
        override val minArgs = 1
        override val maxArgs = 1
        override val requiresFile = true
        override val fileExtension = ".ppc"

        override fun run(args: List<String>): Boolean {
            if (!validateArgs(args, this)) return false
            val prevRoot = root
            println("${YELLOW}Loading script: ${args[0]}${RESET}")
            try {
                val file = File(root + args[0])
                val lines = file.readLines()
                root = System.getProperty("user.dir") + "\\"
                for (line in lines) {
                    if (!line.trim().startsWith(commentCode)) {
                        if (!cmdParser(line)) {
                            println("${RED}Script Error: Failed to execute command in script: ${BLUE}$line${RESET}")
                            break
                        }
                    }
                }
            } catch (e: Exception) {
                println("${RED}App Error: Failed to load script. ${e.message}${RESET}")
                return false
            }
            root = prevRoot
            return true
        }
}