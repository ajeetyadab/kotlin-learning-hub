# Chapter 15: Advanced Classes

## The Problem — Duplicate Code Between Related Classes

Before diving into the features of this chapter, start with the problem they all solve.

In Chapter 11, you built a `Person` class and a `Student` class separately:

```kotlin
class Person(var firstName: String, var lastName: String) {
    fun fullName() = "$firstName $lastName"
}

class Student(
    var firstName: String,
    var lastName: String,
    var grades: MutableList<Grade> = mutableListOf()
) {
    fun recordGrade(grade: Grade) {
        grades.add(grade)
    }
}
```

Look carefully at both classes. `Student` has `firstName`, `lastName`, and a `fullName()` method — all duplicated from `Person`. You had to type it all twice.

More importantly: a student **is** a person. They share the same basic identity. That relationship exists in the real world, and it should exist in your code too.

This is the problem that **inheritance** solves.

---

## Inheritance — Sharing Code Through "Is-A" Relationships

Inheritance lets one class automatically receive all the properties and methods of another class. The class that gives is called the **superclass** (or parent class). The class that receives is called the **subclass** (or child class).

You say a subclass **is-a** superclass. A `Student` is-a `Person`. An `OboePlayer` is-a `BandMember` is-a `Student` is-a `Person`.

### The `open` Keyword — Kotlin is Closed by Default

In many languages like Java, every class can be inherited from unless you explicitly block it. Kotlin takes the opposite approach: **classes are closed to inheritance by default**. You must mark a class `open` to allow it to be subclassed.

This is intentional. Designing a class for inheritance is non-trivial. By requiring `open`, Kotlin forces you to make a deliberate choice.

Here is the improved version of `Person` and `Student`:

```kotlin
// "open" means: other classes are allowed to inherit from Person
open class Person(var firstName: String, var lastName: String) {
    fun fullName() = "$firstName $lastName"
}

// Student inherits from Person — written with a colon followed by the superclass
class Student(
    firstName: String,         // No "var" here — these are already declared in Person
    lastName: String,
    var grades: MutableList<Grade> = mutableListOf()
) : Person(firstName, lastName) {   // Pass values up to Person's constructor

    open fun recordGrade(grade: Grade) {
        grades.add(grade)
    }
}
```

A few important things to notice:

- `firstName` and `lastName` in `Student`'s constructor have **no `var` or `val`**. That is because they are not being declared here — they already exist as properties in `Person`. Here, they are just being received and forwarded.
- The `: Person(firstName, lastName)` part calls `Person`'s constructor with those values. This creates the `Person` part of the `Student` object.
- `Student`'s own `recordGrade()` is marked `open` so future subclasses can override it.

Now use them:

```kotlin
val john = Person(firstName = "Johnny", lastName = "Appleseed")
val jane = Student(firstName = "Jane", lastName = "Appleseed")

john.fullName()   // "Johnny Appleseed" — Person's method
jane.fullName()   // "Jane Appleseed" — inherited from Person!
```

`jane` can call `fullName()` even though it is defined in `Person`. Inheritance gave it to her automatically.

The reverse is not true:

```kotlin
val history = Grade(letter = 'B', points = 9.0, credits = 3.0)
jane.recordGrade(history)        // Works — jane is a Student
// john.recordGrade(history)     // ERROR — john is only a Person
```

Inheritance flows **downward** — from parent to child. A `Person` does not automatically get `Student`-specific features.

---

## Class Hierarchies — Chains of Inheritance

There is no limit to how deep you can go. A subclass can itself be subclassed:

```kotlin
// Student is now open so it can be subclassed further
open class Student(
    firstName: String,
    lastName: String,
    var grades: MutableList<Grade> = mutableListOf()
) : Person(firstName, lastName) {
    open fun recordGrade(grade: Grade) { grades.add(grade) }
}

// BandMember is a kind of Student
open class BandMember(
    firstName: String,
    lastName: String
) : Student(firstName, lastName) {
    open val minimumPracticeTime: Int
        get() = 2   // BandMembers practice at least 2 hours
}

// OboePlayer is a kind of BandMember
class OboePlayer(
    firstName: String,
    lastName: String
) : BandMember(firstName, lastName) {
    // Oboe players need twice the practice of a regular band member
    override val minimumPracticeTime: Int = super.minimumPracticeTime * 2
}
```

