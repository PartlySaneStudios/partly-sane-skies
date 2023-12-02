package me.partlysanestudios.partlysaneskies.dungeons.healeralert

import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.utils.IslandType
import me.partlysanestudios.partlysaneskies.utils.MinecraftUtils


object HealerAlert {
    private fun isPlayerLowOnHealth(): Boolean {
        if (!IslandType.CATACOMBS.onIsland()) {
            return false
        }


        val scoreBoard = MinecraftUtils.getScoreboardLines()
        for (line in scoreBoard) {
            if (line[0] !='[') {
                continue


            }
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
    public fun run() {
        if (!PartlySaneSkies.config.healerAlert){
            return}
    }
}
