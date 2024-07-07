package me.partlysanestudios.partlysaneskies.features.economy.minioncalculator

import com.google.gson.JsonObject
import me.partlysanestudios.partlysaneskies.data.skyblockdata.SkyblockDataManager
import me.partlysanestudios.partlysaneskies.utils.MathUtils.round
import me.partlysanestudios.partlysaneskies.utils.StringUtils.formatNumber
import me.partlysanestudios.partlysaneskies.utils.SystemUtils.getJsonFromPath
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class Minion(val id: String, obj: JsonObject) {
    val displayName: String = obj.getJsonFromPath("/displayName")?.asString ?: id
    private val drops = mutableMapOf<String, Double>()
    private val cooldowns = mutableMapOf<Int, Duration>()
    internal val category: String = obj.getJsonFromPath("/category")?.asString ?: ""
    val maxTier: Int = obj.getJsonFromPath("/maxTier")?.asInt ?: 11

    private val krampusSpeedIncrease = setOf("SNOW_GENERATOR")

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

    fun getBaseItemsPerMinute(tier: Int, upgrades: List<MinionUpgrade>, fuel: MinionFuel?): MutableMap<String, Double> {
        var cooldown = cooldowns[tier] ?: cooldowns[maxTier] ?: 1.seconds

        var speedUpgrade = 0.0
        if (upgrades.contains(MinionUpgrade.MINION_EXPANDER)) speedUpgrade += 0.05
        if (upgrades.contains(MinionUpgrade.FLYCATCHER_UPGRADE)) speedUpgrade += 0.2
        if (upgrades.contains(MinionUpgrade.SOULFLOW_ENGINE) || upgrades.contains(MinionUpgrade.LESSER_SOULFLOW_ENGINE)) speedUpgrade -= 0.5
        if (upgrades.contains(MinionUpgrade.SOULFLOW_ENGINE) && id == "VOIDLING_GENERATOR") speedUpgrade += 0.03 * tier

        fuel?.let { speedUpgrade += it.upgrade }

        cooldown /= (1 + speedUpgrade)

        val items = drops.mapValues { (_, dropValue) ->
            (1 / (2 * cooldown.inWholeMinutes).coerceAtLeast(1L)) * dropValue
        }.toMutableMap()

        fuel?.amountNeeded()?.let {
            items[fuel.id] = -it
        }

        val baseItemsProduced = items.values.sum()

        if (MinionUpgrade.KRAMPUS_HELMET in upgrades) {
            val produced = (baseItemsProduced * 0.045 / 100)
            items["RED_GIFT"] = if (id in krampusSpeedIncrease) produced * 4 else produced
        }

        if (MinionUpgrade.DIAMOND_SPREADING in upgrades) {
            items["DIAMOND"] = baseItemsProduced * 0.1
        }

        if (MinionUpgrade.POTATO_SPREADING in upgrades) {
            items["POTATO_ITEM"] = baseItemsProduced * 0.05
        }

        val soulflowUpgrades = setOf(MinionUpgrade.SOULFLOW_ENGINE, MinionUpgrade.LESSER_SOULFLOW_ENGINE)
        if (soulflowUpgrades.any { it in upgrades }) {
            items["RAW_SOULFLOW"] = 1.0 / 3
        }

        return items
    }

    fun getTotalProfitPerMinute(tier: Int, upgrades: List<MinionUpgrade>, fuel: MinionFuel?): Double {
        var totalProfit = 0.0

        for ((itemId, amount) in getBaseItemsPerMinute(tier, upgrades, fuel)) {
            val price = runCatching { SkyblockDataManager.getItem(itemId)?.getBestPrice() }.getOrNull() ?: 0.0
            totalProfit += price * amount
        }

        return totalProfit
    }

    fun costBreakdown(tier: Int, hours: Double, upgrades: List<MinionUpgrade>, fuel: MinionFuel?) = buildString {
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
            val price = SkyblockDataManager.getItem(itemId)?.getBestPrice()?.round(1) ?: 0.0

            val totalItemProfit = (amountPerMinute * 60 * hours).round(1)
            append("\n   ")
            append("§6x${totalItemProfit.formatNumber()} ")
            append("§6${SkyblockDataManager.getItem(itemId)?.name} ")
            append("§7for ${price.formatNumber()} coins each.")
        }
    }
}
