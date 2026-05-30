package collections


fun main(){
    var numberString = listOf("one","two","three")
    println(numberString.joinToString())



    //Every time you modify a String, a new object is created in memory.
    //
    //For small work → fine.
    //For repeated modifications (loops, text building, logs, JSON, SQL queries) → inefficient.
    //
    //That is where StringBuffer and StringBuilder come in.

    val sb = StringBuffer("the list of numbers ")

    println(numberString.joinTo(sb))

    println(numberString.joinToString(separator = "|", prefix = "start :", postfix = " :end"))
//
    var numbers: List<Int> = (1..100).toList()
//
    println(numbers.joinToString(limit = 15, truncated = "...."))
//
    println(numberString.joinToString { " ele : ${it.uppercase()}" })


}



// exercise
//fun main (){
//    // execise 1
//
//    val fruits = listOf<String>("mango","banana","apple")
//    println(fruits.joinToString(separator = " - "))
//    println(fruits.joinToString(prefix = "[", postfix = "]"))
//
//    // exercise2
//
//    val names = listOf<String>("ajeet","ravi","sunil")
//
//    println(names.joinToString { names->names[0].uppercase()+names.substring(1,) })
//
//    println(names.joinToString { name->name.replaceFirstChar {it.uppercase() } })
//    // kotlin does not support slicing instead use substring to achieve javascript/ python  type slicing
//
//
//    val task = listOf("wakeup","study","kotlin","sleep")
//
//for((index,value) in task.withIndex()){
//    println("${index + 1}. $value")
//}
//
//    // truncated big range
//    val range = (1..1000).toList()
//    println(range.joinToString(limit = 10 , truncated = "and more"))
//
//
//    // append into a buffer with
//
//    val error = listOf("disk full","timeout","bad input")
//
//   val sb = StringBuilder("Log: ")
//    println(error.joinTo(sb, separator = " | "))
//
//    // Exercise 7 — Mini real-world challenge: build a CSV row
//    val row = listOf("Ajeet", "Meerut", "Kotlin Learner")
//    println(row.joinToString { "\"$it\"" })
//
//
//
//
//
//
//}

