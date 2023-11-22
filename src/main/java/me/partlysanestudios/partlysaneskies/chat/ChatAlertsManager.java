//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.chat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.partlysanestudios.partlysaneskies.system.commands.PSSCommand;
import me.partlysanestudios.partlysaneskies.utils.ChatUtils;
import me.partlysanestudios.partlysaneskies.utils.StringUtils;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ChatAlertsManager {
    static String DATA_PATH_NAME = "./config/partly-sane-skies/chatAlertsData.json";
    static ArrayList<String> chatAlertsList = new ArrayList<>();

    // Loads all the chat alerts data
    public static void load() throws IOException {
        File file = new File(DATA_PATH_NAME);
        
        // If the file doesn't exist, fill it with an empty array list to prevent NullPointerException
        if (file.createNewFile()) {
            file.setWritable(true);
            FileWriter writer = new FileWriter(file);
            writer.write(new Gson().toJson(new ArrayList<String>()));
            writer.close();
        }

        // Make a new reader
        file.setWritable(true);
        Reader reader = Files.newBufferedReader(Paths.get(file.getPath()));

        // Reads the file and set it as the list
        @SuppressWarnings("unchecked")
        ArrayList<String> list = (ArrayList<String>) new Gson().fromJson(reader, ArrayList.class);
        chatAlertsList = list;

    }

    public static void registerCommand() {
        new PSSCommand("chatalerts")
                .addAlias("ca")
                .addAlias("chatAlert")
                .addAlias("chal")
                .setDescription("Operates the chat alerts feature: /chatalerts <add/remove/list> ")
                .setRunnable(((sender, args) -> {
                    // If the user doesn't provide any arguments whatsoever print message
                    if (args.length == 0) {
                        ChatUtils.INSTANCE.sendClientMessage("§cIncorrect usage. Correct usage: /chatalerts add/remove/list");
                        return;
                    }

                    // Looks at the first argument given
                    switch (args[0]) {
                        // If the user does /chatalerts list
                        case "list":
                            ChatAlertsManager.listAlerts();
                            break;

                        // If the user does /chatalerts add
                        case "add":
                            // Prints error message if no message alert is given
                            if (args.length == 1) {
                                ChatUtils.INSTANCE.sendClientMessage("§cIncorrect usage. Correct usage: /chatalerts add [alert]");
                                break;
                            }

                            // Adds each argument as a space
                            StringBuilder alert = new StringBuilder();
                            for (int i = 1; i < args.length; i++) {
                                alert.append(args[i]);
                                alert.append(" ");
                            }

                            // Removes any leading or trailing spaces
                            alert = new StringBuilder(StringUtils.INSTANCE.stripLeading(alert.toString()));
                            alert = new StringBuilder(StringUtils.INSTANCE.stripTrailing(alert.toString()));

                            ChatAlertsManager.addAlert(alert.toString());

                            break;

                        // If the user does /chatalerts remove
                        case "remove":
                            // If no number is given
                            if (args.length == 1) {
                                ChatUtils.INSTANCE.sendClientMessage("§cIncorrect usage. Correct usage: /chatalerts remove [number]");
                                break;
                            }

                            // Tries to parse the number given as a number
                            int id;
                            try {
                                id = Integer.parseInt(args[1]);
                            } catch (NumberFormatException e) { // If the number cannot be parsed, prints an error message
                                ChatUtils.INSTANCE.sendClientMessage("§c\"" + args[1] + "\" could not be read as a number. Correct Usage: /chatalerts remove [number]");
                                break;
                            }

                            // Removes the chat alert
                            ChatAlertsManager.removeAlert(id);
                            break;

                        // If none of the above are given
                        default:
                            ChatUtils.INSTANCE.sendClientMessage("§cIncorrect usage. Correct usage: /chatalerts add/remove/list");
                            break;
                    }
                }))
                .register();
    }

    // Saves all the chat alerts data
    public static void save() throws IOException {
        // Creates a new file and Gson instance
        File file = new File(DATA_PATH_NAME);
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        builder.serializeSpecialFloatingPointValues();
        Gson gson = builder.create();

        // Converts list to JSON string
        String json = gson.toJson(chatAlertsList);

        // Writes string to file
        FileWriter writer = new FileWriter(file);
        writer.write(json);
        writer.close();

    }

    // Adds the given alert to the list
    public static void addAlert(String alert) {
        // Adds the alert
        chatAlertsList.add(alert);
        
        // Tries to save
        try {
            save();
        } catch (IOException e) { // If unable to save, warn user
            ChatUtils.INSTANCE.sendClientMessage("§cChat Alerts was unable to save. Please try again.");
            e.printStackTrace();
        }

        // Print success message
        ChatUtils.INSTANCE.sendClientMessage("§b\"§d" + alert + "§b\" was successfully added as alert number §d" + chatAlertsList.size() + "§b.");
    }

    public static int getChatAlertCount() {
        return chatAlertsList.size();
    }

    // Lists all the alerts to the chat
    public static void listAlerts() {
        // Creates header message
        StringBuilder message = new StringBuilder("§d§m-----------------------------------------------------\n§bChat Alerts:" +
                "\n§d§m-----------------------------------------------------\n");

        // Creates the index number on the left of the message
        int i = 1;
        
        // For each alert, format it so its ##. [alert] 
        for (String alert : chatAlertsList) {
            message.append(StringUtils.INSTANCE.formatNumber(i)).append(": ").append(alert).append("\n");
            i++;
        }

        // Sends a message to the client
        ChatUtils.INSTANCE.sendClientMessage(message.toString());
    }

    // Removes an alert given a number
    public static void removeAlert(int id) {
        // Checks if the number is in the list
        if (id > chatAlertsList.size() || chatAlertsList.size() < 0) {
            ChatUtils.INSTANCE.sendClientMessage("§cChat alert number " + id + " was not found. Please enter a valid number.");
            return;
        }

        // Gets the chat alert that was removed
        String message = chatAlertsList.get(id - 1);

        // Removes the given alert
        chatAlertsList.remove(id - 1);

        // Tries to save
        try {
            save();
        } catch (IOException e) { // Warns user if unable to save
            ChatUtils.INSTANCE.sendClientMessage("§cChat Alerts was unable to save. Please try again.");
            e.printStackTrace();
        }
        // Prints a success message
        ChatUtils.INSTANCE.sendClientMessage("§bChat Alert number §d" + id + " §b(\"§d" + message + "§b\") was successfully removed.");
    }

    // All the different message prefixes
    static String[] MESSAGE_PREFIXES = new String[] {"§r§7: ", "§r§f: ", "§f: "};

    // Runs when a chat message is received
    public static IChatComponent checkChatAlert(IChatComponent message) {
        String formattedMessage = message.getFormattedText();

        // Finds the location after the username starts
        // Ex: "[MVP+] Su386: "
        int beginMessageIndex = -1;
        // Checks all the different message prefixes so that it works with nons
        for (String messagePrefix : MESSAGE_PREFIXES) {
            beginMessageIndex = formattedMessage.indexOf(messagePrefix);
            
            // If it finds it, stops checking the rest
            if (beginMessageIndex != -1) {
                break;
            }
        }

        // If the message does not have ":" at the beginning
        if (beginMessageIndex == -1) {
            return message;
        }

        String unformattedMessage = StringUtils.INSTANCE.removeColorCodes(formattedMessage);
        String rawMessage = formattedMessage.substring(beginMessageIndex);

        // Removes all formatting and extra spaces from the message
        rawMessage = StringUtils.INSTANCE.removeColorCodes(rawMessage);
        rawMessage = rawMessage.replaceFirst(": ", "");
        rawMessage = StringUtils.INSTANCE.stripLeading(rawMessage);
        rawMessage = StringUtils.INSTANCE.stripTrailing(rawMessage);

        String lowerCaseMessage = rawMessage.toLowerCase();


        // Checks each alert, seeing if the message contains it
        for (String alert : chatAlertsList) {
            // If the message doesn't contain the alert, continue
            if (!lowerCaseMessage.contains(alert.toLowerCase())) {
                continue;
            }

            // Creates a new message that will be shown to the user
            StringBuilder messageBuilder = new StringBuilder(formattedMessage);

            // Gets index of alert in unformatted text
            int alertIndexUnformatted = unformattedMessage.toLowerCase().indexOf(alert.toLowerCase(), unformattedMessage.indexOf(rawMessage));

            // Number of color codes in the whole string
            int numOfColorCodeTotal = numOfColorCodes(formattedMessage);
            // Number of color codes not including the last §r 
            int numOfColorCodeBefore = numOfColorCodeTotal - 1; 
            // Index of the alert in formatted string
            int alertIndexFormatted = numOfColorCodeBefore * 2 + alertIndexUnformatted; 

            // Inserts the previous color code right after the alert
            char[] charsToAdd = getLastColorCode(formattedMessage.substring(0, alertIndexFormatted + 1)).toCharArray();
            messageBuilder.insert(alertIndexFormatted + alert.length(), charsToAdd, 0, charsToAdd.length);
            
            // Inserts a purple and bold color code to highlight the message right before the alert
            charsToAdd = ("§d§l").toCharArray();
            messageBuilder.insert(alertIndexFormatted, charsToAdd, 0, charsToAdd.length);



            // Shows a message to user
            return new ChatComponentText(messageBuilder.toString());
        }
        return message;
    }

    // Returns the number of color codes in a String
    private static int numOfColorCodes(String str) {
        int i = 0;

        // Counts each instance of §
        StringBuilder textBuilder = new StringBuilder(str);
        while (textBuilder.indexOf("§") != -1) {
            textBuilder.deleteCharAt(textBuilder.indexOf("§") + 1);
            textBuilder.deleteCharAt(textBuilder.indexOf("§"));
            i++;
        }

        return i;
    }

    /// All the none color format codes
    static String[] NON_COLOR_CODES = new String[] {"§r", "§o", "§n", "§m", "§l", "§k"};
    // Returns the last color code in a string
    private static String getLastColorCode(String str) {
        String currentCode = "";
        StringBuilder textBuilder = new StringBuilder(str);
        while (textBuilder.indexOf("§") != -1) {
            boolean shouldContinue = false;
            // Checks if the color code is a non-color code
            for (String code : NON_COLOR_CODES) {
                if (textBuilder.indexOf("§") == -1 || !textBuilder.substring(textBuilder.indexOf("§"), textBuilder.indexOf("§") + 2).equalsIgnoreCase(code)) {
                    continue;
                }
                textBuilder.deleteCharAt(textBuilder.indexOf("§") + 1);
                textBuilder.deleteCharAt(textBuilder.indexOf("§"));
                shouldContinue = true;
                break;
            }


            if (shouldContinue || textBuilder.indexOf("§") == -1) {
                continue;
            }

            // Sets current code to the current format code
            currentCode = textBuilder.substring(textBuilder.indexOf("§"), textBuilder.indexOf("§") + 2);
            textBuilder.deleteCharAt(textBuilder.indexOf("§") + 1);
            textBuilder.deleteCharAt(textBuilder.indexOf("§"));
        }
        return currentCode;
    }
}
