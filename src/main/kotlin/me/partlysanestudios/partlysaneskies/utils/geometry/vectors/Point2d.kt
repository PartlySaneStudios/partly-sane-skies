//
// Written by Su386 and J10a1n15.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.utils.geometry.vectors

import kotlin.math.pow
import kotlin.math.sqrt

open class Point2d(val x: Double, val y: Double) {
    fun getPointX(): Double {
        return x
    }

    fun getPointY(): Double {
        return y
    }

    fun distanceTo(point2: Point2d): Double {
        return sqrt(
            (point2.getPointX() - this.getPointX()).pow(2.0) +
                    (point2.getPointY() - this.getPointY()).pow(2.0)
        )
    }

    override fun toString(): String {
        return "Point2d(x=$x, y=$y)"
    }


    operator fun plus(point: Point2d): Point2d {
        return Point2d(point.x + this.x, point.y + this.y)
    }

    operator fun minus(point: Point2d): Point2d {
        return Point2d(point.x - this.x, point.y - this.y)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Point2d

        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        return result
    }
}