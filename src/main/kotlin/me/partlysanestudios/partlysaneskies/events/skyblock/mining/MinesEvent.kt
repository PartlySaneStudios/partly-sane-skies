//
// Written by J10a1n15.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.events.skyblock.mining

import me.partlysanestudios.partlysaneskies.events.EventManager
import me.partlysanestudios.partlysaneskies.features.mining.events.MiningEvent
import me.partlysanestudios.partlysaneskies.utils.HypixelUtils.inAdvancedMiningIsland

class MinesEvent(val miningEvent: MiningEvent) {
    companion object {
        internal fun onMessageReceived(functionList: List<EventManager.EventFunction>, formattedMessage: String) {
            if (!inAdvancedMiningIsland()) return

            MiningEvent.entries
                .firstOrNull { it.triggeredEvent(formattedMessage) }
                ?.let { event ->
                    for (function in functionList) {
                        try {
                            function.function.call(function.obj, MinesEvent(event))
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
        }
    }
}
