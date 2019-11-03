package optimisation.gradient

import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

private const val EPS = 0.000001
private const val INF = 100000.0

fun main() {
    print("Введите тип функции (1 - функция Розенброка, 2 - функция Химмельблау): ")
    val type = readLine()!!.toInt()

    GradientMain().optimise(type)
}

class GradientMain() {

    fun optimise(type: Int) {

        var iterCounter = 0
        var alpha: Double

        var v0 = Vertex( 4.0,4.0, type)
        var v1:Vertex

        alpha = v0.dichotomy()
        v1 = v0.getNextVertex(alpha)


        while(Vertex.distance(v0,v1) > EPS){
            println("V($iterCounter): ${v1.getString()}    alpha = $alpha")
            v0 = v1
            alpha = v0.dichotomy()
            v1 = v0.getNextVertex(alpha)
            iterCounter++
        }

        println("-----------------------------------------------------------------------------\n" +
                "Minimum ${v1.getString()}\n" +
                "Количество итераций = $iterCounter" )
    }


    class Vertex(var x1: Double, var x2: Double, val type: Int) {

        fun getString(): String = " x=$x1 y=$x2 "

        fun f(): Double = when (type) {
            1 -> 100 * (x2 - x1.pow(2)).pow(2) + 5 * (1 - x1).pow(2)
            2 -> (x1.pow(2) + x2 - 11).pow(2) + (x1 + x2.pow(2) - 7).pow(2)
            else -> 0.0
        }

        fun fdx1(): Double = when (type) {
            1 -> 400 * x1.pow(3)  - 400 * x1 * x2 + 10 * x1 - 10
            2 -> 4 * x1 * (x1.pow(2) + x2 - 11) + 2*(x1 + x2.pow(2) - 7)
            else -> 0.0
        }

        fun fdx2(): Double = when (type) {
            1 -> 200 * x2 - 200 * x1.pow(2)
            2 -> 2 * (x1.pow(2) + x2 - 11) + 4 * x2 * (x1 + x2.pow(2) - 7)
            else -> 0.0
        }

        fun dichotomy():Double{

            var l = -INF
            var r = INF
            var cl: Double
            var cr: Double
            var iterCounter = 0


            while(abs(r-l) > EPS){
                cl = (r+l) * 0.5 - 0.1 * EPS
                cr = (r+l) * 0.5 + 0.1 * EPS
                if(Vertex(x1 - cl * Vertex(x1,x2,type).fdx1(),x2 - cl * Vertex(x1,x2,type).fdx2(), type).f() <=
                        Vertex(x1 - cr * Vertex(x1,x2,type).fdx1(),x2 - cr * Vertex(x1,x2,type).fdx2(), type).f()){
                    r = cr
                }else{
                    l = cl
                }
            }

            return (r+l)/2
        }

        fun getNextVertex(alpha:Double):Vertex = Vertex(x1 - alpha*fdx1(),x2 - alpha*fdx2(),type)

        companion object {
            fun distance(v1: Vertex, v2: Vertex): Double = sqrt((v1.x1 - v2.x1).pow(2) + (v1.x2 - v2.x2).pow(2))
        }


    }
}