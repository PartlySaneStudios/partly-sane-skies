//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.features.debug

import cc.polyfrost.oneconfig.config.core.OneColor
import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.data.skyblockdata.IslandType
import me.partlysanestudios.partlysaneskies.features.dungeons.playerrating.PlayerRating
import me.partlysanestudios.partlysaneskies.render.gui.hud.BannerRenderer.renderNewBanner
import me.partlysanestudios.partlysaneskies.render.gui.hud.PSSBanner
import me.partlysanestudios.partlysaneskies.render.waypoint.Waypoint
import me.partlysanestudios.partlysaneskies.render.waypoint.WaypointManager
import me.partlysanestudios.partlysaneskies.system.SystemNotification
import me.partlysanestudios.partlysaneskies.utils.ChatUtils
import me.partlysanestudios.partlysaneskies.utils.ChatUtils.sendClientMessage
import me.partlysanestudios.partlysaneskies.utils.HypixelUtils
import me.partlysanestudios.partlysaneskies.utils.SystemUtils.log
import net.minecraft.util.BlockPos
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.apache.logging.log4j.Level

object DebugKey {
    fun init() {
        PartlySaneSkies.config.dev.debugMode = false
    }


    fun isDebugMode(): Boolean {
        return PartlySaneSkies.config.dev.debugMode
    }

    // Runs when debug key is pressed
    fun onDebugKeyPress() {
        PartlySaneSkies.config.dev.debugMode = !PartlySaneSkies.config.dev.debugMode
        sendClientMessage("Debug mode: " + isDebugMode())


        if (PartlySaneSkies.config.dev.debugRenderTestBanner) {
            renderNewBanner(PSSBanner("Test", 5000L, 5f, OneColor(255, 0, 255, 1).toJavaColor()))
        }

        if (PartlySaneSkies.config.dev.debugAddSlacker) {
            PlayerRating.rackPoints("FlagTheSlacker", "Debug Slacker")
        }
        if (PartlySaneSkies.config.dev.debugSpawnWaypoint) {
            val originalPos = PartlySaneSkies.minecraft.thePlayer.position
            val modifiedPos = BlockPos(originalPos.x - 1, originalPos.y, originalPos.z - 1)

            if (PartlySaneSkies.config.dev.debugMode) {
                WaypointManager.addWaypoint(
                    Waypoint(
                        "Debug Waypoint",
                        modifiedPos
                    )
                )
            } else {
                WaypointManager.removeWaypoint(
                    Waypoint(
                        "Debug Waypoint",
                        modifiedPos
                    )
                )
            }
        }
        if (PartlySaneSkies.config.dev.debugSendSystemNotification) {
            SystemNotification.showNotification("Debug mode: ${isDebugMode()}")
        }

        if (PartlySaneSkies.config.dev.percyMode) {
            Thread() {
                sendClientMessage("Dumping...")
                PercyMode.dump()

            }.start()
        }
        if (PartlySaneSkies.config.dev.debugPrintCurrentLocationFromIslandType) {
            sendClientMessage("Island Type: ${IslandType.getCurrentIsland()}")
        }
    }

    // Runs chat analyzer for debug mode
    @SubscribeEvent
    fun chatAnalyzer(evnt: ClientChatReceivedEvent) {
        if (isDebugMode() && PartlySaneSkies.config.dev.debugChatAnalyser) {
            log(Level.INFO, evnt.message.formattedText)
        }
    }
}