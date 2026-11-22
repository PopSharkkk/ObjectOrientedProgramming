# Q053 — A generic bag for the sorting table

**Paper.** Quiz 2 · **Marks.** 20 · **Chapter.** 9 · **Difficulty.** medium

> Complete the `Bag<T>` methods in `java/BagDemo.java` and `python/bag_demo.py`.
> The `main` driver is given — leave it.

## The question

At the sorting table, staff count what comes out of each bag: three shirts, four towels,
three socks, and so on. The lost-and-found box counts sock sizes the same way. Write one
generic class `Bag<T>`, a multiset, that serves both.

- (a) Provide `add(item)`, `add(item, n)` for `n>0`, `count(item)`, `remove(item)` (one copy,
  reporting whether there was one), `size()` (all copies) and `distinct()`. Choose the
  underlying collection and justify it. [8]
- (b) Add `mostCommon()` — the largest count, first added on a tie — returning nothing for
  an empty bag without using `null`. [4]
- (c) Add `asMap()` (a read-only view) and a static generic `union(a, b)` that combines two
  bags whose element types may be different subtypes of a common type. [5]
- (d) Explain what the compiler does with `Bag<String> g = new Bag<>(); g.add(42);`, and
  what Python does with the same mistake. [3]

## TODO

| Where | What | Hint |
|:--|:--|:--|
| `add(item, n)` | reject `n<=0`, `merge`, `size += n` | `Integer::sum` / `get(item, 0) + n` |
| `count` | `getOrDefault` / `get` with default 0 | `Object` parameter, not `T` |
| `remove` | absent → false; count 1 → remove key; else decrement | keep `distinct()` == `counts.size()` |
| `distinct` | `counts.size()` / `len(counts)` | |
| `mostCommon` | first strictly-larger in insertion order; `Optional` / `None` | `>` not `>=` |
| `asMap` | `Collections.unmodifiableMap` / `MappingProxyType` | a view, not a copy |
| `union` | generic method with wildcards | `static <T> Bag<T> union(Bag<? extends T> a, Bag<? extends T> b)` |

## Run

```bash
javac -d classes java/*.java && java -cp classes BagDemo
python3 python/bag_demo.py
```

Expected (Java):

```
garments:        {shirt=3, towel=4, sock=3, sheet=1} size=11
count(shirt)=3 count(jacket)=0 count(42)=0
most common:     towel
remove(sheet)=true remove(sheet)=false
after removals:  {shirt=3, towel=4, sock=3} size=10 distinct=3
sock sizes:      {39=2, 42=2, 40=1} size=5 sum of sizes=202 most common=39
union:           {shirt=3, towel=4, sock=3, 39=2, 42=2, 40=1} size=15
asMap().put refused: UnsupportedOperationException
add(scarf, 0) refused: n must be positive: 0
empty bag most common present? false
```

(Python prints `{'shirt': 3, ...}` with the same numbers, `True`/`False` for booleans, and
`TypeError` for the read-only assignment.)
