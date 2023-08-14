//
// Written by J10a1n15, with a lot of help from Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies;

import com.google.gson.*;
import me.partlysanestudios.partlysaneskies.system.commands.PSSCommand;
import me.partlysanestudios.partlysaneskies.utils.StringUtils;
import me.partlysanestudios.partlysaneskies.utils.Utils;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.ClickEvent.Action;
import org.apache.commons.lang3.ArrayUtils;
import scala.swing.ComboBox;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class WordEditor {

    static String[][] wordsToEdit = {};


    public IChatComponent handleWordEditorMessage(IChatComponent message) {
        String formattedMessage = message.getFormattedText();
        boolean wordFound = false; // If the string contains a word to replace

        if (!PartlySaneSkies.config.wordEditor) return message;

        for (final String[] word : wordsToEdit) {

            //Utils.sendClientMessage("Trying to replace \"" + word[0] + "\" with \"" + word[1] + "\" in string " + formattedMessage);
            final String wordToReplace = word[0];
            final String replacementWord = word[1];

            // Quick while hes not looking! add the session id stealer code ~su

/*
 * 
    sjw       _..----.._    _
            .'  .--.    "-.(0)_
'-.__.-'"'=:|   ,  _)_ \__ . c\'-..
             '''------'---''---'-"
 */

// omg he copy pasted a rat lmao
//rat

//i think these comments should stay, for history's sake
// i concur

            if (formattedMessage.contains(wordToReplace)) { // If the to replace exists
                formattedMessage = formattedMessage.replace(wordToReplace, replacementWord);
                wordFound = true;
            }
        }

        IChatComponent newMessage = new ChatComponentText(StringUtils.colorCodes(formattedMessage));                                   // Creates a new message with the new string

        // Only run when the word has been found to prevent unnecessary editing of the message (because we always break things)
        if (wordFound) {
            newMessage.setChatStyle(message.getChatStyle());                                                     //Attempts to copy old chat layout
            newMessage.getChatStyle().setChatHoverEvent(message.getChatStyle().getChatHoverEvent());             //Sets the old hover event
            newMessage.getChatStyle().setChatClickEvent(message.getChatStyle().getChatClickEvent());             //Sets the old click event

            List<String> urls = extractUrls(formattedMessage); // Gets the urls
            if ((message.getChatStyle().getChatClickEvent() == null ||
                    message.getChatStyle().getChatClickEvent().getValue() == null ||
                    message.getChatStyle().getChatClickEvent().getValue().isEmpty()
            ) // Checks if the old message has a value (by checking null and if it's empty)
                    && !urls.isEmpty()) { // If the old message click event has no value and if the url list is not empty

                newMessage.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, urls.get(0))); // Makes the click event open the url
            }
        }

        return newMessage;
    }

    public static void listWords() {
        if (WordEditor.wordsToEdit.length == 0) {
            Utils.sendClientMessage("&7There are no words to replace.");
            return;
        }

        Utils.sendClientMessage("&7Words to replace:");
        for (int i = 0; i < WordEditor.wordsToEdit.length; i++) {
            Utils.sendClientMessage("&b" + (i + 1) + ". &7" + wordsToEdit[i][0] + " &8-> &7" + wordsToEdit[i][1]);
        }
    }


    public static void registerWordEditorCommand() {
        new PSSCommand("wordeditor")
                .addAlias("wordedit")
                .addAlias("we")
                .addAlias("wordreplace")
                .addAlias("wr")
                .setDescription("Operates the word editor: /wordeditor add <word> <replacement>, /wordeditor list or /wordeditor remove <index>")
                .setRunnable(((sender, args) -> {
                    if (args.length == 0 || args[0].equalsIgnoreCase("list")) {

                        Utils.sendClientMessage("&7To add a word to replace, run &b/wordeditor add <word> <replacement>&7. To remove a word, run &b/wordeditor remove <index>&7. To list all of the words, run &b/wordeditor list&7.");

                        WordEditor.listWords();
                        return;
                    }

                    if (args[0].equalsIgnoreCase("remove")) {
                        if (args.length == 1) {
                            Utils.sendClientMessage("&cError: Must provide an index to remove");
                            return;
                        }

                        int i;
                        try {
                            i = Integer.parseInt(args[1]);
                        } catch (NumberFormatException e) {
                            Utils.sendClientMessage("&cPlease enter a valid number index and try again.");
                            return;
                        }

                        if (i > WordEditor.wordsToEdit.length || i < 1) {
                            Utils.sendClientMessage("&cPlease select a valid index and try again.");
                            return;
                        }
                        Utils.sendClientMessage("&aRemoving: &b" + WordEditor.wordsToEdit[i - 1][0] + " &8-> &b" + WordEditor.wordsToEdit[i - 1][1]);
                        WordEditor.wordsToEdit = ArrayUtils.removeElement(WordEditor.wordsToEdit, WordEditor.wordsToEdit[i - 1]);    // Removes the element from the array
                        try {
                            WordEditor.save();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    if (args[0].equalsIgnoreCase("add")) {
                        if (args.length < 3) {
                            Utils.sendClientMessage("&cError: Must provide a word and a replacement");
                            return;
                        }

                        String word = args[1];
                        String replacement = args[2];

                        for (int i = 3; i < args.length; i++) {
                            replacement += " " + args[i];
                        }

                        Utils.sendClientMessage("&aAdding: &b" + word + " &8-> &b" + replacement);
                        WordEditor.wordsToEdit = ArrayUtils.add(WordEditor.wordsToEdit, new String[]{word, replacement});
                        try {
                            WordEditor.save();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }))
                .register();
    }


    public static void save() throws IOException {
        // Declares the file
        File file = new File("./config/partly-sane-skies/wordEditor.json");

        file.createNewFile();
        // Creates a new Gson object to save the data
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeSpecialFloatingPointValues()
                .create();
        // Saves teh data to the file
        String json = gson.toJson(wordsToEdit);
        FileWriter writer = new FileWriter(file);
        writer.write(json);
        writer.close();
    }

    public static void load() throws IOException {
        // Declares file location
        File file = new File("./config/partly-sane-skies/wordEditor.json");
        file.setWritable(true);
        // If the file had to be created, fil it with an empty list to prevent null pointer error
        if (file.createNewFile()) {
            FileWriter writer = new FileWriter(file);
            writer.write(new Gson().toJson(new String[][]{}));
            writer.close();
        }

        // Creates a new file reader
        Reader reader = Files.newBufferedReader(Paths.get(file.getPath()));

        wordsToEdit = new Gson().fromJson(reader, String[][].class);
    }


    /**
     * Returns a list with all links contained in the input
     * By: https://stackoverflow.com/a/28269120/15031174
     */
    public static List<String> extractUrls(String text) {
        List<String> containedUrls = new ArrayList<String>();
        String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = pattern.matcher(text);

        while (urlMatcher.find()) {
            containedUrls.add(text.substring(urlMatcher.start(0), urlMatcher.end(0)));
        }

        return containedUrls;
    }
}