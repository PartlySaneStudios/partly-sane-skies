//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.features.commands;

import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import me.partlysanestudios.partlysaneskies.commands.PSSCommand;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class Discord {
    public static void registerDiscordCommand() {
        new PSSCommand("discord")
                .addAlias("pssdisc")
                .addAlias("pssd")
                .addAlias("psdisc")
                .addAlias("psdiscord")
                .addAlias("pssdiscord")
                .addAlias("didcord")
                .setDescription("Join the Partly Sane Studios Discord Server")
                .setRunnable((s, a) -> {
                    // Creates a new message with the correct text
                    IChatComponent message = new ChatComponentText(PartlySaneSkies.CHAT_PREFIX + "§9Join the discord: https://discord.gg/" + PartlySaneSkies.Companion.getDiscordCode());
                    // Sets the text to be clickable with a link
                    message.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.gg/" + PartlySaneSkies.Companion.getDiscordCode()));
                    // Prints message
                    PartlySaneSkies.Companion.getMinecraft().ingameGUI.getChatGUI().printChatMessage(message);
                })
                .register();
    }
}
