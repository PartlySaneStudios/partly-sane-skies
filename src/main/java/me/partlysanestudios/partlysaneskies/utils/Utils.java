package me.partlysanestudios.partlysaneskies.utils;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.util.HashMap;

import me.partlysanestudios.partlysaneskies.Main;
import net.minecraft.util.ChatComponentText;

public class Utils {
    
    public static HashMap<String, Integer> colorCodeToHex = new HashMap<String, Integer>();

    public static void init() {
        colorCodeToHex.put("§4", 0xAA0000);
        colorCodeToHex.put("§c", 0xFF5555);
        colorCodeToHex.put("§6", 0xFFAA00);
        colorCodeToHex.put("§e", 0xFFFF55);
        colorCodeToHex.put("§a", 0x55FF55);
        colorCodeToHex.put("§b", 0x55FFFF);
        colorCodeToHex.put("§3", 0x00AAAA);
        colorCodeToHex.put("§1", 0x0000AA);
        colorCodeToHex.put("§9", 0x5555FF);
        colorCodeToHex.put("§d", 0xFF55FF);
        colorCodeToHex.put("§5", 0xAA00AA);
        colorCodeToHex.put("§f", 0xFFFFFF);
        colorCodeToHex.put("§7", 0xAAAAAA);
        colorCodeToHex.put("§8", 0x555555);
        colorCodeToHex.put("§0", 0x000000);
    }

    public static void visPrint(Object print) {
        System.out.println("\n\n\n" + print.toString() + "\n\n\n");

        try {Main.minecraft.ingameGUI.getChatGUI().printChatMessage( new ChatComponentText("\n            " + print.toString() + ""));}
        catch(NullPointerException e) {}
        finally {}
    }

    public static void sendClientMessage(String text) {
        try {Main.minecraft.ingameGUI.getChatGUI().printChatMessage( new ChatComponentText(text));}
        catch(NullPointerException e) {}
        finally {}
    }
    
    public static String colorCodes(String text) {
        return text.replace("&", "§");
    }

    public static String removeColorCodes(String text) {
        StringBuilder textBuilder = new StringBuilder(text);
        while(textBuilder.indexOf("§") != -1) {
            textBuilder.deleteCharAt(textBuilder.indexOf("§"));
            textBuilder.deleteCharAt(textBuilder.indexOf("§")+1);
        }
        return textBuilder.toString();
    }

    public static boolean detectScoreboardName(String desiredName) {
        String scoreboardName = Main.minecraft.thePlayer.getWorldScoreboard().getObjectiveInDisplaySlot(1).getDisplayName();
    
        if(removeColorCodes(scoreboardName).contains(desiredName)) return true;
    
        return false;
    }

    public static double toPercentageOfWidth(double value) {
        return value/(Main.minecraft.displayWidth/2);
    }

    public static double toPercentageOfHeight(double value) {
        return value/(Main.minecraft.displayHeight/2);
    }

    public static double fromPercentageOfWidth(double value) {
        return value*(Main.minecraft.displayWidth/2);
    }
    public static double fromPercentageOfHeight(double value) {
        return value*(Main.minecraft.displayHeight/2);
    }

    public static void copyStringToClipboard(String string) {
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(getTransferableString(string), null);
    }

    private static Transferable getTransferableString(final String string) {
        return new Transferable() {
            @Override
            public DataFlavor[] getTransferDataFlavors() {
                return new DataFlavor[]{DataFlavor.stringFlavor};
            }

            @Override
            public boolean isDataFlavorSupported(DataFlavor flavor) {
                return DataFlavor.stringFlavor.equals(flavor);
            }

            @Override
            public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
                if (DataFlavor.stringFlavor.equals(flavor)) {
                    return string;
                }
                throw new UnsupportedFlavorException(flavor);
            }
        };
    }
}
