package me.partlysanestudios.partlysaneskies.auctionhouse.menu

import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIImage
import gg.essential.elementa.constraints.*
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.constraint
import gg.essential.elementa.dsl.pixels
import me.partlysanestudios.partlysaneskies.system.ThemeManager
import java.awt.Color

class AuctionHouseGui : WindowScreen(ElementaVersion.V2) {
    private val heightPercent = .333
    private val sideBarPercent = .667

    private val sizeHeight = window.getHeight() * heightPercent
    private val sizeWidth = sizeHeight * 1.4725

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
        .setChildOf(backgroundImage)

    private val auctionInformationBar = AuctionInformationBar((sideBarPercent * sizeWidth * 1.05 + backgroundImage.getWidth()).pixels , CenterConstraint(), (sideBarPercent * sizeWidth).pixels, (sizeHeight * 1.1).pixels, 1f)
        .setChildOf(backgroundImage)

    init {

    }
}