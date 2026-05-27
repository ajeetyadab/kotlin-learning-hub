package classes



fun main(){

    // first
    Direction.NORTH
    Direction.SOUTH
    Direction.EAST
    Direction.WEST

// second
println(Direction2.NORTH.distance)

// for loop in enum classes


    for (dir in Direction2.entries){
        println(dir.distance)
    }

    Direction2.NORTH.printData()



// using when with enum classes

//    var direction2 = Direction.SOUTH
    var direction = Direction2.valueOf("EAST")

    when(direction){
        Direction2.NORTH -> println("direction is toward north")
        Direction2.SOUTH -> println("direction is toward south")
        Direction2.EAST -> println("direction is toward east")
        Direction2.WEST -> println("direction is toward west")

    }
}

enum class Direction {
    NORTH,
    SOUTH,
    EAST,
    WEST




}

// passing argument in enum classes

enum class Direction2(var direction:String,var distance:Int){
    NORTH("north",10),
    SOUTH("south",20),
    EAST("east",50),
    WEST("west",30); // if you are going to implement a method
                                            // then put semicolon after the last enum object i.e. WEST

    fun printData(){
        println(" Direction :$direction, distance:$distance")
    }


}