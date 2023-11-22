//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.dungeons.permpartyselector;

import com.google.gson.*;
import me.partlysanestudios.partlysaneskies.system.commands.PSSCommand;
import me.partlysanestudios.partlysaneskies.utils.ChatUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.Map.Entry;

public class PermPartyManager {
    public static HashMap<String, PermParty> permPartyMap = new HashMap<>();
    public static PermParty favoriteParty;

    // Loads all the data from the perm Party
    public static void load() throws IOException {
        // Declares file location
        File file = new File("./config/partly-sane-skies/permPartyData.json");
        file.setWritable(true);
        // If the file had to be created, fil it with an empty list to prevent null pointer error
        if (file.createNewFile()) {
            FileWriter writer = new FileWriter(file);
            writer.write(new Gson().toJson(new HashMap<String, PermParty>()));
            writer.close();
        }

        

        // Creates a new file reader
        Reader reader = Files.newBufferedReader(Paths.get(file.getPath()));

        JsonObject object = new JsonParser().parse(reader).getAsJsonObject();

        HashMap<String, PermParty> map = new HashMap<>();
        
        for (Map.Entry<String, JsonElement> en : object.entrySet()) {
            JsonObject partyJson = en.getValue().getAsJsonObject();
            JsonArray partyMembersJson = partyJson.get("partyMembers").getAsJsonArray();
            
            ArrayList<String> partyMembersList = new ArrayList<>();
            for(JsonElement member : partyMembersJson) {
                partyMembersList.add(member.getAsString());
            }

            PermParty permParty = new PermParty(partyJson.get("name").getAsString(), partyMembersList);

            if (partyJson.has("isFavourite")) {
                permParty.isFavorite = partyJson.get("isFavourite").getAsBoolean();
            }
            else if (partyJson.has("isFavorite")) {
                permParty.isFavorite = partyJson.get("isFavorite").getAsBoolean();
            }

            map.put(en.getKey(), permParty);
        }

        permPartyMap = map;
    }

