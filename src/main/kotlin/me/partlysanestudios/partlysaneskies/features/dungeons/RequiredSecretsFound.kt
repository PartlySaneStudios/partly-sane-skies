//
// Written by J10a1n15 and Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.features.dungeons

import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.config
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.minecraft
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.time
import me.partlysanestudios.partlysaneskies.data.skyblockdata.IslandType
import me.partlysanestudios.partlysaneskies.events.SubscribePSSEvent
import me.partlysanestudios.partlysaneskies.events.skyblock.dungeons.DungeonStartEvent
import me.partlysanestudios.partlysaneskies.render.gui.hud.BannerRenderer.renderNewBanner
import me.partlysanestudios.partlysaneskies.render.gui.hud.PSSBanner
import me.partlysanestudios.partlysaneskies.utils.HypixelUtils.isSkyblock
import me.partlysanestudios.partlysaneskies.utils.MinecraftUtils.getTabList
import net.minecraft.client.Minecraft
import net.minecraft.client.audio.PositionedSoundRecord
import net.minecraft.util.ResourceLocation

object RequiredSecretsFound {

    private var alreadySendThisRun = false
    private var lastCheckTime = time

    @SubscribePSSEvent
    fun onDungeonStart(event: DungeonStartEvent?) {
        alreadySendThisRun = false
    }

    fun tick() {
        if (!isSkyblock()) {
            return
        }
        if (!IslandType.CATACOMBS.onIsland()) {
            return
        }
        if (alreadySendThisRun) {
            return
        }
        if (lastCheckTime + 100 > time) { //checks every 100ms
            return
        }
        lastCheckTime = time

        for (line in getTabList()) {
            if (line.contains("Secrets Found: §r§a")) {
                if (config.secretsBanner) {
                    renderNewBanner(
                        PSSBanner(
                            "Required Secrets Found!",
                            (config.secretsBannerTime * 1000).toLong(),
                            3.0f,
                            config.secretsBannerColor.toJavaColor()
                        )
                    )
                }
                if (config.secretsChatMessage) {
                    Minecraft.getMinecraft().thePlayer.sendChatMessage("/pc " + config.secretsChatMessageString)
                }
                if (config.secretsSound) {
                    if (config.secretsAirRaidSiren) {
                        minecraft.soundHandler.playSound(PositionedSoundRecord.create(ResourceLocation("partlysaneskies", "airraidsiren")))
                    } else {
                        minecraft.soundHandler.playSound(PositionedSoundRecord.create(ResourceLocation("partlysaneskies", "bell")))
                    }
                }
                alreadySendThisRun = true
                break
            }
        }
    }
}