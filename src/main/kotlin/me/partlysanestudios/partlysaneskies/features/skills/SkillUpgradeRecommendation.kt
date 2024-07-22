/*
 * Written by Su386.
 * See LICENSE for copyright and license notices.
 */

package me.partlysanestudios.partlysaneskies.features.skills

import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.minecraft
import me.partlysanestudios.partlysaneskies.commands.PSSCommand
import me.partlysanestudios.partlysaneskies.data.skyblockdata.SkyblockDataManager.getPlayer
import me.partlysanestudios.partlysaneskies.data.skyblockdata.SkyblockDataManager.getSkill
import me.partlysanestudios.partlysaneskies.data.skyblockdata.SkyblockPlayer
import me.partlysanestudios.partlysaneskies.utils.ChatUtils.sendClientMessage
import me.partlysanestudios.partlysaneskies.utils.MathUtils.round
import me.partlysanestudios.partlysaneskies.utils.StringUtils.titleCase
import java.io.IOException
import java.util.Locale
import kotlin.math.ceil
import kotlin.math.pow


object SkillUpgradeRecommendation {

    private val weightConstants = mapOf(
        "mining" to 1.68207448,
        "foraging" to 1.732826,
        "enchanting" to 1.46976583,
        "combat" to 1.65797687265,
        "fishing" to 1.906418,
        "alchemy" to 1.5,
        "farming" to 1.717848139,
    )

    private fun getRecommendedSkills(username: String?): LinkedHashMap<String, Double> {
        val skillScoreMap = hashMapOf<String, Double>()
        val player = getPlayer(username!!)

        weightConstants.keys.forEach { skill ->
            val skillLevel = getSkillLevel(skill, player)
            val maxSkillLevel = getSkill(skill.uppercase(Locale.getDefault()))?.maxLevel?.toDouble() ?: 0.0

            if (skillLevel < maxSkillLevel) {
                skillScoreMap[skill] = if (skillLevel < 5) 100000.0 else calculateScore(skill, player)
            }
        }

        val catacombsLevel = player.catacombsLevel
        val maxCatacombsLevel = 50.0

        if (catacombsLevel < maxCatacombsLevel) {
            skillScoreMap["catacombs"] = if (catacombsLevel < 5) {
                100000.0
            } else {
                val weightDiff = calculateCatacombsWeight(catacombsLevel) - calculateCatacombsWeight(ceil(catacombsLevel))
                (maxCatacombsLevel - catacombsLevel) / weightDiff * 1.10 + 10
            }
        }

        return skillScoreMap.entries
            .sortedBy { it.value }
            .associateTo(LinkedHashMap()) { it.key to it.value }
    }


    private fun getSkillLevel(skill: String, player: SkyblockPlayer) = when (skill) {
        "mining" -> player.miningLevel
        "foraging" -> player.foragingLevel
        "enchanting" -> player.enchantingLevel
        "combat" -> player.combatLevel
        "fishing" -> player.fishingLevel
        "alchemy" -> player.alchemyLevel
        "farming" -> player.farmingLevel
        else -> -1.0
    }

    private fun printMessage(map: HashMap<String, Double>) {
        val message = StringBuilder(
            """
             §3§m-----------------------------------------------------§r
             §b§l§nRecommended skills to level up (In Order):§r
             
             §7This calculation is based off of the amount of weight each skill will add when you level it up. Lower level skills will be prioritized.§r
             §7§oNote: Sometimes, low level skills such as alchemy will show up first. These skills are less important but due to the mathematical approach, they will appear first. 
             
             
             §8(Skill) : (Upgrade Importance Score)
             
             """.trimIndent(),
        )

        for (entry in map.entries.reversed()) {
            message.append("\n${entry.key.titleCase()} : ${entry.value.round(2)}")
        }

        message.append("\n§3§m-----------------------------------------------------§r")

        sendClientMessage((message.toString()))
    }

    fun registerCommand() {
        PSSCommand("skillup")
            .addAlias("skillu", "su")
            .setDescription("Recommends which skill to upgrade: /skillup [username]")
            .setRunnable { args: Array<String> ->
                sendClientMessage("Loading...")

                val task = Runnable {
                    val username = if (args.isNotEmpty()) args[0] else minecraft.thePlayer.name
                    val skillMap: HashMap<String, Double> = try {
                        getRecommendedSkills(username)
                    } catch (e: IOException) {
                        sendClientMessage("Error getting data for $username. Maybe the player is nicked or there is an invalid API key.")
                        return@Runnable
                    }

                    minecraft.addScheduledTask {
                        printMessage(skillMap)
                    }
                }

                Thread(task).start()
            }.register()
    }

    private fun calculateSkillWeight(level: Double, constant: Double) = (level * 10).pow(constant + level / 100) / 1250

    private fun calculateCatacombsWeight(level: Double) = level.pow(4.5) * 0.0002149604615

    private fun calculateScore(skill: String, player: SkyblockPlayer): Double {
        val currentSkillLevel = getSkillLevel(skill, player)

        val weightConstant: Double = weightConstants[skill] ?: 1.0

        val awayFromMaxComponent = getSkillLevel(skill, player) - getSkill(skill.uppercase(Locale.getDefault()))!!.maxLevel
        val currentSenitherWeight = calculateSkillWeight(currentSkillLevel - 5, weightConstant)
        val nextLevelSenitherWeight = calculateSkillWeight(ceil(currentSkillLevel - 5), weightConstant)
        val levelUpSenitherWeightComponent = currentSenitherWeight - nextLevelSenitherWeight

        return awayFromMaxComponent / levelUpSenitherWeightComponent + 10
    }

}
