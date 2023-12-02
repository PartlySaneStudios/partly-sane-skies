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

import net.minecraft.inventory.IInventory
import net.minecraft.util.ResourceLocation
import java.awt.Color

class CategoriesBar (val xConstraint: XConstraint, val yConstraint: YConstraint, val heightConstraint: HeightConstraint, val widthConstraint: WidthConstraint, val inventory: IInventory) {
    val topBarImagePaths = arrayOf(
        "textures/gui/custom_ah/weapons_icon.png",
        "textures/gui/custom_ah/armor_icon.png",
        "textures/gui/custom_ah/accessories_icon.png",
        "textures/gui/custom_ah/consumables_icon.png",
        "textures/gui/custom_ah/block_icon.png",
        "textures/gui/custom_ah/misc_icon.png"
    )

    val topBarFurfSkyImagePaths = arrayOf(
        "textures/gui/custom_ah/furfsky/weapons_icon.png",
        "textures/gui/custom_ah/furfsky/armor_icon.png",
        "textures/gui/custom_ah/furfsky/accessories_icon.png",
        "textures/gui/custom_ah/furfsky/consumables_icon.png",
        "textures/gui/custom_ah/furfsky/block_icon.png",
        "textures/gui/custom_ah/furfsky/misc_icon.png"
    )

    private val topBar = UIBlock()
        .setX(xConstraint)
        .setY(yConstraint)
        .setWidth(widthConstraint)
        .setHeight(heightConstraint)
        .setColor(Color(0, 0, 0, 0))

    private val topBarImage = ThemeManager.getCurrentBackgroundUIImage()
        .setX(CenterConstraint())
        .setY(CenterConstraint())
        .setWidth(widthConstraint)
        .setHeight(heightConstraint)
        .setChildOf(topBar)


    var selectedItem = -1

    private val categoryWidth = (topBarImage.getWidth()) / 6
    private val categoryHeight = (topBarImage.getHeight())
    private val imageSide = categoryHeight * .9

    private val categoryList = ArrayList<UIComponent>()
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

        for (i in 0..5) {
            val slot = i * 9
            val icon = UIBlock().constrain {
                x = (categoryWidth * i + categoryWidth * .1).pixels
                y = CenterConstraint()
                width = (categoryWidth * .8).pixels
                height = categoryHeight.pixels
            }.setColor(Color(0, 0, 0, 0)) childOf topBarImage

            var imagePath =
                if (PartlySaneSkies.config.customAhGuiTextures == 1) {
                    topBarFurfSkyImagePaths[i]
                } else {
                    topBarImagePaths[i]
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

            categoryList.add(icon)
        }
    }

    fun loadItemInformationBar(informationBar: ItemInformationBar){
        for (i in 0 until categoryList.size) {
            val slot = i * 9

            val item = inventory.getStackInSlot(slot)

            if (item == null) {
                continue
            }

            val auction = AuctionElement(0, item, 0.pixels, 0.pixels, 0.pixels, 0.0f)
            val icon = categoryList[i]

            icon.onMouseEnter {
                informationBar.loadAuction(auction)
            }
            icon.onMouseLeave {
                informationBar.clearInfo()
            }
        }
    }

    fun setChildOf(component: UIComponent) {
        topBar.setChildOf(component)
    }



    fun update() {
        if (categoryList.size > selectedItem && selectedItem != -1) {
            for (element in categoryList) {
                element.setColor(Color(0, 0, 0, 0))
            }
            categoryList[selectedItem].setColor(ThemeManager.getAccentColor().toJavaColor())
        }
    }
}