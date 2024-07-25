//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.utils

import cc.polyfrost.oneconfig.config.core.OneColor
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import java.nio.file.Path
import javax.imageio.ImageIO

object ImageUtils {
    //    Save all images in ./config/partly-sane-skies/image_variants/{name of base image}/{name of coloured image}
    private const val IMAGE_SAVE_PATH = "./config/partly-sane-skies/image_variants/"


    @Throws(IOException::class)
    fun saveImage(image: BufferedImage, path: Path) {
        val output = path.toFile()
        ImageIO.write(image, "PNG", output)
    }

    @Throws(IOException::class)
    fun loadImage(path: String): BufferedImage {
        return ImageIO.read(File(path))
    }

    fun replaceColor(image: BufferedImage, oldColor: Color, newColor: Color): BufferedImage {
        return replaceColor(
            image, OneColor(oldColor), OneColor(
                newColor
            )
        )
    }

    fun replaceColor(image: BufferedImage, oldColor: OneColor, newColor: Color): BufferedImage {
        return replaceColor(image, oldColor, OneColor(newColor))
    }

    fun replaceColor(image: BufferedImage, oldColor: Color, newColor: OneColor): BufferedImage {
        return replaceColor(image, OneColor(oldColor), newColor)
    }

    fun replaceColor(image: BufferedImage, oldColor: OneColor, newColor: OneColor): BufferedImage {
        val width = image.width
        val height = image.height
        for (x in 0 until width) {
            for (y in 0 until height) {
                val intColor = image.getRGB(x, y)
                val pixelColor = Color(intColor)
                if (pixelColor == oldColor.toJavaColor()) {
                    image.setRGB(x, y, newColor.toJavaColor().rgb)
                }
            }
        }
        return image
    }

    fun Color.applyOpacity(opacity: Int): Color {
        return Color(this.red, this.green, this.blue, opacity)
    }

    val Color.asHex: Int get() {
        // Get RGB components and combine them
        val red = this.red
        val green = this.green
        val blue = this.blue

        // Combine into a single integer (shift and bitwise OR)
        return red shl 16 or (green shl 8) or blue
    }

    operator fun Color.plus(color: Color): Color {
        val red = if (this.red + color.red > 255) {
            0
        } else {
            this.red + color.red
        }
        val blue = if (this.blue + color.blue > 255) {
            0
        } else {
            this.blue + color.blue
        }
        val green = if (this.green + color.green > 255) {
            0
        } else {
            this.green + color.green
        }

        return Color(red, green, blue)
    }

    operator fun Color.minus(color: Color): Color {
        val red = if (this.red - color.red < 0) {
            0
        } else {
            this.red - color.red
        }
        val blue = if (this.blue - color.blue < 0) {
            0
        } else {
            this.blue - color.blue
        }
        val green = if (this.green - color.green < 0) {
            0
        } else {
            this.green - color.green
        }

        return Color(red, green, blue)
    }
}
