package classes

fun main(){
    var c1 = Car("tata","nexon","1.2L Revotron Turbo-Petrol",4)
    c1.move()

    var p1 = Plane("airbus","Airbus A320 neon","tufofan",4)
    p1.move()
}

open class Vehicle(val name:String,val model:String){

    open fun move(){
        println("vehicle is moving")
    }
    fun stop(){
        println("vehicle stopped")
    }


}

class Car(name:String,model:String,val engine:String,val door:Int): Vehicle(name,model){

    override fun move(){
        super.move()
        CarMove()
    }

    fun CarMove(){

        println("$name $model started moving")
    }


}

class Plane(name:String,model:String,val engine:String,val door:Int): Vehicle(name,model){

    override fun move() {
//        super.move()  on not using
        println("plane started flying")
    }
}