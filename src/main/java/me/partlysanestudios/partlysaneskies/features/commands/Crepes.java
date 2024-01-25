//
// Written by FlagMaster.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.features.commands;

import me.partlysanestudios.partlysaneskies.commands.PSSCommand;
import me.partlysanestudios.partlysaneskies.utils.ChatUtils;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;

public class Crepes {
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
}
