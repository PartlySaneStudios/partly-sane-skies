//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.render.gui.hud.cooldown

import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.components.Window
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.dsl.percent
import gg.essential.elementa.dsl.pixels
import gg.essential.universal.UMatrixStack
import me.partlysanestudios.partlysaneskies.render.gui.components.PSSHorizontalCooldown
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
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