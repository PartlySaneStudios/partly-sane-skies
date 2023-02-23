package me.partlysanestudios.partlysaneskies;

import java.awt.Color;
import java.io.File;

import gg.essential.vigilance.Vigilant;
import gg.essential.vigilance.data.Property;
import gg.essential.vigilance.data.PropertyType;

public class ConfigScreen extends Vigilant {

    // --------------Category: General------------
    // API
    @Property(
        type = PropertyType.TEXT, 
        protectedText = true, 
        name = "API Key", 
        category = "General", 
        subcategory = "API", 
        description = "Do /api new to automatically set your API Key. Do not show your API key to anyone unless you know what you're doing."
    )
    public String apiKey = "";

    @Property(
        type = PropertyType.SELECTOR, 
        options = {
            "Commas (1,000,000)",
            "Spaces (1 000 000)",
            "Periods (1.000.000)",
    }, 
        category = "General", 
        subcategory = "Appearance", 
        name = "Hundreds Place Format", 
        description = "The seperator between different hundreds places."
    )
    public int hundredsPlaceFormat = 1;

    @Property(
        type = PropertyType.SELECTOR, 
        options = {
            "Commas (1,52)",
            "Periods (1.52)",
    }, 
        category = "General", 
        subcategory = "Appearance", 
        name = "Decimal Place Format", 
        description = "The character to represent decimal places."
    )
    public int decimalPlaceFormat = 1;

    // Main Menu
    @Property(
        type = PropertyType.SWITCH, 
        category = "General", 
        subcategory = "Main Menu", 
        name = "Show a custom Minecraft Main Menu"
    )
    public boolean customMainMenu = true;

    @Property(
        type = PropertyType.SELECTOR, 
        options = {
            "Random Image",
            "View of Main Hub Mountain",
            "Aerial View of Hub from Community House",
            "Stunning Aerial View of Hub",
            "View from Hub Portal (Day)",
            "Hub Portal (Night)",
            "Wolf Ruins"
        }, 
        category = "General", 
        subcategory = "Main Menu", 
        name = "Custom Minecraft Main Menu Image"
    )
    public int customMainMenuImage = 1;

    @Property(
        type = PropertyType.SWITCH,
        name = "Color Private Messages", 
        category = "General", 
        subcategory = "Chat Color", 
        description = "Private messages pink to make them more visable in busy lobbies."
    )
    public boolean colorPrivateMessages = false;

    @Property(
        type = PropertyType.SWITCH,
        name = "Color Party Chat", 
        category = "General", 
        subcategory = "Chat Color", 
        description = "Color messages from the party chat blue to make them more visable in busy lobbies."
    )
    public boolean colorPartyChat = false;

    @Property(
        type = PropertyType.SWITCH,
        name = "Color Guild Chat", 
        category = "General", 
        subcategory = "Chat Color", 
        description = "Color messages from the guild chat green to make them more visable in busy lobbies."
    )
    public boolean colorGuildChat = false;

    @Property(
        type = PropertyType.SWITCH,
        name = "Color Guild Officer Chat", 
        category = "General", 
        subcategory = "Chat Color", 
        description = "Color messages from the guild officer chat aqua to make them more visable in busy lobbies."
    )
    public boolean colorOfficerChat = false;

    @Property(
        type = PropertyType.SWITCH,
        name = "Skyblock Co-op Chat", 
        category = "General", 
        subcategory = "Chat Color", 
        description = "Color messages from the skyblock coop chat aqua to make them more visable in busy lobbies."
    )
    public boolean colorCoopChat = false;



    // ----------------- Category: Skyblock -------------------
    // Rare Drop
    @Property(
        type = PropertyType.SWITCH, 
        name = "Rare Drop Banner", 
        subcategory = "Rare Drop", 
        description = "On rare drop, get a Pumpkin Dicer like banner.", 
        category = "Skyblock"
    )
    public boolean rareDropBanner = false;

    @Property(
        type = PropertyType.DECIMAL_SLIDER, 
        minF = 1, 
        maxF = 7, 
        subcategory = "Rare Drop", 
        name = "Rare Drop Banner Time", 
        description = "The amount of seconds the rare drop banner appears for.", 
        category = "Skyblock"
    )
    public float rareDropBannerTime = 3.5f;

    @Property(
        type = PropertyType.SWITCH, 
        name = "Custom Rare Drop Sound", 
        subcategory = "Rare Drop", 
        description = "Plays a custom sound when you get a rare drop.", 
        category = "Skyblock"
    )
    public boolean rareDropBannerSound = false;

