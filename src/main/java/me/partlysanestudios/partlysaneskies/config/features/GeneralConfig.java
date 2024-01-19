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
