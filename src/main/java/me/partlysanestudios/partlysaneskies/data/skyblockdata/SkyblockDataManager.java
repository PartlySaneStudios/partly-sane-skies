//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.data.skyblockdata;

import com.google.gson.*;
import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import me.partlysanestudios.partlysaneskies.data.pssdata.PublicDataManager;
import me.partlysanestudios.partlysaneskies.system.requests.Request;
import me.partlysanestudios.partlysaneskies.system.requests.RequestsManager;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SkyblockDataManager {

//    --------------------------- Items ---------------------------
    public static ArrayList<String> bitIds = new ArrayList<>();
    private static HashMap<String, String> nameToIdMap = new HashMap<>();
    private static HashMap<String, SkyblockItem> idToItemMap = new HashMap<>();
    private static long lastAhUpdateTime = PartlySaneSkies.getTime();

    public static boolean checkLastUpdate() {
        return PartlySaneSkies.getTime() >= lastAhUpdateTime + (1000 * 60 * 5);
    }

    public static void updateBz() {
        try{
            RequestsManager.newRequest(new Request("https://api.hypixel.net/skyblock/bazaar", s -> {
                if (!s.hasSucceeded()) {
                    return;
                }
                JsonObject object = new JsonParser().parse(s.getResponse()).getAsJsonObject().getAsJsonObject("products");


                for (Map.Entry<String, SkyblockItem> en : idToItemMap.entrySet()) {
                    if (!object.has(en.getKey())) {
                        continue;
                    }
                    en.getValue().setBazaarSellPrice(object.getAsJsonObject(en.getKey()).getAsJsonObject("quick_status").get("sellPrice").getAsDouble());
                    en.getValue().setBazaarBuyPrice(object.getAsJsonObject(en.getKey()).getAsJsonObject("quick_status").get("buyPrice").getAsDouble());
                }
            }));


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updateAverageLowestBin() {
        try {
            RequestsManager.newRequest(new Request("https://moulberry.codes/auction_averages_lbin/1day.json", s -> {
                if (!s.hasSucceeded()) {
                    return;
                }
                @SuppressWarnings("unchecked")
                HashMap<String, Double> map = (HashMap<String, Double>) new Gson().fromJson(s.getResponse(),
                        HashMap.class);

                for (Map.Entry<String, SkyblockItem> en : idToItemMap.entrySet()) {
                    if (!map.containsKey(en.getKey())) {
                        continue;
                    }
                    en.getValue().setAverageLowestBinPrice(map.get(en.getKey()));
                }
            }));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updateAll() {
        lastAhUpdateTime = PartlySaneSkies.getTime();
        updateAverageLowestBin();
        updateBz();
        updateLowestBin();

    }

    public static void updateLowestBin() {
        try {
            RequestsManager.newRequest(new Request("http://moulberry.codes/lowestbin.json", s -> {
                if (!s.hasSucceeded()) {
                    return;
                }
                @SuppressWarnings("unchecked")
                HashMap<String, Double> map = (HashMap<String, Double>) new Gson().fromJson(s.getResponse(),
                        HashMap.class);

                for (Map.Entry<String, SkyblockItem> en : idToItemMap.entrySet()) {
                    if (!map.containsKey(en.getKey())) {
                        continue;
                    }
                    en.getValue().setLowestBinPrice(map.get(en.getKey()));
                }
            }));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void initItems() throws IOException {
        RequestsManager.newRequest(new Request("https://api.hypixel.net/resources/skyblock/items", s -> {
            String itemDataString = s.getResponse();
            if (!s.hasSucceeded()) {

                return;
            }
            JsonObject itemDataJson = new JsonParser().parse(itemDataString).getAsJsonObject();

            JsonArray itemArray = itemDataJson.get("items").getAsJsonArray();

            nameToIdMap = new HashMap<>();
            idToItemMap = new HashMap<>();

            for (JsonElement itemJson : itemArray) {
                JsonObject itemObject = itemJson.getAsJsonObject();

                String id = itemObject.get("id").getAsString();
                String name = itemObject.get("name").getAsString();
                double npcSellPrice = -1;

                if (itemObject.has("npc_sell_price")) {
                    npcSellPrice = itemObject.get("npc_sell_price").getAsDouble();
                }

                String rarity = "COMMON";
                if (itemObject.has("tier")) {
                    rarity = itemObject.get("tier").getAsString();
                }


                SkyblockItem item = new SkyblockItem(id, name, npcSellPrice, rarity);
                nameToIdMap.put(name, id);
                idToItemMap.put(id, item);
            }
        }));

    }

    public static void initBitValues() throws IOException {
        JsonObject bitsShopObject = new JsonParser().parse(PublicDataManager.getFile("constants/bits_shop.json")).getAsJsonObject().getAsJsonObject("bits_shop");
        for (Map.Entry<String, JsonElement> entry : bitsShopObject.entrySet()) {
            String id = entry.getKey();
            int bitCost = entry.getValue().getAsInt();
            SkyblockItem item = getItem(id);
            if (item == null) {
                continue;
            }
            bitIds.add(item.getId());
            item.setBitCost(bitCost);
        }

    }

    public static String getId(String name) {
        return nameToIdMap.get(name);
    }

    public static SkyblockItem getItem(String id) {
        return idToItemMap.get(id);
    }

    public static void runUpdater() {
        if (checkLastUpdate()) {
            lastAhUpdateTime = PartlySaneSkies.getTime();
            new Thread(() -> {
                lastAhUpdateTime = PartlySaneSkies.getTime();
                updateAll();
                lastAhUpdateTime = PartlySaneSkies.getTime();
            }).start();
            lastAhUpdateTime = PartlySaneSkies.getTime();
        }

    }









    //    --------------------------- Skills ---------------------------
    private static HashMap<String, SkyblockSkill> idToSkillMap = new HashMap<>();
    public static void initSkills() throws MalformedURLException {
        RequestsManager.newRequest(new Request("https://api.hypixel.net/resources/skyblock/skills", s -> {
            String itemDataString = s.getResponse();
            if (!s.hasSucceeded()) {
                return;
            }
            JsonObject skillDataJson = new JsonParser().parse(itemDataString).getAsJsonObject();

            JsonObject skillObject = skillDataJson.get("skills").getAsJsonObject();

            idToSkillMap = new HashMap<>();

//            Goes through each skill
            for (Map.Entry<String, JsonElement> en : skillObject.entrySet()) {
//                Gets the id from the key of the element
                String id = en.getKey();
//                Gets the data from the skill
                JsonObject object = en.getValue().getAsJsonObject();

//                Gets the max level
                int maxLevel = object.get("maxLevel").getAsInt();
//                Gets the json array containing the level
                JsonArray levelArray = object.getAsJsonArray("levels");
//                Creates a new hashmap to store the level and the total Experience Required to get to that level
                HashMap<Integer, Float> levelToExpMap = new HashMap<>();
//                Adds the level to the map
                for (int i = 0; i < levelArray.size(); i++) {
                    int level = i + 1;
                    float experience = levelArray.get(i).getAsJsonObject().get("totalExpRequired").getAsFloat();

                    levelToExpMap.put(level, experience);
                }
//                Instantiates a new skyblock skill with all the data
                SkyblockSkill skill = new SkyblockSkill(id, maxLevel, levelToExpMap);
//                Adds the skill to the map
                idToSkillMap.put(id, skill);
            }
        }));
    }

    public static SkyblockSkill getSkill(String skillId) {
        return idToSkillMap.get(skillId);
    }


    //    --------------------------- Players ---------------------------
    private static final HashMap<String, SkyblockPlayer> playerCache = new HashMap<>();

    public static SkyblockPlayer getPlayer(String username) throws MalformedURLException {
        SkyblockPlayer player;
        if (playerCache.containsKey(username)) {
            player = playerCache.get(username);

            if (!player.isExpired()) {
                return player;
            }

        }
        else {
            player = new SkyblockPlayer(username);

        }
        player.instantiatePlayer();
        playerCache.put(username, player);
        return player;
    }
}
