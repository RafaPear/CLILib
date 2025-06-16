# 🧩 CLILib

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
import pt.clilib.App
fun main() {
    val app = App()
    // load all commands
    app.registerDefaultCommands()
    // or choose groups like utils and directory commands
    // app.registerDefaultCommands("--utils --dir")
    app.runtimeCLI()
}
```

You can also execute commands from a file:

```kotlin
app.runFromFile("Scripts/setup.ppc")
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

## 🤝 Contributions
Contributions are welcome! See [How to Contribute](https://github.com/RafaPear/CLILib/wiki/🤝-Como-Contribuir).

## 📄 License
Distributed under the [MIT License](https://opensource.org/licenses/MIT).

## ✨ Credits
Developed with ❤️ by Rafael Vermelho Pereira
