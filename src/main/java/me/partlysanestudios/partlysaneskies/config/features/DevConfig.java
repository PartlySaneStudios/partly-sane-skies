package me.partlysanestudios.partlysaneskies.config.features;

import cc.polyfrost.oneconfig.config.annotations.KeyBind;
import cc.polyfrost.oneconfig.config.annotations.Switch;
import cc.polyfrost.oneconfig.config.core.OneKeyBind;
import org.lwjgl.input.Keyboard;

public class DevConfig {
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
