//
// Written by J10a1n15.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.mining;

import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import me.partlysanestudios.partlysaneskies.garden.endoffarmnotifer.EndOfFarmNotifier;
import me.partlysanestudios.partlysaneskies.system.BannerRenderer;
import me.partlysanestudios.partlysaneskies.system.PSSBanner;
import me.partlysanestudios.partlysaneskies.utils.LocationUtils;
import me.partlysanestudios.partlysaneskies.utils.StringUtils;
import me.partlysanestudios.partlysaneskies.utils.Utils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Pickaxes {
    private static final Pattern pattern = Pattern.compile("(Mining Speed Boost|Pickobulus|Maniac Miner|Vein Seeker) is now available!");
    public static final String[] pickaxeAbilities = {"Mining Speed Boost", "Pickobulus", "Maniac Miner", "Vein Seeker"};

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        if (PartlySaneSkies.config.onlyGiveWarningOnMiningIsland){
            if (!LocationUtils.onMiningIsland()) return;
        }

        String message = StringUtils.removeColorCodes(event.message.getFormattedText());
        Matcher matcher = pattern.matcher(message);

        if (matcher.find()) {
            if (PartlySaneSkies.config.pickaxeAbilityReadyBanner){
                BannerRenderer.INSTANCE.renderNewBanner(new PSSBanner("Pickaxe Ability Ready!", (long) (PartlySaneSkies.config.pickaxeBannerTime * 1000), 4.0f, PartlySaneSkies.config.pickaxeBannerColor.toJavaColor()));
            }
            if (PartlySaneSkies.config.pickaxeAbilityReadySound) {
                if (PartlySaneSkies.config.pickaxeAbilityReadySiren) {
                    PartlySaneSkies.minecraft.thePlayer.playSound("partlysaneskies:airraidsiren", 100, 1);
                } else {
                    PartlySaneSkies.minecraft.thePlayer.playSound("partlysaneskies:bell", 100, 1);
                }
            }
        }
    }

    @SubscribeEvent
    public void onClick(PlayerInteractEvent event) {
        if (!PartlySaneSkies.config.blockAbilityOnPrivateIsland) {
            return;
        }
        if (LocationUtils.inGarden() || LocationUtils.onPrivateIsland()){} else return; //dont mind me not wanting to nest code

        if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK || event.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR) {
            String[] loreOfItemInHand = Utils.getLore(Utils.getCurrentlyHoldingItem()).toArray(new String[0]);

            if (Utils.isArrOfStringsInLore(pickaxeAbilities, loreOfItemInHand)) {
                event.setCanceled(true);
            }
        }
    }
}