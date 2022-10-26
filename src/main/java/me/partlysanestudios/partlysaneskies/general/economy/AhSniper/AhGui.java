package me.partlysanestudios.partlysaneskies.general.economy.AhSniper;

import java.awt.Color;

import gg.essential.elementa.ElementaVersion;
import gg.essential.elementa.UIComponent;
import gg.essential.elementa.WindowScreen;
import gg.essential.elementa.components.UIBlock;
import gg.essential.elementa.components.UIImage;
import gg.essential.elementa.components.UIWrappedText;
import gg.essential.elementa.constraints.CenterConstraint;
import gg.essential.elementa.constraints.PixelConstraint;
import me.partlysanestudios.partlysaneskies.Main;
import me.partlysanestudios.partlysaneskies.utils.Utils;
import me.partlysanestudios.partlysaneskies.utils.guicomponents.UIItemRender;
import net.minecraft.inventory.IInventory;

public class AhGui extends WindowScreen {

    float mainBoxHeight = 407.4f;
    float mainBoxWidth = mainBoxHeight * (6f / 4f);

    UIComponent mainBox = new UIBlock()
            .setX(new CenterConstraint())
            .setY(new CenterConstraint())
            .setWidth(widthScaledConstraint(mainBoxWidth))
            .setHeight(widthScaledConstraint(mainBoxHeight))
            .setColor(Main.BASE_COLOR)
            .setChildOf(getWindow());

    UIComponent bottomBar = new UIBlock() 
            .setX(new CenterConstraint())
            .setY(new PixelConstraint(mainBox.getBottom() + widthScaledConstraint(15).getValue()))
            .setWidth(widthScaledConstraint(mainBoxWidth - 100))
            .setHeight(widthScaledConstraint(40))
            .setColor(Main.BASE_DARK_COLOR)
            .setChildOf(getWindow());
    
    UIComponent topBar = new UIBlock() 
            .setX(new CenterConstraint())
            .setY(new PixelConstraint(mainBox.getTop() - widthScaledConstraint(15 + 40).getValue()))
            .setWidth(widthScaledConstraint(mainBoxWidth - 100))
            .setHeight(widthScaledConstraint(40))
            .setColor(Main.BASE_DARK_COLOR)
            .setChildOf(getWindow());

    int numOfColumns = 6;
    int numOfRows = 4;
    float pad = 70;
    float boxSide = (mainBoxWidth - ((numOfColumns) * pad)) / numOfColumns;

    public AhGui(ElementaVersion version) {
        super(version);
        
        Utils.applyBackground(mainBox);
        Utils.applyBackground(bottomBar);
        Utils.applyBackground(topBar);
    }

