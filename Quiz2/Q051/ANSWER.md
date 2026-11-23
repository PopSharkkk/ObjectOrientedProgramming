# Q051 — Your answer

## (d) HashMap order vs Python dict order

TODO(Q051): explain why printing `map.keySet()` from a `HashMap` may come out in a
different order on another machine or Java version, and what the Python `dict`
guarantees instead (and whether that guarantee is alphabetical).

hash map is a data structure that maps keys to values by taking the hash value of the key so in another machine or java version it can used a diffrent hash value and result in diffrent order

but in python dictionary it guarantee the insertion order, so the keys is iterated by the order of input but it dont guarantee alphabetical order
