# Chapter 9: Maps & Sets
### Kotlin Apprentice — Beginner-Friendly Rewrite

---

## Part 1: Maps

---

## Why Do We Need Maps?

You've already learned about lists and arrays. They're great for storing a sequence of values — a list of scores, a list of names, a list of items.

But here's a problem. Suppose you want to store the scores for each player in a card game:

```kotlin
val names  = listOf("Anna", "Brian", "Craig", "Donna")
val scores = listOf(2, 2, 8, 6)
```

To find Anna's score, you have to know that Anna is at index 0, and then go look up `scores[0]`. The two lists are connected only by position. If you accidentally mix up the order, everything falls apart.

Wouldn't it be nicer to look up Anna's score by just saying "give me Anna's score" — directly, by name?

That's exactly what a **map** does.

---

## What Is a Map?

A map is a collection of **pairs**. Each pair has two parts:

- A **key** — used to look something up
- A **value** — the thing you get back

Think of a map like a real-world dictionary. You look up a word (the key), and the dictionary gives you the definition (the value). You don't need to know which page number the word is on — you just search by the word itself.

Here's the map that replaces those two separate lists:

```
"Anna"  →  2
"Brian" →  2
"Craig" →  8
"Donna" →  6
```

Now you can ask: "What is Anna's score?" and get `2` back instantly — no index needed.

**Rules of maps:**

