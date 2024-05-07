//
// Written by Su386.
// See LICENSE for copyright and license notices.
//
package me.partlysanestudios.partlysaneskies.features.themes

import cc.polyfrost.oneconfig.config.core.OneColor
import gg.essential.elementa.components.UIImage
import gg.essential.universal.utils.ReleasedDynamicTexture
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.config
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.minecraft
import me.partlysanestudios.partlysaneskies.utils.ElementaUtils.uiImageFromResourceLocation
import me.partlysanestudios.partlysaneskies.utils.ImageUtils.loadImage
import me.partlysanestudios.partlysaneskies.utils.ImageUtils.replaceColor
import me.partlysanestudios.partlysaneskies.utils.ImageUtils.saveImage
import net.minecraft.client.renderer.texture.TextureUtil
import net.minecraft.client.resources.IResource
import net.minecraft.util.ResourceLocation
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

object ThemeManager {
    private val PRIMARY_DEBUG_COLOR: Color = Color(255, 0, 0)
    private val SECONDARY_DEBUG_COLOR: Color = Color(0, 0, 255)
    private val ACCENT_DEBUG_COLOR: Color = Color(0, 255, 0)
    private var lastThemeName: String = ""
    private val backgroundUIImages: ArrayList<UIImage> = ArrayList()
    private val buttonDataList: ArrayList<ButtonData> = ArrayList()
    private val toggleDataList: ArrayList<ToggleData> = ArrayList()
    private val defaultThemes: Array<Theme> = arrayOf(
        Theme("Classic (Dark)", Color(46, 47, 50), Color(37, 38, 41), Color(1, 255, 255)),
        Theme("Royal Dark (Dark)", Color(46, 47, 50), Color(39, 31, 43), Color(91, 192, 235)),
        Theme("Midnight Forest (Dark)", Color(46, 47, 50), Color(40, 50, 38), Color(35, 206, 107)),
        Theme("Moonless Night (Very Dark)", Color(24, 24, 27), Color(15, 17, 20), Color(8, 124, 167)),
        Theme("Stormy Night (Very Dark)", Color(23, 23, 34), Color(5, 5, 27), Color(94, 74, 227)),
        Theme("The Void (Very Dark)", Color(14, 17, 21), Color(5, 5, 12), Color(113, 179, 64)),
        Theme("Classic (Light)", Color(245, 245, 245), Color(213, 210, 195), Color(42, 84, 209)),
        Theme("Royal Light (Light)", Color(245, 245, 245), Color(127, 106, 147), Color(242, 97, 87)),
        Theme("Partly Cloudy (Light)", Color(245, 245, 245), Color(84, 95, 117), Color(217, 114, 255)),
        Theme("Waterfall (Colorful)", Color(214, 237, 246), Color(172, 215, 236), Color(108, 197, 81)),
        Theme("Jungle (Colorful)", Color(201, 227, 172), Color(144, 190, 109), Color(254, 100, 163)),
        Theme("Dunes (Colorful)", Color(229, 177, 129), Color(222, 107, 72), Color(131, 34, 50))
    )