    public void refreshGui(IInventory inventory) {
        Auction[][] auctions = AhSniper.getAuctions(inventory);
        for (int row = 0; row < numOfRows; row++) {
            for (int column = 0; column < numOfColumns; column++) {
                float x = (boxSide + pad) * column + pad / 2;
                float y = (boxSide + pad) * row + pad / 2;
                if (auctions[row][column] == null) {
                    continue;
                }
                try {
                    makeItemBox(auctions[row][column], x, y, mainBox);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    Utils.visPrint("Slot " + x + ", " + y + "had an exception.");
                }

            }
        }

        float itemBarLocationStep = (mainBoxWidth - 100)/6f;
        for (int itemBar = 1; itemBar < 6; itemBar ++) {
            new UIBlock()
                    .setX(widthScaledConstraint(itemBarLocationStep * itemBar))
                    .setY(new CenterConstraint())
                    .setWidth(widthScaledConstraint(2))
                    .setHeight(widthScaledConstraint(25))
                    .setColor(Main.DARK_ACCENT_COLOR)
                    .setChildOf(topBar);
        }
        UIComponent weaponsIcon = new UIBlock()
                .setX(widthScaledConstraint(itemBarLocationStep*0))
                .setY(new CenterConstraint())
                .setWidth(widthScaledConstraint(itemBarLocationStep))
                .setHeight(new PixelConstraint(topBar.getHeight()))
                .setColor(new Color(0,0,0, 0))
                .setChildOf(topBar);
        weaponsIcon.onMouseClickConsumer(event -> {
            Utils.clickOnSlot(0);
        });
        UIImage.ofResource("/assets/partlysaneskies/textures/gui/custom_ah/weapons_icon.png")
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setWidth(widthScaledConstraint(30))
                .setHeight(widthScaledConstraint(30))
                .setChildOf(weaponsIcon);

        UIComponent armorIcon = new UIBlock()
                .setX(widthScaledConstraint(itemBarLocationStep*1))
                .setY(new CenterConstraint())
                .setWidth(widthScaledConstraint(itemBarLocationStep))
                .setHeight(new PixelConstraint(topBar.getHeight()))
                .setColor(new Color(0,0,0,0))
                .setChildOf(topBar);
        armorIcon.onMouseClickConsumer(event -> {
            Utils.clickOnSlot(9);
        });
        UIImage.ofResource("/assets/partlysaneskies/textures/gui/custom_ah/armor_icon.png")
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setWidth(widthScaledConstraint(30))
                .setHeight(widthScaledConstraint(30))
                .setChildOf(armorIcon);


        UIComponent accessoriesIcon = new UIBlock()
                .setX(widthScaledConstraint(itemBarLocationStep*2))
                .setY(new CenterConstraint())
                .setWidth(widthScaledConstraint(itemBarLocationStep))
                .setHeight(new PixelConstraint(topBar.getHeight()))
                .setColor(new Color(0,0,0,0))
                .setChildOf(topBar);
        accessoriesIcon.onMouseClickConsumer(event -> {
            Utils.clickOnSlot(18);
        });
        UIImage.ofResource("/assets/partlysaneskies/textures/gui/custom_ah/accessories_icon.png")
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setWidth(widthScaledConstraint(30))
                .setHeight(widthScaledConstraint(30))
                .setChildOf(accessoriesIcon);

        UIComponent consumablesIcon = new UIBlock()
                .setX(widthScaledConstraint(itemBarLocationStep*3))
                .setY(new CenterConstraint())
                .setWidth(widthScaledConstraint(itemBarLocationStep))
                .setHeight(new PixelConstraint(topBar.getHeight()))
                .setColor(new Color(0,0,0,0))
                .setChildOf(topBar);
        consumablesIcon.onMouseClickConsumer(event -> {
            Utils.clickOnSlot(27);
        });
        UIImage.ofResource("/assets/partlysaneskies/textures/gui/custom_ah/consumables_icon.png")
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setWidth(widthScaledConstraint(30))
                .setHeight(widthScaledConstraint(30))
                    .setChildOf(consumablesIcon);

        UIComponent blocksIcon = new UIBlock()
                .setX(widthScaledConstraint(itemBarLocationStep*4))
                .setY(new CenterConstraint())
                .setWidth(widthScaledConstraint(itemBarLocationStep))
                .setHeight(new PixelConstraint(topBar.getHeight()))
                .setColor(new Color(0,0,0,0))
                .setChildOf(topBar);
        blocksIcon.onMouseClickConsumer(event -> {
            Utils.clickOnSlot(36);
        });
        UIImage.ofResource("/assets/partlysaneskies/textures/gui/custom_ah/block_icon.png")
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setWidth(widthScaledConstraint(30))
                .setHeight(widthScaledConstraint(30))
                .setChildOf(blocksIcon);

        UIComponent miscIcon = new UIBlock()
                .setX(widthScaledConstraint(itemBarLocationStep*5))
                .setY(new CenterConstraint())
                .setWidth(widthScaledConstraint(itemBarLocationStep))
                .setHeight(new PixelConstraint(topBar.getHeight()))
                .setColor(new Color(0,0,0,0))
                .setChildOf(topBar);
        miscIcon.onMouseClickConsumer(event -> {
            Utils.clickOnSlot(45);
        });
        UIImage.ofResource("/assets/partlysaneskies/textures/gui/custom_ah/misc_icon.png")
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setWidth(widthScaledConstraint(30))
                .setHeight(widthScaledConstraint(30))
                .setChildOf(miscIcon);








                // Bottom Bar

        itemBarLocationStep = (mainBoxWidth - 100)/8f;
        for (int itemBar = 1; itemBar < 8; itemBar++) {
            new UIBlock()
                    .setX(widthScaledConstraint(itemBarLocationStep * itemBar))
                    .setY(new CenterConstraint())
                    .setWidth(widthScaledConstraint(2))
                    .setHeight(widthScaledConstraint(25))
                    .setColor(Main.DARK_ACCENT_COLOR)
                    .setChildOf(bottomBar);
        }

        UIComponent leftArrowIcon = new UIBlock()
                .setX(widthScaledConstraint(itemBarLocationStep*0))
                .setY(new CenterConstraint())
                .setWidth(widthScaledConstraint(itemBarLocationStep))
                .setHeight(new PixelConstraint(topBar.getHeight()))
                .setColor(new Color(0,0,0,0))
                .setChildOf(bottomBar);
        leftArrowIcon.onMouseClickConsumer(event -> {
            Utils.clickOnSlot(46);
        });
        UIImage.ofResource("/assets/partlysaneskies/textures/gui/custom_ah/left_arrow_icon.png")
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setWidth(widthScaledConstraint(30))
                .setHeight(widthScaledConstraint(30))
                .setChildOf(leftArrowIcon);
            
        UIComponent resetIcon = new UIBlock()
                .setX(widthScaledConstraint(itemBarLocationStep*1))
                .setY(new CenterConstraint())
                .setWidth(widthScaledConstraint(itemBarLocationStep))
                .setHeight(new PixelConstraint(topBar.getHeight()))
                .setColor(new Color(0,0,0,0))
                .setChildOf(bottomBar);
        resetIcon.onMouseClickConsumer(event -> {
            Utils.clickOnSlot(47);
        });
        UIImage.ofResource("/assets/partlysaneskies/textures/gui/custom_ah/reset_icon.png")
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setWidth(widthScaledConstraint(30))
                .setHeight(widthScaledConstraint(30))
                .setChildOf(resetIcon);
        
        UIComponent searchIcon = new UIBlock()
                .setX(widthScaledConstraint(itemBarLocationStep*2))
                .setY(new CenterConstraint())
                .setWidth(widthScaledConstraint(itemBarLocationStep))
                .setHeight(new PixelConstraint(topBar.getHeight()))
                .setColor(new Color(0,0,0,0))
                .setChildOf(bottomBar);
        searchIcon.onMouseClickConsumer(event -> {
            Utils.clickOnSlot(48);
        });
        UIImage.ofResource("/assets/partlysaneskies/textures/gui/custom_ah/search_icon.png")
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setWidth(widthScaledConstraint(30))
                .setHeight(widthScaledConstraint(30))
                .setChildOf(searchIcon);
        

        UIComponent goBackIcon = new UIBlock()
                .setX(widthScaledConstraint(itemBarLocationStep*3))
                .setY(new CenterConstraint())
                .setWidth(widthScaledConstraint(itemBarLocationStep))
                .setHeight(new PixelConstraint(topBar.getHeight()))
                .setColor(new Color(0,0,0,0))
                .setChildOf(bottomBar);
        goBackIcon.onMouseClickConsumer(event -> {
            Utils.clickOnSlot(49);
        });
        UIImage.ofResource("/assets/partlysaneskies/textures/gui/custom_ah/go_back_icon.png")
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setWidth(widthScaledConstraint(30))
                .setHeight(widthScaledConstraint(30))
                .setChildOf(goBackIcon);

        UIComponent filterIcon = new UIBlock()
                .setX(widthScaledConstraint(itemBarLocationStep*4))
                .setY(new CenterConstraint())
                .setWidth(widthScaledConstraint(itemBarLocationStep))
                .setHeight(new PixelConstraint(topBar.getHeight()))
                .setColor(new Color(0,0,0,0))
                .setChildOf(bottomBar);
        filterIcon.onMouseClickConsumer(event -> {
            Utils.clickOnSlot(50);
        });
        UIImage.ofResource("/assets/partlysaneskies/textures/gui/custom_ah/filter_icon.png")
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setWidth(widthScaledConstraint(30))
                .setHeight(widthScaledConstraint(30))
                .setChildOf(filterIcon);

        UIComponent rarityIcon = new UIBlock()
                .setX(widthScaledConstraint(itemBarLocationStep*5))
                .setY(new CenterConstraint())
                .setWidth(widthScaledConstraint(itemBarLocationStep))
                .setHeight(new PixelConstraint(topBar.getHeight()))
                .setColor(new Color(0,0,0,0))
                .setChildOf(bottomBar);
        rarityIcon.onMouseClickConsumer(event -> {
            if (event.getMouseButton() == 0){
                Utils.clickOnSlot(51);
            }
            else {
                Utils.rightClickOnSlot(51);
            }
        });
        UIImage.ofResource("/assets/partlysaneskies/textures/gui/custom_ah/rarity_icon.png")
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setWidth(widthScaledConstraint(30))
                .setHeight(widthScaledConstraint(30))
                .setChildOf(rarityIcon);

        UIComponent binFilterIcon = new UIBlock()
                .setX(widthScaledConstraint(itemBarLocationStep*6))
                .setY(new CenterConstraint())
                .setWidth(widthScaledConstraint(itemBarLocationStep))
                .setHeight(new PixelConstraint(topBar.getHeight()))
                .setColor(new Color(0,0,0,0))
                .setChildOf(bottomBar);
        binFilterIcon.onMouseClickConsumer(event -> {
            if (event.getMouseButton() == 0){
                Utils.clickOnSlot(52);
            }
            else {
                Utils.rightClickOnSlot(52);
            }
        });
        UIImage.ofResource("/assets/partlysaneskies/textures/gui/custom_ah/bin_filter_icon.png")
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setWidth(widthScaledConstraint(30))
                .setHeight(widthScaledConstraint(30))
                .setChildOf(binFilterIcon);
        
        UIComponent rightArrowIcon = new UIBlock()
                .setX(widthScaledConstraint(itemBarLocationStep*7))
                .setY(new CenterConstraint())
                .setWidth(widthScaledConstraint(itemBarLocationStep))
                .setHeight(new PixelConstraint(topBar.getHeight()))
                .setColor(new Color(0,0,0,0))
                .setChildOf(bottomBar);
        rightArrowIcon.onMouseClickConsumer(event -> {
            Utils.clickOnSlot(53);
        });
        UIImage.ofResource("/assets/partlysaneskies/textures/gui/custom_ah/right_arrow_icon.png")
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setWidth(widthScaledConstraint(30))
                .setHeight(widthScaledConstraint(30))
                .setChildOf(rightArrowIcon);


        int paneType = inventory.getStackInSlot(1).getItemDamage();

        armorIcon.setColor(new Color(0,0,0,0));
        weaponsIcon.setColor(new Color(0,0,0,0));
        accessoriesIcon.setColor(new Color(0,0,0,0));
        consumablesIcon.setColor(new Color(0,0,0,0));
        blocksIcon.setColor(new Color(0,0,0,0));
        miscIcon.setColor(new Color(0,0,0,0));

        switch (paneType) {
            
            case 1:
                weaponsIcon.setColor(Utils.applyOpacityToColor(Main.ACCENT_COLOR, 75));
                break;
            case 11:
                armorIcon.setColor(Utils.applyOpacityToColor(Main.ACCENT_COLOR, 75));
                break;
            case 13:
                accessoriesIcon.setColor(Utils.applyOpacityToColor(Main.ACCENT_COLOR, 75));
                break;
            case 14:
                consumablesIcon.setColor(Utils.applyOpacityToColor(Main.ACCENT_COLOR, 75));
                break;
            case 12:
                blocksIcon.setColor(Utils.applyOpacityToColor(Main.ACCENT_COLOR, 75));
                break;
            case 10:
                miscIcon.setColor(Utils.applyOpacityToColor(Main.ACCENT_COLOR, 75));
                break;

            default:

                Utils.visPrint("none " + paneType);
        }
    }

