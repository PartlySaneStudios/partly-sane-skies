//
// Written by Su386.
// See LICENSE for copright and license notices.
//

package me.partlysanestudios.partlysaneskies.auctionhouse;

import java.util.ArrayList;
import java.util.List;

import gg.essential.elementa.ElementaVersion;
import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import me.partlysanestudios.partlysaneskies.utils.StringUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;

public class AhManager {

    private static boolean guiAlreadyOpen;

    static AhGui gui;
    static IInventory inventory;

    public static void runDisplayGuiCheck() {

        if (!isAhGui()) {
            return;
        }
        if (PartlySaneSkies.minecraft.currentScreen instanceof AhGui) {
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
        guiAlreadyOpen = true;
        inventory = PartlySaneSkies.getSeparateUpperLowerInventories(PartlySaneSkies.minecraft.currentScreen)[0];
        boolean preloaded = ahChestFullyLoaded(inventory);
        gui = new AhGui(ElementaVersion.V2);
        
        if (preloaded) {
            PartlySaneSkies.minecraft.displayGuiScreen(gui);
            gui.loadGui(inventory);
            return;
        }
        
        openAh();
    }

    public static void openAh() {
        
            if (ahChestFullyLoaded(inventory)) {
                inventory = PartlySaneSkies.getSeparateUpperLowerInventories(PartlySaneSkies.minecraft.currentScreen)[0];
                PartlySaneSkies.minecraft.displayGuiScreen(gui);
                gui.loadGui(inventory);
                
            } else {
                new Thread() {
                    @Override
                    public void run() {
                        PartlySaneSkies.minecraft.addScheduledTask(() -> {
                            inventory = PartlySaneSkies.getSeparateUpperLowerInventories(PartlySaneSkies.minecraft.currentScreen)[0];
                            openAh();
                        });
                    }
                    
                }.start();
            }

    }

    public static boolean isAhGui() {
        if (!(PartlySaneSkies.minecraft.currentScreen instanceof GuiChest)) {
            return false;
        }

        IInventory upper = PartlySaneSkies.getSeparateUpperLowerInventories(PartlySaneSkies.minecraft.currentScreen)[0];
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

    public static int[] convertSlotToChestCoordinate(int slot) {
        int x = (slot + 1) % 9;
        if (x == 0)
            x = 9;
        int y = (slot + 1) / 9 + 1;

        return new int[] { x, y };
    }
}
