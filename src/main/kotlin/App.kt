import cmdUtils.CmdRegister
import cmdUtils.commands.cli.*
import cmdUtils.commands.file.*
import cmdUtils.commands.directory.*
import cmdUtils.commands.functions.*
import cmdUtils.commands.varOp.*
import tools.*
import java.awt.Color
import java.io.File
import java.io.PrintStream

class App(
    val title : String = "CLI App",
    val bgColor : Color = Color.BLACK,
    val fgColor : Color = Color.WHITE,
    val borderColor : Color = Color.BLACK,
    val prompt : String = "${GRAY}${root} >> $RESET",
) {

    var defaultCommands = mutableListOf(
        CdCmd, ClrCmd, ExitCmd, HelpCmd,
        LoadScriptCmd, LsCmd, MeasureCmd,
        PrintCmd, VersionCmd, WaitCmd,
        WaitForCmd, MkCmd, MkTemplateCmd,
        MkDirCmd, MkFileCmd, EditCmd, VarCmd,
        AddVarCmd, SubVarCmd, DivVarCmd, MultVarCmd,
        ExprVarCmd, DelFileCmd, DelDirCmd, WindowCmd
    )

    /**
     * Metodo que inicia o ciclo principal da interface de linha de comandos (CLI).
     * Limpa o ecrã e redesenha o prompt, depois entra num ciclo infinito
     * onde imprime o prompt, lê a linha de comando do utilizador e a envia para
     * o parser de comandos que,s por sua vez, resolve o comando ou comandos para as suas ações.
     */
    fun runtimeCLI(newTerminal : Boolean = false) {
        if (newTerminal){


            val terminal = TerminalWindow(title, bgColor, fgColor, prompt,
                "${BLUE}App: Welcome to the CLI!${RESET}\n" +
                        "${BLUE}App: Type 'help' for a list of commands${RESET}\n")
            // redireciona saída
            val stream = ConsoleOutputStream(terminal)
            // autoFlush = false
            System.setOut(PrintStream(stream, false, "UTF-8"))
            System.setErr(PrintStream(stream, false, "UTF-8"))
        }
        else {
            clearAndRedrawPrompt()
            while (true) {
                print("${GRAY}${root} >> $RESET")
                val input = readLine()
                cmdParser(input)
            }
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
        val file = File(file)
        if (!file.exists()) {
            println("${RED}App Error: File not found: $file$RESET")
            return
        }
        if (!file.canRead()) {
            println("${RED}App Error: File not readable: $file$RESET")
            return
        }
        if (!file.isFile) {
            println("${RED}App Error: Not a file: $file$RESET")
            return
        }
        val lines = file.readLines()
        for (line in lines) {
            if (!line.trim().startsWith(commentCode)) {
                cmdParser(line)
            }
        }
    }

    fun registerDefaultCommands() {
        // Register all default cmdUtils.commands from cmdUtils.commands package
        CmdRegister.registerAll(defaultCommands)
    }
}
