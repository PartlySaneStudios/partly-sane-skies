//
// Written by J10a1n15.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.events.skyblock.mining

import me.partlysanestudios.partlysaneskies.events.EventManager
import me.partlysanestudios.partlysaneskies.features.mining.events.MiningEvent
import me.partlysanestudios.partlysaneskies.utils.ChatUtils
import me.partlysanestudios.partlysaneskies.utils.HypixelUtils.inAdvancedMiningIsland

class MinesEvent(val miningEvent: MiningEvent) {
    companion object {
        internal fun onMessageRecieved(functionList: List<EventManager.EventFunction>, formattedMessage: String) {
            if (!inAdvancedMiningIsland()) return

            MiningEvent.entries.forEach {
                if (it.triggeredEvent(formattedMessage)) {
                    ChatUtils.sendClientMessage("Triggered event: ${it.event}")
                    for (function in functionList) {
                        try {
                            function.function.call(function.obj, it)
                        } catch (exception: Exception) {
                            exception.printStackTrace()
                        }
                    }
                }

            }
        }
    }
}