package me.partlysanestudios.partlysaneskies.features.economy.minioncalculator

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import me.partlysanestudios.partlysaneskies.data.pssdata.PublicDataManager
import me.partlysanestudios.partlysaneskies.data.skyblockdata.SkyblockDataManager
import me.partlysanestudios.partlysaneskies.events.SubscribePSSEvent
import me.partlysanestudios.partlysaneskies.events.data.LoadPublicDataEvent
import me.partlysanestudios.partlysaneskies.utils.MathUtils.round
import me.partlysanestudios.partlysaneskies.utils.StringUtils.formatNumber
import me.partlysanestudios.partlysaneskies.utils.SystemUtils
import me.partlysanestudios.partlysaneskies.utils.SystemUtils.getJsonFromPath
import org.apache.logging.log4j.Level

object MinionData {
    // A hashmap with the key as the minion id, and the value as the minion object
    val minionMap = mutableMapOf<String, Minion>()

    // A hashmap with the key as the id of the fuel, and the value as the fuel object
    val fuelMap = mutableMapOf<String, MinionFuel>()

    // The URL with the location of the minion data
    private const val MINIONS_DATA_URL = "constants/minion_data.json"

    // init: runs before the request ------ CALL THIS TO INIT

    fun getMostProfitMinionString(hours: Double, upgrades: Array<Minion.Upgrade>, fuel: MinionFuel?) = buildString {
        append("§7In §6$hours§7 hour(s): (Upgrade:${upgrades.asList()})")
        val mostProfitableMinions = getBestMinions(upgrades, fuel)

        mostProfitableMinions.forEachIndexed { i, (minion, _) ->
            append("\n\n§7${i+1}.${minion.costBreakdown(minion.maxTier, hours, upgrades, fuel)}")
        }
    }

    fun getBestMinions(upgrade: Array<Minion.Upgrade>, fuel: MinionFuel?): List<Pair<Minion, Double>> =
        minionMap.map { (_, minion) ->
            minion to minion.getTotalProfitPerMinute(minion.maxTier, upgrade, fuel)
        }.sortedByDescending { it.second }


    @JvmStatic // TODO: replace with getBestMinions
    fun getMostProfitMinion(upgrades: Array<Minion.Upgrade>, fuel: MinionFuel?): LinkedHashMap<Minion, Double> {
        val priceMap = HashMap<Minion, Double>()

        for ((id, minion) in minionMap) {
            priceMap[minion] = minion.getTotalProfitPerMinute(minion.maxTier, upgrades, fuel)
        }
        return sortMap(priceMap)
    }

    @JvmStatic
    fun getMinion(id: String): Minion? = minionMap[id]

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
        val displayName: String = obj.getJsonFromPath("/displayName")?.asString ?: ""
        val drops = HashMap<String, Double>()
        val cooldowns = HashMap<Int, Double>()
        val category: String = obj.getJsonFromPath("/category")?.asString ?: ""
        val maxTier: Int = obj.getJsonFromPath("/maxTier")?.asInt ?: 11
        private val kraumpusSpeedIncrease = arrayOf("SNOW_GENERATOR")

        init {
            obj.getJsonFromPath("/drops")?.asJsonObject?.let { dropsJson ->
                for ((dropId, dropValue) in dropsJson.entrySet()) {
                    drops[dropId] = dropValue.asDouble
                }
            }
            obj.getJsonFromPath("/cooldown")?.asJsonObject?.let { cooldownJson ->
                for ((tier, cooldownValue) in cooldownJson.entrySet()) {
                    cooldowns[tier.toInt()] = cooldownValue.asDouble
                }
            }
        }

        override fun toString(): String = "$id: Drops:$drops Cooldowns:$cooldowns"

