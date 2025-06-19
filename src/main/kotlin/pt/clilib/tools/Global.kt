package pt.clilib.tools

import java.io.File
import java.nio.file.Path
import java.nio.file.Paths
import java.util.Properties

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
}


