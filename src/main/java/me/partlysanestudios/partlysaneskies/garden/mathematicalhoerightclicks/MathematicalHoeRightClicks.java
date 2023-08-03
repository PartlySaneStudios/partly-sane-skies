package me.partlysanestudios.partlysaneskies.garden.mathematicalhoerightclicks;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import me.partlysanestudios.partlysaneskies.system.requests.Request;
import me.partlysanestudios.partlysaneskies.system.requests.RequestsManager;
import me.partlysanestudios.partlysaneskies.utils.StringUtils;
import me.partlysanestudios.partlysaneskies.utils.Utils;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.ClickEvent.Action;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.IOException;
import java.util.ArrayList;

public class MathematicalHoeRightClicks {

    public static long lastMessageSendTime = 0;
    public static long lastAllowHoeRightClickTime = 0;

    private static ArrayList<String> hoes;
    public static void loadHoes() {
        try {
            RequestsManager.newRequest(new Request("https://raw.githubusercontent.com/PartlySaneStudios/partly-sane-skies-public-data/main/data/constants/mathematical_hoes.json", request -> {
                JsonArray array = new JsonParser().parse(request.getResponse()).getAsJsonObject().get("hoes").getAsJsonArray();

                hoes = new ArrayList<>();
                for (int i = 0; i < array.size(); i++) {
                    String hoe = array.get(i).getAsString();

                    hoes.add(hoe);
                }
            }));

        } catch (IOException e) {
            e.printStackTrace();
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
        return hoes.contains(Utils.getItemId(heldItem));

    }

    @SubscribeEvent
    public void onClick(PlayerInteractEvent event) {
        if (!PartlySaneSkies.config.blockHoeRightClicks) {
            return;
        }

        if (Utils.onCooldown(lastAllowHoeRightClickTime, (long) (PartlySaneSkies.config.allowRightClickTime * 60L * 1000L))) {
            return;
        }

        if (!isHoldingHoe()) {
            return;
        }


        if (!Utils.onCooldown(lastMessageSendTime, 3000)) {
            IChatComponent message = new ChatComponentText(PartlySaneSkies.CHAT_PREFIX + StringUtils.colorCodes("&8Right Clicks are disabled while holding a Mathematical Hoe\n&7Click this message or run /allowhoerightclick to allow right clicks for " + PartlySaneSkies.config.allowRightClickTime + " minutes."));
            message.getChatStyle().setChatClickEvent(new ClickEvent(Action.RUN_COMMAND, "/allowhoerightclick"));
            PartlySaneSkies.minecraft.ingameGUI.getChatGUI().printChatMessage(message);
            lastMessageSendTime = PartlySaneSkies.getTime();
        }

        event.setCanceled(true);
    }
}
