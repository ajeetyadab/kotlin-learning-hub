# Chapter 13: Properties

## What Are Properties, Really?

You already know that a class groups data and behaviour together. The data part — the pieces of information stored inside a class — are called **properties**.

You have been using properties since Chapter 11. But there is much more to them than just storing a value. In Kotlin, properties can:

- Store a plain value (like a name or a number).
- Compute their value on the fly, every time you ask for it.
- Automatically react when their value changes.
- Delay their setup until the moment you first need them.
- Be added to classes you did not write — even library classes.

This chapter walks through all of these capabilities, one at a time.

---

## Constructor Properties

The simplest kind of property is one declared directly in the class constructor. You have seen this pattern many times already.

Imagine you are building an address book app. The basic unit is a contact — someone with a name and an email address:

```kotlin
// A simple class with two properties in the constructor
class Contact(var fullName: String, var emailAddress: String)
```

Both properties use `var`, which means they can be changed after the object is created.

Create a contact and access its properties:

```kotlin
val contact = Contact(
    fullName = "Grace Murray",
    emailAddress = "grace@navy.mil"
)

val name = contact.fullName          // "Grace Murray"
val email = contact.emailAddress     // "grace@navy.mil"
```

You use **dot notation** (`contact.fullName`) to read a property's value.

You can update the value the same way:

```kotlin
// Grace got married and changed her last name
contact.fullName = "Grace Hopper"
val grace = contact.fullName        // "Grace Hopper"
```

### Making a Property Read-Only with `val`

Sometimes a property should never change after the object is created. For a contact, an email address is a good candidate — once set, it identifies the person.

Change `emailAddress` to `val`:

```kotlin
class Contact2(var fullName: String, val emailAddress: String)
```

Now try to change the email address:

```kotlin
val contact2 = Contact2(
    fullName = "Grace Murray",
    emailAddress = "grace@navy.mil"
)

// ERROR: Val cannot be reassigned
contact2.emailAddress = "grace@gmail.com"
```

Kotlin will not compile this. `val` on a property is a compile-time guarantee: this value will not change. Use `val` whenever you know the data should stay fixed.

---

## Default Values

Sometimes you can make a reasonable assumption about what a property's starting value should be. In that case, you can give it a **default value** right in the definition.

A contact type is a good example. Most people are friends first:

```kotlin
class Contact3(
    var fullName: String,
    val emailAddress: String,
    var type: String = "Friend"   // default: every contact starts as a Friend
)
```

Now you can create a contact without specifying the type:

```kotlin
val contact3 = Contact3(
    fullName = "Grace Murray",
    emailAddress = "grace@navy.mil"
)
// contact3.type is "Friend" automatically
```

If the contact is actually a work contact, you can either pass it in directly:

```kotlin
val workContact = Contact3(
    fullName = "Grace Murray",
    emailAddress = "grace@navy.mil",
    type = "Work"   // override the default
)
```

Or change it afterwards:

```kotlin
contact3.type = "Work"
```

Default values save you from having to pass the same value every time when most cases share the same starting point.

---

## Property Initializers

Not every property needs to be declared in the constructor. You can also define and initialize properties **inside the class body**.

This is useful when a property's value depends on other properties, or when you want to compute it once at creation time.

### Initializing in the Body

```kotlin
class Person(val firstName: String, val lastName: String) {
    // fullName is computed once when the object is created,
    // using the values from the constructor
    val fullName = "$firstName $lastName"
}
```

```kotlin
val person = Person("Grace", "Hopper")
println(person.fullName)   // Output: Grace Hopper
```

`fullName` is not passed in — it is assembled from `firstName` and `lastName` automatically.

### Initializing in the `init` Block

Some properties cannot be initialized with a simple one-liner. For more complex setup, use the `init` block:

```kotlin
class Address {
    var address1: String           // no initial value yet
    var address2: String? = null   // optional — starts as null
    var city = ""                  // starts as empty string
    var state: String              // no initial value yet

    init {
        // Properties without initial values MUST be set here
        address1 = ""
        state = ""
    }
}
```

Because every property in `Address` gets a value either at declaration or in `init`, you can create an `Address` with no arguments:

```kotlin
val address = Address()   // Works — all properties are initialized
```

**Rule:** Every non-nullable property must have a value before the object finishes being created. You can set it at declaration, in the constructor, or in `init` — but it must be set somewhere.

---

## Custom Accessors

So far, properties have stored their value in a plain storage slot (called a **backing field**). But properties can also be defined with custom code that runs when you read or write them. This custom code is called an **accessor**.

There are two kinds of accessors: a **getter** (runs when you read the property) and a **setter** (runs when you write to it).

