package me.partlysanestudios.partlysaneskies.modschecker;

import com.google.gson.Gson;
import me.partlysanestudios.partlysaneskies.system.commands.PSSCommand;
import me.partlysanestudios.partlysaneskies.system.requests.Request;
import me.partlysanestudios.partlysaneskies.system.requests.RequestsManager;
import me.partlysanestudios.partlysaneskies.utils.Utils;
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

public class ModChecker {

    public static void registerModCheckCommand() {
        new PSSCommand("modcheck", Collections.emptyList(), "Checks the mods in your mod folder if they are updated", (s, a) -> {
            new Thread(ModChecker::run).start();
        }).register();
    }

    private static final List<KnownMod> knownMods = new ArrayList<>();

//    static {
//
//        // Forge versions
//        knownMods.add(new KnownMod("Forge Mod Loader", "8.0.99.99", "596512ad5f12f95d8a3170321543d4455d23b8fe649c68580c5f828fe74f6668", true));
//        knownMods.add(new KnownMod("Minecraft Forge", "11.15.1.2318", "596512ad5f12f95d8a3170321543d4455d23b8fe649c68580c5f828fe74f6668", true));
//
//        // Mod loaders
//        knownMods.add(new KnownMod("ChatTriggers", "2.2.0", "3b077d3e99eb714d13e76826d75929807e8ab582023c59bc405d4bad67193254", true));
//        knownMods.add(new KnownMod("Essential", "1.0.0", "a70c02554a96faff60647eb204652a20b5551798506f1e4c0970daef401f782e", true));
//        knownMods.add(new KnownMod("OneConfig", "0.2.1-alpha181", "004fc41df0eaa5e5e5cdc1e50f66f3b7b85ab85b03b453898db97726e43dc472", true));
//
//        // SkyBlock Mods
//        knownMods.add(new KnownMod("Partly Sane Skies", "beta-v0.4.2", "24848f6bc47d0f147f9c6de09b754430a8d249ffec71e4f3a516e765dded6c4e", true));
//        knownMods.add(new KnownMod("Fancy Warp Menu", "0.8.0.69", "685e3234864170a433dd6717635170562a40d920f83f927a6ecfa228ae957e0d", true));
//        knownMods.add(new KnownMod("NotEnoughUpdates", "2.1.1+Alpha-22", "955a263c158e422d9b4f9e6cf1eca1a4f8518e3b195868d11ffa5977dc047253", true));
//        knownMods.add(new KnownMod("SkyblockAddons", "1.7.0", "5f3f023254f2e8216ed3657cd31858f46dc44eeede134b62d57af9d5cdfcda6a", true));
//        knownMods.add(new KnownMod("SkyHanni", "0.21.Beta.12", "f7514c894fe647873026d5e227327b181ccbfef7be433828b6eaeacdb1378395", true));
//        knownMods.add(new KnownMod("Skytils", "1.7.8", "bed2f299f5acebc4c1fbbc1a753d351a518b1ce8944be87403db73ff298fc778", true));
//        knownMods.add(new KnownMod("SoopyV2Forge", "1.1", "9623d49290b41f9d271bc3c53a69a48e77eda323614e92fc73ba288ff3a648f3", true));
//
//
//        // Non SkyBlock Mods
//
//        knownMods.add(new KnownMod("Patcher", "1.8.6", "4a134770967bf18c91345d39f70f49373adfcbce9a4adc8d3f4116c44f1ad3a9", true));
//        knownMods.add(new KnownMod("spark", "1.10.37", "477a2ecf0b53e2d9d9d5b620a2d7177e81c4204055126e608d8e89d9647887be", true));
//        knownMods.add(new KnownMod("Smooth Font", "1.8.9-1.15.2", "b0e057451f09f732759e161366abe188c04da25b126695184aa865df15b28098", true));
//
//        knownMods.add(new KnownMod("", "", "", true));
//        knownMods.add(new KnownMod("", "", "", true));
//    }

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
        int modsFound = 0;
        StringBuilder chatBuilder = new StringBuilder();
        StringBuilder debugBuilder = new StringBuilder();
        for (ModContainer container : Loader.instance().getActiveModList()) {
            String modId = container.getModId();

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

                // can fml has the same hash as "Minecraft Forge"
                if (fileName.equals("Forge Mod Loader")) {
                    if (hash.equals("596512ad5f12f95d8a3170321543d4455d23b8fe649c68580c5f828fe74f6668")) {
                        continue;
                    }
                }
                KnownMod mod = findMod(hash);
                if (mod == null) {
                    String message = "\n§c" + modName + " §7(" + fileName + ") §cunknown or outdated!";
                    chatBuilder.append(message);
                    debugBuilder.append("\nUnknown mod!");
                    debugBuilder.append("\nfileName: " + fileName);
                    debugBuilder.append("\nmodName: " + modName);
//                    debugBuilder.append("\nmodId: " + modId);
                    debugBuilder.append("\nhash: " + hash);
                    debugBuilder.append("\nversion: " + version);
                    debugBuilder.append("\ndisplayVersion: " + displayVersion);
                    debugBuilder.append("\n ");
                } else {
                    if (mod.latest) {
                        String message = "\n§a" + modName + " §7up to date (version §e" + mod.version + "§7)";
                        chatBuilder.append(message);
                    } else {
                        String latestVersion = findNewestMod(mod.name).version;
                        String message = "\n§e" + modName + " §7outdated §7(§e" + mod.version + " §7-> §e" + latestVersion + "§7)";
                        chatBuilder.append(message);
                    }
                }
                modsFound++;
            } catch (IOException e) {
                Utils.sendClientMessage("Error reading hash of mod " + fileName + "!", true);
                debugBuilder.append("\nerror reading hash!");
                debugBuilder.append("\nerror reading hash!");
                debugBuilder.append("\nfileName: " + fileName);
                debugBuilder.append("\nmodName: " + modName);
//                debugBuilder.append("\nmodId: " + modId);
                debugBuilder.append("\nversion: " + version);
                debugBuilder.append("\ndisplayVersion: " + displayVersion);
                debugBuilder.append("\n ");
            }
        }
        Utils.sendClientMessage(" \n§7Found " + modsFound + " mods:" + chatBuilder);
        Utils.copyStringToClipboard(debugBuilder.toString());
    }

    private static void loadModDataFromRepo() {

//        String userName = "PartlySaneStudios";
        String userName = "hannibal002";

//        String branchName = "main";
        String branchName = "mods";

        try {
            String url = "https://raw.githubusercontent.com/" + userName + "/partly-sane-skies-public-data" + "/" + branchName + "/data/constants/mods.json";
            RequestsManager.newRequest(new Request(url, request -> {
                read(new Gson().fromJson(request.getResponse(), ModDataJson.class));
            }));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void read(ModDataJson modData) {
        knownMods.clear();
        for (Map.Entry<String, ModDataJson.ModInfo> entry : modData.getMods().entrySet()) {
            String modName = entry.getKey();
            ModDataJson.ModInfo modInfo = entry.getValue();
            String download = modInfo.getDownload();

            KnownMod latest = null;
            for (Map.Entry<String, String> e : modInfo.getVersions().entrySet()) {
                String version = e.getKey();
                String hash = e.getValue();

                latest = new KnownMod(modName, version, hash);
                knownMods.add(latest);
            }
            if (latest != null) {
                latest.latest = true;
            }
        }
    }

    @NotNull
    private static KnownMod findNewestMod(String name) {
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
        for (KnownMod mod : knownMods) {
            if (mod.hash.equals(hash)) {
                return mod;
            }
        }

        return null;
    }

    @NotNull
    private static String generateHash(File file) throws IOException {
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
