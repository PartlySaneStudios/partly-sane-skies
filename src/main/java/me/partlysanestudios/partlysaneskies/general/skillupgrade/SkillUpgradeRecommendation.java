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

    
    public static int maxLevel(Skills skill) {
        if(skill.equals(Skills.MINING)) return 60;
        else if(skill.equals(Skills.FORAGING)) return 50;
        else if(skill.equals(Skills.ENCHANTING)) return 60;
        else if(skill.equals(Skills.COMBAT)) return 60;
        else if(skill.equals(Skills.FARMING)) return 60;
        else if(skill.equals(Skills.FISHING)) return 50;
        else if(skill.equals(Skills.ALCHEMY)) return 50;
        else if(skill.equals(Skills.CATACOMBS)) return 50;
        else return -1;
    }

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
        if (skills.getAsJsonObject("mining").get("level").getAsDouble() < maxLevel(Skills.MINING)) 
            map.put(Skills.MINING,50 * skills.getAsJsonObject("mining").get("level").getAsDouble() / (Math.pow(skills.getAsJsonObject("mining").get("level").getAsDouble()* 10, 1.68207448 + skills.getAsJsonObject("mining").get("level").getAsDouble()/100)/1250) - (Math.pow(Math.ceil(skills.getAsJsonObject("mining").get("level").getAsDouble())* 10, 1.68207448 + (Math.floor(skills.getAsJsonObject("mining").get("level").getAsDouble())+1)/100)/1250));
        if (skills.getAsJsonObject("foraging").get("level").getAsDouble() < maxLevel(Skills.FORAGING)) 
            map.put(Skills.FORAGING, 50 * skills.getAsJsonObject("foraging").get("level").getAsDouble() / (Math.pow(skills.getAsJsonObject("foraging").get("level").getAsDouble()* 10, 1.732826 + skills.getAsJsonObject("foraging").get("level").getAsDouble()/100)/1250) - (Math.pow(Math.ceil(skills.getAsJsonObject("foraging").get("level").getAsDouble())* 10, 1.732826 + (Math.floor(skills.getAsJsonObject("foraging").get("level").getAsDouble())+1)/100)/1250));
        if (skills.getAsJsonObject("enchanting").get("level").getAsDouble() < maxLevel(Skills.ENCHANTING)) 
            map.put(Skills.ENCHANTING, 50 * skills.getAsJsonObject("enchanting").get("level").getAsDouble() / (Math.pow(skills.getAsJsonObject("enchanting").get("level").getAsDouble()* 10, 1.468976583 + skills.getAsJsonObject("enchanting").get("level").getAsDouble()/100)/1250) - (Math.pow(Math.ceil(skills.getAsJsonObject("enchanting").get("level").getAsDouble())* 10, 1.468976583 + (Math.floor(skills.getAsJsonObject("enchanting").get("level").getAsDouble())+1)/100)/1250));
        if (skills.getAsJsonObject("farming").get("level").getAsDouble() < maxLevel(Skills.FARMING)) 
            map.put(Skills.FARMING, 50 * skills.getAsJsonObject("farming").get("level").getAsDouble() / (Math.pow(skills.getAsJsonObject("farming").get("level").getAsDouble()* 10, 1.717848139 + skills.getAsJsonObject("farming").get("level").getAsDouble()/100)/1250) - (Math.pow(Math.ceil(skills.getAsJsonObject("farming").get("level").getAsDouble())* 10, 1.717848139 + (Math.floor(skills.getAsJsonObject("farming").get("level").getAsDouble())+1)/100)/1250));
        if (skills.getAsJsonObject("combat").get("level").getAsDouble() < maxLevel(Skills.COMBAT)) 
            map.put(Skills.COMBAT, 50* skills.getAsJsonObject("combat").get("level").getAsDouble() / (Math.pow(skills.getAsJsonObject("combat").get("level").getAsDouble()* 10, 1.65797687265 + skills.getAsJsonObject("combat").get("level").getAsDouble()/100)/1250) - (Math.pow(Math.ceil(skills.getAsJsonObject("combat").get("level").getAsDouble())* 10, 1.65797687265 + (Math.floor(skills.getAsJsonObject("combat").get("level").getAsDouble())+1)/100)/1250));
        if (skills.getAsJsonObject("fishing").get("level").getAsDouble() < maxLevel(Skills.FISHING)) 
            map.put(Skills.FISHING, 50 * skills.getAsJsonObject("fishing").get("level").getAsDouble() / (Math.pow(skills.getAsJsonObject("fishing").get("level").getAsDouble()* 10, 1.906418 + skills.getAsJsonObject("fishing").get("level").getAsDouble()/100)/1250) - (Math.pow(Math.ceil(skills.getAsJsonObject("fishing").get("level").getAsDouble())* 10, 1.906418 + (Math.floor(skills.getAsJsonObject("fishing").get("level").getAsDouble())+1)/100)/1250));
        if (skills.getAsJsonObject("alchemy").get("level").getAsDouble() < maxLevel(Skills.ALCHEMY)) {
            map.put(Skills.ALCHEMY,(Math.pow(skills.getAsJsonObject("alchemy").get("level").getAsDouble()* 10, 1.5 + skills.getAsJsonObject("alchemy").get("level").getAsDouble()/100)/1250) - (Math.pow(Math.ceil(skills.getAsJsonObject("alchemy").get("level").getAsDouble())* 10, 1.5 + (Math.floor(skills.getAsJsonObject("alchemy").get("level").getAsDouble())+1)/100)/1250));
        }
       
        
        if (senitherJson.getAsJsonObject("data").getAsJsonObject("dungeons").getAsJsonObject("types").getAsJsonObject("catacombs").get("level").getAsDouble() < maxLevel(Skills.CATACOMBS)) map.put(Skills.CATACOMBS,(Math.pow(Math.floor(senitherJson.getAsJsonObject("data").getAsJsonObject("dungeons").getAsJsonObject("types").getAsJsonObject("catacombs").get("level").getAsDouble()), 4.5)* 0.0002149604615) - (Math.pow(Math.floor(senitherJson.getAsJsonObject("data").getAsJsonObject("dungeons").getAsJsonObject("types").getAsJsonObject("catacombs").get("level").getAsDouble()+1), 4.5)* 0.0002149604615));

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
        "\n\n&8(Skill) : (Upgrade Importance)\n";

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
}
