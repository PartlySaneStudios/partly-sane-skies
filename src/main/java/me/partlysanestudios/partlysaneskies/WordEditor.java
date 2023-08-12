//
// Written by J10a1n15, with a lot of help from Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies;

import me.partlysanestudios.partlysaneskies.garden.endoffarmnotifer.EndOfFarmNotifier;
import me.partlysanestudios.partlysaneskies.system.commands.PSSCommand;
import me.partlysanestudios.partlysaneskies.utils.StringUtils;
import me.partlysanestudios.partlysaneskies.utils.Utils;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.ClickEvent.Action;
import org.apache.commons.lang3.ArrayUtils;
import scala.swing.ComboBox;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * TODO LIST:
 * - Add a file to save the words to replace
 * - Add a UI to add/remove/edit words to replace
 * - In the main config, add a button to open the UI
 * - Add a command to open the UI
 * - Add a main toggle in the main config
 * - Maybe in a new page, like "Chat" or something, where the chat colors, as well as the owo (todo) language can be toggeled
 *
 */


public class WordEditor {

    static String[][] testArr = {
            {"test", "teeeest"},
            {"pommes", "pommmmmes"},
            {"ei", "eiei"}
    };


    @SubscribeEvent
    public void onChat(final ClientChatReceivedEvent event) {
        String formattedMessage = event.message.getFormattedText();
        boolean wordFound = false; // If the string contains a word to replace

        /*
         * 0: standard chat message
         * 1: system message
         * 2: action bar message
         */
        if (event.type != 0) {
            return;
        }

        for (final String[] word : testArr) {

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

        // Only run when the word has been found to prevent unnecessary editing of the message (because we always break things)
        if (wordFound) {
            event.setCanceled(true);

            ChatComponentText message = new ChatComponentText(formattedMessage); // Creates a new message
            message.setChatStyle(event.message.getChatStyle());                                         //Attempts to copy old chat layout
            message.getChatStyle().setChatHoverEvent(event.message.getChatStyle().getChatHoverEvent()); //Sets the old hover event
            message.getChatStyle().setChatClickEvent(event.message.getChatStyle().getChatClickEvent()); //Sets the old click event

            List<String> urls = extractUrls(formattedMessage); // Gets the urls
            if ((event.message.getChatStyle().getChatClickEvent() == null ||
                    event.message.getChatStyle().getChatClickEvent().getValue() == null ||
                    event.message.getChatStyle().getChatClickEvent().getValue().isEmpty()
            ) // Checks if the old message has a value (by checking null and if its empty)
                    && !urls.isEmpty()) { // If the old message click event has no value and if the url list is not empty

                message.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, urls.get(0))); // Makes the click event open the url
            }

            PartlySaneSkies.minecraft.ingameGUI
                    .getChatGUI()
                    .printChatMessage(message);
        }


    }

    public static void listWords() {
        if (WordEditor.testArr.length == 0) {
            Utils.sendClientMessage("&7There are no words to replace.");
            return;
        }

        Utils.sendClientMessage("&7Words to replace:");
        for (int i = 0; i < WordEditor.testArr.length; i++) {
            Utils.sendClientMessage("&b" + (i + 1) + ". &7" + testArr[i][0] + " &8-> &7" + testArr[i][1]);
        }
    }


    public static void registerWordEditorCommand() {
        new PSSCommand("wordeditor")
                .addAlias("wordedit")
                .addAlias("we")
                .addAlias("wordreplace")
                .addAlias("wr")
                .setDescription("Operates the word editor: /wordeditor <add> <word> <replacement>, /wordeditor <list> or /wordeditor <remove> <index>")
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

                        if (i > WordEditor.testArr.length || i < 1) {
                            Utils.sendClientMessage("&cPlease select a valid index and try again.");
                            return;
                        }
                        Utils.sendClientMessage("&aRemoving: &b" + WordEditor.testArr[i - 1][0] + " &8-> &b" + WordEditor.testArr[i - 1][1]);
                        ArrayUtils.removeElement(WordEditor.testArr, WordEditor.testArr[i - 1]);    // Removes the element from the array
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
                        WordEditor.testArr = ArrayUtils.add(WordEditor.testArr, new String[]{word, replacement});
                        try {
                            WordEditor.save();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }))
                .register();
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