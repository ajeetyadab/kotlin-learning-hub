# Chapter 14: Methods

## What Is a Method?

A **method** is simply a function that lives inside a class or object.

That is it. There is no new keyword for it. No special syntax. You write `fun` exactly like you would for a regular function — but you put it inside the class body instead of outside it.

The reason methods exist is the same reason classes exist: to keep related things together. A method belongs to its class. It knows about the class's properties and can work with them directly. It does not need you to pass the object as a parameter — it is already part of it.

You have already been using methods throughout this book: `removeAt()` on lists, `add()` on mutable lists, `forEach()` on collections. All of these are methods — functions attached to a type.

```kotlin
val numbers = arrayListOf(1, 2, 3)
numbers.removeAt(numbers.lastIndex)   // removeAt() is a method on ArrayList
println(numbers)                      // Output: [1, 2]
```

---

## Methods vs Custom Getters — When to Use Which

In Chapter 13, you learned that a property can have a custom getter that runs code when you read it. That sounds a lot like a method. So what is the difference, and which should you use?

Here is a simple decision guide:

**Use a custom getter (property) when:**
- The value is quick to compute — fast, no database calls, no file reads.
- You need a setter as well as a getter (reading and writing the same "slot").
- It feels like reading an attribute of the object: `tv.diagonal`, `circle.area`.

**Use a method when:**
- The calculation is expensive — slow, reads from a database or network, or involves heavy computation.
- The operation does something beyond just returning a value — it changes state, has side effects, or takes meaningful parameters.
- It feels like asking the object to do something: `date.advance()`, `registry.addStudent(s)`.

Using a method for expensive operations is good communication. When another developer sees `circle.area` (a property), they expect it to be instant. When they see `circle.calculatePreciseArea()` (a method), they know it might take a moment. The choice signals cost.

If you are unsure, a useful rule of thumb: if the call is O(1) — constant time, trivially fast — go with a custom getter. Otherwise, use a method.

---

## From Standalone Function to Method — Step by Step

Let's walk through the natural evolution of turning a free-standing function into a proper method. This shows exactly what a method adds and why.

### Step 1: A Function That Takes the Object as a Parameter

Here is a simple date class and a standalone function that calculates months until December:

```kotlin
// A lookup array of all months in order
val months = arrayOf(
    "January", "February", "March",
    "April", "May", "June",
    "July", "August", "September",
    "October", "November", "December"
)

class SimpleDate1(var month: String)

// A standalone function — must take the date as a parameter
fun monthsUntilWinterBreak(from: SimpleDate1): Int {
    // indexOf("December") gives 11, indexOf("October") gives 9
    // Result: 11 - 9 = 2 months until December
    return months.indexOf("December") - months.indexOf(from.month)
}
```

Usage:

```kotlin
val date1 = SimpleDate1("October")
println(monthsUntilWinterBreak(date1))   // Output: 2
```

This works, but there is a problem. The function is not connected to the class in any meaningful way. You could call it with any `SimpleDate1` from anywhere. It is loose and unorganized.

### Step 2: Move It Inside the Class

Making it a method is as simple as moving the function definition inside the class body:

```kotlin
class SimpleDate2(var month: String) {
    // Same function, now inside the class — it is a method
    fun monthsUntilWinterBreak(from: SimpleDate2): Int {
        return months.indexOf("December") - months.indexOf(from.month)
    }
}
```

You call it with dot notation on an instance:

```kotlin
val date2 = SimpleDate2("October")
println(date2.monthsUntilWinterBreak(date2))   // Output: 2
```

It works — but something is awkward. You are calling the method **on** `date2`, but also **passing** `date2` as a parameter. That is redundant. The method already has access to the instance it is called on. Why pass it again?

### Step 3: Use `this` to Access the Current Instance

Inside a method, Kotlin gives you a special keyword: `this`. It refers to the object the method was called on — the current instance.

```kotlin
class SimpleDate3(var month: String) {
    fun monthsUntilWinterBreak(): Int {
        // No parameter needed — "this" IS the current instance
        return months.indexOf("December") - months.indexOf(this.month)
    }
}
```

