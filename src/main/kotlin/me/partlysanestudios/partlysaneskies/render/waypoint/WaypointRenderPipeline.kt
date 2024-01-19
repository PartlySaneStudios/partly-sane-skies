//
// Written by Su386 except where specified.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.render.waypoint

import net.minecraft.client.Minecraft
import net.minecraft.util.Vec3

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

    // Written by j10a1n15
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