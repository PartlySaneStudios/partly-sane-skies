//
// Written by j10a1n15.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.render

import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.util.BlockPos
import org.lwjgl.opengl.GL11

object TextRenderer {
    fun renderText3d(
        pos: BlockPos,
        text: String,
    ) {
        val x = pos.x.toDouble() + 0.5
        val y = pos.y.toDouble() + 1.0
        val z = pos.z.toDouble() + 0.5

        val renderManager = PartlySaneSkies.minecraft.renderManager
        val fontRenderer = PartlySaneSkies.minecraft.fontRendererObj

        GlStateManager.pushMatrix()
        GlStateManager.translate(
            x - renderManager.viewerPosX,
            y - renderManager.viewerPosY,
            z - renderManager.viewerPosZ,
        )
        GlStateManager.rotate(-renderManager.playerViewY, 0.0f, 1.0f, 0.0f)
        GlStateManager.rotate(renderManager.playerViewX, 1.0f, 0.0f, 0.0f)
        GlStateManager.scale(-0.025, -0.025, 0.025)

        GlStateManager.disableCull()
        GlStateManager.enableBlend()
        GlStateManager.disableLighting()
        GlStateManager.disableDepth()

        GlStateManager.enableBlend()
        GlStateManager.tryBlendFuncSeparate(
            GL11.GL_SRC_ALPHA,
            GL11.GL_ONE_MINUS_SRC_ALPHA,
            GL11.GL_SRC_ALPHA,
            GL11.GL_ONE,
        )

        fontRenderer.drawString(text, -fontRenderer.getStringWidth(text) / 2, 0, 0xFFFFFF)

        GlStateManager.resetColor()
        GlStateManager.disableBlend()
        GlStateManager.enableDepth()

        GlStateManager.popMatrix()
    }
}
