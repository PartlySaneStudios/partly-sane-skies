//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.events.render

import me.partlysanestudios.partlysaneskies.events.EventManager
import me.partlysanestudios.partlysaneskies.render.waypoint.Waypoint
import me.partlysanestudios.partlysaneskies.utils.vectors.Point3d
import me.partlysanestudios.partlysaneskies.utils.vectors.Point3d.Companion.toPoint3d
import net.minecraft.client.Minecraft
import net.minecraft.util.Vec3

class RenderWaypointEvent(
    val pipeline: WaypointRenderPipeline
) {

    companion object {
        internal fun onEventCall(partialTicks: Float, functions: List<EventManager.EventFunction>) {
            val pipeline = WaypointRenderPipeline()

            for (function in functions) {
                val event = RenderWaypointEvent(pipeline)
                try {
                    function.function.call(function.obj, event)
                } catch (exception: Exception) {
                    exception.printStackTrace()
                }
            }

            pipeline.renderAll(partialTicks)
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
            val playerPos = mc.thePlayer.getPositionEyes(partialTicks).toPoint3d()

            for (waypoint in waypointsToRender) {
                val waypointPos =
                    Point3d(waypoint.position.x.toDouble(), waypoint.position.y.toDouble(), waypoint.position.z.toDouble())
                val distance = playerPos.distanceTo(waypointPos)

                waypoint.render(distance.toDouble())
            }
        }
    }
}