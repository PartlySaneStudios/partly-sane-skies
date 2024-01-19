//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.config;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.Number;
import me.partlysanestudios.partlysaneskies.config.features.*;
import org.lwjgl.input.Keyboard;

import cc.polyfrost.oneconfig.config.annotations.*;
import cc.polyfrost.oneconfig.config.core.OneColor;
import cc.polyfrost.oneconfig.config.core.OneKeyBind;
import cc.polyfrost.oneconfig.config.data.InfoType;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import cc.polyfrost.oneconfig.libs.universal.UKeyboard;
import me.partlysanestudios.partlysaneskies.features.dungeons.PearlRefill;
import scala.reflect.macros.Universe;

public class OneConfigScreen extends Config {

    public OneConfigScreen() {
        // Available mod types: PVP, HUD, UTIL_QOL, HYPIXEL, SKYBLOCK
        super(new Mod("Partly Sane Skies", ModType.SKYBLOCK, "/assets/partlysaneskies/textures/logo_oneconfig.png"), "partly-sane-skies/config.json");
        initialize();

        registerKeyBind(refillPearlsKeybind, PearlRefill.INSTANCE::keybindAction);
    }

    public void resetBrokenStringsTick() {
        if (dungeons.arrowLowChatMessage.isEmpty()) {
            dungeons.arrowLowChatMessage = "Partly Sane Skies > Warning! {player} only has {count} arrows remaining!";
            save();
        }
        if (dungeons.watcherChatMessage.isEmpty()) {
            dungeons.watcherChatMessage = "Partly Sane Skies > The watcher is done spawning mobs. Ready to clear.";
            save();
        }
        if (dungeons.secretsChatMessageString.isEmpty()) {
            dungeons.secretsChatMessageString = "Partly Sane Skies > All required secrets have been found!";
            save();
        }
        if (dungeons.pickaxeAbilityReadyBannerText.isEmpty()) {
            dungeons.pickaxeAbilityReadyBannerText = "Pickaxe Ability Ready!";
            save();
        }

        if (dungeons.autoGGMessageSPlus.isEmpty()) {
            dungeons.autoGGMessageSPlus = "GG Easy";
            save();
        }
        if (dungeons.autoGGMessageS.isEmpty()) {
            dungeons.autoGGMessageS = "GG";
            save();
        }
        if (dungeons.autoGGMessageSPlus.isEmpty()) {
            dungeons.autoGGMessageOther = "Welp, GG";
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
    public SkyBlockConfig skyblock = new SkyBlockConfig();
    public DungeonsConfig dungeons = new DungeonsConfig();
    public MiningConfig mining = new MiningConfig();
    public FarmingConfig farming = new FarmingConfig();
    public ForagingConfig foraging = new ForagingConfig();
    public EconomyConfig economy = new EconomyConfig();
    public ChatConfig chat = new ChatConfig();
}
