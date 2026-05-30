package collections
//
//fun main(){
//
//    // plus
//    val number = mutableListOf("one","two","three","five","six")
//    println(number +"four")
//
//    // minus
//    println(number -"three")
//    println(number - mutableSetOf("five","six"))
//
//
//
//}


//fun main (){
//    // groupings
//
//    val numbers = mutableListOf("one","two","three","four","five")
//
//    println(numbers.groupBy { it.first().uppercase()})
//    println(numbers.groupBy(keySelector = {it.first()}, valueTransform = {it.uppercase()}))
//    println(numbers.groupBy(keySelector = {it.first().uppercase()}))
//
//}

fun main(){

    val numbers = listOf("onee","owoo","three","four","oie","other","occupation")

//    println(numbers.slice(1..3))
//
//    println(numbers.slice(0..4 step 2))
//
//    println(numbers.take(1))
//
//    println(numbers.takeLast(1))
//
//    println(numbers.drop(2))

    // ////"Keep taking elements from the begining of the list as long as the condition is true."

    println(numbers.takeWhile(predicate = {it.startsWith("o")})) // returns only first match

    // "Keep taking elements from the last of the list as long as the condition is true."
    println(numbers.takeLastWhile(predicate = {it.startsWith("o")})) // returns the last match

    ////"Keep dropping elements from the last of the list as long as the condition is true."
    println(numbers.dropLastWhile {it.startsWith("o")})


    //"Keep dropping elements from the start of the list as long as the condition is true."
    println(numbers.dropWhile(predicate = {it.length >3}))


    //chunks
    var numberString = (0..10).toList()
    println(numberString.chunked(3){it.sum()})
}
