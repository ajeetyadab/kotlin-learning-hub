package collections

fun main(){

    //list
    val names = mutableListOf<String>("name1","name2","name3")
    names.add("name4")
    println(names)

    names.forEach { println(it) }

    names.remove("name2")
    println(names)


    // set
    println("==========SET============")
    val setNames = mutableSetOf<String>("name1","name2","name3","name2")
    setNames.forEach { println(it) }
    setNames.add("name5")
    println(setNames)

    // class example

    println("---example---")
    val u1 = User("Aj")
    val u2 = User("Aj")
    val u3 = User("Tarun")
    val u4 = User("Santosh")
    val u5 = User("Bhikam")

    var sets: MutableSet<User> = mutableSetOf(u1,u2,u3,u4,u4)
    sets.forEach { println(it.name) }
}


class User(val name:String)

