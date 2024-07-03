package me.partlysanestudios.partlysaneskies.features.gui.hud.rngdropbanner

import cc.polyfrost.oneconfig.utils.gui.GuiUtils
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import me.partlysanestudios.partlysaneskies.commands.PSSCommand
import me.partlysanestudios.partlysaneskies.utils.ChatUtils
import me.partlysanestudios.partlysaneskies.utils.StringUtils.pluralize
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.io.Reader
import java.nio.file.Files
import java.nio.file.Paths

object RareDropGUIManager {

    private var filters: MutableMap<FilterType, Set<String>> = mutableMapOf()
    private const val CONFIG_PATH = "./config/partly-sane-skies/rareDropFilters.json"

    var currentFilterType: FilterType = FilterType.BLACKLIST
        get() = FilterType.BLACKLIST
        set(value) {
            field = value
            saveData()
        }

    var currentFilter: Set<String>
        get() = filters[currentFilterType] ?: emptySet()
        set(value) {
            filters[currentFilterType] = value
        }

    fun registerCommand() {
        PSSCommand("raredrop")
            .addAlias("rd")
            .setDescription("Opens the Rare Drop GUI")
            .setRunnable { _, _ ->
                openGui()
            }
            .register()
    }

    fun addFilter(vararg filters: String) {
        ChatUtils.sendClientMessage("Added ${"filter".pluralize(filters.size)}")
        currentFilter += filters
        saveData()
    }

    private fun openGui() {
        GuiUtils.displayScreen(RareDropGUI())
    }

    val presets = listOf(
        RareDropPreset(
            "Dungeons",
            listOf(
                "Conjuring",
                "Silent Death",
                "Dreadlord Sword",
                "Zombie Soldier Cutlass",
                "Earth Shard",
                "Zombie Commander Whip",
                "Zombie Knight Sword",
                "Soulstealer Bow",
                "Sniper Bow",
                "Machine Gun Shortbow",
                "Bouncy Helmet",
                "Bouncy Chestplate",
                "Bouncy Leggings",
                "Bouncy Boots",
                "Heavy Helmet",
                "Heavy Chestplate",
                "Heavy Leggings",
                "Heavy Boots",
                "Rotten Helmet",
                "Rotten Chestplate",
                "Rotten Leggings",
                "Rotten Boots",
                "Sniper Helmet",
                "Skeleton Grunt Helmet",
                "Skeleton Grunt Chestplate",
                "Skeleton Grunt Leggings",
                "Skeleton Grunt Boots",
                "Skeleton Lord Helmet",
                "Skeleton Lord Chestplate",
                "Skeleton Lord Leggings",
                "Skeleton Lord Boots",
                "Skeleton Master Helmet",
                "Skeleton Master Chestplate",
                "Skeleton Master Leggings",
                "Skeleton Master Boots",
                "Skeleton Soldier Helmet",
                "Skeleton Soldier Chestplate",
                "Skeleton Soldier Leggings",
                "Skeleton Soldier Boots",
                "Skeletor Helmet",
                "Skeletor Chestplate",
                "Skeletor Leggings",
                "Skeletor Boots",
                "Super Heavy Helmet",
                "Super Heavy Chestplate",
                "Super Heavy Leggings",
                "Super Heavy Boots",
                "Zombie Commander Helmet",
                "Zombie Commander Chestplate",
                "Zombie Commander Leggings",
                "Zombie Commander Boots",
                "Zombie Knight Helmet",
                "Zombie Knight Chestplate",
                "Zombie Knight Leggings",
                "Zombie Knight Boots",
                "Zombie Lord Helmet",
                "Zombie Lord Chestplate",
                "Zombie Lord Leggings",
                "Zombie Lord Boots",
                "Zombie Soldier Helmet",
                "Zombie Soldier Chestplate",
                "Zombie Soldier Leggings",
                "Zombie Soldier Boots",
            ),
        ),
        RareDropPreset(
            "Useless End Drops",
            listOf(
                "Ender Helmet",
                "Ender Chestplate",
                "Ender Leggings",
                "Ender Boots",
                "Ender Belt",
                "Ender Cloak",
                "Ender Gauntlet",
                "Ender Necklace",
                "Enchanted Ender Pearl",
                "End Stone Bow",
                "Ender Monocle",
                "Enchanted Eye of Ender",
                "Enchanted End Stone",
                "Enchanted Obsidian",
            ),
        ),
        RareDropPreset(
            "Useless Garden Drops",
            listOf(
                "Beady Eyes",
                "Buzzin' Beats Vinyl",
                "Cicada Symphony Vinyl",
                "Clipped Wings",
                "DynaMITES Vinyl",
                "Earthworm Ensemble Vinyl",
                "Not Just A Pest Vinyl",
                "Pretty Fly Vinyl",
            ),
        ),
    )

    enum class FilterType(val displayName: String) {
        BLACKLIST("Blacklist"),
        WHITELIST("Whitelist"),
    }

    @Throws(IOException::class)
    fun saveData() {
        val file = File(CONFIG_PATH)
        file.createNewFile()
        val gson = GsonBuilder()
            .setPrettyPrinting()
            .serializeSpecialFloatingPointValues()
            .create()
        val writer = FileWriter(file)
        writer.write(gson.toJson(filters))
        writer.close()
    }

    @Throws(IOException::class)
    fun loadData() {
        val file = File(CONFIG_PATH)
        file.setWritable(true)

        if (file.createNewFile()) {
            val writer = FileWriter(file)
            writer.write(Gson().toJson(emptyMap<FilterType, Set<String>>()))
            writer.close()
        }

        val reader: Reader = Files.newBufferedReader(Paths.get(file.path))
        val jsonElement = JsonParser().parse(reader)
        reader.close()

        filters = mutableMapOf<FilterType, Set<String>>().apply {
            jsonElement.asJsonObject.entrySet().forEach { (key, value) ->
                val filterType = FilterType.valueOf(key)
                val filterSet = value.asJsonArray.map { it.asString }.toSet()
                this[filterType] = filterSet
            }
        }
    }

}
