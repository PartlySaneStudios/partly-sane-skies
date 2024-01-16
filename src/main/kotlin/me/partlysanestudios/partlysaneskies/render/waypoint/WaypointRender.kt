package me.partlysanestudios.partlysaneskies.render.waypoint

import me.partlysanestudios.partlysaneskies.render.BlockHighlightRenderer
import me.partlysanestudios.partlysaneskies.render.BeamRenderer
import me.partlysanestudios.partlysaneskies.render.TextRenderer.renderText3d
import me.partlysanestudios.partlysaneskies.render.waypoint.WaypointManager.getWaypoints
import net.minecraft.client.Minecraft
import net.minecraft.util.AxisAlignedBB
import net.minecraft.util.Vec3
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object WaypointRender {
    @SubscribeEvent
    fun onRenderWorldLast(event: RenderWorldLastEvent) {
        val mc = Minecraft.getMinecraft()
        val playerPos = mc.thePlayer.getPositionEyes(event.partialTicks)

        for (waypoint in getWaypoints()) {
            val waypointPos =
                Vec3(waypoint.position.x.toDouble(), waypoint.position.y.toDouble(), waypoint.position.z.toDouble())
            val distance = playerPos.distanceTo(waypointPos)

            renderWaypoint(waypoint, distance)
        }
    }

    private fun renderWaypoint(waypoint: Waypoint, distance: Double) {
        // Render waypoint beam
        if (waypoint.showBeam) {
            BeamRenderer.renderBeam(waypoint.position, waypoint.outlineColor, waypoint.fillColor)
        }

        // Render waypoint block highlight
        if (waypoint.showBlockHighlight) {
            BlockHighlightRenderer.renderColoredBlockHighlight(waypoint.position, waypoint.outlineColor, waypoint.fillColor)
        }

        // Render waypoint label
        if (waypoint.showLabel) {
            renderText3d(waypoint.position.up().up(), waypoint.name)
        }

        // Render waypoint distance
        if (waypoint.showDistance) {
            val distanceText = "${distance.toInt()}m"
            renderText3d(waypoint.position.up(), distanceText)
        }
    }
}