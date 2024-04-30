//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.config

import cc.polyfrost.oneconfig.config.Config
import cc.polyfrost.oneconfig.config.annotations.*
import cc.polyfrost.oneconfig.config.annotations.Number
import cc.polyfrost.oneconfig.config.core.OneColor
import cc.polyfrost.oneconfig.config.core.OneKeyBind
import cc.polyfrost.oneconfig.config.data.InfoType
import cc.polyfrost.oneconfig.config.data.Mod
import cc.polyfrost.oneconfig.config.data.ModType
import me.partlysanestudios.partlysaneskies.features.debug.ExampleHud
import me.partlysanestudios.partlysaneskies.features.gui.hud.CooldownHud
import org.lwjgl.input.Keyboard

object OneConfigScreen : Config(
    // Available mod types: PVP, HUD, UTIL_QOL, HYPIXEL, SKYBLOCK
    Mod("Partly Sane Skies", ModType.SKYBLOCK, "/assets/partlysaneskies/textures/logo_oneconfig.png"),
    "partly-sane-skies/config.json"
) {
    fun resetBrokenStringsTick() {
        if (arrowLowChatMessage.isEmpty()) {
            arrowLowChatMessage = "Partly Sane Skies > Warning! {player} only has {count} arrows remaining!"
            save()
        }
        if (watcherChatMessage.isEmpty()) {
            watcherChatMessage = "Partly Sane Skies > The watcher is done spawning mobs. Ready to clear."
            save()
        }
        if (secretsChatMessageString.isEmpty()) {
            secretsChatMessageString = "Partly Sane Skies > All required secrets have been found!"
            save()
        }
        if (pickaxeAbilityReadyBannerText.isEmpty()) {
            pickaxeAbilityReadyBannerText = "Pickaxe Ability Ready!"
            save()
        }
        if (autoGGMessageSPlus.isEmpty()) {
            autoGGMessageSPlus = "GG Easy"
            save()
        }
        if (autoGGMessageS.isEmpty()) {
            autoGGMessageS = "GG"
            save()
        }
        if (autoGGMessageSPlus.isEmpty()) {
            autoGGMessageOther = "Welp, GG"
            save()
        }
        if (repoOwner.isEmpty()) {
            repoOwner = "PartlySaneStudios"
            save()
        }
        if (repoName.isEmpty()) {
            repoName = "partly-sane-skies-public-data"
            save()
        }
        if (apiUrl.isEmpty()) {
            apiUrl = "http://partlysanecloud.su386.dev"
            save()
        }
        if(prettyMimicKilledString.isEmpty()) {
            prettyMimicKilledString = "Mimic Killed!"
            save()
        }
    }

    /**
     * Config Rules:
     *
     * Order of the parameters:
     * 1. Name
     * 2. Description
     * 3. Category
     * 4. Subcategory
     * 5. Size (not required)
     * 6. Depending on the annotation, the rest of the parameters are different.
     *
     *
     * For ease of organization and understanding, each category and subcategory is clearly delineated<br>
     * within the code comments using the following format:<br>
     * // ------------- Category: CATEGORY_NAME ---------------------------------<br>
     * // SUBCATEGORY_NAME
     */



    // ------------- Category: General ---------------------------------
    @Info(
        type = InfoType.INFO,
        text = "Hover over an option to see a description and more information."
    )
    var ignored = false

    @Dropdown(
        name = "Update Channel",
        description = "Select the update channel you want to use.",
        category = "General",
        subcategory = "Updates",
        options = ["Release", "Pre-release"]
    )
    var releaseChannel = "@RELEASE_CHANNEL@".toInt()

    //    Discord
    @Switch(
        name = "Discord RPC",
        category = "General",
        subcategory = "Discord"
    )
    var discordRPC = true

    @Switch(
        name = "Only show Discord RPC when in Skyblock",
        category = "General",
        subcategory = "Discord"
    )
    var discordRPCOnlySkyblock = false

    @Dropdown(
        name = "Playing...",
        category = "General",
        subcategory = "Discord",
        options = ["Hypixel Skyblock", "sbe bad"]
    )
    var discordPlayingMode = 0

    @Text(
        name = "Discord Game Name",
        category = "General",
        subcategory = "Discord"
    )
    var discordRPCName = "sbe bad"

    @Text(
        name = "Discord Game Description",
        category = "General",
        subcategory = "Discord"
    )
    var discordRPCDescription = "Playing Hypixel Skyblock"

    //    Appearance
    @Dropdown(
        name = "Hundreds Place Format",
        description = "The separator between different hundreds places.",
        category = "General",
        subcategory = "Appearance",
        options = ["Commas (1,000,000)", "Spaces (1 000 000)", "Periods (1.000.000)"]
    )
    var hundredsPlaceFormat = 1

    @Dropdown(
        name = "Decimal Place Format",
        description = "The character to represent decimal places.",
        category = "General",
        subcategory = "Appearance",
        options = ["Commas (1,52)", "Periods (1.52)"]
    )
    var decimalPlaceFormat = 1

    @Switch(
        name = "24 hour time",
        description = "Display time in 24-hour hour time (15:30) instead of 12 hour time (3:30 PM).",
        category = "General",
        subcategory = "Appearance"
    )
    var hour24time = false

    @Dropdown(
        name = "Preferred Currency",
        description = "Select your preferred currency conversion for the /c2c command. Currencies are listed in alphabetical order. Default currency is USD.",
        category = "General",
        subcategory = "Appearance",
        options = ["AUD (Australian Dollar)", "BRL (Brazilian Real)", "CAD (Canadian Dollar)", "DKK (Danish Krone)", "EUR (Euro)", "KPW (North Korean Won)", "NOK (Norwegian Krone)", "NZD (New Zealand Dollar)", "PLN (Polish Zloty)", "GBP (Pound Sterling)", "SEK (Swedish Krona)", "USD (United States Dollar)"]
    )
    var prefCurr = 11

    @Slider(
        name = "Banner Size",
        description = "The size of the banners that appear on your screen.",
        category = "General",
        subcategory = "Appearance",
        min = 0.1f,
        max = 2f
    )
    var bannerSize = 1f

    // Main Menu
    @Switch(
        name = "Show a Custom Minecraft Main Menu",
        description = "Show a custom main menu with a background image and announcements",
        category = "General",
        subcategory = "Main Menu"
    )
    var customMainMenu = true

    @Switch(
        name = "Announcements on Main Menu",
        description = "Display announcements such as recent skyblock updates on the main menu.",
        category = "General",
        subcategory = "Main Menu"
    )
    var displayAnnouncementsCustomMainMenu = true

    @Dropdown(
        name = "Custom Minecraft Main Menu Image",
        description = "Select one of our many high quality included images, or you can use your custom image.\nTo use your own image, place your image in the \"/config/partly-sane-skies\" folder and title your image \"background.png\".",
        category = "General",
        subcategory = "Main Menu",
        options = ["Random Image", "View of Main Hub Mountain", "Aerial View of Hub from Community House", "Stunning Aerial View of Hub", "View from Hub Portal (Day)", "Hub Portal (Night)", "Wolf Ruins", "Custom Image"]
    )
    var customMainMenuImage = 1

    @Switch(
        name = "Check Mods On Startup",
        description = "Automatically Send Message on Startup.",
        category = "General",
        subcategory = "Mods Checker"
    )
    var checkModsOnStartup = true

    @Switch(
        name = "Use Beta Versions",
        description = "Use the beta version of mods instead of normal versions.",
        category = "General",
        subcategory = "Mods Checker"
    )
    // if someone boots their game the first time when after this got added,
    // it will default to true, so it will check for beta mods
    var lookForBetaMods = "0" != "@RELEASE_CHANNEL@"

    @Switch(
        name = "Show up to date mods",
        description = "Show mods that are up to date.",
        category = "General",
        subcategory = "Mods Checker"
    )
    var showUpToDateMods = true

    @Dropdown(
        name = "Privacy Mode",
        description = "Blocks the diagnostics reports from other mods from being sent to their servers. (Partly Sane Skies cannot guarantee it's ability to stop all data)",
        category = "General",
        subcategory = "Privacy",
        size = 2,
        options = [
            "[Off] Always allow mods to send telemetry",
            "[Protected] Allow telemetry after startup (recommended)",
            "[Strong] Allow telemetry after mod initialization ",
            "[Strict] Always block telemetry (May causes crashes)"
        ]
    )
    var privacyMode = 1

    //    Config
    @KeyBind(
        name = "Config Hotkey",
        description = "The keybind to open the config menu.",
        category = "General",
        subcategory = "Config"
    )
    var oneConfigKeybind = OneKeyBind(Keyboard.KEY_F7)

    //    Help
    @KeyBind(
        name = "Help Hotkey",
        description = "The keybind to show the mod commands.",
        category = "General",
        subcategory = "Help"
    )
    var helpKeybind = OneKeyBind(Keyboard.KEY_H)

    //    ------------------ Category: Themes ---------------------
    //    Themes
    @Dropdown(
        name = "Selected Theme",
        description = "Pick from one of our 9 custom designed themes.",
        category = "Themes",
        subcategory = "Theme",
        options = ["Classic (Dark)", "Royal Dark (Dark)", "Midnight Forest (Dark)", "Moonless Night (Very Dark)", "Stormy Night (Very Dark)", "The Void (Very Dark)", "Classic (Light)", "Royal Light (Light)", "Partly Cloudy (Light)", "Waterfall (Colorful)", "Jungle (Colorful)", "Dunes (Colorful)"]
    )
    var themeIndex = 0

    //    Accent Color
    @Switch(
        name = "Use custom accent color",
        description = "Uses the default Partly Sane Skies accent color.",
        category = "Themes",
        subcategory = "Accent Color"
    )
    var useCustomAccentColor = false

    @Color(
        name = "Custom Accent Color",
        description = "Choose a custom accent color for your game.",
        category = "Themes",
        subcategory = "Accent Color"
    )
    var accentColor = OneColor(1, 255, 255, 255)

    //    Custom Themes
    @Switch(
        name = "Create your own Theme",
        description = "Enable to be able to create your own custom themes.",
        category = "Themes",
        subcategory = "Custom Themes"
    )
    var customTheme = false

    @Color(
        name = "Custom Primary Color",
        description = "Choose a custom primary color for your game.",
        category = "Themes",
        subcategory = "Custom Themes"
    )
    var primaryColor = OneColor(42, 43, 46, 255)

    @Color(
        name = "Custom Secondary Color",
        description = "Choose a custom secondary color for your game.",
        category = "Themes",
        subcategory = "Custom Themes"
    )
    var secondaryColor = OneColor(42, 43, 46, 255)

    //    Resource Packs
    @Switch(
        name = "Disable themes to use resource packs",
        description = "Disable themes to be able to use resource packs to modify Partly Sane Skies menus.",
        category = "Themes",
        subcategory = "Resource Packs"
    )
    var disableThemes = false

    // ----------------- Category: SkyBlock -------------------
    // Rare Drop
    @Switch(
        name = "Rare Drop Banner",
        description = "On rare drop, get a Pumpkin Dicer like banner.",
        category = "SkyBlock",
        subcategory = "Rare Drop"
    )
    var rareDropBanner = false

    @Slider(
        name = "Rare Drop Banner Time",
        description = "The amount of seconds the rare drop banner appears for.",
        category = "SkyBlock",
        subcategory = "Rare Drop",
        min = 1f,
        max = 7f
    )
    var rareDropBannerTime = 3.5f

    @Switch(
        name = "Custom Rare Drop Sound",
        description = "Plays a custom sound when you get a rare drop.",
        category = "SkyBlock",
        subcategory = "Rare Drop"
    )
    var rareDropBannerSound = false

    @Switch(
        name = "Block Common Drops",
        description = "Blocks Drops with the rarity of Common.",
        category = "SkyBlock",
        subcategory = "Rare Drop"
    )
    var blockCommonDrops = false

    @Switch(
        name = "Block Uncommon Drops",
        description = "Blocks Drops with the rarity of Uncommon.",
        category = "SkyBlock",
        subcategory = "Rare Drop"
    )
    var blockUncommonDrops = false

    @Switch(
        name = "Block Rare Drops",
        description = "Blocks Drops with the rarity of Rare.",
        category = "SkyBlock",
        subcategory = "Rare Drop"
    )
    var blockRareDrops = false

    @Switch(
        name = "Block Epic Drops",
        description = "Blocks Drops with the rarity of Epic.",
        category = "SkyBlock",
        subcategory = "Rare Drop"
    )
    var blockEpicDrops = false

    @Switch(
        name = "Block Legendary Drops",
        description = "Blocks Drops with the rarity of Legendary.",
        category = "SkyBlock",
        subcategory = "Rare Drop"
    )
    var blockLegendaryDrops = false

    // Location Banner
    @Switch(
        name = "Location Banner",
        description = "An MMO RPG style banner shows up when you switch locations.",
        category = "SkyBlock",
        subcategory = "Location Banner"
    )
    var locationBannerDisplay = false

    @Slider(
        name = "Location Banner Time",
        description = "The amount of seconds the location banner appears for.",
        category = "SkyBlock",
        subcategory = "Location Banner",
        min = 1f,
        max = 7f
    )
    var locationBannerTime = 3.5f

    // Open Wiki
    @Switch(
        name = "Open Wiki Automatically",
        description = "When the Open Wiki Article Keybind is used, automatically open the article without confirmation first.",
        category = "SkyBlock",
        subcategory = "Open Wiki"
    )
    var openWikiAutomatically = true

    @KeyBind(
        name = "Wiki Article Opener Hotkey",
        description = "The keybind to open the wiki article.",
        category = "SkyBlock",
        subcategory = "Open Wiki"
    )
    var wikiKeybind = OneKeyBind(Keyboard.KEY_NONE)

    // Pet Minion Alert
    @Switch(
        name = "Incorrect Pet for Minion Alert",
        description = "Warns you if you don't have the right pet for leveling up the minions, that way you never lose any pet EXP because you still have your level 100 dungeon pet activated.\nRequires pets to be visible.",
        category = "SkyBlock",
        subcategory = "Incorrect Pet for Minion Alert"
    )
    var incorrectPetForMinionAlert = false

    @Switch(
        name = "Selected Pet Information",
        description = "Gives you information about the currently selected pet while in the minion menu.\nRequires pets to be visible.",
        category = "SkyBlock",
        subcategory = "Incorrect Pet for Minion Alert"
    )
    var selectedPetInformation = false

    @Switch(
        name = "Air Raid Siren",
        description = "Plays a WWII air raid siren when you have the wrong pet. \nPros: \nKeeps you up at late night grinds \n(RECOMMENDED, ESPECIALLY AT 3 AM).",
        category = "SkyBlock",
        subcategory = "Incorrect Pet for Minion Alert"
    )
    var incorrectPetForMinionAlertSiren = false

    @Switch(
        name = "Refresh Keybind (Ctrl + R / Command + R / F5)",
        description = "Refresh any menu with a \"Refresh\" button with (Ctrl + R) or (Command + R), depending on your operating system.\nOr just use (F5).",
        category = "SkyBlock",
        subcategory = "Refresh Keybind"
    )
    var refreshKeybind = false

    @Text(
        name = "Selected Pet",
        description = "The selected pet that will be used for minion collecting (Use /pets and click the pet keybind to select).",
        category = "SkyBlock",
        subcategory = "Incorrect Pet for Minion Alert",
        size = 2,
        secure = true
    )
    var selectedPet = ""

    @Slider(
        name = "Mute Time",
        description = "The amount of minutes the pet alert will mute for when you mute it.",
        category = "SkyBlock",
        subcategory = "Incorrect Pet for Minion Alert",
        min = 1f,
        max = 15f
    )
    var petAlertMuteTime = 7.5f

    @KeyBind(
        name = "Favorite Pet Hotkey",
        description = "The keybind to favorite the pet you have selected.",
        category = "SkyBlock",
        subcategory = "Incorrect Pet for Minion Alert"
    )
    var favouritePetKeybind = OneKeyBind(Keyboard.KEY_NONE)

    //    Enhanced sound
    @Dropdown(
        name = "Note Block Instrument Type",
        description = "Choose the instrument type for the note block sounds.",
        category = "SkyBlock",
        subcategory = "Enhanced SkyBlock Sounds",
        options = ["Default SkyBlock Noteblocks", "Clarinet (Live)", "Clarinet (Computer)", "Electric Piano", "Flute", "Organ", "Piano", "String Orchestra", "Trombone", "Trumpet", "Violin", "Wind Ensemble", "Discord New Message Sound", "Kazoo"]
    )
    var customSoundOption = 0

    @Dropdown(
        name = "Explosions",
        description = "Choose the explosion sound.",
        category = "SkyBlock",
        subcategory = "Enhanced SkyBlock Sounds",
        options = ["Default", "Off", "Realistic"]
    )
    var customExplosion = 0

    //    Shortcuts
    @KeyBind(
        name = "Wardrobe Menu Hotkey",
        description = "The keybind to open the wardrobe menu.",
        category = "SkyBlock",
        subcategory = "Shortcuts"
    )
    var wardrobeKeybind = OneKeyBind(Keyboard.KEY_NONE)

    @KeyBind(
        name = "Pet Menu Hotkey",
        description = "The keybind to open the pet menu.",
        category = "SkyBlock",
        subcategory = "Shortcuts"
    )
    var petKeybind = OneKeyBind(Keyboard.KEY_NONE)

    @KeyBind(
        name = "Crafting Menu Hotkey",
        description = "The keybind to open the crafting menu.",
        category = "SkyBlock",
        subcategory = "Shortcuts"
    )
    var craftKeybind = OneKeyBind(Keyboard.KEY_NONE)

    @KeyBind(
        name = "Storage Menu Hotkey",
        description = "The keybind to open the storage menu.",
        category = "SkyBlock",
        subcategory = "Shortcuts"
    )
    var storageKeybind = OneKeyBind(Keyboard.KEY_NONE)

    // Cooldown Hud
    @HUD(
        name = "Cooldown HUD",
        category = "SkyBlock",
        subcategory = "Cooldown HUD"
    )
    var cooldownHud = CooldownHud.oneConfigHud

    // ------------- Category: Dungeons ---------------------------------
    // Party Manager
    @Switch(
        name = "Automatically kick offline on party manager load",
        description = "Automatically kicks offline members in your party when you open party manager.",
        category = "Dungeons",
        subcategory = "Party Manager"
    )
    var autoKickOfflinePartyManager = false

    @Switch(
        name = "Warn Low Arrows in Chat",
        description = "Warns you party when a member has low arrows.",
        category = "Dungeons",
        subcategory = "Party Manager"
    )
    var warnLowArrowsInChat = false

    @Text(
        name = "Arrow Low Warning",
        description = "Message to send when a player has low arrows.\nUse {player} to signify the player's username, and {count} to signify the remaining arrow count.",
        category = "Dungeons",
        subcategory = "Party Manager",
        size = 2
    )
    var arrowLowChatMessage = "Partly Sane Skies > Warning! {player} only has {count} arrows remaining!"

    @Number(
        name = "Arrow Low Count",
        subcategory = "Party Manager",
        description = "The amount of arrows you must have to be considered low on arrows.",
        category = "Dungeons",
        min = 0f,
        max = 1000f
    )
    var arrowLowCount = 300

    @Switch(
        name = "Get data on party join",
        description = "Automatically gets the data for party members someone joins the party. This saves time and reduces the chance of the data not being able to be accessed.",
        category = "Dungeons",
        subcategory = "Party Manager"
    )
    var getDataOnJoin = true

    @Switch(
        name = "Toggle Run Colors in Partymanager",
        description = "Toggles the colors of the runs in the party manager.",
        category = "Dungeons",
        subcategory = "Party Manager"
    )
    var toggleRunColors = true

    @Number(
        name = "Customize Max Runs for Red in Run Colors",
        description = "Customize maximum runs required for the color red.",
        category = "Dungeons",
        subcategory = "Party Manager",
        min = 0f,
        max = Int.MAX_VALUE.toFloat()
    )
    var runColorsRedMax = 1

    @Number(
        name = "Customize Max Runs for Yellow in Run Colors",
        description = "Customize maximum runs required for the color yellow.",
        category = "Dungeons",
        subcategory = "Party Manager",
        min = 0f,
        max = Int.MAX_VALUE.toFloat()
    )
    var runColorsYellowMax = 9

    @KeyBind(
        name = "Hotkey",
        description = "The keybind to open the party manager.",
        subcategory = "Party Manager",
        category = "Dungeons"
    )
    var partyManagerKeybind = OneKeyBind(Keyboard.KEY_M)

    // Watcher Ready Warning
    @Switch(
        name = "Watcher Ready Warning",
        description = "Sends a warning when the watcher is done spawning mobs.",
        category = "Dungeons",
        subcategory = "Watcher Ready"
    )
    var watcherReadyBanner = false

    @Switch(
        name = "Watcher Ready Sound",
        description = "Plays a sound when the watcher is done spawning mobs.",
        category = "Dungeons",
        subcategory = "Watcher Ready"
    )
    var watcherReadySound = false

    @Slider(
        name = "Watcher Ready Banner Time",
        description = "The amount of seconds the watcher ready banner appears for.",
        category = "Dungeons",
        subcategory = "Watcher Ready",
        min = 1f,
        max = 7f
    )
    var watcherReadyBannerTime = 3.5f

    @Color(
        name = "Watcher Ready Banner Color",
        description = "The color of the watcher ready text.",
        category = "Dungeons",
        subcategory = "Watcher Ready"
    )
    var watcherReadyBannerColor = OneColor(255, 45, 6)

    @Switch(
        name = "Watcher Ready Chat Message",
        description = "Send a message to your party when watcher is done spawning mobs.",
        category = "Dungeons",
        subcategory = "Watcher Ready"
    )
    var watcherReadyChatMessage = false

    @Text(
        name = "Watcher Ready Text",
        description = "Message to send when the watcher is ready to clear.",
        category = "Dungeons",
        subcategory = "Watcher Ready",
        size = 2
    )
    var watcherChatMessage = "Partly Sane Skies > The watcher is done spawning mobs. Ready to clear."

    @Switch(
        name = "Air Raid Siren",
        description = "Plays a WWII air raid siren when the watcher is done spawning mobs. \nPros: \nKeeps you up at late night grinds \n(RECOMMENDED, ESPECIALLY AT 3 AM)",
        category = "Dungeons",
        subcategory = "Watcher Ready"
    )
    var watcherReadyAirRaidSiren = false

    // Pretify Skytils Mimic Killed
    @Switch(
        name = "Pretty Mimic Killed",
        description = "Changes the skytils mimic killed message to be more visually appealing",
        category = "Dungeons",
        subcategory = "Pretty Mimic Killed Message"
    )
    var prettyMimicKilled = true

    // Pretify Skytils Mimic Killed
    @Text(
        name = "Pretty Mimic Killed Message",
        description = "Changes the skytils mimic killed message to be more visually appealing",
        category = "Dungeons",
        subcategory = "Pretty Mimic Killed Message"
    )
    var prettyMimicKilledString = "Mimic Killed!"

    //    Dungeon Waypoints
    @Switch(
        name = "Enable Waypoints for Terminals, Devices and Levers",
        description = "Enables waypoints for terminals, devices and levers in Phase 3 of Floor 7.",
        category = "Dungeons",
        subcategory = "Terminal Waypoints"
    )
    var terminalWaypoints = true

    // Pearl Refill
    @Switch(
        name = "Auto Pearl Refill",
        description = "Automatically refills your pearls when a run starts.",
        category = "Dungeons",
        subcategory = "Pearl Refill"
    )
    var autoPearlRefill = false

    @KeyBind(
        name = "Refill Pearls Hotkey",
        description = "The keybind to automatically refill your pearls.",
        category = "Dungeons",
        subcategory = "Pearl Refill"
    )
    var pearlRefillKeybind = OneKeyBind(Keyboard.KEY_P)
    
    // Item Refill
    @Switch(
        name = "Auto Item Refill",
        description = "Automatically refills your utility items when a run starts.",
        category = "Dungeons",
        subcategory = "Item Refill"
    )
    var autoItemRefill = false

    @KeyBind(
        name = "Refill Items Hotkey",
        description = "The keybind to automatically refill your pearls.",
        category = "Dungeons",
        subcategory = "Item Refill"
    )
    var itemRefillKeybind = OneKeyBind(Keyboard.KEY_P)

    @Switch(
        name = "Refill Ender Pearls",
        description = "Refills ender pearls",
        category = "Dungeons",
        subcategory = "Item Refill"
    )
    var refillPearls = true

    @Switch(
        name = "Refill Superboom Tnt",
        description = "Refills superboom tnt",
        category = "Dungeons",
        subcategory = "Item Refill"
    )
    var refillSuperboomTnt = true

    @Switch(
        name = "Refill Spirit Leaps",
        description = "Refills spirit leaps",
        category = "Dungeons",
        subcategory = "Item Refill"
    )
    var refillSpiritLeaps = true

    @Switch(
        name = "Refill Decoys",
        description = "Refills decoy",
        category = "Dungeons",
        subcategory = "Item Refill"
    )
    var refillDecoys = false

    // Health Alert
    @Switch(
        name = "Alert when dungeon team members are low",
        description = "Displays a banner when a teammate in Dungeons has low health.",
        category = "Dungeons",
        subcategory = "Health Alert"
    )
    var healerAlert = false

    @Switch(
        name = "Alert when you are low",
        description = "Displays a banner when you are low on health.",
        category = "Dungeons",
        subcategory = "Health Alert"
    )
    var alertWhenPlayerLow = false

    @Switch(
        name = "Alert outside of dungeons",
        description = "Alert outside of dungeons.",
        category = "Dungeons",
        subcategory = "Health Alert"
    )
    var alertOutsideDungeons  = false

    @Dropdown(
        name = "Alert when below...",
        description = "Choose at what percentage healer alert will trigger.",
        category = "Dungeons",
        subcategory = "Health Alert",
        options = ["25% Health", "50% Health"]
    )
    var colouredHealerAlert = 0

    @Slider(
        name = "Cooldown Between Warnings",
        description = "Choose the delay between Low Health Alerts.",
        category = "Dungeons",
        subcategory = "Health Alert",
        min = 1f,
        max = 15f
    )
    var healerAlertCooldownSlider = 3.5f


    @Color(
        name = "Party Members Low Color",
        category = "Dungeons",
        subcategory = "Health Alert",
    )
    var partyMemberLowColor = OneColor(java.awt.Color.RED)

    @Color(
        name = "Player Low Color",
        category = "Dungeons",
        subcategory = "Health Alert",
    )
    var playerLowColor = OneColor(java.awt.Color.RED)

    // Required Secrets Found
    @Switch(
        name = "Required Secrets Found Banner",
        description = "Sends a warning when all required secrets have been found.",
        category = "Dungeons",
        subcategory = "Required Secrets Found"
    )
    var secretsBanner = false

    @Switch(
        name = "Required Secrets Found Sound",
        description = "Plays a sound when all required secrets have been found.",
        category = "Dungeons",
        subcategory = "Required Secrets Found"
    )
    var secretsSound = false

    @Slider(
        name = "Required Secrets Found Banner Time",
        description = "The amount of seconds the required secrets found banner appears for.",
        category = "Dungeons",
        subcategory = "Required Secrets Found",
        min = 1f,
        max = 7f
    )
    var secretsBannerTime = 3.5f

    @Color(
        name = "Required Secrets Found Banner Color",
        description = "The color of the required secrets found text.",
        category = "Dungeons",
        subcategory = "Required Secrets Found"
    )
    var secretsBannerColor = OneColor(255, 45, 6)

    @Switch(
        name = "Required Secrets Found Chat Message",
        description = "Send a message to your party when all required secrets have been found.",
        category = "Dungeons",
        subcategory = "Required Secrets Found"
    )
    var secretsChatMessage = false

    @Text(
        name = "Required Secrets Found Text",
        description = "Message to send when all required secrets have been found.",
        category = "Dungeons",
        subcategory = "Required Secrets Found",
        size = 2
    )
    var secretsChatMessageString = "Partly Sane Skies > All required secrets have been found!"

    @Switch(
        name = "Air Raid Siren",
        description = "Plays a WWII air raid siren when all required secrets have been found. \nPros: \nKeeps you up at late night grinds \n(RECOMMENDED, ESPECIALLY AT 3 AM)",
        category = "Dungeons",
        subcategory = "Required Secrets Found"
    )
    var secretsAirRaidSiren = false

    // Dungeon Player Breakdown
    @Switch(
        name = "Dungeon Player Breakdown",
        description = "At the end of the dungeon, send a message informing you how much of the dungeon each player has completed.",
        category = "Dungeons",
        subcategory = "Dungeon Player Breakdown"
    )
    var dungeonPlayerBreakdown = false

    @Dropdown(
        name = "Message Content",
        description = "Shows more information about how many blessings and secrets each player collected.",
        category = "Dungeons",
        subcategory = "Dungeon Player Breakdown",
        options = ["Condensed", "Standard", "Enhanced"]
    )
    var enhancedDungeonPlayerBreakdown = 1

    @Slider(
        name = "Delay",
        description = "Delay after a dungeon ends.",
        category = "Dungeons",
        subcategory = "Dungeon Player Breakdown",
        min = 0f,
        max = 20f
    )
    var dungeonPlayerBreakdownDelay = 1f

    @Switch(
        name = "Send in Party Chat",
        description = "Send a condensed version to the rest of your party.",
        category = "Dungeons",
        subcategory = "Dungeon Player Breakdown"
    )
    var partyChatDungeonPlayerBreakdown = false

    @Switch(
        name = "Dungeon Snitcher",
        description = "Automatically sends messages recommending to kick party members.",
        category = "Dungeons",
        subcategory = "Dungeon Player Breakdown"
    )
    var dungeonSnitcher = false

    @Slider(
        name = "Dungeon Snitcher Percent",
        description = "Percentage of a dungeon complete where the player is slacking.",
        category = "Dungeons",
        subcategory = "Dungeon Player Breakdown",
        min = 0f,
        max = 50f
    )
    var dungeonSnitcherPercent = 7.5f

    //    Auto GG
    @Switch(
        name = "Enable Automatic GG message",
        description = "Sends an automatic gg message of your choosing whenever a dungeon is complete.",
        category = "Dungeons",
        subcategory = "Auto GG"
    )
    var autoGgEnabled = false

    @Slider(
        name = "Cooldown after dungeon ends",
        description = "The amount of seconds after a dungeon ends before the auto gg message is sent.",
        category = "Dungeons",
        subcategory = "Auto GG",
        min = 0f,
        max = 10f
    )
    var autoGGCooldown = 1.5f

    @Dropdown(
        name = "Send in",
        description = "Where to send the auto gg message.",
        category = "Dungeons",
        subcategory = "Auto GG",
        options = ["Party Chat", "All Chat"]
    )
    var sendAutoGGInWhatChat = 0

    @Text(
        name = "Text when S+ score",
        description = "Sends this message whenever a dungeon is complete and the score is S+.",
        category = "Dungeons",
        subcategory = "Auto GG"
    )
    var autoGGMessageSPlus = "GG Easy"

    @Text(
        name = "Text when S score",
        description = "Sends this message whenever a dungeon is complete and the score is S.",
        category = "Dungeons",
        subcategory = "Auto GG"
    )
    var autoGGMessageS = "GG"

    @Text(
        name = "Text when other score",
        description = "Sends this message whenever a dungeon is complete and the score is not S+/S.",
        category = "Dungeons",
        subcategory = "Auto GG"
    )
    var autoGGMessageOther = "Welp, GG"

    // -------------- Category: Mining --------------
    // Worm Warning
    @Switch(
        name = "Worm Warning Banner",
        description = "A banner appears on your screen when a worm spawns.",
        category = "Mining",
        subcategory = "Worm Warning"
    )
    var wormWarningBanner = false

    @Color(
        name = "Worm Warning Banner Color",
        description = "The color of the worm warning text.",
        category = "Mining",
        subcategory = "Worm Warning"
    )
    var wormWarningBannerColor = OneColor(34, 255, 0)

    @Slider(
        name = "Worm Warning Banner Time",
        description = "The amount of seconds the worm warning banner appears for.",
        category = "Mining",
        subcategory = "Worm Warning",
        min = 1f,
        max = 7f
    )
    var wormWarningBannerTime = 3.5f

    @Switch(
        name = "Worm Warning Sound",
        description = "Plays a sound when a worm spawns.",
        category = "Mining",
        subcategory = "Worm Warning"
    )
    var wormWarningBannerSound = false

    //Pickaxes
    @Switch(
        name = "Pickaxe Ability Ready Banner",
        description = "A banner appears on your screen when your pickaxe ability is ready.",
        category = "Mining",
        subcategory = "Pickaxes"
    )
    var pickaxeAbilityReadyBanner = true

    @Text(
        name = "Banner Text",
        description = "The text that appears on the banner when your pickaxe ability is ready.",
        category = "Mining",
        subcategory = "Pickaxes"
    )
    var pickaxeAbilityReadyBannerText = "Pickaxe Ability Ready!"

    @Switch(
        name = "Pickaxe Ability Ready Sound",
        description = "Plays a sound when your pickaxe ability is ready.",
        category = "Mining",
        subcategory = "Pickaxes"
    )
    var pickaxeAbilityReadySound = false

    @Switch(
        name = "Use Air Raid Siren for Pickaxe Ability Ready",
        description = "Plays a WWII air raid siren when your pickaxe ability is ready. \nPros: \nKeeps you up at late night grinds \n(RECOMMENDED, ESPECIALLY AT 3 AM)",
        category = "Mining",
        subcategory = "Pickaxes"
    )
    var pickaxeAbilityReadySiren = false

    @Switch(
        name = "Hide Ready Message from Chat",
        description = "Hides the message that appears in chat when your pickaxe ability is ready.",
        category = "Mining",
        subcategory = "Pickaxes"
    )
    var hideReadyMessageFromChat = false

    @Switch(
        name = "Warn only on mining islands.",
        description = "Makes it less annoying when you don't want to mine.",
        category = "Mining",
        subcategory = "Pickaxes"
    )
    var onlyGiveWarningOnMiningIsland = true

    @Slider(
        name = "Ready Banner Time",
        description = "The amount of seconds the ready banner appears for.",
        category = "Mining",
        subcategory = "Pickaxes",
        min = 1f,
        max = 7f
    )
    var pickaxeBannerTime = 3.5f

    @Color(
        name = "Ready Banner Color",
        description = "The color of the ready banner text.",
        category = "Mining",
        subcategory = "Pickaxes"
    )
    var pickaxeBannerColor = OneColor(255, 255, 0)

    @Switch(
        name = "Block Ability on Private Island (UAYOR)",
        description = "Blocks the use of pickaxe abilities on your private island. (Use at your own risk)",
        category = "Mining",
        subcategory = "Pickaxes"
    )
    var blockAbilityOnPrivateIsland = false

    // Gemstone Waypoints
    @Switch(
        name = "Render Gemstone Waypoints",
        description = "Shows Topaz waypoints",
        category = "Mining",
        subcategory = "Gemstone Waypoints",
        size = 2
    )
    var renderGemstoneWaypoints = false

    @Slider(
        name = "Gemstone Waypoint Render Distance (Chunks)",
        description = "Radius of chunks to render waypoints in. (Only works in integer denominations)",
        category = "Mining",
        subcategory = "Gemstone Waypoints",
        min = 1f,
        max = 16f
    )
    var gemstoneWaypointRenderDistance = 6

    @Slider(
        name = "Minimum Gemstone Size",
        description = "Hides gemstones that are smaller than this size.",
        category = "Mining",
        subcategory = "Gemstone Waypoints",
        min = 0f,
        max = 100f
    )
    var gemstoneMinSize = 15

    @Switch(
        name = "Show waypoint beam",
        description = "Show a beam going from the waypoint to the top of the world. Disable with large amounts of waypoints",
        category = "Mining",
        subcategory = "Gemstone Waypoints",
        size = 2
    )
    var showGemstoneBeam = false

    @Switch(
        name = "Show Topaz Waypoints",
        description = "Shows Topaz waypoints",
        category = "Mining",
        subcategory = "Gemstone Waypoints"
    )
    var topazWaypoints = true

    @Switch(
        name = "Show Ruby Waypoints",
        description = "Shows Ruby waypoints",
        category = "Mining",
        subcategory = "Gemstone Waypoints"
    )
    var rubyWaypoints = true

    @Switch(
        name = "Show Sapphire Waypoints",
        description = "Shows Sapphire waypoints",
        category = "Mining",
        subcategory = "Gemstone Waypoints"
    )
    var sapphireWaypoints = true

    @Switch(
        name = "Show Amethyst Waypoints",
        description = "Shows Amethyst waypoints",
        category = "Mining",
        subcategory = "Gemstone Waypoints"
    )
    var amethystWaypoints = true

    @Switch(
        name = "Show Amber Waypoints",
        description = "Shows Amber waypoints",
        category = "Mining",
        subcategory = "Gemstone Waypoints"
    )
    var amberWaypoints = true

    @Switch(
    name = "Show Jade Waypoints",
    description = "Shows Jade waypoints",
    category = "Mining",
    subcategory = "Gemstone Waypoints"
    )
    var jadeWaypoints = true



    //Events
    @Info(
        type = InfoType.INFO,
        text = "Some Events may not trigger, not all have been tested. If you find an event that doesn't trigger, please report it on our discord server.",
        category = "Mining",
        subcategory = "Events",
        size = 2
    )
    @Switch(name = "Main Toggle", description = "Toggles the events.", category = "Mining", subcategory = "Events")
    var miningEventsToggle = true

    @Switch(
        name = "Show Event Banner",
        description = "Shows a banner when an enabled event is going active.",
        category = "Mining",
        subcategory = "Events"
    )
    var miningShowEventBanner = true

    @Switch(
        name = "Send System Notifications",
        description = "Sends a system notification when an event is going active.",
        category = "Mining",
        subcategory = "Events"
    )
    var miningSendSystemNotifications = false

    @Switch(
        name = "Also warn 20s before event activation",
        description = "Shows a banner and plays sound 20s before an enabled event is going active.",
        category = "Mining",
        subcategory = "Events"
    )
    var miningWarn20sBeforeEvent = false

    @Switch(
        name = "2x Powder activation sound",
        description = "Plays a sound when 2x Powder event is going active.",
        category = "Mining",
        subcategory = "Events"
    )
    var mining2xPowderSound = false

    @Switch(
        name = "Gone with the wind activation sound",
        description = "Plays a sound when Gone with the wind event is going active.",
        category = "Mining",
        subcategory = "Events"
    )
    var miningGoneWithTheWindSound = false

    @Switch(
        name = "Better Together activation sound",
        description = "Plays a sound when Better Together event is going active.",
        category = "Mining",
        subcategory = "Events"
    )
    var miningBetterTogetherSound = false

    @Switch(
        name = "Goblin Raid activation sound",
        description = "Plays a sound when Goblin Raid event is going active.",
        category = "Mining",
        subcategory = "Events"
    )
    var miningGoblinRaidSound = false

    @Switch(
        name = "Raffle activation sound",
        description = "Plays a sound when Raffle event is going active.",
        category = "Mining",
        subcategory = "Events"
    )
    var miningRaffleSound = false

    @Switch(
        name = "Mithril Gourmand activation sound",
        description = "Plays a sound when Mithril Gourmand event is going active.",
        category = "Mining",
        subcategory = "Events"
    )
    var miningMithrilGourmandSound = false

    @Switch(
        name = "Powder Ghast activation sound",
        description = "Plays a sound when Powder Ghast is about to spawn.",
        category = "Mining",
        subcategory = "Events"
    )
    var miningPowderGhastSound = false

    @Switch(
        name = "Fallen Star activation sound",
        description = "Plays a sound when Fallen Star is about to spawn.",
        category = "Mining",
        subcategory = "Events"
    )
    var miningFallenStarSound = false

    @Slider(
        name = "Event Banner Time",
        description = "The amount of seconds the event banner appears for.",
        category = "Mining",
        subcategory = "Events",
        min = 1f,
        max = 7f
    )
    var miningEventBannerTime = 3.5f

    @Color(
        name = "Event Banner Color",
        description = "The color of the event banner text.",
        category = "Mining",
        subcategory = "Events"
    )
    var miningEventBannerColor = OneColor(255, 255, 255)

    // ------------- Category: Farming ---------------------------------
    //    Hoes
    @Switch(
        name = "Stop right clicks on Mathematical Hoes",
        description = "Cancels the right click on mathematical hoes to prevent it from opening the recipes list. (Use at your own risk)",
        category = "Farming",
        subcategory = "Hoes"
    )
    var blockHoeRightClicks = false

    @Slider(
        name = "Allow Time",
        description = "The amount of minutes the hoe will be allowed to be used for, using /allowhoerightclick.",
        category = "Farming",
        subcategory = "Hoes",
        min = 1f,
        max = 15f
    )
    var allowRightClickTime = 3f

    @KeyBind(
        name = "Allow Hoe Right Clicks Opener Hotkey",
        description = "The keybind to open the allow hoe right click menu.",
        category = "Farming",
        subcategory = "Hoes"
    )
    var allowHoeRightClickKeybind = OneKeyBind(Keyboard.KEY_NONE)

    //    Farm notifier
    @Switch(
        name = "Show end of farm regions",
        description = "Highlights your regions.",
        category = "Farming",
        subcategory = "End of Farm Notifier"
    )
    var showFarmRegions = true

    @Slider(
        name = "Time between chimes",
        description = "The amount of seconds between the chime sounds.",
        category = "Farming",
        subcategory = "End of Farm Notifier",
        min = 1f,
        max = 5f
    )
    var farmnotifierChimeTime = 3f

    @Slider(
        name = "Highlight Time",
        description = "The amount of seconds that a highlighted region will stay highlighted for.",
        category = "Farming",
        subcategory = "End of Farm Notifier",
        min = 1f,
        max = 120f
    )
    var farmHightlightTime = 30f

    // Garden Visitors
    @Switch(
        name = "Visitor Trade Cost",
        description = "Gives you information about the cost of visitor trades.",
        category = "Farming",
        subcategory = "Garden Visitors"
    )
    var gardenShopTradeInfo = false

    @Switch(
        name = "Display Garden Visitor Stats",
        description = "Shows visited/accepted stats per NPC rarity.\nPros: based on item tooltips, which might capture more Garden visitor data\n(especially if you had Garden visitors before you installed SkyHanni).\nCons: Only shows for current Visitor's Logbook page and not all pages.",
        category = "Farming",
        subcategory = "Garden Visitors"
    )
    var visitorLogbookStats = false

    //    Wrong Tool for Crop
    @Switch(
        name = "Wrong Tool for Crop Enabled",
        description = "When enabled, a warning will appear, notifying you that you are using the wrong tool for the current crop.",
        category = "Farming",
        subcategory = "Wrong Tool for Crop"
    )
    var wrongToolForCropEnabled = false

    @Switch(
        name = "Allow Mathematical Hoes",
        description = "When enabled, mathematical hoes will be considered a valid tool.",
        category = "Farming",
        subcategory = "Wrong Tool for Crop"
    )
    var mathematicalHoeValid = true

    @Switch(
        name = "Allow Other SkyBlock Tools",
        description = "When enabled, other SkyBlock tools will be considered a valid tool.",
        category = "Farming",
        subcategory = "Wrong Tool for Crop"
    )
    var otherSkyblockToolsValid = true

    @Switch(
        name = "Allow Right Tool Type",
        description = "When enabled, the system only looks for the right tool type (hoes, etc.).",
        category = "Farming",
        subcategory = "Wrong Tool for Crop"
    )
    var vanillaToolsValid = false

    @Switch(
        name = "Require Replenish",
        description = "When enabled, the tool must have replenish if it is to be used on a replenishable crop.",
        category = "Farming",
        subcategory = "Wrong Tool for Crop"
    )
    var requireReplenish = true

    @Switch(
        name = "Air Raid Siren",
        description = "When enabled, the warning will contain an air raid siren.",
        category = "Farming",
        subcategory = "Wrong Tool for Crop"
    )
    var wrongToolForCropAirRaid = false

    @Slider(
        name = "Banner Time",
        description = "The amount of seconds the banner appears for.",
        category = "Farming",
        subcategory = "Wrong Tool for Crop",
        min = 1f,
        max = 7f
    )
    var wrongToolForCropBannerTime = 3.5f

    @Slider(
        name = "Time Between Warnings",
        description = "The amount of seconds between each warning.",
        category = "Farming",
        subcategory = "Wrong Tool for Crop",
        min = 1f,
        max = 7f
    )
    var wrongToolForCropCooldown = 3.5f

    // Composter
    @Switch(
        name = "Best Crops to Compost",
        description = "Gives you information about which crops are the best to compost.",
        category = "Farming",
        subcategory = "Composter"
    )
    var bestCropsToCompost = false

    // Skymart
    @Switch(
        name = "SkyMart Value",
        description = "Gives you information about the best value crops to compost.",
        category = "Farming",
        subcategory = "SkyMart"
    )
    var skymartValue = false

    // ------------- Category: Foraging ---------------------------------
    //    Treecapitator Cooldown Indicator
    @Switch(
        name = "Treecapitator Cooldown Indicator Enabled",
        description = "Displays a cooldown indicator below your crosshair whenever your treecapitator is on cooldown.",
        category = "Foraging",
        subcategory = "Treecapitator Cooldown Indicator"
    )
    var treecapCooldown = false

    @Switch(
        name = "Use Monkey Pet",
        description = "Use the monkey pet to dynamically adjust the length of the cooldown.",
        category = "Foraging",
        subcategory = "Treecapitator Cooldown Indicator"
    )
    var treecapCooldownMonkeyPet = true

    // ------------- Category: Economy ---------------------------------
    // Community Center
    @Switch(
        name = "Best Item for Bits",
        description = "Gives you information about which item in the Bits Shop is the best to sell.",
        category = "Economy",
        subcategory = "Community Center"
    )
    var bestBitShopItem = false

    @Switch(
        name = "Only Show Affordable Items",
        description = "When making recommendations for what you can buy, only recommend the items that you are able to afford.",
        category = "Economy",
        subcategory = "Community Center"
    )
    var bitShopOnlyShowAffordable = true

    @Slider(
        name = "BIN Snipe Percentage",
        description = "The percent of the price that the BIN sniper considers a \"snipe\". Example: 85%, Lowest BIN: 1 000 000, will look for a price of 850000 or less.",
        category = "Economy",
        subcategory = "BIN Sniper",
        min = 0f,
        max = 100f
    )
    var BINSniperPercent = 87f

    // Auction House
    @Switch(
        name = "Custom Auction House GUI",
        description = "Toggle using the custom Auction House GUI and BIN Sniper Helper.",
        category = "Economy",
        subcategory = "Auction House"
    )
    var customAhGui = true

    @Dropdown(
        name = "Custom Auction House GUI Icons",
        description = "Use either the Partly Sane Studios developed textures, or the FurfSky Reborn developed textures\n\nAll of the textures under FurfSky Reborn are fully developed by the FurfSky Reborn team.\nhttps://furfsky.net/",
        category = "Economy",
        subcategory = "Auction House",
        options = ["Partly Sane Studios", "FurfSky Reborn"]
    )
    var customAhGuiTextures = 1

    @Slider(
        name = "Master Scale",
        description = "The scale of the entire Auction House GUI.",
        category = "Economy",
        subcategory = "Auction House",
        min = .1f,
        max = 1f
    )
    var masterAuctionHouseScale = .333333f

    @Slider(
        name = "Item Padding",
        category = "Economy",
        subcategory = "Auction House",
        min = 0f,
        max = .2f
    )
    var auctionHouseItemPadding = .075f

    @Slider(
        name = "Side Bar Height",
        category = "Economy",
        subcategory = "Auction House",
        min = .25f,
        max = 2f
    )
    var auctionHouseSideBarHeight = 1.333f

    @Slider(
        name = "Side Bar Width",
        category = "Economy",
        subcategory = "Auction House",
        min = .25f,
        max = 2f
    )
    var auctionHouseSideBarWidth = .667f

    @Slider(
        name = "Side Bar Padding",
        category = "Economy",
        subcategory = "Auction House",
        min = -.5f,
        max = .5f
    )
    var auctionSideBarPadding = .05f

    @Slider(
        name = "Auction House Text Scale",
        category = "Economy",
        subcategory = "Auction House",
        min = .11f,
        max = 2f
    )
    var auctionHouseTextScale = .75f

    // Excessive Coin warning
    @Switch(
        name = "Excessive Coin and No Booster Cookie",
        description = "Warns you if you have a lot of coins in your purse and no booster cookie.",
        category = "Economy",
        subcategory = "Excessive Coin Warning"
    )
    var noCookieWarning = false

    @Number(
        name = "Maximum Allowed Amount Without Booster Cookie",
        description = "The maximum allowed amount of money allowed before it warns you about having no booster cookie.",
        category = "Economy",
        subcategory = "Excessive Coin Warning",
        min = 0f,
        max = Int.MAX_VALUE.toFloat()
    )
    var maxWithoutCookie = 750000

    @Slider(
        name = "Excessive Coin Warning Time",
        description = "The amount of seconds the warning appears for appears for.",
        category = "Economy",
        subcategory = "Excessive Coin Warning",
        min = 1f,
        max = 7f
    )
    var noCookieWarnTime = 3.5f

    @Slider(
        name = "Excessive Coin Warn Cooldown",
        description = "The amount of seconds between each warning.",
        category = "Economy",
        subcategory = "Excessive Coin Warning",
        min = 1f,
        max = 300f
    )
    var noCookieWarnCooldown = 20f

    //    ------------------ Category: Chat ---------------------
    //    WordEditor
    @Switch(
        name = "Word Editor Main Toggle",
        description = "Allows you to edit words in chat. Can be configured with /wordeditor.",
        category = "Chat",
        subcategory = "Word Editor"
    )
    var wordEditor = true

    //  Chat Alerts
    @Switch(
        name = "Send System Notification",
        description = "Sends a system notification when a message triggered by the Chat Alert was send.",
        category = "Chat",
        subcategory = "Chat Alerts"
    )
    var chatAlertSendSystemNotification = false

    //Chat Colors
    @Switch(
        name = "Color Private Messages",
        description = "Private messages pink to make them more visible in busy lobbies.",
        category = "Chat",
        subcategory = "Chat Color"
    )
    var colorPrivateMessages = false

    @Switch(
        name = "Color Nons Messages",
        description = "Color messages from the non-ranked players to white to make them more visible in busy lobbies.",
        category = "Chat",
        subcategory = "Chat Color"
    )
    var colorNonMessages = false

    @Switch(
        name = "Color Party Chat",
        description = "Color messages from the party chat blue to make them more visible in busy lobbies.",
        category = "Chat",
        subcategory = "Chat Color"
    )
    var colorPartyChat = false

    @Switch(
        name = "Color Guild Chat",
        description = "Color messages from the guild chat green to make them more visible in busy lobbies.",
        category = "Chat",
        subcategory = "Chat Color"
    )
    var colorGuildChat = false

    @Switch(
        name = "Color Guild Officer Chat",
        description = "Color messages from the guild officer chat aqua to make them more visible in busy lobbies.",
        category = "Chat",
        subcategory = "Chat Color"
    )
    var colorOfficerChat = false

    @Switch(
        name = "SkyBlock Co-op Chat",
        description = "Color messages from the SkyBlock coop chat aqua to make them more visible in busy lobbies.",
        category = "Chat",
        subcategory = "Chat Color"
    )
    var colorCoopChat = false

    @Switch(
        name = "Visible Colors",
        description = "Converts the custom colors mentioned above to more visible colors. Dark Green -> Light Green and Blue -> Gold. (Recommended)",
        category = "Chat",
        subcategory = "Chat Color"
    )
    var visibleColors = false

    //Fun
    @Switch(
        name = "OwO Language toggle",
        description = "Replaces all chat messages with OwO language.\nThis feature basically breaks the whole chat, so please be warned.",
        category = "Chat",
        subcategory = "Fun"
    )
    var owoLanguage = false
    @Switch(
        name = "OwO Chat Transformer",
        description = "Transforms every chat message you send into OwO language.",
        category = "Chat",
        subcategory = "Fun"
    )
    var transformOWO = false

    //    ------------- DEV ------------
    @KeyBind(
        name = "Debug Hotkey",
        description = "The keybind to toggle the debug mode.",
        category = "Dev"
    )
    var debugKeybind = OneKeyBind(Keyboard.KEY_NONE)

    @Switch(
        name = "Debug Mode",
        description = "Toggles the debug mode.",
        category = "Dev"
    )
    var debugMode = false

    @Switch(
        name = "Render TEST Banner",
        description = "Renders a test banner on your screen.",
        category = "Dev"
    )
    var debugRenderTestBanner = false

    @Switch(
        name = "Chat Analyser",
        description = "Analyse chat messages and print them to the console.",
        category = "Dev"
    )
    var debugChatAnalyser = false

    @Switch(
        name = "Add a slacker to the party",
        description = "Adds a slacker to the party.",
        category = "Dev"
    )
    var debugAddSlacker = false

    @Switch(
        name = "Spawn Waypoint",
        description = "Spawns a waypoint at your current location.",
        category = "Dev"
    )
    var debugSpawnWaypoint = false

    @Switch(
        name = "Send a system notification",
        description = "Sends a system notification.",
        category = "Dev"
    )
    var debugSendSystemNotification = false

    @Switch(
        name = "Print pet world parsing information",
        description = "Prints information about the pet world parsing.",
        category = "Dev"
    )
    var debugPrintPetWorldParsingInformation = false

    @Switch(
        name = "Print current location from Island Type",
        description = "Prints your current location from the island type.",
        category = "Dev"
    )
    var debugPrintCurrentLocationFromIslandType = false

    @Switch(
        name = "Log cached F7 puzzles",
        description = "Logs cached F7 puzzles.",
        category = "Dev"
    )
    var debugLogCachedF7Puzzles = false

    @Switch(
        name = "Print Current Cached Stats",
        description = "Prints the current cached stats.",
        category = "Dev"
    )
    var debugPrintCurrentCachedStats = false

    @Switch(
        name = "Render RNG Drop Banner",
        description = "Renders an RNG Drop Banner.",
        category = "Dev"
    )
    var debugRenderRNGBanner = false

    @Switch(
        name = "Generate Cylinder",
        description = "Generates a cylinder.",
        category = "Dev",
    )
    var debugCylinder = false

    @Switch(
        name = "Scan crystal hollows crystals",
        description = "Don't use this",
        category = "Dev",
    )
    var debugScanCrystalHollowsCrystals = false

    @Switch(
        name = "Convert crystal hollows crystals scan to pretty data",
        description = "Don't use this pt. 2",
        category = "Dev",
    )
    var debugConvertScanToPrettyData = false

    @Switch(
        name = "Remove Crystal Nucleus Coords from Crystal hollows pretty data",
        description = "Don't use this pt. 3",
        category = "Dev"
    )
    var debugConvertPrettyDataToNoNucleus = false

    // Example HUD
    @HUD(
        name = "Test Hud Element",
        category = "Dev",
        subcategory = "Example HUD"
    )
    var hud = ExampleHud.oneConfigHud

    // Percy Mode
    @Switch(
        name = "Percy Mode",
        description = "Toggles Percy Mode.",
        category = "Dev",
        subcategory = "Percy Mode"
    )
    var percyMode = false

    @Switch(
        name = "Current Screen",
        description = "Dumps the current screen.",
        category = "Dev",
        subcategory = "Percy Mode"
    )
    var debugCurrentScreenDump = false

    @Switch(
        name = "Entity Dump",
        description = "Dumps all entities.",
        category = "Dev",
        subcategory = "Percy Mode"
    )
    var debugEntityDump = false

    @Switch(
        name = "Inventory Dump",
        description = "Dumps your inventory.",
        category = "Dev",
        subcategory = "Percy Mode"
    )
    var debugInventoryDump = false

    @Switch(
        name = "Player Dump",
        description = "Dumps all players.",
        category = "Dev",
        subcategory = "Percy Mode"
    )
    var debugPlayerDump = false

    // API
    @Number(
        name = "Time between requests",
        description = "The time between API calls. Only change if you know what you're doing. Changing this will reduce the amount of time API requests take, however may result in more errors.",
        category = "Dev",
        subcategory = "API",
        min = .1f,
        max = 30f
    )
    var timeBetweenRequests = 0.5f

    @Slider(
        name = "Player Data Cache Time",
        description = "Saves the data from other party members to save time upon loading data about players. The bigger the value the more minutes you will save but the less accurate your data will be.",
        category = "Dev",
        subcategory = "API",
        min = 0f,
        max = 90f
    )
    var playerDataCacheTime = 5

    @Switch(
        name = "Print errors in chat",
        description = "Send errors on getting data in chat (Recommended, however if you get spammed or have a bad internet connection, turn it off).",
        category = "Dev",
        subcategory = "API"
    )
    var printApiErrors = true

    @Text(
        name = "Public Data Repo Owner",
        description = "Change the owner of the repo used for public data.",
        category = "Dev",
        subcategory = "API Source",
        secure = true
    )
    var repoOwner = "PartlySaneStudios"

    @Text(
        name = "Public Data Repo Name",
        description = "Change the name of the repo used for public data.",
        category = "Dev",
        subcategory = "API Source",
        secure = true
    )
    var repoName = "partly-sane-skies-public-data"

    @Text(
        name = "API URL",
        category = "Dev",
        subcategory = "API Source",
        secure = true
    )
    var apiUrl = "http://partlysanecloud.su386.dev"

    @Switch(
        name = "Load API Data Directly from GitHub",
        category = "Dev",
        subcategory = "API Source"
    )
    var useGithubForPublicData = false

    init {
        initialize()
    }
}
