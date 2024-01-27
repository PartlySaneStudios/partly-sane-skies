//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.features.debug

import cc.polyfrost.oneconfig.config.core.OneColor
import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.config
import me.partlysanestudios.partlysaneskies.data.cache.StatsData
import me.partlysanestudios.partlysaneskies.data.skyblockdata.IslandType
import me.partlysanestudios.partlysaneskies.events.SubscribePSSEvent
import me.partlysanestudios.partlysaneskies.events.minecraft.render.RenderWaypointEvent
import me.partlysanestudios.partlysaneskies.features.dungeons.TerminalWaypoints
import me.partlysanestudios.partlysaneskies.features.dungeons.playerrating.PlayerRating
import me.partlysanestudios.partlysaneskies.render.gui.hud.BannerRenderer.renderNewBanner
import me.partlysanestudios.partlysaneskies.render.gui.hud.PSSBanner
import me.partlysanestudios.partlysaneskies.render.waypoint.Waypoint
import me.partlysanestudios.partlysaneskies.system.SystemNotification
import me.partlysanestudios.partlysaneskies.utils.ChatUtils.sendClientMessage
import me.partlysanestudios.partlysaneskies.utils.SystemUtils.copyStringToClipboard
import me.partlysanestudios.partlysaneskies.utils.SystemUtils.log
import me.partlysanestudios.partlysaneskies.utils.vectors.Point3d
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.apache.logging.log4j.Level

object DebugKey {
    fun init() {
        config.debugMode = false
    }


    fun isDebugMode(): Boolean {
        return config.debugMode
    }

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
            val originalPos = PartlySaneSkies.minecraft.thePlayer.position
            val modifiedPoint = Point3d(originalPos.x - 1.0, originalPos.y.toDouble(), originalPos.z - 1.0)
            waypointPoint = modifiedPoint
        }
        if (config.debugSendSystemNotification) {
            SystemNotification.showNotification("Debug mode: ${isDebugMode()}")
        }

        if (config.percyMode) {
            Thread() {
                sendClientMessage("Dumping...")
                PercyMode.dump()

            }.start()
        }
        if (config.debugPrintCurrentLocationFromIslandType) {
            sendClientMessage("Island Type: ${IslandType.getCurrentIsland()}")
        }

        if (config.debugLogCachedF7Puzzles) {
            TerminalWaypoints.logCachedPuzzles()
        }
        if (config.debugPrintCurrentCachedStats) {
            sendClientMessage("Health: ${StatsData.currentHealth}/${StatsData.maxHealth}, Defense: ${StatsData.defense}, Mana: ${StatsData.currentMana}/${StatsData.maxMana}")
        }
    }

    // Runs chat analyzer for debug mode
    @SubscribeEvent
    fun chatAnalyzer(event: ClientChatReceivedEvent) {
        if (isDebugMode() && config.debugChatAnalyser) {
            log(Level.INFO, event.message.formattedText)
            copyStringToClipboard(event.message.formattedText)
        }
    }

    private var waypointPoint = Point3d(0.0, 0.0, 0.0)
    @SubscribePSSEvent
    fun onWaypointRender(event: RenderWaypointEvent) {

        if (isDebugMode() && config.debugSpawnWaypoint) {
            event.pipeline.add(Waypoint("Debug Waypoint", waypointPoint.toBlockPosInt()))
        }
    }
}