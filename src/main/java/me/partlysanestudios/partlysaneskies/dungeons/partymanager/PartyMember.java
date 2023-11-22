//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.dungeons.partymanager;

import gg.essential.elementa.UIComponent;
import gg.essential.elementa.components.UIRoundedRectangle;
import gg.essential.elementa.components.UIText;
import gg.essential.elementa.components.UIWrappedText;
import gg.essential.elementa.constraints.CenterConstraint;
import gg.essential.elementa.constraints.PixelConstraint;
import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import me.partlysanestudios.partlysaneskies.data.skyblockdata.SkyblockDataManager;
import me.partlysanestudios.partlysaneskies.data.skyblockdata.SkyblockPlayer;
import me.partlysanestudios.partlysaneskies.system.guicomponents.PSSButton;
import me.partlysanestudios.partlysaneskies.utils.ElementaUtils;
import me.partlysanestudios.partlysaneskies.utils.MathUtils;
import me.partlysanestudios.partlysaneskies.utils.StringUtils;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.net.MalformedURLException;

public class PartyMember {

    public enum PartyRank {
        MEMBER,
        MODERATOR,
        LEADER
    }

    public String username;
    public PartyRank rank;
    public int secretCount;
    public float skyblockLevel;
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
    SkyblockPlayer player;

    // Creates a new party member based on the username and partyRank
    public PartyMember(String username, PartyRank partyRank) {
        this.username = username;
        this.rank = partyRank;
    }

    public void setRank(PartyRank partyRank) {
        this.rank = partyRank;
    }

    public void populateData() throws MalformedURLException {

        this.player = SkyblockDataManager.getPlayer(username);

        // Gets the player's secret count
        secretCount = player.secretsCount;

        // Gets the player's catacombs' level
        catacombsLevel = player.catacombsLevel;

        // Gets the player's combat level
        combatLevel = player.combatLevel;

        // Gets the player's average skill level
        averageSkillLevel = player.averageSkillLevel;


        String[] playerArmor = player.armorName;
        if (playerArmor.length >= 3) {
            helmetName = playerArmor[3];

        }
        else {
            helmetName = "";
        }
        if (playerArmor.length >= 2) {
            chestplateName = playerArmor[2];

        }
        else {
            chestplateName = "";
        }
        if (playerArmor.length >= 1) {
            leggingsName = playerArmor[1];

        }
        else {
            leggingsName = "";
        }
        bootsName = playerArmor[0];


        // Attempts to get the selected dungeon class
        selectedDungeonClass = player.selectedDungeonClass;

        this.arrowCount = player.arrowCount;
        this.arrowCountString = String.valueOf(this.arrowCount);
        if (arrowCount == -1) { 
            this.arrowCountString = "(Unknown)";
        }

        // Gets all the floor runs
        f1Runs = player.normalRunCount[1];
        f2Runs = player.normalRunCount[2];
        f3Runs = player.normalRunCount[3];
        f4Runs = player.normalRunCount[4];
        f5Runs = player.normalRunCount[5];
        f6Runs = player.normalRunCount[6];
        f7Runs = player.normalRunCount[7];

        // Gets all the master floor runs
        m1Runs = player.masterModeRunCount[1];
        m2Runs = player.masterModeRunCount[2];
        m3Runs = player.masterModeRunCount[3];
        m4Runs = player.masterModeRunCount[4];
        m5Runs = player.masterModeRunCount[5];
        m6Runs = player.masterModeRunCount[6];
        m7Runs = player.masterModeRunCount[7];

        petName = player.petName;

        skyblockLevel = player.skyblockLevel;
        health = player.baseHealth;
        intelligence = player.baseIntelligence;
        defense = player.baseDefense;
        effectHealth = player.baseEffectedHealth;

        // Attempts to get the average secrets per run
        secretsPerRun = player.secretsPerRun;
    }

