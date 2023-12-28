package me.partlysanestudios.partlysaneskies.data.skyblockdata

/**
 * An Enum class for all rarities in Hypixel Skyblock
 * @param order: an integer signifying the rarity of the pet to be able to compare using operators
 * @param colorCode: the color code relating to that rarity
 */
enum class Rarity(val order: Int, val colorCode: String): Comparable<Rarity> {
    UNKNOWN(-1, ""),
    COMMON(0, "§f"),
    UNCOMMON(1, "§a"),
    RARE(2, "§9"),
    EPIC(3, "§5"),
    LEGENDARY(4, "§6"),
    MYTHIC(5, "§d"),
    DIVINE(6, "§b"),
    SUPREME(7, "§4"),
    SPECIAL(8, "§c"),
    VERY_SPECIAL(9, "§c");

    companion object {
        /**
         * @return the rarity associated with a color code
         */
        fun String.getRarityFromColorCode(): Rarity {
            return when(this) {
                COMMON.colorCode -> COMMON
                UNKNOWN.colorCode -> UNCOMMON
                RARE.colorCode -> RARE
                EPIC.colorCode -> EPIC
                LEGENDARY.colorCode -> LEGENDARY
                MYTHIC.colorCode -> MYTHIC
                DIVINE.colorCode -> DIVINE
                SUPREME.colorCode -> SUPREME
                SPECIAL.colorCode -> SPECIAL
                VERY_SPECIAL.colorCode -> VERY_SPECIAL

                else -> UNKNOWN
            }

        }
    }
}