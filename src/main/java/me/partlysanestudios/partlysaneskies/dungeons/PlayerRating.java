//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.dungeons;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import me.partlysanestudios.partlysaneskies.data.pssdata.PublicDataManager;
import me.partlysanestudios.partlysaneskies.system.commands.PSSCommand;
import me.partlysanestudios.partlysaneskies.utils.ChatUtils;
import me.partlysanestudios.partlysaneskies.utils.MathUtils;
import me.partlysanestudios.partlysaneskies.utils.StringUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PlayerRating {

    private static String currentPlayer = "";

    public static HashMap<String, String> positivePatterns = new HashMap<>();

    // A map that has the <Pattern, <Cause Category, Points>>
    private static HashMap<String, HashMap<String, Integer>> playerPointCategoryMap = new HashMap<>();
    // A map that has <Player, Total Points> Map
    private static HashMap<String, Integer> totalPlayerPoints = new HashMap<>();
    // A map which has <Category, Total Count>
    private static HashMap<String, Integer> categoryPointMap = new HashMap<>();
    private static int totalPoints = 0;

    public static String lastMessage = "";
    
    public static void initPatterns() {
        currentPlayer = PartlySaneSkies.minecraft.getSession().getUsername();

        String str = PublicDataManager.getFile("constants/dungeons_player_rate_pattern_strings.json");
        if (str.equals("")) {
            return;
        }
        JsonObject patternJson = new JsonParser().parse(str).getAsJsonObject();
        JsonObject positivePatternsJson = patternJson.getAsJsonObject("positive_strings");

        for (Map.Entry<String, JsonElement> entry : positivePatternsJson.entrySet()) {
            positivePatterns.put(entry.getKey(), entry.getValue().getAsString());
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
        player = StringUtils.INSTANCE.stripLeading(player);
        player = StringUtils.INSTANCE.stripTrailing(player);

        // If the player has already been registered
        if (playerPointCategoryMap.containsKey(player)) {
            HashMap<String, Integer> individualPlayerPointMap = playerPointCategoryMap.get(player);
            // If the player already has this category registered
            if (individualPlayerPointMap.containsKey(category)) {
                individualPlayerPointMap.put(category, individualPlayerPointMap.get(category) + 1);
            }
            // If this category doesn't exist yet
            else {
                individualPlayerPointMap.put(category, 1);
            }

            totalPlayerPoints.put(player, totalPlayerPoints.get(player) + 1);
        }
        // If this player doesn't exist yet
        else {
            HashMap<String, Integer> individualPlayerPointMap = new HashMap<>();
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
        StringBuilder str = new StringBuilder();

        if (PartlySaneSkies.config.enhancedDungeonPlayerBreakdown == 0) { 
            for (Map.Entry<String, HashMap<String, Integer>> entry : playerPointCategoryMap.entrySet()) {
                String playerStr = "§d" + entry.getKey() + "  §9" + MathUtils.INSTANCE.round((double) totalPlayerPoints.get(entry.getKey()) / totalPoints * 100d, 0) +"%§7 | ";
                
                str.append(playerStr);
            }
            
            return (str.toString());
        }

        str.append("§a§nDungeon Overview:\n\n");
        for (Map.Entry<String, HashMap<String, Integer>> entry : playerPointCategoryMap.entrySet()) {
            String playerName = entry.getKey();
            StringBuilder playerStr = new StringBuilder("§d" + playerName + "§7 completed §d" + MathUtils.INSTANCE.round((double) totalPlayerPoints.get(playerName) / totalPoints * 100d, 0) + "%§7 of the dungeon.\n");
            if (PartlySaneSkies.config.enhancedDungeonPlayerBreakdown == 2) {
                playerStr.append("§2   Breakdown:\n");
                for (Map.Entry<String, Integer> entry2 : entry.getValue().entrySet()) {
                    playerStr.append("     §d").append(MathUtils.INSTANCE.round((double) entry2.getValue() / categoryPointMap.get(entry2.getKey()) * 100d, 0)).append("%§7 of ").append(entry2.getKey()).append("\n");
                }
            }
            
            str.append(playerStr);
        }

        str = new StringBuilder((str.toString()));



        return str.toString();
    }

    public static String getChatMessage() {
        StringBuilder str = new StringBuilder();

        str.append("Partly Sane Skies > ");
        
        for (Map.Entry<String, HashMap<String, Integer>> entry : playerPointCategoryMap.entrySet()) {
            String playerStr = entry.getKey() + "  " + MathUtils.INSTANCE.round((double) totalPlayerPoints.get(entry.getKey()) / totalPoints * 100d, 0) + "% | ";
            
            str.append(playerStr);
        }

        return str.toString();
    }


    public static void handleMessage(String message) {
        for (Map.Entry<String, String> entry : positivePatterns.entrySet()) {
            if (!StringUtils.INSTANCE.startsWithPattern(message, entry.getKey(), "{player}")) {
                continue;
            }

            rackPoints(StringUtils.INSTANCE.recognisePattern(message, entry.getKey(), "{player}"), entry.getValue());
        }
    }

    public static void reset() {
        categoryPointMap = new HashMap<>();
        totalPlayerPoints = new HashMap<>();
        playerPointCategoryMap = new HashMap<>();

        totalPoints = 0;
    }
    
    public static void reprintLastScore() {
        ChatUtils.INSTANCE.sendClientMessage(lastMessage, true);
    }

    public static void registerReprintCommand() {
        new PSSCommand("reprintscore", Arrays.asList("rps", "rs"), "Reprints the last score in a dungeon run", (s, a) -> {
            reprintLastScore();
        }).register();
    }

    // §r§fTeam Score: §r
    @SubscribeEvent
    public void onChatEvent(ClientChatReceivedEvent event) {
        if (!PartlySaneSkies.config.dungeonPlayerBreakdown) {
            return;
        }
        // If end of dungeon
        if (event.message.getFormattedText().contains("Catacombs Experience§r")) {
            final String string = getDisplayString();
            lastMessage = string;

            new Thread(() -> {
                try {
                    Thread.sleep(125);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                PartlySaneSkies.minecraft.addScheduledTask(() -> {
                    if (string.equals("")) {
                        return;
                    }
                    ChatUtils.INSTANCE.sendClientMessage(string, true);
                });
            }).start();

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
