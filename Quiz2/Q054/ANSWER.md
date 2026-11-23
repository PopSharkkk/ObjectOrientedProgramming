# Q054 — Your answer

## (a) The eight lines

TODO(Q054): for each of the eight lines, say whether it compiles and why

- (totalKg(shirts);

  compile Shirt is extension of Garment so totalKg func that only accept extend of Garment accept it

- totalKg(anything);

  doesnt compile, anything is not a subtype of Garment so it cannot be passed to the function(extend)

- addSpareTowels(basket, 2);

  Compile, Garment is superclass of towel so the super func in addSpareTowels accept it

- addSpareTowels(bathTowels, 1);

  doesnt compile, bathTowels is the extend of towel but the super function cannot safely accept it, it need superclass not subclass

- transfer(bathTowels, towels);

  Compile, T is inferred as Towel. BathTowel extends Towel, and List<Towel> can accept Towel objects.

- transfer(towels, shirts);

  doesnt compile. need a type that a supertype of towel but subtype of shirt which didnt exist

- List<Garment> g = shirts;

  doesnt compile, List<Shirt>is not a subtype of List<Garment>.

- view.add(new Shirt("x"))).

  The actual element type of List<? extends Garment> is unknown. Adding a Shirt is therefore unsafe.

## (c) The rule

TODO(Q054): state the PECS rule and apply it to `transfer`.

PECS = Producer Extends, Consumer Super
is to protect you from unsafe operations to the wrong datatype when you read or write

- Upper bound - extend

  `extends` restrict a parameter to a class or any of it subclass so its perfect for read only

- Lower bound - super

  `super` restric it to a class or its superclass perfect for add item to a list

## (d) Python

TODO(Q054): which calls does mypy reject, and how do you express `? super Towel` in Python
(variance: covariant / contravariant / invariant)?

mypy reject `total_kg(anything)`, `add_spare_towels(bath_towels, 1)`,`transfer(towels, shirts)`

to express `? super Towel` in Python it use

```py
T = TypeVar("T")
T_contra = TypeVar("T_contra", contravariant=True)


class Sink(Protocol[T_contra]):
    def append(self, item: T_contra, /) -> None: ...
```

It uses contravariance because Sink is a consumer. A Sink[Towel] can accept Towel objects and objects of its subtypes, while the underlying collection may also accept objects of Towel's supertypes.
