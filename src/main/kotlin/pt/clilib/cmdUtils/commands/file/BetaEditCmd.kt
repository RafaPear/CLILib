package pt.clilib.cmdUtils.commands.file

import pt.clilib.cmdUtils.Command
import pt.clilib.cmdUtils.CommandInfo
import pt.clilib.tools.CYAN
import pt.clilib.tools.ESC
import pt.clilib.tools.Environment
import pt.clilib.tools.RED
import pt.clilib.tools.RESET
import pt.clilib.tools.validateArgs
import java.io.File
import java.io.InputStream

object BetaEditCmd : Command {
    override val info = CommandInfo(
        description = "Edit a file",
        longDescription = "Edit a file using the default editor.",
        usage = "bedit <file>",
        aliases = listOf("bedit"),
        minArgs = 1,
        maxArgs = 1,
        requiresFile = true,
        fileExtension = ""
    )

    override fun run(args: List<String>): Boolean {
        if (!validateArgs(args, this)) return false
        val file = Environment.resolve(args[0]).toFile()
        val fileName = file.absolutePath
        return if (file.exists()) {
            println("${CYAN}Editing $fileName...${RESET}")
            // Open the file in the default editor for windows, mac and linux
            // Coloca terminal em raw mode e sem eco
            val osUnix = System.getProperty("os.name").contains("nux") ||
                    System.getProperty("os.name").contains("Mac")
            if (osUnix) try {
                Runtime.getRuntime().exec(arrayOf("sh", "-c", "stty -echo raw </dev/tty")).waitFor()
            } catch (_: Exception) {}

            Runtime.getRuntime().addShutdownHook(Thread {
                if (osUnix) Runtime.getRuntime().exec(arrayOf("sh", "-c", "stty sane </dev/tty")).waitFor()
                print("$ESC[?25h")
            })

            val filename = args.getOrNull(0) ?: "novo.txt"
            val buffer = File(filename).takeIf { it.exists() }?.readLines()?.map { StringBuilder(it) }?.toMutableList()
                ?: mutableListOf(StringBuilder())
            var row = 0
            var col = 0

            fun clamp() {
                if (row < 0) row = 0
                if (row >= buffer.size) row = buffer.size - 1
                if (col < 0) col = 0
                if (col > buffer[row].length) col = buffer[row].length
            }

            fun render(status: String = "") {
                print("$ESC[2J$ESC[H$ESC[?25l")
                buffer.forEach {
                    println(it.toString())
                }
                println("— $filename | $status".padEnd(80))
                print("$ESC[${row + 1};${col + 1}H$ESC[?25h")
                System.out.flush()
            }

            fun save(): Boolean = try {
                File(filename).writeText(buffer.joinToString("\n"))
                true
            } catch (_: Exception) { false }

            render("Ctrl-S para gravar • Ctrl-Q para sair")

            val inp: InputStream = System.`in`
            loop@ while (true) {
                val b1 = inp.read()
                when (b1) {
                    // ----- setas Unix
                    27 -> if (inp.read() == 91) when (inp.read()) {
                        65 -> row--     // ↑
                        66 -> row++     // ↓
                        68 -> col--     // ←
                        67 -> col++     // →
                    }
                    // ----- setas Windows
                    0xE0 -> when (inp.read()) {
                        72 -> row--
                        80 -> row++
                        75 -> col--
                        77 -> col++
                    }
                    127, 8 -> { // Backspace
                        if (col > 0) {
                            buffer[row].deleteCharAt(col - 1); col--
                        } else if (row > 0) {
                            col = buffer[row - 1].length
                            buffer[row - 1].append(buffer[row])
                            buffer.removeAt(row); row--
                        }
                    }
                    13 -> { // Enter
                        val line = buffer[row]
                        val newLine = StringBuilder(line.substring(col))
                        line.setLength(col)
                        buffer.add(row + 1, newLine)
                        row++; col = 0
                    }
                    17 -> { // Ctrl-Q
                        render("Sai…")
                        break@loop
                    }
                    19 -> { // Ctrl-S
                        render(if (save()) "Gravado 👍" else "Erro ao gravar ❌")
                        continue@loop
                    }
                    in 32..126 -> { // caracteres imprimíveis
                        buffer[row].insert(col, b1.toChar()); col++
                    }
                }
                clamp()
                render()
            }
            println("${CYAN}File $fileName edited successfully!${RESET}")
            true
        } else {
            println("${RED}App Error: File $fileName does not exist.${RESET}")
            false
        }
    }
}