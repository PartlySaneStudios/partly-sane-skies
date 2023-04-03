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


package me.partlysanestudios.partlysaneskies.dungeons;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import me.partlysanestudios.partlysaneskies.utils.StringUtils;
import me.partlysanestudios.partlysaneskies.utils.Utils;
import me.partlysanestudios.partlysaneskies.utils.requests.Request;
import me.partlysanestudios.partlysaneskies.utils.requests.RequestsManager;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PlayerRating {

    private static String currentPlayer = "";

    public static HashMap<String, String> positivePatterns = new HashMap<String, String>();

    // A map that has the <Pattern, <Cause Category, Points>>
    private static HashMap<String, HashMap<String, Integer>> playerPointCategoryMap = new HashMap<String, HashMap<String, Integer> >(); 
    // A map that has <Player, Total Points> Map
    private static HashMap<String, Integer> totalPlayerPoints = new HashMap<String, Integer>();
    // A map that has <Category, Total Count>
    private static HashMap<String, Integer> categoryPointMap = new HashMap<String, Integer>();
    private static int totalPoints = 0;
    
    public static void initPatterns() {
        currentPlayer = PartlySaneSkies.minecraft.getSession().getUsername();

        try {
            RequestsManager.newRequest(new Request("https://raw.githubusercontent.com/PartlySaneStudios/partly-sane-skies-public-data/main/data/constants/dungeons_player_rate_pattern_strings.json", s -> {
                if (!s.hasSucceeded()) {
                    return;
                }
                JsonObject patternJson = new JsonParser().parse(s.getResponse()).getAsJsonObject();

                JsonObject positivePatternsJson = patternJson.getAsJsonObject("positive_strings");

                for (Map.Entry<String, JsonElement> entry : positivePatternsJson.entrySet()) {
                    positivePatterns.put(entry.getKey(), entry.getValue().getAsString());
                }
            }
            ));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        
    }

    public static void rackPoints(String player, String category) {
        if (player.equalsIgnoreCase("You")) {
            player = currentPlayer;
        }

        for (String rank : PartlySaneSkies.RANK_NAMES) {
            player = player.replace(rank, "");
        }

        player = player.replaceAll("\\P{Print}", "");
        player = StringUtils.stripLeading(player);
        player = StringUtils.stripTrailing(player);

        // If the player has already been registered
        if (playerPointCategoryMap.containsKey(player)) {
            HashMap<String, Integer> individualPlayerPointMap = playerPointCategoryMap.get(player);
            // If the player already has this category registered
            if (individualPlayerPointMap.containsKey(category)) {
                individualPlayerPointMap.put(category, individualPlayerPointMap.get(category) + 1);
            }
            // If this category doesnt exist yet
            else {
                individualPlayerPointMap.put(category, 1);
            }

            totalPlayerPoints.put(player, totalPlayerPoints.get(player) + 1);
        }
        // If this player doesn't exist yet
        else {
            HashMap<String, Integer> individualPlayerPointMap = new HashMap<String, Integer>();
            individualPlayerPointMap.put(category, 1);
            playerPointCategoryMap.put(player, individualPlayerPointMap);
            totalPlayerPoints.put(player, 1);
        }

        // If the categoryPointMap contains the category
        if (categoryPointMap.containsKey(category)) {
            categoryPointMap.put(category, categoryPointMap.get(category) + 1);
        }
        // If the categoryPointMap does not contain the category
        else {
            categoryPointMap.put(category, 1);
        }


        totalPoints ++;
    }
    public static String getDisplayString() {
        String str = "";

        if (PartlySaneSkies.config.enhancedDungeonPlayerBreakdown == 0) { 
            for (Map.Entry<String, HashMap<String, Integer>> entry : playerPointCategoryMap.entrySet()) {
                String playerStr = "&d" + entry.getKey() + "  &9" + Utils.round((double) totalPlayerPoints.get(entry.getKey()) / totalPoints * 100d, 0) +"%&7 | ";
                
                str += playerStr;
            }
            
            return StringUtils.colorCodes(str);
        }

        str += "&a&nDungeon Overview:\n\n";
        for (Map.Entry<String, HashMap<String, Integer>> entry : playerPointCategoryMap.entrySet()) {
            String playerName = entry.getKey();
            String playerStr = "&d" + playerName + "&7 completed &d" + Utils.round((double) totalPlayerPoints.get(playerName) / totalPoints * 100d, 0) +"%&7 of the dungeon.\n";
            if (PartlySaneSkies.config.enhancedDungeonPlayerBreakdown == 2) {
                playerStr += "&2   Breakdown:\n";
                for (Map.Entry<String, Integer> entry2 : entry.getValue().entrySet()) {
                    playerStr += "     &d" +  Utils.round((double) entry2.getValue() / categoryPointMap.get(entry2.getKey()) * 100d, 0) + "%&7 of " + entry2.getKey() + "\n";
                }
            }
            
            str += playerStr;
        }

        str = StringUtils.colorCodes(str);



        return str;
    }

    public static String getChatMessage() {
        String str = "";

        str += "Partly Sane Skies > ";
        
        for (Map.Entry<String, HashMap<String, Integer>> entry : playerPointCategoryMap.entrySet()) {
            String playerStr = "" + entry.getKey() + "  " + Utils.round((double) totalPlayerPoints.get(entry.getKey()) / totalPoints * 100d, 0) + "% | ";
            
            str += playerStr;
        }

        return str;
    }


    public static void handleMessage(String message) {
        for (Map.Entry<String, String> entry : positivePatterns.entrySet()) {
            if (!StringUtils.startsWithPattern(message, entry.getKey(), "{player}")) {
                continue;
            }

            rackPoints(StringUtils.recognisePattern(message, entry.getKey(), "{player}"), entry.getValue());
        }
    }

    public static void reset() {
        categoryPointMap = new HashMap<String, Integer>();
        totalPlayerPoints = new HashMap<String, Integer>();
        playerPointCategoryMap = new HashMap<String, HashMap<String, Integer> >();

        totalPoints = 0;
    }

    // §r§fTeam Score: §r
    @SubscribeEvent
    public void onChatEvent(ClientChatReceivedEvent event) {
        if (!PartlySaneSkies.config.dungeonPlayerBreakdown) {
            return;
        }
        // If end of dungeon
        if (event.message.getFormattedText().contains("§r§fTeam Score: §r")) {
            final String string = getDisplayString();
            
            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(125);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    PartlySaneSkies.minecraft.addScheduledTask(() -> { 
                        if (string.equals("")) {
                            return;
                        }
                        Utils.sendClientMessage(string, true);
                    });
                }
            }.start();
            if (PartlySaneSkies.config.partyChatDungeonPlayerBreakdown) {
                PartlySaneSkies.minecraft.thePlayer.sendChatMessage("/pc " + getChatMessage());
            }
            reset();
            return;
        }

        if (event.message.getUnformattedText().contains("You are playing on profile:") || event.message.getFormattedText().contains("[NPC] §bMort§f: Here, I found this map when I first entered")) {
            reset();
            return;
        }
        
        handleMessage(event.message.getUnformattedText());
    }
}
