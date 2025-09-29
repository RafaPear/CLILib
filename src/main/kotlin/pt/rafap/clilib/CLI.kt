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
import pt.rafap.clilib.tools.Environment.formatedPrompt
import pt.rafap.clilib.tools.Terminal.doTerminalInteraction
import pt.rafap.clilib.tools.tExt.compareTo

/**
 * Main entry point for interacting with the command-line library.
 * Provides helpers to run the interactive loop, execute single commands,
 * and load commands from files while managing default command registration.
 */
class CLI() {

    /**
     * Default command set that can be registered via [registerDefaultCommands].
     */
    var defaultCommands = arrayOf(
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
     * Starts the main command-line interface (CLI) loop.
     * Clears the screen and draws the prompt, then enters an infinite loop where it prints the prompt,
     * reads the user's input line and sends it to the command parser.
     *
     * @param useExt If true, attempts to open a compatible external terminal; otherwise runs in the
     * current terminal (or stdin/stdout mode if not attached to a terminal).
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
                print(formatedPrompt)
                val input = readln()
                cmdParser(input)
            }
        }
    }

    /**
     * Executes a single command.
     * Sends the given command string to the command parser.
     *
     * @param cmd The command to execute.
     */
    fun runSingleCmd(cmd: String) {
        cmdParser(cmd)
    }

    /**
     * Executes a set of commands from a file.
     * Reads the file line by line and sends each line to the command parser,
     * ignoring lines that start with the comment code [commentCode].
     *
     * @param file Path (relative or absolute) to the file to read.
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

    /**
     * Registers the default commands, optionally filtered by groups.
     *
     * Recognized options: "--all", "--cli", "--file", "--dir", "--var", "--utils".
     * When empty, registers all default commands.
     *
     * @param options Space-separated options indicating which groups to load.
     */
    fun registerDefaultCommands(options: String = "") {
        val tokens = options.split(Regex("\\s+")).filter { it.isNotBlank() }
        if (tokens.isEmpty()) {
            CmdRegister.registerAll(*defaultCommands)
            return
        }

        val load = mutableListOf<Command>()

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

        CmdRegister.registerAll(*load.toTypedArray())
    }
}
