//
// Written by J10a1n15.
// See LICENSE for copyright and license notices.
//
// Time spend afking in the mines: ~4h
//

package me.partlysanestudios.partlysaneskies.features.mining;

import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import me.partlysanestudios.partlysaneskies.gui.hud.BannerRenderer;
import me.partlysanestudios.partlysaneskies.gui.hud.PSSBanner;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MiningEvents {

    private static boolean showBanner = false;

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        if (!PartlySaneSkies.config.miningEventsToggle) return;

        String displayText = "";

        String message = event.message.getFormattedText();

        // 2x Powder
        if (message.contains("The §b2x Powder §eevent starts in §a20 §eseconds!") && PartlySaneSkies.config.mining2xPowderSound && PartlySaneSkies.config.miningWarn20sBeforeEvent) {
            showBanner = true;
            PartlySaneSkies.minecraft.thePlayer.playSound("partlysaneskies:bell", 100, 1);
            if (PartlySaneSkies.config.miningShowEventBanner) displayText = "2x Powder Event in 20s!";
        }

        if (message.contains("§l2X POWDER STARTED!") && PartlySaneSkies.config.mining2xPowderSound) {
            showBanner = true;
            PartlySaneSkies.minecraft.thePlayer.playSound("partlysaneskies:bell", 100, 1);
            if (PartlySaneSkies.config.miningShowEventBanner) displayText = "2x Powder Event Started!";
        }

        // Gone with the wind
        if (message.contains("The §9Gone with the Wind §eevent starts in §a20 §eseconds!") && PartlySaneSkies.config.miningGoneWithTheWindSound && PartlySaneSkies.config.miningWarn20sBeforeEvent) {
            showBanner = true;
            PartlySaneSkies.minecraft.thePlayer.playSound("partlysaneskies:bell", 100, 1);
            if (PartlySaneSkies.config.miningShowEventBanner) displayText = "Gone with the Wind Event in 20s!";
        }

        if (message.contains("§r§9§lGONE WITH THE WIND STARTED!") && PartlySaneSkies.config.miningGoneWithTheWindSound) {
            showBanner = true;
            PartlySaneSkies.minecraft.thePlayer.playSound("partlysaneskies:bell", 100, 1);
            if (PartlySaneSkies.config.miningShowEventBanner) displayText = "Gone with the Wind Event Started!";
        }

        // Better Together
        if (message.contains("The §dBetter Together §eevent starts in §a20 §eseconds!") && PartlySaneSkies.config.miningBetterTogetherSound && PartlySaneSkies.config.miningWarn20sBeforeEvent) {
            showBanner = true;
            PartlySaneSkies.minecraft.thePlayer.playSound("partlysaneskies:bell", 100, 1);
            if (PartlySaneSkies.config.miningShowEventBanner) displayText = "Better Together Event in 20s!";
        }

        if (message.contains("§r§d§lBETTER TOGETHER STARTED!") && PartlySaneSkies.config.miningBetterTogetherSound) {
            showBanner = true;
            PartlySaneSkies.minecraft.thePlayer.playSound("partlysaneskies:bell", 100, 1);
            if (PartlySaneSkies.config.miningShowEventBanner) displayText = "Better Together Event Started!";
        }

        // Goblin Raid
        if (message.contains("§eThe §cGoblin Raid §eevent starts in §a20 §eseconds!") && PartlySaneSkies.config.miningGoblinRaidSound && PartlySaneSkies.config.miningWarn20sBeforeEvent) {
            showBanner = true;
            PartlySaneSkies.minecraft.thePlayer.playSound("partlysaneskies:bell", 100, 1);
            if (PartlySaneSkies.config.miningShowEventBanner) displayText = "Goblin Raid Event in 20s!";
        }

        if (message.contains("§r§c§lGOBLIN RAID STARTED!") && PartlySaneSkies.config.miningGoblinRaidSound) {
            showBanner = true;
            PartlySaneSkies.minecraft.thePlayer.playSound("partlysaneskies:bell", 100, 1);
            if (PartlySaneSkies.config.miningShowEventBanner) displayText = "Goblin Raid Event Started!";
        }

        // Raffle
        if (message.contains("The §6Raffle §eevent starts in §a20 §eseconds!") && PartlySaneSkies.config.miningRaffleSound && PartlySaneSkies.config.miningWarn20sBeforeEvent) {
            showBanner = true;
            PartlySaneSkies.minecraft.thePlayer.playSound("partlysaneskies:bell", 100, 1);
            if (PartlySaneSkies.config.miningShowEventBanner) displayText = "Raffle Event in 20s!";
        }

        if (message.contains("§r§6§lRAFFLE STARTED!") && PartlySaneSkies.config.miningRaffleSound) {
            showBanner = true;
            PartlySaneSkies.minecraft.thePlayer.playSound("partlysaneskies:bell", 100, 1);
            if (PartlySaneSkies.config.miningShowEventBanner) displayText = "Raffle Event Started!";
        }

        // Gourmand
        if (message.contains("§eThe §bMithril Gourmand §eevent starts in §a20 §eseconds!") && PartlySaneSkies.config.miningMithrilGourmandSound && PartlySaneSkies.config.miningWarn20sBeforeEvent){
            showBanner = true;
            PartlySaneSkies.minecraft.thePlayer.playSound("partlysaneskies:bell", 100, 1);
            if (PartlySaneSkies.config.miningShowEventBanner) displayText = "Mithril Gourmand Event in 20s!";
        }

        if (message.contains("§r§b§lMITHRIL GOURMAND STARTED!") && PartlySaneSkies.config.miningMithrilGourmandSound){
            showBanner = true;
            PartlySaneSkies.minecraft.thePlayer.playSound("partlysaneskies:bell", 100, 1);
            if (PartlySaneSkies.config.miningShowEventBanner) displayText = "Mithril Gourmand Event Started!";
        }

        // Powder Ghast
        if (message.contains("§6The sound of pickaxes clashing against the rock has attracted the attention of the §r§6§lPOWDER GHAST!") && PartlySaneSkies.config.miningPowderGhastSound){
            showBanner = true;
            PartlySaneSkies.minecraft.thePlayer.playSound("partlysaneskies:bell", 100, 1);
            if (PartlySaneSkies.config.miningShowEventBanner) displayText = "Powder Ghast Spawned!";
        }

        // Fallen Star
        if (message.contains("§eA §r§5Fallen Star §r§ehas crashed at") && PartlySaneSkies.config.miningFallenStarSound){
            showBanner = true;
            PartlySaneSkies.minecraft.thePlayer.playSound("partlysaneskies:bell", 100, 1);
            if (PartlySaneSkies.config.miningShowEventBanner) displayText = "Fallen Star Spawned!";
        }

        if (showBanner) {
            BannerRenderer.INSTANCE.renderNewBanner(new PSSBanner(displayText, (long) (PartlySaneSkies.config.miningEventBannerTime * 1000), 4.0f, PartlySaneSkies.config.miningEventBannerColor.toJavaColor()));
        }
        showBanner = false;
    }
}

