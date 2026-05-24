# Chapter 11: Classes
### Kotlin Apprentice — Beginner-Friendly Rewrite

---

## Why Classes Exist

So far, you've worked with individual pieces of data — a String here, an Int there, a list of values. This works fine for simple programs.

But real programs model real things. A student has a first name, a last name, a list of grades, and a credit total. A product in a store has a name, a price, a size, and a quantity in stock. These things have both **data** (the facts about them) and **behaviour** (what they can do).

Trying to represent a student with a bunch of separate variables gets messy fast:

```kotlin
// Without classes — scattered, hard to manage
var studentFirstName = "Jane"
var studentLastName  = "Appleseed"
var studentGrades    = mutableListOf<String>()
var studentCredits   = 0.0
```

What if you have 30 students? You'd need 30 × 4 = 120 separate variables. And passing a student to a function would mean passing four separate arguments every time.

A **class** solves this. It lets you bundle related data and behaviour into one named unit. Then you can create as many instances of that unit as you need.

This style of programming — organising code around types that hold both data and behaviour — is called **object-oriented programming (OOP)**.

---

## Your First Class

Here's how you define a class in Kotlin:

```kotlin
class Person(var firstName: String, var lastName: String) {

    val fullName: String
        get() = "$firstName $lastName"   // Combines firstName and lastName on demand
}
```

Let's break this down piece by piece.

**`class Person`** — the `class` keyword followed by the name. By convention, class names start with a capital letter.

**`(var firstName: String, var lastName: String)`** — this is the **primary constructor**. It defines what information you must supply when creating a `Person`. Here, both `firstName` and `lastName` are declared as `var` — they're mutable `String` properties.

**`{ ... }`** — the curly braces contain the **body** of the class. Everything inside is a member of the class — properties and functions.

**`fullName`** — this is a third property with a **custom getter**. Instead of storing a separate value, it computes its result from `firstName` and `lastName` every time you read it.

---

## Creating an Instance

A class is just a blueprint. To actually use it, you create an **instance** — a real, living object based on that blueprint.

```kotlin
// Create an instance of Person using the primary constructor
val john = Person(firstName = "Johnny", lastName = "Appleseed")
```

You call the class name like a function, passing the values the constructor requires. The result is stored in `john`.

Now you can access the properties:

```kotlin
println(john.firstName)  // > Johnny
println(john.lastName)   // > Appleseed
println(john.fullName)   // > Johnny Appleseed
```

`john.fullName` runs the custom getter, which combines `john.firstName` and `john.lastName` and returns the result.

The instances you create are called **objects**. "Objects" in object-oriented programming just means "instances of classes."

---

## Reference Types — A Crucial Concept

Before going further, you need to understand something fundamental about classes in Kotlin: they are **reference types**.

This is different from the basic types you've used so far (Int, String, Boolean, etc.), which are **value types**.

To understand the difference, think of an analogy.

Imagine you write your home address on a piece of paper and give it to a friend. Your friend now has the **address** — a reference to where you live. If you repaint your house, your friend doesn't get a new piece of paper. They still have the same address, and when they visit, they'll see the newly painted house.

This is how class instances work. The variable doesn't hold the object directly — it holds an **address** (reference) pointing to where the object lives in memory.

Here's what this looks like in code:

```kotlin
class SimplePerson(val name: String)

var var1 = SimplePerson(name = "John")
```

`var1` does not *contain* John. It contains a **reference** — an address pointing to the `SimplePerson` object somewhere in memory.

Now assign `var1` to another variable:

```kotlin
var var2 = var1
```

What got copied? Not the object. Just the **reference**. Both `var1` and `var2` now point to the **same** `SimplePerson` object in memory.

---

## The Heap and the Stack (Simplified)

When you run a Kotlin program, memory is divided into two regions: the **stack** and the **heap**.

**The stack** is fast and tightly managed. When a function runs, it creates a stack frame that holds local variables. When the function finishes, the frame is automatically destroyed. Think of it like a notepad you use during a calculation — you write things down, do the math, then erase everything.

**The heap** is a larger, more flexible pool of memory. Objects (class instances) live here. The heap doesn't automatically clean up — objects stay alive as long as something holds a reference to them.

When you write `val john = Person(...)`:
- The `Person` object is created on the **heap**
- The reference (address) to that object is stored in `john` on the **stack**

