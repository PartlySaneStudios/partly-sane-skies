//
// Written by Su386 with help from Hannibal002.
// See LICENSE for copyright and license notices.
//


//
// Time spent pulling hair out in this file:
// Su386: 14.5 hours and 13 cups of tea
//
// Total: 14.5 hours and 13 cups of tea
//
// Good luck to any future devs.
// The thoughts and prayers of the ancients are with you (Stargate Reference)
//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


//
// Lets goooo it finally works (half of the issues were my own stupidity)
//

//
// Still better than java tho
//


package me.partlysanestudios.partlysaneskies.garden.endoffarmnotifer

import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.garden.endoffarmnotifer.points.Point3d
import me.partlysanestudios.partlysaneskies.system.ThemeManager
import me.partlysanestudios.partlysaneskies.utils.ChatUtils
import me.partlysanestudios.partlysaneskies.utils.ImageUtils

import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.lwjgl.opengl.GL11
import java.awt.Color

object RangeHighlight {

    @SubscribeEvent
    fun onRenderWorldLast(event: RenderWorldLastEvent) {
        if (!PartlySaneSkies.config.showFarmRegions && EndOfFarmNotifier.rangeToHighlight == null) {
            return
        }
        if (!EndOfFarmNotifier.inGarden()) {
            return
        }

        for (range in EndOfFarmNotifier.ranges) {
            var color = Color(255, 255, 255)
            color = ImageUtils.applyOpacityToColor(color, (.2 * 255).toInt())
//                ChatUtils.sendClientMessage("Range to highlight: ${EndOfFarmNotifier.rangeToHighlight}, Current Range: $range")
            if (range.equals(EndOfFarmNotifier.rangeToHighlight)) {
//                    ChatUtils.sendClientMessage("Is range to highlight")
                color = ThemeManager.getAccentColor().toJavaColor()
                color = ImageUtils.applyOpacityToColor(color, (.4 * 255).toInt())
            }

            val effectiveRange = Range3d(range.points[0].x, range.points[0].y, range.points[0].z, range.points[1].x + 1, range.points[1].y + 1, range.points[1].z + 1)
            renderBox(effectiveRange, event.partialTicks, color)
        }
    }
    fun renderBox(range: Range3d, partialTicks: Float, color: Color) {
        try {
            renderBoxFaces(range, color, false, partialTicks)
            renderBoxEdges(range, false, partialTicks)

            val pos1Block = Range3d(range.points[0].x, range.points[0].y, range.points[0].z, range.points[0].x + 1, range.points[0].y + 1, range.points[0].z + 1)
            renderBoxFaces(pos1Block, Color(255, 100, 100, (.75 * 255).toInt()), false, partialTicks)
            renderBoxEdges(pos1Block, false, partialTicks)

            val pos2Block = Range3d(range.points[1].x, range.points[1].y, range.points[1].z, range.points[1].x - 1, range.points[1].y - 1, range.points[1].z - 1)
            renderBoxFaces(pos2Block, Color(100, 100, 255, (.75 * 255).toInt()), false, partialTicks)
            renderBoxEdges(pos2Block, false, partialTicks)
        } catch (e: NullPointerException) {
            ChatUtils.sendClientMessage("Failed rendering of $range")
            throw RuntimeException(e)
        }

    }

//        Renders the faces of a box given a range
    private fun renderBoxFaces(range: Range3d, color: Color, renderRelativeToPlayer: Boolean = false, partialTicks: Float) {
//            Sets the correct state
        GlStateManager.enableBlend()
        GlStateManager.disableLighting()
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0)
        GlStateManager.disableTexture2D()
        GlStateManager.disableCull()

//            Gets the tessellator
        val tessellator = Tessellator.getInstance() // from my understanding it's just a tesseract but for nerdier nerds
        val worldRenderer = tessellator.worldRenderer

        GlStateManager.color(color.red / 255f, color.blue / 255f, color.green / 255f, color.alpha / 255f)


        //            Gets the x y z adn z where 1 is the smaller coordinate and 2 is the bigger one
        var x1 = range.points[0].x
        var x2 = range.points[1].x

        var y1 = range.points[0].y
        var y2 = range.points[1].y

        var z1 = range.points[0].z
        var z2 = range.points[1].z

        if(!renderRelativeToPlayer) {
            val playerPos = getExactPlayerPosition(partialTicks)
            //            Gets the x y z adn z where 1 is the smaller coordinate and 2 is the bigger one
            x1 = range.points[0].x - playerPos.x
            x2 = range.points[1].x - playerPos.x

            y1 = range.points[0].y - playerPos.y
            y2 = range.points[1].y - playerPos.y

            z1 = range.points[0].z - playerPos.z
            z2 = range.points[1].z - playerPos.z
        }


//            ChatUtils.sendClientMessage("x1: $x1, x2: $x2, y1: $y1, y2: $y2, z1: $z1, z2: $z2")

//            Draws each face

