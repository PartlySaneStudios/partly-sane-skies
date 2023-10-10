//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.system;


import gg.essential.vigilance.Vigilant;
import gg.essential.vigilance.data.Property;
import gg.essential.vigilance.data.PropertyType;

import java.awt.*;
import java.io.File;

public class VigilantConfigScreen extends Vigilant {
    public VigilantConfigScreen() {
        super(new File("partly-sane-skies/config-vigilant.toml"));
    }

//    public OneConfigScreen() {
//        // Available mod types: PVP, HUD, UTIL_QOL, HYPIXEL, SKYBLOCK
//
////        super(new Mod("Partly Sane Skies", ModType.SKYBLOCK, "/assets/partlysaneskies/textures/logo_oneconfig.png"), "partly-sane-skies/config.json");
////        initialize();
//        super.initialize();
//    }

    public void resetBrokenStrings() {
        if (arrowLowChatMessage.isEmpty()) {
            arrowLowChatMessage = "Partly Sane Skies > Warning! {player} only has {count} arrows remaining!";
            super.writeData();
        }
        if (watcherChatMessage.isEmpty()) {
            watcherChatMessage = "Partly Sane Skies > The watcher is done spawning mobs. Ready to clear.";
            writeData();
        }
        if (secretsChatMessageString.isEmpty()){
            secretsChatMessageString = "Partly Sane Skies > All required secrets have been found!";
            writeData();
        }
    }

//    @Info(
//        type = InfoType.INFO,
//        text = "Hover over an option to see a description and more information."
//
//    )
//    public static boolean ignored;

 
    @Property(
        type = PropertyType.TEXT,
        hidden = true,
        name = "API Key", 
        category = "General", 
        subcategory = "API", 
        description = "Do /api new to automatically set your API Key. Do not show your API key to anyone unless you know what you're doing."
    )
    public String apiKey = "";

    @Property(
        type = PropertyType.SWITCH, 
            name = "Force Custom API Key",
            category = "General",
            subcategory = "API",
            description = "Forces the use of a custom API key for Hypixel requests. (Requires API Key field to be populated)"
    )
    public boolean forceCustomAPIKey = false;

    @Property(
        type = PropertyType.DECIMAL_SLIDER, 
        minF = .1f,
        maxF = 30f,
        name = "Time between requests",
        category = "General",
        subcategory = "API",
        description = "The time between API calls. Only change if you know what you're doing. Changing this will reduce the amount of time API requests take, however may result in more errors"
    )
    public float timeBetweenRequests = 0.5f;

    @Property(
        type = PropertyType.DECIMAL_SLIDER, 
        name = "Player Data Cache Time",
        minF = 0,
        
        maxF = 90,
        subcategory = "API",
        description = "Saves the data from other party members to save time upon loading data about players. The bigger the value the more minFutes you will save but the less accurate your data will be.",
        category = "General"
    )
    public float playerDataCacheTime = 5;

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
        description = "The separator between different hundreds places."
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

    @Property(
        type = PropertyType.SWITCH, 
        category = "General",
        subcategory = "Appearance",
        name = "24 hour time",
        description = "Display time in 24-hour hour time (15:30) instead of 12 hour time (3:30 PM)"
    )
    public boolean hour24time = false;
    

    @Property(
        type = PropertyType.SWITCH, 
        category = "General",
        subcategory = "Appearance",
        name = "Legacy Version Warning",
        description = "Warns you if you are using a legacy version of Partly Sane Skies"
    )
    public boolean legacyVersionWarning = true;
    // Main Menu

    @Property(
        type = PropertyType.SWITCH, 
        category = "General", 
        subcategory = "Main Menu", 
        name = "Show a Custom minFecraft Main Menu"
    )
    public boolean customMainMenu = true;

    @Property(
        type = PropertyType.SWITCH, 
        category = "General", 
        subcategory = "Main Menu", 
        name = "Announcements on Main Menu",
        description = "Display announcements such as recent skyblock updates on the main menu"
    )
    public boolean displayAnnouncementsCustomMainMenu = true;

