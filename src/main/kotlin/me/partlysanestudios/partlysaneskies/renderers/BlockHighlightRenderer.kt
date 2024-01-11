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
        val worldRenderer = Tessellator.getInstance().worldRenderer

        GlStateManager.pushMatrix()
        GlStateManager.translate(-renderManager.viewerPosX, -renderManager.viewerPosY, -renderManager.viewerPosZ)

        GlStateManager.disableTexture2D()
        GlStateManager.disableLighting()
        GlStateManager.disableDepth()

        GlStateManager.color(
            ((color shr 16) and 0xFF) / 255.0f,
            ((color shr 8) and 0xFF) / 255.0f,
            (color and 0xFF) / 255.0f,
            0.4f
        )

        GlStateManager.enableBlend()
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO)

        worldRenderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION)
        val x = pos.x.toDouble()
        val y = pos.y.toDouble()
        val z = pos.z.toDouble()

        // front
        worldRenderer.pos(x, y, z).endVertex()
        worldRenderer.pos(x + 1, y, z).endVertex()

        worldRenderer.pos(x + 1, y, z).endVertex()
        worldRenderer.pos(x + 1, y + 1, z).endVertex()

        worldRenderer.pos(x + 1, y + 1, z).endVertex()
        worldRenderer.pos(x, y + 1, z).endVertex()

        worldRenderer.pos(x, y + 1, z).endVertex()
        worldRenderer.pos(x, y, z).endVertex()

        // back
        worldRenderer.pos(x, y, z + 1).endVertex()
        worldRenderer.pos(x + 1, y, z + 1).endVertex()

        worldRenderer.pos(x + 1, y, z + 1).endVertex()
        worldRenderer.pos(x + 1, y + 1, z + 1).endVertex()

        worldRenderer.pos(x + 1, y + 1, z + 1).endVertex()
        worldRenderer.pos(x, y + 1, z + 1).endVertex()

        worldRenderer.pos(x, y + 1, z + 1).endVertex()
        worldRenderer.pos(x, y, z + 1).endVertex()

        // sides
        worldRenderer.pos(x, y, z).endVertex()
        worldRenderer.pos(x, y, z + 1).endVertex()

        worldRenderer.pos(x + 1, y, z).endVertex()
        worldRenderer.pos(x + 1, y, z + 1).endVertex()

        worldRenderer.pos(x + 1, y + 1, z).endVertex()
        worldRenderer.pos(x + 1, y + 1, z + 1).endVertex()

        worldRenderer.pos(x, y + 1, z).endVertex()
        worldRenderer.pos(x, y + 1, z + 1).endVertex()


        Tessellator.getInstance().draw()

        GlStateManager.disableBlend()
        GlStateManager.enableDepth()
        GlStateManager.enableLighting()
        GlStateManager.enableTexture2D()

        GlStateManager.popMatrix()
    }
}