# Chapter 12: Objects

## Before We Start — A Word About the Word "Object"

Kotlin uses the word **object** in three different ways, and this can be confusing at first. Here is a quick map so you always know which one is being discussed:

| Term | What it means |
|------|---------------|
| **object** (keyword) | A special Kotlin keyword used to create a type with exactly one instance |
| **object** (class instance) | The general programming term — when you create a class and call it, the result is an object (instance) |
| **anonymous object** | A one-off object created on the spot, without defining a named class |

This chapter is mostly about the first kind — the `object` keyword. But you will see all three, so keep this table in mind.

---

## The Problem the `object` Keyword Solves

In Chapter 11, you learned that a `class` is a blueprint. From one blueprint, you can build many houses — or in programming terms, many instances.

But sometimes you want **exactly one instance** of something. Not two. Not zero. One — for the entire life of your program.

Think about a school. A school has one principal's office. No matter who walks into the building, there is only one office. You do not create a new principal's office every time someone needs to speak to the principal. The office is shared.

In programming, this is called the **Singleton Pattern**.

A singleton is a type that guarantees only one instance of itself will ever exist during a program's run.

In other languages like Java, implementing a singleton requires writing a lot of boilerplate code manually. Kotlin makes it simple with one keyword: **`object`**.

---

## Named Objects — Singletons in Kotlin

When you write `object` instead of `class`, Kotlin automatically creates a type that:

- Has **exactly one instance** — ever.
- Is created the first time it is accessed.
- Cannot be created again — no constructor allowed.

The syntax looks like a class, but with `object` in place of `class`:

```kotlin
object X {
    var x = 0
}
```

That is it. You do not create an instance of `X` the way you would with a class. There is no `val myX = X()`. The object `X` already exists — you just use it directly:

```kotlin
X.x = 5
println(X.x) // prints: 5
```

You access its properties and call its methods using the object's name with dot syntax, just like you would with a class instance.

### Why No Constructor?

Because the whole point of a constructor is to create a new instance. With a named object, Kotlin creates the one instance for you automatically. There is nothing for a constructor to do.

Think of it this way: a constructor is a factory. If there is only ever going to be one product, you do not need a factory — the product just exists.

### What Kotlin Does Behind the Scenes

If you were to look at what Kotlin compiles your `object` into (as Java code), you would see something like this:

```java
// This is what Kotlin generates automatically — you do not write this
public final class X {
    private static int x;
    public static final X INSTANCE;   // The one and only instance

    static {
        INSTANCE = new X();           // Created once, never again
    }

    public final int getX() { return x; }
    public final void setX(int val) { x = val; }
}
```

Kotlin handles all of this automatically when you write `object X { ... }`. You get the safety of a singleton without writing any of this boilerplate.

---

## Singleton Use Case 1: Shared Data Registry

A very common use of singletons is a **registry** — a single place that holds a collection of data for your whole app.

Imagine a school app. You have a `Student` data class:

```kotlin
// A data class that holds info about one student
data class Student(
    val id: Int,
    val firstName: String,
    val lastName: String
) {
    var fullName = "$lastName, $firstName"   // e.g., "Curie, Marie"
}
```

Now, where do you keep the list of all students? You could put it in a regular class — but then nothing stops someone from accidentally creating two separate lists, each with a different set of students. That would be a bug waiting to happen.

Instead, use an `object` to create a single, shared registry:

```kotlin
// A singleton registry — only one of these will ever exist
object StudentRegistry {
    // The master list of all students
    val allStudents = mutableListOf<Student>()

    // Add a student to the registry
    fun addStudent(student: Student) {
        allStudents.add(student)
    }

    // Remove a student from the registry
    fun removeStudent(student: Student) {
        allStudents.remove(student)
    }

    // Print every student's full name
    fun listAllStudents() {
        allStudents.forEach {
            println(it.fullName)
        }
    }
}
```

Now create some students and register them:

```kotlin
fun main() {
    val marie = Student(1, "Marie", "Curie")
    val albert = Student(2, "Albert", "Einstein")
    val emmy = Student(3, "Emmy", "Noether")

    StudentRegistry.addStudent(marie)
    StudentRegistry.addStudent(albert)
    StudentRegistry.addStudent(emmy)

    StudentRegistry.listAllStudents()
    // Output:
    // Curie, Marie
    // Einstein, Albert
    // Noether, Emmy
}
```

