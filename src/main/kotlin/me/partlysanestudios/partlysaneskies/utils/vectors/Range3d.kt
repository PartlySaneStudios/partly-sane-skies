//
// Written by Su386 and J10a1n15.
// See LICENSE for copyright and license notices.
//
package me.partlysanestudios.partlysaneskies.utils.vectors

import kotlin.math.max
import kotlin.math.min

class Range3d(private val point1: Point3d, private val point2: Point3d) {
    constructor(x1: Double, y1: Double, z1: Double, x2: Double, y2: Double, z2: Double): this (
        Point3d(x1, y1, z1),
        Point3d(x2, y2, z2)
    )

    var rangeName: String = ""

    private val smallCoordinate = Point3d(
        min(point1.x, point2.x),
        min(point1.y, point1.y),
        min(point1.z, point2.z)
    )
    private val largeCoordinate = Point3d(
        max(point1.x, point2.x),
        max(point1.y, point2.y),
        max(point1.z, point2.z)
    )

    fun isInRange(point3d: Point3d): Boolean {
        return isInRange(point3d.x, point3d.y, point3d.z)
    }
    fun isInRange(x: Double, y: Double, z: Double): Boolean {
        if (smallCoordinate.x <= x && x - 1 <= largeCoordinate.x) {
            if (smallCoordinate.y - 1 <= y && y - 1 <= largeCoordinate.y) {
                return smallCoordinate.z <= z && z - 1 <= largeCoordinate.z
            }
        }
        return false
    }

    val sortedPoints: Array<Point3d>
        get() {
            return arrayOf(
                Point3d(smallCoordinate.x, smallCoordinate.y, smallCoordinate.z),
                Point3d(largeCoordinate.x, largeCoordinate.y, largeCoordinate.z)
            )
        }


    val points: Array<Point3d>
        get() {
            return arrayOf(Point3d(point1.x, point1.y, point1.z), Point3d(point2.x, point2.y, point2.z))
        }
    //POINT 2D AND 3D


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val range3d = other as Range3d
        return smallCoordinate == range3d.smallCoordinate && largeCoordinate == range3d.largeCoordinate
    }

    override fun hashCode(): Int {
        var result = smallCoordinate.hashCode()
        result = 31 * result + largeCoordinate.hashCode()
        return result
    }

    override fun toString(): String {
        return "Range3d(smallCoordinate=$smallCoordinate, largeCoordinate=$largeCoordinate, rangeName='$rangeName')"
    }
}