//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.system;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.Number;
import cc.polyfrost.oneconfig.config.annotations.*;
import cc.polyfrost.oneconfig.config.core.OneColor;
import cc.polyfrost.oneconfig.config.core.OneKeyBind;
import cc.polyfrost.oneconfig.config.data.InfoType;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import org.lwjgl.input.Keyboard;

public class OneConfigScreen extends Config {

    public OneConfigScreen() {
        // Available mod types: PVP, HUD, UTIL_QOL, HYPIXEL, SKYBLOCK
        super(new Mod("Partly Sane Skies", ModType.SKYBLOCK, "/assets/partlysaneskies/textures/logo_oneconfig.png"), "partly-sane-skies/config.json");
        initialize();
    }

    public void resetBrokenStrings() {
        if (arrowLowChatMessage.isEmpty()) {
            arrowLowChatMessage = "Partly Sane Skies > Warning! {player} only has {count} arrows remaining!";
            save();
        }
        if (watcherChatMessage.isEmpty()) {
            watcherChatMessage = "Partly Sane Skies > The watcher is done spawning mobs. Ready to clear.";
            save();
        }
        if (secretsChatMessageString.isEmpty()){
            secretsChatMessageString = "Partly Sane Skies > All required secrets have been found!";
            save();
        }
    }

    @Info(
        type = InfoType.INFO,
        text = "Hover over an option to see a description and more information."

    )
    public static boolean ignored;

    @HypixelKey
    @Text(
        secure = true, 
        name = "API Key", 
        category = "General", 
        subcategory = "API", 
        description = "Do /api new to automatically set your API Key. Do not show your API key to anyone unless you know what you're doing.",
            size = 2
    )
    public String apiKey = "";

    @Switch(
            name = "Force Custom API Key",
            category = "General",
            subcategory = "API",
            description = "Forces the use of a custom API key for Hypixel requests. (Requires API Key field to be populated)"
    )
    public boolean forceCustomAPIKey = false;

    @Number(
        min = .1f,
        max = 30f,
        name = "Time between requests",
        category = "General",
        subcategory = "API",
        description = "The time between API calls. Only change if you know what you're doing. Changing this will reduce the amount of time API requests take, however may result in more errors"
    )
    public float timeBetweenRequests = 0.5f;

    @Slider(
            name = "Player Data Cache Time",
            min = 0,
            max = 90,
            subcategory = "API",
            description = "Saves the data from other party members to save time upon loading data about players. The bigger the value the more minutes you will save but the less accurate your data will be.",
            category = "General"
    )
    public int playerDataCacheTime = 5;

