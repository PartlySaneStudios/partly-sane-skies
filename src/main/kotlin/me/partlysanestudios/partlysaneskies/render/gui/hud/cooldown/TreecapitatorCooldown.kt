//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.render.gui.hud.cooldown

import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.data.cache.PetData
import me.partlysanestudios.partlysaneskies.data.skyblockdata.Rarity
import me.partlysanestudios.partlysaneskies.utils.HypixelUtils
import me.partlysanestudios.partlysaneskies.utils.MinecraftUtils
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList
import net.minecraft.nbt.NBTTagString


object TreecapitatorCooldown : Cooldown() {
    override fun getTotalTime(): Long {
        var cooldown = 2000L

        if (PetData.getCurrentPetName() == "Monkey"
            && PetData.getCurrentPetRarity().order >= Rarity.LEGENDARY.order
            && PetData.getCurrentPetLevel() != -1
            && PartlySaneSkies.config.foraging.treecapCooldownMonkeyPet != false
        ) {
            cooldown -= (cooldown * PetData.getCurrentPetLevel() / 200.0).toLong()
        }
        return cooldown
    }

    override fun getDisplayName(): String {
        return "Treecapitator"
    }

    private var treecapitatorAxe: ItemStack? = null;
    override fun getItemToDisplay(): ItemStack {
        if (treecapitatorAxe == null) {
            val itemStack = ItemStack(Items.golden_axe)
            itemStack.setStackDisplayName("§5Treecapitator")
            val compound = itemStack.tagCompound ?: NBTTagCompound()

            val displayCompound = compound.getCompoundTag("display") ?: NBTTagCompound()
            val loreList = NBTTagList()

            val lore = arrayOf(
                "§9Efficiency V",
                "§7Increases how quickly your tool",
                "§7breaks blocks.",
                "",
                "§7A forceful Gold Axe which can break",
                "§7a large amount of logs in a single hit!",
                "§8Cooldown: §a2s",
                "",
                "§7§8This item can be reforged!",
                "§5§lEPIC AXE"
            )

            // Convert lore strings to NBTTagString and add them to the list
            for (line in lore) {
                loreList.appendTag(NBTTagString(line))
            }

            displayCompound.setTag("Lore", loreList)
            compound.setTag("display", displayCompound)

            val extraAttributesTag = compound.getCompoundTag("ExtraAttributes") ?: NBTTagCompound()
            extraAttributesTag.setString("id", "TREECAPITATOR_AXE")
            compound.setTag("ExtraAttributes", extraAttributesTag)

            treecapitatorAxe = itemStack
        }

        return treecapitatorAxe ?: ItemStack(Items.golden_axe)
    }

    fun checkForCooldown() {
        if (!PartlySaneSkies.config.foraging.treecapCooldown) {
            return
        }

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

        this.startCooldown()
    }


}