//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.gui.hud.cooldown

import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.utils.ChatUtils
import me.partlysanestudios.partlysaneskies.utils.HypixelUtils
import me.partlysanestudios.partlysaneskies.utils.MinecraftUtils
import net.minecraft.init.Items
import net.minecraft.item.ItemStack

object TreecapitatorCooldown: Cooldown() {
    override fun getTotalTime(): Long {
        return 2000L
    }

    override fun getDisplayName(): String {
        return "Treecapitator"
    }

    private var treecapitatorAxe = ItemStack(Items.golden_axe);
    override fun getItemToDisplay(): ItemStack {
        return treecapitatorAxe
    }

    fun checkForCooldown() {
        if (MinecraftUtils.getCurrentlyHoldingItem() == null) {
            return
        }

        val itemInUse = MinecraftUtils.getCurrentlyHoldingItem()!!
        val idInUse = HypixelUtils.getItemId(itemInUse)

        if (!idInUse.equals("TREECAPITATOR_AXE")) {
            return
        }

        if (isCooldownActive()) {
            return
        }

        treecapitatorAxe = itemInUse
        this.startCooldown()


    }


}