The full chain: `OboePlayer → BandMember → Student → Person`.

This is called a **class hierarchy**. You can think of it exactly like a family tree. `Person` is the grandparent. `Student` is the parent. `BandMember` and `OboePlayer` are children and grandchildren.

An `OboePlayer` automatically inherits everything: `firstName`, `lastName`, `fullName()`, `grades`, `recordGrade()`, `minimumPracticeTime` — all of it flows down the chain.

**Rules for subclassing:**
- A class can only inherit from **one** other class (single inheritance).
- A class can only inherit from a class marked **`open`**.
- You can chain as deeply as needed, but deeper hierarchies become harder to reason about.

---

## Polymorphism — One Type, Many Forms

**Polymorphism** sounds intimidating but the idea is simple: because `OboePlayer` is-a `Person`, you can use an `OboePlayer` anywhere a `Person` is expected.

```kotlin
// This function expects a Person
fun phonebookName(person: Person): String {
    return "${person.lastName}, ${person.firstName}"
}

val person = Person(firstName = "Johnny", lastName = "Appleseed")
val oboePlayer = OboePlayer(firstName = "Jane", lastName = "Appleseed")

phonebookName(person)       // "Appleseed, Johnny"
phonebookName(oboePlayer)   // "Appleseed, Jane" — works! OboePlayer IS-A Person
```

The function `phonebookName` only knows it has a `Person`. It has no idea the second call is actually passing an `OboePlayer`. It just uses the `Person` parts — `firstName` and `lastName` — which every `Person` (and every subclass of `Person`) has.

This is useful when you have many different subclasses and you want to write one function that works with all of them. For example: a function that processes all `Student` objects, regardless of whether each student is a regular `Student`, a `BandMember`, or an `OboePlayer`.

---

## Runtime Hierarchy Checks — `is`, `as`, and `as?`

Since a `Student` variable can hold a `BandMember` or `OboePlayer` at runtime, you sometimes need to check what the actual type is.

### Checking Type with `is` and `!is`

```kotlin
var hallMonitor = Student(firstName = "Jill", lastName = "Bananapeel")
hallMonitor = oboePlayer   // Allowed — OboePlayer IS-A Student

// The compiler thinks hallMonitor is a "Student" — it doesn't know it's actually an OboePlayer
// hallMonitor.minimumPracticeTime   // ERROR — Student doesn't have this property

// Use "is" to check the actual runtime type
println(hallMonitor is OboePlayer)    // true — it IS an OboePlayer
println(hallMonitor !is OboePlayer)   // false — !is is the opposite
println(hallMonitor is Person)        // true — OboePlayer IS-A Person too
```

The `is` check looks at what the object actually **is** at runtime, not what type the variable was declared as.

### Casting with `as` and `as?`

Once you know something **is** a specific type, you can **cast** it — tell the compiler "treat this as that type":

```kotlin
// as? is the SAFE cast — returns null if the cast fails (does not crash)
val bandMemberPracticeTime = (hallMonitor as? BandMember)?.minimumPracticeTime
// Returns 4 if hallMonitor is an OboePlayer, otherwise null
```

**`as` vs `as?`:**
- `as` — unsafe cast. Crashes with an exception if the cast fails. Use only when you are absolutely certain.
- `as?` — safe cast. Returns `null` if the cast fails. Use this in normal code.

### Static Dispatch — Why Casting Down Sometimes Matters

There is one subtle situation where casting to a *parent* type (casting "up") matters: when you have two functions with the same name but different parameter types.

```kotlin
fun afterClassActivity(student: Student): String = "Goes home!"
fun afterClassActivity(student: BandMember): String = "Goes to practice!"
```

Kotlin picks which function to call based on the **declared type** at compile time:

