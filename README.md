# GE Transaction Writer

A minimal, passive RuneLite plugin that logs all Grand Exchange transactions to a local JSON file.

Each event (BUY, SELL, CANCEL, EMPTY_SLOT) is recorded with full detail and timestamp.
The output can be used for tracking flips, analyzing market activity, or feeding personal tools.

---

### 📦 Output

- Format: newline-delimited JSON
- Location: `~/.runelite/getransactionwriter/transactions-YYYY-MM-DD.json`
- Fields: item name, quantity, price, timestamp, slot, state

---

### Roadmap

This plugin is a minimal, standalone logging tool. Future updates may include:

- Config panel to toggle fields and file output modes
- Optional session ID tagging
- Sidebar tooltip showing current log file and event count
- Additional GE event types (e.g. “Collect All” detection)

All features will remain local-only and Plugin Hub–compliant.

---

### Contact

Maintainer: `Mreedon` on Discord
