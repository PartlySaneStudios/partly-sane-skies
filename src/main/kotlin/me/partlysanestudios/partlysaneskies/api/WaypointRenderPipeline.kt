//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.api

import me.partlysanestudios.partlysaneskies.render.waypoint.Waypoint
import me.partlysanestudios.partlysaneskies.utils.geometry.vectors.Point3d.Companion.toPoint3d
import net.minecraft.client.Minecraft

class WaypointRenderPipeline {
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

        for (waypoint in waypointsToRender) {
            val waypointPos = waypoint.position.toPoint3d()
            val distance = waypointPos.distanceToPlayer()

            waypoint.render(distance)
        }
    }
}
