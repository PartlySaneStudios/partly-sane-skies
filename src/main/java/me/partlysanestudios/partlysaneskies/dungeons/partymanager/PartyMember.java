/*
 * Partly Sane Skies: A Hypixel Skyblock QOL and Economy mod
 * Created by Su386#9878 (Su386yt) and FlagMaster#1516 (FlagHater), the Partly Sane Studios team
 * Copyright (C) ©️ Su386 and FlagMaster 2023
 * This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 * 
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 * 
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package me.partlysanestudios.partlysaneskies.dungeons.partymanager;

import java.awt.Color;
import java.io.IOException;
import java.util.Map.Entry;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import gg.essential.elementa.UIComponent;
import gg.essential.elementa.components.UIImage;
import gg.essential.elementa.components.UIRoundedRectangle;
import gg.essential.elementa.components.UIText;
import gg.essential.elementa.constraints.CenterConstraint;
import gg.essential.elementa.constraints.PixelConstraint;
import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
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
    public boolean refresh = false;

    // Data
    public long timeDataGet;
    public int secretsCount;
    public float skyblockLevel;
    public float senitherWeight;
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

    // Creates a new party member based on the username and partyRank
    public PartyMember(String username, PartyRank partyRank) {
        this.timeDataGet = 0;
        this.username = username;
        this.rank = partyRank;
    }

    public void setRank(PartyRank partyRank) {
        this.rank = partyRank;
    }

    public void resetExpire() {
        timeDataGet = Minecraft.getSystemTime();
    }

    // Uses the username to request data from the Skycrypt API
    public void getSkycryptData() throws IOException, NullPointerException {
        // Creates a JSON parser to parse the Json returned by the request
        JsonParser parser = new JsonParser();

        // Requests the Skycrypt API
        String response = Utils.getRequest("https://sky.shiiyu.moe/api/v2/profile/" + this.username);
        // If the response is in error, send error message and exit
        if (response.startsWith("Error")) {
            Utils.sendClientMessage(Utils.colorCodes("Error getting data for " + username + ". Maybe the player is nicked."));
            return;
        }
        // Creates a Json object based on the response
        JsonObject skycryptJson = parser.parse(response).getAsJsonObject();

        String currentProfileId = "";
        // Finds the id of the current profile
        for (Entry<String, JsonElement> en : skycryptJson.getAsJsonObject("profiles").entrySet()) {
            if (en.getValue().getAsJsonObject().get("current").getAsBoolean()) {
                currentProfileId = en.getKey();
            }
        }

        // Creates a new json object with just the current profiles data
        JsonObject profileData = skycryptJson.getAsJsonObject("profiles").getAsJsonObject(currentProfileId).getAsJsonObject("data");

        // Gets the player's senither weight 
        senitherWeight = profileData.getAsJsonObject("weight").getAsJsonObject("senither")
                .get("overall").getAsFloat();

        // Gets the player's secret count
        secretsCount = profileData.getAsJsonObject("dungeons").get("secrets_found").getAsInt();

        // Gets the player's catacombs level
        catacombsLevel = profileData.getAsJsonObject("dungeons").getAsJsonObject("catacombs")
                .getAsJsonObject("level").get("levelWithProgress").getAsFloat();

        // Gets the player's combat level
        combatLevel = profileData.getAsJsonObject("levels").getAsJsonObject("combat").get("levelWithProgress").getAsFloat();

        // Gets the player's average skill level
        averageSkillLevel =  profileData.get("average_level").getAsFloat();

        // Creates a new json object with just the current profles items
        JsonObject profileItems = skycryptJson.getAsJsonObject("profiles").getAsJsonObject(currentProfileId).getAsJsonObject("items");
        // Attempts to get the helmet slot

        try {
            helmetName = profileItems.getAsJsonArray("armor").get(3).getAsJsonObject().get("display_name").getAsString();
            if (helmetName.equals("null")) {
                helmetName = "None";
            }
            helmetName = formatText(helmetName);
        } catch (NullPointerException e) {
            helmetName = "None";
        } catch (IndexOutOfBoundsException e) {
            helmetName = "None";
        }

        // Attempts to get the chestplate slot
        try {
            chestplateName = profileItems.getAsJsonArray("armor").get(2).getAsJsonObject().get("display_name").getAsString();
            chestplateName = formatText(chestplateName);
            if (chestplateName.equals("null")) {
                chestplateName = "None";
            }
        } catch (NullPointerException e) {
            chestplateName = "None";
        } catch (IndexOutOfBoundsException e) {
            chestplateName = "None";
        }

        // Attempts to get the legging slot
        try {
            leggingsName = profileItems.getAsJsonArray("armor").get(1).getAsJsonObject().get("display_name").getAsString();
            if (leggingsName.equals("null")) {
                    leggingsName = "None";
            }
            leggingsName = formatText(leggingsName);
        } catch (NullPointerException e) {
            leggingsName = "None";
        } catch (IndexOutOfBoundsException e) {
            leggingsName = "None";
        }

        // Attempts to get the boots slot
        try {
            bootsName = profileItems.getAsJsonArray("armor").get(0).getAsJsonObject().get("display_name").getAsString();
            if (bootsName.equals("null")) {
                helmetName = "None";
            }
            bootsName = formatText(bootsName);
        } catch (NullPointerException e) {
            bootsName = "None";
        } catch (IndexOutOfBoundsException e) {
            bootsName = "None";
        }

        // Attempts to get the selected dungeon class
        try {
            selectedDungeonClass = profileData.getAsJsonObject("dungeons").get("selected_class").getAsString();
            if (selectedDungeonClass.equals("null")) {
                selectedDungeonClass = "None";
            }
            selectedDungeonClass = formatText(selectedDungeonClass);
        } catch (NullPointerException e) {
            selectedDungeonClass = "None";
        }
        

        // Gets all of the floor runs
        f1Runs = getFloorRuns("1", profileData);
        f2Runs = getFloorRuns("2", profileData);
        f3Runs = getFloorRuns("3", profileData);
        f4Runs = getFloorRuns("4", profileData);
        f5Runs = getFloorRuns("5", profileData);
        f6Runs = getFloorRuns("6", profileData);
        f7Runs = getFloorRuns("7", profileData);

        // Gets all of the master floor runs
        m1Runs = getMasterFloorRuns("1", profileData);
        m2Runs = getMasterFloorRuns("2", profileData);
        m3Runs = getMasterFloorRuns("3", profileData);
        m4Runs = getMasterFloorRuns("4", profileData);
        m5Runs = getMasterFloorRuns("5", profileData);
        m6Runs = getMasterFloorRuns("6", profileData);
        m7Runs = getMasterFloorRuns("7", profileData);

        skyblockLevel = profileData.getAsJsonObject("skyblock_level")
                .get("level").getAsInt();

        JsonObject statsJson = profileData.getAsJsonObject("stats");
        health = statsJson.get("health").getAsFloat();
        intelligence = statsJson.get("intelligence").getAsFloat();
        defense = statsJson.get("defense").getAsFloat();
        effectHealth = statsJson.get("effective_health").getAsFloat();

        // Attempts to get the average secrets per run
        try {
            secretsPerRun = secretsCount / (f1Runs + f2Runs + f3Runs + f4Runs + f5Runs + f6Runs + f7Runs + m1Runs
                    + m2Runs + m3Runs + m4Runs + m5Runs + m6Runs + m7Runs);
        } catch (NullPointerException e) {
        }
    }

    public static String getUUID(String username) throws IOException {
        // Creates a Json parser to parse the json data
        JsonParser parser = new JsonParser();

        // Gets the player's UUID
        String response = Utils.getRequest("https://api.mojang.com/users/profiles/minecraft/" + username);
        if (response.startsWith("Error")) {
            Utils.sendClientMessage(Utils.colorCodes("Error getting data for " + username + ". Maybe the player is nicked."));
            return "";
        }

        // Returns the player's UUID
        JsonObject uuidJson = (JsonObject) parser.parse(response);
        return uuidJson.get("id").getAsString();
    }

    public int getFloorRuns(String floor, JsonObject profileData) {
        JsonElement runsElement; 
        try{
            runsElement = profileData.getAsJsonObject("dungeons").getAsJsonObject("catacombs").getAsJsonObject("floors").getAsJsonObject(floor).getAsJsonObject("stats").get("tier_completions");
        } catch(NullPointerException e) {
            runsElement = null; 
        }
        int runs = 0;
        if (runsElement != null) {
            runs = runsElement.getAsInt();
        }
        return runs;
    }

    public int getMasterFloorRuns(String floor, JsonObject profileData) {
        JsonElement runsElement; 
        try{
            runsElement = profileData.getAsJsonObject("dungeons").getAsJsonObject("master_catacombs").getAsJsonObject("floors").getAsJsonObject(floor).getAsJsonObject("stats").get("tier_completions");
        } catch(NullPointerException e) {
            runsElement = null; 
        }
        int runs = 0;
        if (runsElement != null) {
            runs = runsElement.getAsInt();
        }
        return runs;
    }

    public void createBlock(UIComponent memberBlock, float scaleFactor) {
        // Name plate
        new UIText(this.username + "")
                .setTextScale(new PixelConstraint(3f * scaleFactor))
                .setX(new PixelConstraint(20f * scaleFactor))
                .setY(new PixelConstraint(20f * scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);

        

        new UIText(this.selectedDungeonClass + "")
                .setTextScale(new PixelConstraint(1f * scaleFactor))
                .setX(new PixelConstraint(150f * scaleFactor))
                .setY(new PixelConstraint(50f * scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);
        createMemberBlockColumnOne(memberBlock, scaleFactor);
        createMemberBlockColumnTwo(memberBlock, scaleFactor);
        createMemberBlockColumnThree(memberBlock, scaleFactor);
        createMemberBlockColumnFour(memberBlock, scaleFactor);
        createMemberBlockColumnFive(memberBlock, scaleFactor);
    }

    private void createMemberBlockColumnOne(UIComponent memberBlock, float scaleFactor) {

        new UIText("Catacombs Level: " + Utils.formatNumber(Utils.round(this.catacombsLevel, 2)))
                .setTextScale((new PixelConstraint(1.333f * scaleFactor)))
                .setX(new PixelConstraint(20f * scaleFactor))
                .setY(new PixelConstraint(135f * scaleFactor))
                .setColor(Utils.colorCodetoColor.get("&c"))
                .setChildOf(memberBlock);

        new UIText("Average Skill Level " + Utils.formatNumber(Utils.round(this.averageSkillLevel, 2)))
                .setTextScale((new PixelConstraint(1.333f * scaleFactor)))
                .setX(new PixelConstraint(20f * scaleFactor))
                .setY(new PixelConstraint(150f * scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);

        new UIText("Combat Level: " + Utils.formatNumber(Utils.round(this.combatLevel, 2)))
                .setTextScale((new PixelConstraint(1.333f * scaleFactor)))
                .setX(new PixelConstraint(20f * scaleFactor))
                .setY(new PixelConstraint(165f * scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);
    }

    private void createMemberBlockColumnTwo(UIComponent memberBlock, float scaleFactor) {
        new UIText("Secrets: " + Utils.formatNumber(this.secretsCount))
                .setTextScale((new PixelConstraint(1.333f * scaleFactor)))
                .setX(new PixelConstraint(150f * scaleFactor))
                .setY(new PixelConstraint(74f * scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);

        new UIText("Secrets Per Run: " + Utils.formatNumber(Utils.round(this.secretsPerRun, 2)))
                .setTextScale((new PixelConstraint(1.333f * scaleFactor)))
                .setX(new PixelConstraint(150f * scaleFactor))
                .setY(new PixelConstraint(90f * scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);

        new UIText("Senither Weight: " + Utils.formatNumber(Utils.round((this.senitherWeight), 2)))
                .setTextScale((new PixelConstraint(1.2f * scaleFactor)))
                .setX(new PixelConstraint(150f * scaleFactor))
                .setY(new PixelConstraint(105f * scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);
                new UIText("Skyblock Level: " + Utils.round(this.skyblockLevel, 1))
                .setTextScale(new PixelConstraint(1f * scaleFactor))
                .setX(new PixelConstraint(20f * scaleFactor))
                .setY(new PixelConstraint(50f * scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);

        new UIText("❤ " + Utils.formatNumber(Math.round(this.health)))
                .setTextScale((new PixelConstraint(1.333f * scaleFactor)))
                .setX(new PixelConstraint(20f * scaleFactor))
                .setY(new PixelConstraint(75f * scaleFactor))
                .setColor(Utils.colorCodetoColor.get("&c"))
                .setChildOf(memberBlock);

        new UIText("❈ " + Utils.formatNumber(Math.round(this.defense)))
                .setTextScale((new PixelConstraint(1.333f * scaleFactor)))
                .setX(new PixelConstraint(20f * scaleFactor))
                .setY(new PixelConstraint(90f * scaleFactor))
                .setColor(Utils.colorCodetoColor.get("&a"))
                .setChildOf(memberBlock);

        new UIText("EHP: " + Utils.formatNumber(Math.round(this.effectHealth)))
                .setTextScale((new PixelConstraint(1.3f * scaleFactor)))
                .setX(new PixelConstraint(20f * scaleFactor))
                .setY(new PixelConstraint(105f * scaleFactor))
                .setColor(new Color(45, 133, 48))
                .setChildOf(memberBlock);

        new UIText("✎ " + Utils.formatNumber(Math.round(this.intelligence)))
                .setTextScale((new PixelConstraint(1.333f * scaleFactor)))
                .setX(new PixelConstraint(20f * scaleFactor))
                .setY(new PixelConstraint(120f * scaleFactor))
                .setColor(Utils.colorCodetoColor.get("&b"))
                .setChildOf(memberBlock);

    }

    private void createMemberBlockColumnThree(UIComponent memberBlock, float scaleFactor) {
        new UIText("Runs:")
                .setTextScale(new PixelConstraint(2.5f * scaleFactor))
                .setX(new PixelConstraint(390f * scaleFactor))
                .setY(new PixelConstraint(20f * scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);

        new UIText("Floor 1: " + Utils.formatNumber(Math.round(this.f1Runs)))
                .setTextScale(new PixelConstraint(1.3f * scaleFactor))
                .setX(new PixelConstraint(340f * scaleFactor))
                .setY(new PixelConstraint(50f * scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);

        new UIText("Floor 2: " + Utils.formatNumber(Math.round(this.f2Runs)))
                .setTextScale(new PixelConstraint(1.3f * scaleFactor))
                .setX(new PixelConstraint(340f * scaleFactor))
                .setY(new PixelConstraint(70f * scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);

        new UIText("Floor 3: " + Utils.formatNumber(Math.round(this.f3Runs)))
                .setTextScale(new PixelConstraint(1.3f * scaleFactor))
                .setX(new PixelConstraint(340f * scaleFactor))
                .setY(new PixelConstraint(90f * scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);

        new UIText("Floor 4: " + Utils.formatNumber(Math.round(this.f4Runs)))
                .setTextScale(new PixelConstraint(1.3f * scaleFactor))
                .setX(new PixelConstraint(340f * scaleFactor))
                .setY(new PixelConstraint(110f * scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);

        new UIText("Floor 5: " + Utils.formatNumber(Math.round(this.f5Runs)))
                .setTextScale(new PixelConstraint(1.3f * scaleFactor))
                .setX(new PixelConstraint(340f * scaleFactor))
                .setY(new PixelConstraint(130f * scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);

        new UIText("Floor 6: " + Utils.formatNumber(Math.round(this.f6Runs)))
                .setTextScale(new PixelConstraint(1.3f * scaleFactor))
                .setX(new PixelConstraint(340f * scaleFactor))
                .setY(new PixelConstraint(150f * scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);

        new UIText("Floor 7: " + Utils.formatNumber(Math.round(this.f7Runs)))
                .setTextScale(new PixelConstraint(1.3f * scaleFactor))
                .setX(new PixelConstraint(340f * scaleFactor))
                .setY(new PixelConstraint(170f * scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);

        new UIText("Master 1: " + Utils.formatNumber(Math.round(this.m1Runs)))
                .setTextScale(new PixelConstraint(1.3f * scaleFactor))
                .setX(new PixelConstraint(460f * scaleFactor))
                .setY(new PixelConstraint(50f * scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);

        new UIText("Master 2: " + Utils.formatNumber(Math.round(this.m2Runs)))
                .setTextScale(new PixelConstraint(1.3f * scaleFactor))
                .setX(new PixelConstraint(460f * scaleFactor))
                .setY(new PixelConstraint(70f * scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);

        new UIText("Master 3: " + Utils.formatNumber(Math.round(this.m3Runs)))
                .setTextScale(new PixelConstraint(1.3f * scaleFactor))
                .setX(new PixelConstraint(460f * scaleFactor))
                .setY(new PixelConstraint(90f * scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);

        new UIText("Master 4: " + Utils.formatNumber(Math.round(this.m4Runs)))
                .setTextScale(new PixelConstraint(1.3f * scaleFactor))
                .setX(new PixelConstraint(460f * scaleFactor))
                .setY(new PixelConstraint(110f * scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);

        new UIText("Master 5: " + Utils.formatNumber(Math.round(this.m5Runs)))
                .setTextScale(new PixelConstraint(1.3f * scaleFactor))
                .setX(new PixelConstraint(460f * scaleFactor))
                .setY(new PixelConstraint(130f * scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);

        new UIText("Master 6: " + Utils.formatNumber(Math.round(this.m6Runs)))
                .setTextScale(new PixelConstraint(1.3f * scaleFactor))
                .setX(new PixelConstraint(460f * scaleFactor))
                .setY(new PixelConstraint(150f * scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);

        new UIText("Master 7: " + Utils.formatNumber(Math.round(this.m7Runs)))
                .setTextScale(new PixelConstraint(1.3f * scaleFactor))
                .setX(new PixelConstraint(460f * scaleFactor))
                .setY(new PixelConstraint(170f * scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);
    }

    private void createMemberBlockColumnFour(UIComponent memberBlock, float scaleFactor) {
        new UIText("Gear:")
                .setTextScale(new PixelConstraint(2.5f * scaleFactor))
                .setX(new PixelConstraint(580f * scaleFactor))
                .setY(new PixelConstraint(20f * scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);

        new UIText("" + this.helmetName)

                .setTextScale(new PixelConstraint(1.15f * scaleFactor))
                .setX(new PixelConstraint(580f * scaleFactor))
                .setY(new PixelConstraint(50f * scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);

        new UIText("" + this.chestplateName)
                .setTextScale(new PixelConstraint(1.15f * scaleFactor))
                .setX(new PixelConstraint(580f * scaleFactor))
                .setY(new PixelConstraint(85f * scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);

        new UIText("" + this.leggingsName)
                .setTextScale(new PixelConstraint(1.15f * scaleFactor))
                .setX(new PixelConstraint(580f * scaleFactor))
                .setY(new PixelConstraint(120f * scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);

        new UIText("" + this.bootsName)
                .setTextScale(new PixelConstraint(1.15f * scaleFactor))
                .setX(new PixelConstraint(580f * scaleFactor))
                .setY(new PixelConstraint(155f * scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);
    }

    private void createMemberBlockColumnFive(UIComponent memberBlock, float scaleFactor) {
        UIComponent kickButton = new UIRoundedRectangle(10f)
                .setX(new PixelConstraint(800f * scaleFactor))
                .setY(new PixelConstraint(15f * scaleFactor))
                .setWidth(new PixelConstraint(125f * scaleFactor))
                .setHeight(new PixelConstraint(55f * scaleFactor))
                .setColor(PartlySaneSkies.DARK_ACCENT_COLOR)
                .setChildOf(memberBlock);

        new UIText("Kick")
                .setTextScale(new PixelConstraint(2 * scaleFactor))
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setColor(Color.white)
                .setChildOf(kickButton);

        kickButton.onMouseClickConsumer(event -> {
            PartlySaneSkies.minecraft.thePlayer.sendChatMessage("/party kick " + this.username);
        });

        UIComponent promoteButton = new UIRoundedRectangle(10f)
                .setX(new PixelConstraint(800f * scaleFactor))
                .setY(new PixelConstraint(75 * scaleFactor))
                .setWidth(new PixelConstraint(125f * scaleFactor))
                .setHeight(new PixelConstraint(55f * scaleFactor))
                .setColor(PartlySaneSkies.DARK_ACCENT_COLOR)
                .setChildOf(memberBlock);

        new UIText("Promote")

                .setTextScale(new PixelConstraint(2 * scaleFactor))
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setColor(Color.white)
                .setChildOf(promoteButton);

        promoteButton.onMouseClickConsumer(event -> {
            PartlySaneSkies.minecraft.thePlayer.sendChatMessage("/party promote " + this.username);
        });

        UIComponent transferButton = new UIRoundedRectangle(10f)
                .setX(new PixelConstraint(800f * scaleFactor))
                .setY(new PixelConstraint(135f * scaleFactor))
                .setWidth(new PixelConstraint(125f * scaleFactor))
                .setHeight(new PixelConstraint(55f * scaleFactor))
                .setColor(PartlySaneSkies.DARK_ACCENT_COLOR)
                .setChildOf(memberBlock);

        new UIText("Transfer")
                .setTextScale(new PixelConstraint(2 * scaleFactor))
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setColor(Color.white)
                .setChildOf(transferButton);

        transferButton.onMouseClickConsumer(event -> {
            PartlySaneSkies.minecraft.thePlayer.sendChatMessage("/party transfer " + this.username);
        });

        UIComponent refreshButton = new UIRoundedRectangle(10f)
                .setX(new PixelConstraint(memberBlock.getWidth() - 30f * scaleFactor))
                .setY(new PixelConstraint(10f * scaleFactor))
                .setWidth(new PixelConstraint(20f * scaleFactor))
                .setHeight(new PixelConstraint(20f * scaleFactor))
                .setColor(new Color(60, 222, 79))
                .setChildOf(memberBlock);

        UIImage.ofResource("/assets/partlysaneskies/textures/gui/party_finder/refresh.png")
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setWidth(new PixelConstraint(20f * scaleFactor))
                .setHeight(new PixelConstraint(20f * scaleFactor))
                .setChildOf(refreshButton);

        refreshButton.onMouseClickConsumer(event -> {
            this.timeDataGet = 0;
            PartlySaneSkies.minecraft.displayGuiScreen(null);
            PartyManager.startPartyManager();
        });
    }


    public boolean isExpired() {
        if (this.rank.equals(PartyRank.LEADER) && this.isPlayer)
            return this.timeDataGet + PartlySaneSkies.config.partyManagerCacheTime * 60 * 1000 * 2 < Minecraft.getSystemTime();
        if (this.refresh) {
            this.refresh = false;
            return true;
        }
        return this.timeDataGet + PartlySaneSkies.config.partyManagerCacheTime * 60 * 1000 < Minecraft.getSystemTime();
    }

    private static String formatText(String text) {
        text = Utils.removeColorCodes(text);
        text = text.replace("✪", "*");
        text = text.replaceAll("\\P{Print}", "");
        while (Character.isWhitespace(text.charAt(0))) {
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
