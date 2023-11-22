//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.utils

import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.garden.endoffarmnotifer.points.Point2d
import me.partlysanestudios.partlysaneskies.garden.endoffarmnotifer.points.Point3d
import java.util.concurrent.ThreadLocalRandom
import kotlin.math.pow
import kotlin.math.sqrt

object MathUtils {
    fun Double.round(decimalPlaces: Int): Double {
        return Math.round(this * 10.0.pow(decimalPlaces.toDouble()) / 10.0.pow(decimalPlaces.toDouble())).toDouble()
    }

    fun randInt(min: Int, max: Int): Int {
        return ThreadLocalRandom.current().nextInt(min, max + 1)
    }


    // Takes the last time the event happened in Unix epoch time in milliseconds,
    // and takes the length that the event should last in millisecond
    // Returns false if the event is over, returns true if it is still ongoing
    fun onCooldown(lastTime: Long, length: Long): Boolean {
        return PartlySaneSkies.getTime() <= lastTime + length
    }

    fun getDistance2d(point1: Point2d, point2: Point2d): Float {
        return sqrt(
            (point2.getPointX() - point1.getPointX()).pow(2.0) + (point2.getPointY() - point1.getPointY()).pow(
                2.0
            )
        )
            .toFloat()
    }

    fun getDistance3d(point1: Point3d, point2: Point3d): Float {
        return sqrt(
            (point2.getPointX() - point1.getPointX()).pow(2.0) + (point2.getPointY() - point1.getPointY()).pow(
                2.0
            ) + (point2.getPointZ() - point1.getPointZ()).pow(2.0)
        )
            .toFloat()
    }
}
