//
// Written by J10a1n15.
// See LICENSE for copyright and license notices.
// Inspired by SkyHanni, https://github.com/hannibal002/SkyHanni/
//

package me.partlysanestudios.partlysaneskies.data.skyblockdata

import me.partlysanestudios.partlysaneskies.utils.HypixelUtils

enum class IslandType(val islandName: String) {
    HUB("Hub"),
    DUNGEON_HUB("Dungeon Hub"),
    PRIVATE_ISLAND("Private Island"),
    GARDEN("Garden"),
    THE_PARK("The Park"),
    SPIDERS_DEN("Spider's Den"),
    CRIMSON_ISLE("Crimson Isle"),
    THE_END("The End"),
    GOLD_MINE("Gold Mine"),
    DEEP_CAVERNS("Deep Caverns"),
    DWARVEN_MINES("Dwarven Mines"),
    CRYSTAL_HOLLOWS("Crystal Hollows"),
    MINESHAFT("Mineshaft"),
    FARMING_ISLAND("The Farming Islands"),
    WINTER_ISLAND("Jerry's Workshop"),
    RIFT("The Rift"),
    CATACOMBS("Catacombs"),
    KUUDRA("Kuudra"),

    NONE(""),
    ;

    fun onIsland() = this == HypixelUtils.currentIsland

    companion object {
        /**
         * @param islandTypes The island types to check for
         * @return True if the player is on any of the specified islands
         */
        fun inAnyIsland(vararg islandTypes: IslandType) = islandTypes.any { it.onIsland() }
    }
}
