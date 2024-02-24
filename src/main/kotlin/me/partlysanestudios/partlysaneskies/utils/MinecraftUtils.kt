//
// Written by J10a1n15 and Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.utils

import com.google.common.collect.ComparisonChain
import com.google.common.collect.Ordering
import gg.essential.elementa.dsl.pixels
import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.features.economy.auctionhousemenu.AuctionElement
import me.partlysanestudios.partlysaneskies.features.economy.auctionhousemenu.AuctionHouseGui
import me.partlysanestudios.partlysaneskies.utils.HypixelUtils.getItemId
import me.partlysanestudios.partlysaneskies.utils.StringUtils.removeColorCodes
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.network.NetworkPlayerInfo
import net.minecraft.entity.Entity
import net.minecraft.entity.item.EntityArmorStand
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.scoreboard.ScorePlayerTeam
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.apache.commons.lang3.reflect.FieldUtils
import java.lang.IndexOutOfBoundsException
import java.util.stream.Collectors

object MinecraftUtils {
    /**
     * Tablist Getter hard inspired by NEU
     * NEU-Repo: https://github.com/NotEnoughUpdates/NotEnoughUpdates
     * The Code: https://github.com/NotEnoughUpdates/NotEnoughUpdates/blob/master/src/main/java/io/github/moulberry/notenoughupdates/util/TabListUtils.java
     *
     * Changes made:
     * - Small rewrites
     * - Translated to kotlin
    */
    private val playerOrdering = Ordering.from { overlay1: NetworkPlayerInfo?, overlay2: NetworkPlayerInfo? ->
        comparePlayers(
            overlay1!!, overlay2!!
        )
    }

    @SideOnly(Side.CLIENT)
    fun getTabList(): List<String> {
        return try {
            val players = PartlySaneSkies.minecraft.thePlayer.sendQueue.playerInfoMap.stream()
                .sorted(playerOrdering)
                .collect(Collectors.toList())
            players.stream()
                .map { info: NetworkPlayerInfo? ->
                    PartlySaneSkies.minecraft.ingameGUI.tabList.getPlayerName(info)
                }
                .collect(Collectors.toList())
        } catch (e: Exception) {
            ArrayList()
        }
    }

    private fun comparePlayers(overlay1: NetworkPlayerInfo, overlay2: NetworkPlayerInfo): Int {
        val team1 = overlay1.playerTeam
        val team2 = overlay2.playerTeam
        return ComparisonChain.start()
            .compare(
                if (team1 != null) team1.registeredName else "",
                if (team2 != null) team2.registeredName else ""
            )
            .compare(overlay1.gameProfile.name, overlay2.gameProfile.name)
            .result()
    }

    /**
     * @param color if true, returns the scoreboard name with color codes
     * @return the name of the scoreboard
     */
    fun getScoreboardName(color: Boolean = false): String =
        (PartlySaneSkies.minecraft.thePlayer?.worldScoreboard?.getObjectiveInDisplaySlot(1)?.displayName ?: "")
            .let { if (color) it else it.removeColorCodes() }

    fun IInventory.getItemstackList(): ArrayList<ItemStack> {
        val list = ArrayList<ItemStack>()

        for (i in 0..53) {
            try {
                list.add(this.getStackInSlot(i) ?: continue)

            } catch (_: IndexOutOfBoundsException) {

            }
        }

        return list
    }

    /**
     * @return the scoreboard lines
     */
    fun getScoreboardLines(): List<String> {
        return try {
            val scoreboard = PartlySaneSkies.minecraft.theWorld.scoreboard
            val objective = scoreboard.getObjectiveInDisplaySlot(1)
            val scoreCollection = scoreboard.getSortedScores(objective)
            val scoreLines: MutableList<String> = ArrayList()
            for (score in scoreCollection) {
                scoreLines.add(
                    ScorePlayerTeam.formatPlayerName(
                        scoreboard.getPlayersTeam(score.playerName),
                        score.playerName
                    )
                )
            }
            scoreLines
        } catch (e: IllegalArgumentException) {
            if (e.message == "Cannot locate declared field class net.minecraft.client.gui.inventory.GuiChest.field_147015_w") {
                println("Strange error message in PartlySaneSkies.getScoreboardLines()")
            }
            e.printStackTrace()
            emptyList()
        }
    }

    fun getCurrentlyHoldingItem(): ItemStack? {
        return PartlySaneSkies.minecraft.thePlayer?.heldItem
    }

    // Returns an array of length 2, where the 1st index is the upper inventory,
    // and the 2nd index is the lower inventory.
    // Returns null if there is no inventory, also returns null if there is no access to inventory
    fun GuiScreen.getSeparateUpperLowerInventories(): Array<IInventory?> {
        val upperInventory: IInventory
        val lowerInventory: IInventory
        try {
            upperInventory = FieldUtils.readField(
                this,
                getDecodedFieldName("upperChestInventory"), true
            ) as IInventory
            lowerInventory = FieldUtils.readField(
                this,
                getDecodedFieldName("lowerChestInventory"), true
            ) as IInventory
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
            return arrayOf(null, null)
        }
        return arrayOf(upperInventory, lowerInventory)
    }

