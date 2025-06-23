This page gathers the main interfaces, objects and functions that make up the technical core of CLILib — useful if you want to extend or integrate the library with other systems.

---

## 🔧 `Command` interface

All commands must implement this interface:

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
    val commands: List<String>
        get() = emptyList()
    fun run(args: List<String>): Boolean
}
```

---

## 🧩 Command registry (`CmdRegister`)

```kotlin
CmdRegister.register(cmd)
CmdRegister.registerAll(listOf(cmd1, cmd2))
CmdRegister.unregister(cmd)
CmdRegister.all()
CmdRegister.find(alias)
```

---

## 🧠 Variable system (`VarRegister`)

```kotlin
VarRegister.register("name", value)
VarRegister.modify("name", newValue)
VarRegister.get("name")
VarRegister.all()
VarRegister.isRegistered("name")
VarRegister.unregister("name")
```

---

## 🧪 Command execution (`cmdParser`)

```kotlin
cmdParser("print Hello world")
cmdParser("var x 5 | print $x")
```

---

## ⚙️ Core utilities

- `clearAndRedrawPrompt()` — clear and redraw the prompt
- `clearPrompt()` — clear the terminal
- `drawPrompt()` — print the welcome message
- `replaceVars()`, `replaceArgs()` — replace variables and arguments
- `validateArgs(args, command)` — validate arguments and files
- `readJsonFile(path, callback)` — read `.json` files
- `ProgressBar(...)` — visual progress bar for long loops

---

## 🌍 Globals

```kotlin
val root: String     // Current directory
val version: String  // Application version
val commentCode = "//"
var lastCmdDump: Any? // Value returned by the last command
```

---

Use this reference as a quick guide for development and library extensions.
