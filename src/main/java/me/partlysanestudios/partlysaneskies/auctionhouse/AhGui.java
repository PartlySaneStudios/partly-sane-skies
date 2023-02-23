package me.partlysanestudios.partlysaneskies.auctionhouse;

import java.awt.Color;
import java.util.List;

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

    UIComponent mainBox;

    UIComponent bottomBar;

    UIComponent topBar;

    UIComponent rightWindow;

    UIComponent leftWindow;

    int numOfColumns = 6;
    int numOfRows = 4;
    float pad = 70;
    float boxSide = (mainBoxWidth - ((numOfColumns) * pad)) / numOfColumns;

    public AhGui(ElementaVersion version) {
        super(version);
    }

    public void loadGui(IInventory inventory) {
        mainBox = new UIBlock()
            .setX(new CenterConstraint())
            .setY(new CenterConstraint())
            .setWidth(widthScaledConstraint(mainBoxWidth))
            .setHeight(widthScaledConstraint(mainBoxHeight))
            .setColor(Main.BASE_COLOR)
            .setChildOf(getWindow());
        

        bottomBar = new UIBlock()
        .setX(new CenterConstraint())
        .setY(new PixelConstraint(mainBox.getBottom() + widthScaledConstraint(15).getValue()))
        .setWidth(widthScaledConstraint(mainBoxWidth - 100))
        .setHeight(widthScaledConstraint(40))
        .setColor(Main.BASE_DARK_COLOR)
        .setChildOf(getWindow());

        topBar = new UIBlock()
            .setX(new CenterConstraint())
            .setY(new PixelConstraint(mainBox.getTop() - widthScaledConstraint(15 + 40).getValue()))
            .setWidth(widthScaledConstraint(mainBoxWidth - 100))
            .setHeight(widthScaledConstraint(40))
            .setColor(Main.BASE_DARK_COLOR)
            .setChildOf(getWindow());

        rightWindow = new UIBlock()
            .setX(new PixelConstraint(mainBox.getRight() + widthScaledConstraint(15).getValue()))
            .setY(new CenterConstraint())
            .setWidth(widthScaledConstraint(180))
            .setHeight(new PixelConstraint(bottomBar.getBottom() - topBar.getHeight()))
            .setColor(Main.BASE_DARK_COLOR)
            .setChildOf(getWindow());

        leftWindow = new UIBlock()
            .setX(new PixelConstraint(mainBox.getLeft() - widthScaledConstraint(15 + 180).getValue()))
            .setY(new CenterConstraint())
            .setWidth(widthScaledConstraint(180))
            .setHeight(new PixelConstraint(bottomBar.getBottom() - topBar.getHeight()))
            .setColor(Main.BASE_DARK_COLOR)
            .setChildOf(getWindow());

        itemInfoText = new UIWrappedText("", true, null, true)
            .setX(new CenterConstraint())
            .setY(widthScaledConstraint(50 * 1.5f))
            .setWidth(widthScaledConstraint(170))
            .setColor(Color.white)
            .setChildOf(rightWindow);

        itemInfoHeader = new UIWrappedText("", true, null, true)
            .setTextScale(widthScaledConstraint(1.5f))
            .setX(new CenterConstraint())
            .setY(widthScaledConstraint(30))
            .setWidth(widthScaledConstraint(170))
            .setColor(Color.white)
            .setChildOf(rightWindow);
        itemLore = new UIWrappedText("", true, null, true)
            .setX(new CenterConstraint())
            .setY(widthScaledConstraint(50 * 1.5f))
            .setWidth(widthScaledConstraint(170))
            .setColor(Color.white)
            .setChildOf(leftWindow);
        itemName = new UIWrappedText("", true, null, true)
            .setTextScale(widthScaledConstraint(1.5f))
            .setX(new CenterConstraint())
            .setY(widthScaledConstraint(30))
            .setWidth(widthScaledConstraint(170))
            .setColor(Color.white)
            .setChildOf(leftWindow);;
        
        Utils.applyBackground(mainBox);
        Utils.applyBackground(bottomBar);
        Utils.applyBackground(topBar);
        Utils.applyBackground(leftWindow);
        Utils.applyBackground(rightWindow);


        
        Auction[][] auctions = AhManager.getAuctions(inventory);
        for (int row = 0; row < numOfRows; row++) {
            for (int column = 0; column < numOfColumns; column++) {
                float x = (boxSide + pad) * column + pad / 4;
                float y = (boxSide + pad) * row + pad / 4;
                if (auctions[row][column] == null) {
                    continue;
                }
                try {
                    makeItemBox(auctions[row][column], x, y, mainBox);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

            }
        }

        float itemBarLocationStep = (mainBoxWidth - 100) / 6f;
        float itemBlockWidth = itemBarLocationStep*.75f;
        for (int itemBar = 1; itemBar < 6; itemBar++) {
            new UIBlock()
                    .setX(widthScaledConstraint(itemBarLocationStep * itemBar))
                    .setY(new CenterConstraint())
                    .setWidth(widthScaledConstraint(2))
                    .setHeight(widthScaledConstraint(25))
                    .setColor(Main.DARK_ACCENT_COLOR)
                    .setChildOf(topBar);
        }
        UIComponent weaponsIcon = new UIBlock()
                .setX(widthScaledConstraint(itemBarLocationStep * 0 + (itemBarLocationStep - itemBlockWidth)/2))
                .setY(new CenterConstraint())
                .setWidth(widthScaledConstraint(itemBlockWidth))
                .setHeight(new PixelConstraint(topBar.getHeight()))
                .setColor(new Color(0, 0, 0, 0))
                .setChildOf(topBar);
        weaponsIcon.onMouseClickConsumer(event -> {
            Utils.clickOnSlot(0);
        });
        weaponsIcon.onMouseEnterRunnable(() -> {
            ((UIWrappedText) itemName).setText(inventory.getStackInSlot(0).getDisplayName());
            ((UIWrappedText) itemLore).setText(Utils.getLoreAsString(inventory.getStackInSlot(0)));
        });
        weaponsIcon.onMouseLeaveRunnable(() -> {
            ((UIWrappedText) itemName).setText("");
            ((UIWrappedText) itemLore).setText("");
        });
        UIImage.ofResource("/assets/partlysaneskies/textures/gui/custom_ah/weapons_icon.png")
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setWidth(widthScaledConstraint(30))
                .setHeight(widthScaledConstraint(30))
                .setChildOf(weaponsIcon);

        UIComponent armorIcon = new UIBlock()
                .setX(widthScaledConstraint(itemBarLocationStep * 1 + (itemBarLocationStep - itemBlockWidth)/2))
                .setY(new CenterConstraint())
                .setWidth(widthScaledConstraint(itemBlockWidth))
                .setHeight(new PixelConstraint(topBar.getHeight()))
                .setColor(new Color(0, 0, 0, 0))
                .setChildOf(topBar);
        armorIcon.onMouseClickConsumer(event -> {
            Utils.clickOnSlot(9);
        });
        armorIcon.onMouseEnterRunnable(() -> {
            ((UIWrappedText) itemName).setText(inventory.getStackInSlot(9).getDisplayName());
            ((UIWrappedText) itemLore).setText(Utils.getLoreAsString(inventory.getStackInSlot(9)));
        });
        armorIcon.onMouseLeaveRunnable(() -> {
            ((UIWrappedText) itemName).setText("");
            ((UIWrappedText) itemLore).setText("");
        });
        UIImage.ofResource("/assets/partlysaneskies/textures/gui/custom_ah/armor_icon.png")
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setWidth(widthScaledConstraint(30))
                .setHeight(widthScaledConstraint(30))
                .setChildOf(armorIcon);

        UIComponent accessoriesIcon = new UIBlock()
                .setX(widthScaledConstraint(itemBarLocationStep * 2 + (itemBarLocationStep - itemBlockWidth)/2))
                .setY(new CenterConstraint())
                .setWidth(widthScaledConstraint(itemBlockWidth))
                .setHeight(new PixelConstraint(topBar.getHeight()))
                .setColor(new Color(0, 0, 0, 0))
                .setChildOf(topBar);
        accessoriesIcon.onMouseClickConsumer(event -> {
            Utils.clickOnSlot(18);
        });
        accessoriesIcon.onMouseEnterRunnable(() -> {
            ((UIWrappedText) itemName).setText(inventory.getStackInSlot(18).getDisplayName());
            ((UIWrappedText) itemLore).setText(Utils.getLoreAsString(inventory.getStackInSlot(18)));
        });
        accessoriesIcon.onMouseLeaveRunnable(() -> {
            ((UIWrappedText) itemName).setText("");
            ((UIWrappedText) itemLore).setText("");
        });
        UIImage.ofResource("/assets/partlysaneskies/textures/gui/custom_ah/accessories_icon.png")
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setWidth(widthScaledConstraint(30))
                .setHeight(widthScaledConstraint(30))
                .setChildOf(accessoriesIcon);

        UIComponent consumablesIcon = new UIBlock()
                .setX(widthScaledConstraint(itemBarLocationStep * 3+  (itemBarLocationStep - itemBlockWidth)/2))
                .setY(new CenterConstraint())
                .setWidth(widthScaledConstraint(itemBlockWidth))
                .setHeight(new PixelConstraint(topBar.getHeight()))
                .setColor(new Color(0, 0, 0, 0))
                .setChildOf(topBar);
        consumablesIcon.onMouseClickConsumer(event -> {
            Utils.clickOnSlot(27);
        });
        consumablesIcon.onMouseEnterRunnable(() -> {
            ((UIWrappedText) itemName).setText(inventory.getStackInSlot(27).getDisplayName());
            ((UIWrappedText) itemLore).setText(Utils.getLoreAsString(inventory.getStackInSlot(27)));
        });
        consumablesIcon.onMouseLeaveRunnable(() -> {
            ((UIWrappedText) itemName).setText("");
            ((UIWrappedText) itemLore).setText("");
        });
        UIImage.ofResource("/assets/partlysaneskies/textures/gui/custom_ah/consumables_icon.png")
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setWidth(widthScaledConstraint(30))
                .setHeight(widthScaledConstraint(30))
                .setChildOf(consumablesIcon);

        UIComponent blocksIcon = new UIBlock()
                .setX(widthScaledConstraint(itemBarLocationStep * 4 + (itemBarLocationStep - itemBlockWidth)/2))
                .setY(new CenterConstraint())
                .setWidth(widthScaledConstraint(itemBlockWidth))
                .setHeight(new PixelConstraint(topBar.getHeight()))
                .setColor(new Color(0, 0, 0, 0))
                .setChildOf(topBar);
        blocksIcon.onMouseClickConsumer(event -> {
            Utils.clickOnSlot(36);
        });
        blocksIcon.onMouseEnterRunnable(() -> {
            ((UIWrappedText) itemName).setText(inventory.getStackInSlot(36).getDisplayName());
            ((UIWrappedText) itemLore).setText(Utils.getLoreAsString(inventory.getStackInSlot(36)));
        });
        blocksIcon.onMouseLeaveRunnable(() -> {
            ((UIWrappedText) itemName).setText("");
            ((UIWrappedText) itemLore).setText("");
        });
        UIImage.ofResource("/assets/partlysaneskies/textures/gui/custom_ah/block_icon.png")
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setWidth(widthScaledConstraint(30))
                .setHeight(widthScaledConstraint(30))
                .setChildOf(blocksIcon);

        UIComponent miscIcon = new UIBlock()
                .setX(widthScaledConstraint(itemBarLocationStep * 5 + (itemBarLocationStep - itemBlockWidth)/2))
                .setY(new CenterConstraint())
                .setWidth(widthScaledConstraint(itemBlockWidth))
                .setHeight(new PixelConstraint(topBar.getHeight()))
                .setColor(new Color(0, 0, 0, 0))
                .setChildOf(topBar);
        miscIcon.onMouseClickConsumer(event -> {
            Utils.clickOnSlot(45);
        });
        miscIcon.onMouseEnterRunnable(() -> {
            ((UIWrappedText) itemName).setText(inventory.getStackInSlot(45).getDisplayName());
            ((UIWrappedText) itemLore).setText(Utils.getLoreAsString(inventory.getStackInSlot(45)));
        });
        miscIcon.onMouseLeaveRunnable(() -> {
            ((UIWrappedText) itemName).setText("");
            ((UIWrappedText) itemLore).setText("");
        });
        UIImage.ofResource("/assets/partlysaneskies/textures/gui/custom_ah/misc_icon.png")
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setWidth(widthScaledConstraint(30))
                .setHeight(widthScaledConstraint(30))
                .setChildOf(miscIcon);

        // Bottom Bar

        itemBarLocationStep = (mainBoxWidth - 100) / 8f;
        itemBlockWidth = itemBarLocationStep*.75f;
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
                .setX(widthScaledConstraint(itemBarLocationStep * 0 + (itemBarLocationStep - itemBlockWidth)/2))
                .setY(new CenterConstraint())
                .setWidth(widthScaledConstraint(itemBlockWidth))
                .setHeight(new PixelConstraint(topBar.getHeight()))
                .setColor(new Color(0, 0, 0, 0))
                .setChildOf(bottomBar);
        leftArrowIcon.onMouseClickConsumer(event -> {
            Utils.clickOnSlot(46);
        });
        leftArrowIcon.onMouseEnterRunnable(() -> {
            ((UIWrappedText) itemName).setText(inventory.getStackInSlot(46).getDisplayName());
            ((UIWrappedText) itemLore).setText(Utils.getLoreAsString(inventory.getStackInSlot(46)));
        });
        leftArrowIcon.onMouseLeaveRunnable(() -> {
            ((UIWrappedText) itemName).setText("");
            ((UIWrappedText) itemLore).setText("");
        });
        UIImage.ofResource("/assets/partlysaneskies/textures/gui/custom_ah/left_arrow_icon.png")
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setWidth(widthScaledConstraint(30))
                .setHeight(widthScaledConstraint(30))
                .setChildOf(leftArrowIcon);

        UIComponent resetIcon = new UIBlock()
                .setX(widthScaledConstraint(itemBarLocationStep * 1 + (itemBarLocationStep - itemBlockWidth)/2))
                .setY(new CenterConstraint())
                .setWidth(widthScaledConstraint(itemBlockWidth))
                .setHeight(new PixelConstraint(topBar.getHeight()))
                .setColor(new Color(0, 0, 0, 0))
                .setChildOf(bottomBar);
        resetIcon.onMouseClickConsumer(event -> {
            Utils.clickOnSlot(47);
        });
        resetIcon.onMouseEnterRunnable(() -> {
            ((UIWrappedText) itemName).setText(inventory.getStackInSlot(47).getDisplayName());
            ((UIWrappedText) itemLore).setText(Utils.getLoreAsString(inventory.getStackInSlot(47)));
        });
        resetIcon.onMouseLeaveRunnable(() -> {
            ((UIWrappedText) itemName).setText("");
            ((UIWrappedText) itemLore).setText("");
        });
        UIImage.ofResource("/assets/partlysaneskies/textures/gui/custom_ah/reset_icon.png")
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setWidth(widthScaledConstraint(30))
                .setHeight(widthScaledConstraint(30))
                .setChildOf(resetIcon);

        UIComponent searchIcon = new UIBlock()
                .setX(widthScaledConstraint(itemBarLocationStep * 2 + (itemBarLocationStep - itemBlockWidth)/2))
                .setY(new CenterConstraint())
                .setWidth(widthScaledConstraint(itemBlockWidth))
                .setHeight(new PixelConstraint(topBar.getHeight()))
                .setColor(new Color(0, 0, 0, 0))
                .setChildOf(bottomBar);
        searchIcon.onMouseClickConsumer(event -> {
            Utils.clickOnSlot(48);
        });
        searchIcon.onMouseEnterRunnable(() -> {
            ((UIWrappedText) itemName).setText(inventory.getStackInSlot(48).getDisplayName());
            ((UIWrappedText) itemLore).setText(Utils.getLoreAsString(inventory.getStackInSlot(48)));
        });
        searchIcon.onMouseLeaveRunnable(() -> {
            ((UIWrappedText) itemName).setText("");
            ((UIWrappedText) itemLore).setText("");
        });
        UIImage.ofResource("/assets/partlysaneskies/textures/gui/custom_ah/search_icon.png")
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setWidth(widthScaledConstraint(30))
                .setHeight(widthScaledConstraint(30))
                .setChildOf(searchIcon);

        UIComponent goBackIcon = new UIBlock()
                .setX(widthScaledConstraint(itemBarLocationStep * 3 + (itemBarLocationStep - itemBlockWidth)/2))
                .setY(new CenterConstraint())
                .setWidth(widthScaledConstraint(itemBlockWidth))
                .setHeight(new PixelConstraint(topBar.getHeight()))
                .setColor(new Color(0, 0, 0, 0))
                .setChildOf(bottomBar);
        goBackIcon.onMouseClickConsumer(event -> {
            Utils.clickOnSlot(49);
        });
        goBackIcon.onMouseEnterRunnable(() -> {
            ((UIWrappedText) itemName).setText(inventory.getStackInSlot(49).getDisplayName());
            ((UIWrappedText) itemLore).setText(Utils.getLoreAsString(inventory.getStackInSlot(49)));
        });
        goBackIcon.onMouseLeaveRunnable(() -> {
            ((UIWrappedText) itemName).setText("");
            ((UIWrappedText) itemLore).setText("");
        });
        UIImage.ofResource("/assets/partlysaneskies/textures/gui/custom_ah/go_back_icon.png")
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setWidth(widthScaledConstraint(30))
                .setHeight(widthScaledConstraint(30))
                .setChildOf(goBackIcon);

        UIComponent filterIcon = new UIBlock()
                .setX(widthScaledConstraint(itemBarLocationStep * 4 + (itemBarLocationStep - itemBlockWidth)/2))
                .setY(new CenterConstraint())
                .setWidth(widthScaledConstraint(itemBlockWidth))
                .setHeight(new PixelConstraint(topBar.getHeight()))
                .setColor(new Color(0, 0, 0, 0))
                .setChildOf(bottomBar);
        filterIcon.onMouseClickConsumer(event -> {
            Utils.clickOnSlot(50);
        });
        filterIcon.onMouseEnterRunnable(() -> {
            ((UIWrappedText) itemName).setText(inventory.getStackInSlot(50).getDisplayName());
            ((UIWrappedText) itemLore).setText(Utils.getLoreAsString(inventory.getStackInSlot(50)));
        });
        filterIcon.onMouseLeaveRunnable(() -> {
            ((UIWrappedText) itemName).setText("");
            ((UIWrappedText) itemLore).setText("");
        });
        List<String> sortLoreList = Utils.getLore(inventory.getStackInSlot(50));

        String sortSelectedLine = "";

        String sortImageName = "";

        for (String line : sortLoreList) {
            if (line.contains("▶")) {
                sortSelectedLine = line;
                break;
            }
        }
        sortSelectedLine = Utils.removeColorCodes(sortSelectedLine);

        if (sortSelectedLine.toLowerCase().contains("highest")) {
            sortImageName = "price_high_low";
        } 
        else if (sortSelectedLine.toLowerCase().contains("lowest")) {
            sortImageName = "price_low_high";
        }
        else if (sortSelectedLine.toLowerCase().contains("soon")) {
            sortImageName = "ending_soon";
        }
        else if (sortSelectedLine.toLowerCase().contains("most")) {
            sortImageName = "random";
        }
        

        UIImage.ofResource("/assets/partlysaneskies/textures/gui/custom_ah/sort_filter/" + sortImageName + ".png")
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setWidth(widthScaledConstraint(30))
                .setHeight(widthScaledConstraint(30))
                .setChildOf(filterIcon);

        UIComponent rarityIcon = new UIBlock()
                .setX(widthScaledConstraint(itemBarLocationStep * 5 + (itemBarLocationStep - itemBlockWidth)/2))
                .setY(new CenterConstraint())
                .setWidth(widthScaledConstraint(itemBlockWidth))
                .setHeight(new PixelConstraint(topBar.getHeight()))
                .setColor(new Color(0, 0, 0, 0))
                .setChildOf(bottomBar);
        rarityIcon.onMouseClickConsumer(event -> {
            if (event.getMouseButton() == 0) {
                Utils.clickOnSlot(51);
            } else {
                Utils.rightClickOnSlot(51);
            }
        });
        rarityIcon.onMouseEnterRunnable(() -> {
            ((UIWrappedText) itemName).setText(inventory.getStackInSlot(51).getDisplayName());
            ((UIWrappedText) itemLore).setText(Utils.getLoreAsString(inventory.getStackInSlot(51)));
        });
        rarityIcon.onMouseLeaveRunnable(() -> {
            ((UIWrappedText) itemName).setText("");
            ((UIWrappedText) itemLore).setText("");
        });
            // ▶
        
        sortLoreList = Utils.getLore(inventory.getStackInSlot(51));

        sortSelectedLine = "";

        sortImageName = "no_filter";

        for (String line : sortLoreList) {
            if (line.contains("▶")) {
                sortSelectedLine = line;
                break;
            }
        }
        sortSelectedLine = Utils.removeColorCodes(sortSelectedLine);

        
        if (sortSelectedLine.toLowerCase().contains("uncommon")) {
            sortImageName = "uncommon";
        }
        else if (sortSelectedLine.toLowerCase().contains("common")) {
            sortImageName = "common";
        } 
        else if (sortSelectedLine.toLowerCase().contains("rare")) {
            sortImageName = "rare";
        }
        else if (sortSelectedLine.toLowerCase().contains("epic")) {
            sortImageName = "epic";
        }
        else if (sortSelectedLine.toLowerCase().contains("legendary")) {
            sortImageName = "legendary";
        }
        else if (sortSelectedLine.toLowerCase().contains("special")) {
            sortImageName = "special";
        }
        else if (sortSelectedLine.toLowerCase().contains("very special")) {
            sortImageName = "special";
        }
        else if (sortSelectedLine.toLowerCase().contains("divine")) {
            sortImageName = "divine";
        }
        else if (sortSelectedLine.toLowerCase().contains("mythic")) {
            sortImageName = "mythic";
        }
        else if (sortSelectedLine.toLowerCase().contains("unobtainable")) {
            sortImageName = "unobtainable";
        }

        UIImage.ofResource("/assets/partlysaneskies/textures/gui/custom_ah/rarirty_filter/" + sortImageName + ".png")
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setWidth(widthScaledConstraint(30))
                .setHeight(widthScaledConstraint(30))
                .setChildOf(rarityIcon);

        UIComponent binFilterIcon = new UIBlock()
                .setX(widthScaledConstraint(itemBarLocationStep * 6 + (itemBarLocationStep - itemBlockWidth)/2))
                .setY(new CenterConstraint())
                .setWidth(widthScaledConstraint(itemBlockWidth))
                .setHeight(new PixelConstraint(topBar.getHeight()))
                .setColor(new Color(0, 0, 0, 0))
                .setChildOf(bottomBar);
        binFilterIcon.onMouseClickConsumer(event -> {
            if (event.getMouseButton() == 0) {
                Utils.clickOnSlot(52);
            } else {
                Utils.rightClickOnSlot(52);
            }
        });
        binFilterIcon.onMouseEnterRunnable(() -> {
            ((UIWrappedText) itemName).setText(inventory.getStackInSlot(52).getDisplayName());
            ((UIWrappedText) itemLore).setText(Utils.getLoreAsString(inventory.getStackInSlot(52)));
        });
        binFilterIcon.onMouseLeaveRunnable(() -> {
            ((UIWrappedText) itemName).setText("");
            ((UIWrappedText) itemLore).setText("");
        });

        List<String> binLoreList = Utils.getLore(inventory.getStackInSlot(52));

        String binSelectedLine = "";

        String binimageName = "all";

        for (String line : binLoreList) {
            if (line.contains("▶")) {
                binSelectedLine = line;
                break;
            }
        }
        binSelectedLine = Utils.removeColorCodes(binSelectedLine);

        
        if (binSelectedLine.toLowerCase().contains("bin only")) {
            binimageName = "bin_only";
        }
        else if (binSelectedLine.toLowerCase().contains("auctions only")) {
            binimageName = "auction_only";
        } 

        UIImage.ofResource("/assets/partlysaneskies/textures/gui/custom_ah/type/" + binimageName + ".png")
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setWidth(widthScaledConstraint(30))
                .setHeight(widthScaledConstraint(30))
                .setChildOf(binFilterIcon);

        UIComponent rightArrowIcon = new UIBlock()
                .setX(widthScaledConstraint(itemBarLocationStep * 7 + (itemBarLocationStep - itemBlockWidth)/2))
                .setY(new CenterConstraint())
                .setWidth(widthScaledConstraint(itemBlockWidth))
                .setHeight(new PixelConstraint(topBar.getHeight()))
                .setColor(new Color(0, 0, 0, 0))
                .setChildOf(bottomBar);
        rightArrowIcon.onMouseClickConsumer(event -> {
            Utils.clickOnSlot(53);
        });
        rightArrowIcon.onMouseEnterRunnable(() -> {
            ((UIWrappedText) itemName).setText(inventory.getStackInSlot(53).getDisplayName());
            ((UIWrappedText) itemLore).setText(Utils.getLoreAsString(inventory.getStackInSlot(53)));
        });
        rightArrowIcon.onMouseLeaveRunnable(() -> {
            ((UIWrappedText) itemName).setText("");
            ((UIWrappedText) itemLore).setText("");
        });
        UIImage.ofResource("/assets/partlysaneskies/textures/gui/custom_ah/right_arrow_icon.png")
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setWidth(widthScaledConstraint(30))
                .setHeight(widthScaledConstraint(30))
                .setChildOf(rightArrowIcon);

        int paneType = inventory.getStackInSlot(1).getItemDamage();

        armorIcon.setColor(new Color(0, 0, 0, 0));
        weaponsIcon.setColor(new Color(0, 0, 0, 0));
        accessoriesIcon.setColor(new Color(0, 0, 0, 0));
        consumablesIcon.setColor(new Color(0, 0, 0, 0));
        blocksIcon.setColor(new Color(0, 0, 0, 0));
        miscIcon.setColor(new Color(0, 0, 0, 0));

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
                break;
        }
    }

    UIComponent itemName;

    UIComponent itemLore;

    UIComponent itemInfoHeader;

    UIComponent itemInfoText;

    public void makeItemBox(Auction auction, float x, float y, UIComponent parent) throws NullPointerException {
        Color boxColor;

        if (auction.shouldHighlight()) {
            boxColor = Main.ACCENT_COLOR;
        } else {
            boxColor = Main.BASE_LIGHT_COLOR;
        }

        UIComponent backgroundBox = new UIBlock()
                .setX(widthScaledConstraint(x - boxSide * .25f))
                .setY(widthScaledConstraint(y))
                .setWidth(widthScaledConstraint(boxSide * 1.5f))
                .setHeight(widthScaledConstraint(boxSide * 2))
                .setColor(new Color(0, 0, 0, 0))
                .setChildOf(mainBox);

        UIComponent box = new UIBlock()
                .setX(new CenterConstraint())
                .setY(widthScaledConstraint(0))
                .setWidth(widthScaledConstraint(boxSide))
                .setHeight(widthScaledConstraint(boxSide))
                .setColor(boxColor)
                .setChildOf(backgroundBox);

        backgroundBox.onMouseClickConsumer(event -> {
            auction.selectAuction();
        });

        backgroundBox.onMouseEnterRunnable(() -> {
            ((UIWrappedText) itemName).setText(auction.getName());
            ((UIWrappedText) itemLore).setText(auction.getLore());

            String header;
            if (auction.isBin()) {
                header = "Buy It Now Details:";
            } else {
                header = "Auction Details:";
            }
            ((UIWrappedText) itemInfoHeader).setText(header);

            String info = "";
            info += "&6Offer Information:\n\n\n";
            info += "&eSelling Price: \n&6" + Utils.formatNumber(auction.getPrice()) + "\n\n";
            info += "&eEnding In: \n&6" + auction.getFormattedEndingTime();

            info += "\n\n\n\n\n\n";

            info += "&eMarket Stats:\n\n\n";
            info += "&bCurrent Lowest Bin: \n&e" + Utils.formatNumber(Utils.round(auction.getLowestBin(), 2)) + "\n\n";
            info += "&bAverage Lowest Bin (Last Day): \n&e"
                    + Utils.formatNumber(Utils.round(auction.getAverageLowestBin(), 2)) + "\n\n";
            info += "&bItem Inflation: \n&e"
                    + Utils.formatNumber(
                            Utils.round((auction.getLowestBin() / auction.getAverageLowestBin()) * 100d, 2) - 100)
                    + "%\n\n";
            info += "&bItem Mark up: \n&e" + Utils.formatNumber(Utils.round(
                    (auction.getPrice() / auction.getLowestBin()) * 100 - 100,
                    2)) + "%\n";

            
            ((UIWrappedText) itemInfoText).setText(Utils.colorCodes(info));
        });

        backgroundBox.onMouseLeaveRunnable(() -> {
            ((UIWrappedText) itemName).setText("");
            ((UIWrappedText) itemLore).setText("");

            ((UIWrappedText) itemInfoHeader).setText("");
            ((UIWrappedText) itemInfoText).setText("");
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
                .setChildOf(backgroundBox);
    }

    public float getWindowWidth() {
        return this.getWindow().getWidth();
    }

    private float getWidthScaleFactor() {
        return this.getWindow().getWidth() / 1097f;
    }

    private PixelConstraint widthScaledConstraint(float value) {
        return new PixelConstraint(value * getWidthScaleFactor());
    }

}
