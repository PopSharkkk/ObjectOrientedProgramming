# Q060 — Tracing the listeners of a quote panel

**Paper.** Quiz 2 · **Marks.** 20 · **Chapter.** 10 · **Difficulty.** medium

> Complete `quote` and `refresh` in `java/QuotePanel.java` and `python/quote_panel.py`.
> The components, listeners, `--selftest` and window are given. Write parts (a) and (b)
> in `ANSWER.md`.

## The question

The quote panel has a weight field, a service combo box (WF Rp7,000/kg min 3 kg 48 h;
IR Rp5,000/kg min 2 kg 24 h; WDI Rp10,000/kg min 3 kg 72 h), an Express check box, a
Quote button and a result label. Chargeable weight = weight raised to the minimum, rounded
up to the next half kg; express costs ⌊3P/2⌋ and takes max(6, ⌊h/4⌋) hours. Four listeners:
the button and the field (Enter) re-quote and set `quoted`; the combo box and the check box
re-quote only if `quoted` is set. Every listener increments `handled`.

Starting with WF and the box unticked, the clerk: types 4.2; clicks Quote; ticks Express;
chooses WDI; types abc; presses Enter; unticks Express; chooses IR; types 0.4 and clicks
Quote. Give the label and `handled` after each step.

- (a) The trace. [8]
- (b) Step 5 leaves a label that contradicts the field — why, and what event fixes it? [3]
- (c) The Swing panel, with the rules in a separate Swing-free method, and a `--selftest`
  that performs (a) headless. [5]
- (d) The Tkinter version with a window-free controller. [4]

## TODO

| Where | What | Hint |
|:--|:--|:--|
| `quote` | the pure rules | parse, validate, ceil half-kg, express 3P/2 & max(6,h/4) |
| `refresh` | `handled++`; set `quoted` if explicit; re-quote only if `quoted` | |
| (a)(b) | written answers | `ANSWER.md` |

## Run

```bash
javac -d classes java/*.java && java -cp classes QuotePanel --selftest
python3 python/quote_panel.py --selftest
```

Expected first lines:

```
0. start             handled=0  label: (no quote yet)
1. type 4.2          handled=0  label: (no quote yet)
2. click Quote       handled=1  label: WF 4.5 kg: Rp31,500, ready in 48 h
3. tick Express      handled=2  label: WF 4.5 kg express: Rp47,250, ready in 12 h
4. choose WDI        handled=3  label: WDI 4.5 kg express: Rp67,500, ready in 18 h
```