### Custom Getter — Computed Properties

Some values are not stored directly — they are calculated from other stored values. A **custom getter** lets a property compute its value on demand.

A perfect example: the size of a TV screen. The industry measures screens by their **diagonal** length, not by height or width. But height and width are what you actually know. So `diagonal` should be computed from them.

```kotlin
import kotlin.math.roundToInt
import kotlin.math.sqrt

class TV(var height: Double, var width: Double) {

    // diagonal is NOT stored anywhere — it is calculated fresh every time you read it
    val diagonal: Int
        get() {
            // Pythagorean theorem: diagonal² = height² + width²
            val result = sqrt(height * height + width * width)
            // roundToInt() rounds 109.99 to 110, not truncates it to 109
            return result.roundToInt()
        }
}
```

Notice: there is no `= someValue` after `diagonal`. Instead, there is a `get()` block. Kotlin runs this block every time you ask for `diagonal`.

```kotlin
val tv = TV(height = 53.93, width = 95.87)
val size = tv.diagonal   // Output: 110 (a 110-inch TV)
```

What happens when you change the width?

```kotlin
tv.width = tv.height          // Make the TV square
val diagonal = tv.diagonal    // Output: 76
```

The computed property automatically reflects the new dimensions. You do not need to manually update `diagonal` — it always re-calculates from the current `height` and `width`.

**Key point:** A property with only a custom getter has no storage of its own. It recalculates every time it is accessed. This is sometimes called a **computed property**.

---

### Custom Setter — Writing to a Computed Property

A custom getter makes a property readable. A **custom setter** makes a computed property writable too.

But here is where it gets interesting: since `diagonal` has no storage of its own, you cannot actually "store" a diagonal value. So what does the setter do?

It works backwards. If you tell the TV "I want a 70-inch screen," the setter calculates what `height` and `width` need to be to produce that diagonal — and stores those.

Think of it like ordering a custom TV: you tell the shop the screen size you want, and they figure out the physical dimensions.

Update the TV class to make `diagonal` read-write:

```kotlin
class TV(var height: Double, var width: Double) {

    // Changed from val to var because we're adding a setter
    var diagonal: Int
        get() {
            val result = sqrt(height * height + width * width)
            return result.roundToInt()
        }
        set(value) {
            // Assume a standard 16:9 aspect ratio
            val ratioWidth = 16.0
            val ratioHeight = 9.0

            // Calculate the diagonal of a 16:9 rectangle (using Pythagorean theorem)
            val ratioDiagonal = sqrt(ratioWidth * ratioWidth + ratioHeight * ratioHeight)

            // Work backwards: given the diagonal we want, what height and width does that give?
            height = value.toDouble() * ratioHeight / ratioDiagonal
            width = height * ratioWidth / ratioHeight
            // Note: no return statement — setters only SET, they don't return
        }
}
```

Here is what each part does:

- `set(value)` — `value` is whatever you assign: `tv.diagonal = 70` makes `value` be `70`.
- Since `value` is an `Int`, we call `.toDouble()` to convert it before the math.
- The setter works out `height` and `width` assuming a standard 16:9 ratio, then stores them.
- There is no `return` in a setter — setters only store values, they never return one.

Try it out:

```kotlin
tv.diagonal = 70
println(tv.height)   // Output: ~34.32
println(tv.width)    // Output: ~61.01
```

Now you have a TV size calculator. Want to know the height of a 55-inch TV? Set `tv.diagonal = 55` and read `tv.height`.

---

## Companion Object Properties

Properties defined directly in a class belong to **each instance** separately. Your TV's `height` is different from your neighbour's TV's `height`.

But sometimes you need a property that belongs to the **class itself** — shared by all instances. In Kotlin, these go in the **companion object** (which you learned about in Chapter 12).

Imagine a game with multiple levels:

```kotlin
class Level(
    val id: Int,
    var boss: String,
    var unlocked: Boolean
) {
    companion object {
        // This tracks the highest level ever reached — shared across ALL level instances
        var highestLevel = 1
    }
}
```

Create some levels:

```kotlin
val level1 = Level(id = 1, boss = "Chameleon", unlocked = true)
val level2 = Level(id = 2, boss = "Squid", unlocked = false)
val level3 = Level(id = 3, boss = "Chupacabra", unlocked = false)
val level4 = Level(id = 4, boss = "Yeti", unlocked = false)
```

You access `highestLevel` on the **class**, not on any instance:

```kotlin
val highestLevel = Level.highestLevel   // Output: 1

// This would be an ERROR — companion properties don't live on instances:
// val highestLevel = level3.highestLevel   // Won't compile
```

