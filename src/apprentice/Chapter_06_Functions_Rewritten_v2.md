# Chapter 6: Functions

---

## What Is a Function, and Why Should You Care?

Let's start with a problem.

Imagine you are writing an app and you need to show a welcome message in three different places — when the user opens the app, when they log in, and when they come back after being idle. Without functions, you'd write the same lines of code three separate times.

Now imagine you realise the message has a typo. You'd have to find all three copies and fix each one. Miss even one and your app is broken.

This is exactly the problem functions solve.

A **function** is a named block of code that you write once and can run as many times as you want, from anywhere in your program. Give the block a name, and whenever you need to run that code, just use the name. One change in the function automatically applies everywhere it's used.

Think of a function like a **recipe card**. The recipe is written once. You can cook that dish as many times as you want without rewriting the recipe. And if you improve the recipe, every future cook benefits.

---

## Writing Your First Function

Here is the simplest possible function in Kotlin:

```kotlin
// This is a function declaration.
// 'fun' tells Kotlin "I'm defining a function."
// 'printMyName' is the name we chose.
// The empty () means it needs no information from outside.
// Everything inside { } is what runs when we use this function.
fun printMyName() {
    println("My name is Ajeet.")
}
```

Let's look at every part:

| Part | What it means |
|---|---|
| `fun` | Kotlin keyword meaning "function" — always required |
| `printMyName` | The name you give the function — you choose this |
| `()` | Parentheses — always required, even when empty |
| `{ }` | Curly braces — everything inside is the function's code |

Declaring a function does **not** run it. It just defines it — like writing a recipe but not cooking yet.

To actually run the code inside, you **call** the function:

```kotlin
printMyName()   // This runs the code. Output: My name is Ajeet.
```

You can call it as many times as you want:

```kotlin
printMyName()   // My name is Ajeet.
printMyName()   // My name is Ajeet.
printMyName()   // My name is Ajeet.
```

Three lines. One function. No repeated code.

> **You've already been using functions.** `println()` is a function — it was written by Kotlin's creators and you've been calling it every time you print something. Now you're learning to write your own.

---

## Making Functions Flexible: Parameters

The function above always does the exact same thing. That's fine for some tasks, but often you want a function that can work with different data each time.

For example: instead of printing *your* name, what if the function could print *anyone's* name?

That's what **parameters** are for. A parameter is a piece of information you hand to the function when you call it, so the function can use that data to do its job.

Think of a parameter like an **ingredient slot** in a recipe. The recipe says "add 1 cup of [INGREDIENT]". You decide what goes in that slot when you cook.

Here's the function upgraded with a parameter:

```kotlin
// 'name' is the parameter — it's the ingredient slot.
// ': String' means whatever goes in that slot must be text (a String).
fun printName(name: String) {
    println("My name is $name.")   // $name is replaced with whatever was passed in
}
```

Now when you call it, you provide a value (called an **argument**):

```kotlin
printName("Alice")    // Output: My name is Alice.
printName("Bob")      // Output: My name is Bob.
printName("Priya")    // Output: My name is Priya.
```

Same function. Different argument each time. Three different results.

> **Parameter vs. Argument — what's the difference?**
> - A **parameter** is the placeholder name declared in the function: `name: String`
> - An **argument** is the actual value you pass when calling: `"Alice"`
>
> Parameter is the blank on the form. Argument is what you write in the blank.

### Multiple Parameters

You can have more than one parameter. Just separate them with commas:

```kotlin
// This function takes two numbers and prints their multiplication result.
// 'multiplier' and 'value' are two separate parameters, both of type Int.
fun printMultipleOf(multiplier: Int, value: Int) {
    println("$multiplier × $value = ${multiplier * value}")
}

printMultipleOf(4, 2)    // Output: 4 × 2 = 8
printMultipleOf(3, 7)    // Output: 3 × 7 = 21
```

Each argument maps to its parameter in order: `4` goes to `multiplier`, `2` goes to `value`.

