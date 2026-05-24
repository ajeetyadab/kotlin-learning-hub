# Chapter 8: Arrays & Lists

---

## Why Do We Need Collections?

Imagine you're building a quiz app. You need to store five questions.

Without collections, you'd write this:

```kotlin
val question1 = "What is the capital of France?"
val question2 = "What is 2 + 2?"
val question3 = "What colour is the sky?"
val question4 = "How many days in a week?"
val question5 = "What is the boiling point of water?"
```

That's five separate variables for five questions. Now imagine the quiz has 50 questions. Or 500. You'd need 500 separate variables, all named slightly differently, and every time you wanted to loop through them or count them, you'd be writing repetitive, messy code.

This is the problem that **collections** solve. A collection is a single variable that holds multiple values. Instead of 500 variables, you have one — and it can hold all 500 questions.

Kotlin has several types of collections. This chapter covers the two most commonly used: **arrays** and **lists**.

---

## Part 1: Arrays

### What Is an Array?

An array is a collection that stores multiple values of the **same type**, in a **fixed order**, in a **fixed size**.

Think of an array like a **row of numbered lockers** in a school. Each locker holds exactly one item. The lockers are numbered from left to right starting at 0. The number of lockers is decided when you build the hallway — you can't add or remove lockers later. But you can swap out what's *inside* each locker.

```
Locker:   [0]    [1]    [2]    [3]    [4]
Content: "Anna" "Bob" "Cindy" "Dan"  "Eli"
```

Three important things to notice:
- All items are the same type (`String`)
- They are in a specific, numbered order
- The same value can appear more than once (two lockers could both say "Anna")

### Zero-Indexing — Why Counting Starts at 0

In everyday life, you count from 1. In programming, you count from 0.

This surprises beginners every time. Here's why it works this way:

The index represents the **distance from the start**. The first element is 0 steps from the start, so its index is 0. The second is 1 step away, so index 1. And so on.

```
Index:     0       1       2       3       4
Value:  "Anna"  "Bob"  "Cindy"  "Dan"   "Eli"
```

A 5-element array has indices 0 through 4. The last index is always `size - 1`. So if you have 5 elements, the last one is at index 4 — not 5. Trying to access index 5 would be stepping outside the hallway into thin air, which causes a crash.

---

### Creating an Array

The easiest way to create an array in Kotlin is with `arrayOf()`:

```kotlin
// Creates an array of 4 integers.
// Kotlin infers the type as Array<Int> automatically.
val evenNumbers = arrayOf(2, 4, 6, 8)

// Creates an array of 5 strings.
// Type is inferred as Array<String>.
val vowels = arrayOf("a", "e", "i", "o", "u")
```

`arrayOf()` is a standard library function that takes whatever values you give it and bundles them into an array.

The type `Array<Int>` means: "an array whose elements are all `Int`". The `<Int>` part is called a **type argument** — it specifies what type of things the array holds. You'll learn more about this syntax later in the generics chapter. For now, just know that Kotlin infers it automatically from what you put inside.

**Creating an array with a default value:**

If you need an array where all elements start as the same value, you can use the `Array` constructor:

```kotlin
// Creates an array of 5 elements, all set to the value 5.
// Result: [5, 5, 5, 5, 5]
val fiveFives = Array(5) { 5 }
```

The `{ 5 }` part is a lambda — a small block of code that returns the default value. You'll learn lambdas in detail later. For now, just know this pattern creates an array where every slot starts with the same value.

**Use `val` for arrays you won't replace:**

```kotlin
// 'val' means the variable 'vowels' always points to THIS array.
// The elements inside can still be updated, but you can't swap the whole array out.
val vowels = arrayOf("a", "e", "i", "o", "u")
```

---

### `Array<Int>` vs `IntArray` — Boxed vs Primitive

This is a slightly advanced concept, but important to know.

When Kotlin runs on a computer or Android device, it runs on the **JVM** (Java Virtual Machine). The JVM has two categories of numbers:
- **Primitive** `int` — stored directly as a raw number in memory. Fast and memory-efficient.
- **Boxed** `Integer` — stored as an object wrapping a number. Slightly slower and uses more memory.

