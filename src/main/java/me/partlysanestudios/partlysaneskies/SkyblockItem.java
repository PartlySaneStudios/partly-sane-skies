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

package me.partlysanestudios.partlysaneskies;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import me.partlysanestudios.partlysaneskies.utils.requests.Request;
import me.partlysanestudios.partlysaneskies.utils.requests.RequestsManager;
import net.minecraft.client.Minecraft;

public class SkyblockItem {
    private String id;
    private String name;
    private String rarity;
    private double npcSellPrice;
    private double bazaarPrice;
    private double lowestBinPrice;
    private double averageLowestBinPrice;
    private int bitCost;

    public SkyblockItem(String id, String name, double npcSellPrice, String rarity) {
        this.id = id;
        this.name = name;
        this.rarity = rarity;
        this.npcSellPrice = npcSellPrice;
        this.bazaarPrice = -1;
        this.lowestBinPrice = -1;
        this.averageLowestBinPrice = -1;
        this.bitCost = -1;
    }


    public SkyblockItem setBazaarPrice(double price) {
        this.bazaarPrice = price;
        return this;
    }

    public SkyblockItem setLowestBinPrice(double price) {
        this.lowestBinPrice = price;

        return this;
    }
    public SkyblockItem setBitCost(int bitCost) {
        this.bitCost = bitCost;

        return this;
    }

    public SkyblockItem setAverageLowestBinPrice(double price) {
        this.averageLowestBinPrice = price;
        return this;
    }
    
    public int getBitCost() {
        return this.bitCost;
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

    public boolean hasBitCost() {
        return bitCost != -1;
    }




    // Static funcitions

    private static HashMap<String, String> nameToIdMap = new HashMap<String, String>();
    private static HashMap<String, SkyblockItem> idToItemMap =new HashMap<String, SkyblockItem>();
    private static long lastAhUpdateTime = Minecraft.getSystemTime();
    public static ArrayList<String> bitIds = new ArrayList<String>();

    public static void init() throws IOException {
        RequestsManager.newRequest(new Request("https://api.hypixel.net/resources/skyblock/items", s -> {
            String itemDataString = s.getResponse();
            if (itemDataString.startsWith("Error")) {
                
                return;
            }
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
        }));
        
    }

    public static void initBitValues() throws IOException {
        RequestsManager.newRequest(new Request("https://raw.githubusercontent.com/PartlySaneStudios/partly-sane-skies-public-data/main/data/constants/bits_shop.json", s -> {
            if (s.getResponse().startsWith("Error")) {
                return;
            }
    
            JsonObject bitsShopObject = new JsonParser().parse(s.getResponse()).getAsJsonObject().getAsJsonObject("bits_shop");
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
        }));
        
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
                    lastAhUpdateTime = Minecraft.getSystemTime();
                    updateAll();
                    lastAhUpdateTime = Minecraft.getSystemTime();
                }
            }.start();
            lastAhUpdateTime = Minecraft.getSystemTime();
        }

    }
    
    public static void updateAll() {
        lastAhUpdateTime = Minecraft.getSystemTime();
        updateAverageLowestBin();;
        updateBz();
        updateLowestBin();
        
    }

    public static void updateLowestBin() {
        try {
            RequestsManager.newRequest(new Request("http://moulberry.codes/lowestbin.json", s -> {
                if (s.getResponse().startsWith("Error")) {
                    return;
                }
                @SuppressWarnings("unchecked")
                HashMap<String, Double> map = (HashMap<String, Double>) new Gson().fromJson(s.getResponse(),
                        new HashMap<String, Double>().getClass());
    
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

    public static void updateAverageLowestBin() {
        try {
            RequestsManager.newRequest(new Request("https://moulberry.codes/auction_averages_lbin/1day.json", s -> {
                if (s.getResponse().startsWith("Error")) {
                    return;
                }
                @SuppressWarnings("unchecked")
                HashMap<String, Double> map = (HashMap<String, Double>) new Gson().fromJson(s.getResponse(),
                        new HashMap<String, Double>().getClass());
    
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

    public static void updateBz() {
        try{
            RequestsManager.newRequest(new Request("https://sky.shiiyu.moe/api/v2/bazaar", s -> {
                if (s.getResponse().startsWith("Error")) {
                    return;
                }
                JsonObject object = new JsonParser().parse(s.getResponse()).getAsJsonObject();
    
                for (Map.Entry<String, SkyblockItem> en : idToItemMap.entrySet()) {
                    if (!object.has(en.getKey())) {
                        continue;
                    }
                    en.getValue().setBazaarPrice(object.getAsJsonObject(en.getKey()).get("price").getAsDouble());
                }
            }));
            

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean checkLastUpdate() {
        if (Minecraft.getSystemTime() < lastAhUpdateTime + (1000 * 60 * 5)) {
            return false;
        }

        return true;
    }
}
