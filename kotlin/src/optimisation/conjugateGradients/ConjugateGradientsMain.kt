package optimisation.conjugateGradients

import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

private const val EPS = 0.000001
private const val INF = 100000.0

fun main() {
    print("Введите тип функции (1 - функция Розенброка, 2 - функция Химмельблау): ")
    val type = readLine()!!.toInt()

    ConjugateGradientsMain().optimise(type)
}

class ConjugateGradientsMain {
    fun optimise(type: Int) {

        var iterCounter = 0
        var alpha: Double
        var beta: Double

        var v2 = Vertex( -10.0,-5.0, type)
        println("V(0): ${v2.getString()}")
        var v1: Vertex


        var gradientSquare = v2.gradientSquare()
        var newGradientSquare: Double

        while(gradientSquare > EPS){
            iterCounter++
            alpha = v2.dichotomy()
            v1 = v2
            v2 = v2.getNextVertex(alpha)

            newGradientSquare = v2.gradientSquare()

            beta = if(iterCounter % 10 ==0) 0.0 else newGradientSquare/gradientSquare

            v2.d1= v2.fdx1() + beta * v1.fdx1()
            v2.d2= v2.fdx2() + beta * v1.fdx2()

            gradientSquare = newGradientSquare
            println("V($iterCounter): ${v1.getString()}    alpha = $alpha  beta = $beta")

        }

        println("-----------------------------------------------------------------------------\n" +
                "Minimum ${v2.getString()}\n" +
                "Количество итераций = $iterCounter" )

    }

    class Vertex(var x1: Double, var x2: Double, val type: Int) {
        var d1:Double? = null
        var d2:Double? = null

        fun getString(): String = " x=$x1 y=$x2 "

        fun f(): Double = when (type) {
            1 -> 100 * (x2 - x1.pow(2)).pow(2) + 5 * (1 - x1).pow(2)
            2 -> (x1.pow(2) + x2 - 11).pow(2) + (x1 + x2.pow(2) - 7).pow(2)
            else -> 0.0
        }

        //  в данном методе производные берутся с минусом(т.к. составляющие антиградиента)
        fun fdx1(): Double = d1 ?: (-1)*when (type) {
            1 -> 400 * x1.pow(3)  - 400 * x1 * x2 + 10 * x1 - 10
            2 -> 4 * x1 * (x1.pow(2) + x2 - 11) + 2*(x1 + x2.pow(2) - 7)
            else -> 0.0
        }

        fun fdx2(): Double = d2 ?: (-1)*when (type) {
            1 -> 200 * x2 - 200 * x1.pow(2)
            2 -> 2 * (x1.pow(2) + x2 - 11) + 4 * x2 * (x1 + x2.pow(2) - 7)
            else -> 0.0
        }

        fun dichotomy():Double{

            var l = -INF
            var r = INF
            var cl: Double
            var cr: Double


            while(abs(r-l) > EPS){
                cl = (r+l) * 0.5 - 0.1 * EPS
                cr = (r+l) * 0.5 + 0.1 * EPS
                if(Vertex(x1 + cl * fdx1(),x2 + cl * fdx2(), type).f() <=
                        Vertex(x1 + cr * fdx1(),x2 + cr * fdx2(), type).f()){
                    r = cr
                }else{
                    l = cl
                }
            }

            return (r+l)/2
        }

        fun getNextVertex(alpha:Double):Vertex = Vertex(x1 + alpha*fdx1(),x2 + alpha*fdx2(),type)

        fun gradientSquare():Double = fdx1().pow(2) + fdx2().pow(2)

    }
}