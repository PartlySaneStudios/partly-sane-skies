//
// Written by J10a1n15 and Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.features.dungeons

import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.config
import me.partlysanestudios.partlysaneskies.api.events.PSSEvent
import me.partlysanestudios.partlysaneskies.commands.PSSCommand
import me.partlysanestudios.partlysaneskies.data.skyblockdata.SkyblockDataManager
import me.partlysanestudios.partlysaneskies.events.skyblock.dungeons.DungeonStartEvent
import me.partlysanestudios.partlysaneskies.utils.ChatUtils
import me.partlysanestudios.partlysaneskies.utils.MinecraftUtils.countItemInInventory
import me.partlysanestudios.partlysaneskies.utils.StringUtils.pluralize
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent

object ItemRefill {
    @PSSEvent.Subscribe
    fun onDungeonStart(event: DungeonStartEvent) {
        if (!config.autoItemRefill) return
        runItemRefill()
    }

    fun registerCommand() {
        PSSCommand("itemrefill")
            .addAlias("refillitems", "ir", "pearlrefill", "refillpearl", "pr")
            .setDescription("Refills your dungeon items.")
            .setRunnable { runItemRefill() }
            .register()
    }

    @SubscribeEvent
    fun checkKeyBinds(event: InputEvent.KeyInputEvent) {
        if (config.itemRefillKeybind.isActive) {
            runItemRefill()
        }
    }

    private fun runItemRefill() {
        Thread {
            val list = mutableMapOf<String, Int>()

            if (config.refillPearls) list["ENDER_PEARL"] = 16
            if (config.refillDecoys) list["DUNGEON_DECOY"] = 64
            if (config.refillSpiritLeaps) list["SPIRIT_LEAP"] = 16
            if (config.refillSuperboomTnt) list["SUPERBOOM_TNT"] = 64

            list.forEach { (item, fallbackMax) ->
                val currentAmount = countItemInInventory(item)
                val maxStackSize = SkyblockDataManager.getItem(item)?.getStackSize() ?: fallbackMax
                val itemName = SkyblockDataManager.getItem(item)?.name ?: ""
                if (currentAmount < maxStackSize) {
                    val diff = maxStackSize - currentAmount
                    ChatUtils.sendClientMessage("Refilling $diff ${itemName.pluralize(diff)}s...")
                    PartlySaneSkies.minecraft.thePlayer.sendChatMessage(
                        "/gfs ${item.lowercase()} $diff",
                    )
                }

                Thread.sleep(2000)
            }
        }.start()
    }
}
