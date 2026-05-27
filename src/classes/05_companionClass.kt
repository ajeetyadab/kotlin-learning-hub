package classes

// companion objects are directly called on class no need to
//enunciate the object

fun main(){
    println(Calculator.sum(3,4))
    println(Calculator.substraction(10,9))
    println(Calculator.comProp)
    println(lekhpal.duty1)
}
class Calculator{

    companion object{
        val comProp: String = "Hello I am comp property"

       fun  sum(a:Int,b:Int):Int{
            return a+b
        }


        fun substraction(a:Int,b:Int):Int{
            return a-b
        }
    }

}

object lekhpal{
    var duty1: String = "Mutation"
    fun landMeasureMent(){
        println("only does measurement if landholder gives concent i.e.not core duty")
    }
}