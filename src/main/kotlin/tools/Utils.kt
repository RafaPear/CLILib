package tools

import cmdUtils.Command
import cmdUtils.CmdRegister
import java.io.File
import kotlin.concurrent.thread
import kotlin.random.Random
import org.json.JSONObject

fun readJsonFile(filePath: String, onJson: (JSONObject) -> Boolean): Boolean {
    val file = File(root + filePath)
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

fun validateArgs(args: List<String>, command: Command): Boolean {
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
 * Função que processa os comandos introduzidos pelo utilizador. Retorna true caso a execução tenha sido bem sucedida.
 * False caso contrário.
 *
 * @param input A string de entrada que contém os comandos a serem processados.
 * @return true se a execução foi bem sucedida, false caso contrário.
 * */
fun cmdParser(input: String?, args: List<String> = emptyList(), supress : Boolean = false): Boolean {
    if (input.isNullOrBlank()) return true

    val tokens = input
        .trim().split('|')
        .map { it
            .trim()
            .split(Regex("\\s+"))
            .toMutableList()
        }
        .toMutableList()

    var good = true

    for (token in tokens) {
        // Coloca o valor de uma variavel no lugar da referência. Exemplo "$a" -> 24
        token.replaceAll { it.replaceVars().replaceArgs(args) }
        if (!good) {
            println("${RED}App Error: Previous command failed. Aborting.$RESET")
            return false
        }
        val command = CmdRegister.find(token[0])
        var similar : String? = null
        CmdRegister.all().forEach {
            it.aliases.forEach { i ->
                if (i.startsWith(token[0]))
                    similar = i
            }
        }
        if (command == null) {
            if (!supress) {
                if (similar == null) {
                    println("${RED}App Error: Unknown command ${token[0]}$RESET")
                }
                else {
                    println("${RED}App Error: Unknown command ${token[0]}. Did you mean '$similar'?$RESET")
                }
            }
            return false
        }
        good = command.run(token.drop(1))
    }
    return good
}

fun String.replaceArgs(args: List<String>): String {
    var nInput = this
    var count = 0
    for (arg in args) {
        nInput = nInput.replace("arg[$count]", arg)
        count++
    }
    return nInput
}

fun String.replaceVars(auto : Boolean = false): String {
    var nInput = this
    val vars = VarRegister.all()
    for ((name, value) in vars) {
        nInput = if (auto)
            nInput.replace(name, value.toString())
        else
            nInput.replace("$$name", value.toString())
    }
    return nInput
}

/**
 * Função que imprime uma árvore de diretórios plana a partir de um diretório especificado.
 * A função lista os arquivos e subdiretórios dentro do diretório, ordenando-os por nome.
 *
 * @param dir O diretório a ser listado.
 */
fun printFlatDirectoryTree(dir: File) {
    if (!dir.exists()) return
    val children = dir.listFiles()?.sortedBy { it.name.lowercase() } ?: return
    for (file in children) {
        // Usa escapes Unicode para evitar problemas de encoding
        val branch = "\u2514\u2500\u2500 "  // └──
        if (file.isDirectory) {
            println("$branch${BLUE}${file.name}/$RESET")
        } else {
            println("$branch${GREEN}${file.name}$RESET")
        }
    }
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
    repeat(50) { println() }
    drawPrompt()
}

/**
 * Função que limpa o ecrã sem redesenhar o prompt.
 * A função imprime 50 linhas em branco para limpar o ecrã.
 */
fun clearPrompt() {
    repeat(50) { println() }
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
fun generateRandomGraphFile(
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
