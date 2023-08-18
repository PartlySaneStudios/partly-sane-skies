package me.partlysanestudios.partlysaneskies.system.guicomponents

import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.components.UICircle
import gg.essential.elementa.components.UIImage
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.pixels
import gg.essential.universal.UMatrixStack
import gg.essential.universal.UMouse
import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.garden.endoffarmnotifer.Range3d.Point2d
import me.partlysanestudios.partlysaneskies.system.ThemeManager
import me.partlysanestudios.partlysaneskies.utils.Utils
import net.minecraft.client.renderer.texture.TextureUtil
import net.minecraft.util.ResourceLocation
import org.jetbrains.kotlin.gradle.utils.toSetOrEmpty
import java.awt.Color
import java.awt.image.BufferedImage
import java.util.*
import java.util.concurrent.CompletableFuture
import kotlin.math.cos
import kotlin.math.sin

class PSSQuickActionMenu(val actions: Array<PSSQuickAction>, val key: Int): WindowScreen(ElementaVersion.V2) {

    private var innerCircle: UICircle
    private val STANDARD_COLOR = Color(50, 50, 50, 80)
    init {

        val radius = if (window.getHeight().coerceAtMost(window.getWidth()) == window.getWidth()) {
            window.getWidth() * 1f / 3f
        } else {
            window.getHeight() * 2f / 5f
        }

        innerCircle = UICircle(
            radius = radius,
            color = STANDARD_COLOR
        ).constrain {
            x = CenterConstraint()
            y = CenterConstraint()
        } childOf window

        generateSections()
    }



    private fun generateSections() {
        val radius = innerCircle.getRadius()
        val centerX = innerCircle.getWidth() + radius
        val centerY = innerCircle.getHeight() + radius

        val angleDivision = 360f/actions.size

        val startingAngle = 90f

        var angle = startingAngle

        val actionRadius = Point2d(
                sin(Math.toRadians(startingAngle.toDouble())) * radius / 2f,
                cos(Math.toRadians(startingAngle.toDouble())) * radius / 2f
            ).getDistanceTo(Point2d(
                sin(Math.toRadians(startingAngle + angle.toDouble())) * radius / 2f,
                cos(Math.toRadians(startingAngle + angle.toDouble())) * radius / 2f
            )).toFloat() / 3f

        for (action in actions) {
            val actionX = sin(Math.toRadians(angle.toDouble())) * radius / 2f
            val actionY = cos(Math.toRadians(angle.toDouble())) * radius / 2f

            Utils.visPrint("New Action at $actionX, $actionY with a radius of $actionRadius")

            action.uiComponent = UICircle(actionRadius, color = STANDARD_COLOR) .constrain {
                x = (actionX + radius).pixels
                y = (actionY + radius).pixels
            }.onMouseClick {
                action.runnable.run()
            }
            as UICircle childOf innerCircle

            val imageWidth = action.image.width
            val imageHeight = action.image.height

            Utils.visPrint("Image width $imageWidth, Image Height $imageHeight")
            val widthHeightRation = imageWidth / imageHeight

            val uiImageWidth: Float
            val uiImageHeight: Float

            if (imageWidth == imageWidth.coerceAtLeast(imageHeight)) {
                uiImageWidth = action.uiComponent.getRadius() * .8f
                uiImageHeight = (1 / widthHeightRation) * uiImageWidth
            } else {
                uiImageHeight = action.uiComponent.getRadius() * .8f
                uiImageWidth = widthHeightRation * uiImageHeight
            }
            val image = UIImage(CompletableFuture.supplyAsync {
                action.image
            }).constrain {
                x = CenterConstraint()
                y = CenterConstraint()

                width = uiImageWidth.pixels
                height = uiImageHeight.pixels

            } childOf action.uiComponent

            action.position = Point2d((action.uiComponent.getLeft() + action.uiComponent.getRight()) / 2.0, (action.uiComponent.getTop() + action.uiComponent.getBottom()) / 2.0)
            angle -= angleDivision
        }
    }

    override fun onDrawScreen(matrixStack: UMatrixStack, mouseX: Int, mouseY: Int, partialTicks: Float) {
        super.onDrawScreen(matrixStack, mouseX, mouseY, partialTicks)

        val mousePoint = Point2d(UMouse.Scaled.x, UMouse.Scaled.y)
        var closestToMouse = actions[0]
        for (action in actions) {
            if (action.position.getDistanceTo(mousePoint) < closestToMouse.position.getDistanceTo(mousePoint)) {
                closestToMouse = action
            }
            action.uiComponent.setColor(STANDARD_COLOR)
        }

        closestToMouse.uiComponent.setColor(ThemeManager.getAccentColor().toJavaColor())
    }




    class PSSQuickAction(val id: String, val runnable: Runnable, val image: BufferedImage) {
        constructor(
            id: String,
            runnable: Runnable,
            image: ResourceLocation
        ): this(
            id,
            runnable,
            image = TextureUtil.readBufferedImage(PartlySaneSkies.minecraft.resourceManager.getResource(image).inputStream)
        )

        var uiComponent: UICircle = UICircle()
        var position: Point2d = Point2d(0.0, 0.0)

    }


}