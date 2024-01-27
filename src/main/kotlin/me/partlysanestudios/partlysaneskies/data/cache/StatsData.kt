package me.partlysanestudios.partlysaneskies.data.cache

import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object StatsData {

    val currentHealth: Double get() = cachedCurrentHealth
    val maxHealth: Double get() = cachedMaxHealth
    val defense: Double get() = cachedCurrentDefense
    val currentMana: Double get() = cachedCurrentMana
    val maxMana: Double get() = cachedMaxMana



    private var cachedCurrentHealth = -1.0
    private var cachedMaxHealth = -1.0
    private var cachedCurrentDefense = -1.0
    private var cachedCurrentMana = -1.0
    private var cachedMaxMana = -1.0

    @SubscribeEvent
    fun onChatMessage(event: ClientChatReceivedEvent) {
        if (event.type != 2.toByte()) {
            return
        }

        val message = event.message.formattedText
        parseDefenseFromString(message)
        parseHealthFromString(message)
        parseManaFromString(message)
    }




    private fun parseHealthFromString(actionBarMessage: String) {

        val healthRegex = "§(\\w)([\\d,]+)\\/([\\d,]+)❤".toRegex()

        if (!healthRegex.containsMatchIn(actionBarMessage)) {
            return
        }
        val matchResult = healthRegex.find(actionBarMessage) ?: return

        val (_, health, max) = matchResult.destructured

        this.cachedCurrentHealth = health.replace(",", "").toDoubleOrNull() ?: cachedCurrentHealth
        this.cachedMaxHealth = max.replace(",", "").toDoubleOrNull() ?: cachedMaxHealth
    }

    private fun parseManaFromString(actionBarMessage: String) {

        val manaRegex = "§(\\w)([\\d,]+)\\/([\\d,]+)✎ Mana".toRegex()

        if (!manaRegex.containsMatchIn(actionBarMessage)) {
            return
        }
        val matchResult = manaRegex.find(actionBarMessage) ?: return

        val (_, current, max) = matchResult.destructured

        this.cachedCurrentMana = current.replace(",", "").toDoubleOrNull() ?: cachedCurrentMana
        this.cachedMaxMana = max.replace(",", "").toDoubleOrNull() ?: cachedMaxMana
    }

    private fun parseDefenseFromString(actionBarMessage: String) {

        val healthRegex = "§(\\w)([\\d,]+)§a❈ Defense".toRegex()

        if (!healthRegex.containsMatchIn(actionBarMessage)) {
            return
        }
        val matchResult = healthRegex.find(actionBarMessage) ?: return

        val (_, current) = matchResult.destructured

        this.cachedCurrentDefense = current.replace(",", "").toDoubleOrNull() ?: cachedCurrentDefense
    }
}