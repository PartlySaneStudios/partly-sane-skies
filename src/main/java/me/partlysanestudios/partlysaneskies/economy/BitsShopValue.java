//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

 
package me.partlysanestudios.partlysaneskies.economy;

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
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BitsShopValue {

    // Sorts the hashmap in decending order
    public static LinkedHashMap<String, Double> sortMap(HashMap<String, Double> map) {
        List<Map.Entry<String, Double>> list = new LinkedList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
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

        HashMap<String, Double> map = new HashMap<String, Double> ();
        long bitCount = PartlySaneSkies.getBits();
        boolean filterAffordable = PartlySaneSkies.config.bitShopOnlyShowAffordable;

        for (String id : SkyblockItem.bitIds) {
            SkyblockItem item = SkyblockItem.getItem(id);
            if (filterAffordable && bitCount < item.getBitCost()) {
                continue;
            }
            map.put(id, item.getPrice() / item.getBitCost());
        }
        LinkedHashMap<String, Double> sortedMap = sortMap(map);
        
        int i = 1;
        for (Map.Entry<String, Double> en : sortedMap.entrySet()) {
            SkyblockItem item = SkyblockItem.getItem(en.getKey());
            str += "&6" + i + ". &d" + item.getName() + "&7 costs &d" + StringUtils.formatNumber(item.getBitCost()) + "&7 bits and sells for &d" + StringUtils.formatNumber(Utils.round(item.getPrice(), 1)) + "&7 coins \n&8 (" + StringUtils.formatNumber(Utils.round(en.getValue(), 1)) + " coins per bit)\n";
            i++;
            if (i > 5) {
                break;
            }
        }

        if (filterAffordable) {
            str += "\n\n&8&oOnly showing affordable items";
        }
        str = StringUtils.colorCodes(str);
        
        return str;
    }

    public static boolean isCommunityShop() {
        if (PartlySaneSkies.minecraft.currentScreen == null) {
            return false;
        }
        if (!(PartlySaneSkies.minecraft.currentScreen instanceof GuiChest)) {
            return false;
        }

        IInventory[] inventories = PartlySaneSkies.getSeparateUpperLowerInventories(PartlySaneSkies.minecraft.currentScreen);
        IInventory shop = inventories[0];

        if (!StringUtils.removeColorCodes(shop.getDisplayName().getFormattedText()).contains("Community Shop")) {
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
        if (!isCommunityShop()) {
            box.hide();
            return;
        }
        if (!PartlySaneSkies.config.bestBitShopItem) {
            return;
        }

        box.unhide(true);
        box.setX(widthScaledConstraint(700))
            .setY(new CenterConstraint())
            .setWidth(widthScaledConstraint(250))
            .setHeight(widthScaledConstraint(300));

        image.setX(new CenterConstraint())
            .setY(new CenterConstraint())
            .setWidth(new PixelConstraint(box.getWidth()))
            .setHeight(new PixelConstraint(box.getHeight()));
        
        textComponent.setX(widthScaledConstraint(pad))
            .setTextScale(widthScaledConstraint(1f))
            .setY(widthScaledConstraint(2 * pad))
            .setWidth(new PixelConstraint(box.getWidth() - widthScaledConstraint(2 * pad).getValue()));
            

        String textString = "&e&lTop Items:\n\n";

        textString += getString();
        textString += "\n\n";
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