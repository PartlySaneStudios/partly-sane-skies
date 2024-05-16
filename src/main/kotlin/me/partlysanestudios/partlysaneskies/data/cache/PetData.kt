//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.data.cache

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.Expose
import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.data.skyblockdata.Rarity
import me.partlysanestudios.partlysaneskies.data.skyblockdata.Rarity.Companion.getRarityFromColorCode
import me.partlysanestudios.partlysaneskies.features.debug.DebugKey
import me.partlysanestudios.partlysaneskies.utils.ChatUtils
import me.partlysanestudios.partlysaneskies.utils.MathUtils
import me.partlysanestudios.partlysaneskies.utils.MinecraftUtils.chestInventory
import me.partlysanestudios.partlysaneskies.utils.MinecraftUtils.getAllPets
import me.partlysanestudios.partlysaneskies.utils.StringUtils.removeColorCodes
import net.minecraft.client.gui.inventory.GuiChest
import net.minecraft.entity.Entity
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.io.FileWriter
import java.io.IOException
import java.io.Reader
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.io.path.Path

object PetData {
    private var petDataJson: PetDataJson? = null

    val jsonPath = Path("./config/partly-sane-skies/petData.json")

    var lastSaveTime = -1L
    fun tick() {
        parsePetGuiForCache()
        if (MathUtils.onCooldown(lastSaveTime, (60*1000L*.25).toLong())) {
            return
        }

        lastSaveTime = PartlySaneSkies.time
        Thread({
            save()
        }, "Pet Data Save").start()

    }

    @Throws(IOException::class)
    fun load() {
        val file = jsonPath.toFile()

        // If the file doesn't exist, fill it with an empty array list to prevent NullPointerException
        if (file.createNewFile()) {
            file.setWritable(true)
            val writer = FileWriter(file)
            writer.write(Gson().toJson(PetDataJson()))
            writer.close()
        }

        // Make a new reader
        file.setWritable(true)
        val reader: Reader = Files.newBufferedReader(Paths.get(file.path))

        // Reads the file and set it as the list
        val petDataJson = Gson().fromJson(reader, PetDataJson::class.java) as PetDataJson
        this.petDataJson = petDataJson
    }

    // Saves all the pet data
    @Throws(IOException::class)
    fun save() {
        // Creates a new file and Gson instance
        val file = jsonPath.toFile()
        val builder = GsonBuilder()
        builder.setPrettyPrinting()
        builder.serializeSpecialFloatingPointValues()
        val gson = builder.create()

        // Converts list to JSON string
        val json = gson.toJson(petDataJson)

        // Writes string to file
        val writer = FileWriter(file)
        writer.write(json)
        writer.close()
    }

    /**
     * @return the current pet level or -1 when a pet is not spawned or unknown
     */
    fun getCurrentPetLevel(): Int {
        parsePetFromWorld()
        return petDataJson?.currentPetLevel ?: -1
    }
    /**
     * @return the current pet name or an empty string when a pet is not spawned or unknown
     */
    fun getCurrentPetName(): String {
        parsePetFromWorld()
        return petDataJson?.currentPetName ?: ""
    }

    /**
     * @return the current pet rarity or [Rarity.UNKNOWN] when a pet is not spawned or unknown
     */
    fun getCurrentPetRarity(): Rarity {
        parsePetFromWorld()
        return petDataJson?.currentPetRarity ?: Rarity.UNKNOWN
    }



    @SubscribeEvent
    fun parseFromChat(event: ClientChatReceivedEvent){
//        Parse despawn message
        if (event.message.unformattedText.startsWith("You despawned your")) {
            petDataJson?.currentPetName = ""
            petDataJson?.currentPetLevel = -1
            petDataJson?.currentPetRarity = Rarity.UNKNOWN
        }

        if (event.message.unformattedText.startsWith("You summoned your")) {
            // Define the regular expression pattern
            val regex = "§r§aYou summoned your §r(§.)(\\w+(\\s\\w+)*)( ✦)?§r§a!§r"
            // Create a Pattern object
            val pattern: Pattern = Pattern.compile(regex)

            // Create a Matcher object
            val matcher: Matcher = pattern.matcher(event.message.formattedText)
            if (!matcher.find()) {
                return
            }

            petDataJson?.currentPetName = matcher.group(2)
            petDataJson?.currentPetRarity = matcher.group(1)?.getRarityFromColorCode() ?: Rarity.UNKNOWN
            petDataJson?.currentPetLevel = petDataJson?.petNameLevelMap?.get(petDataJson?.currentPetRarity)?.get(petDataJson?.currentPetName) ?: -1
        }

        val petLevelUpRegex = "§r§aYour §r(§.)(\\w+(\\s\\w+)*)( ✦)? §r§aleveled up to level §r§9(\\d+)§r§a!§r".toRegex()
        if (petLevelUpRegex.find(event.message.formattedText) != null) {

            // Find the match
            val matchResult = petLevelUpRegex.find(event.message.formattedText)

            // Extract pet name and level if a match is found
            matchResult?.let {
                val colorCode = it.groupValues[1]
                val petName = it.groupValues[2]
                val petLevel = it.groupValues[5].toInt()

                val petRarity = colorCode.getRarityFromColorCode()
                petDataJson?.petNameLevelMap?.get(petRarity)?.put(petName, petLevel)
            }
        }

        val autoPetRegex = """\[Lvl (\d+)] (§\w)(\w+(\s\w+)*)( ✦)""".toRegex()

        if (autoPetRegex.find(event.message.formattedText) != null) {
            val matchResult = autoPetRegex.find(event.message.formattedText)!!
            // Extract the pet level and name
            val (_, petLevel, colorCode, petName, _, _) = matchResult.destructured


            petDataJson?.currentPetLevel = petLevel.toInt()
            petDataJson?.currentPetName = petName
            petDataJson?.currentPetRarity = colorCode.getRarityFromColorCode()
            petDataJson?.petNameLevelMap?.get(petDataJson?.currentPetRarity)?.put(petName, petLevel.toInt())

        }
    }

