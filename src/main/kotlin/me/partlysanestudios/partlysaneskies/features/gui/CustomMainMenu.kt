package me.partlysanestudios.partlysaneskies.features.gui

import com.google.gson.JsonParser
import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.UIComponent
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIImage
import gg.essential.elementa.components.UIText
import gg.essential.elementa.components.UIWrappedText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.XConstraint
import gg.essential.elementa.constraints.YConstraint
import gg.essential.elementa.dsl.*
import gg.essential.universal.UMatrixStack
import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.config
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.discordCode
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.minecraft
import me.partlysanestudios.partlysaneskies.data.pssdata.PublicDataManager
import me.partlysanestudios.partlysaneskies.features.security.PrivacyMode.enablePrivacyMode
import me.partlysanestudios.partlysaneskies.features.themes.ThemeManager
import me.partlysanestudios.partlysaneskies.features.themes.ThemeManager.accentColor
import me.partlysanestudios.partlysaneskies.render.gui.constraints.ScaledPixelConstraint.Companion.scaledPixels
import me.partlysanestudios.partlysaneskies.utils.ElementaUtils.uiImageFromResourceLocation
import me.partlysanestudios.partlysaneskies.utils.MathUtils.randInt
import me.partlysanestudios.partlysaneskies.utils.SystemUtils
import net.minecraft.client.audio.PositionedSoundRecord
import net.minecraft.client.gui.GuiMainMenu
import net.minecraft.client.gui.GuiMultiplayer
import net.minecraft.client.gui.GuiOptions
import net.minecraft.client.gui.GuiSelectWorld
import net.minecraft.client.multiplayer.ServerData
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.event.GuiOpenEvent
import net.minecraftforge.fml.client.FMLClientHandler
import net.minecraftforge.fml.client.GuiModList
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.awt.Color
import java.io.File
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class CustomMainMenu: WindowScreen(ElementaVersion.V5) {
    companion object {
        @SubscribeEvent
        fun onGuiOpen(event: GuiOpenEvent) {
            if (!config.customMainMenu) {
                return
            }

            if (event.gui !is GuiMainMenu) {
                return
            }
            event.setCanceled(true)

            if (config.privacyMode == 1) {
                enablePrivacyMode()
            }

            minecraft.displayGuiScreen(CustomMainMenu())
            minecraft.soundHandler.playSound(PositionedSoundRecord.create(ResourceLocation("partlysaneskies", "bell")))
        }
    }

    private val backgroundBox = UIBlock().constrain {
        width = 100.percent
        height = 100.percent
        x = CenterConstraint()
        y = CenterConstraint()
        color = Color(0, 0, 0, 0).constraint
    } childOf window

    private val backgroundImage = getBackgroundImage().constrain {
        width = 100.percent
        height = 100.percent
        x = CenterConstraint()
        y = CenterConstraint()
    } childOf backgroundBox

    private val middleMenuBackground = UIBlock().constrain {
        x = 350.scaledPixels
        y = CenterConstraint()
        height = 100.percent
        width = 125.scaledPixels
        color = Color(0, 0, 0, 75).constraint
    } childOf backgroundImage

    private val leftMiddleMenuSide = UIBlock().constrain {
        x = (-2).scaledPixels
        y = CenterConstraint()
        height = 100.percent
        width = 2.scaledPixels
        color = ThemeManager.accentColor.toJavaColor().constraint
    } childOf middleMenuBackground

    private val rightMiddleMenuSide = UIBlock().constrain {
        x = 100.percent
        y = CenterConstraint()
        height = 100.percent
        width = 2.scaledPixels
        color = ThemeManager.accentColor.toJavaColor().constraint
    } childOf middleMenuBackground

    private val titleImage = ResourceLocation("partlysaneskies", "textures/gui/main_menu/title_text.png").uiImageFromResourceLocation().constrain {
        x = CenterConstraint()
        y = 50.scaledPixels
        height = 75.scaledPixels
        width = (75 * (928f / 124)).scaledPixels // Ratio between width and height * height
    } childOf middleMenuBackground

    private val updateWarning = UIWrappedText(text = "Your version of Partly Sane Skies is out of date.\nPlease update to the latest version", centered = true).constrain {
        textScale = 2.25.scaledPixels
        x = 133.scaledPixels
        y = CenterConstraint()
        width = 700.scaledPixels
        color = Color(0, 0, 0, 0).constraint
    }.onMouseClick {
        SystemUtils.openLink("https://github.com/PartlySaneStudios/partly-sane-skies/releases")
    } childOf middleMenuBackground

    private val singlePlayerButton = UIBlock().constrain {
        x = CenterConstraint()
        y = 200.scaledPixels
        height = 40.scaledPixels
        width = middleMenuBackground.getWidth().pixels
        color = Color(0, 0, 0, 0).constraint
    }.onMouseClick {
        mc.displayGuiScreen(GuiSelectWorld(this@CustomMainMenu))
    }.onMouseEnter {
        for (child in this.children) {
            child.setColor(Color(255, 255, 255))
        }
    }.onMouseLeave {
        for (child in this.children) {
            child.setColor(Color.white)
        }
    } childOf middleMenuBackground

    private val singlePlayerText = UIText("Singleplayer").constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        textScale = 1.scaledPixels
        color = Color.white.constraint
    }.onMouseEnter {
        for (child in this.children) {
            child.setColor(Color(255, 255, 255))
        }
    }.onMouseLeave {
        for (child in this.children) {
            child.setColor(Color.white)
        }
    } childOf singlePlayerButton

    private val multiPlayerButton = UIBlock().constrain {
        x = CenterConstraint()
        y = 240.scaledPixels
        height = 40.scaledPixels
        width = middleMenuBackground.getWidth().pixels
        color = Color(0, 0, 0, 0).constraint
    }.onMouseClick {
        mc.displayGuiScreen(GuiMultiplayer(this@CustomMainMenu))
    }.onMouseEnter {
        for (child in this.children) {
            child.setColor(Color(255, 255, 255))
        }
    }.onMouseLeave {
        for (child in this.children) {
            child.setColor(Color.white)
        }
    } childOf middleMenuBackground

    private val multiPlayerText = UIText("Multiplayer").constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        textScale = 1.scaledPixels
        color = Color.white.constraint
    } childOf multiPlayerButton

    private val joinHypixelButton = UIBlock().constrain {
        x = CenterConstraint()
        y = 280.scaledPixels
        height = 40.scaledPixels
        width = middleMenuBackground.getWidth().pixels
        color = Color(0, 0, 0, 0).constraint
    }.onMouseClick {
        FMLClientHandler.instance().connectToServer(GuiMultiplayer(minecraft.currentScreen), ServerData("AddictionGame", "hypixel.net", false))
    }.onMouseEnter {
        for (child in this.children) {
            child.setColor(Color(255, 255, 255))
        }
    }.onMouseLeave {
        for (child in this.children) {
            child.setColor(Color.white)
        }
    } childOf middleMenuBackground

    private val joinHypixelText = UIText("Join Hypixel").constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        textScale = 1.scaledPixels
        color = Color.white.constraint
    } childOf joinHypixelButton

    private val modsButton = UIBlock().constrain {
        x = CenterConstraint()
        y = 320.scaledPixels
        height = 40.scaledPixels
        width = middleMenuBackground.getWidth().pixels
        color = Color(0, 0, 0, 0).constraint
    }.onMouseClick {
        mc.displayGuiScreen(GuiModList(this@CustomMainMenu))
    }.onMouseEnter {
        for (child in this.children) {
            child.setColor(Color(255, 255, 255))
        }
    }.onMouseLeave {
        for (child in this.children) {
            child.setColor(Color.white)
        }
    } childOf middleMenuBackground

    private val modsText = UIText("Mods").constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        textScale = 1.scaledPixels
        color = Color.white.constraint
    } childOf modsButton

    private val optionsButton = UIBlock().constrain {
        x = CenterConstraint()
        y = 380.scaledPixels
        height = 20.scaledPixels
        width = 100.percent
        color = Color(0, 0, 0, 0).constraint
    }.onMouseClick {
        mc.displayGuiScreen(GuiOptions(this@CustomMainMenu, mc.gameSettings))
    }.onMouseEnter {
        for (child in this.children) {
            child.setColor(Color(255, 255, 255))
        }
    }.onMouseLeave {
        for (child in this.children) {
            child.setColor(Color.white)
        }
    } childOf middleMenuBackground

    private val optionsText = UIText("Options").constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        textScale = 1.scaledPixels
        color = Color.white.constraint
    } childOf optionsButton

    private val optionsDivide = UIBlock().constrain {
        x = CenterConstraint()
        y = 400.scaledPixels
        height = 1.scaledPixels
        width = 90.percent
        color = ThemeManager.accentColor.toJavaColor().constraint
    } childOf middleMenuBackground


    private val pssOptionsButton = UIBlock().constrain {
        x = CenterConstraint()
        y = 400.scaledPixels
        height = 20.scaledPixels
        width = 100.percent
        color = Color(0, 0, 0, 0).constraint
    }.onMouseClick {
        mc.displayGuiScreen(GuiSelectWorld(this@CustomMainMenu))
    }.onMouseEnter {
        for (child in this.children) {
            child.setColor(Color(255, 255, 255))
        }
    }.onMouseLeave {
        for (child in this.children) {
            child.setColor(Color.white)
        }
    } childOf middleMenuBackground

    private val pssOptionsText = UIText("Partly Sane Skies Config").constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        textScale = 0.735.scaledPixels
        color = Color.white.constraint
    } childOf pssOptionsButton

    private val quitButton = UIBlock().constrain {
        x = CenterConstraint()
        y = 440.scaledPixels
        height = 40.scaledPixels
        width = 100.percent
        color = Color(0, 0, 0, 0).constraint
    }.onMouseClick {
        mc.displayGuiScreen(GuiSelectWorld(this@CustomMainMenu))
    }.onMouseEnter {
        for (child in this.children) {
            child.setColor(Color(255, 255, 255))
        }
    }.onMouseLeave {
        for (child in this.children) {
            child.setColor(Color.white)
        }
    } childOf middleMenuBackground

    private val quitText = UIText("Quit").constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        textScale = 1.scaledPixels
        color = Color.white.constraint
    } childOf quitButton

    private val timeText = UIText().constrain {
        x = CenterConstraint()
        y = (100.percent - 10.scaledPixels)
        color = Color.white.constraint
        textScale = 0.5.scaledPixels
    } childOf middleMenuBackground

    private val discordText = UIText("Discord: discord.gg/$discordCode").constrain {
        x = 10.scaledPixels
        y = (100.percent - 20.scaledPixels)
        textScale = 1.scaledPixels
        color = Color(69, 79, 191).constraint
    } childOf backgroundImage


    private fun getBackgroundImage(): UIImage {
        val images = arrayOf(
            "random -- this option will never be called",
            "image_1.png",
            "image_2.png",
            "image_3.png",
            "image_4.png",
            "image_5.png",
            "image_6.png"
        )

        val image: String = if (config.customMainMenuImage == 0) {
            "textures/gui/main_menu/" + images[randInt(1, images.size)]
        } else if (config.customMainMenuImage < 7) {
            "textures/gui/main_menu/" + images[config.customMainMenuImage]
        } else {
            ""
        }

        return if (config.customMainMenuImage == 7) {
            UIImage.ofFile(File("./config/partly-sane-skies/background.png"))
        } else {
            ResourceLocation("partlysaneskies", image).uiImageFromResourceLocation()
        }
    }

    private fun createAnnouncements(announcements: ArrayList<Announcement>) {
        val startY = 125.scaledPixels
        val padY = 15.scaledPixels

        var parent: UIComponent = backgroundBox
        var yConstraint: YConstraint = startY
        var xConstraint: XConstraint = 33.scaledPixels
        for (announcement in announcements) {
            val title = UIWrappedText().constrain {
                x = xConstraint
                y = yConstraint
                width = 300.scaledPixels
                textScale = 1.5.scaledPixels
            }.setText(
                "§e${announcement.title}"
            ).onMouseClick {
                SystemUtils.openLink(announcement.link)
            } childOf parent

            val description = UIWrappedText().constrain {
                x = 0.percent
                y = 100.percent + 5.scaledPixels
                width = 100.percent
                textScale = 1.33.scaledPixels
            }.setText(
                "§8${announcement.date}§r\n§7${announcement.description}"
            ).onMouseClick {
                SystemUtils.openLink(announcement.link)
            } childOf title

            announcement.titleComponent = title as UIWrappedText
            announcement.descriptionComponent = description as UIWrappedText

            if (description.getBottom() > window.getBottom()) {
                title.setColor(Color(0, 0, 0, 0))
                description.setColor(Color(0, 0, 0, 0))
                break
            }

            parent = description
            yConstraint = 100.percent + padY
            xConstraint = 0.percent
        }
    }


    private class Announcement(val title: String, val description: String, val date: String, val link: String) {
        var titleComponent: UIWrappedText? = null
        var descriptionComponent: UIWrappedText? = null
    }


    init {
        if (config.displayAnnouncementsCustomMainMenu) {
            Thread {
                val data = PublicDataManager.getFile("main_menu.json")
                val jsonObject = JsonParser().parse(data).asJsonObject

                val announcementJsonArray = jsonObject["announcements"].asJsonArray
                val announcements = ArrayList<Announcement>()

                for (element in announcementJsonArray) {
                    val announcement = element.getAsJsonObject()
                    val title = announcement["name"].asString
                    val desc = announcement["description"].asString
                    val date = announcement["date"].asString
                    val urlString = announcement["link"].asString

                    announcements.add(Announcement(title, desc, date, urlString))
                }

                createAnnouncements(announcements)
            }.start()
        }
        if (!PartlySaneSkies.isLatestVersion) {
            updateWarning.setColor(Color.red)
        }
    }

    override fun onDrawScreen(matrixStack: UMatrixStack, mouseX: Int, mouseY: Int, partialTicks: Float) {
        super.onDrawScreen(matrixStack, mouseX, mouseY, partialTicks)

        val userZoneId = ZoneId.systemDefault()
        val currentTime = LocalDateTime.now(userZoneId)
        var timeString = currentTime.format(DateTimeFormatter.ofPattern("hh:mm:ss a  dd MMMM yyyy", Locale.ENGLISH))
        if (config.hour24time) {
            timeString = currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss dd MMMM yyyy", Locale.ENGLISH))
        }
        timeText.setText(timeString)

        rightMiddleMenuSide.setColor(accentColor.toJavaColor())
        leftMiddleMenuSide.setColor(accentColor.toJavaColor())
        optionsDivide.setColor(accentColor.toJavaColor())
    }
}