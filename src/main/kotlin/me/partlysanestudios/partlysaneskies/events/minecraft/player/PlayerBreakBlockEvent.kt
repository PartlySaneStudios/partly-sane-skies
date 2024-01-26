package me.partlysanestudios.partlysaneskies.events.minecraft.player

import me.partlysanestudios.partlysaneskies.events.EventManager
import me.partlysanestudios.partlysaneskies.utils.vectors.Point3d
import me.partlysanestudios.partlysaneskies.utils.vectors.Point3d.Companion.toPoint3d
import net.minecraft.util.BlockPos
import net.minecraft.util.EnumFacing
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

class PlayerBreakBlockEvent(val point: Point3d, val side: EnumFacing) {
    companion object {
        private fun callEvent(functions: List<EventManager.EventFunction>, pos: BlockPos, side: EnumFacing) {
            for (function in functions) {
                val event = PlayerBreakBlockEvent(pos.toPoint3d(), side)
                function.function.call(function.obj, event)
            }


        }

        // Called from the mixin because writing this code in java is about 50 times harder
        internal fun onPlayerBreakBlock(blockPos: BlockPos, side: EnumFacing, cir: CallbackInfoReturnable<Boolean>) {
            val onPlayerBreakBlockEventFunctions = ArrayList<EventManager.EventFunction>()

            for (function in EventManager.registeredFunctions) {
                val paramClass = function.function.parameters[1].type.classifier as? KClass<*>

                if (paramClass?.isSubclassOf(PlayerBreakBlockEvent::class) == true) {
                    onPlayerBreakBlockEventFunctions.add(function)
                }
            }
            callEvent(onPlayerBreakBlockEventFunctions, blockPos, side)
        }
    }
}