package me.partlysanestudios.partlysaneskies.general.economy;

import java.io.IOException;
import java.util.HashMap;

import com.google.gson.Gson;

import me.partlysanestudios.partlysaneskies.utils.Utils;
import net.minecraft.client.Minecraft;

public class ItemLowestBin {
    public static HashMap<String, Float> lowestBin;
    public static long lastAhUpdateTime = 0;


    public static void runUpdater() {
        if (checkLastUpdate()) {
            lastAhUpdateTime = Minecraft.getSystemTime();
            updateAh();
        }
    }

    public static void updateAh() {
        new Thread() {
            @Override
            public void run() {
                try {
                    String request = Utils.getRequest("http://moulberry.codes/lowestbin.json");
                    @SuppressWarnings("unchecked")
                    HashMap<String, Float> map = (HashMap<String, Float>) new Gson().fromJson(request, lowestBin.getClass());

                    lowestBin = map;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();;
    }

    public static boolean checkLastUpdate() {
        if (Minecraft.getSystemTime() < lastAhUpdateTime + 1000*60*3) {
            return true;
        }

        return false;
    }
}
