package classes


fun main(){
var s1 = Result.Success("success")
    var e1 = Result.Error.RecoverableError("recoverable error")

getData(s1)
    getData(e1)
}

fun getData(result:Result){
    when(result){
        is Result.Success ->println(result.message)
        is Result.Loading ->println(result.message)
        is Result.Error.UnRecoverableError ->println(result.message)
        is Result.Error.RecoverableError ->println(result.message)
    }

}

sealed class Result(val message:String){



    class Success(message:String):Result(message)
    class Loading(message:String):Result(message)
    sealed class Error(message:String):Result(message){
        class RecoverableError(message:String): Error(message)
        class UnRecoverableError(message:String): Error(message)
    }



}