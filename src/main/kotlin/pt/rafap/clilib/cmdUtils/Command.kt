package pt.rafap.clilib.cmdUtils

/**
 * Represents a command that can be executed by the CLI application.
 * A command has descriptive metadata and a [run] function that
 * performs the desired behaviour when invoked.
 */
interface Command {
    /** Command metadata. */
    val info: CommandInfo

    /** Short command description shown in help. */
    val description: String
        get() = info.description

    /** Detailed description for extended help. */
    val longDescription: String
        get() = info.longDescription

    /** Usage string displayed in help. */
    val usage: String
        get() = info.usage

    /** Command aliases used to invoke this command. */
    val aliases: List<String>
        get() = info.aliases

    /** Minimum number of arguments accepted. */
    val minArgs: Int
        get() = info.minArgs

    /** Maximum number of arguments accepted (-1 for unlimited). */
    val maxArgs: Int
        get() = info.maxArgs

    /** Optional argument-based subcommands. */
    val commands: List<String>
        get() = info.commands

    /** Whether file paths are expected as arguments. */
    val requiresFile: Boolean
        get() = info.requiresFile

    /** Expected file extension when [requiresFile] is true. */
    val fileExtension: String
        get() = info.fileExtension

    /**
     * Executes the command with the provided [args].
     * Returns true if the execution succeeded, false otherwise.
     */
    fun run(args: List<String>): Boolean
}
