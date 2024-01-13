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
 * SkyHanni
 * GSON
 * Elementa
 * Vigilance
 * OneConfig
 * SkyCrypt
 *
 */

package me.partlysanestudios.partlysaneskies;

import gg.essential.elementa.ElementaVersion;
import me.partlysanestudios.partlysaneskies.data.api.Request;
import me.partlysanestudios.partlysaneskies.data.api.RequestsManager;
import me.partlysanestudios.partlysaneskies.config.keybinds.Keybinds;
import me.partlysanestudios.partlysaneskies.features.gui.RefreshKeybinds;
import me.partlysanestudios.partlysaneskies.config.oneconfig.OneConfigScreen;
import me.partlysanestudios.partlysaneskies.data.cache.PetData;
import me.partlysanestudios.partlysaneskies.data.pssdata.PublicDataManager;
import me.partlysanestudios.partlysaneskies.data.skyblockdata.SkyblockDataManager;
import me.partlysanestudios.partlysaneskies.features.chat.ChatAlertsManager;
import me.partlysanestudios.partlysaneskies.features.chat.ChatManager;
import me.partlysanestudios.partlysaneskies.features.chat.WordEditor;
import me.partlysanestudios.partlysaneskies.features.debug.DebugKey;
import me.partlysanestudios.partlysaneskies.features.dungeons.AutoGG;
import me.partlysanestudios.partlysaneskies.features.commands.Crepes;
import me.partlysanestudios.partlysaneskies.features.commands.Discord;
import me.partlysanestudios.partlysaneskies.features.commands.Version;
import me.partlysanestudios.partlysaneskies.features.dungeons.PearlRefill;
import me.partlysanestudios.partlysaneskies.features.gui.custommainmenu.CustomMainMenu;
import me.partlysanestudios.partlysaneskies.features.dungeons.playerrating.PlayerRating;
import me.partlysanestudios.partlysaneskies.features.dungeons.RequiredSecretsFound;
import me.partlysanestudios.partlysaneskies.features.dungeons.WatcherReady;
import me.partlysanestudios.partlysaneskies.features.dungeons.healeralert.HealerAlert;
import me.partlysanestudios.partlysaneskies.features.dungeons.party.PartyFriendManager;
import me.partlysanestudios.partlysaneskies.features.dungeons.party.partymanager.PartyManager;
import me.partlysanestudios.partlysaneskies.features.dungeons.party.permpartyselector.PermPartyManager;
import me.partlysanestudios.partlysaneskies.features.economy.BitsShopValue;
import me.partlysanestudios.partlysaneskies.features.economy.CoinsToBoosterCookieConversion;
import me.partlysanestudios.partlysaneskies.features.economy.NoCookieWarning;
import me.partlysanestudios.partlysaneskies.features.economy.auctionhousemenu.AuctionHouseGui;
import me.partlysanestudios.partlysaneskies.features.economy.minioncalculator.MinionData;
import me.partlysanestudios.partlysaneskies.features.economy.minioncalculator.ProfitMinionCalculator;
import me.partlysanestudios.partlysaneskies.features.farming.MathematicalHoeRightClicks;
import me.partlysanestudios.partlysaneskies.features.farming.VisitorLogbookStats;
import me.partlysanestudios.partlysaneskies.features.farming.endoffarmnotifer.EndOfFarmNotifier;
import me.partlysanestudios.partlysaneskies.features.farming.endoffarmnotifer.RangeHighlight;
import me.partlysanestudios.partlysaneskies.features.farming.garden.CompostValue;
import me.partlysanestudios.partlysaneskies.features.farming.garden.GardenTradeValue;
import me.partlysanestudios.partlysaneskies.features.farming.garden.SkymartValue;
import me.partlysanestudios.partlysaneskies.features.commands.HelpCommand;
import me.partlysanestudios.partlysaneskies.features.information.WikiArticleOpener;
import me.partlysanestudios.partlysaneskies.features.gui.hud.LocationBannerDisplay;
import me.partlysanestudios.partlysaneskies.features.gui.hud.rngdropbanner.DropBannerDisplay;
import me.partlysanestudios.partlysaneskies.features.mining.MiningEvents;
import me.partlysanestudios.partlysaneskies.features.mining.Pickaxes;
import me.partlysanestudios.partlysaneskies.features.mining.WormWarning;
import me.partlysanestudios.partlysaneskies.features.security.modschecker.ModChecker;
import me.partlysanestudios.partlysaneskies.features.skills.PetAlert;
import me.partlysanestudios.partlysaneskies.features.skills.SkillUpgradeRecommendation;
import me.partlysanestudios.partlysaneskies.features.sound.Prank;
import me.partlysanestudios.partlysaneskies.features.sound.enhancedsound.EnhancedSound;
import me.partlysanestudios.partlysaneskies.features.themes.ThemeManager;
import me.partlysanestudios.partlysaneskies.gui.hud.BannerRenderer;
import me.partlysanestudios.partlysaneskies.gui.hud.cooldown.CooldownManager;
import me.partlysanestudios.partlysaneskies.utils.ChatUtils;
import me.partlysanestudios.partlysaneskies.utils.SystemUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;


