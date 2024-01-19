package me.partlysanestudios.partlysaneskies.render.waypoint

import net.minecraft.client.Minecraft
import net.minecraft.util.Vec3

class WaypointRenderPipeline() {
    private val waypointsToRender = ArrayList<Waypoint>()

    fun add(waypoint: Waypoint) {
        waypointsToRender.add(waypoint)
    }

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