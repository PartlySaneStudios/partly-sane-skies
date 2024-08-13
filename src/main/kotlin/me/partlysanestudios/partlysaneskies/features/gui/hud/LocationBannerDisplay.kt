//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.features.gui.hud

import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.config
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.time
import me.partlysanestudios.partlysaneskies.render.gui.hud.BannerRenderer.renderNewBanner
import me.partlysanestudios.partlysaneskies.render.gui.hud.PSSBanner
import me.partlysanestudios.partlysaneskies.utils.HypixelUtils.getRegionName
import me.partlysanestudios.partlysaneskies.utils.StringUtils.colorCodeToColor
import me.partlysanestudios.partlysaneskies.utils.StringUtils.removeColorCodes
import me.partlysanestudios.partlysaneskies.utils.StringUtils.stripLeading
import me.partlysanestudios.partlysaneskies.utils.StringUtils.stripTrailing
import java.awt.Color
import java.util.Locale

object LocationBannerDisplay {
    private var TEXT_SCALE = 5f
    private var lastLocation = ""
    private var lastLocationTime = time
    private var displayString = "empty"
    var color: Color = Color.white

    fun checkLocationTick() {
        if (!config.locationBannerDisplay) return
        val regionName = getRegionName()
        var noColorCodeRegionName = regionName.removeColorCodes()
        if (checkExpire()) {
            displayString = ""
        }
        if (noColorCodeRegionName.isEmpty()) {
            return
        }
        noColorCodeRegionName = stripLeading(noColorCodeRegionName)
        noColorCodeRegionName = stripTrailing(noColorCodeRegionName)
        noColorCodeRegionName = noColorCodeRegionName.replace("\\P{Print}".toRegex(), "") // Removes the RANDOM EMOJIS
        // THAT ARE PRESENT IN SKY-BLOCK LOCATIONS
        // LOOK AT THIS:
        // The Catac🔮ombs (F5)
        // The Catac👽ombs (F5)
        // The Catac🔮ombs (F5)
        // Dungeon H👾ub
        // Mountain⚽
        // Village⚽
        // Coal Mine⚽
        // THEY'RE NOT EVEN VISIBLE IN MINECRAFT - Su386
        // (ITS NOT SPELLED VISABLE - j10a)
        // (It's* - Su386)
        if (lastLocation == noColorCodeRegionName) {
            return
        }
        if (noColorCodeRegionName.lowercase(Locale.getDefault()).contains("none")) {
            return
        }
        if (regionName.isNotEmpty()) {
            color = regionName.substring(3, 5).colorCodeToColor()
        }
        if (color == Color.white) {
            color = Color(170, 170, 170)
        }
        displayString = noColorCodeRegionName
        lastLocation = noColorCodeRegionName
        lastLocationTime = time
        renderNewBanner(PSSBanner(displayString, (config.locationBannerTime * 1000).toLong(), TEXT_SCALE, color))
    }

    private fun checkExpire(): Boolean = timeSinceLastChange > config.locationBannerTime * 1000

    private val timeSinceLastChange: Long
        get() = time - lastLocationTime
}
