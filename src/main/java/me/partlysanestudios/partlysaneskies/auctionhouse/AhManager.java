//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.auctionhouse;

import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import me.partlysanestudios.partlysaneskies.auctionhouse.menu.AuctionHouseGui;
import me.partlysanestudios.partlysaneskies.utils.StringUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AhManager {

    static AuctionHouseGui gui;
    static IInventory inventory;

    public static void runDisplayGuiCheck() {

        if (!isAhGui()) {
            return;
        }
        boolean guiAlreadyOpen;
        if (PartlySaneSkies.minecraft.currentScreen instanceof AuctionHouseGui) {
            guiAlreadyOpen = true;
        } else {
            guiAlreadyOpen = false;
            gui = null;
        }

        if (guiAlreadyOpen) {
            return;
        }

        if (PartlySaneSkies.isDebugMode) {
            return;
        }
        if (!PartlySaneSkies.config.customAhGui) {
            return;
        }
        inventory = Objects.requireNonNull(PartlySaneSkies.getSeparateUpperLowerInventories(PartlySaneSkies.minecraft.currentScreen))[0];
        boolean preloaded = ahChestFullyLoaded(inventory);

        if (preloaded) {
            gui = new AuctionHouseGui(inventory);
            PartlySaneSkies.minecraft.displayGuiScreen(gui);
            return;
        }

        openAh();
    }

    public static void openAh() {

            if (ahChestFullyLoaded(inventory)) {
                inventory = Objects.requireNonNull(PartlySaneSkies.getSeparateUpperLowerInventories(PartlySaneSkies.minecraft.currentScreen))[0];
                gui = new AuctionHouseGui(inventory);
                PartlySaneSkies.minecraft.displayGuiScreen(gui);

            } else {
                new Thread(() -> PartlySaneSkies.minecraft.addScheduledTask(() -> {
                    inventory = Objects.requireNonNull(PartlySaneSkies.getSeparateUpperLowerInventories(PartlySaneSkies.minecraft.currentScreen))[0];
                    openAh();
                })).start();
            }

    }

    public static boolean isAhGui() {
        if (!(PartlySaneSkies.minecraft.currentScreen instanceof GuiChest)) {
            return false;
        }

        IInventory upper = Objects.requireNonNull(PartlySaneSkies.getSeparateUpperLowerInventories(PartlySaneSkies.minecraft.currentScreen))[0];
        return StringUtils.removeColorCodes(upper.getDisplayName().getFormattedText()).contains("Auctions Browser") || StringUtils.removeColorCodes(upper.getDisplayName().getFormattedText()).contains("Auctions: \"");
    }

    static int TOTAL_ROWS = 4;
    static int TOTAL_COLUMNS = 6;
    static IInventory[] separateInventories;

    public static Auction[][] getAuctions(IInventory inventory) {
        GuiScreen screen = PartlySaneSkies.minecraft.currentScreen;
        if (isAhGui()) {
            separateInventories = PartlySaneSkies.getSeparateUpperLowerInventories(screen);
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
            // If its equal to null and the stack is an arrow (not the end of the page)
            // Then Return
            if (inventory.getStackInSlot(i) == null) {
                if(inventory.getStackInSlot(53) == null){
                    return false;
                } else if (Item.getIdFromItem(inventory.getStackInSlot(53).getItem()) != 264) {
                    continue;
                }
                return false;

            }
        }
        return true;
    }

    private static List<Auction> getAuctionContents(IInventory inventory) {
        List<Auction> list = new ArrayList<>();
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

    public static int[] convertSlotToChestCoordinate(int slot) {
        int x = (slot + 1) % 9;
        if (x == 0)
            x = 9;
        int y = (slot + 1) / 9 + 1;

        return new int[] { x, y };
    }
}
