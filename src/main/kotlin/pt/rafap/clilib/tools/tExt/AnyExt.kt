package pt.rafap.clilib.tools.tExt

import kotlin.reflect.KFunction
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.jvm.jvmErasure

/**
 * Converte um objeto em uma string formatada.
 *
 * - Se for uma lista, converte cada elemento em uma linha.
 * - Se for uma classe, retorna a representação genérica da classe.
 * - Se for um array, converte cada elemento em uma linha.
 * - Se for um mapa, formata as entradas como "chave: valor".
 * - Caso contrário, chama `toString()`.
 *
 * @return String formatada representando o objeto.
 */
fun Any?.joinToString(): String {
    return when (this) {
        is List<*> -> joinToString("\n")
        is Class<*> -> toGenericString()
        is Array<*> -> joinToString("\n") { it.joinToString() }
        is Map<*, *> -> entries.joinToString("\n") { "${it.key}: ${it.value}" }
        else -> toString()
    }
}

/**
 * Compara dois objetos de forma genérica.
 *
 * @param other  Outro objeto a ser comparado.
 * @return       Resultado da comparação: -1 se `this` é menor, 0 se são iguais, 1 se `this` é maior.
 * @throws IllegalArgumentException se os objetos não puderem ser comparados.
 */
operator fun Any.compareTo(other: Any): Int {
    return when {
        this is Comparable<*> && other is Comparable<*> -> (this as Comparable<Any>).compareTo(other)
        this is Number && other is Number -> this.toDouble().compareTo(other.toDouble())
        else -> throw IllegalArgumentException("Cannot compare $this with $other")
    }
}

/**
 * Obtém um método de operador binário pelo nome e tipo do argumento.
 *
 * @param name  Nome do operador (ex: "plus", "minus", etc.).
 * @param arg   Argumento para o qual o método deve ser encontrado.
 * @return      O método correspondente ou `null` se não existir.
 */
fun Any.getMethodOrNull(name: String, arg: Any): KFunction<*>? =
    this::class.memberFunctions.firstOrNull { f ->
        f.isOperator &&
                f.name == name &&
                f.parameters.size == 2 &&                 // receiver + 1 arg
                f.parameters[1].type.jvmErasure == arg::class
    }

/**
 * Executa uma operação binária entre `this` e [other].
 *
 * @param op  Nome do operador tal como reconhecido pelo compilador
 *            ("plus", "minus", "times", "div", etc.).
 * @return    Resultado da operação ou `null` se não existir.
 */
fun Any.doOperation(other: Any, op: String): Any? {
    if (this is Number && other is Number) {
        val a = this.toDouble()
        val b = other.toDouble()
        return when (op) {
            "plus", "+",  "add"   -> a + b
            "minus", "-", "sub"   -> a - b
            "times", "*", "mul"   -> a * b
            "div", "/",  "quot"   -> a / b
            "rem", "%",  "mod"    -> a % b
            else                  -> null   // não suportado
        }
    }

    val fn = getMethodOrNull(op, other) ?: return null
    return runCatching { fn.call(this, other) }.getOrNull()
}