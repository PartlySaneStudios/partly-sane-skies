package me.partlysanestudios.partlysaneskies.data.skyblockdata

/**
 * An Enum class for all rarities in Hypixel Skyblock
 * @param order: an integer signifying the rarity of the pet to be able to compare using operators
 * @param colorCode: the color code relating to that rarity
 * @param displayName: a prettier version of the enum name used for display
 */
enum class Rarity(val order: Int, val colorCode: String, val displayName: String): Comparable<Rarity> {
    UNKNOWN(-1, "", "Unknown"),
    COMMON(0, "§f", "Common"),
    UNCOMMON(1, "§a", "Uncommon"),
    RARE(2, "§9", "Rare"),
    EPIC(3, "§5", "Epic"),
    LEGENDARY(4, "§6", "Legendary"),
    MYTHIC(5, "§d", "Mythic"),
    DIVINE(6, "§b", "Divine"),
    SUPREME(7, "§4", "Supreme"),
    SPECIAL(8, "§c", "Special"),
    VERY_SPECIAL(9, "§c", "Very Special");

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