You don't need to manage the heap manually in Kotlin. The system's garbage collector handles freeing memory when nothing references an object anymore. But understanding that objects live on the heap explains why classes behave the way they do.

---

## Sharing References — The Consequence

Since assigning a class variable copies the reference (not the object), changes through one variable are visible through all variables pointing to the same object.

```kotlin
var homeOwner = john        // homeOwner now references the SAME object as john

john.firstName = "John"     // Mutate the object through 'john'

println(john.firstName)       // > John
println(homeOwner.firstName)  // > John  ← also changed, because same object!
```

Even though you changed `firstName` using `john`, `homeOwner` sees the change too. They're both looking at the same object.

This shared reference behaviour is very powerful — useful when you want many parts of your program to see the same up-to-date data. But it can also cause bugs when changes happen unexpectedly. We'll see this more concretely later.

---

## Object Identity — `===` vs `==`

With shared references, a new question arises: how do you know if two variables point to the *same* object, versus two *different* objects that just happen to have the same data?

Kotlin gives you two operators:

| Operator | Meaning |
|---|---|
| `==` | **Equality** — Do these two objects have the same *values*? |
| `===` | **Identity** — Are these two variables pointing to the *same* object in memory? |

```kotlin
println(homeOwner === john)  // > true — same object, same memory address
```

Now create a brand new `Person` with identical data:

```kotlin
val impostorJohn = Person(firstName = "John", lastName = "Appleseed")

println(john === impostorJohn)       // > false — different objects in memory
println(impostorJohn === homeOwner)  // > false — both impostors point elsewhere
```

Even though `impostorJohn` has the exact same first and last name as `john`, it's a completely different object. The `===` identity check catches this.

Now watch what happens when you reassign a variable:

```kotlin
homeOwner = impostorJohn     // homeOwner now points to the impostor
println(john === homeOwner)  // > false — john still points to the original

homeOwner = john             // reassign back to the original
println(john === homeOwner)  // > true — same object again
```

`===` only returns `true` when both sides point to the exact same block of memory.

**Practical use:** You probably won't use `===` very often in everyday Kotlin. But understanding it reveals what "reference type" actually means, and it's essential for debugging subtle bugs where two variables share state unintentionally.

---

## Methods — Behaviour Inside a Class

A class can contain functions. When a function lives inside a class, it's called a **method**. Methods have direct access to the class's properties.

Let's build two classes — `Grade` and `Student`:

```kotlin
// Represents a single academic grade
class Grade(
    val letter: String,    // "A", "B", "C", etc.
    val points: Double,    // Grade points earned (e.g. 16.0 for an A worth 4 credits)
    val credits: Double    // Number of credits this course is worth
)

// Represents a student with a list of grades
class Student(
    val firstName: String,
    val lastName: String,
    val grades: MutableList<Grade> = mutableListOf(),  // Starts empty
    var credits: Double = 0.0                           // Total credits, starts at 0
) {
    // A method that records a new grade for this student
    fun recordGrade(grade: Grade) {
        grades.add(grade)          // Add the grade to the list
        credits += grade.credits   // Add the grade's credits to the running total
    }
}
```

Now use these classes:

```kotlin
val jane = Student(firstName = "Jane", lastName = "Appleseed")

val history = Grade(letter = "B", points = 9.0,  credits = 3.0)
val math    = Grade(letter = "A", points = 16.0, credits = 4.0)

jane.recordGrade(history)  // Records history grade, credits becomes 3.0
jane.recordGrade(math)     // Records math grade, credits becomes 7.0

println(jane.credits)  // > 7.0
```

Notice: `recordGrade()` is a method. It lives inside the class and modifies the object's own properties directly.

---

## Mutability and Constants — `val` on Classes

You defined `jane` as a `val`:

```kotlin
val jane = Student(firstName = "Jane", lastName = "Appleseed")
```

But then you called `jane.recordGrade(history)` which changed her grades and credits. Didn't you say `val` means constant?

Here's the key insight: `val` makes the **reference** constant — not the object itself.

```
jane  →  [reference]  →  Student("Jane")
```

`val` locks the arrow. `jane` will always point to this specific `Student` object. You cannot reassign `jane` to point to a different `Student`:

```kotlin
// ERROR — can't reassign a val reference to a different object
jane = Student(firstName = "John", lastName = "Appleseed")
```

But the object at the other end of the arrow is still mutable. Its internal properties (`grades`, `credits`) can change freely. The lock is on the reference, not the object's contents.