    public void createBlock(UIComponent memberBlock, float scaleFactor) {
        // Name plate
        new UIText(this.username)
                .setTextScale(new PixelConstraint(3f * scaleFactor))
                .setX(new PixelConstraint(20f * scaleFactor))
                .setY(new PixelConstraint(20f * scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);

        

        new UIText(this.selectedDungeonClass)
                .setTextScale(new PixelConstraint(scaleFactor))
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

        new UIText("Catacombs Level: " + StringUtils.INSTANCE.formatNumber(MathUtils.INSTANCE.round(this.catacombsLevel, 2)))
                .setTextScale((new PixelConstraint(1.333f * scaleFactor)))
                .setX(new PixelConstraint(20f * scaleFactor))
                .setY(new PixelConstraint(135f * scaleFactor))
                .setColor(StringUtils.INSTANCE.colorCodeToColor("§c"))
                .setChildOf(memberBlock);

        new UIText("Average Skill Level " + StringUtils.INSTANCE.formatNumber(MathUtils.INSTANCE.round(this.averageSkillLevel, 2)))
                .setTextScale((new PixelConstraint(1.333f * scaleFactor)))
                .setX(new PixelConstraint(20f * scaleFactor))
                .setY(new PixelConstraint(150f * scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);

        new UIText("Combat Level: " + StringUtils.INSTANCE.formatNumber(MathUtils.INSTANCE.round(this.combatLevel, 2)))
                .setTextScale((new PixelConstraint(1.333f * scaleFactor)))
                .setX(new PixelConstraint(20f * scaleFactor))
                .setY(new PixelConstraint(165f * scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);
    }

    private void createMemberBlockColumnTwo(UIComponent memberBlock, float scaleFactor) {
        new UIText("Secrets: " + StringUtils.INSTANCE.formatNumber(this.secretCount))
                .setTextScale((new PixelConstraint(1.333f * scaleFactor)))
                .setX(new PixelConstraint(150f * scaleFactor))
                .setY(new PixelConstraint(74f * scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);

        new UIText("Secrets Per Run: " + StringUtils.INSTANCE.formatNumber(MathUtils.INSTANCE.round(this.secretsPerRun, 2)))
                .setTextScale((new PixelConstraint(1.333f * scaleFactor)))
                .setX(new PixelConstraint(150f * scaleFactor))
                .setY(new PixelConstraint(90f * scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);

        new UIText("SkyBlock Level: " + MathUtils.INSTANCE.round(this.skyblockLevel, 1))
                .setTextScale(new PixelConstraint(scaleFactor))
                .setX(new PixelConstraint(20f * scaleFactor))
                .setY(new PixelConstraint(50f * scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);

        new UIText("❤ " + StringUtils.INSTANCE.formatNumber(Math.round(this.health)))
                .setTextScale((new PixelConstraint(1.333f * scaleFactor)))
                .setX(new PixelConstraint(20f * scaleFactor))
                .setY(new PixelConstraint(75f * scaleFactor))
                .setColor(StringUtils.INSTANCE.colorCodeToColor("§c"))
                .setChildOf(memberBlock);

        new UIText("❈ " + StringUtils.INSTANCE.formatNumber(Math.round(this.defense)))
                .setTextScale((new PixelConstraint(1.333f * scaleFactor)))
                .setX(new PixelConstraint(20f * scaleFactor))
                .setY(new PixelConstraint(90f * scaleFactor))
                .setColor(StringUtils.INSTANCE.colorCodeToColor("§a"))
                .setChildOf(memberBlock);

        new UIText("EHP: " + StringUtils.INSTANCE.formatNumber(Math.round(this.effectHealth)))
                .setTextScale((new PixelConstraint(1.3f * scaleFactor)))
                .setX(new PixelConstraint(20f * scaleFactor))
                .setY(new PixelConstraint(105f * scaleFactor))
                .setColor(new Color(45, 133, 48))
                .setChildOf(memberBlock);

        new UIText("✎ " + StringUtils.INSTANCE.formatNumber(Math.round(this.intelligence)))
                .setTextScale((new PixelConstraint(1.333f * scaleFactor)))
                .setX(new PixelConstraint(20f * scaleFactor))
                .setY(new PixelConstraint(120f * scaleFactor))
                .setColor(StringUtils.INSTANCE.colorCodeToColor("§b"))
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

        new UIText("Floor 1: " + StringUtils.INSTANCE.formatNumber(this.f1Runs))
                .setTextScale(new PixelConstraint(1.3f * scaleFactor))
                .setX(new PixelConstraint(340f * scaleFactor))
                .setY(new PixelConstraint(50f * scaleFactor))
                .setColor(colorFloorRuns(this.f1Runs))
                .setChildOf(memberBlock);

        new UIText("Floor 2: " + StringUtils.INSTANCE.formatNumber(this.f2Runs))
                .setTextScale(new PixelConstraint(1.3f * scaleFactor))
                .setX(new PixelConstraint(340f * scaleFactor))
                .setY(new PixelConstraint(70f * scaleFactor))
                .setColor(colorFloorRuns(this.f2Runs))
                .setChildOf(memberBlock);

        new UIText("Floor 3: " + StringUtils.INSTANCE.formatNumber(this.f3Runs))
                .setTextScale(new PixelConstraint(1.3f * scaleFactor))
                .setX(new PixelConstraint(340f * scaleFactor))
                .setY(new PixelConstraint(90f * scaleFactor))
                .setColor(colorFloorRuns(this.f3Runs))
                .setChildOf(memberBlock);

        new UIText("Floor 4: " + StringUtils.INSTANCE.formatNumber(this.f4Runs))
                .setTextScale(new PixelConstraint(1.3f * scaleFactor))
                .setX(new PixelConstraint(340f * scaleFactor))
                .setY(new PixelConstraint(110f * scaleFactor))
                .setColor(colorFloorRuns(this.f4Runs))
                .setChildOf(memberBlock);

        new UIText("Floor 5: " + StringUtils.INSTANCE.formatNumber(this.f5Runs))
                .setTextScale(new PixelConstraint(1.3f * scaleFactor))
                .setX(new PixelConstraint(340f * scaleFactor))
                .setY(new PixelConstraint(130f * scaleFactor))
                .setColor(colorFloorRuns(this.f5Runs))
                .setChildOf(memberBlock);

        new UIText("Floor 6: " + StringUtils.INSTANCE.formatNumber(this.f6Runs))
                .setTextScale(new PixelConstraint(1.3f * scaleFactor))
                .setX(new PixelConstraint(340f * scaleFactor))
                .setY(new PixelConstraint(150f * scaleFactor))
                .setColor(colorFloorRuns(this.f6Runs))
                .setChildOf(memberBlock);

        new UIText("Floor 7: " + StringUtils.INSTANCE.formatNumber(this.f7Runs))
                .setTextScale(new PixelConstraint(1.3f * scaleFactor))
                .setX(new PixelConstraint(340f * scaleFactor))
                .setY(new PixelConstraint(170f * scaleFactor))
                .setColor(colorFloorRuns(this.f7Runs))
                .setChildOf(memberBlock);

        new UIText("Master 1: " + StringUtils.INSTANCE.formatNumber(this.m1Runs))
                .setTextScale(new PixelConstraint(1.3f * scaleFactor))
                .setX(new PixelConstraint(460f * scaleFactor))
                .setY(new PixelConstraint(50f * scaleFactor))
                .setColor(colorFloorRuns(this.m1Runs))
                .setChildOf(memberBlock);

        new UIText("Master 2: " + StringUtils.INSTANCE.formatNumber(this.m2Runs))
                .setTextScale(new PixelConstraint(1.3f * scaleFactor))
                .setX(new PixelConstraint(460f * scaleFactor))
                .setY(new PixelConstraint(70f * scaleFactor))
                .setColor(colorFloorRuns(this.m2Runs))
                .setChildOf(memberBlock);

        new UIText("Master 3: " + StringUtils.INSTANCE.formatNumber(this.m3Runs))
                .setTextScale(new PixelConstraint(1.3f * scaleFactor))
                .setX(new PixelConstraint(460f * scaleFactor))
                .setY(new PixelConstraint(90f * scaleFactor))
                .setColor(colorFloorRuns(this.m3Runs))
                .setChildOf(memberBlock);

        new UIText("Master 4: " + StringUtils.INSTANCE.formatNumber(this.m4Runs))
                .setTextScale(new PixelConstraint(1.3f * scaleFactor))
                .setX(new PixelConstraint(460f * scaleFactor))
                .setY(new PixelConstraint(110f * scaleFactor))
                .setColor(colorFloorRuns(this.m4Runs))
                .setChildOf(memberBlock);

        new UIText("Master 5: " + StringUtils.INSTANCE.formatNumber(this.m5Runs))
                .setTextScale(new PixelConstraint(1.3f * scaleFactor))
                .setX(new PixelConstraint(460f * scaleFactor))
                .setY(new PixelConstraint(130f * scaleFactor))
                .setColor(colorFloorRuns(this.m5Runs))
                .setChildOf(memberBlock);

        new UIText("Master 6: " + StringUtils.INSTANCE.formatNumber(this.m6Runs))
                .setTextScale(new PixelConstraint(1.3f * scaleFactor))
                .setX(new PixelConstraint(460f * scaleFactor))
                .setY(new PixelConstraint(150f * scaleFactor))
                .setColor(colorFloorRuns(this.m6Runs))
                .setChildOf(memberBlock);

        new UIText("Master 7: " + StringUtils.INSTANCE.formatNumber(this.m7Runs))
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

        new UIWrappedText(this.helmetName)
                .setTextScale(new PixelConstraint(1.15f * scaleFactor))
                .setWidth(new PixelConstraint(200 * scaleFactor))
                .setX(new PixelConstraint(580f * scaleFactor))
                .setY(new PixelConstraint(50f * scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);

        new UIWrappedText(this.chestplateName)
                .setTextScale(new PixelConstraint(1.15f * scaleFactor))
                .setWidth(new PixelConstraint(200 * scaleFactor))
                .setX(new PixelConstraint(580f * scaleFactor))
                .setY(new PixelConstraint(85f * scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);

        new UIWrappedText(this.leggingsName)
                .setTextScale(new PixelConstraint(1.15f * scaleFactor))
                .setWidth(new PixelConstraint(200 * scaleFactor))
                .setX(new PixelConstraint(580f * scaleFactor))
                .setY(new PixelConstraint(120f * scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);

        new UIWrappedText(this.bootsName)
                .setTextScale(new PixelConstraint(1.15f * scaleFactor))
                .setWidth(new PixelConstraint(200 * scaleFactor))
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
                message = message.replace("{count}", String.valueOf(this.arrowCount));
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
                .setTextScale(scaleFactor)
                .onMouseClickConsumer(event -> PartlySaneSkies.minecraft.thePlayer.sendChatMessage("/party kick " + this.username));
        
        new PSSButton()
                .setX(new PixelConstraint(800 * scaleFactor))
                .setY(new PixelConstraint(75 * scaleFactor))
                .setWidth(125f * scaleFactor)
                .setHeight(55f * scaleFactor)
                .setChildOf(memberBlock)
                .setText("Promote")
                .setTextScale(scaleFactor)
                .onMouseClickConsumer(event -> PartlySaneSkies.minecraft.thePlayer.sendChatMessage("/party promote " + this.username));

        new PSSButton()
                .setX(new PixelConstraint(800 * scaleFactor))
                .setY(new PixelConstraint(135 * scaleFactor))
                .setWidth(125f * scaleFactor)
                .setHeight(55f * scaleFactor)
                .setChildOf(memberBlock)
                .setText("Transfer")
                .setTextScale(scaleFactor)
                .onMouseClickConsumer(event -> PartlySaneSkies.minecraft.thePlayer.sendChatMessage("/party transfer " + this.username));

        UIComponent refreshButton = new UIRoundedRectangle(10f)
                .setX(new PixelConstraint(memberBlock.getWidth() - 30f * scaleFactor))
                .setY(new PixelConstraint(10f * scaleFactor))
                .setWidth(new PixelConstraint(20f * scaleFactor))
                .setHeight(new PixelConstraint(20f * scaleFactor))
                .setColor(new Color(60, 222, 79))
                .setChildOf(memberBlock);

        ElementaUtils.INSTANCE.uiImageFromResourceLocation(new ResourceLocation("partlysaneskies", "textures/gui/party_finder/refresh.png"))
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setWidth(new PixelConstraint(20f * scaleFactor))
                .setHeight(new PixelConstraint(20f * scaleFactor))
                .setChildOf(refreshButton);

        refreshButton.onMouseClickConsumer(event -> {
            player.refresh();
            PartlySaneSkies.minecraft.displayGuiScreen(null);
            PartyManager.startPartyManager();
        });
    }
}
