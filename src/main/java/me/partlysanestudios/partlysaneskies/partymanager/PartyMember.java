package me.partlysanestudios.partlysaneskies.partymanager;

import java.io.IOException;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import me.partlysanestudios.partlysaneskies.Main;
import me.partlysanestudios.partlysaneskies.utils.Utils;
import net.minecraft.client.Minecraft;

public class PartyMember {
    public enum PartyRank {
        MEMBER,
        MODERATOR,
        LEADER
    }

    public String username;
    public PartyRank rank;


    // Data
    public long timeDataGet;
    public int secretsCount;
    public float hypixelLevel;
    public float senitherWeight;
    public float senitherWeightOverflow;
    public float catacombsLevel;
    public float combatLevel;
    public float secretsPerRun;
    public float averageSkillLevel;

    public String helmetName;
    public String chestplateName;
    public String leggingsName;
    public String bootsName;
    
    public String petName;

    public String selectedDungeonClass;

    public int f1Runs;
    public int f2Runs;
    public int f3Runs;
    public int f4Runs;
    public int f5Runs;
    public int f6Runs;
    public int f7Runs;

    public int m1Runs;
    public int m2Runs;
    public int m3Runs;
    public int m4Runs;
    public int m5Runs;
    public int m6Runs;
    public int m7Runs;

    public float health;
    public float defense;
    public float intelligence;
    public float effectHealth;



    public PartyMember(String username, PartyRank partyRank) {
        this.timeDataGet = 0;
        this.username = username;
        this.rank = partyRank;
    }

