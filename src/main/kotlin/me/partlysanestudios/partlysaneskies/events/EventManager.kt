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
        val kClass = obj::class // get the class
        for (function in kClass.memberFunctions) { // for each function in the class
            if (function.annotations.any { it.annotationClass != SubscribePSSEvent::class }) { // if the functions are not annotated, continue
                continue
            }
            val functionParameters = function.parameters
            if (functionParameters.size != 1) { // if there is not only 1 parameter
                log(Level.WARN, "Unable to add ${function.name} due to multiple function parameters")
                continue
            }

            registeredFunctions.add(function) // adds the function to a list to call
        }
    }

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

            if (param::class.isInstance(RenderWaypointEvent::class)) {
                waypointRenderEventFunctions.add(function)
            }
        }

        RenderWaypointEvent.onEventCall(event.partialTicks, waypointRenderEventFunctions)
    }

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


            if (param::class.isInstance(DungeonStartEvent::class)) {
                dungeonStartEventFunctions.add(function)
            }
            else if (param::class.isInstance(DungeonEndEvent::class)) {
                dungeonEndEventFunctions.add(function)
            }
        }

        val message = event.message.formattedText

        DungeonStartEvent.onMessageRecieved(dungeonStartEventFunctions, message)
        DungeonEndEvent.onMessageRecieved(dungeonEndEventFunctions, message)
    }
}