Because `StudentRegistry` is an `object`, no matter where in your code you write `StudentRegistry.addStudent(...)`, you are always working with the **same** list. There is no risk of a second, separate registry accidentally existing.

---

## Singleton Use Case 2: A Namespace for Constants

Another great use for singletons is grouping related constants together in one place. This prevents naming conflicts and keeps your code organized.

For example, if your app talks to a server and parses JSON, you might have keys like `"id"`, `"first_name"`, and `"last_name"`. These are just plain strings, so if you scatter them around your code, typos become hard to catch.

A better approach:

```kotlin
// A named object acting as a namespace — groups related constants together
object JsonKeys {
    const val JSON_KEY_ID = "id"
    const val JSON_KEY_FIRSTNAME = "first_name"
    const val JSON_KEY_LASTNAME = "last_name"
}
```

Now instead of typing the string `"first_name"` somewhere and risking a typo, you write `JsonKeys.JSON_KEY_FIRSTNAME`. Your IDE can autocomplete it, and any mistake will be caught immediately.

---

## How Objects Compare to Classes

Named objects and classes have a lot in common. Here is a side-by-side summary:

| Feature | `class` | `object` |
|---------|---------|---------|
| Can have properties | Yes | Yes |
| Can have methods | Yes | Yes |
| Can have a constructor | Yes | **No** |
| Can inherit from a class | Yes | Yes |
| Can implement interfaces | Yes | Yes |
| Number of instances | As many as you want | **Exactly one** |
| Properties must be initialized | Yes | Yes (at declaration or in `init`) |

One important note: even though `object` has no constructor, it **can** have an `init` block. The `init` block runs once when the object is first accessed.

---

## Companion Objects — Replacing `static`

In languages like Java, the keyword `static` marks a class member (property or method) that belongs to the **class itself**, not to any individual instance.

For example, imagine a counter that tracks how many instances of a class have been created. That counter should be shared across all instances — it should not be reset every time you create a new object.

**Kotlin does not have the `static` keyword.** Instead, Kotlin uses a **companion object**.

### What Is a Companion Object?

A companion object is an `object` that lives **inside** a class. It is attached to the class, not to any particular instance. There is only ever one companion object per class.

```kotlin
class MyClass {
    companion object {
        // Anything here is shared across all instances
        var instanceCount = 0
    }
}
```

You access companion object members using the class name:

```kotlin
println(MyClass.instanceCount)   // No instance needed
```

### A Real Example: The Factory Pattern

Here is a practical use case. Suppose you want to:

1. Automatically assign a unique ID to each `Scientist` created.
2. Make sure every scientist gets a proper ID — no one can create a `Scientist` without going through the proper process.

You can do this with a **private constructor** and a **factory method** in the companion object.

A private constructor means no one outside the class can call `Scientist(...)` directly. The only way to create a new scientist is through the factory method you provide.

```kotlin
class Scientist private constructor(   // private = cannot call Scientist(...) directly
    val id: Int,
    val firstName: String,
    val lastName: String
) {
    companion object {
        // Tracks the last ID assigned — shared across all instances
        var currentId = 0

        // Factory method: the only way to create a Scientist
        fun newScientist(firstName: String, lastName: String): Scientist {
            currentId += 1   // Increment the shared counter
            return Scientist(currentId, firstName, lastName)
        }
    }

    var fullName = "$firstName $lastName"
}
```

Now create some scientists:

```kotlin
// You cannot write: val emmy = Scientist(1, "Emmy", "Noether")
// The constructor is private! You must use the factory method:
val emmy = Scientist.newScientist("Emmy", "Noether")
val isaac = Scientist.newScientist("Isaac", "Newton")
val nick = Scientist.newScientist("Nikola", "Tesla")
```

Each call to `newScientist()` automatically assigns the next ID. Emmy gets 1, Isaac gets 2, Nikola gets 3.

Now add them to a singleton repository:

```kotlin
object ScientistRepository {
    val allScientists = mutableListOf<Scientist>()

    fun addScientist(scientist: Scientist) {
        allScientists.add(scientist)
    }

    fun removeScientist(scientist: Scientist) {
        allScientists.remove(scientist)
    }

    fun listAllScientists() {
        allScientists.forEach {
            println("${it.id}: ${it.fullName}")
        }
    }
}
```

```kotlin
ScientistRepository.addScientist(emmy)
ScientistRepository.addScientist(isaac)
ScientistRepository.addScientist(nick)

ScientistRepository.listAllScientists()
// Output:
// 1: Emmy Noether
// 2: Isaac Newton
// 3: Nikola Tesla
```

