package me.partlysanestudios.partlysaneskies.help;

import org.lwjgl.input.Keyboard;

import me.partlysanestudios.partlysaneskies.Keybinds;
import me.partlysanestudios.partlysaneskies.Main;
import me.partlysanestudios.partlysaneskies.utils.Utils;
import net.minecraft.util.ChatComponentText;

public class Help {

    public static void printHelpMessage() {
        try {
            Main.minecraft.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(Utils.colorCodes(
                    "&3&m-----------------------------------------------------&r" +
                            "\n" +
                            "\n&b&l&nWelcome to Partly Sane Skies!&r" +
                            "\nPartly Sane Skies is a mod developed by Su386 and FlagMaster. This mod aims to be a quality of life mod for Hypixel Skyblock."
                            +
                            "\n" +
                            "\n &6> Open the config: " +
                            "\n    &6> &eDefault " + Keyboard.getKeyName(Keybinds.helpKey.getKeyCode()) +
                            "\n    &6> &eMost features are turned off by default so to use the mod, you will need to configure the settings"
                            +
                            "\n    &6> &eTo change the keybinding, press Esc, Options, Video Settings, Controls, and scroll down to \"Partly Sane Skies\"."
                            +
                            "\n" +
                            "\n &2> Try out the new Partly Sane Skies Party Manager" +
                            "\n    &2> &aCommand: /pm" +
                            "\n" +
                            "\n &1> Join the discord" +
                            "\n    &1> &9To recieve any future updates" +
                            "\n" +
                            "\n &5> Visit the GitHub" +
                            "\n    &5> &dAll of the features wouldn't fit in this message, so check out the GitHub to see all of the features."
                            +
                            "\n&3&m-----------------------------------------------------&r" +
                            "\nCommands:" +
                            "\n > /pss" +
                            "\n    > Brings up the help screen" +
                            "\n" +
                            "\n > /pm, /partymanager" +
                            "\n    > Opens the Party Manager" +
                            "\n" +
                            "\n > /permparty, /permp, /pp" +
                            "\n    > Allows you to auto party permanent parties" +
                            "\n" +
                            "\n > /skillup <username>, /su <username> " +
                            "\n    > Recommends which skill you should upgrade next." +
                            "\n&3&m-----------------------------------------------------&r")));
        } catch (NullPointerException e) {
        } finally {
        }
    }
}
