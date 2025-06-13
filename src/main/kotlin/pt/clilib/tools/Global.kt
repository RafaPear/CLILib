package pt.clilib.tools

import java.io.File

internal const val version = "1.0"

/**
 * Define o código para os comentários.
*/
internal const val commentCode = "//"

/**Guarda globalmente o caminho base que está a ser utilizado*/
internal var root: String = File(System.getProperty("user.dir")).absolutePath + File.separator
    get() = field.trimEnd('/', '\\') + File.separator

internal var lastCmdDump: Any? = null