When you write `Array<Int>`, Kotlin uses the boxed `Integer` type under the hood. This is fine for most purposes, but if you're storing millions of numbers and performance matters, it's worth using primitive arrays instead.

Kotlin provides dedicated functions for primitive arrays:

```kotlin
// Primitive int array — faster, less memory
val oddNumbers = intArrayOf(1, 3, 5, 7)    // type: IntArray

// Primitive double array
val prices = doubleArrayOf(9.99, 4.99, 2.49)   // type: DoubleArray

// Primitive boolean array
val flags = booleanArrayOf(true, false, true)   // type: BooleanArray

// Creates a DoubleArray of size 4, all values default to 0.0
val zeros = DoubleArray(4)    // [0.0, 0.0, 0.0, 0.0]
```

You can also convert between the two forms when needed:

```kotlin
// Convert Array<Int> to IntArray
val boxedArray = arrayOf(1, 3, 5, 7)      // Array<Int>
val primitiveArray = boxedArray.toIntArray()   // IntArray
```

**When to use which:**

| Use | When |
|---|---|
| `arrayOf()` / `Array<Int>` | General use, readability, working with lists |
| `intArrayOf()` / `IntArray` | Performance-critical code with many numbers |

For everyday learning and app development, `arrayOf()` is perfectly fine. You won't need to think about boxing/unboxing until you're optimising real applications.

---

### Iterating Over an Array

**Iterating** means going through each element one by one. The `for` loop is the standard way to do this:

```kotlin
val fruits = arrayOf("apple", "banana", "cherry")

// 'fruit' is a temporary variable that holds each element in turn
for (fruit in fruits) {
    println(fruit)   // prints each fruit on a new line
}
// Output:
// apple
// banana
// cherry
```

On the first loop: `fruit = "apple"`. On the second: `fruit = "banana"`. On the third: `fruit = "cherry"`. Then it stops.

**If you also need the index:**

```kotlin
for ((index, fruit) in fruits.withIndex()) {
    println("$index: $fruit")
}
// Output:
// 0: apple
// 1: banana
// 2: cherry
```

`withIndex()` gives you both the position and the value together. The `(index, fruit)` syntax unpacks them into two separate variables — this is called **destructuring**.

**Alternative — forEach:**

```kotlin
// forEach is another way to iterate — you'll learn its full syntax in the Lambdas chapter
fruits.forEach { fruit ->
    println(fruit)
}
```

Both the `for` loop and `forEach` produce the same result. For now, use whichever feels clearer.

---

## Part 2: Lists

### How Lists Differ from Arrays

Lists and arrays are very similar — both hold multiple values of the same type in order. The key difference is **size**:

| | Array | List |
|---|---|---|
| Size | Fixed — set when created, cannot grow or shrink | Dynamic — can grow and shrink as needed |
| Mutability | Elements can be updated; size cannot change | Immutable OR mutable depending on how you create it |
| Performance | Slightly faster for raw access | Slightly more flexible |

Think of an array as a **printed form** with a fixed number of blank lines. You can fill in those lines, but you can't add more lines or remove any.

A list is more like a **notepad** — you can write as many items as you want, cross things out, and add new lines anywhere.

For most everyday programming in Kotlin, **lists are preferred** over arrays because of their flexibility.

---

### Creating an Immutable List

Use `listOf()` to create a list whose elements cannot be changed after creation:

```kotlin
// An immutable list of planet names
// Type is inferred as List<String>
val innerPlanets = listOf("Mercury", "Venus", "Earth", "Mars")
```

Once created, you cannot add, remove, or change any element in this list. It is locked. This is called an **immutable** list.

> **Why would you want a locked list?** Because if you know the data never needs to change, locking it prevents bugs. It's impossible to accidentally add or delete something from a list that doesn't allow it.

---

### Creating a Mutable List

Use `mutableListOf()` to create a list you can change:

