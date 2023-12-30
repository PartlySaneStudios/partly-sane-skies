package me.partlysanestudios.partlysaneskies.renderers.waypoint

import me.partlysanestudios.partlysaneskies.renderers.waypoint.WaypointManager.getWaypoints
import me.partlysanestudios.partlysaneskies.utils.ChatUtils
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.util.AxisAlignedBB
import net.minecraft.util.BlockPos
import net.minecraft.util.Vec3
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.lwjgl.opengl.GL11

object WaypointRenderer {
    @SubscribeEvent
    fun onRenderWorldLast(event: RenderWorldLastEvent) {
        val mc = Minecraft.getMinecraft()
        val playerPos = mc.thePlayer.getPositionEyes(event.partialTicks)

        ChatUtils.sendClientMessage("Trying to render waypoints")

        for (waypoint in getWaypoints()) {
            val waypointPos = Vec3(waypoint.position.x + 0.5, waypoint.position.y.toDouble(), waypoint.position.z + 0.5)
            val distance = playerPos.distanceTo(waypointPos)

            ChatUtils.sendClientMessage("Rendering waypoint: ${waypoint.name} at distance: $distance")

            renderWaypoint(waypoint, distance)
        }
    }

    private fun renderWaypoint(waypoint: Waypoint, distance: Double) {
        val mc = Minecraft.getMinecraft()
        val renderManager = mc.renderManager

        val x = waypoint.position.x - renderManager.viewerPosX
        val y = waypoint.position.y - renderManager.viewerPosY
        val z = waypoint.position.z - renderManager.viewerPosZ

        GlStateManager.pushMatrix()
        GlStateManager.translate(x, y, z)

        // Render waypoint beam
        if (waypoint.showBeam) {
            ChatUtils.sendClientMessage("Rendering waypoint beam")
            GlStateManager.disableDepth()
            GlStateManager.disableTexture2D()
            GlStateManager.enableBlend()
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)

            renderWaypointBeam(waypoint.position, waypoint.color)

            GlStateManager.disableBlend()
            GlStateManager.enableTexture2D()
            GlStateManager.enableDepth()
        }

        // Render waypoint block highlight
        if (waypoint.showBlockHighlight) {
            ChatUtils.sendClientMessage("Rendering waypoint block highlight")
            GlStateManager.disableDepth()
            GlStateManager.disableTexture2D()
            GlStateManager.enableBlend()
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)

            renderBlockHighlight(waypoint.position, waypoint.color)

            GlStateManager.disableBlend()
            GlStateManager.enableTexture2D()
            GlStateManager.enableDepth()
        }

        // Render waypoint label
        if (waypoint.showLabel) {
            ChatUtils.sendClientMessage("Rendering waypoint label")
            showLabel(waypoint)
        }

        // Render waypoint distance
        if (waypoint.showDistance) {
            ChatUtils.sendClientMessage("Rendering waypoint distance")
            showDistance(waypoint, distance)
        }


        GlStateManager.popMatrix()

        ChatUtils.sendClientMessage("Finished rendering waypoint: ${waypoint.name}")
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

        val labelBox = AxisAlignedBB(labelX, labelY, labelZ, labelX + labelWidth, labelY + labelHeight, labelZ + labelHeight)

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

        val distanceLabelBox = AxisAlignedBB(distanceLabelX, distanceLabelY, distanceLabelZ, distanceLabelX + distanceLabelWidth, distanceLabelY + distanceLabelHeight, distanceLabelZ + distanceLabelHeight)

        if (distanceLabelBox.isVecInside(Vec3(renderManager.viewerPosX, renderManager.viewerPosY, renderManager.viewerPosZ))) {
            mc.fontRendererObj.drawString(distanceLabel, distanceLabelX.toInt(), distanceLabelY.toInt(), waypoint.color)
        }
    }

    private fun renderBlockHighlight(waypointPos: BlockPos, color: Int) {
        val tessellator = Tessellator.getInstance()
        val worldRenderer = tessellator.worldRenderer

        GlStateManager.color(
            ((color shr 16) and 0xFF) / 255.0f,
            ((color shr 8) and 0xFF) / 255.0f,
            (color and 0xFF) / 255.0f,
            0.3f
        )

        val x = waypointPos.x.toDouble() - Minecraft.getMinecraft().renderManager.viewerPosX - 1
        val y = waypointPos.y.toDouble() - Minecraft.getMinecraft().renderManager.viewerPosY
        val z = waypointPos.z.toDouble() - Minecraft.getMinecraft().renderManager.viewerPosZ + 1

        worldRenderer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION)
        worldRenderer.pos(x, y, z).endVertex()
        worldRenderer.pos(x + 2.0, y, z).endVertex()
        worldRenderer.pos(x + 2.0, y, z + 2.0).endVertex()
        worldRenderer.pos(x, y, z + 2.0).endVertex()
        worldRenderer.pos(x, y, z).endVertex()
        tessellator.draw()
    }

    private fun renderWaypointBeam(waypointPos: BlockPos, color: Int) {
        val playerPos = Minecraft.getMinecraft().renderViewEntity.getPositionEyes(0.0f)
        val relativeWaypointPos = Vec3(waypointPos.x + 0.5 - playerPos.xCoord, waypointPos.y.toDouble() + 0.5 - playerPos.yCoord, waypointPos.z + 0.5 - playerPos.zCoord)

        val tessellator = Tessellator.getInstance()
        val worldRenderer = tessellator.worldRenderer

        GlStateManager.color(
            ((color shr 16) and 0xFF) / 255.0f,
            ((color shr 8) and 0xFF) / 255.0f,
            (color and 0xFF) / 255.0f,
            0.3f
        )

        worldRenderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION)
        worldRenderer.pos(0.0, 0.0, 0.0).endVertex()
        worldRenderer.pos(relativeWaypointPos.xCoord, relativeWaypointPos.yCoord, relativeWaypointPos.zCoord).endVertex()
        tessellator.draw()
    }
}