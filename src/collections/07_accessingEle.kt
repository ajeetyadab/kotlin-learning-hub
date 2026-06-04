package collections

fun main(){
    val myList = mutableListOf("one","two","three","five")

    println(myList.elementAt(1))

    println(myList.first{it.length>3})

    println(myList.last())

    println(myList.isEmpty())
    println(myList.random())
}