This is the key difference: `level1.boss` gives you level 1's boss. `Level.highestLevel` gives you the game-wide progress tracker.

### `@JvmStatic` for Java Interop

If your Kotlin code is used from Java (for example, in an Android project where you mix languages), you can add `@JvmStatic` to a companion property to make the Java call cleaner:

```kotlin
companion object {
    @JvmStatic var highestLevel = 1
}
```

Without `@JvmStatic`, Java code must write `Level.Companion.getHighestLevel()`. With it, Java can write just `Level.getHighestLevel()`. In Kotlin, there is no difference — you always write `Level.highestLevel` either way.

---

## Delegated Properties

Everything you have seen so far has been either: store a value directly, or compute it in a getter/setter. But there is a third option: **delegate** the property's behaviour to a helper object.

The idea of delegation is simple. Instead of writing the getter/setter logic yourself, you say: "I'm going to let another object handle this property for me." That other object takes care of what happens when the property is read or written.

Kotlin provides several built-in delegates in the `kotlin.properties.Delegates` class.

```kotlin
import kotlin.properties.Delegates
```

### Observing Changes — `Delegates.observable`

Sometimes you want to **react** when a property changes. For example, when a player unlocks a new level, you want to automatically update the `highestLevel` tracker.

You could write a custom setter to do this — but then you would need to remember to call the update logic every time. A delegated `observable` handles this automatically.

```kotlin
class DelegatedLevel(val id: Int, var boss: String) {
    companion object {
        var highestLevel = 1   // Shared tracker for all instances
    }

    // unlocked is delegated to Delegates.observable
    // - First argument (false): the initial value
    // - Lambda: runs AFTER the value changes, with old and new values
    var unlocked: Boolean by Delegates.observable(false) { _, old, new ->
        // If the new value is "true" and this level is higher than the record...
        if (new && id > highestLevel) {
            highestLevel = id   // ...update the record
        }
        println("unlocked changed: $old -> $new")
    }
}
```

Try it:

```kotlin
val delegatedLevel1 = DelegatedLevel(id = 1, boss = "Chameleon")
val delegatedLevel2 = DelegatedLevel(id = 2, boss = "Squid")

println(DelegatedLevel.highestLevel)   // Output: 1

delegatedLevel2.unlocked = true        // Prints: unlocked changed: false -> true

println(DelegatedLevel.highestLevel)   // Output: 2 (automatically updated!)
```

The lambda receives three parameters: the property itself (which we ignore using `_`), the old value, and the new value. The lambda runs **after** the change, so `new` has the updated value.

---

### Limiting Values — `Delegates.vetoable`

`observable` runs after the change and cannot stop it. `vetoable` runs before the change and can **veto** (reject) it.

The lambda for `vetoable` returns `true` to accept the change, or `false` to reject it and revert to the previous value.

Imagine a light bulb that can only handle a maximum electrical current:

```kotlin
class LightBulb {
    companion object {
        const val maxCurrent = 40   // Maximum safe current (amps)
    }

    // current starts at 0 and is limited to maxCurrent
    var current by Delegates.vetoable(0) { _, _, new ->
        if (new > maxCurrent) {
            println("Current too high, falling back to previous setting.")
            false   // REJECT the change — current stays at its previous value
        } else {
            true    // ACCEPT the change
        }
    }
}
```

Try it:

```kotlin
val light = LightBulb()

light.current = 50                    // Prints: Current too high, falling back to previous setting.
println(light.current)                // Output: 0 (rejected — still at initial value)

light.current = 40
println(light.current)                // Output: 40 (accepted — within limit)
```

When you set `current = 50`, the lambda returns `false`, so the change is rejected and `current` stays at `0`. When you set `current = 40`, the lambda returns `true`, so the change goes through.

**Quick comparison:**

| Delegate | When lambda runs | Can stop the change? |
|----------|-----------------|----------------------|
| `observable` | After the change | No — change already happened |
| `vetoable` | Before the change | Yes — return `false` to reject |

> **Important:** Do not confuse delegated property observers with custom getters and setters. They are completely different mechanisms. A property can have one or the other, not both.

---

### Lazy Properties — Delay Until Needed

Some calculations are expensive — they take a meaningful amount of time or memory. If the result might never be needed, why calculate it upfront?

**Lazy properties** solve this. They are declared with `by lazy { ... }`, and the code inside the braces runs exactly once — the first time you access the property. After that, the result is stored and returned instantly on every subsequent access.

A good analogy: imagine a restaurant that only cooks your dessert when you order it, not when you walk in the door. Until you ask for dessert, no work is done. Once it is prepared, it is ready instantly if you want seconds.

