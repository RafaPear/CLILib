package pt.rafap.clilib.tools.tExt

import pt.rafap.clilib.datastore.Colors.WHITE
import pt.rafap.clilib.registers.VarRegister
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.iterator


internal fun String.replaceArgs(args: List<String>): String {
    var nInput = this
    var count = 0
    for (arg in args) {
        nInput = nInput.replace("arg[$count]", arg)
        count++
    }
    return nInput
}

internal fun String.replaceVars(auto : Boolean = false): String {
    var nInput = this
    val vars = VarRegister.all()
    for ((name, value) in vars) {
        nInput = if (auto)
            nInput.replace(name, value.joinToString())
        else
            nInput.replace("#$name", value.joinToString())
    }
    return nInput
}

fun String.colorize(color: String): String = "${color}$this${WHITE}"
