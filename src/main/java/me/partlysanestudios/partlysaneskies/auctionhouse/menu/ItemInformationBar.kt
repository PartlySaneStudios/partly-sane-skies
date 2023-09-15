package me.partlysanestudios.partlysaneskies.auctionhouse.menu

import gg.essential.elementa.UIComponent
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIImage
import gg.essential.elementa.components.UIWrappedText
import gg.essential.elementa.constraints.*
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.constraint
import gg.essential.elementa.dsl.pixels
import me.partlysanestudios.partlysaneskies.system.ThemeManager
import java.awt.Color

class ItemInformationBar(xConstraint: XConstraint, yConstraint: YConstraint, heightConstraint: HeightConstraint, widthConstraint: WidthConstraint, textScaleFactor: Float) {
    private val baseBlock: UIBlock = UIBlock().constrain {
        color = Color(0, 0, 0, 0).constraint
        x = xConstraint
        y = yConstraint
        height = heightConstraint
        width = widthConstraint
    }

    private val backgroundImage: UIImage = ThemeManager.getCurrentBackgroundUIImage().constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        height = heightConstraint
        width = widthConstraint
    } childOf baseBlock

    var headerString = ""

    var descriptionString = ""

    private val header: UIWrappedText = UIWrappedText(centered = true) constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        height = heightConstraint
        width = widthConstraint
        textScale = (1.25f * textScaleFactor).pixels
    } childOf backgroundImage

    private val description: UIWrappedText = UIWrappedText(centered = true) constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        height = heightConstraint
        width = widthConstraint
    } childOf backgroundImage


    init {
        description.setText(descriptionString)
        header.setText(headerString)
    }

    fun setChildOf(component: UIComponent) {
        baseBlock childOf component
    }

    fun loadAuction(auction: Auction) {
        headerString = auction.getName()
        descriptionString = auction.getLore()
    }

    fun clearInfo() {
        headerString = ""
        descriptionString = ""
    }

    fun update() {
        if (description.getText() != descriptionString) {
            description.setText(descriptionString)
        }

        if (header.getText() != headerString) {
            header.setText(headerString)
        }
    }

}