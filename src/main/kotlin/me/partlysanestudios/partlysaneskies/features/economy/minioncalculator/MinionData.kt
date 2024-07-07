package me.partlysanestudios.partlysaneskies.features.economy.minioncalculator

import com.google.gson.JsonParser
import me.partlysanestudios.partlysaneskies.data.pssdata.PublicDataManager
import me.partlysanestudios.partlysaneskies.events.SubscribePSSEvent
import me.partlysanestudios.partlysaneskies.events.data.LoadPublicDataEvent
import me.partlysanestudios.partlysaneskies.utils.SystemUtils.getJsonFromPath

object MinionData {

    private val minionMap = mutableMapOf<String, Minion>()

    internal val fuelMap = mutableMapOf<String, MinionFuel>()

    private const val MINIONS_DATA_URL = "constants/minion_data.json"

    fun getMostProfitMinionString(hours: Double, upgrades: List<MinionUpgrade>, fuel: MinionFuel?) = buildString {
        append("§7In §6$hours§7 hour(s): (Upgrade:${upgrades})")
        val mostProfitableMinions = getBestMinions(upgrades, fuel)

        mostProfitableMinions.forEachIndexed { i, (minion) ->
            append("\n\n§7${i+1}.${minion.costBreakdown(minion.maxTier, hours, upgrades, fuel)}")
        }
    }

    fun getBestMinions(upgrade: List<MinionUpgrade>, fuel: MinionFuel?): List<Pair<Minion, Double>> =
        minionMap.map { (_, minion) ->
            minion to minion.getTotalProfitPerMinute(minion.maxTier, upgrade, fuel)
        }.sortedByDescending { it.second }

    @SubscribePSSEvent
    fun init(event: LoadPublicDataEvent) {
        val file = PublicDataManager.getFile(MINIONS_DATA_URL)
        val jsonObj = JsonParser().parse(file).asJsonObject

        val minionObjects = jsonObj.getJsonFromPath("/minions")?.asJsonObject ?: return

        for ((id, minionElement) in minionObjects.entrySet()) {
            val minionObj = minionElement.asJsonObject
            val minion = Minion(id, minionObj)
            minionMap[id] = minion
        }

        val fuelObjects = jsonObj.getJsonFromPath("/fuels")?.asJsonObject ?: return
        for ((id, fuelElement) in fuelObjects.entrySet()) {
            val fuelObj = fuelElement.asJsonObject
            val fuel = MinionFuel(id, fuelObj)
            fuelMap[id] = fuel
        }
    }
}
