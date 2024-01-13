package me.partlysanestudios.partlysaneskies.render

import me.partlysanestudios.partlysaneskies.render.RenderPrimitives.drawBoxOutline
import me.partlysanestudios.partlysaneskies.render.points.Point3d
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.util.BlockPos
import org.lwjgl.opengl.GL11


object BlockHighlightRenderer {
    fun renderColoredBlockHighlight(pos: BlockPos, color: Int) {
        val minecraft = Minecraft.getMinecraft()
        val renderManager = minecraft.renderManager
        val tessellator = Tessellator.getInstance()
        val worldRenderer = tessellator.worldRenderer

        GlStateManager.pushMatrix()
        GlStateManager.translate(-renderManager.viewerPosX, -renderManager.viewerPosY, -renderManager.viewerPosZ)


        GlStateManager.enableBlend()
        GlStateManager.disableTexture2D()
        GlStateManager.disableLighting()
        GlStateManager.disableDepth()

        GlStateManager.color(
            ((color shr 16) and 0xFF) / 255.0f,
            ((color shr 8) and 0xFF) / 255.0f,
            (color and 0xFF) / 255.0f,
            1f
        )

        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO)
        GL11.glLineWidth(4.0f)

        worldRenderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION)
        val x = pos.x.toDouble()
        val y = pos.y.toDouble()
        val z = pos.z.toDouble()

        worldRenderer.drawBoxOutline(Point3d(x, y, z), Point3d(x + 1, y + 1, z + 1))


        tessellator.draw()

        GlStateManager.disableBlend()
        GlStateManager.enableDepth()
        GlStateManager.enableLighting()
        GlStateManager.enableTexture2D()

        GlStateManager.popMatrix()
    }
}