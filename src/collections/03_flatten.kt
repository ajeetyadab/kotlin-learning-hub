package collections

fun main(){

println(numbers[0][1])

    // looping through this nested arrays

    for(number in numbers){
        for(num in number){
            println("$num")
        }
    }

    // using flatten method
    val numbersFlatten = numbers.flatten()
    println(numbersFlatten.javaClass)
}

val numbers = arrayOf(arrayOf(1,2,3),arrayOf(4,5,6),arrayOf(7,8,9))
