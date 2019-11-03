package optimisation.dichotomy

import kotlin.math.abs
import kotlin.math.exp

private const val EPS = 0.00001
private const val INF = 100000.0

fun main() {
    print("Ввод \"a\": ")
    val a = readLine()!!.toInt()
    print("Ввод \"b\": ")
    val b = readLine()!!.toInt()
    dichotomyOptimisation(a,b)
}

fun dichotomyOptimisation(a:Int,b:Int){
    var c = 0.0
    var l = -INF
    var r = INF
    var iterCounter = 0

    println("left_border = $l right_border = $r")

    while(abs(r-l) > EPS){
        c = (r+l)/2
        if(f(a,b,c-(1/10)*EPS)<f(a,b,c+(1/10)*EPS)){
            r = c
        }else{
            l = c
        }

        iterCounter++
        println("left_border = $l right_border = $r")
    }

    println("-----------------------------------------------------------------------------\n" +
            "Число итераций : $iterCounter\n" +
            "Точка минимума : x = $c y =${f(a,b,c)}" )
}

fun f(a:Int,b:Int, x:Double):Double = a / exp(x) + b * x
