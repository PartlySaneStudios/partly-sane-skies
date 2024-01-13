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
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.GuiScreenEvent.KeyboardInputEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import org.lwjgl.input.Keyboard;

public final class Keybinds {

    private final static String PSS_CATEGORY = "Partly Sane Skies";
    public static KeyBinding configKey;
    public static KeyBinding partyManagerKey;
    public static KeyBinding helpKey;
    public static KeyBinding wardrobeKeybind;
    public static KeyBinding petKeybind;
    public static KeyBinding craftKeybind;
    public static KeyBinding storageKeybind;
    public static KeyBinding wikiKeybind;
    public static KeyBinding favouritePetKeybind;
    public static KeyBinding allowHoeRightClickKeybind;


    public static void init() {
        // debugKey = registerKey("Debug", PSS_CATEGORY, Keyboard.KEY_F4);
        configKey = registerKey("Config", PSS_CATEGORY, Keyboard.KEY_F7);
        partyManagerKey = registerKey("Party Manager", PSS_CATEGORY, Keyboard.KEY_M);
        helpKey = registerKey("Help", PSS_CATEGORY, Keyboard.KEY_H);
        wardrobeKeybind = registerKey("Open Wardrobe Menu", PSS_CATEGORY, Keyboard.CHAR_NONE);
        petKeybind = registerKey("Open Pets Menu", PSS_CATEGORY, Keyboard.CHAR_NONE);
        craftKeybind = registerKey("Open Crafting Table", PSS_CATEGORY, Keyboard.CHAR_NONE);
        storageKeybind = registerKey("Open Storage Menu", PSS_CATEGORY, Keyboard.CHAR_NONE);
        wikiKeybind = registerKey("Open Wiki Article", PSS_CATEGORY, Keyboard.KEY_X);
        favouritePetKeybind = registerKey("Favourite Pet", PSS_CATEGORY, Keyboard.KEY_F);
        allowHoeRightClickKeybind = registerKey("Allow Hoe Right Click", PSS_CATEGORY, Keyboard.CHAR_NONE);
    }

    private static KeyBinding registerKey(String name, String category, int keycode) {
        final KeyBinding key = new KeyBinding(name, keycode, category);
        ClientRegistry.registerKeyBinding(key);
        return key;
    }

    @SubscribeEvent
    public void keybindWhileInGui(KeyboardInputEvent.Post event) {
        if (PartlySaneSkies.Companion.getConfig().debugKeybind.isActive()) {
            DebugKey.INSTANCE.onDebugKeyPress();
        }

        if (Keyboard.isKeyDown(wikiKeybind.getKeyCode())) {
            WikiArticleOpener.keyDown();
        }

        if (Keyboard.isKeyDown(favouritePetKeybind.getKeyCode())) {
            PetAlert.favouritePet();
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
            if (PartlySaneSkies.Companion.getMinecraft().currentScreen instanceof AuctionHouseGui ||
                    (PartlySaneSkies.Companion.getMinecraft().currentScreen instanceof GuiChest && AuctionHouseGui.Companion.isAhGui(MinecraftUtils.INSTANCE.getSeparateUpperLowerInventories(PartlySaneSkies.Companion.getMinecraft().currentScreen)[0]))) {

                MinecraftUtils.INSTANCE.clickOnSlot(46);
            }
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
            if (PartlySaneSkies.Companion.getMinecraft().currentScreen instanceof AuctionHouseGui ||
                    (PartlySaneSkies.Companion.getMinecraft().currentScreen instanceof GuiChest && AuctionHouseGui.Companion.isAhGui(MinecraftUtils.INSTANCE.getSeparateUpperLowerInventories(PartlySaneSkies.Companion.getMinecraft().currentScreen)[0]))) {

                MinecraftUtils.INSTANCE.clickOnSlot(53);
            }
        }
    }

    @SubscribeEvent
    public void checkKeyBinds(KeyInputEvent event) {
        if (PartlySaneSkies.Companion.getConfig().debugKeybind.isActive()) {
            DebugKey.INSTANCE.onDebugKeyPress();
        }
        
        if (configKey.isPressed()) {
            PartlySaneSkies.Companion.getConfig().openGui();
        }
        if (partyManagerKey.isPressed()) {
            PartyManager.startPartyManager();
        }
        if (helpKey.isPressed()) {
            HelpCommand.printHelpMessage();
        }
        if (craftKeybind.isPressed()) {
            PartlySaneSkies.Companion.getMinecraft().thePlayer.sendChatMessage("/craft");
        }
        if (petKeybind.isPressed()) {
            PartlySaneSkies.Companion.getMinecraft().thePlayer.sendChatMessage("/pets");
        }
        if (wardrobeKeybind.isPressed()) {
            PartlySaneSkies.Companion.getMinecraft().thePlayer.sendChatMessage("/wardrobe");
        }
        if (storageKeybind.isPressed()) {
            PartlySaneSkies.Companion.getMinecraft().thePlayer.sendChatMessage("/storage");
        }
        if (allowHoeRightClickKeybind.isPressed()) {
            boolean canRightClickHoe = MathUtils.INSTANCE.onCooldown(MathematicalHoeRightClicks.lastAllowHoeRightClickTime, (long) (PartlySaneSkies.Companion.getConfig().allowRightClickTime * 60L * 1000L));

            if(canRightClickHoe){
                IChatComponent message = new ChatComponentText(PartlySaneSkies.CHAT_PREFIX + ("§dThe ability to right-click with a hoe has been §cdisabled§d again.\n§dClick this message or run /allowhoerightclick to allow right-clicks for " + PartlySaneSkies.Companion.getConfig().allowRightClickTime + " again."));
                message.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/allowhoerightclick"));
                PartlySaneSkies.Companion.getMinecraft().ingameGUI.getChatGUI().printChatMessage(message);
                MathematicalHoeRightClicks.lastAllowHoeRightClickTime = 0;
            } else {
                IChatComponent message = new ChatComponentText(PartlySaneSkies.CHAT_PREFIX + ("§dThe ability to right-click with a hoe has been §aenabled§d for " + PartlySaneSkies.Companion.getConfig().allowRightClickTime + " minutes.\n§dClick this message or run /allowhoerightclick to disable right-clicks again."));
                message.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/allowhoerightclick"));
                PartlySaneSkies.Companion.getMinecraft().ingameGUI.getChatGUI().printChatMessage(message);
                MathematicalHoeRightClicks.lastAllowHoeRightClickTime = PartlySaneSkies.Companion.getTime();
            }
        }
    }
}
