//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.config;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.Number;
import me.partlysanestudios.partlysaneskies.config.features.GeneralConfig;
import me.partlysanestudios.partlysaneskies.config.features.ThemesConfig;
import org.lwjgl.input.Keyboard;

import cc.polyfrost.oneconfig.config.annotations.*;
import cc.polyfrost.oneconfig.config.core.OneColor;
import cc.polyfrost.oneconfig.config.core.OneKeyBind;
import cc.polyfrost.oneconfig.config.data.InfoType;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import cc.polyfrost.oneconfig.libs.universal.UKeyboard;
import me.partlysanestudios.partlysaneskies.features.dungeons.PearlRefill;

public class OneConfigScreen extends Config {

    public OneConfigScreen() {
        // Available mod types: PVP, HUD, UTIL_QOL, HYPIXEL, SKYBLOCK
        super(new Mod("Partly Sane Skies", ModType.SKYBLOCK, "/assets/partlysaneskies/textures/logo_oneconfig.png"), "partly-sane-skies/config.json");
        initialize();

        registerKeyBind(refillPearlsKeybind, PearlRefill.INSTANCE::keybindAction);
    }

    public void resetBrokenStringsTick() {
        if (arrowLowChatMessage.isEmpty()) {
            arrowLowChatMessage = "Partly Sane Skies > Warning! {player} only has {count} arrows remaining!";
            save();
        }
        if (watcherChatMessage.isEmpty()) {
            watcherChatMessage = "Partly Sane Skies > The watcher is done spawning mobs. Ready to clear.";
            save();
        }
        if (secretsChatMessageString.isEmpty()) {
            secretsChatMessageString = "Partly Sane Skies > All required secrets have been found!";
            save();
        }
        if (pickaxeAbilityReadyBannerText.isEmpty()) {
            pickaxeAbilityReadyBannerText = "Pickaxe Ability Ready!";
            save();
        }

        if (autoGGMessageSPlus.isEmpty()) {
            autoGGMessageSPlus = "GG Easy";
            save();
        }
        if (autoGGMessageS.isEmpty()) {
            autoGGMessageS = "GG";
            save();
        }
        if (autoGGMessageSPlus.isEmpty()) {
            autoGGMessageOther = "Welp, GG";
            save();
        }


        if (general.repoOwner.isEmpty()) {
            general.repoOwner = "PartlySaneStudios";
            save();
        }
        if (general.repoName.isEmpty()) {
            general.repoName = "partly-sane-skies-public-data";
            save();
        }
    }

    public GeneralConfig general = new GeneralConfig();
    public ThemesConfig themes = new ThemesConfig();


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
    @KeyBind(
            name = "Wiki Article Opener Hotkey",
            category = "SkyBlock",
            subcategory = "Open Wiki"
    )
    public OneKeyBind wikiKeybind = new OneKeyBind(Keyboard.KEY_NONE);

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
            secure = true,
            size = 2
    )
    public String selectedPet = /*PartlySaneSkies.Companion.getConfig().selectededPet |*/ "";

    @Slider(
            min = 1,
            max = 15,
            subcategory = "Incorrect Pet for Minion Alert",
            name = "Mute Time",
            description = "The amount of minutes the pet alert will mute for when you mute it.",
            category = "SkyBlock"
    )
    public float petAlertMuteTime = 7.5f;
    @KeyBind(
            name = "Favorite Pet Hotkey",
            category = "SkyBlock",
            subcategory = "Incorrect Pet for Minion Alert"
    )
    public OneKeyBind favouritePetKeybind = new OneKeyBind(Keyboard.KEY_NONE);

//    Enhanced sound

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

