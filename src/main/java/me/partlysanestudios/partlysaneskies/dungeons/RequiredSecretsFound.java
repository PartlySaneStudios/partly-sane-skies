//
// Written by J10a1n15.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.dungeons;

import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import me.partlysanestudios.partlysaneskies.system.BannerRenderer;
import me.partlysanestudios.partlysaneskies.system.PSSBanner;
import me.partlysanestudios.partlysaneskies.utils.Utils;
import me.partlysanestudios.partlysaneskies.utils.TabListUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class RequiredSecretsFound {

    private boolean alreadySendThisRun = false;
    private long lastCheckTime = PartlySaneSkies.getTime();

    @SubscribeEvent
    public void checkRequiredSecrets(TickEvent.ClientTickEvent event) {
        if (!PartlySaneSkies.isSkyblock()) {
            return;
        }

        if (!Utils.inDungeons()){
            return;
        }

        if (alreadySendThisRun){
            return;
        }

        if (lastCheckTime + 100 > PartlySaneSkies.getTime()) { //checks every 100ms
            return;
        }
        lastCheckTime = PartlySaneSkies.getTime();


        for (String line : TabListUtils.getTabList()) {
            if (line.contains("Secrets Found: §r§a")) {
                if (PartlySaneSkies.config.secretsBanner) {
                    BannerRenderer.INSTANCE.renderNewBanner(new PSSBanner("Required Secrets Found!", (long) (PartlySaneSkies.config.secretsBannerTime * 1000), 3.0f, PartlySaneSkies.config.secretsBannerColor.toJavaColor()));
                }
                if (PartlySaneSkies.config.secretsChatMessage) {
                    Minecraft.getMinecraft().thePlayer.sendChatMessage("/pc " + PartlySaneSkies.config.secretsChatMessageString);
                }
                if (PartlySaneSkies.config.secretsSound) {
                    if (PartlySaneSkies.config.secretsAirRaidSiren){
                        PartlySaneSkies.minecraft.getSoundHandler()
                                .playSound(PositionedSoundRecord.create(new ResourceLocation("partlysaneskies", "airraidsiren")));
                    } else {
                        PartlySaneSkies.minecraft.getSoundHandler()
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
        String unFormattedMessage = event.message.getUnformattedText();
        if (unFormattedMessage.contains("§r§eentered §r§aThe Catacombs§r§e, §r§eFloor") || unFormattedMessage.contains("§r§eentered §r§c§lMM§r§c Catacombs§r§e, §r§eFloor")) {
            alreadySendThisRun = false;
        }
    }
}