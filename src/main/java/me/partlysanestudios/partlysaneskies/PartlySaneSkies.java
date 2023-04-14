/* 
 * 
 * Written by Su386.
 * See LICENSE for copright and license notices.
 * 
 * 
 * Partly Sane Skies would not be possible with out the help of these projects:
 * (see CREDITS.md for more information)
 * Minecraft Forge
 * Skytils
 * Not Enough Updates
 * GSON
 * Elementa
 * Vigilance
 * OneConfig
 * SkyCrypt
 * 
 */

package me.partlysanestudios.partlysaneskies;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.reflect.FieldUtils;

import gg.essential.elementa.ElementaVersion;
import me.partlysanestudios.partlysaneskies.auctionhouse.AhManager;
import me.partlysanestudios.partlysaneskies.chatalerts.ChatAlertsCommand;
import me.partlysanestudios.partlysaneskies.chatalerts.ChatAlertsManager;
import me.partlysanestudios.partlysaneskies.dungeons.PlayerRating;
import me.partlysanestudios.partlysaneskies.dungeons.WatcherReady;
import me.partlysanestudios.partlysaneskies.dungeons.partymanager.PartyManager;
import me.partlysanestudios.partlysaneskies.dungeons.partymanager.PartyManagerCommand;
import me.partlysanestudios.partlysaneskies.dungeons.permpartyselector.PermPartyCommand;
import me.partlysanestudios.partlysaneskies.dungeons.permpartyselector.PermPartyManager;
import me.partlysanestudios.partlysaneskies.economy.BitsShopValue;
import me.partlysanestudios.partlysaneskies.garden.CompostValue;
import me.partlysanestudios.partlysaneskies.garden.GardenTradeValue;
import me.partlysanestudios.partlysaneskies.garden.SkymartValue;
import me.partlysanestudios.partlysaneskies.garden.endoffarmnotifer.CreateRangeCommand;
import me.partlysanestudios.partlysaneskies.garden.endoffarmnotifer.EndOfFarmNotfier;
import me.partlysanestudios.partlysaneskies.garden.endoffarmnotifer.Pos1Command;
import me.partlysanestudios.partlysaneskies.garden.endoffarmnotifer.Pos2Command;
import me.partlysanestudios.partlysaneskies.garden.endoffarmnotifer.RangeCommand;
import me.partlysanestudios.partlysaneskies.help.ConfigCommand;
import me.partlysanestudios.partlysaneskies.help.DiscordCommand;
import me.partlysanestudios.partlysaneskies.help.HelpCommand;
import me.partlysanestudios.partlysaneskies.partyfriend.PartyFriendManager;
import me.partlysanestudios.partlysaneskies.partyfriend.PartyFriendManagerCommand;
import me.partlysanestudios.partlysaneskies.petalert.PetAlert;
import me.partlysanestudios.partlysaneskies.petalert.PetAlertMuteCommand;
import me.partlysanestudios.partlysaneskies.rngdropbanner.DropBannerDisplay;
import me.partlysanestudios.partlysaneskies.skillupgrade.SkillUpgradeCommand;
import me.partlysanestudios.partlysaneskies.skillupgrade.SkillUpgradeRecommendation;
import me.partlysanestudios.partlysaneskies.utils.StringUtils;
import me.partlysanestudios.partlysaneskies.utils.Utils;
import me.partlysanestudios.partlysaneskies.utils.requests.Request;
import me.partlysanestudios.partlysaneskies.utils.requests.RequestsManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.inventory.IInventory;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

@Mod(modid = PartlySaneSkies.MODID, version = PartlySaneSkies.VERSION, name = PartlySaneSkies.NAME)
public class PartlySaneSkies {

    public static void main(String[] args) throws IOException {
        
    }

    public static final String MODID = "partlysaneskies";
    public static final String NAME = "Partly Sane Skies";
    public static final String VERSION = "beta-v0.2.1";
    public static final String CHAT_PREFIX = StringUtils.colorCodes("&r&b&lPartly Sane Skies&r&7>> &r");
    public static final boolean IS_LEGACY_VERSION = false;
    public static String discordCode = "v4PU3WeH7z";

    public static OneConfigScreen config;
    public static Minecraft minecraft;

    public static boolean isDebugMode;

    private static LocationBannerDisplay locationBannerDisplay;

    public static final Color BASE_DARK_COLOR = new Color(32, 33, 36);
    public static final Color BASE_COLOR = new Color(42, 43, 46);
    public static final Color BASE_LIGHT_COLOR = new Color(85, 85, 88);
    public static final Color ACCENT_COLOR = new Color(1, 255, 255);
    public static final Color DARK_ACCENT_COLOR = new Color(1, 122, 122);
    // Names of all of the ranks to remove from people's names
    public static final String[] RANK_NAMES = { "[VIP]", "[VIP+]", "[MVP]", "[MVP+]", "[MVP++]", "[YOUTUBE]", "[MOJANG]",
            "[EVENTS]", "[MCP]", "[PIG]", "[PIG+]", "[PIG++]", "[PIG+++]", "[GM]", "[ADMIN]", "[OWNER]", "[NPC]" };

