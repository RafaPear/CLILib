package pt.clilib

import pt.clilib.cmdUtils.CmdRegister
import pt.clilib.cmdUtils.commands.cli.*
import pt.clilib.cmdUtils.commands.file.*
import pt.clilib.cmdUtils.commands.directory.*
import pt.clilib.cmdUtils.commands.functions.*
import pt.clilib.cmdUtils.commands.varOp.*
import pt.clilib.cmdUtils.Command
import pt.clilib.tools.*
import java.awt.Color
import org.jline.reader.EndOfFileException
import org.jline.reader.LineReader
import org.jline.reader.LineReaderBuilder
import org.jline.reader.UserInterruptException
import org.jline.reader.impl.completer.StringsCompleter

class CLI() {

    var defaultCommands = mutableListOf(
        CdCmd, ClrCmd, ExitCmd,
        LoadScriptCmd, LsCmd, MeasureCmd,
        PrintCmd, VersionCmd, WaitCmd,
        WaitForCmd, MkCmd, MkTemplateCmd,
        MkDirCmd, MkFileCmd, EditCmd, VarCmd,
        AddVarCmd, SubVarCmd, DivVarCmd, MultVarCmd,
        ExprVarCmd, DelFileCmd, DelDirCmd, WindowCmd,
        BufferCmd
    )

    val prompt: String
        get() = "${GRAY}${Environment.prompt} >> $RESET"

    init {
        CmdRegister.register(HelpCmd)
    }

    /**
     * Metodo que inicia o ciclo principal da interface de linha de comandos (CLI).
     * Limpa o ecrã e redesenha o prompt, depois entra num ciclo infinito
     * onde imprime o prompt, lê a linha de comando do utilizador e a envia para
     * o parser de comandos que,s por sua vez, resolve o comando ou comandos para as suas ações.
     */
    fun runtimeCLI() {
        }

        clearAndRedrawPrompt()
        val reader = LineReaderBuilder.builder()
            .completer(StringsCompleter(CmdRegister.all().flatMap { it.aliases }))
            .build()

        while (true) {
            val input = try {
                reader.readLine(prompt)
            } catch (_: UserInterruptException) {
                continue
            } catch (_: EndOfFileException) {
                break
            cmdParser(input)
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
        val file = Environment.resolve(file).toFile()
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
            if ("--cli" in tokens) load.addAll(listOf(CdCmd, ClrCmd, ExitCmd, HelpCmd, PrintCmd, VersionCmd, WaitCmd, WaitForCmd, MkCmd, WindowCmd, BufferCmd))
            if ("--file" in tokens) load.addAll(listOf(MkFileCmd, MkTemplateCmd, EditCmd, BetaEditCmd, DelFileCmd))
            if ("--dir" in tokens) load.addAll(listOf(CdCmd, LsCmd, MkDirCmd, DelDirCmd))
            if ("--var" in tokens) load.addAll(listOf(VarCmd, AddVarCmd, SubVarCmd, DivVarCmd, MultVarCmd, ExprVarCmd))
            if ("--utils" in tokens) load.addAll(listOf(LoadScriptCmd, MeasureCmd, WhileCmd, FunCmd, IfCmd))
        }

        if (load.isEmpty()) load.addAll(defaultCommands)

        CmdRegister.registerAll(load.toList())
    }

    fun registerDefaultCommands() = registerDefaultCommands("")
}
