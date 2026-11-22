# Q054 — Which calls compile? Wildcards at the sorting desk

**Paper.** Quiz 2 · **Marks.** 20 · **Chapter.** 9 · **Difficulty.** hard

> Complete the three methods in `java/WildcardDesk.java` and the functions in
> `python/wildcard_desk.py`. The hierarchy and `main` are given — leave them.
> Write the compile-verdicts and the rule in `ANSWER.md`.

## The question

The sorting desk models garments with `Garment ← Shirt`, `Garment ← Towel ← BathTowel`,
and three helpers:

```java
static double totalKg(List<? extends Garment> items)
static void addSpareTowels(List<? super Towel> dst, int n)
static <T> int transfer(Collection<? extends T> src, Collection<? super T> dst)
```

Given `List<Garment> basket; List<Shirt> shirts; List<Towel> towels;
List<BathTowel> bathTowels; List<Object> anything;` and
`List<? extends Garment> view = towels;`, say for each line whether it compiles, and why:
`totalKg(shirts)`, `totalKg(anything)`, `addSpareTowels(basket, 2)`,
`addSpareTowels(bathTowels, 1)`, `transfer(bathTowels, towels)`, `transfer(towels, shirts)`,
`List<Garment> g = shirts;`, `view.add(new Shirt("x"));`.

- (a) The verdicts for the eight lines. [8]
- (b) Implement the three methods and a `main` that runs every compilable line. [6]
- (c) State the rule that decides between `extends` and `super`. [2]
- (d) Python counterpart with type hints: which calls does `mypy` reject, and how to
  express `? super Towel` in Python. [4]

## TODO

| Where | What | Hint |
|:--|:--|:--|
| `totalKg` | read-only → `? extends` | sum `g.grams`, divide by 1000 |
| `addSpareTowels` | write-only → `? super` | `dst.add(new Towel(...))` |
| `transfer` | read + write → type parameter `T` | `? extends T` source, `? super T` destination, then `src.clear()` |
| (a)(c)(d) | written answers | `ANSWER.md` |

## Run

```bash
javac -d classes java/*.java && java -cp classes WildcardDesk
python3 python/wildcard_desk.py
```

Expected (Java):

```
(1) totalKg(shirts)          = 0.5
(3) basket after spares      = [spare-1, spare-2], anything = [spare-1]
(5) transfer(bath -> towels) = 2 moved, towels = [hand, bath-1, bath-2], bathTowels = []
    transfer(shirts -> basket) = 2 moved, basket = [spare-1, spare-2, batik, polo]
(8) view.get(0) = hand, totalKg(view) = 2.4
    after view.clear(): towels = []
```
