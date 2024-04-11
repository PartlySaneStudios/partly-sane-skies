//
// Written by hannibal002.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.features.security.modschecker;

import com.google.gson.Gson;
import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import me.partlysanestudios.partlysaneskies.commands.PSSCommand;
import me.partlysanestudios.partlysaneskies.data.api.GetRequest;
import me.partlysanestudios.partlysaneskies.data.api.Request;
import me.partlysanestudios.partlysaneskies.data.api.RequestsManager;
import me.partlysanestudios.partlysaneskies.data.pssdata.PublicDataManager;
import me.partlysanestudios.partlysaneskies.features.debug.DebugKey;
import me.partlysanestudios.partlysaneskies.utils.ChatUtils;
import me.partlysanestudios.partlysaneskies.utils.SystemUtils;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion;

public class ModChecker {

    public static void registerModCheckCommand() {
        new PSSCommand("modcheck", new ArrayList<>(), "Checks the mods in your mod folder if they are updated",
                (s, a) -> {
                    new Thread(() -> {
                        if (a.length > 0) {
                            ChatUtils.INSTANCE.sendClientMessage("Loading... (using data from custom repository)");
                            loadModDataFromRepo(PublicDataManager.INSTANCE.getRepoOwner(),
                                    PublicDataManager.INSTANCE.getRepoName());
                        } else {
                            ChatUtils.INSTANCE.sendClientMessage("Loading...");
                            loadModDataFromRepo();
                        }
                    }).start();
                }).addAlias("modscheck").addAlias("modchecker").addAlias("modschecker").addAlias("pssmodscheck")
                .addAlias("pssmodchecker").addAlias("pssmodschecker").register();
    }

    @Nullable
    private static List<KnownMod> knownMods;
    private static boolean hasRunOnStartup;

    static class KnownMod {

        private final String id;
        private final String name;
        private final String downloadLink;
        private final String version;
        private final String hash;
        private boolean latest = false;

        KnownMod(String id, String name, String version, String downloadLink, String hash) {
            this.id = id;
            this.name = name;
            this.version = version;
            this.downloadLink = downloadLink;
            this.hash = hash;
        }
    }

