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
import gg.essential.universal.UMatrixStack
import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.system.ThemeManager
import me.partlysanestudios.partlysaneskies.utils.ChatUtils
import me.partlysanestudios.partlysaneskies.utils.MinecraftUtils
import me.partlysanestudios.partlysaneskies.utils.StringUtils
import me.partlysanestudios.partlysaneskies.utils.StringUtils.removeColorCodes

import net.minecraft.client.gui.inventory.GuiChest
import net.minecraft.inventory.IInventory
import net.minecraft.item.Item
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent
import java.awt.Color

class AuctionHouseGui(defaultAuctionInventory: IInventory) : WindowScreen(ElementaVersion.V2) {
    private val heightPercent = PartlySaneSkies.config.masterAuctionHouseScale
    private val sideBarHeightPercent = PartlySaneSkies.config.auctionHouseSideBarHeight
    private val sideBarWidthPercent = PartlySaneSkies.config.auctionHouseSideBarWidth
    private val sideBarPadding = 1 + PartlySaneSkies.config.auctionSideBarPadding
    private val textScale = window.getWidth() / 1075.0f * heightPercent/.333f * PartlySaneSkies.config.auctionHouseTextScale

    private val sizeHeight = window.getHeight() * heightPercent
    private val sizeWidth = sizeHeight * 1.4725

    private val boxSide = sizeHeight * ((1 - PartlySaneSkies.config.auctionHouseItemPadding * 5) / 4)
    private val pad = sizeHeight * PartlySaneSkies.config.auctionHouseItemPadding

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


    private val sideBarHeight = sideBarHeightPercent * sizeHeight
    private val sideBarWidth = sizeWidth * sideBarWidthPercent
    private val itemInformationBarX = -(sideBarWidth * sideBarPadding)
    private val auctionInformationBarX = (sideBarWidth * (sideBarPadding - 1)) + backgroundImage.getWidth()

    private val itemInformationBar = ItemInformationBar(itemInformationBarX.pixels , CenterConstraint(), sideBarHeight.pixels, sideBarWidth.pixels, textScale)


    private val marketInformationBar = MarketInformationBar(auctionInformationBarX.pixels , CenterConstraint(), sideBarHeight.pixels, sideBarWidth.pixels, textScale)

    private val categoriesBarHeight = 0.1665 * sizeHeight
    private val categoriesBarY = backgroundImage.getTop() - pad - categoriesBarHeight
    private val categoriesBar = CategoriesBar(CenterConstraint(), categoriesBarY.pixels, categoriesBarHeight.pixels, sizeWidth.pixels, defaultAuctionInventory)


    private val settingsBarY = backgroundImage.getBottom() + pad
    private val settingsBar = SettingsBar(CenterConstraint(), settingsBarY.pixels, categoriesBarHeight.pixels, sizeWidth.pixels, defaultAuctionInventory)

    private val auctions = getAuctions(defaultAuctionInventory)

    init {
        marketInformationBar.setChildOf(backgroundImage)
        itemInformationBar.setChildOf(backgroundImage)
        categoriesBar.setChildOf(window)
        categoriesBar.loadItemInformationBar(itemInformationBar)
        settingsBar.setChildOf(window)
        settingsBar.loadItemInformationBar(itemInformationBar)

        for (row in 0 until 4) {
            for (column in 0 until 6) {
                val x = ((boxSide + pad) * column + pad).toFloat()
                val y = ((boxSide + pad) * row + pad).toFloat()

                try {
                    auctions[row][column]
                        .setX(x.pixels)
                        .setY(y.pixels)
                        .setHeight(boxSide.pixels)
                        .setChildOf(backgroundImage)

                    auctions[row][column].loadItemInformationBar(itemInformationBar)
                    auctions[row][column].loadAuctionInformationBar(marketInformationBar)
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

            list.add(AuctionElement(i, inventory.getStackInSlot(i), 0.pixels, 0.pixels, 0.pixels, textScale))
        }
        return list
    }


    override fun onDrawScreen(matrixStack: UMatrixStack, mouseX: Int, mouseY: Int, partialTicks: Float) {
        super.onDrawScreen(matrixStack, mouseX, mouseY, partialTicks)

        itemInformationBar.update()
        marketInformationBar.update()
        categoriesBar.update()

        for (row in auctions) {
            for (auction in row) {
                auction.highlightIfCheapBin()
            }
        }

        window.draw(matrixStack)
    }





    companion object {
        @SubscribeEvent
        fun onGuiOpen(event: ClientTickEvent) {
            val gui = PartlySaneSkies.minecraft.currentScreen
            if (gui == null) {
                return
            }
//            ChatUtils.sendClientMessage("A gui has been opened")

            if (gui !is GuiChest) {
                return
            }
            if (!isAhGui(MinecraftUtils.getSeparateUpperLowerInventories(gui)[0])) {
//                ChatUtils.sendClientMessage("Not AH Gui")
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
//            val inventory = MinecraftUtils.getSeparateUpperLowerInventories(event.gui)[0]

//            ChatUtils.sendClientMessage("Opening menu")
            val inventory = MinecraftUtils.getSeparateUpperLowerInventories(gui)[0]
//            event.isCanceled = true
            if (isAuctionHouseFullyLoaded(inventory!!)) {
                val ahGui = AuctionHouseGui(inventory)
                PartlySaneSkies.minecraft.displayGuiScreen(ahGui)
                return
            }
            openMenu()
        }

        fun isAhGui(inventory: IInventory?): Boolean {
            if (inventory == null) {
                return true
            }

            if (PartlySaneSkies.minecraft.currentScreen !is GuiChest) {
                return false
            }
            return inventory.displayName.formattedText.removeColorCodes()
                .contains("Auctions Browser") || inventory.displayName.formattedText.removeColorCodes()
                .contains("Auctions: \"")
        }

        private fun openMenu() {
            var inventory = MinecraftUtils.getSeparateUpperLowerInventories(PartlySaneSkies.minecraft.currentScreen)[0]

            if (inventory == null) {
                ChatUtils.sendClientMessage("Error opening auction house. Inventory not open.")

                return
            }
            if (isAuctionHouseFullyLoaded(inventory)) {
//                ChatUtils.sendClientMessage("Auction house is already loaded")
                inventory = MinecraftUtils.getSeparateUpperLowerInventories(PartlySaneSkies.minecraft.currentScreen)[0]
                val gui = AuctionHouseGui(inventory!!)
                PartlySaneSkies.minecraft.displayGuiScreen(gui)
            } else {
                Thread {
                    PartlySaneSkies.minecraft.addScheduledTask {
//                        ChatUtils.sendClientMessage("Trying again")
                        openMenu()
                    }
                }.start()
            }
        }

        private fun isAuctionHouseFullyLoaded(inventory: IInventory): Boolean {
//            ChatUtils.sendClientMessage("Checking if is loaded")
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
//                        ChatUtils.sendClientMessage("Slot $i is broken")

                        return false
                    } else if (Item.getIdFromItem(inventory.getStackInSlot(53).item) != 264) {
                        continue
                    }
//                    ChatUtils.sendClientMessage("Slot $i is broken")
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
}