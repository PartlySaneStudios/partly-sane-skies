package me.partlysanestudios.partlysaneskies;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.reflect.FieldUtils;

import gg.essential.elementa.ElementaVersion;
import me.partlysanestudios.partlysaneskies.dungeons.WatcherReady;
import me.partlysanestudios.partlysaneskies.dungeons.partymanager.PartyManager;
import me.partlysanestudios.partlysaneskies.dungeons.partymanager.PartyManagerCommand;
import me.partlysanestudios.partlysaneskies.dungeons.permpartyselector.PermPartyCommand;
import me.partlysanestudios.partlysaneskies.dungeons.permpartyselector.PermPartyManager;
import me.partlysanestudios.partlysaneskies.general.LocationBannerDisplay;
import me.partlysanestudios.partlysaneskies.general.NoCookieWarning;
import me.partlysanestudios.partlysaneskies.general.WikiArticleOpener;
import me.partlysanestudios.partlysaneskies.general.WormWarning;
import me.partlysanestudios.partlysaneskies.general.chatalerts.ChatAlertsCommand;
import me.partlysanestudios.partlysaneskies.general.chatalerts.ChatAlertsManager;
import me.partlysanestudios.partlysaneskies.general.economy.ItemLowestBin;
import me.partlysanestudios.partlysaneskies.general.economy.auctionhouse.AhManager;
import me.partlysanestudios.partlysaneskies.general.partyfriend.PartyFriendManager;
import me.partlysanestudios.partlysaneskies.general.partyfriend.PartyFriendManagerCommand;
import me.partlysanestudios.partlysaneskies.general.petalert.PetAlert;
import me.partlysanestudios.partlysaneskies.general.rngdropbanner.DropBannerDisplay;
import me.partlysanestudios.partlysaneskies.general.skillupgrade.SkillUpgradeCommand;
import me.partlysanestudios.partlysaneskies.general.skillupgrade.SkillUpgradeRecommendation;
import me.partlysanestudios.partlysaneskies.help.HelpCommand;
import me.partlysanestudios.partlysaneskies.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.inventory.IInventory;
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
public class Main {

    public static void main(String[] args) {

    }

    public static final String MODID = "partlysaneskies";
    public static final String NAME = "Partly Sane Skies";
    public static final String VERSION = "0.0.8";
    public static String CHAT_PREFIX = Utils.colorCodes("&r&b&lPartly Sane Skies&r&7>> &r");

    public static ConfigScreen config;
    public static Minecraft minecraft;

    public static boolean isDebugMode;

    private static LocationBannerDisplay locationBannerDisplay;

    public static Color BASE_DARK_COLOR = new Color(32, 33, 36);
    public static Color BASE_COLOR = new Color(42, 43, 46);
    public static Color BASE_LIGHT_COLOR = new Color(85, 85, 88);
    public static Color ACCENT_COLOR = new Color(1, 255, 255);
    public static Color DARK_ACCENT_COLOR = new Color(1, 122, 122);

