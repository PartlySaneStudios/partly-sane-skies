//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.features.commands;

import gg.essential.elementa.components.Window;
import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import me.partlysanestudios.partlysaneskies.commands.CommandManager;
import me.partlysanestudios.partlysaneskies.commands.PSSCommand;
import me.partlysanestudios.partlysaneskies.utils.ChatUtils;
import org.lwjgl.input.Keyboard;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class HelpCommand {

    public static void registerConfigCommand() {
        new PSSCommand("pssconfig")
                .addAlias("pssc")
                .addAlias("pssconf")
                .setDescription("Opens the config menu")
                .setRunnable((s, a) -> {
                    ChatUtils.INSTANCE.sendClientMessage("§bOpening config menu...");

                    Window.Companion.enqueueRenderOperation(() -> PartlySaneSkies.Companion.getConfig().openGui());
                })
                .register();
    }

    public static void registerPSSCommand() {
        new PSSCommand("pss")
                .setDescription("Prints help message and opens the config menu")
                .setRunnable((s, a) -> {
                    printHelpMessage();

                    ChatUtils.INSTANCE.sendClientMessage("§bOpening config menu...");

                    Window.Companion.enqueueRenderOperation(() -> PartlySaneSkies.Companion.getConfig().openGui());
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
                        ChatUtils.INSTANCE.sendClientMessage("Opening config GUI...");
                        Window.Companion.enqueueRenderOperation(() -> PartlySaneSkies.Companion.getConfig().openGui());
                        return;
                    }

                    printHelpMessage();

                }).register();
    }

    private static String getOneConfigKeyBindString() {
        if (PartlySaneSkies.Companion.getConfig().getOneConfigKeybind().getSize() == 0) {
            return Keyboard.getKeyName(Keyboard.CHAR_NONE);
        }

        String str = Keyboard.getKeyName(PartlySaneSkies.Companion.getConfig().getOneConfigKeybind().getKeyBinds().get(0));
        for (int i = 1; i < PartlySaneSkies.Companion.getConfig().getOneConfigKeybind().getSize(); i++) {
            str += " + " +  Keyboard.getKeyName(PartlySaneSkies.Companion.getConfig().getOneConfigKeybind().getKeyBinds().get(i));
        }

        return str;
    }

    public static void printHelpMessage() {
        StringBuilder str = new StringBuilder("§3§m-----------------------------------------------------§r" +
                "\n" +
                "\n§b§l§nWelcome to Partly Sane Skies!§r" +
                "\nPartly Sane Skies is a mod developed by Su386 and FlagMaster. This mod aims to be a quality of life mod for Hypixel SkyBlock." +
                "\n" +
                "\n §6> Open the config: " +
                "\n    §6> §ePress " + getOneConfigKeyBindString() + " or use /pssc" +
                "\n    §6> §eMost features are turned off by default so to use the mod, you will need to configure the settings" +
                "\n" +
                "\n" +
                "\n §1> Join the discord" +
                "\n    §1> §9To receive any future updates" +
                "\n    §1> §9/pssdiscord" +
                "\n" +
                "\n §5> Visit the GitHub" +
                "\n    §5> §dAll of the features wouldn't fit in this message, so check out the GitHub to see all of the features." +
                "\n§3§m-----------------------------------------------------§r" +
                "\n§dCommands:");


        for (Map.Entry<String, PSSCommand> en : CommandManager.commandList.entrySet()) {
            PSSCommand command = en.getValue();
            str.append("\n");
            str.append("\n §b> /").append(command.getName());

            for (String alias : command.getAliases()) {
                str.append(", /").append(alias);
            }

            str.append("\n§3    > ").append(command.getDescription());

        }

        str.append("\n§3§m-----------------------------------------------------§r");

        ChatUtils.INSTANCE.sendClientMessage(str.toString(), true);
    }
}
