# Chapter 16: Enum & Sealed Classes

---

## Why Enums Exist

Imagine you're writing a program that tracks what day of the week it is. You decide to use an `Int` — Monday is 1, Tuesday is 2, and so on.

That works. But what happens when someone passes in 9? Or -3? Or 47? Your code would have no idea what to do with that, and the compiler can't warn you either — it just sees a valid integer.

Now imagine using a `String` instead. You'd constantly be checking: "is it `"Monday"` or `"monday"` or `"Mon"`?" One typo anywhere in your code and it silently breaks.

The problem in both cases is the same: **you have a fixed, known set of valid values, but you're using a type that allows infinite possible values.**

This is exactly the problem that `enum` was invented to solve.

An **enum** (short for "enumeration") lets you define a type that can only ever be one of a specific, limited set of values. The compiler then knows every possible option — and can help you handle all of them correctly.

---

## Creating Your First Enum Class

In Kotlin, you create an enum using `enum class`:

```kotlin
enum class DayOfTheWeek {
    Sunday,
    Monday,
    Tuesday,
    Wednesday,
    Thursday,
    Friday,
    Saturday
}
```

Each name inside the braces is called a **case**. These are the only valid values this type can hold.

Once you have this, you can loop through every day and print some information that Kotlin gives you automatically:

```kotlin
for (day in DayOfTheWeek.values()) {
    println("Day ${day.ordinal}: ${day.name}")
}
```

Output:
```
Day 0: Sunday
Day 1: Monday
Day 2: Tuesday
Day 3: Wednesday
Day 4: Thursday
Day 5: Friday
Day 6: Saturday
```

Kotlin gave you three things for free the moment you declared an enum class:

- **`values()`** — a companion function that returns a `List` of all the cases, in the order they're declared. This is how you loop through all possibilities.
- **`ordinal`** — a property on each case that gives its zero-based index in the list. `Sunday` is 0, `Monday` is 1, and so on.
- **`name`** — a property that returns the case's name as a `String`. So `DayOfTheWeek.Friday.name` gives you `"Friday"`.

These exist because enum classes are real classes in Kotlin. Each case is an instance of the class, which means it can have properties, functions, and companion objects — just like any other class.

---

## Accessing an Enum by Index

Because `values()` returns a `List`, you can grab a specific case by its position:

```kotlin
val dayIndex = 0
val dayAtIndex = DayOfTheWeek.values()[dayIndex]
println("Day at $dayIndex is $dayAtIndex")
// Output: Day at 0 is Sunday
```

This is useful when someone gives you a number and you need to know what day it represents. Just be careful not to go out of bounds — accessing an index that doesn't exist throws an `ArrayIndexOutOfBoundsException`.

---

## Accessing an Enum by Name

You can also get a case by name using `valueOf()`:

```kotlin
val tuesday = DayOfTheWeek.valueOf("Tuesday")
println("Tuesday is day ${tuesday.ordinal}")
// Output: Tuesday is day 2
```

This looks up the case whose name exactly matches the string you pass in.

**But what if the name doesn't exist?**

```kotlin
val notADay = DayOfTheWeek.valueOf("Blernsday") // 💥
```

This throws an `IllegalArgumentException` at runtime. Unlike `as?` (which gives you `null` on failure), `valueOf()` does not return a nullable — it crashes. The Kotlin designers decided this kind of mistake is serious enough to deserve a crash rather than silent failure.

So if you're working with strings that come from user input or the network, always wrap `valueOf()` in a `try/catch` or use a safer lookup.

---

## Changing Case Order

One of the nice things about enum classes is how easy it is to change the order of cases.

In the United States, a week starts on Sunday. In most of Europe, a week starts on Monday. If you're working with European date data, you'd want your enum ordered differently.

All you need to do is rearrange the cases:

```kotlin
enum class DayOfTheWeek {
    Monday,
    Tuesday,
    Wednesday,
    Thursday,
    Friday,
    Saturday,
    Sunday;  // <-- semicolon here because more declarations follow
}
```

Two things to notice:
- Cases are separated by commas.
- The **last case** needs a **semicolon** (not a comma) if you plan to add properties or functions after the list. The semicolon signals to the compiler: "the list of cases ends here — what comes next is something else."
- If you only have cases and nothing else, the semicolon is optional.

After reordering, run the same `values()` loop and the ordinals automatically reflect the new order — no other code changes needed:

```
Day 0: Monday
Day 1: Tuesday
Day 2: Wednesday
...
Day 6: Sunday
```

This is the power of letting Kotlin manage the ordinals for you rather than hard-coding integers.

---

## Enum Class Properties

Enum classes can have properties, just like regular classes. You add them to the constructor.

Let's say you want each day to know whether it's a weekend day or not:

```kotlin
enum class DayOfTheWeek(val isWeekend: Boolean) {
    Monday(false),
    Tuesday(false),
    Wednesday(false),
    Thursday(false),
    Friday(false),
    Saturday(true),
    Sunday(true);
}
```

Every case now has to pass a value for `isWeekend` when it's declared. Saturday and Sunday get `true`, the rest get `false`.

You can now use `isWeekend` on any `DayOfTheWeek` value:

```kotlin
for (day in DayOfTheWeek.values()) {
    println("Day ${day.ordinal}: ${day.name}, is weekend: ${day.isWeekend}")
}
```

Output:
```
Day 0: Monday, is weekend: false
Day 1: Tuesday, is weekend: false
...
Day 5: Saturday, is weekend: true
Day 6: Sunday, is weekend: true
```

You can also give the property a default value. If `isWeekend` defaults to `false`, you only need to specify it for the cases that are `true`:

```kotlin
enum class DayOfTheWeek(val isWeekend: Boolean = false) {
    Monday,
    Tuesday,
    Wednesday,
    Thursday,
    Friday,
    Saturday(true),
    Sunday(true);
}
```

Same result, less repetition.

---

## Enum Class Functions

Enum classes can also have instance functions — functions that run on a particular case.

Here's a function that calculates how many days from the current day until another given day:

```kotlin
enum class DayOfTheWeek(val isWeekend: Boolean = false) {
    Monday,
    Tuesday,
    Wednesday,
    Thursday,
    Friday,
    Saturday(true),
    Sunday(true);

    fun daysUntil(other: DayOfTheWeek): Int {
        // 1. If the target day comes after today, simple subtraction
        return if (this.ordinal < other.ordinal) {
            other.ordinal - this.ordinal   // e.g., Monday(0) to Friday(4) = 4
        } else {
            // 2. If the target day comes before or equals today,
            //    wrap around the week by adding the total day count
            other.ordinal - this.ordinal + DayOfTheWeek.values().count()
        }
    }
}
```

Let's say today is Monday and you want to know how many days until Friday:

```kotlin
val today = DayOfTheWeek.Monday
val secondDay = DayOfTheWeek.Friday
val daysUntil = today.daysUntil(secondDay)
// daysUntil = 4
```

And if today is Friday and you want to know when Friday comes around again — that's 7 days (a full week), not 0. The wrap-around logic handles this.

---

## Companion Object in an Enum Class

Enum classes can also have companion objects with functions that don't need a specific instance.

A good example is a function that figures out what today is:

```kotlin
enum class DayOfTheWeek(val isWeekend: Boolean = false) {
    // ... cases ...

    companion object {
        // Uses Java's Calendar class to get today's day of the week
        fun today(): DayOfTheWeek {
            // 1. Get the current day as an integer (Java Calendar is 1-indexed, starting Sunday)
            val calendarDayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)

            // 2. Adjust: subtract 2 to convert from Java's 1-indexed Sunday-first
            //    to our 0-indexed Monday-first ordering
            var adjustedDay = calendarDayOfWeek - 2

            val days = DayOfTheWeek.values()

            // 3. If the result went negative (i.e., today is Sunday),
            //    wrap it around by adding the total number of days
            if (adjustedDay < 0) {
                adjustedDay += days.count()
            }

            // 4. Return the case whose ordinal matches our adjusted number
            return days.first { it.ordinal == adjustedDay }
        }
    }
}
```

> **Note:** You need `import java.util.*` at the top of your file to use `Calendar`.

This function bridges Kotlin's enum with Java's calendar system. The Java `Calendar` class numbers days 1 (Sunday) through 7 (Saturday). Our enum starts at 0 and starts with Monday. The subtraction of 2 handles both the offset and the shift from Sunday-first to Monday-first indexing. The wrap-around handles the edge case when today is Sunday.

You call it like this:

```kotlin
val today = DayOfTheWeek.today()
val isWeekend = "It is${if (today.isWeekend) "" else " not"} the weekend"
println("It is $today. $isWeekend.")
// Example: It is Monday. It is not the weekend.
```

