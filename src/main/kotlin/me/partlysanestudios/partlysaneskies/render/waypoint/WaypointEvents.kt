//
// Written by Su386.
// See LICENSE for copyright and license notices.
//



package me.partlysanestudios.partlysaneskies.render.waypoint

import me.partlysanestudios.partlysaneskies.render.points.Point3d
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import kotlin.reflect.full.memberFunctions
/**
 * @see WaypointEvents.ExampleObject for example
 */
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


    /**
     * An example object for the waypoint events system
     */
    private object ExampleObject {

        /**
         * Example of how to render a waypoint
         *
         * Function that is called when the waypoint about to be rendered (called every frame)
         *
         * @param renderPipeline Add your waypoint you want to render to this pipeline
         */
        @WaypointEvent
        private fun onWaypointRender(renderPipeline: WaypointRenderPipeline) {
            val condition = true

            val customWaypoint = Waypoint(
                "Example Waypoint",
                Point3d(0.0, 0.0, 0.0).toBlockPosInt()
            )
            if (condition) {
                renderPipeline.add(customWaypoint)
            }
        }


        /**
         * Example of how to register you event
         *
         * Call WaypointEvents.register(ExampleObject) so that the events are called
         */
        private fun load() {
            WaypointEvents.register(ExampleObject)
        }
    }
}