If you changed `jane` from `val` to `var`, you could reassign it to a different `Student` entirely:

```kotlin
var jane = Student(firstName = "Jane", lastName = "Appleseed")
jane = Student(firstName = "John", lastName = "Appleseed")
// Now jane points to a different Student object
// The original "Jane" object has no references and will be cleaned up
```

To protect individual properties from being changed, declare them as `val` inside the class:

```kotlin
class Student(
    val firstName: String,   // Can't change after creation
    val lastName: String,    // Can't change after creation
    var credits: Double = 0.0  // Can change
)
```

---

## State and Side Effects — The Hidden Danger

Because class instances are mutable and can be referenced from many places, they have **state** — data that can change over time. This is powerful, but it comes with a danger: **side effects**.

A side effect is when calling a method changes something in a way that's not obvious from the outside.

Here's an example. Suppose the `Student` class's `recordGrade()` assumes each grade will only be recorded once. But watch what happens:

```kotlin
println(jane.credits)  // > 7.0

// Teacher made a mistake — math was actually 5 credits, not 4
val correctedMath = Grade(letter = "A", points = 20.0, credits = 5.0)
jane.recordGrade(correctedMath)

println(jane.credits)  // > 12.0, not 12 as you might expect!
```

Wait — why 12? Because `math` was already recorded in a previous call. The corrected grade was added *on top* of the original. The `Student` class naively assumed no grade would be recorded twice. It silently gave Jane an extra 4 credits.

This is a side effect. The `recordGrade()` method changed state in a way that surprised us.

As programs grow, this problem gets worse. A `Student` with 20 properties and 10 methods can produce side effects that are very hard to track down. This is why experienced developers are careful about mutation and shared references.

Being aware of state and side effects is one of the most important skills in programming.

---

## Data Classes — Less Boilerplate, More Power

Very often, you'll create a class whose main purpose is simply to hold data. Think of a student record, a product in a database, a coordinate point, or a user profile.

For these data-holding classes, you frequently need four common operations:
1. **Print** the object in a readable way
2. **Compare** two objects to see if their data is equal
3. **Copy** an object to get a duplicate with perhaps one changed property
4. **Hash** the object (so it can be used as a map key)

If you use a regular class, you have to write all this yourself. Here's what that looks like for a full `Student` class:

```kotlin
class Student(
    var firstName: String,
    var lastName: String,
    var id: Int
) {
    // Custom equals() — compares all properties
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        if (javaClass != other.javaClass) return false
        val obj = other as Student?
        if (firstName != obj?.firstName) return false
        if (id != obj.id) return false
        if (lastName != obj.lastName) return false
        return true
    }

    // Custom hashCode() — required whenever equals() is overridden
    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + firstName.hashCode()
        result = prime * result + id
        result = prime * result + lastName.hashCode()
        return result
    }

    // Custom toString() — readable output when printed
    override fun toString(): String {
        return "Student (firstName=$firstName, lastName=$lastName, id=$id)"
    }

    // Custom copy() — create a duplicate with optional changes
    fun copy(
        firstName: String = this.firstName,
        lastName: String = this.lastName,
        id: Int = this.id
    ) = Student(firstName, lastName, id)
}
```

That's a lot of repetitive code just to support the basics. And if you add a new property, you have to update `equals()`, `hashCode()`, `toString()`, and `copy()` by hand.

### Enter `data class`

Kotlin has a solution: **data classes**. Add the `data` keyword before `class`, and Kotlin generates all four of those methods for you automatically:

```kotlin
data class StudentData(
    var firstName: String,
    var lastName: String,
    var id: Int
)
```

That's it. One line instead of dozens. Kotlin automatically provides:
- `equals()` — compares all constructor properties
- `hashCode()` — computed from all constructor properties
- `toString()` — formatted output of all properties
- `copy()` — create a modified copy with specific properties changed

Let's see it in action:

```kotlin
val marie = StudentData("Marie", "Curie",   id = 1)
val emmy  = StudentData("Emmy",  "Noether", id = 2)
val marieCopy = marie.copy()   // A copy with the same data

println(marie)
// > StudentData(firstName=Marie, lastName=Curie, id=1)

println(emmy)
// > StudentData(firstName=Emmy, lastName=Noether, id=2)

println(marie == emmy)      // > false — different data
println(marie == marieCopy) // > true  — same data values

println(marie === marieCopy) // > false — different objects in memory
```

