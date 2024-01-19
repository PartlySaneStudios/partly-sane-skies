package me.partlysanestudios.partlysaneskies.config.features;

import cc.polyfrost.oneconfig.config.annotations.*;
import cc.polyfrost.oneconfig.config.core.OneColor;
import cc.polyfrost.oneconfig.config.data.InfoType;

public class MiningConfig {
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
}
