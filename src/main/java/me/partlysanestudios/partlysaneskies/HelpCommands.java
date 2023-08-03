package me.partlysanestudios.partlysaneskies;

import gg.essential.elementa.components.Window;
import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import me.partlysanestudios.partlysaneskies.system.Keybinds;
import me.partlysanestudios.partlysaneskies.system.commands.CommandManager;
import me.partlysanestudios.partlysaneskies.system.commands.PSSCommand;
import me.partlysanestudios.partlysaneskies.utils.StringUtils;
import me.partlysanestudios.partlysaneskies.utils.Utils;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import org.lwjgl.input.Keyboard;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class HelpCommands {

    public static void registerDiscordCommand() {
        new PSSCommand("discord")
                .addAlias("pssdisc")
                .addAlias("pssd")
                .addAlias("psdisc")
                .addAlias("psdiscord")
                .setDescription("Join the Partly Sane Studios Discord Server")
                .setRunnable((s, a) -> {
                    // Creates a new message with the correct text
                    IChatComponent message = new ChatComponentText(PartlySaneSkies.CHAT_PREFIX + StringUtils.colorCodes("&9Join the discord: https://discord.gg/v4PU3WeH7z"));
                    // Sets the text to be clickable with a link
                    message.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.gg/v4PU3WeH7z"));
                    // Prints message
                    PartlySaneSkies.minecraft.ingameGUI.getChatGUI().printChatMessage(message);
                })
                .register();
    }

    public static void registerConfigCommand() {
        new PSSCommand("pssconfig")
                .addAlias("pssc")
                .addAlias("pssconf")
                .setDescription("Opens the config menu")
                .setRunnable((s, a) -> {
                    Utils.sendClientMessage("&bOpening config menu...");

                    Window.Companion.enqueueRenderOperation(() -> PartlySaneSkies.config.openGui());
                })
                .register();
    }

    public static void registerPSSCommand() {
        new PSSCommand("pss")
                .setDescription("Prints help message and opens the config menu")
                .setRunnable((s, a) -> {
                    printHelpMessage();

                    Utils.sendClientMessage("&bOpening config menu...");

                    Window.Companion.enqueueRenderOperation(() -> PartlySaneSkies.config.openGui());
                }).register();
    }

    static List<String> configAliases = Arrays.asList("conf", "c", "config");
    public static void registerHelpCommand() {
        new PSSCommand("psshelp")
                .addAlias("pssh")
                .addAlias("pshelp")
                .addAlias("helpss")
                .addAlias("helppss")
                .addAlias("psshelp")
                .addAlias("helpihavenoideawhatpartlysaneskiesis")
                .setDescription("Show the Partly Sane Skies help message")
                .setRunnable((s, a) -> {
                    if (a.length > 0 && configAliases.contains(a[0].toLowerCase())) {
                        Utils.sendClientMessage("Opening config GUI...");
                        Window.Companion.enqueueRenderOperation(() -> PartlySaneSkies.config.openGui());
                        return;
                    }



                    printHelpMessage();

                }).register();
    }

    public static void printHelpMessage() {
        StringBuilder str = new StringBuilder("&3&m-----------------------------------------------------&r" +
                "\n" +
                "\n&b&l&nWelcome to Partly Sane Skies!&r" +
                "\nPartly Sane Skies is a mod developed by Su386 and FlagMaster. This mod aims to be a quality of life mod for Hypixel Skyblock." +
                "\n" +
                "\n &6> Open the config: " +
                "\n    &6> &ePress " + Keyboard.getKeyName(Keybinds.configKey.getKeyCode()) + " or use /pssc" +
                "\n    &6> &eMost features are turned off by default so to use the mod, you will need to configure the settings" +
                "\n    &6> &eTo change the keybinding, press Esc, Options, Video Settings, Controls, and scroll down to \"Partly Sane Skies\"." +
                "\n" +
                "\n" +
                "\n &1> Join the discord" +
                "\n    &1> &9To receive any future updates" +
                "\n    &1> &9/pssdiscord" +
                "\n" +
                "\n &5> Visit the GitHub" +
                "\n    &5> &dAll of the features wouldn't fit in this message, so check out the GitHub to see all of the features." +
                "\n&3&m-----------------------------------------------------&r" +
                "\n&dCommands:");


        for (Map.Entry<String, PSSCommand> en : CommandManager.commandList.entrySet()) {
            PSSCommand command = en.getValue();
            str.append("\n");
            str.append("\n &b> /").append(command.getName());

            for (String alias : command.getAliases()) {
                str.append(", /").append(alias);
            }

            str.append("\n&3    > ").append(command.getDescription());

        }

        str.append("\n&3&m-----------------------------------------------------&r");

        Utils.sendClientMessage(StringUtils.colorCodes(str.toString()), true);

    }
}