    public static void registerCommand() {
        new PSSCommand("permparty")
                .addAlias("pp")
                .addAlias("permp")
                .setDescription("Operates the perm party manager: /permparty [<partyid>/add/remove/list/delete/new/fav]")
                .setRunnable((sender ,args) -> {
                    if (args.length == 0) {
                        ChatUtils.INSTANCE.sendClientMessage(
                                (
                                        "§3/pp <partyid>\n§7Parties everyone in the perm party." +
                                                "\n§3/pp add <partyid> <playerusername>\n§7Adds a player to the perm party." +
                                                "\n§3/pp list {partyid}\n§7Lists all of the members in a given party. If no party is specified, lists all perm parties."
                                                +
                                                "\n§3/pp delete <partyid>\n§7Deletes a perm party. (Note: There is no way to undo this action)."
                                                +
                                                "\n§3/pp new <partyid> {partymembers}\n§7Creates a new perm party." +
                                                "\n§3/pp fav {partyid}\n§7Sets party as favorite. If no party is specified, parties everyone in the favorite perm party."));
                    } else if (args[0].equalsIgnoreCase("add")) {
                        if (args.length == 3) {
                            if (PermPartyManager.permPartyMap.containsKey(args[1])) {
                                PermParty party = PermPartyManager.permPartyMap.get(args[1]);
                                party.addMember(args[2]);
                                PermPartyManager.permPartyMap.put(party.name, party);
                                ChatUtils.INSTANCE.sendClientMessage(("Added player " + args[2] + " to party " + args[1] + "."));
                            } else {
                                ChatUtils.INSTANCE.sendClientMessage(("§cNo party was found with the ID " + args[1]
                                        + ".\n§cCorrect usage: /pp add <partyid> <playerusername>\n§7Adds a player to the perm party."));
                            }
                        } else {
                            ChatUtils.INSTANCE.sendClientMessage((
                                    "§cCorrect usage: /pp add <partyid> <playerusername>\n§7Adds a player to the perm party."));
                        }
                    }

                    // Remove arg
                    else if (args[0].equalsIgnoreCase("remove")) {
                        if (args.length == 3) {
                            if (PermPartyManager.permPartyMap.containsKey(args[1])) {

                                PermParty party = PermPartyManager.permPartyMap.get(args[1]);
                                if (party.partyMembers.contains(args[2])) {
                                    party.removeMember(args[2]);
                                    PermPartyManager.permPartyMap.put(party.name, party);
                                    ChatUtils.INSTANCE.sendClientMessage("Removed member " + args[2] + " from party " + args[1] + ".");
                                } else {
                                    ChatUtils.INSTANCE.sendClientMessage(("§cNo player was found with the name " + args[2]
                                            + ".\n§cCorrect usage: /pp remove <partyid> <playerusername>\n§7Removes a player from the perm party."));
                                }

                            } else {
                                ChatUtils.INSTANCE.sendClientMessage(("§cNo party was found with the ID " + args[1]
                                        + ".\n§cCorrect usage: /pp remove <partyid> <playerusername>\n§7Removes a player from the perm party."));
                            }
                        } else {
                            ChatUtils.INSTANCE.sendClientMessage((
                                    "§cCorrect usage: /pp remove <partyid> <playerusername>\n§7Removes a player from the perm party."));
                        }
                    }

                    // List arg
                    else if (args[0].equalsIgnoreCase("list")) {
                        if (args.length == 1) {
                            for (PermParty party : PermPartyManager.permPartyMap.values()) {
                                ChatUtils.INSTANCE.sendClientMessage(party.name + " | Members: " + party.getMemberString());
                            }
                        } else {
                            if (PermPartyManager.permPartyMap.containsKey(args[1])) {
                                PermParty party = PermPartyManager.permPartyMap.get(args[0]);
                                ChatUtils.INSTANCE.sendClientMessage(party.name + " | Members: " + party.getMemberString());
                            } else {
                                ChatUtils.INSTANCE.sendClientMessage(("§cNo party was found with the ID " + args[1]
                                        + ".\n§cCorrect usage: /pp list {partyid}\n§7Lists all of the members in a given party. If no party is specified, lists all parties."));
                            }
                        }
                    } else if (args[0].equalsIgnoreCase("delete")) {
                        if (args.length == 2) {
                            if (PermPartyManager.permPartyMap.containsKey(args[1])) {
                                PermPartyManager.deleteParty(args[1]);
                                ChatUtils.INSTANCE.sendClientMessage("Deleted party " + args[1] + ".");
                            } else {
                                ChatUtils.INSTANCE.sendClientMessage(("§cNo party was found with the ID " + args[1]
                                        + ".\n§cCorrect usage: /pp delete <partyid>\n§7Deletes a perm party. (Note: There is no way to undo this action)."));
                            }
                        } else {
                            ChatUtils.INSTANCE.sendClientMessage((
                                    "§cCorrect usage: /pp delete <partyid>\n§7Deletes a perm party. (Note: There is no way to undo this action)."));
                        }
                    } else if (args[0].equalsIgnoreCase("new")) {
                        if (args.length >= 2) {
                            List<String> partyMembers = new ArrayList<>();
                            partyMembers.addAll(Arrays.asList(args).subList(2, args.length));
                            PermPartyManager.addParty(args[1], partyMembers);

                            ChatUtils.INSTANCE.sendClientMessage("Created party " + args[1] + ".");
                        } else {
                            ChatUtils.INSTANCE.sendClientMessage(("§cCorrect usage: /pp new <partyid> {partymembers}\n§7Creates a new perm party."));
                        }
                    } else if (args[0].equalsIgnoreCase("fav")) {
                        if (args.length == 2) {
                            if (PermPartyManager.permPartyMap.containsKey(args[1])) {
                                PermPartyManager.favoriteParty(args[1]);
                                ChatUtils.INSTANCE.sendClientMessage("Set " + args[1] + " to your favorite.");
                            } else {
                                ChatUtils.INSTANCE.sendClientMessage(("§cNo party was found with the ID " + args[1]
                                        + ".\n§cCorrect usage: /pp fav {partyid}\n§7Sets party as favorite. If no party is specified, parties everyone in the favorite perm party."));
                            }
                        } else {
                            if (PermPartyManager.favoriteParty != null) {
                                PermParty party = PermPartyManager.favoriteParty;
                                party.partyAll();
                            } else {
                                ChatUtils.INSTANCE.sendClientMessage((
                                        "§cCorrect usage: /pp fav {partyid}\n§7Sets party as favorite. If no party is specified, parties everyone in the favorite perm party."));
                            }

                        }
                    } else {
                        if (PermPartyManager.permPartyMap.containsKey(args[0])) {
                            PermParty party = PermPartyManager.permPartyMap.get(args[0]);
                            party.partyAll();
                        } else {
                            ChatUtils.INSTANCE.sendClientMessage(
                                    ("§cCorrect usage: /pp <partyid>\n§7Parties everyone in a party."));
                        }
                    }
                })
                .register();
    }

    // Saves all the party member data to a json file
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
            ChatUtils.INSTANCE.sendClientMessage("Could not save Permanent Party Data.");
        }
    }

    // Adds the party with the specified list of members
    public static boolean addParty(String name, List<String> partyMembers) {
        // Creates an adds a new PermParty
        permPartyMap.put(name, new PermParty(name, partyMembers));
        // Saves the party map
        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
            ChatUtils.INSTANCE.sendClientMessage("Could not save Permanent Party Data.");
            return false;
        }
        return true;
    }

    // Sets the party as the favorite party 
    public static boolean favoriteParty(String name) {
        // Removes all favorites from other parties
        for (Entry<String, PermParty> en : permPartyMap.entrySet()) {
            PermParty party = en.getValue();
            party.isFavorite = false;
            permPartyMap.put(en.getKey(), party);
        }
        // Sets the current party as the favorite
        PermParty party = permPartyMap.get(name);
        party.isFavorite = true;
        permPartyMap.put(name, party);
        favoriteParty = party;

        // Saves all the data
        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
            ChatUtils.INSTANCE.sendClientMessage("Could not save Permanent Party Data.");
            return false;
        }
        return true;
    }

    // Loads the favorite party into the variable
    public static void loadFavoriteParty() {
        // Goes through each party to find the favorite
        for (Entry<String, PermParty> en : permPartyMap.entrySet()) {
            PermParty party = en.getValue();
            if (party.isFavorite) {
                favoriteParty = party;
            }
        }
    }
}
