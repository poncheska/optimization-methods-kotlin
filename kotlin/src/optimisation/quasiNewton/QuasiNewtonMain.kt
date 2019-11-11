package optimisation.quasiNewton

import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt


private const val EPS = 0.000001
private const val INF = 100000.0

fun main() {
    print("Введите тип функции (1 - функция Розенброка, 2 - функция Химмельблау): ")
    val type = readLine()!!.toInt()

    QuasiNewtonMain().optimise(type)
}

class QuasiNewtonMain {
    fun optimise(type: Int) {

        var iterCounter = 1
        var alpha: Double

        var v0 = Vertex(-4.0, 10.0, type)
        var v1: Vertex

        var p1: Double
        var p2: Double

        var y1: Double
        var y2: Double

        var H0 = arrayListOf(arrayListOf(1.0, 0.0), arrayListOf(0.0, 1.0))
        var H1: ArrayList<ArrayList<Double>>

        println("V(0): ${v0.getString()}")

        p1 = -(v0.fdx1() * H0[0][0] + v0.fdx2() * H0[0][1])
        p2 = -(v0.fdx1() * H0[1][0] + v0.fdx2() * H0[1][1])

        alpha = v0.dichotomy(p1, p2)
        v1 = v0.getNextVertex(alpha, p1, p2)

        y1 = v1.fdx1() - v0.fdx1()
        y2 = v1.fdx2() - v0.fdx2()

        var z1 = v1.x1 - v0.x1
        var z2 = v1.x2 - v0.x2

        H1 = getNewH(H0,p1,p2,y1,y2,alpha,z1,z2)


        while (Vertex.distance(v0, v1) > EPS) {
            println("V($iterCounter): ${v1.getString()}    alpha = $alpha")
            println(H0)
            H0 = H1
            v0 = v1
            p1 = -(v0.fdx1() * H0[0][0] + v0.fdx2() * H0[0][1])
            p2 = -(v0.fdx1() * H0[1][0] + v0.fdx2() * H0[1][1])

            alpha = v0.dichotomy(p1, p2)
            v1 = v0.getNextVertex(alpha, p1, p2)

            z1 = v1.x1 - v0.x1
            z2 = v1.x2 - v0.x2

            H1 = getNewH(H0, p1, p2, y1, y2, alpha, z1, z2)

            y1 = v1.fdx1() - v0.fdx1()
            y2 = v1.fdx2() - v0.fdx2()

            iterCounter++
        }

        println("-----------------------------------------------------------------------------\n" +
                "Minimum ${v1.getString()}\n" +
                "Количество итераций = $iterCounter")
    }

    fun getNewH(h0: ArrayList<ArrayList<Double>>, p1: Double, p2: Double,
                y1: Double, y2: Double, alpha: Double, z1: Double, z2: Double): ArrayList<ArrayList<Double>> {


        val divider = z1*y1 + z2*y2

        val a00 = 1 - z1*y1/divider
        val a01 = 1 - z1*y2/divider
        val a10 = 1 - z2*y1/divider
        val a11 = 1 - z2*y2/divider

        val b00 =1 - y1*z1/divider
        val b01 =1 - y1*z2/divider
        val b10 =1 - y2*z1/divider
        val b11 =1 - y2*z2/divider

        val c00 = a00*h0[0][0] + a01*h0[1][0]
        val c01 = a00*h0[0][1] + a01*h0[1][1]
        val c10 = a10*h0[0][0] + a11*h0[1][0]
        val c11 = a10*h0[0][1] + a11*h0[1][1]

        val h00 = c00*b00 + c01*b10 + z1.pow(2)/divider
        val h01 = c00*b01 + c01*b11 + z1*z2/divider
        val h10 = c10*b00 + c11*b10 + z1*z2/divider
        val h11 = c10*b01 + c11*b11 + z2.pow(2)/divider

//        var divider = (alpha*p1 -(y1 * h0[0][0] + y2 * h0[0][1])) * y1 +
//                (alpha*p2 -(y1 * h0[1][0] + y2 * h0[1][1])) * y2

//        val h00 = h0[0][0] - (alpha*p1 -(y1 * h0[0][0] + y2 * h0[0][1])).pow(2)/divider
//        val h01 = h0[0][1] - (alpha*p1 -(y1 * h0[0][0] + y2 * h0[0][1]))*(alpha*p2 -(y1 * h0[1][0] + y2 * h0[1][1]))/divider
//        val h10 = h0[1][0] - (alpha*p1 -(y1 * h0[0][0] + y2 * h0[0][1]))*(alpha*p2 -(y1 * h0[1][0] + y2 * h0[1][1]))/divider
//        val h11 = h0[1][1] - (alpha*p2 -(y1 * h0[1][0] + y2 * h0[1][1])).pow(2)/divider

        return arrayListOf(arrayListOf(h00,h01), arrayListOf(h10,h11))
    }


    class Vertex(var x1: Double, var x2: Double, val type: Int) {

        fun getString(): String = " x=$x1 y=$x2 "

        fun f(): Double = when (type) {
            1 -> 100 * (x2 - x1.pow(2)).pow(2) + 5 * (1 - x1).pow(2)
            2 -> (x1.pow(2) + x2 - 11).pow(2) + (x1 + x2.pow(2) - 7).pow(2)
            else -> 0.0
        }

        fun fdx1(): Double = when (type) {
            1 -> 400 * x1.pow(3) - 400 * x1 * x2 + 10 * x1 - 10
            2 -> 4 * x1 * (x1.pow(2) + x2 - 11) + 2 * (x1 + x2.pow(2) - 7)
            else -> 0.0
        }

        fun fdx2(): Double = when (type) {
            1 -> 200 * x2 - 200 * x1.pow(2)
            2 -> 2 * (x1.pow(2) + x2 - 11) + 4 * x2 * (x1 + x2.pow(2) - 7)
            else -> 0.0
        }

        fun dichotomy(p1: Double, p2: Double): Double {
            var l = -INF
            var r = INF
            var cl: Double
            var cr: Double


            while (abs(r - l) > EPS) {
                cl = (r + l) * 0.5 - 0.1 * EPS
                cr = (r + l) * 0.5 + 0.1 * EPS
                if (Vertex(x1 + cl * p1, x2 + cl * p2, type).f() <=
                        Vertex(x1 + cr * p1, x2 + cr * p2, type).f()) {
                    r = cr
                } else {
                    l = cl
                }
            }

            return (r + l) / 2

        }

        fun getNextVertex(alpha: Double, p1: Double, p2: Double): Vertex =
                Vertex(x1 + alpha * p1, x2 + alpha * p2, type)

        companion object {
            fun distance(v1: Vertex, v2: Vertex): Double = sqrt((v1.x1 - v2.x1).pow(2) + (v1.x2 - v2.x2).pow(2))
        }
    }




}
