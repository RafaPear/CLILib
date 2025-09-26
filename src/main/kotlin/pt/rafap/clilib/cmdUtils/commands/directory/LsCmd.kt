package pt.rafap.clilib.cmdUtils.commands.directory

import pt.rafap.clilib.cmdUtils.Command
import pt.rafap.clilib.cmdUtils.CommandInfo
import pt.rafap.clilib.datastore.Colors.BLUE
import pt.rafap.clilib.datastore.Colors.GREEN
import pt.rafap.clilib.datastore.Colors.RED
import pt.rafap.clilib.datastore.Colors.WHITE
import pt.rafap.clilib.tools.Environment
import pt.rafap.clilib.tools.validateArgs
import java.io.File

/**
 * Lists files and folders in the current directory or a relative directory passed as an argument.
 *
 * CommandInfo:
 * - description: List files in the current directory
 * - longDescription: List files in the current directory or a specified relative directory.
 * - usage: ls [-p path] [-c columns]
 * - aliases: ls
 * - minArgs: 0
 * - maxArgs: 4
 */
object LsCmd : Command {
    override val info = CommandInfo(
        description = "List files in the current directory",
        longDescription = "List files in the current directory or a specified relative directory.",
        usage = "ls [-p path] [-c columns]",
        aliases = listOf("ls"),
        minArgs = 0,
        maxArgs = 4
    )

    override fun run(args: List<String>): Boolean {
        if (!validateArgs(args, this)) return false
        // val path = if (args.isNotEmpty()) Environment.resolve(args[0]) else Environment.root
        val path = Environment.resolve(parseArg(args, "-p", Environment.root.toString()))
        val target = path.toFile()

        val cols: Int = parseArg(args, "-c", "4").toIntOrNull() ?: 4

        val displayPath = path.toString()
        if (!target.exists() || !target.isDirectory) {
            println("${RED}App Error: Directory does not exist or is invalid: $displayPath$WHITE")
            return false
        }
        println("${GREEN}Files in ${displayPath.ifEmpty { "current" }} directory:$WHITE")
        printFlatDirectoryTree(target, cols)
        return true
    }

    private fun parseArg(args: List<String>, flag: String, defaultValue: String): String {
        return if (args.contains(flag)) {
            val index = args.indexOf(flag)
            if (index + 1 < args.size) {
                args[index + 1]
            } else {
                defaultValue
            }
        } else {
            defaultValue
        }
    }

    /**
     * Prints a flat directory tree from a specified directory.
     * Lists files and subdirectories inside the directory, sorted by name.
     *
     * @param dir The directory to list.
     */
    private fun printFlatDirectoryTree(dir: File, cols: Int) {
        if (!dir.exists()) return
        val children = dir.listFiles()?.sortedBy { it.name.lowercase() } ?: return
        var currentLine = 0
        var currentCol = 0
        val lines: MutableList<MutableList<String>> = mutableListOf()
        val maxSizePerCol: MutableList<Int> = MutableList(cols) { 0 }
        for (file in children) {
            // Use Unicode escapes to avoid encoding issues
            val fileName = if (file.isDirectory) file.name + "/" else file.name
            val message = if (file.isDirectory) {
                "${BLUE}${fileName}${WHITE}"
            } else {
                "${GREEN}${fileName}${WHITE}"
            }

            if (lines.size <= currentLine) {
                lines.add(mutableListOf())
            }

            lines[currentLine].add(message)

            if (maxSizePerCol[currentCol] < message.length) {
                maxSizePerCol[currentCol] = message.length
            }

            currentCol++
            if (currentCol >= cols) {
                currentCol = 0
                currentLine++
            }
        }

        for (line in lines) {
            for (col in line.indices) {
                val toPrint = line[col]
                val padding = " ".repeat(maxSizePerCol[col] - toPrint.length + 2)
                print(toPrint + padding)
            }
            println()
        }
    }
}