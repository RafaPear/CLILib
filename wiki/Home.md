**CLILib** is a Kotlin library for creating command line interfaces (CLI) in a modular, extensible and interactive way. Designed to speed up the development of command-based tools, the library offers:

- Built-in commands ready to use (e.g. `cd`, `ls`, `print`, `help`, `exit`, etc.)
- Scripting support and the ability to run multiple commands on a single line
- A variable system with registration and direct manipulation
- Dynamic registration and execution of custom commands
- Argument validation, expression parsing and file handling

The library aims to make it easy to quickly prototype functional CLIs while providing a solid and extensible base for more complex projects.

## ✨ Main features

- ✅ Interactive prompt with command interpretation
- 🧠 Arithmetic expression parsing (e.g. `expr a + b * (2 - c)`)
- 📂 Commands with directory, file and script support
- 🧩 Dynamic command registration (`CmdRegister`)
- 🔧 Global variable system (`VarRegister`)
- 📜 Ability to run `.ppc` scripts
- 🧪 Testable with `runSingleCmd`, `runFromFile` or `runtimeCLI`

## 📎 Script example

```bash
var a 10 | var b 20 | expr b - a | var c | print $c
```

## 🚀 Quick start
```kotlin
fun main() {
    val app = App()
    app.registerDefaultCommands()
    app.runtimeCLI()
}
```
