//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies;

import me.partlysanestudios.partlysaneskies.data.skyblockdata.SkyblockDataManager;
import me.partlysanestudios.partlysaneskies.data.skyblockdata.SkyblockPlayer;
import me.partlysanestudios.partlysaneskies.system.commands.PSSCommand;
import me.partlysanestudios.partlysaneskies.utils.ChatUtils;
import me.partlysanestudios.partlysaneskies.utils.MathUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import static java.util.Comparator.naturalOrder;

public class SkillUpgradeRecommendation {

    private static final HashMap<String, Double> weightConstants = new HashMap<>();

    public static LinkedHashMap<String, Double> getRecommendedSkills(String username) throws IOException {
        HashMap<String, Double> map = new HashMap<>();

        SkyblockPlayer player = SkyblockDataManager.getPlayer(username);

        for (String skill : weightConstants.keySet()) {
            if (getSkillLevel(skill, player) == SkyblockDataManager.getSkill(skill.toUpperCase()).getMaxLevel()) {
                continue;
            }

            double level = getSkillLevel(skill, player);

            // If the level is under 5, all levels have equals importance
            if (level < 5) {
                map.put(skill, 100000d);
                continue;
            }

            double score = calculateScore(skill, player);
            map.put(skill, score);
        }

        double catacombsLevel = player.catacombsLevel;

        double maxCatacombsLevel = 50;

        if (catacombsLevel < maxCatacombsLevel) {
            if (catacombsLevel < 5) {
                map.put("catacombs", 100000d);
            } else {
                map.put("catacombs", (maxCatacombsLevel - catacombsLevel) / (calculateCatacombsWeight(catacombsLevel)
                        - calculateCatacombsWeight(Math.ceil(catacombsLevel))) * 1.10 + 10);
            }
        }

        return sortWeightMap(map);
    }

    private static float getSkillLevel(String skill, SkyblockPlayer player) {
        switch(skill) {
            case "mining":
                return player.miningLevel;

            case "foraging":
                return player.foragingLevel;

            case "enchanting":
                return player.enchantingLevel;

            case "combat":
                return player.combatLevel;

            case "fishing":
                return player.fishingLevel;

            case "alchemy":
                return player.alchemyLevel;

            case "farming" :
                return player.farmingLevel;
        }

        return -1;
    }


    // Prints the final message with the weight.
    public static void printMessage(HashMap<String, Double> map) {
        // Message header
        StringBuilder message = new StringBuilder("§3§m-----------------------------------------------------§r\n" +
                "§b§l§nRecommended skills to level up (In Order):§r" +
                "\n\n§7This calculation is based off of the amount of weight each skill will add when you level it up. Lower level skills will be prioritized.§r"
                +
                "\n§7§oNote: Sometimes, low level skills such as alchemy will show up first. These skills are less important but due to the mathematical approach, they will appear first. \n"
                +
                "\n\n§8(Skill) : (Upgrade Importance Score)\n");

        // Convert the entry set to an array for easier handling
        @SuppressWarnings("unchecked")
        Entry<String, Double>[] entryArray = new Entry[map.size()];
        entryArray = map.entrySet().toArray(entryArray);

        // Loops through the array backwards to get the biggest value first
        for (int i = entryArray.length - 1; i >= 0; i--) {
            Entry<String, Double> entry = entryArray[i];
            message.append("\n").append(formatWord(entry.getKey())).append(" : ").append(MathUtils.INSTANCE.round(entry.getValue(), 2));
        }

        message.append("\n§3§m-----------------------------------------------------§r");

        // Send message
        ChatUtils.INSTANCE.sendClientMessage((message.toString()));
    }

    // Populates the constant hashmap
    public static void populateSkillMap() {
        weightConstants.put("mining", 1.68207448);
        weightConstants.put("foraging", 1.732826);
        weightConstants.put("enchanting", 1.46976583);
        weightConstants.put("combat", 1.65797687265);
        weightConstants.put("fishing", 1.906418);
        weightConstants.put("alchemy", 1.5);
        weightConstants.put("farming", 1.717848139);
    }

