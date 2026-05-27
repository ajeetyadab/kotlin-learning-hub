package classes

abstract class AppNotification(val title:String){

    // concrete showBanner()

    fun showBanner(){
        println("showing banner at top of screen: $title")
    }

    abstract fun  handleTapAction()


}

class MessaggeNotification(title:String): AppNotification(title){

    override fun handleTapAction() {
        println("opening chat window")
    }



}
class SystemAlertNotification(title:String): AppNotification(title){
    override fun handleTapAction() {
        println("navigating to app setting")

    }
}
fun main(){
    val notifications = listOf<AppNotification>(
        MessaggeNotification("new message from sakshi"),
        SystemAlertNotification("low battery:10%"),
        MessaggeNotification("stock price triggered take position"))
        MessaggeNotification("new message from sakshi")


    for( i in notifications){
       i.showBanner()
        i.handleTapAction()
        println("--")
    }

}