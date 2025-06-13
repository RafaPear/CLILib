package pt.clilib.tools

import java.io.File
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

/**Guarda globalmente o caminho base que está a ser utilizado*/
var root: String = File(System.getProperty("user.dir")).absolutePath + File.separator
    get() = field.trimEnd('/', '\\') + File.separator

