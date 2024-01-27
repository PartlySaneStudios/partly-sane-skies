//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.events.minecraft.player

import me.partlysanestudios.partlysaneskies.events.EventManager
import me.partlysanestudios.partlysaneskies.utils.vectors.Point3d
import me.partlysanestudios.partlysaneskies.utils.vectors.Point3d.Companion.toPoint3d
import net.minecraft.util.BlockPos
import net.minecraft.util.EnumFacing
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

class PlayerBreakBlockEvent(val point: Point3d, val side: EnumFacing) {
    companion object {
        private fun callEvent(functions: List<EventManager.EventFunction>, pos: BlockPos, side: EnumFacing) {
            for (function in functions) {
                try {
                    val event = PlayerBreakBlockEvent(pos.toPoint3d(), side)
                    function.function.call(function.obj, event)
                } catch (exception: Exception) {
                    exception.printStackTrace()
                }
            }
        }

        // Called from the mixin because writing this code in java is about 50 times harder
        internal fun onPlayerBreakBlock(blockPos: BlockPos, side: EnumFacing, cir: CallbackInfoReturnable<Boolean>) {
            callEvent(EventManager.registeredFunctions[PlayerBreakBlockEvent::class] ?: ArrayList(), blockPos, side)
        }
    }
}