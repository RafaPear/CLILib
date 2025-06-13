# 🧩 CLILib

CLILib é uma biblioteca Kotlin para criar interfaces de linha de comandos extensíveis. Permite definir comandos, variáveis e scripts de forma simples e composável.

## 🚀 Funcionalidades
- Registo dinâmico de comandos
- Suporte a scripts `.ppc`
- Variáveis globais e operações matemáticas
- Encadeamento de comandos com `|`
- Criação de comandos via ficheiros JSON
- Comandos prontos como `print`, `var`, `cd`, `ls` e `loadscript`

## 🛠️ Início rápido
```kotlin
import pt.clilib.App
fun main() {
    val app = App()
    app.registerDefaultCommands()
    app.runtimeCLI()
}
```

Também é possível executar comandos a partir de um ficheiro:

```kotlin
app.runFromFile("Scripts/setup.ppc")
```

### 📥 Instalação

```bash
git clone https://github.com/RafaPear/CLILib.git
cd CLILib
./gradlew build
```

O arquivo JAR resultante fica em `build/libs`. Por exemplo:

```
build/libs/CLILib-1.0-SNAPSHOT.jar
```

Após a compilação, execute o exemplo com:

```bash
./gradlew runExample
```

### 📦 Publicar no GitHub Packages
Defina as variáveis de ambiente `USERNAME` e `TOKEN` (o `TOKEN` pode ser o seu
`GITHUB_TOKEN`) e execute:

```bash
./gradlew publish
```

## 📎 Exemplo
```bash
var a 10 | var b 5 | add a b total | print Resultado: $total
```

## 📚 Documentação
Consulte a [Wiki do projeto](https://github.com/RafaPear/CLILib/wiki) para mais detalhes sobre a arquitetura, lista de comandos e guias de extensão.

## 🤝 Contribuições
Contribuições são bem‑vindas! Veja [Como Contribuir](https://github.com/RafaPear/CLILib/wiki/🤝-Como-Contribuir).

## 📄 Licença
Distribuído sob a [MIT License](https://opensource.org/licenses/MIT).

## ✨ Créditos
Desenvolvido com ❤️ por Rafael Vermelho Pereira