    // Method runs at mod initialization
    @EventHandler
    public void init(FMLInitializationEvent evnt) {
        System.out.println("Hallo World!");
        PartlySaneSkies.isDebugMode = false;
        PartlySaneSkies.minecraft = Minecraft.getMinecraft();

        // Creates the partly-sane-skies directory if not already made
        new File("./config/partly-sane-skies/").mkdirs();
        
        // Loads the config files and options
        PartlySaneSkies.config = new OneConfigScreen();    
        Request mainMenuRequest = null;
        try {
            mainMenuRequest = new Request("https://raw.githubusercontent.com/PartlySaneStudios/partly-sane-skies-public-data/main/data/main_menu.json", s -> {
                CustomMainMenu.setMainMenuInfo(s.getResponse());
            });
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        RequestsManager.newRequest(mainMenuRequest);
        trackLoad();
        RequestsManager.run();

        new Thread() {
            @Override
            public void run() {
                // Loads perm party data
                try {
                    PermPartyManager.load();
                    PermPartyManager.loadFavouriteParty();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                

                // Loads chat alerts data
                try {
                    ChatAlertsManager.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    EndOfFarmNotfier.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        
        }.start();

        

        // Registers all of the events
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new DropBannerDisplay());
        MinecraftForge.EVENT_BUS.register(new PartyManager());
        MinecraftForge.EVENT_BUS.register(new WatcherReady());
        MinecraftForge.EVENT_BUS.register(new WormWarning());
        MinecraftForge.EVENT_BUS.register(new CustomMainMenu(ElementaVersion.V2));
        MinecraftForge.EVENT_BUS.register(new Keybinds());
        MinecraftForge.EVENT_BUS.register(new PartyFriendManager());
        MinecraftForge.EVENT_BUS.register(new WikiArticleOpener());
        MinecraftForge.EVENT_BUS.register(new NoCookieWarning());
        locationBannerDisplay = new LocationBannerDisplay();
        MinecraftForge.EVENT_BUS.register(locationBannerDisplay);
        MinecraftForge.EVENT_BUS.register(new ChatAlertsManager());
        MinecraftForge.EVENT_BUS.register(new GardenTradeValue());
        MinecraftForge.EVENT_BUS.register(new ChatColors());
        MinecraftForge.EVENT_BUS.register(new CompostValue());
        MinecraftForge.EVENT_BUS.register(new EnhancedSound());
        MinecraftForge.EVENT_BUS.register(new BitsShopValue());
        MinecraftForge.EVENT_BUS.register(new PlayerRating());
        MinecraftForge.EVENT_BUS.register(new SkymartValue());
        MinecraftForge.EVENT_BUS.register(new EndOfFarmNotfier());

        // Registers all client side commands
        ClientCommandHandler.instance.registerCommand(new PartyManagerCommand());
        ClientCommandHandler.instance.registerCommand(new HelpCommand());
        ClientCommandHandler.instance.registerCommand(new SkillUpgradeCommand());
        ClientCommandHandler.instance.registerCommand(new PermPartyCommand());
        ClientCommandHandler.instance.registerCommand(new PartyFriendManagerCommand());
        ClientCommandHandler.instance.registerCommand(new ChatAlertsCommand());
        ClientCommandHandler.instance.registerCommand(new PetAlertMuteCommand());
        ClientCommandHandler.instance.registerCommand(new DiscordCommand());
        ClientCommandHandler.instance.registerCommand(new ConfigCommand());
        ClientCommandHandler.instance.registerCommand(new CreateRangeCommand());
        ClientCommandHandler.instance.registerCommand(new Pos2Command());
        ClientCommandHandler.instance.registerCommand(new Pos1Command());
        ClientCommandHandler.instance.registerCommand(new RangeCommand());

        // Initialises keybinds
        Keybinds.init();

        // Itialises Utils class
        Utils.init();


        // Initializes skill upgrade recommendation
        SkillUpgradeRecommendation.populateSkillMap();

        // API Calls
        PlayerRating.initPatterns();

        try {
            SkyblockItem.init();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            SkyblockItem.initBitValues();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SkyblockItem.updateAll();
        CompostValue.init();
        try {
            SkymartValue.initCopperValues();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Loads user player data for PartyManager
        try {
            PartyManager.loadPlayerData(PartlySaneSkies.minecraft.getSession().getUsername());
        } catch (IOException e) {
            System.out.println("Partly Sane Skies: Unable to load player data.");
            e.printStackTrace();
        }


        
        
        // Finished loading
        System.out.println("Partly Sane Skies has loaded.");
    }

    // Method runs every tick
    @SubscribeEvent
    public void clientTick(ClientTickEvent evnt) {
        // Runs the request manager
        RequestsManager.run();

        // Checks if the current location is the same as the previous location for the location banner display
        locationBannerDisplay.checkLocation();
        // Checks if the current screen is the auciton house to run AHManager
        AhManager.runDisplayGuiCheck();

        SkyblockItem.runUpdater();

        // Checks if the player is collecting minions
        PetAlert.runPetAlert();

        EndOfFarmNotfier.run();
        config.resetBrokenStrings();
    }

    // Runs when the chat message starts with "Your new API key is "
    // Updates the API key to the nwe API key
    @SubscribeEvent
    public void newApiKey(ClientChatReceivedEvent event) {
        if (event.message.getUnformattedText().startsWith("Your new API key is ")) {
            config.apiKey = event.message.getUnformattedText().replace("Your new API key is ", "");
            Utils.sendClientMessage(StringUtils.colorCodes("Saved new API key!"));
            config.save();
        }
    }

    // Runs chat analyzer for debug mode
    @SubscribeEvent
    public void chatAnalyzer(ClientChatReceivedEvent evnt) {
        if (PartlySaneSkies.isDebugMode)
            System.out.println(evnt.message.getFormattedText());
    }

    @SubscribeEvent
    public void world(WorldEvent.Load event) {
        // Code that is supposed to be here is dead code so removed on this branch
        // Code that is supposed to go here:
        // https://github.com/PartlySaneStudios/partly-sane-skies/blob/essential-based/src/main/java/me/partlysanestudios/partlysaneskies/PartlySaneSkies.java#LL303C5-L327C10
    }

    @SubscribeEvent
    public void onClientConnectedToServer(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        if (!CustomMainMenu.latestVersion.equals(VERSION)) {
            new Thread(() -> {
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                
                Utils.sendClientMessage("&b--------------------------------------------------", true);

                Utils.sendClientMessage("&cWe have detected a new version of Partly Sane Skies.");
                
                ChatComponentText skyclientMessage = new ChatComponentText(StringUtils.colorCodes("&aIf you are using Skyclient, make sure you update when prompted."));
                PartlySaneSkies.minecraft.ingameGUI
                        .getChatGUI()
                        .printChatMessage(skyclientMessage);
            
                ChatComponentText githubMessage = new ChatComponentText(StringUtils.colorCodes("&9If you are not using Skyclient, click here go to the github and download the latest version."));
                githubMessage.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/PartlySaneStudios/partly-sane-skies/releases"));
                githubMessage.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText("Click here to open the downloads page")));
                PartlySaneSkies.minecraft.ingameGUI
                        .getChatGUI()
                        .printChatMessage(githubMessage);
                
                        Utils.sendClientMessage("&b--------------------------------------------------", true);
            }).start();
        }
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
        return StringUtils.removeColorCodes(scoreboardName);
    }

    // Runs when debug key is pressed
    // public static void debugMode() {
    //     PartlySaneSkies.isDebugMode = !PartlySaneSkies.isDebugMode;
    //     Utils.sendClientMessage("Debug mode: " + PartlySaneSkies.isDebugMode);
    // }

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


    // Returns the time in miliseconds
    public static long getTime() {
        return System.currentTimeMillis();
    }

    // Gets the current skyblock region from the scoreboard
    public static String getRegionName() {
        if (!isSkyblock()) {
            return "";
        }

        List<String> scoreboard = getScoreboardLines();

        String location = null;

        for (String line : scoreboard) {
            if (StringUtils.stripLeading(line).contains("⏣")) {
                location = StringUtils.stripLeading(line).replace("⏣", "");
                location = StringUtils.stripLeading(location);
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
            if (StringUtils.stripLeading(line).contains("Piggy:") || StringUtils.stripLeading(line).contains("Purse:")) {
                money = StringUtils.stripLeading(StringUtils.removeColorCodes(line)).replace("Piggy: ", "");
                money = StringUtils.stripLeading(StringUtils.removeColorCodes(line)).replace("Purse: ", "");
                money = StringUtils.stripLeading(money);
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

    // Gets the amount of bits from the scoreboard
    public static long getBits() {
        if (!isSkyblock()) {
            return 0l;
        }

        List<String> scoreboard = getScoreboardLines();

        String bits = null;

        for (String line : scoreboard) {
            if (StringUtils.stripLeading(line).contains("Bits:")) {
                bits = StringUtils.stripLeading(StringUtils.removeColorCodes(line)).replace("Bits: ", "");
                bits = StringUtils.stripLeading(bits);
                bits = bits.replace(",", "");
                bits = bits.replaceAll("\\P{Print}", "");
                break;
            }
        }

        if (bits == null) {
            return 0l;
        }
        try {
            return Long.parseLong(bits);
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

    // Sends a ping to the count API to track the amount of users per day
    public void trackLoad() {
        try {
            RequestsManager.newRequest(new Request("https://api.countapi.xyz/hit/partly-sane-skies-load", s -> {
                System.out.println("\n\nPartly Sane Skies startup count:\n" + s.getResponse() + "\n\n");
            }));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