    fun tick() {
        if (!config.useCustomAccentColor) {
            config.accentColor = accentColor
        }
        if (!config.customTheme) {
            config.primaryColor = primaryColor
            config.secondaryColor = secondaryColor
        }

//        If the theme has changed
        if (lastThemeName != defaultThemes[config.themeIndex].name) {
            for (image: UIImage in backgroundUIImages) {
                try {
                    image.applyTexture(ReleasedDynamicTexture(loadImage(currentBackgroundFile.path)))
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            for (data: ButtonData in buttonDataList) {
                try {
                    val color = data.color
                    data.image.applyTexture(ReleasedDynamicTexture(loadImage(getCurrentButtonFile(color).path)))
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            for (data: ToggleData in toggleDataList) {
                try {
                    val color = data.color
                    data.image.applyTexture(ReleasedDynamicTexture(loadImage(getCurrentToggleFile(data.isSelected, color).path)))
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            lastThemeName = defaultThemes[config.themeIndex].name
        }
    }

    val currentBackgroundUIImage: UIImage
        get() {
            val image: UIImage = if (config.disableThemes) {
                ResourceLocation(
                    "partlysaneskies",
                    "textures/gui/base_color_background.png"
                ).uiImageFromResourceLocation()
            } else {
                try {
                    UIImage.ofFile(currentBackgroundFile)
                } catch (e: IOException) {
                    ResourceLocation("partlysaneskies","textures/gui/base_color_background.png").uiImageFromResourceLocation()
                }
            }
            backgroundUIImages.add(image)
            return image
        }
    val currentButtonUIImage: UIImage
        get() = getCurrentButtonUIImage(accentColor)

    fun getCurrentButtonUIImage(accentColor: OneColor): UIImage {
        val accentColor: OneColor = accentColor
        val image: UIImage
        if (config.disableThemes) {
            image = if ((accentColor == ThemeManager.accentColor)) {
                ResourceLocation("partlysaneskies","textures/gui/base_color_button.png").uiImageFromResourceLocation()
            } else {
                ResourceLocation("partlysaneskies","textures/gui/base_color_button_transparent.png").uiImageFromResourceLocation()
            }
        } else {
            image = try {
                UIImage.ofFile(getCurrentButtonFile(accentColor))
            } catch (e: IOException) {
                if ((accentColor == ThemeManager.accentColor)) {
                    ResourceLocation("partlysaneskies","textures/gui/base_color_button.png").uiImageFromResourceLocation()
                } else {
                    ResourceLocation("partlysaneskies","textures/gui/base_color_button_transparent.png").uiImageFromResourceLocation()
                }
            }
        }

        buttonDataList.add(ButtonData(image, accentColor))
        return image
    }

    fun getCurrentToggleUIImage(selected: Boolean): UIImage {
        return getCurrentToggleUIImage(selected, accentColor)
    }

    fun getCurrentToggleUIImage(selected: Boolean, accentColor: OneColor): UIImage {
        var accentColor: OneColor = accentColor
        var image: UIImage
        if (config.disableThemes) {
            image = if (selected) {
                ResourceLocation("partlysaneskies","textures/gui/selected_toggle.png").uiImageFromResourceLocation()
            } else {
                ResourceLocation("partlysaneskies","textures/gui/unselected_toggle.png").uiImageFromResourceLocation()
            }
        }
        image = try {
            UIImage.ofFile(getCurrentToggleFile(selected, accentColor))
        } catch (e: IOException) {
            if (selected) {
                ResourceLocation("partlysaneskies","textures/gui/selected_toggle.png").uiImageFromResourceLocation()
            } else {
                ResourceLocation("partlysaneskies","textures/gui/unselected_toggle.png").uiImageFromResourceLocation()
            }
        }

        toggleDataList.add(ToggleData(image, selected, accentColor))
        return image
    }

    val currentBackgroundFile: File
        get() = getBackgroundWithColor(primaryColor, secondaryColor, accentColor)

    val currentButtonFile: File
        get() {
            return getCurrentButtonFile(accentColor)
        }

    fun getCurrentButtonFile(accentColor: OneColor): File {
        return getButtonWithColor(primaryColor, secondaryColor, accentColor)
    }

    fun getCurrentToggleFile(selected: Boolean): File {
        return getCurrentToggleFile(selected, accentColor)
    }

    fun getCurrentToggleFile(selected: Boolean, accentColor: OneColor): File {
        return if (selected) {
            getToggleWithColor(primaryColor, secondaryColor, accentColor)
        } else {
            getToggleWithColor(primaryColor, secondaryColor, secondaryColor)
        }
    }

    val primaryColor: OneColor
        get() {
            return if (!config.customTheme) {
                val themeIndex: Int = config.themeIndex
                OneColor(defaultThemes[themeIndex].primaryColor)
            } else {
                config.primaryColor
            }
        }
    val darkPrimaryColor: OneColor
        get() {
            return OneColor(darkenColor(primaryColor))
        }
    val lightPrimaryColor: OneColor
        get() {
            return OneColor(lightenColor(primaryColor))
        }
    val secondaryColor: OneColor
        get() {
            return if (!config.customTheme) {
                val themeIndex: Int = config.themeIndex
                OneColor(defaultThemes[themeIndex].secondaryColor)
            } else {
                config.secondaryColor
            }
        }
    val darkSecondaryColor: OneColor
        get() {
            return OneColor(darkenColor(secondaryColor))
        }
    val lightSecondaryColor: OneColor
        get() {
            return OneColor(lightenColor(secondaryColor))
        }
    val accentColor: OneColor
        get() {
            return if (!config.useCustomAccentColor) {
                val themeIndex: Int = config.themeIndex
                OneColor(defaultThemes[themeIndex].defaultAccentColor)
            } else {
                config.accentColor
            }
        }
    val darkAccentColor: Color
        get() {
            return darkenColor(accentColor)
        }
    val lightAccentColor: Color
        get() {
            return lightenColor(accentColor)
        }

    private fun darkenColor(color: OneColor): Color {
        return darkenColor(color.toJavaColor())
    }

    private fun darkenColor(color: Color): Color {
        val averageR: Int = (color.red * .761).toInt()
        val averageG: Int = (color.green * .761).toInt()
        val averageB: Int = (color.blue * .761).toInt()
        return Color(averageR, averageG, averageB, color.getTransparency())
    }

    private fun lightenColor(color: OneColor): Color {
        return lightenColor(color.toJavaColor())
    }

    private fun lightenColor(color: Color): Color {
        val averageR: Int = (color.red * .798 + 255 * .202).toInt()
        val averageG: Int = (color.green * .798 + 255 * .202).toInt()
        val averageB: Int = (color.blue * .798 + 255 * .202).toInt()
        return Color(averageR, averageG, averageB, color.getTransparency())
    }

    @Throws(IOException::class)
    fun getBackgroundWithColor(primaryColor: OneColor, secondaryColor: OneColor, accentColor: OneColor): File {
        val primaryColorHex: String = Integer.toHexString(primaryColor.getRGB() and 0xffffff)
        val secondaryColorHex: String = Integer.toHexString(secondaryColor.getRGB() and 0xffffff)
        val accentColorHex: String = Integer.toHexString(accentColor.getRGB() and 0xffffff)
        val folderPath: Path = Paths.get("./config/partly-sane-skies/image_variants/background")
        val fileName = "background-$primaryColorHex-$secondaryColorHex-$accentColorHex.png"
        val filePath: Path = Paths.get("$folderPath/$fileName")

        if (Files.exists(filePath)) {
            return filePath.toFile()
        }

        val backgroundResourceLocation = ResourceLocation("partlysaneskies", "textures/debug_gui_textures/background.png")
        val debugTexture: IResource = minecraft.resourceManager.getResource(backgroundResourceLocation)
        val debugImage: BufferedImage = TextureUtil.readBufferedImage(debugTexture.inputStream)
        folderPath.toFile().mkdirs()
        filePath.toFile().createNewFile()
        replaceColor(debugImage, PRIMARY_DEBUG_COLOR, primaryColor)
        replaceColor(debugImage, SECONDARY_DEBUG_COLOR, secondaryColor)
        replaceColor(debugImage, ACCENT_DEBUG_COLOR, accentColor)
        saveImage(debugImage, filePath)
        return filePath.toFile()
    }

    @Throws(IOException::class)
    fun getButtonWithColor(primaryColor: OneColor, secondaryColor: OneColor, accentColor: OneColor): File {
        val primaryHex: String = Integer.toHexString(primaryColor.getRGB() and 0xffffff)
        val secondaryColorHex: String = Integer.toHexString(secondaryColor.getRGB() and 0xffffff)
        val accentColorHex: String = Integer.toHexString(accentColor.getRGB() and 0xffffff)
        val folderPath: Path = Paths.get("./config/partly-sane-skies/image_variants/button")
        val fileName = "button-$primaryHex-$secondaryColorHex-$accentColorHex.png"
        val filePath: Path = Paths.get("$folderPath/$fileName")

        if (Files.exists(filePath)) {
            return filePath.toFile()
        }
        val buttonResourceLocation: ResourceLocation =
            ResourceLocation("partlysaneskies", "textures/debug_gui_textures/button.png")
        val debugTexture: IResource = minecraft.resourceManager.getResource(buttonResourceLocation)
        val debugImage: BufferedImage = TextureUtil.readBufferedImage(debugTexture.inputStream)
        folderPath.toFile().mkdirs()
        filePath.toFile().createNewFile()
        replaceColor(debugImage, PRIMARY_DEBUG_COLOR, primaryColor)
        replaceColor(debugImage, SECONDARY_DEBUG_COLOR, secondaryColor)
        replaceColor(
            debugImage, ACCENT_DEBUG_COLOR,
            (accentColor)
        )
        saveImage(debugImage, filePath)
        return filePath.toFile()
    }

    @Throws(IOException::class)
    fun getToggleWithColor(primaryColor: OneColor, secondaryColor: OneColor, accentColor: OneColor): File {
        val primaryColorHex: String = Integer.toHexString(primaryColor.getRGB() and 0xffffff)
        val secondaryColorHex: String = Integer.toHexString(secondaryColor.getRGB() and 0xffffff)
        val accentColorHex: String = Integer.toHexString(accentColor.getRGB() and 0xffffff)
        val folderPath: Path = Paths.get("./config/partly-sane-skies/image_variants/toggle")
        val fileName: String = "toggle-$primaryColorHex-$secondaryColorHex-$accentColorHex.png"
        val filePath: Path = Paths.get("$folderPath/$fileName")
        if (Files.exists(filePath)) {
            return filePath.toFile()
        }
        val toggleResourceLocation: ResourceLocation =
            ResourceLocation("partlysaneskies", "textures/debug_gui_textures/toggle.png")
        val debugTexture: IResource = minecraft.resourceManager.getResource(toggleResourceLocation)
        val debugImage: BufferedImage = TextureUtil.readBufferedImage(debugTexture.inputStream)
        folderPath.toFile().mkdirs()
        filePath.toFile().createNewFile()
        replaceColor(debugImage, PRIMARY_DEBUG_COLOR, primaryColor)
        replaceColor(debugImage, SECONDARY_DEBUG_COLOR, secondaryColor)
        replaceColor(
            debugImage, ACCENT_DEBUG_COLOR,
            (accentColor)
        )
        saveImage(debugImage, filePath)
        return filePath.toFile()
    }

    class ButtonData(val image: UIImage, val color: OneColor) {
    }

    class ToggleData(val image: UIImage, selected: Boolean, val color: OneColor) {
        val isSelected: Boolean = selected

    }

    class Theme(val name: String, val primaryColor: Color, val secondaryColor: Color, val defaultAccentColor: Color)
}
