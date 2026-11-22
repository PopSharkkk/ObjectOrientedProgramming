# Q060 — Your answer

## (a) The trace

Initial state: weight field `""`, service `WF`, Express unticked (`false`), `quoted = false`, `handled = 0`, label `(no quote yet)`.

| Step | Action | Event / Listener triggered | `quoted` | `handled` | Result label |
|---:|---|---|:---:|---:|---|
| 0 | Start | *(initial state)* | `false` | 0 | `(no quote yet)` |
| 1 | Type `4.2` | None (typing in a text field does not fire `ActionEvent`) | `false` | 0 | `(no quote yet)` |
| 2 | Click *Quote* | Button `ActionListener` -> `refresh(true)` | `true` | 1 | `WF 4.5 kg: Rp31,500, ready in 48 h` |
| 3 | Tick *Express* | Check box `ItemListener` (`SELECTED`) -> `refresh(false)` | `true` | 2 | `WF 4.5 kg express: Rp47,250, ready in 12 h` |
| 4 | Choose `WDI` | Combo box `ActionListener` -> `refresh(false)` | `true` | 3 | `WDI 4.5 kg express: Rp67,500, ready in 18 h` |
| 5 | Type `abc` | None (typing in a text field does not fire `ActionEvent`) | `true` | 3 | `WDI 4.5 kg express: Rp67,500, ready in 18 h` *(stale)* |
| 6 | Press Enter | Text field `ActionListener` -> `refresh(true)` | `true` | 4 | `invalid weight: enter a number` |
| 7 | Untick *Express* | Check box `ItemListener` (`DESELECTED`) -> `refresh(false)` | `true` | 5 | `invalid weight: enter a number` |
| 8 | Choose `IR` | Combo box `ActionListener` -> `refresh(false)` | `true` | 6 | `invalid weight: enter a number` |
| 9 | Type `0.4` and click *Quote* | Typing fires none; button `ActionListener` -> `refresh(true)` | `true` | 7 | `IR 2.0 kg: Rp10,000, ready in 24 h` |

### Calculation notes

- **Step 2 (`4.2`, `WF`, normal):** `WF` is Rp7,000/kg, min 3.0 kg, 48 h. Chargeable weight = `ceil(2 × max(4.2, 3.0)) / 2` = `⌈8.4⌉ / 2` = `9 / 2` = `4.5 kg`. Price = `7,000 × 9 / 2` = `Rp31,500`; time = `48 h`.
- **Step 3 (`4.2`, `WF`, express):** Express price = `⌊3 × 31,500 / 2⌋` = `Rp47,250`; express time = `max(6, ⌊48 / 4⌋)` = `12 h`.
- **Step 4 (`4.2`, `WDI`, express):** `WDI` is Rp10,000/kg, min 3.0 kg, 72 h. Base price for `4.5 kg` = `10,000 × 9 / 2` = `Rp45,000`; express price = `⌊3 × 45,000 / 2⌋` = `Rp67,500`; express time = `max(6, ⌊72 / 4⌋)` = `18 h`.
- **Steps 6–8 (`abc`):** `"abc"` fails numeric parsing, so `quote(...)` returns the parse error text. Because `quoted` remains `true` once set, unticking Express (step 7) and choosing IR (step 8) still increment `handled` and re-run `quote("abc", ...)`, keeping the error message on the label.
- **Step 9 (`0.4`, `IR`, normal):** `IR` is Rp5,000/kg, min 2.0 kg, 24 h, and Express was unticked in step 7. `0.4` is in `(0, 50]` so it is valid and raised to the 2.0 kg minimum: `⌈2 × max(0.4, 2.0)⌉ / 2` = `4 / 2` = `2.0 kg`. Price = `5,000 × 4 / 2` = `Rp10,000`; time = `24 h`.

## (b) The stale label

### Why step 5 leaves a label that contradicts the field

At step 4, the label shows a valid quote (`WDI 4.5 kg express: Rp67,500, ready in 18 h`) computed from `"4.2"`. When the clerk types `"abc"` in step 5, **editing text inside a `JTextField` (or Tkinter `Entry`) does not fire an `ActionEvent` (or `<Return>`)** — a text field's `ActionListener` fires only when the user presses **Enter**. Because no listener is attached to text edits, no event is delivered (`handled` stays `3`), `refresh` is not called, and the label remains stuck on the old quote for `4.5 kg` while the input field shows `"abc"`.

### What event to listen to in order to fix it

- **In Swing (Java):** Register a **`DocumentListener`** on the text field's underlying `Document` (`kg.getDocument().addDocumentListener(...)`), which receives **`DocumentEvent`** notifications (`insertUpdate`, `removeUpdate`, `changedUpdate`) whenever the text changes — including typing, deleting, copy/paste, and programmatic `setText(...)`. (`DocumentListener` is preferred over `KeyListener`, which misses clipboard/programmatic edits and fires before the field text updates on `keyPressed`.)
- **In Tkinter (Python):** Attach a write trace to the `Entry`'s `StringVar` using **`kg.trace_add("write", ...)`**, which invokes a callback whenever the variable's text changes, and forward that change to the controller.

Inside that listener, you resolve the stale display in one of two ways:
1. **Live re-quote:** If `quoted` is `true`, immediately call `refresh(false)` so the label updates to the new quote or error message as the user types.
2. **Invalidate the stale quote:** Clear/reset the label (e.g., to `"(no quote yet)"` or `"(edited — press Quote)"`) and set `quoted = false` until the user clicks *Quote* or presses *Enter* again.
