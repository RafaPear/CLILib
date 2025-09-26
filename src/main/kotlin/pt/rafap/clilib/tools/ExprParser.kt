package pt.rafap.clilib.tools

import pt.rafap.clilib.tools.tExt.replaceVars

internal class ExprParser {
    private var pos = 0
    private var input = ""

    fun parse(expression: String): Double {
        pos = 0
        input = expression.trim().replaceVars(true) // remove espaços
        return parseExpression()
    }

    private fun parseExpression(): Double {
        var value = parseTerm()
        while (pos < input.length) {
            when (input[pos]) {
                '+' -> {
                    pos++
                    value += parseTerm()
                }

                '-' -> {
                    pos++
                    value -= parseTerm()
                }

                else -> return value
            }
        }
        return value
    }

    private fun parseTerm(): Double {
        var value = parseFactor()
        while (pos < input.length) {
            when (input[pos]) {
                '*' -> {
                    pos++
                    value *= parseFactor()
                }

                '/' -> {
                    pos++
                    value /= parseFactor()
                }

                else -> return value
            }
        }
        return value
    }

    private fun parseFactor(): Double {
        if (pos >= input.length) throw IllegalArgumentException("Fim inesperado da expressão")

        return when (val ch = input[pos]) {
            in '0'..'9' -> parseNumber()
            '(' -> {
                pos++ // consome '('
                val value = parseExpression()
                if (pos >= input.length || input[pos] != ')') {
                    throw IllegalArgumentException("Parêntesis não fechado")
                }
                pos++ // consome ')'
                value
            }

            '-' -> {
                pos++
                -parseFactor()
            }

            else -> throw IllegalArgumentException("Carácter inesperado: '$ch'")
        }
    }

    private fun parseNumber(): Double {
        val start = pos
        while (pos < input.length && (input[pos] in '0'..'9' || input[pos] == '.')) {
            pos++
        }
        val numberStr = input.substring(start, pos)
        return numberStr.toDoubleOrNull() ?: throw IllegalArgumentException("Número inválido: $numberStr")
    }
}
