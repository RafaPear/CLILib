package tools

const val version = "1.0"

/**
 * Define o código para os comentários.
*/
const val commentCode = "//"

/**Guarda globalmente o caminho base que está a ser utilizado*/
var root = System.getProperty("user.dir") + '\\'
    get() = field.dropLastWhile { it == '\\' || it == '/' } + '\\'