- Every key must be **unique**. You can't have two entries for "Anna".
- Different keys *can* point to the same value. (Both Anna and Brian scored 2 — that's fine.)
- All keys must be the same type. All values must be the same type.
- Maps are **unordered** — there's no guaranteed first or last element.

---

## Creating a Map

### Immutable map with `mapOf()`

The simplest way to create a map is with `mapOf()`. You pass in pairs, using the `to` keyword to connect each key with its value:

```kotlin
// Create a map where keys are player names and values are their birth years
var yearOfBirth = mapOf(
    "Anna"  to 1990,
    "Brian" to 1991,
    "Craig" to 1992,
    "Donna" to 1993
)
```

The `to` keyword creates a `Pair` — a simple bundle of two values. Kotlin uses these pairs to populate the map.

This map is **immutable** (read-only). You can look things up, but you cannot add, remove, or change entries after it's created.

### Mutable map with `mutableMapOf()`

If you need to change the map later — add new players, update scores, remove entries — use `mutableMapOf()`:

```kotlin
// Create a mutable map of player names to their scores
var namesAndScores = mutableMapOf(
    "Anna"  to 2,
    "Brian" to 2,
    "Craig" to 8,
    "Donna" to 6
)

println(namesAndScores)
// > {Anna=2, Brian=2, Craig=8, Donna=6}
```

Kotlin infers the type as `MutableMap<String, Int>` — a map from Strings to Ints.

Notice the output: the order of the pairs is not guaranteed. Maps don't preserve insertion order the way lists do.

### Empty mutable map

You can create an empty mutable map to fill in later:

```kotlin
var namesAndScores = mutableMapOf<String, Int>()
// Now empty — you'll add entries as needed
```

### Using `HashMap` directly

You can also create a map using the `HashMap` class:

```kotlin
// Import needed at top of file: import java.util.HashMap
var pairs = HashMap<String, Int>()
```

You can even give it a starting **capacity** — how many entries you expect to store. This is just a performance hint:

```kotlin
var pairs = HashMap<String, Int>(20) // Hint: we expect around 20 entries
```

**When to use `HashMap` vs `mutableMapOf()`:** For most beginner code, `mutableMapOf()` is fine and easier to read. Use `HashMap` directly only when performance matters (more on this later).

---

## Accessing Values in a Map

There are two ways to look up a value: using square brackets (index notation), or using the `get()` function.

### Using Square Brackets `[]`

Just like arrays use a number inside brackets to get an element, maps use a **key**:

```kotlin
var namesAndScores = mutableMapOf(
    "Anna"  to 2,
    "Brian" to 2,
    "Craig" to 8,
    "Donna" to 6
)

println(namesAndScores["Anna"])
// > 2
```

The map looks for a pair with key "Anna", finds it, and returns its value `2`.

What if you look up a key that doesn't exist?

```kotlin
println(namesAndScores["Greg"])
// > null
```

It returns `null` — because Greg isn't in the map. This is safe! Unlike arrays, where looking up an out-of-bounds index causes a crash, maps simply return `null` for missing keys.

This means map lookups always return a **nullable type**. `namesAndScores["Anna"]` doesn't return `Int` — it returns `Int?`. Keep this in mind when you use the result.

### Using `get()`

You can also use the `get()` function — it does exactly the same thing:

```kotlin
println(namesAndScores.get("Craig"))
// > 8
```

In fact, `namesAndScores["Craig"]` is just shorthand that Kotlin translates into `namesAndScores.get("Craig")` internally.

### Checking Size and Emptiness

Maps share useful properties with other collections:

```kotlin
namesAndScores.isEmpty() // false — it has entries
namesAndScores.size      // 4 — four key-value pairs
```

---

## Modifying a Mutable Map

You need a mutable map to make changes. Let's say Bob wants to join the card game. We'll store some information about him:

```kotlin
val bobData = mutableMapOf(
    "name"       to "Bob",
    "profession" to "CardPlayer",
    "country"    to "USA"
)
```

The type here is `MutableMap<String, String>` — both keys and values are Strings.

### Adding a New Pair

Two ways to add a new key-value pair:

**Using `put()`:**

```kotlin
bobData.put("state", "CA")  // Adds the key "state" with value "CA"
```

**Using square bracket assignment (shorter and preferred):**

```kotlin
bobData["city"] = "San Francisco"  // Adds key "city" with value "San Francisco"
```

Both do the same thing. The bracket syntax is cleaner and more common.

### Updating an Existing Value

Turns out Bob was caught cheating at cards! You need to change his name and profession so no one recognizes him.

**Using `put()` to update:**

```kotlin
bobData.put("name", "Bobby")  // Changes the value for "name" from "Bob" to "Bobby"
```

An important detail: `put()` **returns the old value** when updating. So `bobData.put("name", "Bobby")` returns `"Bob"` — the previous value. If the key didn't exist, it returns `null`.

**Using bracket notation to update:**

```kotlin
bobData["profession"] = "Mailman"  // Updates "profession" to "Mailman"
```

**Using `+=` to add a pair:**

```kotlin
val pair = "nickname" to "Bobby D"
bobData += pair  // Adds the pair "nickname" → "Bobby D"

println(bobData)
// > {name=Bobby, profession=Mailman, country=USA, state=CA, city=San Francisco, nickname=Bobby D}
```

So the map now knows Bobby's full disguise.

---

## Removing Pairs

Bobby — er, "Bobby" — still doesn't feel safe. He wants to erase all information about his location:

```kotlin
bobData.remove("city")          // Removes the "city" key and its value entirely

bobData.remove("state", "CA")   // Removes "state" only IF its value is "CA"
```

The first `remove()` takes a key and deletes the pair no matter what the value is.

The second `remove()` takes both a key and a value — it only removes the pair if the value matches. This is a safer version when you want to make sure you're deleting what you think you're deleting.

---

## Iterating Through a Map

You use a `for` loop to go through a map, just like with lists. The difference is that each iteration gives you a **pair** (key + value), not just a single value.

To handle pairs nicely, Kotlin lets you unpack them using **destructuring**:

```kotlin
for ((player, score) in namesAndScores) {
    println("$player - $score")  // Prints each player and their score
}
// > Anna - 2
// > Brian - 2
// > Craig - 8
// > Donna - 6
```

The `(player, score)` syntax automatically splits each pair into its key and value. This is called **destructuring declaration** — instead of getting the pair as one thing, you break it into two named variables.

You can also iterate over just the keys:

```kotlin
for (player in namesAndScores.keys) {
    print("$player, ")  // No newline — prints all on one line
}
println()               // Newline at the end
// > Anna, Brian, Craig, Donna,
```

Or just the values:

```kotlin
for (score in namesAndScores.values) {
    println(score)
}
```

---

## How Maps Work Internally — Hashing

This section explains *why* maps are so fast. You don't need to memorize this, but understanding it helps you appreciate what's happening under the hood.

### The Problem Maps Solve (Efficiently)

With a list, if you want to find whether "Anna" is in it, you might have to check every element one by one. For a list of 1,000,000 names, that could mean checking all million names. That's slow.

Maps solve this with a technique called **hashing**.

### What Is Hashing?

**Hashing** means taking any value — a String, an Int, a Boolean — and converting it to a number. That number is called a **hash value**.

```kotlin
println("some string".hashCode())  // > 1395333309
println(1.hashCode())              // > 1
println(false.hashCode())          // > 1237
```

The hash value is calculated the same way every time for the same input. No matter how many times you hash `"some string"`, you always get `1395333309`.

Maps use this hash number to decide **where to store** a value internally. When you look something up, the map hashes your key, goes directly to that location, and retrieves the value. No searching needed.

### Why This Makes Maps Fast

| Operation | Speed |
|---|---|
| Accessing a value by key | O(1) — instant |
| Inserting a new pair | O(1) — instant |
| Deleting a pair | O(1) — instant |
| Checking if a key exists | O(1) — instant |

O(1) means "constant time" — the operation takes the same amount of time whether the map has 5 entries or 5 million. This is much better than lists, which need O(n) time to search — meaning they get slower as they grow.

**The trade-off:** Maps give up ordering. You gain speed but lose the guarantee that items come out in the order you put them in.

### Using `HashMap` for Performance

If you're writing performance-critical code and know you need a map, use `HashMap<K, V>` with `hashMapOf()` instead of `mapOf()`. The underlying performance is the same, but it's explicit:

```kotlin
val fastMap = hashMapOf("Anna" to 2, "Brian" to 2)
```

For most everyday code, `mapOf()` and `mutableMapOf()` work perfectly well.

---

## Part 2: Sets

---

## What Is a Set?

A **set** is a collection of **unique values** of the same type, with **no guaranteed order**.

The key word is *unique*. A set will never hold the same value twice. If you try to add a duplicate, it simply ignores it.

This is incredibly useful when you want to track *whether something exists*, not *how many times* it exists — like a guest list, a list of visited pages, or a set of allowed permissions.

Compare the three collection types:

| Type | Ordered? | Allows Duplicates? | Look Up By |
|---|---|---|---|
| Array | Yes | Yes | Index (number) |
| List | Yes | Yes | Index (number) |
| Set | No | No | Value itself |
| Map | No | Keys unique | Key |

---

## Creating a Set

### Using `setOf()`

```kotlin
val names = setOf("Anna", "Brian", "Craig", "Anna")
// Notice: "Anna" appears twice in the input

println(names)
// > [Anna, Brian, Craig]
```

The duplicate "Anna" is silently ignored. The set only keeps one copy.

`setOf()` creates an **immutable** set — you can't change it after creation.

### Using `HashSet` (empty set)

For a mutable set that you'll fill later:

```kotlin
val hashSet = HashSet<Int>()
// An empty set that can hold integers
```

### Creating a Set From an Array

You can convert an existing array into a set. This is useful when you have data with potential duplicates and you want to eliminate them.

To do this, you use the **spread operator** `*`, which "spreads" an array's elements as individual arguments:

```kotlin
val someArray = arrayOf(1, 2, 3, 1)  // Note: contains duplicate 1

var someSet = mutableSetOf(*someArray)  // Spread the array into the set

println(someSet)
// > [1, 2, 3]
```

Even though `someArray` contains `1` twice, the set only keeps one copy. The type `MutableSet<Int>` is inferred automatically from the array's element type.

---

## Accessing Elements in a Set

Sets don't have index-based access like arrays and lists. You can't say "give me element at position 2." That concept doesn't exist for sets.

Instead, you work with sets by asking questions about membership.

### Checking If a Value Exists

```kotlin
println(someSet.contains(1))  // true  — 1 is in the set
println(4 in someSet)         // false — 4 is not in the set
```

Both `contains()` and `in` do the same thing. The `in` keyword often reads more naturally in code.

### First and Last (With a Warning)

You can call `first()` and `last()` on a set:

```kotlin
println(someSet.first())
println(someSet.last())
```

But here's the important warning: **sets are unordered**. You don't know which element will be "first" or "last." These methods will return *some* element, but you can't predict which one. Don't rely on this for logic that depends on a specific element.

### Iterating Through a Set

You iterate exactly like a list — the order of iteration is not guaranteed, but you'll visit every element exactly once:

```kotlin
for (number in someSet) {
    println(number)  // Prints each unique number
}
```

---

## Adding and Removing Elements

You need a **mutable set** to modify it.

### Adding an Element

```kotlin
someSet.add(5)  // Adds 5 to the set

someSet.add(1)  // 1 is already there — nothing happens, no error
```

If the element already exists, `add()` simply does nothing. This is one of the nicest features of sets — you can call `add()` freely without worrying about creating duplicates.

### Removing an Element

```kotlin
val removedOne = someSet.remove(1)
// Removes 1 from the set; returns true because it was found and removed

println(removedOne)  // > true
println(someSet)     // > [2, 3, 5]
```

`remove()` returns a `Boolean`:
- `true` — the element was found and removed
- `false` — the element wasn't in the set (nothing happened)

---

## How Sets Work Internally

Sets use the same hashing mechanism as maps. Each value is hashed to find its storage location, which is why all operations are O(1) — instant, regardless of set size.

`HashSet` is the concrete type that backs a set in Kotlin. Its performance characteristics are identical to `HashMap`:

| Operation | Speed |
|---|---|
| Checking if a value exists | O(1) |
| Adding a value | O(1) |
| Removing a value | O(1) |

The trade-off, again, is no ordering. If you need to know the order elements were added, use a list instead.

---

## When to Use Each Collection Type

Here's a simple guide to choosing the right tool:

| Situation | Use |
|---|---|
| You need items in order, possibly with duplicates | `List` |
| You need fast lookup by a name or identifier | `Map` |
| You need to track whether items exist (no duplicates) | `Set` |
| You need fixed size and sequential access | `Array` |

---

## Putting It All Together — A Practical Example

Let's use both a map and a set together in a mini scenario:

```kotlin
// Track which players are in the game and their scores
val scores = mutableMapOf(
    "Anna"  to 14,
    "Brian" to 7,
    "Craig" to 14,
    "Donna" to 3
)

// Use a set to track which scores are unique
val uniqueScores = mutableSetOf<Int>()

for ((_, score) in scores) {         // _ means we don't need the key (name)
    uniqueScores.add(score)          // Duplicates are automatically ignored
}

println("All scores: $scores")
println("Unique scores: $uniqueScores")
// All scores: {Anna=14, Brian=7, Craig=14, Donna=3}
// Unique scores: [14, 7, 3]   (note: 14 appears only once)
```

This pattern — fill a map with data, extract unique values into a set — is very common in real Kotlin programs.

---

## Challenges

### Challenge 1: Which Statements Are Valid?

Given these declarations:

```kotlin
// Group A: Creating maps
val map1: Map<Int, Int> = emptyMap()  // Valid — explicit type annotation
val map2 = emptyMap()                 // INVALID — type cannot be inferred
val map3: Map<Int, Int> = emptyMap()  // Valid — same as map1, just written differently
```

**map2 is invalid** because Kotlin can't figure out what type the keys and values should be. You must either annotate the type explicitly or provide values so Kotlin can infer the type.

Now given:

```kotlin
val map4 = mapOf("One" to 1, "Two" to 2, "Three" to 3)
// Type: Map<String, Int>
```

```kotlin
map4[1]       // INVALID — keys are Strings, not Ints. Type mismatch.
map4["One"]   // Valid — returns 1
map4["Zero"] = 0  // INVALID — map4 is immutable (mapOf), can't assign
map4[0] = "Zero"  // INVALID — immutable AND wrong key type
```

Now given:

```kotlin
val map5 = mutableMapOf(
    "NY" to "New York",
    "CA" to "California"
)
// Type: MutableMap<String, String>
```

```kotlin
map5["NY"]           // Valid — returns "New York"
map5["WA"] = "Washington"  // Valid — adds a new pair
map5["CA"] = null    // INVALID — values are String (non-nullable), null not allowed
```

**Key insight on statement 10:** The map is `MutableMap<String, String>`, not `MutableMap<String, String?>`. You can't assign `null` to a value slot that expects a non-nullable `String`.

---

### Challenge 2: Print Long State Names

Write a function that takes a map of two-letter state codes to full state names, and prints all states with names longer than 8 characters.

```kotlin
fun printLongStateNames(states: Map<String, String>) {
    // Loop through each key-value pair in the map
    for ((code, name) in states) {
        // Check if the full name has more than 8 characters
        if (name.length > 8) {
            println(name)
        }
    }
}

// Test it
val stateCodes = mapOf(
    "NY" to "New York",       // 8 characters — NOT printed (not longer than 8)
    "CA" to "California",     // 10 characters — printed
    "TX" to "Texas",          // 5 characters — not printed
    "WA" to "Washington"      // 10 characters — printed
)

printLongStateNames(stateCodes)
// > California
// > Washington
```

---

### Challenge 3: Merge Two Maps

Write a function that combines two maps into one. If a key appears in both maps, keep the value from the **second** map (it overrides the first).

```kotlin
fun mergeMaps(
    map1: Map<String, String>,
    map2: Map<String, String>
): Map<String, String> {
    // Start with a mutable copy of the first map
    val result = mutableMapOf<String, String>()

    // Add all pairs from map1
    for ((key, value) in map1) {
        result[key] = value
    }

    // Add all pairs from map2; if a key already exists, it overwrites map1's value
    for ((key, value) in map2) {
        result[key] = value
    }

    return result
}

// Test it
val map1 = mapOf("a" to "Apple", "b" to "Banana")
val map2 = mapOf("b" to "Blueberry", "c" to "Cherry")

val merged = mergeMaps(map1, map2)
println(merged)
// > {a=Apple, b=Blueberry, c=Cherry}
// Note: "b" now maps to "Blueberry" from map2, overriding "Banana" from map1
```

---

### Challenge 4: Count Character Occurrences

Write a function that counts how many times each character appears in a string. Return the result as a map from `Char` to `Int`.

```kotlin
fun occurrencesOfCharacters(text: String): Map<Char, Int> {
    val result = mutableMapOf<Char, Int>()

    for (character in text) {
        // getOrDefault returns the current count, or 0 if this character hasn't been seen
        val currentCount = result.getOrDefault(character, defaultValue = 0)
        result[character] = currentCount + 1
    }

    return result
}

// Test it
val counts = occurrencesOfCharacters("hello")
println(counts)
// > {h=1, e=1, l=2, o=1}
```

**What is `getOrDefault()`?** It's a special map function that returns the value for a key if it exists, or a default value if it doesn't. Without it, you'd have to handle the `null` case manually every time:

```kotlin
// Without getOrDefault — verbose
val currentCount = result[character] ?: 0

// With getOrDefault — cleaner
val currentCount = result.getOrDefault(character, defaultValue = 0)
```

Both approaches work. `getOrDefault()` is just shorter.

---

### Challenge 5: Check If All Values Are Unique

Write a function that returns `true` if all values in a map are different from each other (no duplicate values).

The trick: use a set! Add all values to a set, then compare the set's size to the map's size. If they're equal, all values were unique (the set didn't drop any duplicates). If the set is smaller, some values were duplicated.

