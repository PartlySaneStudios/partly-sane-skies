//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.features.dungeons

import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.config
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.minecraft
import me.partlysanestudios.partlysaneskies.events.SubscribePSSEvent
import me.partlysanestudios.partlysaneskies.events.skyblock.dungeons.DungeonEndEvent
import me.partlysanestudios.partlysaneskies.events.skyblock.dungeons.DungeonStartEvent
import me.partlysanestudios.partlysaneskies.features.debug.DebugKey.isDebugMode
import me.partlysanestudios.partlysaneskies.features.themes.ThemeManager.accentColor
import me.partlysanestudios.partlysaneskies.render.RenderPrimitives.drawBoxFill
import me.partlysanestudios.partlysaneskies.utils.MinecraftUtils
import me.partlysanestudios.partlysaneskies.utils.vectors.Point3d
import me.partlysanestudios.partlysaneskies.utils.vectors.Point3d.Companion.toPoint3d
import me.partlysanestudios.partlysaneskies.utils.vectors.Range3d
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.entity.boss.EntityWither
import net.minecraft.util.EnumFacing.Axis
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.lwjgl.opengl.GL11

object GoldorWall {
    var lastGoldorPos: Point3d? = null
    var lastFacingDirection: Axis? = null

    @SubscribeEvent
    fun renderWall(event: RenderWorldLastEvent) {
        if (!config.goldorWall) {
            return
        }

        if (!(TerminalWaypoints.getCurrentBoundingBox(Point3d.atPlayer()) != null || (isDebugMode() && config.debugGoldorWall))) {
            return
        }

        val goldor = getGoldor()
        if (goldor != null) {
            lastGoldorPos = goldor.position.toPoint3d()
            lastFacingDirection = goldor.horizontalFacing.axis
        }


        val point1 = if (lastFacingDirection == Axis.X && lastGoldorPos != null) {
            (Point3d(lastGoldorPos?.x ?: 0.0, (lastGoldorPos?.y?.minus(10)) ?: 0.0, (lastGoldorPos?.z?.minus(10)) ?: 0.0))
        } else if (lastFacingDirection == Axis.Z && lastGoldorPos != null){
            (Point3d((lastGoldorPos?.x?.minus(10)) ?: 0.0, (lastGoldorPos?.y?.minus(10)) ?: 0.0, lastGoldorPos?.z ?: 0.0))
        } else {
            return
        }

        val point2 = if (lastFacingDirection == Axis.X && lastGoldorPos != null) {
            (Point3d(lastGoldorPos?.x ?: 0.0, (lastGoldorPos?.y?.plus(10)) ?: 0.0, (lastGoldorPos?.z?.plus(10)) ?: 0.0))
        } else if (lastFacingDirection == Axis.Z && lastGoldorPos != null){
            (Point3d((lastGoldorPos?.x?.plus(10)) ?: 0.0, (lastGoldorPos?.y?.plus(10)) ?: 0.0, (lastGoldorPos?.z ?: 0.0) + 0.2))
        } else {
            return
        }


        //            Sets the correct state
        val renderManager = minecraft.renderManager

        GlStateManager.pushMatrix()
        GlStateManager.translate(-renderManager.viewerPosX, -renderManager.viewerPosY, -renderManager.viewerPosZ)

        GlStateManager.disableTexture2D()
        GlStateManager.disableCull()
        GlStateManager.enableBlend()
        GlStateManager.disableLighting()

        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO)
        GL11.glLineWidth(4.0f)


//            Gets the tessellator
        val tessellator = Tessellator.getInstance() // from my understanding it's just a tesseract but for nerdier nerds
        val worldRenderer = tessellator.worldRenderer

        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION)
        GlStateManager.color(accentColor.toJavaColor().red/255f, accentColor.toJavaColor().green/255f, accentColor.toJavaColor().blue/255f, (accentColor.toJavaColor().alpha/255f) * .667f)
        val range = Range3d(point1, point2)
        worldRenderer.drawBoxFill(range.points[0], range.points[1])
        tessellator.draw()

        GlStateManager.resetColor()
        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()
        GlStateManager.enableDepth()

        GlStateManager.popMatrix()
    }

    private fun getGoldor(): EntityWither? {
        val allMobs = MinecraftUtils.getAllEntitiesInWorld()

        val withers = ArrayList<EntityWither>()
        for (mob in allMobs) {
            if (mob is EntityWither) {
                withers.add(mob)
            }
        }

        var goldor: EntityWither? = null

        for (wither in withers) {
            if (wither.displayName.unformattedText.contains("Goldor")) {
                goldor = wither;
                break
            }
        }

        return goldor
    }

    @SubscribePSSEvent
    fun onDungeonStart(event: DungeonStartEvent) {
        lastGoldorPos = null
        lastFacingDirection = null
    }

    @SubscribePSSEvent
    fun onDungeonEnd(event: DungeonEndEvent) {
        lastGoldorPos = null
        lastFacingDirection = null
    }
}