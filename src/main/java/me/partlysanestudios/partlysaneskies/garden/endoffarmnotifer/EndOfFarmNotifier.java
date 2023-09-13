//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.garden.endoffarmnotifer;

import com.google.gson.*;
import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import me.partlysanestudios.partlysaneskies.system.BannerRenderer;
import me.partlysanestudios.partlysaneskies.system.PSSBanner;
import me.partlysanestudios.partlysaneskies.system.commands.PSSCommand;
import me.partlysanestudios.partlysaneskies.utils.StringUtils;
import me.partlysanestudios.partlysaneskies.utils.Utils;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class EndOfFarmNotifier {
    public static ArrayList<Range3d> ranges = new ArrayList<>();
    public static int[] selectedPos1;
    public static int[] selectedPos2;
    public static long lastChimeTime = 0;

    public static Color color;
    public static String displayString = "";
    public static int TEXT_SCALE = 7;

    public static Range3d rangeToHighlight = null;
    public static long rangeToHighlightSetTime = 0;


    public static void run() {
        if (!Utils.onCooldown(rangeToHighlightSetTime, (long) (PartlySaneSkies.config.farmHightlightTime * 1000))) {
            rangeToHighlight = null;
        }

        if (!playerInRange()) {
            displayString = "";
            return;
        }
        if (Utils.onCooldown(lastChimeTime, (long) (PartlySaneSkies.config.farmnotifierChimeTime * 1000))) {
            return;
        }

        PartlySaneSkies.minecraft.getSoundHandler()
                .playSound(PositionedSoundRecord.create(new ResourceLocation("partlysaneskies", "bell")));
        displayString = "END OF FARM";
        lastChimeTime = PartlySaneSkies.getTime();

        BannerRenderer.INSTANCE.renderNewBanner(new PSSBanner(displayString, 1000, TEXT_SCALE, Color.red));
    }

    public static Range3d createNewRange(String name) {
        if (selectedPos1 == null || selectedPos2 == null) {
            return null;
        }

        double smallY = Math.min(selectedPos1[1], selectedPos2[1]);

        double bigY = Math.max(selectedPos1[1], selectedPos2[1]);
        Range3d range = new Range3d(selectedPos1[0], smallY - 1, selectedPos1[2], selectedPos2[0], bigY + 1, selectedPos2[2]);
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

        if (!inGarden()) {
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

        ArrayList<Range3d> list = new ArrayList<>();
        
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


    public static boolean inGarden() {
        String location = PartlySaneSkies.getRegionName();
        location = StringUtils.removeColorCodes(location);
        location = StringUtils.stripLeading(location);
        location = StringUtils.stripTrailing(location);
        location = location.replaceAll("\\P{Print}", ""); // Removes the RANDOM EMOJIS THAT ARE PRESENT IN SKYBLOCK LOCATIONS
        if (!(location.startsWith("The Garden")  || location.startsWith("Plot: "))) {
            return false;
        } else {
            return true;
        }
    }

    public static void registerPos1Command() {
        new PSSCommand("/pos1")
                .setDescription("Sets one corner of the End of Farm Notifier")
                .setRunnable((s, a) -> {
                    EndOfFarmNotifier.selectedPos1 = new int[] {s.getPosition().getX(), s.getPosition().getY(), s.getPosition().getZ()};

                    Utils.sendClientMessage("§7Set §bpositon 1§7 to §b(" + EndOfFarmNotifier.selectedPos1[0] + ", " + EndOfFarmNotifier.selectedPos1[1] + ", " + EndOfFarmNotifier.selectedPos1[2] + ")§7");

                })
                .register();
    }

    public static void registerPos2Command() {
        new PSSCommand("/pos2")
                .setDescription("Sets other corner of the End of Farm Notifier")
                .setRunnable((s, a) -> {
                    EndOfFarmNotifier.selectedPos2 = new int[] {s.getPosition().getX(), s.getPosition().getY(), s.getPosition().getZ()};

                    Utils.sendClientMessage("§7Set §bpositon 2§7 to §b(" + EndOfFarmNotifier.selectedPos2[0] + ", " + EndOfFarmNotifier.selectedPos2[1] + ", " + EndOfFarmNotifier.selectedPos2[2] + ")§7");

                })
                .register();
    }

    public static void registerCreateRangeCommand() {
        new PSSCommand("/create")
                .setDescription("Creates the range from two positions")
                .setRunnable((s, a) -> {
                    String name = "";
                    if (a.length >= 1) {
                        name = a[0];
                    }

                    if (EndOfFarmNotifier.createNewRange(name) == null) {
                        Utils.sendClientMessage("§cUnable to create a new farm notifier. Make sure both §b//pos1§c and §b//pos2§c have been selected.");
                        return;
                    }

                    Utils.sendClientMessage("§aCreated new Farm Notifier");

                })
                .register();
    }

    public static void registerFarmNotifierCommand() {
        new PSSCommand("/farmnotifier")
                .addAlias("/fn")
                .addAlias("/farmnotif")
                .addAlias("farmnotifier")
                .addAlias("fn")
                .addAlias("farmnotif")
                .setDescription("Operates the Farm Notifier feature: /fn [list/remove/highlight/show]")
                .setRunnable(((sender, args) -> {
                    if (args.length == 0 || args[0].equalsIgnoreCase("list")) {

                        Utils.sendClientMessage("§7To create a new farm notifier, run §b//pos1§7 at one end of your selection, then run §b//pos2§7 at the other end of your farm. Once the area has been selected, run §b//create§7.\n\n§b//farmnotifier§7 command:\n§b//fn remove <index>:§7 remove a given index from the list.\n§b//fn list:§7 lists all of the farm notifiers and their indexes");

                        EndOfFarmNotifier.listRanges();
                        return;
                    }

                    if (args[0].equalsIgnoreCase("remove")) {
                        if (args.length == 1) {
                            Utils.sendClientMessage("§cError: Must provide an index to remove");
                            return;
                        }

                        int i;
                        try{
                            i = Integer.parseInt(args[1]);
                        } catch (NumberFormatException e) {
                            Utils.sendClientMessage("§cPlease enter a valid number index and try again.");
                            return;
                        }

                        if (i > EndOfFarmNotifier.ranges.size()) {
                            Utils.sendClientMessage("§cPlease select a valid index and try again.");
                            return;
                        }
                        Utils.sendClientMessage("§aRemoving: §b" + EndOfFarmNotifier.ranges.get(i - 1).toString());
                        EndOfFarmNotifier.ranges.remove(i - 1);
                        try {
                            EndOfFarmNotifier.save();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    if (args[0].equalsIgnoreCase("highlight") || args[0].equalsIgnoreCase("show")) {
                        if (args.length == 1) {
                            Utils.sendClientMessage("§cError: Must provide an index to highlight");
                            return;
                        }

                        int i;
                        try{
                            i = Integer.parseInt(args[1]);
                        } catch (NumberFormatException e) {
                            Utils.sendClientMessage("§cPlease enter a valid number index and try again.");
                            return;
                        }

                        if (i > EndOfFarmNotifier.ranges.size()) {
                            Utils.sendClientMessage("§cPlease select a valid index and try again.");
                            return;
                        }

                        rangeToHighlight = EndOfFarmNotifier.ranges.get(i - 1);
                        rangeToHighlightSetTime = PartlySaneSkies.getTime();
                    }
                }))
                .register();
    }

    // Lists all the ranges to the chat
    public static void listRanges() {
        // Creates header message
        StringBuilder message = new StringBuilder("§d§m-----------------------------------------------------\n§bEnd of Farms:" +
                "\n§d§m-----------------------------------------------------\n");

        // Creates the index number on the left of the message
        int i = 1;
        
        // For each alert, format it so its ##. [range] 
        for (Range3d range : ranges) {
            message.append("§6").append(StringUtils.formatNumber(i)).append("§7: ").append(range.toString()).append("\n");
            i++;
        }

        // Sends a message to the client
        Utils.sendClientMessage(message.toString());
    }
}
