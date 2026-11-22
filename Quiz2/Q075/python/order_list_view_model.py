# order_list_view_model.py - Q075: a filtered, sorted list between an order book and its view.
from dataclasses import dataclass

STATUSES = ["RECEIVED", "WASHING", "DRYING", "READY", "PICKED_UP"]


@dataclass
class Order:
    id: str
    customer: str
    service: str
    status: str
    ready_hour: int


class OrderBook:
    def __init__(self):
        self._orders, self._listeners = [], []

    def add_listener(self, fn):
        self._listeners.append(fn)

    def _fire(self):
        for fn in self._listeners:
            fn()

    def add(self, o):
        self._orders.append(o)
        self._fire()

    def advance(self, oid):
        for o in self._orders:
            if o.id == oid:
                o.status = STATUSES[STATUSES.index(o.status) + 1]
        self._fire()

    def orders(self):
        return tuple(self._orders)


SORT_KEYS = {
    "ready": lambda o: (o.ready_hour, o.id),
    "customer": lambda o: (o.customer.casefold(), o.id),
}


class OrderListViewModel:
    def __init__(self, book, view):
        self.book, self.view = book, view
        self.filter, self.only_status, self.sort = "", None, "ready"
        self.shown, self.updates = None, 0
        book.add_listener(self.recompute)
        self.recompute()

    def set_filter(self, text):
        self.filter = (text or "").strip().casefold()
        self.recompute()

    def set_only_status(self, status):
        self.only_status = status
        self.recompute()

    def set_sort(self, key):
        self.sort = key
        self.recompute()

    def recompute(self):
        rows = [
            f"{o.id} {o.customer} {o.service} {o.status} {o.ready_hour:02d}:00"
            for o in sorted(self.book.orders(), key=SORT_KEYS[self.sort])
            if self.filter in o.customer.casefold()
            and (self.only_status is None or o.status == self.only_status)
        ]
        if rows != self.shown:
            self.shown = rows
            self.updates += 1
            self.view(list(rows))


def main():
    book = OrderBook()
    for o in [Order("A1", "Sari", "WF", "WASHING", 14), Order("A2", "Budi", "WDI", "READY", 11),
              Order("A3", "Ayu", "IR", "RECEIVED", 16), Order("A4", "Dewi", "WF", "READY", 12),
              Order("A5", "Rahmat", "WDI", "WASHING", 13), Order("A6", "Citra", "WF", "DRYING", 15)]:
        book.add(o)
    step, log = ["0. attach"], []
    vm = OrderListViewModel(book, lambda rows: log.append(f"{step[0]} -> {rows}"))
    script = [("1. filter 'ra'", lambda: vm.set_filter("ra")),
              ("2. sort by customer", lambda: vm.set_sort("customer")),
              ("3. add A7 Budi", lambda: book.add(Order("A7", "Budi", "IR", "RECEIVED", 17))),
              ("4. advance A5", lambda: book.advance("A5")),
              ("5. filter 'RA'", lambda: vm.set_filter("RA")),
              ("6. only DRYING", lambda: vm.set_only_status("DRYING")),
              ("7. advance A6", lambda: book.advance("A6")),
              ("8. filter ''", lambda: vm.set_filter("")),
              ("9. only READY", lambda: vm.set_only_status("READY"))]
    for name, action in script:
        step[0] = name
        before = vm.updates
        action()
        if vm.updates == before:
            log.append(f"{name} -> (no update)")
    print("\n".join(log))
    print(f"view updates: {vm.updates} for 10 events")


if __name__ == "__main__":
    main()