    @Dropdown(
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

    @Dropdown(
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

    @Switch(
        category = "General",
        subcategory = "Appearance",
        name = "24 hour time",
        description = "Display time in 24-hour hour time (15:30) instead of 12 hour time (3:30 PM)"
    )
    public boolean hour24time = false;
    

    @Switch(
        category = "General",
        subcategory = "Appearance",
        name = "Legacy Version Warning",
        description = "Warns you if you are using a legacy version of Partly Sane Skies"
    )
    public boolean legacyVersionWarning = true;
    // Main Menu

    @Switch(
        category = "General", 
        subcategory = "Main Menu", 
        name = "Show a Custom Minecraft Main Menu"
    )
    public boolean customMainMenu = true;

    @Switch(
        category = "General", 
        subcategory = "Main Menu", 
        name = "Announcements on Main Menu",
        description = "Display announcements such as recent skyblock updates on the main menu"
    )
    public boolean displayAnnouncementsCustomMainMenu = true;

    @Dropdown(
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
        name = "Custom Minecraft Main Menu Image",
        description = "Select one of our many high quality included images, or you can use your custom image.\nTo use your own image, place your image in the /config/partly-sane-skies folder and title your image background.png"
    )
    public int customMainMenuImage = 1;

    @Switch(
        name = "Print errors in chat",
        category = "General",
        subcategory = "API",
        description = "Send errors on getting APIs in chat (Recommended, however if you get spammed or have a bad internet connection, turn it off)"
        
    )
    public boolean printApiErrors = true;


//    ------------------ Category: Themes ---------------------
//    Themes
    @Dropdown(
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
    @Switch(
            name = "Use default accent color",
            description = "Uses the default Partly Sane Skies accent color",
            category = "Themes",
            subcategory = "Accent Color"
    )
    public boolean useDefaultAccentColor = true;

    @Color(
            name = "Custom Accent Color",
            description = "Choose a custom accent color for your game",
            category = "Themes",
            subcategory = "Accent Color"
    )
    public OneColor accentColor = new OneColor(1, 255, 255, 255);

//    Custom Themes
    @Switch(
            name = "Create your own Theme",
            description = "Enable to be able to create your own custom themes",
            category = "Themes",
            subcategory = "Custom Themes"
    )
    public boolean customTheme = false;

    @Color(
            name = "Custom Primary Color",
            description = "Choose a custom primary color for your game",
            category = "Themes",
            subcategory = "Custom Themes"
    )
    public OneColor primaryColor = new OneColor(42, 43, 46, 255);

    @Color(
            name = "Custom Secondary Color",
            description = "Choose a custom secondary color for your game",
            category = "Themes",
            subcategory = "Custom Themes"
    )
    public OneColor secondaryColor = new OneColor(42, 43, 46, 255);


//    Resource Packs
    @Switch(
            name = "Disable themes to use resource packs",
            description = "Disable themes to be able to use resource packs to modify Partly Sane Skies menus",
            category = "Themes",
            subcategory = "Resource Packs"
    )
    public boolean disableThemes = false;











    // ----------------- Category: SkyBlock -------------------
    // Rare Drop
    @Switch( 
        name = "Rare Drop Banner", 
        subcategory = "Rare Drop", 
        description = "On rare drop, get a Pumpkin Dicer like banner.", 
        category = "SkyBlock"
    )
    public boolean rareDropBanner = false;


    @Slider(
        min = 1, 
        max = 7, 
        subcategory = "Rare Drop", 
        name = "Rare Drop Banner Time", 
        description = "The amount of seconds the rare drop banner appears for.", 
        category = "SkyBlock"
    )
    public float rareDropBannerTime = 3.5f;

    @Switch(
        name = "Custom Rare Drop Sound", 
        subcategory = "Rare Drop", 
        description = "Plays a custom sound when you get a rare drop.", 
        category = "SkyBlock"
    )
    public boolean rareDropBannerSound = false;

    // Location Banner
    @Switch( 
        name = "Location Banner", 
        subcategory = "Location Banner", 
        description = "An MMO RPG style banner shows up when you switch locations.", 
        category = "SkyBlock"
    )
    public boolean locationBannerDisplay = false;

    @Slider(
        min = 1, 
        max = 7, 
        subcategory = "Location Banner", 
        name = "Location Banner Time", 
        description = "The amount of seconds the location banner appears for.", 
        category = "SkyBlock"
    )
    public float locationBannerTime = 3.5f;

    // Open Wiki
    @Switch( 
        name = "Open Wiki Automatically", 
        category = "SkyBlock",
        description = "When the Open Wiki Article Keybind is used, automatically open the article without confirmation first.", 
        subcategory = "Open Wiki"
    )
    public boolean openWikiAutomatically = true;

    // Pet Minion Alert
    @Switch(
        name = "Incorrect Pet for Minion Alert", 
        category = "SkyBlock",
        description = "Warns you if you don't have the right pet for leveling up the minions, that way you never lose any pet EXP because you still have your level 100 dungeon pet activated.\nRequires pets to be visible.", 
        subcategory = "Incorrect Pet for Minion Alert"
    )
    public boolean incorrectPetForMinionAlert = false;

    @Switch(
            name = "Selected Pet Information",
            category = "SkyBlock",
            description = "Gives you information about the currently selected pet while in the minion menu\nRequires pets to be visible.",
            subcategory = "Incorrect Pet for Minion Alert"
    )
    public boolean selectedPetInformation = false;

    @Switch(
        name = "Air Raid Siren", 
        category = "SkyBlock",
        description = "Plays a WWII air raid siren when you have the wrong pet. \nPros: \nKeeps you up at late night grinds \n(RECOMMENDED, ESPECIALLY AT 3 AM).", 
        subcategory = "Incorrect Pet for Minion Alert"
    )
    public boolean incorrectPetForMinionAlertSiren = false;

    @Switch(
            name = "Refresh Keybind (Ctrl + R / Command + R / F5)",
            category = "SkyBlock",
            description = "Refresh any menu with a \"Refresh\" button with (Ctrl + R) or (Command + R), depending on your operating system.\nOr just use (F5).",
            subcategory = "Refresh Keybind"
    )
    public boolean refreshKeybind = false;

    @Text(
        category = "SkyBlock",
        subcategory = "Incorrect Pet for Minion Alert",
        name = "Selected Pet",
        description = "The selected pet that will be used for minion collecting (Use /pets and click the pet keybind to select",
        secure =  true,
        size = 2
    )
    public String selectedPet = /*PartlySaneSkies.config.selectededPet |*/ "";

    @Slider(
        min = 1, 
        max = 15, 
        subcategory = "Incorrect Pet for Minion Alert", 
        name = "Mute Time", 
        description = "The amount of minutes the pet alert will mute for when you mute it.", 
        category = "SkyBlock"
    )
    public float petAlertMuteTime = 7.5f;

    @Dropdown(
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

    @Dropdown(
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

    // -------------- Category: Mining --------------
    // Worm Warning
    @Switch(
        name = "Worm Warning Banner", 
        subcategory = "Worm Warning", 
        description = "A banner appears on your screen when a worm spawns.", 
        category = "Mining"
    )
    public boolean wormWarningBanner = false;

    @Color(
        subcategory = "Worm Warning", 
        name = "Worm Warning Banner Color", 
        description = "The color of the worm warning text", 
        category = "Mining"
    )
    public OneColor wormWarningBannerColor = new OneColor(34, 255, 0);

    @Slider(
        min = 1, 
        max = 7, 
        subcategory = "Worm Warning", 
        name = "Worm Warning Banner Time", 
        description = "The amount of seconds the worm warning banner appears for.", 
        category = "Mining"
    )
    public float wormWarningBannerTime = 3.5f;

    @Switch(
        name = "Worm Warning Sound", 
        subcategory = "Worm Warning", 
        description = "Plays a sound when a worm spawns.", 
        category = "Mining"
    )
    public boolean wormWarningBannerSound = false;

    //Pickaxes
    @Switch(
            name = "Pickaxe Ability Ready Banner",
            subcategory = "Pickaxes",
            description = "A banner appears on your screen when your pickaxe ability is ready.",
            category = "Mining"
    )
    public boolean pickaxeAbilityReadyBanner = true;

    @Switch(
            name = "Pickaxe Ability Ready Sound",
            subcategory = "Pickaxes",
            description = "Plays a sound when your pickaxe ability is ready.",
            category = "Mining"
    )
    public boolean pickaxeAbilityReadySound = false;

    @Switch(
            name = "Use Air Raid Siren for Pickaxe Ability Ready",
            subcategory = "Pickaxes",
            description = "Plays a WWII air raid siren when your pickaxe ability is ready. \nPros: \nKeeps you up at late night grinds \n(RECOMMENDED, ESPECIALLY AT 3 AM)",
            category = "Mining"
    )
    public boolean pickaxeAbilityReadySiren = false;

    @Switch(
            name = "Only give a warning when you are on a mining island",
            subcategory = "Pickaxes",
            description = "Makes it less annoying when you don't want to mine",
            category = "Mining"
    )
    public boolean onlyGiveWarningOnMiningIsland = true;

    @Slider(
            min = 1,
            max = 7,
            subcategory = "Pickaxes",
            name = "Ready Banner Time",
            description = "The amount of seconds the ready banner appears for.",
            category = "Mining"
    )
    public float pickaxeBannerTime = 3.5f;

    @Color(
            subcategory = "Pickaxes",
            name = "Ready Banner Color",
            description = "The color of the ready banner text",
            category = "Mining"
    )
    public OneColor pickaxeBannerColor = new OneColor(255, 255, 0);

    @Switch(
            name = "Block Ability on Private Island (UAYOR)",
            subcategory = "Pickaxes",
            description = "Blocks the use of pickaxe abilities on your private island. (Use at your own risk)",
            category = "Mining"
    )
    public boolean blockAbilityOnPrivateIsland = false;

    //Events
    @Info(
            type = InfoType.INFO,
            text = "Some Events may not trigger, not all have been tested. If you find an event that doesn't trigger, please report it on our discord server.",
            size = 2,
            category = "Mining",
            subcategory = "Events"
    )

    @Switch(
            name = "Main Toggle",
            category = "Mining",
            subcategory = "Events",
            description = "Toggles the events"
    )
    public boolean miningEventsToggle = true;

    @Switch(
            name = "Show Event Banner",
            category = "Mining",
            subcategory = "Events",
            description = "Shows a banner when an enabled event is going active"
    )
    public boolean miningShowEventBanner = true;

    @Switch(
            name = "Also warn 20s before event activation",
            category = "Mining",
            subcategory = "Events",
            description = "Shows a banner and plays sound 20s before an enabled event is going active"
    )
    public boolean miningWarn20sBeforeEvent = false;

    @Switch(
            name = "2x Powder activation sound",
            category = "Mining",
            subcategory = "Events",
            description = "Plays a sound when 2x Powder event is going active"
    )
    public boolean mining2xPowderSound = false;

    @Switch(
            name = "Gone with the wind activation sound",
            category = "Mining",
            subcategory = "Events",
            description = "Plays a sound when Gone with the wind event is going active"
    )
    public boolean miningGoneWithTheWindSound = false;

    @Switch(
            name = "Better Together activation sound",
            category = "Mining",
            subcategory = "Events",
            description = "Plays a sound when Better Together event is going active"
    )
    public boolean miningBetterTogetherSound = false;

    @Switch(
            name = "Goblin Raid activation sound",
            category = "Mining",
            subcategory = "Events",
            description = "Plays a sound when Goblin Raid event is going active"
    )
    public boolean miningGoblinRaidSound = false;

    @Switch(
            name = "Raffle activation sound",
            category = "Mining",
            subcategory = "Events",
            description = "Plays a sound when Raffle event is going active"
    )
    public boolean miningRaffleSound = false;

    @Switch(
            name = "Mithril Gourmand activation sound",
            category = "Mining",
            subcategory = "Events",
            description = "Plays a sound when Mithril Gourmand event is going active"
    )
    public boolean miningMithrilGourmandSound = false;

    @Switch(
            name = "Powder Ghast activation sound",
            category = "Mining",
            subcategory = "Events",
            description = "Plays a sound when Powder Ghast is about to spawn"
    )
    public boolean miningPowderGhastSound = false;

    @Switch(
            name = "Fallen Star activation sound",
            category = "Mining",
            subcategory = "Events",
            description = "Plays a sound when Fallen Star is about to spawn"
    )
    public boolean miningFallenStarSound = false;

    @Slider(
            min = 1,
            max = 7,
            subcategory = "Events",
            name = "Event Banner Time",
            description = "The amount of seconds the event banner appears for.",
            category = "Mining"
    )
    public float miningEventBannerTime = 3.5f;

    @Color(
            subcategory = "Events",
            name = "Event Banner Color",
            description = "The color of the event banner text",
            category = "Mining"
    )
    public OneColor miningEventBannerColor = new OneColor(255, 255, 255);

    // ------------- Category: Dungeons ---------------------------------
    // Party Manager
    @Switch(
        name = "Automatically kick offline on party manager load", 
        subcategory = "Party Manager", 
        description = "Automatically kicks offline members in your party when you open party manager.", 
        category = "Dungeons"
    )
    public boolean autoKickOfflinePartyManager = false;

    @Switch(
        name = "Warn Low Arrows in Chat", 
        subcategory = "Party Manager", 
        description = "Warns you party when a member has low arrows.", 
        category = "Dungeons"
    )
    public boolean warnLowArrowsInChat = false;

    @Text(
        subcategory = "Party Manager", 
        name = "Arrow Low Warning", 
        description = "Message to send when a player has low arrows.\nUse {player} to signify the player's username, and {count} to signify the remaining arrow count.", 
        category = "Dungeons",
            size = 2
    )
    public String arrowLowChatMessage = "Partly Sane Skies > Warning! {player} only has {count} arrows remaining!";

    @Number(
        name = "Arrow Low Count", 
        min = 0,
        max = 1000,
        subcategory = "Party Manager", 
        description = "The amount of arrows you must have to be considered low on arrows.", 
        category = "Dungeons"
    )
    public int arrowLowCount = 300;

    @Switch(
        name = "Print errors in chat",
        category = "Dungeons",
        subcategory = "Party Manager",
        description = "Send errors on getting data in chat (Recommended, however if you get spammed or have a bad internet connection, turn it off)"
        
    )
    public boolean printPartyManagerApiErrors = true;

    @Switch(
        name = "Get data on party join", 
        subcategory = "Party Manager", 
        description = "Automatically gets the data for party members someone joins the party. This saves time and reduces the chance of the data not being able to be accessed.", 
        category = "Dungeons"
    )
    public boolean getDataOnJoin = true;

    @Switch(
        name = "Toggle Run Colors in Partymanager",
        subcategory = "Party Manager",
        description = "Toggles the colors of the runs in the party manager. ",
        category = "Dungeons"
    )
    public boolean toggleRunColors = true;

    @Number(
        name = "Customize Max Runs for Red in Run Colors",
        min = 0,
        max = Integer.MAX_VALUE,
        subcategory = "Party Manager",
        description = "Customize maximum runs required for the color red",
        category = "Dungeons"
    )
    public int runColorsRedMax = 1;

    @Number(
        name = "Customize Max Runs for Yellow in Run Colors",
        min = 0,
        max = Integer.MAX_VALUE,
        subcategory = "Party Manager",
        description = "Customize maximum runs required for the color yellow",
        category = "Dungeons"
    )
    public int runColorsYellowMax = 9;



    // Watcher Ready Warning
    @Switch(
        name = "Watcher Ready Warning", 
        subcategory = "Watcher Ready", 
        description = "Sends a warning when the watcher is done spawning mobs.", 
        category = "Dungeons"
    )
    public boolean watcherReadyBanner = false;

    @Switch(
        name = "Watcher Ready Sound", 
        subcategory = "Watcher Ready", 
        description = "Plays a sound when the watcher is done spawning mobs.", 
        category = "Dungeons"
    )
    public boolean watcherReadySound = false;

    @Slider(
        min = 1, 
        max = 7, 
        subcategory = "Watcher Ready", 
        name = "Watcher Ready Banner Time", 
        description = "The amount of seconds the watcher ready banner appears for.", 
        category = "Dungeons"
    )
    public float watcherReadyBannerTime = 3.5f;

    @Color(
        subcategory = "Watcher Ready", 
        name = "Watcher Ready Banner Color", 
        description = "The color of the watcher ready text", 
        category = "Dungeons"
    )
    public OneColor watcherReadyBannerColor = new OneColor(255, 45, 6);

    @Switch(
        name = "Watcher Ready Chat Message", 
        subcategory = "Watcher Ready", 
        description = "Send a message to your party when watcher is done spawning mobs.", 
        category = "Dungeons"
    )
    public boolean watcherReadyChatMessage = false;


    @Text(
        subcategory = "Watcher Ready", 
        name = "Watcher Ready Text", 
        description = "Message to send when the watcher is ready to clear.", 
        category = "Dungeons",
            size = 2
    )
    public String watcherChatMessage = "Partly Sane Skies > The watcher is done spawning mobs. Ready to clear.";

    @Switch(
        subcategory = "Watcher Ready", 
        name = "Air Raid Siren", 
        description = "Plays a WWII air raid siren when the watcher is done spawning mobs. \nPros: \nKeeps you up at late night grinds \n(RECOMMENDED, ESPECIALLY AT 3 AM)", 
        category = "Dungeons"
    )
    public boolean watcherReadyAirRaidSiren = false;

    // Required Secrets Found
    @Switch(
            name = "Required Secrets Found Banner",
            subcategory = "Required Secrets Found",
            description = "Sends a warning when all required secrets have been found.",
            category = "Dungeons"
    )
    public boolean secretsBanner = false;

    @Switch(
            name = "Required Secrets Found Sound",
            subcategory = "Required Secrets Found",
            description = "Plays a sound when all required secrets have been found.",
            category = "Dungeons"
    )
    public boolean secretsSound = false;

    @Slider(
            min = 1,
            max = 7,
            subcategory = "Required Secrets Found",
            name = "Required Secrets Found Banner Time",
            description = "The amount of seconds the required secrets found banner appears for.",
            category = "Dungeons"
    )
    public float secretsBannerTime = 3.5f;

    @Color(
            subcategory = "Required Secrets Found",
            name = "Required Secrets Found Banner Color",
            description = "The color of the required secrets found text",
            category = "Dungeons"
    )
    public OneColor secretsBannerColor = new OneColor(255, 45, 6);

    @Switch(
            name = "Required Secrets Found Chat Message",
            subcategory = "Required Secrets Found",
            description = "Send a message to your party when all required secrets have been found.",
            category = "Dungeons"
    )
    public boolean secretsChatMessage = false;


    @Text(
            subcategory = "Required Secrets Found",
            name = "Required Secrets Found Text",
            description = "Message to send when all required secrets have been found.",
            category = "Dungeons",
            size = 2
    )
    public String secretsChatMessageString = "Partly Sane Skies > All required secrets have been found!";

    @Switch(
            subcategory = "Required Secrets Found",
            name = "Air Raid Siren",
            description = "Plays a WWII air raid siren when all required secrets have been found. \nPros: \nKeeps you up at late night grinds \n(RECOMMENDED, ESPECIALLY AT 3 AM)",
            category = "Dungeons"
    )
    public boolean secretsAirRaidSiren = false;

// Dungeon Player Breakdown
    @Switch(
        subcategory = "Dungeon Player Breakdown", 
        name = "Dungeon Player Breakdown", 
        description = "At the end of the dungeon, send a message informing you how much of the dungeon each player has completed", 
        category = "Dungeons"
    )
    public boolean dungeonPlayerBreakdown = false;

    @Dropdown(
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

    @Switch(
        subcategory = "Dungeon Player Breakdown", 
        name = "Send in Party Chat", 
        description = "Send a condensed version to the rest of your party.", 
        category = "Dungeons"
    )
    public boolean partyChatDungeonPlayerBreakdown = false;

    // ------------- Category: Farming ---------------------------------
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


//    Farn notifier
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

// Garden
    @Switch(
        subcategory = "Garden", 
        name = "Garden Shop Trade Cost", 
        description = "Gives you information about the cost of garden shop trades.", 
        category = "Farming"
    )
    public boolean gardenShopTradeInfo = false;
    
    @Switch(
        subcategory = "Garden", 
        name = "Best Crops to Compost", 
        description = "Gives you information about which crops are the best to compost.", 
        category = "Farming"
    )
    public boolean bestCropsToCompost = false;

    @Switch(
            subcategory = "Garden",
            name = "Display Garden Visitor Stats",
            description = "Shows visited/accepted stats per NPC rarity.\nPros: based on item tooltips, which might capture more Garden visitor data\n(especially if you had Garden visitors before you installed SkyHanni).\nCons: Only shows for current Visitor's Logbook page and not all pages.",
            category = "Farming"
    )
    public boolean visitorLogbookStats = false;
// ------------- Category: Economy ---------------------------------
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

    @Dropdown(
            category = "Economy",
            name = "Coins to Cookies Preferred Currency",
            description = "Select your preferred currency conversion for the /c2c command. Currencies are listed in alphabetical order. Default currency is USD.",
            options = {
                "AUD",
                "BRL",
                "CAD",
                "DKK",
                "EUR",
                "NOK",
                "NZD",
                "PLN",
                "GBP",
                "SEK",
                "USD"
            }
    )
    public int prefCurr = 10;

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

    //    ------------------ Category: Chat ---------------------
//    WordEditor
    @Switch(
        name = "Word Editor Main Toggle",
        category = "Chat",
        subcategory = "Word Editor",
        description = "Allows you to edit words in chat. Can be configured with /wordeditor"
    )
    public boolean wordEditor = true;

//Chat Colors
    @Switch(
            name = "Color Private Messages",
            category = "Chat",
            subcategory = "Chat Color",
            description = "Private messages pink to make them more visible in busy lobbies."
    )
    public boolean colorPrivateMessages = false;

    @Switch(
            name = "Color Nons Messages",
            category = "Chat",
            subcategory = "Chat Color",
            description = "Color messages from the non-ranked players to white to make them more visible in busy lobbies."
    )
    public boolean colorNonMessages = false;

    @Switch(
            name = "Color Party Chat",
            category = "Chat",
            subcategory = "Chat Color",
            description = "Color messages from the party chat blue to make them more visible in busy lobbies."
    )
    public boolean colorPartyChat = false;

    @Switch(
            name = "Color Guild Chat",
            category = "Chat",
            subcategory = "Chat Color",
            description = "Color messages from the guild chat green to make them more visible in busy lobbies."
    )
    public boolean colorGuildChat = false;

    @Switch(
            name = "Color Guild Officer Chat",
            category = "Chat",
            subcategory = "Chat Color",
            description = "Color messages from the guild officer chat aqua to make them more visible in busy lobbies."
    )
    public boolean colorOfficerChat = false;

    @Switch(
            name = "SkyBlock Co-op Chat",
            category = "Chat",
            subcategory = "Chat Color",
            description = "Color messages from the SkyBlock coop chat aqua to make them more visible in busy lobbies."
    )
    public boolean colorCoopChat = false;

    @Switch(
            name = "Visible Colors",
            category = "Chat",
            subcategory = "Chat Color",
            description = "Converts the custom colors mentioned above to more visible colors. Dark Green -> Light Green and Blue -> Gold. (Recommended)"
    )
    public boolean visibleColors = false;

    //Fun
    @Switch(
            name = "OwO Language toggle",
            category = "Chat",
            subcategory = "Fun",
            description = "Replaces all chat messages with OwO language.\nThis feature basically breaks the whole chat, so please be warned"
    )
    public boolean owoLanguage = false;


//    ------------- DEBUG ------------
    @KeyBind(
            category = "Debug",
            name = "Debug Keybind"
    )
    public static OneKeyBind debugConfig = new OneKeyBind(Keyboard.KEY_NONE);

}
