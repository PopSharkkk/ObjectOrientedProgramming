# quote_panel.py - Q060: a price-quote panel; the controller is traced event by event without a window.
import math
import sys

SERVICES = {  # code: (label, rate, minimum kg, hours)
    "WF": ("Wash & Fold", 7000, 3.0, 48),
    "IR": ("Iron Only", 5000, 2.0, 24),
    "WDI": ("Wash, Dry & Iron", 10000, 3.0, 72),
}


def quote(kg_text, code, express):
    """The model: pure rules, no Tkinter."""
    try:
        kg = float((kg_text or "").strip())
    except ValueError:
        return "invalid weight: enter a number"
    if not math.isfinite(kg):
        return "invalid weight: enter a number"
    if kg <= 0 or kg > 50:
        return "weight must be > 0 and <= 50 kg"
    _, rate, min_kg, hours = SERVICES[code]
    half_kg = math.ceil(2 * max(kg, min_kg))
    price = rate * half_kg // 2
    if express:
        price = 3 * price // 2
        hours = max(6, hours // 4)
    tag = " express" if express else ""
    return f"{code} {half_kg / 2:.1f} kg{tag}: Rp{price:,}, ready in {hours} h"


class QuoteController:
    """Holds what the widgets hold and reacts to their events; the window only forwards events."""

    def __init__(self):
        self.kg_text = ""
        self.code = "WF"
        self.express = False
        self.label = "(no quote yet)"
        self.quoted = False
        self.handled = 0

    def refresh(self, explicit):
        self.handled += 1
        if explicit:
            self.quoted = True
        if self.quoted:
            self.label = quote(self.kg_text, self.code, self.express)

    # one method per kind of event
    def on_quote(self):
        self.refresh(True)

    def on_enter(self):
        self.refresh(True)

    def on_service(self, code):
        self.code = code
        self.refresh(False)

    def on_express(self, ticked):
        self.express = ticked
        self.refresh(False)


def run_window():
    import tkinter as tk                     # imported only when a window is wanted
    from tkinter import ttk
    c = QuoteController()
    root = tk.Tk()
    root.title("Profitina Laundry - quote")
    kg = tk.StringVar()
    code = tk.StringVar(value="WF")
    express = tk.BooleanVar()
    label = tk.StringVar(value=c.label)

    def forward(handler, *args):
        c.kg_text = kg.get()                 # copy the field into the controller, then let it decide
        handler(*args)
        label.set(c.label)

    entry = tk.Entry(root, textvariable=kg, width=6)
    entry.grid(row=0, column=1)
    entry.bind("<Return>", lambda e: forward(c.on_enter))
    tk.Label(root, text="Service").grid(row=1, column=0)
    combo = ttk.Combobox(root, textvariable=code, values=list(SERVICES), state="readonly")
    combo.grid(row=1, column=1)
    combo.bind("<<ComboboxSelected>>", lambda e: forward(c.on_service, code.get()))
    tk.Checkbutton(root, text="Express", variable=express,
                   command=lambda: forward(c.on_express, express.get())).grid(row=2, column=0)
    tk.Button(root, text="Quote", command=lambda: forward(c.on_quote)).grid(row=2, column=1)
    tk.Label(root, textvariable=label).grid(row=3, column=0, columnspan=2)
    root.mainloop()


def self_test():
    c = QuoteController()

    def step(what, action):
        action()
        print(f"{what:<20} handled={c.handled}  label: {c.label}")

    step("0. start", lambda: None)
    step("1. type 4.2", lambda: setattr(c, "kg_text", "4.2"))
    step("2. click Quote", c.on_quote)
    step("3. tick Express", lambda: c.on_express(True))
    step("4. choose WDI", lambda: c.on_service("WDI"))
    step("5. type abc", lambda: setattr(c, "kg_text", "abc"))
    step("6. press Enter", c.on_enter)
    step("7. untick Express", lambda: c.on_express(False))
    step("8. choose IR", lambda: c.on_service("IR"))
    step("9. type 0.4, click", lambda: (setattr(c, "kg_text", "0.4"), c.on_quote()))


if __name__ == "__main__":
    if "--selftest" in sys.argv:
        self_test()
    else:
        run_window()
