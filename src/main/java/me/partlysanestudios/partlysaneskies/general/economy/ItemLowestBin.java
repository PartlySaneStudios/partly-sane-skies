package me.partlysanestudios.partlysaneskies.general.economy;

import java.io.IOException;
import java.util.HashMap;

import com.google.gson.Gson;

import me.partlysanestudios.partlysaneskies.utils.Utils;
import net.minecraft.client.Minecraft;

public class ItemLowestBin {
    public static HashMap<String, Double> lowestBin;
    public static HashMap<String, Double> avgLowestBin;
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
                    HashMap<String, Double> map = (HashMap<String, Double>) new Gson().fromJson(request,
                            new HashMap<String, Double>().getClass());

                    lowestBin = map;
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    String request = Utils.getRequest("https://moulberry.codes/auction_averages_lbin/1day.json");
                    @SuppressWarnings("unchecked")
                    HashMap<String, Double> map = (HashMap<String, Double>) new Gson().fromJson(request,
                            new HashMap<String, Double>().getClass());

                    avgLowestBin = map;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        ;
    }

    public static boolean checkLastUpdate() {
        if (Minecraft.getSystemTime() < lastAhUpdateTime + 1000 * 60 * 3) {
            return true;
        }

        return false;
    }
}
