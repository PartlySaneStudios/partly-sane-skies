//
// Written by FlagMaster with help from Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.features.dungeons

import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.data.skyblockdata.IslandType
import me.partlysanestudios.partlysaneskies.render.gui.hud.BannerRenderer
import me.partlysanestudios.partlysaneskies.render.gui.hud.PSSBanner
import me.partlysanestudios.partlysaneskies.utils.MathUtils
import me.partlysanestudios.partlysaneskies.utils.MinecraftUtils
import me.partlysanestudios.partlysaneskies.utils.StringUtils.removeColorCodes
import net.minecraft.client.audio.PositionedSoundRecord
import net.minecraft.util.ResourceLocation
import java.awt.Color


object HealerAlert {
    private var lastWarnTime = 0L

    private fun isPlayerLowOnHealth(): Boolean {
        if (!IslandType.CATACOMBS.onIsland()) {
            return false
        }


        val scoreBoard = MinecraftUtils.getScoreboardLines()
        for (line in scoreBoard) {
            if (line.removeColorCodes()[0] != '[') {
                continue
            }

            // TODO: do with regex
            val indexOfFirstSpace = line.indexOf(" ")
            val indexOfSecondSpace = line.indexOf(" ", indexOfFirstSpace + 1)
            val health = line.substring(indexOfSecondSpace)
            if (PartlySaneSkies.config.colouredHealerAlert == 1) {
                return health.contains("§e") || health.indexOf("§c") != health.lastIndexOf("§c")
            }
            return health.indexOf("§c") != health.lastIndexOf("§c")

        }
        return false
    }

    fun run() {
        if (!PartlySaneSkies.config.healerAlert) {
            return
        }
        if (isPlayerLowOnHealth()) {
            if (MathUtils.onCooldown(
                    lastWarnTime,
                    (PartlySaneSkies.config.healerAlertCooldownSlider * 1000).toLong()
                )
            ) {
                return
            }
            lastWarnTime = PartlySaneSkies.time
            BannerRenderer.renderNewBanner(PSSBanner("A player is low", 3500, color = Color.RED))
            PartlySaneSkies.minecraft.soundHandler
                .playSound(PositionedSoundRecord.create(ResourceLocation("partlysaneskies", "bell")))
        }
    }
}
