//
// Written by J10a1n15.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.features.dungeons;

import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import me.partlysanestudios.partlysaneskies.data.skyblockdata.IslandType;
import me.partlysanestudios.partlysaneskies.events.SubscribePSSEvent;
import me.partlysanestudios.partlysaneskies.events.skyblock.dungeons.DungeonStartEvent;
import me.partlysanestudios.partlysaneskies.render.gui.hud.BannerRenderer;
import me.partlysanestudios.partlysaneskies.render.gui.hud.PSSBanner;
import me.partlysanestudios.partlysaneskies.utils.HypixelUtils;
import me.partlysanestudios.partlysaneskies.utils.MinecraftUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RequiredSecretsFound {

    private static boolean alreadySendThisRun = false;
    private static long lastCheckTime = PartlySaneSkies.Companion.getTime();

    public static void tick() {
        if (!HypixelUtils.INSTANCE.isSkyblock()) {
            return;
        }

        if (!IslandType.CATACOMBS.onIsland()){
            return;
        }

        if (alreadySendThisRun){
            return;
        }

        if (lastCheckTime + 100 > PartlySaneSkies.Companion.getTime()) { //checks every 100ms
            return;
        }
        lastCheckTime = PartlySaneSkies.Companion.getTime();


        for (String line : MinecraftUtils.INSTANCE.getTabList()) {
            if (line.contains("Secrets Found: §r§a")) {
                if (PartlySaneSkies.Companion.getConfig().getSecretsBanner()) {
                    BannerRenderer.INSTANCE.renderNewBanner(new PSSBanner("Required Secrets Found!", (long) (PartlySaneSkies.Companion.getConfig().getSecretsBannerTime() * 1000), 3.0f, PartlySaneSkies.Companion.getConfig().getSecretsBannerColor().toJavaColor()));
                }
                if (PartlySaneSkies.Companion.getConfig().getSecretsChatMessage()) {
                    Minecraft.getMinecraft().thePlayer.sendChatMessage("/pc " + PartlySaneSkies.Companion.getConfig().getSecretsChatMessageString());
                }
                if (PartlySaneSkies.Companion.getConfig().getSecretsSound()) {
                    if (PartlySaneSkies.Companion.getConfig().getSecretsAirRaidSiren()){
                        PartlySaneSkies.Companion.getMinecraft().getSoundHandler()
                                .playSound(PositionedSoundRecord.create(new ResourceLocation("partlysaneskies", "airraidsiren")));
                    } else {
                        PartlySaneSkies.Companion.getMinecraft().getSoundHandler()
                                .playSound(PositionedSoundRecord.create(new ResourceLocation("partlysaneskies", "bell")));
                    }
                }

                alreadySendThisRun = true;
                break;
            }
        }
    }

    @SubscribePSSEvent
    public void onDungeonStart(DungeonStartEvent event) {
        alreadySendThisRun = false;
    }
}