    // Method runs at mod initialization
    @EventHandler
    public void init(FMLInitializationEvent evnt) {
        System.out.println("Hallo World!");
        Main.isDebugMode = false;
        Main.minecraft = Minecraft.getMinecraft();

        // Creates the partly-sane-skies directory if not already made
        new File("./config/partly-sane-skies/").mkdirs();
        
        // Loads the config files and options
        Main.config = new ConfigScreen();
        

        // Loads perm party data
        try {
            PermPartyManager.permPartyMap = PermPartyManager.load();
            PermPartyManager.loadFavouriteParty();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ItemLowestBin.updateAh();

        // Loads chat alerts data
        try {
            ChatAlertsManager.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Loads user player data for PartyManager
        try {
            PartyManager.loadPersonalPlayerData();
        } catch (IOException e) {
            System.out.println("Partly Sane Skies: Unable to load player data.");
            e.printStackTrace();
        }

        // Registers all of the events
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new DropBannerDisplay());
        MinecraftForge.EVENT_BUS.register(new PartyManager());
        MinecraftForge.EVENT_BUS.register(new WatcherReady());
        MinecraftForge.EVENT_BUS.register(new WormWarning());
        MinecraftForge.EVENT_BUS.register(new PartlySaneSkiesMainMenu(ElementaVersion.V2));
        MinecraftForge.EVENT_BUS.register(new Keybinds());
        MinecraftForge.EVENT_BUS.register(new PartyFriendManager());
        MinecraftForge.EVENT_BUS.register(new WikiArticleOpener());
        MinecraftForge.EVENT_BUS.register(new NoCookieWarning());
        locationBannerDisplay = new LocationBannerDisplay();
        MinecraftForge.EVENT_BUS.register(locationBannerDisplay);
        MinecraftForge.EVENT_BUS.register(new ChatAlertsManager());

        // Registers all client side commands
        ClientCommandHandler.instance.registerCommand(new PartyManagerCommand());
        ClientCommandHandler.instance.registerCommand(new HelpCommand());
        ClientCommandHandler.instance.registerCommand(new SkillUpgradeCommand());
        ClientCommandHandler.instance.registerCommand(new PermPartyCommand());
        ClientCommandHandler.instance.registerCommand(new PartyFriendManagerCommand());
        ClientCommandHandler.instance.registerCommand(new ChatAlertsCommand());

        // Initialises keybinds
        Keybinds.init();

        // Itialises Utils class
        Utils.init();

        // Initializes skill upgrade recommendation
        SkillUpgradeRecommendation.populateSkillMap();


        // Finished loading
        System.out.println("Partly Sane Skies has loaded.");
    }

    // Method runs every tick
    @SubscribeEvent
    public void clientTick(ClientTickEvent evnt) {
        // Checks if the current location is the same as the previous location for the location banner display
        locationBannerDisplay.checkLocation();

        // Updates item lowest bin price
        ItemLowestBin.runUpdater();

        // Checks if the current screen is the auciton house to run AHManager
        AhManager.runDisplayGuiCheck();

        // Checks if the player is collecting minions
        PetAlert.runPetAlert();
    }

    // Runs when the chat message starts with "Your new API key is "
    // Updates the API key to the nwe API key
    @SubscribeEvent
    public void newApiKey(ClientChatReceivedEvent event) {
        if (event.message.getUnformattedText().startsWith("Your new API key is ")) {
            config.apiKey = event.message.getUnformattedText().replace("Your new API key is ", "");
            Utils.sendClientMessage(Utils.colorCodes("Saved new API key!"));
            config.writeData();
        }
    }

    // Runs chat analyzer for debug mode
    @SubscribeEvent
    public void chatAnalyzer(ClientChatReceivedEvent evnt) {
        if (Main.isDebugMode)
            System.out.println(evnt.message.getFormattedText());
    }

    // Returns an array of length 2, where the 1st index is the upper invetory, 
    // and the 2nd index is the lower inventory.s]
    // Retuns null if there is no inventory, also returns null if there is no access to inventory
    public static IInventory[] getSeparateUpperLowerInventories(GuiScreen gui) {
        IInventory upperInventory;
        IInventory lowerInventory;
        try {
            upperInventory = (IInventory) FieldUtils.readDeclaredField(gui,
                    Utils.getDecodedFieldName("upperChestInventory"), true);
            lowerInventory = (IInventory) FieldUtils.readDeclaredField(gui,
                    Utils.getDecodedFieldName("lowerChestInventory"), true);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    
        return new IInventory[] { upperInventory, lowerInventory };
    }

    // Returns the name of the scoreboard without colorcodes
    public static String getScoreboardName() {
        String scoreboardName = minecraft.thePlayer.getWorldScoreboard().getObjectiveInDisplaySlot(1).getDisplayName();
        return Utils.removeColorCodes(scoreboardName);
    }

    // Runs when debug key is pressed
    public static void debugMode() {
        Main.isDebugMode = !Main.isDebugMode;
        Utils.visPrint("Debug mode: " + Main.isDebugMode);
        Utils.visPrint(PetAlert.parsePetNameFromEntity(PetAlert.getUsersPet("Su386").getName()));
    }

    // Returns a list of lines on the scoreboard,
    // where each line is a new entry
    public static List<String> getScoreboardLines() {
        Scoreboard scoreboard = minecraft.theWorld.getScoreboard();
        ScoreObjective objective = scoreboard.getObjectiveInDisplaySlot(1);
        Collection<Score> scoreCollection = scoreboard.getSortedScores(objective);

        List<String> scoreLines = new ArrayList<String>();
        for (Score score : scoreCollection) {
            scoreLines.add(ScorePlayerTeam.formatPlayerName(scoreboard.getPlayersTeam(score.getPlayerName()),
                    score.getPlayerName()));
        }

        return scoreLines;
    }

    // Gets the current skyblock region from the scoreboard
    public static String getRegionName() {
        if (!isSkyblock()) {
            return "";
        }

        List<String> scoreboard = getScoreboardLines();

        String location = null;

        for (String line : scoreboard) {
            if (Utils.stripLeading(line).contains("⏣")) {
                location = Utils.stripLeading(line).replace("⏣", "");
                location = Utils.stripLeading(location);
                break;
            }
        }

        if (location == null) {
            return "";
        }

        return location;
    }

    // Gets the amount of coins in your purse from the scoreboard
    public static long getCoins() {
        if (!isSkyblock()) {
            return 0l;
        }

        List<String> scoreboard = getScoreboardLines();

        String money = null;

        for (String line : scoreboard) {
            if (Utils.stripLeading(line).contains("Piggy:") || Utils.stripLeading(line).contains("Purse:")) {
                money = Utils.stripLeading(Utils.removeColorCodes(line)).replace("Piggy: ", "");
                money = Utils.stripLeading(Utils.removeColorCodes(line)).replace("Purse: ", "");
                money = Utils.stripLeading(money);
                money = money.replace(",", "");
                money = money.replaceAll("\\P{Print}", "");
                break;
            }
        }

        if (money == null) {
            return 0l;
        }
        try {
            return Long.parseLong(money);
        } catch (NumberFormatException event) {
            return 0;
        }
    }

    // Returns if the current gamemode is skyblock
    public static boolean isSkyblock() {
        try {
            if (getScoreboardName().toLowerCase().contains("skyblock")) {
                return true;
            }
        } catch (NullPointerException expt) {
            return false;
        }
        return false;
    }

    // Returns if the current server is hypixel
    public static boolean isHypixel() {
        try {
            return minecraft.getCurrentServerData().serverIP.contains(".hypixel.net");
        } catch (NullPointerException expt) {
        } finally {
        }
        return false;
    }
}