Now the call is clean:

```kotlin
val date3 = SimpleDate3("September")
println(date3.monthsUntilWinterBreak())   // Output: 3
```

### Step 4: Drop `this` — Kotlin Infers It

Here is the cleanest version. Most of the time, you do not even need to write `this`. Kotlin is smart enough to know that `month` inside the method body refers to `this.month` — the instance's property:

```kotlin
class SimpleDate(var month: String) {
    fun monthsUntilWinterBreak(): Int {
        // Kotlin understands that "month" means "this.month"
        return months.indexOf("December") - months.indexOf(month)
    }
}
```

This is the idiomatic Kotlin style. Write `this.something` only when you need to disambiguate — for example, when a local variable and a property have the same name.

**When would you need `this` explicitly?**

```kotlin
class SimpleDate(var month: String) {
    fun updateMonth(month: String) {
        // Here, "month" is ambiguous — is it the parameter or the property?
        // You need "this.month" to make it clear you mean the property
        this.month = month   // Assign the parameter value to the property
    }
}
```

Without `this.month`, Kotlin would not know you meant the property. Here, `this` is required.

---

## Mini-Exercise: Method to Property

The `monthsUntilWinterBreak()` method just returns a single computed value with no parameters and no side effects. That is the profile of a computed property, not a method.

**Solution — convert it to a custom getter:**

```kotlin
class SimpleDate(var month: String) {
    // A computed property — feels like reading an attribute
    val monthsUntilWinterBreak: Int
        get() = months.indexOf("December") - months.indexOf(month)
}

val date = SimpleDate("October")
println(date.monthsUntilWinterBreak)   // Output: 2
```

Notice: no parentheses when accessing it. It reads like a property, not a function call — which makes it feel natural.

---

## Companion Object Methods

In Chapter 12, you learned that a **companion object** holds members that belong to the class itself, not to any individual instance. Just as companion objects can hold properties, they can also hold methods.

A companion object method is useful when the operation is about the **type in general**, not about any specific instance.

A great example is math utilities. If you have a `factorial` function, it does not belong to any particular number — it is a general math operation:

```kotlin
class MyMath {
    companion object {
        // factorial(6) = 1 × 2 × 3 × 4 × 5 × 6 = 720
        fun factorial(number: Int): Int {
            // fold() starts with 1 and multiplies each number from 1..number into it
            // Think of it as: start = 1, then 1×1, then 1×2, then 2×3, then 6×4...
            return (1..number).fold(1) { accumulator, next -> accumulator * next }
        }
    }
}
```

You call companion object methods on the class name, not on an instance:

```kotlin
println(MyMath.factorial(6))   // Output: 720
println(MyMath.factorial(4))   // Output: 24
```

### Understanding `fold()` Briefly

`fold()` is a collection function from Chapter 10. It works through a range or list, accumulating a running result. Here `fold(1)` starts the accumulator at `1`, then multiplies each number from 1 to `number` into it:

- Start: `1`
- After 1: `1 × 1 = 1`
- After 2: `1 × 2 = 2`
- After 3: `2 × 3 = 6`
- After 4: `6 × 4 = 24`
- After 5: `24 × 5 = 120`
- After 6: `120 × 6 = 720`

You could write this with a `for` loop, but `fold` expresses the intent clearly in one line.

### Using `object` as a Pure Namespace

If `MyMath` never needs to have instances — you will only ever call `MyMath.factorial()` — you do not even need the `class`/`companion object` combination. Just use a Kotlin `object`:

```kotlin
// Even simpler — MyMath as a pure singleton/namespace
object MyMath {
    fun factorial(number: Int): Int {
        return (1..number).fold(1) { acc, next -> acc * next }
    }
}

MyMath.factorial(5)   // Output: 120
```

---

## Mini-Exercise: Triangle Number

Add a `triangleNumber()` method to `MyMath`. The triangle number for `n` is the sum 1 + 2 + 3 + ... + n. For example, the triangle number for 3 is 6.

**Solution:**

