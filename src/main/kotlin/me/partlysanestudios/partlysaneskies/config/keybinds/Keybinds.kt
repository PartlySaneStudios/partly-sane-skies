//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.config.keybinds

import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.features.commands.HelpCommand
import me.partlysanestudios.partlysaneskies.features.debug.DebugKey.onDebugKeyPress
import me.partlysanestudios.partlysaneskies.features.dungeons.party.partymanager.PartyManager
import me.partlysanestudios.partlysaneskies.features.economy.auctionhousemenu.AuctionHouseGui
import me.partlysanestudios.partlysaneskies.features.economy.auctionhousemenu.AuctionHouseGui.Companion.isAhGui
import me.partlysanestudios.partlysaneskies.features.farming.MathematicalHoeRightClicks
import me.partlysanestudios.partlysaneskies.features.information.WikiArticleOpener
import me.partlysanestudios.partlysaneskies.features.skills.PetAlert
import me.partlysanestudios.partlysaneskies.utils.MathUtils.onCooldown
import me.partlysanestudios.partlysaneskies.utils.MinecraftUtils.clickOnSlot
import me.partlysanestudios.partlysaneskies.utils.MinecraftUtils.getSeparateUpperLowerInventories
import net.minecraft.client.gui.inventory.GuiChest
import net.minecraft.event.ClickEvent
import net.minecraft.util.ChatComponentText
import net.minecraft.util.IChatComponent
import net.minecraftforge.client.event.GuiScreenEvent.KeyboardInputEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent
import org.lwjgl.input.Keyboard

object Keybinds {
    @SubscribeEvent
    fun keybindWhileInGui(event: KeyboardInputEvent.Post?) {
        if (PartlySaneSkies.config.dev.debugKeybind.isActive()) {
            onDebugKeyPress()
        }
        if (PartlySaneSkies.config.skyblock.wikiKeybind.isActive()) {
            WikiArticleOpener.keyDown()
        }
        if (PartlySaneSkies.config.skyblock.favouritePetKeybind.isActive()) {
            PetAlert.favouritePet()
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
            if (PartlySaneSkies.minecraft.currentScreen is AuctionHouseGui || PartlySaneSkies.minecraft.currentScreen is GuiChest && isAhGui(
                    PartlySaneSkies.minecraft.currentScreen.getSeparateUpperLowerInventories()[0]
                )
            ) {
                clickOnSlot(46)
            }
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
            if (PartlySaneSkies.minecraft.currentScreen is AuctionHouseGui || PartlySaneSkies.minecraft.currentScreen is GuiChest && isAhGui(
                    PartlySaneSkies.minecraft.currentScreen.getSeparateUpperLowerInventories()[0]
                )
            ) {
                clickOnSlot(53)
            }
        }
    }

    @SubscribeEvent
    fun checkKeyBinds(event: InputEvent.KeyInputEvent?) {
        if (PartlySaneSkies.config.dev.debugKeybind.isActive()) {
            onDebugKeyPress()
        }
        if (PartlySaneSkies.config.general.oneConfigKeybind.isActive()) {
            PartlySaneSkies.config.openGui()
        }
        if (PartlySaneSkies.config.dungeons.partyManagerKeybind.isActive()) {
            PartyManager.startPartyManager()
        }
        if (PartlySaneSkies.config.general.helpKeybind.isActive()) {
            HelpCommand.printHelpMessage()
        }
        if (PartlySaneSkies.config.skyblock.craftKeybind.isActive()) {
            PartlySaneSkies.minecraft.thePlayer.sendChatMessage("/craft")
        }
        if (PartlySaneSkies.config.skyblock.petKeybind.isActive()) {
            PartlySaneSkies.minecraft.thePlayer.sendChatMessage("/pets")
        }
        if (PartlySaneSkies.config.skyblock.wardrobeKeybind.isActive()) {
            PartlySaneSkies.minecraft.thePlayer.sendChatMessage("/wardrobe")
        }
        if (PartlySaneSkies.config.skyblock.storageKeybind.isActive()) {
            PartlySaneSkies.minecraft.thePlayer.sendChatMessage("/storage")
        }
        if (PartlySaneSkies.config.farming.allowHoeRightClickKeybind.isActive()) {
            val canRightClickHoe = onCooldown(
                MathematicalHoeRightClicks.lastAllowHoeRightClickTime,
                (PartlySaneSkies.config.farming.allowRightClickTime * 60L * 1000L).toLong()
            )
            if (canRightClickHoe) {
                val message: IChatComponent = ChatComponentText(
                    PartlySaneSkies.CHAT_PREFIX + """§dThe ability to right-click with a hoe has been §cdisabled§d again.
§dClick this message or run /allowhoerightclick to allow right-clicks for ${PartlySaneSkies.config.farming.allowRightClickTime} again."""
                )
                message.chatStyle.setChatClickEvent(ClickEvent(ClickEvent.Action.RUN_COMMAND, "/allowhoerightclick"))
                PartlySaneSkies.minecraft.ingameGUI.chatGUI.printChatMessage(message)
                MathematicalHoeRightClicks.lastAllowHoeRightClickTime = 0
            } else {
                val message: IChatComponent = ChatComponentText(
                    PartlySaneSkies.CHAT_PREFIX + """§dThe ability to right-click with a hoe has been §aenabled§d for ${PartlySaneSkies.config.farming.allowRightClickTime} minutes.
§dClick this message or run /allowhoerightclick to disable right-clicks again."""
                )
                message.chatStyle.setChatClickEvent(ClickEvent(ClickEvent.Action.RUN_COMMAND, "/allowhoerightclick"))
                PartlySaneSkies.minecraft.ingameGUI.chatGUI.printChatMessage(message)
                MathematicalHoeRightClicks.lastAllowHoeRightClickTime = PartlySaneSkies.time
            }
        }
    }
}