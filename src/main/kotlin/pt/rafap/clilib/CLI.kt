package pt.rafap.clilib

import pt.rafap.clilib.cmdUtils.Command
import pt.rafap.clilib.cmdUtils.commands.cli.*
import pt.rafap.clilib.cmdUtils.commands.directory.CdCmd
import pt.rafap.clilib.cmdUtils.commands.directory.DelDirCmd
import pt.rafap.clilib.cmdUtils.commands.directory.LsCmd
import pt.rafap.clilib.cmdUtils.commands.directory.MkDirCmd
import pt.rafap.clilib.cmdUtils.commands.file.*
import pt.rafap.clilib.cmdUtils.commands.functions.*
import pt.rafap.clilib.cmdUtils.commands.varOp.*
import pt.rafap.clilib.datastore.Colors.RED
import pt.rafap.clilib.datastore.Colors.WHITE
import pt.rafap.clilib.registers.CmdRegister
import pt.rafap.clilib.tools.*
import pt.rafap.clilib.tools.Environment.prompt
import pt.rafap.clilib.tools.Terminal.doTerminalInteraction

class CLI() {

    var defaultCommands = mutableListOf(
        CdCmd, ClrCmd, ExitCmd,
        LoadScriptCmd, LsCmd, MeasureCmd,
        PrintCmd, VersionCmd, WaitCmd,
        WaitForCmd, MkCmd, MkTemplateCmd,
        MkDirCmd, MkFileCmd, EditCmd, BetaEditCmd, VarCmd,
        AddVarCmd, SubVarCmd, DivVarCmd, MultVarCmd,
        ExprVarCmd, DelFileCmd, DelDirCmd, WindowCmd,
        BufferCmd, DebugCmd
    )

    init {
        CmdRegister.register(HelpCmd)
    }

    /**
     * Metodo que inicia o ciclo principal da interface de linha de comandos (CLI).
     * Limpa o ecrã e redesenha o prompt, depois entra num ciclo infinito
     * onde imprime o prompt, lê a linha de comando do utilizador e a envia para
     * o parser de comandos que,s por sua vez, resolve o comando ou comandos para as suas ações.
     */
    fun runtimeCLI(useExt: Boolean = false) {
        if (useExt) {
            openExternalTerminal()
        }
        if (isRunningInTerminal()){
            clearAndRedrawPrompt()
            doTerminalInteraction()
        } else if (!useExt) {
            clearAndRedrawPrompt()
            while (true) {
                print(prompt)
                val input = readln()
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
        cmdParser(cmd)
    }

    /**
     * Metodo que executa um conjunto de comandos a partir de um ficheiro.
     * Lê o ficheiro linha a linha e envia cada linha para o parser de comandos.
     *
     * @param file O caminho do ficheiro a ser lido.
     */
    fun runFromFile(file: String) {
        val file = Environment.resolve(file).toFile()
        if (!file.exists()) {
            println("${RED}App Error: File not found: $file$WHITE")
            return
        }
        if (!file.canRead()) {
            println("${RED}App Error: File not readable: $file$WHITE")
            return
        }
        if (!file.isFile) {
            println("${RED}App Error: Not a file: $file$WHITE")
            return
        }
        val lines = file.readLines()
        for (line in lines) {
            if (!line.trim().startsWith(commentCode)) {
                cmdParser(line)
            }
        }
    }

    fun registerDefaultCommands(options: String = "") {
        val tokens = options.split(Regex("\\s+")).filter { it.isNotBlank() }
        if (tokens.isEmpty()) {
            CmdRegister.registerAll(defaultCommands)
            return
        }

        val load = mutableSetOf<Command>()

        if ("--all" in tokens || "-all" in tokens) {
            load.addAll(defaultCommands)
        } else {
            if ("--cli" in tokens) load.addAll(listOf(CdCmd,
                ClrCmd, ExitCmd, HelpCmd, PrintCmd, VersionCmd, WaitCmd, WaitForCmd, MkCmd, WindowCmd, BufferCmd))
            if ("--file" in tokens) load.addAll(listOf(MkFileCmd, MkTemplateCmd, EditCmd, BetaEditCmd, DelFileCmd))
            if ("--dir" in tokens) load.addAll(listOf(CdCmd, LsCmd, MkDirCmd, DelDirCmd))
            if ("--var" in tokens) load.addAll(listOf(VarCmd, AddVarCmd, SubVarCmd, DivVarCmd, MultVarCmd, ExprVarCmd))
            if ("--utils" in tokens) load.addAll(listOf(LoadScriptCmd, MeasureCmd, WhileCmd, FunCmd, IfCmd))
        }

        if (load.isEmpty()) load.addAll(defaultCommands)

        CmdRegister.registerAll(load.toList())
    }
}
