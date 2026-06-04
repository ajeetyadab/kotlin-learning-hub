package collections

fun main(){
    data class User(
        val name:String,
        val age:Int
    )

    val users = listOf(

        User("ajeet",38),
        User("rahul",36),
        User("tarun",30)
    )
    var sb = StringBuffer()

    val myList = mutableListOf<String>()

    for ( user in users){
        myList.add(user.name+"(${user.age})")


    }
    println(myList.joinToString(separator = "|"))

    println(users.joinTo(sb, separator= " | ",transform = {it.name+"(${it.age})"}))

}