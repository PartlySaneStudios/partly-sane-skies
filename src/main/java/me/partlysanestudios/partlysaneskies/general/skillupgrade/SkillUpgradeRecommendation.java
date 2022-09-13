package me.partlysanestudios.partlysaneskies.general.skillupgrade;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import me.partlysanestudios.partlysaneskies.utils.Utils;

public class SkillUpgradeRecommendation {
    public enum Skills {
        MINING,
        FORAGING,
        ENCHANTING,
        FARMING,
        COMBAT,
        FISHING,
        ALCHEMY,
        CATACOMBS
    }

    private final static double MINING_CONSTANT = 1.68207448;
    private final static double FORAGING_CONSTANT = 1.732826;
    private final static double ENCHANTING_CONSTANT = 1.46976583;
    private final static double FARMING_CONSTANT = 1.717848139;
    private final static double COMBAT_CONSTANT = 1.65797687265;
    private final static double FISHING_CONSTANT = 1.906418;
    private final static double ALCHEMY_CONSTANT = 1.5;

    public static LinkedHashMap<Skills, Double> getRecomendedSkills(String username) throws IOException {
        HashMap<Skills, Double> map = new HashMap<Skills, Double>();

        JsonParser parser = new JsonParser();
        
        String response = "";
 
        response = Utils.getRequest("https://api.mojang.com/users/profiles/minecraft/" + username);
    
        if(response.startsWith("Error")) {
            Utils.sendClientMessage(Utils.colorCodes("Error getting data for " + username +". Maybe the player is nicked or there is an invalid API key. Try running /api new."));
            return null;
        }
        JsonObject uuidJson = (JsonObject) parser.parse(response);
        
        String uuid = uuidJson.get("id").getAsString();

        response = Utils.getRequest("https://hypixel-api.senither.com/v1/profiles/" + uuid + "/latest?key=8329f5b9-ee7b-43e9-985e-6a6d5f0aa870");

        if(response.startsWith("Error")) {
            Utils.sendClientMessage(Utils.colorCodes("Error getting data for " + username + ". Maybe the player is nicked or there is an invalid API key. Try running /api new."));
            return null;
        }

        JsonObject senitherJson = (JsonObject) parser.parse(response);
        JsonObject skills = senitherJson.getAsJsonObject("data").getAsJsonObject("skills");


        if (skills.getAsJsonObject("mining").get("level").getAsDouble() < 60) {
            double level = skills.getAsJsonObject("mining").get("level").getAsDouble();
            map.put(Skills.MINING, ((60 - level)) / (calculateSkillWeight(level, MINING_CONSTANT) + calculateSkillWeight(Math.ceil(level), MINING_CONSTANT)));
        }
        if (skills.getAsJsonObject("foraging").get("level").getAsDouble() < 50) {
            double level = skills.getAsJsonObject("foraging").get("level").getAsDouble();
            map.put(Skills.FORAGING, ((50 - level)) / (calculateSkillWeight(level, FORAGING_CONSTANT) + calculateSkillWeight(Math.ceil(level), FORAGING_CONSTANT)));
        }
        if (skills.getAsJsonObject("enchanting").get("level").getAsDouble() < 60) {
            double level = skills.getAsJsonObject("enchanting").get("level").getAsDouble();
            map.put(Skills.ENCHANTING, ((60 - level)) / (calculateSkillWeight(level, ENCHANTING_CONSTANT) + calculateSkillWeight(Math.ceil(level), ENCHANTING_CONSTANT)));
        }
        if (skills.getAsJsonObject("farming").get("level").getAsDouble() < 60) {
            double level = skills.getAsJsonObject("farming").get("level").getAsDouble();
            map.put(Skills.FARMING, ((60 - level)) / (calculateSkillWeight(level, FARMING_CONSTANT) + calculateSkillWeight(Math.ceil(level), FARMING_CONSTANT)));
        }
        if (skills.getAsJsonObject("combat").get("level").getAsDouble() < 60) {
            double level = skills.getAsJsonObject("combat").get("level").getAsDouble();
            map.put(Skills.COMBAT, ((60 - level)) / (calculateSkillWeight(level, COMBAT_CONSTANT) + calculateSkillWeight(Math.ceil(level), COMBAT_CONSTANT)));
        }
        if (skills.getAsJsonObject("fishing").get("level").getAsDouble() < 50) {
            double level = skills.getAsJsonObject("fishing").get("level").getAsDouble();
            map.put(Skills.FISHING, ((50 - level)) / (calculateSkillWeight(level, FISHING_CONSTANT) + calculateSkillWeight(Math.ceil(level), FISHING_CONSTANT)));
        }
        if (skills.getAsJsonObject("alchemy").get("level").getAsDouble() < 50) {
            double level = skills.getAsJsonObject("alchemy").get("level").getAsDouble();
            map.put(Skills.ALCHEMY, ((50 - level)) / (calculateSkillWeight(level, ALCHEMY_CONSTANT) + calculateSkillWeight(Math.ceil(level), ALCHEMY_CONSTANT)));
        }

        if (senitherJson.getAsJsonObject("data").getAsJsonObject("dungeons").getAsJsonObject("types").getAsJsonObject("catacombs").get("level").getAsDouble() < 50){
            double level = senitherJson.getAsJsonObject("data").getAsJsonObject("dungeons").getAsJsonObject("types").getAsJsonObject("catacombs").get("level").getAsDouble();
            map.put(Skills.CATACOMBS, ((50 - level)) / (calculateCatacombsWeight(level) + calculateCatacombsWeight(Math.ceil(level))));
        }

        LinkedHashMap<Skills, Double> sortedMap = new LinkedHashMap<Skills, Double>();
        ArrayList<Double> list = new ArrayList<>();
        for (Map.Entry<Skills, Double> entry : map.entrySet()) {
            list.add(entry.getValue());
        }
        Collections.sort(list, new Comparator<Double>() {
            public int compare(Double str, Double str1) {
                return (str).compareTo(str1);
            }
        });
        for (Double str : list) {
            for (Entry<Skills, Double> entry : map.entrySet()) {
                if (entry.getValue().equals(str)) {
                    sortedMap.put(entry.getKey(), str);
                }
            }
        }

        return sortedMap;
    }

    public static void printMessage(HashMap<Skills, Double> map) {
        String message = 
        "&3&m-----------------------------------------------------&r\n"+
        "&b&l&nRecommended skills to level up (In Order):&r" +
        "\n\n&7This calculation is based off of the amount of weight each skill will add when you level it up. Lower level skills will be prioritized.&r" + 
        "\n&7&oNote: Catacombs is often first, but it takes a very long time to level up. \n" + 
        "\n\n&8(Skill) : (Upgrade Importance Score)\n";

        for(Entry<Skills, Double> entry : map.entrySet()) {
            message += "\n" + formatWord(entry.getKey().toString()) + " : " + Utils.round(entry.getValue()*-1d, 2);
        }

        message = message + "\n&3&m-----------------------------------------------------&r";
        Utils.sendClientMessage(Utils.colorCodes(message));
    }
    

    private static String formatWord(String text) {
        while(Character.isWhitespace(text.charAt(0))) {
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

    public static double calculateSkillWeight(double level, double constant){
        return Math.pow(level * 10, constant + level / 100) / 1250;
    }

    public static double calculateCatacombsWeight(double level) {
        return Math.pow(level, 4.5) * 0.0002149604615;
    }
}
