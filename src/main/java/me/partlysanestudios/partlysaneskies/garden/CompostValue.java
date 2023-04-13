//
// Written by Su386.
// See LICENSE for copright and license notices.
//

package me.partlysanestudios.partlysaneskies.garden;

import java.awt.Color;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import gg.essential.elementa.ElementaVersion;
import gg.essential.elementa.UIComponent;
import gg.essential.elementa.components.UIRoundedRectangle;
import gg.essential.elementa.components.UIWrappedText;
import gg.essential.elementa.components.Window;
import gg.essential.elementa.constraints.CenterConstraint;
import gg.essential.elementa.constraints.PixelConstraint;
import gg.essential.universal.UMatrixStack;
import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import me.partlysanestudios.partlysaneskies.SkyblockItem;
import me.partlysanestudios.partlysaneskies.utils.StringUtils;
import me.partlysanestudios.partlysaneskies.utils.Utils;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CompostValue {
    public static HashMap<String, Double> compostValueMap = new HashMap<String, Double>();
    public static HashMap<String, Double> costPerOrganicMatterMap = new HashMap<String, Double>();
    public static HashMap<String, Double> costPerCompostMap = new HashMap<String, Double>();

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
            costPerOrganicMatterMap.put(en.getKey(), SkyblockItem.getItem(en.getKey()).getPrice()/en.getValue());
        }
    }

    public static void requestCostPerCompost() {
        for (Map.Entry<String, Double> en : costPerOrganicMatterMap.entrySet()) {
            costPerCompostMap.put(en.getKey(), en.getValue() * 4000);
        }
    }

    // Sorts the hashmap in decending order
    public static LinkedHashMap<String, Double> sortMap(HashMap<String, Double> map) {
        List<Map.Entry<String, Double>> list = new LinkedList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        LinkedHashMap<String, Double> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Double> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    public static String getString() {
        String str = "";

        requestCostPerOrganicMatter();
        requestCostPerCompost();

        HashMap<String, Double> map = sortMap(costPerCompostMap);

        int i = 1;
        for (Map.Entry<String, Double> en : map.entrySet()) {
            String id = en.getKey();
            double cropPerCompost = 4000d / compostValueMap.get(id);
            String cropName = SkyblockItem.getItem(id).getName();
            double costPerCompost = en.getValue();
            str += "&6"+ i + ". &7x&d" + StringUtils.formatNumber(Math.ceil(cropPerCompost * 10)) + " " + cropName + "&7 costing &d"+ StringUtils.formatNumber(Utils.round(costPerCompost * 10, 1)) + "&7 coins to fill. \n&8(x" + StringUtils.formatNumber(Math.ceil(cropPerCompost)) + "/Compost)\n";

            i++;
            if (i > 5) {
                break;
            }
        }

        str = StringUtils.colorCodes(str);
        
        return str;
    }

    // 22
    public static boolean isComposter() {
        if (PartlySaneSkies.minecraft.currentScreen == null) {
            return false;
        }
        if (!(PartlySaneSkies.minecraft.currentScreen instanceof GuiChest)) {
            return false;
        }

        IInventory[] inventories = PartlySaneSkies.getSeparateUpperLowerInventories(PartlySaneSkies.minecraft.currentScreen);
        IInventory composter = inventories[0];

        // Slots 22 should be the collect compost button
        ItemStack collectCompostButton = composter.getStackInSlot(22);

        if (collectCompostButton == null) {
            return false;
        }

        String collectCompostButtonName = StringUtils.removeColorCodes(collectCompostButton.getDisplayName());
        // If the names are not equal to the desired names, then you know it screen
        if (!collectCompostButtonName.equals("Collect Compost")) {
            return false;
        }
        if (!StringUtils.removeColorCodes(composter.getDisplayName().getFormattedText()).contains("Composter")) {
            return false;
        };


        return true;
    }

    static Window window = new Window(ElementaVersion.V2);

    UIComponent box = new UIRoundedRectangle(widthScaledConstraint(5).getValue())
            .setColor(new Color(0, 0, 0, 0))
            .setChildOf(window);
    
    UIComponent image = Utils.uiimageFromResourceLocation(new ResourceLocation("partlysaneskies:textures/gui/base_color_background.png"))
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
            

        String textString = "&e&lTop Crops:\n\n";

        textString += getString();
        textString += "\n\n";
        textString += "&e&lCompost:\n\n";
        double compostSellPrice = SkyblockItem.getItem("COMPOST").getPrice();
        textString += "&7x10 Compost currently sells for &d" + StringUtils.formatNumber(Utils.round(compostSellPrice * 10, 1))  + "&7 coins.\n&8(" + StringUtils.formatNumber(Utils.round(compostSellPrice, 1)) + "/Compost)";

        textString = StringUtils.colorCodes(textString);
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