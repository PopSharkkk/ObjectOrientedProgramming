# EF234302 Object-Oriented Programming (Class D)

[![Java 25](https://img.shields.io/badge/Java-25.x-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org)
[![Python 3.12](https://img.shields.io/badge/Python-3.12-3776AB?style=for-the-badge&logo=python&logoColor=white)](https://www.python.org)
[![License: MIT](https://img.shields.io/badge/License-MIT-3B82F6?style=for-the-badge)](LICENSE)

> **Course:** EF234302 Object-Oriented Programming (Class D) — Midterm Examination & Quiz 2  
> **Institution:** Department of Informatics, Faculty of Intelligent Electrical and Informatics Technology (FTEIC), Institut Teknologi Sepuluh Nopember (ITS), Surabaya  
> **Academic Year:** Semester Odd 2026/2027  
> **Instructor:** MM Irfan Subakti  
> **Teaching Assistants:** Aqila Zahira Naia Puteri Arifin, Felda Ega Fadhila  
> **Student:** Rinaltra Nabasa S (5025251024)

---

## Repository Structure

```text
ObjectOrientedProgramming/
├── README.md
├── .gitignore
├── Midterm/
│   ├── Q1/                  # Question 1: Weekly Load Statistics (15 marks)
│   │   ├── java/WeeklyLoad.java
│   │   └── python/weekly_load.py
│   ├── Q2/                  # Question 2: Custom Checked Exception (20 marks)
│   │   ├── java/OverloadException.java
│   │   ├── java/Washer.java
│   │   ├── java/LoadingBay.java
│   │   └── python/loading_bay.py
│   ├── Q3/                  # Question 3: Rounding Bugfix & Boundary Tests (20 marks)
│   │   ├── java/Pricing.java
│   │   ├── java/PricingCheck.java
│   │   ├── python/pricing.py
│   │   └── python/test_pricing.py
│   ├── Q4/                  # Question 4: Dynamic Dispatch & Static Overloading (20 marks)
│   │   ├── ANSWER.md
│   │   ├── java/Dispatch.java
│   │   └── python/dispatch.py
│   └── Q5/                  # Question 5: Machine Room Scheduling Capstone (25 marks)
│       ├── data/jobs.txt
│       ├── jobs.txt
│       ├── java/Machine.java
│       ├── java/Washer.java
│       ├── java/Dryer.java
│       ├── java/ScheduleException.java
│       ├── java/MachineRoom.java
│       └── python/machine_room.py
└── Quiz2/
    ├── Q051/                # Question 1: Orders per Service — Map Tally (15 marks, template)
    │   ├── ANSWER.md
    │   ├── java/ServiceTally.java
    │   └── python/service_tally.py
    ├── Q053/                # Question 2: Generic Multiset Bag<T> (20 marks, template)
    │   ├── java/BagDemo.java
    │   └── python/bag_demo.py
    ├── Q054/                # Question 4: Wildcards & Variance at the Sorting Desk (20 marks, template)
    │   ├── ANSWER.md
    │   ├── java/WildcardDesk.java
    │   └── python/wildcard_desk.py
    ├── Q060/                # Question 3: Tracing Listeners of a Quote Panel (20 marks)
    │   ├── ANSWER.md
    │   ├── java/QuotePanel.java
    │   └── python/quote_panel.py
    └── Q075/                # Question 5: Filtered, Sorted Order List View-Model Capstone (25 marks)
        ├── ANSWER.md
        ├── java/OrderListViewModel.java
        └── python/order_list_view_model.py
```

---

## Midterm Problem Summary & Verification

### Q1 — Weekly Load Statistics (15 Marks)
Implements four fundamental array helper functions in both Java and Python:
- `total(a)`: Calculates the sum of all elements.
- `average(a)`: Calculates `total(a) / len(a)`.
- `indexOfMax(a)` / `index_of_max(a)`: Identifies the first index of the maximum value (preserving ties).
- `countAbove(a, limit)` / `count_above(a, limit)`: Counts elements strictly greater than the given threshold.

**Execution:**
```bash
# Java
javac -d classes Midterm/Q1/java/*.java && java -cp classes WeeklyLoad

# Python
python Midterm/Q1/python/weekly_load.py
```
**Expected Output:**
```text
total 347.0 kg, average 49.57 kg
busiest day: Sat (72.0 kg)
days above average: 3
```

---

### Q2 — Custom Checked Exception: Washer Overload (20 Marks)
Demonstrates object-oriented error handling and checked exception patterns:
- `OverloadException` (Java) / `OverloadError` (Python): Custom checked exception storing machine ID, requested kg, capacity kg, and excess kg calculation.
- `Washer.load(kg)`: Enforces domain validation by throwing `IllegalArgumentException` / `ValueError` for non-positive weights, and `OverloadException` when weight exceeds capacity.
- `LoadingBay.place(washers, kg)`: Sequentially searches available machines, catching overload exceptions until a bag is accepted or returning `null`.

**Execution:**
```bash
# Java
javac -d classes Midterm/Q2/java/*.java && java -cp classes LoadingBay

# Python
python Midterm/Q2/python/loading_bay.py
```

---

### Q3 — Price Rounding & Boundary Equivalence (20 Marks)
Fixes a subtle rounding defect where `Math.round(x * 2) / 2.0` improperly rounded to the nearest half-kg instead of rounding **up** to the next half-kg:
- **Correction:** Implemented `Math.ceil(Math.max(kg, MINIMUM_KG) * 2) / 2.0` in Java and `math.ceil(max(kg, MINIMUM_KG) * 2) / 2` in Python.
- Validated against 10 comprehensive boundary condition test cases (minimum thresholds, half-kg boundaries, member discounts, express multipliers, and negative/zero validation).

**Execution:**
```bash
# Java
javac -d classes Midterm/Q3/java/*.java && java -cp classes PricingCheck

# Python
python Midterm/Q3/python/test_pricing.py
```
**Result:** 10 passed, 0 failed (all boundary tests green).

---

### Q4 — Dynamic Dispatch, Static Overloading & Type Casting (20 Marks)
Detailed conceptual analysis and output prediction in [Midterm/Q4/ANSWER.md](Midterm/Q4/ANSWER.md):
1. **Predicted Output:** Complete line-by-line runtime trace for Java and Python.
2. **Static Overloading vs. Dynamic Dispatch:** Contrasts compile-time early binding of static overloaded methods (`kind(Machine)` based on declared variable type) with runtime late binding of polymorphic instance methods (`m.name()` / `m.minutes()` via `invokevirtual` vtable dispatch).
3. **The Bad Cast:** Contrasts Java's static type safety check resulting in runtime `ClassCastException` against Python's dynamic duck-typing lookup resulting in `AttributeError`.

---

### Q5 — Machine Room Scheduling Capstone (25 Marks)
A complete object-oriented system scheduling laundry jobs across heterogeneous machines:
- **`Machine` (Abstract Base Class):** Enforces template method pattern in `schedule(start, kg)` (validates load constraints, busy state, accumulates statistics). Implements `Comparable` and value equality based on machine ID.
- **`Washer`:** Calculates cycle duration as `30 + Math.ceil(kg) * 3`.
- **`Dryer`:** Calculates cycle duration as `50` minutes for `kg <= 5`, and `70` minutes otherwise.
- **`MachineRoom`:** Parses input stream, rejects duplicate machines on installation, dispatches incoming jobs, and outputs formatted chronological telemetry.

**Execution:**
```bash
# Java
cd Midterm/Q5 && javac -d classes java/*.java && java -cp classes MachineRoom

# Python
cd Midterm/Q5 && python python/machine_room.py
```

---

## Quiz 2 Problem Summary & Verification

Quiz 2 covers Chapters 9–11 (Collections & Generics, Event-Driven UI Listeners, and MVC/MVVM architecture). Currently completed: **Q060** and **Q075** (`Q051`, `Q053`, and `Q054` remain as starter templates).

### Q060 — Tracing the Listeners of a Quote Panel (20 Marks)
Demonstrates event-driven GUI architecture with pure, UI-free pricing rules separated from Swing (`QuotePanel.View`) and Tkinter (`QuoteController`):
- **`quote(...)`:** Parses and validates weight in `(0, 50]`, applies each service's minimum weight (`WF` 3 kg, `IR` 2 kg, `WDI` 3 kg), rounds chargeable weight up to the next half-kilogram, and applies Express pricing (`⌊3P/2⌋`) and turnaround (`max(6, ⌊h/4⌋)` hours).
- **`refresh(explicit)`:** Increments `handled` on every listener event; sets `quoted` on explicit requests (*Quote* button or Enter) and re-quotes on combo box / Express checkbox changes only once `quoted` is set.
- **Written Analysis ([Quiz2/Q060/ANSWER.md](Quiz2/Q060/ANSWER.md)):** Full step-by-step trace (steps 0–9) plus explanation of why typing `abc` at step 5 leaves a stale label (text edits do not fire `ActionEvent`) and how to fix it via Swing's `DocumentListener` (`DocumentEvent`) or Tkinter's `StringVar.trace_add("write", ...)`.

**Execution:**
```bash
# Java (headless self-test)
cd Quiz2/Q060 && javac -d classes java/*.java && java -cp classes QuotePanel --selftest

# Python (window-free controller self-test)
cd Quiz2/Q060 && python python/quote_panel.py --selftest
```
**Expected Output:**
```text
0. start             handled=0  label: (no quote yet)
1. type 4.2          handled=0  label: (no quote yet)
2. click Quote       handled=1  label: WF 4.5 kg: Rp31,500, ready in 48 h
3. tick Express      handled=2  label: WF 4.5 kg express: Rp47,250, ready in 12 h
4. choose WDI        handled=3  label: WDI 4.5 kg express: Rp67,500, ready in 18 h
5. type abc          handled=3  label: WDI 4.5 kg express: Rp67,500, ready in 18 h
6. press Enter       handled=4  label: invalid weight: enter a number
7. untick Express    handled=5  label: invalid weight: enter a number
8. choose IR         handled=6  label: invalid weight: enter a number
9. type 0.4, click   handled=7  label: IR 2.0 kg: Rp10,000, ready in 24 h
```

---

### Q075 — Filtered, Sorted Order List View-Model Capstone (25 Marks)
Integrates Chapters 9–11 via an MVVM `OrderListViewModel` sitting between `OrderBook` and the list widget:
- **Comparator Chains & Sort Keys:** `BY_READY` (`readyHour`, then `id`) and `BY_CUSTOMER` (case-insensitive customer name, then `id`) in Java (`Comparator.comparingInt` / `Comparator.comparing` + `thenComparing`), mirrored by tuple keys in Python.
- **Change-Driven Recompute:** Filters by case-insensitive customer substring and optional `Status`, sorts, formats each row (`<id> <customer> <service> <status> <HH>:00`), and notifies the view callback (`Consumer<List<String>>` / callable) **only when the computed rows actually change** (`!rows.equals(shown)` / `rows != self.shown`).
- **Written Analysis ([Quiz2/Q075/ANSWER.md](Quiz2/Q075/ANSWER.md)):** Complete 10-event trace, rationale for row diffing and headless testability, and breakdown of Chapter 9 collection/generics patterns used.

**Execution:**
```bash
# Java
cd Quiz2/Q075 && javac -d classes java/*.java && java -cp classes OrderListViewModel

# Python
cd Quiz2/Q075 && python python/order_list_view_model.py
```
**Expected Output:**
```text
0. attach -> [A2 Budi WDI READY 11:00, A4 Dewi WF READY 12:00, A5 Rahmat WDI WASHING 13:00, A1 Sari WF WASHING 14:00, A6 Citra WF DRYING 15:00, A3 Ayu IR RECEIVED 16:00]
1. filter 'ra' -> [A5 Rahmat WDI WASHING 13:00, A6 Citra WF DRYING 15:00]
2. sort by customer -> [A6 Citra WF DRYING 15:00, A5 Rahmat WDI WASHING 13:00]
3. add A7 Budi -> (no update)
4. advance A5 -> [A6 Citra WF DRYING 15:00, A5 Rahmat WDI DRYING 13:00]
5. filter 'RA' -> (no update)
6. only DRYING -> (no update)
7. advance A6 -> [A5 Rahmat WDI DRYING 13:00]
8. filter '' -> (no update)
9. only READY -> [A2 Budi WDI READY 11:00, A6 Citra WF READY 15:00, A4 Dewi WF READY 12:00]
view updates: 6 for 10 events
```

---

## Academic Integrity
The submission includes the signed Academic Integrity Pledge declaration in accordance with the department's examination standards.
