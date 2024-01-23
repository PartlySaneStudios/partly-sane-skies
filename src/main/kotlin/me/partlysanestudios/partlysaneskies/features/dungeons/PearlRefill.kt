//
// Written by J10a1n15.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.features.dungeons

import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.commands.PSSCommand
import me.partlysanestudios.partlysaneskies.events.SubscribePSSEvent
import me.partlysanestudios.partlysaneskies.events.skyblock.dungeons.DungeonStartEvent
import me.partlysanestudios.partlysaneskies.utils.ChatUtils
import me.partlysanestudios.partlysaneskies.utils.MinecraftUtils.countItemInInventory
import net.minecraft.command.ICommandSender

object PearlRefill {
    @SubscribePSSEvent
    fun onDungeonStart(event: DungeonStartEvent) {
        if (!PartlySaneSkies.config.autoPearlRefill) return

        runPearlRefill()
    }

    fun registerCommand() {
        PSSCommand("pearlrefill")
            .addAlias("refillpearl")
            .addAlias("pr")
            .setDescription("Refills your ender pearls to 16.")
            .setRunnable { s: ICommandSender, a: Array<String> ->
                runPearlRefill()
            }.register()
    }

    fun keybindAction(){ runPearlRefill() }

    private fun runPearlRefill() {
        val pearlAmount = countItemInInventory("ENDER_PEARL")

        if (pearlAmount < 16) {
            ChatUtils.sendClientMessage("Refilling ${16 - pearlAmount} pearls...")
            PartlySaneSkies.minecraft.thePlayer.sendChatMessage("/gfs ender_pearl ${16 - pearlAmount}")
        } else {
            ChatUtils.sendClientMessage("Unable to refill, you already have 16 pearls!")
        }
    }
}