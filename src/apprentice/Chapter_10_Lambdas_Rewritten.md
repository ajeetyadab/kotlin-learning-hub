# Chapter 10: Lambdas
### Kotlin Apprentice — Beginner-Friendly Rewrite

---

## Why Lambdas Exist

In Chapter 6, you learned how to write named functions:

```kotlin
fun double(x: Int): Int {
    return x * 2
}
```

Named functions are great — you define them once, give them a name, and call them from anywhere.

But sometimes you need a small piece of behaviour just for one specific moment. You don't need a full named function with a permanent name stored in your codebase. You just want to say "here's a quick action — use it right here, right now."

That's what a **lambda** is. A lambda is a function without a name. You can write it inline, pass it around, store it in a variable, or hand it to another function as an argument.

Lambdas are also called **anonymous functions** or **closures** (more on the closure part later). You'll see all three terms used interchangeably.

They are especially powerful when working with collections — lists, arrays, maps, and sets — because they let you describe *what to do with each element* in just a line or two.

---

## Your First Lambda

Let's start with a named function and convert it into a lambda, step by step.

Here's a named function that multiplies two numbers:

```kotlin
fun multiply(a: Int, b: Int): Int {
    return a * b
}
```

Now here's the same logic as a lambda, stored in a variable:

```kotlin
// Declare a variable that can hold a lambda
// Type: (Int, Int) -> Int  means: takes two Ints, returns an Int
var multiplyLambda: (Int, Int) -> Int

// Assign the lambda to the variable
multiplyLambda = { a: Int, b: Int ->
    a * b   // The last expression is automatically returned
}
```

Let's look at the lambda syntax carefully:

```
{ a: Int, b: Int -> a * b }
  ↑ parameters    ↑ body (after the arrow)
```

- The **curly braces `{}`** wrap the whole lambda.
- Before the `->` arrow: the **parameter list** (names and types).
- After the `->` arrow: the **body** — what the lambda does.
- The **last expression** in the body is automatically returned. No `return` keyword needed.

Now call it just like a regular function:

```kotlin
val lambdaResult = multiplyLambda(4, 2)  // Calls the lambda with 4 and 2
println(lambdaResult)                    // > 8
```

One important difference from named functions: **you cannot use named arguments with lambdas**. This won't work:

```kotlin
multiplyLambda(a = 4, b = 2)  // ERROR — lambdas don't support named arguments
```

---

## Shorthand Syntax

Lambdas are designed to be concise. Kotlin gives you several ways to write them more briefly.

### Step 1: Let Kotlin Infer the Types

If Kotlin already knows the type of your lambda variable (because you declared it), you can drop the type annotations from the parameters:

```kotlin
// Kotlin knows multiplyLambda is (Int, Int) -> Int
// So you don't need to say "a: Int, b: Int" again
multiplyLambda = { a, b ->
    a * b
}
```

This is exactly the same lambda — just shorter. Kotlin figures out the types from the variable's declared type.

### Step 2: The `it` Keyword — For Single-Parameter Lambdas

When a lambda has **exactly one parameter**, you can skip even the parameter name. Kotlin provides a built-in name for it: **`it`**.

Here's a lambda with one parameter the verbose way:

```kotlin
var doubleLambda: (Int) -> Int = { a: Int ->
    2 * a
}
```

With `it`, this shrinks to:

```kotlin
doubleLambda = { 2 * it }  // "it" is the single parameter
```

`it` is not a magic word you have to memorize — it's just the default name Kotlin gives the single parameter when you don't name it yourself.

You can also use `it` in a fresh declaration:

```kotlin
// Type annotation required when using "it" in a new declaration
val square: (Int) -> Int = { it * it }  // Squares the input
```

`it` makes code shorter, but use it wisely. If the lambda body is long or you need to refer to the parameter multiple times with different names, giving it an explicit name is clearer.

---

## Lambdas as Arguments

This is where lambdas become truly powerful. You can pass a lambda as an argument to a function — just like passing an `Int` or a `String`.

Here's a function that accepts a lambda as its third parameter:

```kotlin
fun operateOnNumbers(
    a: Int,
    b: Int,
    operation: (Int, Int) -> Int  // Third parameter is a lambda type
): Int {
    val result = operation(a, b)  // Call the lambda with a and b
    println(result)
    return result
}
```

