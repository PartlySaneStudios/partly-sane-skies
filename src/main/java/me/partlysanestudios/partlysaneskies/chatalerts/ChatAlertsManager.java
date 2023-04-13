//
// Written by Su386.
// See LICENSE for copright and license notices.
//

package me.partlysanestudios.partlysaneskies.chatalerts;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import me.partlysanestudios.partlysaneskies.utils.StringUtils;
import me.partlysanestudios.partlysaneskies.utils.Utils;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChatAlertsManager {
    static String DATA_PATH_NAME = "./config/partly-sane-skies/chatAlertsData.json";
    static ArrayList<String> chatAlertsList = new ArrayList<String>();

    // Loads all of the chat alerts data
    public static ArrayList<String> load() throws IOException {
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

        // Reads the file, and set it as the list
        @SuppressWarnings("unchecked")
        ArrayList<String> list = (ArrayList<String>) new Gson().fromJson(reader, new ArrayList<String>().getClass());
        chatAlertsList = list;

        return chatAlertsList;
    }

    // Saves all of the chat alerts data
    public static List<String> save() throws IOException {
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
        
        return chatAlertsList;
    }

    // Adds the given alert to the list
    public static void addAlert(String alert) {
        // Adds the alert
        chatAlertsList.add(alert);
        
        // Tries to save
        try {
            save();
        } catch (IOException e) { // If unable to save, warn user
            Utils.sendClientMessage("&cChat Alerts was unable to save. Please try again.");
            e.printStackTrace();
        }

        // Print sucess message
        Utils.sendClientMessage("&b\"&d" + alert + "&b\" was successfully added as alert number &d" + chatAlertsList.size() + "&b.");
    }

    // Lists all of the alerts to the chat
    public static void listAlerts() {
        // Creates header message
        String message = "&d-----------------------------------------------------\n&bChat Alerts:" +
                "\n&d-----------------------------------------------------\n";

        // Creates the index number on the left of the message
        int i = 1;
        
        // For each alert, format it so its ##. [alert] 
        for (String alert : chatAlertsList) {
            message += StringUtils.formatNumber(i) + ": " + alert + "\n";
            i++;
        }

        // Sends message to the client
        Utils.sendClientMessage(message);
    }

    // Removes an alert given a number
    public static void removeAlert(int id) {
        // Checks if the number is in the list
        if (id > chatAlertsList.size() || chatAlertsList.size() < 0) {
            Utils.sendClientMessage("&cChat alert number " + id + " was not found. Please enter a valid number.");
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
            Utils.sendClientMessage("&cChat Alerts was unable to save. Please try again.");
            e.printStackTrace();
        }
        // Prints success message
        Utils.sendClientMessage("&bChat Alert number &d" + id + " &b(\"&d" + message + "&b\") was successsfully removed.");
    }

    // All of the different message prefixes
    static String[] MESSAGE_PREFIXES = new String[] {"§r§7: ", "§r§f: ", "§f: "};

    // Runs when a chat message is recieved
    @SubscribeEvent
    public void checkChatAlert(ClientChatReceivedEvent e) {
        String formattedMessage = e.message.getFormattedText();

        // Finds the location after the username starts
        // Ex: "[MVP+] Su386: "
        int beginMessageIndex = -1;
        // Checks all of the different message prefixes so that it works with nons
        for (String messagePrefix : MESSAGE_PREFIXES) {
            beginMessageIndex = formattedMessage.indexOf(messagePrefix);
            
            // If it finds it, stops checking the rest
            if (beginMessageIndex != -1) {
                break;
            }
        }

        // If the message does not have ": " at the beginning
        if (beginMessageIndex == -1) {
            return;
        }

        String unformattedMessage = StringUtils.removeColorCodes(formattedMessage);
        String rawMessage = formattedMessage.substring(beginMessageIndex);

        // Removes all formating and extra spaces from the message
        rawMessage = StringUtils.removeColorCodes(rawMessage);
        rawMessage = rawMessage.replaceFirst(": ", "");
        rawMessage = StringUtils.stripLeading(rawMessage);
        rawMessage = StringUtils.stripTrailing(rawMessage);

        String lowerCaseMessage = rawMessage.toLowerCase();


        // Checks each alert, seeing if the message contains it
        for (String alert : chatAlertsList) {
            // If the message doesn't contain the alert, continue
            if (!lowerCaseMessage.contains(alert.toLowerCase())) {
                continue;
            }

            // Prohibits origional messsage from being sent to the player
            e.setCanceled(true);

            // Creates a new message that will be shown to the user
            StringBuilder messageBuilder = new StringBuilder(formattedMessage);

            // Gets index of alert in unformatted text
            int alertIndexUnformatted = unformattedMessage.toLowerCase().indexOf(alert.toLowerCase(), unformattedMessage.indexOf(rawMessage));

            // Number of color codes in the whole string
            int numOfColorCodeTotal = numOfColorCodes(formattedMessage);
            // Number of color codes not including the last &r 
            int numOfColorCodeBefore = numOfColorCodeTotal - 1; 
            // Index of the alert in formatted string
            int alertIndexFormatted = numOfColorCodeBefore * 2 + alertIndexUnformatted; 

            // Inserts the previous color code right after the alert
            char[] charsToAdd = getLastColorCode(formattedMessage.substring(0, alertIndexFormatted + 1)).toCharArray();
            messageBuilder.insert(alertIndexFormatted + alert.length(), charsToAdd, 0, charsToAdd.length);
            
            // Inserts a purple and bold color code to highlight the message right before the alert
            charsToAdd = StringUtils.colorCodes("&d&l").toCharArray();
            messageBuilder.insert(alertIndexFormatted, charsToAdd, 0, charsToAdd.length);

            // Plays a flute sound 
            PartlySaneSkies.minecraft
                    .getSoundHandler()
                    .playSound(PositionedSoundRecord.create(new ResourceLocation("partlysaneskies", "flute_scale")));

            // Shows message to user
            Utils.sendClientMessage(messageBuilder.toString(), true);

            // Exists loop
            break;
        }
        
    }

    // Retuns the amount of colour codes in a String
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

    /// All of the none color format codes
    static String[] NON_COLOR_CODES = new String[] {"§r", "§o", "§n", "§m", "§l", "§k"};
    // Returns the last color code in a string
    private static String getLastColorCode(String str) {
        String currentCode = "";
        StringBuilder textBuilder = new StringBuilder(str);
        while (textBuilder.indexOf("§") != -1) {
            boolean shouldContinue = false;
            // Checks if the color code is a non color code
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
