package classes

fun main(){
    var user1 = UserInfomation("aj","Singh",37)
    var user2 = UserInfomation("aj","Singh",37)
    println(user1 == user2)
    println(user1.equals(user2))

    // if we are trying to check structurally these two functions no way we can do this
    // except overriding the equal method

}


class UserInfomation(var firstName:String,var lastName:String,var age:Int):Any(){

    override fun equals(other: Any?): Boolean {
        if(this === other){
            return true
        }
        if(other is UserInfomation){
            if(this.firstName == other.firstName && this.lastName == other.lastName && this.age == other.age){
                return true
            }
        }
        return false
    }
}