### Why the Factory Pattern Is Powerful

The combination of **private constructor + companion object factory method** is a powerful design pattern. It gives you control over how objects are created.

In this case, it ensures:
- Every scientist always has a unique ID.
- No one can accidentally create a scientist by calling `Scientist(42, "Bob", "Smith")` and bypassing the counter.
- The ID logic lives in exactly one place.

### Naming a Companion Object

By default, Kotlin gives the companion object an implicit name: `Companion`. You can give it a custom name like this:

```kotlin
class Scientist private constructor(...) {
    companion object Factory {   // Named "Factory"
        // ...
    }
}
```

In Kotlin code, you never need to use the companion object's name — you just use the class name directly:

```kotlin
Scientist.newScientist("Emmy", "Noether")   // Works fine in Kotlin
```

But if you are calling Kotlin code **from Java**, you need to include the companion object name:

```java
// Java code calling Kotlin
Scientist isaac = Scientist.Factory.newScientist("Isaac", "Newton");
// Without a custom name, it would be: Scientist.Companion.newScientist(...)
```

This is a detail you mostly only need to worry about if you are mixing Kotlin and Java in the same project.

---

## Mini-Exercise: Counting Students

Update the `Student` data class to track how many student instances have been created. Use a companion object with a `numberOfStudents()` method.

**Solution:**

```kotlin
data class Student(
    val id: Int,
    val firstName: String,
    val lastName: String
) {
    var fullName = "$lastName, $firstName"

    companion object {
        // Shared counter — not tied to any individual Student instance
        private var count = 0

        // Returns how many students have been created so far
        fun numberOfStudents(): Int = count
    }

    // init runs every time a new Student is created
    init {
        count += 1   // Increment the shared counter
    }
}
```

```kotlin
val marie = Student(1, "Marie", "Curie")
val albert = Student(2, "Albert", "Einstein")

println(Student.numberOfStudents())   // Output: 2
```

Notice that `count` is in the companion object (shared, class-level), while `fullName` is in the instance body (different for each student). The `init` block runs for every new student created and increments the shared counter.

---

## Anonymous Objects — One-Off Implementations

So far you have seen:

- **Named objects** — singletons with a name, created once for the whole program.
- **Companion objects** — objects attached to a class to hold shared members.

There is a third use of the `object` keyword: **anonymous objects**.

### What Is an Anonymous Object?

Sometimes you need to implement an interface or override some behavior — but just once, for one specific purpose. You do not want to define a whole new named class just for that.

An anonymous object lets you create an implementation on the spot, without giving it a name.

This is most useful when you have an **interface** (a contract that says "any type implementing me must have these methods") and you need a quick one-time implementation.

### An Example

Suppose you have this interface:

```kotlin
// An interface — a contract. Any type implementing Counts must have these two methods.
interface Counts {
    fun studentCount(): Int
    fun scientistCount(): Int
}
```

You want an object that checks how many students and scientists are currently in the registries. You could define a whole new class — but you only need this once. Instead, use an anonymous object:

```kotlin
// Create an anonymous object that implements the Counts interface
val counter = object : Counts {

    // Provide an implementation for studentCount()
    override fun studentCount(): Int {
        return StudentRegistry.allStudents.size
    }

    // Provide an implementation for scientistCount()
    override fun scientistCount(): Int {
        return ScientistRepository.allScientists.size
    }
}

println(counter.studentCount())    // Output: 3
println(counter.scientistCount())  // Output: 3
```

The syntax is `object : InterfaceName { ... }`. Inside the braces, you write the implementations for all the required methods.

### How Anonymous Objects Differ from Named Objects

This is the key difference that trips people up:

| | Named object | Anonymous object |
|---|---|---|
| Created with | `object Name { ... }` | `object : Interface { ... }` |
| Instances | **Exactly one** (singleton) | **A new one each time** |
| Has a name | Yes | No |
| Typical use | Shared state, registries, namespaces | One-off implementations |

A named object like `StudentRegistry` is the same object everywhere in your program. An anonymous object is created fresh each time that line of code runs.

---

## Challenges

### Challenge 1: A Threshold Checker Object

Create a named object called `Threshold` that holds a threshold value and a method to check whether a given number is above it.

**Solution:**

```kotlin
object Threshold {
    // The threshold value — feel free to change this
    private val limit = 100

    // Returns true if the given value is above the limit
    fun isAboveThreshold(value: Int): Boolean {
        return value > limit
    }
}

// Usage
println(Threshold.isAboveThreshold(50))    // Output: false
println(Threshold.isAboveThreshold(150))   // Output: true
```

