package me.partlysanestudios.partlysaneskies.dungeons.partymanager;

import java.awt.Color;
import java.io.IOException;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import gg.essential.elementa.UIComponent;
import gg.essential.elementa.components.UIImage;
import gg.essential.elementa.components.UIRoundedRectangle;
import gg.essential.elementa.components.UIText;
import gg.essential.elementa.constraints.CenterConstraint;
import gg.essential.elementa.constraints.PixelConstraint;
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
    public boolean isPlayer = false;


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

    public void setRank(PartyRank partyRank) {
        this.rank = partyRank;
    }

    public int getData() throws IOException, NullPointerException {
        timeDataGet = Minecraft.getSystemTime();
        JsonParser parser = new JsonParser();
        String response = Utils.getRequest("https://api.mojang.com/users/profiles/minecraft/" + username);
        if(response.startsWith("Error")) {
            Utils.sendClientMessage(Utils.colorCodes("Error getting data for " + username + ". Maybe the player is nicked or there is an invalid API key. Try running /api new."));
            return -3;
        }
        JsonObject uuidJson = (JsonObject) parser.parse(response);
        
        String uuid = uuidJson.get("id").getAsString();
        
        
        response =  Utils.getRequest("https://api.slothpixel.me/api/skyblock/profile/" + uuid);
        if(response.startsWith("Error")) {
            Utils.sendClientMessage(Utils.colorCodes("Error getting data for " + username + ". Maybe the player is nicked or there is an invalid API key. Try running /api new."));
            return -1;
        }
        JsonObject slothpixelJson = (JsonObject) parser.parse(response);

        try {
            combatLevel = slothpixelJson.getAsJsonObject("members").getAsJsonObject(uuid).getAsJsonObject("skills").getAsJsonObject("combat").get("floatLevel").getAsFloat();
        } catch(NullPointerException e) {
            combatLevel = 0; 

        }
        
        try {
            averageSkillLevel = slothpixelJson.getAsJsonObject("members").getAsJsonObject(uuid).get("average_skill_level").getAsFloat();
        } catch(NullPointerException e) {
            averageSkillLevel = 0;
        } 

        try {
            helmetName = slothpixelJson.getAsJsonObject("members").getAsJsonObject(uuid).getAsJsonArray("armor").get(3).getAsJsonObject().get("name").getAsString();
            helmetName = formatText(helmetName);
        }
        catch(NullPointerException e) {
            e.printStackTrace();
        }



        try {
            chestplateName = slothpixelJson.getAsJsonObject("members").getAsJsonObject(uuid).getAsJsonArray("armor").get(2).getAsJsonObject().get("name").getAsString();
            chestplateName = formatText(chestplateName);
        }
        catch(NullPointerException e) {
            e.printStackTrace();
        }



        try {
            leggingsName = slothpixelJson.getAsJsonObject("members").getAsJsonObject(uuid).getAsJsonArray("armor").get(1).getAsJsonObject().get("name").getAsString();
            leggingsName = formatText(leggingsName);
        }
        catch(NullPointerException e) {
            e.printStackTrace();
        }



        try {
            bootsName = slothpixelJson.getAsJsonObject("members").getAsJsonObject(uuid).getAsJsonArray("armor").get(0).getAsJsonObject().get("name").getAsString();
            bootsName = formatText(bootsName);
        }
        catch(NullPointerException e) {
            e.printStackTrace();
        }



        try {
            selectedDungeonClass = slothpixelJson.getAsJsonObject("members").getAsJsonObject(uuid).getAsJsonObject("dungeons").get("selected_dungeon_class").getAsString();
            selectedDungeonClass = formatText(selectedDungeonClass);
        } catch(NullPointerException e) {
            selectedDungeonClass = "None";
        }
       

        try{
            if(slothpixelJson.getAsJsonObject("members").getAsJsonObject(uuid).getAsJsonObject("dungeons").getAsJsonObject("dungeon_types").getAsJsonObject("catacombs").getAsJsonObject("tier_completions").get("1") == null)
                f1Runs = 0;
            else f1Runs = slothpixelJson.getAsJsonObject("members").getAsJsonObject(uuid).getAsJsonObject("dungeons").getAsJsonObject("dungeon_types").getAsJsonObject("catacombs").getAsJsonObject("tier_completions").get("1").getAsInt();
        } catch(NullPointerException e) {
            f1Runs = 0;
        }
        

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

        hypixelLevel = slothpixelJson.getAsJsonObject("members").getAsJsonObject(uuid).getAsJsonObject("player").get("level").getAsFloat();
        hypixelLevel = slothpixelJson.getAsJsonObject("members").getAsJsonObject(uuid).get("average_skill_level").getAsFloat();
        response = Utils.getRequest("https://hypixel-api.senither.com/v1/profiles/" + uuid + "/latest?key=" + Main.config.apiKey);
        if(response.startsWith("Error")) {
            Utils.sendClientMessage(Utils.colorCodes("Error getting data for " + username + ". Maybe the player is nicked or there is an invalid API key. Try running /api new."));
            return -2;
        }
        JsonObject senitherJson = (JsonObject) parser.parse(response);


        secretsCount = senitherJson.getAsJsonObject("data").getAsJsonObject("dungeons").get("secrets_found").getAsInt();
        senitherWeight = senitherJson.getAsJsonObject("data").get("weight").getAsFloat();
        senitherWeightOverflow = senitherJson.getAsJsonObject("data").get("weight_overflow").getAsFloat();
        catacombsLevel = senitherJson.getAsJsonObject("data").getAsJsonObject("dungeons").getAsJsonObject("types").getAsJsonObject("catacombs").get("level").getAsFloat();
        
        secretsPerRun = secretsCount/(f1Runs+f2Runs+f3Runs+f4Runs+f5Runs+f6Runs+f7Runs+m1Runs+m2Runs+m3Runs+m4Runs+m5Runs+m6Runs+m7Runs);
        return 0;
    }


    public void createBlock(UIComponent memberBlock, float scaleFactor) {
        // Name plate
        new UIText(this.username + "")
        .setTextScale(new PixelConstraint(3f*scaleFactor))
        .setX(new PixelConstraint(20f*scaleFactor))
        .setY(new PixelConstraint(20f*scaleFactor))
        .setColor(Color.white)
        .setChildOf(memberBlock);

    new UIText("Hypixel Level: " + Utils.round(this.hypixelLevel, 1))
        .setTextScale(new PixelConstraint(1f*scaleFactor))
        .setX(new PixelConstraint(20f*scaleFactor))
        .setY(new PixelConstraint(50f*scaleFactor))
        .setColor(Color.white)
        .setChildOf(memberBlock);

    new UIText(this.selectedDungeonClass + "")
        .setTextScale(new PixelConstraint(1f*scaleFactor))
        .setX(new PixelConstraint(150f*scaleFactor))
        .setY(new PixelConstraint(50f*scaleFactor))
        .setColor(Color.white)
        .setChildOf(memberBlock);
        createMemberBlockColumnOne(memberBlock, scaleFactor);
        createMemberBlockColumnTwo(memberBlock, scaleFactor);
        createMemberBlockColumnThree(memberBlock, scaleFactor);
        createMemberBlockColumnFour(memberBlock, scaleFactor);
        createMemberBlockColumnFive(memberBlock, scaleFactor);
    }



    private void createMemberBlockColumnOne(UIComponent memberBlock, float scaleFactor) {
        new UIText("❤ " + Math.round(this.health))
                .setTextScale((new PixelConstraint(1.333f*scaleFactor)))
                .setX(new PixelConstraint(20f*scaleFactor))
                .setY(new PixelConstraint(75f*scaleFactor))
                .setColor(Utils.colorCodetoColor.get("&c"))
                .setChildOf(memberBlock);

            new UIText("❈ " + Math.round(this.defense))
                .setTextScale((new PixelConstraint(1.333f*scaleFactor)))
                .setX(new PixelConstraint(20f*scaleFactor))
                .setY(new PixelConstraint(90f*scaleFactor))
                .setColor(Utils.colorCodetoColor.get("&a"))
                .setChildOf(memberBlock);

            new UIText("EHP " + Math.round(this.effectHealth))
                .setTextScale((new PixelConstraint(1.3f*scaleFactor)))
                .setX(new PixelConstraint(20f*scaleFactor))
                .setY(new PixelConstraint(105f*scaleFactor))
                .setColor(new Color(45, 133, 48))
                .setChildOf(memberBlock);

            new UIText("✎ " + Math.round(this.intelligence))
                .setTextScale((new PixelConstraint(1.333f*scaleFactor)))
                .setX(new PixelConstraint(20f*scaleFactor))
                .setY(new PixelConstraint(120f*scaleFactor))
                .setColor(Utils.colorCodetoColor.get("&b"))
                .setChildOf(memberBlock);
            
         
            new UIText("Catacombs Level: " + Utils.round(this.catacombsLevel, 2))
                .setTextScale((new PixelConstraint(1.333f*scaleFactor)))
                .setX(new PixelConstraint(20f*scaleFactor))
                .setY(new PixelConstraint(135f*scaleFactor))
                .setColor(Utils.colorCodetoColor.get("&c"))
                .setChildOf(memberBlock);
            
            new UIText("Average Skill Level " + Utils.round(this.averageSkillLevel, 2))
                .setTextScale((new PixelConstraint(1.333f*scaleFactor)))
                .setX(new PixelConstraint(20f*scaleFactor))
                .setY(new PixelConstraint(150f*scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);

            new UIText("Combat Level: " + Utils.round(this.combatLevel, 2))
                .setTextScale((new PixelConstraint(1.333f*scaleFactor)))
                .setX(new PixelConstraint(20f*scaleFactor))
                .setY(new PixelConstraint(165f*scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);
    }

    private void createMemberBlockColumnTwo(UIComponent memberBlock, float scaleFactor) {
        new UIText("Secrets: " + this.secretsCount)
        .setTextScale((new PixelConstraint(1.333f*scaleFactor)))
        .setX(new PixelConstraint(150f*scaleFactor))
        .setY(new PixelConstraint(74f*scaleFactor))
        .setColor(Color.white)
        .setChildOf(memberBlock);

        new UIText("Secrets Per Run: " + Utils.round(this.secretsPerRun, 2))
            .setTextScale((new PixelConstraint(1.333f*scaleFactor)))
            .setX(new PixelConstraint(150f*scaleFactor))
            .setY(new PixelConstraint(90f*scaleFactor))
            .setColor(Color.white)
            .setChildOf(memberBlock);

        new UIText("Total Weight: " + Utils.round((this.senitherWeight + this.senitherWeightOverflow), 2))
            .setTextScale((new PixelConstraint(1.2f*scaleFactor)))
            .setX(new PixelConstraint(150f*scaleFactor))
            .setY(new PixelConstraint(105f*scaleFactor))
            .setColor(Color.white)
            .setChildOf(memberBlock);
        new UIText("Sentiher Weight: " + Utils.round((this.senitherWeight), 3))
            .setTextScale((new PixelConstraint(.9f*scaleFactor)))
            .setX(new PixelConstraint(150f*scaleFactor))
            .setY(new PixelConstraint(115f*scaleFactor))
            .setColor(Color.white)
            .setChildOf(memberBlock);

        new UIText("Overflow Weight: " + Utils.round((this.senitherWeightOverflow), 3))
            .setTextScale((new PixelConstraint(.9f*scaleFactor)))
            .setX(new PixelConstraint(150f*scaleFactor))
            .setY(new PixelConstraint(125f*scaleFactor))
            .setColor(Color.white)
            .setChildOf(memberBlock);
    }

    private void createMemberBlockColumnThree(UIComponent memberBlock, float scaleFactor){
        new UIText("Runs:")
        .setTextScale(new PixelConstraint(2.5f*scaleFactor))
        .setX(new PixelConstraint(390f*scaleFactor))
        .setY(new PixelConstraint(20f*scaleFactor))
        .setColor(Color.white)
        .setChildOf(memberBlock);

    new UIText("Floor 1: " + Math.round(this.f1Runs))
        .setTextScale(new PixelConstraint(1.3f*scaleFactor))
        .setX(new PixelConstraint(340f*scaleFactor))
        .setY(new PixelConstraint(50f*scaleFactor))
        .setColor(Color.white)
        .setChildOf(memberBlock);

    new UIText("Floor 2: " + Math.round(this.f2Runs))
        .setTextScale(new PixelConstraint(1.3f*scaleFactor))
        .setX(new PixelConstraint(340f*scaleFactor))
        .setY(new PixelConstraint(70f*scaleFactor))
        .setColor(Color.white)
        .setChildOf(memberBlock);
    
    new UIText("Floor 3: " + Math.round(this.f3Runs))
        .setTextScale(new PixelConstraint(1.3f*scaleFactor))
        .setX(new PixelConstraint(340f*scaleFactor))
        .setY(new PixelConstraint(90f*scaleFactor))
        .setColor(Color.white)
        .setChildOf(memberBlock);
    
    new UIText("Floor 4: " + Math.round(this.f4Runs))
        .setTextScale(new PixelConstraint(1.3f*scaleFactor))
        .setX(new PixelConstraint(340f*scaleFactor))
        .setY(new PixelConstraint(110f*scaleFactor))
        .setColor(Color.white)
        .setChildOf(memberBlock);
    
    new UIText("Floor 5: " + Math.round(this.f5Runs))
        .setTextScale(new PixelConstraint(1.3f*scaleFactor))
        .setX(new PixelConstraint(340f*scaleFactor))
        .setY(new PixelConstraint(130f*scaleFactor))
        .setColor(Color.white)
        .setChildOf(memberBlock);
    
    new UIText("Floor 6: " + Math.round(this.f6Runs))
        .setTextScale(new PixelConstraint(1.3f*scaleFactor))
        .setX(new PixelConstraint(340f*scaleFactor))
        .setY(new PixelConstraint(150f*scaleFactor))
        .setColor(Color.white)
        .setChildOf(memberBlock);

    new UIText("Floor 7: " + Math.round(this.f7Runs))
        .setTextScale(new PixelConstraint(1.3f*scaleFactor))
        .setX(new PixelConstraint(340f*scaleFactor))
        .setY(new PixelConstraint(170f*scaleFactor))
        .setColor(Color.white)
        .setChildOf(memberBlock);



    new UIText("Master 1: " + Math.round(this.m1Runs))
        .setTextScale(new PixelConstraint(1.3f*scaleFactor))
        .setX(new PixelConstraint(460f*scaleFactor))
        .setY(new PixelConstraint(50f*scaleFactor))
        .setColor(Color.white)
        .setChildOf(memberBlock);

    new UIText("Master 2: " + Math.round(this.m2Runs))
        .setTextScale(new PixelConstraint(1.3f*scaleFactor))
        .setX(new PixelConstraint(460f*scaleFactor))
        .setY(new PixelConstraint(70f*scaleFactor))
        .setColor(Color.white)
        .setChildOf(memberBlock);
    
    new UIText("Master 3: " + Math.round(this.m3Runs))
        .setTextScale(new PixelConstraint(1.3f*scaleFactor))
        .setX(new PixelConstraint(460f*scaleFactor))
        .setY(new PixelConstraint(90f*scaleFactor))
        .setColor(Color.white)
        .setChildOf(memberBlock);
    
    new UIText("Master 4: " + Math.round(this.m4Runs))
        .setTextScale(new PixelConstraint(1.3f*scaleFactor))
        .setX(new PixelConstraint(460f*scaleFactor))
        .setY(new PixelConstraint(110f*scaleFactor))
        .setColor(Color.white)
        .setChildOf(memberBlock);
    
    new UIText("Master 5: " + Math.round(this.m5Runs))
        .setTextScale(new PixelConstraint(1.3f*scaleFactor))
        .setX(new PixelConstraint(460f*scaleFactor))
        .setY(new PixelConstraint(130f*scaleFactor))
        .setColor(Color.white)
        .setChildOf(memberBlock);
    
    new UIText("Master 6: " + Math.round(this.m6Runs))
        .setTextScale(new PixelConstraint(1.3f*scaleFactor))
        .setX(new PixelConstraint(460f*scaleFactor))
        .setY(new PixelConstraint(150f*scaleFactor))
        .setColor(Color.white)
        .setChildOf(memberBlock);

    new UIText("Master 7: " + Math.round(this.m7Runs))
        .setTextScale(new PixelConstraint(1.3f*scaleFactor))
        .setX(new PixelConstraint(460f*scaleFactor))
        .setY(new PixelConstraint(170f*scaleFactor))
        .setColor(Color.white)
        .setChildOf(memberBlock);
    }

    private void createMemberBlockColumnFour(UIComponent memberBlock, float scaleFactor) {
        new UIText("Gear:")
                .setTextScale(new PixelConstraint(2.5f*scaleFactor))
                .setX(new PixelConstraint(580f*scaleFactor))
                .setY(new PixelConstraint(20f*scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);

        new UIText("" + this.helmetName)
            
            .setTextScale(new PixelConstraint(1.15f*scaleFactor))
            .setX(new PixelConstraint(580f*scaleFactor))
            .setY(new PixelConstraint(50f*scaleFactor))
            .setColor(Color.white)
            .setChildOf(memberBlock);
        
        new UIText("" + this.chestplateName)
            .setTextScale(new PixelConstraint(1.15f*scaleFactor))
            .setX(new PixelConstraint(580f*scaleFactor))
            .setY(new PixelConstraint(85f*scaleFactor))
            .setColor(Color.white)
            .setChildOf(memberBlock);

        new UIText("" + this.leggingsName)
            .setTextScale(new PixelConstraint(1.15f*scaleFactor))
            .setX(new PixelConstraint(580f*scaleFactor))
            .setY(new PixelConstraint(120f*scaleFactor))
            .setColor(Color.white)
            .setChildOf(memberBlock);

        new UIText("" + this.bootsName)
            .setTextScale(new PixelConstraint(1.15f*scaleFactor))
            .setX(new PixelConstraint(580f*scaleFactor))
            .setY(new PixelConstraint(155f*scaleFactor))
            .setColor(Color.white)
            .setChildOf(memberBlock);
    }

    private void createMemberBlockColumnFive(UIComponent memberBlock, float scaleFactor) {
        UIComponent kickButton = new UIRoundedRectangle(10f)
        .setX(new PixelConstraint(800f*scaleFactor))
        .setY(new PixelConstraint(15f * scaleFactor))
        .setWidth(new PixelConstraint(125f*scaleFactor))
        .setHeight(new PixelConstraint(55f * scaleFactor))
        .setColor(new Color(61, 90, 181))
        .setChildOf(memberBlock);
    
        new UIText("Kick")
            .setTextScale(new PixelConstraint(2*scaleFactor))
            .setX(new CenterConstraint())
            .setY(new CenterConstraint())
            .setColor(Color.white)
            .setChildOf(kickButton);

        kickButton.onMouseClickConsumer(event -> {
            if(this.rank.equals(PartyRank.LEADER)) Main.minecraft.thePlayer.sendChatMessage("/party kick " + this.username);
            else Main.minecraft.thePlayer.sendChatMessage("/pc Recommend you kick " + this.username);
        });



        UIComponent promoteButton = new UIRoundedRectangle(10f)
            .setX(new PixelConstraint(800f*scaleFactor))
            .setY(new PixelConstraint(75 * scaleFactor))
            .setWidth(new PixelConstraint(125f*scaleFactor))
            .setHeight(new PixelConstraint(55f * scaleFactor))
            .setColor(new Color(61, 90, 181))
            .setChildOf(memberBlock);
        
        new UIText("Promote")

            .setTextScale(new PixelConstraint(2*scaleFactor))
            .setX(new CenterConstraint())
            .setY(new CenterConstraint())
            .setColor(Color.white)
            .setChildOf(promoteButton);

        promoteButton.onMouseClickConsumer(event -> {
            Main.minecraft.thePlayer.sendChatMessage("/party promote " + this.username);
        });



        UIComponent transferButton = new UIRoundedRectangle(10f)
            .setX(new PixelConstraint(800f*scaleFactor))
            .setY(new PixelConstraint(135f * scaleFactor))
            .setWidth(new PixelConstraint(125f*scaleFactor))
            .setHeight(new PixelConstraint(55f * scaleFactor))
            .setColor(new Color(61, 90, 181))
            .setChildOf(memberBlock);
        
        new UIText("Transfer")
            .setTextScale(new PixelConstraint(2*scaleFactor))
            .setX(new CenterConstraint())
            .setY(new CenterConstraint())
            .setColor(Color.white)
            .setChildOf(transferButton);

        transferButton.onMouseClickConsumer(event -> {
            Main.minecraft.thePlayer.sendChatMessage("/party transfer " + this.username);
        });

        UIComponent refreshButton = new UIRoundedRectangle(10f)
            .setX(new PixelConstraint(memberBlock.getWidth()-30f*scaleFactor))
            .setY(new PixelConstraint(10f*scaleFactor))
            .setWidth(new PixelConstraint(20f*scaleFactor))
            .setHeight(new PixelConstraint(20f*scaleFactor))
            .setColor(new Color(60, 222, 79))
            .setChildOf(memberBlock);
        
        UIImage.ofResource("/assets/partlysaneskies/textures/gui/party_finder/refresh.png")
            .setX(new CenterConstraint())
            .setY(new CenterConstraint())
            .setWidth(new PixelConstraint(20f*scaleFactor))
            .setHeight(new PixelConstraint(20f*scaleFactor))
            .setChildOf(refreshButton);

        refreshButton.onMouseClickConsumer(event ->{
            try {
                this.getData();
                Main.minecraft.displayGuiScreen(null);
                PartyManager.startPartyManager();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    
    public boolean isExpired() {
        if(this.rank.equals(PartyRank.LEADER)) return this.timeDataGet + Main.config.partyManagerCacheTime * 60 * 1000 * 2 < Minecraft.getSystemTime();
        return this.timeDataGet + Main.config.partyManagerCacheTime * 60 * 1000 < Minecraft.getSystemTime();
    }

    

    private String formatText(String text) { 
        text = Utils.removeColorCodes(text);
        text = text.replace("✪", "*");
        text = text.replaceAll("\\P{Print}", "");
        while(Character.isWhitespace(text.charAt(0))) {
            text = new StringBuilder(text)
                    .replace(0, 1, "")
                    .toString();
        }
        text = text.replaceAll("[0123456789]", "");
        text = new StringBuilder(text)
                    .replace(0, 1, "" + Character.toUpperCase(text.charAt(0)))
                    .toString();
        return text; 
    }
}
