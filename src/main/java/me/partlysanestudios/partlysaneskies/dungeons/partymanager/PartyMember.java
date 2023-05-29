//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.dungeons.partymanager;

import java.awt.Color;
import java.util.Map.Entry;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import gg.essential.elementa.UIComponent;
import gg.essential.elementa.components.UIRoundedRectangle;
import gg.essential.elementa.components.UIText;
import gg.essential.elementa.constraints.CenterConstraint;
import gg.essential.elementa.constraints.PixelConstraint;
import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import me.partlysanestudios.partlysaneskies.utils.StringUtils;
import me.partlysanestudios.partlysaneskies.utils.Utils;
import me.partlysanestudios.partlysaneskies.utils.guicomponents.PSSButton;
import net.minecraft.util.ResourceLocation;

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
    public int errorOnDataGet = 0;

    // Data
    public long timeDataGet = 0;
    public int secretsCount;
    public float skyblockLevel;
    public float senitherWeight;
    public float catacombsLevel;
    public float combatLevel;
    public float secretsPerRun;
    public float averageSkillLevel;

    public String helmetName = "(Unknown)";
    public String chestplateName = "(Unknown)";
    public String leggingsName = "(Unknown)";
    public String bootsName = "(Unknown)";
    public int arrowCount = -1;
    public String arrowCountString = "(Unknown)";

    public String petName = "(Unknown)";

    public String selectedDungeonClass = "(Unknown)";

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
        timeDataGet = PartlySaneSkies.getTime();
    }

    // Uses the username to request data from the Skycrypt API
    public void setSkycryptData(String response) throws NullPointerException {
        if (response.startsWith("Error:")) {
            return;
        }

        this.timeDataGet = PartlySaneSkies.getTime();
        // Creates a JSON parser to parse the Json returned by the request
        JsonParser parser = new JsonParser();

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

        this.arrowCount = -1;
        this.arrowCount = getArrowCount(profileItems);
        this.arrowCountString = this.arrowCount + "";
        if (arrowCount == -1) { 
            this.arrowCountString = "(Unknown)";
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

    // Looks for every element of an arrow in the player's profile
    public int getArrowCount(JsonObject itemData) {
        JsonArray quiver = itemData.getAsJsonArray("quiver");
        int sum = 0;
        for (JsonElement slotElement : quiver) {
            JsonObject slotObject = slotElement.getAsJsonObject();
            if (slotObject.getAsJsonArray("categories") == null ) {
                continue;
            }
            if (!(slotObject.getAsJsonArray("categories").size() > 0)) {
                continue;
            }
            // Check all of the categories to see if it has arrow as a tag
            for (JsonElement categoryElement : slotObject.getAsJsonArray("categories")) {
                if (categoryElement == null ) {
                    continue;
                }
                if (categoryElement.getAsString() == null) {
                    continue;
                }

                if (categoryElement.getAsString().equals("arrow")) {

                    // If it does, check to see if it has a count
                    if (slotObject.has("Count")) {
                        sum += slotObject.get("Count").getAsInt();
                    }
                    else {
                        sum += 1;
                    }
                }
            }
        }

        return sum;
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

        new UIText("Catacombs Level: " + StringUtils.formatNumber(Utils.round(this.catacombsLevel, 2)))
                .setTextScale((new PixelConstraint(1.333f * scaleFactor)))
                .setX(new PixelConstraint(20f * scaleFactor))
                .setY(new PixelConstraint(135f * scaleFactor))
                .setColor(Utils.colorCodetoColor.get("&c"))
                .setChildOf(memberBlock);

        new UIText("Average Skill Level " + StringUtils.formatNumber(Utils.round(this.averageSkillLevel, 2)))
                .setTextScale((new PixelConstraint(1.333f * scaleFactor)))
                .setX(new PixelConstraint(20f * scaleFactor))
                .setY(new PixelConstraint(150f * scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);

        new UIText("Combat Level: " + StringUtils.formatNumber(Utils.round(this.combatLevel, 2)))
                .setTextScale((new PixelConstraint(1.333f * scaleFactor)))
                .setX(new PixelConstraint(20f * scaleFactor))
                .setY(new PixelConstraint(165f * scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);
    }

    private void createMemberBlockColumnTwo(UIComponent memberBlock, float scaleFactor) {
        new UIText("Secrets: " + StringUtils.formatNumber(this.secretsCount))
                .setTextScale((new PixelConstraint(1.333f * scaleFactor)))
                .setX(new PixelConstraint(150f * scaleFactor))
                .setY(new PixelConstraint(74f * scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);

        new UIText("Secrets Per Run: " + StringUtils.formatNumber(Utils.round(this.secretsPerRun, 2)))
                .setTextScale((new PixelConstraint(1.333f * scaleFactor)))
                .setX(new PixelConstraint(150f * scaleFactor))
                .setY(new PixelConstraint(90f * scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);

        new UIText("Senither Weight: " + StringUtils.formatNumber(Utils.round((this.senitherWeight), 2)))
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

        new UIText("❤ " + StringUtils.formatNumber(Math.round(this.health)))
                .setTextScale((new PixelConstraint(1.333f * scaleFactor)))
                .setX(new PixelConstraint(20f * scaleFactor))
                .setY(new PixelConstraint(75f * scaleFactor))
                .setColor(Utils.colorCodetoColor.get("&c"))
                .setChildOf(memberBlock);

        new UIText("❈ " + StringUtils.formatNumber(Math.round(this.defense)))
                .setTextScale((new PixelConstraint(1.333f * scaleFactor)))
                .setX(new PixelConstraint(20f * scaleFactor))
                .setY(new PixelConstraint(90f * scaleFactor))
                .setColor(Utils.colorCodetoColor.get("&a"))
                .setChildOf(memberBlock);

        new UIText("EHP: " + StringUtils.formatNumber(Math.round(this.effectHealth)))
                .setTextScale((new PixelConstraint(1.3f * scaleFactor)))
                .setX(new PixelConstraint(20f * scaleFactor))
                .setY(new PixelConstraint(105f * scaleFactor))
                .setColor(new Color(45, 133, 48))
                .setChildOf(memberBlock);

        new UIText("✎ " + StringUtils.formatNumber(Math.round(this.intelligence)))
                .setTextScale((new PixelConstraint(1.333f * scaleFactor)))
                .setX(new PixelConstraint(20f * scaleFactor))
                .setY(new PixelConstraint(120f * scaleFactor))
                .setColor(Utils.colorCodetoColor.get("&b"))
                .setChildOf(memberBlock);

    }

    public Color colorFloorRuns(int floorRuns) {
        if(!PartlySaneSkies.config.toggleRunColors) {
            return Color.WHITE;
        }

        if (floorRuns <= PartlySaneSkies.config.runColorsRedMax) {
            return Color.RED;
        }
        else if (floorRuns <= PartlySaneSkies.config.runColorsYellowMax) {
            return Color.YELLOW;
        }
        else {
            return Color.GREEN;
        }
    }


    private void createMemberBlockColumnThree(UIComponent memberBlock, float scaleFactor) {
        new UIText("Runs:")
                .setTextScale(new PixelConstraint(2.5f * scaleFactor))
                .setX(new PixelConstraint(390f * scaleFactor))
                .setY(new PixelConstraint(20f * scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);

        new UIText("Floor 1: " + StringUtils.formatNumber(Math.round(this.f1Runs)))
                .setTextScale(new PixelConstraint(1.3f * scaleFactor))
                .setX(new PixelConstraint(340f * scaleFactor))
                .setY(new PixelConstraint(50f * scaleFactor))
                .setColor(colorFloorRuns(this.f1Runs))
                .setChildOf(memberBlock);

        new UIText("Floor 2: " + StringUtils.formatNumber(Math.round(this.f2Runs)))
                .setTextScale(new PixelConstraint(1.3f * scaleFactor))
                .setX(new PixelConstraint(340f * scaleFactor))
                .setY(new PixelConstraint(70f * scaleFactor))
                .setColor(colorFloorRuns(this.f2Runs))
                .setChildOf(memberBlock);

        new UIText("Floor 3: " + StringUtils.formatNumber(Math.round(this.f3Runs)))
                .setTextScale(new PixelConstraint(1.3f * scaleFactor))
                .setX(new PixelConstraint(340f * scaleFactor))
                .setY(new PixelConstraint(90f * scaleFactor))
                .setColor(colorFloorRuns(this.f3Runs))
                .setChildOf(memberBlock);

        new UIText("Floor 4: " + StringUtils.formatNumber(Math.round(this.f4Runs)))
                .setTextScale(new PixelConstraint(1.3f * scaleFactor))
                .setX(new PixelConstraint(340f * scaleFactor))
                .setY(new PixelConstraint(110f * scaleFactor))
                .setColor(colorFloorRuns(this.f4Runs))
                .setChildOf(memberBlock);

        new UIText("Floor 5: " + StringUtils.formatNumber(Math.round(this.f5Runs)))
                .setTextScale(new PixelConstraint(1.3f * scaleFactor))
                .setX(new PixelConstraint(340f * scaleFactor))
                .setY(new PixelConstraint(130f * scaleFactor))
                .setColor(colorFloorRuns(this.f5Runs))
                .setChildOf(memberBlock);

        new UIText("Floor 6: " + StringUtils.formatNumber(Math.round(this.f6Runs)))
                .setTextScale(new PixelConstraint(1.3f * scaleFactor))
                .setX(new PixelConstraint(340f * scaleFactor))
                .setY(new PixelConstraint(150f * scaleFactor))
                .setColor(colorFloorRuns(this.f6Runs))
                .setChildOf(memberBlock);

        new UIText("Floor 7: " + StringUtils.formatNumber(Math.round(this.f7Runs)))
                .setTextScale(new PixelConstraint(1.3f * scaleFactor))
                .setX(new PixelConstraint(340f * scaleFactor))
                .setY(new PixelConstraint(170f * scaleFactor))
                .setColor(colorFloorRuns(this.f7Runs))
                .setChildOf(memberBlock);

        new UIText("Master 1: " + StringUtils.formatNumber(Math.round(this.m1Runs)))
                .setTextScale(new PixelConstraint(1.3f * scaleFactor))
                .setX(new PixelConstraint(460f * scaleFactor))
                .setY(new PixelConstraint(50f * scaleFactor))
                .setColor(colorFloorRuns(this.m1Runs))
                .setChildOf(memberBlock);

        new UIText("Master 2: " + StringUtils.formatNumber(Math.round(this.m2Runs)))
                .setTextScale(new PixelConstraint(1.3f * scaleFactor))
                .setX(new PixelConstraint(460f * scaleFactor))
                .setY(new PixelConstraint(70f * scaleFactor))
                .setColor(colorFloorRuns(this.m2Runs))
                .setChildOf(memberBlock);

        new UIText("Master 3: " + StringUtils.formatNumber(Math.round(this.m3Runs)))
                .setTextScale(new PixelConstraint(1.3f * scaleFactor))
                .setX(new PixelConstraint(460f * scaleFactor))
                .setY(new PixelConstraint(90f * scaleFactor))
                .setColor(colorFloorRuns(this.m3Runs))
                .setChildOf(memberBlock);

        new UIText("Master 4: " + StringUtils.formatNumber(Math.round(this.m4Runs)))
                .setTextScale(new PixelConstraint(1.3f * scaleFactor))
                .setX(new PixelConstraint(460f * scaleFactor))
                .setY(new PixelConstraint(110f * scaleFactor))
                .setColor(colorFloorRuns(this.m4Runs))
                .setChildOf(memberBlock);

        new UIText("Master 5: " + StringUtils.formatNumber(Math.round(this.m5Runs)))
                .setTextScale(new PixelConstraint(1.3f * scaleFactor))
                .setX(new PixelConstraint(460f * scaleFactor))
                .setY(new PixelConstraint(130f * scaleFactor))
                .setColor(colorFloorRuns(this.m5Runs))
                .setChildOf(memberBlock);

        new UIText("Master 6: " + StringUtils.formatNumber(Math.round(this.m6Runs)))
                .setTextScale(new PixelConstraint(1.3f * scaleFactor))
                .setX(new PixelConstraint(460f * scaleFactor))
                .setY(new PixelConstraint(150f * scaleFactor))
                .setColor(colorFloorRuns(this.m6Runs))
                .setChildOf(memberBlock);

        new UIText("Master 7: " + StringUtils.formatNumber(Math.round(this.m7Runs)))
                .setTextScale(new PixelConstraint(1.3f * scaleFactor))
                .setX(new PixelConstraint(460f * scaleFactor))
                .setY(new PixelConstraint(170f * scaleFactor))
                .setColor(colorFloorRuns(this.m7Runs))
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
        
        Color arrowWarningColor = Color.white;
        if (this.arrowCount < PartlySaneSkies.config.arrowLowCount) {
            arrowWarningColor = Color.red;
            if (PartlySaneSkies.config.warnLowArrowsInChat && this.arrowCount >= 0) {
                String message = PartlySaneSkies.config.arrowLowChatMessage;
                message = message.replace("{player}", this.username);
                message = message.replace("{count}", this.arrowCount + "");
                PartlySaneSkies.minecraft.thePlayer.sendChatMessage("/pc " + message);
            }
        }
        new UIText("Arrows Remaining: " + this.arrowCountString)
                .setTextScale(new PixelConstraint(1.15f * scaleFactor))
                .setX(new PixelConstraint(580f * scaleFactor))
                .setY(new PixelConstraint(190f * scaleFactor))
                .setColor(arrowWarningColor)
                .setChildOf(memberBlock);
    }

    private void createMemberBlockColumnFive(UIComponent memberBlock, float scaleFactor) {
        new PSSButton()
                .setX(new PixelConstraint(800 * scaleFactor))
                .setY(new PixelConstraint(15 * scaleFactor))
                .setWidth(125f * scaleFactor)
                .setHeight(55f * scaleFactor)
                .setChildOf(memberBlock)
                .setText("Kick")
                .setTextScale(1f * scaleFactor)
                .onMouseClickConsumer(event -> {
                    PartlySaneSkies.minecraft.thePlayer.sendChatMessage("/party kick " + this.username);
                });
        
        new PSSButton()
                .setX(new PixelConstraint(800 * scaleFactor))
                .setY(new PixelConstraint(75 * scaleFactor))
                .setWidth(125f * scaleFactor)
                .setHeight(55f * scaleFactor)
                .setChildOf(memberBlock)
                .setText("Promote")
                .setTextScale(1f * scaleFactor)
                .onMouseClickConsumer(event -> {
                    PartlySaneSkies.minecraft.thePlayer.sendChatMessage("/party promote " + this.username);
                });

        new PSSButton()
                .setX(new PixelConstraint(800 * scaleFactor))
                .setY(new PixelConstraint(135 * scaleFactor))
                .setWidth(125f * scaleFactor)
                .setHeight(55f * scaleFactor)
                .setChildOf(memberBlock)
                .setText("Transfer")
                .setTextScale(1f * scaleFactor)
                .onMouseClickConsumer(event -> {
                    PartlySaneSkies.minecraft.thePlayer.sendChatMessage("/party transfer " + this.username);
                });

        UIComponent refreshButton = new UIRoundedRectangle(10f)
                .setX(new PixelConstraint(memberBlock.getWidth() - 30f * scaleFactor))
                .setY(new PixelConstraint(10f * scaleFactor))
                .setWidth(new PixelConstraint(20f * scaleFactor))
                .setHeight(new PixelConstraint(20f * scaleFactor))
                .setColor(new Color(60, 222, 79))
                .setChildOf(memberBlock);

        Utils.uiimageFromResourceLocation(new ResourceLocation("partlysaneskies:textures/gui/party_finder/refresh.png"))
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
            return this.timeDataGet + PartlySaneSkies.config.partyManagerCacheTime * 60 * 1000 * 2 < PartlySaneSkies.getTime();
        if (this.refresh) {
            this.refresh = false;
            return true;
        }
        if (this.errorOnDataGet > 1 && this.errorOnDataGet < 3) {
            return true;
        }
        return this.timeDataGet + PartlySaneSkies.config.partyManagerCacheTime * 60 * 1000 < PartlySaneSkies.getTime();
    }

    private static String formatText(String text) {
        text = StringUtils.removeColorCodes(text);
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