Here is a `Circle` class that calculates pi using a precise formula rather than using the standard library value:

```kotlin
class Circle(var radius: Double = 0.0) {

    // pi is lazy — the formula does NOT run when the Circle is created.
    // It runs the first time pi is accessed, then the result is stored forever.
    val pi: Double by lazy {
        // Machin's formula for pi — a famous high-precision calculation
        ((4.0 * Math.atan(1.0 / 5.0)) - Math.atan(1.0 / 239.0)) * 4.0
    }

    // circumference is a regular computed property (custom getter)
    // It re-calculates every time it is accessed (because radius can change)
    val circumference: Double
        get() = pi * radius * 2
}
```

Watch the timing:

```kotlin
val circle = Circle(5.0)
// At this point, pi has NOT been calculated yet. The lazy block has not run.

val c = circle.circumference   // NOW pi is calculated for the first time
// Output: ~31.42

// From now on, accessing pi returns the already-computed value instantly.
```

Why is `circumference` not lazy? Because `radius` can change. If you change the radius, the circumference must re-calculate. But pi is always the same number — once calculated, you never need to recalculate it.

**Rule:** Use `by lazy` for a property that:
- Is expensive to compute.
- Does not change once calculated.
- Might not be needed at all.

Because the value never changes after the first calculation, lazy properties must be declared with `val`, not `var`.

---

### Mini-Exercises: Improving the Circle Class

The lazy `pi` calculation above is interesting, but in real code you should use `kotlin.math.PI` from Kotlin's standard library. Here is how to improve the class:

```kotlin
import kotlin.math.PI

class Circle(var radius: Double = 0.0) {

    // 1. No more lazy pi — use the standard library constant
    // val pi: Double by lazy { ... }   <- removed

    // 2. circumference uses PI directly
    val circumference: Double
        get() = PI * radius * 2

    // 3. NEW: area is computed lazily because it could be expensive if radius were complex
    //    area = pi * r²
    val area: Double by lazy {
        PI * radius * radius
    }
}
```

```kotlin
val circle = Circle(5.0)
println(circle.circumference)   // Output: ~31.42 (recalculates if radius changes)
println(circle.area)            // Output: ~78.54 (calculated once, then stored)
```

---

## `lateinit` — "I'll Set This Later"

Normally, every non-nullable property must have a value the moment the object is created. But sometimes that is not possible. Maybe you are creating an object that will receive a dependency later — after construction.

`lateinit` tells Kotlin: "I promise I will set this before I use it. Trust me."

```kotlin
class Lamp {
    // bulb has no value yet — will be assigned later
    lateinit var bulb: LightBulb
}
```

Two important rules:
- `lateinit` can only be used with `var`, not `val` (because the whole point is to assign it later).
- `lateinit` only works with non-nullable reference types (not `Int`, `Double`, `Boolean`, etc. — those have default values and do not need lateinit).

Here is what happens when you forget to assign the value before using it:

```kotlin
val lamp = Lamp()
// At this point, bulb has no value

println(lamp.bulb)
// CRASH: kotlin.UninitializedPropertyAccessException:
//        lateinit property bulb has not been initialized
```

This crash is actually helpful — it tells you exactly what went wrong, instead of silently giving you a null or a garbage value.

Once you assign a value, everything works normally:

```kotlin
lamp.bulb = LightBulb()
// Now lamp.bulb is safe to use
lamp.bulb.current = 30
```

**When to use `lateinit` vs `lazy`:**

| | `lateinit` | `by lazy` |
|--|-----------|-----------|
| Assigned by | You, manually, at some point after creation | Kotlin, automatically, on first access |
| `var` or `val` | Must be `var` | Must be `val` |
| Nullable? | No — the property is non-nullable | No |
| Type restriction | Reference types only (no `Int`, `Double`) | Any type |
| Use case | Dependency injection, Android views | Expensive computations, deferred setup |

---

## Extension Properties

You already know that Kotlin lets you add **extension functions** to existing classes. The same idea works for properties: you can add a **property** to a class without modifying its source code.

This is especially useful when you are using a class from a library that you did not write and cannot change.

Recall the `Circle` class, which has `radius` and `circumference`. But what about `diameter`? Diameter is simply twice the radius — a trivially useful property to have.

If you cannot edit the `Circle` class, add an **extension property**:

```kotlin
// Syntax: val ClassName.propertyName: Type
//             get() = ...
val Circle.diameter: Double
    get() = 2.0 * radius   // 'radius' is accessed just as if we were inside the class
```

Use it exactly like any other property:

