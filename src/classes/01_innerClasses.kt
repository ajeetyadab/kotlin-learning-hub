package classes

fun main(){
    var m1 = DataView(mutableListOf("ajeet","rahul","vikash")).Data(1).displayData()

}

class DataView(var lists: MutableList<String>){

    inner class Data(var index:Int){

        fun displayData() {
            println(lists[index])
        }
    }
}