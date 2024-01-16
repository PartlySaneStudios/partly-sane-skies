//
// Written by J10a1n15.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.features.dungeons;

import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import me.partlysanestudios.partlysaneskies.render.gui.hud.BannerRenderer;
import me.partlysanestudios.partlysaneskies.render.gui.hud.PSSBanner;
import me.partlysanestudios.partlysaneskies.utils.HypixelUtils;
import me.partlysanestudios.partlysaneskies.utils.IslandType;
import me.partlysanestudios.partlysaneskies.utils.MinecraftUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class RequiredSecretsFound {

    private boolean alreadySendThisRun = false;
    private long lastCheckTime = PartlySaneSkies.Companion.getTime();

    @SubscribeEvent
    public void checkRequiredSecrets(TickEvent.ClientTickEvent event) {
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
                if (PartlySaneSkies.Companion.getConfig().secretsBanner) {
                    BannerRenderer.INSTANCE.renderNewBanner(new PSSBanner("Required Secrets Found!", (long) (PartlySaneSkies.Companion.getConfig().secretsBannerTime * 1000), 3.0f, PartlySaneSkies.Companion.getConfig().secretsBannerColor.toJavaColor()));
                }
                if (PartlySaneSkies.Companion.getConfig().secretsChatMessage) {
                    Minecraft.getMinecraft().thePlayer.sendChatMessage("/pc " + PartlySaneSkies.Companion.getConfig().secretsChatMessageString);
                }
                if (PartlySaneSkies.Companion.getConfig().secretsSound) {
                    if (PartlySaneSkies.Companion.getConfig().secretsAirRaidSiren){
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

    @SubscribeEvent
    public void onChatMessage(ClientChatReceivedEvent event) {
        // I need to remember that formatted text has the §r stuff in it, not the other way around
        String formattedMessage = event.message.getFormattedText();
 
        // Dungeon start
        if (formattedMessage.equals("§r§aStarting in 1 second.§r")) {
            alreadySendThisRun = false;
        }
    }
}