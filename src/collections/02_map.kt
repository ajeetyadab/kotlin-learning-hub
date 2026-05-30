package collections

fun main(){
  println(myMap[1])

//    keys

    myMap[2] = "RRaghu"
    myMap.remove(2)

    myMap.forEach{println("key: ${it.key}, value: ${it.value}")}



}

val myMap = mutableMapOf<Int,String>(1 to "Aj",2 to "Raghu",3 to "Shyam")