    @Property(
        type = PropertyType.SELECTOR, 
        options = {
            "Random Image",
            "View of Main Hub Mountain",
            "Aerial View of Hub from Community House",
            "Stunning Aerial View of Hub",
            "View from Hub Portal (Day)",
            "Hub Portal (Night)",
            "Wolf Ruins",
            "Custom Image"
        }, 
        category = "General", 
        subcategory = "Main Menu", 
        name = "Custom minFecraft Main Menu Image",
        description = "Select one of our many high quality included images, or you can use your custom image.\nTo use your own image, place your image in the /config/partly-sane-skies folder and title your image background.png"
    )
    public int customMainMenuImage = 1;

    @Property(
        type = PropertyType.SWITCH, 
        name = "Print errors in chat",
        category = "General",
        subcategory = "API",
        description = "Send errors on getting APIs in chat (Recommended, however if you get spammed or have a bad internet connection, turn it off)"
        
    )
    public boolean printApiErrors = true;


//    ------------------ Category: Themes ---------------------
//    Themes
    @Property(
        type = PropertyType.SELECTOR, 
            name = "Selected Theme",
            category = "Themes",
            subcategory = "Theme",
            description = "Pick from one of our 9 custom designed themes",
            options = {
                    "Classic (Dark)",
                    "Royal Dark (Dark)",
                    "Midnight Forest (Dark)",

                    "Moonless Night (Very Dark)",
                    "Stormy Night (Very Dark)",
                    "The Void (Very Dark)",

                    "Classic (Light)",
                    "Royal Light (Light)",
                    "Partly Cloudy (Light)",

                    "Waterfall (Colorful)",
                    "Jungle (Colorful)",
                    "Dunes (Colorful)"
            }
    )
    public int themeIndex = 0;

//    Accent Color
    @Property(
        type = PropertyType.SWITCH, 
            name = "Use default accent color",
            description = "Uses the default Partly Sane Skies accent color",
            category = "Themes",
            subcategory = "Accent Color"
    )
    public boolean useDefaultAccentColor = true;

    @Property(
        type = PropertyType.COLOR, 
            name = "Custom Accent Color",
            description = "Choose a custom accent color for your game",
            category = "Themes",
            subcategory = "Accent Color"
    )
    public Color accentColor = new Color(1, 255, 255, 255);

//    Custom Themes
    @Property(
        type = PropertyType.SWITCH, 
            name = "Create your own Theme",
            description = "Enable to be able to create your own custom themes",
            category = "Themes",
            subcategory = "Custom Themes"
    )
    public boolean customTheme = false;

    @Property(
        type = PropertyType.COLOR, 
            name = "Custom Primary Color",
            description = "Choose a custom primary color for your game",
            category = "Themes",
            subcategory = "Custom Themes"
    )
    public Color primaryColor = new Color(42, 43, 46, 255);

    @Property(
        type = PropertyType.COLOR, 
            name = "Custom Secondary Color",
            description = "Choose a custom secondary color for your game",
            category = "Themes",
            subcategory = "Custom Themes"
    )
    public Color secondaryColor = new Color(42, 43, 46, 255);


//    Resource Packs
    @Property(
        type = PropertyType.SWITCH, 
            name = "Disable themes to use resource packs",
            description = "Disable themes to be able to use resource packs to modify Partly Sane Skies menus",
            category = "Themes",
            subcategory = "Resource Packs"
    )
    public boolean disableThemes = false;











    // ----------------- Category: SkyBlock -------------------
    // Rare Drop
    @Property(
        type = PropertyType.SWITCH,  
        name = "Rare Drop Banner", 
        subcategory = "Rare Drop", 
        description = "On rare drop, get a Pumpkin Dicer like banner.", 
        category = "SkyBlock"
    )
    public boolean rareDropBanner = false;


    @Property(
        type = PropertyType.DECIMAL_SLIDER, 
        minF = 1, 
        maxF = 7, 
        subcategory = "Rare Drop", 
        name = "Rare Drop Banner Time", 
        description = "The amount of seconds the rare drop banner appears for.", 
        category = "SkyBlock"
    )
    public float rareDropBannerTime = 3.5f;

    @Property(
        type = PropertyType.SWITCH, 
        name = "Custom Rare Drop Sound", 
        subcategory = "Rare Drop", 
        description = "Plays a custom sound when you get a rare drop.", 
        category = "SkyBlock"
    )
    public boolean rareDropBannerSound = false;

    // Location Banner
    @Property(
        type = PropertyType.SWITCH,  
        name = "Location Banner", 
        subcategory = "Location Banner", 
        description = "An MMO RPG style banner shows up when you switch locations.", 
        category = "SkyBlock"
    )
    public boolean locationBannerDisplay = false;

