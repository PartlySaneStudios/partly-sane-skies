package me.partlysanestudios.partlysaneskies.general.economy.auctionhouse;

import java.util.ArrayList;
import java.util.List;

import gg.essential.elementa.ElementaVersion;
import me.partlysanestudios.partlysaneskies.Main;
import me.partlysanestudios.partlysaneskies.utils.Utils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.IInventory;

public class AhManager {

    private static boolean guiAlreadyOpen;

    static AhGui gui;
    static IInventory inventory;

    public static void runDisplayGuiCheck() {

        if (!isAhGui()) {
            return;
        }
        if (Main.minecraft.currentScreen instanceof AhGui) {
            guiAlreadyOpen = true;
        } else {
            guiAlreadyOpen = false;
            gui = null;
        }

        if (guiAlreadyOpen) {
            return;
        }

        if (Main.isDebugMode) {
            return;
        }
        if (!Main.config.customAhGui) {
            return;
        }
        guiAlreadyOpen = true;
        inventory = Main.getSeparateUpperLowerInventories(Main.minecraft.currentScreen)[0];
        boolean loaded = ahChestFullyLoaded(inventory);
        gui = new AhGui(ElementaVersion.V2);
        new Thread() {
            @Override
            public void run() {
                if (!loaded) {
                    try {
                        Thread.sleep(100);
                        inventory = Main.getSeparateUpperLowerInventories(Main.minecraft.currentScreen)[0];
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                Main.minecraft.displayGuiScreen(gui);
                gui.refreshGui(inventory);
            }
        }.start();
    }

    public static boolean isAhGui() {
        if (!(Main.minecraft.currentScreen instanceof GuiChest)) {
            return false;
        }

        IInventory upper = Main.getSeparateUpperLowerInventories(Main.minecraft.currentScreen)[0];
        return Utils.removeColorCodes(upper.getDisplayName().getFormattedText()).contains("Auctions Browser") || Utils.removeColorCodes(upper.getDisplayName().getFormattedText()).contains("Auctions: \"");
    }

    static int TOTAL_ROWS = 4;
    static int TOTAL_COLUMNS = 6;
    static IInventory[] separateInventories;

    public static Auction[][] getAuctions(IInventory inventory) {
        GuiScreen screen = Main.minecraft.currentScreen;
        if (isAhGui()) {
            separateInventories = Main.getSeparateUpperLowerInventories(screen);
        }
        List<Auction> items = getAuctionContents(inventory);

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

    private static boolean ahChestFullyLoaded(IInventory inventory) {
        for (int i = 0; i < 54; i++) {
            if (convertSlotToChestCoordinate(i)[0] <= 2
                    || convertSlotToChestCoordinate(i)[0] == 9
                    || convertSlotToChestCoordinate(i)[1] == 1
                    || convertSlotToChestCoordinate(i)[1] == 6) {
                continue;
            }
            if (inventory.getStackInSlot(i) == null) {
                return false;
            }
        }
        return true;
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
