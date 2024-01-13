//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.features.farming;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import me.partlysanestudios.partlysaneskies.data.pssdata.PublicDataManager;
import me.partlysanestudios.partlysaneskies.commands.PSSCommand;
import me.partlysanestudios.partlysaneskies.utils.HypixelUtils;
import me.partlysanestudios.partlysaneskies.utils.MathUtils;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.ClickEvent.Action;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

public class MathematicalHoeRightClicks {

    public static long lastMessageSendTime = 0;
    public static long lastAllowHoeRightClickTime = 0;

    private static ArrayList<String> hoes;
    public static void loadHoes() {
        String str = PublicDataManager.INSTANCE.getFile("constants/mathematical_hoes.json");
        JsonArray array = new JsonParser().parse(str).getAsJsonObject().get("hoes").getAsJsonArray();

        hoes = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            String hoe = array.get(i).getAsString();

            hoes.add(hoe);
        }


    }
    public static boolean isHoldingHoe() {
        if (hoes == null) {
            return false;
        }
        if (PartlySaneSkies.minecraft.thePlayer == null) {
            return false;
        }
        ItemStack heldItem = PartlySaneSkies.minecraft.thePlayer.getHeldItem();

        if (heldItem == null) {
            return false;
        }

        return hoes.contains(HypixelUtils.INSTANCE.getItemId(heldItem));
    }

    public static void registerCommand() {
        new PSSCommand("allowhoerightclick")
                .addAlias("allowhoerightclicks")
                .addAlias("ahrc")
                .setDescription("Allows hoe right clicks for a few minutes")
                .setRunnable((s, a) -> {
                    boolean canRightClickHoe = MathUtils.INSTANCE.onCooldown(MathematicalHoeRightClicks.lastAllowHoeRightClickTime, (long) (PartlySaneSkies.config.allowRightClickTime * 60L * 1000L));

                    if(canRightClickHoe){
                        IChatComponent message = new ChatComponentText(PartlySaneSkies.CHAT_PREFIX + "§bThe ability to right-click with a hoe has been §cdisabled§b again.\n§dClick this message or run /allowhoerightclick to allow right-clicks for " + PartlySaneSkies.config.allowRightClickTime + " again.");
                        message.getChatStyle().setChatClickEvent(new ClickEvent(Action.RUN_COMMAND, "/allowhoerightclick"));
                        PartlySaneSkies.minecraft.ingameGUI.getChatGUI().printChatMessage(message);
                        MathematicalHoeRightClicks.lastAllowHoeRightClickTime = 0;
                    } else {
                        IChatComponent message = new ChatComponentText(PartlySaneSkies.CHAT_PREFIX + "§bThe ability to right-click with a hoe has been §aenabled§b for " + PartlySaneSkies.config.allowRightClickTime + " minutes.\n§dClick this message or run /allowhoerightclick to disable right-clicks again.");
                        message.getChatStyle().setChatClickEvent(new ClickEvent(Action.RUN_COMMAND, "/allowhoerightclick"));
                        PartlySaneSkies.minecraft.ingameGUI.getChatGUI().printChatMessage(message);
                        MathematicalHoeRightClicks.lastAllowHoeRightClickTime = PartlySaneSkies.getTime();
                    }
                })
                .register();
    }

    @SubscribeEvent
    public void onClick(PlayerInteractEvent event) {
        if (!PartlySaneSkies.config.blockHoeRightClicks) {
            return;
        }

        if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK || event.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR) {

            if (MathUtils.INSTANCE.onCooldown(lastAllowHoeRightClickTime, (long) (PartlySaneSkies.config.allowRightClickTime * 60L * 1000L))) {
                return;
            }

            if (!isHoldingHoe()) {
                return;
            }


            if (!MathUtils.INSTANCE.onCooldown(lastMessageSendTime, 3000)) {
                IChatComponent message = new ChatComponentText(PartlySaneSkies.CHAT_PREFIX + "§8Right Clicks are disabled while holding a Mathematical Hoe\n§7Click this message or run /allowhoerightclick to allow right clicks for " + PartlySaneSkies.config.allowRightClickTime + " minutes.");
                message.getChatStyle().setChatClickEvent(new ClickEvent(Action.RUN_COMMAND, "/allowhoerightclick"));
                PartlySaneSkies.minecraft.ingameGUI.getChatGUI().printChatMessage(message);
                lastMessageSendTime = PartlySaneSkies.getTime();
            }

            event.setCanceled(true);
        }
    }
}