```kotlin
// A mutable list — you can add, remove, and update players
val players = mutableListOf("Alice", "Bob", "Cindy", "Dan")
```

This list starts with four elements but can grow or shrink as the game progresses.

**Creating an empty list:**

Sometimes you start with no data and add items later. For that, create an empty list:

```kotlin
// Empty immutable list — not useful on its own
val emptyImmutable: List<String> = listOf()

// Empty mutable list — you'll add items to this later
val newPlayers = mutableListOf<String>()
```

When the list is empty, Kotlin can't infer the type from the contents — so you must tell it explicitly, either as a type declaration (`: List<String>`) or as a type argument on the function (`mutableListOf<String>()`). Both are valid.

---

### Useful Properties and Methods

Before diving into indexing, here are some built-in tools that every list (and array) comes with.

> **Quick note on properties vs. methods:** A **property** is like a variable attached to a value — you read it with a dot: `list.size`. A **method** is like a function attached to a value — you call it with a dot and parentheses: `list.isEmpty()`. You'll learn all about this in the Classes chapter. For now, just use them as shown.

```kotlin
val players = mutableListOf("Alice", "Bob", "Cindy", "Dan")

// How many elements are in the list?
println(players.size)         // Output: 4

// Is the list completely empty?
println(players.isEmpty())    // Output: false

// Get the first element (throws an exception if the list is empty!)
println(players.first())      // Output: Alice

// Get the last element (also throws if empty!)
println(players.last())       // Output: Dan
```

> **Warning: `first()` and `last()` can crash your program if the list is empty.** Always check `isEmpty()` first if you're not sure whether the list has elements.

**Getting the min and max safely:**

Unlike `first()` and `last()`, these return `null` instead of crashing when the list is empty — so their return type is nullable:

```kotlin
val scores = listOf(15, 3, 42, 8, 27)

// Returns the smallest value, or null if the list is empty
val lowest = scores.minOrNull()    // 3 (type: Int?)

// Returns the largest value, or null if the list is empty
val highest = scores.maxOrNull()   // 42 (type: Int?)

// Always check for null before using these
if (highest != null) {
    println("Top score: $highest")   // Output: Top score: 42
}
```

Notice: `minOrNull()` finds the **smallest value**, not the item at the lowest index. For strings, it returns the alphabetically earliest:

```kotlin
val names = listOf("Cindy", "Alice", "Dan", "Bob")
println(names.minOrNull())   // Output: Alice  (alphabetically first)
println(names.first())       // Output: Cindy  (first in position)
```

`first()` and `minOrNull()` are completely different — don't confuse them.

---

### Accessing Elements by Index

The most direct way to get a specific element is with the **indexing syntax** — square brackets containing the index number:

```kotlin
val players = mutableListOf("Alice", "Bob", "Cindy", "Dan")

// Access by index (remember: index 0 = first element)
val firstPlayer = players[0]    // "Alice"
val secondPlayer = players[1]   // "Bob"
val lastPlayer = players[3]     // "Dan"

println("First player: $firstPlayer")   // Output: First player: Alice
```

This is the same as calling `.get(index)`, but the square bracket syntax is cleaner and preferred:

```kotlin
val second = players.get(1)    // same result, less readable
val second = players[1]        // preferred
```

> **Critical warning: Index Out of Bounds**
>
> If you try to access an index that doesn't exist, your program crashes with an `IndexOutOfBoundsException`:
>
> ```kotlin
> val players = mutableListOf("Alice", "Bob", "Cindy", "Dan")
> println(players[4])   // ❌ CRASH — index 4 doesn't exist (only 0-3)
> println(players[3])   // ✅ Fine — "Dan"
> ```
>
> The safe rule: valid indices are `0` to `list.size - 1`. Always stay within this range.

---

### Slicing a Range of Elements

What if you want several consecutive elements at once, not just one? Use `slice()` with a range:

