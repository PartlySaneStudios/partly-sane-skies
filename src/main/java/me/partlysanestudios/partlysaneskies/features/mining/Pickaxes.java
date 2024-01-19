//
// Written by J10a1n15.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.features.mining;

import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import me.partlysanestudios.partlysaneskies.data.skyblockdata.IslandType;
import me.partlysanestudios.partlysaneskies.render.gui.hud.BannerRenderer;
import me.partlysanestudios.partlysaneskies.render.gui.hud.PSSBanner;
import me.partlysanestudios.partlysaneskies.utils.MinecraftUtils;
import me.partlysanestudios.partlysaneskies.utils.StringUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Pickaxes {
    private static final Pattern pattern = Pattern.compile("(Mining Speed Boost|Pickobulus|Maniac Miner|Vein Seeker) is now available!");
    public static final String[] pickaxeAbilities = {"Mining Speed Boost", "Pickobulus", "Maniac Miner", "Vein Seeker"};

    @SubscribeEvent(priority = net.minecraftforge.fml.common.eventhandler.EventPriority.HIGHEST)
    public void onChat(ClientChatReceivedEvent event) {
        if (PartlySaneSkies.Companion.getConfig().onlyGiveWarningOnMiningIsland){
            if (!IslandType.DWARVEN_MINES.onIsland() && !IslandType.CRYSTAL_HOLLOWS.onIsland()) return;
        }

        String message = StringUtils.INSTANCE.removeColorCodes(event.message.getFormattedText());
        Matcher matcher = pattern.matcher(message);

        if (matcher.find()) {
            if (PartlySaneSkies.Companion.getConfig().pickaxeAbilityReadyBanner){
                BannerRenderer.INSTANCE.renderNewBanner(new PSSBanner(PartlySaneSkies.Companion.getConfig().pickaxeAbilityReadyBannerText, (long) (PartlySaneSkies.Companion.getConfig().pickaxeBannerTime * 1000), 4.0f, PartlySaneSkies.Companion.getConfig().pickaxeBannerColor.toJavaColor()));
            }
            if (PartlySaneSkies.Companion.getConfig().pickaxeAbilityReadySound) {
                if (PartlySaneSkies.Companion.getConfig().pickaxeAbilityReadySiren) {
                    PartlySaneSkies.Companion.getMinecraft().thePlayer.playSound("partlysaneskies:airraidsiren", 100, 1);
                } else {
                    PartlySaneSkies.Companion.getMinecraft().thePlayer.playSound("partlysaneskies:bell", 100, 1);
                }
            }

            if (PartlySaneSkies.Companion.getConfig().hideReadyMessageFromChat) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onClick(PlayerInteractEvent event) {
        if (!PartlySaneSkies.Companion.getConfig().blockAbilityOnPrivateIsland) {
            return;
        }
        if (IslandType.GARDEN.onIsland() || IslandType.PRIVATE_ISLAND.onIsland()){} else return; //dont mind me not wanting to nest code

        if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK || event.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR) {
            if (MinecraftUtils.INSTANCE.getCurrentlyHoldingItem() == null) return;
            String[] loreOfItemInHand = MinecraftUtils.INSTANCE.getLore(MinecraftUtils.INSTANCE.getCurrentlyHoldingItem()).toArray(new String[0]);

            if (MinecraftUtils.INSTANCE.isArrOfStringsInLore(pickaxeAbilities, loreOfItemInHand)) {
                event.setCanceled(true);
            }
        }
    }
}