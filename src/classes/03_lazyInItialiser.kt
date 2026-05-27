package classes


fun main(){
val u1 = UserInfo("ajeet","lekhpal",38)

    val u2 by lazy {
        // Type 'Lazy<UserInfo>' has no method 'setValue(Nothing?, KMutableProperty0<*>, UserInfo)',
        // so it cannot serve as a delegate for var (read-write property). explain this
        // remember this use only val while using lazy

        UserInfo("Tarun Gupta","Lekhpal",38)
    }
}

class UserInfo(var name:String,var profession:String,var age: Int){

    init {
        println(" A user $name :$profession  age $age is created")
    }
}