```kotlin
val players = mutableListOf("Alice", "Bob", "Cindy", "Dan", "Eli")

// Get players at index 1 and 2 (Bob and Cindy)
val middlePlayers = players.slice(1..2)
println(middlePlayers.joinToString())   // Output: Bob, Cindy
```

The range `1..2` means "from index 1 up to and including index 2."

`joinToString()` is a handy method that converts a list to a single readable string, with commas between elements by default.

> **Important:** `slice()` returns a **new, separate list**. Changing the slice does not affect the original list.

---

### Checking if an Element Exists

Use the `in` operator to check if a value is inside a list or array:

```kotlin
val players = mutableListOf("Alice", "Bob", "Cindy", "Dan")

println("Alice" in players)    // Output: true
println("Frank" in players)    // Output: false
```

Use `!in` to check the opposite — whether something is **not** in the list:

```kotlin
fun isEliminated(player: String): Boolean {
    return player !in players    // true if the player is NOT in the list
}

println(isEliminated("Bob"))    // Output: false  (Bob is still in)
println(isEliminated("Frank"))  // Output: true   (Frank was never in)
```

The `in` operator is equivalent to the `.contains()` method, but reads more naturally:

```kotlin
players.contains("Alice")   // same as "Alice" in players
```

You can also combine `slice()` and `contains()` to check within a specific range:

```kotlin
// Is "Alice" in the slice from index 1 to 3?
players.slice(1..3).contains("Alice")   // false — Alice is at index 0
```

---

## Part 3: Modifying Mutable Lists

This section only applies to lists created with `mutableListOf()`. Immutable lists (`listOf()`) cannot be changed after creation.

---

### Adding Elements

**Add to the end with `add()`:**

```kotlin
val players = mutableListOf("Alice", "Bob", "Cindy", "Dan")

players.add("Eli")    // adds "Eli" to the end

println(players.joinToString())
// Output: Alice, Bob, Cindy, Dan, Eli
```

**Add to the end with `+=`:**

```kotlin
players += "Gina"    // shorthand for add() — appends to the end

println(players.joinToString())
// Output: Alice, Bob, Cindy, Dan, Eli, Gina
```

**Insert at a specific position with `add(index, value)`:**

Sometimes you need to add an element at a specific spot, not the end. Use `add()` with two arguments — the index where you want it, and the value:

```kotlin
// Insert "Frank" at index 5 (between Eli and Gina)
players.add(5, "Frank")

println(players.joinToString())
// Output: Alice, Bob, Cindy, Dan, Eli, Frank, Gina
```

When you insert at an index, the element that was there (and all elements after it) shift one position to the right to make room.

> **Note on arrays and `+=`:** Arrays are fixed-size, so you cannot truly add elements. However, if an array is declared as `var`, you can use `+=` — but this secretly creates a **brand new array** with the extra element and assigns it to the variable. The original array is replaced entirely. This is different from a mutable list's `+=`, which genuinely adds to the same list.
>
> ```kotlin
> var numbers = arrayOf(1, 2, 3)
> numbers += 4    // creates a NEW array [1, 2, 3, 4] and assigns it to 'numbers'
> ```

---

### Removing Elements

**Remove by value with `remove()`:**

```kotlin
// Removes "Gina" from the list. Returns true if found and removed, false if not found.
val wasRemoved = players.remove("Gina")
println("Removed: $wasRemoved")   // Output: Removed: true
```

`remove()` returns a `Boolean` — useful for confirming that the removal actually happened.

**Remove by index with `removeAt()`:**

```kotlin
// Removes the element at index 2 (Cindy). Returns the removed element.
val removedPlayer = players.removeAt(2)
println("$removedPlayer was removed")   // Output: Cindy was removed
```

`removeAt()` returns the element that was removed — useful if you want to do something with it (like add it to a "removed players" list).

**Finding an element's index with `indexOf()`:**

If you don't know the index of an element but you know its value, use `indexOf()`:

```kotlin
val index = players.indexOf("Dan")
println("Dan is at index $index")   // Output: Dan is at index 2 (after removals)
```

