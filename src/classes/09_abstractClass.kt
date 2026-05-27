package classes

abstract class PaymentMethod {

    fun generateReceipt(amount: Double) {
        println("Generating receipt for $amount")
    }

    abstract fun processPayment(amount: Double): Boolean

}
    class CreditCardPayment : PaymentMethod(){
        override fun processPayment(amount: Double): Boolean {
           println("connecting to stripe API")
            println("chanrging $amount to credit card")
            return true
        }
    }
class UPIPayment: PaymentMethod(){
    override fun processPayment(amount: Double): Boolean {
        println("opening UPI app")
        println("Transfering $amount")
        return true
    }
}

fun main(){
 val myPayment = CreditCardPayment()
    myPayment.processPayment(49.99)
    myPayment.generateReceipt(49.90)
}