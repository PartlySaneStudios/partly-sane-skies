package me.partlysanestudios.partlysaneskies.features.items.rngdrop

import me.partlysanestudios.partlysaneskies.PartlySaneSkies
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
        get() = timeDropped + displayTime < PartlySaneSkies.time
}