If the element isn't in the list, `indexOf()` returns `-1`. If the same value appears multiple times, `indexOf()` returns the first occurrence.

---

### Updating Elements

To replace an existing element, use the indexing syntax with assignment:

```kotlin
// Frank wants to be called Franklin now — update his name at index 4
players[4] = "Franklin"

println(players.joinToString())
// Output: Alice, Bob, Dan, Eli, Franklin
```

This is equivalent to calling `.set(index, value)`, but the `[]` syntax is cleaner and preferred:

```kotlin
players.set(4, "Franklin")   // same result, but less readable
```

**Sorting the list after updates:**

```kotlin
players.sort()    // sorts the list alphabetically in-place
println(players.joinToString())
// Output: Alice, Bob, Dan, Eli, Franklin
```

> Arrays can also have their elements updated with indexing syntax, even though their size is fixed:
>
> ```kotlin
> val numbers = arrayOf(1, 2, 3)
> numbers[0] = 99   // ✅ Fine — updating the value inside the array
> println(numbers.joinToString())   // Output: 99, 2, 3
> ```

---

## Part 4: Iterating Through a List

**Simple iteration — for each element:**

```kotlin
val players = mutableListOf("Alice", "Bob", "Dan", "Eli", "Franklin")

for (player in players) {
    println(player)
}
// Output:
// Alice
// Bob
// Dan
// Eli
// Franklin
```

On each loop, `player` automatically holds the next element. You don't have to manage index numbers yourself.

**Iteration with index — when position matters:**

```kotlin
for ((index, player) in players.withIndex()) {
    println("${index + 1}. $player")   // index + 1 for human-friendly numbering (1, 2, 3...)
}
// Output:
// 1. Alice
// 2. Bob
// 3. Dan
// 4. Eli
// 5. Franklin
```

`withIndex()` wraps each element with its position. The `(index, player)` syntax unpacks both pieces at once.

**Using iteration to calculate something:**

```kotlin
// Add up all elements in a list of integers
fun sumOfElements(list: List<Int>): Int {
    var sum = 0           // start with zero
    for (number in list) {
        sum += number     // add each element to the running total
    }
    return sum
}

val scores = listOf(2, 2, 8, 6, 1)
println(sumOfElements(scores))   // Output: 19
```

---

## Part 5: Nullability in Collections

When you combine nullability (from Chapter 7) with collections, there are three distinct things that can be nullable — and they mean very different things:

### 1. The list itself is nullable (`List<Int>?`)

The list might not exist at all. The elements inside, if the list exists, are always real integers.

```kotlin
// The list itself can be null, but its elements (if any) are always Int
var nullableList: List<Int>? = listOf(1, 2, 3, 4)

nullableList = null    // ✅ Fine — you can set the whole list to null
```

To use this, you'd need to null-check the list itself first:

```kotlin
nullableList?.forEach { println(it) }   // safe call — skips if list is null
```

### 2. The elements are nullable (`List<Int?>`)

The list always exists, but some individual elements inside it might be null.

```kotlin
// The list itself always exists, but some elements can be null
var listOfNullables: List<Int?> = listOf(1, 2, null, 4)

listOfNullables = null   // ❌ ERROR — the list itself is not nullable
```

When iterating, you have to handle the possible null in each element:

```kotlin
for (item in listOfNullables) {
    println(item ?: "missing")   // uses "missing" if item is null
}
// Output:
// 1
// 2
// missing
// 4
```

### 3. Both the list and elements are nullable (`List<Int?>?`)

The most flexible (and most dangerous) case. Use this only when both scenarios are genuinely possible.

```kotlin
// Both the list AND its elements can be null
var nullableListOfNullables: List<Int?>? = listOf(1, 2, null, 4)

nullableListOfNullables = null   // ✅ Fine
```

**Summary table:**

| Type | List can be null? | Elements can be null? |
|---|---|---|
| `List<Int>` | ❌ No | ❌ No |
| `List<Int>?` | ✅ Yes | ❌ No |
| `List<Int?>` | ❌ No | ✅ Yes |
| `List<Int?>?` | ✅ Yes | ✅ Yes |

