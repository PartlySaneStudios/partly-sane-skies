//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.features.foraging

import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.api.events.PSSEvent
import me.partlysanestudios.partlysaneskies.data.cache.PetData
import me.partlysanestudios.partlysaneskies.data.skyblockdata.Rarity
import me.partlysanestudios.partlysaneskies.events.minecraft.player.PlayerBreakBlockEvent
import me.partlysanestudios.partlysaneskies.render.gui.hud.cooldown.Cooldown
import me.partlysanestudios.partlysaneskies.render.gui.hud.cooldown.CooldownManager
import me.partlysanestudios.partlysaneskies.utils.HypixelUtils.getItemId
import me.partlysanestudios.partlysaneskies.utils.MinecraftUtils
import me.partlysanestudios.partlysaneskies.utils.geometry.vectors.Point3d
import net.minecraft.block.material.Material
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList
import net.minecraft.nbt.NBTTagString

object TreecapitatorCooldown : Cooldown() {
    init {
        CooldownManager.registerCooldown(this)
    }

    override fun getTotalTime(): Long {
        var cooldown = 2000L

        if (PetData.getCurrentPetName() == "Monkey" &&
            PetData.getCurrentPetRarity().order >= Rarity.LEGENDARY.order &&
            PetData.getCurrentPetLevel() != -1 &&
            PartlySaneSkies.config.treecapCooldownMonkeyPet
        ) {
            cooldown -= (cooldown * PetData.getCurrentPetLevel() / 200.0).toLong()
        }
        return cooldown
    }

    override fun getDisplayName(): String = "Treecapitator"

    private var treecapitatorAxe: ItemStack? = null

    override fun getItemToDisplay(): ItemStack {
        if (treecapitatorAxe == null) {
            val itemStack = ItemStack(Items.golden_axe)
            itemStack.setStackDisplayName("§5Treecapitator")
            val compound = itemStack.tagCompound ?: NBTTagCompound()

            val displayCompound = compound.getCompoundTag("display") ?: NBTTagCompound()
            val loreList = NBTTagList()

            val lore =
                arrayOf(
                    "§9Efficiency V",
                    "§7Increases how quickly your tool",
                    "§7breaks blocks.",
                    "",
                    "§7A forceful Gold Axe which can break",
                    "§7a large amount of logs in a single hit!",
                    "§8Cooldown: §a2s",
                    "",
                    "§7§8This item can be reforged!",
                    "§5§lEPIC AXE",
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

    @PSSEvent.Subscribe
    fun checkForCooldown(event: PlayerBreakBlockEvent) {
        if (!PartlySaneSkies.config.treecapCooldown) {
            return
        }

        val itemInUse = MinecraftUtils.getCurrentlyHoldingItem() ?: return
        val idInUse = itemInUse.getItemId()

        if (idInUse != "TREECAPITATOR_AXE") {
            return
        }

        if (isCooldownActive()) { // Checks if it a
            return
        }

        if (!isWood(event.point)) { // Checks if the block is wood
            return
        }

        if (!isAdjacentToWood(event.point)) { // Checks if the block is adjacent to wood
            return
        }

        this.startCooldown()
    }

    private fun isWood(point: Point3d): Boolean {
        val block = point.getBlockAtPoint()
        val material = block?.material
        return material == Material.wood
    }

    private fun isAdjacentToWood(point: Point3d): Boolean {
        for (x in -1..1) {
            for (y in -1..1) {
                for (z in -1..1) {
                    if (x == 0 && y == 0 && z == 0) {
                        continue
                    }

                    val pointOffset = Point3d(x.toDouble(), y.toDouble(), z.toDouble())
                    if (isWood(point + pointOffset)) {
                        return true
                    }
                }
            }
        }
        return false
    }
}
