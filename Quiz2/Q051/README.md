# Q051 — Orders per service: counting with a map

**Paper.** Quiz 2 · **Marks.** 15 · **Chapter.** 9 · **Difficulty.** easy

> Complete `java/ServiceTally.java` and `python/service_tally.py`.

## The question

At closing time the front desk exports the day's orders as one line of `code:kilograms`
entries:

```
WF:4.2 IR:1.3 WDI:2.6 WF:6.3 SH:1.0 WF:3.0 IR:2.4 WDI:5.1 WF:3.8 SH:0.8 IR:1.9 WF:2.2
```

where `WF` is Wash & Fold, `IR` Iron Only, `WDI` Wash, Dry & Iron and `SH` Shoe Care.

- (a) Count the orders for each service with a map, printing the services in alphabetical
  order of their codes. [4]
- (b) Total the kilograms for each service in the same pass. [3]
- (c) Print the codes ranked by number of orders, most first; ties ranked by kilograms,
  most first. [4]
- (d) A classmate stores the counts in a `HashMap` and prints `map.keySet()`. Explain why
  that report may come out in a different order on another machine or Java version, and
  say what the Python `dict` guarantees instead. [4]

## TODO

| Where | What | Hint |
|:--|:--|:--|
| `count` / `kg` fill (Java) | `count.merge(code, 1, Integer::sum)` / `kg.merge(code, weight, Double::sum)` | absent → value, present → merge |
| `count` / `kg` fill (Python) | `count[code] += 1` / `kg[code] += float(weight)` | `Counter` / `defaultdict` |
| ranking (Java) | sort by count desc, then kg desc | `Comparator.comparing(...).reversed().thenComparing(..., Comparator.reverseOrder())` |
| ranking (Python) | `sorted(count, key=lambda c: (-count[c], -kg[c]))` | negate = largest first |
| (d) | written explanation in `ANSWER.md` | TreeMap/LinkedHashMap vs HashMap; Python dict keeps insertion order |

## Run

```bash
javac -d classes java/*.java && java -cp classes ServiceTally
python3 python/service_tally.py
```

Expected (Java):

```
(a)+(b) per service, in code order:
  IR   orders=3  kg=5.6
  SH   orders=2  kg=1.8
  WDI  orders=2  kg=7.7
  WF   orders=5  kg=19.5
(c) ranking: [WF, IR, WDI, SH]
(d) HashMap iteration order: [SH, IR, WDI, WF]
second method: {IR=3, SH=2, WDI=2, WF=5}
```

(Python prints `['WF', 'IR', 'WDI', 'SH']` for the ranking, `['WF', 'IR', 'WDI', 'SH']`
for dict order, and a list of pairs for `most_common`.)
