//
// Written by J10a1n15.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.features.mining.events

import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.config
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.minecraft
import me.partlysanestudios.partlysaneskies.api.events.PSSEvent
import me.partlysanestudios.partlysaneskies.events.minecraft.PSSChatEvent
import me.partlysanestudios.partlysaneskies.events.skyblock.mining.MinesEvent
import me.partlysanestudios.partlysaneskies.render.gui.hud.BannerRenderer.renderNewBanner
import me.partlysanestudios.partlysaneskies.render.gui.hud.PSSBanner
import me.partlysanestudios.partlysaneskies.system.SystemNotification.showNotification
import me.partlysanestudios.partlysaneskies.utils.HypixelUtils.inAdvancedMiningIsland
import me.partlysanestudios.partlysaneskies.utils.StringUtils.removeColorCodes
import org.lwjgl.opengl.Display

object MiningEventNotifier {

    @PSSEvent.Subscribe
    fun onMiningEvent(event: MinesEvent) {
        if (!config.miningEventsToggle) return

        if (event.miningEvent.config().not()) return

        minecraft.thePlayer.playSound("partlysaneskies:bell", 100F, 1F)
        val text = event.miningEvent.color + event.miningEvent.event
        if (config.miningSendSystemNotifications && !Display.isActive()) {
            showNotification(text.removeColorCodes())
        }
        if (config.miningShowEventBanner) {
            renderNewBanner(PSSBanner(text, (config.miningEventBannerTime * 1000).toLong(), 4f))
        }
    }

    @PSSEvent.Subscribe
    fun onChat(event: PSSChatEvent) {
        if (!inAdvancedMiningIsland()) return

        MiningEvent.entries
            .firstOrNull { it.triggeredEvent(event.message) }
            ?.let { MinesEvent(it).post() }
    }
}
