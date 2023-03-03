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

package me.partlysanestudios.partlysaneskies.auctionhouse;

import java.util.List;

import gg.essential.elementa.UIComponent;
import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import me.partlysanestudios.partlysaneskies.SkyblockItem;
import me.partlysanestudios.partlysaneskies.utils.Utils;
import net.minecraft.item.ItemStack;

public class Auction {
    // private String seller;
    private int slot;
    private ItemStack item;
    private UIComponent box;
    private String itemId;

    public Auction(int slot, ItemStack item) {
        this.slot = slot;
        this.item = item;
        this.itemId = Utils.getItemId(item);
    }

    public void selectAuction() {
        Utils.clickOnSlot(slot);
    }

    public boolean isBin() {
        List<String> loreList = Utils.getLore(item);
        for (String line : loreList) {
            if (Utils.removeColorCodes(line).contains("Buy it now: ")) {
                return true;
            }
        }
        return false;
    }

    public long getPrice() {
        List<String> loreList = Utils.getLore(item);
        String buyItNowPrice = "";

        for (String line : loreList) {
            if (Utils.removeColorCodes(line).contains("Buy it now:")
                    || Utils.removeColorCodes(line).contains("Top bid:")
                    || Utils.removeColorCodes(line).contains("Starting bid:")) {
                buyItNowPrice = Utils.removeColorCodes(line).replaceAll("[^0-9]", "");
            }
        }

        return Long.parseLong(buyItNowPrice);
    }

    public ItemStack getItem() {
        return this.item;
    }

    public UIComponent setBox(UIComponent uiComponent) {
        this.box = uiComponent;
        return this.box;
    }

    public UIComponent getBox() {
        return this.box;
    }

    public boolean shouldHighlight() {
        if (!isBin()) {
            return false;
        } else if (isCheapBin()) {
            return true;
        }
        return false;

    }

    private boolean isCheapBin() {
        long sellingPrice = getPrice();
        if (!SkyblockItem.getItem(itemId).hasPrice()) {
            return false;
        }
        double averageAhPrice = SkyblockItem.getItem(itemId).getPrice();

        if (sellingPrice <= averageAhPrice * PartlySaneSkies.config.BINSniperPercent) {
            return true;
        }
        return false;
    }

    public int getItemQuantity() {
        return item.stackSize;
    }

    public int getSlot() {
        return slot;
    }

    public String getName() {
        return item.getDisplayName();
    }

    public double getAverageLowestBin() {
        if (!SkyblockItem.getItem(itemId).hasAverageLowestBin()) {
            return 0;
        }
        return SkyblockItem.getItem(itemId).getAverageLowestBin();
    }

    public double getLowestBin() {
        if (!SkyblockItem.getItem(itemId).hasPrice()) {
            return 0;
        }
        
        return SkyblockItem.getItem(itemId).getPrice();
    }

    public String getFormattedEndingTime() {
        List<String> loreList = Utils.getLore(item);

        for (String loreLine : loreList) {
            if (Utils.removeColorCodes(loreLine).contains("Ends in:")) {
                return Utils.removeColorCodes(loreLine).replace("Ends in: ", "");
            }
            if (Utils.removeColorCodes(loreLine).contains("Ending Soon")) {
                return Utils.removeColorCodes(loreLine);
            }
        }

        return "";
    }

    public String getLore() {
        return Utils.getLoreAsString(this.item);
    }

}