The `==` comparison now works correctly for data classes — it compares **values** using the generated `equals()`. The `===` identity check still tells you they're separate objects.

### Copying With Changes

The generated `copy()` is especially useful. You can create a modified version of an object without touching the original:

```kotlin
val updatedMarie = marie.copy(lastName = "Sklodowska-Curie")
// marie is unchanged; updatedMarie has the new last name

println(marie.lastName)        // > Curie
println(updatedMarie.lastName) // > Sklodowska-Curie
```

This is a safe way to "change" data without mutating the original — very useful when you want to avoid side effects.

---

## Destructuring Declarations

Data classes support a feature called **destructuring declarations** — a way to unpack all the properties of an object into separate variables at once.

You already saw this with maps in Chapter 9. Data classes support the same thing:

```kotlin
val (firstName, lastName, id) = marie
// Unpacks marie's properties in constructor order

println(firstName)  // > Marie
println(lastName)   // > Curie
println(id)         // > 1
```

The `val (firstName, lastName, id)` syntax creates three new variables simultaneously, each assigned the corresponding property of `marie`.

This is particularly useful when a function returns a data class and you want to unpack the result immediately:

```kotlin
fun getStudent(): StudentData {
    return StudentData("Ada", "Lovelace", id = 3)
}

val (first, last, studentId) = getStudent()
println("$first $last, ID: $studentId")  // > Ada Lovelace, ID: 3
```

**Important warning:** The variables are assigned **in constructor order** — not by name. If your data class constructor is `StudentData(firstName, lastName, id)` and you write `val (a, b, c) = marie`, then `a` gets `firstName`, `b` gets `lastName`, and `c` gets `id`. If you swap the order of variables, you get the wrong values — no error, just silent incorrect behaviour.

---

## When to Use a Regular Class vs a Data Class

Here's a simple guide:

| Situation | Use |
|---|---|
| Object has behaviour (methods that change state) | Regular `class` |
| Object mainly holds data you want to compare, copy, or print | `data class` |
| You need only one instance ever (covered in Chapter 12) | `object` |

For model objects — students, products, users, coordinates, messages — `data class` is almost always the right choice.

---

## Putting It All Together

Here's a complete example showing a regular class and a data class working together:

```kotlin
// Data class — just holds data about a book
data class Book(
    val title: String,
    val author: String,
    val price: Double
)

// Regular class — has behaviour (a shopping cart)
class BookStore {
    private val inventory = mutableListOf<Book>()
    var totalSales = 0.0

    // Method — adds a book to the store's inventory
    fun addBook(book: Book) {
        inventory.add(book)
    }

    // Method — sells a book (removes it and records the sale)
    fun sellBook(title: String) {
        val book = inventory.find { it.title == title }
        if (book != null) {
            inventory.remove(book)
            totalSales += book.price
            println("Sold: ${book.title} for \$${book.price}")
        } else {
            println("Book not found: $title")
        }
    }

    fun printInventory() {
        println("--- Inventory ---")
        inventory.forEach { println("${it.title} by ${it.author} — \$${it.price}") }
    }
}

// Using the classes
val store = BookStore()
store.addBook(Book("Kotlin Apprentice", "Irina Galata", 49.99))
store.addBook(Book("Clean Code", "Robert Martin", 35.00))

store.printInventory()
store.sellBook("Clean Code")
println("Total sales: \$${store.totalSales}")
```

---

## Challenges

### Challenge 1: Movie Lists

Build a movie-viewing app where users can create and share movie lists.

```kotlin
// Data class — a movie list has a name and a list of movie titles
class MovieList(val name: String) {
    val titles: MutableList<String> = mutableListOf()

    // Method — prints all movies in this list
    fun print() {
        println("Movie list: $name")
        if (titles.isEmpty()) {
            println("  (no movies yet)")
        } else {
            titles.forEach { println("  - $it") }
        }
    }
}

// User class — can create and manage movie lists
class User(val name: String) {
    // Map from list name to MovieList — a user can have multiple lists
    val movieLists: MutableMap<String, MovieList> = mutableMapOf()

    // Adds a MovieList to this user's collection
    fun addList(movieList: MovieList) {
        movieLists[movieList.name] = movieList
    }

    // Retrieves a MovieList by name (returns null if not found)
    fun list(name: String): MovieList? {
        return movieLists[name]
    }
}

// Create two users
val jane = User(name = "Jane")
val john = User(name = "John")

// Jane creates a list and shares it with John
val favourites = MovieList(name = "Favourites")
jane.addList(favourites)
john.addList(favourites)  // John gets the SAME list object — shared reference!

// Both users add movies to the shared list
jane.list("Favourites")?.titles?.add("Inception")
john.list("Favourites")?.titles?.add("The Matrix")

// Print from both users — both see all changes because it's the same object
jane.list("Favourites")?.print()
// Movie list: Favourites
//   - Inception
//   - The Matrix

john.list("Favourites")?.print()
// Movie list: Favourites
//   - Inception
//   - The Matrix
```

