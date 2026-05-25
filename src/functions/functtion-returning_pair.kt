package functions

import jdk.internal.net.http.common.Pair.pair

fun main(){

    var c1 = Calculator(50,40)

    println(c1.first)
    println(c1.second)

    // or using destructuring
    var(number1,number2) = c1

    println(number1)
    println(number2)


}

fun Calculator(a:Int,b:Int): Pair<Int, Int> {
    var sum = a+b
    var sub = a-b
  return Pair(sum,sub)

}


