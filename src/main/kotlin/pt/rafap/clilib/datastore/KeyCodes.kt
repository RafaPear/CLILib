package pt.rafap.clilib.datastore

object KeyCodes {
    // Teclas comuns
    const val BACKSPACE = 8
    const val TAB = 9
    const val ENTER = 13
    const val ESCAPE = 27
    const val SPACE = 32
    const val DELETE = 127

    // Teclas de controlo (CTRL + letra)
    const val CTRL_A = 1
    const val CTRL_B = 2
    const val CTRL_C = 3
    const val CTRL_D = 4
    const val CTRL_E = 5
    const val CTRL_L = 12
    const val CTRL_Z = 26

    // Sequência ANSI: ESC [ ?
    const val ANSI_PREFIX = 91

    // Setas (ANSI)
    const val ARROW_UP = 57416
    const val ARROW_DOWN = 57424
    const val ARROW_RIGHT = 57421
    const val ARROW_LEFT = 57419

    // Funções (F1 a F4, ESC O ?)
    const val F1 = 80  // ESC O P
    const val F2 = 81
    const val F3 = 82
    const val F4 = 83
}