```kotlin
afterClassActivity(oboePlayer)              // "Goes to practice!" — uses BandMember version
afterClassActivity(oboePlayer as Student)   // "Goes home!" — forced to use Student version
```

This is called **static dispatch** — the decision is made at compile time based on types, not at runtime. Casting to a parent type changes which version of the overloaded function gets called.

---

## Overriding Methods

A subclass can replace a superclass method with its own version. This is called **overriding**.

To override, you must:
1. Mark the method in the superclass as `open`.
2. Write `override` before the method in the subclass.

Example: `StudentAthlete` needs to track failing grades to determine eligibility:

```kotlin
class StudentAthlete(
    firstName: String,
    lastName: String
) : Student(firstName, lastName) {

    val failedClasses = mutableListOf<Grade>()

    // Override recordGrade() — intercept every grade that gets recorded
    override fun recordGrade(grade: Grade) {
        super.recordGrade(grade)   // FIRST: call Student's version to record the grade normally

        // THEN: add our extra logic — track failing grades
        if (grade.letter == 'F') {
            failedClasses.add(grade)
        }
    }

    // An athlete is eligible as long as they have fewer than 3 failed classes
    val isEligible: Boolean
        get() = failedClasses.size < 3
}
```

If you write a method in a subclass that has the same signature as one in the superclass but forget the `override` keyword, Kotlin gives you a build error:

```
'recordGrade' hides member of supertype 'Student' and needs 'override' modifier
```

This is a safety feature — it prevents you from accidentally hiding a superclass method without realising it.

Try it out:

```kotlin
val math = Grade(letter = 'B', points = 9.0, credits = 3.0)
val science = Grade(letter = 'F', points = 9.0, credits = 3.0)
val physics = Grade(letter = 'F', points = 9.0, credits = 3.0)
val chemistry = Grade(letter = 'F', points = 9.0, credits = 3.0)

val dom = StudentAthlete(firstName = "Dom", lastName = "Grady")
dom.recordGrade(math)
dom.recordGrade(science)
dom.recordGrade(physics)
println(dom.isEligible)    // true — only 2 fails so far

dom.recordGrade(chemistry)
println(dom.isEligible)    // false — 3 failed classes
```

---

## The `super` Keyword

Inside an overridden method, `super` refers to the **parent class's version** of that method.

```kotlin
override fun recordGrade(grade: Grade) {
    super.recordGrade(grade)   // Runs Student's recordGrade() — adds grade to the grades list
    // ... then our extra logic
}
```

Without `super.recordGrade(grade)`, the grade would never be added to `grades`, because `StudentAthlete` does not duplicate that code — it calls up to `Student` to handle it.

### When to Call `super` — Order Matters

Always call `super` **first** in an overridden method, before your own extra logic. Here is why:

```kotlin
// BAD VERSION — super called LAST
override fun recordGrade(grade: Grade) {
    var newFailedClasses = mutableListOf<Grade>()
    for (g in grades) {            // Scans current grades for F's
        if (g.letter == 'F') {
            newFailedClasses.add(g)
        }
    }
    failedClasses = newFailedClasses

    super.recordGrade(grade)       // Adds the new grade AFTER scanning — too late!
}
```

The bug: if the new `grade` is an `F`, it is not yet in `grades` when you scan. So `failedClasses` does not include it. Calling `super` last caused the new grade to be missed.

**Best practice:** Call `super` first. Let the parent class do its work, then add your extra behaviour on top of the already-updated state.

---

## Preventing Further Inheritance

By default, a Kotlin class is **not open** — it cannot be subclassed. You have to explicitly write `open` to allow it.

```kotlin
// FinalStudent is NOT open — nobody can inherit from it
class FinalStudent(firstName: String, lastName: String) : Person(firstName, lastName)

class FinalStudentAthlete(firstName: String, lastName: String)
    : FinalStudent(firstName, lastName)   // BUILD ERROR — FinalStudent is not open!
```

This is the opposite of Java (where every class can be inherited from unless marked `final`). Kotlin's philosophy: **opt in to inheritance deliberately**.

The same applies to individual methods. If a class is `open` but you only want some methods to be overridable:

