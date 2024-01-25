package me.partlysanestudios.partlysaneskies.events.minecraft.player

import me.partlysanestudios.partlysaneskies.events.EventManager
import me.partlysanestudios.partlysaneskies.utils.vectors.Point3d
import me.partlysanestudios.partlysaneskies.utils.vectors.Point3d.Companion.toPoint3d
import net.minecraft.util.BlockPos
import net.minecraft.util.EnumFacing

class PlayerBreakBlockEvent(val point: Point3d, val side: EnumFacing) {
    companion object {
        internal fun onEventCall(functions: List<EventManager.EventFunction>, pos: BlockPos, side: EnumFacing) {
            val event = PlayerBreakBlockEvent(pos.toPoint3d(), side)
            for (function in functions) {
                function.function.call(function.obj, event)
            }
        }
    }
}