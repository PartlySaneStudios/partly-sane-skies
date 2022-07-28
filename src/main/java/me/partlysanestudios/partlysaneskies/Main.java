package me.partlysanestudios.partlysaneskies;

import me.partlysanestudios.partlysaneskies.keybind.KeyInit;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
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

    
    public static Minecraft minecraft;
    
    @EventHandler
    public void init(FMLInitializationEvent event) {
        System.out.println("Hallo World!");
        isHypixel = false;
        isSkyblock = false;
        minecraft = Minecraft.getMinecraft();


        MinecraftForge.EVENT_BUS.register(this);
        KeyInit.init();


        System.out.println("Partly Sane Skies has loaded.");
    }

    @SubscribeEvent
    public void joinServerEvent(ClientConnectedToServerEvent e) {
        visPrint(minecraft.getCurrentServerData().serverIP);
        if(minecraft.getCurrentServerData().serverIP.contains(".hypixel.net")) {
            isHypixel = true;
            visPrint("CONNECTED TO HYPIXEL");
        }
    }
    @SubscribeEvent
    public void clientTick(ClientTickEvent e) {
        if(KeyInit.debugKey.isPressed()) {
            Main.visPrint(Main.detectScoreboardName("§lSKYBLOCK"));
            Main.visPrint(Main.minecraft.thePlayer.getWorldScoreboard().getObjectiveInDisplaySlot(1).getDisplayName());
        }

        try {
            isSkyblock = Main.detectScoreboardName("§lSKYBLOCK");
            isHypixel = minecraft.getCurrentServerData().serverIP.contains(".hypixel.net");
        }
        catch(NullPointerException expt) {

        }
        finally {

        }
    }
    
    @SubscribeEvent
    public void worldSwitch(WorldEvent.Load e) {
        
    }

    public static void visPrint(Object print) {
        System.out.println("\n\n\n" + print.toString() + "\n\n\n");
    }

    public static boolean detectScoreboardName(String desiredName) {
        String scoreboardName = minecraft.thePlayer.getWorldScoreboard().getObjectiveInDisplaySlot(1).getDisplayName();

        if(scoreboardName.contains(desiredName)) return true;

        return false;
    }
}
