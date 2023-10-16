//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.economy;

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
import me.partlysanestudios.partlysaneskies.data.skyblockdata.SkyblockItem;
import me.partlysanestudios.partlysaneskies.system.ThemeManager;
import me.partlysanestudios.partlysaneskies.utils.ElementaUtils;
import me.partlysanestudios.partlysaneskies.utils.ScoreboardUtils;
import me.partlysanestudios.partlysaneskies.utils.StringUtils;
import me.partlysanestudios.partlysaneskies.utils.Utils;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.*;

public class BitsShopValue {

    // Sorts the hashmap in descending order
    public static LinkedHashMap<String, Double> sortMap(HashMap<String, Double> map) {
        List<Map.Entry<String, Double>> list = new LinkedList<>(map.entrySet());
        list.sort((o1, o2) -> (o2.getValue()).compareTo(o1.getValue()));

        LinkedHashMap<String, Double> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Double> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    public static String getString() {
        StringBuilder str = new StringBuilder();

        HashMap<String, Double> map = new HashMap<>();
        long bitCount = ScoreboardUtils.getBits();
        boolean filterAffordable = PartlySaneSkies.config.bitShopOnlyShowAffordable;

        if (SkyblockDataManager.bitIds.isEmpty()) {
            try {
                SkyblockDataManager.initBitValues();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (String id : SkyblockDataManager.bitIds) {
            SkyblockItem item = SkyblockDataManager.getItem(id);
            if (filterAffordable && bitCount < item.getBitCost()) {
                continue;
            }
            map.put(id, item.getSellPrice() / item.getBitCost());
        }
        LinkedHashMap<String, Double> sortedMap = sortMap(map);
        
        int i = 1;
        for (Map.Entry<String, Double> en : sortedMap.entrySet()) {
            SkyblockItem item = SkyblockDataManager.getItem(en.getKey());
            str.append("§6").append(i).append(". §d").append(item.getName()).append("§7 costs §d").append(StringUtils.formatNumber(item.getBitCost())).append("§7 bits and sells for §d").append(StringUtils.formatNumber(Utils.round(item.getSellPrice(), 1))).append("§7 coins \n§8 (").append(StringUtils.formatNumber(Utils.round(en.getValue(), 1))).append(" coins per bit)\n");
            i++;
            if (i > 5) {
                break;
            }
        }

        if (filterAffordable) {
            str.append("\n\n§8§oOnly showing affordable items");
        }
        str = new StringBuilder((str.toString()));
        
        return str.toString();
    }

    public static boolean isCommunityShop() {
        if (PartlySaneSkies.minecraft.currentScreen == null) {
            return false;
        }
        if (!(PartlySaneSkies.minecraft.currentScreen instanceof GuiChest)) {
            return false;
        }

        IInventory[] inventories = PartlySaneSkies.getSeparateUpperLowerInventories(PartlySaneSkies.minecraft.currentScreen);
        assert inventories != null;
        IInventory shop = inventories[0];

        return StringUtils.removeColorCodes(shop.getDisplayName().getFormattedText()).contains("Community Shop");
    }

    static Window window = new Window(ElementaVersion.V2);

    UIComponent box = new UIRoundedRectangle(ElementaUtils.widthScaledConstraint(5, window).getValue())
            .setColor(new Color(0, 0, 0, 0))
            .setChildOf(window);
    
    UIComponent image = ThemeManager.getCurrentBackgroundUIImage()
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
        box.setX(ElementaUtils.widthScaledConstraint(700, window))
            .setY(new CenterConstraint())
            .setWidth(ElementaUtils.widthScaledConstraint(250, window))
            .setHeight(ElementaUtils.widthScaledConstraint(300, window));

        image.setX(new CenterConstraint())
            .setY(new CenterConstraint())
            .setWidth(new PixelConstraint(box.getWidth()))
            .setHeight(new PixelConstraint(box.getHeight()));
        
        textComponent.setX(ElementaUtils.widthScaledConstraint(pad, window))
            .setTextScale(ElementaUtils.widthScaledConstraint(1f, window))
            .setY(ElementaUtils.widthScaledConstraint(2 * pad, window))
            .setWidth(new PixelConstraint(box.getWidth() - ElementaUtils.widthScaledConstraint(2 * pad, window).getValue()));

        String message = "§e§lTop Items:\n\n" + getString();

        textComponent.setText(message);

        window.draw(new UMatrixStack());
    }
}