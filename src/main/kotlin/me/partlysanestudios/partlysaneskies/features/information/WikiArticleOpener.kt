//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.features.information

import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.config
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.minecraft
import me.partlysanestudios.partlysaneskies.features.economy.auctionhousemenu.AuctionHouseGui
import me.partlysanestudios.partlysaneskies.utils.HypixelUtils.getItemId
import me.partlysanestudios.partlysaneskies.utils.HypixelUtils.isSkyblock
import me.partlysanestudios.partlysaneskies.utils.StringUtils.removeColorCodes
import me.partlysanestudios.partlysaneskies.utils.SystemUtils.openLink
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.item.ItemStack
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object WikiArticleOpener {
    private var isWaitingForArticle = false

    @SubscribeEvent
    fun openArticle(e: ClientChatReceivedEvent) {
        if (!isWaitingForArticle) {
            return
        }
        if (e.message.formattedText.removeColorCodes().contains("Invalid")) {
            isWaitingForArticle = false
            return
        }
        if (!e.message.formattedText.removeColorCodes().contains("Click HERE")) {
            return
        }
        isWaitingForArticle = false
        val wikiLink = e.message.chatStyle.chatClickEvent.value
        if (config.openWikiAutomatically) {
            openLink(wikiLink)
        }
    }

    fun keyDown() {
        if (!isSkyblock()) {
            return
        }
        val item: ItemStack
        if (minecraft.currentScreen !is GuiContainer) {
            return
        }
        if (minecraft.currentScreen is AuctionHouseGui) {
            return
        }
        val container = minecraft.currentScreen as GuiContainer
        val slot = container.slotUnderMouse ?: return
        item = slot.stack ?: return

        if (item.getItemId().isEmpty()) {
            return
        }
        getArticle(item.getItemId())
    }

    private fun getArticle(id: String) {
        isWaitingForArticle = true
        minecraft.thePlayer.sendChatMessage("/wiki $id")
    }
}

/*
 * Ca_mo is in the mod now ig
 * https://media.discordapp.net/attachments/1082885746400178286/1093721027273556098/image.png
 * Ca_mo will now be in everyone's Partly Sane Skies
 */

// hi ca_mo - j10a

// Nearly forgot camo. Camo is now in kt (even better than full hd) - Su386
