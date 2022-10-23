package me.partlysanestudios.partlysaneskies.general.economy.AhSniper;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.reflect.FieldUtils;

import gg.essential.elementa.ElementaVersion;
import me.partlysanestudios.partlysaneskies.Main;
import me.partlysanestudios.partlysaneskies.utils.Utils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.IInventory;

public class AhSniper {

    private static boolean guiAlreadyOpen;
    public static void runDisplayGuiCheck() {
        if (!isAhGui()) {
            return;
        }

        if (Main.minecraft.currentScreen instanceof AhGui) {
            guiAlreadyOpen = true;
        }
        else {
            guiAlreadyOpen = false;
        }

        if (guiAlreadyOpen) {
            return;
        }

        if (Main.isDebugMode) {
            return;
        }

        AhGui gui = new AhGui(ElementaVersion.V2);
        Main.minecraft.displayGuiScreen(gui);
    }

    public static boolean isAhGui() {
        if (!(Main.minecraft.currentScreen instanceof GuiChest)) {
            return false;
        }

        IInventory upper = getSeparateUpperLowerGui(Main.minecraft.currentScreen)[0];
        return Utils.removeColorCodes(upper.getDisplayName().getFormattedText()).contains("Auctions Browser");
    }

    static int TOTAL_ROWS = 4;
    static int TOTAL_COLUMNS = 6;

    public static Auction[][] getAuctions() {
        GuiScreen screen = Main.minecraft.currentScreen;
        IInventory[] separateInventory = getSeparateUpperLowerGui(screen);

        IInventory upper = separateInventory[0];

        List<Auction> items = getAuctionContents(upper);

        Auction[][] auctions = new Auction[TOTAL_ROWS][TOTAL_COLUMNS];

        int row = 0;
        int column = 0;
        for (Auction auction : items) {
            if (column > TOTAL_COLUMNS - 1) {
                row++;
                column = 0;
            }
            // if (row > 4-1) {
            // break;
            // }

            auctions[row][column] = auction;
            column++;

        }

        return auctions;
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
            if (convertSlotToChestCoordinate(i)[0] <= 2
                    || convertSlotToChestCoordinate(i)[0] == 9
                    || convertSlotToChestCoordinate(i)[1] == 1
                    || convertSlotToChestCoordinate(i)[1] == 6) {
                continue;
            }
            if (inventory.getStackInSlot(i) == null) {
                continue;
            }
            list.add(new Auction(i, inventory.getStackInSlot(i)));
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

    public static int[] convertSlotToChestCoordinate(int slot) {
        int x = (slot + 1) % 9;
        if (x == 0)
            x = 9;
        int y = (slot + 1) / 9 + 1;

        return new int[] { x, y };
    }
}
