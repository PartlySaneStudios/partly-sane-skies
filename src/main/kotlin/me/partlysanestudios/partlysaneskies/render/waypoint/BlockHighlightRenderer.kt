package me.partlysanestudios.partlysaneskies.render.waypoint

import me.partlysanestudios.partlysaneskies.render.RenderPrimitives.drawBoxFill
import me.partlysanestudios.partlysaneskies.render.RenderPrimitives.drawBoxOutline
import me.partlysanestudios.partlysaneskies.utils.vectors.Point3d
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.util.BlockPos
import org.lwjgl.opengl.GL11
import java.awt.Color


object BlockHighlightRenderer {
    fun renderColoredBlockHighlight(pos: BlockPos, outlineColor: Color, fillColor: Color) {
        val minecraft = Minecraft.getMinecraft()
        val renderManager = minecraft.renderManager
        val tessellator = Tessellator.getInstance()
        val worldRenderer = tessellator.worldRenderer

        GlStateManager.pushMatrix()
        GlStateManager.translate(-renderManager.viewerPosX, -renderManager.viewerPosY, -renderManager.viewerPosZ)

        GlStateManager.disableTexture2D()
        GlStateManager.disableCull()
        GlStateManager.enableBlend()
        GlStateManager.disableLighting()
        GlStateManager.disableDepth()

        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO)
        GL11.glLineWidth(4.0f)

        val x = pos.x.toDouble()
        val y = pos.y.toDouble()
        val z = pos.z.toDouble()

        worldRenderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION)
        GlStateManager.color(outlineColor.red/255f, outlineColor.green/255f, outlineColor.blue/255f, outlineColor.alpha/255f)
        worldRenderer.drawBoxOutline(Point3d(x, y, z), Point3d(x + 1, y + 1, z + 1))
        tessellator.draw()


        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION)
        GlStateManager.color(fillColor.red/255f, fillColor.green/255f, fillColor.blue/255f, fillColor.alpha/255f)
        worldRenderer.drawBoxFill(Point3d(x, y, z), Point3d(x + 1, y + 1, z + 1))
        tessellator.draw()

        GlStateManager.resetColor()
        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()
        GlStateManager.enableDepth()


        GlStateManager.popMatrix()
    }
}