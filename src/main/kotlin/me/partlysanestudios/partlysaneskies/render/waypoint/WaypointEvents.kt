package me.partlysanestudios.partlysaneskies.render.waypoint

import me.partlysanestudios.partlysaneskies.utils.ChatUtils.sendClientMessage
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import kotlin.reflect.full.memberFunctions

object WaypointEvents {
    private val registeredClasses = ArrayList<Any>()

    @SubscribeEvent
    fun onScreenRender(event: RenderWorldLastEvent) {
        val pipeline = WaypointRenderPipeline()

        for (obj in registeredClasses) {
            try {
                runAnnotatedFunctions(obj, pipeline)
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
        }

        pipeline.renderAll(event.partialTicks)
    }

    /**
     * Registers a new waypoint object for waypoint events
     *
     * @param obj The object to be registered
     */
    fun register(obj: Any) {
        registeredClasses.add(obj)
    }


    /**
     * Calls all functions in the object and passes a parameter
     *
     * @param obj The object whose functions annotated with @WaypointEvent are called
     * @param pipeline The pipeline passed to the object
     */
    private fun runAnnotatedFunctions(obj: Any, pipeline: WaypointRenderPipeline) {
        val kClass = obj::class
        for (function in kClass.memberFunctions) {
            try {
                if (function.annotations.any { it.annotationClass == WaypointEvent::class }) {
                    function.call(obj, pipeline)
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
        }
    }
}

