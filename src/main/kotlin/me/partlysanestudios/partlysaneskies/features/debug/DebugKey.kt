//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.features.debug

import cc.polyfrost.oneconfig.config.core.OneColor
import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.components.Window
import gg.essential.universal.UResolution
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.config
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.minecraft
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.time
import me.partlysanestudios.partlysaneskies.data.cache.StatsData
import me.partlysanestudios.partlysaneskies.data.skyblockdata.Rarity
import me.partlysanestudios.partlysaneskies.events.SubscribePSSEvent
import me.partlysanestudios.partlysaneskies.events.minecraft.PSSChatEvent
import me.partlysanestudios.partlysaneskies.events.minecraft.render.RenderWaypointEvent
import me.partlysanestudios.partlysaneskies.features.dungeons.PlayerRating
import me.partlysanestudios.partlysaneskies.features.dungeons.TerminalWaypoints
import me.partlysanestudios.partlysaneskies.features.gui.hud.DropBannerDisplay
import me.partlysanestudios.partlysaneskies.features.items.rngdrop.Drop
import me.partlysanestudios.partlysaneskies.features.themes.ThemeManager
import me.partlysanestudios.partlysaneskies.render.RenderEuclid.drawCylinderFill
import me.partlysanestudios.partlysaneskies.render.RenderEuclid.drawCylinderOutline
import me.partlysanestudios.partlysaneskies.render.gui.hud.BannerRenderer.renderNewBanner
import me.partlysanestudios.partlysaneskies.render.gui.hud.PSSBanner
import me.partlysanestudios.partlysaneskies.render.waypoint.Waypoint
import me.partlysanestudios.partlysaneskies.system.SystemNotification
import me.partlysanestudios.partlysaneskies.utils.ChatUtils.sendClientMessage
import me.partlysanestudios.partlysaneskies.utils.HypixelUtils
import me.partlysanestudios.partlysaneskies.utils.MinecraftUtils.containerInventory
import me.partlysanestudios.partlysaneskies.utils.MinecraftUtils.getItemstackList
import me.partlysanestudios.partlysaneskies.utils.MinecraftUtils.xSize
import me.partlysanestudios.partlysaneskies.utils.MinecraftUtils.ySize
import me.partlysanestudios.partlysaneskies.utils.SystemUtils.copyStringToClipboard
import me.partlysanestudios.partlysaneskies.utils.SystemUtils.log
import me.partlysanestudios.partlysaneskies.utils.geometry.vectors.Point3d
import net.minecraft.client.gui.inventory.GuiChest
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.apache.logging.log4j.Level
import org.lwjgl.opengl.GL11
import java.awt.Color

object DebugKey {
    fun init() {
        config.debugMode = false
    }

    fun isDebugMode(): Boolean = config.debugMode