As a rule: **use the least permissive type that fits your situation**. Don't allow null if you don't need to.

---

## Challenges

### Challenge 1: Which Are Valid?

Look at these ten statements and determine which are valid and which cause errors. Think through each one before reading the answer.

```kotlin
// Given this setup:
val array4 = arrayOf(1, 2, 3)
val array5 = arrayOf(1, 2, 3)   // declared as val

1. val array1 = Array<Int>()          // Missing size and initializer
2. val array2 = arrayOf()             // Empty — type cannot be inferred
3. val array3: Array<String> = arrayOf()   // Empty but type is explicit

4. println(array4[0])                 // Accessing index 0
5. println(array4[5])                 // Accessing index 5 of a 3-element array
6. array4[0] = 4                      // Updating element of a val array

7. array5[0] = array5[1]              // Replacing one element with another
8. array5[0] = "Six"                  // Assigning a String to an Int array
9. array5 += 6                        // Using += on a val array
10. for item in array5 { println(item) }  // Wrong for-loop syntax in Kotlin
```

**Answers:**

1. ❌ Invalid — `Array<Int>()` needs a size argument and an initializer. Use `arrayOf()` or `Array(size) { defaultValue }`.
2. ❌ Invalid — Kotlin cannot infer the type from an empty `arrayOf()`. Add a type: `arrayOf<Int>()`.
3. ✅ Valid — the type is declared explicitly, so an empty `arrayOf()` is fine.
4. ✅ Valid — index 0 exists in a 3-element array. Prints `1`.
5. ❌ Invalid — index 5 doesn't exist in a 3-element array (valid: 0, 1, 2). Crashes with `IndexOutOfBoundsException`.
6. ✅ Valid — `val` means you can't replace the array variable itself, but you CAN update elements inside it.
7. ✅ Valid — copying one element's value into another's slot. Both are `Int`, no type mismatch.
8. ❌ Invalid — `array5` holds `Int` elements. You cannot assign a `String` to it.
9. ❌ Invalid — `array5` is declared as `val`, so you cannot reassign it with `+=`.
10. ❌ Invalid — Kotlin's for-loop syntax is `for (item in array5) { ... }`, not `for item in`.

---

### Challenge 2: Remove First Occurrence

Write a function that removes the **first** occurrence of a given integer from a list and returns the new list. The original list is not modified.

```kotlin
fun removeOne(item: Int, list: List<Int>): List<Int> {
    // Create a mutable copy so we can modify it
    val result = list.toMutableList()

    // indexOf returns the first position of 'item', or -1 if not found
    val index = result.indexOf(item)

    // Only remove if the item actually exists in the list
    if (index != -1) {
        result.removeAt(index)
    }

    return result
}

// Test it
println(removeOne(3, listOf(1, 2, 3, 4, 3)))   // Output: [1, 2, 4, 3]
println(removeOne(9, listOf(1, 2, 3)))           // Output: [1, 2, 3] — 9 not found
```

---

### Challenge 3: Remove All Occurrences

Write a function that removes **every** occurrence of a given integer:

```kotlin
fun removeAll(item: Int, list: List<Int>): List<Int> {
    // filter() keeps only elements that do NOT equal 'item'
    // You'll learn filter() in detail in the Lambdas chapter
    return list.filter { it != item }
}

// Or using a for loop (no lambdas needed):
fun removeAllLoop(item: Int, list: List<Int>): List<Int> {
    val result = mutableListOf<Int>()
    for (number in list) {
        if (number != item) {
            result.add(number)   // only keep numbers that are NOT 'item'
        }
    }
    return result
}

// Test both
println(removeAll(3, listOf(1, 2, 3, 4, 3, 3)))     // Output: [1, 2, 4]
println(removeAllLoop(3, listOf(1, 2, 3, 4, 3, 3)))  // Output: [1, 2, 4]
```

---

### Challenge 4: Reverse an Array (Without Using `.reverse()`)

