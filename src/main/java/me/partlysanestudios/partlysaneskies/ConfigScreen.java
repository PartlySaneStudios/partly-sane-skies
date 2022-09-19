package me.partlysanestudios.partlysaneskies;

import java.awt.Color;
import java.io.File;

import gg.essential.vigilance.Vigilant;
import gg.essential.vigilance.data.Property;
import gg.essential.vigilance.data.PropertyType;

public class ConfigScreen extends Vigilant{

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
    

    // Main Menu
    @Property(
        type= PropertyType.SWITCH,
        category = "General",
        subcategory =  "Main Menu",
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
        description = "The amount of time the rare drop banner appears for.",
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
        description = "The amount of time the worm warning banner appears for.",
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
        type = PropertyType.SLIDER,
        name = "Party Manager Cache Time",
        min = 0,
        max = 90,
        subcategory = "Party Manager",
        description = "Saves the data from other party members to save time upon loading Party Manager. The bigger the value the more time you will save but the less accurate your data will be.",
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
        description = "The amount of time the watcher ready banner appears for.",
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
        description = "Plays a WWII air raid siren when the watcher is done spawning mobs. \nPros: \nKeeps you up at late night grinds \n(RECOMMENDED)",
        category = "Dungeons"
    )
    public boolean watcherReadyAirRaidSiren = false;














    public ConfigScreen() {
        super(new File("./config/partly-sane-skies/pss.toml"));
        this.initialize();

        
    }

    
}
