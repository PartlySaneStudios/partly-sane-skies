package me.partlysanestudios.partlysaneskies;

import java.io.IOException;
import java.util.HashMap;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import me.partlysanestudios.partlysaneskies.utils.Utils;

public class IdManager {
    private static HashMap<String, String> nameToIdMap;
    private static HashMap<String, String> idToNameMap;

    public static void init() throws IOException {
        String jsonString = Utils.getRequest("https://api.hypixel.net/resources/skyblock/items");
        
        JsonObject json = new JsonParser().parse(jsonString).getAsJsonObject();

        JsonArray itemArray = json.get("items").getAsJsonArray();
        
        nameToIdMap = new HashMap<String, String>();
        idToNameMap = new HashMap<String, String>();

        for (JsonElement item : itemArray) {
            JsonObject itemObject = item.getAsJsonObject();

            String id = itemObject.get("id").getAsString();
            String name = itemObject.get("name").getAsString();


            nameToIdMap.put(name, id);
            idToNameMap.put(id, name);
        }
    }

    public static String getId(String name) {
        return nameToIdMap.get(name);
    }

    public static String getName(String id) {
        return idToNameMap.get(id);
    }
}
