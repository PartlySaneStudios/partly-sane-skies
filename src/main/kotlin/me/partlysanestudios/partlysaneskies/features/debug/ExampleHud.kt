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
import me.partlysanestudios.partlysaneskies.features.debug.ExampleHudLogic.DEFAULT_COLOR
import me.partlysanestudios.partlysaneskies.features.debug.ExampleHudLogic.DEFAULT_HEIGHT
import me.partlysanestudios.partlysaneskies.features.debug.ExampleHudLogic.DEFAULT_WIDTH
import me.partlysanestudios.partlysaneskies.features.debug.ExampleHudLogic.EXAMPLE_COLOR
import me.partlysanestudios.partlysaneskies.features.debug.ExampleHudLogic.box
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.awt.Color

class ExampleHud: Hud(false, 50.0F, 50.0F) {
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
        return scale * 5
    }

    override fun getHeight(scale: Float, example: Boolean): Float {
        return scale * 5
    }

    companion object {
        @SubscribeEvent
        fun onScreenRender(event: RenderGameOverlayEvent.Text) {
            ExampleHudLogic.window.draw(gg.essential.universal.UMatrixStack())
        }
    }
}
//    Because of the way OneConfig works, I don't think I can store the Elementa objects/other complex objects in any Hud Class
private object ExampleHudLogic {
    const val DEFAULT_WIDTH = 5
    const val DEFAULT_HEIGHT = 5
    val EXAMPLE_COLOR = Color.red
    val DEFAULT_COLOR = Color.black

    val window = Window(ElementaVersion.V2)

    val box = UIBlock()
        .constrain {
            x = 50.pixels
            y = 50.pixels
            height = DEFAULT_HEIGHT.pixels
            width = DEFAULT_WIDTH.pixels
            color = DEFAULT_COLOR.constraint
        } childOf window
}