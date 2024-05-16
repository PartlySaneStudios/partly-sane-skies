//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

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
import me.partlysanestudios.partlysaneskies.render.gui.constraints.ScaledPixelConstraint.Companion.scaledPixels
import me.partlysanestudios.partlysaneskies.utils.ElementaUtils
import me.partlysanestudios.partlysaneskies.utils.MinecraftUtils
import me.partlysanestudios.partlysaneskies.utils.MinecraftUtils.xSize
import net.minecraft.client.gui.inventory.GuiChest
import net.minecraftforge.client.event.GuiScreenEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.apache.commons.lang3.reflect.FieldUtils

/**
 * Basic implementation of a GUI rendering on the side of a chest.
 */
abstract class SidePanel {
    /**
     * The base of the panel. This is aligned, shown, hidden and moved by the [onScreenRender], and [alignPanel] functions
     */
    abstract val panelBaseComponent: UIComponent

    /**
     * The distance between the edge of the chest Gui and the edge of the panel.
     * Used by the [alignPanel] function.
     */
    open val pad: XConstraint = 10.scaledPixels

    private var parented = false
    internal val window = Window(ElementaVersion.V5)

    /**
     * Basic render code. Can be overridden for custom implementaions.
     * @param event an event of type [GuiScreenEvent.BackgroundDrawnEvent] passed by the Forge EventBus.
     */
    @SubscribeEvent
    open fun onScreenRender(event: GuiScreenEvent.BackgroundDrawnEvent) {
        if (!parented) {
            panelBaseComponent childOf window
            parented = true
        }
        if (!shouldDisplayPanel()) {
            panelBaseComponent.hide()
            return
        }

        panelBaseComponent.unhide()

        onPanelRender(event)

        window.draw(UMatrixStack())
    }

    /**
     * This function can be overriden to add functions to run every frame the panel is rendered.
     * This function is empty; no super class are required.
     * This function is only run when [shouldDisplayPanel] returns true.
     */
    open fun onPanelRender(event: GuiScreenEvent.BackgroundDrawnEvent) {}

    /**
     * This function automatically aligns the [panelBaseComponent] panel to be [pad] distance from the edge of the chest.
     * This does not rescale when the gui is rescaled and should be called regularly.
     * This function is not natively called.
     */
    open fun alignPanel() {
        if (shouldDisplayPanel()) {
            val currentScreen = PartlySaneSkies.minecraft.currentScreen as GuiChest
            val xSize = currentScreen.xSize
            val xCoord = (ElementaUtils.windowWidth / 2 + xSize / 2).pixels + pad

            panelBaseComponent.setX(xCoord)
        }
    }

    /**
     * Called every time the checks for display
     * @return Whether the panel should be is rendered
     */
    abstract fun shouldDisplayPanel(): Boolean
}