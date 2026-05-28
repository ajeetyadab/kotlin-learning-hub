package classes



interface Printer{
    fun print()
    fun scan()
}




class SimplePrinter: Printer{

    override fun print() {
        println("simplly printing")
    }

    override fun scan() {
        println("simplly scanning")
    }

}



// delegation old way



class AmazingPrinter: Printer{
    val smprint = SimplePrinter()

    override fun print() {
       smprint.print()
    }

    override fun scan() {
        println("scan by amazing scanner")
    }

}



// modern way of delegation

class FancyPrint(private val simplePrint: SimplePrinter = SimplePrinter()): Printer by simplePrint{
    override fun print() {
        println("printed by Fancy printer")
    }

}



// exercise implement delegation for this

interface SoundSystem {
    fun playMusic()
    fun adjustVolume()
    fun connectBluetooth()
    fun showEqualizer()
    fun setTreble()
    fun setBass()
    fun setBalance()
    fun enableNightMode()
    // ... 20 और functions
}
class BasicSound: SoundSystem{
    override fun adjustVolume() {
        println("volume adjusted")
    }

    override fun connectBluetooth() {
        println("connected to bluetooth")
    }

    override fun enableNightMode() {
        println("nightr mode enabled")
    }

    override fun playMusic() {
        println("playing music")
    }

    override fun setBalance() {
        println("balance set")
    }

    override fun showEqualizer() {
        println("showing equalizer")
    }

    override fun setTreble() {
        println("set Treble")
    }

    override fun setBass() {
        println("setting Bass Bass")
    }
}

class DelegatedSound(private val basicSound: BasicSound = BasicSound()): SoundSystem by basicSound{

    override fun adjustVolume() {
        println("sound generating from delegated class")
    }


}


fun main(){
    val p1 = AmazingPrinter()
    p1.print()
    p1.scan()

    val p2 = FancyPrint()
    p2.print()
    p2.scan()


    // exercise
    println("=======exercise==========")
    var d1 = DelegatedSound()
    d1.adjustVolume()
    d1.setBass()


}