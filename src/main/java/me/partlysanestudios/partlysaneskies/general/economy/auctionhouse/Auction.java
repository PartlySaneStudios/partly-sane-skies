package me.partlysanestudios.partlysaneskies.general.economy.auctionhouse;

import java.util.List;

import gg.essential.elementa.UIComponent;
import me.partlysanestudios.partlysaneskies.general.economy.ItemLowestBin;
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
                    || Utils.removeColorCodes(line).contains("Top Bid:")
                    || Utils.removeColorCodes(line).contains("Starting Bid:")) {
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
        if (ItemLowestBin.avgLowestBin == null) {
            return false;
        }
        if (!ItemLowestBin.avgLowestBin.containsKey(itemId)) {
            return false;
        }
        double averageAhPrice = ItemLowestBin.avgLowestBin.get(itemId);

        if (sellingPrice <= averageAhPrice * .60) {
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
        if (ItemLowestBin.avgLowestBin == null) {
            return 0;
        }
        if (!ItemLowestBin.avgLowestBin.containsKey(itemId)) {
            return 0;
        }
        return ItemLowestBin.avgLowestBin.get(itemId);
    }

    public double getLowestBin() {
        if (ItemLowestBin.lowestBin == null) {
            return 0;
        }
        if (!ItemLowestBin.lowestBin.containsKey(itemId)) {
            return 0;
        }
        return ItemLowestBin.lowestBin.get(itemId);
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