//    Shortcuts
//    Config
    @KeyBind(
            name = "Wardrobe Menu Hotkey",
            category = "SkyBlock",
            subcategory = "Shortcuts"
    )
    public OneKeyBind wardrobeKeybind = new OneKeyBind(Keyboard.KEY_NONE);
    @KeyBind(
            name = "Pet Menu Hotkey",
            category = "SkyBlock",
            subcategory = "Shortcuts"
    )
    public OneKeyBind petKeybind = new OneKeyBind(Keyboard.KEY_NONE);
    @KeyBind(
            name = "Crafting Menu Hotkey",
            category = "SkyBlock",
            subcategory = "Shortcuts"
    )
    public OneKeyBind craftKeybind = new OneKeyBind(Keyboard.KEY_NONE);
    @KeyBind(
            name = "Storage Menu Hotkey",
            category = "SkyBlock",
            subcategory = "Shortcuts"
    )
    public OneKeyBind storageKeybind = new OneKeyBind(Keyboard.KEY_NONE);


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

    @KeyBind(
            name = "Hotkey",
            subcategory = "Party Manager",
            category = "Dungeons"
    )
    public OneKeyBind partyManagerKeybind = new OneKeyBind(Keyboard.KEY_M);


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

    // Healer Alert
    @Switch(
            subcategory = "Healer Alert",
            name = "Healer Alert",
            description = "Displays a banner when a teammate in Dungeons has low health.",
            category = "Dungeons"
    )
    public boolean healerAlert = false;

    @Dropdown(
            subcategory = "Healer Alert",
            name = "Alert when below...",
            description = "Choose at what percentage healer alert will trigger",
            category = "Dungeons",
            options = {
                    "25% Health",
                    "50% Health"
            }
    )
    public int colouredHealerAlert = 0;
    @Slider(
            subcategory = "Healer Alert",
            name = "Cooldown Between Warnings",
            description = "Choose the delay between Low Health Alerts",
            category = "Dungeons",
            min = 1f,
            max = 15f
    )
    public float healerAlertCooldownSlider = 3.5f;

    // Pearl Refill
    @Switch(
            name = "Auto Pearl Refill",
            subcategory = "Pearl Refill",
            description = "Automatically refills your pearls when a run starts.",
            category = "Dungeons"
    )
    public boolean autoPearlRefill = false;

    @KeyBind(
            name = "Refill Pearls Hotkey",
            subcategory = "Pearl Refill",
            description = "The keybind to automatically refill your pearls.",
            category = "Dungeons"
    )
    public static OneKeyBind refillPearlsKeybind = new OneKeyBind(UKeyboard.KEY_P);

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
    @Slider(
            subcategory = "Dungeons Player Breakdown",
            name = "Delay",
            description = "Delay after a dungeon ends",
            category = "Dungeons",
            min = 0,
            max = 20
    )
    public float dungeonPlayerBreakdownDelay = 1f;

    @Switch(
            subcategory = "Dungeon Player Breakdown",
            name = "Send in Party Chat",
            description = "Send a condensed version to the rest of your party.",
            category = "Dungeons"
    )
    public boolean partyChatDungeonPlayerBreakdown = false;

    @Switch(
            subcategory = "Dungeon Player Breakdown",
            name = "Dungeon Snitcher",
            description = "Automatically sends messages recommending to kick party members",
            category = "Dungeons"
    )
    public boolean dungeonSnitcher = false;

    @Slider(
            subcategory = "Dungeon Player Breakdown",
            name = "Dungeon Snitcher Percent",
            description = "Percentage of a dungeon complete where the player is slacking",
            category = "Dungeons",
            min = 0,
            max = 50
    )
    public float dungeonSnitcherPercent = 7.5f;

    //    Auto GG
    @Switch(
            subcategory = "Auto GG",
            name = "Enable Automatic GG message",
            description = "Sends an automatic gg message of your choosing whenever a dungeon is complete.",
            category = "Dungeons"
    )
    public boolean autoGgEnabled = false;

    @Slider(
            subcategory = "Auto GG",
            name = "Cooldown after dungeon ends",
            description = "The amount of seconds after a dungeon ends before the auto gg message is sent.",
            category = "Dungeons",
            min = 0,
            max = 10
    )
    public float autoGGCooldown = 1.5f;

    @Dropdown(
            subcategory = "Auto GG",
            name = "Send in",
            description = "Where to send the auto gg message",
            category = "Dungeons",
            options = {"Party Chat", "All Chat"}
    )
    public int sendAutoGGInWhatChat = 0;

    @Text(
            subcategory = "Auto GG",
            category = "Dungeons",
            name = "Text when S+ score",
            description = "Sends this message whenever a dungeon is complete and the score is S+."
    )
    public String autoGGMessageSPlus = "GG Easy";

    @Text(
            subcategory = "Auto GG",
            category = "Dungeons",
            name = "Text when S score",
            description = "Sends this message whenever a dungeon is complete and the score is S."
    )
    public String autoGGMessageS = "GG";

    @Text(
            subcategory = "Auto GG",
            category = "Dungeons",
            name = "Text when other score",
            description = "Sends this message whenever a dungeon is complete and the score is not S+/S."
    )
    public String autoGGMessageOther = "Welp, GG";

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

    @Text(
            name = "Banner Text",
            subcategory = "Pickaxes",
            description = "The text that appears on the banner when your pickaxe ability is ready.",
            category = "Mining"
    )
    public String pickaxeAbilityReadyBannerText = "Pickaxe Ability Ready!";

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
            name = "Hide Ready Message from Chat",
            subcategory = "Pickaxes",
            description = "Hides the message that appears in chat when your pickaxe ability is ready.",
            category = "Mining"
    )
    public boolean hideReadyMessageFromChat = false;

    @Switch(
            name = "Warn only on mining islands.",
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
            name = "Send System Notifications",
            category = "Mining",
            subcategory = "Events",
            description = "Sends a system notification when an event is going active"
    )
    public boolean miningSendSystemNotifications = false;


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

