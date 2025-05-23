# CLILib

**CLILib** é uma biblioteca modular para construir interfaces de linha de comandos (CLI) interativas e extensíveis em Kotlin. Ela fornece uma estrutura robusta para criar, registar e executar comandos personalizados, facilitando o desenvolvimento de aplicações baseadas em terminal.

## ✨ Funcionalidades

- **Sistema de Comandos Modular**: Implemente e registe comandos personalizados facilmente.
- **Execução de Scripts**: Suporte para execução de scripts contendo múltiplos comandos.
- **Validação de Argumentos**: Verificação automática de argumentos fornecidos aos comandos.
- **Suporte a Cores ANSI**: Saída colorida para melhor legibilidade no terminal.
- **Barra de Progresso**: Visualização do progresso de operações longas.

## 🛠️ Comandos Disponíveis por predefinição

| Comando       | Descrição                                         |
|---------------|---------------------------------------------------|
| `cd`          | Altera o diretório atual.                         |
| `clr`         | Limpa o ecrã do terminal.                         |
| `exit`        | Encerra a aplicação CLI.                          |
| `help`        | Exibe ajuda geral ou específica de um comando.    |
| `loadscript`  | Executa comandos a partir de um ficheiro script.  |
| `ls`          | Lista os conteúdos do diretório atual.            |
| `measure`     | Mede o tempo de execução de um comando.           |
| `print`       | Imprime uma mensagem no ecrã.                     |
| `version`     | Exibe a versão atual da aplicação.                |
| `wait`        | Aguarda por um número especificado de segundos.   |
| `waitfor`     | Aguarda até que um ficheiro específico exista.    |