        // Front face
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION)
        worldRenderer.pos(x1, y1, z1).endVertex()
        worldRenderer.pos(x2, y1, z1).endVertex()
        worldRenderer.pos(x2, y2, z1).endVertex()
        worldRenderer.pos(x1, y2, z1).endVertex()
        tessellator.draw()

        // Back face
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION)
        worldRenderer.pos(x1, y1, z2).endVertex()
        worldRenderer.pos(x2, y1, z2).endVertex()
        worldRenderer.pos(x2, y2, z2).endVertex()
        worldRenderer.pos(x1, y2, z2).endVertex()
        tessellator.draw()

        // Top face
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION)
        worldRenderer.pos(x1, y2, z1).endVertex()
        worldRenderer.pos(x2, y2, z1).endVertex()
        worldRenderer.pos(x2, y2, z2).endVertex()
        worldRenderer.pos(x1, y2, z2).endVertex()
        tessellator.draw()

        // Bottom face
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION)
        worldRenderer.pos(x1, y1, z1).endVertex()
        worldRenderer.pos(x2, y1, z1).endVertex()
        worldRenderer.pos(x2, y1, z2).endVertex()
        worldRenderer.pos(x1, y1, z2).endVertex()
        tessellator.draw()

        // Left face
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION)
        worldRenderer.pos(x1, y1, z1).endVertex()
        worldRenderer.pos(x1, y2, z1).endVertex()
        worldRenderer.pos(x1, y2, z2).endVertex()
        worldRenderer.pos(x1, y1, z2).endVertex()
        tessellator.draw()

        // Right face
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION)
        worldRenderer.pos(x2, y1, z1).endVertex()
        worldRenderer.pos(x2, y2, z1).endVertex()
        worldRenderer.pos(x2, y2, z2).endVertex()
        worldRenderer.pos(x2, y1, z2).endVertex()
        tessellator.draw()

//            Resets
        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()
    }


    fun renderBoxEdges(range: Range3d, renderRelativeToPlayer: Boolean = false, partialTicks: Float) {
        //            Sets the correct state
        GlStateManager.enableBlend()
        GlStateManager.disableLighting()
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0)
        GlStateManager.disableTexture2D()
        GlStateManager.disableCull()

//            Gets the tessellator
        val tessellator = Tessellator.getInstance() // from my understanding it's just a tesseract but for nerdier nerds
        val worldRenderer = tessellator.worldRenderer

        //            Gets the x y z adn z where 1 is the smaller coordinate and 2 is the bigger one
        var x1 = range.points[0].x
        var x2 = range.points[1].x

        var y1 = range.points[0].y
        var y2 = range.points[1].y

        var z1 = range.points[0].z
        var z2 = range.points[1].z
        if(!renderRelativeToPlayer) {
            val playerPos = getExactPlayerPosition(partialTicks)
            //            Gets the x y z adn z where 1 is the smaller coordinate and 2 is the bigger one
            x1 = range.points[0].x - playerPos.x
            x2 = range.points[1].x - playerPos.x

            y1 = range.points[0].y - playerPos.y
            y2 = range.points[1].y - playerPos.y

            z1 = range.points[0].z - playerPos.z
            z2 = range.points[1].z - playerPos.z
        }


        // Front face
        worldRenderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION)
        GlStateManager.color(1f, 0f, 0f, 1f)
        worldRenderer.pos(x1, y1, z1).endVertex()
        worldRenderer.pos(x2, y1, z1).endVertex()
        tessellator.draw()

        worldRenderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION)
        GlStateManager.color(0f, 1f, 0f, 1f)
        worldRenderer.pos(x1, y1, z1).endVertex()
        worldRenderer.pos(x1, y2, z1).endVertex()
        tessellator.draw()

        worldRenderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION)
        GlStateManager.color(0f, 1f, 0f, 1f)
        worldRenderer.pos(x2, y1, z1).endVertex()
        worldRenderer.pos(x2, y2, z1).endVertex()
        tessellator.draw()

        worldRenderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION)
        GlStateManager.color(1f, 0f, 0f, 1f)
        worldRenderer.pos(x1, y2, z1).endVertex()
        worldRenderer.pos(x2, y2, z1).endVertex()
        tessellator.draw()

        // Back face
        worldRenderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION)
        GlStateManager.color(1f, 0f, 0f, 1f)
        worldRenderer.pos(x1, y1, z2).endVertex()
        worldRenderer.pos(x2, y1, z2).endVertex()
        tessellator.draw()

        worldRenderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION)
        GlStateManager.color(0f, 1f, 0f, 1f)
        worldRenderer.pos(x1, y1, z2).endVertex()
        worldRenderer.pos(x1, y2, z2).endVertex()
        tessellator.draw()

        worldRenderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION)
        GlStateManager.color(0f, 1f, 0f, 1f)
        worldRenderer.pos(x2, y1, z2).endVertex()
        worldRenderer.pos(x2, y2, z2).endVertex()
        tessellator.draw()

        worldRenderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION)
        GlStateManager.color(1f, 0f, 0f, 1f)
        worldRenderer.pos(x1, y2, z2).endVertex()
        worldRenderer.pos(x2, y2, z2).endVertex()
        tessellator.draw()

        // Connecting edges
        worldRenderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION)
        GlStateManager.color(0f, 0f, 1f, 1f)
        worldRenderer.pos(x1, y1, z1).endVertex()
        worldRenderer.pos(x1, y1, z2).endVertex()
        tessellator.draw()

        worldRenderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION)
        GlStateManager.color(0f, 0f, 1f, 1f)
        worldRenderer.pos(x2, y1, z1).endVertex()
        worldRenderer.pos(x2, y1, z2).endVertex()
        tessellator.draw()

        worldRenderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION)
        GlStateManager.color(0f, 0f, 1f, 1f)
        worldRenderer.pos(x1, y2, z1).endVertex()
        worldRenderer.pos(x1, y2, z2).endVertex()
        tessellator.draw()

        worldRenderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION)
        GlStateManager.color(0f, 0f, 1f, 1f)
        worldRenderer.pos(x2, y2, z1).endVertex()
        worldRenderer.pos(x2, y2, z2).endVertex()
        tessellator.draw()

//            Resets
        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()
    }

    fun getExactPlayerPosition(partialTicks: Float): Point3d {
        val player = PartlySaneSkies.minecraft.thePlayer
        val xPos = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks
        val yPos = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks
        val zPos = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks

        return Point3d(xPos, yPos, zPos)
    }

}
