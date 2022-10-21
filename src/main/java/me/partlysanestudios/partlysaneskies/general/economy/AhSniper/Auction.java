package me.partlysanestudios.partlysaneskies.general.economy.AhSniper;

import java.util.List;

import gg.essential.elementa.UIComponent;
import me.partlysanestudios.partlysaneskies.utils.Utils;
import net.minecraft.item.ItemStack;

public class Auction {
    private String seller;
    private int slot;
    private ItemStack item;
    private UIComponent box;


    public Auction(String seller, int slot, ItemStack item) {
        this.seller = seller;
        this.slot = slot;
        this.item = item;
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
            if (Utils.removeColorCodes(line).contains("Buy it now: ")) {
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
}
