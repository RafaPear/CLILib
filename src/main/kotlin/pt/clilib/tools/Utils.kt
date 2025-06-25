package pt.clilib.tools

import org.json.JSONObject
import pt.clilib.registers.VarRegister
import pt.clilib.registers.CmdRegister
import pt.clilib.cmdUtils.Command
import pt.clilib.datastore.Colors
import pt.clilib.ext.RawConsoleInput
import pt.clilib.datastore.Colors.CYAN
import pt.clilib.datastore.Colors.RED
import pt.clilib.datastore.Colors.RESET
import java.io.File
import java.nio.file.Paths
import kotlin.concurrent.thread
import kotlin.random.Random

internal fun readJsonFile(filePath: String, onJson: (JSONObject) -> Boolean): Boolean {
    val file = Environment.resolve(filePath).toFile()
    if (!file.exists()) {
        println("${RED}App Error: Ficheiro não encontrado: $filePath$RESET")
        return false
    }
    return try {
        val json = JSONObject(file.readText())
        onJson(json)
    } catch (e: Exception) {
        println("${RED}App Error: Erro ao ler ficheiro JSON: ${e.message}$RESET")
        false
    }
}

internal fun validateArgs(args: List<String>, command: Command): Boolean {
    val minArgs = command.minArgs
    val maxArgs = command.maxArgs
    val requiresFile = command.requiresFile
    val fileExtension = command.fileExtension

    if (args.size < minArgs || (args.size > maxArgs && maxArgs != -1)) {
        if (minArgs == maxArgs) {
            println("${RED}App Error: Invalid number of arguments. Expected $minArgs, got ${args.size}$RESET")
        } else {
            println("${RED}App Error: Invalid number of arguments. Expected between $minArgs and $maxArgs, got ${args.size}$RESET")
        }
        return false
    }
    if (requiresFile && !args.all { it.endsWith(fileExtension) }) {
        println("${RED}App Error: Invalid file name. File names must end with $fileExtension$RESET")
        return false
    }
    return true
}

/**
 * Validates whether [name] is a valid identifier for variables or functions.
 * A valid name starts with a letter and may contain letters, digits or
 * underscores.
 */
internal fun isValidIdentifier(name: String): Boolean {
    if (name.isEmpty() || !name.first().isLetter()) return false
    return name.all { it.isLetterOrDigit() || it == '_' }
}

/**
 * Função que processa os comandos introduzidos pelo utilizador. Retorna true caso a execução tenha sido bem sucedida.
 * False caso contrário.
 *
 * @param input A string de entrada que contém os comandos a serem processados.
 * @return true se a execução foi bem sucedida, false caso contrário.
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
            println("${RED}App Error: Previous command ($prev) failed. Aborting.$RESET")
            return false
        }
        val command = CmdRegister.find(token[0])
        val similar = CmdRegister.findSimilar(token[0])
        if (command == null) {
            if (!supress) {
                if (similar == null)
                    println("${RED}App Error: Unknown command ${token[0]}$RESET")
                else
                    println("${RED}App Error: Unknown command ${token[0]}. Did you mean '$similar'?$RESET")
            }
            return false
        }
        good = command.run(token.drop(1))
        prev = token.joinToString(" ")
    }
    return good
}

fun eval(expr: String): Boolean {
    val regex = Regex("""\s*(\S+)\s*(==|!=|<=|>=|<|>)\s*(\S+)\s*""")
    val match = regex.matchEntire(expr) ?: return false

    val (aStr, op, bStr) = match.destructured

    val a = aStr.toDoubleOrNull()
    val b = bStr.toDoubleOrNull()

    if (a != null && b != null) {
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

    return false
}

internal fun splitOutsideBraces(input: String): List<String> {
    val result = mutableListOf<String>()
    var current = StringBuilder()
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

            '|', '\n' -> {
                if (insideBraces == 0) {
                    val trimmed = current.toString().trim()
                    if (trimmed.isNotEmpty()) result.add(trimmed)
                    current.clear()
                } else {
                    if (chars.firstOrNull() == '}') chars.removeAt(0)
                    current.append(" |")
                }
            }
            ' ' -> {
                if (current.isNotEmpty() && current.last() != ' ' && current.last() != '{') {
                    current.append(c)
                }
            }
            else -> current.append(c)
        }
    }

    if (current.isNotEmpty()) {
        val trimmed = current.toString().trim()
        if (trimmed.isNotEmpty()) result.add(trimmed)
    }

    return result
}




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
            nInput.replace(name, value.toString())
        else
            nInput.replace("#$name", value.toString())
    }
    return nInput
}


/**
 * Função que imprime uma mensagem de boas-vindas e instruções para o utilizador.
 * A função exibe uma mensagem de boas-vindas e sugere que o utilizador digite 'help' para obter uma lista de comandos disponíveis.
 */
