package me.partlysanestudios.partlysaneskies.economy.minioncalculator;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.partlysanestudios.partlysaneskies.utils.Utils;
import me.partlysanestudios.partlysaneskies.utils.requests.Request;
import me.partlysanestudios.partlysaneskies.utils.requests.RequestsManager;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

public class MinionData {
    public static final String MINIONS_DATA_URL = "https://raw.githubusercontent.com/PartlySaneStudios/partly-sane-skies-public-data/main/data/constants/minion_data.json";
    public static HashMap<String, Minion> minions = new HashMap<>();
    public static void preRequestInit() throws MalformedURLException {
        RequestsManager.newRequest(new Request(MINIONS_DATA_URL, r -> {
            postRequestInit(r);
        }));
    }

    public static void postRequestInit(Request request) {
        JsonObject obj = new JsonParser().parse(request.getResponse()).getAsJsonObject();

        for (Map.Entry<String, JsonElement> en : obj.entrySet()) {
            String id = en.getKey();
            JsonObject minionObj = en.getValue().getAsJsonObject();
            Minion minion= new Minion(id, minionObj);
            minions.put(id, minion);
        }


    }

    public static class Minion {
        private String id;
        private String displayName;
        private HashMap<String, Double> drops;
        private HashMap<Integer, Double> cooldowns;
        private String category;
        private int maxTier;


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
    }


}
