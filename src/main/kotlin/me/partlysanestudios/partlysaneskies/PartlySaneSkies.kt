/*
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
 */

package me.partlysanestudios.partlysaneskies

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import me.partlysanestudios.partlysaneskies.config.Keybinds
import me.partlysanestudios.partlysaneskies.config.OneConfigScreen
import me.partlysanestudios.partlysaneskies.config.psconfig.Config
import me.partlysanestudios.partlysaneskies.config.psconfig.ConfigManager
import me.partlysanestudios.partlysaneskies.config.psconfig.Toggle
import me.partlysanestudios.partlysaneskies.config.psconfig.Toggle.Companion.asBoolean
import me.partlysanestudios.partlysaneskies.config.psconfig.Toggle.Companion.asToggle
import me.partlysanestudios.partlysaneskies.data.cache.PetData
import me.partlysanestudios.partlysaneskies.data.cache.StatsData
import me.partlysanestudios.partlysaneskies.data.cache.VisitorLogbookData
import me.partlysanestudios.partlysaneskies.data.pssdata.PublicDataManager
import me.partlysanestudios.partlysaneskies.data.skyblockdata.SkyblockDataManager
import me.partlysanestudios.partlysaneskies.events.EventManager
import me.partlysanestudios.partlysaneskies.events.SubscribePSSEvent
import me.partlysanestudios.partlysaneskies.events.data.LoadPublicDataEvent
import me.partlysanestudios.partlysaneskies.features.chat.ChatAlertsManager
import me.partlysanestudios.partlysaneskies.features.chat.ChatManager
import me.partlysanestudios.partlysaneskies.features.chat.ChatTransformer
import me.partlysanestudios.partlysaneskies.features.chat.WordEditor
import me.partlysanestudios.partlysaneskies.features.commands.Crepes
import me.partlysanestudios.partlysaneskies.features.commands.HelpCommand
import me.partlysanestudios.partlysaneskies.features.commands.PSSDiscord
import me.partlysanestudios.partlysaneskies.features.commands.SanityCheck
import me.partlysanestudios.partlysaneskies.features.commands.Version
import me.partlysanestudios.partlysaneskies.features.debug.DebugKey
import me.partlysanestudios.partlysaneskies.features.debug.ExampleHud
import me.partlysanestudios.partlysaneskies.features.debug.ExampleWebhook
import me.partlysanestudios.partlysaneskies.features.discord.DiscordRPC
import me.partlysanestudios.partlysaneskies.features.discord.webhooks.WebhookMenu
import me.partlysanestudios.partlysaneskies.features.dungeons.AutoGG
import me.partlysanestudios.partlysaneskies.features.dungeons.HealthAlert
import me.partlysanestudios.partlysaneskies.features.dungeons.ItemRefill
import me.partlysanestudios.partlysaneskies.features.dungeons.PlayerRating
import me.partlysanestudios.partlysaneskies.features.dungeons.RequiredSecretsFound
import me.partlysanestudios.partlysaneskies.features.dungeons.TerminalWaypoints
import me.partlysanestudios.partlysaneskies.features.dungeons.WatcherReady
import me.partlysanestudios.partlysaneskies.features.dungeons.party.PartyFriendManager
import me.partlysanestudios.partlysaneskies.features.dungeons.party.partymanager.PartyManager
import me.partlysanestudios.partlysaneskies.features.dungeons.party.permpartyselector.PermPartyManager
import me.partlysanestudios.partlysaneskies.features.economy.BitsShopValue
import me.partlysanestudios.partlysaneskies.features.economy.CoinsToBoosterCookies
import me.partlysanestudios.partlysaneskies.features.economy.NoCookieWarning
import me.partlysanestudios.partlysaneskies.features.economy.auctionhousemenu.AuctionHouseGui
import me.partlysanestudios.partlysaneskies.features.economy.minioncalculator.MinionData
import me.partlysanestudios.partlysaneskies.features.economy.minioncalculator.ProfitMinionCalculator
import me.partlysanestudios.partlysaneskies.features.farming.MathematicalHoeRightClicks
import me.partlysanestudios.partlysaneskies.features.farming.WrongToolCropWarning
import me.partlysanestudios.partlysaneskies.features.farming.endoffarmnotifer.EndOfFarmNotifier
import me.partlysanestudios.partlysaneskies.features.farming.endoffarmnotifer.RangeHighlight
import me.partlysanestudios.partlysaneskies.features.farming.garden.CompostValue
import me.partlysanestudios.partlysaneskies.features.farming.garden.CropMilestoneWebhook
import me.partlysanestudios.partlysaneskies.features.farming.garden.SkymartValue
import me.partlysanestudios.partlysaneskies.features.farming.garden.VisitorLogbookStats
import me.partlysanestudios.partlysaneskies.features.farming.garden.VisitorTradeValue
import me.partlysanestudios.partlysaneskies.features.foraging.TreecapitatorCooldown
import me.partlysanestudios.partlysaneskies.features.gui.RefreshKeybinds
import me.partlysanestudios.partlysaneskies.features.gui.hud.CooldownHud
import me.partlysanestudios.partlysaneskies.features.gui.hud.LocationBannerDisplay
import me.partlysanestudios.partlysaneskies.features.gui.hud.rngdropbanner.DropBannerDisplay
import me.partlysanestudios.partlysaneskies.features.gui.hud.rngdropbanner.DropWebhook
import me.partlysanestudios.partlysaneskies.features.gui.mainmenu.PSSMainMenu
import me.partlysanestudios.partlysaneskies.features.information.WikiArticleOpener
import me.partlysanestudios.partlysaneskies.features.mining.MiningEvents
import me.partlysanestudios.partlysaneskies.features.mining.PickaxeWarning
import me.partlysanestudios.partlysaneskies.features.mining.crystalhollows.WormWarning
import me.partlysanestudios.partlysaneskies.features.mining.crystalhollows.gemstonewaypoints.GemstoneData
import me.partlysanestudios.partlysaneskies.features.mining.crystalhollows.gemstonewaypoints.GemstoneWaypointRender
import me.partlysanestudios.partlysaneskies.features.security.PrivacyMode
import me.partlysanestudios.partlysaneskies.features.security.modschecker.ModChecker
import me.partlysanestudios.partlysaneskies.features.skills.BestiaryLevelUpWebhook
import me.partlysanestudios.partlysaneskies.features.skills.BestiaryMilestoneWebhook
import me.partlysanestudios.partlysaneskies.features.skills.PetAlert
import me.partlysanestudios.partlysaneskies.features.skills.PetLevelUpWebhook
import me.partlysanestudios.partlysaneskies.features.skills.SkillUpgradeRecommendation
import me.partlysanestudios.partlysaneskies.features.skills.SkillUpgradeWebhook
import me.partlysanestudios.partlysaneskies.features.sound.EnhancedSound
import me.partlysanestudios.partlysaneskies.features.sound.Prank
import me.partlysanestudios.partlysaneskies.features.themes.ThemeManager
import me.partlysanestudios.partlysaneskies.render.gui.hud.BannerRenderer
import me.partlysanestudios.partlysaneskies.render.gui.hud.cooldown.CooldownManager
import me.partlysanestudios.partlysaneskies.utils.ChatUtils.sendClientMessage
import me.partlysanestudios.partlysaneskies.utils.SystemUtils.log
import net.minecraft.client.Minecraft
import net.minecraft.event.ClickEvent
import net.minecraft.event.HoverEvent
import net.minecraft.util.ChatComponentText
import net.minecraft.util.IChatComponent
import net.minecraftforge.common.MinecraftForge.EVENT_BUS
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.File
import java.io.IOException
import java.net.MalformedURLException

