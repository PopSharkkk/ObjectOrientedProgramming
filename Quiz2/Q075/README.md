# Q075 — A filtered, sorted order list: a view-model between model and view

**Paper.** Quiz 2 · **Marks.** 25 · **Chapter.** 9–11 · **Difficulty.** hard

> Complete the comparators and the view-model (`recompute` + setters) in
> `java/OrderListViewModel.java` and `python/order_list_view_model.py`. The model and
> driver are given. Write parts (a), (c) and (d) in `ANSWER.md`.

## The question

The order list can be filtered by a text (customer name, case-insensitive), restricted to
one status, and sorted by ready time or by customer name (ties broken by id). A view-model
sits between the book and the widget, recomputes the rows, and tells the view only when the
rows change.

The book: A1 Sari WF WASHING 14:00, A2 Budi WDI READY 11:00, A3 Ayu IR RECEIVED 16:00,
A4 Dewi WF READY 12:00, A5 Rahmat WDI WASHING 13:00, A6 Citra WF DRYING 15:00.

Events after attach: filter "ra"; sort by customer; add A7 Budi IR RECEIVED 17:00;
advance A5; filter "RA"; only DRYING; advance A6; filter cleared; only READY.

- (a) For each event, the rows shown (or "no update"). [10]
- (b) Implement in Java (stream + comparator chain) and Python (comprehension + sort key). [8]
- (c) Why compare new rows with old instead of notifying on every event, and why is the
  view-model easy to test? [4]
- (d) Which Chapter 9 collection/generics ideas does the design use? [3]

## TODO

| Where | What | Hint |
|:--|:--|:--|
| comparators | ready vs customer, tie-break by id | `thenComparing(id)` |
| `recompute` | filter + status + sort + format | compare `rows.equals(shown)` before notifying |

## Run

```bash
javac -d classes java/*.java && java -cp classes OrderListViewModel
python3 python/order_list_view_model.py
```

Expected: `view updates: 6 for 10 events` (with the trace in between).
