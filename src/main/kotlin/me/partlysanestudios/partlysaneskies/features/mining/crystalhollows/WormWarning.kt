//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.features.mining.crystalhollows

import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.config
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.minecraft
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.time
import me.partlysanestudios.partlysaneskies.events.SubscribePSSEvent
import me.partlysanestudios.partlysaneskies.events.minecraft.PSSChatEvent
import me.partlysanestudios.partlysaneskies.render.gui.hud.BannerRenderer.renderNewBanner
import me.partlysanestudios.partlysaneskies.render.gui.hud.PSSBanner

object WormWarning {
    private var wormWarningString = ""
    private var wormWarningBannerTime: Long = 0

    @SubscribePSSEvent
    fun onChat(event: PSSChatEvent) {
        if (event.component.unformattedText.startsWith("You hear the sound of something approaching...")) {
            if (config.wormWarningBanner) {
                wormWarningBannerTime = time
                wormWarningString = "A Worm Has Spawned!"
                renderNewBanner(
                    PSSBanner(
                        wormWarningString,
                        (config.wormWarningBannerTime * 1000).toLong(),
                        3f,
                        config.wormWarningBannerColor.toJavaColor(),
                    ),
                )
            }
            if (config.wormWarningBannerSound) {
                minecraft.thePlayer.playSound("partlysaneskies:bell", 100f, 1f)
            }
        }
    }
}
