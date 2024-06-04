//
// Written by J10a1n15.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.features.mining.events

import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.config
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.minecraft
import me.partlysanestudios.partlysaneskies.events.SubscribePSSEvent
import me.partlysanestudios.partlysaneskies.events.skyblock.mining.MinesEvent
import me.partlysanestudios.partlysaneskies.render.gui.hud.BannerRenderer.renderNewBanner
import me.partlysanestudios.partlysaneskies.render.gui.hud.PSSBanner
import me.partlysanestudios.partlysaneskies.system.SystemNotification.showNotification
import org.lwjgl.opengl.Display

object MiningEventNotifier {

    @SubscribePSSEvent
    fun onMiningEvent(event: MinesEvent) {
        if (!config.miningEventsToggle) return

        if (event.miningEvent.config().not()) return

        minecraft.thePlayer.playSound("partlysaneskies:bell", 100F, 1F)
        val text = event.miningEvent.color + event.miningEvent.event
        if (config.miningShowEventBanner && !Display.isActive()) {
            showNotification(text)
        }
        if (config.miningShowEventBanner) {
            renderNewBanner(PSSBanner(text, (config.miningEventBannerTime * 1000).toLong(), 4f))
        }
    }
}
