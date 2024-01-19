package me.partlysanestudios.partlysaneskies.config.features;

import cc.polyfrost.oneconfig.config.annotations.*;
import cc.polyfrost.oneconfig.config.annotations.Number;
import cc.polyfrost.oneconfig.config.core.OneKeyBind;
import org.lwjgl.input.Keyboard;

public class DevConfig {
    // API
    @Number(
            min = .1f,
            max = 30f,
            name = "Time between requests",
            category = "Dev",
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
            category = "Dev"
    )
    public int playerDataCacheTime = 5;

    @Switch(
            name = "Print errors in chat",
            category = "Dev",
            subcategory = "API",
            description = "Send errors on getting data in chat (Recommended, however if you get spammed or have a bad internet connection, turn it off)"

    )
    public boolean printApiErrors = true;

    @Text(
            name = "Public Data Repo Owner",
            category = "Dev",
            subcategory = "API",
            secure = true,
            description = "Change the owner of the repo used for public data."
    )
    public String repoOwner = "PartlySaneStudios";

    @Text(
            name = "Public Data Repo Name",
            category = "Dev",
            subcategory = "API",
            secure = true,
            description = "Change the name of the repo used for public data."
    )
    public String repoName = "partly-sane-skies-public-data";

    @KeyBind(
            category = "Dev",
            name = "Debug Hotkey"
    )
    public OneKeyBind debugKeybind = new OneKeyBind(Keyboard.KEY_NONE);

    @Switch(
            name = "Debug Mode",
            category = "Dev"
    )
    public boolean debugMode = false;

    @Switch(
            name = "Render TEST Banner",
            category = "Dev"
    )
    public boolean debugRenderTestBanner = false;

    @Switch(
            name = "Chat Analyser",
            category = "Dev"
    )
    public boolean debugChatAnalyser = false;

    @Switch(
            name = "Add a slacker to the party",
            category = "Dev"
    )
    public boolean debugAddSlacker = false;

    @Switch(
            name = "Spawn Waypoint",
            category = "Dev",
            description = "Spawns a waypoint at your current location"
    )
    public boolean debugSpawnWaypoint = false;

    @Switch(
            name = "Send a system notification",
            category = "Dev"
    )
    public boolean debugSendSystemNotification = false;

    @Switch(
            name = "Print pet world parsing information",
            category = "Dev"
    )
    public boolean debugPrintPetWorldParsingInformation = false;

    @Switch(
            name = "Print current location from Island Type",
            category = "Dev"
    )
    public boolean debugPrintCurrentLocationFromIslandType = false;

    @Switch(
            name = "Percy Mode",
            category = "Dev",
            subcategory = "Percy Mode"
    )
    public boolean percyMode = false;

    @Switch(
            name = "Current Screen",
            category = "Dev",
            subcategory = "Percy Mode"
    )
    public boolean debugCurrentScreenDump = false;
    @Switch(
            name = "Entity Dump",
            category = "Dev",
            subcategory = "Percy Mode"
    )
    public boolean debugEntityDump = false;
    @Switch(
            name = "Inventory Dump",
            category = "Dev",
            subcategory = "Percy Mode"
    )
    public boolean debugInventoryDump = false;
    @Switch(
            name = "Player Dump",
            category = "Dev",
            subcategory = "Percy Mode"
    )
    public boolean debugPlayerDump = false;
}