    // Runs when debug key is pressed
    fun onDebugKeyPress() {
        config.debugMode = !config.debugMode
        sendClientMessage("Debug mode: " + isDebugMode())

        if (config.debugRenderTestBanner) {
            renderNewBanner(PSSBanner("Test", 5000L, 5f, OneColor(255, 0, 255, 1).toJavaColor()))
        }

        if (config.debugAddSlacker) {
            PlayerRating.rackPoints("FlagTheSlacker", "Debug Slacker")
        }

        if (config.debugSpawnWaypoint) {
            val originalPos = minecraft.thePlayer.position
            val modifiedPoint = Point3d(originalPos.x - 1.0, originalPos.y.toDouble(), originalPos.z - 1.0)
            waypointPoint = modifiedPoint
        }

        if (config.debugSendSystemNotification) {
            SystemNotification.showNotification("Debug mode: ${isDebugMode()}")
        }

        if (config.percyMode) {
            Thread {
                sendClientMessage("Dumping...")
                PercyMode.dump()
            }.start()
        }

        if (config.debugPrintCurrentLocationFromIslandType) {
            sendClientMessage("Island Type: ${HypixelUtils.currentIsland}")
        }

        if (config.debugLogCachedF7Puzzles) {
            TerminalWaypoints.logCachedPuzzles()
        }

        if (config.debugPrintCurrentCachedStats) {
            sendClientMessage(
                "Health: ${StatsData.currentHealth}/${StatsData.maxHealth}, Defense: ${StatsData.defense}, Mana: ${StatsData.currentMana}/${StatsData.maxMana}",
            )
        }

        if (config.debugRenderRNGBanner) {
            DropBannerDisplay.dropToRender = Drop("Test Name", "Test Category", Color.MAGENTA, Rarity.UNOBTAINABLE, 69, time)
        }
        if (config.debugCylinder) {
            cylinderPoint = Point3d.atPlayer()
        }

        if (config.debugSendDiscordWebhook) {
            ExampleWebhook.trigger()
        }
        if (config.debugScanCrystalHollowsCrystals) {
            Thread {
                CrystalHollowsGemstoneMapper.scanWorld()
            }.start()
        }

        if (config.debugConvertScanToPrettyData) {
            Thread {
                CrystalHollowsGemstoneMapper.getPrettyData()
            }.start()
        }

        if (config.debugConvertPrettyDataToNoNucleus) {
            Thread {
                CrystalHollowsGemstoneMapper.removeNucleusCords()
            }.start()
        }
        if (config.debugLogDisplaySize) {
            val width = UResolution.scaledWidth
            val height = UResolution.scaledHeight
            sendClientMessage("$width, $height")
            val window = Window(ElementaVersion.V5)
            val windowWidth = window.getWidth()
            val windowHeight = window.getHeight()
            sendClientMessage("$windowWidth, $windowHeight")
        }

        if (config.testDevEnv) {
            if (minecraft.currentScreen !is GuiChest) {
                sendClientMessage("Not Chest")
            } else {
                val chest = minecraft.currentScreen as GuiChest
                sendClientMessage("Inventory: ${chest.containerInventory.getItemstackList()}")
                sendClientMessage("xSize: ${chest.xSize}")
                sendClientMessage("ySize: ${chest.ySize}")
            }
        }
    }

    // Runs chat analyzer for debug mode
    @SubscribePSSEvent
    fun onChat(event: PSSChatEvent) {
        if (isDebugMode() && config.debugChatAnalyser) {
            log(Level.INFO, event.message)
            copyStringToClipboard(event.message)
        }
    }

    private var waypointPoint = Point3d(0.0, 0.0, 0.0)

    @SubscribePSSEvent
    fun onWaypointRender(event: RenderWaypointEvent) {
        if (isDebugMode() && config.debugSpawnWaypoint) {
            event.pipeline.add(Waypoint("Debug Waypoint", waypointPoint.toBlockPosInt()))
        }
    }

    private var cylinderPoint = Point3d(0.0, 0.0, 0.0)

    @SubscribeEvent
    fun onWorldRenderLast(event: RenderWorldLastEvent) {
        if (!(isDebugMode() && config.debugCylinder)) {
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
        GlStateManager.color(
            ThemeManager.accentColor.toJavaColor().red / 255f,
            ThemeManager.accentColor.toJavaColor().green / 255f,
            ThemeManager.accentColor.toJavaColor().blue / 255f,
            (ThemeManager.accentColor.toJavaColor().alpha / 255f) * .667f,
        )
        worldRenderer.drawCylinderFill(cylinderPoint, 8.0, 20.0)
        tessellator.draw()

        worldRenderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION)
        GlStateManager.color(1.0F, 0.0F, 0.0F, 1.0F)
        worldRenderer.drawCylinderOutline(cylinderPoint, 8.0, 20.0)
        tessellator.draw()

        GlStateManager.resetColor()
        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()
        GlStateManager.enableDepth()

        GlStateManager.popMatrix()
    }
}
