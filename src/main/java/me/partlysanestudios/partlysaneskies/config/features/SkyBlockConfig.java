package me.partlysanestudios.partlysaneskies.config.features;

import cc.polyfrost.oneconfig.config.annotations.*;
import cc.polyfrost.oneconfig.config.core.OneKeyBind;
import org.lwjgl.input.Keyboard;

public class SkyBlockConfig {
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

}
