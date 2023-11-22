//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.garden;

import gg.essential.elementa.ElementaVersion;
import gg.essential.elementa.UIComponent;
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
import java.util.List;
import java.util.*;

public class CompostValue {
    public static HashMap<String, Double> compostValueMap = new HashMap<>();
    public static HashMap<String, Double> costPerOrganicMatterMap = new HashMap<>();
    public static HashMap<String, Double> costPerCompostMap = new HashMap<>();
    public static double compostCost = 4000;
    public static double fillLevel = 0;
    public static double maxCompost = 40000;

    public static void init() {
        compostValueMap.put("WHEAT", 1d);
        compostValueMap.put("HAY_BLOCK", 9d);
        compostValueMap.put("ENCHANTED_BREAD", 60d);
        compostValueMap.put("ENCHANTED_HAY_BLOCK", 1296d);
        compostValueMap.put("CARROT_ITEM", 0.29);
        compostValueMap.put("ENCHANTED_CARROT", 46.4);
        compostValueMap.put("POTATO_ITEM", 0.33);
        compostValueMap.put("POISONOUS_POTATO", 0.33);
        compostValueMap.put("ENCHANTED_POTATO", 52.8);
        compostValueMap.put("ENCHANTED_POISONOUS_POTATO", 52.8);
        compostValueMap.put("ENCHANTED_BAKED_POTATO", 8448d);
        compostValueMap.put("PUMPKIN", 1d);
        compostValueMap.put("ENCHANTED_PUMPKIN", 160d);
        compostValueMap.put("POLISHED_PUMPKIN", 25600d);
        compostValueMap.put("MELON", 0.2);
        compostValueMap.put("MELON_BLOCK", 1.8);
        compostValueMap.put("ENCHANTED_MELON", 32d);
        compostValueMap.put("ENCHANTED_MELON_BLOCK", 5120d);
        compostValueMap.put("RED_MUSHROOM", 1d);
        compostValueMap.put("HUGE_MUSHROOM_2", 9d);
        compostValueMap.put("ENCHANTED_RED_MUSHROOM", 160d);
        compostValueMap.put("ENCHANTED_HUGE_MUSHROOM_2", 5184d);
        compostValueMap.put("BROWN_MUSHROOM", 1d);
        compostValueMap.put("HUGE_MUSHROOM_1", 9d);
        compostValueMap.put("ENCHANTED_BROWN_MUSHROOM", 160d);
        compostValueMap.put("ENCHANTED_HUGE_MUSHROOM_1", 5184d);
        compostValueMap.put("INK_SACK:3", 0.4);
        compostValueMap.put("ENCHANTED_COCOA", 64d);
        compostValueMap.put("CACTUS", 0.5);
        compostValueMap.put("ENCHANTED_CACTUS_GREEN", 80d);
        compostValueMap.put("SUGAR_CANE", 0.5);
        compostValueMap.put("ENCHANTED_SUGAR", 80d);
        compostValueMap.put("ENCHANTED_PAPER", 96d);
        compostValueMap.put("ENCHANTED_SUGAR_CANE", 12800d);
        compostValueMap.put("NETHER_STALK", 0.33);
        compostValueMap.put("ENCHANTED_NETHER_STALK", 52.8);
        compostValueMap.put("MUTANT_NETHER_STALK", 8448d);
    }

    public static void requestCostPerOrganicMatter() {
        for (Map.Entry<String, Double> en : compostValueMap.entrySet()) {
            costPerOrganicMatterMap.put(en.getKey(), SkyblockDataManager.getItem(en.getKey()).getBuyPrice()/en.getValue());
        }
    }

    public static void requestCostPerCompost() {
        for (Map.Entry<String, Double> en : costPerOrganicMatterMap.entrySet()) {
            costPerCompostMap.put(en.getKey(), en.getValue() * compostCost);
        }
    }

    // Sorts the hashmap in descending order
    public static LinkedHashMap<String, Double> sortMap(HashMap<String, Double> map) {
        List<Map.Entry<String, Double>> list = new LinkedList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());

