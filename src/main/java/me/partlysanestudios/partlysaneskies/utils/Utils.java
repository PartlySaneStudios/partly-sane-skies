package me.partlysanestudios.partlysaneskies.utils;

import me.partlysanestudios.partlysaneskies.Main;
import net.minecraft.util.ChatComponentText;

public class Utils {

    public static void visPrint(Object print) {
        System.out.println("\n\n\n" + print.toString() + "\n\n\n");
        Main.minecraft.ingameGUI.getChatGUI().printChatMessage( new ChatComponentText("\n" + print.toString() + "\n"));
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
}
