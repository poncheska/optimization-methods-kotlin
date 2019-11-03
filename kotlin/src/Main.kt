import kotlin.math.max
import java.util.ArrayList

// sandbox

fun main(args: Array<String>) {
    val str: String = "BOB"
    println("H $str W ${sum(1,2)}")
    check(1,0)
    println(calculateEvenDigits("534534"))
    var ss:SugarStorage = SugarStorage(100)
    println(ss.volume)
    ss.decreaseSugar(1000)
    println(ss.volume)
    ss.volume = -532
    println(ss.volume)



}

fun sum(a:Int = 0, b:Int =0):Int{
    return a+b
}


fun check(a:Int,b:Int){
    when{
        a==1 && b==0 -> println("01")
        a==0 && b==1 -> println("10")
        else -> println("nope")
    }
}

fun calculateEvenDigits(input:String): Int{
    var sum:Int = 0

    for(i in input){
        if(i.toString().toInt() % 2 == 0)
            sum += i.toString().toInt()
    }

    return sum
}

class SugarStorage(volume:Int){
    var volume:Int = 0
    set(v:Int){
        if(v>=0) {
            field = v
        }else{
            volume = 0
        }
    }
    init{
        this.volume = volume
    }

    fun decreaseSugar(v:Int){
        if(v<0)
            return
        volume -= v
    }
    fun increaseSugar(v:Int){
        if(v<0)
            return
        volume += v
    }

}

fun calculateWordStat(input:String): String{
    var list:ArrayList<String> = ArrayList(input.split(" "))
    var str = ""
    var count = 0
    var maxStr = ""
    var max = 0
    while(list.size != 0){
        var delList:ArrayList<Int> = ArrayList()
        str = list[0]
        for(i in 0 until list.size){
            if(list[i]==str){
                count += 1
                delList.add(i)
            }
        }

        for(i in delList){
            list.removeAt(i)
        }

        if(max<count){
            max=count
            maxStr = str
        }
    }
    return maxStr
}

