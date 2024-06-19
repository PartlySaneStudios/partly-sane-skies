//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.features.gui.hud.rngdropbanner

import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.time
import me.partlysanestudios.partlysaneskies.data.skyblockdata.Rarity
import java.awt.Color

class Drop(
    var name: String,
    var dropCategory: String,
    var dropCategoryColor: Color,
    var dropRarity: Rarity,
    var magicFind: Int,
    var timeDropped: Long,
) {
    var displayTime = 0

    val isStillDisplay: Boolean
        get() = timeDropped + displayTime < time
}