```kotlin
fun isInvertible(map: Map<String, Int>): Boolean {
    // A set automatically removes duplicates
    val valueSet = mutableSetOf<Int>()
    for (value in map.values) {
        valueSet.add(value)
    }

    // If no duplicates were dropped, the sizes match
    return valueSet.size == map.size
}

// Test it
val uniqueValues = mapOf("a" to 1, "b" to 2, "c" to 3)
println(isInvertible(uniqueValues))  // > true — all values are different

val duplicateValues = mapOf("a" to 1, "b" to 2, "c" to 1)
println(isInvertible(duplicateValues))  // > false — 1 appears twice
```

---

### Challenge 6: Working With Nullable Map Values

Given this map where some values can be `null`:

```kotlin
var nameTitleLookup: Map<String, String?> = mutableMapOf(
    "Mary"    to "Engineer",
    "Patrick" to "Intern",
    "Ray"     to "Hacker"
)
```

The type is `Map<String, String?>` — values are nullable Strings.

**Task 1:** Set Patrick's value to `null` (he's been assigned no title):

```kotlin
// Cast to MutableMap first since the variable is declared as Map
(nameTitleLookup as MutableMap)["Patrick"] = null

// Or declare it as MutableMap from the start:
var nameTitleLookup = mutableMapOf(
    "Mary"    to "Engineer",
    "Patrick" to "Intern",
    "Ray"     to "Hacker"
)

nameTitleLookup["Patrick"] = null
// Patrick's entry still exists in the map, but his value is now null
```

