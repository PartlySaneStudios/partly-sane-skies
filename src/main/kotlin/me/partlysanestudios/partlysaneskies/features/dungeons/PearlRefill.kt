//
// Written by J10a1n15.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.features.dungeons

import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.config
import me.partlysanestudios.partlysaneskies.commands.PSSCommand
import me.partlysanestudios.partlysaneskies.data.skyblockdata.SkyblockDataManager
import me.partlysanestudios.partlysaneskies.events.SubscribePSSEvent
import me.partlysanestudios.partlysaneskies.events.skyblock.dungeons.DungeonStartEvent
import me.partlysanestudios.partlysaneskies.utils.ChatUtils
import me.partlysanestudios.partlysaneskies.utils.MinecraftUtils.countItemInInventory
import net.minecraft.command.ICommandSender
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent
import java.util.*
import kotlin.collections.HashMap

object PearlRefill {
    @SubscribePSSEvent
    fun onDungeonStart(event: DungeonStartEvent) {
        if (!PartlySaneSkies.config.autoPearlRefill) return
        runItemRefil()
    }

    fun registerCommand() {
        PSSCommand("itemrefill")
            .addAlias("refillitems")
            .addAlias("ir")
            .addAlias("pearlrefill")
            .addAlias("refillpearl")
            .addAlias("pr")
            .setDescription("Refills your ender pearls to 16.")
            .setRunnable { s: ICommandSender, a: Array<String> ->
                runItemRefil()
            }.register()
    }

    @SubscribeEvent
    fun checkKeyBinds(event: InputEvent.KeyInputEvent?) {
        if (config.itemRefillKeybind.isActive()) {
            runItemRefil()
        }

    }

    private fun runItemRefil() {
        Thread() {
            val list = ArrayList<String>()

            if (config.refillPearls) {
                list.add("ENDER_PEARL")
            }
            if (config.refillDecoys) {
                list.add("DUNGEON_DECOY")
            }
            if (config.refillSpiritLeaps) {
                list.add("SPIRIT_LEAP")
            }
            if (config.refillSuperboomTnt) {
                list.add("SUPERBOOM_TNT")
            }

            val itemAmount = HashMap<String, Int>()

            for (item in list) {
                itemAmount[item] = countItemInInventory(item)
            }

            for (en in itemAmount.entries) {
                val maxStackSize = SkyblockDataManager.getItem(en.key)?.getStackSize() ?: 64
                val itemName = SkyblockDataManager.getItem(en.key)?.name ?: ""
                if (en.value < maxStackSize) {
                    ChatUtils.sendClientMessage("Refilling ${maxStackSize - en.value} ${itemName}s...")
                    PartlySaneSkies.minecraft.thePlayer.sendChatMessage("/gfs ${en.key.lowercase(Locale.getDefault())} ${maxStackSize - en.value}")
                }

                Thread.sleep(2000)
            }
        }.start()

    }
}