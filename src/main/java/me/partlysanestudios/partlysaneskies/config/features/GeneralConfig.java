package me.partlysanestudios.partlysaneskies.config.features;

import cc.polyfrost.oneconfig.config.annotations.*;
import cc.polyfrost.oneconfig.config.annotations.Number;
import cc.polyfrost.oneconfig.config.core.OneKeyBind;
import cc.polyfrost.oneconfig.config.data.InfoType;
import org.lwjgl.input.Keyboard;

public class GeneralConfig {
    @Info(
            type = InfoType.INFO,
            text = "Hover over an option to see a description and more information."
    )
    public boolean ignored;

    @Dropdown(
            name = "Update Channel",
            subcategory = "Updates",
            category = "General",
            options = {
                    "Release",
                    "Pre-release",
            }
    )
    public int releaseChannel = Integer.parseInt("@RELEASE_CHANNEL@");

    //    Discord

    @Switch(
            category = "General",
            subcategory = "Discord",
            name = "Discord RPC"
    )
    public boolean discordRPC = true;

    @Dropdown(
            category = "General",
            subcategory = "Discord",
            name = "Playing...",
            options = { "Hypixel Skyblock", "sbe bad" }
    )
    public int discordPlayingMode = 0;

    @Text(
            category = "General",
            subcategory = "Discord",
            name = "Discord Game Name"
    )
    public String discordRPCName = "sbe bad";

    @Text(
            category = "General",
            subcategory = "Discord",
            name = "Discord Game Description"
    )
    public String discordRPCDescription = "Playing Hypixel Skyblock";


    // API
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

    @Switch(
            name = "Print errors in chat",
            category = "General",
            subcategory = "API",
            description = "Send errors on getting data in chat (Recommended, however if you get spammed or have a bad internet connection, turn it off)"

    )
    public boolean printApiErrors = true;

    @Text(
            name = "Public Data Repo Owner",
            category = "General",
            subcategory = "API",
            secure = true,
            description = "Change the owner of the repo used for public data."
    )
    public String repoOwner = "PartlySaneStudios";

    @Text(
            name = "Public Data Repo Name",
            category = "General",
            subcategory = "API",
            secure = true,
            description = "Change the name of the repo used for public data."
    )
    public String repoName = "partly-sane-skies-public-data";


    //    Appearance
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

    @Dropdown(
            category = "General",
            subcategory = "Appearance",
            name = "Preferred Currency",
            description = "Select your preferred currency conversion for the /c2c command. Currencies are listed in alphabetical order. Default currency is USD.",
            options = {
                    "AUD (Australian Dollar)",
                    "BRL (Brazilian Real)",
                    "CAD (Canadian Dollar)",
                    "DKK (Danish Krone)",
                    "EUR (Euro)",
                    "KPW (North Korean Won)",
                    "NOK (Norwegian Krone)",
                    "NZD (New Zealand Dollar)",
                    "PLN (Polish Zloty)",
                    "GBP (Pound Sterling)",
                    "SEK (Swedish Krona)",
                    "USD (United States Dollar)"
            }
    )
    public int prefCurr = 11;


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
            name = "Check Mods On Startup",
            category = "General",
            subcategory = "Mods Checker",
            description = "Automatically Send Message on Startup"

    )
    public boolean checkModsOnStartup = true;

    @Switch(
            name = "Use Beta Versions",
            category = "General",
            subcategory = "Mods Checker",
            description = "Use the beta version of mods instead of normal versions"
    )
    public boolean lookForBetaMods = !"0".equals("@RELEASE_CHANNEL@");

    @Switch(
            name = "Show up to date mods",
            category = "General",
            subcategory = "Mods Checker",
            description = "Show mods that are up to date"
    )
    public boolean showUpToDateMods = true;

    @Switch(
            name = "Privacy Mode",
            category = "General",
            subcategory = "Privacy",
            description = "Blocks the diagnostics reports from other mods from being sent to their servers."
    )
    public boolean privacyMode = true;

    //    Config
    @KeyBind(
            name = "Config Hotkey",
            category = "General",
            subcategory = "Config"
    )
    public OneKeyBind oneConfigKeybind = new OneKeyBind(Keyboard.KEY_F7);

    //    Help
    @KeyBind(
            name = "Help Hotkey",
            category = "General",
            subcategory = "Help"
    )
    public OneKeyBind helpKeybind = new OneKeyBind(Keyboard.KEY_H);

}
