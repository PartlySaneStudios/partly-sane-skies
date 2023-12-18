//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.data.pssdata;

import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import me.partlysanestudios.partlysaneskies.system.commands.PSSCommand;
import me.partlysanestudios.partlysaneskies.system.requests.Request;
import me.partlysanestudios.partlysaneskies.system.requests.RequestsManager;
import me.partlysanestudios.partlysaneskies.utils.ChatUtils;
import net.minecraft.util.ChatComponentText;

import java.net.MalformedURLException;
import java.util.HashMap;

public class PublicDataManager {
    private static HashMap<String, String> fileCache = new HashMap<>();
    private static final Lock lock = new Lock();


    public static String getRepoOwner() {
        if (PartlySaneSkies.config == null) {
            return "PartlySaneStudios";
        }
        return PartlySaneSkies.config.repoOwner;
    }

    public static String getRepoName() {
        if (PartlySaneSkies.config == null) {
            return "partly-sane-skies-public-data";
        }
        return PartlySaneSkies.config.repoName;
    }

    public static String getFile(String path) {
        String fixedPath = path;
        if (fixedPath.startsWith("/")) {
            fixedPath = fixedPath.substring(1);
        }

        if (fixedPath.endsWith("/")) {
            fixedPath = fixedPath.substring(0, fixedPath.length() - 1);
        }

        if (fileCache.containsKey(fixedPath)) {
            return fileCache.get(fixedPath);
        }

        try {
            RequestsManager.newRequest(new Request("https://raw.githubusercontent.com/" + getRepoOwner() + "/" + getRepoName() + "/main/data/" + path, r -> {
                if (!r.hasSucceeded()) {
                    lock.notifyAll();
                    return;
                }
                fileCache.put(path, r.getResponse());

                synchronized (lock) {
                    lock.notifyAll();
                }
            }));
        } catch (MalformedURLException e) {
            synchronized (lock) {
                lock.notifyAll();
            }
        }

        try {
            synchronized (lock) {
                lock.wait();
            }

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (!fileCache.containsKey(path)) {
            return "";
        }

        return fileCache.get(path);
    }
    public static void registerDataCommand(){
        new PSSCommand("updatepssdata")
                .addAlias("clearhashmap")
                .addAlias("clearpssdata")
                .setDescription("Clears your Partly Sane Studios data")
                .setRunnable((s, a) -> {
                    ChatComponentText chatcomponent = new ChatComponentText("§b§4-----------------------------------------------------§7" +
                            "\nData Refreshed" +
                            "\n§b§4-----------------------------------------------------§0"
                    );
                    fileCache.clear();
                    ChatUtils.INSTANCE.sendClientMessage(chatcomponent);
                }).register();
    }

    private static class Lock {

    }
}
