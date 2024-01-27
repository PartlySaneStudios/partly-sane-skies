package me.partlysanestudios.partlysaneskies.features.debug

import cc.polyfrost.oneconfig.hud.Hud
import cc.polyfrost.oneconfig.libs.universal.UMatrixStack
import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.Window
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.constraint
import gg.essential.elementa.dsl.pixels
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.awt.Color

object ExampleHud: Hud(false, 50.0F, 50.0F) {
    private const val DEFAULT_WIDTH = 5
    private const val DEFAULT_HEIGHT = 5
    private val EXAMPLE_COLOR = Color.red
    private val DEFAULT_COLOR = Color.black

    val window = Window(ElementaVersion.V2)

    val box = UIBlock()
        .constrain {
            x = 0.pixels
            y = 0.pixels
            height = DEFAULT_HEIGHT.pixels
            width = DEFAULT_WIDTH.pixels
            color = DEFAULT_COLOR.constraint
        } childOf window
    override fun draw(matrices: UMatrixStack?, x: Float, y: Float, scale: Float, example: Boolean) {
        box.setX(x.pixels)
        box.setY(y.pixels)
        box.setHeight((scale * DEFAULT_HEIGHT).pixels)
        box.setWidth((scale * DEFAULT_WIDTH).pixels)
        val color = if (example) {
            EXAMPLE_COLOR
        } else {
            DEFAULT_COLOR
        }
        box.setColor(color)
    }

    override fun getWidth(scale: Float, example: Boolean): Float {
        return scale * DEFAULT_WIDTH
    }

    override fun getHeight(scale: Float, example: Boolean): Float {
        return scale * DEFAULT_HEIGHT
    }

    @SubscribeEvent
    fun onScreenRender(event: RenderGameOverlayEvent.Text) {
        window.draw(gg.essential.universal.UMatrixStack())
    }
}