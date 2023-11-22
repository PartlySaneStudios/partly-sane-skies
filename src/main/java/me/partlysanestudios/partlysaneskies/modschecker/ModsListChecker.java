//
// Written by hannibal002.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.modschecker;

import com.google.gson.Gson;
import me.partlysanestudios.partlysaneskies.system.commands.PSSCommand;
import me.partlysanestudios.partlysaneskies.system.requests.Request;
import me.partlysanestudios.partlysaneskies.system.requests.RequestsManager;
import me.partlysanestudios.partlysaneskies.utils.ChatUtils;
import me.partlysanestudios.partlysaneskies.utils.SystemUtils;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ModsListChecker {

    public static void registerModCheckCommand() {
        new PSSCommand("modcheck", Collections.emptyList(), "Checks the mods in your mod folder if they are updated", (s, a) -> {
            new Thread(ModsListChecker::run).start();
        }).register();
    }

    @Nullable
    private static List<KnownMod> knownMods;

    static class KnownMod {

        private final String name;
        private final String version;
        private final String hash;
        private boolean latest = false;

        KnownMod(String name, String version, String hash) {
            this.name = name;
            this.version = version;
            this.hash = hash;
        }
    }

    public static void run() {
        loadModDataFromRepo();
    }

    public static void run2() {
        int modsFound = 0;
        StringBuilder chatBuilder = new StringBuilder();
        StringBuilder debugBuilder = new StringBuilder();
        for (ModContainer container : Loader.instance().getActiveModList()) {

            String version = container.getVersion();
            String displayVersion = container.getDisplayVersion();


            String modName = container.getName();
            File modFile = container.getSource();
            String fileName = modFile.getName();

            // can not read hash of Minecraft Coder Pack or other stuff like Smooth Font Core
            if (fileName.equals("minecraft.jar")) {
                continue;
            }

            try {
                String hash = generateHash(modFile);

                // FML has the same hash as "Minecraft Forge", therefore ignroing it
                if (modName.equals("Forge Mod Loader")) {
                    if (hash.equals("596512ad5f12f95d8a3170321543d4455d23b8fe649c68580c5f828fe74f6668")) {
                        continue;
                    }
                }
                KnownMod mod = findMod(hash);
                if (mod == null) {
                    String message = "\n§c" + modName + " §7(" + fileName + ") is §cunknown §7or outdated!";
                    chatBuilder.append(message);
                    debugBuilder.append("\nUnknown mod!");
                    debugBuilder.append("\nfileName: " + fileName);
                    debugBuilder.append("\nmodName: " + modName);
                    debugBuilder.append("\nhash: " + hash);
                    debugBuilder.append("\nversion: " + version);
                    debugBuilder.append("\ndisplayVersion: " + displayVersion);
                    debugBuilder.append("\n ");
                } else {
                    if (mod.latest) {
                        String message = "\n§a" + mod.name + " §7is up to date";
                        chatBuilder.append(message);
                    } else {
                        String latestVersion = findNewestMod(mod.name).version;
                        String message = "\n§e" + mod.name + " §7is §coutdated §7(§e" + mod.version + " §7-> §e" + latestVersion + "§7)";
                        chatBuilder.append(message);
                    }
                }
                modsFound++;
            } catch (IOException e) {
                ChatUtils.INSTANCE.sendClientMessage("Error reading hash of mod " + fileName + "!", true);
                debugBuilder.append("\nerror reading hash!");
                debugBuilder.append("\nerror reading hash!");
                debugBuilder.append("\nfileName: " + fileName);
                debugBuilder.append("\nmodName: " + modName);
                debugBuilder.append("\nversion: " + version);
                debugBuilder.append("\ndisplayVersion: " + displayVersion);
                debugBuilder.append("\n ");
            }
        }
        ChatUtils.INSTANCE.sendClientMessage(" \n§7Found " + modsFound + " mods:" + chatBuilder);
        SystemUtils.INSTANCE.copyStringToClipboard(debugBuilder.toString());
    }

    private static void loadModDataFromRepo() {
        String userName = "PartlySaneStudios";
        String branchName = "main";

        try {
            String url = "https://raw.githubusercontent.com/" + userName +
                    "/partly-sane-skies-public-data" + "/" + branchName + "/data/constants/mods.json";
            RequestsManager.newRequest(new Request(url, request -> {
                knownMods = null;
                try {
                    knownMods = read(new Gson().fromJson(request.getResponse(), ModDataJson.class));
                    run2();
                } catch (Exception e) {
                    ChatUtils.INSTANCE.sendClientMessage("§cError reading the mod data from repo!");
                    e.printStackTrace();
                }
            }));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<KnownMod> read(ModDataJson modData) {
        List<KnownMod> list = new ArrayList<>();
        for (Map.Entry<String, ModDataJson.ModInfo> entry : modData.getMods().entrySet()) {
            String modName = entry.getKey();
            ModDataJson.ModInfo modInfo = entry.getValue();
            String download = modInfo.getDownload();

            KnownMod latest = null;
            for (Map.Entry<String, String> e : modInfo.getVersions().entrySet()) {
                String version = e.getKey();
                String hash = e.getValue();

                latest = new KnownMod(modName, version, hash);
                list.add(latest);
            }
            if (latest != null) {
                latest.latest = true;
            }
        }
        return list;
    }

    @NotNull
    private static KnownMod findNewestMod(String name) {
        if (knownMods == null) throw new IllegalStateException("known mods is null");
        for (KnownMod mod : knownMods) {
            if (mod.name.equals(name)) {
                if (mod.latest) {
                    return mod;
                }
            }
        }
        throw new IllegalStateException("Found no newest mod with the name `" + name + "'");
    }

    @Nullable
    private static KnownMod findMod(String hash) {
        if (knownMods == null) throw new IllegalStateException("known mods is null");
        for (KnownMod mod : knownMods) {
            if (mod.hash.equals(hash)) {
                return mod;
            }
        }

        return null;
    }

    @NotNull
    public static String generateHash(File file) throws IOException {
        try (FileInputStream stream = new FileInputStream(file)) {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = stream.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }

            byte[] md5Hash = digest.digest();
            StringBuilder md5HashStr = new StringBuilder();
            for (byte b : md5Hash) {
                md5HashStr.append(String.format("%02x", b));
            }

            return md5HashStr.toString();
        } catch (Exception e) {
            throw new IOException("Error generating MD5 hash: " + e.getMessage());
        }
    }
}