```kotlin
class MyMath {
    companion object {
        fun factorial(number: Int): Int {
            return (1..number).fold(1) { acc, next -> acc * next }
        }

        // triangleNumber(4) = 1 + 2 + 3 + 4 = 10
        fun triangleNumber(number: Int): Int {
            // fold(0) starts at 0 and ADDS each number (instead of multiplying)
            return (1..number).fold(0) { acc, next -> acc + next }
        }
    }
}

println(MyMath.triangleNumber(3))   // Output: 6
println(MyMath.triangleNumber(4))   // Output: 10
println(MyMath.triangleNumber(10))  // Output: 55
```

The only difference from `factorial` is the starting value (0 instead of 1) and the operation (addition instead of multiplication).

---

## Extension Methods — Adding Methods to Classes You Don't Own

You have already seen extension **properties** in Chapter 13. Extension **methods** work the same way: you add a new method to an existing class without modifying its source code.

This is useful in two situations:
1. You are using a library class and want to add a method that the library does not provide.
2. You want to add a method to keep your own class tidy, without cluttering its definition.

### Syntax

```kotlin
fun ClassName.newMethodName(params): ReturnType {
    // Inside here, "this" refers to the instance the method is called on
}
```

### Example: Summer Break on SimpleDate

Suppose `SimpleDate` comes from a library. It already has `monthsUntilWinterBreak()`, but you want to add `monthsUntilSummerBreak()` — without touching the library code:

```kotlin
fun SimpleDate.monthsUntilSummerBreak(): Int {
    val monthIndex = months.indexOf(month)   // 'month' = 'this.month', implicit this

    return when {
        // Before or at June — count forward to June
        monthIndex <= months.indexOf("June") ->
            months.indexOf("June") - monthIndex

        // During summer (June, July, August) — already in summer break
        monthIndex <= months.indexOf("August") -> 0

        // After August — wrap around to next year's June
        else -> months.indexOf("June") + (12 - monthIndex)
    }
}
```

Use it like any other method:

```kotlin
val date = SimpleDate("December")
println(date.monthsUntilSummerBreak())   // Output: 6

val date2 = SimpleDate("March")
println(date2.monthsUntilSummerBreak())  // Output: 3

val date3 = SimpleDate("July")
println(date3.monthsUntilSummerBreak())  // Output: 0 (already in summer!)
```

The extension method behaves exactly like a method defined inside `SimpleDate`. Nobody calling it can tell the difference.

### Extension Methods on Built-In Types

You can extend any type — including built-in ones like `Int`, `String`, or `List`. For example, adding an `abs()` method to `Int`:

```kotlin
// Add abs() directly to Int
fun Int.abs(): Int {
    return if (this < 0) -this else this
}

println(4.abs())    // Output: 4
println((-4).abs()) // Output: 4
```

You can call `.abs()` directly on any integer literal or variable. Inside the extension function, `this` refers to the `Int` value the method was called on.

> **Note:** The Kotlin standard library already has an `abs()` function — so in real code you would use that. But this shows the concept clearly.

**A word of caution:** Be careful when adding extension methods to standard library types. If you add a method called `sort()` to `String`, for example, future developers reading your code might be confused about where it came from. Use this power thoughtfully.

---

## Companion Object Extensions

If your class has a companion object, you can add extension methods to the companion object too. This lets you extend the class-level (non-instance) methods — even from outside the class definition.

The syntax uses the companion object's name. If it has no custom name, Kotlin gives it the implicit name `Companion`:

```kotlin
fun MyMath.Companion.primeFactors(value: Int): List<Int> {
    // ...
}
```

### Example: `primeFactors()` for MyMath

Prime factors are the prime numbers that multiply together to give the original number. For example, the prime factors of 81 are [3, 3, 3, 3] (because 3 × 3 × 3 × 3 = 81).

