//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.events.render

import me.partlysanestudios.partlysaneskies.render.waypoint.Waypoint
import net.minecraft.client.Minecraft
import net.minecraft.util.Vec3
import kotlin.reflect.KFunction

class RenderWaypointEvent(
    val pipeline: WaypointRenderPipeline
) {

    companion object {
        fun onEventCall(partialTicks: Float, functions: ArrayList<KFunction<*>>) {
            val pipeline = WaypointRenderPipeline()

            for (function in functions) {
                try {
                    val event = RenderWaypointEvent(pipeline)
                    runAnnotatedFunction(function, event)
                } catch (exception: Exception) {
                    exception.printStackTrace()
                }
            }

            pipeline.renderAll(partialTicks)
        }

        /**
         * Calls all functions in the object and passes a parameter
         *
         * @param function the function to be run
         * @param event The [RenderWaypointEvent] that will be passed to the function
         */
        private fun runAnnotatedFunction(function: KFunction<*>, event: RenderWaypointEvent) {

            try {
                function.call(event)
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
        }
    }

    class WaypointRenderPipeline() {
        private val waypointsToRender = ArrayList<Waypoint>()

        /**
         * Adds a waypoint to the render pipeline
         *
         * @param waypoint the waypoint to be rendered
         */
        fun add(waypoint: Waypoint) {
            waypointsToRender.add(waypoint)
        }

        // Written partially by j10a1n15
        /**
         * Renders all the waypoints in the pipeline
         *
         * @param partialTicks the amount of partial ticks since the last frame that rendered
         */
        internal fun renderAll(partialTicks: Float) {
            val mc = Minecraft.getMinecraft()
            val playerPos = mc.thePlayer.getPositionEyes(partialTicks)

            for (waypoint in waypointsToRender) {
                val waypointPos =
                    Vec3(waypoint.position.x.toDouble(), waypoint.position.y.toDouble(), waypoint.position.z.toDouble())
                val distance = playerPos.distanceTo(waypointPos)

                waypoint.render(distance)
            }
        }
    }
}