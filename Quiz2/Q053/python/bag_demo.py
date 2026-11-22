# bag_demo.py - Q053: a generic multiset Bag[T] for the sorting table.
from types import MappingProxyType
from typing import Dict, Generic, Mapping, Optional, TypeVar

T = TypeVar("T")


class Bag(Generic[T]):
    """A multiset: each distinct element with the number of times it was added."""

    def __init__(self) -> None:
        self._counts: Dict[T, int] = {}      # a dict keeps first-added order
        self._size = 0

    def add(self, item: T, n: int = 1) -> None:
        # TODO(Q053): if n <= 0: raise ValueError(...);
        #   self._counts[item] = self._counts.get(item, 0) + n; self._size += n

    def count(self, item: object) -> int:
        # TODO(Q053): return self._counts.get(item, 0)
        return 0

    def remove(self, item: T) -> bool:
        # TODO(Q053): if absent return False; if count == 1 delete the key, else decrement; self._size -= 1
        return False

    def __len__(self) -> int:
        return self._size

    def distinct(self) -> int:
        # TODO(Q053): return len(self._counts)
        return 0

    def most_common(self) -> Optional[T]:
        # TODO(Q053): if empty return None; return max(self._counts, key=lambda k: self._counts[k])
        return None

    def as_map(self) -> Mapping[T, int]:
        # TODO(Q053): return MappingProxyType(self._counts)
        return self._counts

    @staticmethod
    def union(a: "Bag[T]", b: "Bag[T]") -> "Bag[T]":
        # TODO(Q053): result = Bag(); for each bag add every (k, n) into result; return result
        return Bag()

    def __repr__(self) -> str:
        return f"{self._counts} size={self._size}"


def main() -> None:
    garments: Bag[str] = Bag()
    for g in "shirt towel shirt sock sock shirt sheet sock".split():
        garments.add(g)
    garments.add("towel", 3)
    print("garments:       ", garments)
    print(f"count(shirt)={garments.count('shirt')} count(jacket)={garments.count('jacket')} "
          f"count(42)={garments.count(42)}")
    print("most common:    ", garments.most_common())
    print(f"remove(sheet)={garments.remove('sheet')} remove(sheet)={garments.remove('sheet')}")
    print("after removals: ", garments, f"distinct={garments.distinct()}")

    sock_sizes: Bag[int] = Bag()
    sock_sizes.add(39, 2)
    sock_sizes.add(42, 2)
    sock_sizes.add(40)
    total = sum(k * n for k, n in sock_sizes.as_map().items())
    print("sock sizes:     ", sock_sizes, f"sum of sizes={total} most common={sock_sizes.most_common()}")

    lost_and_found: Bag[object] = Bag.union(garments, sock_sizes)  # type: ignore[arg-type]
    print("union:          ", lost_and_found)

    try:
        garments.as_map()["scarf"] = 99  # type: ignore[index]
    except TypeError:
        print("as_map() assignment refused: TypeError")
    try:
        garments.add("scarf", 0)
    except ValueError as e:
        print("add(scarf, 0) refused:", e)
    print("empty bag most common present?", Bag().most_common() is not None)


if __name__ == "__main__":
    main()
