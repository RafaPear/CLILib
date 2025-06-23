CLILib supports scripting and global variables, allowing you to automate tasks, create reusable command sequences and share values between commands.

---

## 📜 `.ppc` scripts

A script is simply a text file with commands line by line. Script files must use the `.ppc` extension.

### 📥 Loading a script

```bash
loadscript example.ppc
```

Or chained:

```bash
var name example.ppc | loadscript $name
```

### 📌 Example `example.ppc`

```bash
var a 10
var b 5
add a b sum
print The sum of $a + $b is: $sum
```

---

## 🧠 Variables

Variables are global and can be created, changed and used by any command.

### Create or modify

```bash
var x 20
var name Rafael
```

If no value is provided, CLILib uses the last result (`lastCmdDump`):

```bash
expr 4 * 5
var result
```

### Use variables

```bash
print The value of x is $x
```

### List or delete

```bash
var -l        # List all variables
var -d name   # Remove the variable "name"
```

---

## ➕ Operations between variables

- `add a b result` – add
- `sub a b result` – subtract
- `mult a b result` – multiply
- `div a b result` – divide
- `expr <expression>` – evaluate expressions with parentheses and operators

### Example:

```bash
var a 8
var b 2
div a b res
print Result: $res
```

---

## 🔗 Command chaining

Use `|` to chain commands:

```bash
var a 5 | var b 3 | add a b total | print Total: $total
```

Each command in the chain runs sequentially.

---

## 🧪 Tip: using expressions

```bash
var x 10
var y 2
expr x * (y + 3)
var result
print Expression result: $result
```

---

This system makes CLILib powerful for automating tasks, calculating values and creating reusable scripts.
