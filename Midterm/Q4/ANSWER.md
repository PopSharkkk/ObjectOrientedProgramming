# Q4 — Your answer

## 1. Predicted output

### As provided in the template (with TODO comments intact):
```text
machine 30 min / a machine
machine 30 min / a machine
machine 30 min / a machine
a dryer / a machine / machine
machine 30 min
Exception in thread "main" java.lang.ClassCastException: class Washer cannot be cast to class Dryer (Washer and Dryer are in unnamed module of loader 'app')
	at Dispatch.main(Dispatch.java:56)
```

*(Note: If the TODO(Q045) override methods in `Washer` and `Dryer` are active/uncommented to implement polymorphism as described in the prompt, the output is:*
```text
machine 30 min / a machine
washer 50 min / a machine
dryer 50 min / a machine
a dryer / a machine / dryer
dryer 40 min
Exception in thread "main" java.lang.ClassCastException: class Washer cannot be cast to class Dryer (Washer and Dryer are in unnamed module of loader 'app')
	at Dispatch.main(Dispatch.java:56)
```
*)*

---

## 2. Static overloading vs dynamic dispatch

- **Static Overloading (`kind(m)`)**: 
  Method overloading resolution happens at **compile time** (static binding). The Java compiler selects which overloaded version of `kind(...)` to call based strictly on the declared (static / compile-time) type of the argument reference. Since `m` is declared as `Machine m = d;`, its compile-time type is `Machine`. Therefore, the compiler binds the call to `kind(Machine)` (generating `invokestatic`), ignoring the fact that the object on the heap at runtime is actually a `Dryer`.

- **Dynamic Dispatch (`m.name()` / `m.minutes()`)**: 
  Non-static, non-private instance methods in Java use **runtime dynamic dispatch** (late binding via `invokevirtual`). When an instance method is invoked on `m`, the JVM inspects the actual object's runtime class on the heap. Even though `m` is declared as `Machine`, the JVM finds and executes the method of the actual runtime object (`Dryer`), enabling runtime polymorphism.

---

## 3. The bad cast

- **In Java (`Dryer wrong = (Dryer) w;`)**:
  `w` is declared with compile-time type `Machine`, but at runtime it references a `Washer` instance (`room[1]`). The explicit cast `(Dryer) w` compiles because both `Dryer` and `Washer` are subclasses of `Machine`. However, at runtime the JVM checks whether the actual object is an instance of `Dryer`. Because `Washer` does not inherit from `Dryer` (they are sibling subclasses), the runtime type check fails and the JVM throws a `java.lang.ClassCastException`.

- **In Python (`w.set_heat(4)`)**:
  Python is dynamically typed and does not use explicit type casting. Instead, it relies on attribute/method lookup at runtime (duck typing). In Python, `w` refers to an instance of `Washer`. When `w.set_heat(4)` is executed, Python searches the `Washer` class and its base classes for a `set_heat` method. Because `set_heat` is defined only on `Dryer`, Python raises an `AttributeError: 'Washer' object has no attribute 'set_heat'`.