```kotlin
val unitCircle = Circle(1.0)
println(unitCircle.diameter)   // Output: 2.0

val bigCircle = Circle(10.0)
println(bigCircle.diameter)    // Output: 20.0
```

**Important limitation:** Extension properties do not have a **backing field**. They cannot store any data of their own. This means:
- You can only define them using a custom getter (and optionally a custom setter).
- They cannot store state — they can only compute a value from what the class already has.

---

## Challenges

### Challenge 1: Rewrite IceCream with Defaults and Lazy

The original `IceCream` class has uninitialized properties:

```kotlin
class IceCream {
    val name: String
    val ingredients: ArrayList<String>
}
```

Rewrite it to:
1. Give `name` a default value.
2. Lazily initialize the `ingredients` list (it may be expensive to set up, and not every ice cream object necessarily needs its ingredients list right away).

**Solution:**

```kotlin
class IceCream(
    val name: String = "Vanilla"   // 1. Default name — "Vanilla" if not specified
) {
    // 2. ingredients list is created lazily — only when first accessed
    //    Creating an ArrayList is cheap here, but the pattern is important to understand
    val ingredients: ArrayList<String> by lazy {
        println("Initializing ingredients list...")
        ArrayList<String>()
    }
}
```

```kotlin
val scoop = IceCream()
println(scoop.name)               // Output: Vanilla
                                  // "Initializing ingredients list..." has NOT printed yet

scoop.ingredients.add("cream")    // NOW the lazy block runs: "Initializing ingredients list..."
scoop.ingredients.add("sugar")    // Subsequent accesses use the already-created list

println(scoop.ingredients)        // Output: [cream, sugar]
```

A named scoop:

```kotlin
val chocolate = IceCream("Chocolate")
println(chocolate.name)           // Output: Chocolate
```

---

### Challenge 2: FuelTank with Delegated Observer

You are given a simple `FuelTank`:

```kotlin
class FuelTank {
    var level = 0.0   // decimal between 0 (empty) and 1 (full)
}
```

Rewrite it to:
1. Add a `lowFuel` Boolean property.
2. Automatically flip `lowFuel` to `true` when `level` drops below 10% (0.1).
3. Turn `lowFuel` back to `false` when the level rises back above 10%.
4. Add a `FuelTank` to the `Car` class, then fill and drain the tank.

**Solution:**

```kotlin
import kotlin.properties.Delegates

class FuelTank {
    var lowFuel = false   // Warning light — off by default

    // level is observed — the lambda runs every time level changes
    var level: Double by Delegates.observable(0.0) { _, _, new ->
        // If level drops below 10%, turn on the warning
        // If level rises back to 10% or above, turn off the warning
        lowFuel = new < 0.1
    }
}

class Car(val make: String, val color: String) {
    val fuelTank = FuelTank()   // Every car has its own fuel tank
}
```

```kotlin
val car = Car("Tesla", "Red")

// Fill the tank to 100%
car.fuelTank.level = 1.0
println("Low fuel warning: ${car.fuelTank.lowFuel}")   // false — tank is full

// Drive for a while — burn through fuel
car.fuelTank.level = 0.5
println("Low fuel warning: ${car.fuelTank.lowFuel}")   // false — still half full

car.fuelTank.level = 0.08
println("Low fuel warning: ${car.fuelTank.lowFuel}")   // true — below 10%!

// Find a gas station and refuel
car.fuelTank.level = 0.5
println("Low fuel warning: ${car.fuelTank.lowFuel}")   // false — warning clears automatically
```

Every time `level` changes, the `observable` lambda runs and re-evaluates whether the `lowFuel` flag should be on. No manual logic needed — the observer handles it automatically.

---

## Summary — All Property Types at a Glance

| Property type | How it works | When to use |
|---------------|--------------|-------------|
| `var` property | Stores a value, readable and writable | Most properties |
| `val` property | Stores a value, read-only after creation | Immutable data |
| Default value | Property starts with a given value | Optional parameters with a sensible default |
| Custom getter | Computed on every read, no storage | Value derived from other properties |
| Custom setter | Runs code on write, sets other properties | Writing one value should update others |
| Companion object property | Shared across all instances, accessed via class name | Game scores, counters, shared state |
| `by Delegates.observable` | Lambda runs after every change | Reacting to changes automatically |
| `by Delegates.vetoable` | Lambda runs before change; can reject it | Enforcing rules or limits |
| `by lazy` | Computed once on first access, stored | Expensive setup that might not be needed |
| `lateinit var` | Declared now, assigned later | Dependencies set after object creation |
| Extension property | Added to a class you do not own | Adding computed properties to library classes |
