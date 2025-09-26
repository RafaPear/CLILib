package pt.rafap.clilib.tools

import org.json.JSONObject
import pt.rafap.clilib.cmdUtils.Command
import pt.rafap.clilib.datastore.Colors.CYAN
import pt.rafap.clilib.datastore.Colors.RED
import pt.rafap.clilib.datastore.Colors.WHITE
import pt.rafap.clilib.ext.RawConsoleInput
import pt.rafap.clilib.registers.CmdRegister
import pt.rafap.clilib.registers.VarRegister
import pt.rafap.clilib.tools.tExt.compareTo
import pt.rafap.clilib.tools.tExt.replaceArgs
import pt.rafap.clilib.tools.tExt.replaceVars
import java.nio.file.Paths

/**
 * Reads a JSON file and passes the result to the [onJson] callback.
 *
 * @param filePath Path to the JSON file (relative to current root or absolute).
 * @param onJson Callback that receives the parsed JSON object and returns true/false depending on success.
 * @return true if the callback returns true and no errors occur; false otherwise.
 */
fun readJsonFile(filePath: String, onJson: (JSONObject) -> Boolean): Boolean {
    val file = Environment.resolve(filePath).toFile()
    if (!file.exists()) {
        println("${RED}App Error: Ficheiro não encontrado: $filePath$WHITE")
        return false
    }
    return try {
        val json = JSONObject(file.readText())
        onJson(json)
    } catch (e: Exception) {
        println("${RED}App Error: Erro ao ler ficheiro JSON: ${e.message}$WHITE")
        false
    }
}

/**
 * Validates the number and format of arguments for a [command].
 *
 * @param args List of arguments provided in the call.
 * @param command Command definition that sets limits and requirements (e.g., required file extension).
 * @return true if the arguments are valid; false otherwise (with an error message).
 */
fun validateArgs(args: List<String>, command: Command): Boolean {
    val minArgs = command.minArgs
    val maxArgs = command.maxArgs
    val requiresFile = command.requiresFile
    val fileExtension = command.fileExtension

    if (args.size < minArgs || (args.size > maxArgs && maxArgs != -1)) {
        if (minArgs == maxArgs) {
            println("${RED}App Error: Invalid number of arguments. Expected $minArgs, got ${args.size}$WHITE")
        } else {
            println("${RED}App Error: Invalid number of arguments. Expected between $minArgs and $maxArgs, got ${args.size}$WHITE")
        }
        return false
    }
    if (requiresFile && !args.all { it.endsWith(fileExtension) }) {
        println("${RED}App Error: Invalid file name. File names must end with $fileExtension$WHITE")
        return false
    }
    return true
}

/**
 * Validates whether [name] is a valid identifier for variables or functions.
 * A valid name starts with a letter and may contain letters, digits or
 * underscores.
 */
fun isValidIdentifier(name: String): Boolean {
    if (name.isEmpty() || !name.first().isLetter()) return false
    return name.all { it.isLetterOrDigit() || it == '_' }
}

/**
 * Processes commands entered by the user.
 * Returns true if execution succeeded; false otherwise.
 *
 * @param input The input string containing the commands to process.
 * @param args Optional arguments used for placeholder replacement when executing scripted commands.
 * @param supress If true, suppresses unknown command errors (useful for scripted flows).
 * @return true if execution succeeded; false otherwise.
 * */
fun cmdParser(input: String?, args: List<String> = emptyList(), supress : Boolean = false): Boolean {
    if (input.isNullOrBlank()) return true

    val tokens = splitOutsideBraces(
        input
            .replaceArgs(args)
            .trim()
    ).map {
        it.trim().split(Regex("\\s+")).toMutableList()
    }.toMutableList()

    var good = true
    var prev = ""

    for (token in tokens) {
        // Coloca o valor de uma variavel no lugar da referência. Exemplo "$a" -> 24
        for (i in token.indices) {
            if (token[i].contains("|"))
                break
            token[i] = token[i].replaceVars()
        }
        if (!good) {
            println("${RED}App Error: Previous command ($prev) failed. Aborting.$WHITE")
            return false
        }
        val command = CmdRegister.find(token[0])
        val similar = CmdRegister.findSimilar(token[0])
        if (command == null) {
            if (!supress) {
                if (similar == null)
                    println("${RED}App Error: Unknown command ${token[0]}$WHITE")
                else
                    println("${RED}App Error: Unknown command ${token[0]}. Did you mean '$similar'?$WHITE")
            }
            return false
        }
        good = command.run(token.drop(1))
        prev = token.joinToString(" ")
    }
    return good
}

/**
 * Evaluates a simple boolean expression between two operands, supporting operators
 * ==, !=, <, <=, >, >=. Referenced variables will be resolved via [VarRegister].
 *
 * @param expr Expression to evaluate (e.g., "a >= 10").
 * @return true if the expression is true; false if it is false or invalid.
 */
