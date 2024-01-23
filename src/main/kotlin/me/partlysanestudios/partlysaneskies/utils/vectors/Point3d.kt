//
// Written by Su386 and J10a1n15.
// See LICENSE for copyright and license notices.
//
package me.partlysanestudios.partlysaneskies.utils.vectors

import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.minecraft
import net.minecraft.util.BlockPos

open class Point3d(x: Double, y: Double, val z: Double) : Point2d(x, y) {
    companion object {
        fun atPlayer(): Point3d {
            return Point3d(minecraft.thePlayer?.posX ?: -1.0, minecraft.thePlayer?.posY ?: -1.0, minecraft.thePlayer?.posZ ?: -1.0)
        }
    }
    constructor(blockPos: BlockPos): this(blockPos.x.toDouble(), blockPos.y.toDouble(), blockPos.z.toDouble())
    fun getPointZ(): Double {
        return z
    }

    fun toBlockPos(): BlockPos {
        return BlockPos(x, y, z)
    }

    fun toBlockPosInt(): BlockPos {
        return BlockPos(x.toInt(), y.toInt(), z.toInt())
    }


    override fun equals(obj: Any?): Boolean {
        if (obj == null) {
            return false
        }
        if (obj !is Point3d) {
            return false
        }
        if (obj.x != x) {
            return false
        }
        if (obj.y != y) {
            return false
        }
        if (obj.z != z) {
            return false
        }

        return true

    }
}