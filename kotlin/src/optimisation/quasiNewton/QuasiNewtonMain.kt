package optimisation.quasiNewton

import kotlin.math.abs
import kotlin.math.pow

private const val EPS = 0.000001

fun main() {
    print("Введите тип функции (1 - функция Розенброка, 2 - функция Химмельблау): ")
    val type = readLine()!!.toInt()

    QuasiNewtonMain().optimise(type)
}

class QuasiNewtonMain {
    fun optimise(type: Int) {
        var iterCounter = 0

        var v0 = Vertex( 1.1,1.1, type)
        println("V(0): ${v0.getString()}")

        while((v0.fdx1().pow(2) + v0.fdx2().pow(2)) > EPS){
            iterCounter++
            v0 = v0.getNextVertex()
            println("V($iterCounter): ${v0.getString()}   dx1=${v0.fdx1()} dx2=${v0.fdx2()}" )
        }
        println("-----------------------------------------------------------------------------\n" +
                "Minimum ${v0.getString()}\n" +
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

        fun fd2x1(): Double = when (type) {
            1 -> 1200 * x1.pow(2)  - 400 * x2 + 10
            2 -> 4 *(x1.pow(2) + x2 - 11) + 8 * x1.pow(2)  + 2
            else -> 0.0
        }

        fun fd2x2(): Double = when (type) {
            1 -> 200 * x2 - 200 * x1.pow(2)
            2 -> 2 + 4 * (x1 + x2.pow(2) - 7) + 8* x2.pow(2)
            else -> 0.0
        }

        fun fdx1dx2(): Double = when (type) {
            1 -> - 400 * x1
            2 -> 4 * x1 + 4 * x2
            else -> 0.0
        }

        fun getNextVertex():Vertex{
            val inverseGesse11 = fd2x2()/(fd2x1()*fd2x2()-fdx1dx2().pow(2))
            val inverseGesse12 = fdx1dx2()/(-fd2x1()*fd2x2()+fdx1dx2().pow(2))
            val inverseGesse22 = fd2x1()/(fd2x1()*fd2x2()-fdx1dx2().pow(2))
            val newX1 = x1 - (inverseGesse11*fdx1() + inverseGesse12*fdx2())
            val newX2 = x2 - (inverseGesse12*fdx1() + inverseGesse22*fdx2())
            return Vertex(newX1,newX2,type)
        }

    }
}