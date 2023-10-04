//
// Written by J10a1n15.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.mining;

import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import me.partlysanestudios.partlysaneskies.garden.endoffarmnotifer.EndOfFarmNotifier;
import me.partlysanestudios.partlysaneskies.system.BannerRenderer;
import me.partlysanestudios.partlysaneskies.system.PSSBanner;
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
            if (!onMiningIsland()) return;
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
        if (EndOfFarmNotifier.inGarden() || onPrivateIsland()){} else return; //dont mind me not wanting to nest code

        String[] loreOfItemInHand = Utils.getLore(Utils.getCurrentlyHoldingItem()).toArray(new String[0]);

        if (Utils.isArrOfStringsInLore(pickaxeAbilities, loreOfItemInHand)) {
            event.setCanceled(true);
        }
    }

    public static boolean onPrivateIsland() {
        String location = PartlySaneSkies.getRegionName();
        location = StringUtils.removeColorCodes(location);
        location = StringUtils.stripLeading(location);
        location = StringUtils.stripTrailing(location);
        location = location.replaceAll("\\P{Print}", ""); // Removes the RANDOM EMOJIS THAT ARE PRESENT IN SKYBLOCK LOCATIONS      - Su rant, pls ignore

        return location.startsWith("Your Island"); //Really hope that's the only private island name, and you cant rename it

    }

    public static boolean onMiningIsland() {
        String location = PartlySaneSkies.getRegionName();
        location = StringUtils.removeColorCodes(location);
        location = StringUtils.stripLeading(location);
        location = StringUtils.stripTrailing(location);
        location = location.replaceAll("\\P{Print}", ""); // Removes the RANDOM EMOJIS THAT ARE PRESENT IN SKYBLOCK LOCATIONS      - Su rant, pls ignore

        String[] miningLocations = {
                "jungle", "jungle temple", "mithril deposits", "mines of divan", "goblin holdout",
                "goblin queen's den", "precursor remnants", "lost precursor city", "crystal nucleus",
                "magma fields", "khazad-dûm", "fairy grotto", "dragon's lair", "the forge", "forge basin",
                "palace bridge", "royal palace", "aristocrat passage", "hanging court", "divan's gateway",
                "far reserve", "goblin burrows", "miner's guild", "great ice wall", "the mist", "c&c minecarts co.",
                "grand library", "barracks of heroes", "dwarven village", "the lift", "royal quarters", "lava springs",
                "cliffside veins", "rampart's quarry", "upper mines", "royal mines", "gold mine", "coal mine",
                "gunpowder mines", "lapis quarry", "pigman's den", "slimehill", "diamond reserve", "obsidian sanctuary"
        };

        for (String loc : miningLocations) {
            if (location.toLowerCase().contains(loc)) {
                return true;
            }
        }
        return false;
    }
}