Because `Threshold` is a named object, there is only one threshold in the entire program — you never need to create it, and you cannot accidentally create a second one with a different limit.

---

### Challenge 2: A Student Class with a Factory Method

Create a `Student` class that uses a factory method `loadStudent(studentMap: Map<String, String>)` to create a student from a map of string keys and values. If a key is missing, default to `"First"` or `"Last"`.

**Solution:**

```kotlin
class Student private constructor(
    val firstName: String,
    val lastName: String
) {
    companion object {
        // Factory method: creates a Student from a map
        // e.g. mapOf("first_name" to "Niels", "last_name" to "Bohr")
        fun loadStudent(studentMap: Map<String, String>): Student {
            // Use the map value if present; otherwise fall back to defaults
            val firstName = studentMap["first_name"] ?: "First"
            val lastName = studentMap["last_name"] ?: "Last"
            return Student(firstName, lastName)
        }
    }

    val fullName = "$firstName $lastName"
}
```

```kotlin
// Creating a student from a complete map
val niels = Student.loadStudent(mapOf("first_name" to "Niels", "last_name" to "Bohr"))
println(niels.fullName)   // Output: Niels Bohr

// Creating a student from an incomplete map — defaults are used
val unknown = Student.loadStudent(mapOf("first_name" to "Ada"))
println(unknown.fullName)   // Output: Ada Last

// Creating a student from an empty map — both defaults used
val mystery = Student.loadStudent(emptyMap())
println(mystery.fullName)   // Output: First Last
```

The `?:` operator is the **Elvis operator** from Chapter 7. It says: "use the value on the left if it is not null; otherwise use the value on the right." This is a clean way to provide defaults when a map lookup returns null (because the key is missing).

---

### Challenge 3: An Anonymous Object Implementing ThresholdChecker

Create an anonymous object that implements this interface:

```kotlin
interface ThresholdChecker {
    val lower: Int
    val upper: Int

    // Returns true if value is HIGHER than the upper threshold
    fun isLit(value: Int): Boolean

    // Returns true if value is LOWER than the lower threshold
    fun tooQuiet(value: Int): Boolean
}
```

Use `lower = 7` and `upper = 10`.

**Solution:**

```kotlin
// An anonymous object implementing ThresholdChecker
// No class name needed — this is a one-time implementation
val checker = object : ThresholdChecker {
    override val lower: Int = 7    // Anything below 7 is too quiet
    override val upper: Int = 10   // Anything above 10 is lit

    // True if value is strictly above the upper threshold
    override fun isLit(value: Int): Boolean {
        return value > upper
    }

    // True if value is strictly below the lower threshold
    override fun tooQuiet(value: Int): Boolean {
        return value < lower
    }
}

// Test it out
println(checker.isLit(15))       // Output: true  (15 > 10)
println(checker.isLit(9))        // Output: false (9 is not above 10)
println(checker.tooQuiet(3))     // Output: true  (3 < 7)
println(checker.tooQuiet(8))     // Output: false (8 is not below 7)
println(checker.isLit(10))       // Output: false (10 is NOT above 10 — strictly greater)
println(checker.tooQuiet(7))     // Output: false (7 is NOT below 7 — strictly less)
```

Notice how the edge cases work: `isLit(10)` returns `false` because the condition is "strictly greater than" (`>`), not "greater than or equal to." Always pay attention to `>` vs `>=` when checking thresholds.

---

## Summary — Three Ways to Use `object`

Here is a clean summary of everything in this chapter:

```
object Name { ... }
```
→ Creates a **named object** (singleton). Only one instance exists for the whole program. Used for shared registries, namespaces, and global constants.

```
companion object { ... }   // inside a class
```
→ Creates a **companion object**. Attached to the class itself, not to instances. Used for factory methods, counters shared across instances, and the equivalent of Java's `static` members.

```
val x = object : SomeInterface { ... }
```
→ Creates an **anonymous object**. A one-off implementation of an interface, created fresh each time. No name, no class definition needed.

| Use case | Tool to use |
|----------|-------------|
| One shared registry or store | Named `object` |
| Constants shared across your app | Named `object` (as namespace) |
| Shared class-level counter or ID generator | `companion object` |
| Controlling how instances are created | `companion object` with private constructor |
| One-time interface implementation | Anonymous `object` |