```kotlin
fun MyMath.Companion.primeFactors(value: Int): List<Int> {
    var remainingValue = value       // We will divide this down as we find factors
    var testFactor = 2               // Start testing from 2 (the smallest prime)
    val primes = mutableListOf<Int>()

    // Keep dividing until testFactor² exceeds remainingValue
    // (Optimization: if testFactor² > remainingValue, remainingValue itself is prime)
    while (testFactor * testFactor <= remainingValue) {
        if (remainingValue % testFactor == 0) {
            // testFactor divides evenly — it is a prime factor
            primes.add(testFactor)
            remainingValue /= testFactor   // Divide it out and continue
        } else {
            // Does not divide evenly — try the next number
            testFactor += 1
        }
    }

    // If there is anything left over, it is a prime factor itself
    if (remainingValue > 1) {
        primes.add(remainingValue)
    }

    return primes
}
```

**Walking through `primeFactors(81)` step by step:**

- `testFactor = 2`: 81 ÷ 2 = 40.5 — does not divide evenly. Try 3.
- `testFactor = 3`: 81 ÷ 3 = 27 — divides! Add 3. `remainingValue` = 27.
- `testFactor = 3`: 27 ÷ 3 = 9 — divides! Add 3. `remainingValue` = 9.
- `testFactor = 3`: 9 ÷ 3 = 3 — divides! Add 3. `remainingValue` = 3.
- Now `testFactor² = 9 > 3 = remainingValue` — loop ends.
- `remainingValue = 3 > 1` — add 3 as the final factor.
- Result: `[3, 3, 3, 3]`.

```kotlin
println(MyMath.primeFactors(81))    // Output: [3, 3, 3, 3]
println(MyMath.primeFactors(12))    // Output: [2, 2, 3]  (2×2×3 = 12)
println(MyMath.primeFactors(100))   // Output: [2, 2, 5, 5]
```

You have added a sophisticated method to `MyMath` without touching its original definition at all.

---

## Challenges

### Challenge 1: `grow()` on Circle

Given this `Circle` class:

```kotlin
import kotlin.math.PI

class Circle(var radius: Double = 0.0) {
    val area: Double
        get() = PI * radius * radius
}
```

Write a method `grow(factor: Int)` that multiplies the circle's area by the given factor. For example, `circle.grow(factor = 3)` should triple the area.

**Solution:**

The area of a circle is `PI * r²`. If you want to triple the area, the new radius must satisfy: `PI * newR² = 3 × PI * r²`. Simplifying: `newR² = 3 × r²`, so `newR = r × √factor`.

```kotlin
import kotlin.math.PI
import kotlin.math.sqrt

class Circle(var radius: Double = 0.0) {
    // area is now var so we can add a setter
    var area: Double
        get() = PI * radius * radius
        set(newArea) {
            // Work backwards: area = PI * r², so r = sqrt(area / PI)
            radius = sqrt(newArea / PI)
        }

    // Multiply the area by a factor
    fun grow(factor: Int) {
        area *= factor   // This triggers the setter, which updates radius
    }
}
```

```kotlin
val circle = Circle(radius = 5.0)
println(circle.area)       // Output: ~78.54

circle.grow(factor = 3)
println(circle.area)       // Output: ~235.62 (tripled)
println(circle.radius)     // Output: ~8.66 (radius adjusted automatically)
```

The setter handles the reverse calculation: when you set `area`, it figures out what `radius` must be to produce that area.

---

### Challenge 2: Fix `advance()` for Month Boundaries

The naive `advance()` just adds 1 to the day. It breaks at the end of a month:

```kotlin
var date = SimpleDate(month = "December", day = 31)
date.advance()
// date.month is "December" — should be "January"!
// date.day is 32 — should be 1!
```

Rewrite `advance()` to handle month rollovers, including the December → January wraparound.

**Solution:**

We need a way to know how many days are in each month. For simplicity, we will use approximate days (ignoring leap years):

```kotlin
val months = arrayOf(
    "January", "February", "March",
    "April", "May", "June",
    "July", "August", "September",
    "October", "November", "December"
)

// Days in each month (ignoring leap years)
val daysInMonth = arrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)

class SimpleDate(var month: String, var day: Int = 0) {

    fun advance() {
        val monthIndex = months.indexOf(month)

        // Check if we've hit the last day of this month
        if (day < daysInMonth[monthIndex]) {
            // Still within the month — just go to the next day
            day += 1
        } else {
            // Last day of the month — roll over to the 1st of the next month
            day = 1
            // If December, wrap around to January; otherwise just go to next month
            month = if (monthIndex == 11) {
                months[0]   // December → January
            } else {
                months[monthIndex + 1]
            }
        }
    }
}
```

