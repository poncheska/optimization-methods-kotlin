package optimisation.goldenRatio

import kotlin.math.abs
import kotlin.math.exp
import kotlin.math.sqrt

private const val EPS = 0.00001
private const val INF = 10000.0
private var GOLDEN_RATIO = 0.5 * (1.0 + sqrt(5.0))

fun main() {
    print("Ввод \"a\": ")
    val a = readLine()!!.toInt()
    print("Ввод \"b\": ")
    val b = readLine()!!.toInt()
    goldenRatioOptimisation(a,b)
}

fun goldenRatioOptimisation(a:Int,b:Int){
    var l = -INF
    var r = INF
    var x1:Double
    var x2:Double
    var t:Double
    var iterCounter = 0
    println("left_border = $l right_border = $r")

    while(abs(r-l) > EPS){
        t = (r-l)/ GOLDEN_RATIO
        x1 = r - t
        x2 = l + t

        if(f(a,b,x1)>=f(a,b,x2)){
            l = x1
        }else{
            r = x2
        }

        iterCounter++
        println("left_border = $l right_border = $r")
    }

    println("-----------------------------------------------------------------------------\n" +
            "Число итераций : $iterCounter\n" +
            "Точка минимума : x = ${(l+r)/2} y =${f(a,b,(l+r)/2)}" )
}

fun f(a:Int,b:Int, x:Double):Double = a / exp(x) + b * x
