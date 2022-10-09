package me.partlysanestudios.partlysaneskies.general;

import java.net.URI;
import java.net.URISyntaxException;

import me.partlysanestudios.partlysaneskies.Main;
import me.partlysanestudios.partlysaneskies.utils.Utils;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WikiArticleOpener {
    public static boolean isWaitingForArticle = false;
    public static NBTTagCompound getItemAttributes(ItemStack item) {
        return item.getTagCompound().getCompoundTag("ExtraAttributes");
    }

    public static String getItemId(ItemStack item) {
        return getItemAttributes(item).getString("id");
    }

    public static void getArticle(String id) {
        isWaitingForArticle = true;
        Main.minecraft.thePlayer.sendChatMessage("/wiki " + id);
    }

    @SubscribeEvent
    public void openArticle(ClientChatReceivedEvent e) {
        if(!isWaitingForArticle) {
            return;
        }
        if (Utils.removeColorCodes(e.message.getFormattedText()).contains("Invalid")) {
            isWaitingForArticle = false;
            return;
        }
        if(!Utils.removeColorCodes(e.message.getFormattedText()).contains("Click HERE")) {
            return;
        }
        
        isWaitingForArticle = false;
        String wikiLink = e.message.getChatStyle().getChatClickEvent().getValue();
        if(Main.config.openWikiAutomatically) {
            URI uri;
            try {
                uri = new URI(wikiLink);
                try {
                    Class<?> oclass = Class.forName("java.awt.Desktop");
                    Object object = oclass.getMethod("getDesktop", new Class[0]).invoke((Object)null, new Object[0]);
                    oclass.getMethod("browse", new Class[] {URI.class}).invoke(object, new Object[] {uri});
                }
                catch (Throwable throwable) {
                    Utils.sendClientMessage("Couldn\'t open link");
                    throwable.printStackTrace();
                }
            } catch (URISyntaxException except) {
                Utils.sendClientMessage("Couldn\'t open link");
                except.printStackTrace();
            }
        }
    }

    public static void keyDown() {
        if(!Main.isSkyblock()) {
            return;
        }
        ItemStack item;
        if (!(Main.minecraft.currentScreen instanceof GuiContainer)) {
            return;
        }
        GuiContainer container = (GuiContainer) Main.minecraft.currentScreen;
        Slot slot = container.getSlotUnderMouse();
        if(slot == null) return;
        item = slot.getStack();

        if(item == null) {
            return;
        }

        if (getItemId(item).equals("")) {
            return;
        }
        WikiArticleOpener.getArticle(WikiArticleOpener.getItemId(item));
    }
}
