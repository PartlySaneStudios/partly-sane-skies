package me.partlysanestudios.partlysaneskies.general.skillupgrade;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import me.partlysanestudios.partlysaneskies.utils.Utils;

public class SkillUpgradeRecommendation {

    private static HashMap<String, Double> weightConstants = new HashMap<String, Double>();

    public static LinkedHashMap<String, Double> getRecomendedSkills(String username) throws IOException {
        HashMap<String, Double> map = new HashMap<String, Double>();

        JsonObject profileData = getCurrentProfileData(username);

        for (String skill : weightConstants.keySet()) {
            if (getSkillLevel(skill, profileData) == getMaxSkillLevel(skill, profileData)) {
                continue;
            }
            double level = getSkillLevel(skill, profileData);

            // If level is under 5, all levels have equals importance
            if (level < 5) {
                map.put(skill, 100000d);
                continue;
            }

            double score = calculateScore(skill, profileData);
            map.put(skill, score);
        }

        double catacombsLevel = profileData
                .getAsJsonObject("dungeons")
                .getAsJsonObject("catacombs")
                .getAsJsonObject("level")
                .get("levelWithProgress")
                .getAsDouble();

        double maxCatacombsLevel = profileData
                .getAsJsonObject("dungeons")
                .getAsJsonObject("catacombs")
                .getAsJsonObject("level")
                .get("maxLevel")
                .getAsDouble();

        if (catacombsLevel < maxCatacombsLevel) {
            if (catacombsLevel < 5) {
                map.put("catacombs", 100000d);
            } else {
                map.put("catacombs", (maxCatacombsLevel - catacombsLevel) / (calculateCatacombsWeight(catacombsLevel)
                        - calculateCatacombsWeight(Math.ceil(catacombsLevel))) * 1.10 + 10);
            }
        }

        LinkedHashMap<String, Double> sortedMap = sortWeightMap(map);

        return sortedMap;
    }

    // Prints the final message with the weight.
    public static void printMessage(HashMap<String, Double> map) {
        // Message header
        String message = "&3&m-----------------------------------------------------&r\n" +
                "&b&l&nRecommended skills to level up (In Order):&r" +
                "\n\n&7This calculation is based off of the amount of weight each skill will add when you level it up. Lower level skills will be prioritized.&r"
                +
                "\n&7&oNote: Sometimes, low level skills such as alchemy will show up first. These skills are less important but due to the mathematical approach, they will appear first. \n"
                +
                "\n\n&8(Skill) : (Upgrade Importance Score)\n";

        // Convert the entry set to an array for easier handling
        @SuppressWarnings("unchecked")
        Entry<String, Double>[] entryArray = new Entry[map.size()];
        entryArray = map.entrySet().toArray(entryArray);

        // Loops through the array backwards to get the biggest value first
        for (int i = entryArray.length - 1; i >= 0; i--) {
            Entry<String, Double> entry = entryArray[i];
            message += "\n" + formatWord(entry.getKey()) + " : " + Utils.round(entry.getValue(), 2);
        }

        message = message + "\n&3&m-----------------------------------------------------&r";

        // Send message
        Utils.sendClientMessage(Utils.colorCodes(message));
    }

    // Populates the constant hashmap
    public static void populateSkillMap() {
        weightConstants.put("mining", 1.68207448);
        weightConstants.put("foraging", 1.732826);
        weightConstants.put("enchanting", 1.46976583);
        weightConstants.put("combat", 1.65797687265);
        weightConstants.put("fishing", 1.906418);
        weightConstants.put("alchemy", 1.5);
    }

    // Gets the skycrypt data for a given player username
    private static JsonObject getSkycryptData(String username) throws IOException {
        JsonParser parser = new JsonParser();
        // Requests username from API
        String response = Utils.getRequest("https://sky.shiiyu.moe/api/v2/profile/" + username);

        // If error, warn plauer
        if (response.startsWith("Error")) {
            Utils.sendClientMessage(Utils.colorCodes("Error getting data for " + username + ". Maybe the player is nicked or the API is down."));
            return null;
        }
        return (JsonObject) parser.parse(response);
    }

    // Gets the player's current active skill profile
    private static JsonObject getCurrentProfileData(String username) throws IOException {

        JsonObject skycryptJson = getSkycryptData(username);

        String currentProfileId = "";

        for (Entry<String, JsonElement> en : skycryptJson.getAsJsonObject("profiles").entrySet()) {
            if (en.getValue().getAsJsonObject().get("current").getAsBoolean()) {
                currentProfileId = en.getKey();
            }
        }

        return skycryptJson.getAsJsonObject("profiles").getAsJsonObject(currentProfileId).getAsJsonObject("data");
    }

    // Sorts a double hashmap by its values
    private static LinkedHashMap<String, Double> sortWeightMap(HashMap<String, Double> unsortedHashMap) {
        LinkedHashMap<String, Double> sortedMap = new LinkedHashMap<String, Double>();
        ArrayList<Double> valueList = new ArrayList<>();
        for (Map.Entry<String, Double> entry : unsortedHashMap.entrySet()) {
            valueList.add(entry.getValue());
        }

        Collections.sort(valueList, new Comparator<Double>() {
            public int compare(Double value, Double value1) {
                return (value).compareTo(value1);
            }
        });

        for (Double value : valueList) {
            for (Entry<String, Double> entry : unsortedHashMap.entrySet()) {
                if (entry.getValue().equals(value)) {
                    sortedMap.put(entry.getKey(), value);
                }
            }
        }

        return sortedMap;
    }

    // Gets the current player's skill level.
    private static float getSkillLevel(String skillName, JsonObject profileData) {
        return profileData.getAsJsonObject("levels").getAsJsonObject(skillName).get("levelWithProgress").getAsFloat();
    }

    // Gets the max possible skill level for a given skill
    private static int getMaxSkillLevel(String skillName, JsonObject profileData) {
        return profileData.getAsJsonObject("levels").getAsJsonObject(skillName).get("maxLevel").getAsInt();
    }

    // Returns the weight contributed to a given skill given their constant
    private static double calculateSkillWeight(double level, double constant) {
        return Math.pow(level * 10, constant + level / 100) / 1250;
    }

    // Returns the weight contributated to catacombs by a given skill
    private static double calculateCatacombsWeight(double level) {
        return Math.pow(level, 4.5) * 0.0002149604615;
    }

    // Calculates the importance of upgrading skill.
    private static double calculateScore(String skill, JsonObject profileData) {
        // Current skill level
        double currentSkillLevel = getSkillLevel(skill, profileData);

        // Senither weight constant
        double weightConstant = weightConstants.get(skill);

        // Math
        double awayFromMaxComponent = getMaxSkillLevel(skill, profileData) - 60;
        double currentSenitherWeight = calculateSkillWeight(currentSkillLevel - 5, weightConstant);
        double nextLevelSenitherWeight = calculateSkillWeight(Math.ceil(currentSkillLevel - 5), weightConstant);
        double levelUpSenitherWeightComponent = currentSenitherWeight - nextLevelSenitherWeight;

        double score = awayFromMaxComponent / levelUpSenitherWeightComponent + 10;

        return score;
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
                .replace(0, 1, "" + Character.toUpperCase(text.charAt(0)))
                .toString();
        return text;
    }
}
