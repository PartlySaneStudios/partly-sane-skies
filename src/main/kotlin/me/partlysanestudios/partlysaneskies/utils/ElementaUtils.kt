//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.utils

import gg.essential.elementa.UIComponent
import gg.essential.elementa.components.UIImage
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.PixelConstraint
import me.partlysanestudios.partlysaneskies.features.themes.ThemeManager
import net.minecraft.client.Minecraft
import net.minecraft.util.ResourceLocation
import java.awt.Color
import java.io.IOException
import java.util.concurrent.CompletableFuture
import javax.imageio.ImageIO

object ElementaUtils {
    fun ResourceLocation.uiImageFromResourceLocation(): UIImage {
        return try {
            val resource = Minecraft.getMinecraft().resourceManager.getResource(this)
            UIImage(CompletableFuture.supplyAsync {
                try {
                    return@supplyAsync ImageIO.read(resource.inputStream)
                } catch (e: IOException) {
                    e.printStackTrace()
                } finally {
                    try {
                        resource.inputStream.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
                null
            })
        } catch (exception: NullPointerException) {
            UIImage.ofResource("/assets/partlysaneskies/" + this.resourcePath)
            //            return UIImage.ofResource("/assets/partlysaneskies/textures/null_texture.png");
        } catch (exception: IOException) {
            UIImage.ofResource("/assets/partlysaneskies/" + this.resourcePath)
        }
    }

    fun UIComponent.applyBackground() {
        val image = ThemeManager.getCurrentBackgroundUIImage()
            .setX(CenterConstraint())
            .setY(CenterConstraint())
            .setWidth(PixelConstraint(this.getWidth()))
            .setHeight(PixelConstraint(this.getHeight())) as UIImage
        this.insertChildAt(image, 0)
    }

    fun Color.weightedAverage(thisColorWeight: Float, otherColor: Color, otherColorWeight: Float): Color {
        val totalWeight = thisColorWeight + otherColorWeight
        val thisColorPercent = thisColorWeight/totalWeight
        val otherColorPercent = otherColorWeight/totalWeight

        var finalR = this.red * thisColorPercent + otherColor.red * otherColorPercent
        var finalG = this.green * thisColorPercent + otherColor.green * otherColorPercent
        var finalB = this.blue * thisColorPercent + otherColor.blue * otherColorPercent
        var finalA = this.alpha * thisColorPercent + otherColor.alpha * otherColorPercent


        if (finalR > 255) {
            finalR = 255f
        }
        if (finalG > 255) {
            finalG = 255f
        }
        if (finalB > 255) {
            finalB = 255f
        }
        if (finalA > 255) {
            finalA = 255f
        }


        if (finalR < 0) {
            finalR = 0f
        }
        if (finalG < 0) {
            finalG = 0f
        }
        if (finalB < 0) {
            finalB = 0f
        }
        if (finalA < 0) {
            finalA = 0f
        }



        return Color(finalR/255f, finalG/255f, finalB/255f, finalA/255f)

    }
}