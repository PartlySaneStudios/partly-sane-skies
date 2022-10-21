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

public class AhSniper {
    public static void runAlgorithm() {
        if (!isAhGui()) {
            return;
        }

        GuiScreen screen = Main.minecraft.currentScreen;
        IInventory[] separateInventory = getSeparateUpperLowerGui(screen);

        IInventory upper = separateInventory[0];

        List<Auction> items = getAuctionContents(upper);

        List<Auction> lowBinItems = new ArrayList<Auction>();
        for (Auction auction : items) {
            if (auction.isBin()) {
                continue;
            }
            long sellingPrice = auction.getPrice();
            float averageAhPrice = ItemLowestBin.lowestBin.get(Utils.getItemId(auction.getItem()));

            if (sellingPrice * .60 <= averageAhPrice) {
                lowBinItems.add(auction);
            }
        }

        Utils.visPrint(lowBinItems);
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

    private static List<Auction> getAuctionContents(IInventory inventory) {
        List<Auction> list = new ArrayList<Auction>();
        for (int i = 0; i < 54; i++) {
            if (convertSlotToChestCoordinate(i)[0] <= 2 || convertSlotToChestCoordinate(i)[0] == 9
                    || convertSlotToChestCoordinate(i)[1] == 1 || convertSlotToChestCoordinate(i)[1] == 6) {
                continue;
            }

            // TODO: Get auction seller 
            list.add(new Auction(null, i, inventory.getStackInSlot(i)));
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
