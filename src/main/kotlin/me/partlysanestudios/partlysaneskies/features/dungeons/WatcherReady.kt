//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.features.dungeons

import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.config
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.minecraft
import me.partlysanestudios.partlysaneskies.render.gui.hud.BannerRenderer.renderNewBanner
import me.partlysanestudios.partlysaneskies.render.gui.hud.PSSBanner
import net.minecraft.client.audio.PositionedSoundRecord
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object WatcherReady {

    @SubscribeEvent
    fun watcherReadyChatEvent(event: ClientChatReceivedEvent) {
        if (event.message.unformattedText.startsWith("[BOSS] The Watcher: That will be enough for now.")) {
            if (config.watcherReadyBanner) {
                renderNewBanner(
                    PSSBanner(
                        "Watcher Ready!",
                        (config.watcherReadyBannerTime * 1000).toLong(),
                        3.0f,
                        config.watcherReadyBannerColor.toJavaColor()
                    )
                )
            }
            if (config.watcherReadyChatMessage) {
                minecraft.thePlayer.sendChatMessage("/pc " + config.watcherChatMessage)
            }
            if (config.watcherReadySound) {
                minecraft.soundHandler.playSound(
                    PositionedSoundRecord.create(
                        ResourceLocation("partlysaneskies", "bell")
                    )
                )
            }
            if (config.watcherReadyAirRaidSiren) {
                minecraft.soundHandler.playSound(
                    PositionedSoundRecord.create(
                        ResourceLocation("partlysaneskies", "airraidsiren")
                    )
                )
            }
        }
    }
}
