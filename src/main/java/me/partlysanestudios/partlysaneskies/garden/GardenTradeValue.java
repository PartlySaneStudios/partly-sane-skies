//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.garden;

import gg.essential.elementa.ElementaVersion;
import gg.essential.elementa.UIComponent;
import gg.essential.elementa.components.UIImage;
import gg.essential.elementa.components.UIRoundedRectangle;
import gg.essential.elementa.components.UIWrappedText;
import gg.essential.elementa.components.Window;
import gg.essential.elementa.constraints.CenterConstraint;
import gg.essential.elementa.constraints.PixelConstraint;
import gg.essential.universal.UMatrixStack;
import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import me.partlysanestudios.partlysaneskies.data.skyblockdata.SkyblockDataManager;
import me.partlysanestudios.partlysaneskies.system.ThemeManager;
import me.partlysanestudios.partlysaneskies.utils.MathUtils;
import me.partlysanestudios.partlysaneskies.utils.MinecraftUtils;
import me.partlysanestudios.partlysaneskies.utils.StringUtils;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GardenTradeValue {
    private static final Window window = new Window(ElementaVersion.V2);
    // Returns if the player is currently viewing a trading screen
    public static boolean isTrade() {
        if (PartlySaneSkies.minecraft.currentScreen == null) {
            return false;
        }
        if (!(PartlySaneSkies.minecraft.currentScreen instanceof GuiChest)) {
            return false;
        }

        IInventory[] inventories = MinecraftUtils.INSTANCE.getSeparateUpperLowerInventories(PartlySaneSkies.minecraft.currentScreen);
        assert inventories != null;
        IInventory trader = inventories[0];

        // Slots 29 and 33 are where the buttons should be
        ItemStack acceptButton = trader.getStackInSlot(29);
        ItemStack refuseButton = trader.getStackInSlot(33);

        if (acceptButton == null) {
            return false;
        }
        if (refuseButton == null) {
            return false;
        }

        String acceptButtonName = StringUtils.INSTANCE.removeColorCodes(acceptButton.getDisplayName());
        String refuseButtonName = StringUtils.INSTANCE.removeColorCodes(refuseButton.getDisplayName());

        // If the names are not equal to the desired names, then you know it screen
        // is not the trader screen
        if (!refuseButtonName.equals("Refuse Offer")) {
            return false;
        }
        return acceptButtonName.equals("Accept Offer");
    }

    // Returns a hashmap containing the name of an item and the quantity
    public static HashMap<String, Integer> getQuantityCostMap() {
        IInventory[] inventories = MinecraftUtils.INSTANCE.getSeparateUpperLowerInventories(PartlySaneSkies.minecraft.currentScreen);
        assert inventories != null;
        IInventory trader = inventories[0];

        // Slots 29 is where the accept buttons is
        ItemStack acceptButton = trader.getStackInSlot(29);

        ArrayList<String> formattedAcceptButtonLore = MinecraftUtils.INSTANCE.getLore(acceptButton);
        
        // Removes all the format codes from lore
        ArrayList<String> unformattedAcceptButtonLore = removeColorCodesFromList(formattedAcceptButtonLore);

        int costLineIndex = unformattedAcceptButtonLore.indexOf("Items Required:");
        int rewardsStartIndex = unformattedAcceptButtonLore.indexOf("Rewards:");

        // Finds each item of the lore
        ArrayList<String> cost = new ArrayList<>();
        for (int i = costLineIndex + 1; i < rewardsStartIndex; i++) {
            cost.add(unformattedAcceptButtonLore.get(i));
        }

    
        HashMap<String, Integer> costMap = new HashMap<>();

        for (String costLine : cost) {
            // All the messages are formatted <Name> x<Cost>
            // so the name is up until the last 'x,' and the cost starts
            // after the x
            int costStartIndex = costLine.lastIndexOf("x");
            // If the item does not have a multiple, it means it only has one
            boolean singleItem = false;
            String amountString;
            // If there is no x, pass
            if (costStartIndex == -1) {
                if (costLine.length() > 5) {
                    singleItem = true;
                    costStartIndex = costLine.length();
                }
                else{
                    continue;
                }
                
            }

            // Gets the name of ihe item and formats it
            String name = costLine.substring(0, costStartIndex);
            name = StringUtils.INSTANCE.stripLeading(name);
            name = StringUtils.INSTANCE.stripTrailing(name);

            int amount;
            if (singleItem) {
                amount = 1;
            }
            else {
                // Gets the cost of the item and converts it to an integer
                amountString = costLine.substring(costStartIndex + 1);
                // Replaces all non-numeric characters in the string
                amountString = amountString.replaceAll("[^\\d.]", "");
                amountString = amountString.replace(",", "");
                amountString = amountString.replace(".", "");

                amount = Integer.parseInt(amountString);
            }

            

            // Adds it to the cost map
            costMap.put(name, amount);
        }

        return costMap;
    }

    public static List<String> getRewardsLore() {
        IInventory[] inventories = MinecraftUtils.INSTANCE.getSeparateUpperLowerInventories(PartlySaneSkies.minecraft.currentScreen);
        assert inventories != null;
        IInventory trader = inventories[0];

        // Slots 29 is where the accept buttons is
        ItemStack acceptButton = trader.getStackInSlot(29);

        ArrayList<String> formattedAcceptButtonLore = MinecraftUtils.INSTANCE.getLore(acceptButton);

        // Removes all the format codes from lore
        ArrayList<String> unformattedAcceptButtonLore = removeColorCodesFromList(formattedAcceptButtonLore);

        int rewardsStartIndex = unformattedAcceptButtonLore.indexOf("Rewards:");

        return formattedAcceptButtonLore.subList(rewardsStartIndex, formattedAcceptButtonLore.size());
    }

    public static int getCopperReturn() {
        

        
        List<String> unformattedRewardsLore = removeColorCodesFromList(getRewardsLore());

        for (String line : unformattedRewardsLore) {
            if (!line.contains(" Copper")) {
                continue;
            }

            String strippedLine = StringUtils.INSTANCE.stripLeading(line);
            strippedLine = StringUtils.INSTANCE.stripTrailing(strippedLine);

            int amountStartIndex = strippedLine.indexOf("+") + 1;
            int amountEndIndex = strippedLine.indexOf(" C");
            String amountString = strippedLine.substring(amountStartIndex, amountEndIndex);
            return Integer.parseInt(amountString);
        }

        return -1;

    }

    // Returns a new list with all format codes removed
    public static ArrayList<String> removeColorCodesFromList(List<String> list) {
        ArrayList<String> newList = new ArrayList<>();

        for (String oldLine : list) {
            newList.add(StringUtils.INSTANCE.removeColorCodes(oldLine));
        }

        return newList;
    }

    public static double getItemCost(String itemId, int quantity) {
        if (SkyblockDataManager.getItem(itemId) == null ) {
            return 0;
        }
        
        return quantity * SkyblockDataManager.getItem(itemId).getBuyPrice();
    }

    public static HashMap<String, Double> getCoinCostMap() {
        HashMap<String, Integer> quantityMap = getQuantityCostMap();

        HashMap<String, Double> coinMap = new HashMap<>();
        for (Map.Entry<String, Integer> en : quantityMap.entrySet()) {
            String id = SkyblockDataManager.getId(en.getKey());
            double price = getItemCost(id, en.getValue());
            coinMap.put(en.getKey(), price);
        }

        return coinMap;
    }

    public static double getTotalCost() {
        HashMap<String, Double> costMap = getCoinCostMap();

        double totalCost = 0;

        for (double individualCost : costMap.values()) {
            totalCost += individualCost;
        }

        return totalCost;
    }

    UIComponent box = new UIRoundedRectangle(widthScaledConstraint(5).getValue())
            .setColor(new Color(0, 0, 0, 0))
            .setChildOf(window);
    
    UIImage image = (UIImage) ThemeManager.getCurrentBackgroundUIImage()
        .setChildOf(box);
    
    float pad = 5;
    UIWrappedText textComponent = (UIWrappedText) new UIWrappedText()
        .setChildOf(box);

    @SubscribeEvent
    public void renderInformation(GuiScreenEvent.BackgroundDrawnEvent event) {
        if (!isTrade()) {
            box.hide();
            return;
        }
        if (!PartlySaneSkies.config.gardenShopTradeInfo) {
            return;
        }

        box.unhide(true);
        box.setX(widthScaledConstraint(700))
            .setY(new CenterConstraint())
            .setWidth(widthScaledConstraint(200))
            .setHeight(widthScaledConstraint(300));

        image.setX(new CenterConstraint())
            .setY(new CenterConstraint())
            .setWidth(new PixelConstraint(box.getWidth()))
            .setHeight(new PixelConstraint(box.getHeight()));
        
        textComponent.setX(widthScaledConstraint(pad))
            .setTextScale(widthScaledConstraint(1f))
            .setY(widthScaledConstraint(2 * pad))
            .setWidth(new PixelConstraint(box.getWidth() - widthScaledConstraint(2 * pad).getValue()));

        StringBuilder textString = new StringBuilder();

        textString.append("§e§lTotal Cost: §r§d").append(StringUtils.INSTANCE.formatNumber(MathUtils.INSTANCE.round(getTotalCost(), 2))).append("\n\n");
        
        textString.append("§e§lCopper Received: §r§d").append(StringUtils.INSTANCE.formatNumber(MathUtils.INSTANCE.round(getCopperReturn(), 2))).append("\n\n");

        double pricePerCopper = getTotalCost() / getCopperReturn();
        textString.append("§e§lCoins/Copper: §r§d").append(StringUtils.INSTANCE.formatNumber(MathUtils.INSTANCE.round(pricePerCopper, 2))).append("\n\n");

        StringBuilder priceBreakdown = new StringBuilder();
        HashMap<String, Double> coinCostMap = getCoinCostMap();
        for (Map.Entry<String, Integer> en : getQuantityCostMap().entrySet()){
            priceBreakdown.append("§7x§d").append(en.getValue()).append(" §7").append(en.getKey()).append(" for a total of §d").append(StringUtils.INSTANCE.formatNumber(MathUtils.INSTANCE.round(coinCostMap.get(en.getKey()), 2))).append("§7 coins.\n");
        }

        textString.append("§e§lPrice Breakdown:§r\n");
        textString.append(priceBreakdown);
        textString.append("\n\n");

        textString.append("§e§lRewards:§r\n");
        for (String line : getRewardsLore()) {
            textString.append(line).append("\n");
        }

        textString = new StringBuilder((textString.toString()));

        textComponent.setText(textString.toString());

        window.draw(new UMatrixStack());
    }

    private static float getWidthScaleFactor() {
        return window.getWidth() / 1097f;
    }

    private static PixelConstraint widthScaledConstraint(float value) {
        return new PixelConstraint(value * getWidthScaleFactor());
    }
}
