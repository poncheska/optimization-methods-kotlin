package optimisation.simplex

import kotlin.math.pow
import kotlin.math.sqrt

private const val EPS = 0.00000001

fun main() {
    print("Введите тип функции (1 - функция Розенброка, 2 - функция Химмельблау): ")
    val type = readLine()!!.toInt()

    SimplexMain().optimise(type)
}

class SimplexMain {

    fun optimise(type: Int) {

        var iterCounter = 0

        // изначальные вершины
        val v1 = Vertex(10.0, 9.0, type)
        val v2 = Vertex(10.0, -2.0, type)
        val v3 = Vertex(21.0, 1.0, type)

        var vCentral: Vertex
        var vReflected: Vertex
        var vExpanded: Vertex
        var vCompressed: Vertex
        var vBuffer: Vertex

        // массив в котором вершины упорядочены по возрастанию значения функции
        var vertexArray: ArrayList<Vertex> = arrayListOf(v1, v2, v3)

        while (Vertex.getRadius(vertexArray[0], vertexArray[1], vertexArray[2]) > EPS) {
            iterCounter++

            vertexArray.sortBy { it.f() }

            vCentral = Vertex.center(vertexArray[0], vertexArray[1], type)

            vReflected = Vertex.reflection(vCentral, vertexArray[2], type)

            if (vReflected.f() < vertexArray[0].f()) {
                vExpanded = Vertex.expand(vCentral, vReflected, type)
                if (vExpanded.f() < vReflected.f()) {
                    vertexArray[2] = vExpanded
                } else {
                    vertexArray[2] = vReflected
                }
            } else if (vertexArray[0].f() < vReflected.f() && vReflected.f() < vertexArray[1].f()) {
                vertexArray[2] = vReflected
            } else if ((vertexArray[1].f() < vReflected.f() && vReflected.f() < vertexArray[2].f())
                    || (vertexArray[2].f() < vReflected.f())) {
                if (vertexArray[1].f() < vReflected.f() && vReflected.f() < vertexArray[2].f()) {
                    vBuffer = vReflected
                    vReflected = vertexArray[2]
                    vertexArray[2] = vBuffer
                }

                vCompressed = Vertex.compression(vertexArray[2], vCentral, type)

                if (vCompressed.f() < vertexArray[2].f()) {
                    vertexArray[2] = vCompressed
                } else {
                    vertexArray[1] = Vertex.globalCompression(vertexArray[0], vertexArray[1], type)
                    vertexArray[2] = Vertex.globalCompression(vertexArray[0], vertexArray[2], type)
                }
            }

            println("V1: ${vertexArray[0].getString()} V2: ${vertexArray[1].getString()} V3: ${vertexArray[2].getString()}")
        }

        println("-----------------------------------------------------------------------------\n" +
                "Minimum ${Vertex.globalCenter(vertexArray[0],vertexArray[1],vertexArray[2],type).getString()}\n" +
                "Количество итераций = $iterCounter" )

    }

    class Vertex(var x1: Double, var x2: Double, val type: Int) {

        fun getString():String = " x=$x1 y=$x2 "

        fun f(): Double = when (type) {
            1 -> 100 * (x2 - x1.pow(2)).pow(2) + 5 * (1 - x1).pow(2)
            2 -> (x1.pow(2) + x2 - 11).pow(2) + (x1 + x2.pow(2) - 7).pow(2)
            else -> 0.0
        }

        companion object {
            val alpha = 1.0
            val beta = 0.5
            val gamma = 2.0

            fun center(v1: Vertex, v2: Vertex, type: Int): Vertex = Vertex((v1.x1 + v2.x1) / 2, (v1.x2 + v2.x2) / 2, type)

            fun reflection(vCentral: Vertex, vMax: Vertex, type: Int): Vertex =
                    Vertex((1 + alpha) * vCentral.x1 - alpha * vMax.x1, (1 + alpha) * vCentral.x2 - alpha * vMax.x2, type)

            fun expand(vCentral: Vertex, vReflected: Vertex, type: Int): Vertex =
                    Vertex((1 - gamma) * vCentral.x1 + gamma * vReflected.x1, (1 - gamma) * vCentral.x2 + gamma * vReflected.x2, type)

            fun compression(vMax: Vertex, vCentral: Vertex, type: Int): Vertex =
                    Vertex(beta * vMax.x1 + (1 - beta) * vCentral.x1, beta * vMax.x2 + (1 - beta) * vCentral.x2, type)

            fun globalCompression(vMin: Vertex, v: Vertex, type: Int): Vertex =
                    Vertex(vMin.x1 + (v.x1 - vMin.x1) / 2, vMin.x2 + (v.x2 - vMin.x2) / 2, type)

            fun distance(v1: Vertex, v2: Vertex): Double = sqrt((v1.x1 - v2.x1).pow(2) + (v1.x2 - v2.x2).pow(2))

            fun globalCenter(v1: Vertex, v2: Vertex, v3: Vertex,type: Int) =
                    Vertex((v1.x1 + v2.x1 + v3.x1) / 3, (v1.x2 + v2.x2 + v3.x2) / 3, type)

            fun getRadius(v1: Vertex, v2: Vertex, v3: Vertex): Double {
                val vCenter = Vertex.globalCenter(v1,v2,v3,0)
                return arrayListOf(distance(v1, vCenter), distance(v2, vCenter), distance(v3, vCenter)).max()!!
            }
        }

    }

}