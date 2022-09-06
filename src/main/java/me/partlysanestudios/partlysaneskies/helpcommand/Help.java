package me.partlysanestudios.partlysaneskies.helpcommand;

import org.lwjgl.input.Keyboard;

import me.partlysanestudios.partlysaneskies.Main;
import me.partlysanestudios.partlysaneskies.keybind.KeyInit;
import me.partlysanestudios.partlysaneskies.utils.Utils;
import net.minecraft.util.ChatComponentText;

public class Help {
    
    public static void printHelpMessage() {
        try {
            Main.minecraft.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(Utils.colorCodes("""
&3&m-----------------------------------------------------&r
&b&l&nWelcome to Partly Sane Skies!&r
Partly Sane Skies is a mod developed by Su386 and FlagMaster. This mod aims to be a quality of life mod for Hypixel Skyblock.

 &6> Open the config: 
    &6> &eDefault """ + Keyboard.getKeyName(KeyInit.helpKey.getKeyCode()) + """
    &6> &eMost features are turned off by default so to use the mod, you will need to configure the settings
    &6> &eTo change the keybinding, press Esc, Options, Video Settings, Controls, and scroll down to \"Partly Sane Skies\".

 &2> Try out the new Partly Sane Skies Party Manager
    &2> &aCommand: /pm

 &1> Join the discord
    &1> &9To recieve any future updates

 &5> Visit the GitHub
    &5> 7dAll of the features wouldn't fit in this message, so check out the GitHub to see all of the features.
&3&m-----------------------------------------------------&r
Commands:
 > /pss
    > Brings up the help screen

 > /pm, /partymanager
    > Opens the Party Manager

 > /skillup <username>, /su <username> 
    > Recommends which skill you should upgrade next.
&3&m-----------------------------------------------------&r
""")));
        }   
        catch(NullPointerException e) {}
        finally {}
    }
}
