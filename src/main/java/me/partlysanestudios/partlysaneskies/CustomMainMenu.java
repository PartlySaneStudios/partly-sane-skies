//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import gg.essential.elementa.ElementaVersion;
import gg.essential.elementa.UIComponent;
import gg.essential.elementa.WindowScreen;
import gg.essential.elementa.components.UIBlock;
import gg.essential.elementa.components.UIImage;
import gg.essential.elementa.components.UIText;
import gg.essential.elementa.components.UIWrappedText;
import gg.essential.elementa.constraints.CenterConstraint;
import gg.essential.elementa.constraints.PixelConstraint;
import me.partlysanestudios.partlysaneskies.system.ThemeManager;
import me.partlysanestudios.partlysaneskies.system.requests.Request;
import me.partlysanestudios.partlysaneskies.utils.ElementaUtils;
import me.partlysanestudios.partlysaneskies.utils.MathUtils;
import me.partlysanestudios.partlysaneskies.utils.SystemUtils;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.GuiModList;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.io.File;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class CustomMainMenu extends WindowScreen {

    public CustomMainMenu(ElementaVersion version) {
        super(version);

    }

    HashMap<Integer, String> imageIdMap = new HashMap<>();

    public void populateMap() {
        imageIdMap.put(1, "image_1.png");
        imageIdMap.put(2, "image_2.png");
        imageIdMap.put(3, "image_3.png");
        imageIdMap.put(4, "image_4.png");
        imageIdMap.put(5, "image_5.png");
        imageIdMap.put(6, "image_6.png");
    }

    public UIComponent background;
    private UIComponent middleMenu;
    private UIComponent middleLeftBar;
    private UIComponent middleRightBar;
    private UIComponent titleImage;

    private UIComponent singleplayerButton;
    private UIComponent multiplayerButton;
    private UIComponent joinHypixelButton;
    private UIComponent modsButton;
    private UIComponent optionsButton;
    private UIComponent pssOptionsButton;
    private UIComponent quitButton;

    private UIComponent optionsButtonSplitBar;

    private UIComponent updateWarning;

    private UIComponent singleplayerText;
    private UIComponent multiplayerText;
    private UIComponent joinHypixelText;
    private UIComponent modsText;
    private UIComponent optionsText;
    private UIComponent pssOptionsText;
    private UIComponent quitText;
    private UIComponent timeText;
    private UIComponent discordText;
    private static String funFactWebsite = "https://uselessfacts.jsph.pl/today";
    public static String funFactApi = "https://uselessfacts.jsph.pl/api/v2/facts/today";
    private static String funFact = "Loading...";
    private UIWrappedText funFactTitle;
    private static UIWrappedText funFactText;
    private final String hypixelIP = "mc.hypixel.net"; // I want to use "ilovecatgirls.xyz" to bad

    private static ArrayList<Announcement> announcements = new ArrayList<>();
    public static String latestVersion = "(Unknown)";
    // private static String latestVersionDate;
    // private static String latestVersionDescription;

    @SubscribeEvent
    public void openCustomMainMenu(GuiOpenEvent e) {
        if (!(PartlySaneSkies.config.customMainMenu))
            return;
        if (!(e.gui instanceof GuiMainMenu))
            return;
        e.setCanceled(true);
        PartlySaneSkies.minecraft.displayGuiScreen(new CustomMainMenu(ElementaVersion.V2));
        PartlySaneSkies.minecraft.getSoundHandler()
                .playSound(PositionedSoundRecord.create(new ResourceLocation("partlysaneskies", "bell")));
    }

    @Override
    public void initScreen(int width, int height) {
        populateMap();
        if (background == null) {
            float scaleFactor = (getWindow().getWidth()) / 1075f;
            populateGui(scaleFactor);
        } else {
            float scaleFactor = (getWindow().getWidth()) / 1075f;
            resizeGui(scaleFactor);
        }
    }

    public void populateGui(float scaleFactor) {
        String image;

        if (PartlySaneSkies.config.customMainMenuImage == 0) {
            image = "textures/gui/main_menu/" + imageIdMap.get(MathUtils.INSTANCE.randInt(1, imageIdMap.size()));
        } else
            image = "textures/gui/main_menu/" + imageIdMap.get(PartlySaneSkies.config.customMainMenuImage);

        if (PartlySaneSkies.config.customMainMenuImage == 7) {
            background = UIImage.ofFile(new File("./config/partly-sane-skies/background.png"));
        }
        else{
            background = ElementaUtils.INSTANCE.uiImageFromResourceLocation(new ResourceLocation("partlysaneskies", image));
        }


        background
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setWidth(new PixelConstraint(getWindow().getWidth()))
                .setHeight(new PixelConstraint(getWindow().getHeight()))
                .setChildOf(getWindow());
        

        middleMenu = new UIBlock()
                .setX(new PixelConstraint(300 * scaleFactor))
                .setY(new CenterConstraint())
                .setHeight(new PixelConstraint(getWindow().getHeight()))
                .setWidth(new PixelConstraint(125 * scaleFactor))
                .setColor(new Color(0, 0, 0, 75))
                .setChildOf(background);

        middleLeftBar = new UIBlock()
                .setX(new PixelConstraint(-2 * scaleFactor))
                .setY(new CenterConstraint())
                .setHeight(new PixelConstraint(middleMenu.getHeight()))
                .setWidth(new PixelConstraint(2 * scaleFactor))
                .setColor(ThemeManager.getAccentColor().toJavaColor())
                .setChildOf(middleMenu);

        middleRightBar = new UIBlock()
                .setX(new PixelConstraint(middleMenu.getWidth()))
                .setY(new CenterConstraint())
                .setHeight(new PixelConstraint(middleMenu.getHeight()))
                .setWidth(new PixelConstraint(2 * scaleFactor))
                .setColor(ThemeManager.getAccentColor().toJavaColor())
                .setChildOf(middleMenu);

        float titleHeight = 75;
        float titleWidth = titleHeight * (928f / 124);

        titleImage = ElementaUtils.INSTANCE.uiImageFromResourceLocation((new ResourceLocation("partlysaneskies", "textures/gui/main_menu/title_text.png")))
                .setX(new CenterConstraint())
                .setY(new PixelConstraint(50 * scaleFactor))
                .setHeight(new PixelConstraint(titleHeight * scaleFactor))
                .setWidth(new PixelConstraint(titleWidth * scaleFactor))
                .setChildOf(middleMenu);

        
        if (!PartlySaneSkies.isLatestVersion()){
            updateWarning = new UIWrappedText("Your version of Partly Sane Skies is out of date.\nPlease update to the latest version", true, new Color(0, 0, 0), true)
                .setTextScale(new PixelConstraint(2.25f * scaleFactor))
                .setX(new CenterConstraint())
                .setY(new PixelConstraint(133 * scaleFactor))
                .setWidth(new PixelConstraint(700 * scaleFactor))
                .setColor(new Color(255, 0, 0))
                .setChildOf(middleMenu);
            
            updateWarning.onMouseClickConsumer(event -> SystemUtils.INSTANCE.openLink("https://github.com/PartlySaneStudios/partly-sane-skies/releases"));
        }

        if (PartlySaneSkies.config.displayAnnouncementsCustomMainMenu) {
            for (int i = 0; i <= 3 && i < announcements.size(); i++) {
                announcements.get(i).createTitle(scaleFactor, i, background);
                announcements.get(i).createDescription(scaleFactor, i, background);
            }
        }

        singleplayerButton = new UIBlock()
                .setX(new CenterConstraint())
                .setY(new PixelConstraint(200 * scaleFactor))
                .setHeight(new PixelConstraint(40 * scaleFactor))
                .setWidth(new PixelConstraint(middleMenu.getWidth()))
                .setColor(new Color(0, 0, 0, 0))
                .setChildOf(middleMenu);

        singleplayerText = new UIText("Singleplayer")
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setTextScale(new PixelConstraint(1 * scaleFactor))
                .setChildOf(singleplayerButton);

        singleplayerButton.onMouseClickConsumer(event -> this.mc.displayGuiScreen(new GuiSelectWorld(this)));

        singleplayerButton.onMouseEnterRunnable(() -> singleplayerText.setColor(new Color(200, 200, 200)));

        singleplayerButton.onMouseLeaveRunnable(() -> singleplayerText.setColor(new Color(255, 255, 255)));

        multiplayerButton = new UIBlock()
                .setX(new CenterConstraint())
                .setY(new PixelConstraint(240 * scaleFactor))
                .setHeight(new PixelConstraint(40 * scaleFactor))
                .setWidth(new PixelConstraint(middleMenu.getWidth()))
                .setColor(new Color(0, 0, 0, 0))
                .setChildOf(middleMenu);

        multiplayerText = new UIText("Multiplayer")
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setTextScale(new PixelConstraint(1 * scaleFactor))
                .setChildOf(multiplayerButton);

        multiplayerButton.onMouseClickConsumer(event -> this.mc.displayGuiScreen(new GuiMultiplayer(this)));

        multiplayerButton.onMouseEnterRunnable(() -> multiplayerText.setColor(new Color(200, 200, 200)));

        multiplayerButton.onMouseLeaveRunnable(() -> multiplayerText.setColor(new Color(255, 255, 255)));

        joinHypixelButton = new UIBlock()
                .setX(new CenterConstraint())
                .setY(new PixelConstraint(280 * scaleFactor))
                .setHeight(new PixelConstraint(40 * scaleFactor))
                .setWidth(new PixelConstraint(middleMenu.getWidth()))
                .setColor(new Color(0, 0, 0, 0))
                .setChildOf(middleMenu);

        joinHypixelText = new UIText("Join Hypixel")
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setTextScale(new PixelConstraint(1 * scaleFactor))
                .setChildOf(joinHypixelButton);

        joinHypixelButton.onMouseClickConsumer(event -> FMLClientHandler.instance().connectToServer(new GuiMultiplayer(PartlySaneSkies.minecraft.currentScreen), new ServerData("AddictionGame", hypixelIP, false)));

        joinHypixelButton.onMouseEnterRunnable(() -> joinHypixelText.setColor(new Color(200, 200, 200)));

        joinHypixelButton.onMouseLeaveRunnable(() -> joinHypixelText.setColor(new Color(255, 255, 255)));

        modsButton = new UIBlock()
                .setX(new CenterConstraint())
                .setY(new PixelConstraint(320 * scaleFactor))
                .setHeight(new PixelConstraint(40 * scaleFactor))
                .setWidth(new PixelConstraint(middleMenu.getWidth()))
                .setColor(new Color(0, 0, 0, 0))
                .setChildOf(middleMenu);

        modsText = new UIText("Mods")
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setTextScale(new PixelConstraint(1 * scaleFactor))
                .setChildOf(modsButton);

        modsButton.onMouseClickConsumer(event -> this.mc.displayGuiScreen(new GuiModList(this)));

        modsButton.onMouseEnterRunnable(() -> modsText.setColor(new Color(200, 200, 200)));

        modsButton.onMouseLeaveRunnable(() -> modsText.setColor(new Color(255, 255, 255)));

        optionsButton = new UIBlock()
                .setX(new PixelConstraint(0))
                .setY(new PixelConstraint(380 * scaleFactor))
                .setHeight(new PixelConstraint(20 * scaleFactor))
                .setWidth(new PixelConstraint(middleMenu.getWidth()))
                .setColor(new Color(0, 0, 0, 0))
                .setChildOf(middleMenu);

        optionsText = new UIText("Options")
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setTextScale(new PixelConstraint(.75f * scaleFactor))
                .setChildOf(optionsButton);

        optionsButton.onMouseClickConsumer(event -> this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings)));

        optionsButton.onMouseEnterRunnable(() -> optionsText.setColor(new Color(200, 200, 200)));

        optionsButton.onMouseLeaveRunnable(() -> optionsText.setColor(new Color(255, 255, 255)));

        optionsButtonSplitBar = new UIBlock()
                .setX(new CenterConstraint())
                .setY(new PixelConstraint(400f * scaleFactor))
                .setHeight(new PixelConstraint(1 * scaleFactor))
                .setWidth(new PixelConstraint(middleMenu.getWidth() * .90f))
                .setColor(ThemeManager.getAccentColor().toJavaColor())
                .setChildOf(middleMenu);

        pssOptionsButton = new UIBlock()
                .setX(new CenterConstraint())
                .setY(new PixelConstraint(400 * scaleFactor))
                .setHeight(new PixelConstraint(20 * scaleFactor))
                .setWidth(new PixelConstraint(middleMenu.getWidth()))
                .setColor(new Color(0, 0, 0, 0))
                .setChildOf(middleMenu);

        pssOptionsText = new UIText("Partly Sane Skies Config")
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setTextScale(new PixelConstraint(.735f * scaleFactor))
                .setChildOf(pssOptionsButton);

        pssOptionsButton.onMouseClickConsumer(event -> PartlySaneSkies.config.openGui());

        pssOptionsButton.onMouseEnterRunnable(() -> pssOptionsText.setColor(new Color(200, 200, 200)));

        pssOptionsButton.onMouseLeaveRunnable(() -> pssOptionsText.setColor(new Color(255, 255, 255)));

        quitButton = new UIBlock()
                .setX(new CenterConstraint())
                .setY(new PixelConstraint(440 * scaleFactor))
                .setHeight(new PixelConstraint(40 * scaleFactor))
                .setWidth(new PixelConstraint(middleMenu.getWidth()))
                .setColor(new Color(0, 0, 0, 0))
                .setChildOf(middleMenu);

        quitText = new UIText("Quit Game")
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setTextScale(new PixelConstraint(1 * scaleFactor))
                .setChildOf(quitButton);

        quitButton.onMouseClickConsumer(event -> this.mc.shutdown());

        quitButton.onMouseEnterRunnable(() -> quitText.setColor(new Color(200, 200, 200)));

        quitButton.onMouseLeaveRunnable(() -> quitText.setColor(new Color(255, 255, 255)));

        timeText = new UIText()
            .setX(new CenterConstraint())
            .setY(new PixelConstraint(middleMenu.getHeight() - 10 * scaleFactor))
            .setColor(Color.white)
            .setTextScale(new PixelConstraint(.5f * scaleFactor))
            .setChildOf(middleMenu);
        
        discordText = new UIText("Discord: discord.gg/" + PartlySaneSkies.discordCode)
            .setX(new PixelConstraint(10 * scaleFactor))
            .setY(new PixelConstraint(background.getHeight() - 20 * scaleFactor))
            .setTextScale(new PixelConstraint(1 * scaleFactor))
            .setColor(new Color(69, 79, 191))
            .setChildOf(background);

        discordText.onMouseClickConsumer(event -> SystemUtils.INSTANCE.openLink("https://discord.gg/" + PartlySaneSkies.discordCode));

        funFactTitle = (UIWrappedText) new UIWrappedText("Fun Fact of the Day", true, new Color(120, 120, 120), true)
                .setX(new PixelConstraint((int) background.getWidth() * 0.6f))
                .setY(new PixelConstraint(10 * scaleFactor + 20))
                .setWidth(new PixelConstraint(700 * scaleFactor))
                .setTextScale(new PixelConstraint(1 * scaleFactor))
                .setColor(new Color(255, 255, 255))
                .setChildOf(background);

        funFactText = (UIWrappedText) new UIWrappedText(funFact, true, new Color(120, 120, 120), true)
                .setX(new PixelConstraint((int) background.getWidth() * 0.6f))
                .setY(new PixelConstraint(25 * scaleFactor + 30))
                .setWidth(new PixelConstraint(700 * scaleFactor))
                .setTextScale(new PixelConstraint(1 * scaleFactor))
                .setColor(new Color(255, 255, 255))
                .setChildOf(background);

        funFactText.onMouseClickConsumer(event -> SystemUtils.INSTANCE.openLink(funFactWebsite));
    }

    public void resizeGui(float scaleFactor) {
        background
                .setWidth(new PixelConstraint(getWindow().getWidth()))
                .setHeight(new PixelConstraint(getWindow().getHeight()));

        middleMenu
                .setX(new PixelConstraint(300 * scaleFactor))
                .setHeight(new PixelConstraint(getWindow().getHeight()))
                .setWidth(new PixelConstraint(125 * scaleFactor));

        middleLeftBar
                .setX(new PixelConstraint(-2 * scaleFactor))
                .setY(new CenterConstraint())
                .setHeight(new PixelConstraint(middleMenu.getHeight()))
                .setWidth(new PixelConstraint(2 * scaleFactor));

        middleRightBar
                .setX(new PixelConstraint(middleMenu.getWidth()))
                .setY(new CenterConstraint())
                .setHeight(new PixelConstraint(middleMenu.getHeight()))
                .setWidth(new PixelConstraint(2 * scaleFactor));

        float titleHeight = 75;
        float titleWidth = titleHeight * (928f / 124);
        titleImage
                .setY(new PixelConstraint(50 * scaleFactor))
                .setWidth(new PixelConstraint(titleWidth * scaleFactor))
                .setHeight(new PixelConstraint(titleHeight * scaleFactor));

        if (updateWarning != null) {
            updateWarning
                .setTextScale(new PixelConstraint(2.25f * scaleFactor))
                .setX(new CenterConstraint())
                .setWidth(new PixelConstraint(700 * scaleFactor))
                .setY(new PixelConstraint(133 * scaleFactor));
        }

        for (int i = 0; i <= 3 && i < announcements.size(); i++) {
            announcements.get(i).updateTitleComponent(scaleFactor);
            announcements.get(i).updateDescriptionComponent(scaleFactor);
        }

        singleplayerButton
                .setY(new PixelConstraint(200 * scaleFactor))
                .setHeight(new PixelConstraint(40 * scaleFactor))
                .setWidth(new PixelConstraint(middleMenu.getWidth()));

        singleplayerText
                .setTextScale(new PixelConstraint(1 * scaleFactor));

        multiplayerButton
                .setY(new PixelConstraint(240 * scaleFactor))
                .setHeight(new PixelConstraint(40 * scaleFactor))
                .setWidth(new PixelConstraint(middleMenu.getWidth()));

        multiplayerText
                .setTextScale(new PixelConstraint(1 * scaleFactor));

        joinHypixelButton
                .setY(new PixelConstraint(280 * scaleFactor))
                .setHeight(new PixelConstraint(40 * scaleFactor))
                .setWidth(new PixelConstraint(middleMenu.getWidth()));

        joinHypixelText
                .setTextScale(new PixelConstraint(1 * scaleFactor));

        modsButton
                .setY(new PixelConstraint(320 * scaleFactor))
                .setHeight(new PixelConstraint(40 * scaleFactor))
                .setWidth(new PixelConstraint(middleMenu.getWidth()));

        modsText
                .setTextScale(new PixelConstraint(1 * scaleFactor));

        optionsButton
                .setX(new PixelConstraint(0))
                .setY(new PixelConstraint(380 * scaleFactor))
                .setHeight(new PixelConstraint(20 * scaleFactor))
                .setWidth(new PixelConstraint(middleMenu.getWidth()));

        optionsText
                .setTextScale(new PixelConstraint(.75f * scaleFactor));

        optionsButtonSplitBar
                .setX(new CenterConstraint())
                .setY(new PixelConstraint(400f * scaleFactor))
                .setHeight(new PixelConstraint(1 * scaleFactor))
                .setWidth(new PixelConstraint(middleMenu.getWidth() * .90f));

        pssOptionsButton
                .setY(new PixelConstraint(400 * scaleFactor))
                .setHeight(new PixelConstraint(20 * scaleFactor))
                .setWidth(new PixelConstraint(middleMenu.getWidth()));

        pssOptionsText
                .setTextScale(new PixelConstraint(.735f * scaleFactor));

        quitButton
                .setY(new PixelConstraint(440f * scaleFactor))
                .setHeight(new PixelConstraint(40f * scaleFactor))
                .setWidth(new PixelConstraint(middleMenu.getWidth()));

        quitText
                .setTextScale(new PixelConstraint(1 * scaleFactor));

        timeText
                .setX(new CenterConstraint())
                .setY(new PixelConstraint(middleMenu.getHeight() - 10 * scaleFactor))
                .setTextScale(new PixelConstraint(.5f * scaleFactor))
                .setHeight(new PixelConstraint(middleMenu.getWidth()));

        discordText
                .setX(new PixelConstraint(10 * scaleFactor))
                .setY(new PixelConstraint(background.getHeight() - 20 * scaleFactor))
                .setTextScale(new PixelConstraint(1 * scaleFactor));

        funFactTitle
                .setX(new PixelConstraint((int) background.getWidth() * 0.6f))
                .setY(new PixelConstraint(10 * scaleFactor + 20))
                .setTextScale(new PixelConstraint(1 * scaleFactor));

        funFactText
                .setX(new PixelConstraint((int) background.getWidth() * 0.6f))
                .setY(new PixelConstraint(25 * scaleFactor + 30))
                .setTextScale(new PixelConstraint(1 * scaleFactor));
    }

    public static void setMainMenuInfo(Request request) {
        if (!request.hasSucceeded()) {
            return;
        }

        String responseString = request.getResponse();

        JsonObject object;
        try {
            object = new JsonParser().parse(responseString).getAsJsonObject();
        } catch (NullPointerException | IllegalStateException  e) {
            noInfoFound();
            e.printStackTrace();
            return;
        }

        try {
            Prank.Companion.setPrankKillSwitch(object.get("prank_sound").getAsBoolean());
        } catch(NullPointerException | IllegalStateException e){
            e.printStackTrace();
        }

        JsonArray array;
        announcements = new ArrayList<>();
        try {
            array = object.get("announcements").getAsJsonArray();
            for (JsonElement element : array) {
                JsonObject announcement = element.getAsJsonObject();

                String title = announcement.get("name").getAsString();
                String desc = announcement.get("description").getAsString();
                String date = announcement.get("date").getAsString();
                
                String urlString = announcement.get("link").getAsString();

                announcements.add(new CustomMainMenu.Announcement(title, date, desc, urlString));
            }
        } catch (NullPointerException | IllegalStateException e) {
            e.printStackTrace();
        }
            
        try {
            JsonObject modInfo = object.getAsJsonObject("mod_info");

            latestVersion = modInfo.get("latest_version").getAsString();
            // latestVersionDescription = modInfo.get("latest_version_description").getAsString();
            // latestVersionDate = modInfo.get("latest_version_release_date").getAsString();

        } catch (NullPointerException | IllegalStateException e) {
            CustomMainMenu.latestVersion = "(Unknown)";
            e.printStackTrace();
            // CustomMainMenu.latestVersionDate = "(Unknown)";
            // CustomMainMenu.latestVersionDescription = "";
        }

        try {
            JsonObject modInfo = object.getAsJsonObject("mod_info");

            PartlySaneSkies.discordCode = modInfo.get("discord_invite_code").getAsString();
        } catch (NullPointerException | IllegalStateException e) {
            PartlySaneSkies.discordCode = "v4PU3WeH7z";
            e.printStackTrace();
            // CustomMainMenu.latestVersionDate = "(Unknown)";
            // CustomMainMenu.latestVersionDescription = "";
        }
        
    }

    public static void noInfoFound() {
        CustomMainMenu.latestVersion = "(Unknown)";
        // CustomMainMenu.latestVersionDate = "(Unknown)";
        // CustomMainMenu.latestVersionDescription = "";
        CustomMainMenu.announcements = new ArrayList<>();
    }

    public static void setFunFact(Request request) {
        if (!request.hasSucceeded()) {
            return;
        }

        String responseString = request.getResponse();

        JsonObject object;
        try {
            object = new JsonParser().parse(responseString).getAsJsonObject();
        } catch (NullPointerException | IllegalStateException  e) {
            noInfoFound();
            e.printStackTrace();
            return;
        }
            //{"id":"c1dbf293f84042595368168a5b1c558d","text":"%60 of all people using the Internet, use it for pornography.","source":"djtech.net","source_url":"http://www.djtech.net/humor/useless_facts.htm","language":"en","permalink":"https://uselessfacts.jsph.pl/api/v2/facts/c1dbf293f84042595368168a5b1c558d"}
        try {
            JsonObject factInfo = object.getAsJsonObject();

            String fact = factInfo.get("text").getAsString();
            String source = factInfo.get("source").getAsString();

            CustomMainMenu.funFact = fact + "\nSource: " + source;
            CustomMainMenu.funFactText.setText(CustomMainMenu.funFact);

        } catch (NullPointerException | IllegalStateException | ClassCastException e) {
            CustomMainMenu.funFact = "Failed to load fun fact.";
            CustomMainMenu.funFactText.setText(CustomMainMenu.funFact);
            CustomMainMenu.funFactWebsite = "https://uselessfacts.jsph.pl/today";
            e.printStackTrace();
        }
    }


    public static class Announcement {
        private final String title;
        private final String date;
        private final String description;
        private final String link;

        private int postNum;
        private UIComponent titleComponent;
        private UIComponent descriptionComponent;
        
        public Announcement(String title, String date, String description, String link) {
            this.title = title;
            this.date = date;
            this.description = description;
            this.link = link;
        }

        public String getLink() {
            return this.link;
        }
        
        public String getTitle() {
            return this.title;
        }

        public String getDate() {
            return this.date;
        }

        public String getDescription() {
            return this.description;
        }

        public UIWrappedText createTitle(float scaleFactor, int postNum, UIComponent parent) {
            UIComponent text = new UIWrappedText(("§e" + title))
                .setTextScale(new PixelConstraint(1.5f * scaleFactor))
                .setX(new PixelConstraint(33f * scaleFactor))
                .setY(new PixelConstraint(125 * scaleFactor + 145 * (postNum) * scaleFactor))
                .setWidth(new PixelConstraint(256 * scaleFactor))
                .setChildOf(parent);
            this.postNum = postNum;
            this.titleComponent = text;

            text.onMouseClickConsumer(event -> SystemUtils.INSTANCE.openLink(link));
            return (UIWrappedText) text;
        }
        public UIWrappedText createDescription(float scaleFactor, int postNum, UIComponent parent) {
            UIComponent text = new UIWrappedText(("§8" + date + "§r\n§7" + description))
                .setTextScale(new PixelConstraint(1.33f * scaleFactor))
                .setX(new PixelConstraint(33f * scaleFactor))
                .setY(new PixelConstraint(160 * scaleFactor + 145 * (postNum) * scaleFactor))
                .setWidth(new PixelConstraint(250 * scaleFactor))
                .setChildOf(parent);
            this.postNum = postNum;
            this.descriptionComponent = text;

            text.onMouseClickConsumer(event -> SystemUtils.INSTANCE.openLink(link));
            return (UIWrappedText) text;
        }

        public void updateTitleComponent(float scaleFactor) {
            if (this.titleComponent == null) {
                return;
            }
            this.titleComponent
                .setTextScale(new PixelConstraint(1.5f * scaleFactor))
                .setX(new PixelConstraint(33f * scaleFactor))
                .setY(new PixelConstraint(125 * scaleFactor + 145 * (postNum) * scaleFactor))
                .setWidth(new PixelConstraint(250 * scaleFactor));
        }

        public void updateDescriptionComponent(float scaleFactor) {
            if (this.descriptionComponent == null) {
                return;
            }
            this.descriptionComponent
                .setTextScale(new PixelConstraint(1.33f * scaleFactor))
                .setX(new PixelConstraint(33f * scaleFactor))
                .setY(new PixelConstraint(160 * scaleFactor + 145 * (postNum) * scaleFactor))
                .setWidth(new PixelConstraint(250 * scaleFactor));
        }
    }

    @Override
    public void onDrawScreen(gg.essential.universal.UMatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.onDrawScreen(matrixStack, mouseX, mouseY, partialTicks);

        ZoneId userZoneId = ZoneId.systemDefault();
        
        LocalDateTime currentTime = LocalDateTime.now(userZoneId);
        String timeString = currentTime.format(DateTimeFormatter.ofPattern("hh:mm:ss a  dd MMMM yyyy", Locale.ENGLISH));
        if (PartlySaneSkies.config.hour24time) {
            timeString = currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss dd MMMM yyyy", Locale.ENGLISH));
        }

        middleLeftBar.setColor(ThemeManager.getAccentColor().toJavaColor());
        middleRightBar.setColor(ThemeManager.getAccentColor().toJavaColor());
        optionsButtonSplitBar.setColor(ThemeManager.getAccentColor().toJavaColor());
        

        ((UIText) timeText).setText(timeString);
    }
}
