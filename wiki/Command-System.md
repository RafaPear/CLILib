CLILib was designed with a modular and extensible command system. Each command is an object implementing the `Command` interface which allows you to:

- Define aliases and the number of expected arguments
- Execute custom logic when the command runs
- Validate arguments as files with optional extensions

---

## 📦 `Command` interface

All commands must implement the `Command` interface:

```kotlin
interface Command {
    val description: String
    val longDescription: String
        get() = description
    val usage: String
    val aliases: List<String>
    val minArgs: Int
        get() = 0
    val maxArgs: Int
        get() = 0
    val requiresFile: Boolean
        get() = false
    val fileExtension: String
        get() = ""
    fun run(args: List<String>): Boolean
}
```

- **aliases** – alternative names (e.g. `print`, `p`, `echo`)
- **minArgs**, **maxArgs** – required/optional argument count
- **requiresFile**, **fileExtension** – validate file arguments
- **run(...)** – executed when the command is called

---

## 🧩 Registering commands

A command becomes available once registered through `CmdRegister`:

```kotlin
CmdRegister.register(MyCustomCommand)
```

Or in batch:

```kotlin
CmdRegister.registerAll(listOf(Cmd1, Cmd2, Cmd3))
```

`App.registerDefaultCommands()` already registers all built‑in commands:

```kotlin
val app = App()
app.registerDefaultCommands()
```

## 🔎 Command execution

User input is parsed by `cmdParser(...)` which:

* Splits commands using `|`
* Replaces variables and arguments (`$var`, `arg[0]`, etc.)
* Looks up the command via `CmdRegister.find(...)`
* Runs the corresponding `run(args)`

## ✏️ Creating a custom command

```kotlin
object MyHelloCmd : Command {
    override val description = "Say hello to the user"
    override val usage = "hello <name>"
    override val aliases = listOf("hello", "hi")
    override val minArgs = 1
    override val maxArgs = 1

    override fun run(args: List<String>): Boolean {
        println("Hello, ${'$'}{args[0]}!")
        return true
    }
}
```

Then register it:

```kotlin
CmdRegister.register(MyHelloCmd)
```

## 📚 Built-in commands

CLILib ships with several ready to use commands covering CLI utilities, directory management, file handling, scripting, variables and flow control. These commands are automatically registered when using the default command set.

---

### 🖥️ Interface commands

- `clr` — clear the terminal
- `exit`, `e` — exit the application
- `help`, `h` — show all commands with descriptions
- `version` — display project version and credits
- `print` — output text to the terminal
- `wait`, `w` — pause execution for a number of milliseconds
- `waitforuser`, `wfu` — wait for the user to press Enter
- `mkcmd` — create simple commands from `.json` files
- `mkcmdtemplate`, `mkcmdtpl` — generate a JSON template for custom commands

---

### 📂 Directory and file commands

- `cd <dir>` — change current directory
- `ls [dir]` — list files/folders in a directory
- `mkdir <name>` — create a new directory
- `deldir`, `rmdir <dir>` — delete a directory and its contents
- `mkfile <name>` — create an empty file with the given name
- `delfile`, `rmfile <file>` — remove a file
- `edit <file>` — open a file in the system default editor

---

### 📜 Scripting and measurement commands

- `loadscript`, `lscript <file.ppc>` — execute a `.ppc` script line by line
- `measure`, `m`, `time <command>` — measure command execution time

---

### 🧠 Variable commands

- `var <name> [value|options]` — create, modify, list or delete variables
  - Provide `<value>` to set the variable
  - Without `<value>` the result from the last command (`lastCmdDump`) is used
  - Options:
    - `-l`, `--list` — list all variables
    - `-d`, `--delete` — delete a variable
    - `-h`, `--help` — show help for the command
- `add <var1> <var2> [result]` — add two variables and store the result
- `sub <var1> <var2> [result]` — subtract one variable from another
- `mult <var1> <var2> [result]` — multiply two variables
- `div <var1> <var2> [result]` — divide one variable by another
- `expr <expression>` — evaluate a math expression and store the result

---

### 🔄 Flow control commands

- `while <condition> {commands}` — repeat commands while the condition is true
- `if <condition> {commands}` — run commands only when the condition is true
- `fun <name> {commands}` — define a reusable function

---

More commands will be added as the project evolves.