> **Common mistake:** Passing arguments in the wrong order.
> ```kotlin
> printMultipleOf(2, 4)    // Works, but means 2 × 4, not 4 × 2
> ```
> With positional arguments, order matters. The first argument always goes to the first parameter.

---

## Named Arguments: Making Your Code Readable

When you call a function with multiple arguments, it's not always obvious what each number or word represents just by looking at the call:

```kotlin
printMultipleOf(4, 2)     // Which is multiplier? Which is value?
```

**Named arguments** solve this. You write the parameter name followed by `=` and then the value:

```kotlin
printMultipleOf(multiplier = 4, value = 2)     // Crystal clear
```

This is especially helpful with three or more parameters:

```kotlin
fun createProfile(username: String, age: Int, country: String) {
    println("User: $username, Age: $age, Country: $country")
}

// Without named arguments — confusing
createProfile("ajeet", 28, "India")

// With named arguments — self-documenting
createProfile(username = "ajeet", age = 28, country = "India")
```

Named arguments have a bonus power — you can pass them **in any order**:

```kotlin
// This is perfectly valid:
createProfile(country = "India", username = "ajeet", age = 28)
```

Kotlin matches each value to its parameter by name, not position.

---

## Default Values: Making Parameters Optional

Sometimes a parameter almost always gets the same value. Forcing the caller to type it every single time is tedious. Kotlin lets you set a **default value** for a parameter, which is used automatically if the caller doesn't provide one.

```kotlin
// 'value' has a default of 1.
// If the caller doesn't provide 'value', Kotlin uses 1 automatically.
fun printMultipleOf(multiplier: Int, value: Int = 1) {
    println("$multiplier × $value = ${multiplier * value}")
}

// No second argument — 'value' defaults to 1
printMultipleOf(4)        // Output: 4 × 1 = 4

// Second argument provided — default is ignored
printMultipleOf(4, 5)     // Output: 4 × 5 = 20
```

Defaults are most useful at the **end** of the parameter list, so callers can simply omit the ones they don't need:

```kotlin
fun sendEmail(to: String, subject: String, urgent: Boolean = false) {
    // ...
}

sendEmail("boss@work.com", "Report")             // urgent = false (default)
sendEmail("boss@work.com", "URGENT!", true)      // urgent = true (explicitly set)
```

> **Tip:** If you want to skip a middle parameter but provide one that comes after it, use named arguments:
> ```kotlin
> fun register(name: String, age: Int = 0, country: String = "Unknown") { ... }
>
> register("Ajeet", country = "India")   // ✅ skips 'age', uses named argument for 'country'
> register("Ajeet", "India")            // ❌ Error — Kotlin thinks "India" is the age (an Int)
> ```

---

## Return Values: Getting Results Back

Every function you've written so far has either printed something or done some calculation internally, but it hasn't handed any result back to you. Functions can also **return a value** — produce a result that the caller can use.

Think of it like this:
- A function that only prints is like a calculator that shows the answer on screen but won't let you use it.
- A function that returns is like a calculator where you can take the answer and use it in your next step.

To make a function return a value, you do two things:

1. Write `: ReturnType` after the closing parenthesis, before `{`
2. Use `return` inside the function to send the value back

```kotlin
// This function takes two numbers and returns their product.
// ': Int' after the () declares that this function returns an Int.
fun multiply(number: Int, multiplier: Int): Int {
    return number * multiplier    // 'return' sends this value back to the caller
}
```

Now the caller can use the result:

```kotlin
val result = multiply(4, 3)     // result = 12
println(result)                  // Output: 12

// Or use the return value directly without storing it
println(multiply(5, 6))          // Output: 30

// Or use it in an expression
val doubled = multiply(7, 2) + 1   // 14 + 1 = 15
```

### Why Not Just Print Inside the Function?

Because printing is a dead end. Once you print, the data is gone — it's just text on a screen. When you *return* a value, the caller can store it, compare it, transform it, pass it to another function, or use it however they need.

Return values make your functions genuinely useful building blocks.