    @Property(
        type = PropertyType.DECIMAL_SLIDER, 
        minF = 1, 
        maxF = 7, 
        subcategory = "Location Banner", 
        name = "Location Banner Time", 
        description = "The amount of seconds the location banner appears for.", 
        category = "SkyBlock"
    )
    public float locationBannerTime = 3.5f;

    // Open Wiki
    @Property(
        type = PropertyType.SWITCH,  
        name = "Open Wiki Automatically", 
        category = "SkyBlock",
        description = "When the Open Wiki Article Keybind is used, automatically open the article without confirmation first.", 
        subcategory = "Open Wiki"
    )
    public boolean openWikiAutomatically = true;

    // Pet minFion Alert
    @Property(
        type = PropertyType.SWITCH, 
        name = "Incorrect Pet for minFion Alert", 
        category = "SkyBlock",
        description = "Warns you if you don't have the right pet for leveling up the minFions, that way you never lose any pet EXP because you still have your level 100 dungeon pet activated.\nRequires pets to be visible.", 
        subcategory = "Incorrect Pet for minFion Alert"
    )
    public boolean incorrectPetForMinionAlert = false;

    @Property(
        type = PropertyType.SWITCH, 
            name = "Selected Pet Information",
            category = "SkyBlock",
            description = "Gives you information about the currently selected pet while in the minFion menu\nRequires pets to be visible.",
            subcategory = "Incorrect Pet for minFion Alert"
    )
    public boolean selectedPetInformation = false;

    @Property(
        type = PropertyType.SWITCH, 
        name = "Air Raid Siren", 
        category = "SkyBlock",
        description = "Plays a WWII air raid siren when you have the wrong pet. \nPros: \nKeeps you up at late night grinds \n(RECOMMENDED, ESPECIALLY AT 3 AM).", 
        subcategory = "Incorrect Pet for minFion Alert"
    )
    public boolean incorrectPetForMinionAlertSiren = false;

    @Property(
        type = PropertyType.TEXT, 
        category = "SkyBlock",
        subcategory = "Incorrect Pet for minFion Alert",
        name = "Selected Pet",
        description = "The selected pet that will be used for minFion collecting (Use /pets and click the pet keybind to select",
        hidden =  true
//        size = 2
    )
    public String selectedPet = /*PartlySaneSkies.config.selectededPet |*/ "";

    @Property(
        type = PropertyType.DECIMAL_SLIDER, 
        minF = 1, 
        maxF = 15, 
        subcategory = "Incorrect Pet for minFion Alert", 
        name = "Mute Time", 
        description = "The amount of minFutes the pet alert will mute for when you mute it.", 
        category = "SkyBlock"
    )
    public float petAlertMuteTime = 7.5f;

    @Property(
        type = PropertyType.SELECTOR, 
        category = "SkyBlock",
        subcategory = "Enhanced SkyBlock Sounds",
        name = "Note Block Instrument Type",
        options = {
            "Default SkyBlock Noteblocks",
            "Clarinet (Live)",
            "Clarinet (Computer)",
            "Electric Piano",
            "Flute",
            "Organ",
            "Piano",
            "String Orchestra",
            "Trombone",
            "Trumpet",
            "Violin",
            "Wind Ensemble",
            "Discord New Message Sound",
            "Kazoo",
        }
    )
    public int customSoundOption = 0;

    @Property(
        type = PropertyType.SELECTOR, 
        category = "SkyBlock",
        subcategory = "Enhanced SkyBlock Sounds",
        name = "Explosions",
        options = {
            "Default",
            "Off",
            "Realistic"
        }
    )
    public int customExplosion = 0;

    // -------------- Category: mining --------------
    // Worm Warning
    @Property(
        type = PropertyType.SWITCH, 
        name = "Worm Warning Banner", 
        subcategory = "Worm Warning", 
        description = "A banner appears on your screen when a worm spawns.", 
        category = "mining"
    )
    public boolean wormWarningBanner = false;

    @Property(
        type = PropertyType.COLOR, 
        subcategory = "Worm Warning", 
        name = "Worm Warning Banner Color", 
        description = "The color of the worm warning text", 
        category = "mining"
    )
    public Color wormWarningBannerColor = new Color(34, 255, 0);

