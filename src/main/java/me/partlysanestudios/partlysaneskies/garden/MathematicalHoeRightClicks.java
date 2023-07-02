package me.partlysanestudios.partlysaneskies.garden;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import me.partlysanestudios.partlysaneskies.utils.Utils;
import me.partlysanestudios.partlysaneskies.utils.requests.Request;
import me.partlysanestudios.partlysaneskies.utils.requests.RequestsManager;
import net.minecraft.event.ClickEvent;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;

import java.io.IOException;
import java.util.ArrayList;

public class MathematicalHoeRightClicks {


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
            throw new RuntimeException(e);
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

        if (!isHoldingHoe()) {
            return;
        }



        Utils.sendClientMessage("&8Right Clicks are disabled while holding a Mathematical Hoe");
        event.setCanceled(true);

    }
}