> **Common mistake — forgetting to return:**
> ```kotlin
> fun multiply(number: Int, multiplier: Int): Int {
>     number * multiplier    // ❌ This calculates the result but throws it away!
> }                          // Kotlin will give a compile error
> ```
> You must use `return` to actually send the value back.

> **Common mistake — type mismatch:**
> ```kotlin
> fun getGreeting(): String {
>     return 42    // ❌ Error — function says it returns String, but you returned an Int
> }
> ```
> What you `return` must match the declared return type exactly.

---

## Returning Multiple Values with Pair

A function can only return one value. But what if you genuinely need two values back at once — like both the result of a multiplication and the result of a division?

Kotlin provides `Pair` — a built-in type that bundles exactly two values together, so you can return them as one thing.

```kotlin
// Returns both the product and the quotient of two numbers
// Pair<Int, Int> means: a pair where both items are Ints
fun multiplyAndDivide(number: Int, factor: Int): Pair<Int, Int> {
    return Pair(number * factor, number / factor)
}
```

To get both values out when you call it, use **destructuring** — a shorthand that unpacks both at once:

```kotlin
// The parentheses on the left unpack the Pair into two separate variables
val (product, quotient) = multiplyAndDivide(8, 2)

println("Product: $product")    // Output: Product: 16
println("Quotient: $quotient")  // Output: Quotient: 4
```

If you prefer, you can also access the values using `.first` and `.second`:

```kotlin
val result = multiplyAndDivide(8, 2)
println(result.first)    // 16
println(result.second)   // 4
```

> **Important:** The order of values in `Pair(x, y)` matches the order of variables in `val (a, b)`. The first value goes to the first variable, the second to the second.

---

## Single-Expression Functions: A Shorter Way to Write Simple Functions

When a function's entire job is to calculate and return one single expression, Kotlin gives you a cleaner shorthand. Instead of writing braces, `return`, and the return type, you use `=` and write the expression directly:

**The long way:**
```kotlin
fun multiply(number: Int, multiplier: Int): Int {
    return number * multiplier
}
```

**The short way (single-expression form):**
```kotlin
fun multiply(number: Int, multiplier: Int) = number * multiplier
```

Both versions work identically. The second is just less noise.

Notice you don't write `: Int` either — Kotlin looks at `number * multiplier`, sees that both are `Int`s, and figures out the return type automatically. This is called **type inference**.

More examples of this style:

```kotlin
// Checks if a number is even
fun isEven(n: Int) = n % 2 == 0

// Converts Celsius to Fahrenheit
fun toFahrenheit(celsius: Double) = celsius * 9 / 5 + 32

// Returns the greeting message
fun greet(name: String) = "Hello, $name!"
```

Clean, readable, and still perfectly correct.

> **Only use this form when the function genuinely fits on one line.** If you need multiple steps (declare a variable, do some logic, then return), use the full form with `{ }`. Trying to force a complex function into single-expression form makes code harder to read, not easier.

---

## Parameters Are Read-Only

There's an important rule you need to know about parameters: **you cannot change them inside the function**. They are locked — read-only.

Let's see what happens when you try:

```kotlin
fun incrementAndPrint(value: Int) {
    value += 1        // ❌ ERROR: Val cannot be reassigned
    println(value)
}
```

Kotlin refuses to compile this and gives you:
```
Val cannot be reassigned
```

Why? Because parameters behave like `val` — constants. Once the value comes in, it stays exactly as it was. You can read it, use it, compare it — but you cannot replace it.

**Why is this a good thing?**

Imagine you call `doSomething(myNumber)` and the function secretly changes `myNumber`. Now your variable has a different value than you expected, and you have no idea why. This kind of hidden change leads to bugs that are extremely difficult to find.

By making parameters read-only, Kotlin protects you from this entire category of bugs.

**What to do instead:**

If you need a modified version of the parameter, create a new variable inside the function:

```kotlin
// ✅ Correct approach: create a new variable based on the parameter
fun incrementAndPrint(value: Int): Int {
    val newValue = value + 1    // 'value' stays untouched; 'newValue' is separate
    println(newValue)
    return newValue
}

incrementAndPrint(5)    // prints 6, original 5 is unchanged
```