    @Property(
        type = PropertyType.DECIMAL_SLIDER, 
        minF = 1, 
        maxF = 7, 
        subcategory = "Worm Warning", 
        name = "Worm Warning Banner Time", 
        description = "The amount of seconds the worm warning banner appears for.", 
        category = "mining"
    )
    public float wormWarningBannerTime = 3.5f;

    @Property(
        type = PropertyType.SWITCH, 
        name = "Worm Warning Sound", 
        subcategory = "Worm Warning", 
        description = "Plays a sound when a worm spawns.", 
        category = "mining"
    )
    public boolean wormWarningBannerSound = false;

    //Pickaxes
    @Property(
        type = PropertyType.SWITCH, 
            name = "Pickaxe Ability Ready Banner",
            subcategory = "Pickaxes",
            description = "A banner appears on your screen when your pickaxe ability is ready.",
            category = "mining"
    )
    public boolean pickaxeAbilityReadyBanner = true;

    @Property(
        type = PropertyType.SWITCH, 
            name = "Pickaxe Ability Ready Sound",
            subcategory = "Pickaxes",
            description = "Plays a sound when your pickaxe ability is ready.",
            category = "mining"
    )
    public boolean pickaxeAbilityReadySound = false;

    @Property(
        type = PropertyType.SWITCH, 
            name = "Use Air Raid Siren for Pickaxe Ability Ready",
            subcategory = "Pickaxes",
            description = "Plays a WWII air raid siren when your pickaxe ability is ready. \nPros: \nKeeps you up at late night grinds \n(RECOMMENDED, ESPECIALLY AT 3 AM)",
            category = "mining"
    )
    public boolean pickaxeAbilityReadySiren = false;

    @Property(
        type = PropertyType.SWITCH, 
            name = "Only give a warning when you are on a mining island",
            subcategory = "Pickaxes",
            description = "Makes it less annoying when you don't want to minFe",
            category = "mining"
    )
    public boolean onlyGiveWarningOnMiningIsland = true;

    @Property(
        type = PropertyType.DECIMAL_SLIDER, 
            minF = 1,
            maxF = 7,
            subcategory = "Pickaxes",
            name = "Ready Banner Time",
            description = "The amount of seconds the ready banner appears for.",
            category = "mining"
    )
    public float pickaxeBannerTime = 3.5f;

    @Property(
        type = PropertyType.COLOR, 
            subcategory = "Pickaxes",
            name = "Ready Banner Color",
            description = "The color of the ready banner text",
            category = "mining"
    )
    public Color pickaxeBannerColor = new Color(255, 255, 0);

    @Property(
        type = PropertyType.SWITCH, 
            name = "Block Ability on Private Island (UAYOR)",
            subcategory = "Pickaxes",
            description = "Blocks the use of pickaxe abilities on your private island. (Use at your own risk)",
            category = "mining"
    )
    public boolean blockAbilityOnPrivateIsland = false;

    //Events
//    @Info(
//            type = InfoType.INFO,
//            text = "Some Events may not trigger, not all have been tested. If you find an event that doesn't trigger, please report it on our discord server.",
//            size = 2,
//            category = "Mining",
//            subcategory = "Events"
//    )

    @Property(
        type = PropertyType.SWITCH, 
            name = "Main Toggle",
            category = "Mining",
            subcategory = "Events",
            description = "Toggles the events"
    )
    public boolean miningEventsToggle = true;

    @Property(
        type = PropertyType.SWITCH, 
            name = "Show Event Banner",
            category = "Mining",
            subcategory = "Events",
            description = "Shows a banner when an enabled event is going active"
    )
    public boolean miningShowEventBanner = true;

    @Property(
        type = PropertyType.SWITCH, 
            name = "Also warn 20s before event activation",
            category = "Mining",
            subcategory = "Events",
            description = "Shows a banner and plays sound 20s before an enabled event is going active"
    )
    public boolean miningWarn20sBeforeEvent = false;

    @Property(
        type = PropertyType.SWITCH, 
            name = "2x Powder activation sound",
            category = "Mining",
            subcategory = "Events",
            description = "Plays a sound when 2x Powder event is going active"
    )
    public boolean mining2xPowderSound = false;

    @Property(
        type = PropertyType.SWITCH, 
            name = "Gone with the wind activation sound",
            category = "Mining",
            subcategory = "Events",
            description = "Plays a sound when Gone with the wind event is going active"
    )
    public boolean miningGoneWithTheWindSound = false;

