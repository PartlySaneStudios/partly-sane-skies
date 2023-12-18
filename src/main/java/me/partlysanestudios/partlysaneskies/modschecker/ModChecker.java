//
// Written by hannibal002.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.modschecker;

import com.google.gson.Gson;
import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import me.partlysanestudios.partlysaneskies.data.pssdata.PublicDataManager;
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

public class ModChecker {

    public static void registerModCheckCommand() {
        new PSSCommand("modcheck", new ArrayList<>(), "Checks the mods in your mod folder if they are updated", (s, a) -> {
            new Thread(() -> {
                if (a.length > 0) {
                    ChatUtils.INSTANCE.sendClientMessage("Loading... (using data from custom repository)");
                    loadModDataFromRepo(PublicDataManager.getRepoOwner(), PublicDataManager.getRepoName());
                } else {
                    ChatUtils.INSTANCE.sendClientMessage("Loading...");
                    loadModDataFromRepo();
                }
            }).start();
        }).addAlias("modscheck").addAlias("modchecker").addAlias("modschecker").addAlias("pssmodscheck").addAlias("pssmodchecker").addAlias("pssmodschecker").register();
    }

    @Nullable
    private static List<KnownMod> knownMods;
    private static boolean hasRunOnStartup;

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

    public static void runOnStartup() {
        new Thread(() -> {
            if (!PartlySaneSkies.config.checkModsOnStartup) {
                return;
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if (!hasRunOnStartup) {
                hasRunOnStartup = true;
                ChatUtils.INSTANCE.sendClientMessage("Loading...");
                loadModDataFromRepo();
            }
        }).start();

    }

    public static void run2() {
        int modsFound = 0;
        StringBuilder chatBuilder = new StringBuilder();
        StringBuilder debugBuilder = new StringBuilder();

        ArrayList<ModContainer> knownMods = new ArrayList<>();
        ArrayList<ModContainer> unknownMods = new ArrayList<>();
        ArrayList<ModContainer> outdatedMods = new ArrayList<>();

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
                    unknownMods.add(container);
                } else {
                    if (mod.latest) {
                        knownMods.add(container);
                    } else {
                        outdatedMods.add(container);
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

        chatBuilder.append("\n§7Disclaimer: You should always exercise caution when downloading things from the internet. The PSS Mod Checker is not foolproof. Use at your own risk.");

        if (!knownMods.isEmpty()) {
            chatBuilder.append("\n\n§6Up to date Mods: (" + knownMods.size() + ")");
        }
        for (ModContainer container : knownMods) {
            File modFile = container.getSource();
            String hash = null;
            try {
                hash = generateHash(modFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            KnownMod mod = findMod(hash);
            String message = "\n§a" + mod.name + " §7is up to date";
            chatBuilder.append(message);
        }

        if (!outdatedMods.isEmpty()) {
            chatBuilder.append("\n\n§6Out of Date Mods: (" + outdatedMods.size() + ")");
        }
        for (ModContainer container : outdatedMods) {
            File modFile = container.getSource();
            String hash = null;
            try {
                hash = generateHash(modFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            KnownMod mod = findMod(hash);

            String latestVersion = findNewestMod(mod.name).version;
            String message = "\n§e" + mod.name + " §7is §coutdated §7(§e" + mod.version + " §7-> §e" + latestVersion + "§7)";
            chatBuilder.append(message);
        }


        if (!unknownMods.isEmpty()) {
            chatBuilder.append("\n\n§cUnknown Mods: (" + unknownMods.size() + ")");
            chatBuilder.append("\n§7These mods have not been verified by PSS admins!");
        }
        for (ModContainer container : unknownMods) {
            String version = container.getVersion();
            String displayVersion = container.getDisplayVersion();


            String modName = container.getName();
            File modFile = container.getSource();
            String fileName = modFile.getName();

            String hash = null;
            try {
                hash = generateHash(modFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String message = "\n§c" + modName + " §7(" + fileName + ") is §cunknown!";
            chatBuilder.append(message);

            debugBuilder.append("\n\"" + container.getModId() + "\": {");
            debugBuilder.append("\n    \"name\": \"" + modName + "\",");
            debugBuilder.append("\n    \"download\": \"" + container.getMetadata().url + "\",");
            debugBuilder.append("\n    \"versions\": {");
            debugBuilder.append("\n        \"" + container.getVersion() + "\": \"" + hash + "\"");
            debugBuilder.append("\n    }");
            debugBuilder.append("\n},");
        }

        chatBuilder.append("\n\n§9If you believe any of these mods may be a mistake, report it in the PSS discord! §7(/pssdiscord)");


        if (PartlySaneSkies.config.debugMode) {
            ChatUtils.INSTANCE.sendClientMessage("§8Unknown Mods:\n" + insertCharacterAfterNewLine(debugBuilder.toString(), "§8") + "\n\n");
            SystemUtils.INSTANCE.copyStringToClipboard("```json\n"+ debugBuilder.toString() + "\n```");
        }

        ChatUtils.INSTANCE.sendClientMessage(" \n§7Found " + modsFound + " mods:" + chatBuilder);


    }
    private static String insertCharacterAfterNewLine(String originalString, String insertionChar) {
        StringBuilder stringBuilder = new StringBuilder();

        for (char c : originalString.toCharArray()) {
            stringBuilder.append(c);
            if (c == '\n') {
                stringBuilder.append(insertionChar);
            }
        }

        return stringBuilder.toString();
    }

    private static void loadModDataFromRepo() {
        loadModDataFromRepo("PartlySaneStudios", "partly-sane-skies-public-data");
    }

    private static void loadModDataFromRepo(String userName, String repoName) {
                try {
            String url = "https://raw.githubusercontent.com/" + userName +
                    "/" + repoName + "/main/data/mods.json";
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
