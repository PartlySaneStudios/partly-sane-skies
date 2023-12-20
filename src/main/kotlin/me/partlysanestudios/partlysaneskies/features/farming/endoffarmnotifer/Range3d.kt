//
// Written by Su386 and J10a1n15.
// See LICENSE for copyright and license notices.
//
package me.partlysanestudios.partlysaneskies.features.farming.endoffarmnotifer

import me.partlysanestudios.partlysaneskies.features.farming.endoffarmnotifer.points.Point3d
import kotlin.math.max
import kotlin.math.min

class Range3d(x1: Double, y1: Double, z1: Double, x2: Double, y2: Double, z2: Double) {
    private val smallCoordinate: DoubleArray
    private val largeCoordinate: DoubleArray
    var rangeName: String

    init {
        smallCoordinate = DoubleArray(3)
        largeCoordinate = DoubleArray(3)
        smallCoordinate[0] = min(x1, x2)
        smallCoordinate[1] = min(y1, y2)
        smallCoordinate[2] = min(z1, z2)
        largeCoordinate[0] = max(x1, x2)
        largeCoordinate[1] = max(y1, y2)
        largeCoordinate[2] = max(z1, z2)
        rangeName = ""
    }

    fun isInRange(x: Double, y: Double, z: Double): Boolean {
        if (smallCoordinate[0] <= x && x - 1 <= largeCoordinate[0]) {
            if (smallCoordinate[1] - 1 <= y && y - 1 <= largeCoordinate[1]) {
                return smallCoordinate[2] <= z && z - 1 <= largeCoordinate[2]
            }
        }
        return false
    }

    val points: Array<Point3d>
        get() = arrayOf(
            Point3d(
                smallCoordinate[0], smallCoordinate[1], smallCoordinate[2]
            ), Point3d(
                largeCoordinate[0],
                largeCoordinate[1], largeCoordinate[2]
            )
        )

    override fun toString(): String {
        return "§7" + rangeName + " §b(" + smallCoordinate[0] + ", " + smallCoordinate[1] + ", " + smallCoordinate[2] + ")§7 to §b(" + largeCoordinate[0] + ", " + largeCoordinate[1] + ", " + largeCoordinate[2] + ")"
    }

    //POINT 2D AND 3D


    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val range3d = o as Range3d
        return smallCoordinate.contentEquals(range3d.smallCoordinate) && largeCoordinate.contentEquals(range3d.largeCoordinate)
    }

    override fun hashCode(): Int {
        var result = smallCoordinate.contentHashCode()
        result = 31 * result + largeCoordinate.contentHashCode()
        return result
    }
}