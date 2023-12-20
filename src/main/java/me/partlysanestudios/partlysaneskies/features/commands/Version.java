package me.partlysanestudios.partlysaneskies.features.commands;

import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import me.partlysanestudios.partlysaneskies.commands.PSSCommand;
import me.partlysanestudios.partlysaneskies.utils.ChatUtils;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;

public class Version {
    public static void registerVersionCommand(){
        new PSSCommand("partlysaneskiesversion")
                .addAlias("pssversion")
                .addAlias("pssv")
                .addAlias("partlysaneskiesv")
                .setDescription("Prints the version of Partly Sane Skies you are using")
                .setRunnable((s, a) -> {
                    ChatComponentText chatcomponent = new ChatComponentText("§b§m-----------------------------------------------------§0" +
                            "\n§b§lPartly Sane Skies Version:" +
                            "\n    §e" + PartlySaneSkies.VERSION +
                            (PartlySaneSkies.isLatestVersion() ? "\n§aYou are using the latest version of Partly Sane Skies!" : "\n§cYou are not using the latest version of Partly Sane Skies! Click here to download the newest version!") +
                            "\n§b§m-----------------------------------------------------§0"
                    );
                    if (!PartlySaneSkies.isLatestVersion()){
                        chatcomponent.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/PartlySaneStudios/partly-sane-skies/releases"));
                    }

                    ChatUtils.INSTANCE.sendClientMessage(chatcomponent);
                }).register();
    }
}
