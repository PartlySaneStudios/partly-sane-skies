//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies;

import gg.essential.elementa.components.Window;
import me.partlysanestudios.partlysaneskies.system.Keybinds;
import me.partlysanestudios.partlysaneskies.system.commands.CommandManager;
import me.partlysanestudios.partlysaneskies.system.commands.PSSCommand;
import me.partlysanestudios.partlysaneskies.utils.ChatUtils;
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
                    IChatComponent message = new ChatComponentText(PartlySaneSkies.CHAT_PREFIX + ("§9Join the discord: https://discord.gg/" + PartlySaneSkies.discordCode));
                    // Sets the text to be clickable with a link
                    message.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.gg/" + PartlySaneSkies.discordCode));
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
                    ChatUtils.INSTANCE.sendClientMessage("§bOpening config menu...");

                    Window.Companion.enqueueRenderOperation(() -> PartlySaneSkies.config.openGui());
                })
                .register();
    }

    public static void registerPSSCommand() {
        new PSSCommand("pss")
                .setDescription("Prints help message and opens the config menu")
                .setRunnable((s, a) -> {
                    printHelpMessage();

                    ChatUtils.INSTANCE.sendClientMessage("§bOpening config menu...");

                    Window.Companion.enqueueRenderOperation(() -> PartlySaneSkies.config.openGui());
                }).register();
    }

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

    public static void registerCrepesCommand() {
        new PSSCommand("crepes")
                .addAlias("crêpes")
                .setDescription("Crepes!")
                .setRunnable((s, a) -> {
                    ChatComponentText chatComponent = new ChatComponentText("§0§m-----------------------------------------------------§0" +
                            "\n§6Ingredients:" +
                            "\n" +
                            "\n§f- 250g of wheat flour" +
                            "\n§f- 50g of butter" +
                            "\n§f- 50cl of milk" +
                            "\n§f- 10cl of water" +
                            "\n§f- 4 (big) eggs" +
                            "\n§f- 2 tablespoons of powdered sugar" +
                            "\n§f- 1 pinch of salt" +
                            "\n" +
                            "\n§f1) In a large mixing bowl, add the wheat flour, the 2 tablespoons of powdered sugar, and 1 pinch of salt" +
                            "\n§f2) While whisking the bowl, progressively pour in the milk and the water " +
                            "\n§f3) After, add in the (melted) butter and the eggs" +
                            "\n§f4) Leave the bowl in the fridge for 30-45 minutes" +
                            "\n§f5) Look up a tutorial on how to make crêpes on a pan" +
                            "\n§f6) Enjoy your (burnt) crêpes with something like Nutella, sugar, or marmalade" +
                            "\n" +
                            "\n§fSource: " +
                            "\n§5http://www.recettes-bretonnes.fr/crepe-bretonne/recette-crepe.html" +
                            "\n§0§m-----------------------------------------------------§0");
                    chatComponent.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL,"http://www.recettes-bretonnes.fr/crepe-bretonne/recette-crepe.html"));
                    ChatUtils.INSTANCE.sendClientMessage(chatComponent);
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
                        Window.Companion.enqueueRenderOperation(() -> PartlySaneSkies.config.openGui());
                        return;
                    }



                    printHelpMessage();

                }).register();
    }

    public static void printHelpMessage() {
        StringBuilder str = new StringBuilder("§3§m-----------------------------------------------------§r" +
                "\n" +
                "\n§b§l§nWelcome to Partly Sane Skies!§r" +
                "\nPartly Sane Skies is a mod developed by Su386 and FlagMaster. This mod aims to be a quality of life mod for Hypixel SkyBlock." +
                "\n" +
                "\n §6> Open the config: " +
                "\n    §6> §ePress " + Keyboard.getKeyName(Keybinds.configKey.getKeyCode()) + " or use /pssc" +
                "\n    §6> §eMost features are turned off by default so to use the mod, you will need to configure the settings" +
                "\n    §6> §eTo change the keybinding, press Esc, Options, Controls and scroll down to \"Partly Sane Skies\"." +
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

        ChatUtils.INSTANCE.sendClientMessage((str.toString()), true);

    }
}
