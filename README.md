# EF234302 Object-Oriented Programming (Class D)

[![Java 25](https://img.shields.io/badge/Java-25.x-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org)
[![Python 3.12](https://img.shields.io/badge/Python-3.12-3776AB?style=for-the-badge&logo=python&logoColor=white)](https://www.python.org)
[![License: MIT](https://img.shields.io/badge/License-MIT-3B82F6?style=for-the-badge)](LICENSE)

> **Course:** EF234302 Object-Oriented Programming (Class D) — Midterm Examination  
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
└── Midterm/
    ├── Q1/                  # Question 1: Weekly Load Statistics (15 marks)
    │   ├── java/WeeklyLoad.java
    │   └── python/weekly_load.py
    ├── Q2/                  # Question 2: Custom Checked Exception (20 marks)
    │   ├── java/OverloadException.java
    │   ├── java/Washer.java
    │   ├── java/LoadingBay.java
    │   └── python/loading_bay.py
    ├── Q3/                  # Question 3: Rounding Bugfix & Boundary Tests (20 marks)
    │   ├── java/Pricing.java
    │   ├── java/PricingCheck.java
    │   ├── python/pricing.py
    │   └── python/test_pricing.py
    ├── Q4/                  # Question 4: Dynamic Dispatch & Static Overloading (20 marks)
    │   ├── ANSWER.md
    │   ├── java/Dispatch.java
    │   └── python/dispatch.py
    └── Q5/                  # Question 5: Machine Room Scheduling Capstone (25 marks)
        ├── data/jobs.txt
        ├── jobs.txt
        ├── java/Machine.java
        ├── java/Washer.java
        ├── java/Dryer.java
        ├── java/ScheduleException.java
        ├── java/MachineRoom.java
        └── python/machine_room.py
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

## Academic Integrity
The submission includes the signed Academic Integrity Pledge declaration in accordance with the department's examination standards.
