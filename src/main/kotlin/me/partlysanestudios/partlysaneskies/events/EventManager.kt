package me.partlysanestudios.partlysaneskies.events

import me.partlysanestudios.partlysaneskies.events.render.RenderWaypointEvent
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import kotlin.reflect.KFunction
import kotlin.reflect.full.memberFunctions

object EventManager {
    private val registeredObjects = ArrayList<Any>()

    fun register(obj: Any) {

    }

    @SubscribeEvent
    fun onScreenRender(event: RenderWorldLastEvent) {
        val waypointRenderEventFunctions = ArrayList<KFunction<*>>()

        for (obj in registeredObjects) {
            val kClass = obj::class
            for (function in kClass.memberFunctions) {

                if (function.annotations.any { it.annotationClass != SubscribePSSEvent::class }) {
                    continue
                }
                val functionParameters = function.parameters
                if (functionParameters.size != 1) {
                    continue
                }
                val param = functionParameters[0]


                if (param::class == RenderWaypointEvent::class) {
                    waypointRenderEventFunctions.add(function)
                }
            }
        }

        RenderWaypointEvent.onEventCall(event.partialTicks, waypointRenderEventFunctions)

    }

}