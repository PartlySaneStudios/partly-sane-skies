package me.partlysanestudios.partlysaneskies.renderers

import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.WorldRenderer
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.util.BlockPos
import org.lwjgl.opengl.GL11

object BlockHighlightRenderer {
    fun render(pos: BlockPos, color: Int) {
        renderColoredBlockHighlight(pos, color)
    }
    private fun renderColoredBlockHighlight(pos: BlockPos, color: Int) {
        val minecraft = Minecraft.getMinecraft()
        val renderManager = minecraft.renderManager

        GlStateManager.pushMatrix()
        GlStateManager.translate(-renderManager.viewerPosX, -renderManager.viewerPosY, -renderManager.viewerPosZ)

        GlStateManager.enableBlend()
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO)

        GlStateManager.disableTexture2D()
        GlStateManager.depthMask(false)
        GlStateManager.disableDepth()

        val alpha = (color ushr 24 and 0xFF) / 255.0f
        GlStateManager.color(
            ((color shr 16) and 0xFF) / 255.0f,
            ((color shr 8) and 0xFF) / 255.0f,
            (color and 0xFF) / 255.0f,
            alpha
        )

        val tessellator = Tessellator.getInstance()
        val worldRenderer = tessellator.worldRenderer
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION)

        val x = pos.x.toDouble()
        val y = pos.y.toDouble()
        val z = pos.z.toDouble()

        val expansion = 0.002
        val offset = 1.0 - expansion

        // North
        worldRenderer.pos(x - expansion, y - expansion, z - expansion).endVertex()
        worldRenderer.pos(x - expansion, y + offset, z - expansion).endVertex()
        worldRenderer.pos(x + offset, y + offset, z - expansion).endVertex()
        worldRenderer.pos(x + offset, y - expansion, z - expansion).endVertex()

        // South
        worldRenderer.pos(x - expansion, y - expansion, z + offset).endVertex()
        worldRenderer.pos(x + offset, y - expansion, z + offset).endVertex()
        worldRenderer.pos(x + offset, y + offset, z + offset).endVertex()
        worldRenderer.pos(x - expansion, y + offset, z + offset).endVertex()

        // East
        worldRenderer.pos(x - expansion, y - expansion, z - expansion).endVertex()
        worldRenderer.pos(x - expansion, y - expansion, z + offset).endVertex()
        worldRenderer.pos(x - expansion, y + offset, z + offset).endVertex()
        worldRenderer.pos(x - expansion, y + offset, z - expansion).endVertex()

        // West
        worldRenderer.pos(x + offset, y - expansion, z - expansion).endVertex()
        worldRenderer.pos(x + offset, y + offset, z - expansion).endVertex()
        worldRenderer.pos(x + offset, y + offset, z + offset).endVertex()
        worldRenderer.pos(x + offset, y - expansion, z + offset).endVertex()

        // Down
        worldRenderer.pos(x - expansion, y - expansion, z - expansion).endVertex()
        worldRenderer.pos(x + offset, y - expansion, z - expansion).endVertex()
        worldRenderer.pos(x + offset, y - expansion, z + offset).endVertex()
        worldRenderer.pos(x - expansion, y - expansion, z + offset).endVertex()

        // Up
        worldRenderer.pos(x - expansion, y + offset, z - expansion).endVertex()
        worldRenderer.pos(x - expansion, y + offset, z + offset).endVertex()
        worldRenderer.pos(x + offset, y + offset, z + offset).endVertex()
        worldRenderer.pos(x + offset, y + offset, z - expansion).endVertex()

        tessellator.draw()

        GlStateManager.enableDepth()
        GlStateManager.depthMask(true)
        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()

        GlStateManager.popMatrix()
    }
}