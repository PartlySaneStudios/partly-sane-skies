//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.gui.hud.cooldown

import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.components.Window
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.dsl.percent
import gg.essential.elementa.dsl.pixels
import gg.essential.universal.UMatrixStack
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.util.*

object CooldownManager {
    val cooldownsDisplayableAtOnce = 3

    val window = Window(ElementaVersion.V2)

    val cooldowns = ArrayList<Cooldown>()
    val cooldownElements = ArrayList<UIHorizontalCooldownElement>()

    fun init() {
        var previousCooldownElement = UIHorizontalCooldownElement(CenterConstraint(), 55f.percent, 50f.pixels, 7f.pixels).setChildOf(window)

        cooldownElements.add(previousCooldownElement)
        for (i in 2..cooldownsDisplayableAtOnce) {
            val newCooldownElement = UIHorizontalCooldownElement(CenterConstraint(), 120f.percent, 50f.pixels, 7f.pixels).setChildOf(previousCooldownElement.boundingBox)
            previousCooldownElement = newCooldownElement
            cooldownElements.add(newCooldownElement)
        }
    }

    fun getActiveCooldowns(): List<Cooldown> {
        val activeCooldowns = LinkedList<Cooldown>()

        for (cooldown in cooldowns) {
            if (cooldown.isCooldownActive()) {
                activeCooldowns.add(cooldown)
            }
        }

        return activeCooldowns
    }

    @SubscribeEvent
    fun onScreenRender(event: RenderGameOverlayEvent.Text)  {
        val activeCooldownList = getActiveCooldowns()
        val sortedActiveCooldownsList = activeCooldownList.sortedBy {
            it.getTimeRemaining()
        }

        val cooldownsToDisplay = if (sortedActiveCooldownsList.size > cooldownsDisplayableAtOnce) {
            cooldownsDisplayableAtOnce
        } else {
            sortedActiveCooldownsList.size
        }

        for (cooldownElement in cooldownElements) {
            cooldownElement.setCooldownToDisplay(null)
        }

        for (i in 0..<cooldownsToDisplay) {
            cooldownElements[i].setCooldownToDisplay(sortedActiveCooldownsList[i])
        }


        for (cooldownElemet in cooldownElements) {
            cooldownElemet.tick()
        }


        window.draw(UMatrixStack())
    }

}