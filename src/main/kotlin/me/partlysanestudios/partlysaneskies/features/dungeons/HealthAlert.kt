//
// Written by FlagMaster and Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.features.dungeons

import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.config
import me.partlysanestudios.partlysaneskies.config.OneConfigScreen
import me.partlysanestudios.partlysaneskies.data.cache.StatsData
import me.partlysanestudios.partlysaneskies.data.skyblockdata.IslandType
import me.partlysanestudios.partlysaneskies.render.gui.hud.BannerRenderer
import me.partlysanestudios.partlysaneskies.render.gui.hud.PSSBanner
import me.partlysanestudios.partlysaneskies.utils.MathUtils
import me.partlysanestudios.partlysaneskies.utils.MinecraftUtils
import me.partlysanestudios.partlysaneskies.utils.StringUtils.removeColorCodes
import net.minecraft.client.audio.PositionedSoundRecord
import net.minecraft.util.ResourceLocation
import java.awt.Color


object HealthAlert {
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

    private fun alertWhenPlayerLowOnHealth(): Boolean {
        if (!config.alertOutsideDungeons && !IslandType.CATACOMBS.onIsland()){
            return false
        }

        val healthPercent = if (config.colouredHealerAlert == 1) {
            .25
        } else {
            .5
        }

        return if (StatsData.currentHealth / StatsData.maxHealth < healthPercent) {
            true
        } else {
            false
        }
    }

    fun checkPlayerTick() {
        var warn = false
        if (isPlayerLowOnHealth() && OneConfigScreen.healerAlert) {
            lastWarnTime = PartlySaneSkies.time
            BannerRenderer.renderNewBanner(PSSBanner("A player in your party is low", 3500, color = config.partyMemberLowColor.toJavaColor()))
            PartlySaneSkies.minecraft.soundHandler
                .playSound(PositionedSoundRecord.create(ResourceLocation("partlysaneskies", "bell")))
            if (MathUtils.onCooldown(
                    lastWarnTime,
                    (config.healerAlertCooldownSlider * 1000).toLong()
                )
            ) {
                return
            }
        }

        if (alertWhenPlayerLowOnHealth() && config.alertWhenPlayerLow) {
            lastWarnTime = PartlySaneSkies.time
            BannerRenderer.renderNewBanner(PSSBanner("Your health is low", 3500, color = config.playerLowColor.toJavaColor()))
            PartlySaneSkies.minecraft.soundHandler
                .playSound(PositionedSoundRecord.create(ResourceLocation("partlysaneskies", "bell")))
            if (MathUtils.onCooldown(
                    lastWarnTime,
                    (config.healerAlertCooldownSlider * 1000).toLong()
                )
            ) {
                return
            }
        }
    }
}
