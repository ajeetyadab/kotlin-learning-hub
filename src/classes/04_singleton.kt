package classes

import javax.naming.Context

fun main(){
    // ex-1
    DataManager.connect()
    DataManager.connect()

    // ex-3
    val instance: Database? = Database.getInstance()
    println(instance)
    val instance2 = Database.getInstance()
    println(instance2)


//val mgr = MyManager() cant call a private constructor like this

}

// ex-1
object DataManager {

    fun connect(){
        println("Connected")
    }

}
// that's okay but with object we cannot pass parameters
// ex-2
class MyManager private constructor(context: Context) {
    companion object {
        @Volatile
        private var INSTANCE: MyManager? = null

        fun getInstance(context: Context): MyManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: MyManager(context).also { INSTANCE = it }
            }
        }
    }
}



// ex-3
class Database private constructor(){
    companion object{
        private var instance: Database? = null
        fun getInstance(): Database?{
            if(instance == null){
                instance = Database()
            }
            return instance
        }

    }
}


// I am not getting intution about how a variable inside a class can be of type class itsels
// let understand this by this example

class Person(val name:String,val age:Int){
    var dad: Person? = null

}
// usage eample in main fun

//fun main(){
//    val p1 = Person("raj",20)
//    val p2 = Person("Shyam",45)
//    p1.dad = p2
//    println(p1.dad?.name)
//}
