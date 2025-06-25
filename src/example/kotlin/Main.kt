import pt.clilib.CLI
import pt.clilib.registers.CmdRegister
import pt.clilib.cmdUtils.commands.file.BetaEditCmd
import pt.clilib.cmdUtils.commands.functions.FunCmd
import pt.clilib.cmdUtils.commands.functions.IfCmd
import pt.clilib.cmdUtils.commands.functions.WhileCmd
import pt.clilib.tools.openExternalTerminal

import pt.clilib.tools.isRunningInTerminal

fun main() {
    openExternalTerminal()
    if (!isRunningInTerminal()) return
    /*while (true){
        println("Press any key to continue...")
        val input = RawConsoleInput.read(true)
        if (input.toChar().isWhitespace() || input == KeyCodes.CTRL_C || input == KeyCodes.ESCAPE) { // Ctrl+C and escape
            println("No input detected, exiting.")
            break
        } else {
            println("You pressed: $input")
        }
    }*/

    val cli = CLI()
    // app.runSingleCmd("var a 10 | var b 20 | expr b - a | var c | print \$c")
    CmdRegister.register(IfCmd)
    CmdRegister.register(WhileCmd)
    CmdRegister.register(FunCmd)
    CmdRegister.register(BetaEditCmd)
    cli.registerDefaultCommands("--all")

    cli.runSingleCmd("""
        fun teste {
            var a arg[0]
            var b arg[1]
            while a!=b {
                if a<b {
                    expr a + 1
                    var a
                }
                if a>b {
                    expr a - 1
                    var a
                }
                print a: #a, b: #b
                wait arg[2]
            }
        }
    """)
    cli.runSingleCmd("window | betaedit Scripts/exampleScript.ppc")
    cli.runtimeCLI()
    // cli.runFromFile("Scripts/exampleScript.ppc")
}
