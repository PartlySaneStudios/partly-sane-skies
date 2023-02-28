package me.partlysanestudios.partlysaneskies.utils;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

import javax.imageio.ImageIO;

import gg.essential.elementa.UIComponent;
import gg.essential.elementa.components.UIImage;
import gg.essential.elementa.constraints.CenterConstraint;
import gg.essential.elementa.constraints.PixelConstraint;
import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import me.partlysanestudios.partlysaneskies.WikiArticleOpener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.resources.IResource;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ResourceLocation;

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

        try {
            PartlySaneSkies.minecraft.ingameGUI
                    .getChatGUI()
                    .printChatMessage(new ChatComponentText("\n            " + print.toString() + ""));
            StringSelection stringSelection = new StringSelection(print.toString());
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        } catch (NullPointerException e) {
        } finally {
        }
    }

    public static void sendClientMessage(String text) {
        sendClientMessage(text, false);
    }

    // If true, Sends a message discretely without the Prefix Partly Sane Skies >:
    public static void sendClientMessage(String text, boolean silent) {
        text = Utils.colorCodes(text);
        if (silent) {
            try {
                PartlySaneSkies.minecraft.ingameGUI
                    .getChatGUI()
                    .printChatMessage(new ChatComponentText(text));

            } catch (NullPointerException e) {
            }
        }
        else {
            try {
                PartlySaneSkies.minecraft.ingameGUI
                        .getChatGUI()
                        .printChatMessage(new ChatComponentText(colorCodes(PartlySaneSkies.CHAT_PREFIX) + "" + text));
            } catch (NullPointerException e) {
            } 
        }
    }

    public static String colorCodes(String text) {
        return text.replace("&", "§");
    }

    public static String removeColorCodes(String text) {
        StringBuilder textBuilder = new StringBuilder(text);
        while (textBuilder.indexOf("§") != -1) {
            textBuilder.deleteCharAt(textBuilder.indexOf("§") + 1);
            textBuilder.deleteCharAt(textBuilder.indexOf("§"));
        }
        return textBuilder.toString();
    }

    public static double toPercentageOfWidth(double value) {
        return value / (PartlySaneSkies.minecraft.displayWidth / 2);
    }

    public static double toPercentageOfHeight(double value) {
        return value / (PartlySaneSkies.minecraft.displayHeight / 2);
    }

    public static double fromPercentageOfWidth(double value) {
        return value * (PartlySaneSkies.minecraft.displayWidth / 2);
    }

    public static double fromPercentageOfHeight(double value) {
        return value * (PartlySaneSkies.minecraft.displayHeight / 2);
    }

    public static void copyStringToClipboard(String string) {
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(getTransferableString(string), null);
    }

    private static Transferable getTransferableString(final String string) {
        return new Transferable() {
            @Override
            public DataFlavor[] getTransferDataFlavors() {
                return new DataFlavor[] { DataFlavor.stringFlavor };
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

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            httpURLConnection.disconnect();
            return response.toString();

        } else {
            sendClientMessage("Error: " + httpURLConnection.getResponseMessage() + ":" + httpURLConnection.getResponseCode() + "\nContact PSS admins for more information");
            httpURLConnection.disconnect();
            return "Error" + responseCode;
        }
    }

    public static double round(double num, int decimalPlaces) {
        return Math.round((num * ((double) (Math.pow(10, decimalPlaces))))) / ((double) (Math.pow(10, decimalPlaces)));
    }

    public static String wrapText(String text, int charNumber) {
        char[] charArray = text.toCharArray();
        List<String> words = new ArrayList<String>();
        List<Character> chars = new ArrayList<Character>();
        for (char c : charArray) {
            if (c == ' ') {
                words.add(charArrayToString(chars));
                chars.clear();
            } else {
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
        for (String word : words) {
            charsOnLine += word.length();
            if (charsOnLine >= charNumber) {
                line.add(word);
                String lineString = previousFormatCode;
                for (String wordOnLine : line) {
                    lineString += (wordOnLine + " ");
                }
                wrappedText += colorCodes(lineString) + "\n";
                line.clear();
                charsOnLine = 0;
                previousFormatCode = currentLineFormatCode;
            } else {
                line.add(word);
            }
            for (char c : word.toCharArray()) {
                if (wasPreviousCharFormatCode) {
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
        for (String wordOnLine : line) {
            lineString += (wordOnLine + " ");
        }
        wrappedText += colorCodes(lineString);
        line.clear();
        return wrappedText;
    }

    public static String charArrayToString(List<Character> chars) {
        String string = "";
        for (char c : chars)
            string += c;
        return string;
    }

    public static int randint(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public static String stripLeading(String str) {
        if (str.equals("")) {
            return str;
        }
        if (Character.isWhitespace(str.charAt(0))) {
            str = new StringBuilder(str).replace(0, 1, "").toString();
            str = stripLeading(str);
        }

        return str;
    }

    public static String stripTrailing(String str) {
        if (str.equals("")) {
            return str;
        }
        if (Character.isWhitespace(str.charAt(str.length() - 1))) {
            str = new StringBuilder(str).replace(str.length() - 1, str.length(), "").toString();
            str = stripTrailing(str);
        }

        return str;
    }

    public static String getDecodedFieldName(String codedName) {
        return new HashMap<String, String>() {
            {
                put("footer", "field_175255_h");
                put("header", "field_175256_i");
                put("upperChestInventory", "field_147015_w");
                put("lowerChestInventory", "field_147016_v");
                put("persistantChatGUI", "field_73840_e");
                put("sentMessages", "field_146248_g");
                put("streamIndicator", "field_152127_m");
                put("updateCounter", "field_73837_f");
                put("overlayPlayerList", "field_175196_v");
                put("guiIngame", "field_175251_g");
                put("chatMessages", "field_146253_i");
                put("theSlot", "field_147006_u");
                put("stackTagCompound", "field_77990_d");
            }
        }.get(codedName);
    }

    public static String getItemId(ItemStack item) {
        return WikiArticleOpener.getItemAttributes(item).getString("id");
    }

    public static void clickOnSlot(int slot) {
        PlayerControllerMP controller = PartlySaneSkies.minecraft.playerController;

        // controller.windowClick(Main.minecraft.thePlayer.openContainer.windowId, slot,
        // 2, 3, Main.minecraft.thePlayer);
        controller.windowClick(PartlySaneSkies.minecraft.thePlayer.openContainer.windowId, slot, 0, 0, PartlySaneSkies.minecraft.thePlayer);
    }

    public static void rightClickOnSlot(int slot) {
        PlayerControllerMP controller = PartlySaneSkies.minecraft.playerController;

        // controller.windowClick(Main.minecraft.thePlayer.openContainer.windowId, slot,
        // 2, 3, Main.minecraft.thePlayer);
        controller.windowClick(PartlySaneSkies.minecraft.thePlayer.openContainer.windowId, slot, 0, 0, PartlySaneSkies.minecraft.thePlayer);
    }

    public static ArrayList<String> getLore(ItemStack itemStack) {
        NBTTagList tagList = itemStack.getTagCompound().getCompoundTag("display").getTagList("Lore", 8);
        ArrayList<String> loreList = new ArrayList<String>();
        for (int i = 0; i < tagList.tagCount(); i++) {
            loreList.add(tagList.getStringTagAt(i));
        }

        return loreList;
    }

    public static Color applyOpacityToColor(Color color, int opacity) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), opacity);
    }

    public static UIComponent applyBackground(UIComponent component) {
        UIImage image = (UIImage) Utils.uiimageFromResourceLocation(new ResourceLocation("partlysaneskies:textures/gui/base_color_background.png"))
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setWidth(new PixelConstraint(component.getWidth()))
                .setHeight(new PixelConstraint(component.getHeight()));

        component.insertChildAt(image, 0);
        return component;
    }

    public static String formatNumber(double num) {

        DecimalFormat decimalFormat = new DecimalFormat("#,###.00");

        String hundredsPlaceFormat = "";
        switch (PartlySaneSkies.config.hundredsPlaceFormat) {
            case 0:
                hundredsPlaceFormat = ",";
                break;

            case 1:
                hundredsPlaceFormat = " ";
                break;

            case 2:
                hundredsPlaceFormat = ".";
                break;
        }

        String decimalPlaceFormat = "";
        switch (PartlySaneSkies.config.decimalPlaceFormat) {
            case 0:
                decimalPlaceFormat = ",";
                break;

            case 1:
                decimalPlaceFormat = ".";
                break;
        }
        String formattedNum = decimalFormat.format(num);

        if (formattedNum.charAt(0) == '.') {
            formattedNum = formattedNum.replaceFirst(".", "");
        }

        if (formattedNum.endsWith(".00")) {
            formattedNum = formattedNum.replace(".00", "");
        }

        if (formattedNum.equalsIgnoreCase("00")) {
            formattedNum = "0";
        }

        formattedNum = formattedNum.replace(",", "_");
        formattedNum = formattedNum.replace(".", decimalPlaceFormat);
        formattedNum = formattedNum.replace("_", hundredsPlaceFormat);

        

        return formattedNum;
    }

    public static String getLoreAsString(ItemStack item) {
        List<String> loreList = getLore(item);
        String loreString = "";
        for (String loreLine : loreList) {
            loreString += loreLine + "\n";
        }
    
        return loreString;
    }

    public static UIImage uiimageFromResourceLocation(ResourceLocation location)  {
        try {
            IResource resource = Minecraft.getMinecraft().getResourceManager().getResource(location);
        
            UIImage image = new UIImage(CompletableFuture.supplyAsync(() -> {
                try {
                    return ImageIO.read(resource.getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finally {
                    try {
                        resource.getInputStream().close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }));
            return image;
        } catch (NullPointerException | IOException exception) {
            exception.printStackTrace();
            return UIImage.ofResource("/assets/partlysaneskies/textures/null_texture.png");
        }
        
        

        
    }
}