> **Note:** When you put an enum case directly in a string template (`$today`), Kotlin automatically uses the `name` property. If you want something else (like the ordinal), use the full expression: `${today.ordinal}`.

---

## Using `when` With Enum Classes

`when` is one of the most powerful tools you can use with enum classes. You've seen `when` used with integers and strings. With enums, it becomes even better — because the compiler knows every possible value.

```kotlin
when (today) {
    DayOfTheWeek.Monday    -> println("I don't care if $today's blue")
    DayOfTheWeek.Tuesday   -> println("$today's gray")
    DayOfTheWeek.Wednesday -> println("And $today, too")
    DayOfTheWeek.Thursday  -> println("$today, I don't care 'bout you")
    DayOfTheWeek.Friday    -> println("It's $today, I'm in love")
    DayOfTheWeek.Saturday  -> println("$today, Wait...")
    DayOfTheWeek.Sunday    -> println("$today always comes too late")
}
```

This `when` is **exhaustive** — it covers every possible case of the enum. The compiler knows this and actually uses it to help you:

**If you add an `else` branch**, the compiler warns you: `'when' is exhaustive so 'else' is redundant here`. The `else` is useless because all cases are already handled. Remove it.

**If you remove a case**, the compiler warns you: `'when' expression on enum is recommended to be exhaustive — add 'Monday', 'Tuesday', ... or 'else' branch instead`. You haven't covered all possibilities, so you might be silently missing a case.

This is a major advantage over using `when` with a plain `Int` or `String` — the compiler can't warn you about missing integers, but it can absolutely warn you about missing enum cases.

Keep an eye on these warnings especially when you add new cases to an enum class in the future. Old `when` expressions won't automatically handle new cases — the compiler will tell you what you missed.

---

## Sealed Classes: When Enum Isn't Enough

Enum classes are great, but they have a limitation: **every case is a single instance of the class, shared across your entire program**. You can't store different data in different instances of the same case.

For example, imagine you need to represent different currencies in your app — US Dollar, Euro, and some Cryptocurrency. Each currency has its own exchange rate, and you want to track an actual amount of money per instance.

With an enum, all instances of `Crypto` would share the same `amount`. You can't have one `Crypto` wallet with 0.27541 and another with 1.5 — they'd overwrite each other.

This is where **sealed classes** come in.

A sealed class is like an enum in one important way: it has a **fixed, known set of subtypes**. But unlike enum cases, each subtype is a full class — you can create as many **instances** of each subtype as you want, and each instance holds its own separate data.

Here's a quick comparison:

| Feature | Enum class | Sealed class |
|---|---|---|
| Fixed set of types | ✅ | ✅ |
| Works with exhaustive `when` | ✅ | ✅ |
| Multiple instances per type | ❌ (one per case) | ✅ |
| Each instance stores own data | ❌ | ✅ |
| Can have abstract members | ❌ | ✅ |
| Free `values()`, `ordinal`, `name` | ✅ | ❌ |

Key rules about sealed classes:
- They are **abstract** — you can never instantiate the sealed class itself directly
- All **direct subclasses** must be defined **in the same file** as the sealed class
- The sealed class's constructors are always **private**
- You *can* create indirect subclasses (subclasses of subclasses) outside the file, but this usually causes more problems than it solves

---

## Creating a Sealed Class

Let's build the currency example. You're working for a company that accepts US Dollars, Euros, and Cryptocurrency:

```kotlin
sealed class AcceptedCurrency {
    class Dollar : AcceptedCurrency()
    class Euro   : AcceptedCurrency()
    class Crypto : AcceptedCurrency()
}
```

All three subclasses are defined inside `AcceptedCurrency`, which keeps them in the same file and signals that this is a closed family of types.

Create an instance of `Crypto` and try to print it:

```kotlin
val currency = AcceptedCurrency.Crypto()
println("You've got some $currency!")
// Output: You've got some AcceptedCurrency$Crypto@76ed5528!
```

That output is gibberish — unlike enum cases, sealed class instances don't automatically have a nice `name`. You need to add that yourself.

---

## Adding Properties to a Sealed Class

You can add a non-abstract property with a custom getter that uses `when(this)` to return the right value for each subtype:

