package me.partlysanestudios.partlysaneskies.features.gui

import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.UIComponent
import gg.essential.elementa.components.Window
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.dsl.childOf
import gg.essential.universal.UMatrixStack
import me.partlysanestudios.partlysaneskies.features.farming.garden.CompostValue
import me.partlysanestudios.partlysaneskies.render.gui.constraints.ScaledPixelConstraint.Companion.scaledPixels
import net.minecraftforge.client.event.GuiScreenEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

abstract class SidePanel {
    abstract val component: UIComponent

    internal val window = Window(ElementaVersion.V5)

    init {
        component childOf window
    }


    @SubscribeEvent
    open fun onScreenRender(event: GuiScreenEvent.BackgroundDrawnEvent) {
        if (!shouldDisplayPanel()) {
            component.hide()
            return
        }

        component.unhide()
        component.setWidth(700.scaledPixels)
            .setY(CenterConstraint())

        frameUpdate(event)

        window.draw(UMatrixStack())


    }

    // This is called only when the shouldDisplayPanel is true
    open fun frameUpdate(event: GuiScreenEvent.BackgroundDrawnEvent) {}

    abstract fun shouldDisplayPanel(): Boolean
}