//
// Written by Su386.
// See LICENSE for copyright and license notices.
//
package me.partlysanestudios.partlysaneskies.features.economy

import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.config
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.minecraft
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.time
import me.partlysanestudios.partlysaneskies.render.gui.hud.BannerRenderer.renderNewBanner
import me.partlysanestudios.partlysaneskies.render.gui.hud.PSSBanner
import me.partlysanestudios.partlysaneskies.utils.HypixelUtils.getCoins
import me.partlysanestudios.partlysaneskies.utils.HypixelUtils.isSkyblock
import me.partlysanestudios.partlysaneskies.utils.MinecraftUtils.footer
import me.partlysanestudios.partlysaneskies.utils.StringUtils.removeColorCodes
import net.minecraft.client.audio.PositionedSoundRecord
import net.minecraft.util.ResourceLocation
import java.awt.Color
import java.util.Locale

object NoCookieWarning {
    private const val TEXT_SCALE = 2.5f
    private var displayString = ""
    private var lastWarnTime = 0L

    // Returns 1 if it has a cookie, 0 if it doesn't and -1 if it cannot be
    // determined
    private fun hasBoosterCookie(): Int {
        for (chatComponent in minecraft.ingameGUI.tabList.footer.siblings) {
            if (chatComponent.formattedText
                    .removeColorCodes()
                    .lowercase(Locale.getDefault())
                    .contains("not active! obtain booster cookies")
            ) {
                return 0
            }
        }
        return if (minecraft.ingameGUI.tabList.footer.siblings
                .isEmpty()
        ) {
            -1
        } else {
            1
        }
    }

    private fun hasLotsOfCoins(): Boolean = getCoins() > config.maxWithoutCookie

    private fun warn() {
        lastWarnTime = time
        val color = Color.red
        displayString = "No Booster Cookie. You will lose your coins on death"
        renderNewBanner(PSSBanner(displayString, (config.noCookieWarnTime * 1000).toLong(), TEXT_SCALE, color))
        minecraft.soundHandler.playSound(PositionedSoundRecord.create(ResourceLocation("partlysaneskies", "bell")))
    }

    private val timeSinceLastWarn: Long
        get() = time - lastWarnTime

    private fun checkExpire(): Boolean = timeSinceLastWarn > config.noCookieWarnTime * 1000

    private fun checkWarnAgain(): Boolean =
        if (timeSinceLastWarn > config.noCookieWarnCooldown * 1000) {
            true
        } else {
            false
        }

    fun checkCoinsTick() {
        if (!isSkyblock()) {
            return
        }
        if (!config.noCookieWarning) {
            return
        }
        if (hasBoosterCookie() == 1) {
            return
        }
        if (checkExpire()) displayString = ""
        if (!checkWarnAgain()) {
            return
        }
        if (!hasLotsOfCoins()) {
            return
        }
        warn()
        lastWarnTime = time
    }
}
