package me.partlysanestudios.partlysaneskies.renderers

import me.partlysanestudios.partlysaneskies.PartlySaneSkies.minecraft
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.util.BlockPos
import org.lwjgl.opengl.GL11

object BeamRenderer {
    fun render(pos: BlockPos, color: Int) {
        renderBeam(pos, color)
    }

    private fun renderBeam(pos: BlockPos, color: Int) {
        GL11.glPushMatrix()
        GL11.glDisable(GL11.GL_TEXTURE_2D)
        GL11.glDisable(GL11.GL_LIGHTING)

        GL11.glTranslated(
            pos.x.toDouble() - minecraft.renderManager.viewerPosX,
            pos.y.toDouble() - minecraft.renderManager.viewerPosY,
            pos.z.toDouble() - minecraft.renderManager.viewerPosZ
        )

        GL11.glEnable(GL11.GL_BLEND)
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
        GL11.glDisable(GL11.GL_DEPTH_TEST)
        GL11.glLineWidth(7.0f)

        // Set the color
        val r = ((color shr 16) and 0xFF) / 255.0f
        val g = ((color shr 8) and 0xFF) / 255.0f
        val b = (color and 0xFF) / 255.0f
        GL11.glColor4f(r, g, b, 0.7f)

        val x1 = 0.5
        val y1 = 0.5
        val z1 = 0.5
        val y2 = 256.0

        // Draw the beam
        GL11.glBegin(GL11.GL_LINES)
        GL11.glVertex3d(x1, y1, z1)
        GL11.glVertex3d(x1, y2, z1)
        GL11.glEnd()


        GL11.glEnable(GL11.GL_DEPTH_TEST)
        GL11.glDisable(GL11.GL_BLEND)
        GL11.glEnable(GL11.GL_TEXTURE_2D)
        GL11.glEnable(GL11.GL_LIGHTING)
        GL11.glPopMatrix()
    }
}