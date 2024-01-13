//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.config.keybinds;

import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import me.partlysanestudios.partlysaneskies.features.commands.HelpCommand;
import me.partlysanestudios.partlysaneskies.features.debug.DebugKey;
import me.partlysanestudios.partlysaneskies.features.dungeons.party.partymanager.PartyManager;
import me.partlysanestudios.partlysaneskies.features.economy.auctionhousemenu.AuctionHouseGui;
import me.partlysanestudios.partlysaneskies.features.farming.MathematicalHoeRightClicks;
import me.partlysanestudios.partlysaneskies.features.information.WikiArticleOpener;
import me.partlysanestudios.partlysaneskies.features.skills.PetAlert;
import me.partlysanestudios.partlysaneskies.utils.MathUtils;
import me.partlysanestudios.partlysaneskies.utils.MinecraftUtils;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.GuiScreenEvent.KeyboardInputEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import org.lwjgl.input.Keyboard;

public final class Keybinds {
    @SubscribeEvent
    public void keybindWhileInGui(KeyboardInputEvent.Post event) {
        if (PartlySaneSkies.config.debugKeybind.isActive()) {
            DebugKey.INSTANCE.onDebugKeyPress();
        }
        if (PartlySaneSkies.config.wikiKeybind.isActive()) {
            WikiArticleOpener.keyDown();
        }
        if (PartlySaneSkies.config.favouritePetKeybind.isActive()) {
            PetAlert.favouritePet();
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
            if (PartlySaneSkies.minecraft.currentScreen instanceof AuctionHouseGui ||
                    (PartlySaneSkies.minecraft.currentScreen instanceof GuiChest && AuctionHouseGui.Companion.isAhGui(MinecraftUtils.INSTANCE.getSeparateUpperLowerInventories(PartlySaneSkies.minecraft.currentScreen)[0]))) {

                MinecraftUtils.INSTANCE.clickOnSlot(46);
            }
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
            if (PartlySaneSkies.minecraft.currentScreen instanceof AuctionHouseGui ||
                    (PartlySaneSkies.minecraft.currentScreen instanceof GuiChest && AuctionHouseGui.Companion.isAhGui(MinecraftUtils.INSTANCE.getSeparateUpperLowerInventories(PartlySaneSkies.minecraft.currentScreen)[0]))) {

                MinecraftUtils.INSTANCE.clickOnSlot(53);
            }
        }
    }

    @SubscribeEvent
    public void checkKeyBinds(KeyInputEvent event) {
        if (PartlySaneSkies.config.debugKeybind.isActive()) {
            DebugKey.INSTANCE.onDebugKeyPress();
        }
        if (PartlySaneSkies.config.oneConfigKeybind.isActive()) {
            PartlySaneSkies.config.openGui();
        }
        if (PartlySaneSkies.config.partyManagerKeybind.isActive()) {
            PartyManager.startPartyManager();
        }
        if (PartlySaneSkies.config.helpKeybind.isActive()) {
            HelpCommand.printHelpMessage();
        }
        if (PartlySaneSkies.config.craftKeybind.isActive()) {
            PartlySaneSkies.minecraft.thePlayer.sendChatMessage("/craft");
        }
        if (PartlySaneSkies.config.petKeybind.isActive()) {
            PartlySaneSkies.minecraft.thePlayer.sendChatMessage("/pets");
        }
        if (PartlySaneSkies.config.wardrobeKeybind.isActive()) {
            PartlySaneSkies.minecraft.thePlayer.sendChatMessage("/wardrobe");
        }
        if (PartlySaneSkies.config.storageKeybind.isActive()) {
            PartlySaneSkies.minecraft.thePlayer.sendChatMessage("/storage");
        }
        if (PartlySaneSkies.config.allowHoeRightClickKeybind.isActive()) {
            boolean canRightClickHoe = MathUtils.INSTANCE.onCooldown(MathematicalHoeRightClicks.lastAllowHoeRightClickTime, (long) (PartlySaneSkies.config.allowRightClickTime * 60L * 1000L));

            if(canRightClickHoe){
                IChatComponent message = new ChatComponentText(PartlySaneSkies.CHAT_PREFIX + ("§dThe ability to right-click with a hoe has been §cdisabled§d again.\n§dClick this message or run /allowhoerightclick to allow right-clicks for " + PartlySaneSkies.config.allowRightClickTime + " again."));
                message.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/allowhoerightclick"));
                PartlySaneSkies.minecraft.ingameGUI.getChatGUI().printChatMessage(message);
                MathematicalHoeRightClicks.lastAllowHoeRightClickTime = 0;
            } else {
                IChatComponent message = new ChatComponentText(PartlySaneSkies.CHAT_PREFIX + ("§dThe ability to right-click with a hoe has been §aenabled§d for " + PartlySaneSkies.config.allowRightClickTime + " minutes.\n§dClick this message or run /allowhoerightclick to disable right-clicks again."));
                message.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/allowhoerightclick"));
                PartlySaneSkies.minecraft.ingameGUI.getChatGUI().printChatMessage(message);
                MathematicalHoeRightClicks.lastAllowHoeRightClickTime = PartlySaneSkies.getTime();
            }
        }
    }
}
