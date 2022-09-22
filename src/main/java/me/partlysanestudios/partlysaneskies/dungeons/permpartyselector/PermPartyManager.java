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
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;

import me.partlysanestudios.partlysaneskies.utils.Utils;

public class PermPartyManager {
    public static HashMap<String, PermParty> permPartyMap = new HashMap<String, PermParty>(); 
    public static PermParty favouriteParty;

    public static HashMap<String, PermParty> load() throws IOException {
        File file = new File("./config/partly-sane-skies/permPartyData.json");
        if(file.createNewFile()) {
            file.setWritable(true);
            FileWriter writer = new FileWriter(file);
            writer.write(new Gson().toJson(new ArrayList<PermParty>()));
            writer.close();
        }
        file.setWritable(true);
        Reader reader;
        reader = Files.newBufferedReader(Paths.get(file.getPath()));
        
        @SuppressWarnings("unchecked")
        
        List<LinkedTreeMap<String, String>> map = new Gson().fromJson(reader, ((List<LinkedTreeMap<String, String>>) new ArrayList<LinkedTreeMap<String, String>>()).getClass());
        HashMap<String, PermParty> finalMap = new HashMap<String, PermParty>();
        for(LinkedTreeMap<String, String> key : map) {
            PermParty instance = new Gson().fromJson(key.toString(), PermParty.class);
            finalMap.put(instance.name, instance);
        }
        permPartyMap = finalMap;
        
        return permPartyMap;
    }

    public static HashMap<String, PermParty> save() throws IOException {
        List<PermParty> map = new ArrayList<PermParty>();
        for(Entry<String,PermParty> en : permPartyMap.entrySet()){
            map.add(en.getValue());
        }
        File file = new File("./config/partly-sane-skies/permPartyData.json");
        GsonBuilder builder = new GsonBuilder(); 
        builder.setPrettyPrinting(); 
        builder.serializeSpecialFloatingPointValues();
        Gson gson = builder.create(); 
        String json = gson.toJson(map);
        FileWriter writer = new FileWriter(file);
        writer.write(json);
        writer.close();
        return permPartyMap;
    }

    public static boolean deleteParty(String name) {
        permPartyMap.remove(name);
        
        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
            Utils.sendClientMessage("Could not save Permanent Party Data.");
            return false;
        }
        return true;
    }

    public static boolean addParty(String name, List<String> partyMembers) {
        permPartyMap.put(name, new PermParty(name, partyMembers));
        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
            Utils.sendClientMessage("Could not save Permanent Party Data.");
            return false;
        }
        return true;
    }

    public static boolean favouriteParty(String name) {
        for(Entry<String, PermParty> en : permPartyMap.entrySet()) {
            PermParty party = en.getValue();
            party.isFavourite = false;
            permPartyMap.put(en.getKey(), party);
        }
        PermParty party = permPartyMap.get(name);
        party.isFavourite = true;
        permPartyMap.put(name, party);
        favouriteParty = party;

        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
            Utils.sendClientMessage("Could not save Permanent Party Data.");
            return false;
        }
        return true;
    }
}