```kotlin
var date = SimpleDate(month = "December", day = 31)
date.advance()
println(date.month)   // Output: January
println(date.day)     // Output: 1

var date2 = SimpleDate(month = "October", day = 15)
date2.advance()
println(date2.month)  // Output: October
println(date2.day)    // Output: 16

var date3 = SimpleDate(month = "January", day = 31)
date3.advance()
println(date3.month)  // Output: February
println(date3.day)    // Output: 1
```

---

### Challenge 3: `MyMath` Object with `isEven` and `isOdd`

Create a Kotlin `object` named `MyMath` with `isEven()` and `isOdd()` methods.

**Solution:**

```kotlin
object MyMath {
    // Returns true if the number is divisible by 2 (no remainder)
    fun isEven(number: Int): Boolean = number % 2 == 0

    // Returns true if the number is NOT divisible by 2
    fun isOdd(number: Int): Boolean = number % 2 != 0
}

println(MyMath.isEven(4))    // Output: true
println(MyMath.isEven(7))    // Output: false
println(MyMath.isOdd(9))     // Output: true
println(MyMath.isOdd(10))    // Output: false
```

Using a named `object` (not a class) is appropriate here because you never need an instance of `MyMath` — you just need a place to group related utility functions.

---

### Challenge 4: `isEven()` and `isOdd()` as Extension Methods on `Int`

Add these directly to `Int` so you can call them on any integer:

**Solution:**

```kotlin
// Extend Int directly — 'this' refers to the Int value
fun Int.isEven(): Boolean = this % 2 == 0
fun Int.isOdd(): Boolean = this % 2 != 0

println(4.isEven())     // Output: true
println(7.isEven())     // Output: false
println(9.isOdd())      // Output: true
println((-6).isEven())  // Output: true
```

This is elegant and reads naturally: `4.isEven()` is very clear. However, adding methods to built-in types like `Int` can confuse other developers who do not know you defined it. Use this style when the added method is clearly named and does exactly what it says.

---

### Challenge 5: `primeFactors()` as an Extension on `Int`

Add `primeFactors()` directly to `Int`:

**Solution:**

```kotlin
// Extension method on Int — 'this' is the Int value being factored
fun Int.primeFactors(): List<Int> {
    var remainingValue = this   // Start with the number itself
    var testFactor = 2
    val primes = mutableListOf<Int>()

    while (testFactor * testFactor <= remainingValue) {
        if (remainingValue % testFactor == 0) {
            primes.add(testFactor)
            remainingValue /= testFactor
        } else {
            testFactor += 1
        }
    }

    if (remainingValue > 1) {
        primes.add(remainingValue)
    }

    return primes
}
```

```kotlin
println(81.primeFactors())    // Output: [3, 3, 3, 3]
println(12.primeFactors())    // Output: [2, 2, 3]
println(100.primeFactors())   // Output: [2, 2, 5, 5]
println(13.primeFactors())    // Output: [13]  (13 is prime — only factor is itself)
```

> This is a good candidate for an extension method (rather than a custom getter on a property) because it involves a loop and actual computation — it is not O(1). Using a method signals to other developers that this call has a cost.

---

## Summary — Four Kinds of Methods

| Type | Where defined | How called | Used for |
|------|--------------|------------|----------|
| Instance method | Inside `class` or `object` body | `instance.methodName()` | Behaviour tied to one specific object |
| Companion object method | Inside `companion object { }` | `ClassName.methodName()` | Behaviour about the type in general |
| Extension method | Outside the class: `fun ClassName.method()` | `instance.methodName()` — looks identical | Adding methods to classes you don't own |
| Companion extension | Outside: `fun ClassName.Companion.method()` | `ClassName.methodName()` | Adding class-level methods externally |
