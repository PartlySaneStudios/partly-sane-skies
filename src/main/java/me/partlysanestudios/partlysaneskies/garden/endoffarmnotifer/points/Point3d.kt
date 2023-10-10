package me.partlysanestudios.partlysaneskies.garden.endoffarmnotifer.points

import me.partlysanestudios.partlysaneskies.garden.endoffarmnotifer.points.Point2d

open class Point3d(x: Double, y: Double, val z: Double) : Point2d(x, y) {
    fun getPointZ(): Double {
        return z
    }
}