package classes


fun main(){

    var b1 = BankAccount("aj",1000.0)
    b1.deposit(100.0)
    b1.deposit(50.0)
    b1.deposit(150.0)
    b1.withdraw(25.0)
    println(b1.showbalance)
    for(entry in b1.transactions){
        println(entry)
    }



}
class BankAccount(val name:String, balance:Double,val transactions: MutableList<String> = mutableListOf()){
    private var bal = balance

    fun deposit(amount:Double){
        if(amount>0){
            bal += amount
            transactions.add("added $amount")
        }else{
            return println("deposit amount cant be less than zero")
        }
    }

    fun withdraw(amount: Double){
        if(amount>0 && amount<=bal){
            bal -=amount
            transactions.add("withdrew $amount")
        }
    }
    val showbalance: Double
        get() {
            return bal
        }


}