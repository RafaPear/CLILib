CLILib is an evolving project open to improvements and new features. This page explains how you can contribute effectively — with code, ideas, documentation or tests.

---

## 🛠️ Possible contributions

- 💡 **Suggest new features** (useful commands, UX improvements, etc.)
- 🧩 **Create custom commands** and share them with the community
- 🐛 **Report bugs** and unexpected behavior
- 📚 **Improve the documentation** with examples, tutorials or fixes
- 🧪 **Write test scripts** to validate CLI behaviour
- 🔍 **Review existing code** or optimise logic

---

## 🚀 Getting started

1. **Fork** the repository
2. Create a new branch for your feature:

```bash
git checkout -b feature/your-feature-name
```

3. Make your changes locally
4. Test your commands and scripts
5. Commit and push to your fork:

```bash
git add .
git commit -m "Add new command: hello"
git push origin feature/your-feature-name
```

6. Open a **Pull Request** with a brief explanation

---

## 📋 Best practices

- Keep the code modular — implement the `Command` interface for new commands
- Use `validateArgs(...)` inside your commands
- Provide clear and helpful error messages
- Use descriptive aliases and avoid confusing abbreviations
- Add comments to the code when necessary

---

## 📁 Recommended organization

- CLI commands → `cmdUtils.commands.cli`
- File commands → `cmdUtils.commands.file`
- Directory commands → `cmdUtils.commands.directory`
- Variable operations → `cmdUtils.commands.varOp`
- Misc commands → `cmdUtils.commands.functions`

---

## 💬 Questions or suggestions?

If you have questions or want to discuss ideas for the project, feel free to open an **issue** or contact the authors listed in the credits.

---

Thank you for helping make CLILib better! 🚀
