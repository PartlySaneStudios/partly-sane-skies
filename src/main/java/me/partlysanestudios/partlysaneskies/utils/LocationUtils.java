//
// Written by J10a1n15.
// See LICENSE for copyright and license notices.
//
package me.partlysanestudios.partlysaneskies.utils;

import me.partlysanestudios.partlysaneskies.PartlySaneSkies;

import javax.xml.stream.Location;
import java.util.List;

public class LocationUtils {

    /*
        Hypixel and Skyblock
    */
    public static boolean isSkyblock() {
        try {
            if (ScoreboardUtils.getScoreboardName().toLowerCase().contains("skyblock")) {
                return true;
            }
        } catch (NullPointerException expt) {
            return false;
        }
        return false;
    }

    public static boolean isHypixel() {
        try {
            return PartlySaneSkies.minecraft.getCurrentServerData().serverIP.contains(".hypixel.net");
        } catch (NullPointerException ignored) {
        }
        return false;
    }


    /*
        Islands
     */
    public static boolean inGarden() {
        String location = LocationUtils.getLocation();
        return location.startsWith("The Garden") || location.startsWith("Plot: ");
    }

    public static boolean onPrivateIsland() {
        return LocationUtils.getLocation().startsWith("Your Island"); //Really hope that's the only private island name, and you cant rename it
    }

    public static boolean onMiningIsland() {
        String[] miningLocations = {
                "jungle", "jungle temple", "mithril deposits", "mines of divan", "goblin holdout",
                "goblin queen's den", "precursor remnants", "lost precursor city", "crystal nucleus",
                "magma fields", "khazad-dûm", "fairy grotto", "dragon's lair", "the forge", "forge basin",
                "palace bridge", "royal palace", "aristocrat passage", "hanging court", "divan's gateway",
                "far reserve", "goblin burrows", "miner's guild", "great ice wall", "the mist", "c&c minecarts co.",
                "grand library", "barracks of heroes", "dwarven village", "the lift", "royal quarters", "lava springs",
                "cliffside veins", "rampart's quarry", "upper mines", "royal mines", "gold mine", "coal mine",
                "gunpowder mines", "lapis quarry", "pigman's den", "slimehill", "diamond reserve", "obsidian sanctuary", "dwarven mines"
        };

        for (String loc : miningLocations) {
            if (LocationUtils.getLocation().toLowerCase().contains(loc)) {
                return true;
            }
        }
        return false;
    }

    public static boolean inDungeons(){
        return LocationUtils.getLocation().toLowerCase().contains("catacombs");
    }

    /*
        Utils for the Utils or smth
     */
    public static String getLocation(){
        String regionName = LocationUtils.getRegionName();
        String noColorCodeRegionName = StringUtils.removeColorCodes(regionName);

        if (noColorCodeRegionName.isEmpty()) {
            return regionName;
        }

        noColorCodeRegionName = StringUtils.stripLeading(noColorCodeRegionName);
        noColorCodeRegionName = StringUtils.stripTrailing(noColorCodeRegionName);
        return noColorCodeRegionName.replaceAll("\\P{Print}", ""); // Removes the RANDOM EMOJIS
        // THAT ARE PRESENT IN SKY-BLOCK LOCATIONS
        // LOOK AT THIS:
        // The Catac🔮ombs (F5)
        // The Catac👽ombs (F5)
        // The Catac🔮ombs (F5)
        // Dungeon H👾ub
        // Mountain⚽
        // Village⚽
        // Coal Mine⚽
        // THEY'RE NOT EVEN VISIBLE IN MINECRAFT - Su386
        // (ITS NOT SPELLED VISABLE - j10a)
        // (It's* - Su386)
    }

    public static String getRegionName() {
        if (!LocationUtils.isSkyblock()) {
            return "";
        }

        List<String> scoreboard = ScoreboardUtils.getScoreboardLines();

        String location = null;

        for (String line : scoreboard) {
            if (StringUtils.stripLeading(line).contains("⏣")) {
                location = StringUtils.stripLeading(line).replace("⏣", "");
                location = StringUtils.stripLeading(location);
                break;
            }
        }

        if (location == null) {
            return "";
        }

        return location;
    }
}