    public int getData() throws IOException {
        timeDataGet = Minecraft.getSystemTime();
        JsonParser parser = new JsonParser();
        JsonObject uuidJson = (JsonObject) parser.parse(Utils.getRequest("https://api.mojang.com/users/profiles/minecraft/" + username));
        String uuid = uuidJson.get("id").getAsString();

        
        
        String response =  Utils.getRequest("https://api.slothpixel.me/api/skyblock/profile/" + uuid);
        if(response.startsWith("Error")) return -1;
        JsonObject slothpixelJson = (JsonObject) parser.parse(response);
        response = null;

        combatLevel = slothpixelJson.getAsJsonObject("members").getAsJsonObject(uuid).getAsJsonObject("skills").getAsJsonObject("combat").get("floatLevel").getAsFloat();

        helmetName = slothpixelJson.getAsJsonObject("members").getAsJsonObject(uuid).getAsJsonArray("armor").get(3).getAsJsonObject().get("name").getAsString();
        chestplateName = slothpixelJson.getAsJsonObject("members").getAsJsonObject(uuid).getAsJsonArray("armor").get(2).getAsJsonObject().get("name").getAsString();
        leggingsName = slothpixelJson.getAsJsonObject("members").getAsJsonObject(uuid).getAsJsonArray("armor").get(1).getAsJsonObject().get("name").getAsString();
        bootsName = slothpixelJson.getAsJsonObject("members").getAsJsonObject(uuid).getAsJsonArray("armor").get(0).getAsJsonObject().get("name").getAsString();
        
        
        helmetName = helmetName.replaceAll("\\P{Print}", "");
        chestplateName = chestplateName.replaceAll("\\P{Print}", "");
        leggingsName = leggingsName.replaceAll("\\P{Print}", "");
        bootsName = bootsName.replaceAll("\\P{Print}", "");



        helmetName = helmetName.replace("✪", "*");
        if(Character.isLowerCase(helmetName.charAt(0))|| Character.isDigit(helmetName.charAt(0)) || Character.isWhitespace(helmetName.charAt(0))) {
            helmetName = new StringBuilder(helmetName)
                .replace(0, 1, "")
                .toString();
        }
        helmetName = helmetName.replaceAll("[0123456789]", "");



        chestplateName = chestplateName.replace("✪", "*");
        if(Character.isLowerCase(chestplateName.charAt(0)) || Character.isDigit(chestplateName.charAt(0)) || Character.isWhitespace(chestplateName.charAt(0))) {
            chestplateName = new StringBuilder(chestplateName)
                .replace(0, 1, "")
                .toString();
        }
        chestplateName = chestplateName.replaceAll("[0123456789]", "");



        leggingsName = leggingsName.replace("✪", "*");
        if(Character.isLowerCase(leggingsName.charAt(0))|| Character.isDigit(leggingsName.charAt(0)) || Character.isWhitespace(leggingsName.charAt(0))) {
            leggingsName = new StringBuilder(leggingsName)
                .replace(0, 1, "")
                .toString();
        }
        leggingsName = leggingsName.replaceAll("[0123456789]", "");



        bootsName = bootsName.replace("✪", "*");
        if(Character.isLowerCase(bootsName.charAt(0))|| Character.isDigit(bootsName.charAt(0)) || Character.isWhitespace(bootsName.charAt(0))) {
            bootsName = new StringBuilder(bootsName)
                .replace(0, 1, "")
                .toString();
        }
        bootsName = bootsName.replaceAll("[0123456789]", "");

        selectedDungeonClass = slothpixelJson.getAsJsonObject("members").getAsJsonObject(uuid).getAsJsonObject("dungeons").get("selected_dungeon_class").getAsString();



        if(slothpixelJson.getAsJsonObject("members").getAsJsonObject(uuid).getAsJsonObject("dungeons").getAsJsonObject("dungeon_types").getAsJsonObject("catacombs").getAsJsonObject("tier_completions").get("1") == null)
            f1Runs = 0;
        else f1Runs = slothpixelJson.getAsJsonObject("members").getAsJsonObject(uuid).getAsJsonObject("dungeons").getAsJsonObject("dungeon_types").getAsJsonObject("catacombs").getAsJsonObject("tier_completions").get("1").getAsInt();

        if(slothpixelJson.getAsJsonObject("members").getAsJsonObject(uuid).getAsJsonObject("dungeons").getAsJsonObject("dungeon_types").getAsJsonObject("catacombs").getAsJsonObject("tier_completions").get("2") == null)
            f2Runs = 0;
        else f2Runs = slothpixelJson.getAsJsonObject("members").getAsJsonObject(uuid).getAsJsonObject("dungeons").getAsJsonObject("dungeon_types").getAsJsonObject("catacombs").getAsJsonObject("tier_completions").get("2").getAsInt();

        if(slothpixelJson.getAsJsonObject("members").getAsJsonObject(uuid).getAsJsonObject("dungeons").getAsJsonObject("dungeon_types").getAsJsonObject("catacombs").getAsJsonObject("tier_completions").get("3") == null)
            f3Runs = 0;
        else f3Runs = slothpixelJson.getAsJsonObject("members").getAsJsonObject(uuid).getAsJsonObject("dungeons").getAsJsonObject("dungeon_types").getAsJsonObject("catacombs").getAsJsonObject("tier_completions").get("3").getAsInt();

        if(slothpixelJson.getAsJsonObject("members").getAsJsonObject(uuid).getAsJsonObject("dungeons").getAsJsonObject("dungeon_types").getAsJsonObject("catacombs").getAsJsonObject("tier_completions").get("4") == null)
            f4Runs = 0;
        else f4Runs = slothpixelJson.getAsJsonObject("members").getAsJsonObject(uuid).getAsJsonObject("dungeons").getAsJsonObject("dungeon_types").getAsJsonObject("catacombs").getAsJsonObject("tier_completions").get("4").getAsInt();

        if(slothpixelJson.getAsJsonObject("members").getAsJsonObject(uuid).getAsJsonObject("dungeons").getAsJsonObject("dungeon_types").getAsJsonObject("catacombs").getAsJsonObject("tier_completions").get("5") == null) 
            f5Runs = 0;
        else f5Runs = slothpixelJson.getAsJsonObject("members").getAsJsonObject(uuid).getAsJsonObject("dungeons").getAsJsonObject("dungeon_types").getAsJsonObject("catacombs").getAsJsonObject("tier_completions").get("5").getAsInt();

        if(slothpixelJson.getAsJsonObject("members").getAsJsonObject(uuid).getAsJsonObject("dungeons").getAsJsonObject("dungeon_types").getAsJsonObject("catacombs").getAsJsonObject("tier_completions").get("6") == null) 
            f6Runs = 0;
        else f6Runs = slothpixelJson.getAsJsonObject("members").getAsJsonObject(uuid).getAsJsonObject("dungeons").getAsJsonObject("dungeon_types").getAsJsonObject("catacombs").getAsJsonObject("tier_completions").get("6").getAsInt();

        if(slothpixelJson.getAsJsonObject("members").getAsJsonObject(uuid).getAsJsonObject("dungeons").getAsJsonObject("dungeon_types").getAsJsonObject("catacombs").getAsJsonObject("tier_completions").get("7") == null)
            f7Runs = 0;
        else f7Runs = slothpixelJson.getAsJsonObject("members").getAsJsonObject(uuid).getAsJsonObject("dungeons").getAsJsonObject("dungeon_types").getAsJsonObject("catacombs").getAsJsonObject("tier_completions").get("7").getAsInt();


        if(slothpixelJson.getAsJsonObject("members").getAsJsonObject(uuid).getAsJsonObject("dungeons").getAsJsonObject("dungeon_types").getAsJsonObject("master_catacombs").getAsJsonObject("tier_completions").get("1") == null)
            m1Runs = 0;
        else m1Runs = slothpixelJson.getAsJsonObject("members").getAsJsonObject(uuid).getAsJsonObject("dungeons").getAsJsonObject("dungeon_types").getAsJsonObject("master_catacombs").getAsJsonObject("tier_completions").get("1").getAsInt();

        if(slothpixelJson.getAsJsonObject("members").getAsJsonObject(uuid).getAsJsonObject("dungeons").getAsJsonObject("dungeon_types").getAsJsonObject("master_catacombs").getAsJsonObject("tier_completions").get("2") == null)
            m2Runs = 0;
        else m2Runs = slothpixelJson.getAsJsonObject("members").getAsJsonObject(uuid).getAsJsonObject("dungeons").getAsJsonObject("dungeon_types").getAsJsonObject("master_catacombs").getAsJsonObject("tier_completions").get("2").getAsInt();

        if(slothpixelJson.getAsJsonObject("members").getAsJsonObject(uuid).getAsJsonObject("dungeons").getAsJsonObject("dungeon_types").getAsJsonObject("master_catacombs").getAsJsonObject("tier_completions").get("3") == null)
            m3Runs = 0;
        else m3Runs = slothpixelJson.getAsJsonObject("members").getAsJsonObject(uuid).getAsJsonObject("dungeons").getAsJsonObject("dungeon_types").getAsJsonObject("master_catacombs").getAsJsonObject("tier_completions").get("3").getAsInt();

        if(slothpixelJson.getAsJsonObject("members").getAsJsonObject(uuid).getAsJsonObject("dungeons").getAsJsonObject("dungeon_types").getAsJsonObject("master_catacombs").getAsJsonObject("tier_completions").get("4") == null)
            m4Runs = 0;
        else m4Runs = slothpixelJson.getAsJsonObject("members").getAsJsonObject(uuid).getAsJsonObject("dungeons").getAsJsonObject("dungeon_types").getAsJsonObject("master_catacombs").getAsJsonObject("tier_completions").get("4").getAsInt();

        if(slothpixelJson.getAsJsonObject("members").getAsJsonObject(uuid).getAsJsonObject("dungeons").getAsJsonObject("dungeon_types").getAsJsonObject("master_catacombs").getAsJsonObject("tier_completions").get("5") == null) 
            m5Runs = 0;
        else m5Runs = slothpixelJson.getAsJsonObject("members").getAsJsonObject(uuid).getAsJsonObject("dungeons").getAsJsonObject("dungeon_types").getAsJsonObject("master_catacombs").getAsJsonObject("tier_completions").get("5").getAsInt();

        if(slothpixelJson.getAsJsonObject("members").getAsJsonObject(uuid).getAsJsonObject("dungeons").getAsJsonObject("dungeon_types").getAsJsonObject("master_catacombs").getAsJsonObject("tier_completions").get("6") == null) 
            m6Runs = 0;
        else m6Runs = slothpixelJson.getAsJsonObject("members").getAsJsonObject(uuid).getAsJsonObject("dungeons").getAsJsonObject("dungeon_types").getAsJsonObject("master_catacombs").getAsJsonObject("tier_completions").get("6").getAsInt();

        if(slothpixelJson.getAsJsonObject("members").getAsJsonObject(uuid).getAsJsonObject("dungeons").getAsJsonObject("dungeon_types").getAsJsonObject("master_catacombs").getAsJsonObject("tier_completions").get("7") == null)
            m7Runs = 0;
        else m7Runs = slothpixelJson.getAsJsonObject("members").getAsJsonObject(uuid).getAsJsonObject("dungeons").getAsJsonObject("dungeon_types").getAsJsonObject("master_catacombs").getAsJsonObject("tier_completions").get("7").getAsInt();




        slothpixelJson.getAsJsonObject("members").getAsJsonObject(uuid).getAsJsonObject("attributes").get("health").getAsFloat();
        intelligence = slothpixelJson.getAsJsonObject("members").getAsJsonObject(uuid).getAsJsonObject("attributes").get("intelligence").getAsFloat();
        defense = slothpixelJson.getAsJsonObject("members").getAsJsonObject(uuid).getAsJsonObject("attributes").get("defense").getAsFloat();
        effectHealth = slothpixelJson.getAsJsonObject("members").getAsJsonObject(uuid).getAsJsonObject("attributes").get("effective_health").getAsFloat();


        response = Utils.getRequest("https://hypixel-api.senither.com/v1/profiles/" + uuid + "/latest?key=" + Main.config.apiKey);
        if(response.startsWith("Error")) return -2;
        JsonObject senitherJson = (JsonObject) parser.parse(response);


        secretsCount = senitherJson.getAsJsonObject("data").getAsJsonObject("dungeons").get("secrets_found").getAsInt();
        hypixelLevel = slothpixelJson.getAsJsonObject("members").getAsJsonObject(uuid).getAsJsonObject("player").get("level").getAsFloat();
        senitherWeight = senitherJson.getAsJsonObject("data").get("weight").getAsFloat();
        senitherWeightOverflow = senitherJson.getAsJsonObject("data").get("weight_overflow").getAsFloat();
        catacombsLevel = senitherJson.getAsJsonObject("data").getAsJsonObject("dungeons").getAsJsonObject("types").getAsJsonObject("catacombs").get("level").getAsFloat();
        
        secretsPerRun = secretsCount/(f1Runs+f2Runs+f3Runs+f4Runs+f5Runs+f6Runs+f7Runs+m1Runs+m2Runs+m3Runs+m4Runs+m5Runs+m6Runs+m7Runs);

        averageSkillLevel = senitherJson.getAsJsonObject("data").getAsJsonObject("skills").get("average_skills").getAsFloat();
        return 0;
    }

    public boolean isExpired() {
        return this.timeDataGet + 30 * 60 * 1000 < Minecraft.getSystemTime();
    }
}