    private fun parsePetFromWorld() {
        if (DebugKey.isDebugMode() && (PartlySaneSkies.config.debugPrintPetWorldParsingInformation)) {
            ChatUtils.visPrint("getting users pet")
        }
        val usersPet = getUsersPet() ?: return

        if (DebugKey.isDebugMode() && PartlySaneSkies.config.debugPrintPetWorldParsingInformation) {
            ChatUtils.visPrint("Creating pattern")
        }
        val pattern = """§8\[§7Lv(\d+)§8] (§\w)([\w']+)\s*('s)? (\w+)""".toRegex()

        if (DebugKey.isDebugMode() && PartlySaneSkies.config.debugPrintPetWorldParsingInformation) {
            ChatUtils.visPrint("Finding pattern")
            ChatUtils.visPrint("display name ${usersPet.displayName.formattedText}")
        }

        val matchResult = pattern.find(usersPet.displayName.unformattedText) ?: return

        if (DebugKey.isDebugMode() && PartlySaneSkies.config.debugPrintPetWorldParsingInformation) {
            ChatUtils.visPrint("founD")
        }
        val (_, level, colorCode, _, petType) = matchResult.destructured
        if (DebugKey.isDebugMode() && PartlySaneSkies.config.debugPrintPetWorldParsingInformation) {
            ChatUtils.visPrint(petType)
        }
        petDataJson?.currentPetName = petType
        petDataJson?.currentPetRarity = colorCode.getRarityFromColorCode()
        if (level.toIntOrNull() != null) {
            petDataJson?.currentPetLevel = level.toInt()
        }
    }


    private fun parsePetGuiForCache() {
        if (!isPetGui()) {
            return
        }

        val inventory = (PartlySaneSkies.minecraft.currentScreen as GuiChest).chestInventory

        for (i in 0..<inventory.sizeInventory) {
            val item = inventory.getStackInSlot(i)?: continue

            val regex = "(§.)\\[Lvl (\\d+)] (§.)(\\w+(\\s\\w+)*)( ✦)?".toRegex()

            val matchResult = regex.find(item.displayName?: "") ?: continue

            val (_, petLevel, colorCode, petName, _) = matchResult.destructured

            val petRarity = colorCode.getRarityFromColorCode()
            petDataJson?.petNameLevelMap?.get(petRarity)?.put(petName, petLevel.toIntOrNull()?: continue) ?: continue
        }
    }

    /**
     * @return if the current GUI is the pets gui
     */
    private fun isPetGui(): Boolean {
        val currentScreen = PartlySaneSkies.minecraft.currentScreen
        if (currentScreen !is GuiChest) {
            return false
        }
        val upper = currentScreen.chestInventory
        return upper.displayName.formattedText.removeColorCodes().contains("Pets")
    }

    /**
     * Using a list of all pets from getUsersPet(), check to see if it's owned by the player
     * @return the current user's pet
     */
    private fun getUsersPet(): Entity? {
        val name = PartlySaneSkies.minecraft.thePlayer?.getName()?: ""
        val petEntities = getAllPets()
        // If the pet says Ex: "[Lv100] *Su386*'s Black Cat" return that entity

        for (entity in petEntities) {
            if (entity.name.removeColorCodes().lowercase(Locale.getDefault()).contains(name.lowercase(Locale.getDefault()))) {
                return entity
            }
        }
        return null
    }


    /**
     * PetDataJson is a private class representing the data that is saved in the cache
     * @property currentPetRarity: the current pet's rarity
     * @property currentPetLevel: The current pet's level
     * @property currentPetName: The current pet's name
     * @property petNameLevelMap: A two-dimensional map with the rarity as the first key, and a hashmap containing the pet name as the key and the pet level as the value as the value
     */
    private class PetDataJson {

        @Expose
        var currentPetName: String = ""

        @Expose
        var currentPetLevel: Int = -1

        @Expose
        var currentPetRarity: Rarity = Rarity.UNKNOWN

        @Expose
        val petNameLevelMap: HashMap<Rarity, HashMap<String, Int>> = HashMap()

        init {
            for (rarity in Rarity.entries) {
                petNameLevelMap[rarity] = HashMap()
            }
        }
    }
}