//
// Written by Su386 and J10a1n15.
// See LICENSE for copyright and license notices.
//
package me.partlysanestudios.partlysaneskies.utils.vectors

import kotlin.math.max
import kotlin.math.min

class Range3d(x1: Double, y1: Double, z1: Double, x2: Double, y2: Double, z2: Double) {
    constructor(point1: Point3d, point2: Point3d) : this(point1.x, point1.y, point1.z, point2.x, point2.y, point2.z)

    private val smallCoordinate: DoubleArray = DoubleArray(3)
    private val largeCoordinate: DoubleArray = DoubleArray(3)
    var rangeName: String

    init {
        smallCoordinate[0] = min(x1, x2)
        smallCoordinate[1] = min(y1, y2)
        smallCoordinate[2] = min(z1, z2)
        largeCoordinate[0] = max(x1, x2)
        largeCoordinate[1] = max(y1, y2)
        largeCoordinate[2] = max(z1, z2)
        rangeName = ""
    }

    fun isInRange(point3d: Point3d): Boolean {
        return isInRange(point3d.x, point3d.y, point3d.z)
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