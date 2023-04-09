/*
 * Partly Sane Skies: A Hypixel Skyblock QOL and Economy mod
 * Created by Su386#9878 (Su386yt) and FlagMaster#1516 (FlagHater), the Partly Sane Studios team
 * Copyright (C) ©️ Su386 and FlagMaster 2023
 * This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 * 
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 * 
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package me.partlysanestudios.partlysaneskies;

import me.partlysanestudios.partlysaneskies.auctionhouse.AhGui;
import me.partlysanestudios.partlysaneskies.utils.StringUtils;
import me.partlysanestudios.partlysaneskies.utils.Utils;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WikiArticleOpener {
    public static boolean isWaitingForArticle = false;

    public static NBTTagCompound getItemAttributes(ItemStack item) {
        return item.getTagCompound().getCompoundTag("ExtraAttributes");
    }

    public static void getArticle(String id) {
        isWaitingForArticle = true;
        PartlySaneSkies.minecraft.thePlayer.sendChatMessage("/wiki " + id);
    }

    @SubscribeEvent
    public void openArticle(ClientChatReceivedEvent e) {
        if (!isWaitingForArticle) {
            return;
        }
        if (StringUtils.removeColorCodes(e.message.getFormattedText()).contains("Invalid")) {
            isWaitingForArticle = false;
            return;
        }
        if (!StringUtils.removeColorCodes(e.message.getFormattedText()).contains("Click HERE")) {
            return;
        }

        isWaitingForArticle = false;
        String wikiLink = e.message.getChatStyle().getChatClickEvent().getValue();
        if (PartlySaneSkies.config.openWikiAutomatically) {
            Utils.openLink(wikiLink);
        }
    }

    public static void keyDown() {
        if (!PartlySaneSkies.isSkyblock()) {
            return;
        }
        ItemStack item;
        if (!(PartlySaneSkies.minecraft.currentScreen instanceof GuiContainer)) {
            return;
        }
        if (PartlySaneSkies.minecraft.currentScreen instanceof AhGui) {
            return;
        }
        GuiContainer container = (GuiContainer) PartlySaneSkies.minecraft.currentScreen;
        Slot slot = container.getSlotUnderMouse();
        if (slot == null)
            return;
        item = slot.getStack();

        if (item == null) {
            return;
        }

        if (Utils.getItemId(item).equals("")) {
            return;
        }
        WikiArticleOpener.getArticle(Utils.getItemId(item));
    }
}





/*
 * Ca_mo is in the mod now ig
 * https://media.discordapp.net/attachments/1082885746400178286/1093721027273556098/image.png
 * Ca_mo will now be in everyone's Partly Sane Skies
 */