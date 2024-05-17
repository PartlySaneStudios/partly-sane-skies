//
// Written by J10a1n15.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.features.mining.events

import cc.polyfrost.oneconfig.utils.dsl.mc
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.config
import me.partlysanestudios.partlysaneskies.render.gui.hud.BannerRenderer.renderNewBanner
import me.partlysanestudios.partlysaneskies.render.gui.hud.PSSBanner
import me.partlysanestudios.partlysaneskies.system.SystemNotification.showNotification
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.lwjgl.opengl.Display

object MiningEventNotifier {

    @SubscribeEvent
    fun onChat(event: ClientChatReceivedEvent) {
        if (!config.miningEventsToggle) return

        val message = event.message.formattedText

        MiningEvent.entries.forEach {
            if (it.config().not()) return@forEach

            if (it.triggeredEvent(message)) {
                mc.thePlayer.playSound("partlysaneskies:bell", 100F, 1F)
                val text = it.color + it.event
                if (config.miningShowEventBanner && !Display.isActive()) {
                    showNotification(text)
                }
                if (config.miningShowEventBanner) renderNewBanner(PSSBanner(text, (config.miningEventBannerTime * 1000).toLong(), 4f))
            }
            if (it.triggeredEvent20s(message) && config.miningWarn20sBeforeEvent) {
                mc.thePlayer.playSound("partlysaneskies:bell", 100F, 1F)
                val text = it.color + it.event20s
                if (config.miningShowEventBanner && !Display.isActive()) {
                    showNotification(text)
                }
                if (config.miningShowEventBanner) {
                    renderNewBanner(PSSBanner(text, (config.miningEventBannerTime * 1000).toLong(), 4f))
                 }
            }
        }
    }
}
