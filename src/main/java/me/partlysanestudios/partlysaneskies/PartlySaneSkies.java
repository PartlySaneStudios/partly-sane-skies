/* 
 * 
 * Written by Su386.
 * See LICENSE for copyright and license notices.
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

import cc.polyfrost.oneconfig.config.core.OneColor;
import gg.essential.elementa.ElementaVersion;
import me.partlysanestudios.partlysaneskies.auctionhouse.menu.AuctionHouseGui;
import me.partlysanestudios.partlysaneskies.chat.ChatAlertsManager;
import me.partlysanestudios.partlysaneskies.chat.ChatManager;
import me.partlysanestudios.partlysaneskies.chat.WordEditor;
import me.partlysanestudios.partlysaneskies.data.skyblockdata.SkyblockDataManager;
import me.partlysanestudios.partlysaneskies.dungeons.PlayerRating;
import me.partlysanestudios.partlysaneskies.dungeons.WatcherReady;
import me.partlysanestudios.partlysaneskies.dungeons.partymanager.PartyManager;
import me.partlysanestudios.partlysaneskies.dungeons.permpartyselector.PermPartyManager;
import me.partlysanestudios.partlysaneskies.economy.BitsShopValue;
import me.partlysanestudios.partlysaneskies.economy.minioncalculator.MinionData;
import me.partlysanestudios.partlysaneskies.economy.minioncalculator.ProfitMinionCalculator;
import me.partlysanestudios.partlysaneskies.garden.CompostValue;
import me.partlysanestudios.partlysaneskies.garden.GardenTradeValue;
import me.partlysanestudios.partlysaneskies.garden.MathematicalHoeRightClicks;
import me.partlysanestudios.partlysaneskies.garden.SkymartValue;
import me.partlysanestudios.partlysaneskies.garden.endoffarmnotifer.EndOfFarmNotifier;
import me.partlysanestudios.partlysaneskies.garden.endoffarmnotifer.RangeHighlight;
import me.partlysanestudios.partlysaneskies.mining.MiningEvents;
import me.partlysanestudios.partlysaneskies.mining.WormWarning;
import me.partlysanestudios.partlysaneskies.mining.*;
import me.partlysanestudios.partlysaneskies.rngdropbanner.DropBannerDisplay;
import me.partlysanestudios.partlysaneskies.system.*;
import me.partlysanestudios.partlysaneskies.system.requests.Request;
import me.partlysanestudios.partlysaneskies.system.requests.RequestsManager;
import me.partlysanestudios.partlysaneskies.utils.StringUtils;
import me.partlysanestudios.partlysaneskies.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.inventory.IInventory;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


@Mod(modid = PartlySaneSkies.MODID, version = PartlySaneSkies.VERSION, name = PartlySaneSkies.NAME)
public class PartlySaneSkies {

    public static void main(String[] args) throws IOException {

    }

    public static Logger LOGGER = LogManager.getLogger("Partly Sane Skies");
    public static final String MODID = "@MOD_ID@";
    public static final String NAME = "@MOD_NAME@";
    public static final String VERSION = "@MOD_VERSION@";
    //    -----------------------CHANGE TO FALSE BEFORE RELEASING
    public static final boolean DOGFOOD = Boolean.parseBoolean("@DOGFOOD@");
    public static final String CHAT_PREFIX = ("§r§b§lPartly Sane Skies§r§7>> §r");
    public static final boolean IS_LEGACY_VERSION = false;
    public static String discordCode = "v4PU3WeH7z";

    public static OneConfigScreen config;
    public static Minecraft minecraft;

    public static boolean isDebugMode;

    private static LocationBannerDisplay locationBannerDisplay;

    private static String API_KEY;


    // Names of all the ranks to remove from people's names
    public static final String[] RANK_NAMES = { "[VIP]", "[VIP+]", "[MVP]", "[MVP+]", "[MVP++]", "[YOUTUBE]", "[MOJANG]",
            "[EVENTS]", "[MCP]", "[PIG]", "[PIG+]", "[PIG++]", "[PIG+++]", "[GM]", "[ADMIN]", "[OWNER]", "[NPC]" };

    // Method runs at mod initialization
    @EventHandler
    public void init(FMLInitializationEvent evnt) {
        Utils.log(Level.INFO,"Hallo World!");
        PartlySaneSkies.isDebugMode = false;
        PartlySaneSkies.minecraft = Minecraft.getMinecraft();

        // Creates the partly-sane-skies directory if not already made
        new File("./config/partly-sane-skies/").mkdirs();
        
        // Loads the config files and options
        PartlySaneSkies.config = new OneConfigScreen();    
        Request mainMenuRequest = null;
        try {
            mainMenuRequest = new Request("https://raw.githubusercontent.com/PartlySaneStudios/partly-sane-skies-public-data/main/data/main_menu.json", CustomMainMenu::setMainMenuInfo);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        RequestsManager.newRequest(mainMenuRequest);
        trackLoad();
        RequestsManager.run();

        new Thread(() -> {
            // Loads perm party data
            try {
                PermPartyManager.load();
                PermPartyManager.loadFavoriteParty();
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
                EndOfFarmNotifier.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                WordEditor.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        

        // Registers all the events
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
        MinecraftForge.EVENT_BUS.register(new GardenTradeValue());
        MinecraftForge.EVENT_BUS.register(ChatManager.INSTANCE);
        MinecraftForge.EVENT_BUS.register(new CompostValue());
        MinecraftForge.EVENT_BUS.register(new EnhancedSound());
        MinecraftForge.EVENT_BUS.register(new BitsShopValue());
        MinecraftForge.EVENT_BUS.register(new PlayerRating());
        MinecraftForge.EVENT_BUS.register(new SkymartValue());
        MinecraftForge.EVENT_BUS.register(new PetAlert());
        MinecraftForge.EVENT_BUS.register(new MathematicalHoeRightClicks());
        MinecraftForge.EVENT_BUS.register(RangeHighlight.INSTANCE);
        MinecraftForge.EVENT_BUS.register(BannerRenderer.INSTANCE);
        MinecraftForge.EVENT_BUS.register(new MiningEvents());
        MinecraftForge.EVENT_BUS.register(AuctionHouseGui.Companion);
        MinecraftForge.EVENT_BUS.register(new Pickaxes());



        // Registers all client side commands
        HelpCommands.registerPSSCommand();
        PartyManager.registerCommand();
        HelpCommands.registerHelpCommand();
        SkillUpgradeRecommendation.registerCommand();
        PermPartyManager.registerCommand();
        PartyFriendManager.registerCommand();
        ChatAlertsManager.registerCommand();
        PetAlert.registerCommand();
        HelpCommands.registerDiscordCommand();
        HelpCommands.registerConfigCommand();
        EndOfFarmNotifier.registerPos1Command();
        EndOfFarmNotifier.registerPos2Command();
        EndOfFarmNotifier.registerCreateRangeCommand();
        EndOfFarmNotifier.registerFarmNotifierCommand();
        ProfitMinionCalculator.registerCommand();
        MathematicalHoeRightClicks.registerCommand();
        WordEditor.registerWordEditorCommand();
        HelpCommands.registerCrepesCommand();
        HelpCommands.registerVersionCommand();

        // Initializes keybinds
        Keybinds.init();

        // Initializes Utils class
        Utils.init();

        MathematicalHoeRightClicks.loadHoes();


        // Initializes skill upgrade recommendation
        SkillUpgradeRecommendation.populateSkillMap();

        // API Calls
        new Thread(PlayerRating::initPatterns).start();


        try {
            SkyblockDataManager.initItems();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SkyblockDataManager.updateAll();
        CompostValue.init();
        try {
            SkymartValue.initCopperValues();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            MinionData.preRequestInit();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            SkyblockDataManager.initSkills();
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Thread(() -> {
            try {
                SkyblockDataManager.initBitValues();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();


        // Loads user player data for PartyManager
        new Thread(() -> {
            try {
                SkyblockDataManager.getPlayer(PartlySaneSkies.minecraft.getSession().getUsername());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }).start();

        // Finished loading
        Utils.log(Level.INFO,"Partly Sane Skies has loaded.");
    }

    public static String getAPIKey() {
        if (config.forceCustomAPIKey) {
            return config.apiKey;
        }
        return API_KEY;
    }

    // Method runs every tick
    @SubscribeEvent
    public void clientTick(ClientTickEvent evnt) {
        // Runs the request manager
        RequestsManager.run();

        // Checks if the current location is the same as the previous location for the location banner display
        locationBannerDisplay.checkLocation();

        SkyblockDataManager.runUpdater();

        // Checks if the player is collecting minions
        PetAlert.runPetAlert();

        EndOfFarmNotifier.run();
        config.resetBrokenStrings();
        ThemeManager.run();
    }

    // Runs when the chat message starts with "Your new API key is"
    // Updates the API key to the new API key
    @SubscribeEvent
    public void newApiKey(ClientChatReceivedEvent event) {
        if (event.message.getUnformattedText().startsWith("Your new API key is ")) {
            config.apiKey = event.message.getUnformattedText().replace("Your new API key is ", "");
            Utils.sendClientMessage(("Saved new API key!"));
            config.save();
        }
    }

    // Runs chat analyzer for debug mode
    @SubscribeEvent
    public void chatAnalyzer(ClientChatReceivedEvent evnt) {
        if (PartlySaneSkies.isDebugMode)
            Utils.log(Level.INFO, evnt.message.getFormattedText());
    }

    @SubscribeEvent
    public void world(WorldEvent.Load event) {
        // Code that is supposed to be here is dead code so removed on this branch
        // Code that is supposed to go here:
        // https://github.com/PartlySaneStudios/partly-sane-skies/blob/essential-based/src/main/java/me/partlysanestudios/partlysaneskies/PartlySaneSkies.java#LL303C5-L327C10
    }

    @SubscribeEvent
    public void onClientConnectedToServer(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        if (DOGFOOD) {
            new Thread(() -> {
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                IChatComponent discordMessage = new ChatComponentText(("§9The Partly Sane Skies Discord server: https://discord.gg/" + discordCode));
                discordMessage.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.gg/" + discordCode));
                Utils.sendClientMessage("§b§m--------------------------------------------------", true);
                Utils.sendClientMessage("§cWe have detected you are using a dogfood version of Partly Sane Skies.", false);
                Utils.sendClientMessage("§c§lThis version may be unstable.", true);
                Utils.sendClientMessage("§cOnly use it when recieving permission to do so from a Partly Sane Skies admin.", true);
                Utils.sendClientMessage("§cReport any bugs to a Partly Sane Skies admin in a private ticket.", true);
                Utils.sendClientMessage("§7Version ID: §d" + VERSION, true);
                Utils.sendClientMessage("§7Latest non-dogfood version: §d" + CustomMainMenu.latestVersion, true);
                Utils.sendClientMessage(discordMessage);
                Utils.sendClientMessage("§b§m--------------------------------------------------", true);
            }).start();
        }

        if (!isLatestVersion()) {
            new Thread(() -> {
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                
                Utils.sendClientMessage("§b§m--------------------------------------------------", true);

                Utils.sendClientMessage("§cWe have detected a new version of Partly Sane Skies.");
                
                ChatComponentText skyclientMessage = new ChatComponentText(("§aIf you are using SkyClient, make sure you update when prompted."));
                PartlySaneSkies.minecraft.ingameGUI
                        .getChatGUI()
                        .printChatMessage(skyclientMessage);
            
                ChatComponentText githubMessage = new ChatComponentText(("§9If you are not using SkyClient, click here go to the github and download the latest version."));
                githubMessage.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/PartlySaneStudios/partly-sane-skies/releases"));
                githubMessage.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText("Click here to open the downloads page")));
                PartlySaneSkies.minecraft.ingameGUI
                        .getChatGUI()
                        .printChatMessage(githubMessage);
                
                        Utils.sendClientMessage("§b§m--------------------------------------------------", true);
            }).start();
        }
    }

    // Returns an array of length 2, where the 1st index is the upper inventory,
    // and the 2nd index is the lower inventory.s]
    // Returns null if there is no inventory, also returns null if there is no access to inventory
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

    // Returns the name of the scoreboard without color codes
    public static String getScoreboardName() {
        String scoreboardName = minecraft.thePlayer.getWorldScoreboard().getObjectiveInDisplaySlot(1).getDisplayName();
        return StringUtils.removeColorCodes(scoreboardName);
    }

    // Runs when debug key is pressed
    public static void debugMode() {
        PartlySaneSkies.isDebugMode = !PartlySaneSkies.isDebugMode;
        Utils.sendClientMessage("Debug mode: " + PartlySaneSkies.isDebugMode);
        BannerRenderer.INSTANCE.renderNewBanner(new PSSBanner("Test", 5000L, 5f, new OneColor(255, 0, 255, 1).toJavaColor()));
    }

    // Returns a list of lines on the scoreboard,
    // where each line is a new entry
    public static List<String> getScoreboardLines() {
        try {
            Scoreboard scoreboard = minecraft.theWorld.getScoreboard();
            ScoreObjective objective = scoreboard.getObjectiveInDisplaySlot(1);
            Collection<Score> scoreCollection = scoreboard.getSortedScores(objective);

            List<String> scoreLines = new ArrayList<>();
            for (Score score : scoreCollection) {
                scoreLines.add(ScorePlayerTeam.formatPlayerName(scoreboard.getPlayersTeam(score.getPlayerName()),
                        score.getPlayerName()));
            }

            return scoreLines;
        } catch (IllegalArgumentException e) {
            if (e.getMessage().equals("Cannot locate declared field class net.minecraft.client.gui.inventory.GuiChest.field_147015_w")) {
                System.out.println("Strange error message in PartlySaneSkies.getScoreboardLines()");
            }
            e.printStackTrace();
            return Collections.emptyList();
        }
    }


    // Returns the time in milliseconds
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

    // Gets the number of coins in your purse from the scoreboard
    public static long getCoins() {
        if (!isSkyblock()) {
            return 0L;
        }

        List<String> scoreboard = getScoreboardLines();

        String money = null;

        for (String line : scoreboard) {
            if (StringUtils.stripLeading(line).contains("Piggy:") || StringUtils.stripLeading(line).contains("Purse:")) {
                money = StringUtils.stripLeading(StringUtils.removeColorCodes(line)).replace("Piggy: ", "");
                money = StringUtils.stripLeading(StringUtils.removeColorCodes(money)).replace("Purse: ", "");
                money = StringUtils.stripLeading(money);
                money = money.replace(",", "");
                money = money.replaceAll("\\P{Print}", "");
                break;
            }
        }

        if (money == null) {
            return 0L;
        }
        try {
            return Long.parseLong(money);
        } catch (NumberFormatException event) {
            return 0;
        }
    }

    // Gets the number of bits from the scoreboard
    public static long getBits() {
        if (!isSkyblock()) {
            return 0L;
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
            return 0L;
        }

        String[] charsToRemove = {"(", ")", ".", "-", "+"};

        for (String removalChar : charsToRemove) {
            if (bits.contains(removalChar)) {
                int indexOfEndOfCount = bits.indexOf(removalChar);
                bits = bits.substring(0, indexOfEndOfCount);
            }
        }

        bits = StringUtils.stripLeading(bits);
        bits = StringUtils.stripTrailing(bits);
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
        } catch (NullPointerException ignored) {
        }
        return false;
    }

    // Sends a ping to the count API to track the number of users per day
    public void trackLoad() {

    }

    public static boolean isLatestVersion() {
        if(DOGFOOD) {
            return true;
        }

        else return VERSION.equals(CustomMainMenu.latestVersion);
    }
}
