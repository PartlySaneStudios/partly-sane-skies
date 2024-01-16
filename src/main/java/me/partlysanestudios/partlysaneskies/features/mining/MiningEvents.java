//
// Written by J10a1n15.
// See LICENSE for copyright and license notices.
//
// Time spend afking in the mines: ~4h
//

package me.partlysanestudios.partlysaneskies.features.mining;

import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import me.partlysanestudios.partlysaneskies.system.SystemNotification;
import me.partlysanestudios.partlysaneskies.render.gui.hud.BannerRenderer;
import me.partlysanestudios.partlysaneskies.render.gui.hud.PSSBanner;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.Display;

public class MiningEvents {
    // TODO: lmao this needs a rewrite
    private static boolean showBanner = false;

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        if (!PartlySaneSkies.Companion.getConfig().miningEventsToggle) return;

        String displayText = "";

        String message = event.message.getFormattedText();

        // 2x Powder
        if (message.contains("The §b2x Powder §eevent starts in §a20 §eseconds!") && PartlySaneSkies.Companion.getConfig().mining2xPowderSound && PartlySaneSkies.Companion.getConfig().miningWarn20sBeforeEvent) {
            showBanner = true;
            PartlySaneSkies.Companion.getMinecraft().thePlayer.playSound("partlysaneskies:bell", 100, 1);
            if (PartlySaneSkies.Companion.getConfig().miningShowEventBanner) displayText = "2x Powder Event in 20s!";
            if (PartlySaneSkies.Companion.getConfig().miningSendSystemNotifications && !Display.isActive()) SystemNotification.INSTANCE.showNotification("2x Powder Event in 20s!");
        }

        if (message.contains("§l2X POWDER STARTED!") && PartlySaneSkies.Companion.getConfig().mining2xPowderSound) {
            showBanner = true;
            PartlySaneSkies.Companion.getMinecraft().thePlayer.playSound("partlysaneskies:bell", 100, 1);
            if (PartlySaneSkies.Companion.getConfig().miningShowEventBanner) displayText = "2x Powder Event Started!";
            if (PartlySaneSkies.Companion.getConfig().miningSendSystemNotifications && !Display.isActive()) SystemNotification.INSTANCE.showNotification("2x Powder Event Started!");
        }

        // Gone with the wind
        if (message.contains("The §9Gone with the Wind §eevent starts in §a20 §eseconds!") && PartlySaneSkies.Companion.getConfig().miningGoneWithTheWindSound && PartlySaneSkies.Companion.getConfig().miningWarn20sBeforeEvent) {
            showBanner = true;
            PartlySaneSkies.Companion.getMinecraft().thePlayer.playSound("partlysaneskies:bell", 100, 1);
            if (PartlySaneSkies.Companion.getConfig().miningShowEventBanner) displayText = "Gone with the Wind Event in 20s!";
            if (PartlySaneSkies.Companion.getConfig().miningSendSystemNotifications && !Display.isActive()) SystemNotification.INSTANCE.showNotification("Gone with the Wind Event in 20s!");
        }

        if (message.contains("§r§9§lGONE WITH THE WIND STARTED!") && PartlySaneSkies.Companion.getConfig().miningGoneWithTheWindSound) {
            showBanner = true;
            PartlySaneSkies.Companion.getMinecraft().thePlayer.playSound("partlysaneskies:bell", 100, 1);
            if (PartlySaneSkies.Companion.getConfig().miningShowEventBanner) displayText = "Gone with the Wind Event Started!";
            if (PartlySaneSkies.Companion.getConfig().miningSendSystemNotifications && !Display.isActive()) SystemNotification.INSTANCE.showNotification("Gone with the Wind Event Started!");
        }

        // Better Together
        if (message.contains("The §dBetter Together §eevent starts in §a20 §eseconds!") && PartlySaneSkies.Companion.getConfig().miningBetterTogetherSound && PartlySaneSkies.Companion.getConfig().miningWarn20sBeforeEvent) {
            showBanner = true;
            PartlySaneSkies.Companion.getMinecraft().thePlayer.playSound("partlysaneskies:bell", 100, 1);
            if (PartlySaneSkies.Companion.getConfig().miningShowEventBanner) displayText = "Better Together Event in 20s!";
            if (PartlySaneSkies.Companion.getConfig().miningSendSystemNotifications && !Display.isActive()) SystemNotification.INSTANCE.showNotification("Better Together Event in 20s!");
        }

        if (message.contains("§r§d§lBETTER TOGETHER STARTED!") && PartlySaneSkies.Companion.getConfig().miningBetterTogetherSound) {
            showBanner = true;
            PartlySaneSkies.Companion.getMinecraft().thePlayer.playSound("partlysaneskies:bell", 100, 1);
            if (PartlySaneSkies.Companion.getConfig().miningShowEventBanner) displayText = "Better Together Event Started!";
            if (PartlySaneSkies.Companion.getConfig().miningSendSystemNotifications && !Display.isActive()) SystemNotification.INSTANCE.showNotification("Better Together Event Started!");
        }

        // Goblin Raid
        if (message.contains("§eThe §cGoblin Raid §eevent starts in §a20 §eseconds!") && PartlySaneSkies.Companion.getConfig().miningGoblinRaidSound && PartlySaneSkies.Companion.getConfig().miningWarn20sBeforeEvent) {
            showBanner = true;
            PartlySaneSkies.Companion.getMinecraft().thePlayer.playSound("partlysaneskies:bell", 100, 1);
            if (PartlySaneSkies.Companion.getConfig().miningShowEventBanner) displayText = "Goblin Raid Event in 20s!";
            if (PartlySaneSkies.Companion.getConfig().miningSendSystemNotifications && !Display.isActive()) SystemNotification.INSTANCE.showNotification("Goblin Raid Event in 20s!");
        }

