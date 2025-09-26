import pt.rafap.clilib.CLI
import pt.rafap.clilib.cmdUtils.commands.functions.FunCmd
import pt.rafap.clilib.cmdUtils.commands.functions.IfCmd
import pt.rafap.clilib.cmdUtils.commands.functions.WhileCmd
import pt.rafap.clilib.registers.CmdRegister
import pt.rafap.clilib.registers.VarRegister
import pt.rafap.clilib.tools.isRunningInTerminal
import pt.rafap.clilib.tools.openExternalTerminal

class Vector(val x: Int, val y: Int) : Comparable<Vector> {
    operator fun plus(other: Vector): Vector {
        return Vector(x + other.x, y + other.y)
    }

    operator fun minus(other: Vector): Vector {
        return Vector(x - other.x, y - other.y)
    }

    // Comparable interface implementation
    override fun compareTo(other: Vector): Int {
        return when {
            this.x == other.x && this.y == other.y -> 0
            this.x < other.x || (this.x == other.x && this.y < other.y) -> -1
            else -> 1
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Vector) return false
        return this.x == other.x && this.y == other.y
    }

    override fun toString(): String {
        return "Vector(x=$x, y=$y)"
    }
}

fun main() {
    //openExternalTerminal()
    //if (!isRunningInTerminal()) return

    // Example usage of the Vector class
    val v1 = Vector(1, 2)
    val v2 = Vector(2, 2)

    val cli = CLI()

    CmdRegister.register(IfCmd)
    CmdRegister.register(WhileCmd)
    CmdRegister.register(FunCmd)
    cli.registerDefaultCommands("--all")

    VarRegister.register("v1", v1)
    VarRegister.register("v2", v2)

    cli.runSingleCmd("""
        print #v1 #v2 | 
        fun testFun {
            if v1==v2 { 
                print v1 is equal to v2 
            } elseif v1>v2 {
                print v1 is greater to v2 
            }
            else {
                print v1 is less than v2
            }
        }""")
    cli.runtimeCLI(false)
    // cli.runFromFile("Scripts/exampleScript.ppc")
}