@Mod(
    modid = PartlySaneSkies.MODID,
    version = PartlySaneSkies.VERSION,
    name = PartlySaneSkies.NAME,
    clientSideOnly = true,
)
class PartlySaneSkies {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
        }

        val LOGGER: Logger = LogManager.getLogger("Partly Sane Skies")
        const val MODID = "@MOD_ID@"
        const val NAME = "@MOD_NAME@"
        const val VERSION = "@MOD_VERSION@"

        val DOGFOOD = "@DOGFOOD@".toBoolean()
        const val CHAT_PREFIX = "§r§b§lPartly Sane Skies§r§7>> §r"
        var discordCode = "v4PU3WeH7z"
        val config: OneConfigScreen = OneConfigScreen
        private var cachedFirstLaunch = false
        val isFirstLaunch get() = cachedFirstLaunch

        lateinit var minecraft: Minecraft
            private set

        // Names of all the ranks to remove from people's names
        val RANK_NAMES = arrayOf(
            "[VIP]", "[VIP+]", "[MVP]", "[MVP+]", "[MVP++]", "[YOUTUBE]", "[MOJANG]",
            "[EVENTS]", "[MCP]", "[PIG]", "[PIG+]", "[PIG++]", "[PIG+++]", "[GM]", "[ADMIN]", "[OWNER]", "[NPC]"
        )
        val time: Long
            // Returns the time in milliseconds
            get() = System.currentTimeMillis()
        val isLatestVersion: Boolean
            get() {
                return if (DOGFOOD) {
                    true
                } else if (latestVersion == "(Unknown)") {
                    true
                } else {
                    VERSION == latestVersion
                }
            }

        var latestVersion = "(Unknown)"

        val coreConfig = Config()
            .registerOption("alreadyStarted", Toggle("Already Started", "Has this already been started with PSS enabled?", false))
            .registerOption("promptedMainMenu", Toggle("Prompted main menu", defaultState = false))
    }

    // Method runs at mod initialization
    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {
        log(Level.INFO, "Hallo World!")
        minecraft = Minecraft.getMinecraft()

        // Creates the partly-sane-skies directory if not already made
        File("./config/partly-sane-skies/").mkdirs()

        trackLoad()
        Thread {
            PublicDataManager.getFile("main_menu.json")
        }.start()
        Thread {
            PSSMainMenu.loadFunFact()
        }.start()

        // Loads extra json data
        Thread {
            try {
                PermPartyManager.load()
                PermPartyManager.loadFavoriteParty()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            try {
                ChatAlertsManager.load()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            try {
                PetData.load()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            try {
                VisitorLogbookData.load()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            try {
                EndOfFarmNotifier.load()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            try {
                WordEditor.load()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()


        // Registers all the events
        registerEvent(this)
        registerEvent(PartyManager())
        registerEvent(PartyFriendManager())
        registerEvent(MiningEvents())
        registerEvent(MinionData())
        registerEvent(SkyblockDataManager)
        registerEvent(DropBannerDisplay)
        registerEvent(ChatManager)
        registerEvent(RangeHighlight)
        registerEvent(BannerRenderer)
        registerEvent(VisitorLogbookStats)
        registerEvent(CoinsToBoosterCookies)
        registerEvent(EndOfFarmNotifier)
        registerEvent(RefreshKeybinds)
        registerEvent(AutoGG)
        registerEvent(CooldownManager)
        registerEvent(PetData)
        registerEvent(SanityCheck)
        registerEvent(Keybinds)
        registerEvent(HealthAlert)
        registerEvent(EventManager)
        registerEvent(DebugKey)
        registerEvent(TerminalWaypoints)
        registerEvent(ItemRefill)
        registerEvent(TreecapitatorCooldown)
        registerEvent(WrongToolCropWarning)
        registerEvent(StatsData)
        registerEvent(ExampleHud)
        registerEvent(CooldownHud)
        registerEvent(GoldorWall)
        registerEvent(GemstoneData)
        registerEvent(GemstoneWaypointRender)
        registerEvent(WikiArticleOpener)
        registerEvent(WormWarning)
        registerEvent(PlayerRating)
        registerEvent(PickaxeWarning)
        registerEvent(WatcherReady)
        registerEvent(RequiredSecretsFound)
        registerEvent(EnhancedSound)
        registerEvent(MathematicalHoeRightClicks)
        registerEvent(CompostValue)
        registerEvent(BitsShopValue)
        registerEvent(SkymartValue)
        registerEvent(VisitorTradeValue)
        registerEvent(PSSMainMenu)
        registerEvent(WrongToolCropWarning.CropToolData)
        registerEvent(PetAlert)
        registerEvent(SkillUpgradeWebhook)
        registerEvent(CropMilestoneWebhook)
        registerEvent(BestiaryMilestoneWebhook)
        registerEvent(BestiaryLevelUpWebhook)
        registerEvent(PetLevelUpWebhook)


        // Registers all client side commands
        HelpCommand.registerPSSCommand()
        HelpCommand.registerHelpCommand()
        HelpCommand.registerConfigCommand()
        Crepes.registerCrepesCommand()
        Version.registerVersionCommand()
        PSSDiscord.registerDiscordCommand()
        PublicDataManager.registerDataCommand()
        PartyManager.registerCommand()
        SkillUpgradeRecommendation.registerCommand()
        PermPartyManager.registerCommand()
        PartyFriendManager.registerCommand()
        ChatAlertsManager.registerCommand()
        PetAlert.registerCommand()
        EndOfFarmNotifier.registerPos1Command()
        EndOfFarmNotifier.registerPos2Command()
        EndOfFarmNotifier.registerCreateRangeCommand()
        EndOfFarmNotifier.registerFarmNotifierCommand()
        EndOfFarmNotifier.registerWandCommand()
        CoinsToBoosterCookies.registerCommand()
        ProfitMinionCalculator.registerCommand()
        MathematicalHoeRightClicks.registerCommand()
        WordEditor.registerWordEditorCommand()
        PlayerRating.registerReprintCommand()
        ModChecker.registerModCheckCommand()
        ItemRefill.registerCommand()
        WebhookMenu.registerWebhookCommand()

        registerCoreConfig()
        ExampleWebhook.register()
        DropWebhook.register()
        SkillUpgradeWebhook.register()
        CropMilestoneWebhook.register()
        BestiaryMilestoneWebhook.register()
        BestiaryLevelUpWebhook.register()
        PetLevelUpWebhook.register()



        ConfigManager.loadAllConfigs()


        //Use Polyfrost EventManager cuz chatSendEvent makes transforming chat messages may easier
        cc.polyfrost.oneconfig.events.EventManager.INSTANCE.register(ChatTransformer)

        DebugKey.init()

        // Initializes skill upgrade recommendation
        SkillUpgradeRecommendation.populateSkillMap()
        try {
            SkyblockDataManager.updateAll()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        SkyblockDataManager.updateAll()
        try {
            SkyblockDataManager.initSkills()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        LoadPublicDataEvent.onDataLoad()

        // Loads user player data for PartyManager
        Thread({
            try {
                SkyblockDataManager.getPlayer(minecraft.session?.username ?: "")
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            }
        }, "Init Data").start()
        Thread { DiscordRPC.init() }.start()

        if (config.privacyMode == 2) {
            PrivacyMode.enablePrivacyMode()
        }

        checkFirstLaunch()

        // Finished loading
        log(Level.INFO, "Partly Sane Skies has loaded (Version: ${VERSION}).")
    }

    private fun registerEvent(obj: Any) {
        try {
            EVENT_BUS.register(obj)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            EventManager.register(obj)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Method runs every tick
    @SubscribeEvent
    fun clientTick(event: ClientTickEvent) {
        config.resetBrokenStringsTick()
        LocationBannerDisplay.checkLocationTick()
        EndOfFarmNotifier.checkAllRangesTick()
        SkyblockDataManager.runUpdaterTick()
        PetAlert.runPetAlertTick()
        ThemeManager.tick()
        PetData.tick()
        VisitorLogbookData.scanForVisitors()
        HealthAlert.checkPlayerTick()
        RequiredSecretsFound.tick()
        NoCookieWarning.checkCoinsTick()
        Prank.checkPrankTick()
        AuctionHouseGui.tick()
    }

    @SubscribePSSEvent
    fun loadMainMenuJson(event: LoadPublicDataEvent) {
        val data = PublicDataManager.getFile("main_menu.json")
        val jsonObj = JsonParser().parse(data).asJsonObject
        try {
            latestVersion = if (config.releaseChannel == 0) {
                val modInfo: JsonObject = jsonObj.getAsJsonObject("mod_info")
                modInfo["latest_version"].asString
            } else {
                val modInfo: JsonObject = jsonObj.getAsJsonObject("prerelease_channel")
                modInfo["latest_version"].asString
            }

            // latestVersionDescription = modInfo.get("latest_version_description").getAsString();
            // latestVersionDate = modInfo.get("latest_version_release_date").getAsString();
        } catch (e: NullPointerException) {
            latestVersion = "(Unknown)"
            e.printStackTrace()
            // latestVersionDate = "(Unknown)";
            // latestVersionDescription = "";
        } catch (e: IllegalStateException) {
            latestVersion = "(Unknown)"
            e.printStackTrace()
        }

        try {
            val modInfo: JsonObject = jsonObj.getAsJsonObject("mod_info")
            discordCode = modInfo["discord_invite_code"].asString
        } catch (e: NullPointerException) {
            discordCode = "v4PU3WeH7z"
            e.printStackTrace()
            // latestVersionDate = "(Unknown)";
            // latestVersionDescription = "";
        } catch (e: IllegalStateException) {
            discordCode = "v4PU3WeH7z"
            e.printStackTrace()
        }
    }

    private fun checkFirstLaunch() {
        if (coreConfig.find("alreadyStarted")?.asBoolean != true) {
            cachedFirstLaunch = true
            coreConfig.find("alreadyStarted")?.asToggle?.state = true
            log("Partly Sane Skies starting for the first time")
        }
    }


    private fun registerCoreConfig() {
        ConfigManager.registerNewConfig("psscore.json", coreConfig)
    }

    @SubscribeEvent
    fun onClientConnectedToServer(event: ClientConnectedToServerEvent?) {
        if (DOGFOOD) {
            Thread {
                try {
                    Thread.sleep(4000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                val discordMessage: IChatComponent =
                    ChatComponentText("§9The Partly Sane Skies PSSDiscord server: https://discord.gg/$discordCode")
                discordMessage.chatStyle.setChatClickEvent(
                    ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.gg/$discordCode")
                )
                sendClientMessage("§b§m--------------------------------------------------", true)
                sendClientMessage("§cWe noticed you're using a dogfood version of Partly Sane Skies.", false)
                sendClientMessage("§c§lThis version may be unstable.", true)
                sendClientMessage("§cOnly use it when told to do so by a Partly Sane Skies admin.", true)
                sendClientMessage("§cReport any bugs to Partly Sane Skies admins in a private ticket.", true)
                sendClientMessage("§7Version ID: §d$VERSION", true)
                sendClientMessage("§7Latest non-dogfood version: §dlatestVersion", true)
                sendClientMessage(discordMessage)
                sendClientMessage("§b§m--------------------------------------------------", true)
            }.start()
        }
        ModChecker.runOnStartup()
        if (!isLatestVersion) {
            Thread {
                try {
                    Thread.sleep(4000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                sendClientMessage("§b§m--------------------------------------------------", true)
                sendClientMessage("§cWe have detected a new version of Partly Sane Skies.")
                sendClientMessage("§cYou are currently using version §d$VERSION§c, the latest version is §d$latestVersion§c.")
                val skyclientMessage =
                    ChatComponentText("§aIf you are using SkyClient, make sure you update when prompted.")
                minecraft.ingameGUI
                    .chatGUI
                    .printChatMessage(skyclientMessage)
                val githubMessage =
                    ChatComponentText("§9If you are not using SkyClient, click here go to the github and download the latest version.")
                githubMessage.chatStyle.setChatClickEvent(
                    ClickEvent(
                        ClickEvent.Action.OPEN_URL,
                        "https://github.com/PartlySaneStudios/partly-sane-skies/releases"
                    )
                )
                githubMessage.chatStyle.setChatHoverEvent(
                    HoverEvent(
                        HoverEvent.Action.SHOW_TEXT,
                        ChatComponentText("Click here to open the downloads page")
                    )
                )
                minecraft.ingameGUI
                    .chatGUI
                    .printChatMessage(githubMessage)
                sendClientMessage("§b§m--------------------------------------------------", true)
            }.start()
        }
    }

    // Sends a ping to the count API to track the number of users per day
    private fun trackLoad() {}

}
