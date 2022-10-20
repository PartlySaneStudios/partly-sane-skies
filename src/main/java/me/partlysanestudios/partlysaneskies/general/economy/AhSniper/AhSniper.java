package me.partlysanestudios.partlysaneskies.general.economy.AhSniper;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.reflect.FieldUtils;

import me.partlysanestudios.partlysaneskies.Main;
import me.partlysanestudios.partlysaneskies.general.economy.ItemLowestBin;
import me.partlysanestudios.partlysaneskies.utils.Utils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;

public class AhSniper {
    public static void runAlgorithm() {
        if (!isAhGui()) {
            return;
        }

        GuiScreen screen = Main.minecraft.currentScreen;
        IInventory[] separateInventory = getSeparateUpperLowerGui(screen);

        IInventory upper = separateInventory[0];

        List<ItemStack> items = getAuctionContents(upper);

        List<ItemStack> lowBinItems = new ArrayList<ItemStack>();
        for (ItemStack itemStack : items) {
            if (isBin(itemStack)) {
                continue;
            }
            long sellingPrice = getPrice(itemStack);
            float averageAhPrice = ItemLowestBin.lowestBin.get(Utils.getItemId(itemStack));

            if (sellingPrice * .60 <= averageAhPrice) {
                lowBinItems.add(itemStack);
            }
        }

        Utils.visPrint(lowBinItems);
    }

    private static List<String> getLore(ItemStack itemStack) {
        NBTTagList tagList = itemStack.getTagCompound().getCompoundTag("display").getTagList("Lore", 8);
        ArrayList<String> loreList = new ArrayList<String>();
        for (int i = 0; i < tagList.tagCount(); i++) {
            loreList.add(tagList.getStringTagAt(i));
        }

        return loreList;
    }

    private static boolean isBin(ItemStack itemStack) {
        List<String> loreList = getLore(itemStack);
        for (String line : loreList) {
            if (Utils.removeColorCodes(line).contains("Buy it now: ")) {
                return true;
            }
        }
        return false;
    }

    private static long getPrice(ItemStack itemStack) {
        List<String> loreList = getLore(itemStack);
        String buyItNowPrice = "";

        for (String line : loreList) {
            if (Utils.removeColorCodes(line).contains("Buy it now: ")) {
                buyItNowPrice = Utils.removeColorCodes(line).replaceAll("[^0-9]", "");
            }
        }

        return Long.parseLong(buyItNowPrice);
    }

    private static boolean isAhGui() {
        if (!(Main.minecraft.currentScreen instanceof GuiChest)) {
            return false;
        }

        IInventory upper = getSeparateUpperLowerGui(Main.minecraft.currentScreen)[0];
        return Utils.removeColorCodes(upper.getDisplayName().getFormattedText()).contains("Auction Browser");
    }

    private static IInventory[] getSeparateUpperLowerGui(GuiScreen gui) {
        IInventory upperInventory;
        IInventory lowerInventory;
        try {
            upperInventory = (IInventory) FieldUtils.readDeclaredField(gui,
                    Utils.getDecodedFieldName("upperChestInventory"), true);
            lowerInventory = (IInventory) FieldUtils.readDeclaredField(gui,
                    Utils.getDecodedFieldName("lowerChestInventory"), true);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }

        return new IInventory[] { upperInventory, lowerInventory };
    }

    private static List<ItemStack> getAuctionContents(IInventory inventory) {
        List<ItemStack> list = new ArrayList<ItemStack>();
        for (int i = 0; i < 54; i++) {
            if (convertSlotToChestCoordinate(i)[0] <= 2 || convertSlotToChestCoordinate(i)[0] == 9
                    || convertSlotToChestCoordinate(i)[1] == 1 || convertSlotToChestCoordinate(i)[1] == 6) {
                continue;
            }
            list.add(inventory.getStackInSlot(i));
        }

        return list;
    }

    // private static int convertChestCoordinateToSlot(int x, int y) {
    // return x + 9 * y - 10;
    // }

    // private static int convertAhCoordinateToSlot(int x, int y) {
    // x += 2;
    // y += 1;
    // return x + 9 * y - 10;
    // }

    private static int[] convertSlotToChestCoordinate(int slot) {
        int x = (slot + 1) % 9;
        if (x == 0)
            x = 9;
        int y = (slot + 1) / 9 + 1;

        return new int[] { x, y };
    }
}
