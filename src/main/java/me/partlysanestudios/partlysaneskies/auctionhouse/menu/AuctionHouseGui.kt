//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.auctionhouse.menu

import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIImage
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.constraint
import gg.essential.elementa.dsl.pixels
import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.system.ThemeManager
import me.partlysanestudios.partlysaneskies.utils.StringUtils
import net.minecraft.client.gui.inventory.GuiChest
import net.minecraft.inventory.IInventory
import net.minecraft.item.Item
import net.minecraftforge.client.event.GuiOpenEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.awt.Color
import java.util.*

class AuctionHouseGui(defaultAuctionInventory: IInventory) : WindowScreen(ElementaVersion.V2) {

    companion object {
        @SubscribeEvent
        fun onGuiOpen(event: GuiOpenEvent) { val inventory = PartlySaneSkies.getSeparateUpperLowerInventories(event.gui)[0]

            if (!isAhGui()) {
                return
            }
            val guiAlreadyOpen = PartlySaneSkies.minecraft.currentScreen is AuctionHouseGui

            if (guiAlreadyOpen) {
                return
            }

            if (PartlySaneSkies.isDebugMode) {
                return
            }
            if (!PartlySaneSkies.config.customAhGui) {
                return
            }


            openMenu()
        }

        fun isAhGui(): Boolean {
            if (PartlySaneSkies.minecraft.currentScreen !is GuiChest) {
                return false
            }
            val upper =
                Objects.requireNonNull(PartlySaneSkies.getSeparateUpperLowerInventories(PartlySaneSkies.minecraft.currentScreen))[0]
            return StringUtils.removeColorCodes(upper.displayName.formattedText)
                .contains("Auctions Browser") || StringUtils.removeColorCodes(upper.displayName.formattedText)
                .contains("Auctions: \"")
        }

        fun openMenu() {
            var inventory = PartlySaneSkies.getSeparateUpperLowerInventories(PartlySaneSkies.minecraft.currentScreen)[0]

            if (isAuctionHouseFullyLoaded(inventory)) {
                inventory = PartlySaneSkies.getSeparateUpperLowerInventories(PartlySaneSkies.minecraft.currentScreen)[0]
                val gui = AuctionHouseGui(inventory)
                PartlySaneSkies.minecraft.displayGuiScreen(gui)
            } else {
                Thread {
                    PartlySaneSkies.minecraft.addScheduledTask {
                        openMenu()
                    }
                }.start()
            }
        }

        fun isAuctionHouseFullyLoaded(inventory: IInventory): Boolean {
            for (i in 0..53) {
                if (convertSlotToChestCoordinate(i)[0] <= 2 ||
                        convertSlotToChestCoordinate(i)[0] == 9 ||
                        convertSlotToChestCoordinate(i)[1] == 1 ||
                        convertSlotToChestCoordinate(i)[1] == 6) {
                    continue
                }

                // If its equal to null and the stack is an arrow (not the end of the page)
                // Then Return false
                if (inventory.getStackInSlot(i) == null) {
                    if (inventory.getStackInSlot(53) == null) {
                        return false
                    } else if (Item.getIdFromItem(inventory.getStackInSlot(53).item) != 264) {
                        continue
                    }
                    return false
                }
            }

            return true
        }

        fun convertSlotToChestCoordinate(slot: Int): IntArray {
            var x = (slot + 1) % 9
            if (x == 0) x = 9
            val y = (slot + 1) / 9 + 1
            return intArrayOf(x, y)
        }
    }

    private val heightPercent = .333
    private val sideBarPercent = .667

    private val sizeHeight = window.getHeight() * heightPercent
    private val sizeWidth = sizeHeight * 1.4725

    private val boxSide = sizeHeight * 0.1875
    private val pad = sizeHeight * 0.05

    private val totalRows = 4
    private val totalColumns = 6

    private val baseBlock: UIBlock = UIBlock().constrain {
        color = Color(0, 0, 0, 0).constraint
        x = CenterConstraint()
        y = CenterConstraint()
        height = sizeHeight.pixels
        width = sizeWidth.pixels
    } childOf window

    private val backgroundImage: UIImage = ThemeManager.getCurrentBackgroundUIImage().constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        height = sizeHeight.pixels
        width = sizeWidth.pixels
    } childOf baseBlock

    private val itemInformationBar = ItemInformationBar((sideBarPercent * sizeWidth * -1 * 1.05).pixels , CenterConstraint(), (sideBarPercent * sizeWidth).pixels, (sizeHeight * 1.1).pixels, 1f)


    private val auctionInformationBar = AuctionInformationBar((sideBarPercent * sizeWidth * 1.05 + backgroundImage.getWidth()).pixels , CenterConstraint(), (sideBarPercent * sizeWidth).pixels, (sizeHeight * 1.1).pixels, 1f)





    init {
        val auctions = getAuctions(defaultAuctionInventory)
        auctionInformationBar.setChildOf(backgroundImage)
        itemInformationBar.setChildOf(backgroundImage)

        for (row in 0 until 4) {
            for (column in 0 until 6) {
                val x: Float = ((boxSide + pad) * column + pad / 2).toFloat()
                val y: Float = ((boxSide + pad) * row + pad / 6).toFloat()

                try {
                    auctions[row][column]
                        .setX(x.pixels)
                        .setY(y.pixels)
                        .setHeight(boxSide.pixels)
                        .setChildOf(backgroundImage)

                    auctions[row][column].loadItemInformationBar(itemInformationBar)
                    auctions[row][column].loadAuctionInformationBar(auctionInformationBar)
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                }
            }
        }


    }


    private fun getAuctions(inventory: IInventory): Array<Array<AuctionElement>> {
        val items = getAuctionContents(inventory)
        var indexOfItems = 0

        val auctions: Array<Array<AuctionElement>> = Array(totalRows) {
            Array(totalColumns) {
                indexOfItems++
                items[indexOfItems - 1]
            }
        }

        return auctions
    }

    private fun getAuctionContents(inventory: IInventory): List<AuctionElement> {
        val list: MutableList<AuctionElement> = ArrayList()
        for (i in 0..53) {
            if (convertSlotToChestCoordinate(i)[0] <= 2 || convertSlotToChestCoordinate(i)[0] == 9 || convertSlotToChestCoordinate(
                    i
                )[1] == 1 || convertSlotToChestCoordinate(i)[1] == 6
            ) {
                continue
            }
            if (inventory.getStackInSlot(i) == null) {
                continue
            }
            list.add(AuctionElement(i, inventory.getStackInSlot(i), 0.pixels, 0.pixels, 0.pixels))
        }
        return list
    }
}