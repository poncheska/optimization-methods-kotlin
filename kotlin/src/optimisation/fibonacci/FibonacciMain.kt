package optimisation.fibonacci

import kotlin.math.abs
import kotlin.math.exp

private const val INF = 10000.0
private const val EPS = 0.0000001

fun main() {
    print("Ввод \"a\": ")
    val a = readLine()!!.toInt()
    print("Ввод \"b\": ")
    val b = readLine()!!.toInt()
    print("Ввод количества итераций(не более 50): ")
    val n = readLine()!!.toInt()
    fibonacciOptimisation(a,b,n)
}

fun fibonacciOptimisation(a:Int,b:Int,_n: Int){
    var l = -INF
    var r = INF
    var iterCounter = 0
    var n = _n
    var x1:Double = l + (r-l)* fibonacciNum(n-2)/ fibonacciNum(n)
    var x2:Double = l + (r-l)* fibonacciNum(n-1)/ fibonacciNum(n)

    println("left_border = $l right_border = $r")

    while(n!=0 && abs(r-l) > EPS){

        if(f(a,b,x1)>=f(a,b,x2)){
            l = x1
            x1 = x2
            x2 = r - (x1 - l)
        }else{
            r = x2
            x2 = x1
            x1 = l + (r - x2)
        }

        n--
        iterCounter++
        println("left_border = $l right_border = $r")
    }

    println("-----------------------------------------------------------------------------\n" +
            "Число итераций : $iterCounter\n" +
            "Точка минимума : x = ${(l+r)/2} y =${f(a,b,(l+r)/2)}" )
}

fun f(a:Int,b:Int, x:Double):Double = a / exp(x) + b * x

fun fibonacciNum(n:Int):Double{
    if(n in 1..2){
        return 1.0
    }
    var f1 = 1L
    var f2 = 1L
    var c:Long
    var num = n

    while(num-2 != 0){
        c = f1 + f2
        f1 = f2
        f2 = c
        num--
    }

    return f2.toDouble()
}