`operation` is just a parameter. Its *type* happens to be a lambda type: "takes two Ints, returns an Int." Inside the function, you call it like any normal function: `operation(a, b)`.

### Passing a Lambda Variable

You can create a lambda and pass it by its variable name:

```kotlin
val addLambda = { a: Int, b: Int ->
    a + b  // Adds the two numbers
}

operateOnNumbers(4, 2, operation = addLambda)  // > 6
```

### Passing a Named Function With `::`

You can also pass a regular named function — but you need the `::` **reference operator** to refer to the function itself (rather than calling it):

```kotlin
fun addFunction(a: Int, b: Int) = a + b

operateOnNumbers(4, 2, operation = ::addFunction)  // > 6
```

`::addFunction` means "a reference to the function `addFunction`" — not the result of calling it. The `::` tells Kotlin "treat this function as a value."

### Defining the Lambda Inline

You can skip creating a variable entirely and write the lambda directly at the call site:

```kotlin
operateOnNumbers(4, 2, operation = { a: Int, b: Int ->
    a + b
})
```

With type inference (Kotlin knows the operation type from the function signature):

```kotlin
operateOnNumbers(4, 2, operation = { a, b ->
    a + b
})
```

Even shorter — using the `+` operator directly, since `+` is just an `Int` method called `plus()`:

```kotlin
operateOnNumbers(4, 2, operation = Int::plus)
```

This means "use `Int`'s built-in `plus` function as the operation." All three versions produce the same result.

---

## Trailing Lambda Syntax

There's one more shorthand that's very common in Kotlin. When a **lambda is the last argument** in a function call, you can move it **outside the parentheses**:

Normal inline lambda:

```kotlin
operateOnNumbers(4, 2, operation = { a, b ->
    a + b
})
```

Trailing lambda — moved outside:

```kotlin
operateOnNumbers(4, 2) { a, b ->
    a + b
}
```

The lambda block `{ a, b -> a + b }` now appears after the closing parenthesis of the function call. The `operation =` label is dropped. This looks a bit like a code block belonging to a built-in statement — which is exactly the style Kotlin encourages.

You'll see trailing lambda syntax constantly in Kotlin code. Most of the collection functions (`forEach`, `filter`, `map`, etc.) are written using it. Once you recognise the pattern, it's very readable.

---

## Lambdas With No Meaningful Return Value

Not every lambda needs to return something useful. Sometimes you just want to *do something* — print a line, update a variable — without producing a result.

For this, Kotlin uses the `Unit` type as the return type. `Unit` is Kotlin's equivalent of "nothing meaningful" or "void" in other languages.

```kotlin
// A lambda that takes no parameters and returns Unit (nothing meaningful)
var unitLambda: () -> Unit = {
    println("Kotlin Apprentice is awesome!")
}

unitLambda()  // Runs the lambda — prints the message
```

The type `() -> Unit` reads as: "a function that takes no parameters and returns nothing meaningful."

### `Unit` vs `Nothing`

There's a distinction worth knowing:

- `Unit` — the function *does* return, but the return value is meaningless. Like a function that just prints something.
- `Nothing` — the function **never returns at all**. It always throws an exception or runs forever.

```kotlin
// A lambda that never returns — it always throws
var nothingLambda: () -> Nothing = {
    throw NullPointerException()  // Execution stops here — no return value ever
}
```

For everyday code, you'll almost always use `Unit`. `Nothing` is a rare, advanced concept you won't need for most programs.

---

## Capturing From the Enclosing Scope (Closures)

Earlier, the term "closure" was mentioned. Here's what it actually means.

A lambda can **access variables from outside itself** — from the surrounding code where it was defined. When it does this, it is said to **capture** those variables.

```kotlin
var counter = 0               // A variable defined OUTSIDE the lambda

val incrementCounter = {
    counter += 1              // Lambda uses and modifies the outer variable
}
```

The lambda `incrementCounter` doesn't have its own `counter` — it reaches out and grabs the `counter` from its surrounding scope. Any changes it makes are visible outside too:

```kotlin
incrementCounter()
incrementCounter()
incrementCounter()
incrementCounter()
incrementCounter()

println(counter)  // > 5
```

After five calls, `counter` is 5. The lambda and the outer code share the same variable.

