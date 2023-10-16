package me.partlysanestudios.partlysaneskies.utils;

import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ScoreboardUtils {

    // Gets the number of coins in your purse from the scoreboard
    public static long getCoins() {
        if (!LocationUtils.isSkyblock()) {
            return 0L;
        }

        String money = null;

        for (String line : ScoreboardUtils.getScoreboardLines()) {
            if (StringUtils.stripLeading(line).contains("Piggy:") || StringUtils.stripLeading(line).contains("Purse:")) {
                money = StringUtils.stripLeading(StringUtils.removeColorCodes(line)).replace("Piggy: ", "");
                money = StringUtils.stripLeading(StringUtils.removeColorCodes(money)).replace("Purse: ", "");
                money = StringUtils.stripLeading(money);
                money = money.replace(",", "");
                money = money.replaceAll("\\P{Print}", "");
                break;
            }
        }

        if (money == null) {
            return 0L;
        }
        try {
            return Long.parseLong(money);
        } catch (NumberFormatException event) {
            return 0;
        }
    }

    // Gets the number of bits from the scoreboard
    public static long getBits() {
        if (!LocationUtils.isSkyblock()) {
            return 0L;
        }

        String bits = null;

        for (String line : ScoreboardUtils.getScoreboardLines()) {
            if (StringUtils.stripLeading(line).contains("Bits:")) {
                bits = StringUtils.stripLeading(StringUtils.removeColorCodes(line)).replace("Bits: ", "");
                bits = StringUtils.stripLeading(bits);
                bits = bits.replace(",", "");
                bits = bits.replaceAll("\\P{Print}", "");
                break;
            }
        }

        if (bits == null) {
            return 0L;
        }

        String[] charsToRemove = {"(", ")", ".", "-", "+"};

        for (String removalChar : charsToRemove) {
            if (bits.contains(removalChar)) {
                int indexOfEndOfCount = bits.indexOf(removalChar);
                bits = bits.substring(0, indexOfEndOfCount);
            }
        }

        bits = StringUtils.stripLeading(bits);
        bits = StringUtils.stripTrailing(bits);
        try {
            return Long.parseLong(bits);
        } catch (NumberFormatException event) {
            return 0;
        }
    }



    // Returns the name of the scoreboard without color codes
    public static String getScoreboardName() {
        String scoreboardName = PartlySaneSkies.minecraft.thePlayer.getWorldScoreboard().getObjectiveInDisplaySlot(1).getDisplayName();
        return StringUtils.removeColorCodes(scoreboardName);
    }

    // Returns a list of lines on the scoreboard,
    // where each line is a new entry
    public static List<String> getScoreboardLines() {
        try {
            Scoreboard scoreboard = PartlySaneSkies.minecraft.theWorld.getScoreboard();
            ScoreObjective objective = scoreboard.getObjectiveInDisplaySlot(1);
            Collection<Score> scoreCollection = scoreboard.getSortedScores(objective);

            List<String> scoreLines = new ArrayList<>();
            for (Score score : scoreCollection) {
                scoreLines.add(ScorePlayerTeam.formatPlayerName(scoreboard.getPlayersTeam(score.getPlayerName()),
                        score.getPlayerName()));
            }

            return scoreLines;
        } catch (IllegalArgumentException e) {
            if (e.getMessage().equals("Cannot locate declared field class net.minecraft.client.gui.inventory.GuiChest.field_147015_w")) {
                System.out.println("Strange error message in PartlySaneSkies.getScoreboardLines()");
            }
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
