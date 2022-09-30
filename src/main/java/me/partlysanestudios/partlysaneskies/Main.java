package me.partlysanestudios.partlysaneskies;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import gg.essential.elementa.ElementaVersion;
import me.partlysanestudios.partlysaneskies.dungeons.WatcherReady;
import me.partlysanestudios.partlysaneskies.dungeons.partymanager.PartyManager;
import me.partlysanestudios.partlysaneskies.dungeons.partymanager.PartyManagerCommand;
import me.partlysanestudios.partlysaneskies.dungeons.permpartyselector.PermPartyCommand;
import me.partlysanestudios.partlysaneskies.dungeons.permpartyselector.PermPartyManager;
import me.partlysanestudios.partlysaneskies.general.WormWarning;
import me.partlysanestudios.partlysaneskies.general.locationbanner.LocationBannerDisplay;
import me.partlysanestudios.partlysaneskies.general.rngdropbanner.DropBannerDisplay;
import me.partlysanestudios.partlysaneskies.general.skillupgrade.SkillUpgradeCommand;
import me.partlysanestudios.partlysaneskies.help.HelpCommand;
import me.partlysanestudios.partlysaneskies.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;


@Mod(modid = Main.MODID, version = Main.VERSION, name = Main.NAME)
public class Main
{
    public static final String MODID = "partlysaneskies";
    public static final String NAME = "Partly Sane Skies";
    public static final String VERSION = "1.0";
    public static String CHAT_PREFIX = Utils.colorCodes("&r&b&lPartly Sane Skies&r&7>> &r");

    public static ConfigScreen config;
    public static Minecraft minecraft;

    public static boolean isDebugMode;

    public static LocationBannerDisplay locationBannerDisplay;


    @EventHandler
    public void init(FMLInitializationEvent evnt) {
        System.out.println("Hallo World!");
        Main.isDebugMode = false;
        Main.minecraft = Minecraft.getMinecraft();

        Main.config = new ConfigScreen();
        new File("./config/partly-sane-skies/").mkdirs();
        try {
            PermPartyManager.permPartyMap = PermPartyManager.load();
            PermPartyManager.loadFavouriteParty();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            PartyManager.loadPersonalPlayerData();
        } catch (IOException e) {
            System.out.println("Partly Sane Skies: Unable to load player data.");
            e.printStackTrace();
        }

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new DropBannerDisplay());
        MinecraftForge.EVENT_BUS.register(new PartyManager());
        MinecraftForge.EVENT_BUS.register(new WatcherReady());
        MinecraftForge.EVENT_BUS.register(new WormWarning());
        MinecraftForge.EVENT_BUS.register(new PartlySaneSkiesMainMenu(ElementaVersion.V2));
        MinecraftForge.EVENT_BUS.register(new KeyInit());

        locationBannerDisplay = new LocationBannerDisplay();
        MinecraftForge.EVENT_BUS.register(locationBannerDisplay);

        ClientCommandHandler.instance.registerCommand(new PartyManagerCommand());
        ClientCommandHandler.instance.registerCommand(new HelpCommand());
        ClientCommandHandler.instance.registerCommand(new SkillUpgradeCommand());
        ClientCommandHandler.instance.registerCommand(new PermPartyCommand());
        KeyInit.init();
        Utils.init();

        System.out.println("Partly Sane Skies has loaded.");
    }


    @SubscribeEvent
    public void clientTick(ClientTickEvent evnt) {
        locationBannerDisplay.checkLocation();
    }


    @SubscribeEvent
    public void newApiKey(ClientChatReceivedEvent event) {
        if(event.message.getUnformattedText().startsWith("Your new API key is ")) { 
            config.apiKey = event.message.getUnformattedText().replace("Your new API key is ", "");
            Utils.sendClientMessage(Utils.colorCodes("Saved new API key!"));
            config.writeData();
        }
    }


    @SubscribeEvent
    public void chatAnalyzer(ClientChatReceivedEvent evnt) {
        if(Main.isDebugMode) System.out.println(evnt.message.getFormattedText());
    }


    public static String getScoreboardName() {
        String scoreboardName = minecraft.thePlayer.getWorldScoreboard().getObjectiveInDisplaySlot(1).getDisplayName();
        return Utils.removeColorCodes(scoreboardName);
    }


    public static List<String> getScoreboardLines() {
        Scoreboard scoreboard = minecraft.theWorld.getScoreboard();
        ScoreObjective objective = scoreboard.getObjectiveInDisplaySlot(1);
        Collection<Score> scoreCollection = scoreboard.getSortedScores(objective);

        List<String> scoreLines = new ArrayList<String>();
        for(Score score : scoreCollection) {
            scoreLines.add(ScorePlayerTeam.formatPlayerName(scoreboard.getPlayersTeam(score.getPlayerName()), score.getPlayerName()));
        }
        
        return scoreLines;
    }


    public static String getRegionName() {
        if(!isSkyblock()) {
            return "";
        }

        List<String> scoreboard = getScoreboardLines();

        String location = null;

        for(String line : scoreboard) {
            if (Utils.stripLeading(line).contains("⏣")) {
                location = Utils.stripLeading(line).replace("⏣", "");
                location = Utils.stripLeading(location);
            }
        }

        if (location == null) {
            return "";
        }

        return location;
    }


    public static boolean isSkyblock() {
        try {
            if(getScoreboardName().toLowerCase().contains("skyblock")) return true;
        }
        catch (NullPointerException expt) {
        }
        return false;
    }


    public static boolean isHypixel() {
        try {
            return minecraft.getCurrentServerData().serverIP.contains(".hypixel.net");
        }
        catch(NullPointerException expt) {}
        finally {}
        return false;
    }
}