@Mod(modid = PartlySaneSkies.MODID, version = PartlySaneSkies.VERSION, name = PartlySaneSkies.NAME)
public class PartlySaneSkies {

    public static void main(String[] args) {

    }

    public static Logger LOGGER = LogManager.getLogger("Partly Sane Skies");
    public static final String MODID = "@MOD_ID@";
    public static final String NAME = "@MOD_NAME@";
    public static final String VERSION = "@MOD_VERSION@";
    //    -----------------------CHANGE TO FALSE BEFORE RELEASING
    public static final boolean DOGFOOD = Boolean.parseBoolean("@DOGFOOD@");
    public static final String CHAT_PREFIX = "§r§b§lPartly Sane Skies§r§7>> §r";
    public static String discordCode = "v4PU3WeH7z";

    public static OneConfigScreen config;
    public static Minecraft minecraft;


    // Names of all the ranks to remove from people's names
    public static final String[] RANK_NAMES = {"[VIP]", "[VIP+]", "[MVP]", "[MVP+]", "[MVP++]", "[YOUTUBE]", "[MOJANG]",
            "[EVENTS]", "[MCP]", "[PIG]", "[PIG+]", "[PIG++]", "[PIG+++]", "[GM]", "[ADMIN]", "[OWNER]", "[NPC]"};

