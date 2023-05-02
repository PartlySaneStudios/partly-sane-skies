package me.partlysanestudios.partlysaneskies.economy.minioncalculator;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.partlysanestudios.partlysaneskies.SkyblockItem;
import me.partlysanestudios.partlysaneskies.utils.Utils;
import me.partlysanestudios.partlysaneskies.utils.requests.Request;
import me.partlysanestudios.partlysaneskies.utils.requests.RequestsManager;

import java.net.MalformedURLException;
import java.util.*;

public class MinionData {
    private static final String MINIONS_DATA_URL = "https://raw.githubusercontent.com/PartlySaneStudios/partly-sane-skies-public-data/main/data/constants/minion_data.json";
    public static final HashMap<String, Minion> minions = new HashMap<>();
    public static void preRequestInit() throws MalformedURLException {
        RequestsManager.newRequest(new Request(MINIONS_DATA_URL, MinionData::postRequestInit));
    }

    public static void postRequestInit(Request request) {
        JsonObject obj = new JsonParser().parse(request.getResponse()).getAsJsonObject();

        for (Map.Entry<String, JsonElement> en : obj.entrySet()) {
            String id = en.getKey();
            JsonObject minionObj = en.getValue().getAsJsonObject();
            Minion minion= new Minion(id, minionObj);
            minions.put(id, minion);
        }


        HashMap<Minion, Double> map = getMostProfitMinion();

        for (Map.Entry<Minion, Double> en : map.entrySet()) {
            Utils.visPrint(en.getKey().displayName + ": " + en.getValue() * 60);
        }
    }

    public static LinkedHashMap<Minion, Double> getMostProfitMinion() {
        HashMap<Minion, Double> priceMap = new HashMap<>();

        for (Map.Entry<String, Minion> en : minions.entrySet()) {
            double totalProfit = 0;

            Minion minion = en.getValue();

            for (Map.Entry<String, Double> en2 : minion.getItemsPerMinute(minion.maxTier).entrySet()) {
                String itemId = en2.getKey();
                double amount = en2.getValue();
                double price = 0;
                try {
                    price = SkyblockItem.getItem(itemId).getPrice();
                } catch (NullPointerException e) {
                    Utils.visPrint(itemId + ": DOES NOT HAVE PRICE");
                }


                totalProfit += price * amount;
            }

            priceMap.put(minion, totalProfit);
        }
        return sortMap(priceMap);
    }

    public static Minion getMinion(String id) {
        return minions.get(id);
    }

    // Sorts the hashmap in decending order
    public static LinkedHashMap<Minion, Double> sortMap(HashMap<Minion, Double> map) {
        List<Map.Entry<Minion, Double>> list = new LinkedList<>(map.entrySet());
        list.sort((o1, o2) -> (o2.getValue()).compareTo(o1.getValue()));

        LinkedHashMap<Minion, Double> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<Minion, Double> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }


    public static class Minion {
        private final String id;
        private final String displayName;
        private final HashMap<String, Double> drops;
        private final HashMap<Integer, Double> cooldowns;
        private final String category;
        private final int maxTier;


        public Minion(String id, JsonObject obj) {
            this.id = id;
            displayName = Utils.getJsonFromPath(obj, "/displayName").getAsString();
            maxTier = Utils.getJsonFromPath(obj, "/maxTier").getAsInt();
            category = Utils.getJsonFromPath(obj, "/category").getAsString();

            JsonObject dropsJson = Utils.getJsonFromPath(obj, "/drops").getAsJsonObject();
            drops = new HashMap<>();
            for (Map.Entry<String, JsonElement> en : dropsJson.entrySet()) {
                drops.put(en.getKey(), en.getValue().getAsDouble());
            }

            JsonObject cooldownJson = Utils.getJsonFromPath(obj, "/cooldown").getAsJsonObject();
            cooldowns = new HashMap<>();
            for (Map.Entry<String, JsonElement> en : cooldownJson.entrySet()) {
                cooldowns.put(Integer.parseInt(en.getKey()), en.getValue().getAsDouble());
            }
        }

        @Override
        public String toString() {
            return "" + id + ": Drops:" + drops.toString() + " Cooldowns:" + cooldowns.toString();
        }

        public HashMap<String, Double> getItemsPerMinute(int tier) {

            double cooldownInMiniute = 0;

            try {
                cooldownInMiniute = cooldowns.get(tier) / 60d;
            } catch (NullPointerException e) {
                Utils.visPrint(id + ": DOES NOT HAVE RIGHT TIER");
            }


            HashMap<String, Double> items = new HashMap<>();
            for (Map.Entry<String, Double> en : drops.entrySet()) {
                items.put(en.getKey(), (1 / (cooldownInMiniute * 2d)) * en.getValue());
            }

            return items;
        }
    }


}
