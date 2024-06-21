//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.features.dungeons.party.partymanager;

import gg.essential.elementa.UIComponent;
import gg.essential.elementa.components.UIRoundedRectangle;
import gg.essential.elementa.components.UIText;
import gg.essential.elementa.components.UIWrappedText;
import gg.essential.elementa.constraints.CenterConstraint;
import gg.essential.elementa.constraints.PixelConstraint;
import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import me.partlysanestudios.partlysaneskies.data.skyblockdata.SkyblockDataManager;
import me.partlysanestudios.partlysaneskies.data.skyblockdata.SkyblockPlayer;
import me.partlysanestudios.partlysaneskies.render.gui.components.PSSButton;
import me.partlysanestudios.partlysaneskies.utils.ElementaUtils;
import me.partlysanestudios.partlysaneskies.utils.MathUtils;
import me.partlysanestudios.partlysaneskies.utils.StringUtils;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.net.MalformedURLException;

public class PartyMember {

    public String username;
    public PartyRank rank;
    public int secretCount;
    public double skyblockLevel;
    public double catacombsLevel;
    public double combatLevel;
    public double secretsPerRun;
    public double averageSkillLevel;
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
    public double health;
    public double defense;
    public double intelligence;
    public double effectHealth;
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

        this.player = SkyblockDataManager.INSTANCE.getPlayer(username);

        // Gets the player's secret count
        secretCount = player.getSecretsCount();

        // Gets the player's catacombs' level
        catacombsLevel = player.getCatacombsLevel();

        // Gets the player's combat level
        combatLevel = player.getCombatLevel();

        // Gets the player's average skill level
        averageSkillLevel = player.getAverageSkillLevel();


        String[] playerArmor = player.getArmorName();
        if (playerArmor.length >= 4 && playerArmor[3] != null) {
            helmetName = playerArmor[3];
        } else {
            helmetName = "";
        }

        if (playerArmor.length >= 3 && playerArmor[2] != null) {
            chestplateName = playerArmor[2];
        } else {
            chestplateName = "";
        }

        if (playerArmor.length >= 2 && playerArmor[1] != null) {
            leggingsName = playerArmor[1];
        } else {
            leggingsName = "";
        }

        if (playerArmor.length >= 1 && playerArmor[0] != null) {
            bootsName = playerArmor[0];
        } else {
            bootsName = "";
        }


        // Attempts to get the selected dungeon class
        selectedDungeonClass = player.getSelectedDungeonClass();

        this.arrowCount = player.getArrowCount();
        this.arrowCountString = String.valueOf(this.arrowCount);
        if (arrowCount == -1) {
            this.arrowCountString = "(Unknown)";
        }

        // Gets all the floor runs
        f1Runs = player.getNormalRunCount()[1];
        f2Runs = player.getNormalRunCount()[2];
        f3Runs = player.getNormalRunCount()[3];
        f4Runs = player.getNormalRunCount()[4];
        f5Runs = player.getNormalRunCount()[5];
        f6Runs = player.getNormalRunCount()[6];
        f7Runs = player.getNormalRunCount()[7];

        // Gets all the master floor runs
        m1Runs = player.getMasterModeRunCount()[1];
        m2Runs = player.getMasterModeRunCount()[2];
        m3Runs = player.getMasterModeRunCount()[3];
        m4Runs = player.getMasterModeRunCount()[4];
        m5Runs = player.getMasterModeRunCount()[5];
        m6Runs = player.getMasterModeRunCount()[6];
        m7Runs = player.getMasterModeRunCount()[7];

        petName = player.getPetName();

        skyblockLevel = player.getSkyblockLevel();
        health = player.getBaseHealth();
        intelligence = player.getBaseIntelligence();
        defense = player.getBaseDefense();
        effectHealth = player.getBaseEffectiveHealth();

        // Attempts to get the average secrets per run
        secretsPerRun = player.getSecretsPerRun();
    }

    public void createBlock(UIComponent memberBlock, float scaleFactor, PartyManagerGui partyManagerGui) {
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
        partyManagerGui.updatePartyBreakdown();
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
        if (!PartlySaneSkies.Companion.getConfig().getToggleRunColors()) {
            return Color.WHITE;
        }

        if (floorRuns <= PartlySaneSkies.Companion.getConfig().getRunColorsRedMax()) {
            return Color.RED;
        } else if (floorRuns <= PartlySaneSkies.Companion.getConfig().getRunColorsYellowMax()) {
            return Color.YELLOW;
        } else {
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

// TODO: please for the love of god automate this whenever you rewrite this code
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
        if (this.arrowCount < PartlySaneSkies.Companion.getConfig().getArrowLowCount()) {
            arrowWarningColor = Color.red;
            if (PartlySaneSkies.Companion.getConfig().getWarnLowArrowsInChat() && this.arrowCount >= 0) {
                String message = PartlySaneSkies.Companion.getConfig().getArrowLowChatMessage();
                message = message.replace("{player}", this.username);
                message = message.replace("{count}", String.valueOf(this.arrowCount));
                PartlySaneSkies.Companion.getMinecraft().thePlayer.sendChatMessage("/pc " + message);
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
                .setWidth(new PixelConstraint(125f * scaleFactor))
                .setHeight(new PixelConstraint(55f * scaleFactor))
                .setChildOf(memberBlock)
                .setText("Kick")
                .setTextScale(new PixelConstraint(scaleFactor))
                .onMouseClickConsumer(event -> PartlySaneSkies.Companion.getMinecraft().thePlayer.sendChatMessage("/party kick " + this.username));

        new PSSButton()
                .setX(new PixelConstraint(800 * scaleFactor))
                .setY(new PixelConstraint(75 * scaleFactor))
                .setWidth(new PixelConstraint(125f * scaleFactor))
                .setHeight(new PixelConstraint(55f * scaleFactor))
                .setChildOf(memberBlock)
                .setText("Promote")
                .setTextScale(new PixelConstraint(scaleFactor))
                .onMouseClickConsumer(event -> PartlySaneSkies.Companion.getMinecraft().thePlayer.sendChatMessage("/party promote " + this.username));

        new PSSButton()
                .setX(new PixelConstraint(800 * scaleFactor))
                .setY(new PixelConstraint(135 * scaleFactor))
                .setWidth(new PixelConstraint(125f * scaleFactor))
                .setHeight(new PixelConstraint(55f * scaleFactor))
                .setChildOf(memberBlock)
                .setText("Transfer")
                .setTextScale(new PixelConstraint(scaleFactor))
                .onMouseClickConsumer(event -> PartlySaneSkies.Companion.getMinecraft().thePlayer.sendChatMessage("/party transfer " + this.username));

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
            player.setLastUpdateTime(0);
            PartlySaneSkies.Companion.getMinecraft().displayGuiScreen(null);
            PartyManager.startPartyManager();
        });
    }

    public enum PartyRank {
        MEMBER,
        MODERATOR,
        LEADER
    }
}
