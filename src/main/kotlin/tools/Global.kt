package tools

import java.io.File

const val version = "1.0"

/**
 * Define o código para os comentários.
*/
const val commentCode = "//"

/**Guarda globalmente o caminho base que está a ser utilizado*/
var root: String = File(System.getProperty("user.dir")).absolutePath + File.separator
    get() = field.trimEnd('/', '\\') + File.separator

var lastCmdDump: Any? = null
