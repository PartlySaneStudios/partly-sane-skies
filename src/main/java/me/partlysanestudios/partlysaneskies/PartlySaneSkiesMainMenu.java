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
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.client.GuiModList;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PartlySaneSkiesMainMenu extends WindowScreen{
    
    public PartlySaneSkiesMainMenu(ElementaVersion version) {
        super(version);
    }


    HashMap<Integer, String> imageIdMap = new HashMap<Integer, String>();

    public void populateMap() { 
        imageIdMap.put(1, "/assets/partlysaneskies/textures/gui/main_menu/image_1.png");
    }

    UIComponent background;
    UIComponent middleMenuBar;
    UIComponent titleImage;

    UIComponent singleplayerButton;
    UIComponent multiplayerButton;
    UIComponent modsButton;
    UIComponent optionButton;
    UIComponent quitButton;

    UIComponent singleplayerText;
    UIComponent multiplayerText;
    UIComponent modsText;
    UIComponent optionsText;
    UIComponent quitText;



    @SubscribeEvent
    public void openCustomMainMenu(GuiOpenEvent e) {
        if (!(Main.config.customMainMenu)) return;
        if (!(e.gui instanceof GuiMainMenu)) return;
        e.setCanceled(true);
        Main.minecraft.displayGuiScreen(this);

    
    }

    @Override
    public void initScreen(int width, int height) {
        populateMap();
        if(background == null) {
            float scaleFactor = (getWindow().getWidth())/1075f;
            populateGui(scaleFactor);
        }
        else {
            float scaleFactor = (getWindow().getWidth())/1075f;
            resizeGui(scaleFactor);
        }
    }


    public void populateGui(float scaleFactor) {
        
        String image;
        

        if(Main.config.customMainMenuImage == 0) {
            image = imageIdMap.get(Utils.randint(1, imageIdMap.size()));
        }
        else image = imageIdMap.get(Main.config.customMainMenuImage);

        background = UIImage.ofResource(image)
            .setX(new CenterConstraint())
            .setY(new CenterConstraint())
            .setWidth(new PixelConstraint(getWindow().getWidth()))
            .setHeight(new PixelConstraint(getWindow().getHeight()))
            .setChildOf(getWindow());
        
        middleMenuBar = new UIBlock()
            .setX(new PixelConstraint(300*scaleFactor))
            .setY(new CenterConstraint())
            .setHeight(new PixelConstraint(getWindow().getHeight()))
            .setWidth(new PixelConstraint(125*scaleFactor))
            .setColor(new Color(210, 204, 204, 50))
            .setChildOf(background);
        
        float titleHeight = 75;
        float titleWidth = titleHeight*(928/124);
        titleImage = UIImage.ofResource("/assets/partlysaneskies/textures/gui/main_menu/title_text.png")
            .setX(new CenterConstraint())
            .setY(new PixelConstraint(50*scaleFactor))
            .setHeight(new PixelConstraint(titleHeight*scaleFactor))
            .setWidth(new PixelConstraint(titleWidth*scaleFactor))
            .setChildOf(middleMenuBar);



        singleplayerButton = new UIBlock()
            .setX(new CenterConstraint())
            .setY(new PixelConstraint(200*scaleFactor))
            .setHeight(new PixelConstraint(40*scaleFactor))
            .setWidth(new PixelConstraint(middleMenuBar.getWidth()))
            .setColor(new Color(0, 0, 0, 50))
            .setChildOf(middleMenuBar);

        singleplayerText = new UIText("Singleplayer")
            .setX(new CenterConstraint())
            .setY(new CenterConstraint())
            .setTextScale(new PixelConstraint(1*scaleFactor))
            .setChildOf(singleplayerButton);

        singleplayerButton.onMouseClickConsumer(event -> {
            this.mc.displayGuiScreen(new GuiSelectWorld(this));
        });



        multiplayerButton = new UIBlock()
            .setX(new CenterConstraint())
            .setY(new PixelConstraint(260*scaleFactor))
            .setHeight(new PixelConstraint(40*scaleFactor))
            .setWidth(new PixelConstraint(middleMenuBar.getWidth()))
            .setColor(new Color(0, 0, 0, 50))
            .setChildOf(middleMenuBar);

        multiplayerText = new UIText("Multiplayer")
            .setX(new CenterConstraint())
            .setY(new CenterConstraint())
            .setTextScale(new PixelConstraint(1*scaleFactor))
            .setChildOf(multiplayerButton);

        multiplayerButton.onMouseClickConsumer(event -> {
            this.mc.displayGuiScreen(new GuiMultiplayer(this));
        });



        modsButton = new UIBlock()
            .setX(new CenterConstraint())
            .setY(new PixelConstraint(320*scaleFactor))
            .setHeight(new PixelConstraint(40*scaleFactor))
            .setWidth(new PixelConstraint(middleMenuBar.getWidth()))
            .setColor(new Color(0, 0, 0, 50))
            .setChildOf(middleMenuBar);

        modsText = new UIText("Mods")
            .setX(new CenterConstraint())
            .setY(new CenterConstraint())
            .setTextScale(new PixelConstraint(1*scaleFactor))
            .setChildOf(modsButton);

        modsButton.onMouseClickConsumer(event -> {
            this.mc.displayGuiScreen(new GuiModList(this));
        });



        optionButton = new UIBlock()
            .setX(new CenterConstraint())
            .setY(new PixelConstraint(380*scaleFactor))
            .setHeight(new PixelConstraint(40*scaleFactor))
            .setWidth(new PixelConstraint(middleMenuBar.getWidth()))
            .setColor(new Color(0, 0, 0, 50))
            .setChildOf(middleMenuBar);

        optionsText = new UIText("Options")
            .setX(new CenterConstraint())
            .setY(new CenterConstraint())
            .setTextScale(new PixelConstraint(1*scaleFactor))
            .setChildOf(optionButton);

        optionButton.onMouseClickConsumer(event -> {
            this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
        });

        
        
        quitButton = new UIBlock()
            .setX(new CenterConstraint())
            .setY(new PixelConstraint(440*scaleFactor))
            .setHeight(new PixelConstraint(40*scaleFactor))
            .setWidth(new PixelConstraint(middleMenuBar.getWidth()))
            .setColor(new Color(0, 0, 0, 50))
            .setChildOf(middleMenuBar);

        quitText = new UIText("Quit Game")
            .setX(new CenterConstraint())
            .setY(new CenterConstraint())
            .setTextScale(new PixelConstraint(1*scaleFactor))
            .setChildOf(quitButton);

        quitButton.onMouseClickConsumer(event -> {
            this.mc.shutdown();
        });
    }






    public void resizeGui(float scaleFactor) {
        background
            .setWidth(new PixelConstraint(getWindow().getWidth()))
            .setHeight(new PixelConstraint(getWindow().getHeight()));
        
        middleMenuBar
            .setX(new PixelConstraint(300*scaleFactor))
            .setHeight(new PixelConstraint(getWindow().getHeight()))
            .setWidth(new PixelConstraint(125*scaleFactor));

        float titleHeight = 75;
        float titleWidth = titleHeight*(928/124);
        titleImage
            .setY(new PixelConstraint(50*scaleFactor))
            .setWidth(new PixelConstraint(titleWidth*scaleFactor))
            .setHeight(new PixelConstraint(titleHeight*scaleFactor));


        singleplayerButton
            .setY(new PixelConstraint(200*scaleFactor))
            .setHeight(new PixelConstraint(40*scaleFactor))
            .setWidth(new PixelConstraint(middleMenuBar.getWidth()));
        
        singleplayerText
            .setTextScale(new PixelConstraint(1*scaleFactor));
        

        multiplayerButton

            .setY(new PixelConstraint(260*scaleFactor))
            .setHeight(new PixelConstraint(40*scaleFactor))
            .setWidth(new PixelConstraint(middleMenuBar.getWidth()));
        
        multiplayerText
            .setTextScale(new PixelConstraint(1*scaleFactor));
        

        modsButton
            .setY(new PixelConstraint(320*scaleFactor))
            .setHeight(new PixelConstraint(40*scaleFactor))
            .setWidth(new PixelConstraint(middleMenuBar.getWidth()));
        
        modsText
            .setTextScale(new PixelConstraint(1*scaleFactor));
        

        optionButton
            .setY(new PixelConstraint(380*scaleFactor))
            .setHeight(new PixelConstraint(40*scaleFactor))
            .setWidth(new PixelConstraint(middleMenuBar.getWidth()));
        
        optionsText
            .setTextScale(new PixelConstraint(1*scaleFactor));

        quitButton
            .setY(new PixelConstraint(440*scaleFactor))
            .setHeight(new PixelConstraint(40*scaleFactor))
            .setWidth(new PixelConstraint(middleMenuBar.getWidth()));
        
        quitText
            .setTextScale(new PixelConstraint(1*scaleFactor));
    }
}
