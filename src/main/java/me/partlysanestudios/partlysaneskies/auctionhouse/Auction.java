//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.auctionhouse;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import gg.essential.elementa.UIComponent;
import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import me.partlysanestudios.partlysaneskies.data.skyblockdata.SkyblockDataManager;
import me.partlysanestudios.partlysaneskies.system.ThemeManager;
import me.partlysanestudios.partlysaneskies.utils.StringUtils;
import me.partlysanestudios.partlysaneskies.utils.Utils;
import net.minecraft.item.ItemStack;

public class Auction {
    // private String seller;
    private final int slot;
    private final ItemStack item;
    private UIComponent box;
    private final String itemId;

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
            if (StringUtils.removeColorCodes(line).contains("Buy it now: ")) {
                return true;
            }
        }
        return false;
    }

    public int getAmount() {
        if (item == null) {
            return 1;
        }
        return item.stackSize;
    }

    public double getCostPerAmount() {
        return getPrice() / (double) getAmount();
    }
    public long getPrice() {
        List<String> loreList = Utils.getLore(item);
        String buyItNowPrice = "";

        for (String line : loreList) {
            if (StringUtils.removeColorCodes(line).contains("Buy it now:")
                    || StringUtils.removeColorCodes(line).contains("Top bid:")
                    || StringUtils.removeColorCodes(line).contains("Starting bid:")) {
                buyItNowPrice = StringUtils.removeColorCodes(line).replaceAll("[^0-9]", "");
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
        } else {
            return isCheapBin();
        }

    }

    private boolean isCheapBin() {
        long sellingPrice = getPrice();
        if (SkyblockDataManager.getItem(itemId) == null) {
            return false;
        }
        if (!SkyblockDataManager.getItem(itemId).hasSellPrice()) {
            return false;
        }
        double averageAhPrice = SkyblockDataManager.getItem(itemId).getSellPrice();

        return sellingPrice <= averageAhPrice * (PartlySaneSkies.config.BINSniperPercent / 100d);
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
        if (SkyblockDataManager.getItem(itemId) == null) {
            return 0;
        }
        if (!SkyblockDataManager.getItem(itemId).hasAverageLowestBin()) {
            return 0;
        }
        return SkyblockDataManager.getItem(itemId).getAverageLowestBin();
    }

    public double getLowestBin() {
        try {
            if (!SkyblockDataManager.getItem(itemId).hasSellPrice()) {
                return 0;
            } 
        } catch (NullPointerException exception) {
            exception.printStackTrace();
            return 0;
        }
        
        
        return SkyblockDataManager.getItem(itemId).getSellPrice();
    }

    public boolean hasLowestBin() {
        if (SkyblockDataManager.getItem(itemId) == null) {
            return false;
        }
        return SkyblockDataManager.getItem(itemId).hasSellPrice();
    }

    public boolean hasAverageLowestBin() {
        if (SkyblockDataManager.getItem(itemId) == null) {
            return false;
        }

        return SkyblockDataManager.getItem(itemId).hasAverageLowestBin();
    }

    public String getFormattedEndingTime() {
        List<String> loreList = Utils.getLore(item);

        for (String loreLine : loreList) {
            if (StringUtils.removeColorCodes(loreLine).contains("Ends in:")) {
                return StringUtils.removeColorCodes(loreLine).replace("Ends in: ", "");
            }
            if (StringUtils.removeColorCodes(loreLine).contains("Ending Soon")) {
                return StringUtils.removeColorCodes(loreLine);
            }
        }

        return "";
    }

    public String getLore() {
        return Utils.getLoreAsString(this.item);
    }

    public String getRarity() {
        String str = "";
        String lastLineOfLore;
        try {
            ArrayList<String> loreList = Utils.getLore(item);
            lastLineOfLore = StringUtils.removeColorCodes(loreList.get(loreList.size() - 7 - 1));
        } catch (NullPointerException exception) {
            exception.printStackTrace();
            return "";
        }
        

        if (lastLineOfLore.toUpperCase().contains("UNCOMMON")) {
            str = "UNCOMMON";
        }
        else if (lastLineOfLore.toUpperCase().contains("COMMON")) {
            str = "COMMON";
        }
        else if (lastLineOfLore.toUpperCase().contains("RARE")) {
            str = "RARE";
        }
        else if (lastLineOfLore.toUpperCase().contains("EPIC")) {
            str = "EPIC";
        }
        else if (lastLineOfLore.toUpperCase().contains("LEGENDARY")) {
            str = "LEGENDARY";
        }
        else if (lastLineOfLore.toUpperCase().contains("MYTHIC")) {
            str = "MYTHIC";
        }
        else if (lastLineOfLore.toUpperCase().contains("DIVINE")) {
            str = "DIVINE";
        }
        else if (lastLineOfLore.toUpperCase().contains("SPECIAL")) {
            str = "SPECIAL";
        }

        return str;
    }

    public Color getRarityColor() {
        String rarity = getRarity();
        switch (rarity) {
            case "COMMON":
                return new Color(255, 255, 255);
            case "UNCOMMON":
                return new Color(85, 255, 85);
            case "RARE":
                return new Color(85, 85, 255);
            case "EPIC":
                return new Color(170, 0, 170);
            case "LEGENDARY":
                return new Color(255, 170, 0);
            case "MYTHIC":
                return new Color(255, 85, 255);
            case "DIVINE":
                return new Color(85, 255, 255);
            case "SPECIAL":
                return new Color(255, 85, 85);
            
            default:
                return ThemeManager.getPrimaryColor().toJavaColor();
        }
    }

}
