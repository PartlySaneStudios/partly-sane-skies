//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.events

import me.partlysanestudios.partlysaneskies.events.render.RenderWaypointEvent
import me.partlysanestudios.partlysaneskies.events.skyblock.dungeons.DungeonEndEvent
import me.partlysanestudios.partlysaneskies.events.skyblock.dungeons.DungeonStartEvent
import me.partlysanestudios.partlysaneskies.utils.SystemUtils.log
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.apache.logging.log4j.Level
import kotlin.reflect.KFunction
import kotlin.reflect.full.memberFunctions

object EventManager {
    private val registeredFunctions = ArrayList<KFunction<*>>()

    fun register(obj: Any) {
        val kClass = obj::class
        for (function in kClass.memberFunctions) {
            if (function.annotations.any { it.annotationClass != SubscribePSSEvent::class }) {
                continue
            }
            val functionParameters = function.parameters
            if (functionParameters.size != 1) {
                log(Level.WARN, "Unable to add ${function.name} due to multiple function parameters")
                continue
            }

            registeredFunctions.add(function)
        }
    }

    private val renderWorldLastEvents: Array<Any> = arrayOf(RenderWorldLastEvent::class)
    @SubscribeEvent
    fun onScreenRender(event: RenderWorldLastEvent) {
        val waypointRenderEventFunctions = ArrayList<KFunction<*>>()

        for (function in registeredFunctions) {
            if (function.annotations.any { it.annotationClass != SubscribePSSEvent::class }) {
                continue
            }
            val functionParameters = function.parameters
            if (functionParameters.size != 1) {
                continue
            }
            val param = functionParameters[0]

            if (renderWorldLastEvents.contains(param::class)) {
                waypointRenderEventFunctions.add(function)
            }
        }

        RenderWaypointEvent.onEventCall(event.partialTicks, waypointRenderEventFunctions)
    }



    private val chatEvents: Array<Any> = arrayOf(DungeonStartEvent::class, DungeonEndEvent::class)
    @SubscribeEvent
    fun onChatRecievedEvent(event: ClientChatReceivedEvent) {
        val dungeonStartEventFunctions = ArrayList<KFunction<*>>()
        val dungeonEndEventFunctions = ArrayList<KFunction<*>>()


        for (function in registeredFunctions) {

            if (function.annotations.any { it.annotationClass != SubscribePSSEvent::class }) {
                continue
            }
            val functionParameters = function.parameters
            if (functionParameters.size != 1) {
                continue
            }
            val param = functionParameters[0]


            if (chatEvents.contains(param::class)) {
                dungeonEndEventFunctions.add(function)
                dungeonStartEventFunctions.add(function)
            }
        }

        val message = event.message.formattedText

        DungeonStartEvent.onMessageRecieved(dungeonStartEventFunctions, message)
        DungeonEndEvent.onMessageRecieved(dungeonEndEventFunctions, message)
    }
}