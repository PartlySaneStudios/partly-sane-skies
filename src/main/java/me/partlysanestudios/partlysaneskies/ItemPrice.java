package me.partlysanestudios.partlysaneskies;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import me.partlysanestudios.partlysaneskies.utils.Utils;
import net.minecraft.client.Minecraft;

public class ItemPrice {
    private static HashMap<String, Double> lowestBin;
    private static HashMap<String, Double> avgLowestBin;
    private static HashMap<String, Double> bazaarPrice;
    private static long lastAhUpdateTime = 0;

    public static void runUpdater() {
        if (checkLastUpdate()) {
            lastAhUpdateTime = Minecraft.getSystemTime();
            updateAh();
            updateBz();
        }
    }

    public static void updateAh() {
        new Thread() {
            @Override
            public void run() {
                try {
                    String request = Utils.getRequest("http://moulberry.codes/lowestbin.json");
                    @SuppressWarnings("unchecked")
                    HashMap<String, Double> map = (HashMap<String, Double>) new Gson().fromJson(request,
                            new HashMap<String, Double>().getClass());

                    lowestBin = map;
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    String request = Utils.getRequest("https://moulberry.codes/auction_averages_lbin/1day.json");
                    @SuppressWarnings("unchecked")
                    HashMap<String, Double> map = (HashMap<String, Double>) new Gson().fromJson(request,
                            new HashMap<String, Double>().getClass());

                    avgLowestBin = map;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        ;
    }

    public static void updateBz() {
        new Thread() {
            @Override
            public void run() {
                try{
                    String requestResponse = Utils.getRequest("https://sky.shiiyu.moe/api/v2/bazaar");
                    JsonObject object = new JsonParser().parse(requestResponse).getAsJsonObject();
                    bazaarPrice = new HashMap<String, Double>();

                    for (Map.Entry<String, JsonElement> en: object.entrySet()) {
                        JsonObject jsonObj = en.getValue().getAsJsonObject();
                        double price = jsonObj.get("price").getAsDouble();

                        bazaarPrice.put(en.getKey(), price);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public static boolean checkLastUpdate() {
        if (Minecraft.getSystemTime() < lastAhUpdateTime + 1000 * 60 * 3) {
            return true;
        }

        return false;
    }

    // Returns the price of any given object
    public static double getPrice(String id) {
        if (bazaarPrice.containsKey(id)) {
            return bazaarPrice.get(id);
        }
        if (lowestBin.containsKey(id)) {
            return lowestBin.get(id);
        }
        return -1;
    }

    public static double getAverageLowestBin(String id) {
        if (avgLowestBin.containsKey(id)) {
            return avgLowestBin.get(id);
        }
        return -1;
    }

    public static boolean containsAverageLowestBin(String id) {
        if (avgLowestBin == null) {
            return false;
        }
        return avgLowestBin.containsKey(id);
    }

    public static boolean containsPrice(String id) {
        if (lowestBin == null || bazaarPrice == null) {
            return false;
        }
        return lowestBin.containsKey(id) || bazaarPrice.containsKey(id);
    }
}