The original `value` (5) is never touched. `newValue` (6) is a completely separate variable that only exists inside this function.

> **Note for later:** When you learn about classes, you will see that class constructor parameters *can* use `var` or `val`. That's a different context. For regular function parameters, they are always read-only — no `var` or `val` keyword at all.

---

## Overloading: Same Name, Different Parameters

What if you want two functions that do the same kind of thing, but with different types of data?

For example: a `describe` function that works on numbers, and a `describe` function that works on text. You could name them `describeNumber` and `describeText`, but that clutters your code with names.

**Overloading** solves this. Overloading means defining multiple functions with the **same name**, but **different parameters**. Kotlin uses the type or count of the arguments you pass to decide which version to call.

```kotlin
// Version 1: works with an Int
fun getValue(value: Int): Int {
    return value + 1
}

// Version 2: works with a String (same name, different parameter type)
fun getValue(value: String): String {
    return "The value is $value"
}
```

When you call `getValue`, Kotlin looks at what you pass in and picks the right version:

```kotlin
println(getValue(10))        // picks the Int version → Output: 11
println(getValue("hello"))   // picks the String version → Output: The value is hello
```

You can also overload by using a different **number** of parameters:

```kotlin
fun greet(name: String) {
    println("Hello, $name!")
}

fun greet(name: String, title: String) {
    println("Hello, $title $name!")
}

greet("Alice")              // Output: Hello, Alice!
greet("Smith", "Dr.")       // Output: Hello, Dr. Smith!
```

### The One Rule You Cannot Break

**Return type alone is not enough to distinguish two overloads.** If two functions have the same name AND the same parameters, they are the same function as far as Kotlin is concerned — even if they return different types:

```kotlin
fun getValue(value: String): String {
    return "The value is $value"
}

// ❌ ERROR: Conflicting overloads
fun getValue(value: String): Int {
    return value.length
}
```

Both have `getValue(value: String)`. Kotlin cannot tell them apart when you write `getValue("hello")` — it doesn't know whether you want the `String` version or the `Int` version. This is a compile error.

The rule: **overloads must differ in their parameter list** (types, count, or both). Return type alone cannot be the distinguishing factor.

> **Use overloading with care.** Only overload functions that do genuinely similar things with different data types. If two functions do fundamentally different jobs, give them different names. Overloading for the sake of it just creates confusion.

---

## Functions as Variables

Here is something that surprises most beginners: in Kotlin, **a function can be stored in a variable**, just like a number or a string.

### Why would you ever want that?

Imagine you're building a calculator app. You have an `add` function and a `subtract` function. Depending on which button the user presses, you want to run one or the other. Instead of writing an `if/else` block every time, you can store whichever function the user chose in a variable, and simply call that variable.

This makes your code flexible, reusable, and much cleaner.

### Understanding Function Types

Before you can store a function in a variable, you need to understand that every function has a **type**, just like every variable does.

A function's type describes:
- What parameters it takes
- What it returns

The syntax looks like this:

```
(ParameterType1, ParameterType2) -> ReturnType
```

For example:

```kotlin
fun add(a: Int, b: Int): Int { ... }
// Type: (Int, Int) -> Int
// Read as: "a function that takes two Ints and returns an Int"

fun greet(name: String): String { ... }
// Type: (String) -> String
// Read as: "a function that takes a String and returns a String"

fun sayHello(): Unit { ... }
// Type: () -> Unit
// Read as: "a function that takes nothing and returns nothing"
```

`Unit` means "nothing useful returned" — the equivalent of `void` in some other languages.

### Storing a Function in a Variable: The `::` Operator

To get a reference to a function (without calling it), use `::` before its name:

```kotlin
fun add(a: Int, b: Int): Int {
    return a + b
}

// Store a reference to 'add' in a variable.
// '::add' means: "give me a reference to the add function, don't call it yet."
var myFunction = ::add
```

Kotlin infers the type of `myFunction` as `(Int, Int) -> Int` automatically.

