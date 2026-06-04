package collections


val myList = mutableListOf<Int>(8,12,15,17,19,11,15,25)


// sorting with price
//
//data class Laptop(val brand:String,val color:String,val year:Int,val price:Double): Comparable<Laptop>{
//    override fun compareTo(other: Laptop): Int {
//        return if(this.price>other.price){
//            1
//        }else if(this.price<other.price){
//            return -1
//        } else{
//            0
//        }
//    }
//
//}


////-------- sorting with year
//data class Laptop(val brand:String,val color:String,val year:Int,val price:Double): Comparable<Laptop>{
//    override fun compareTo(other: Laptop): Int {
//        return if(this.year>other.year){
//            return 1
//        } else if(this.year<other.year){
//            return -1
//        }else{
//            0
//        }
//    }
//}

// sorting alphabatically

data class Laptop(val brand:String,val color:String,val year:Int, val price:Double): Comparable<Laptop>{
    override fun compareTo(other: Laptop): Int {

        return if(this.brand>other.brand){
            return 1
        } else if(this.brand<other.brand){
            return -1
        }else{
            0
        }
    }
}


fun main(){
//    println(myList.sorted().forEach { println(it) })

    // sorting a class

    val laptop1 = Laptop("Dell", "Black", 2022, 65000.0)
    val laptop2 = Laptop("HP", "Silver", 2021, 58000.0)
    val laptop3 = Laptop("Lenovo", "Gray", 2022, 72000.0)
    val laptop4 = Laptop("Apple", "Space Gray", 2022, 145000.0)
    val laptop5 = Laptop("Asus", "White", 2021, 81000.0)

    val laptops =mutableListOf<Laptop>(laptop1,laptop2,laptop3,laptop4,laptop5)

//    println(laptops.sorted())
//    println("hp".compareTo("dell"))
//    println(laptops.sortedBy(selector = {it.brand}))
    println(laptops.sortedWith(compareBy<Laptop>{it.year}.thenBy{it.price}).forEach { println(it) })






}


//fun main(){
//    data class Person(val name:String,val age:Int)
//    val person1:Person = Person("aj",38)
//    val person2:Person = Person("rahul",32)
//    val person3:Person = Person("vicky",33)
//
//    val people = mutableListOf<Person>(person1,person2,person3)
//
//    val byAge = Comparator<Person>{p1,p2 -> p1.age - p2.age}
//    val sorted = people.sortedWith(byAge)
//    println(people.sortedWith { person1, person2 -> person1.age - person2.age })
//    println(sorted)
//}