    // Location Banner
    @Property(
        type = PropertyType.SWITCH, 
        name = "Rare Drop Banner", 
        subcategory = "Location Banner", 
        description = "An MMO RPG style banner shows up when you switch locations.", 
        category = "Skyblock"
    )
    public boolean locationBannerDisplay = false;

    @Property(
        type = PropertyType.DECIMAL_SLIDER, 
        minF = 1, 
        maxF = 7, 
        subcategory = "Location Banner", 
        name = "Location Banner Time", 
        description = "The amount of seconds the location banner appears for.", 
        category = "Skyblock"
    )
    public float locationBannerTime = 3.5f;

    // Open Wiki
    @Property(
        type = PropertyType.SWITCH, 
        name = "Open Wiki Automatically", 
        category = "Skyblock", 
        description = "When the Open Wiki Article KeyBINd is used, automatically open the article without confirmation first.", 
        subcategory = "Open Wiki"
    )
    public boolean openWikiAutomatically = true;
   
    // Pet Minion Alert
    @Property(
        type = PropertyType.TEXT,
        category = "Skyblock",
        subcategory = "Incorrect Pet for Minion Alert",
        name = "Selected Pet",
        description = "The selected pet that will be used for minion collecting (Use /pets and click the pet keybind to select",
        protectedText =  true
    )
    public String selectectedPet = "";

    @Property(
        type = PropertyType.DECIMAL_SLIDER, 
        minF = 1, 
        maxF = 15, 
        subcategory = "Incorrect Pet for Minion Alert", 
        name = "Mute Time", 
        description = "The amount of minutes the pet alert will mute for when you mute it.", 
        category = "Skyblock"
    )
    public float petAlertMuteTime = 7.5f;



    // -------------- Category: Mining --------------
    // Worm Warning
    @Property(
        type = PropertyType.SWITCH, 
        name = "Worm Warning Banner", 
        subcategory = "Worm Warning", 
        description = "A banner appears on your screen when a worm spawns.", 
        category = "Mining"
    )
    public boolean wormWarningBanner = false;

    @Property(
        type = PropertyType.COLOR, 
        subcategory = "Watcher Ready", 
        name = "Watcher Ready Banner Colour", 
        description = "The colour of the watcher ready text", 
        category = "Dungeons"
    )
    public Color wormWarningBannerColor = new Color(34, 255, 0);

    @Property(
        type = PropertyType.DECIMAL_SLIDER, 
        minF = 1, 
        maxF = 7, 
        subcategory = "Worm Warning", 
        name = "Worm Warning Banner Time", 
        description = "The amount of seconds the worm warning banner appears for.", 
        category = "Mining"
    )
    public float wormWarningBannerTime = 3.5f;

    @Property(
        type = PropertyType.SWITCH, 
        name = "Worm Warning Sound", 
        subcategory = "Worm Warning", 
        description = "Plays a sound when a worm spawns.", 
        category = "Mining"
    )
    public boolean wormWarningBannerSound = false;



    // ------------- Category: Dungeons ---------------------------------
    // Party Manager
    @Property(
        type = PropertyType.SWITCH, 
        name = "Automatically kick offline on party manager load", 
        subcategory = "Party Manager", 
        description = "Automatically kicks offline members in your party when you open party manager.", 
        category = "Dungeons"
    )
    public boolean autoKickOfflinePartyManager = false;

    @Property(
        type = PropertyType.SLIDER, 
        name = "Party Manager Cache Time", 
        min = 0, 
        max = 90, 
        subcategory = "Party Manager", 
        description = "Saves the data from other party members to save time upon loading Party Manager. The bigger the value the more minutes you will save but the less accurate your data will be.", 
        category = "Dungeons"
    )
    public int partyManagerCacheTime = 30;

    // Watcher Ready Warning
    @Property(
        type = PropertyType.SWITCH, 
        name = "Watcher Ready Warning", 
        subcategory = "Watcher Ready", 
        description = "Sends a warning when the watcher is done spawning mobs.", 
        category = "Dungeons"
    )
    public boolean watcherReadyBanner = false;

    @Property(
        type = PropertyType.SWITCH, 
        name = "Watcher Ready Sound", 
        subcategory = "Watcher Ready", 
        description = "Plays a sound when the watcher is done spawning mobs.", 
        category = "Dungeons"
    )
    public boolean watcherReadySound = false;