```kotlin
fun reverse(array: Array<Int>): Array<Int> {
    val result = mutableListOf<Int>()

    // Iterate backwards through the array — from last index to 0
    for (i in array.indices.reversed()) {
        result.add(array[i])
    }

    return result.toTypedArray()
}

// Test it
println(reverse(arrayOf(1, 2, 3, 4, 5)).joinToString())
// Output: 5, 4, 3, 2, 1
```

`array.indices` gives you the range of valid indices (0 to size-1). `.reversed()` makes that range go backwards.

---

### Challenge 5: Shuffle an Array Randomly

```kotlin
import java.util.Random

val random = Random()

// Returns a random Int from 'from' (inclusive) to 'to' (exclusive)
fun rand(from: Int, to: Int): Int {
    return random.nextInt(to - from) + from
}

fun randomized(array: Array<Int>): Array<Int> {
    // Start with a mutable copy
    val result = array.toMutableList()
    val shuffled = mutableListOf<Int>()

    // Repeatedly pick a random element, remove it from result, add it to shuffled
    while (result.isNotEmpty()) {
        val randomIndex = rand(0, result.size)   // pick a random valid index
        shuffled.add(result[randomIndex])         // take that element
        result.removeAt(randomIndex)              // remove it so we don't pick it again
    }

    return shuffled.toTypedArray()
}

println(randomized(arrayOf(1, 2, 3, 4, 5)).joinToString())
// Output: something like: 3, 1, 5, 2, 4  (random each time)
```

---

### Challenge 6: Find Min and Max Without Built-in Methods

```kotlin
fun minMax(numbers: Array<Int>): Pair<Int, Int>? {
    // If the array is empty, there's no min or max — return null
    if (numbers.isEmpty()) return null

    // Start by assuming the first element is both min and max
    var min = numbers[0]
    var max = numbers[0]

    // Compare every element to the current min and max
    for (number in numbers) {
        if (number < min) min = number   // found a new smallest
        if (number > max) max = number   // found a new largest
    }

    // Return both as a Pair
    return Pair(min, max)
}

// Test it
val result = minMax(arrayOf(3, 1, 7, 2, 9, 4))
if (result != null) {
    val (min, max) = result
    println("Min: $min, Max: $max")   // Output: Min: 1, Max: 9
}

println(minMax(arrayOf()))   // Output: null  (empty array)
```

---

## Key Points

- A **collection** stores multiple values in a single variable. Arrays and lists are the two most common in Kotlin.
- An **array** is a fixed-size, ordered collection. Its size cannot change after creation, but its elements can be updated.
- Array **indices start at 0**. The last valid index is `size - 1`. Accessing beyond this crashes with `IndexOutOfBoundsException`.
- `arrayOf()` creates a general `Array<T>`. `intArrayOf()`, `doubleArrayOf()`, etc. create primitive-type arrays (`IntArray`, `DoubleArray`) which are more memory-efficient.
- A **list** is like an array but can grow and shrink dynamically.
- `listOf()` creates an **immutable** list — elements cannot be added, removed, or changed after creation.
- `mutableListOf()` creates a **mutable** list — you can add, remove, update, and insert elements.
- Use `size` and `isEmpty()` to inspect a list. Use `first()` and `last()` carefully — they crash on empty lists.
- `minOrNull()` and `maxOrNull()` return nullable types — always null-check before using.
- Access elements with `list[index]` (indexing syntax) or `list.get(index)`. The `[]` form is preferred.
- `slice(range)` returns a new sub-list — modifying it does not affect the original.
- Use `in` to check if an element exists: `"Alice" in players`.
- Modify mutable lists with `add()`, `+=`, `add(index, value)`, `remove()`, `removeAt()`, `indexOf()`, and index assignment `list[i] = value`.
- Iterate with `for (item in list)` or `for ((index, item) in list.withIndex())`.
- For nullable collections, `List<Int>?` means the list itself can be null. `List<Int?>` means the elements can be null. `List<Int?>?` means both can be null. Use the least permissive type that fits your needs.
