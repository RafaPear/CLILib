This section shows real examples of using CLILib by combining commands, variables and scripting. They demonstrate how to take full advantage of the library in common situations.

---

## 🔢 Mathematical calculations with variables

```bash
var a 10
var b 3
div a b result
print Result: $result
```

---

## 📊 Evaluate complex expressions

```bash
var x 5
var y 2
expr ($x + 10) * ($y - 1)
var total
print Calculated total: $total
```

---

## 📁 Automate project structure creation

```bash
mkdir src
mkdir bin
mkfile README.md
mkfile .gitignore
print Project structure created successfully.
```

---

## 📝 Script with a sequence of commands (`.ppc`)

Contents of `setup.ppc`:

```bash
var author "Rafael"
var project "CLI Tool"
print Project: $project | print Author: $author
mkdir src | mkdir tests
mkfile README.md
print Setup complete.
```

Run with:

```bash
loadscript setup.ppc
```

---

## 🧪 Measure operation performance

```bash
measure expr 100000 * 3000
```

---

## 🔁 Chain everything on one line

```bash
var x 10 | var y 5 | mult x y r | print Result: $r
```

---

## 📦 Create a custom command from a JSON file

```bash
mkcmdtemplate example.json
edit example.json   # add your own data
mkcmd example.json
example Hello
```

---

These examples show how CLILib can be used effectively — from simple tasks to more complex automation.
