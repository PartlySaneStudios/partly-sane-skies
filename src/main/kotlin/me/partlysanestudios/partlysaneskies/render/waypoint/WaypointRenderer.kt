package me.partlysanestudios.partlysaneskies.render.waypoint

import me.partlysanestudios.partlysaneskies.render.BlockHighlightRenderer
import me.partlysanestudios.partlysaneskies.render.BeamRenderer
import me.partlysanestudios.partlysaneskies.render.waypoint.WaypointManager.getWaypoints
import net.minecraft.client.Minecraft
import net.minecraft.util.AxisAlignedBB
import net.minecraft.util.Vec3
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object WaypointRenderer {
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
            BeamRenderer.renderBeam(waypoint.position, waypoint.color)
        }

        // Render waypoint block highlight
        if (waypoint.showBlockHighlight) {
            BlockHighlightRenderer.renderColoredBlockHighlight(waypoint.position, waypoint.color)
        }

        // Render waypoint label
        if (waypoint.showLabel) {
            showLabel(waypoint)
        }

        // Render waypoint distance
        if (waypoint.showDistance) {
            showDistance(waypoint, distance)
        }
    }

    private fun showLabel(waypoint: Waypoint) {
        val mc = Minecraft.getMinecraft()
        val renderManager = mc.renderManager

        val x = waypoint.position.x - renderManager.viewerPosX
        val y = waypoint.position.y - renderManager.viewerPosY
        val z = waypoint.position.z - renderManager.viewerPosZ

        val label = waypoint.name
        val labelWidth = mc.fontRendererObj.getStringWidth(label)
        val labelHeight = mc.fontRendererObj.FONT_HEIGHT

        val labelX = x - labelWidth / 2
        val labelY = y - labelHeight / 2
        val labelZ = z - labelHeight / 2

        val labelBox =
            AxisAlignedBB(labelX, labelY, labelZ, labelX + labelWidth, labelY + labelHeight, labelZ + labelHeight)

        if (labelBox.isVecInside(Vec3(renderManager.viewerPosX, renderManager.viewerPosY, renderManager.viewerPosZ))) {
            mc.fontRendererObj.drawString(label, labelX.toInt(), labelY.toInt(), waypoint.color)
        }
    }

    private fun showDistance(waypoint: Waypoint, distance: Double) {
        val mc = Minecraft.getMinecraft()
        val renderManager = mc.renderManager

        val x = waypoint.position.x - renderManager.viewerPosX
        val y = waypoint.position.y - renderManager.viewerPosY
        val z = waypoint.position.z - renderManager.viewerPosZ

        val distanceLabel = "%.2f".format(distance)
        val distanceLabelWidth = mc.fontRendererObj.getStringWidth(distanceLabel)
        val distanceLabelHeight = mc.fontRendererObj.FONT_HEIGHT

        val distanceLabelX = x - distanceLabelWidth / 2
        val distanceLabelY = y - distanceLabelHeight / 2
        val distanceLabelZ = z - distanceLabelHeight / 2

        val distanceLabelBox = AxisAlignedBB(
            distanceLabelX,
            distanceLabelY,
            distanceLabelZ,
            distanceLabelX + distanceLabelWidth,
            distanceLabelY + distanceLabelHeight,
            distanceLabelZ + distanceLabelHeight
        )

        if (distanceLabelBox.isVecInside(
                Vec3(
                    renderManager.viewerPosX,
                    renderManager.viewerPosY,
                    renderManager.viewerPosZ
                )
            )
        ) {
            mc.fontRendererObj.drawString(distanceLabel, distanceLabelX.toInt(), distanceLabelY.toInt(), waypoint.color)
        }
    }
}