    @Property(
        type = PropertyType.SWITCH, 
            name = "Better Together activation sound",
            category = "Mining",
            subcategory = "Events",
            description = "Plays a sound when Better Together event is going active"
    )
    public boolean miningBetterTogetherSound = false;

    @Property(
        type = PropertyType.SWITCH, 
            name = "Goblin Raid activation sound",
            category = "Mining",
            subcategory = "Events",
            description = "Plays a sound when Goblin Raid event is going active"
    )
    public boolean miningGoblinRaidSound = false;

    @Property(
        type = PropertyType.SWITCH, 
            name = "Raffle activation sound",
            category = "Mining",
            subcategory = "Events",
            description = "Plays a sound when Raffle event is going active"
    )
    public boolean miningRaffleSound = false;

    @Property(
        type = PropertyType.SWITCH, 
            name = "Mithril Gourmand activation sound",
            category = "Mining",
            subcategory = "Events",
            description = "Plays a sound when Mithril Gourmand event is going active"
    )
    public boolean miningMithrilGourmandSound = false;

    @Property(
        type = PropertyType.SWITCH, 
            name = "Powder Ghast activation sound",
            category = "Mining",
            subcategory = "Events",
            description = "Plays a sound when Powder Ghast is about to spawn"
    )
    public boolean miningPowderGhastSound = false;

    @Property(
        type = PropertyType.SWITCH, 
            name = "Fallen Star activation sound",
            category = "Mining",
            subcategory = "Events",
            description = "Plays a sound when Fallen Star is about to spawn"
    )
    public boolean miningFallenStarSound = false;

    @Property(
        type = PropertyType.DECIMAL_SLIDER, 
            minF = 1,
            maxF = 7,
            subcategory = "Events",
            name = "Event Banner Time",
            description = "The amount of seconds the event banner appears for.",
            category = "mining"
    )
    public float miningEventBannerTime = 3.5f;