        LinkedHashMap<String, Double> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Double> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    public static String getString() {
        StringBuilder str = new StringBuilder();

        requestCostPerOrganicMatter();
        requestCostPerCompost();

        HashMap<String, Double> map = sortMap(costPerCompostMap);

        int i = 1;
        for (Map.Entry<String, Double> en : map.entrySet()) {
            String id = en.getKey();
            double cropPerCompost = compostCost / compostValueMap.get(id);
            String cropName = SkyblockDataManager.getItem(id).getName();
            double costPerCompost = en.getValue();
            double compostAmount = getCurrentCompostAbleToMake();
            if (maxCompost == fillLevel) {
                compostAmount = getMaxCompostAbleToMake();
            }
            str.append("§6").append(i).append(". §7x§d").append(StringUtils.INSTANCE.formatNumber(Math.ceil(cropPerCompost * compostAmount))).append(" ").append(cropName).append("§7 costing §d").append(StringUtils.INSTANCE.formatNumber(MathUtils.INSTANCE.round(costPerCompost * compostAmount, 1))).append("§7 coins to fill. \n§8(x").append(StringUtils.INSTANCE.formatNumber(Math.ceil(cropPerCompost))).append("/Compost)\n");

            i++;
            if (i > 5) {
                break;
            }
        }

        str = new StringBuilder((str.toString()));
        
        return str.toString();
    }

    // 22
    public static boolean isComposter() {
        if (PartlySaneSkies.minecraft.currentScreen == null) {
            return false;
        }
        if (!(PartlySaneSkies.minecraft.currentScreen instanceof GuiChest)) {
            return false;
        }

        IInventory[] inventories = MinecraftUtils.INSTANCE.getSeparateUpperLowerInventories(PartlySaneSkies.minecraft.currentScreen);
        assert inventories != null;
        IInventory composter = inventories[0];

        // Slots 22 should be the collect compost button
        ItemStack collectCompostButton = composter.getStackInSlot(13);

        if (collectCompostButton == null) {
            return false;
        }

        String collectCompostButtonName = StringUtils.INSTANCE.removeColorCodes(collectCompostButton.getDisplayName());
        // If the names are not equal to the desired names, then you know it screens
        if (!collectCompostButtonName.equals("Collect Compost")) {
            return false;
        }
        if (!StringUtils.INSTANCE.removeColorCodes(composter.getDisplayName().getFormattedText()).contains("Composter")) {
            return false;
        }

        if (composter.getStackInSlot(46) != null) {
            compostCost = getCompostCost(composter);
            fillLevel = getOrganicMatterFillLevel(composter);
            maxCompost = getOrganicMatterLimit(composter);
        }

        return true;
    }

    private static String getCompostCostString(IInventory composterInventory) {
        ItemStack infoItem = composterInventory.getStackInSlot(46);
        ArrayList<String> loreList = MinecraftUtils.INSTANCE.getLore(infoItem);
        String costLine = "{compost_cost} organic matter stored";
        for (String line : loreList){
            String unformattedLine = StringUtils.INSTANCE.removeColorCodes(line);
            if (unformattedLine.contains("organic matter stored")) {
                costLine = unformattedLine;
                break;
            }
        }

        String pattern = "{compost_cost} organic matter stored";
        return StringUtils.INSTANCE.recognisePattern(costLine, pattern, "{compost_cost}");
    }

    private static double getCompostCost(IInventory inventory) {
        return StringUtils.INSTANCE.parseAbbreviatedNumber(getCompostCostString(inventory));
    }

    private static String getOrganicMatterFillLevelString(IInventory composterInventory) {
        ItemStack infoItem = composterInventory.getStackInSlot(46);
        ArrayList<String> loreList = MinecraftUtils.INSTANCE.getLore(infoItem);
        String amountLine = "§2§l§m §l§m §l§m §l§m §l§m §l§m §l§m §l§m §l§m §l§m §l§m §l§m §l§m §l§m §l§m §l§m §l§m §l§m §l§m §l§m §r §e0§6/§e40k";
        for (String line : loreList){
            if (line.contains("§6/§e")) {
                amountLine = line;
                break;
            }
        }

        String pattern = StringUtils.INSTANCE.removeColorCodes("§2§l§m §l§m §l§m §l§m §l§m §l§m §l§m §l§m §l§m §l§m §l§m §l§m §l§m §l§m §l§m §l§m §l§m §l§m §l§m §l§m §r §e{compost_amount}§6/");
        String amountString = StringUtils.INSTANCE.recognisePattern(StringUtils.INSTANCE.removeColorCodes(amountLine), pattern, "{compost_amount}");
        
        return amountString.replaceAll("\\d.", "");

    }