    public static void runOnStartup() {
        new Thread(() -> {
            if (!PartlySaneSkies.Companion.getConfig().getCheckModsOnStartup()) {
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

    public static void run() {
        int modsFound = 0;
        IChatComponent chatMessage = new ChatComponentText("");

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

            // can not read hash of Minecraft Coder Pack or other stuff like Smooth Font
            // Core
            if (fileName.equals("minecraft.jar")) {
                continue;
            }

            try {
                String hash = generateHash(modFile);

                // FML has the same hash as "Minecraft Forge", therefore ignoring it
                if (modName.equals("Forge Mod Loader")) {
                    if (hash.equals("596512ad5f12f95d8a3170321543d4455d23b8fe649c68580c5f828fe74f6668")) {
                        continue;
                    }
                }
                KnownMod mod = findModFromHash(hash);
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

        chatMessage.appendSibling(new ChatComponentText(
                "\n§7Disclaimer: You should always exercise caution when downloading things from the internet. The PSS Mod Checker is not foolproof. Use at your own risk."));

        if (PartlySaneSkies.Companion.getConfig().getShowUpToDateMods()) {
            if (!knownMods.isEmpty()) {
                chatMessage.appendSibling(new ChatComponentText("\n\n§6Up to date Mods: (" + knownMods.size() + ")"));
            }
            for (ModContainer container : knownMods) {
                File modFile = container.getSource();
                String hash = null;
                try {
                    hash = generateHash(modFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                KnownMod mod = findModFromHash(hash);
                if (mod == null)
                    continue;

                IChatComponent message = new ChatComponentText("\n§a" + mod.name + " §7is up to date");
                if (SystemUtils.INSTANCE.isValidURL(mod.downloadLink)) {
                    message.getChatStyle()
                            .setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, mod.downloadLink));
                    message.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                            new ChatComponentText("Click for the official website for " + mod.name + "!")));
                }

                chatMessage.appendSibling(message);
            }
        }

        if (!outdatedMods.isEmpty()) {
            chatMessage.appendSibling(new ChatComponentText("\n\n§6Out of Date Mods: (" + outdatedMods.size() + ")"));
        }
        for (ModContainer container : outdatedMods) {
            File modFile = container.getSource();
            String hash = null;
            try {
                hash = generateHash(modFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            KnownMod mod = findModFromHash(hash);
            if (mod == null)
                continue;

            String latestVersion = findNewestModFromId(mod.id).version;
            IChatComponent message = new ChatComponentText(
                    "\n§e" + mod.name + " §7is §coutdated §7(§e" + mod.version + " §7-> §e" + latestVersion + "§7)");

            if (SystemUtils.INSTANCE.isValidURL(mod.downloadLink)) {
                message.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, mod.downloadLink));
                message.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ChatComponentText("Click for the official website for " + mod.name + "!")));
            }

            chatMessage.appendSibling(message);
        }

        if (!unknownMods.isEmpty()) {
            chatMessage.appendSibling(new ChatComponentText("\n\n§cUnknown Mods: (" + unknownMods.size() + ")"));
            chatMessage.appendSibling(new ChatComponentText("\n§7These mods have not been verified by PSS admins!"));
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

            ChatComponentText message = new ChatComponentText("\n§c" + modName + " §7(" + fileName + ") is §cunknown!");

            try {
                KnownMod mod = findNewestModFromId(container.getModId());

                message = new ChatComponentText("\n§c" + modName + " §7(" + fileName
                        + ") is §cunknown! §c(Verified version of " + mod.name + " found.)");

                if (SystemUtils.INSTANCE.isValidURL(mod.downloadLink)) {
                    message.getChatStyle()
                            .setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, mod.downloadLink));
                    message.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                            new ChatComponentText("Click for the official website for " + mod.name + "!")));
                }
            } catch (IllegalStateException e) {

            }

            chatMessage.appendSibling(message);

            debugBuilder.append("\n\"" + container.getModId() + "\": {");
            debugBuilder.append("\n    \"name\": \"" + modName + "\",");
            debugBuilder.append("\n    \"download\": \"" + container.getMetadata().url + "\",");
            debugBuilder.append("\n    \"versions\": {");
            debugBuilder.append("\n        \"" + container.getVersion() + "\": \"" + hash + "\"");
            debugBuilder.append("\n    },");
            debugBuilder.append("\n    \"betaVersions\": {");
            debugBuilder.append("\n        \"" + container.getVersion() + "\": \"" + hash + "\"");
            debugBuilder.append("\n    }");
            debugBuilder.append("\n},");
        }

        chatMessage.appendSibling(new ChatComponentText(
                "\n\n§9If you believe any of these mods may be a mistake, report it in the PSS discord! §7(/pssdiscord)"));

        if (DebugKey.INSTANCE.isDebugMode()) {
            ChatUtils.INSTANCE.sendClientMessage(
                    "§8Unknown Mods:\n" + insertCharacterAfterNewLine(debugBuilder.toString(), "§8") + "\n\n");
            SystemUtils.INSTANCE.copyStringToClipboard("```json\n" + debugBuilder.toString() + "\n```");
        }

        ChatUtils.INSTANCE.sendClientMessage(" \n§7Found " + modsFound + " mods:");
        PartlySaneSkies.Companion.getMinecraft().ingameGUI
                .getChatGUI()
                .printChatMessage(chatMessage);

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
        String url;
        if (Companion.getConfig().getUseGithubForPublicData()) {
            url = "https://raw.githubusercontent.com/" + userName + "/" + repoName + "/main/data/mods.json";
        } else {
            url = PartlySaneSkies.Companion.getConfig().getApiUrl() + "/v1/pss/publicdata?owner=" + userName + "&repo=" + repoName + "&path=/data/mods.json";
        }
        RequestsManager.INSTANCE.newRequest(new GetRequest(url, request -> {
            knownMods = null;
            try {
                knownMods = read(new Gson().fromJson(request.getResponse(), ModDataJson.class));
                run();
            } catch (Exception e) {
                ChatUtils.INSTANCE.sendClientMessage("§cError reading the mod data from repo!");
                e.printStackTrace();
            }
        }, false, false));
    }

    private static List<KnownMod> read(ModDataJson modData) {
        List<KnownMod> list = new ArrayList<>();
        for (Map.Entry<String, ModDataJson.ModInfo> entry : modData.getMods().entrySet()) {
            String modId = entry.getKey();
            ModDataJson.ModInfo modInfo = entry.getValue();
            String download = modInfo.getDownload();

            KnownMod latest = null;
            Map<String, String> versions;
            Map<String, String> betaVersions = new HashMap<>(); // Creates a variable to add all of the beta versions as
                                                                // outdated
            if (PartlySaneSkies.Companion.getConfig().getLookForBetaMods()) {
                versions = modInfo.getBetaVersions();
            } else {
                betaVersions = modInfo.getBetaVersions();

                versions = modInfo.getVersions();
            }
            for (Map.Entry<String, String> e : versions.entrySet()) {
                String version = e.getKey();
                String hash = e.getValue();

                latest = new KnownMod(modId, modInfo.name, version, download, hash);
                list.add(latest);
            }
            for (Map.Entry<String, String> e : betaVersions.entrySet()) {
                String version = e.getKey();
                String hash = e.getValue();

                list.add(new KnownMod(modId, modInfo.name, version, download, hash));

            }
            if (latest != null) {
                latest.latest = true;
            }
        }
        return list;
    }

    @NotNull
    private static KnownMod findNewestModFromId(String id) {
        if (knownMods == null)
            throw new IllegalStateException("known mods is null");
        for (KnownMod mod : knownMods) {
            if (mod.id.equals(id)) {
                if (mod.latest) {
                    return mod;
                }
            }
        }
        throw new IllegalStateException("Found no newest mod with the id `" + id + "'");
    }

    @Nullable
    private static KnownMod findModFromHash(String hash) {
        if (knownMods == null)
            throw new IllegalStateException("known mods is null");
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