Because `MovieList` is a regular class (a reference type), both `jane` and `john` hold a reference to the **same** `MovieList` object. Changes through either user are immediately visible to both. This is the shared reference behaviour in action.

---

### Challenge 2: T-Shirt Store

Design a set of classes for a T-shirt store. Decide which should be regular classes and which should be data classes.

```kotlin
// Data class — a shirt style is pure data (size, color, price, optional image)
data class TShirt(
    val size: String,          // "S", "M", "L", "XL"
    val color: String,         // "Red", "Blue", "Black", etc.
    val price: Double,         // Price in dollars
    val frontImage: String?    // Optional image name on the front, null if plain
)

// Data class — a shipping address is pure data
data class Address(
    val name: String,
    val street: String,
    val city: String,
    val zipCode: String
)

// Regular class — a shopping cart has behaviour (calculate total)
class ShoppingCart(val shippingAddress: Address) {
    val items: MutableList<TShirt> = mutableListOf()

    // Method — adds a T-shirt to the cart
    fun addItem(shirt: TShirt) {
        items.add(shirt)
    }

    // Method — calculates and returns the total price
    fun totalCost(): Double {
        return items.fold(0.0) { total, shirt -> total + shirt.price }
    }

    fun printCart() {
        println("Cart for ${shippingAddress.name}:")
        items.forEach {
            println("  ${it.color} ${it.size} T-shirt — \$${it.price}" +
                    if (it.frontImage != null) " [image: ${it.frontImage}]" else "")
        }
        println("  Total: \$${totalCost()}")
    }
}

// Regular class — a user account has behaviour (manage cart)
class User(
    val name: String,
    val email: String,
    var cart: ShoppingCart
)

// --- Using the classes ---

val address = Address(
    name = "Jane Appleseed",
    street = "123 Main St",
    city = "Springfield",
    zipCode = "12345"
)

val jane = User(
    name = "Jane",
    email = "jane@example.com",
    cart = ShoppingCart(shippingAddress = address)
)

jane.cart.addItem(TShirt(size = "M", color = "Blue",  price = 24.99, frontImage = "logo.png"))
jane.cart.addItem(TShirt(size = "L", color = "Black", price = 19.99, frontImage = null))

jane.cart.printCart()
// Cart for Jane Appleseed:
//   Blue M T-shirt — $24.99 [image: logo.png]
//   Black L T-shirt — $19.99
//   Total: $44.98
```

**Design decisions:**
- `TShirt` and `Address` are `data class` because they're pure data with no behaviour. You want to easily compare, copy, and print them.
- `ShoppingCart` is a regular `class` because it has meaningful behaviour (`addItem()`, `totalCost()`) and mutable state (the item list grows over time).
- `User` is a regular `class` because it manages a cart reference and could have login/logout behaviour added later.

---

## Key Points

- A **class** is a named type that bundles data (properties) and behaviour (methods) together.
- You create an **instance** of a class by calling it like a function with the required constructor arguments.
- Instances are called **objects**.
- Classes are **reference types** — variables hold a reference (address) to an object, not the object itself.
- Objects live on the **heap**; references are stored on the **stack**.
- Assigning a class variable to another variable copies the **reference**, not the object. Both variables point to the same object.
- Changes made through one reference are visible through all references pointing to the same object.
- `===` checks **identity** — whether two variables point to the same object in memory.
- `==` checks **equality** — whether two objects have the same values (using `equals()`).
- `val` on a class variable makes the **reference** constant — you can't reassign the variable, but you can still mutate the object.
- Mutable objects have **state**. Methods that change state can produce **side effects** — changes that are not obvious to the caller.
- **Data classes** (`data class`) automatically generate `equals()`, `hashCode()`, `toString()`, and `copy()`. Use them for model objects whose main purpose is holding data.
- **Destructuring declarations** unpack a data class into separate variables in constructor order.