Now you can **call through the variable** exactly like calling the function directly:

```kotlin
println(myFunction(4, 2))    // Output: 6  — calls add
```

### Swapping Which Function the Variable Points To

Here's where the power becomes clear. You can change which function the variable references:

```kotlin
fun add(a: Int, b: Int): Int = a + b
fun subtract(a: Int, b: Int): Int = a - b

var operation = ::add          // 'operation' points to 'add'
println(operation(10, 4))     // Output: 6

operation = ::subtract         // now 'operation' points to 'subtract'
println(operation(10, 4))     // Output: 6... no wait, Output: 6 → actually 6... 
// add: 10 + 4 = 14, subtract: 10 - 4 = 6
```

Let's be precise:

```kotlin
var operation = ::add
println(operation(10, 4))    // 10 + 4 = 14

operation = ::subtract
println(operation(10, 4))    // 10 - 4 = 6
```

Same variable. Two different behaviours. Zero `if/else`.

> **You can only swap to a function with the same type.** `operation` has type `(Int, Int) -> Int`. You could not reassign it to `::greet` if greet has type `(String) -> String` — the types don't match.

---

## Passing Functions to Other Functions

Once you can store a function in a variable, the next idea follows naturally: you can **pass a function as an argument to another function**.

This sounds strange at first, but it's enormously powerful.

Here's the setup. You have two functions:

```kotlin
fun add(a: Int, b: Int): Int = a + b
fun subtract(a: Int, b: Int): Int = a - b
```

Now you want one function that can apply *either* of these operations and print the result. Here's how:

```kotlin
// 'operation' is a parameter — but it's a function, not a number or string.
// Its type (Int, Int) -> Int means: any function that takes two Ints and returns an Int.
// 'a' and 'b' are regular Int parameters.
fun printResult(operation: (Int, Int) -> Int, a: Int, b: Int) {
    val result = operation(a, b)    // call whichever function was passed in
    println("Result: $result")
}

// Pass the 'add' function as the first argument
printResult(::add, 10, 4)        // Output: Result: 14

// Pass the 'subtract' function as the first argument
printResult(::subtract, 10, 4)   // Output: Result: 6
```

Let's trace through `printResult(::add, 10, 4)` step by step:
1. `operation` receives `::add` — it now refers to the `add` function
2. `a` receives `10`, `b` receives `4`
3. Inside, `operation(a, b)` calls `add(10, 4)`, which returns `14`
4. `println("Result: $result")` prints `Result: 14`

`printResult` itself doesn't care whether you pass `add`, `subtract`, or any other compatible function. It just calls whatever it receives. This is what makes it reusable.

> This pattern — where a function accepts another function as a parameter — is called a **higher-order function**. You'll use this constantly in Kotlin, especially when working with lists, sorting, filtering, and Android event handling.

---

## The `Nothing` Return Type

Every function you have written eventually finishes and gives control back to whoever called it. Even if it returns nothing useful (`Unit`), it at least stops and returns.

But there is a special category of functions that are **designed to never return**. Kotlin marks these with the return type `Nothing`.

### When Would a Function Never Return?

**Case 1 — Intentional crash (throwing an exception)**

Sometimes your app reaches a state so wrong that continuing would cause silent data corruption or security problems. The safest thing is to crash immediately, loudly, and on purpose:

```kotlin
// This function always throws an error — it never returns normally.
fun crashWithMessage(message: String): Nothing {
    throw IllegalStateException(message)
    // 'throw' stops execution entirely. The function never reaches its end.
}
```

**Case 2 — Infinite loop**

Some programs are designed to run forever — waiting for events (user input, network data, timer ticks) and handling them in a continuous loop. The function that starts this loop never returns:

```kotlin
// This loops forever — it never returns to the caller.
fun infiniteLoop(): Nothing {
    while (true) {
        // process events...
    }
}
```

### Why Bother Declaring `Nothing`?

It's useful information for the compiler. When Kotlin knows a function will never return, it can:

- Skip preparing for code that would run after the call (because nothing will)
- Warn you if you accidentally write code after calling a `Nothing` function (that code can never run)
- Help with smart-casting and flow analysis elsewhere in your code

> **For Android developers:** The Android UI system runs on a main thread powered by an event loop that never exits. When you start building Android apps, you'll understand this type of function from the inside out.

Right now, you won't write `Nothing` functions yourself very often. But you'll encounter them in real code, and now you know what they mean.

---

## Writing Good Functions

Knowing how to *write* a function is different from knowing how to write a *good* function. Here are the principles that separate clean, maintainable code from a tangled mess.

### One function, one job

A good function does exactly one thing. If you can't describe what a function does without the word "and", it's probably doing too much.

```kotlin
// ❌ This function validates the user AND saves them AND sends an email
fun processUser(name: String, email: String) { ... }

// ✅ Each function has one clear job
fun validateUser(name: String, email: String): Boolean { ... }
fun saveUser(name: String, email: String) { ... }
fun sendWelcomeEmail(email: String) { ... }
```

The single-job version is easier to test, easier to change, and easier to understand.

### Same inputs should always give the same output

A reliable function, given the same arguments, always produces the same result. No surprises, no hidden dependency on global state.

```kotlin
// ✅ Predictable — 3 + 4 is always 7, every single time
fun add(a: Int, b: Int): Int = a + b

// ⚠️ Unpredictable — result depends on a global variable that could change
var discount = 10
fun calculatePrice(price: Int): Int = price - discount
```

### Use clear, descriptive names

The name of a function should tell you exactly what it does. You should never need to read the body to understand the purpose.

```kotlin
// ❌ What does this do?
fun calc(x: Int, y: Int): Boolean = x > y

// ✅ Crystal clear
fun isFirstGreaterThanSecond(first: Int, second: Int): Boolean = first > second
```

### Keep functions short

If a function is growing beyond 20–30 lines, that's a signal it's doing too much. Break it into smaller functions. Short functions are easier to read, easier to test, and easier to reuse.

### Prefer returning values over printing inside functions

Printing inside a function is a dead end. The result is displayed and gone. If you return a value instead, the caller can do anything with it — display it, store it, pass it somewhere else, or combine it with other values.

```kotlin
// ❌ Less flexible — result is printed and lost
fun calculateArea(width: Int, height: Int) {
    println(width * height)
}

// ✅ More flexible — caller decides what to do with the result
fun calculateArea(width: Int, height: Int): Int {
    return width * height
}
```

---

## Challenges

### Challenge 1: Is It Prime?

A **prime number** is a number greater than 1 that cannot be divided evenly by anything other than 1 and itself. For example: 2, 3, 5, 7, 11, 13 are prime. 4, 6, 8, 9, 10 are not.

**Your task:** Write a function `isPrime(number: Int): Boolean` that returns `true` if the number is prime and `false` otherwise.

Here's how to approach it step by step:

**Step 1 — Write a helper function first:**

```kotlin
// Returns true if 'number' is exactly divisible by 'divisor' (no remainder).
// The % operator gives the remainder of a division.
// If the remainder is 0, the division was exact — so it IS divisible.
fun isNumberDivisible(number: Int, divisor: Int): Boolean {
    return number % divisor == 0
}
```

**Step 2 — Write the main function:**

```kotlin
fun isPrime(number: Int): Boolean {
    // Numbers less than 2 are never prime by definition
    if (number < 2) return false

    // Try every potential divisor from 2 up to (but not including) the number itself.
    // If any of them divide evenly, it's not prime — return false immediately.
    for (divisor in 2 until number) {
        if (isNumberDivisible(number, divisor)) {
            return false    // found a divisor — definitely not prime
        }
    }

    // If we made it through the loop with no divisors found, it's prime
    return true
}
```

**Test it:**

```kotlin
println(isPrime(6))      // false — divisible by 2 and 3
println(isPrime(13))     // true  — only 1 and 13 divide it
println(isPrime(8893))   // true
```

**Want to make it faster?**

