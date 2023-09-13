//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.auctionhouse;

import java.awt.Color;
import java.util.List;

import gg.essential.elementa.ElementaVersion;
import gg.essential.elementa.UIComponent;
import gg.essential.elementa.WindowScreen;
import gg.essential.elementa.components.ScrollComponent;
import gg.essential.elementa.components.UIBlock;
import gg.essential.elementa.components.UIWrappedText;
import gg.essential.elementa.constraints.CenterConstraint;
import gg.essential.elementa.constraints.PixelConstraint;
import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import me.partlysanestudios.partlysaneskies.system.ThemeManager;
import me.partlysanestudios.partlysaneskies.utils.StringUtils;
import me.partlysanestudios.partlysaneskies.utils.Utils;
import me.partlysanestudios.partlysaneskies.system.guicomponents.PSSButton;
import me.partlysanestudios.partlysaneskies.system.guicomponents.PSSItemRender;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

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
            .setColor(new Color(0, 0, 0, 0))
            .setChildOf(getWindow());
        

        bottomBar = new UIBlock()
        .setX(new CenterConstraint())
        .setY(new PixelConstraint(mainBox.getBottom() + widthScaledConstraint(15).getValue()))
        .setWidth(widthScaledConstraint(mainBoxWidth - 100))
        .setHeight(widthScaledConstraint(40))
        .setColor(new Color(0, 0, 0, 0))
        .setChildOf(getWindow());

        topBar = new UIBlock()
            .setX(new CenterConstraint())
            .setY(new PixelConstraint(mainBox.getTop() - widthScaledConstraint(15 + 40).getValue()))
            .setWidth(widthScaledConstraint(mainBoxWidth - 100))
            .setHeight(widthScaledConstraint(40))
            .setColor(new Color(0, 0, 0, 0))
            .setChildOf(getWindow());

        rightWindow = new ScrollComponent()
            .setX(new PixelConstraint(mainBox.getRight() + widthScaledConstraint(15).getValue()))
            .setY(new CenterConstraint())
            .setWidth(widthScaledConstraint(180))
            .setHeight(new PixelConstraint(bottomBar.getBottom() - topBar.getHeight()))
            .setColor(new Color(0, 0, 0, 0))
            .setChildOf(getWindow());

        leftWindow = new ScrollComponent()
            .setX(new PixelConstraint(mainBox.getLeft() - widthScaledConstraint(15 + 180).getValue()))
            .setY(new CenterConstraint())
            .setWidth(widthScaledConstraint(180))
            .setHeight(new PixelConstraint(bottomBar.getBottom() - topBar.getHeight()))
            .setColor(new Color(0, 0, 0, 0))
            .setChildOf(getWindow());
        
        itemName = new UIWrappedText("", true, null, true)
            .setTextScale(widthScaledConstraint(1.25f))
            .setX(new CenterConstraint())
            .setY(widthScaledConstraint(10))
            .setWidth(widthScaledConstraint(170))
            .setColor(Color.white)
            .setChildOf(leftWindow);
        itemLore = new UIWrappedText("", true, null, true)
            .setTextScale(widthScaledConstraint(1f))
            .setX(new CenterConstraint())
            .setY(widthScaledConstraint(40f))
            .setWidth(widthScaledConstraint(170))
            .setColor(Color.white)
            .setChildOf(leftWindow);
        
        itemInfoHeader = new UIWrappedText("", true, null, true)
            .setTextScale(widthScaledConstraint(1.25f))
            .setX(new CenterConstraint())
            .setY(widthScaledConstraint(10))
            .setWidth(widthScaledConstraint(170))
            .setColor(Color.white)
            .setChildOf(rightWindow);
        itemInfoText = new UIWrappedText("", true, null, true)
            .setTextScale(widthScaledConstraint(1f))
            .setX(new CenterConstraint())
            .setY(widthScaledConstraint(40))
            .setWidth(widthScaledConstraint(170))
            .setColor(Color.white)
            .setChildOf(rightWindow);
        
        Utils.applyBackground(mainBox);
        Utils.applyBackground(bottomBar);
        Utils.applyBackground(topBar);
        Utils.applyBackground(leftWindow);
        Utils.applyBackground(rightWindow);

        
        
        Auction[][] auctions = AhManager.getAuctions(inventory);
        for (int row = 0; row < numOfRows; row++) {
            for (int column = 0; column < numOfColumns; column++) {
                float x = (boxSide + pad) * column + pad / 2;
                float y = (boxSide + pad) * row + pad / 6;
                if (auctions[row][column] == null) {
                    continue;
                }
                try {
                    makeItemBox(auctions[row][column], x, y);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

            }
        }

        // Top Bar
        float itemBarLocationStep = (mainBoxWidth - 100) / 6f;
        float itemBlockWidth = itemBarLocationStep*.75f;
        for (int itemBar = 1; itemBar < 6; itemBar++) {
            new UIBlock()
                    .setX(widthScaledConstraint(itemBarLocationStep * itemBar))
                    .setY(new CenterConstraint())
                    .setWidth(widthScaledConstraint(2))
                    .setHeight(widthScaledConstraint(25))
                    .setColor(ThemeManager.getDarkAccentColor())
                    .setChildOf(topBar);
        }

        String[] topBarImagePaths = {
            "textures/gui/custom_ah/weapons_icon.png",
            "textures/gui/custom_ah/armor_icon.png",
            "textures/gui/custom_ah/accessories_icon.png",
            "textures/gui/custom_ah/consumables_icon.png",
            "textures/gui/custom_ah/block_icon.png",
            "textures/gui/custom_ah/misc_icon.png",
        };

        String[] topBarFurfSkyImagePaths = {
            "textures/gui/custom_ah/furfsky/weapons_icon.png",
            "textures/gui/custom_ah/furfsky/armor_icon.png",
            "textures/gui/custom_ah/furfsky/accessories_icon.png",
            "textures/gui/custom_ah/furfsky/consumables_icon.png",
            "textures/gui/custom_ah/furfsky/block_icon.png",
            "textures/gui/custom_ah/furfsky/misc_icon.png",
        };

        int paneType = inventory.getStackInSlot(1).getItemDamage();
        int iteratorId = -1;

        switch (paneType) {
            case 1:
                iteratorId = 0;
                break;
            case 11:
                iteratorId = 1;
                break;
            case 13:
                iteratorId = 2;
                break;
            case 14:
                iteratorId = 3;
                break;
            case 12:
                iteratorId = 4;
                break;
            case 10:
                iteratorId = 5;
                break;

            default:
                break;
        }
        
        for (int i = 0; i <= 45; i += 9) {

            final int slot = i;
            UIComponent icon = new UIBlock()
                .setX(widthScaledConstraint(itemBarLocationStep * i/9 + (itemBarLocationStep - itemBlockWidth)/2))
                .setY(new CenterConstraint())
                .setWidth(widthScaledConstraint(itemBlockWidth))
                .setHeight(new PixelConstraint(topBar.getHeight()))
                .setColor(new Color(0, 0, 0, 0))
                .setChildOf(topBar);
            
            String imagePath = topBarImagePaths[i/9];
            if (PartlySaneSkies.config.customAhGuiTextures == 1) {
                imagePath = topBarFurfSkyImagePaths[i/9];
            }

            Utils.uiimageFromResourceLocation(new ResourceLocation("partlysaneskies", imagePath))
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setWidth(widthScaledConstraint(30))
                .setHeight(widthScaledConstraint(30))
                .setChildOf(icon);
            
            icon.onMouseClickConsumer(event -> {
                if (event.getMouseButton() == 1) {
                    Utils.rightClickOnSlot(slot);
                    return;
                }
                Utils.clickOnSlot(slot);
                });
            try {
                icon.onMouseEnterRunnable(() -> {
                    ((UIWrappedText) itemName).setText(inventory.getStackInSlot(slot).getDisplayName());
                    ((UIWrappedText) itemLore).setText(Utils.getLoreAsString(inventory.getStackInSlot(slot)));
                });
                icon.onMouseLeaveRunnable(() -> {
                    ((UIWrappedText) itemName).setText("");
                    ((UIWrappedText) itemLore).setText("");
                });
            } catch (NullPointerException exception) {
                exception.printStackTrace();
            }
            
            if (iteratorId == i/9) {
                icon.setColor(Utils.applyOpacityToColor(ThemeManager.getAccentColor().toJavaColor(), 75));
            }
        }

        // Bottom Bar

        itemBarLocationStep = (mainBoxWidth - 100) / 8f;
        itemBlockWidth = itemBarLocationStep*.75f;
        for (int itemBar = 1; itemBar < 8; itemBar++) {
            new UIBlock()
                    .setX(widthScaledConstraint(itemBarLocationStep * itemBar))
                    .setY(new CenterConstraint())
                    .setWidth(widthScaledConstraint(2))
                    .setHeight(widthScaledConstraint(25))
                    .setColor(ThemeManager.getDarkAccentColor())
                    .setChildOf(bottomBar);
        }

        String[] bottomBarImagePaths = {
            "textures/gui/custom_ah/left_arrow_icon.png",
            "textures/gui/custom_ah/reset_icon.png",
            "textures/gui/custom_ah/search_icon.png",
            "textures/gui/custom_ah/go_back_icon.png",
            "textures/gui/custom_ah/sort_filter/unknown.png",
            "textures/gui/custom_ah/rarity_filter/no_filter.png",
            "textures/gui/custom_ah/type/all.png",
            "textures/gui/custom_ah/right_arrow_icon.png"
        };

        String[] bottomBarFurfskyImagePaths = {
            "textures/gui/custom_ah/furfsky/left_arrow_icon.png",
            "textures/gui/custom_ah/furfsky/reset_icon.png",
            "textures/gui/custom_ah/furfsky/search_icon.png",
            "textures/gui/custom_ah/furfsky/go_back_icon.png",
            "textures/gui/custom_ah/furfsky/sort_filter/unknown.png",
            "textures/gui/custom_ah/furfsky/rarity_filter/no_filter.png",
            "textures/gui/custom_ah/furfsky/type/all.png",
            "textures/gui/custom_ah/furfsky/right_arrow_icon.png"
        };

        for (int i = 46; i <= 53;i++) {
            final int slot = i;
            UIComponent icon = new UIBlock()
                .setX(widthScaledConstraint(itemBarLocationStep * (i - 46) + (itemBarLocationStep - itemBlockWidth)/2))
                .setY(new CenterConstraint())
                .setWidth(widthScaledConstraint(itemBlockWidth))
                .setHeight(new PixelConstraint(topBar.getHeight()))
                .setColor(new Color(0, 0, 0, 0))
                .setChildOf(bottomBar);

            String imagePath = bottomBarImagePaths[i - 46];
            if (PartlySaneSkies.config.customAhGuiTextures == 1) {
                imagePath = bottomBarFurfskyImagePaths[i - 46];
            }
            // If it's the filter icon, set the picture to the filter icon
            if (i == 50) {
                String sortSelectedLine = "";

                String sortImageName = "unknown";
                try {
                    List<String> sortLoreList = Utils.getLore(inventory.getStackInSlot(slot));
                    for (String line : sortLoreList) {
                        if (line.contains("▶")) {
                            sortSelectedLine = line;
                            break;
                        }
                    }
                    sortSelectedLine = StringUtils.removeColorCodes(sortSelectedLine);

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
                } catch(NullPointerException exception) {
                    exception.printStackTrace();
                }
                
                imagePath = imagePath.replace("unknown", sortImageName);
            } 
            // Rarity filter slot
            else if (i == 51) {
                String filterSelectedLine = "";

                String filterImageName = "no_filter";
                try {
                    List<String> sortLoreList = Utils.getLore(inventory.getStackInSlot(slot));
                    for (String line : sortLoreList) {
                        if (line.contains("▶")) {
                            filterSelectedLine = line;
                            break;
                        }
                    }
                    filterSelectedLine = StringUtils.removeColorCodes(filterSelectedLine);

                    if (filterSelectedLine.toLowerCase().contains("uncommon")) {
                        filterImageName = "uncommon";
                    }
                    else if (filterSelectedLine.toLowerCase().contains("common")) {
                        filterImageName = "common";
                    } 
                    else if (filterSelectedLine.toLowerCase().contains("rare")) {
                        filterImageName = "rare";
                    }
                    else if (filterSelectedLine.toLowerCase().contains("epic")) {
                        filterImageName = "epic";
                    }
                    else if (filterSelectedLine.toLowerCase().contains("legendary")) {
                        filterImageName = "legendary";
                    }
                    else if (filterSelectedLine.toLowerCase().contains("special")) {
                        filterImageName = "special";
                    }
                    else if (filterSelectedLine.toLowerCase().contains("very special")) {
                        filterImageName = "special";
                    }
                    else if (filterSelectedLine.toLowerCase().contains("divine")) {
                        filterImageName = "divine";
                    }
                    else if (filterSelectedLine.toLowerCase().contains("mythic")) {
                        filterImageName = "mythic";
                    }
                    else if (filterSelectedLine.toLowerCase().contains("unobtainable")) {
                        filterImageName = "unobtainable";
                    }
                } catch (NullPointerException exception) {
                    exception.printStackTrace();
                }
                
                imagePath = imagePath.replace("no_filter", filterImageName);
            }
            else if (i == 52) {
                String binSelectedLine = "";

                String binImageName = "all";

                try{
                    List<String> binLoreList = Utils.getLore(inventory.getStackInSlot(slot));
                    for (String line : binLoreList) {
                        if (line.contains("▶")) {
                            binSelectedLine = line;
                            break;
                        }
                    }
                    binSelectedLine = StringUtils.removeColorCodes(binSelectedLine);

                    
                    if (binSelectedLine.toLowerCase().contains("bin only")) {
                        binImageName = "bin_only";
                    }
                    else if (binSelectedLine.toLowerCase().contains("auctions only")) {
                        binImageName = "auction_only";
                    } 
                } catch (NullPointerException exception) {
                    exception.printStackTrace();
                }

                imagePath = imagePath.replace("all", binImageName);
            }

            Utils.uiimageFromResourceLocation(new ResourceLocation("partlysaneskies", imagePath))
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setWidth(widthScaledConstraint(30))
                .setHeight(widthScaledConstraint(30))
                .setChildOf(icon);

            icon.onMouseClickConsumer(event -> {
                if (event.getMouseButton() == 1) {
                    Utils.rightClickOnSlot(slot);
                    return;
                }
                Utils.clickOnSlot(slot);
            });

            ItemStack item = inventory.getStackInSlot(slot);
            if (item != null) {
                try {
                    icon.onMouseEnterRunnable(() -> {
                        ((UIWrappedText) itemName).setText(item.getDisplayName());
                        ((UIWrappedText) itemLore).setText(Utils.getLoreAsString(item));
                    });
                    icon.onMouseLeaveRunnable(() -> {
                        ((UIWrappedText) itemName).setText("");
                        ((UIWrappedText) itemLore).setText("");
                    });
                } catch (NullPointerException exception) {
                    exception.printStackTrace();
                }
            }
            
        }

        
    }

    UIComponent itemName;

    UIComponent itemLore;

    UIComponent itemInfoHeader;

    UIComponent itemInfoText;

    public void makeItemBox(Auction auction, float x, float y) throws NullPointerException {
        UIComponent backgroundBox = new UIBlock()
                .setX(widthScaledConstraint(x - boxSide * .25f))
                .setY(widthScaledConstraint(y))
                .setWidth(widthScaledConstraint(boxSide * 1.5f))
                .setHeight(widthScaledConstraint(boxSide * 2))
                .setColor(new Color(0, 0, 0, 0))
                .setChildOf(mainBox);

        PSSButton box = new PSSButton()
            .setX(widthScaledConstraint(x - boxSide * .25f))
            .setY(widthScaledConstraint(y))
            .setWidth(widthScaledConstraint(boxSide * 1.5f).getValue())
            .setHeight(widthScaledConstraint(boxSide * 1.5f).getValue())
            .setColor(auction.getRarityColor())
            .setChildOf(mainBox)
            .onMouseClickConsumer(event -> auction.selectAuction());

        backgroundBox.onMouseClickConsumer(event -> auction.selectAuction());

        backgroundBox.onMouseEnterRunnable(() -> {
            ((UIWrappedText) itemName).setText(auction.getName());
            ((UIWrappedText) itemLore).setText(auction.getLore());

            String header;
            if (auction.isBin()) {
                header = "Buy It Now Details:";
            } else {
                header = "Auction Details:";
            }
            ((UIWrappedText) itemInfoHeader).setText(header)
                    .setColor(auction.getRarityColor());

            String info = "";
            info += "§6Offer Information:\n\n\n";
            info += "§eSelling Price: \n§6" + StringUtils.formatNumber(auction.getPrice()) + "\n\n";
            info += "§eEnding In: \n§6" + auction.getFormattedEndingTime();

            if (auction.getAmount() != 1) {
                info += "\n\n\n";
                info += "§eQuantity: \n§6" + StringUtils.formatNumber(auction.getAmount()) + "\n";
                info += "§eCost Per Item: \n§6" + StringUtils.formatNumber(Utils.round(auction.getCostPerAmount(),2)) + " coins\n";
            }
            info += "\n\n\n\n\n\n";

            info += "§eMarket Stats:\n\n\n";
            if (auction.hasLowestBin()) {
                info += "§bCurrent Lowest Bin: \n§e" + StringUtils.formatNumber(Utils.round(auction.getLowestBin(), 2)) + "\n\n";
            }
            else {
                info += "§bCurrent Lowest Bin: \n§8§o(Unknown)\n\n";
            }

            if (auction.hasAverageLowestBin()) {
                info += "§bAverage Lowest Bin (Last Day): \n§e"
                        + StringUtils.formatNumber(Utils.round(auction.getAverageLowestBin(), 2)) + "\n\n";
            }
            else {
                info += "§bAverage Lowest Bin (Last Day): \n§8§o(Unknown)\n\n";
            }

            if (auction.hasLowestBin() && auction.hasAverageLowestBin()) {
                info += "§bItem Inflation: \n§e"
                        + StringUtils.formatNumber(
                        Utils.round((auction.getLowestBin() / auction.getAverageLowestBin()) * 100d, 2) - 100)+ "%\n\n";
            }
            else {
                info += "§bInflation: \n§8§o(Unknown)\n\n";
            }

            if (auction.hasLowestBin()) {
                info += "§bItem Mark up: \n§e" + StringUtils.formatNumber(Utils.round(
                        (auction.getPrice() / auction.getLowestBin()) / auction.getAmount() * 100 - 100,
                        2)) + "%\n";
            }
            else {
                info += "§bItem Mark up: \n§8§o(Unknown)\n";
            }



            
            
            ((UIWrappedText) itemInfoText).setText((info));
        });

        backgroundBox.onMouseLeaveRunnable(() -> {
            ((UIWrappedText) itemName).setText("");
            ((UIWrappedText) itemLore).setText("");

            ((UIWrappedText) itemInfoHeader).setText("");
            ((UIWrappedText) itemInfoText).setText("");
        });

        try {
            if (auction.shouldHighlight()) {
                box.setColor(ThemeManager.getAccentColor());
                UIComponent highlightBox = new UIBlock()
                    .setX(new CenterConstraint())
                    .setY(new CenterConstraint())
                    .setWidth(new PixelConstraint(box.getComponent().getWidth() * 1.1f))
                    .setHeight(new PixelConstraint(box.getComponent().getHeight() * 1.1f))
                    .setColor(PartlySaneSkies.config.BINSniperColor.toJavaColor());
                box.insertComponentBeforeBackground(highlightBox);
                // box.setBackgroundVisibility(false);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        new PSSItemRender(auction.getItem())
            .setItemScale(widthScaledConstraint(2f))
            .setX(new CenterConstraint())
            .setY(new CenterConstraint())
            .setWidth(widthScaledConstraint(boxSide))
            .setHeight(widthScaledConstraint(boxSide))
            .setChildOf(box.getComponent());

        // auction.setBox(box);

        new UIWrappedText(auction.getName(), true, null, true)
            .setTextScale(widthScaledConstraint(1f))
            .setX(new CenterConstraint())
            .setY(widthScaledConstraint(boxSide + 20))
            .setWidth(widthScaledConstraint(boxSide + (pad * .5f)))
            .setColor(Color.white)
            .setChildOf(box.getComponent());

        
        
    }

    private float getWidthScaleFactor() {
        return this.getWindow().getWidth() / 1097f;
    }

    private PixelConstraint widthScaledConstraint(float value) {
        return new PixelConstraint(value * getWidthScaleFactor());
    }

}
