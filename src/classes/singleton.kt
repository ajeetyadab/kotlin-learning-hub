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