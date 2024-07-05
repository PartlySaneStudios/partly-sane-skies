package me.partlysanestudios.partlysaneskies.features.economy.minioncalculator

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import me.partlysanestudios.partlysaneskies.data.pssdata.PublicDataManager
import me.partlysanestudios.partlysaneskies.data.skyblockdata.SkyblockDataManager
import me.partlysanestudios.partlysaneskies.events.SubscribePSSEvent
import me.partlysanestudios.partlysaneskies.events.data.LoadPublicDataEvent
import me.partlysanestudios.partlysaneskies.utils.MathUtils.round
import me.partlysanestudios.partlysaneskies.utils.StringUtils.formatNumber
import me.partlysanestudios.partlysaneskies.utils.SystemUtils.getJsonFromPath
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

object MinionData {
    // A hashmap with the key as the minion id, and the value as the minion object
    private val minionMap = mutableMapOf<String, Minion>()

    // A hashmap with the key as the id of the fuel, and the value as the fuel object
    private val fuelMap = mutableMapOf<String, MinionFuel>()

    // The URL with the location of the minion data
    private const val MINIONS_DATA_URL = "constants/minion_data.json"

    // init: runs before the request ------ CALL THIS TO INIT

    fun getMostProfitMinionString(hours: Double, upgrades: List<Minion.Upgrade>, fuel: MinionFuel?) = buildString {
        append("§7In §6$hours§7 hour(s): (Upgrade:${upgrades})")
        val mostProfitableMinions = getBestMinions(upgrades, fuel)

        mostProfitableMinions.forEachIndexed { i, (minion, _) ->
            append("\n\n§7${i+1}.${minion.costBreakdown(minion.maxTier, hours, upgrades, fuel)}")
        }
    }

    fun getBestMinions(upgrade: List<Minion.Upgrade>, fuel: MinionFuel?): List<Pair<Minion, Double>> =
        minionMap.map { (_, minion) ->
            minion to minion.getTotalProfitPerMinute(minion.maxTier, upgrade, fuel)
        }.sortedByDescending { it.second }


    @JvmStatic // TODO: replace with getBestMinions
    fun getMostProfitMinion(upgrades: List<Minion.Upgrade>, fuel: MinionFuel?): LinkedHashMap<Minion, Double> {
        val priceMap = HashMap<Minion, Double>()

        for ((_, minion) in minionMap) {
            priceMap[minion] = minion.getTotalProfitPerMinute(minion.maxTier, upgrades, fuel)
        }
        return sortMap(priceMap)
    }


    @JvmStatic
    fun getMinion(id: String): Minion? = minionMap[id]

    // TODO: remove
    // Sorts the hashmap in descending order
    @JvmStatic
    fun sortMap(map: HashMap<Minion, Double>): LinkedHashMap<Minion, Double> {
        val list = map.entries.sortedByDescending { it.value }

        val sortedMap = LinkedHashMap<Minion, Double>()
        for (entry in list) {
            sortedMap[entry.key] = entry.value
        }

        return sortedMap
    }

    // Runs after the request
    @SubscribePSSEvent
    fun init(event: LoadPublicDataEvent) {
        val file = PublicDataManager.getFile(MINIONS_DATA_URL)
        val jsonObj = JsonParser().parse(file).asJsonObject

        // Gets the minion object from the json
        val minionObjects = jsonObj.getJsonFromPath("/minions")?.asJsonObject ?: return

        // For every item in the json, create a minion from it
        for ((id, minionElement) in minionObjects.entrySet()) {
            val minionObj = minionElement.asJsonObject
            val minion = Minion(id, minionObj)
            minionMap[id] = minion // Add the minion to the minion map
        }

        // Gets the fuel object from the json
        val fuelObjects = jsonObj.getJsonFromPath("/fuels")?.asJsonObject ?: return
        for ((id, fuelElement) in fuelObjects.entrySet()) {
            val fuelObj = fuelElement.asJsonObject
            val fuel = MinionFuel(id, fuelObj)
            fuelMap[id] = fuel // Add the fuel to the fuel map
        }
    }

    class Minion(val id: String, obj: JsonObject) {
        val displayName: String = obj.getJsonFromPath("/display_name")?.asString ?: id
        private val drops = mutableMapOf<String, Double>()
        private val cooldowns = mutableMapOf<Int, Duration>()
        private val category: String = obj.getJsonFromPath("/category")?.asString ?: ""
        val maxTier: Int = obj.getJsonFromPath("/maxTier")?.asInt ?: 11

        private val kraumpusSpeedIncrease = setOf("SNOW_GENERATOR")

        init {
            obj.getJsonFromPath("/drops")?.asJsonObject?.let { dropsJson ->
                for ((dropId, dropValue) in dropsJson.entrySet()) {
                    drops[dropId] = dropValue.asDouble
                    drops[dropId] = dropValue.asDouble
                }
            }
            obj.getJsonFromPath("/cooldown")?.asJsonObject?.let { cooldownJson ->
                for ((tier, cooldownValue) in cooldownJson.entrySet()) {
                    cooldowns[tier.toInt()] = cooldownValue.asDouble.seconds
                }
            }
        }