    fun ItemStack.getLore(): java.util.ArrayList<String> {
        if (this == null) {
            return java.util.ArrayList()
        }
        if (!this.hasTagCompound() || !this.tagCompound.hasKey("display") || !this.tagCompound.getCompoundTag(
                "display"
            ).hasKey("Lore")
        ) {
            return java.util.ArrayList()
        }
        val tagList = this.tagCompound.getCompoundTag("display").getTagList("Lore", 8)
        val loreList = java.util.ArrayList<String>()
        for (i in 0 until tagList.tagCount()) {
            loreList.add(tagList.getStringTagAt(i))
        }
        return loreList
    }

    fun getLoreAsString(item: ItemStack): String {
        val loreList: List<String> = item.getLore()
        val loreString = StringBuilder()
        for (loreLine in loreList) {
            loreString.append(loreLine).append("\n")
        }
        return loreString.toString()
    }

    fun rightClickOnSlot(slot: Int) {
        val controller = PartlySaneSkies.minecraft.playerController
        controller.windowClick(
            PartlySaneSkies.minecraft.thePlayer.openContainer.windowId,
            slot,
            1,
            0,
            PartlySaneSkies.minecraft.thePlayer
        )
    }

    fun clickOnSlot(slot: Int) {
        val controller = PartlySaneSkies.minecraft.playerController
        controller.windowClick(
            PartlySaneSkies.minecraft.thePlayer.openContainer.windowId,
            slot,
            0,
            0,
            PartlySaneSkies.minecraft.thePlayer
        )
    }


    fun getDecodedFieldName(codedName: String?): String? {
        return object : HashMap<String?, String?>() {
            init {
                put("footer", "field_175255_h")
                put("header", "field_175256_i")
                put("upperChestInventory", "field_147015_w")
                put("lowerChestInventory", "field_147016_v")
                put("persistentChatGUI", "field_73840_e")
                put("sentMessages", "field_146248_g")
                put("streamIndicator", "field_152127_m")
                put("updateCounter", "field_73837_f")
                put("overlayPlayerList", "field_175196_v")
                put("guiIngame", "field_175251_g")
                put("chatMessages", "field_146253_i")
                put("theSlot", "field_147006_u")
                put("stackTagCompound", "field_77990_d")
            }
        }[codedName]
    }

    fun isArrOfStringsInLore(arr: Array<String?>, lore: Array<String>): Boolean {
        for (line in lore) {
            for (arrItem in arr) {
                if (line.contains(arrItem!!)) {
                    return true
                }
            }
        }
        return false
    }

    /**
     * @return all the entities loaded in the world
     */
    fun getAllEntitiesInWorld(): List<Entity> {
        return PartlySaneSkies.minecraft.theWorld?.getLoadedEntityList() ?: ArrayList()
    }

    fun getAllPlayersInWorld(): List<Entity> {
        val playerEntities: MutableList<Entity> = java.util.ArrayList()
        val allEntities = getAllEntitiesInWorld()

        // For every entity in the world, check if its instance of an armor stand
        for (entity in allEntities) {
            if (entity is EntityPlayer) {
                playerEntities.add(entity) // If so, add it to the list
            }
        }
        return playerEntities
    }

    /**
     * @return all the pets currently loaded in the world
     */
    fun getAllPets(): List<Entity> {
        val petEntities: MutableList<Entity> = java.util.ArrayList()
        val armorStandEntities = getAllArmorStands()
        val pattern = """§8\[§7Lv(\d+)§8] (§\w)([\w']+)\s*('s)? (\w+)""".toRegex()

        // For every armor stand in the game, check if it's pet by looking for the level tag in front of the name.
        // Ex: "*[Lv*100] Su386's Black Cat"
        for (entity in armorStandEntities) {
            if (pattern.find(entity.name) != null) {
                petEntities.add(entity) // If so, add it to the list
            }
        }
        return petEntities
    }

    /**
     * @return all the armor stands currently loded in the world
     */
    fun getAllArmorStands(): List<Entity> {
        val armorStandEntities: MutableList<Entity> = java.util.ArrayList()
        val allEntities = getAllEntitiesInWorld()

        // For every entity in the world, check if its instance of an armor stand
        for (entity in allEntities) {
            if (entity is EntityArmorStand) {
                armorStandEntities.add(entity) // If so, add it to the list
            }
        }
        return armorStandEntities
    }

    fun ItemStack.removeAllEnchantments(): ItemStack {
        // Get the NBTTagCompound from the ItemStack
        val compound = this.tagCompound ?: NBTTagCompound()

        // Remove the "ench" tag, which stores enchantments
        compound.removeTag("ench")

        // Update the ItemStack with the modified NBTTagCompound
        this.tagCompound = compound
        return this
    }

    /**
     * @param skyblockId the skyblock id of the item to count
     * @return the number of items in the player's inventory
     */
    fun countItemInInventory(skyblockId: String): Int {
        var itemCount = 0

        val inv = PartlySaneSkies.minecraft.thePlayer.inventory.mainInventory

        for (stackInSlot in inv) {
            if ((stackInSlot?.getItemId() ?: "").equals(skyblockId, ignoreCase = true)) {
                itemCount += stackInSlot.stackSize
            }
        }

        return itemCount
    }
}

