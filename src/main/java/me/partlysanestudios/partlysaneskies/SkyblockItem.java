package me.partlysanestudios.partlysaneskies;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import me.partlysanestudios.partlysaneskies.utils.Utils;
import net.minecraft.client.Minecraft;

public class SkyblockItem {
    private String id;
    private String name;
    private String rarity;
    private double npcSellPrice;
    private double bazaarPrice;
    private double lowestBinPrice;
    private double averageLowestBinPrice;

    public SkyblockItem(String id, String name, double npcSellPrice, String rarity) {
        this.id = id;
        this.name = name;
        this.rarity = rarity;
        this.npcSellPrice = npcSellPrice;
        this.bazaarPrice = -1;
        this.lowestBinPrice = -1;
        this.averageLowestBinPrice = -1;
    }


    public SkyblockItem setBazaarPrice(double price) {
        this.bazaarPrice = price;
        return this;
    }

    public SkyblockItem setLowestBinPrice(double price) {
        this.lowestBinPrice = price;

        return this;
    }

    public SkyblockItem setAverageLowestBinPrice(double price) {
        this.averageLowestBinPrice = price;
        return this;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRarity() {
        return rarity;
    }

    public double getPrice() {
        if (getBazaarPrice() != -1) {
            return getBazaarPrice();
        }
        if (getLowestBin() != -1) {
            return getLowestBin();
        }
        if (getNpcSellPrice() != -1) {
            return getAverageLowestBin();
        }
        return -1;
    }

    public double getLowestBin() {
        return this.lowestBinPrice;
    }

    public double getNpcSellPrice() {
        return this.npcSellPrice;
    }

    public double getBazaarPrice() {
        return this.bazaarPrice;
    }

    public double getAverageLowestBin() {
        if (this.averageLowestBinPrice != -1) {
            return this.averageLowestBinPrice;
        }
        return -1;
    }

    public boolean hasAverageLowestBin() {
        if (this.averageLowestBinPrice == -1) {
            return false;
        }
        return true;
    }

    public boolean hasPrice() {
        if (getPrice() == -1) {
            return false;
        }
        return true;
    }



    // Static funcitions

    private static HashMap<String, String> nameToIdMap;
    private static HashMap<String, SkyblockItem> idToItemMap;
    private static long lastAhUpdateTime = 0;

    public static void init() throws IOException {
        String itemDataString = Utils.getRequest("https://api.hypixel.net/resources/skyblock/items");
        
        JsonObject itemDataJson = new JsonParser().parse(itemDataString).getAsJsonObject();

        JsonArray itemArray = itemDataJson.get("items").getAsJsonArray();
        
        nameToIdMap = new HashMap<String, String>();
        idToItemMap = new HashMap<String, SkyblockItem>();

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
    }

    public static String getId(String name) {
        return nameToIdMap.get(name);
    }


    public static SkyblockItem getItem(String id) {
        return idToItemMap.get(id);
    }


    public static void runUpdater() {
        if (checkLastUpdate()) {
            lastAhUpdateTime = Minecraft.getSystemTime();
            new Thread() {
                @Override
                public void run() {
                    updateAll();
                }
            }.start();
        }
    }
    
    public static void updateAll() {
        updateAverageLowestBin();;
        updateBz();
        updateLowestBin();
    }

    public static void updateLowestBin() {
        try {
            String request = Utils.getRequest("http://moulberry.codes/lowestbin.json");
            if (request.startsWith("Error")) {
                return;
            }
            @SuppressWarnings("unchecked")
            HashMap<String, Double> map = (HashMap<String, Double>) new Gson().fromJson(request,
                    new HashMap<String, Double>().getClass());

            for (Map.Entry<String, SkyblockItem> en : idToItemMap.entrySet()) {
                if (!map.containsKey(en.getKey())) {
                    continue;
                }
                en.getValue().setLowestBinPrice(map.get(en.getKey()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void updateAverageLowestBin() {
        try {
            String request = Utils.getRequest("https://moulberry.codes/auction_averages_lbin/1day.json");
            if (request.startsWith("Error")) {
                return;
            }
            @SuppressWarnings("unchecked")
            HashMap<String, Double> map = (HashMap<String, Double>) new Gson().fromJson(request,
                    new HashMap<String, Double>().getClass());

            for (Map.Entry<String, SkyblockItem> en : idToItemMap.entrySet()) {
                if (!map.containsKey(en.getKey())) {
                    continue;
                }
                en.getValue().setAverageLowestBinPrice(map.get(en.getKey()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updateBz() {
        try{
            String requestResponse = Utils.getRequest("https://sky.shiiyu.moe/api/v2/bazaar");
            if (requestResponse.startsWith("Error")) {
                return;
            }
            JsonObject object = new JsonParser().parse(requestResponse).getAsJsonObject();

            for (Map.Entry<String, SkyblockItem> en : idToItemMap.entrySet()) {
                if (!object.has(en.getKey())) {
                    continue;
                }
                en.getValue().setBazaarPrice(object.getAsJsonObject(en.getKey()).get("price").getAsDouble());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean checkLastUpdate() {
        if (Minecraft.getSystemTime() < lastAhUpdateTime + (1000 * 60 * 3)) {
            return true;
        }

        return false;
    }
}