    public void makeItemBox(Auction auction, float x, float y, UIComponent parent) throws NullPointerException {
        Color boxColor;

        if (auction.shouldHighlight()) {
            boxColor = Main.ACCENT_COLOR;
        } else {
            boxColor = Main.BASE_LIGHT_COLOR;
        }

        UIComponent box = new UIBlock()
                .setX(widthScaledConstraint(x))
                .setY(widthScaledConstraint(y))
                .setWidth(widthScaledConstraint(boxSide))
                .setHeight(widthScaledConstraint(boxSide))
                .setColor(boxColor)
                .setChildOf(mainBox);

        box.onMouseClickConsumer(event -> {
            auction.selectAuction();
        });

        new UIItemRender(auction.getItem())
                .setItemScale(widthScaledConstraint(2f))
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setWidth(widthScaledConstraint(boxSide))
                .setHeight(widthScaledConstraint(boxSide))
                .setChildOf(box);

        // auction.setBox(box);

        new UIWrappedText(auction.getName(), true, null, true)
                .setX(new CenterConstraint())
                .setY(widthScaledConstraint(boxSide + 5))
                .setWidth(widthScaledConstraint(boxSide + (pad * .4f)))
                .setColor(Color.white)
                .setChildOf(box);
    }

    public float getWindowWidth() {
        return this.getWindow().getWidth();
    }

    private float getWidthScaleFactor() {
        return this.getWindow().getWidth() / 1097f;
    }

    // private float getHeightScaleFactor() {
    // return this.getWindow().getHeight() / 582f;
    // }

    private PixelConstraint widthScaledConstraint(float value) {
        return new PixelConstraint(value * getWidthScaleFactor());
    }

    // private PixelConstraint heightScaledConstraint(float value) {
    // return new PixelConstraint(value * getHeightScaleFactor());
    // }
}