    @Property(
        type = PropertyType.COLOR, 
            subcategory = "Events",
            name = "Event Banner Color",
            description = "The color of the event banner text",
            category = "mining"
    )
    public Color miningEventBannerColor = new Color(255, 255, 255);

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
        type = PropertyType.SWITCH, 
        name = "Warn Low Arrows in Chat", 
        subcategory = "Party Manager", 
        description = "Warns you party when a member has low arrows.", 
        category = "Dungeons"
    )
    public boolean warnLowArrowsInChat = false;

    @Property(
        type = PropertyType.TEXT, 
        subcategory = "Party Manager", 
        name = "Arrow Low Warning", 
        description = "Message to send when a player has low arrows.\nUse {player} to signify the player's username, and {count} to signify the remaining arrow count.", 
        category = "Dungeons"
//            size = 2
    )
    public String arrowLowChatMessage = "Partly Sane Skies > Warning! {player} only has {count} arrows remaining!";

    @Property(
        type = PropertyType.SLIDER,
        name = "Arrow Low Count", 
        minF = 0,
        maxF = 1000,
        subcategory = "Party Manager", 
        description = "The amount of arrows you must have to be considered low on arrows.", 
        category = "Dungeons"
    )
    public int arrowLowCount = 300;

    @Property(
        type = PropertyType.SWITCH, 
        name = "Print errors in chat",
        category = "Dungeons",
        subcategory = "Party Manager",
        description = "Send errors on getting data in chat (Recommended, however if you get spammed or have a bad internet connection, turn it off)"
        
    )
    public boolean printPartyManagerApiErrors = true;

    @Property(
        type = PropertyType.SWITCH, 
        name = "Get data on party join", 
        subcategory = "Party Manager", 
        description = "Automatically gets the data for party members someone joins the party. This saves time and reduces the chance of the data not being able to be accessed.", 
        category = "Dungeons"
    )
    public boolean getDataOnJoin = true;

    @Property(
        type = PropertyType.SWITCH, 
        name = "Toggle Run Colors in Partymanager",
        subcategory = "Party Manager",
        description = "Toggles the colors of the runs in the party manager. ",
        category = "Dungeons"
    )
    public boolean toggleRunColors = true;

    @Property(
        type = PropertyType.NUMBER,
        name = "Customize maxF Runs for Red in Run Colors",
        minF = 0,
        maxF = Integer.MAX_VALUE,
        subcategory = "Party Manager",
        description = "Customize maxFimum runs required for the color red",
        category = "Dungeons"
    )
    public int runColorsRedMax = 1;

    @Property(
        type = PropertyType.NUMBER,
        name = "Customize maxF Runs for Yellow in Run Colors",
        minF = 0,
        maxF = Integer.MAX_VALUE,
        subcategory = "Party Manager",
        description = "Customize maxFimum runs required for the color yellow",
        category = "Dungeons"
    )
    public int runColorsYellowMax = 9;



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
        name = "Watcher Ready Banner Color", 
        description = "The color of the watcher ready text", 
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
//            size = 2
    )
    public String watcherChatMessage = "Partly Sane Skies > The watcher is done spawning mobs. Ready to clear.";

    @Property(
        type = PropertyType.SWITCH, 
        subcategory = "Watcher Ready", 
        name = "Air Raid Siren", 
        description = "Plays a WWII air raid siren when the watcher is done spawning mobs. \nPros: \nKeeps you up at late night grinds \n(RECOMMENDED, ESPECIALLY AT 3 AM)", 
        category = "Dungeons"
    )
    public boolean watcherReadyAirRaidSiren = false;

    // Required Secrets Found
    @Property(
        type = PropertyType.SWITCH, 
            name = "Required Secrets Found Banner",
            subcategory = "Required Secrets Found",
            description = "Sends a warning when all required secrets have been found.",
            category = "Dungeons"
    )
    public boolean secretsBanner = false;

    @Property(
        type = PropertyType.SWITCH, 
            name = "Required Secrets Found Sound",
            subcategory = "Required Secrets Found",
            description = "Plays a sound when all required secrets have been found.",
            category = "Dungeons"
    )
    public boolean secretsSound = false;

    @Property(
        type = PropertyType.DECIMAL_SLIDER, 
            minF = 1,
            maxF = 7,
            subcategory = "Required Secrets Found",
            name = "Required Secrets Found Banner Time",
            description = "The amount of seconds the required secrets found banner appears for.",
            category = "Dungeons"
    )
    public float secretsBannerTime = 3.5f;

    @Property(
        type = PropertyType.COLOR, 
            subcategory = "Required Secrets Found",
            name = "Required Secrets Found Banner Color",
            description = "The color of the required secrets found text",
            category = "Dungeons"
    )
    public Color secretsBannerColor = new Color(255, 45, 6);

    @Property(
        type = PropertyType.SWITCH, 
            name = "Required Secrets Found Chat Message",
            subcategory = "Required Secrets Found",
            description = "Send a message to your party when all required secrets have been found.",
            category = "Dungeons"
    )
    public boolean secretsChatMessage = false;


    @Property(
        type = PropertyType.TEXT, 
            subcategory = "Required Secrets Found",
            name = "Required Secrets Found Text",
            description = "Message to send when all required secrets have been found.",
            category = "Dungeons"
//            size = 2
    )
    public String secretsChatMessageString = "Partly Sane Skies > All required secrets have been found!";

    @Property(
        type = PropertyType.SWITCH, 
            subcategory = "Required Secrets Found",
            name = "Air Raid Siren",
            description = "Plays a WWII air raid siren when all required secrets have been found. \nPros: \nKeeps you up at late night grinds \n(RECOMMENDED, ESPECIALLY AT 3 AM)",
            category = "Dungeons"
    )
    public boolean secretsAirRaidSiren = false;

// Dungeon Player Breakdown
    @Property(
        type = PropertyType.SWITCH, 
        subcategory = "Dungeon Player Breakdown", 
        name = "Dungeon Player Breakdown", 
        description = "At the end of the dungeon, send a message informinFg you how much of the dungeon each player has completed", 
        category = "Dungeons"
    )
    public boolean dungeonPlayerBreakdown = false;

    @Property(
        type = PropertyType.SELECTOR, 
        subcategory = "Dungeon Player Breakdown", 
        name = "Message Content", 
        description = "Shows more information about how many blessings and secrets each player collected", 
        category = "Dungeons",
        options = {
            "Condensed",
            "Standard",
            "Enhanced"
        }
    )
    public int enhancedDungeonPlayerBreakdown = 1;

    @Property(
        type = PropertyType.SWITCH, 
        subcategory = "Dungeon Player Breakdown", 
        name = "Send in Party Chat", 
        description = "Send a condensed version to the rest of your party.", 
        category = "Dungeons"
    )
    public boolean partyChatDungeonPlayerBreakdown = false;

    // ------------- Category: FarminFg ---------------------------------
