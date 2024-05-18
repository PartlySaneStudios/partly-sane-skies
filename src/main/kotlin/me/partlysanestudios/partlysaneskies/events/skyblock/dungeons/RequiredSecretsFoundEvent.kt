//
// Written by J10a1n15 and Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.events.skyblock.dungeons

import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.data.skyblockdata.IslandType
import me.partlysanestudios.partlysaneskies.events.EventManager
import me.partlysanestudios.partlysaneskies.events.SubscribePSSEvent
import me.partlysanestudios.partlysaneskies.utils.HypixelUtils
import me.partlysanestudios.partlysaneskies.utils.MinecraftUtils

class RequiredSecretsFoundEvent {
    companion object {
        private var alreadySendThisRun = false
        private var lastCheckTime = PartlySaneSkies.time

        @SubscribePSSEvent
        fun onDungeonStart(event: DungeonStartEvent?) {
            alreadySendThisRun = false
        }

        internal fun tick(functionList: List<EventManager.EventFunction>) {
            if (!HypixelUtils.isSkyblock()) {
                return
            }
            if (!IslandType.CATACOMBS.onIsland()) {
                return
            }
            if (alreadySendThisRun) {
                return
            }
            if (lastCheckTime + 100 > PartlySaneSkies.time) { //checks every 100ms
                return
            }
            lastCheckTime = PartlySaneSkies.time

            for (line in MinecraftUtils.getTabList()) {
                if (line.contains("Secrets Found: §r§a")) {
                    for (function in functionList) {
                        try {
                            function.function.call(function.obj, RequiredSecretsFoundEvent())
                        } catch (exception: Exception) {
                            exception.printStackTrace()
                        }
                    }
                    alreadySendThisRun = true
                    break
                }
            }
        }
    }
}