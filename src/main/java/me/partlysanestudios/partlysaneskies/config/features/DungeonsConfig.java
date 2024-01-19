package me.partlysanestudios.partlysaneskies.config.features;

import cc.polyfrost.oneconfig.config.annotations.*;
import cc.polyfrost.oneconfig.config.annotations.Number;
import cc.polyfrost.oneconfig.config.core.OneColor;
import cc.polyfrost.oneconfig.config.core.OneKeyBind;
import cc.polyfrost.oneconfig.libs.universal.UKeyboard;
import org.lwjgl.input.Keyboard;

public class DungeonsConfig {
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
}
