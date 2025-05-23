import cmdRegister.CmdRegister
import commands.*
import tools.GRAY
import tools.RESET
import tools.clearAndRedrawPrompt
import tools.cmdParser
import tools.commentCode
import tools.root
import java.io.File

class App {

    /**
     * Metodo que inicia o ciclo principal da interface de linha de comandos (CLI).
     * Limpa o ecrã e redesenha o prompt, depois entra num ciclo infinito
     * onde imprime o prompt, lê a linha de comando do utilizador e a envia para
     * o parser de comandos que, por sua vez, resolve o comando ou comandos para as suas ações.
     */
    fun runtimeCLI() {
        clearAndRedrawPrompt()
        while (true) {
            print("${GRAY}${root} >> $RESET")
            val input = readLine()
            cmdParser(input)
        }
    }

    /**
     * Metodo que executa um único comando.
     * Limpa o ecrã e redesenha o prompt, depois envia o comando para o parser de comandos.
     *
     * @param cmd O comando a ser executado.
     */
    fun runSingleCmd(cmd: String) {
        clearAndRedrawPrompt()
        cmdParser(cmd)
    }

    /**
     * Metodo que executa um conjunto de comandos a partir de um ficheiro.
     * Lê o ficheiro linha a linha e envia cada linha para o parser de comandos.
     *
     * @param file O caminho do ficheiro a ser lido.
     */
    fun runFromFile(file: String) {
        val lines = File(file).readLines()
        for (line in lines) {
            if (!line.trim().startsWith(commentCode)) {
                cmdParser(line)
            }
        }
    }

    fun registerDefaultCommands() {
        val default = listOf(
            CdCmd, ClrCmd, ExitCmd, HelpCmd,
            LoadScriptCmd, LsCmd, MeasureCmd,
            PrintCmd, VersionCmd, WaitCmd,
            WaitForCmd
        )
        CmdRegister.registerAll(default)
    }
}