fun eval(expr: String): Boolean {
    val regex = Regex("""\s*(\S+)\s*(==|!=|<=|>=|<|>)\s*(\S+)\s*""")
    val match = regex.matchEntire(expr) ?: return false

    val (left, op, right) = match.destructured

    val a = VarRegister.get(left) ?: left
    val b = VarRegister.get(right) ?: right

    if (a !is Comparable<*> && b !is Comparable<*>) {
        println("${RED}App Error: Cannot compare non-comparable values: '$a' and '$b'$WHITE")
        return false
    }

    return when (op) {
        "==" -> a == b
        "!=" -> a != b
        ">"  -> a > b
        "<"  -> a < b
        ">=" -> a >= b
        "<=" -> a <= b
        else -> false
    }
}

/**
 * Splits a string by '|' while ignoring separators that are inside braces '{ }'.
 * Keeps inner content intact and trims unnecessary spaces at the edges.
 *
 * @param input Full input string to split.
 * @return List of segments resulting from outside-brace splits.
 */
internal fun splitOutsideBraces(input: String): List<String> {
    val result = mutableListOf<String>()
    val current = StringBuilder()
    var insideBraces = 0

    val chars = input.trim().toMutableList()

    while (chars.isNotEmpty()) {
        val c = chars.removeAt(0)

        when (c) {
            '{' -> {
                insideBraces++
                current.append(c)
                if (chars.firstOrNull() == '\n') chars.removeAt(0)
            }

            '}' -> {
                if (current.isNotEmpty() && (current.last() == ' ' || current.last() == '|')) {
                    current.deleteAt(current.lastIndex)
                    chars.addFirst(c)
                } else {
                    insideBraces--
                    current.append(c)
                }
            }

            '|' -> {
                if (insideBraces == 0) {
                    val trimmed = current.toString().trim()
                    if (trimmed.isNotEmpty()) result.add(trimmed)
                    current.clear()
                } else {
                    if (chars.firstOrNull() == '}') chars.removeAt(0)
                    current.append("|")
                }
            }
            ' ' -> {
                if (current.isNotEmpty() && current.last() != ' ' && current.last() != '{') {
                    current.append(c)
                }
            }
            else -> {
                if (!c.isWhitespace()) {
                    if (current.isNotEmpty() && (current.last() == '}' || current.last() == '|')) {
                        current.append(' ')
                    }
                    current.append(c)
                }
            }
        }
    }

    if (current.isNotEmpty()) {
        val trimmed = current.toString().trim()
        if (trimmed.isNotEmpty()) result.add(trimmed)
    }

    return result
}

/**
 * Prints the welcome message and the help hint in the initial prompt.
 * Takes no parameters and returns no value.
 */
internal fun drawPrompt() {
    println("${CYAN}App: Welcome to the CLI!$WHITE")
    println("${CYAN}App: Type 'help' for a list of commands$WHITE")
}

/**
 * Clears the screen and redraws the initial prompt.
 * Calls [clearPrompt] and then [drawPrompt].
 */
internal fun clearAndRedrawPrompt() {
    clearPrompt()
    drawPrompt()
}

/**
 * Clears the terminal screen using ANSI escape sequences.
 * Takes no parameters and returns no value.
 */
internal fun clearPrompt() {
    // Clear escape sequence for terminal
    print(pt.rafap.clilib.datastore.Ansi.CURSOR_HOME + pt.rafap.clilib.datastore.Ansi.CLEAR_SCREEN)
}

/**
 * Abre uma nova janela de terminal a executar a aplicação atual.
 * Impede a execução quando já está a correr dentro de um terminal.
 *
 * @return true se o terminal for aberto com sucesso; false em caso de erro.
 */
fun openExternalTerminal(): Boolean {
    RawConsoleInput.resetConsoleMode()
    if (isRunningInTerminal()) {
        println("${RED}App Error: Cannot open terminal from within a terminal session.$WHITE")
        return false
    }
    val javaHome = System.getProperty("java.home")
    val javaBin = Paths.get(javaHome, "bin", "java").toString()
    val classPath = System.getProperty("java.class.path")
    val command = System.getProperty("sun.java.command")
    val exec = "\"$javaBin\" -cp \"$classPath\" $command"
    val os = System.getProperty("os.name").lowercase()

    val builder = when {
        os.contains("win") -> ProcessBuilder("cmd", "/c", "start", "cmd", "/k", exec)
        os.contains("mac") -> ProcessBuilder("osascript", "-e", "tell application \"Terminal\" to do script \"$exec\"")
        else -> ProcessBuilder("x-terminal-emulator", "-e", exec)
    }

    return try {
        builder.start()
        true
    } catch (e: Exception) {
        println("${RED}App Error: Unable to open terminal: ${e.message}$WHITE")
        false
    }
}

fun isRunningInTerminal(): Boolean = System.console() != null
