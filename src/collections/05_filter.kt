package collections

fun main(){
    val myList = listOf("one","two","three","four")
    val myMap = mapOf("key 1" to 1, "key 2" to 2,"key 3" to 3 ,"key 101" to 101)

    var filteredList = myList.filter { it.length>3 }

    println(filteredList)

    println(myMap.filter { it.key.endsWith("1") && it.value>100 })

    val filteredIdx = myList.filterIndexed { index,value->(index != 0) && value.length<5 }

    val filteredNot = myList.filterNot { it.length<=3 }

    println(filteredIdx)
    println(filteredNot)



    // partition
    println("\n")
    var(match,rest) = myList.partition { it.length>3 }

    println("match:$match")
    println("rest: $rest")

    println("\n")

// tests
    println(myList.any{it.endsWith("e")})
    println(myList.none { it.endsWith("w") })
    println(myList.all { it.length>1 })


    // exercises
    println("\n")
    println("---exercises---")
    val scores = mapOf("Asha" to 88,"Ravi" to 45, "Meera" to 92, "Dev" to 60)
    var x = scores.filter { it.value >60 }.keys
    println(x)

    val empty = listOf<Int>()
    println(empty.all { it > 100 })
    println(empty.any { it > 100 })

}