```kotlin
sealed class AcceptedCurrency {
    class Dollar : AcceptedCurrency()
    class Euro   : AcceptedCurrency()
    class Crypto : AcceptedCurrency()

    // Non-abstract property: defined once on the sealed class,
    // returns different values depending on which subtype this is
    val name: String
        get() = when (this) {
            is Euro   -> "Euro"
            is Dollar -> "Dollars"
            is Crypto -> "NerdCoin"
        }
}
```

Now:

```kotlin
val currency = AcceptedCurrency.Crypto()
println("You've got some ${currency.name}!")
// Output: You've got some NerdCoin!
```

---

## Adding Abstract Properties

Since the sealed class is abstract, you can declare abstract properties that every subclass **must** override. This is how you enforce a contract across all your subtypes.

Each currency has a different exchange rate to USD. Add an abstract property and override it in each subclass:

```kotlin
sealed class AcceptedCurrency {
    // Every subclass MUST provide its own valueInDollars
    abstract val valueInDollars: Float

    class Dollar : AcceptedCurrency() {
        override val valueInDollars = 1.0f      // 1 dollar = 1 dollar
    }
    class Euro : AcceptedCurrency() {
        override val valueInDollars = 1.25f     // 1 euro ≈ 1.25 dollars
    }
    class Crypto : AcceptedCurrency() {
        override val valueInDollars = 2534.92f  // 1 NerdCoin ≈ $2534.92
    }

    // Non-abstract: how much of this currency you currently hold
    var amount: Float = 0.0f

    val name: String
        get() = when (this) {
            is Euro   -> "Euro"
            is Dollar -> "Dollars"
            is Crypto -> "NerdCoin"
        }

    // Non-abstract: uses both amount and valueInDollars,
    // which are available to all subclasses
    fun totalValueInDollars(): Float {
        return amount * valueInDollars
    }
}
```

The key insight here: `totalValueInDollars()` is written once on the sealed class. It can safely use both `amount` (a concrete property defined on the sealed class) and `valueInDollars` (an abstract property guaranteed to exist on every subclass). No code duplication across Dollar, Euro, and Crypto.

Let's try it:

```kotlin
val currency = AcceptedCurrency.Crypto()
currency.amount = 0.27541f
println("${currency.amount} of ${currency.name} is ${currency.totalValueInDollars()} in Dollars")
// Output: 0.27541 of NerdCoin is 698.1423 in Dollars
```

You can also create as many separate `Crypto` instances as you want, each holding a different `amount`:

```kotlin
val wallet1 = AcceptedCurrency.Crypto()
wallet1.amount = 0.27541f

val wallet2 = AcceptedCurrency.Crypto()
wallet2.amount = 1.5f

// wallet1 and wallet2 are independent — changing one doesn't affect the other
```

This is the key difference from enum classes: each instance is independent.

---

## Enumeration as a State Machine

One of the most practical real-world uses of enum classes is modeling a **state machine**.

A state machine is just a way of saying: "this thing can be in one of these specific states at any given moment." A traffic light is a state machine — it's either Red, Yellow, or Green. A login flow is a state machine — the user is either LoggedOut, Loading, or LoggedIn.

Here's a download state machine using an enum:

```kotlin
enum class DownloadState {
    Idle,        // Nothing has happened yet
    Starting,    // The download is being set up
    InProgress,  // Data is actively being downloaded
    Error,       // Something went wrong; download stopped
    Success      // Download completed successfully
}
```

Using `when` to respond to each state is clean and readable:

```kotlin
when (downloadState) {
    DownloadState.Idle       -> println("Download has not yet started.")
    DownloadState.Starting   -> println("Starting download...")
    DownloadState.InProgress -> println("Downloading data...")
    DownloadState.Error      -> println("An error occurred. Download terminated.")
    DownloadState.Success    -> println("Download completed successfully.")
}
```

Because `when` on an enum is exhaustive, the compiler ensures you don't forget to handle any state. If you add a new state to the enum later, the compiler immediately highlights every `when` that doesn't handle it yet. This is much safer than using integer constants like `STATE_IDLE = 0`, `STATE_STARTING = 1`, etc.

A real downloader might look like this in use:

