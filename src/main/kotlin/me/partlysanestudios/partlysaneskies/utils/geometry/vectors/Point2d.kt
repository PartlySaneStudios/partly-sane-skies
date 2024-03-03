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
}