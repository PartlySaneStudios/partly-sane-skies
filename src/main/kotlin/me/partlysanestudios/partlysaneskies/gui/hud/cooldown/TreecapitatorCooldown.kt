//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.gui.hud.cooldown

import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.utils.ChatUtils
import me.partlysanestudios.partlysaneskies.utils.HypixelUtils
import me.partlysanestudios.partlysaneskies.utils.MinecraftUtils

object TreecapitatorCooldown: Cooldown() {
    override fun getTotalTime(): Long {
        return 2000L
    }

    override fun getDisplayName(): String {
        return "Treecapitator"
    }

    fun checkForCooldown() {
        if (MinecraftUtils.getCurrentlyHoldingItem() == null) {
            ChatUtils.visPrint("Null")
            return
        }

        val itemInUse = MinecraftUtils.getCurrentlyHoldingItem()!!
        val idInUse = HypixelUtils.getItemId(itemInUse)

        if (!idInUse.equals("TREECAPITATOR_AXE")) {
            ChatUtils.visPrint(idInUse)
            return
        }

        if (isCooldownActive()) {
            ChatUtils.visPrint("Cooldown already active")
            return
        }

        this.startCooldown()


    }


}