```kotlin
open class AnotherStudent(firstName: String, lastName: String)
    : Person(firstName, lastName) {

    open fun recordGrade(grade: Grade) {}   // CAN be overridden
    fun recordTardy() {}                    // CANNOT be overridden — not open
}

class AnotherStudentAthlete(firstName: String, lastName: String)
    : AnotherStudent(firstName, lastName) {

    override fun recordGrade(grade: Grade) {}   // OK
    override fun recordTardy() {}               // BUILD ERROR — not open!
}
```

This gives you fine-grained control. You can say: "You may subclass me, and you may change *this* method, but *that* one is off limits."

---

## Abstract Classes

Sometimes you want a class that:
- **Can** be inherited from.
- **Cannot** be created directly as an instance.
- Forces subclasses to provide certain methods.

This is an **abstract class**.

Think of it like a template or a blueprint. You would never hand someone a blank blueprint as a finished product — but you can use that blueprint to build actual houses.

```kotlin
// abstract = can't instantiate, must be subclassed, implicitly open
abstract class Mammal(val birthDate: String) {
    abstract fun consumeFood()   // abstract method = no body, subclasses MUST implement this
}

class Human(birthDate: String) : Mammal(birthDate) {
    // MUST override consumeFood() or the compiler will refuse to compile
    override fun consumeFood() {
        println("Cooks and eats food")
    }

    fun createBirthCertificate() {
        println("Certificate created for birth date: $birthDate")
    }
}
```

```kotlin
val human = Human("1/1/2000")   // Works — Human is a concrete class
val mammal = Mammal("1/1/2000") // ERROR — Cannot create an instance of an abstract class
```

**Abstract vs `open`:**

| | `open class` | `abstract class` |
|--|------------|----------------|
| Can be instantiated | Yes | No |
| Can be subclassed | Yes | Yes (automatically) |
| Can have abstract methods | No | Yes |
| Subclass must override abstract methods | — | Yes |

Use `abstract` when the class exists only to define a shared contract — not to be used on its own.

---

## Sealed Classes

A **sealed class** is like a more powerful version of an abstract class. It says: "This type can only be one of a specific, fixed set of subtypes — and those subtypes are all defined right here."

The key benefit: when you use a `when` expression on a sealed class, the compiler knows all possible subtypes. If you miss one, it will warn you. This makes your code much safer.

```kotlin
// Shape can only be a Circle or a Square — nothing else
sealed class Shape {
    class Circle(val radius: Int) : Shape()
    class Square(val sideLength: Int) : Shape()
}
```

Create instances of the specific subtypes:

```kotlin
val circle1 = Shape.Circle(4)
val circle2 = Shape.Circle(2)
val square1 = Shape.Square(4)
val square2 = Shape.Square(2)
// val shape = Shape()   // ERROR — sealed class is abstract, can't instantiate directly
```

Use `when` to handle each case — and notice the compiler can verify you covered all cases:

```kotlin
fun size(shape: Shape): Int {
    return when (shape) {
        is Shape.Circle -> shape.radius       // Smart cast — shape is now Circle here
        is Shape.Square -> shape.sideLength   // Smart cast — shape is now Square here
        // No "else" needed — the compiler knows Circle and Square are the only options
    }
}

println(size(circle1))   // Output: 4
println(size(square2))   // Output: 2
```

**Sealed vs Enum:**
- An `enum` has exactly one instance of each value. `Direction.NORTH` is a single object.
- A sealed class can have **many instances** of each subtype. You can create a hundred `Shape.Circle` objects, each with a different radius.
- Use sealed classes when you need a fixed set of types but each type can carry its own unique data.

---

## Secondary Constructors

The primary constructor is the one defined on the class name line. But sometimes you need **multiple ways** to create the same type. That is what secondary constructors are for.

You define them inside the class body using the `constructor` keyword:

```kotlin
open class Shape {
    constructor(size: Int) {
        println("Creating a shape of size $size")
    }

    // Second constructor: also takes a color
    // It delegates to the first constructor using "this(size)"
    constructor(size: Int, color: String) : this(size) {
        println("Color: $color")
    }
}
```