// ------------- Category: Foraging ---------------------------------
//    Treecapitator Cooldown Indicator
    @Switch(
            subcategory = "Treecapitator Cooldown Indidcator",
            name = "Treecapitator Cooldown Indidcator Enabled",
            description = "Displays a cooldown indicator below your crosshair whenever your treecapitator is on cooldown",
            category = "Foraging"
    )
    public boolean treecapCooldown = false;

    @Switch(
            subcategory = "Treecapitator Cooldown Indidcator",
            name = "Use Monkey Pet",
            description = "Use the monkey pet to dynamically adjust the length of the cooldown",
            category = "Foraging"
    )
    public boolean treecapCooldownMonkeyPet = true;



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

    //  Chat Alerts
    @Switch(
            name = "Send System Notification",
            category = "Chat",
            subcategory = "Chat Alerts",
            description = "Sends a system notification when a message triggered by the Chat Alert was send."
    )
    public boolean chatAlertSendSystemNotification = false;

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
            name = "Debug Hotkey"
    )
    public OneKeyBind debugKeybind = new OneKeyBind(Keyboard.KEY_NONE);

    @Switch(
            name = "Debug Mode",
            category = "Debug"
    )
    public boolean debugMode = false;



    @Switch(
            name = "Render TEST Banner",
            category = "Debug"
    )
    public boolean debugRenderTestBanner = false;

    @Switch(
            name = "Chat Analyser",
            category = "Debug"
    )
    public boolean debugChatAnalyser = false;

    @Switch(
            name = "Add a slacker to the party",
            category = "Debug"
    )
    public boolean debugAddSlacker = false;

    @Switch(
            name = "Spawn Waypoint",
            category = "Debug",
            description = "Spawns a waypoint at your current location"
    )
    public boolean debugSpawnWaypoint = false;

    @Switch(
            name = "Send a system notification",
            category = "Debug"
    )
    public boolean debugSendSystemNotification = false;

    @Switch(
            name = "Print pet world parsing information",
            category = "Debug"
    )
    public boolean debugPrintPetWorldParsingInformation = false;

    @Switch(
        name = "Print current location from Island Type",
        category = "Debug"
    )
    public boolean debugPrintCurrentLocationFromIslandType = false;

    @Switch(
            name = "Percy Mode",
            category = "Debug",
            subcategory = "Percy Mode"
    )
    public boolean percyMode = false;

    @Switch(
            name = "Current Screen",
            category = "Debug",
            subcategory = "Percy Mode"
    )
    public boolean debugCurrentScreenDump = false;
    @Switch(
            name = "Entity Dump",
            category = "Debug",
            subcategory = "Percy Mode"
    )
    public boolean debugEntityDump = false;    @Switch(
            name = "Inventory Dump",
            category = "Debug",
            subcategory = "Percy Mode"
    )
    public boolean debugInventoryDump = false;    @Switch(
            name = "Player Dump",
            category = "Debug",
            subcategory = "Percy Mode"
    )
    public boolean debugPlayerDump = false;


}