### Why Is This Useful?

Imagine you want a function that creates an independent counter. Each time you call the function, you get a brand-new counter that is completely separate from any other counter:

```kotlin
// This function creates and returns a lambda — the lambda is a counter
fun countingLambda(): () -> Int {
    var counter = 0            // Each call to countingLambda() gets its own counter

    val incrementCounter: () -> Int = {
        counter += 1           // Captures this counter
        counter                // Returns the current count
    }

    return incrementCounter    // Return the lambda (not the result, the lambda itself)
}
```

Now create two independent counters:

```kotlin
val counter1 = countingLambda()  // counter1 gets its own private counter
val counter2 = countingLambda()  // counter2 gets a completely separate counter

println(counter1())  // > 1
println(counter2())  // > 1
println(counter1())  // > 2
println(counter1())  // > 3
println(counter2())  // > 2
```

`counter1` and `counter2` each track their own count independently. They don't interfere with each other.

This is the power of closures: a lambda can "remember" state from the context where it was created. That state is private to each lambda instance.

---

## Custom Sorting With Lambdas

In Chapter 8, you used `.sort()` to sort an array in default order (alphabetical, numerical, etc.). With lambdas, you can define exactly *how* to sort — your own custom rule.

Start with an array of strings:

```kotlin
val names = arrayOf("ZZZZZZ", "BB", "A", "CCCC", "EEEEE")
names.sorted()  // Default sort: alphabetical
// > [A, BB, CCCC, EEEEE, ZZZZZZ]
```

Now sort by **string length**, longest first:

```kotlin
val namesByLength = names.sortedWith(compareBy {
    -it.length   // Negative length = longer strings come first (descending)
})

println(namesByLength)
// > [ZZZZZZ, EEEEE, CCCC, BB, A]
```

**How it works:**

- `sortedWith()` takes a `Comparator` — a rule for comparing two elements.
- `compareBy { ... }` creates that comparator using a lambda.
- The lambda receives each element as `it` and returns the value to sort by.
- Putting a `-` (minus) in front reverses the order (largest first instead of smallest first).

This is much more flexible than a simple `.sort()`. You can sort by any property, in any direction, with any custom logic.

---

## Iterating Over Collections With Lambdas

This is where lambdas shine brightest. Kotlin provides several powerful functions that work on collections. Each one takes a lambda that describes what to do, and the function handles the looping.

These come from a style of programming called **functional programming** — describing *what you want* rather than writing out step-by-step *how* to do it.

### `forEach` — Do Something for Every Element

`forEach` loops over every element in a collection and runs your lambda for each one.

```kotlin
val values = listOf(1, 2, 3, 4, 5, 6)

values.forEach {
    println("$it: ${it * it}")  // Prints each number and its square
}
// > 1: 1
// > 2: 4
// > 3: 9
// > 4: 16
// > 5: 25
// > 6: 36
```

This replaces a `for` loop entirely. It's shorter and reads almost like English: "for each value, print the value and its square."

### `filter` — Keep Only Matching Elements

`filter` creates a **new list** containing only the elements for which your lambda returns `true`.

```kotlin
var prices = listOf(1.5, 10.0, 4.99, 2.30, 8.19)

// Keep only prices greater than $5
val largePrices = prices.filter {
    it > 5.0  // Return true to keep, false to discard
}

println(largePrices)
// > [10.0, 8.19]
```

The original `prices` list is **not changed**. `filter` always creates and returns a new list. The original stays intact.

How does `filter` know what to keep? The lambda acts as a **test** for each element:
- Return `true` → include this element in the result
- Return `false` → leave it out

### `map` — Transform Every Element

`map` creates a **new list** where every element has been transformed by your lambda.

```kotlin
// Apply a 10% discount to all prices (multiply by 0.9)
val salePrices = prices.map {
    it * 0.9  // Transform each price
}

println(salePrices)
// > [1.35, 9.0, 4.491, 2.07, 7.37]
```

Again, the original `prices` is unchanged. `map` returns a new list of the same size, with each value replaced by whatever your lambda returns.

**`map` can also change the type.** For example, converting a list of strings to integers:

```kotlin
val userInput = listOf("0", "11", "haha", "42")

// Try to convert each string to an Int
// toIntOrNull() returns null if the string isn't a valid number
val numbers = userInput.map {
    it.toIntOrNull()
}

println(numbers)
// > [0, 11, null, 42]
```

The result is `List<Int?>` (nullable Ints), because "haha" can't be converted to a number — it becomes `null`.

> **Note:** Don't confuse the `map` function (which transforms collections) with the `Map` type (which stores key-value pairs). Same name, completely different things.

### `mapNotNull` — Transform and Discard Nulls

`mapNotNull` works just like `map`, but automatically **removes any `null` results** from the output:

```kotlin
val numbers2 = userInput.mapNotNull {
    it.toIntOrNull()  // Returns null for "haha", which gets dropped
}

println(numbers2)
// > [0, 11, 42]
```

`numbers2` is `List<Int>` (non-nullable) because all nulls have been filtered out. This is a common pattern when converting data that might not always be valid.

### `fold` — Combine All Elements Into One Value

`fold` takes all the elements in a collection and combines them into a **single result**.

You provide two things:
1. A **starting value** (the initial result before any elements are processed)
2. A **lambda** that takes the current result and the next element, and returns the new result

```kotlin
// Calculate the total of all prices
var sum = prices.fold(0.0) { currentTotal, nextPrice ->
    currentTotal + nextPrice  // Add each price to the running total
}

println(sum)  // > 26.98
```

Walk through this step by step:
- Start with `currentTotal = 0.0`
- Element 1.5: `0.0 + 1.5 = 1.5` → new total
- Element 10.0: `1.5 + 10.0 = 11.5` → new total
- Element 4.99: `11.5 + 4.99 = 16.49` → new total
- … and so on until all elements are processed.

`fold` can combine elements in any way you choose — sum, product, concatenation, whatever your lambda does.

### `reduce` — Like `fold`, But Uses First Element as Start

`reduce` is similar to `fold`, but it uses the **first element of the collection** as the starting value instead of a value you provide:

```kotlin
sum = prices.reduce { currentTotal, nextPrice ->
    currentTotal + nextPrice
}

println(sum)  // > 26.98
```

Same result. The difference:
- `fold` — you choose the starting value. Works on empty collections (returns the starting value).
- `reduce` — uses the first element as the start. Crashes on an empty collection.

For summing numbers, either works. Prefer `fold` when you need control over the starting value.

### Using These Functions on Maps

`forEach`, `filter`, and `map` work on maps too. When iterating a map with `forEach`, each element is a `Map.Entry` containing a key and a value:

```kotlin
// A map of item prices to stock quantity
val stock = mapOf(
    1.5  to 5,    // Item costing $1.50, with 5 in stock
    10.0 to 2,    // Item costing $10.00, with 2 in stock
    4.99 to 20,   // Item costing $4.99, with 20 in stock
    2.30 to 5,    // Item costing $2.30, with 5 in stock
    8.19 to 30    // Item costing $8.19, with 30 in stock
)

var stockSum = 0.0

stock.forEach {
    stockSum += it.key * it.value  // Price × quantity for each item
}

println(stockSum)  // > 384.5
```

Inside the `forEach` lambda, `it` is a `Map.Entry` — it has `.key` (the price) and `.value` (the quantity). You multiply them together to get the value of that item's stock.

---

## Chaining Lambda Operations

One of the most elegant features of these collection functions is that you can **chain them** — apply one after another in a single expression.

For example: filter a list to names longer than 4 characters, then concatenate them all:

```kotlin
val nameList = listOf("Anna", "Brian", "Craig", "Donna", "Ed")

val result = nameList
    .filter { it.length > 4 }          // Keep only names longer than 4 chars
    .fold("") { acc, name -> acc + name }  // Concatenate the kept names

println(result)  // > BrianCraigDonna
```

You could also write the filter and fold on one line. The result of `.filter { ... }` is a new list, and you immediately call `.fold(...)` on that new list.

This "pipeline" style — data flows through a series of transformations — is the heart of functional programming.

---

## Challenges

### Challenge 1: Repeating Yourself

Write a function that runs a given lambda a given number of times.

```kotlin
fun repeatTask(times: Int, task: () -> Unit) {
    // Loop 'times' times and call the lambda each iteration
    for (i in 1..times) {
        task()
    }
}

// Use it to print a message 10 times
repeatTask(10) {
    println("Kotlin Apprentice is a great book!")
}
// Prints the message 10 times
```

