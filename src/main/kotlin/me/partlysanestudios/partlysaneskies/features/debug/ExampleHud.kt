//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.features.debug

import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.Window
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.constraint
import gg.essential.elementa.dsl.pixels
import me.partlysanestudios.partlysaneskies.config.PSSHud
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.awt.Color

object ExampleHud : PSSHud(false, 50.0F, 50.0F) {
    private const val DEFAULT_WIDTH = 5
    private const val DEFAULT_HEIGHT = 5
    private val EXAMPLE_COLOR = Color.red
    private val DEFAULT_COLOR = Color.black

    private val window = Window(ElementaVersion.V2)

    private val box =
        UIBlock()
            .constrain {
                x = 50.pixels
                y = 50.pixels
                height = DEFAULT_HEIGHT.pixels
                width = DEFAULT_WIDTH.pixels
                color = DEFAULT_COLOR.constraint
            } childOf window

    @SubscribeEvent
    fun onScreenRender(event: RenderGameOverlayEvent.Text) {
        if (!this.enabled) {
            return
        }

        box.setX(x.pixels)
        box.setY(y.pixels)
        box.setHeight((scale * DEFAULT_HEIGHT).pixels)
        box.setWidth((scale * DEFAULT_WIDTH).pixels)
        val color =
            if (example) {
                EXAMPLE_COLOR
            } else {
                DEFAULT_COLOR
            }
        box.setColor(color)
        window.draw(gg.essential.universal.UMatrixStack())
    }

    override fun getWidth(scale: Float, example: Boolean): Float = scale * 5

    override fun getHeight(scale: Float, example: Boolean): Float = scale * 5
}