    private static double getOrganicMatterFillLevel(IInventory inventory) {
        String organicMatterFillLevelString = getOrganicMatterFillLevelString(inventory);
        if (organicMatterFillLevelString.isEmpty()) {
            return 0;
        }
        return StringUtils.INSTANCE.parseAbbreviatedNumber(organicMatterFillLevelString);
    }

    private static String getOrganicMatterLimitString(IInventory composterInventory) {
        ItemStack infoItem = composterInventory.getStackInSlot(46);
        ArrayList<String> loreList = MinecraftUtils.INSTANCE.getLore(infoItem);
        String amountLine = "0/40k";
        for (String line : loreList){
            if (line.contains("§6/§e")) {
                amountLine = line;
                break;
            }
        }

        amountLine = StringUtils.INSTANCE.removeColorCodes(amountLine);
        amountLine = StringUtils.INSTANCE.stripLeading(amountLine);
        amountLine = StringUtils.INSTANCE.stripTrailing(amountLine);

        int indexOfStart = amountLine.indexOf("/");
        amountLine = amountLine.substring(indexOfStart + 1);

        return amountLine;
    }

    public static double getMaxCompostAbleToMake() {
        return maxCompost / compostCost;
    }

    public static double getCurrentCompostAbleToMake() {
        return (maxCompost - fillLevel) / compostCost;
    }

    private static double getOrganicMatterLimit(IInventory inventory) {
        return StringUtils.INSTANCE.parseAbbreviatedNumber(getOrganicMatterLimitString(inventory));
    }



    static Window window = new Window(ElementaVersion.V2);

    UIComponent box = new UIRoundedRectangle(widthScaledConstraint(5).getValue())
            .setColor(new Color(0, 0, 0, 0))
            .setChildOf(window);
    
    UIComponent image = ThemeManager.getCurrentBackgroundUIImage()
            .setChildOf(box);
    
    float pad = 5;
    UIWrappedText textComponent = (UIWrappedText) new UIWrappedText()
        .setChildOf(box);

    @SubscribeEvent
    public void renderInformation(GuiScreenEvent.BackgroundDrawnEvent event) {
        if (!isComposter()) {
            box.hide();
            return;
        }
        if (!PartlySaneSkies.config.bestCropsToCompost) {
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
            

        String textString = "§e§lTop Crops:\n\n";

        textString += getString();
        textString += "\n\n";
        textString += "§e§lCompost:\n\n";
        double compostSellPrice = SkyblockDataManager.getItem("COMPOST").getBazaarSellPrice();

        double compostAmount = getCurrentCompostAbleToMake();
        if (maxCompost == fillLevel) {
            compostAmount = getMaxCompostAbleToMake();
        }
        compostAmount = (float) MathUtils.INSTANCE.round(compostAmount, 0);

        textString += "§7x§d"+ StringUtils.INSTANCE.formatNumber(MathUtils.INSTANCE.round(compostAmount, 0)) +"§7 Compost currently sells for §d" + StringUtils.INSTANCE.formatNumber(MathUtils.INSTANCE.round(compostSellPrice * compostAmount, 1))  + "§7 coins.\n§8(" + StringUtils.INSTANCE.formatNumber(MathUtils.INSTANCE.round(compostSellPrice, 1)) + "/Compost)";

        textString = (textString);
        textComponent.setText(textString);

        window.draw(new UMatrixStack());
    }

    private static float getWidthScaleFactor() {
        return window.getWidth() / 1097f;
    }

    private static PixelConstraint widthScaledConstraint(float value) {
        return new PixelConstraint(value * getWidthScaleFactor());
    }
}