The `: this(size)` part means "first, run the other constructor that takes just a size, then add my extra logic."

When subclassing, call the parent's constructor using `super`:

```kotlin
class Circle : Shape {
    constructor(size: Int) : super(size) {
        // Run Shape(size) first, then add Circle-specific setup
    }

    constructor(size: Int, color: String) : super(size, color) {
        // Run Shape(size, color) first
    }
}
```

Secondary constructors are useful when you want to provide convenient alternative ways to create an object — for example, creating a `Circle` with a radius, or with a diameter, or from a string description.

---

## Nested and Inner Classes

When two classes are closely related, you can define one **inside** the other. This is called **nesting**. It is just a way of grouping them together under the outer class's name.

```kotlin
class Car(val carName: String) {
    class Engine(val engineName: String)   // Nested class
}
```

To use `Engine`, you reference it as `Car.Engine`:

```kotlin
val engine = Car.Engine("rotary")
```

### The Problem With Nested Classes

By default, a nested class is completely isolated — it cannot see the properties of the outer class:

```kotlin
class Car(val carName: String) {
    class Engine(val engineName: String) {
        override fun toString(): String {
            return "$engineName in a $carName"   // ERROR — cannot see carName!
        }
    }
}
```

`Engine` does not have access to `carName` because it is just a nested class — it lives inside `Car`'s namespace, but it has no reference to any specific `Car` instance.

### Inner Classes — Giving Access to the Outer Class

Add the `inner` keyword to give the nested class access to its enclosing class's members:

```kotlin
class Car(val carName: String) {
    inner class Engine(val engineName: String) {
        override fun toString(): String {
            return "$engineName engine in a $carName"   // Now it can see carName!
        }
    }
}
```

Creating an inner class requires an instance of the outer class:

```kotlin
val mazda = Car("mazda")
val mazdaEngine = mazda.Engine("rotary")   // Created from an instance of Car
println(mazdaEngine)   // Output: rotary engine in a mazda
```

**Nested vs Inner — Quick Rule:**

| | Nested class | Inner class |
|--|------------|------------|
| Declared with | `class` | `inner class` |
| Can see outer class members? | No | Yes |
| Created how? | `Car.Engine(...)` | `mazda.Engine(...)` |

Use `inner` when the nested class genuinely needs to access data from the containing instance.

---

## Visibility Modifiers

Visibility modifiers control **who can see what**. They are how you enforce the principle of least privilege: only expose what needs to be exposed.

Kotlin has four visibility levels:

| Modifier | Visible to |
|----------|-----------|
| `public` (default) | Everywhere — other files, other classes, other modules |
| `private` | Only inside the same class (or same file for top-level declarations) |
| `protected` | Only inside the class and its subclasses |
| `internal` | Only within the same module (e.g., the same IntelliJ project) |

Here is a concrete example:

```kotlin
data class Privilege(val id: Int, val name: String)

open class User(
    val username: String,        // public — everyone can see it
    private val id: String,      // private — only User itself can use this
    protected var age: Int       // protected — User and its subclasses can use this
)

class PrivilegedUser(username: String, id: String, age: Int)
    : User(username, id, age) {

    private val privileges = mutableListOf<Privilege>()   // only PrivilegedUser sees this

    fun addPrivilege(privilege: Privilege) {
        privileges.add(privilege)
    }

    fun hasPrivilege(id: Int): Boolean {
        return privileges.map { it.id }.contains(id)
    }

    fun about(): String {
        // return "$username, $id"   // ERROR — id is private to User, even subclasses can't see it
        return "$username, $age"     // OK — age is protected, subclasses can access it
    }
}
```

Try it:

```kotlin
val privilegedUser = PrivilegedUser(username = "sashinka", id = "1234", age = 21)
val privilege = Privilege(1, "invisibility")
privilegedUser.addPrivilege(privilege)
println(privilegedUser.about())   // Output: sashinka, 21

// From outside the class:
println(privilegedUser.username)  // OK — public
// println(privilegedUser.id)     // ERROR — private
// println(privilegedUser.age)    // ERROR — protected (only accessible from inside subclasses)
```

