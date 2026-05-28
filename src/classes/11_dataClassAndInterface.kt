package classes

import java.lang.classfile.instruction.SwitchCase


// data class
data class userDetails(val name:String,val age:Int,val role:String)


// interfaces

interface Animal{
    // properties need not to be initialised in interfaces
    val name: String
    val sound:String

    fun makeSound(){
        println("$name says $sound")
    }
    fun describe()
}

interface Swimmable{
    fun swimming(){
        println("splashing thru water")
    }
}
interface Flyable{
    fun fly(){
        println("soaring through sky")
    }
}

// classes implementing interfaces
class Dog :Animal{
    override val name = "Dog"
    override val sound = "woof"
    override fun describe() {
        println("i am $name . a loyal pet")
    }

}
class Duck:Animal, Swimmable,Flyable{
    override val name = "Duck"
    override val sound = "Quack"
    override fun describe() {
        println("Hi i am $name the Duck i can swim and fly")
    }

}






fun main(){
    val u1 = userDetails("aj",38,"freelancer")
    println(u1)

    val d1 = Duck()
    println(d1.name)
   d1.describe()
    println(d1.sound)
    d1.swimming()
    d1.fly()


    // extra

val loginButton = Button("login",1212,object : OnClickListner{
    override fun onClick() {
        TODO("Not yet implemented")
    }

})

}


// extra
class Button(val text:String,val id:Int,onClickListener:OnClickListner)


interface OnClickListner{
    fun onClick()
}