```kotlin
Downloader().downloadData("foo.com/bar",
    progress = { downloadState ->
        when (downloadState) {
            DownloadState.Idle       -> println("Download has not yet started.")
            DownloadState.Starting   -> println("Starting download...")
            DownloadState.InProgress -> println("Downloading data...")
            DownloadState.Error      -> println("An error occurred. Download terminated.")
            DownloadState.Success    -> println("Download completed successfully.")
        }
    },
    completion = { error, list ->
        error?.let { println("Got error: ${error.message}") }
        list?.let  { println("Got list with ${list.size} items") }
    }
)
```

As the download progresses through its states, the `progress` callback fires and the matching `when` branch prints the current status.

---

## Nullables and Enums

Here's a thought: what does the `Idle` state really mean? It means "nothing is happening yet" — which is exactly what `null` already represents in Kotlin.

Instead of having a dedicated `Idle` case in your enum, you can remove it and make the download state itself **nullable**:

```kotlin
// In the Downloader class:
var downloadState: DownloadState? = null  // null means "not started yet"
```

Then in your `when` expression, handle `null` explicitly:

```kotlin
when (downloadState) {
    null                        -> println("No download state yet")
    DownloadState.Starting      -> println("Starting download...")
    DownloadState.InProgress    -> println("Downloading data...")
    DownloadState.Error         -> println("An error occurred. Download terminated.")
    DownloadState.Success       -> println("Download completed successfully.")
}
```

This approach has a conceptual advantage: `null` in Kotlin already carries the meaning "there is no value." Using it to represent the absence of a state is honest and clear. You don't have to invent a fake `Idle` state to convey the idea of "nothing has happened yet."

It also means you can distinguish between:
- `null` — no state information received yet
- `DownloadState.Starting` — we *know* the download is starting

This pattern is especially useful when state comes from an external source (like a network response) and may genuinely not have arrived yet.

---

## Challenges

### Challenge 1 — Safe Lookup by Index and Name

Add two companion functions to `DayOfTheWeek`:

1. A function that takes an `Int` index and returns a nullable `DayOfTheWeek`. It should return `null` instead of crashing when the index is out of range.
2. A function that takes a `String` and returns a nullable `DayOfTheWeek`. It should return `null` instead of crashing when the name doesn't match any case.

**Solution:**

```kotlin
companion object {
    fun today(): DayOfTheWeek { /* ... as before ... */ }

    // Safe lookup by index — returns null if out of range
    fun fromIndex(index: Int): DayOfTheWeek? {
        val days = values()
        return if (index in 0 until days.count()) {
            days[index]
        } else {
            null
        }
    }

    // Safe lookup by name — returns null if name not found
    fun fromString(name: String): DayOfTheWeek? {
        return values().firstOrNull { it.name.equals(name, ignoreCase = true) }
    }
}
```

Usage:
```kotlin
val day = DayOfTheWeek.fromIndex(3)    // Wednesday
val bad = DayOfTheWeek.fromIndex(99)   // null — no crash
val fri = DayOfTheWeek.fromString("Friday")    // Friday
val nope = DayOfTheWeek.fromString("Blernsday") // null — no crash
```

The difference from `valueOf()`: these functions return `null` on failure instead of throwing an exception. Much safer when the input is unpredictable.

---

### Challenge 2 — Days Until the Next Weekend

Add a function to `DayOfTheWeek` that calculates how many days until the next weekend begins.

Then update your code so that the "weekend" is Wednesday and Thursday instead of Saturday and Sunday — verify the function still works correctly.

**Solution — Part 1 (Saturday and Sunday as weekend):**

```kotlin
fun daysUntilWeekend(): Int {
    // Find the nearest upcoming day where isWeekend is true
    val days = DayOfTheWeek.values()

    // Search forward from the current day (wrapping around if needed)
    for (i in 1..days.count()) {
        val candidate = days[(this.ordinal + i) % days.count()]
        if (candidate.isWeekend) {
            return i
        }
    }

    // If today is the weekend, return 0 or wrap — depends on definition
    return 0
}
```

Usage:
```kotlin
val today = DayOfTheWeek.Wednesday
println("Days until weekend: ${today.daysUntilWeekend()}")
// Output: Days until weekend: 3 (Thursday, Friday... then Saturday)
```

**Part 2 — Change weekend to Wednesday and Thursday:**

Just update the `isWeekend` values in the case declarations:

