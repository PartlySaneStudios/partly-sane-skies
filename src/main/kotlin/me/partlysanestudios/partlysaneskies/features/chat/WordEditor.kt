//
// Written by J10a1n15 and ItsEmpa.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.features.chat

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.commands.PSSCommand
import me.partlysanestudios.partlysaneskies.utils.ChatUtils
import net.minecraft.util.IChatComponent
import org.apache.commons.lang3.ArrayUtils
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.io.Reader
import java.nio.file.Files
import java.nio.file.Paths

object WordEditor {

    private const val WORD_EDITOR_PATH = "./config/partly-sane-skies/wordEditor.json"

    // TODO change to map?
    var wordsToEdit: Array<Array<String>> = arrayOf()
        private set

    fun load() {
        val file = File(WORD_EDITOR_PATH)
        file.setWritable(true)
        if (file.createNewFile()) {
            FileWriter(file).use { it.write(Gson().toJson(arrayOf<Array<String>>())) }
        }
        val reader: Reader = Files.newBufferedReader(Paths.get(file.path))
        wordsToEdit = Gson().fromJson(reader, Array<Array<String>>::class.java)
    }

    fun save() {
        val file = File(WORD_EDITOR_PATH)
        file.createNewFile()
        val gson: Gson = GsonBuilder()
            .setPrettyPrinting()
            .serializeSpecialFloatingPointValues()
            .create()
        val json: String = gson.toJson(wordsToEdit)
        FileWriter(file).use { it.write(json) }
    }

    // already checks config before calling this, so config check is not needed here
    fun handleWordEditorMessage(message: String): String {
        var editedMessage = message
        wordsToEdit.forEach { (wordToReplace, replacementWord) ->
            editedMessage = editedMessage.replace(wordToReplace, replacementWord)
        }
        return editedMessage
    }

    fun shouldEditMessage(message: String): Boolean {
        if (!PartlySaneSkies.config.wordEditor) return false

        return wordsToEdit.any { (wordToReplace, _) -> wordToReplace in message.toLowerCase() }
    }

    // List all words configured for replacement
    fun listWords() {
        if (wordsToEdit.isEmpty()) {
            ChatUtils.sendClientMessage("§7There are no words to replace.")
            return
        }
        ChatUtils.sendClientMessage("§7Words to replace:")
        wordsToEdit.forEachIndexed { index, (wordToReplace, replacementWord) ->
            ChatUtils.sendClientMessage("§b${index + 1}. §7$wordToReplace §8-> §7$replacementWord")
        }
    }

    // Register the word editor command
    fun registerWordEditorCommand() {
        PSSCommand("wordeditor")
            .addAlias("wordedit", "we", "worldreplace", "wr")
            .setDescription("Operates the word editor: /wordeditor add <word> <replacement>, /wordeditor list or /wordeditor remove <index>")
            .setRunnable { args ->
                when {
                    args.isEmpty() || args[0].equals("list", ignoreCase = true) -> {
                        ChatUtils.sendClientMessage("§7To add a word to replace, run §b/wordeditor add <word> <replacement>§7. To remove a word, run §b/wordeditor remove <index>§7. To list all of the words, run §b/wordeditor list§7.")
                        listWords()
                    }
                    args[0].equals("remove", ignoreCase = true) -> {
                        if (args.size < 2) {
                            ChatUtils.sendClientMessage("§cError: Must provide an index to remove")
                            return@setRunnable
                        }
                        val i = args[1].toIntOrNull()?.takeIf { it in 1..wordsToEdit.size } ?: run {
                            ChatUtils.sendClientMessage("§cPlease enter a valid number index and try again.")
                            return@setRunnable
                        }
                        ChatUtils.sendClientMessage("§aRemoving: §b${wordsToEdit[i - 1][0]} §8-> §b${wordsToEdit[i - 1][1]}")
                        wordsToEdit = ArrayUtils.removeElement(wordsToEdit, wordsToEdit[i - 1])
                        save()
                    }
                    args[0].equals("add", ignoreCase = true) -> {
                        if (args.size < 3) {
                            ChatUtils.sendClientMessage("§cError: Must provide a word and a replacement")
                            return@setRunnable
                        }
                        val word = args[1]
                        val replacement = args.drop(2).joinToString(" ")
                        ChatUtils.sendClientMessage("§aAdding: §b$word §8-> §b$replacement")
                        wordsToEdit = wordsToEdit.plus(arrayOf(word, replacement))
                        try {
                            save()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }
            }.register()
    }
}