fun drawPrompt() {
    println("${CYAN}App: Welcome to the CLI!$RESET")
    println("${CYAN}App: Type 'help' for a list of commands$RESET")
}

/**
 * Função que limpa o ecrã e redesenha o prompt.
 * A função imprime 50 linhas em branco para limpar o ecrã e, em seguida, chama a função tools.drawPrompt() para exibir o prompt novamente.
 */
fun clearAndRedrawPrompt() {
    clearPrompt()
    drawPrompt()
}

fun clearPrompt() {
    // Clear escape sequence for terminal
    print("\u001b[H\u001b[2J")
}

/**
 * Função que gera um arquivo de grafo aleatório com um número especificado de nós.
 * O arquivo é salvo no diretório especificado e contém as coordenadas dos nós gerados aleatoriamente.
 *
 * @param fileName O nome do arquivo a ser gerado.
 * @param numNodes O número de nós a serem gerados.
 * @param xRange O intervalo para as coordenadas x dos nós (padrão: -90000000 a -86000000).
 * @param yRange O intervalo para as coordenadas y dos nós (padrão: 32400000 a 32800000).
 */
internal fun generateRandomGraphFile(
    fileName: String,
    numNodes: Int,
    xRange: IntRange = -90000000..-86000000,
    yRange: IntRange = 32400000..32800000
) {
    val file = File(fileName)

    // Certificar que o diretório existe
    file.parentFile?.mkdirs()

    file.printWriter().use { out ->
        // Header
        out.println("c Generated test file for $fileName")
        out.println("c")
        out.println("c graph contains $numNodes nodes")
        out.println("c")

        // Generate random nodes in parallel
        val ranges = listOf(
            1..numNodes / 4,
            numNodes / 4 + 1..numNodes / 2,
            numNodes / 2 + 1..numNodes * 3 / 4,
            numNodes * 3 / 4 + 1..numNodes
        )

        val threads = ranges.map { range ->
            thread {
                for (i in range) {
                    val x = Random.nextInt(xRange.first, xRange.last + 1)
                    val y = Random.nextInt(yRange.first, yRange.last + 1)
                    out.println("v $i $x $y")
                }
            }
        }

        threads.forEach { it.join() }

    }
}
/** Opens a new terminal window running the current application. */
fun openExternalTerminal(): Boolean {
    RawConsoleInput.resetConsoleMode()
    if (isRunningInTerminal()) {
        println("${RED}App Error: Cannot open terminal from within a terminal session.$RESET")
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
        println("${RED}App Error: Unable to open terminal: ${e.message}$RESET")
        false
    }
}

fun isRunningInTerminal(): Boolean = System.console() != null

fun Any?.joinToString(): String {
    return when (this) {
        is List<*> -> joinToString("\n")
        is Class<*> -> toGenericString()
        is Array<*> -> joinToString("\n") { it.joinToString() }
        is Map<*, *> -> entries.joinToString("\n") { "${it.key}: ${it.value}" }
        else -> toString()
    }
}

internal fun String.colorize(color: String): String = "${color}$this${RESET}"
