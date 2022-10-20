package me.partlysanestudios.partlysaneskies;

import java.awt.Color;
import java.util.HashMap;

import gg.essential.elementa.ElementaVersion;
import gg.essential.elementa.UIComponent;
import gg.essential.elementa.WindowScreen;
import gg.essential.elementa.components.UIBlock;
import gg.essential.elementa.components.UIImage;
import gg.essential.elementa.components.UIText;
import gg.essential.elementa.constraints.CenterConstraint;
import gg.essential.elementa.constraints.PixelConstraint;
import me.partlysanestudios.partlysaneskies.utils.Utils;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.client.GuiModList;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PartlySaneSkiesMainMenu extends WindowScreen {

    public PartlySaneSkiesMainMenu(ElementaVersion version) {
        super(version);
    }

    HashMap<Integer, String> imageIdMap = new HashMap<Integer, String>();

    public void populateMap() {
        imageIdMap.put(1, "image_1.png");
        imageIdMap.put(2, "image_2.png");
        imageIdMap.put(3, "image_3.png");
        imageIdMap.put(4, "image_4.png");
        imageIdMap.put(5, "image_5.png");
        imageIdMap.put(6, "image_6.png");
    }

    private UIComponent background;
    private UIComponent middleMenuBar;
    private UIComponent middleLeftBar;
    private UIComponent middleRightBar;
    private UIComponent titleImage;

    private UIComponent singleplayerButton;
    private UIComponent multiplayerButton;
    private UIComponent modsButton;
    private UIComponent optionsButton;
    private UIComponent pssOptionsButton;
    private UIComponent quitButton;

    private UIComponent optionsButtonSplitBar;

    private UIComponent singleplayerText;
    private UIComponent multiplayerText;
    private UIComponent modsText;
    private UIComponent optionsText;
    private UIComponent pssOptionsText;
    private UIComponent quitText;

    @SubscribeEvent
    public void openCustomMainMenu(GuiOpenEvent e) {
        if (!(Main.config.customMainMenu))
            return;
        if (!(e.gui instanceof GuiMainMenu))
            return;
        e.setCanceled(true);
        Main.minecraft.displayGuiScreen(new PartlySaneSkiesMainMenu(ElementaVersion.V2));
        Main.minecraft.getSoundHandler()
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

        if (Main.config.customMainMenuImage == 0) {
            image = imageIdMap.get(Utils.randint(1, imageIdMap.size()));
        } else
            image = imageIdMap.get(Main.config.customMainMenuImage);

        background = UIImage.ofResource("/assets/partlysaneskies/textures/gui/main_menu/" + image)
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setWidth(new PixelConstraint(getWindow().getWidth()))
                .setHeight(new PixelConstraint(getWindow().getHeight()))
                .setChildOf(getWindow());

        middleMenuBar = new UIBlock()
                .setX(new PixelConstraint(300 * scaleFactor))
                .setY(new CenterConstraint())
                .setHeight(new PixelConstraint(getWindow().getHeight()))
                .setWidth(new PixelConstraint(125 * scaleFactor))
                .setColor(new Color(0, 0, 0, 75))
                .setChildOf(background);

        middleLeftBar = new UIBlock()
                .setX(new PixelConstraint(-2 * scaleFactor))
                .setY(new CenterConstraint())
                .setHeight(new PixelConstraint(middleMenuBar.getHeight()))
                .setWidth(new PixelConstraint(2 * scaleFactor))
                .setColor(Main.ACCENT_COLOR)
                .setChildOf(middleMenuBar);

        middleRightBar = new UIBlock()
                .setX(new PixelConstraint(middleMenuBar.getWidth()))
                .setY(new CenterConstraint())
                .setHeight(new PixelConstraint(middleMenuBar.getHeight()))
                .setWidth(new PixelConstraint(2 * scaleFactor))
                .setColor(Main.ACCENT_COLOR)
                .setChildOf(middleMenuBar);

        float titleHeight = 75;
        float titleWidth = titleHeight * (928 / 124);
        titleImage = UIImage.ofResource("/assets/partlysaneskies/textures/gui/main_menu/title_text.png")
                .setX(new CenterConstraint())
                .setY(new PixelConstraint(50 * scaleFactor))
                .setHeight(new PixelConstraint(titleHeight * scaleFactor))
                .setWidth(new PixelConstraint(titleWidth * scaleFactor))
                .setChildOf(middleMenuBar);

        singleplayerButton = new UIBlock()
                .setX(new CenterConstraint())
                .setY(new PixelConstraint(200 * scaleFactor))
                .setHeight(new PixelConstraint(40 * scaleFactor))
                .setWidth(new PixelConstraint(middleMenuBar.getWidth()))
                .setColor(new Color(0, 0, 0, 0))
                .setChildOf(middleMenuBar);

        singleplayerText = new UIText("Singleplayer")
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setTextScale(new PixelConstraint(1 * scaleFactor))
                .setChildOf(singleplayerButton);

        singleplayerButton.onMouseClickConsumer(event -> {
            this.mc.displayGuiScreen(new GuiSelectWorld(this));
        });

        singleplayerButton.onMouseEnterRunnable(() -> {
            singleplayerText.setColor(new Color(200, 200, 200));
        });

        singleplayerButton.onMouseLeaveRunnable(() -> {
            singleplayerText.setColor(new Color(255, 255, 255));
        });

        multiplayerButton = new UIBlock()
                .setX(new CenterConstraint())
                .setY(new PixelConstraint(260 * scaleFactor))
                .setHeight(new PixelConstraint(40 * scaleFactor))
                .setWidth(new PixelConstraint(middleMenuBar.getWidth()))
                .setColor(new Color(0, 0, 0, 0))
                .setChildOf(middleMenuBar);

        multiplayerText = new UIText("Multiplayer")
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setTextScale(new PixelConstraint(1 * scaleFactor))
                .setChildOf(multiplayerButton);

        multiplayerButton.onMouseClickConsumer(event -> {
            this.mc.displayGuiScreen(new GuiMultiplayer(this));
        });

        multiplayerButton.onMouseEnterRunnable(() -> {
            multiplayerText.setColor(new Color(200, 200, 200));
        });

        multiplayerButton.onMouseLeaveRunnable(() -> {
            multiplayerText.setColor(new Color(255, 255, 255));
        });

        modsButton = new UIBlock()
                .setX(new CenterConstraint())
                .setY(new PixelConstraint(320 * scaleFactor))
                .setHeight(new PixelConstraint(40 * scaleFactor))
                .setWidth(new PixelConstraint(middleMenuBar.getWidth()))
                .setColor(new Color(0, 0, 0, 0))
                .setChildOf(middleMenuBar);

        modsText = new UIText("Mods")
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setTextScale(new PixelConstraint(1 * scaleFactor))
                .setChildOf(modsButton);

        modsButton.onMouseClickConsumer(event -> {
            this.mc.displayGuiScreen(new GuiModList(this));
        });

        modsButton.onMouseEnterRunnable(() -> {
            modsText.setColor(new Color(200, 200, 200));
        });

        modsButton.onMouseLeaveRunnable(() -> {
            modsText.setColor(new Color(255, 255, 255));
        });

        optionsButton = new UIBlock()
                .setX(new PixelConstraint(0))
                .setY(new PixelConstraint(380 * scaleFactor))
                .setHeight(new PixelConstraint(20 * scaleFactor))
                .setWidth(new PixelConstraint(middleMenuBar.getWidth()))
                .setColor(new Color(0, 0, 0, 0))
                .setChildOf(middleMenuBar);

        optionsText = new UIText("Options")
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setTextScale(new PixelConstraint(.75f * scaleFactor))
                .setChildOf(optionsButton);

        optionsButton.onMouseClickConsumer(event -> {
            this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
        });

        optionsButton.onMouseEnterRunnable(() -> {
            optionsText.setColor(new Color(200, 200, 200));
        });

        optionsButton.onMouseLeaveRunnable(() -> {
            optionsText.setColor(new Color(255, 255, 255));
        });

        optionsButtonSplitBar = new UIBlock()
                .setX(new CenterConstraint())
                .setY(new PixelConstraint(400f * scaleFactor))
                .setHeight(new PixelConstraint(1 * scaleFactor))
                .setWidth(new PixelConstraint(middleMenuBar.getWidth() * .90f))
                .setColor(Main.ACCENT_COLOR)
                .setChildOf(middleMenuBar);

        pssOptionsButton = new UIBlock()
                .setX(new CenterConstraint())
                .setY(new PixelConstraint(400 * scaleFactor))
                .setHeight(new PixelConstraint(20 * scaleFactor))
                .setWidth(new PixelConstraint(middleMenuBar.getWidth()))
                .setColor(new Color(0, 0, 0, 0))
                .setChildOf(middleMenuBar);

        pssOptionsText = new UIText("Partly Sane Skies Config")
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setTextScale(new PixelConstraint(.735f * scaleFactor))
                .setChildOf(pssOptionsButton);

        pssOptionsButton.onMouseClickConsumer(event -> {
            this.mc.displayGuiScreen(Main.config.gui());
        });

        pssOptionsButton.onMouseEnterRunnable(() -> {
            pssOptionsText.setColor(new Color(200, 200, 200));
        });

        pssOptionsButton.onMouseLeaveRunnable(() -> {
            pssOptionsText.setColor(new Color(255, 255, 255));
        });

        quitButton = new UIBlock()
                .setX(new CenterConstraint())
                .setY(new PixelConstraint(440 * scaleFactor))
                .setHeight(new PixelConstraint(40 * scaleFactor))
                .setWidth(new PixelConstraint(middleMenuBar.getWidth()))
                .setColor(new Color(0, 0, 0, 0))
                .setChildOf(middleMenuBar);

        quitText = new UIText("Quit Game")
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setTextScale(new PixelConstraint(1 * scaleFactor))
                .setChildOf(quitButton);

        quitButton.onMouseClickConsumer(event -> {
            this.mc.shutdown();
        });

        quitButton.onMouseEnterRunnable(() -> {
            quitText.setColor(new Color(200, 200, 200));
        });

        quitButton.onMouseLeaveRunnable(() -> {
            quitText.setColor(new Color(255, 255, 255));
        });
    }

    public void resizeGui(float scaleFactor) {
        background
                .setWidth(new PixelConstraint(getWindow().getWidth()))
                .setHeight(new PixelConstraint(getWindow().getHeight()));

        middleMenuBar
                .setX(new PixelConstraint(300 * scaleFactor))
                .setHeight(new PixelConstraint(getWindow().getHeight()))
                .setWidth(new PixelConstraint(125 * scaleFactor));

        middleLeftBar
                .setX(new PixelConstraint(-2 * scaleFactor))
                .setY(new CenterConstraint())
                .setHeight(new PixelConstraint(middleMenuBar.getHeight()))
                .setWidth(new PixelConstraint(2 * scaleFactor));

        middleRightBar
                .setX(new PixelConstraint(middleMenuBar.getWidth()))
                .setY(new CenterConstraint())
                .setHeight(new PixelConstraint(middleMenuBar.getHeight()))
                .setWidth(new PixelConstraint(2 * scaleFactor));

        float titleHeight = 75;
        float titleWidth = titleHeight * (928 / 124);
        titleImage
                .setY(new PixelConstraint(50 * scaleFactor))
                .setWidth(new PixelConstraint(titleWidth * scaleFactor))
                .setHeight(new PixelConstraint(titleHeight * scaleFactor));

        singleplayerButton
                .setY(new PixelConstraint(200 * scaleFactor))
                .setHeight(new PixelConstraint(40 * scaleFactor))
                .setWidth(new PixelConstraint(middleMenuBar.getWidth()));

        singleplayerText
                .setTextScale(new PixelConstraint(1 * scaleFactor));

        multiplayerButton
                .setY(new PixelConstraint(260 * scaleFactor))
                .setHeight(new PixelConstraint(40 * scaleFactor))
                .setWidth(new PixelConstraint(middleMenuBar.getWidth()));

        multiplayerText
                .setTextScale(new PixelConstraint(1 * scaleFactor));

        modsButton
                .setY(new PixelConstraint(320 * scaleFactor))
                .setHeight(new PixelConstraint(40 * scaleFactor))
                .setWidth(new PixelConstraint(middleMenuBar.getWidth()));

        modsText
                .setTextScale(new PixelConstraint(1 * scaleFactor));

        optionsButton
                .setX(new PixelConstraint(0))
                .setY(new PixelConstraint(380 * scaleFactor))
                .setHeight(new PixelConstraint(20 * scaleFactor))
                .setWidth(new PixelConstraint(middleMenuBar.getWidth()));

        optionsText
                .setTextScale(new PixelConstraint(.75f * scaleFactor));

        optionsButtonSplitBar
                .setX(new CenterConstraint())
                .setY(new PixelConstraint(400f * scaleFactor))
                .setHeight(new PixelConstraint(1 * scaleFactor))
                .setWidth(new PixelConstraint(middleMenuBar.getWidth() * .90f));

        pssOptionsButton
                .setY(new PixelConstraint(400 * scaleFactor))
                .setHeight(new PixelConstraint(20 * scaleFactor))
                .setWidth(new PixelConstraint(middleMenuBar.getWidth()));

        pssOptionsText
                .setTextScale(new PixelConstraint(.735f * scaleFactor));

        quitButton
                .setY(new PixelConstraint(440f * scaleFactor))
                .setHeight(new PixelConstraint(40f * scaleFactor))
                .setWidth(new PixelConstraint(middleMenuBar.getWidth()));

        quitText
                .setTextScale(new PixelConstraint(1 * scaleFactor));
    }
}
