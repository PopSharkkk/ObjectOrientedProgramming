# service_tally.py - Q051: orders and kilograms per service, and a ranking.
from collections import Counter, defaultdict

LOG = ("WF:4.2 IR:1.3 WDI:2.6 WF:6.3 SH:1.0 WF:3.0 "
       "IR:2.4 WDI:5.1 WF:3.8 SH:0.8 IR:1.9 WF:2.2")


def main():
    count = Counter()               # a dict whose missing keys count as 0
    kg = defaultdict(float)         # missing keys start at 0.0
    for entry in LOG.split():
        code, weight = entry.split(":")
        # TODO(Q051): count[code] += 1
        # TODO(Q051): kg[code] += float(weight)

    print("(a)+(b) per service, in code order:")
    for code in sorted(count):
        print(f"  {code:<3}  orders={count[code]}  kg={kg[code]:.1f}")

    # (c) one sort with a tuple key: negate to get "largest first".
    # TODO(Q051): ranking = sorted(count, key=lambda c: (-count[c], -kg[c]))
    ranking = []
    print("(c) ranking:", ranking)

    # (d) a dict keeps insertion order (guaranteed since Python 3.7).
    print("(d) dict iteration order:", list(count))

    # Second method: most_common sorts by count only; ties keep first-seen order.
    print("second method:", count.most_common())


if __name__ == "__main__":
    main()