//    Hoes
    @Property(
        type = PropertyType.SWITCH, 
            subcategory = "Hoes",
            name = "Stop right clicks on Mathematical Hoes",
            description = "Cancels the right click on mathematical hoes to prevent it from opening the recipes list. (Use at your own risk)",
            category = "FarminFg"
    )
    public boolean blockHoeRightClicks = false;

    @Property(
        type = PropertyType.DECIMAL_SLIDER, 
        minF = 1, 
        maxF = 15, 
        subcategory = "Hoes", 
        name = "Allow Time", 
        description = "The amount of minFutes the hoe will be allowed to be used for, using /allowhoerightclick.", 
        category = "FarminFg"
    )
    public float allowRightClickTime = 3f;


//    Farn notifier
    @Property(
        type = PropertyType.SWITCH, 
            subcategory = "End of Farm Notifier",
            category = "FarminFg",
            name = "Show end of farm regions"
    )
    public boolean showFarmRegions = true;

    @Property(
        type = PropertyType.DECIMAL_SLIDER, 
        minF = 1,
        maxF = 5,
        subcategory = "End of Farm Notifier",
        name = "Time during chimes",
        description = "The amount of seconds between the chime sounds",
        category = "FarminFg"
    )
    public float farmnotifierChimeTime = 3;

    @Property(
        type = PropertyType.DECIMAL_SLIDER, 
            minF = 1,
            maxF = 120,
            subcategory = "End of Farm Notifier",
            name = "Highlight Time",
            description = "The amount of seconds that a highlighted region will stay highlighted for",
            category = "FarminFg"
    )
    public float farmHightlightTime = 30f;

// Garden
    @Property(
        type = PropertyType.SWITCH, 
        subcategory = "Garden", 
        name = "Garden Shop Trade Cost", 
        description = "Gives you information about the cost of garden shop trades.", 
        category = "FarminFg"
    )
    public boolean gardenShopTradeInfo = false;
    
    @Property(
        type = PropertyType.SWITCH, 
        subcategory = "Garden", 
        name = "Best Crops to Compost", 
        description = "Gives you information about which crops are the best to compost.", 
        category = "FarminFg"
    )
    public boolean bestCropsToCompost = false;
// ------------- Category: Economy ---------------------------------
// Community Center
    @Property(
        type = PropertyType.SWITCH, 
        subcategory = "Community Center", 
        name = "Best Item for Bits", 
        description = "Gives you information about which item in the Bits Shop is the best to sell.", 
        category = "Economy"
    )
    public boolean bestBitShopItem = false;

    @Property(
        type = PropertyType.SWITCH, 
        subcategory = "Community Center", 
            name = "Only Show Affordable Items", 
        description = "When making recommendations for what you can buy, only recommend the items that you are able to afford.", 
        category = "Economy"
    )
    public boolean bitShopOnlyShowAffordable = true;

    @Property(
        type = PropertyType.DECIMAL_SLIDER, 
            minF = 0,
            maxF = 100,
            category = "Economy",
            subcategory = "BIN Sniper",
            name = "BIN Snipe Percentage",
            description = "The percent of the price that the BIN sniper considers a \"snipe\". Example: 85%, Lowest BIN: 1 000 000, will look for a price of 850000 or less."
    )
    public float BINSniperPercent = 87f;


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
        type = PropertyType.SELECTOR, 

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


    @Property(
        type = PropertyType.DECIMAL_SLIDER, 
            name = "Master Scale",
            minF = .1f,
            maxF = 1,
            subcategory = "Auction House",
            category = "Economy"
    )
    public float masterAuctionHouseScale = .333333f;


    @Property(
        type = PropertyType.DECIMAL_SLIDER, 
            name = "Item Padding",
            minF = 0f,
            maxF = .2f,
            subcategory = "Auction House",
            category = "Economy"
    )
    public float auctionHouseItemPadding = .075f;

    @Property(
        type = PropertyType.DECIMAL_SLIDER, 
            name = "Side Bar Height",
            minF = .25f,
            maxF = 2f,
            subcategory = "Auction House",
            category = "Economy"
    )
    public float auctionHouseSideBarHeight = 1.333f;
    @Property(
        type = PropertyType.DECIMAL_SLIDER, 
            name = "Side Bar Width",
            minF = .25f,
            maxF = 2,
            subcategory = "Auction House",
            category = "Economy"
    )
    public float auctionHouseSideBarWidth = .667f;

    @Property(
        type = PropertyType.DECIMAL_SLIDER, 
            name = "Side Bar Padding",
            minF = -.5f,
            maxF = .5f,
            subcategory = "Auction House",
            category = "Economy"
    )
    public float auctionSideBarPadding = .05f;

    @Property(
        type = PropertyType.DECIMAL_SLIDER, 
            name = "Auction House Text Scale",
            minF = .11f,
            maxF = 2,
            subcategory = "Auction House",
            category = "Economy"
    )
    public float auctionHouseTextScale = .75f;






    // Excessive Coin warning
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
        minF = 0, 
        maxF = Integer.MAX_VALUE,
        name = "maxFimum Allowed Amount Without Booster Cookie", 
        category = "Economy", 
        description = "The maxFimum allowed amount of money allowed before it warns you about having no booster cookie.", 
        subcategory = "Excessive Coin Warning"
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
        minF = 1, 
        maxF = 300, 
        subcategory = "Excessive Coin Warning", 
        name = "Excessive Coin Warn Cooldown", 
        description = "The amount of seconds between each warning", 
        category = "Economy"
    )
    public int noCookieWarnCooldown = 20;

    //    ------------------ Category: Chat ---------------------
