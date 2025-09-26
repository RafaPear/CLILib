# 🧩 CLILib

[![](https://jitpack.io/v/RafaPear/CLILib.svg)](https://jitpack.io/#RafaPear/CLILib)

CLILib is a Kotlin library for building extensible command line interfaces. It allows you to define commands, variables and scripts in a simple and composable way.

## 🚀 Features
- Dynamic command registration
- Support for `.ppc` scripts
- Global variables and math operations
- Command chaining with `|`
- Create commands from JSON files
- Built-in commands like `print`, `var`, `cd`, `ls` and `loadscript`

## 🛠️ Quick start
```kotlin
fun main() {
    val cli = CLI()
    // load all commands
    cli.registerDefaultCommands()
    // or choose groups like utils and directory commands
    // cli.registerDefaultCommands("--utils --dir")
    cli.runtimeCLI()
}
```

You can also execute commands from a file:

```kotlin
cli.runFromFile("Scripts/setup.ppc")
```

### 📥 Installation

```bash
git clone https://github.com/RafaPear/CLILib.git
cd CLILib
./gradlew build
```

The resulting JAR file is placed in `build/libs`. For example:

```
build/libs/CLILib-1.0-SNAPSHOT.jar
```

After the build, run the example with:

```bash
./gradlew runExample
```

### 📦 Publish to GitHub Packages
Define the environment variables `USERNAME` and `TOKEN` (the `TOKEN` can be your
`GITHUB_TOKEN`) and run:

```bash
./gradlew publish
```

## 📎 Example
```bash
var a 10 | var b 5 | add a b total | print Result: $total
```

## 📚 Documentation
Check the [project Wiki](https://github.com/RafaPear/CLILib/wiki) for details about the architecture, list of commands and extension guides.

## ⚙️ Configuration (Colors and ANSI)
You can override the default ANSI colors and control sequences via environment variables. If not set, CLILib falls back to standard ANSI codes.

- Colors:
  - CLILIB_COLOR_BLUE, CLILIB_COLOR_GREEN, CLILIB_COLOR_RED, CLILIB_COLOR_YELLOW, CLILIB_COLOR_CYAN, CLILIB_COLOR_GRAY, CLILIB_COLOR_ORANGE, CLILIB_COLOR_MAGENTA, CLILIB_COLOR_WHITE, CLILIB_COLOR_BLACK, CLILIB_COLOR_BOLD
- ANSI sequences:
  - CLILIB_ANSI_ESC, CLILIB_ANSI_SAVE_CURSOR, CLILIB_ANSI_RESTORE_CURSOR, CLILIB_ANSI_CURSOR_HOME, CLILIB_ANSI_HIDE_CURSOR, CLILIB_ANSI_SHOW_CURSOR, CLILIB_ANSI_CLEAR_SCREEN, CLILIB_ANSI_CLEAR_LINE_TO_END, CLILIB_ANSI_CLEAR_LINE_TO_START, CLILIB_ANSI_CLEAR_LINE, CLILIB_ANSI_DELETE_CHAR

Example (Windows PowerShell):

```powershell
$env:CLILIB_COLOR_GREEN = "`e[32m"
$env:CLILIB_ANSI_CLEAR_SCREEN = "`e[2J"
```

## 🤝 Contributions
Contributions are welcome! See [How to Contribute](https://github.com/RafaPear/CLILib/wiki/How-to-Contribute).

## 📄 License
Distributed under the [MIT License](https://opensource.org/licenses/MIT).

## ✨ Credits
Developed with ❤️ by Rafael Vermelho Pereira
