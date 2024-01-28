package me.partlysanestudios.partlysaneskies.features.gui.hud

import cc.polyfrost.oneconfig.hud.Hud
import cc.polyfrost.oneconfig.libs.universal.UMatrixStack
import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.components.Window
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.dsl.percent
import gg.essential.elementa.dsl.pixels
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
class CooldownHud: Hud(true, 540.0F, 561.6F, 0, 1.0F) {
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
    override fun draw(matrices: UMatrixStack?, x: Float, y: Float, scale: Float, example: Boolean) {
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
    }
    override fun getWidth(scale: Float, example: Boolean): Float {
        return defaultWidth * scale
    }

    override fun getHeight(scale: Float, example: Boolean): Float {
        if (cooldownElements.size < cooldownsDisplayableAtOnce) {
            return 0.0F
        }
        val exampleCooldowns = ArrayList<ExampleCooldown>()

        val topBoundingBoxY = cooldownElements?.get(0)?.boundingBox?.getTop() ?: 0.0F // Yes the safe calls are necessary because OneConfig is weird
        val bottomBoundingBoxY = cooldownElements?.get(cooldownsDisplayableAtOnce - 1)?.boundingBox?.getBottom() ?: 0.0F // Yes the safe calls are necessary because OneConfig is weird
        return bottomBoundingBoxY - topBoundingBoxY
    }

    internal companion object {
        @Transient
        val exampleCooldowns = ArrayList<ExampleCooldown>()
        @Transient
        val cooldownElements = ArrayList<UIHorizontalCooldownElement>()
        @Transient
        val window = Window(ElementaVersion.V2)
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
        @SubscribeEvent
        fun onScreenRender(event: RenderGameOverlayEvent.Text) {
            window.draw(gg.essential.universal.UMatrixStack())
        }
    }
}
//    Because of the way OneConfig works, I don't think I can store the Elementa objects/other complex objects in the Hud Class
private object ElementaObjects {

}