//    WordEditor
    @Property(
        type = PropertyType.SWITCH, 
        name = "Word Editor Main Toggle",
        category = "Chat",
        subcategory = "Word Editor",
        description = "Allows you to edit words in chat. Can be configured with /wordeditor"
    )
    public boolean wordEditor = true;

//Chat Colors
    @Property(
        type = PropertyType.SWITCH, 
            name = "Color Private Messages",
            category = "Chat",
            subcategory = "Chat Color",
            description = "Private messages pink to make them more visible in busy lobbies."
    )
    public boolean colorPrivateMessages = false;

    @Property(
        type = PropertyType.SWITCH, 
            name = "Color Nons Messages",
            category = "Chat",
            subcategory = "Chat Color",
            description = "Color messages from the non-ranked players to white to make them more visible in busy lobbies."
    )
    public boolean colorNonMessages = false;

    @Property(
        type = PropertyType.SWITCH, 
            name = "Color Party Chat",
            category = "Chat",
            subcategory = "Chat Color",
            description = "Color messages from the party chat blue to make them more visible in busy lobbies."
    )
    public boolean colorPartyChat = false;

    @Property(
        type = PropertyType.SWITCH, 
            name = "Color Guild Chat",
            category = "Chat",
            subcategory = "Chat Color",
            description = "Color messages from the guild chat green to make them more visible in busy lobbies."
    )
    public boolean colorGuildChat = false;

    @Property(
        type = PropertyType.SWITCH, 
            name = "Color Guild Officer Chat",
            category = "Chat",
            subcategory = "Chat Color",
            description = "Color messages from the guild officer chat aqua to make them more visible in busy lobbies."
    )
    public boolean colorOfficerChat = false;

    @Property(
        type = PropertyType.SWITCH, 
            name = "SkyBlock Co-op Chat",
            category = "Chat",
            subcategory = "Chat Color",
            description = "Color messages from the SkyBlock coop chat aqua to make them more visible in busy lobbies."
    )
    public boolean colorCoopChat = false;

    @Property(
        type = PropertyType.SWITCH, 
            name = "Visible Colors",
            category = "Chat",
            subcategory = "Chat Color",
            description = "Converts the custom colors mentioned above to more visible colors. Dark Green -> Light Green and Blue -> Gold. (Recommended)"
    )
    public boolean visibleColors = false;

    //Fun
    @Property(
        type = PropertyType.SWITCH, 
            name = "OwO Language toggle",
            category = "Chat",
            subcategory = "Fun",
            description = "Replaces all chat messages with OwO language.\nThis feature basically breaks the whole chat, so please be warned"
    )
    public boolean owoLanguage = false;


//    ------------- DEBUG ------------
//    @Property(
//            type = PropertyType.key
//            category = "Debug",
//            name = "Debug Keybind"
//    )
//    public static OneKeyBind debugConfig = new OneKeyBind(Keyboard.KEY_NONE);

}