    public static void registerCommand() {
        new PSSCommand("skillup")
                .addAlias("skillu")
                .addAlias("su")
                .setDescription("Recommends which skill to upgrade: /skillup [username]")
                .setRunnable((s, a) -> {
                    ChatUtils.INSTANCE.sendClientMessage("Loading...");

                    new Thread(() -> {
                        HashMap<String, Double> map;
                        if (a.length > 0) {
                            try {
                                map = SkillUpgradeRecommendation.getRecommendedSkills(a[0]);
                            } catch (IOException e) {
                                ChatUtils.INSTANCE.sendClientMessage(("Error getting data for " + a[0]
                                        + ". Maybe the player is nicked or there is an invalid API key."));
                                return;
                            }
                        } else {
                            try {
                                map = SkillUpgradeRecommendation.getRecommendedSkills(PartlySaneSkies.minecraft.thePlayer.getName());
                            } catch (IOException e) {
                                ChatUtils.INSTANCE.sendClientMessage(("Error getting data for "
                                        + PartlySaneSkies.minecraft.thePlayer.getName()
                                        + ". Maybe the player is nicked or there is an invalid API key."));
                                return;
                            }
                        }

                        PartlySaneSkies.minecraft.addScheduledTask(() -> {
                            SkillUpgradeRecommendation.printMessage(map);
                        });

                    }).start();
                })
                .register();
    }

    // Sorts a double hashmap by its values
    private static LinkedHashMap<String, Double> sortWeightMap(HashMap<String, Double> unsortedHashMap) {
        LinkedHashMap<String, Double> sortedMap = new LinkedHashMap<>();
        ArrayList<Double> valueList = new ArrayList<>();
        for (Map.Entry<String, Double> entry : unsortedHashMap.entrySet()) {
            valueList.add(entry.getValue());
        }

        valueList.sort(naturalOrder());

        for (Double value : valueList) {
            for (Entry<String, Double> entry : unsortedHashMap.entrySet()) {
                if (entry.getValue().equals(value)) {
                    sortedMap.put(entry.getKey(), value);
                }
            }
        }

        return sortedMap;
    }


    // Returns the weight contributed to a given skill given their constant
    private static double calculateSkillWeight(double level, double constant) {
        return Math.pow(level * 10, constant + level / 100) / 1250;
    }

    // Returns the weight contributed to catacombs by a given skill
    private static double calculateCatacombsWeight(double level) {
        return Math.pow(level, 4.5) * 0.0002149604615;
    }

    // Calculates the importance of upgrading skill.
    private static double calculateScore(String skill, SkyblockPlayer player) {
        // Current skill level
        double currentSkillLevel = getSkillLevel(skill, player);

        // Senither weight constant
        double weightConstant = weightConstants.get(skill);

        // Math
        double awayFromMaxComponent = getSkillLevel(skill, player) - SkyblockDataManager.getSkill(skill.toUpperCase()).getMaxLevel();
        double currentSenitherWeight = calculateSkillWeight(currentSkillLevel - 5, weightConstant);
        double nextLevelSenitherWeight = calculateSkillWeight(Math.ceil(currentSkillLevel - 5), weightConstant);
        double levelUpSenitherWeightComponent;
        levelUpSenitherWeightComponent = currentSenitherWeight - nextLevelSenitherWeight;

        return awayFromMaxComponent / levelUpSenitherWeightComponent + 10;
    }

    // Formats a word to have correct capitalization
    private static String formatWord(String text) {
        while (Character.isWhitespace(text.charAt(0))) {
            text = new StringBuilder(text)
                    .replace(0, 1, "")
                    .toString();
        }
        text = text.toLowerCase();
        text = new StringBuilder(text)
                .replace(0, 1, String.valueOf(Character.toUpperCase(text.charAt(0))))
                .toString();
        return text;
    }
}
