//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.events

import me.partlysanestudios.partlysaneskies.events.minecraft.player.PlayerBreakBlockEvent
import me.partlysanestudios.partlysaneskies.events.minecraft.render.RenderWaypointEvent
import me.partlysanestudios.partlysaneskies.events.skyblock.dungeons.DungeonEndEvent
import me.partlysanestudios.partlysaneskies.events.skyblock.dungeons.DungeonStartEvent
import me.partlysanestudios.partlysaneskies.utils.SystemUtils.log
import net.minecraft.util.BlockPos
import net.minecraft.util.EnumFacing
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.apache.logging.log4j.Level
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.memberFunctions

object EventManager {
    private val registeredFunctions = ArrayList<EventFunction>()

    fun register(obj: Any) {
        val kClass = obj::class // get the class
        for (function in kClass.memberFunctions) { // for each function in the class
            if (!function.hasAnnotation<SubscribePSSEvent>()) { // if the functions are not annotated, continue
                continue
            }

            val functionParameters = function.parameters
            if (functionParameters.size != 2) { // if there is not only 1 parameter (param 1 is always the instance parameter
                log(Level.WARN, "Unable to add ${function.name} due to incorrect number of function parameters (${functionParameters.size}")
                continue
            }

            registeredFunctions.add(EventFunction(obj, function)) // adds the function to a list to call
            log(Level.INFO, "Registered ${function.name} from ${obj.javaClass.name} in PSS events")
        }
    }

    @SubscribeEvent
    fun onScreenRender(event: RenderWorldLastEvent) {
        val waypointRenderEventFunctions = ArrayList<EventFunction>()

        for (function in registeredFunctions) {
            val paramClass = function.function.parameters[1].type.classifier as? KClass<*>

            if (paramClass?.isSubclassOf(RenderWaypointEvent::class) == true) {
                waypointRenderEventFunctions.add(function)
            }
        }

        RenderWaypointEvent.onEventCall(event.partialTicks, waypointRenderEventFunctions)
    }

    @SubscribeEvent
    fun onChatRecievedEvent(event: ClientChatReceivedEvent) {
        val dungeonStartEventFunctions = ArrayList<EventFunction>()
        val dungeonEndEventFunctions = ArrayList<EventFunction>()

        for (function in registeredFunctions) {
            val paramClass = function.function.parameters[1].type.classifier as? KClass<*>

            if (paramClass?.isSubclassOf(DungeonStartEvent::class) == true) {
                dungeonStartEventFunctions.add(function)
            }
            else if (paramClass?.isSubclassOf(DungeonEndEvent::class) == true) {
                dungeonEndEventFunctions.add(function)
            }
        }

        val message = event.message.formattedText

        DungeonStartEvent.onMessageRecieved(dungeonStartEventFunctions, message)
        DungeonEndEvent.onMessageRecieved(dungeonEndEventFunctions, message)
    }


    // Called from the mixin because writing this code in java is about 50 times harder
    fun onPlayerBreakBlock(blockPos: BlockPos, side: EnumFacing, cir: CallbackInfoReturnable<Boolean>) {

        val onPlayerBreakBlockEventFunctions = ArrayList<EventFunction>()

        for (function in registeredFunctions) {
            val paramClass = function.function.parameters[1].type.classifier as? KClass<*>

            if (paramClass?.isSubclassOf(PlayerBreakBlockEvent::class) == true) {
                onPlayerBreakBlockEventFunctions.add(function)
            }
        }

        PlayerBreakBlockEvent.onEventCall(onPlayerBreakBlockEventFunctions, blockPos, side)


    }

    internal class EventFunction(val obj: Any, val function: KFunction<*> )
}