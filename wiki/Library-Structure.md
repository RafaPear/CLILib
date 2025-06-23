CLILib is organized in a modular way to make the code easy to maintain, extend and understand. Below is an overview of the main components and their purpose.

## 🧠 Application core (`App.kt`)

Responsible for controlling the CLI lifecycle:

- `runtimeCLI()` – start the interactive CLI with a prompt and continuous input
- `runSingleCmd(cmd: String)` – execute a single command
- `runFromFile(file: String)` – execute commands from a `.ppc` file
- `registerDefaultCommands()` – register all built‑in commands via `CmdRegister`

---

## 🧩 Command registry (`CmdRegister.kt`)

Central manager of all commands available in the system:

- `register(cmd)` – register a command
- `unregister(cmd)` – remove a command
- `registerAll(list)` – register multiple commands
- `find(alias)` – search for a command by name/alias
- `all()` – return all registered commands

---

## 🔧 Command interface (`Command.kt`)

Defines how each command should be structured:

- `description`, `usage`, `aliases`, `run(args)` are required
- `minArgs`, `maxArgs`, `requiresFile`, etc. are optional
- All commands implement this interface

---

## 🧮 Variable system (`VarRegister.kt`)

Allows creation, modification and lookup of global variables:

- `register(name, value)` – create a variable
- `get(name)` – get a variable value
- `modify(name, value)` – change the value
- `unregister(name)` – remove a variable
- `all()` – return all variables

---

## 🧠 Expression parsing (`ExprParser.kt`)

Enables basic math expressions with operators `+ - * /` and parentheses.

Example:
```kotlin
val parser = ExprParser()
val result = parser.parse("3 + (2 * 5)")
```

## 🛠️ Utilities (`Utils.kt`, `Colors.kt`, `Global.kt`)

- `cmdParser(...)` – interpret and execute chained commands using `|`
- `replaceVars()` – substitute variables (`$a`) with their values
- `validateArgs(...)` – validate argument count and files
- `drawPrompt()`, `clearPrompt()` – manipulate the terminal
- `ProgressBar.kt` – visual progress bar component
- `Colors.kt` – ANSI color codes for the terminal
- `Global.kt` – global constants (e.g. `version`, `commentCode`, `root`)

---

## 📁 Usage example (`Example.kt`)

Demonstrates how to start the application and run scripts:

```kotlin
fun main() {
    val app = App()
    app.registerDefaultCommands()
    app.runFromFile("Scripts/exampleScript.ppc")
}
```
