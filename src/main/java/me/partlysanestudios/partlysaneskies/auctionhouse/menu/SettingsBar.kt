//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.auctionhouse.menu

import gg.essential.elementa.UIComponent
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.constraints.*
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.pixels
import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.system.ThemeManager
import me.partlysanestudios.partlysaneskies.utils.ElementaUtils.uiImageFromResourceLocation
import me.partlysanestudios.partlysaneskies.utils.MinecraftUtils
import me.partlysanestudios.partlysaneskies.utils.MinecraftUtils.getLore
import me.partlysanestudios.partlysaneskies.utils.StringUtils
import me.partlysanestudios.partlysaneskies.utils.StringUtils.removeColorCodes

import net.minecraft.inventory.IInventory
import net.minecraft.util.ResourceLocation
import java.awt.Color
import java.util.*

class SettingsBar (val xConstraint: XConstraint, val yConstraint: YConstraint, val heightConstraint: HeightConstraint, val widthConstraint: WidthConstraint, val inventory: IInventory) {
    private var bottomBarImagePaths = arrayOf(
        "textures/gui/custom_ah/left_arrow_icon.png",
        "textures/gui/custom_ah/reset_icon.png",
        "textures/gui/custom_ah/search_icon.png",
        "textures/gui/custom_ah/go_back_icon.png",
        "textures/gui/custom_ah/sort_filter/unknown.png",
        "textures/gui/custom_ah/rarity_filter/no_filter.png",
        "textures/gui/custom_ah/type/all.png",
        "textures/gui/custom_ah/right_arrow_icon.png"
    )

    private var bottomBarFurfSkyImagePaths = arrayOf(
        "textures/gui/custom_ah/furfsky/left_arrow_icon.png",
        "textures/gui/custom_ah/furfsky/reset_icon.png",
        "textures/gui/custom_ah/furfsky/search_icon.png",
        "textures/gui/custom_ah/furfsky/go_back_icon.png",
        "textures/gui/custom_ah/furfsky/sort_filter/unknown.png",
        "textures/gui/custom_ah/furfsky/rarity_filter/no_filter.png",
        "textures/gui/custom_ah/furfsky/type/all.png",
        "textures/gui/custom_ah/furfsky/right_arrow_icon.png"
    )


    private val bottomBar = UIBlock()
        .setX(xConstraint)
        .setY(yConstraint)
        .setWidth(widthConstraint)
        .setHeight(heightConstraint)
        .setColor(Color(0, 0, 0, 0))

    private val bottomBarImage = ThemeManager.getCurrentBackgroundUIImage()
        .setX(CenterConstraint())
        .setY(CenterConstraint())
        .setWidth(widthConstraint)
        .setHeight(heightConstraint)
        .setChildOf(bottomBar)


    var selectedItem = -1

    private val settingWidth = (bottomBarImage.getWidth()) / 8
    private val settingHeight = (bottomBarImage.getHeight())
    private val imageSide = settingHeight * .9