**Task 2:** Completely remove Ray's entry from the map:

```kotlin
nameTitleLookup.remove("Ray")
// Ray's key and value are both gone

println(nameTitleLookup)
// > {Mary=Engineer, Patrick=null}
```

**Important distinction:**
- `map[key] = null` — the key **stays** in the map, but its value is `null`
- `map.remove(key)` — the key and its value are **completely gone**

This only works because the value type is `String?` (nullable). You can't assign `null` to a map with non-nullable values like `Map<String, String>`.

---

## Key Points

- A **map** stores key-value pairs. Keys are unique. Values can repeat.
- Use `mapOf()` for immutable maps, `mutableMapOf()` for maps you'll change.
- Look up values with `map[key]` or `map.get(key)` — both return a nullable type.
- Adding and updating uses `put()` or bracket assignment `map[key] = value`.
- Removing uses `remove(key)` or `remove(key, value)`.
- Iterate over pairs with `for ((key, value) in map)`, or over just `.keys` or `.values`.
- Maps use **hashing** to make all operations O(1) — extremely fast.
- A **set** stores unique values with no guaranteed order.
- Use `setOf()` for immutable sets, `mutableSetOf()` for sets you'll modify.
- Check membership with `contains()` or the `in` keyword.
- `add()` on a set silently ignores duplicates.
- `remove()` returns `true` if the element was found and removed, `false` otherwise.
- Sets are best when you only care about *whether* something exists, not *how many times*.