    // Method runs at mod initialization
    @EventHandler
    public void init(FMLInitializationEvent evnt) {
        SystemUtils.INSTANCE.log(Level.INFO, "Hallo World!");
        PartlySaneSkies.minecraft = Minecraft.getMinecraft();

        // Creates the partly-sane-skies directory if not already made
        new File("./config/partly-sane-skies/").mkdirs();

        // Loads the config files and options
        PartlySaneSkies.config = new OneConfigScreen();

        Request mainMenuRequest = null;
        try {
            mainMenuRequest = new Request("https://raw.githubusercontent.com/" + PublicDataManager.getRepoOwner() + "/" +  PublicDataManager.getRepoName() + "/main/data/main_menu.json", CustomMainMenu::setMainMenuInfo);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        RequestsManager.newRequest(mainMenuRequest);
        Request funFactRequest = null;
        try {
            funFactRequest = new Request(CustomMainMenu.funFactApi, CustomMainMenu::setFunFact);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        RequestsManager.newRequest(funFactRequest);
        trackLoad();
        RequestsManager.run();

        // Loads extra json data
        new Thread(() -> {
            try {
                PermPartyManager.load();
                PermPartyManager.loadFavoriteParty();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                ChatAlertsManager.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                PetData.INSTANCE.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                EndOfFarmNotifier.INSTANCE.load();
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
        MinecraftForge.EVENT_BUS.register(new GardenTradeValue());
        MinecraftForge.EVENT_BUS.register(new CompostValue());
        MinecraftForge.EVENT_BUS.register(new EnhancedSound());
        MinecraftForge.EVENT_BUS.register(new BitsShopValue());
        MinecraftForge.EVENT_BUS.register(new PlayerRating());
        MinecraftForge.EVENT_BUS.register(new SkymartValue());
        MinecraftForge.EVENT_BUS.register(new PetAlert());
        MinecraftForge.EVENT_BUS.register(new RequiredSecretsFound());
        MinecraftForge.EVENT_BUS.register(new Pickaxes());
        MinecraftForge.EVENT_BUS.register(new MathematicalHoeRightClicks());
        MinecraftForge.EVENT_BUS.register(new MiningEvents());

        MinecraftForge.EVENT_BUS.register(AuctionHouseGui.Companion);

        MinecraftForge.EVENT_BUS.register(ChatManager.INSTANCE);
        MinecraftForge.EVENT_BUS.register(RangeHighlight.INSTANCE);
        MinecraftForge.EVENT_BUS.register(BannerRenderer.INSTANCE);
        MinecraftForge.EVENT_BUS.register(VisitorLogbookStats.INSTANCE);
        MinecraftForge.EVENT_BUS.register(CoinsToBoosterCookieConversion.INSTANCE);
        MinecraftForge.EVENT_BUS.register(EndOfFarmNotifier.INSTANCE);
        MinecraftForge.EVENT_BUS.register(Prank.INSTANCE);
        MinecraftForge.EVENT_BUS.register(RefreshKeybinds.INSTANCE);
        MinecraftForge.EVENT_BUS.register(AutoGG.INSTANCE);
        MinecraftForge.EVENT_BUS.register(CooldownManager.INSTANCE);
        MinecraftForge.EVENT_BUS.register(PetData.INSTANCE);
        MinecraftForge.EVENT_BUS.register(PearlRefill.INSTANCE);


        // Registers all client side commands
        HelpCommand.registerPSSCommand();
        HelpCommand.registerHelpCommand();
        HelpCommand.registerConfigCommand();
        Crepes.registerCrepesCommand();
        Version.registerVersionCommand();
        Discord.registerDiscordCommand();
        PublicDataManager.registerDataCommand();
        PartyManager.registerCommand();
        SkillUpgradeRecommendation.registerCommand();
        PermPartyManager.registerCommand();
        PartyFriendManager.registerCommand();
        ChatAlertsManager.registerCommand();
        PetAlert.registerCommand();
        EndOfFarmNotifier.INSTANCE.registerPos1Command();
        EndOfFarmNotifier.INSTANCE.registerPos2Command();
        EndOfFarmNotifier.INSTANCE.registerCreateRangeCommand();
        EndOfFarmNotifier.INSTANCE.registerFarmNotifierCommand();
        EndOfFarmNotifier.INSTANCE.registerWandCommand();
        CoinsToBoosterCookieConversion.INSTANCE.registerCommand();
        ProfitMinionCalculator.registerCommand();
        MathematicalHoeRightClicks.registerCommand();
        WordEditor.registerWordEditorCommand();
        PlayerRating.registerReprintCommand();
        ModChecker.registerModCheckCommand();
        PearlRefill.INSTANCE.registerCommand();


        CooldownManager.INSTANCE.init();
        DebugKey.INSTANCE.init();



        // Initializes keybinds
        Keybinds.init();


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
            SkyblockDataManager.initSkills();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Loads user player data for PartyManager
        new Thread(() -> {
            MinionData.init();

            try {
                SkyblockDataManager.initBitValues();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                SkyblockDataManager.getPlayer(PartlySaneSkies.minecraft.getSession().getUsername());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try {
                SkymartValue.initCopperValues();
            } catch (IOException e) {
                e.printStackTrace();
            }

            MathematicalHoeRightClicks.loadHoes();
        }, "Init Data").start();

        // Finished loading
        SystemUtils.INSTANCE.log(Level.INFO, "Partly Sane Skies has loaded.");
    }

    // Method runs every tick
    @SubscribeEvent
    public void clientTick(ClientTickEvent evnt) {
        // Runs the request manager
        RequestsManager.run();

        // Checks if the current location is the same as the previous location for the location banner display
        LocationBannerDisplay.checkLocation();

        HealerAlert.INSTANCE.run();
        SkyblockDataManager.runUpdater();

        // Checks if the player is collecting minions
        PetAlert.runPetAlert();

        EndOfFarmNotifier.INSTANCE.run();
        config.resetBrokenStrings();
        ThemeManager.run();

        PetData.INSTANCE.tick();
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
                ChatUtils.INSTANCE.sendClientMessage("§b§m--------------------------------------------------", true);
                ChatUtils.INSTANCE.sendClientMessage("§cWe noticed you're using a dogfood version of Partly Sane Skies.", false);
                ChatUtils.INSTANCE.sendClientMessage("§c§lThis version may be unstable.", true);
                ChatUtils.INSTANCE.sendClientMessage("§cOnly use it when told to do so by a Partly Sane Skies admin.", true);
                ChatUtils.INSTANCE.sendClientMessage("§cReport any bugs to Partly Sane Skies admins in a private ticket.", true);
                ChatUtils.INSTANCE.sendClientMessage("§7Version ID: §d" + VERSION, true);
                ChatUtils.INSTANCE.sendClientMessage("§7Latest non-dogfood version: §d" + CustomMainMenu.latestVersion, true);
                ChatUtils.INSTANCE.sendClientMessage(discordMessage);
                ChatUtils.INSTANCE.sendClientMessage("§b§m--------------------------------------------------", true);
            }).start();
        }
        ModChecker.runOnStartup();

        if (!isLatestVersion()) {
            new Thread(() -> {
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                ChatUtils.INSTANCE.sendClientMessage("§b§m--------------------------------------------------", true);

                ChatUtils.INSTANCE.sendClientMessage("§cWe have detected a new version of Partly Sane Skies.");
                ChatUtils.INSTANCE.sendClientMessage("§cYou are currently using version §d" + VERSION + "§c, the latest version is §d" + CustomMainMenu.latestVersion + "§c.");

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

                ChatUtils.INSTANCE.sendClientMessage("§b§m--------------------------------------------------", true);
            }).start();
        }
    }


    // Returns the time in milliseconds
    public static long getTime() {
        return System.currentTimeMillis();
    }


    // Sends a ping to the count API to track the number of users per day
    public void trackLoad() {

    }

    public static boolean isLatestVersion() {
        if (DOGFOOD) {
            return true;
        } else return VERSION.equals(CustomMainMenu.latestVersion);
    }
}
