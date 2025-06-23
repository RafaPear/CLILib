import pt.clilib.CLI
import pt.clilib.cmdUtils.CmdRegister
import pt.clilib.cmdUtils.commands.functions.FunCmd
import pt.clilib.cmdUtils.commands.functions.IfCmd
import pt.clilib.cmdUtils.commands.functions.WhileCmd

fun main() {
    val cli = CLI()
    // app.runSingleCmd("var a 10 | var b 20 | expr b - a | var c | print \$c")
    CmdRegister.register(IfCmd)
    CmdRegister.register(WhileCmd)
    CmdRegister.register(FunCmd)
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
    cli.runtimeCLI()
    // cli.runFromFile("Scripts/exampleScript.ppc")
}