**A general rule:** Start with the most restrictive visibility (`private`). Only widen it when something genuinely needs to be accessed from outside. This prevents accidental misuse and keeps your classes easier to reason about.

---

## When and Why to Subclass

Inheritance is powerful, but it is not always the right tool. Here is a practical guide.

### ✅ Good reasons to subclass

**Single responsibility** — if a `Student` and a `StudentAthlete` have meaningfully different responsibilities, separating them makes each class cleaner. A `Student` should not need to know about sports eligibility.

**Strong types** — subclassing creates a new type. The compiler can then enforce that only `StudentAthlete` objects go into a team roster, catching bugs at compile time:

```kotlin
class Team {
    var players = mutableListOf<StudentAthlete>()

    val isEligible: Boolean
        get() = players.all { it.isEligible }   // all() returns true if condition holds for every element
}
```

If you try to add a plain `Student` to `players`, the compiler refuses. The type system protects your logic.

**Shared base class** — when you have multiple unrelated types that all share a small common interface, a base class holds the shared part and each subclass handles the unique part:

```kotlin
open class Button {
    fun press() { /* shared behaviour */ }
}

class ImageButton(var image: Image) : Button()   // Has an image, can also be pressed
class TextButton(val text: String) : Button()    // Has text, can also be pressed
```

Putting `image` and `text` into `Button` itself would make `Button` a mess. Each subclass handles its own rendering; `Button` handles the press.

**Extensibility** — if you are using a library class and need to customize its behavior, subclassing (if the class is `open`) is often the only option.

### ⚠️ Think twice before subclassing

If you find yourself making deep hierarchies (5+ levels), it often means the design has gone wrong. Deep inheritance chains are hard to debug and maintain.

If your goal is to **share behaviour** (what things can do) rather than **model identity** (what things are), prefer **interfaces** over subclassing. You will learn about interfaces in Chapter 17.

A quick test: if you can say "A is-a B" naturally — `Student` is-a `Person`, `Circle` is-a `Shape` — inheritance likely makes sense. If the relationship feels forced, it probably is.

---

## Challenges

### Challenge 1: Inheritance Order — What Prints First?

Create three classes `A`, `B`, `C` where `C` inherits from `B` and `B` inherits from `A`. Print a message in each class's `init` block. Create an instance of `C`. What order are the messages printed?

**Solution:**

```kotlin
open class A {
    init { println("I'm A!") }
}

open class B : A() {
    init { println("I'm B!") }
}

class C : B() {
    init { println("I'm C!") }
}

val c = C()
```

**Output:**
```
I'm A!
I'm B!
I'm C!
```

**Why this order?** When you create `C`, Kotlin must first build the `A` part (the root), then the `B` part, then finally the `C` part. Construction always flows from parent to child — top of the hierarchy first, then downward. Think of it as building floors of a building: ground floor first, then each floor on top.

---

### Challenge 2: Casting — `c` to `A`, and `a` to `C`

Cast the `C` instance to type `A`. Then create a plain `A` instance and try to cast it to `C`.

**Solution:**

```kotlin
// Casting c (type C) up to A — safe, always works, use "as"
val cAsA: A = c as A
println("Casting C to A succeeded")

// Casting a (type A) down to C — unsafe, use "as?" to be safe
val a = A()
val aAsC: C? = a as? C
println(aAsC)   // Output: null — 'a' is just an A, not a C
```

**Why `as` for upward cast?** Casting `C` to `A` is guaranteed to succeed — every `C` is-a `A` by definition. `as` is appropriate here.

**Why `as?` for downward cast?** A plain `A` object is not necessarily a `C`. If you used `as` and the cast failed, you would get a `ClassCastException` crash. `as?` returns `null` instead of crashing — much safer.

**Rule:** Cast up with `as`. Cast down with `as?`.

---

### Challenge 3: `StudentBaseballPlayer`

Create a `StudentBaseballPlayer` subclass of `StudentAthlete` with `position`, `number`, and `battingAverage` properties. Discuss the benefits and drawbacks.

