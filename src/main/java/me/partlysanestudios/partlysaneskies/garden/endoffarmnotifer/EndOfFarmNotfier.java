//
// Written by Su386.
// See LICENSE for copright and license notices.
//


package me.partlysanestudios.partlysaneskies.garden.endoffarmnotifer;

import java.awt.Color;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import gg.essential.elementa.ElementaVersion;
import gg.essential.elementa.UIComponent;
import gg.essential.elementa.components.UIText;
import gg.essential.elementa.components.Window;
import gg.essential.elementa.constraints.CenterConstraint;
import gg.essential.elementa.constraints.PixelConstraint;
import gg.essential.universal.UMatrixStack;
import me.partlysanestudios.partlysaneskies.LocationBannerDisplay;
import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import me.partlysanestudios.partlysaneskies.utils.StringUtils;
import me.partlysanestudios.partlysaneskies.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EndOfFarmNotfier {
    public static ArrayList<Range3d> ranges = new ArrayList<Range3d>();
    public static int[] selectedPos1;
    public static int[] selectedPos2;
    public static long lastChimeTime = 0;

    public static Color color;
    public static Window window = new Window(ElementaVersion.V2);
    public static String displayString = "";
    public static int TEXT_SCALE = 7;
    private static UIComponent displayText = new UIText(displayString)
            .setTextScale(new PixelConstraint(TEXT_SCALE))
            .setX(new CenterConstraint())
            .setY(new PixelConstraint(window.getHeight() * .2f))
            .setColor(Color.white)
            .setChildOf(window);


    public static void run() {
        if (!playerInRange()) {
            displayString = "";
            return;
        }
        if (lastChimeTime + 3 * 1000 > Minecraft.getSystemTime()) {
            return;
        }

        PartlySaneSkies.minecraft.getSoundHandler()
                .playSound(PositionedSoundRecord.create(new ResourceLocation("partlysaneskies", "bell")));
        displayString = "END OF FARM";
        lastChimeTime = Minecraft.getSystemTime();
    }

    public static Range3d createNewRange(String name) {
        if (selectedPos1 == null || selectedPos2 == null) {
            return null;
        }
        Range3d range = new Range3d(selectedPos1[0], selectedPos1[1], selectedPos1[2], selectedPos2[0], selectedPos2[1], selectedPos2[2]);
        range.setRangeName(name);
        selectedPos1 = null;
        selectedPos2 = null;
        ranges.add(range);

        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return range;
    }

    public static boolean playerInRange() {
        if (PartlySaneSkies.minecraft.thePlayer == null) {
            return false;
        }
        
        String location = PartlySaneSkies.getRegionName();
        location = StringUtils.removeColorCodes(location);
        location = StringUtils.stripLeading(location);
        location = StringUtils.stripTrailing(location);
        location = location.replaceAll("\\P{Print}", ""); // Removes the RANDOM EMOJIS THAT ARE PRESENT IN SKYBLOCK LOCATIONS
        if (!(location.startsWith("The Garden")  || location.startsWith("Plot: "))) {
            return false;
        }

        if (PartlySaneSkies.minecraft.thePlayer.getPosition() == null) {
            return false;
        }
        for (Range3d range : ranges) {
            if (range.isInRange(PartlySaneSkies.minecraft.thePlayer.getPosition().getX(), PartlySaneSkies.minecraft.thePlayer.getPosition().getY(), PartlySaneSkies.minecraft.thePlayer.getPosition().getZ())) {
                return true;
            }
        }
        return false;
    }

    public static void save() throws IOException {
        // Declares the file
        File file = new File("./config/partly-sane-skies/endOfFarmRanges.json");

        file.createNewFile();
        // Creates a new Gson object to save the data
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeSpecialFloatingPointValues()
                .create();
        // Saves teh data to the file
        String json = gson.toJson(ranges);
        FileWriter writer = new FileWriter(file);
        writer.write(json);
        writer.close();
    }

    public static void load() throws IOException {
        // Declares file location
        File file = new File("./config/partly-sane-skies/endOfFarmRanges.json");
        file.setWritable(true);
        // If the file had to be created, fil it with an empty list to prevent null pointer error
        if (file.createNewFile()) {
            FileWriter writer = new FileWriter(file);
            writer.write(new Gson().toJson(new ArrayList<Range3d>()));
            writer.close();
        }

        // Creates a new file reader
        Reader reader = Files.newBufferedReader(Paths.get(file.getPath()));

        JsonArray array = new JsonParser().parse(reader).getAsJsonArray();

        ArrayList<Range3d> list = new ArrayList<Range3d> ();
        
        for (JsonElement en : array) {
            JsonObject range = en.getAsJsonObject();

            int[] smallCoordinate = new int[3];
            int i = 0;
            for (JsonElement element : range.getAsJsonArray("smallCoordinate")) {
                smallCoordinate[i] = element.getAsInt();
                i++;
            }

            int[] largeCoordinate = new int[3];
            i = 0;
            for (JsonElement element : range.getAsJsonArray("largeCoordinate")) {
                largeCoordinate[i] = element.getAsInt();
                i++;
            }

            list.add(new Range3d(smallCoordinate[0], smallCoordinate[1], smallCoordinate[2], largeCoordinate[0], largeCoordinate[1], largeCoordinate[2]));
        }

        
        ranges = list;
    }

    // Lists all of the ranges to the chat
    public static void listRanges() {
        // Creates header message
        String message = "&d-----------------------------------------------------\n&bEnd of Farms:" +
                "\n&d-----------------------------------------------------\n";

        // Creates the index number on the left of the message
        int i = 1;
        
        // For each alert, format it so its ##. [range] 
        for (Range3d range : ranges) {
            message += StringUtils.formatNumber(i) + ": " + range.toString()  + "\n";
            i++;
        }

        // Sends message to the client
        Utils.sendClientMessage(message);
    }

    
    @SubscribeEvent
    public void renderText(RenderGameOverlayEvent.Text event) {
        short alpha = LocationBannerDisplay.getAlpha(Minecraft.getSystemTime() - lastChimeTime, 5);

        if (color == null)
            color = Color.RED;
        else
            color = new Color(color.getRed(), color.getGreen(), color.getBlue(), (short) alpha);
        float scaleFactor = (window.getWidth()) / 1075f;
        ((UIText) displayText)
                .setText(displayString)
                .setTextScale(new PixelConstraint(TEXT_SCALE * scaleFactor))
                .setX(new CenterConstraint())
                .setY(new PixelConstraint(window.getHeight() * .125f))
                .setColor(color);
        window.draw(new UMatrixStack());
    }
}