Note the trailing lambda syntax: the `task` lambda is passed after the closing parenthesis because it's the last (and only) argument that's a lambda.

---

### Challenge 2: Lambda Sums

Write a function that generates a mathematical series (sequence of numbers) and sums the first `length` values.

```kotlin
fun mathSum(length: Int, series: (Int) -> Int): Int {
    // 'series' is a lambda that takes a position (1, 2, 3...) and returns the value at that position
    var sum = 0
    for (position in 1..length) {
        sum += series(position)   // Call the lambda to get the value at this position
    }
    return sum
}
```

**Sum of first 10 square numbers** (1² + 2² + 3² + … + 10² = 385):

```kotlin
val squareSum = mathSum(10) { position ->
    position * position   // The value at each position is the position squared
}
println(squareSum)  // > 385
```

**Sum of first 10 Fibonacci numbers** (1, 1, 2, 3, 5, 8, 13, 21, 34, 55 → sum = 143):

```kotlin
// Recursive Fibonacci function
fun fibonacci(n: Int): Int {
    if (n <= 1) return n           // Base cases: fib(0) = 0, fib(1) = 1
    return fibonacci(n - 1) + fibonacci(n - 2)
}

val fibSum = mathSum(10) { position ->
    fibonacci(position)   // Get the Fibonacci number at this position
}
println(fibSum)  // > 143
```

---

### Challenge 3: Functional Ratings

You have a map of app names to arrays of ratings:

```kotlin
val appRatings = mapOf(
    "Calendar Pro" to arrayOf(1, 5, 5, 4, 2, 1, 5, 4),
    "The Messenger" to arrayOf(5, 4, 2, 5, 4, 1, 1, 2),
    "Socialise" to arrayOf(2, 1, 2, 2, 1, 2, 4, 2)
)
```

**Step 1:** Create a map of app names to their average ratings.

```kotlin
val averageRatings = mutableMapOf<String, Double>()

appRatings.forEach { (appName, ratings) ->
    // reduce sums all ratings together, then divide by count for the average
    val average = ratings.reduce { sum, rating -> sum + rating }.toDouble() / ratings.size
    averageRatings[appName] = average
}

println(averageRatings)
// > {Calendar Pro=3.375, The Messenger=3.0, Socialise=2.0}
```

**Step 2:** Get the list of app names with an average rating greater than 3.

```kotlin
val goodApps = averageRatings
    .filter { (_, average) -> average > 3.0 }   // Keep entries where average > 3
    .map { (appName, _) -> appName }             // Extract just the app names

println(goodApps)
// > [Calendar Pro, The Messenger]
```

Notice the chaining: `.filter { ... }` runs first and keeps only entries with average > 3. Then `.map { ... }` transforms those entries from `Map.Entry<String, Double>` into just `String` (the app name).

The `_` is used to ignore the part of the pair we don't need — `(_, average)` means "I don't care about the key, just give me the value."

---

## Key Points

- A **lambda** is a function without a name. It can be stored in a variable or passed as an argument.
- The syntax is `{ parameters -> body }`. The last expression is automatically the return value.
- **Type inference** lets you omit type annotations when Kotlin can figure them out.
- **`it`** is the implicit name for a single-parameter lambda's parameter.
- Lambdas can be passed to functions as arguments — the parameter type is a function type like `(Int, Int) -> Int`.
- **`::functionName`** creates a reference to a named function so you can pass it like a lambda.
- **Trailing lambda syntax** moves the last lambda argument outside the parentheses: `foo(x) { ... }`.
- A lambda's return type is `Unit` when it returns nothing meaningful, and `Nothing` when it never returns at all.
- Lambdas are **closures** — they can capture and modify variables from their surrounding scope.
- Each closure instance has its own captured state, independent of other instances.
- **`forEach`** runs a lambda for every element. **`filter`** keeps elements matching a condition. **`map`** transforms every element.
- **`mapNotNull`** transforms elements and drops any that produce `null`.
- **`fold`** combines all elements into one value using a starting value you provide. **`reduce`** does the same but uses the first element as the start.
- Collection functions can be **chained** — the output of one becomes the input of the next.