You actually only need to check divisors up to the **square root** of the number. Here's the intuition: if a number `n` has a divisor `a` larger than its square root, then it must also have a corresponding divisor `b = n / a` that is *smaller* than the square root — and you would have already found `b`. So you don't need to check beyond the square root.

```kotlin
fun isPrimeFaster(number: Int): Boolean {
    if (number < 2) return false
    val limit = Math.sqrt(number.toDouble()).toInt()   // calculate the square root
    for (divisor in 2..limit) {                        // only check up to the square root
        if (isNumberDivisible(number, divisor)) return false
    }
    return true
}
```

---

### Challenge 2: The Fibonacci Sequence (Recursion)

The **Fibonacci sequence** is a famous sequence in mathematics where each number is the sum of the two before it:

```
1, 1, 2, 3, 5, 8, 13, 21, 34, 55, ...
```

So: `fibonacci(1) = 1`, `fibonacci(2) = 1`, `fibonacci(3) = 1 + 1 = 2`, `fibonacci(4) = 1 + 2 = 3`, and so on.

This challenge introduces **recursion** — a function that calls *itself*. It sounds circular, but it works because every recursive call moves closer to a known answer (the base case), and eventually the chain stops.

Think of it like Russian nesting dolls. Each doll contains a smaller version, until you reach the tiniest one that contains nothing.

**Your function:**

```kotlin
fun fibonacci(number: Int): Int {
    // If the input makes no sense (negative), return 0
    if (number < 1) return 0

    // Base cases: the sequence starts with two 1s.
    // These are the "smallest dolls" — no more recursion needed.
    if (number == 1 || number == 2) return 1

    // Recursive case: each Fibonacci number is the sum of the previous two.
    // This calls fibonacci again with a smaller number, moving toward the base case.
    return fibonacci(number - 1) + fibonacci(number - 2)
}
```

**Let's trace `fibonacci(4)` to see how it works:**

```
fibonacci(4)
  = fibonacci(3) + fibonacci(2)
  = (fibonacci(2) + fibonacci(1)) + 1
  = (1 + 1) + 1
  = 3
```

It keeps breaking the problem into smaller pieces until it hits 1 or 2, which it knows the answer to immediately.

**Test it:**

```kotlin
println(fibonacci(1))    // 1
println(fibonacci(2))    // 1
println(fibonacci(3))    // 2
println(fibonacci(4))    // 3
println(fibonacci(5))    // 5
println(fibonacci(6))    // 8
println(fibonacci(10))   // 55
```

> **Performance note:** This recursive approach is elegant but slow for large inputs. `fibonacci(50)` would make billions of recursive calls. A technique called **memoisation** stores the results of previous calls so they don't get recalculated. That's a more advanced topic for later — for now, this version is perfect for understanding how recursion works.

---

## Key Points

- A **function** is a named, reusable block of code. Declare it once with `fun`, call it anywhere with `functionName()`.
- **Parameters** let a function receive data from outside. **Arguments** are the actual values you pass when calling.
- **Named arguments** (`paramName = value`) make call sites readable and allow you to pass parameters out of order.
- **Default values** (`param: Type = default`) make parameters optional — the default is used when no argument is provided.
- **Return values** (`: ReturnType` + `return value`) let a function produce a result the caller can use.
- **`Pair<T1, T2>`** lets you return two values at once. Destructure with `val (a, b) = ...`.
- **Single-expression functions** (`fun name() = expression`) are a concise form for functions with one-line bodies.
- **Parameters are always read-only.** Create a new variable if you need to work with a modified value.
- **Overloading** means multiple functions with the same name but different parameters. Return type alone cannot distinguish overloads.
- **Functions can be stored in variables** using `::functionName`. Every function has a type like `(Int, Int) -> Int`.
- **Functions can be passed to other functions** as arguments — this is the foundation of higher-order functions.
- The **`Nothing` return type** marks functions that never return — they either loop forever or always throw an exception.
- **Good functions** do one job, have predictable outputs, use clear names, stay short, and return values rather than just printing.
