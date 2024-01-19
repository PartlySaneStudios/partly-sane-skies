package me.partlysanestudios.partlysaneskies.config.features;

import cc.polyfrost.oneconfig.config.annotations.KeyBind;
import cc.polyfrost.oneconfig.config.annotations.Slider;
import cc.polyfrost.oneconfig.config.annotations.Switch;
import cc.polyfrost.oneconfig.config.core.OneKeyBind;
import org.lwjgl.input.Keyboard;

public class FarmingConfig {
    //    Hoes
    @Switch(
            subcategory = "Hoes",
            name = "Stop right clicks on Mathematical Hoes",
            description = "Cancels the right click on mathematical hoes to prevent it from opening the recipes list. (Use at your own risk)",
            category = "Farming"
    )
    public boolean blockHoeRightClicks = false;

    @Slider(
            min = 1,
            max = 15,
            subcategory = "Hoes",
            name = "Allow Time",
            description = "The amount of minutes the hoe will be allowed to be used for, using /allowhoerightclick.",
            category = "Farming"
    )
    public float allowRightClickTime = 3f;

    @KeyBind(
            name = "Allow Hoe Right Clicks Opener Hotkey",
            category = "Farming",
            subcategory = "Hoes"
    )
    public OneKeyBind allowHoeRightClickKeybind = new OneKeyBind(Keyboard.KEY_NONE);


    //    Farm notifier
    @Switch(
            subcategory = "End of Farm Notifier",
            category = "Farming",
            name = "Show end of farm regions"
    )
    public boolean showFarmRegions = true;

    @Slider(
            min = 1,
            max = 5,
            subcategory = "End of Farm Notifier",
            name = "Time between chimes",
            description = "The amount of seconds between the chime sounds",
            category = "Farming"
    )
    public float farmnotifierChimeTime = 3;

    @Slider(
            min = 1,
            max = 120,
            subcategory = "End of Farm Notifier",
            name = "Highlight Time",
            description = "The amount of seconds that a highlighted region will stay highlighted for",
            category = "Farming"
    )
    public float farmHightlightTime = 30f;

    // Garden Visitors
    @Switch(
            subcategory = "Garden Visitors",
            name = "Visitor Trade Cost",
            description = "Gives you information about the cost of visitor trades.",
            category = "Farming"
    )
    public boolean gardenShopTradeInfo = false;

    @Switch(
            subcategory = "Garden Visitors",
            name = "Display Garden Visitor Stats",
            description = "Shows visited/accepted stats per NPC rarity.\nPros: based on item tooltips, which might capture more Garden visitor data\n(especially if you had Garden visitors before you installed SkyHanni).\nCons: Only shows for current Visitor's Logbook page and not all pages.",
            category = "Farming"
    )
    public boolean visitorLogbookStats = false;

    // Composter
    @Switch(
            subcategory = "Composter",
            name = "Best Crops to Compost",
            description = "Gives you information about which crops are the best to compost.",
            category = "Farming"
    )
    public boolean bestCropsToCompost = false;

    // Skymart
    @Switch(
            subcategory = "SkyMart",
            name = "SkyMart Value",
            description = "Gives you information about the best value crops to compost",
            category = "Farming"
    )
    public boolean skymartValue = false;
}
