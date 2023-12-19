//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.utils

import gg.essential.elementa.UIComponent
import gg.essential.elementa.components.UIImage
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.PixelConstraint
import me.partlysanestudios.partlysaneskies.system.ThemeManager
import net.minecraft.client.Minecraft
import net.minecraft.util.ResourceLocation
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
}