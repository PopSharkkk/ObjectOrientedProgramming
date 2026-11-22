# Q075 — Your answer

## (a) The trace

Initial book:
- `A1 Sari WF WASHING 14:00`
- `A2 Budi WDI READY 11:00`
- `A3 Ayu IR RECEIVED 16:00`
- `A4 Dewi WF READY 12:00`
- `A5 Rahmat WDI WASHING 13:00`
- `A6 Citra WF DRYING 15:00`

Initial settings on attach: `filter = ""`, `onlyStatus = null` (all statuses), `sort = ready time` (ascending, ties broken by `id`). Status progression: `RECEIVED -> WASHING -> DRYING -> READY -> PICKED_UP`.

| Event | Action | Active settings / model change | View updated? | Rows shown |
|---:|---|---|:---:|---|
| 0 | Attach view-model | `filter=""`, status=all, sort=`ready` | **Yes (1)** | `A2 Budi WDI READY 11:00`, `A4 Dewi WF READY 12:00`, `A5 Rahmat WDI WASHING 13:00`, `A1 Sari WF WASHING 14:00`, `A6 Citra WF DRYING 15:00`, `A3 Ayu IR RECEIVED 16:00` |
| 1 | Filter `"ra"` | `filter="ra"` matches `Rahmat` and `Citra`; sort=`ready` | **Yes (2)** | `A5 Rahmat WDI WASHING 13:00`, `A6 Citra WF DRYING 15:00` |
| 2 | Sort by customer | `filter="ra"`, sort=`customer` (`Citra` < `Rahmat`) | **Yes (3)** | `A6 Citra WF DRYING 15:00`, `A5 Rahmat WDI WASHING 13:00` |
| 3 | Add `A7 Budi IR RECEIVED 17:00` | Model changes, but `"Budi"` does not contain `"ra"` | **No update** | *(unchanged: `A6`, `A5`)* |
| 4 | Advance `A5` | `A5` moves `WASHING -> DRYING`; its displayed row text changes | **Yes (4)** | `A6 Citra WF DRYING 15:00`, `A5 Rahmat WDI DRYING 13:00` |
| 5 | Filter `"RA"` | Normalized case-insensitively to `"ra"` -> identical rows | **No update** | *(unchanged: `A6`, `A5`)* |
| 6 | Show only `DRYING` | `onlyStatus=DRYING`; both `A6` and `A5` are already `DRYING` | **No update** | *(unchanged: `A6`, `A5`)* |
| 7 | Advance `A6` | `A6` moves `DRYING -> READY` and drops out of the `DRYING` view | **Yes (5)** | `A5 Rahmat WDI DRYING 13:00` |
| 8 | Filter cleared (`""`) | `filter=""`, `onlyStatus=DRYING`; `A5` is the only `DRYING` order in the entire book | **No update** | *(unchanged: `A5`)* |
| 9 | Show only `READY` | `filter=""`, `onlyStatus=READY`, sort=`customer` (`Budi` < `Citra` < `Dewi`) | **Yes (6)** | `A2 Budi WDI READY 11:00`, `A6 Citra WF READY 15:00`, `A4 Dewi WF READY 12:00` |

**Summary:** **6 view updates for 10 events** (events 0, 1, 2, 4, 7, and 9 notify the view; events 3, 5, 6, and 8 produce no update).

## (c) Why compare

### Why compare the new rows with the old ones instead of notifying on every event

1. **Avoids wasted rendering and flicker:** Rebuilding or repainting a list/table widget on every model or control event wastes work and can cause visible flicker. In the 10-event trace above, 4 of the 10 events (40%) do not change the visible rows at all — adding an order outside the search filter (event 3), changing only letter case in the filter (event 5), turning on a status filter that all currently visible rows already satisfy (event 6), and clearing the search text when no other order matches the active status filter (event 8).
2. **Preserves interactive widget state:** Replacing a widget's items often clears the user's current row selection, resets keyboard focus, or jumps the scroll position. Skipping no-op updates keeps the user's UI context intact while unrelated background changes arrive.
3. **Prevents unnecessary downstream event cascades:** Updating a list widget can trigger selection-change listeners, detail-pane reloads, or layout passes; comparing `rows` against `shown` stops those cascades whenever the displayed projection did not actually change.

### Why the view-model is easy to test

1. **No UI toolkit dependency (completely headless):** `OrderListViewModel` imports no Swing or Tkinter classes. It holds plain presentation state (`filter`, `onlyStatus`, `sort`, `shown`) and depends only on the `OrderBook` model and a callback (`Consumer<List<String>>` in Java / callable `view` in Python).
2. **Trivial test spy / fake view:** Tests can inject a one-line lambda (`rows -> log.add(...)`) instead of instantiating windows, list widgets, or a display server.
3. **Deterministic and synchronous:** Calling `setFilter`, `setOnlyStatus`, `setSortByCustomer`/`set_sort`, `book.add`, or `book.advance` runs synchronously on the calling thread — no Event Dispatch Thread (EDT), `mainloop`, timers, or simulated mouse/keyboard events are needed, and every assertion checks plain `List<String>` values and the `updates` count directly.
4. **Clean MVVM separation:** Presentation logic (filtering, sorting, formatting, diffing) is isolated from both business data storage (`OrderBook`) and rendering (widgets), making edge cases like tie-breaking, case folding, and suppressed updates simple to test in isolation.

## (d) Chapter 9 ideas

1. **Parameterized collections and programming to interfaces:** `OrderBook` and `OrderListViewModel` declare fields and signatures using the `List<T>` interface (`List<Order>`, `List<Runnable>`, `List<String>`) backed by `ArrayList<>` with diamond type inference, guaranteeing compile-time type safety without casts.
2. **Defensive / unmodifiable collections:** `OrderBook.orders()` returns `List.copyOf(orders)` (and `tuple(self._orders)` in Python) so the view-model cannot mutate the book's internal list or hit concurrent modification issues. Likewise, `Stream.toList()` produces an unmodifiable `List<String>` (and Python passes a fresh `list(rows)` copy), preventing the view from corrupting the view-model's cached `shown` snapshot.
3. **Generic functional interfaces:** The view callback is typed as `Consumer<List<String>>` — a nested generic type — decoupling the view-model from any specific UI widget while keeping the contract type-checked at compile time; model change observers use `Runnable`.
4. **`Comparator<Order>` and comparator chaining:** Sorting is externalized from `Order` via two `Comparator<Order>` strategies (`BY_READY` and `BY_CUSTOMER`) built with `Comparator.comparingInt(...).thenComparing(...)` and `Comparator.comparing(..., String.CASE_INSENSITIVE_ORDER).thenComparing(...)` (and tuple sort keys `(o.ready_hour, o.id)` / `(o.customer.casefold(), o.id)` in Python). This cleanly supports interchangeable multi-key orderings with deterministic tie-breaking by `id`.
5. **Non-mutating stream pipeline (and comprehension):** `recompute()` uses a declarative stream pipeline (`stream().filter(...).filter(...).sorted(sort).map(...).toList()`) in Java — and a list comprehension over `sorted(...)` in Python — transforming the snapshot into formatted rows without ever sorting or mutating the underlying `OrderBook`.
6. **Value-based collection equality:** Change detection (`!rows.equals(shown)` in Java, `rows != self.shown` in Python) relies on `List`'s element-by-element, order-sensitive `equals` contract rather than reference identity.
