# 🧩 CLILib

**CLILib** é uma biblioteca Kotlin para criação de interfaces de linha de comandos (CLI) extensíveis e poderosas. Inclui suporte para scripting, variáveis, execução encadeada, criação de comandos personalizados e muito mais — tudo pronto a usar.

---

## 🚀 Funcionalidades principais

- CLI modular com registo dinâmico de comandos  
- Sistema de scripting `.ppc`  
- Variáveis globais e operações entre variáveis  
- Execução de comandos com `|` (encadeamento)  
- Avaliação de expressões matemáticas  
- Criação de comandos via JSON  
- Comandos prontos como `print`, `var`, `cd`, `ls`, `loadscript`, etc.

---

## 🛠️ Começar

```kotlin
fun main() {
    val app = App()
    app.registerDefaultCommands()
    app.runtimeCLI()
}

```

Podes também executar um ficheiro:

```kotlin
app.runFromFile("Scripts/setup.ppc")

```

---

## 📎 Exemplo de uso

```bash
var a 10 | var b 5 | add a b total | print Resultado: $total

```

---

## 📚 Documentação

Consulta a [📘 Wiki do projeto](https://github.com/RafaPear/CLILib/wiki) para:

- Introdução e estrutura do projeto  
- Lista completa de comandos  
- Guia de scripting e variáveis  
- Como criar os teus próprios comandos

---

## 🤝 Contribuir

Aceitamos contributos! Consulta a secção  
[🤝 Como Contribuir](https://github.com/RafaPear/CLILib/wiki/🤝-Como-Contribuir)

---

## 📄 Licença

Este projeto está licenciado sob a [MIT License](https://opensource.org/licenses/MIT).

---

## ✨ Créditos

Desenvolvido com ❤️ por Rafael Vermelho Pereira
