//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.features.gui.hud

import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.components.Window
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.dsl.percent
import gg.essential.elementa.dsl.percentOfWindow
import gg.essential.elementa.dsl.pixels
import me.partlysanestudios.partlysaneskies.config.PSSHud
import me.partlysanestudios.partlysaneskies.render.gui.components.UIHorizontalCooldownElement
import me.partlysanestudios.partlysaneskies.render.gui.hud.cooldown.Cooldown
import me.partlysanestudios.partlysaneskies.render.gui.hud.cooldown.CooldownManager
import me.partlysanestudios.partlysaneskies.utils.MathUtils
import net.minecraft.init.Items
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

private const val cooldownsDisplayableAtOnce = 3
private const val defaultWidth = 50
private const val defaultHeight = 7
private const val defaultPadding = 120f
object CooldownHud: PSSHud(true, 960F - defaultWidth, 561.6F, 0, 1.0F) {
    private val exampleCooldowns = ArrayList<ExampleCooldown>()
    private val cooldownElements = ArrayList<UIHorizontalCooldownElement>()
    private val window = Window(ElementaVersion.V2)
    init {
        var previousCooldownElement = UIHorizontalCooldownElement(CenterConstraint(), 52f.percent, defaultWidth.pixels, defaultHeight.pixels)
            .setChildOf(window)

        cooldownElements.add(previousCooldownElement)
        for (i in 2..cooldownsDisplayableAtOnce) {
            val newCooldownElement = UIHorizontalCooldownElement(CenterConstraint(), defaultPadding.percent, defaultWidth.pixels, defaultHeight.pixels)
                .setChildOf(previousCooldownElement.boundingBox)
            previousCooldownElement = newCooldownElement
            cooldownElements.add(newCooldownElement)
        }
    }
    @SubscribeEvent
    fun onScreenRender(event: RenderGameOverlayEvent.Text) {
        cooldownElements[0].boundingBox.setX(x.pixels)
        cooldownElements[0].boundingBox.setY(y.pixels)
        cooldownElements[0].boundingBox.setWidth((defaultWidth * scale).pixels)
        cooldownElements[0].boundingBox.setHeight((defaultHeight * scale).pixels)

        for (i in 1..<cooldownsDisplayableAtOnce) {
            cooldownElements[i].setY((defaultPadding * scale).percent)
        }

        if (example) {
            if (exampleCooldowns.size < cooldownElements.size) {
                for (cooldownElement in cooldownElements) {
                    val exampleCooldown = ExampleCooldown()
                    exampleCooldowns.add(exampleCooldown)
                    exampleCooldown.startCooldown()
                }
            }

            for (i in 0..<exampleCooldowns.size) {

                val cooldown = exampleCooldowns[i]
                val cooldownElement = cooldownElements[i]
                cooldownElement.setCooldownToDisplay(cooldown)
            }
        } else {
            exampleCooldowns.clear()
            val activeCooldownList = CooldownManager.getActiveCooldowns()
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
        }

        for (cooldownElement in cooldownElements) {
            cooldownElement.tick()
        }
        window.draw(gg.essential.universal.UMatrixStack())
    }
    override fun getWidth(scale: Float, example: Boolean): Float {
        return defaultWidth * scale
    }

    override fun getHeight(scale: Float, example: Boolean): Float {
        val totalBarHeights = cooldownsDisplayableAtOnce * defaultHeight * scale
        val totalPadding = defaultPadding.percentOfWindow.value * scale * (cooldownsDisplayableAtOnce - 1)
        return totalBarHeights + totalPadding
    }

    class ExampleCooldown: Cooldown() {
        private val item: Item
        init {
            val itemKeys = Item.itemRegistry.keys.toTypedArray()
            val itemsLength = itemKeys.size
            val randomIndex = MathUtils.randInt(0, itemsLength - 1)
            item = Item.itemRegistry.getObjectById(randomIndex) ?: Items.bucket
        }
        override fun getTotalTime(): Long = 1000

        override fun getDisplayName(): String = ""

        override fun getItemToDisplay(): ItemStack = ItemStack(item)
    }
}