package pt.rafap.clilib.tools

import pt.rafap.clilib.datastore.Colors.BLUE
import pt.rafap.clilib.datastore.Colors.BOLD
import pt.rafap.clilib.datastore.Colors.GRAY
import pt.rafap.clilib.datastore.Colors.WHITE
import pt.rafap.clilib.datastore.Colors.YELLOW
import pt.rafap.clilib.tools.Environment.root
import pt.rafap.clilib.tools.tExt.colorize
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

private class StatusService {
    private val versionProperties = Properties()

    init {
        versionProperties.load(this.javaClass.getResourceAsStream("/version.properties"))
    }

    fun getVersion() : String = versionProperties.getProperty("version") ?: "no version"
}

internal val version = StatusService().getVersion()

/**
 * Define o código para os comentários.
*/
internal const val commentCode = "//"

internal const val LAST_CMD_KEY = "buffer"

/**
 * Application environment settings.
 */
object Environment {
    /** Root directory used by the CLI. */
    var root: Path = Paths.get(System.getProperty("user.dir")).toAbsolutePath()

    /** Returns [root] resolved with [path]. */
    fun resolve(path: String): Path = root.resolve(path).normalize()

    /** Changes the root directory. */
    fun changeRoot(path: String) {
        root = Paths.get(path).toAbsolutePath().normalize()
    }

    /** Formatted path string for prompt rendering. */
    val prompt: String
        get() = root.toString() + File.separator

    var showDirectory = false

    var customPromptText = ">> ".colorize(YELLOW)

    val formatedPrompt: String
        get() = if(showDirectory) "${GRAY}${prompt} ${customPromptText}$WHITE" else "$GRAY${customPromptText}$WHITE"

    var debug = true
        get() = field && isRunningInTerminal()
}