```kotlin
enum class DayOfTheWeek(val isWeekend: Boolean = false) {
    Monday,
    Tuesday,
    Wednesday(true),   // now a "weekend" day
    Thursday(true),    // now a "weekend" day
    Friday,
    Saturday,          // no longer a weekend day
    Sunday;            // no longer a weekend day

    fun daysUntilWeekend(): Int { /* same code — no changes needed */ }
}
```

The `daysUntilWeekend()` function doesn't need to change at all because it uses `isWeekend` as a property — it doesn't hard-code which days are weekends. This is good design: the logic is separate from the data.

---

### Challenge 3 — Adding Two AcceptedCurrency Values Together

Write a function that adds together the value of two `AcceptedCurrency` objects and returns the result in dollars.

Think through these two scenarios before writing code:
- What if both currencies are the same type? (e.g., adding two `Dollar` amounts)
- What if the currencies are different types? (e.g., adding `Dollar` and `Crypto`)

**Solution:**

```kotlin
// Add as a function on AcceptedCurrency
fun addedValueInDollars(other: AcceptedCurrency): Float {
    // Regardless of whether currencies match, we can always
    // convert each to its dollar value and add them
    return this.totalValueInDollars() + other.totalValueInDollars()
}
```

Usage:
```kotlin
val myDollars = AcceptedCurrency.Dollar()
myDollars.amount = 50.0f

val myCrypto = AcceptedCurrency.Crypto()
myCrypto.amount = 0.1f

// Mixed currencies: converts each to dollars, then adds
val totalInDollars = myDollars.addedValueInDollars(myCrypto)
println("Total in Dollars: $totalInDollars")
// Output: Total in Dollars: 303.492 (50 + 0.1 * 2534.92)
```

If both are the same type, the math still works fine — you just add the dollar values, which happen to be equal per unit. For different types, each gets converted to dollars first via its `valueInDollars` rate, then summed. This is the safest common ground.

---

### Challenge 4 — Can the User Afford It?

Write a function that takes a `List<AcceptedCurrency>` (a wallet of different currency objects) and a cost in dollars. Return `true` if the total value in the wallet meets or exceeds the cost.

**Solution:**

```kotlin
fun canAfford(wallet: List<AcceptedCurrency>, costInDollars: Float): Boolean {
    // Sum up the dollar value of every currency in the wallet
    val totalFunds = wallet.sumOf { it.totalValueInDollars().toDouble() }.toFloat()
    return totalFunds >= costInDollars
}
```

Usage:
```kotlin
val wallet = listOf(
    AcceptedCurrency.Dollar().also  { it.amount = 20.0f   },
    AcceptedCurrency.Euro().also    { it.amount = 10.0f   },
    AcceptedCurrency.Crypto().also  { it.amount = 0.001f  }
)

// Total: $20 + $12.50 + ~$2.53 = ~$35.03
println(canAfford(wallet, costInDollars = 30.0f))   // true
println(canAfford(wallet, costInDollars = 100.0f))  // false
```

The `also { it.amount = ... }` pattern lets you set `amount` right after creating the instance and still include it inline in the list — a common Kotlin idiom for one-liners.

---

## Summary

Here's what enum classes give you, and when to reach for each tool:

| Tool | Use it when… |
|---|---|
| `enum class` | You have a fixed set of named values and a single shared instance per case is fine |
| `values()` | You need to loop through all possible cases |
| `ordinal` | You need the index of a case (be careful about hard-coding ordinals) |
| `name` | You need the string name of a case |
| `valueOf()` | You have an exact string and want the matching case — but wrap it in try/catch |
| Custom safe lookup | When input is unpredictable; return null instead of crashing |
| Constructor property | When each case needs its own fixed data (like `isWeekend`) |
| Companion function | When you need a function that works on the class level, not a specific instance |
| Instance function | When you need a function that operates relative to the current case |
| `when` with enum | For exhaustive, compiler-checked branching — add `else` only if you mean it |
| `sealed class` | When each type needs multiple independent instances with their own stored data |
| Nullable enum | When `null` naturally represents "no state" — prefer it over an `Idle` case |
| State machine | When something progresses through a fixed set of stages — enum is a perfect fit |

Enum classes shine when you want a locked-down, named set of possibilities with free tools for iteration and lookup. Sealed classes step in when those possibilities need to carry unique data per instance. Together, they cover almost every "limited set of options" problem you'll encounter.

In the next chapter, you'll learn about **interfaces** — a powerful way to define shared behavior that completely different classes can adopt without needing to share a common parent.
