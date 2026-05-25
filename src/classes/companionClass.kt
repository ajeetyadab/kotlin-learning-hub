package classes

// companion objects are directly called on class no need to
//enunciate the object

fun main(){
    println(Calculator.sum(3,4))
    println(Calculator.substraction(10,3))
}
class Calculator{

    companion object{
       fun  sum(a:Int,b:Int):Int{
            return a+b
        }


        fun substraction(a:Int,b:Int):Int{
            return a-b
        }
    }

}