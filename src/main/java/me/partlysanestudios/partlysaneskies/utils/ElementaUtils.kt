package me.partlysanestudios.partlysaneskies.utils

import gg.essential.elementa.components.UIImage
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
}