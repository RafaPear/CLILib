One of CLILib's strengths is how easy it is to create your own commands. Implement the `Command` interface with the required fields and the behaviour you want.

---

## ✏️ Basic command example

```kotlin
object HelloCmd : Command {
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

Then simply register the command:

```kotlin
CmdRegister.register(HelloCmd)
```

---

## 📌 Required fields

- `description` – short description
- `usage` – how to use the command (shown in `help`)
- `aliases` – names the command can be called by
- `run(args: List<String>)` – execution logic

---

## ⚙️ Useful optional fields

- `minArgs` / `maxArgs` — expected number of arguments
- `requiresFile` / `fileExtension` — validate file paths
- `commands` — define subcommands or flags (e.g. `-h`, `--list`)
- `longDescription` — longer description shown with `help <command>`

---

## 🔁 Dynamic commands with JSON

You can also create simple commands from a `.json` file using `mkcmd`.

```bash
mkcmd example.json
```

Example `example.json`:

```json
{
  "description": "Example command via JSON",
  "longDescription": "This command was created from a JSON file.",
  "usage": "exjson <arg>",
  "aliases": ["exjson"],
  "minArgs": 1,
  "maxArgs": 1,
  "requiresFile": false,
  "fileExtension": "",
  "run": "print arg[0]"
}
```

---

## ✅ Best practices

- Call `validateArgs(args, this)` at the start of `run`
- Store temporary results in `lastCmdDump` (so they can be used with `var`)
- Use `println` sparingly (only for useful messages)
- Give aliases clear and short names

---

With these tools you can extend CLILib to support any functionality you need — from system commands to external API integrations.
