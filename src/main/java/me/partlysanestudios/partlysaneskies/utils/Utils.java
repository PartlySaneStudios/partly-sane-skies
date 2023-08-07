//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import gg.essential.elementa.UIComponent;
import gg.essential.elementa.components.UIImage;
import gg.essential.elementa.constraints.CenterConstraint;
import gg.essential.elementa.constraints.PixelConstraint;
import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import me.partlysanestudios.partlysaneskies.WikiArticleOpener;
import me.partlysanestudios.partlysaneskies.system.ThemeManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.resources.IResource;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Level;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

public class Utils {
    public static HashMap<String, Color> colorCodetoColor = new HashMap<>();

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
        Utils.log(Level.INFO,"\n\n\n" + print.toString() + "\n\n\n");

        try {
            PartlySaneSkies.minecraft.ingameGUI
                    .getChatGUI()
                    .printChatMessage(new ChatComponentText("\n            " + print));
            StringSelection stringSelection = new StringSelection(print.toString());
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        } catch (NullPointerException ignored) {
        }
    }

    public static void sendClientMessage(String text) {
        sendClientMessage(text, false);
    }

    // If true, Sends a message discretely without the Prefix Partly Sane Skies >:
    public static void sendClientMessage(String text, boolean silent) {
        text = StringUtils.colorCodes(text);
        if (silent) {
            try {
                PartlySaneSkies.minecraft.ingameGUI
                    .getChatGUI()
                    .printChatMessage(new ChatComponentText(text));

            } catch (NullPointerException ignored) {
            }
        }
        else {
            try {
                PartlySaneSkies.minecraft.ingameGUI
                        .getChatGUI()
                        .printChatMessage(new ChatComponentText(StringUtils.colorCodes(PartlySaneSkies.CHAT_PREFIX) + text));
            } catch (NullPointerException ignored) {
            }
        }
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

    @Deprecated
    // Deprecated: Use RequestManager and Requests instead
    public static String getRequest(String urlString) throws IOException {

        URL url = new URL(urlString);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestProperty("User-Agent", "Partly-Sane-Skies/" + PartlySaneSkies.VERSION);
        int responseCode = httpURLConnection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            httpURLConnection.disconnect();
            return response.toString();

        } else {
            if (PartlySaneSkies.config.printApiErrors) {
                sendClientMessage("Error: " + httpURLConnection.getResponseMessage() + ":" + httpURLConnection.getResponseCode() + "\nContact PSS admins for more information");
            } else {
                Utils.log(Level.ERROR,"Error: " + httpURLConnection.getResponseMessage() + ":" + httpURLConnection.getResponseCode() + "\nContact PSS admins for more information");
            }
            Utils.log(Level.ERROR,"Error: " + httpURLConnection.getResponseMessage() + ":" + httpURLConnection.getResponseCode() + "\nURL: " + urlString);
            httpURLConnection.disconnect();
            return "Error" + responseCode;
        }
    }

    public static double round(double num, int decimalPlaces) {
        return Math.round(num * Math.pow(10, decimalPlaces) / Math.pow(10, decimalPlaces));
    }

    public static int randint(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
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
        if (WikiArticleOpener.getItemAttributes(item) == null) {
            return "";
        }
        return WikiArticleOpener.getItemAttributes(item).getString("id");
    }

    public static void clickOnSlot(int slot) {
        PlayerControllerMP controller = PartlySaneSkies.minecraft.playerController;

        controller.windowClick(PartlySaneSkies.minecraft.thePlayer.openContainer.windowId, slot, 0, 0, PartlySaneSkies.minecraft.thePlayer);
    }

    public static void log(Level level, String message) {
        for (String line : message.split("\n")) {
            PartlySaneSkies.LOGGER.log(level, line);
        }

    }

    public static void rightClickOnSlot(int slot) {
        PlayerControllerMP controller = PartlySaneSkies.minecraft.playerController;

        controller.windowClick(PartlySaneSkies.minecraft.thePlayer.openContainer.windowId, slot, 1, 0, PartlySaneSkies.minecraft.thePlayer);
    }

    public static ArrayList<String> getLore(ItemStack itemStack) {
        NBTTagList tagList = itemStack.getTagCompound().getCompoundTag("display").getTagList("Lore", 8);
        ArrayList<String> loreList = new ArrayList<>();
        for (int i = 0; i < tagList.tagCount(); i++) {
            loreList.add(tagList.getStringTagAt(i));
        }

        return loreList;
    }

    public static Color applyOpacityToColor(Color color, int opacity) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), opacity);
    }

    public static void applyBackground(UIComponent component) {
        UIImage image = (UIImage) ThemeManager.getCurrentBackgroundUIImage()
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setWidth(new PixelConstraint(component.getWidth()))
                .setHeight(new PixelConstraint(component.getHeight()));

        component.insertChildAt(image, 0);
    }

    public static String getLoreAsString(ItemStack item) {
        List<String> loreList = getLore(item);
        StringBuilder loreString = new StringBuilder();
        for (String loreLine : loreList) {
            loreString.append(loreLine).append("\n");
        }
    
        return loreString.toString();
    }

    public static UIImage uiimageFromResourceLocation(ResourceLocation location)  {
        try {
            IResource resource = Minecraft.getMinecraft().getResourceManager().getResource(location);

            return new UIImage(CompletableFuture.supplyAsync(() -> {
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
        } catch (NullPointerException | IOException exception) {


            return UIImage.ofResource("/assets/partlysaneskies/" + location.getResourcePath());
//            return UIImage.ofResource("/assets/partlysaneskies/textures/null_texture.png");
        }
    }

    // Opens a link with a given URL
    public static void openLink(String url) {
        URI uri;
        try {
            uri = new URI(url);
            try {
                Class<?> oclass = Class.forName("java.awt.Desktop");
                Object object = oclass.getMethod("getDesktop", new Class[0]).invoke(null);
                oclass.getMethod("browse", new Class[] { URI.class }).invoke(object, uri);
            } catch (Throwable throwable) {
                Utils.sendClientMessage("Couldn't open link");
                throwable.printStackTrace();
            }
        } catch (URISyntaxException except) {
            Utils.sendClientMessage("Couldn't open link");
            except.printStackTrace();
        }
    }

    // Takes the last time the event happened in Unix epoch time in milliseconds,
    // and takes the length that the event should last in millisecond
    // Returns false if the event is over, returns true if it is still ongoing
    public static boolean onCooldown(long lastTime, long length) {
        return PartlySaneSkies.getTime() <= lastTime + length;
    }

//    Gets the json element from a path string in format /key/key/key/key/
    public static JsonElement getJsonFromPath(JsonObject source, String path) {
        String[] splitPath = path.split("/");

        JsonObject obj = source;
        // Gets the object up until the very last one
        for (int i = 0; i < splitPath.length - 1; i++) {
            if (splitPath[i].isEmpty()) {
                continue;
            }

            obj = obj.getAsJsonObject(splitPath[i]);
            if (obj == null) {
                return obj;
            }
        }


//        Gets the last object as a JsonElement
        return obj.get(splitPath[splitPath.length - 1]);
    }
}