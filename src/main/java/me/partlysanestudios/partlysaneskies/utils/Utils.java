package me.partlysanestudios.partlysaneskies.utils;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.partlysanestudios.partlysaneskies.Main;
import net.minecraft.util.ChatComponentText;

public class Utils {
    
    public static HashMap<String, Color> colorCodetoColor = new HashMap<String, Color>();

    public static void init() {
        colorCodetoColor.put("§4", new Color(170, 0, 0));
        colorCodetoColor.put("§c", new Color(255, 85, 85));
        colorCodetoColor.put("§6", new Color(255, 170, 0));
        colorCodetoColor.put("§e", new Color(255, 255, 85));
        colorCodetoColor.put("§2", new Color(0, 170, 0));
        colorCodetoColor.put("§a", new Color(85, 255, 85));
        colorCodetoColor.put("§b", new Color(85, 255, 255));
        colorCodetoColor.put("§3", new Color(0, 170, 170));
        colorCodetoColor.put("§1", new Color(0, 0, 170));
        colorCodetoColor.put("§9", new Color(85, 85, 255));
        colorCodetoColor.put("§d", new Color(255, 85, 255));
        colorCodetoColor.put("§5", new Color(170, 0, 170));
        colorCodetoColor.put("§f", new Color(0, 0, 0));
        colorCodetoColor.put("§7", new Color(170, 170, 170));
        colorCodetoColor.put("§8", new Color(85, 85, 85));
        colorCodetoColor.put("§0", new Color(0, 0, 0));


        colorCodetoColor.put("&4", new Color(170, 0, 0));
        colorCodetoColor.put("&c", new Color(255, 85, 85));
        colorCodetoColor.put("&6", new Color(255, 170, 0));
        colorCodetoColor.put("&e", new Color(255, 255, 85));
        colorCodetoColor.put("&2", new Color(0, 170, 0));
        colorCodetoColor.put("&a", new Color(85, 255, 85));
        colorCodetoColor.put("&b", new Color(85, 255, 255));
        colorCodetoColor.put("&3", new Color(0, 170, 170));
        colorCodetoColor.put("&1", new Color(0, 0, 170));
        colorCodetoColor.put("&9", new Color(85, 85, 255));
        colorCodetoColor.put("&d", new Color(255, 85, 255));
        colorCodetoColor.put("&5", new Color(170, 0, 170));
        colorCodetoColor.put("&f", new Color(0, 0, 0));
        colorCodetoColor.put("&7", new Color(170, 170, 170));
        colorCodetoColor.put("&8", new Color(85, 85, 85));
        colorCodetoColor.put("&0", new Color(0, 0, 0));
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



    public static String getRequest(String urlString) throws IOException {

        URL url = new URL(urlString);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
        int responseCode = httpURLConnection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in .readLine()) != null) {
                response.append(inputLine);
            }
            in .close();

            httpURLConnection.disconnect();
            return response.toString();

        } else {
            visPrint(httpURLConnection.getResponseMessage());
            visPrint(httpURLConnection.getResponseCode());
            httpURLConnection.disconnect();
            return "Error" + responseCode;
        }
    }


    public static double round(double num, int decimalPlaces) {
        return Math.round((num *((double) (Math.pow(10, decimalPlaces)))))/((double) (Math.pow(10, decimalPlaces)));
    }


    public static String wrapText(String text, int charNumber) {
        char[] charArray = text.toCharArray();
        List<String> words = new ArrayList<String>();
        List<Character> chars = new ArrayList<Character>();
        for(char c : charArray) {
            if (c == ' ') {
                words.add(charArrayToString(chars));
                chars.clear();
            }
            else {
                chars.add(c);
            }
        }
        words.add(charArrayToString(chars));

        // ----------------------------------------

        int charsOnLine = 0;
        String wrappedText = "";
        List<String> line = new ArrayList<String>();
        boolean wasPreviousCharFormatCode = false;
        String previousFormatCode = "";
        String currentLineFormatCode = "";
        for(String word : words) {
            charsOnLine += word.length();
            if(charsOnLine >= charNumber) {
                line.add(word);
                String lineString = previousFormatCode;
                for(String wordOnLine : line) {
                    lineString += (wordOnLine +" ");
                }
                wrappedText += colorCodes(lineString) + "\n";
                line.clear();
                charsOnLine = 0;
                previousFormatCode = currentLineFormatCode;
            }
            else {
                line.add(word);
            }
            for (char c : word.toCharArray()) {
                if(wasPreviousCharFormatCode) {
                    currentLineFormatCode += c;
                    wasPreviousCharFormatCode = false;
                }
                if (c == '&') {
                    currentLineFormatCode += c;
                    wasPreviousCharFormatCode = true;
                }
            }
        }
        String lineString = previousFormatCode;
        for(String wordOnLine : line) {
            lineString += (wordOnLine +" ");
        }
        wrappedText += colorCodes(lineString);
        line.clear();
        return wrappedText;
    }

    public static String charArrayToString(List<Character> chars) {
        String string = "";
        for (char c : chars) string += c;
        return string;
    }
}
