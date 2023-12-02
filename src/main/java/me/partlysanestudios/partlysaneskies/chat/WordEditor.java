//
// Written by J10a1n15, with a lot of help from Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.chat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import me.partlysanestudios.partlysaneskies.system.commands.PSSCommand;
import me.partlysanestudios.partlysaneskies.utils.ChatUtils;
import net.minecraft.util.IChatComponent;
import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;


public class WordEditor {

    static String[][] wordsToEdit = {};
    /*
      Structure:
        {
            [
                "word to replace",
                "replacement word"
            ],
            [
                "word to replace",
                "replacement word"
            ]
         }
     */


    public static String handleWordEditorMessage(String message) {
        if (!PartlySaneSkies.config.wordEditor) return message;

        for (final String[] word : wordsToEdit) {

            // ChatUtils.INSTANCE.sendClientMessage("Trying to replace \"" + word[0] + "\" with \"" + word[1]);
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

            if (message.contains(wordToReplace)) { // If the to replace exists
                message = message.replace(wordToReplace, replacementWord);
            }
        }

        return message;
    }

    // Checks if the message contains a link
    public static boolean shouldEditMessage(IChatComponent message) {
        if (!PartlySaneSkies.config.wordEditor) {
            return false;
        }

        String formattedMessage = message.getFormattedText();

        for (final String[] word : wordsToEdit) {
            final String wordToReplace = word[0];
            if (formattedMessage.contains(wordToReplace)) {
                return true;
            }
        }

        return false;
    }

    public static void listWords() {
        if (WordEditor.wordsToEdit.length == 0) {
            ChatUtils.INSTANCE.sendClientMessage("§7There are no words to replace.");
            return;
        }

        ChatUtils.INSTANCE.sendClientMessage("§7Words to replace:");
        for (int i = 0; i < WordEditor.wordsToEdit.length; i++) {
            ChatUtils.INSTANCE.sendClientMessage("§b" + (i + 1) + ". §7" + wordsToEdit[i][0] + " §8-> §7" + wordsToEdit[i][1]);
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

                        ChatUtils.INSTANCE.sendClientMessage("§7To add a word to replace, run §b/wordeditor add <word> <replacement>§7. To remove a word, run §b/wordeditor remove <index>§7. To list all of the words, run §b/wordeditor list§7.");

                        WordEditor.listWords();
                        return;
                    }

                    if (args[0].equalsIgnoreCase("remove")) {
                        if (args.length == 1) {
                            ChatUtils.INSTANCE.sendClientMessage("§cError: Must provide an index to remove");
                            return;
                        }

                        int i;
                        try {
                            i = Integer.parseInt(args[1]);
                        } catch (NumberFormatException e) {
                            ChatUtils.INSTANCE.sendClientMessage("§cPlease enter a valid number index and try again.");
                            return;
                        }

                        if (i > WordEditor.wordsToEdit.length || i < 1) {
                            ChatUtils.INSTANCE.sendClientMessage("§cPlease select a valid index and try again.");
                            return;
                        }
                        ChatUtils.INSTANCE.sendClientMessage("§aRemoving: §b" + WordEditor.wordsToEdit[i - 1][0] + " §8-> §b" + WordEditor.wordsToEdit[i - 1][1]);
                        WordEditor.wordsToEdit = ArrayUtils.removeElement(WordEditor.wordsToEdit, WordEditor.wordsToEdit[i - 1]);    // Removes the element from the array
                        try {
                            WordEditor.save();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    if (args[0].equalsIgnoreCase("add")) {
                        if (args.length < 3) {
                            ChatUtils.INSTANCE.sendClientMessage("§cError: Must provide a word and a replacement");
                            return;
                        }

                        String word = args[1];
                        String replacement = args[2];

                        for (int i = 3; i < args.length; i++) {
                            replacement += " " + args[i];
                        }

                        ChatUtils.INSTANCE.sendClientMessage("§aAdding: §b" + word + " §8-> §b" + replacement);
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
}