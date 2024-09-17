//
// Written by J10a1n15 and Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.features.dungeons

import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.config
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.minecraft
import me.partlysanestudios.partlysaneskies.api.events.PSSEvent
import me.partlysanestudios.partlysaneskies.data.skyblockdata.IslandType
import me.partlysanestudios.partlysaneskies.events.minecraft.TablistUpdateEvent
import me.partlysanestudios.partlysaneskies.events.skyblock.dungeons.DungeonStartEvent
import me.partlysanestudios.partlysaneskies.render.gui.hud.BannerRenderer.renderNewBanner
import me.partlysanestudios.partlysaneskies.render.gui.hud.PSSBanner
import me.partlysanestudios.partlysaneskies.utils.HypixelUtils.isSkyblock
import net.minecraft.client.Minecraft
import net.minecraft.client.audio.PositionedSoundRecord
import net.minecraft.util.ResourceLocation

object RequiredSecretsFound {
    private var alreadySendThisRun = false

    @PSSEvent.Subscribe
    fun onDungeonStart(event: DungeonStartEvent) {
        alreadySendThisRun = false
    }

    @PSSEvent.Subscribe
    fun onTablistUpdate(event: TablistUpdateEvent) {
        if (!isSkyblock()) return
        if (!IslandType.CATACOMBS.onIsland()) return

        if (alreadySendThisRun) return

        if (event.list.none { it.contains("Secrets Found: §r§a") }) return

        if (config.secretsBanner) {
            renderNewBanner(
                PSSBanner(
                    "Required Secrets Found!",
                    (config.secretsBannerTime * 1000).toLong(),
                    3.0f,
                    config.secretsBannerColor.toJavaColor(),
                ),
            )
        }
        if (config.secretsChatMessage) {
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/pc " + config.secretsChatMessageString)
        }
        if (config.secretsSound) {
            minecraft.soundHandler.playSound(
                if (config.secretsAirRaidSiren) {
                    PositionedSoundRecord.create(ResourceLocation("partlysaneskies", "airraidsiren"))
                } else {
                    PositionedSoundRecord.create(ResourceLocation("partlysaneskies", "bell"))
                },
            )
        }
        alreadySendThisRun = true
    }
}