        fun getBaseItemsPerMinute(tier: Int, upgrades: Array<Upgrade>, fuel: MinionFuel?): HashMap<String, Double> {
            val upgradesList = upgrades.toList()
            var cooldownInSeconds = cooldowns[tier] ?: cooldowns[maxTier] ?: Double.MAX_VALUE

            var speedUpgrade = 0.0
            if (upgradesList.contains(Upgrade.MINION_EXPANDER)) speedUpgrade += 0.05
            if (upgradesList.contains(Upgrade.FLYCATCHER_UPGRADE)) speedUpgrade += 0.2
            if (upgradesList.contains(Upgrade.SOULFLOW_ENGINE) || upgradesList.contains(Upgrade.LESSER_SOULFLOW_ENGINE)) speedUpgrade -= 0.5
            if (upgradesList.contains(Upgrade.SOULFLOW_ENGINE) && id == "VOIDLING_GENERATOR") speedUpgrade += 0.03 * tier

            fuel?.let { speedUpgrade += it.upgrade }

            cooldownInSeconds /= (1 + speedUpgrade)

            // Calculates the correct cooldown in minutes
            val cooldownInMinute = cooldownInSeconds / 60.0

            // Adds the items generated
            val items = HashMap<String, Double>()
            for ((dropId, dropValue) in drops) {
                items[dropId] = (1 / (2 * cooldownInMinute)) * dropValue
            }

            // Adds the fuel in subtracted amount
            if (fuel != null && fuel.duration != -1.0) {
                items[fuel.id] = -fuel.amountNeeded(1.0)
            }

            // Totals the number of items produced
            var baseItemsProduced = 0
            for (itemQuantity in items.values) {
                baseItemsProduced += itemQuantity.toInt()
            }

            // Adds the gifts generated by Krampus helm
            if (upgradesList.contains(Upgrade.KRAMPUS_HELMET)) {
                if (kraumpusSpeedIncrease.contains(id)) {
                    items["RED_GIFT"] = baseItemsProduced * 0.0045 / 100 * 4
                } else {
                    items["RED_GIFT"] = baseItemsProduced * 0.0045 / 100
                }
            }

            // Adds the diamonds generated by diamond spreading
            if (upgradesList.contains(Upgrade.DIAMOND_SPREADING)) {
                items["DIAMOND"] = baseItemsProduced * 0.1
            }

            // Adds the potato generated by potato spreading
            if (upgradesList.contains(Upgrade.POTATO_SPREADING)) {
                items["POTATO_ITEM"] = baseItemsProduced * 0.05
            }

            // Added the soulflow generated by the soulflow engines
            if (upgradesList.contains(Upgrade.SOULFLOW_ENGINE) || upgradesList.contains(Upgrade.LESSER_SOULFLOW_ENGINE)) {
                items["RAW_SOULFLOW"] = 1.0 / 3
            }

            return items
        }

        fun getTotalProfitPerMinute(tier: Int, upgrades: Array<Upgrade>, fuel: MinionFuel?): Double {
            var totalProfit = 0.0

            for ((itemId, amount) in getBaseItemsPerMinute(tier, upgrades, fuel)) {
                var price = 0.0
                try {
                    price = SkyblockDataManager.getItem(itemId)?.getBestPrice() ?: 0.0
                } catch (e: NullPointerException) {
                    SystemUtils.log(Level.WARN, "$itemId: DOES NOT HAVE PRICE")
                }

                totalProfit += price * amount
            }

            return totalProfit
        }

        fun costBreakdown(tier: Int, hours: Double, upgrades: Array<Upgrade>, fuel: MinionFuel?): String {
            // Creates a color for each prefix
            val colorPrefix = when (category) {
                "COMBAT" -> "§c"
                "FARMING" -> "§a"
                "MINING" -> "§9"
                "FORAGING" -> "§d"
                "FISHING" -> "§b"
                else -> "§7"
            }

            val str = StringBuilder("$colorPrefix$displayName§:")

            for ((itemId, amountPerMinute) in getBaseItemsPerMinute(maxTier, upgrades, fuel)) {
                // Individual price of the item
                var price = SkyblockDataManager.getItem(itemId)?.getBestPrice() ?: 0.0
                price = price.round(1) // rounded to 1 decimal place

                // Total amount of money made by the item
                var totalItemProfit = amountPerMinute
                totalItemProfit *= 60 * hours
                totalItemProfit = totalItemProfit.round(1) // rounded to 1 decimal place

                str.append("\n§7   §6x").append(totalItemProfit.formatNumber()).append("§7 §6")
                    .append(SkyblockDataManager.getItem(itemId)?.name).append("§7 for ")
                    .append(price.formatNumber()).append(" coins each.")
            }

            // Total amount of money made in given hours by the minion
            var totalMinionProfit = getTotalProfitPerMinute(tier, upgrades, fuel)
            totalMinionProfit *= 60 * hours
            totalMinionProfit = totalMinionProfit.round(1) // rounded to 1 decimal place

            str.append("\n§7   Total: §6").append(totalMinionProfit.formatNumber()).append("§7 coins in ")
                .append(hours.formatNumber()).append(" hours.")

            return str.toString()
        }

        enum class Upgrade {
            DIAMOND_SPREADING,
            KRAMPUS_HELMET,
            POTATO_SPREADING,
            MINION_EXPANDER,
            FLYCATCHER_UPGRADE,
            LESSER_SOULFLOW_ENGINE,
            SOULFLOW_ENGINE
        }
    }

    class MinionFuel(val id: String, val duration: Double, val upgrade: Double) {
        constructor(id: String, obj: JsonObject) : this(
            id,
            obj.getJsonFromPath("duration")?.asDouble ?: 0.0,
            obj.getJsonFromPath("speed_upgrade")?.asDouble ?: 0.0
        )

        // Returns the amount of fuel needed for the duration specified (in minutes)
        fun amountNeeded(minuteDuration: Double): Double {
            return if (duration == -1.0) {
                -1.0
            } else {
                minuteDuration / duration
            }
        }
    }
}