/* ALL THE MINING EVENTS RELATED MESSAGES

    **MAJOR EVENTS**

    2x POWDER
     §b⚑ §eThe §b2x Powder §eevent starts in §a20 §eseconds!
     §eThis is a passive event! §bIt's happening everywhere in the §bCrystal Hollows!§r

    §r§r§r                          §r§b§l2X POWDER STARTED!§r


    WIND
     §9⚑ §eThe §9Gone with the Wind §eevent starts in §a20 §eseconds!
     §eThis is a passive event! §bIt's happening everywhere in the §bCrystal Hollows!§r

    §r§r§r                   §r§9§lGONE WITH THE WIND STARTED!§r


    BETTER TOGETHER
     §d⚑ §eThe §dBetter Together §eevent starts in §a20 §eseconds!
     §eThis is a passive event! §bIt's happening everywhere in the §bCrystal Hollows!§r

    §r§r§r                    §r§d§lBETTER TOGETHER STARTED!§r


    RAID
     §c⚑ §eThe §cGoblin Raid §eevent starts in §a20 §eseconds!
     §aClick here §eto teleport to §bGarry §eand prepare!§r

    §r§r§r                        §r§c§lGOBLIN RAID STARTED!§r


    RAFFLE
     §6⚑ §eThe §6Raffle §eevent starts in §a20 §eseconds!
     §aClick here §eto teleport to §bGarry §eand prepare!§r

     §r§r§r                            §r§6§lRAFFLE STARTED!§r


    GOURMAND
     §b⚑ §eThe §bMithril Gourmand §eevent starts in §a20 §eseconds!
     §aClick here §eto teleport to §bGarry §eand prepare!§r

    §r§r§r                    §r§b§lMITHRIL GOURMAND STARTED!§r


    **MINOR EVENTS**

    POWDER GHAST
    §r§6The sound of pickaxes clashing against the rock has attracted the attention of the §r§6§lPOWDER GHAST!§r
    §r§eFind the §r§6Powder Ghast§r§e near the §r§bCliffside Veins§r§e!§r


    FALLEN STAR
    §r§5§l✯ §r§eA §r§5Fallen Star §r§ehas crashed at §r§bRoyal Mines§r§e! Nearby ore and Powder drops are amplified!§r

 */