        if (message.contains("§r§c§lGOBLIN RAID STARTED!") && PartlySaneSkies.Companion.getConfig().miningGoblinRaidSound) {
            showBanner = true;
            PartlySaneSkies.Companion.getMinecraft().thePlayer.playSound("partlysaneskies:bell", 100, 1);
            if (PartlySaneSkies.Companion.getConfig().miningShowEventBanner) displayText = "Goblin Raid Event Started!";
            if (PartlySaneSkies.Companion.getConfig().miningSendSystemNotifications && !Display.isActive()) SystemNotification.INSTANCE.showNotification("Goblin Raid Event Started!");
        }

        // Raffle
        if (message.contains("The §6Raffle §eevent starts in §a20 §eseconds!") && PartlySaneSkies.Companion.getConfig().miningRaffleSound && PartlySaneSkies.Companion.getConfig().miningWarn20sBeforeEvent) {
            showBanner = true;
            PartlySaneSkies.Companion.getMinecraft().thePlayer.playSound("partlysaneskies:bell", 100, 1);
            if (PartlySaneSkies.Companion.getConfig().miningShowEventBanner) displayText = "Raffle Event in 20s!";
            if (PartlySaneSkies.Companion.getConfig().miningSendSystemNotifications && !Display.isActive()) SystemNotification.INSTANCE.showNotification("Raffle Event in 20s!");
        }

        if (message.contains("§r§6§lRAFFLE STARTED!") && PartlySaneSkies.Companion.getConfig().miningRaffleSound) {
            showBanner = true;
            PartlySaneSkies.Companion.getMinecraft().thePlayer.playSound("partlysaneskies:bell", 100, 1);
            if (PartlySaneSkies.Companion.getConfig().miningShowEventBanner) displayText = "Raffle Event Started!";
            if (PartlySaneSkies.Companion.getConfig().miningSendSystemNotifications && !Display.isActive()) SystemNotification.INSTANCE.showNotification("Raffle Event Started!");
        }

        // Gourmand
        if (message.contains("§eThe §bMithril Gourmand §eevent starts in §a20 §eseconds!") && PartlySaneSkies.Companion.getConfig().miningMithrilGourmandSound && PartlySaneSkies.Companion.getConfig().miningWarn20sBeforeEvent){
            showBanner = true;
            PartlySaneSkies.Companion.getMinecraft().thePlayer.playSound("partlysaneskies:bell", 100, 1);
            if (PartlySaneSkies.Companion.getConfig().miningShowEventBanner) displayText = "Mithril Gourmand Event in 20s!";
            if (PartlySaneSkies.Companion.getConfig().miningSendSystemNotifications && !Display.isActive()) SystemNotification.INSTANCE.showNotification("Mithril Gourmand Event in 20s!");
        }

        if (message.contains("§r§b§lMITHRIL GOURMAND STARTED!") && PartlySaneSkies.Companion.getConfig().miningMithrilGourmandSound){
            showBanner = true;
            PartlySaneSkies.Companion.getMinecraft().thePlayer.playSound("partlysaneskies:bell", 100, 1);
            if (PartlySaneSkies.Companion.getConfig().miningShowEventBanner) displayText = "Mithril Gourmand Event Started!";
            if (PartlySaneSkies.Companion.getConfig().miningSendSystemNotifications && !Display.isActive()) SystemNotification.INSTANCE.showNotification("Mithril Gourmand Event Started!");
        }

        // Powder Ghast
        if (message.contains("§6The sound of pickaxes clashing against the rock has attracted the attention of the §r§6§lPOWDER GHAST!") && PartlySaneSkies.Companion.getConfig().miningPowderGhastSound){
            showBanner = true;
            PartlySaneSkies.Companion.getMinecraft().thePlayer.playSound("partlysaneskies:bell", 100, 1);
            if (PartlySaneSkies.Companion.getConfig().miningShowEventBanner) displayText = "Powder Ghast Spawned!";
            if (PartlySaneSkies.Companion.getConfig().miningSendSystemNotifications && !Display.isActive()) SystemNotification.INSTANCE.showNotification("Powder Ghast Spawned!");
        }

        // Fallen Star
        if (message.contains("§eA §r§5Fallen Star §r§ehas crashed at") && PartlySaneSkies.Companion.getConfig().miningFallenStarSound){
            showBanner = true;
            PartlySaneSkies.Companion.getMinecraft().thePlayer.playSound("partlysaneskies:bell", 100, 1);
            if (PartlySaneSkies.Companion.getConfig().miningShowEventBanner) displayText = "Fallen Star Spawned!";
            if (PartlySaneSkies.Companion.getConfig().miningSendSystemNotifications && !Display.isActive()) SystemNotification.INSTANCE.showNotification("Fallen Star Spawned!");
        }

        if (showBanner) {
            BannerRenderer.INSTANCE.renderNewBanner(new PSSBanner(displayText, (long) (PartlySaneSkies.Companion.getConfig().miningEventBannerTime * 1000), 4.0f, PartlySaneSkies.Companion.getConfig().miningEventBannerColor.toJavaColor()));
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