package me.partlysanestudios.partlysaneskies.data.cache

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.Expose
import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.features.chat.ChatAlertsManager
import me.partlysanestudios.partlysaneskies.features.debug.DebugKey
import me.partlysanestudios.partlysaneskies.utils.ChatUtils
import me.partlysanestudios.partlysaneskies.utils.MathUtils
import me.partlysanestudios.partlysaneskies.utils.StringUtils.removeColorCodes
import net.minecraft.entity.Entity
import net.minecraft.entity.item.EntityArmorStand
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.io.File
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

    val jsonPath = Path("config/petData.json")

    var lastSaveTime = -1L
    fun tick() {
        if (MathUtils.onCooldown(lastSaveTime, (60*1000L*1.5).toLong())) {
            return
        }
        Thread() {
            save()
        }.start()
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

    // Saves all the chat alerts data
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

    /*
     * @returns the current pet level or -1 when a pet is not spawned or unknown
     */
    fun getCurrentPetLevel(): Int {
        parsePetFromWorld()
        return petDataJson?.currentPetLevel ?: -1
    }
    /*
     * @returns the current pet name or an empty string when a pet is not spawned or unknown
     */
    fun getCurrentPetName(): String {
        parsePetFromWorld()
        return petDataJson?.currentPetName ?: ""
    }

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
        }

        if (event.message.unformattedText.startsWith("You summoned your")) {
            // Define the regular expression pattern
            val regex = "You summoned your (\\w+)!"
            // Create a Pattern object
            val pattern: Pattern = Pattern.compile(regex)

            // Create a Matcher object
            val matcher: Matcher = pattern.matcher(event.message.unformattedText)
            if (!matcher.find()) {
                return
            }

            petDataJson?.currentPetName = matcher.group(1)
            petDataJson?.currentPetLevel = petDataJson?.petNameLevelMap?.get(petDataJson?.currentPetName) ?: -1
        }

        val petLevelUpRegex = "Your (\\w+) leveled up to level (\\d+)!".toRegex()

        if (petLevelUpRegex.find(event.message.unformattedText) != null) {

            // Find the match
            val matchResult = petLevelUpRegex.find(event.message.unformattedText)

            // Extract pet name and level if a match is found
            matchResult?.let {
                val petName = it.groupValues[1]
                val petLevel = it.groupValues[2].toInt()

                petDataJson?.petNameLevelMap?.put(petName, petLevel)
            }
        }

        val autoPetRegex = """\[Lvl (\d+)] (§\w)(\w+)""".toRegex()

        if (autoPetRegex.find(event.message.formattedText) != null) {
            val matchResult = autoPetRegex.find(event.message.formattedText)!!
            // Extract the pet level and name
            val (_, petLevel, colorCode, petName) = matchResult.destructured


            petDataJson?.petNameLevelMap?.put(petName, petLevel.toInt())
            petDataJson?.currentPetLevel = petLevel.toInt()
            petDataJson?.currentPetName = petName
            petDataJson?.currentPetRarity = colorCode.getPetRarityFromColorCode()
        }
    }

    private fun parsePetFromWorld() {
        if (DebugKey.isDebugMode() && PartlySaneSkies.config.debugPrintPetWorldParsingInformation) {
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
        val (_, level, colorCode, owner, petType) = matchResult.destructured
        if (DebugKey.isDebugMode() && PartlySaneSkies.config.debugPrintPetWorldParsingInformation) {
            ChatUtils.visPrint(petType)
        }
        petDataJson?.currentPetName = petType
        petDataJson?.currentPetRarity = colorCode.getPetRarityFromColorCode()
        if (level.toIntOrNull() != null) {
            petDataJson?.currentPetLevel = level.toInt()
        }
    }

    // Using that list of pets, check to see if it's owned by a specific player
    private fun getUsersPet(): Entity? {
        val name = PartlySaneSkies.minecraft.thePlayer.getName()
        val petEntities = getAllPets()
        // If the pet says Ex: "[Lv100] *Su386*'s Black Cat" return that entity

        for (entity in petEntities) {
            if (entity.name.removeColorCodes().lowercase(Locale.getDefault()).contains(name.lowercase(Locale.getDefault()))) {
                return entity
            }
        }
        return null
    }

    // Gets all the pets current loaded by the game
    private fun getAllPets(): List<Entity> {
        val petEntities: MutableList<Entity> = ArrayList()
        val armorStandEntities = getAllArmorStands()
        val pattern = """§8\[§7Lv(\d+)§8] (§\w)([\w']+)\s*('s)? (\w+)""".toRegex()

        // For every armor stand in the game, check if it's pet by looking for the level tag in front of the name.
        // Ex: "*[Lv*100] Su386's Black Cat"
        for (entity in armorStandEntities) {
            if (pattern.find(entity.name) != null) {
                petEntities.add(entity) // If so, add it to the list
            }
        }
        return petEntities
    }

    // Gets all the armor stands currently loaded by the game
    private fun getAllArmorStands(): List<Entity> {
        val armorStandEntities: MutableList<Entity> = ArrayList()
        val allEntities = getAllEntitiesInWorld()

        // For every entity in the world, check if its instance of an armor stand
        for (entity in allEntities) {
            if (entity is EntityArmorStand) {
                armorStandEntities.add(entity) // If so, add it to the list
            }
        }
        return armorStandEntities
    }

    // Returns a list of all loaded entities in the world
    private fun getAllEntitiesInWorld(): List<Entity> {
        return PartlySaneSkies.minecraft.theWorld.getLoadedEntityList()
    }

    private fun String.getPetRarityFromColorCode(): Rarity {
        return if (this == Rarity.COMMON.colorCode) {
            Rarity.COMMON
        } else if (this == Rarity.UNKNOWN.colorCode) {
            Rarity.UNCOMMON
        } else if (this == Rarity.RARE.colorCode) {
            Rarity.RARE
        } else if (this == Rarity.EPIC.colorCode) {
            Rarity.EPIC
        } else if (this == Rarity.LEGENDARY.colorCode){
            Rarity.LEGENDARY
        } else if (Rarity.MYTHIC.colorCode == this) {
            Rarity.MYTHIC
        } else {
            Rarity.UNKNOWN
        }
    }

    enum class Rarity(val order: Int, val colorCode: String): Comparable<Rarity> {
        UNKNOWN(-1, ""),
        COMMON(0, "§f"),
        UNCOMMON(1, "§a"),
        RARE(2, "§9"),
        EPIC(3, "§5"),
        LEGENDARY(4, "§6"),
        MYTHIC(5, "§d")
    }


    private class PetDataJson {
        @Expose
        var currentPetName: String = ""

        @Expose
        var currentPetLevel: Int = -1

        @Expose
        var currentPetRarity: Rarity = Rarity.UNKNOWN

        @Expose
        val petNameLevelMap: HashMap<String, Int> = HashMap()


    }




}