package pt.clilib.cmdUtils

/**
 * Representa um comando que pode ser executado na ‘interface’ de linha de comandos (CLI).
 *
 * Esta ‘interface’ define a estrutura que todos os comandos devem seguir, incluindo:
 * - A descrição textual do comando, visível no menu de ajuda.
 * - A forma de utilização do comando e os seus atalhos (aliases).
 * - O número mínimo e máximo de argumentos aceites.
 * - Se o comando espera ficheiros como entrada e qual a extensão esperada.
 *
 * A função [run] é chamada sempre que o comando é executado no CLI, recebendo os argumentos como uma lista de strings
 * e devolvendo um valor booleano que indica o sucesso ou falha da execução.
 *
 * !! OBRIGATÓRIOS !!
 * Os campos `description`, `usage`, `aliases` e a função `run` são de implementação obrigatória.
 * Todos os outros campos são opcionais.
 *
 * !! EXPLICAÇÃO DAS PROPRIEDADES !!
 *
 * - aliases: contém os diferentes nomes que podem ser usados para invocar o comando principal.
 *
 * - Args (min e max): definem o número mínimo e máximo de argumentos que o comando aceita.
 *   Nota: se `maxArgs` for definido como −1, o comando aceita um número ilimitado de argumentos.
 *   O mínimo tem obrigatoriamente de ser menor ou igual ao máximo. Se forem diferentes, isso
 *   indica que o comando tem argumentos opcionais.
 *   Exemplo: se `minArgs = 1` e `maxArgs = 2`, o primeiro argumento é obrigatório e o segundo é opcional.
 *
 *   Se `minArgs` e `maxArgs` não forem definidos, assume-se que o comando não recebe argumentos
 *   (`minArgs = 0`, `maxArgs = 0`).
 *
 * - requiresFile e fileExtension: indicam se o comando espera caminhos de ficheiros como argumentos.
 *   Se `requiresFile = true`, será feita a validação da extensão dos ficheiros através de `fileExtension`.
 *   Por defeito, considera-se `requiresFile = false` e `fileExtension = ""`.
 *
 * - run: função principal do comando. Deve ser sobreposta para definir o comportamento.
 *   Por convenção (definida por nós), recomenda-se:
 *     1. Validar os argumentos com `tools.validateArgs` no início da função;
 *     2. Usar `println` apenas para mensagens relevantes (como erros), e não para texto aleatório.
 *        A ideia é que o `core` trate da lógica principal.
 */

interface Command {
    /** Define a descrição do comando que será apresentada na última
     coluna do menu Help*/
    val description: String

    /** Define a descrição longa do comando que será apresentada quando
     * o comando help é chamado com argumentos*/
    val longDescription: String
        get() = description

    /**Define o modo de utilização do comando que será apresentado
    na primeira coluna do menu Help
    */val usage: String

    /**Define as diferentes formas de chamamento do comando que serão
    apresentadas na segunda coluna do menu Help*/
    val aliases: List<String>

    /**Define o número mínimo de argumentos.

    DEFAULT = 0*/
    val minArgs: Int
        get() = 0

    /**Define o número máximo de argumentos.

    DEFAULT = 0*/
    val maxArgs: Int
        get() = 0

    val commands: List<String>
        get() = emptyList()

    /**Define se o comando espera receber ficheiros.

    DEFAULT = false*/
    val requiresFile: Boolean
        get() = false

    /**Define se a extensão de ficheiro que o comando espera receber.

    DEFAULT = ""*/
    val fileExtension: String
        get() = ""

    /**Função run é executada quando o comando é chamado no CLI.
    Recebe como parâmetros os argumentos [args] do comando e retorna um
    [Boolean] que indica se a execução do comando foi bem sucedida ou não.*/
    fun run(args: List<String>): Boolean
}