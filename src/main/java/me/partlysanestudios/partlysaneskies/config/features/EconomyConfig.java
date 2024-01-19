package me.partlysanestudios.partlysaneskies.config.features;

import cc.polyfrost.oneconfig.config.annotations.Dropdown;
import cc.polyfrost.oneconfig.config.annotations.Number;
import cc.polyfrost.oneconfig.config.annotations.Slider;
import cc.polyfrost.oneconfig.config.annotations.Switch;

public class EconomyConfig {
    // Community Center
    @Switch(
            subcategory = "Community Center",
            name = "Best Item for Bits",
            description = "Gives you information about which item in the Bits Shop is the best to sell.",
            category = "Economy"
    )
    public boolean bestBitShopItem = false;

    @Switch(
            subcategory = "Community Center",
            name = "Only Show Affordable Items",
            description = "When making recommendations for what you can buy, only recommend the items that you are able to afford.",
            category = "Economy"
    )
    public boolean bitShopOnlyShowAffordable = true;

    @Slider(
            min = 0,
            max = 100,
            category = "Economy",
            subcategory = "BIN Sniper",
            name = "BIN Snipe Percentage",
            description = "The percent of the price that the BIN sniper considers a \"snipe\". Example: 85%, Lowest BIN: 1 000 000, will look for a price of 850000 or less."
    )
    public float BINSniperPercent = 87f;


    // Auction House
    @Switch(
            name = "Custom Auction House GUI",
            category = "Economy",
            subcategory = "Auction House",
            description = "Toggle using the custom Auction House GUI and BIN Sniper Helper."
    )
    public boolean customAhGui = true;

    @Dropdown(

            name = "Custom Auction House GUI Icons",
            category = "Economy",
            options = {
                    "Partly Sane Studios",
                    "FurfSky Reborn"
            },
            subcategory = "Auction House",
            description = "Use either the Partly Sane Studios developed textures, or the FurfSky Reborn developed textures\n\nAll of the textures under FurfSky Reborn are fully developed by the FurfSky Reborn team.\nhttps://furfsky.net/"
    )
    public int customAhGuiTextures = 1;


    @Slider(
            name = "Master Scale",
            min = .1f,
            max = 1,
            subcategory = "Auction House",
            category = "Economy"
    )
    public float masterAuctionHouseScale = .333333f;


    @Slider(
            name = "Item Padding",
            min = 0f,
            max = .2f,
            subcategory = "Auction House",
            category = "Economy"
    )
    public float auctionHouseItemPadding = .075f;

    @Slider(
            name = "Side Bar Height",
            min = .25f,
            max = 2f,
            subcategory = "Auction House",
            category = "Economy"
    )
    public float auctionHouseSideBarHeight = 1.333f;
    @Slider(
            name = "Side Bar Width",
            min = .25f,
            max = 2,
            subcategory = "Auction House",
            category = "Economy"
    )
    public float auctionHouseSideBarWidth = .667f;

    @Slider(
            name = "Side Bar Padding",
            min = -.5f,
            max = .5f,
            subcategory = "Auction House",
            category = "Economy"
    )
    public float auctionSideBarPadding = .05f;

    @Slider(
            name = "Auction House Text Scale",
            min = .11f,
            max = 2,
            subcategory = "Auction House",
            category = "Economy"
    )
    public float auctionHouseTextScale = .75f;


    // Excessive Coin warning
    @Switch(
            name = "Excessive Coin and No Booster Cookie",
            category = "Economy",
            description = "Warns you if you have a lot of coins in your purse and no booster cookie.",
            subcategory = "Excessive Coin Warning"
    )
    public boolean noCookieWarning = false;

    @Number(
            min = 0,
            max = Integer.MAX_VALUE,
            name = "Maximum Allowed Amount Without Booster Cookie",
            category = "Economy",
            description = "The maximum allowed amount of money allowed before it warns you about having no booster cookie.",
            subcategory = "Excessive Coin Warning"
    )
    public int maxWithoutCookie = 750000;

    @Slider(
            min = 1,
            max = 7,
            subcategory = "Excessive Coin Warning",
            name = "Excessive Coin Warning Time",
            description = "The amount of seconds the warning appears for appears for.",
            category = "Economy"
    )
    public float noCookieWarnTime = 3.5f;

    @Slider(
            min = 1,
            max = 300,
            subcategory = "Excessive Coin Warning",
            name = "Excessive Coin Warn Cooldown",
            description = "The amount of seconds between each warning",
            category = "Economy"
    )
    public int noCookieWarnCooldown = 20;
}
