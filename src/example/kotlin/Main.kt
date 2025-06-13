import pt.clilib.App

fun main() {
    val app = App()
    app.registerDefaultCommands()
    // app.runSingleCmd("var a 10 | var b 20 | expr b - a | var c | print \$c")
    app.apply {
        useExternalWindow = true
    }
    app.runtimeCLI()
    // app.runFromFile("Scripts/exampleScript.ppc")
}