    @Property(
        type = PropertyType.DECIMAL_SLIDER, 
        minF = 1, 
        maxF = 7, 
        subcategory = "Watcher Ready", 
        name = "Watcher Ready Banner Time", 
        description = "The amount of seconds the watcher ready banner appears for.", 
        category = "Dungeons"
    )
    public float watcherReadyBannerTime = 3.5f;

    @Property(
        type = PropertyType.COLOR, 
        subcategory = "Watcher Ready", 
        name = "Watcher Ready Banner Colour", 
        description = "The colour of the watcher ready text", 
        category = "Dungeons"
    )
    public Color watcherReadyBannerColor = new Color(255, 45, 6);

    @Property(
        type = PropertyType.SWITCH, 
        name = "Watcher Ready Chat Message", 
        subcategory = "Watcher Ready", 
        description = "Send a message to your party when watcher is done spawning mobs.", 
        category = "Dungeons"
    )
    public boolean watcherReadyChatMessage = false;

    @Property(
        type = PropertyType.TEXT, 
        subcategory = "Watcher Ready", 
        name = "Watcher Ready Text", 
        description = "Message to send when the watcher is ready to clear.", 
        category = "Dungeons"
    )
    public String watcherChatMessage = "The watcher is done spawning mobs. Ready to clear";

    @Property(
        type = PropertyType.SWITCH, 
        subcategory = "Watcher Ready", 
        name = "Watcher Ready Air Raid Siren", 
        description = "Plays a WWII air raid siren when the watcher is done spawning mobs. \nPros: \nKeeps you up at late night grinds \n(RECOMMENDED, ESPECIALLY AT 3 AM)", 
        category = "Dungeons"
    )
    public boolean watcherReadyAirRaidSiren = false;



    // ------------- Category: Economy ---------------------------------
    // Garden
    @Property(
        type = PropertyType.SWITCH, 
        subcategory = "Garden", 
        name = "Garden Shop Trade Cost", 
        description = "Gives you information about the cost of garden shop trades.", 
        category = "Economy"
    )
    public boolean gardenShopTradeInfo = false;

     // Auction House
     @Property(
        type = PropertyType.SWITCH, 
        name = "Custom Auction House GUI", 
        category = "Economy", 
        subcategory = "Auction House", 
        description = "Toggle using the custom Auction House GUI and BIN Sniper Helper."
    )
    public boolean customAhGui = true;

    @Property(
        type = PropertyType.PERCENT_SLIDER, 
        minF = 0, 
        maxF = 1, 
        category = "Economy", 
        subcategory = "Auction House", 
        name = "BIN Snipe Percentage", 
        description = "The percent of the price that the BIN sniper considers a \"snipe\". Example: 85%, Lowest BIN: 1 000 000, will look for a price of 850000 or less."
    )
    public float BINSniperPercent = .87f;


    // Execessive Coin warning
    @Property(
        type = PropertyType.SWITCH, 
        name = "Excessive Coin and No Booster Cookie", 
        category = "Economy", 
        description = "Warns you if you have a lot of coins in your purse and no booster cookie.", 
        subcategory = "Excessive Coin Warning"
    )
    public boolean noCookieWarning = false;

    @Property(
        type = PropertyType.NUMBER, 
        min = 0, 
        max = Integer.MAX_VALUE, 
        name = "Maximum Allowed Amount Without Booster Cookie", 
        category = "Economy", 
        description = "The maximum allowed amount of money allowed before it warns you about having no booster cookie.", 
        subcategory = "Excessive Coin Warning", 
        increment = 25000
    )
    public int maxWithoutCookie = 750000;

    @Property(
        type = PropertyType.DECIMAL_SLIDER, 
        minF = 1, 
        maxF = 7, 
        subcategory = "Excessive Coin Warning", 
        name = "Excessive Coin Warning Time", 
        description = "The amount of seconds the warning appears for appears for.", 
        category = "Economy"
    )
    public float noCookieWarnTime = 3.5f;

    @Property(
        type = PropertyType.SLIDER, 
        min = 1, 
        max = 300, 
        subcategory = "Excessive Coin Warning", 
        name = "Excessive Coin Warn Cooldown", 
        description = "The amount of seconds between each warning", 
        category = "Economy"
    )

    
    public int noCookieWarnCooldown = 20;
    public ConfigScreen() {
        super(new File("./config/partly-sane-skies/pss.toml"));
        this.initialize();
    }
}
