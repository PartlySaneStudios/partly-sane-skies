package me.partlysanestudios.partlysaneskies;

import me.partlysanestudios.partlysaneskies.configgui.ConfigScreen;
import me.partlysanestudios.partlysaneskies.dropBanner.DropBannerDisplay;
import me.partlysanestudios.partlysaneskies.keybind.KeyInit;
// import me.partlysanestudios.partlysaneskies.rngdroptitle.DropBannerDisplay;
import me.partlysanestudios.partlysaneskies.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;


@Mod(modid = Main.MODID, version = Main.VERSION, name = Main.NAME)
public class Main
{
    public static final String MODID = "PartlySaneSkies";
    public static final String NAME = "Partly Sane Skies";
    public static final String VERSION = "1.0";

    public static boolean isHypixel;
    public static boolean isSkyblock;
    public static boolean isDebugMode;

    public static ConfigScreen config;
    public static Minecraft minecraft;
    
    @EventHandler
    public void init(FMLInitializationEvent evnt) {
        System.out.println("Hallo World!");
        Main.isHypixel = false;
        Main.isSkyblock = false;
        Main.isDebugMode = false;
        Main.minecraft = Minecraft.getMinecraft();

        Main.config = new ConfigScreen();

        MinecraftForge.EVENT_BUS.register(this);
        
        KeyInit.init();
        Utils.init();

 
        MinecraftForge.EVENT_BUS.register(new DropBannerDisplay());

        System.out.println("Partly Sane Skies has loaded.");
    }

    @SubscribeEvent
    public void joinServerEvent(ClientConnectedToServerEvent evnt) {
        if(minecraft.getCurrentServerData() == null || minecraft.getCurrentServerData().serverIP == null) return;
        if(minecraft.getCurrentServerData().serverIP.contains(".hypixel.net")) {
            Main.isHypixel = true;
        }
    }

    @SubscribeEvent
    public void clientTick(ClientTickEvent evnt) {
        if(KeyInit.debugKey.isPressed()) {
            Utils.visPrint(Utils.detectScoreboardName("SKYBLOCK"));
            Utils.visPrint(Main.minecraft.thePlayer.getWorldScoreboard().getObjectiveInDisplaySlot(1).getDisplayName());
            Main.isDebugMode = !Main.isDebugMode;
            Utils.visPrint("Debug mode: " + Main.isDebugMode);
        }
        if(KeyInit.configKey.isPressed()) {
            minecraft.displayGuiScreen(Main.config.gui());
        }

        try {
            Main.isSkyblock = Utils.detectScoreboardName("§lSKYBLOCK");
            Main.isHypixel = minecraft.getCurrentServerData().serverIP.contains(".hypixel.net");
        }
        catch(NullPointerException expt) {}
        finally {}
    }

    @SubscribeEvent
    public void chatAnalyzer(ClientChatReceivedEvent evnt) {
        
        if(Main.isDebugMode) System.out.println(evnt.message.getFormattedText());
    }
}