**Solution:**

```kotlin
class StudentBaseballPlayer(
    firstName: String,
    lastName: String,
    val position: String,
    val number: Int,
    val battingAverage: Double
) : StudentAthlete(firstName, lastName)
```

```kotlin
val player = StudentBaseballPlayer(
    firstName = "Babe",
    lastName = "Ruth",
    position = "Pitcher",
    number = 3,
    battingAverage = 0.342
)

println(player.fullName())        // "Babe Ruth" — from Person
println(player.isEligible)        // true — from StudentAthlete
println(player.position)          // "Pitcher" — from StudentBaseballPlayer
```

**Benefits of subclassing `StudentAthlete`:**
- Gets all of `Person`, `Student`, and `StudentAthlete` for free — no duplication.
- `isEligible` tracking from `StudentAthlete` applies automatically.
- Type safety: a `Team<StudentBaseballPlayer>` can enforce that all players are specifically baseball players.
- Adding baseball-specific behavior (batting average thresholds, position restrictions) stays cleanly in one place.

**Drawbacks:**
- The hierarchy is now 4 levels deep: `StudentBaseballPlayer → StudentAthlete → Student → Person`. Deep hierarchies are harder to debug.
- If you need a `StudentBasketballPlayer`, a `StudentSoccerPlayer`, and so on, you end up with many parallel subclasses that differ only in a few properties. In that case, a single `StudentAthlete` with a `sport: Sport` property might be simpler.
- Changes to `Student` or `StudentAthlete` ripple all the way down to `StudentBaseballPlayer` — this coupling can cause unexpected bugs.

---

### Challenge 4: A Sealed `Resource` Class

Create a sealed class `Resource` with subtypes `Success`, `Loading`, and `Error`. `Success` holds a `data` string, `Error` holds an `error` string. Show a practical use case.

**Solution:**

```kotlin
sealed class Resource {
    class Success(val data: String) : Resource()
    object Loading : Resource()             // object works here — Loading has no data
    class Error(val error: String) : Resource()
}
```

Use it with `when` — the compiler verifies all cases are handled:

```kotlin
fun handleResource(resource: Resource) {
    when (resource) {
        is Resource.Success -> println("Data received: ${resource.data}")
        is Resource.Loading -> println("Loading, please wait...")
        is Resource.Error   -> println("Something went wrong: ${resource.error}")
        // No "else" needed — compiler knows these are the only three options
    }
}

handleResource(Resource.Loading)
// Output: Loading, please wait...

handleResource(Resource.Success("User profile loaded"))
// Output: Data received: User profile loaded

handleResource(Resource.Error("Network timeout"))
// Output: Something went wrong: Network timeout
```

**Real-world use case:** This `Resource` pattern is extremely common in Android apps. When you fetch data from a server, the result can be one of three states: it is still loading, it succeeded with data, or it failed with an error. Representing these as a sealed class means the UI code that handles the result is *forced* by the compiler to deal with all three cases. If you add a fourth case (say `Resource.Empty`), the compiler immediately tells you everywhere you need to update your `when` expressions. You cannot accidentally forget a case.

---

## Summary — The Inheritance Toolkit

| Feature | Keyword | Purpose |
|---------|---------|---------|
| Allow inheritance | `open class` | Let others subclass this class |
| Inherit from a class | `: ParentClass(args)` | Take all parent properties and methods |
| Allow method override | `open fun` | Let subclasses replace this method |
| Override a method | `override fun` | Replace the parent's version |
| Call the parent's version | `super.method()` | Run parent logic from inside an override |
| Prevent instantiation, require override | `abstract` | Template class — must be subclassed |
| Lock down the type hierarchy | `sealed` | Only known subtypes, exhaustive `when` |
| Multiple constructors | `constructor` | Different ways to create the same class |
| Group classes by name | Nested class | Organisation, no outer class access |
| Group classes with outer access | `inner class` | Organisation, full outer class access |
| Control who sees what | `private` / `protected` / `internal` / `public` | Encapsulation |
