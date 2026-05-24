package classes

fun main(){
    var u1 = User("Aj","yadav",37)

    u1.favoriteMovie = "intersteller" // if we don't initialize property here complier will throw error
                                        // late init property exception is better than null exception
    println(u1.favoriteMovie)
}


class User(var firstname:String,var lastName:String,var age:Int){

    lateinit var favoriteMovie:String
    // late init won't work with primitive data type

}