        override fun toString(): String = "$id: Drops:$drops Cooldowns:$cooldowns"

        fun getBaseItemsPerMinute(tier: Int, upgrades: List<Upgrade>, fuel: MinionFuel?): MutableMap<String, Double> {
            var cooldown = cooldowns[tier] ?: cooldowns[maxTier] ?: Duration.INFINITE

            var speedUpgrade = 0.0
            if (upgrades.contains(Upgrade.MINION_EXPANDER)) speedUpgrade += 0.05
            if (upgrades.contains(Upgrade.FLYCATCHER_UPGRADE)) speedUpgrade += 0.2
            if (upgrades.contains(Upgrade.SOULFLOW_ENGINE) || upgrades.contains(Upgrade.LESSER_SOULFLOW_ENGINE)) speedUpgrade -= 0.5
            if (upgrades.contains(Upgrade.SOULFLOW_ENGINE) && id == "VOIDLING_GENERATOR") speedUpgrade += 0.03 * tier

            fuel?.let { speedUpgrade += it.upgrade }

            cooldown /= (1 + speedUpgrade)

            // Adds the items generated
            val items = drops.mapValues { (_, dropValue) ->
                (1 / (2 * cooldown.inWholeMinutes)) * dropValue
            }.toMutableMap()

            // Adds the fuel in subtracted amount
            fuel?.amountNeeded()?.let {
                items[fuel.id] = -it
            }

            // Totals the number of items produced
            val baseItemsProduced = items.values.sum()

            // Adds the gifts generated by Krampus helm
            if (Upgrade.KRAMPUS_HELMET in upgrades) {
                val produced = (baseItemsProduced * 0.045 / 100)
                items["RED_GIFT"] = if (id in kraumpusSpeedIncrease) produced * 4 else produced
            }

            // Adds the diamonds generated by diamond spreading
            if (Upgrade.DIAMOND_SPREADING in upgrades) {
                items["DIAMOND"] = baseItemsProduced * 0.1
            }

            // Adds the potato generated by potato spreading
            if (Upgrade.POTATO_SPREADING in upgrades) {
                items["POTATO_ITEM"] = baseItemsProduced * 0.05
            }

            // Added the soulflow generated by the soulflow engines
            val soulflowUpgrades = setOf(Upgrade.SOULFLOW_ENGINE, Upgrade.LESSER_SOULFLOW_ENGINE)
            if (soulflowUpgrades.any { it in upgrades }) {
                items["RAW_SOULFLOW"] = 1.0 / 3
            }

            return items
        }

        fun getTotalProfitPerMinute(tier: Int, upgrades: List<Upgrade>, fuel: MinionFuel?): Double {
            var totalProfit = 0.0

            for ((itemId, amount) in getBaseItemsPerMinute(tier, upgrades, fuel)) {
                val price = runCatching { SkyblockDataManager.getItem(itemId)?.getBestPrice() }.getOrNull() ?: 0.0
                totalProfit += price * amount
            }

            return totalProfit
        }

        fun costBreakdown(tier: Int, hours: Double, upgrades: List<Upgrade>, fuel: MinionFuel?) = buildString {
            val colorPrefix = when (category) {
                "COMBAT" -> "§c"
                "FARMING" -> "§a"
                "MINING" -> "§9"
                "FORAGING" -> "§d"
                "FISHING" -> "§b"
                else -> "§7"
            }
            append("$colorPrefix$displayName§7:")

            for ((itemId, amountPerMinute) in getBaseItemsPerMinute(tier, upgrades, fuel)) {
                // Individual price of the item
                var price = SkyblockDataManager.getItem(itemId)?.getBestPrice() ?: 0.0
                price = price.round(1) // rounded to 1 decimal place

                // Total amount of money made by the item
                val totalItemProfit = (amountPerMinute * 60 * hours).round(1)
                append("\n   ")
                append("§6x${totalItemProfit.formatNumber()} ")
                append("§6${SkyblockDataManager.getItem(itemId)?.name} ")
                append("§7for ${price.formatNumber()} coins each.")
            }
        }

        enum class Upgrade {
            DIAMOND_SPREADING,
            KRAMPUS_HELMET,
            POTATO_SPREADING,
            LESSER_SOULFLOW_ENGINE,
            SOULFLOW_ENGINE,
            MINION_EXPANDER,
            FLYCATCHER_UPGRADE,
        }
    }

    class MinionFuel(val id: String, private val duration: Double?, val upgrade: Double) {
        constructor(id: String, obj: JsonObject) : this(
            id,
            obj.getJsonFromPath("duration")?.asDouble,
            obj.getJsonFromPath("speed_upgrade")?.asDouble ?: 0.0
        )

        // Returns the amount of fuel needed for the duration specified (in minutes)
        fun amountNeeded(minuteDuration: Double = 1.0): Double? = duration?.let { minuteDuration / it }
    }
}
