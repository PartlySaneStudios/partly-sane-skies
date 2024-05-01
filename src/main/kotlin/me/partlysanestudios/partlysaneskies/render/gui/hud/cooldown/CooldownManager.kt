//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.render.gui.hud.cooldown

import java.util.*

object CooldownManager {

    private val cooldowns = ArrayList<Cooldown>()

    fun getActiveCooldowns(): List<Cooldown> {
        val activeCooldowns = LinkedList<Cooldown>()

        for (cooldown in cooldowns) {
            if (cooldown.isCooldownActive()) {
                activeCooldowns.add(cooldown)
            }
        }

        return activeCooldowns
    }

    fun registerCooldown(cooldown: Cooldown) {
        cooldowns.add(cooldown)
    }
}