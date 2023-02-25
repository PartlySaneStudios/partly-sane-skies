package me.partlysanestudios.partlysaneskies.dungeons.permpartyselector;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import me.partlysanestudios.partlysaneskies.utils.Utils;

public class PermPartyManager {
    public static HashMap<String, PermParty> permPartyMap = new HashMap<String, PermParty>();
    public static PermParty favouriteParty;

    // Loads all of the data from the perm Party
    public static void load() throws IOException {
        // Declares file location
        File file = new File("./config/partly-sane-skies/permPartyData.json");
        file.setWritable(true);
        // If the file had to be created, fil it with an empty list to prevent null pointer error
        if (file.createNewFile()) {
            FileWriter writer = new FileWriter(file);
            writer.write(new Gson().toJson(new ArrayList<PermParty>()));
            writer.close();
        }

        

        // Creates a new file reader
        Reader reader = Files.newBufferedReader(Paths.get(file.getPath()));

        JsonObject object = new JsonParser().parse(reader).getAsJsonObject();

        HashMap<String, PermParty> map = new HashMap<String, PermParty>();
        
        for (Map.Entry<String, JsonElement> en : object.entrySet()) {
            JsonObject partyJson = en.getValue().getAsJsonObject();
            JsonArray partyMembersJson = partyJson.get("partyMembers").getAsJsonArray();
            
            ArrayList<String> partyMembersList = new ArrayList<String>();
            for(JsonElement member : partyMembersJson) {
                partyMembersList.add(member.getAsString());
            }

            PermParty permParty = new PermParty(partyJson.get("name").getAsString(), partyMembersList);
            permParty.isFavourite = partyJson.get("isFavourite").getAsBoolean();
            map.put(en.getKey(), permParty);
        }

        permPartyMap = map;
    }

    // Saves all of the party member data to a json file
    public static void save() throws IOException {
        // Declares the file
        File file = new File("./config/partly-sane-skies/permPartyData.json");
        // Creates a new Gson object to save the data
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeSpecialFloatingPointValues()
                .create();
        // Saves teh data to the file
        String json = gson.toJson(permPartyMap);
        FileWriter writer = new FileWriter(file);
        writer.write(json);
        writer.close();
    }

    // Removes the specified perm party from the map
    public static void deleteParty(String name) {
        // Removes the party
        permPartyMap.remove(name);

        // Saves the party map
        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
            Utils.sendClientMessage("Could not save Permanent Party Data.");
        }
    }

    // Addes the party with the specified list of members
    public static boolean addParty(String name, List<String> partyMembers) {
        // Creates an adds a new PermParty
        permPartyMap.put(name, new PermParty(name, partyMembers));
        // Saves the party map
        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
            Utils.sendClientMessage("Could not save Permanent Party Data.");
            return false;
        }
        return true;
    }

    // Sets the party as the favourite party 
    public static boolean favouriteParty(String name) {
        // Removes all favourites from other parties
        for (Entry<String, PermParty> en : permPartyMap.entrySet()) {
            PermParty party = en.getValue();
            party.isFavourite = false;
            permPartyMap.put(en.getKey(), party);
        }
        // Sets the current party as the favourite
        PermParty party = permPartyMap.get(name);
        party.isFavourite = true;
        permPartyMap.put(name, party);
        favouriteParty = party;

        // Saves all of the data
        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
            Utils.sendClientMessage("Could not save Permanent Party Data.");
            return false;
        }
        return true;
    }

    // Loads the favourite party into the variable
    public static void loadFavouriteParty() {
        // Goes through each party to find the favourite
        for (Entry<String, PermParty> en : permPartyMap.entrySet()) {
            PermParty party = en.getValue();
            if (party.isFavourite) {
                favouriteParty = party;
            }
        }
    }
}
