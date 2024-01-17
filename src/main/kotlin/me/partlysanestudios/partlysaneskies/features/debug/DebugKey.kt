//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.features.debug

import cc.polyfrost.oneconfig.config.core.OneColor
import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.features.dungeons.playerrating.PlayerRating
import me.partlysanestudios.partlysaneskies.gui.hud.BannerRenderer.renderNewBanner
import me.partlysanestudios.partlysaneskies.gui.hud.PSSBanner
import me.partlysanestudios.partlysaneskies.system.SystemNotification
import me.partlysanestudios.partlysaneskies.utils.ChatUtils.sendClientMessage
import me.partlysanestudios.partlysaneskies.utils.SystemUtils.log
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.apache.logging.log4j.Level

object DebugKey {
    fun init() {
        PartlySaneSkies.config.debugMode = false
    }


    fun isDebugMode(): Boolean {
        return PartlySaneSkies.config.debugMode
    }

    // Runs when debug key is pressed
    fun onDebugKeyPress() {
        PartlySaneSkies.config.debugMode = !PartlySaneSkies.config.debugMode
        sendClientMessage("Debug mode: " + isDebugMode())


        if (PartlySaneSkies.config.debugRenderTestBanner) {
            renderNewBanner(PSSBanner("Test", 5000L, 5f, OneColor(255, 0, 255, 1).toJavaColor()))
        }

        if (PartlySaneSkies.config.debugAddSlacker) {
            PlayerRating.rackPoints("FlagTheSlacker", "Debug Slacker")
        }

        if (PartlySaneSkies.config.debugSendSystemNotification) {
            SystemNotification.showNotification("Debug mode: ${isDebugMode()}")
        }
    }

    // Runs chat analyzer for debug mode
    @SubscribeEvent
    fun chatAnalyzer(evnt: ClientChatReceivedEvent) {
        if (isDebugMode() && PartlySaneSkies.config.debugChatAnalyser) {
            log(Level.INFO, evnt.message.formattedText)
        }
    }
}