    private val settingList = ArrayList<UIComponent>()
    init {
        val paneType = inventory.getStackInSlot(1).itemDamage

        when (paneType) {
            1 -> selectedItem = 0
            11 -> selectedItem = 1
            13 -> selectedItem = 2
            14 -> selectedItem = 3
            12 -> selectedItem = 4
            10 -> selectedItem = 5
            else -> {}
        }

        for (i in 0..7) {
            val slot = i + 46

            val icon = UIBlock().constrain {
                x = (settingWidth * i + settingWidth * .1).pixels
                y = CenterConstraint()
                width = (settingWidth * .8).pixels
                height = settingHeight.pixels
            }.setColor(Color(0, 0, 0, 0)) childOf bottomBarImage

            var imagePath: String = bottomBarImagePaths[i]
            if (PartlySaneSkies.config.customAhGuiTextures == 1) {
                imagePath = bottomBarFurfSkyImagePaths[i]
            }

            // If it's the filter icon, set the picture to the filter icon
            if (slot == 50) {
                var sortSelectedLine = ""
                var sortImageName = "unknown"
                try {
                    val sortLoreList: List<String> = inventory.getStackInSlot(slot).getLore()
                    for (line in sortLoreList) {
                        if (line.contains("▶")) {
                            sortSelectedLine = line
                            break
                        }
                    }
                    sortSelectedLine = sortSelectedLine.removeColorCodes()
                    if (sortSelectedLine.lowercase(Locale.getDefault()).contains("highest")) {
                        sortImageName = "price_high_low"
                    } else if (sortSelectedLine.lowercase(Locale.getDefault()).contains("lowest")) {
                        sortImageName = "price_low_high"
                    } else if (sortSelectedLine.lowercase(Locale.getDefault()).contains("soon")) {
                        sortImageName = "ending_soon"
                    } else if (sortSelectedLine.lowercase(Locale.getDefault()).contains("most")) {
                        sortImageName = "random"
                    }
                } catch (exception: NullPointerException) {
                    exception.printStackTrace()
                }
                imagePath = imagePath.replace("unknown", sortImageName)
            }
            // Rarity filter slot
            else if (slot == 51) {
                var filterSelectedLine = ""
                var filterImageName = "no_filter"
                try {

                    val sortLoreList: List<String> = inventory.getStackInSlot(slot).getLore()
                    for (line in sortLoreList) {
                        if (line.contains("▶")) {
                            filterSelectedLine = line
                            break
                        }
                    }

                    filterSelectedLine = filterSelectedLine.removeColorCodes()

                    if (filterSelectedLine.lowercase(Locale.getDefault()).contains("uncommon")) {
                        filterImageName = "uncommon"
                    } else if (filterSelectedLine.lowercase(Locale.getDefault()).contains("common")) {
                        filterImageName = "common"
                    } else if (filterSelectedLine.lowercase(Locale.getDefault()).contains("rare")) {
                        filterImageName = "rare"
                    } else if (filterSelectedLine.lowercase(Locale.getDefault()).contains("epic")) {
                        filterImageName = "epic"
                    } else if (filterSelectedLine.lowercase(Locale.getDefault()).contains("legendary")) {
                        filterImageName = "legendary"
                    } else if (filterSelectedLine.lowercase(Locale.getDefault()).contains("special")) {
                        filterImageName = "special"
                    } else if (filterSelectedLine.lowercase(Locale.getDefault()).contains("very special")) {
                        filterImageName = "special"
                    } else if (filterSelectedLine.lowercase(Locale.getDefault()).contains("divine")) {
                        filterImageName = "divine"
                    } else if (filterSelectedLine.lowercase(Locale.getDefault()).contains("mythic")) {
                        filterImageName = "mythic"
                    } else if (filterSelectedLine.lowercase(Locale.getDefault()).contains("unobtainable")) {
                        filterImageName = "unobtainable"
                    }
                } catch (exception: java.lang.NullPointerException) {
                    exception.printStackTrace()
                }
                imagePath = imagePath.replace("no_filter", filterImageName)
            }
            else if (slot == 52) {
                var binSelectedLine = ""
                var binImageName = "all"
                try {
                    val binLoreList: List<String> = inventory.getStackInSlot(slot).getLore()
                    for (line in binLoreList) {
                        if (line.contains("▶")) {
                            binSelectedLine = line
                            break
                        }
                    }
                    binSelectedLine = binSelectedLine.removeColorCodes()
                    if (binSelectedLine.lowercase(Locale.getDefault()).contains("bin only")) {
                        binImageName = "bin_only"
                    } else if (binSelectedLine.lowercase(Locale.getDefault()).contains("auctions only")) {
                        binImageName = "auction_only"
                    }
                } catch (exception: java.lang.NullPointerException) {
                    exception.printStackTrace()
                }
                imagePath = imagePath.replace("all", binImageName)
            }



            ResourceLocation("partlysaneskies", imagePath).uiImageFromResourceLocation().constrain {
                x = CenterConstraint()
                y = CenterConstraint()
                width = imageSide.pixels
                height = imageSide.pixels
            } childOf icon

            icon.onMouseClick {
                if (it.mouseButton == 1) {
                    MinecraftUtils.rightClickOnSlot(slot)
                } else {
                    MinecraftUtils.clickOnSlot(slot)
                }
            }

            settingList.add(icon)
        }
    }

    fun loadItemInformationBar(informationBar: ItemInformationBar){
        for (i in 0 until settingList.size) {
            val slot = i + 46

            val item = inventory.getStackInSlot(slot)

            if (item == null) {
                continue
            }

            val auction = AuctionElement(0, item, 0.pixels, 0.pixels, 0.pixels, 0.0f)
            val icon = settingList[i]

            icon.onMouseEnter {
                informationBar.loadAuction(auction)
            }
            icon.onMouseLeave {
                informationBar.clearInfo()
            }
        }
    }

    fun setChildOf(component: UIComponent) {
        bottomBar.setChildOf(component)
    }



    fun update() {
        if (settingList.size > selectedItem && selectedItem != -1) {
            for (element in settingList) {
                element.setColor(Color(0, 0, 0, 0))
            }
            settingList[selectedItem].setColor(ThemeManager.getAccentColor().toJavaColor())
        }
    }

}