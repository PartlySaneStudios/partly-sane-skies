//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.features.gui.hud.rngdropbanner

import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.time
import java.awt.Color

class Drop(
    var name: String,
    var dropCategory: String,
    var dropCategoryColor: Color,
    var dropRarityColorCode: String,
    var magicFind: Int,
    var timeDropped: Long,
) {
    var displayTime = 0

    val isStillDisplay: Boolean
        get() = timeDropped + displayTime < time
}
