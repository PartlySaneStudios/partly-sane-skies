//
// Written by FlagMaster and Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.features.commands

import me.partlysanestudios.partlysaneskies.commands.PSSCommand
import me.partlysanestudios.partlysaneskies.utils.ChatUtils.sendClientMessage
import net.minecraft.command.ICommandSender
import net.minecraft.event.ClickEvent
import net.minecraft.util.ChatComponentText

object Crepes {
    fun registerCrepesCommand() {
        PSSCommand("crepes")
            .addAlias("crêpes")
            .setDescription("Crepes!")
            .setRunnable { s: ICommandSender?, a: Array<String?>? ->
                val chatComponent = ChatComponentText(
                    """
                    §0§m-----------------------------------------------------§0
                    §6Ingredients:
                    
                    §f- 250g of wheat flour
                    §f- 50g of butter
                    §f- 50cl of milk
                    §f- 10cl of water
                    §f- 4 (big) eggs
                    §f- 2 tablespoons of powdered sugar
                    §f- 1 pinch of salt
                    
                    §f1) In a large mixing bowl, add the wheat flour, the 2 tablespoons of powdered sugar, and 1 pinch of salt
                    §f2) While whisking the bowl, progressively pour in the milk and the water 
                    §f3) After, add in the (melted) butter and the eggs
                    §f4) Leave the bowl in the fridge for 30-45 minutes
                    §f5) Look up a tutorial on how to make crêpes on a pan
                    §f6) Enjoy your (burnt) crêpes with something like Nutella, sugar, or marmalade
                    
                    §fSource: 
                    §5http://www.recettes-bretonnes.fr/crepe-bretonne/recette-crepe.html
                    §0§m-----------------------------------------------------§0
                    """.trimIndent()
                )
                chatComponent.chatStyle.setChatClickEvent(ClickEvent(ClickEvent.Action.OPEN_URL,"http://www.recettes-bretonnes.fr/crepe-bretonne/recette-crepe.html"))
                sendClientMessage(chatComponent)
            }.register()
    }
}
