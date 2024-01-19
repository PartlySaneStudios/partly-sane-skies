//
// Written by Su386 and J10a1n15.
// See LICENSE for copyright and license notices.
//
package me.partlysanestudios.partlysaneskies.utils.vectors

import net.minecraft.util.BlockPos

open class Point3d(x: Double, y: Double, val z: Double) : Point2d(x, y) {
    fun getPointZ(): Double {
        return z
    }

    fun toBlockPos(): BlockPos {
        return BlockPos(x, y, z)
    }

    fun toBlockPosInt(): BlockPos {
        return BlockPos(x.toInt(), y.toInt(), z.toInt())
    }
}