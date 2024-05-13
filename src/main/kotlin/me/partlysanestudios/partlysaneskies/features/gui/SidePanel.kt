package me.partlysanestudios.partlysaneskies.features.gui

import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.UIComponent
import gg.essential.elementa.components.Window
import gg.essential.elementa.constraints.XConstraint
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.pixels
import gg.essential.elementa.dsl.plus
import gg.essential.universal.UMatrixStack
import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.features.farming.garden.CompostValue
import me.partlysanestudios.partlysaneskies.render.gui.constraints.ScaledPixelConstraint.Companion.scaledPixels
import me.partlysanestudios.partlysaneskies.utils.ElementaUtils
import me.partlysanestudios.partlysaneskies.utils.MinecraftUtils
import net.minecraft.client.gui.inventory.GuiChest
import net.minecraftforge.client.event.GuiScreenEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.apache.commons.lang3.reflect.FieldUtils

abstract class SidePanel {
    abstract val component: UIComponent

    internal val window = Window(ElementaVersion.V5)
    open val pad: XConstraint = 10.scaledPixels

    private var parented = false

    init {
    }


    @SubscribeEvent
    open fun onScreenRender(event: GuiScreenEvent.BackgroundDrawnEvent) {
        if (!parented) {
            component childOf window
            parented = true
        }
        if (!shouldDisplayPanel()) {
            component.hide()
            return
        }

        component.unhide()

        frameUpdate(event)

        window.draw(UMatrixStack())
    }

    // This is called only when the shouldDisplayPanel is true
    open fun frameUpdate(event: GuiScreenEvent.BackgroundDrawnEvent) {}


    // This is never called. You must call this to align
    open fun alignPanel() {
        if (shouldDisplayPanel()) {
            val currentScreen = PartlySaneSkies.minecraft.currentScreen as GuiChest
            val xSize = FieldUtils.readField(currentScreen, MinecraftUtils.getDecodedFieldName("xSize"), true) as Int
            val xCoord = (ElementaUtils.windowWidth / 2 + xSize / 2).pixels + pad

            component.setX(xCoord)
        }
    }

    abstract fun shouldDisplayPanel(): Boolean
}