package me.partlysanestudios.partlysaneskies.dungeons.partymanager;

import java.awt.Color;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import gg.essential.elementa.ElementaVersion;
import gg.essential.elementa.UIComponent;
import gg.essential.elementa.WindowScreen;
import gg.essential.elementa.components.ScrollComponent;
import gg.essential.elementa.components.UIBlock;
import gg.essential.elementa.components.UIRoundedRectangle;
import gg.essential.elementa.components.UIText;
import gg.essential.elementa.constraints.CenterConstraint;
import gg.essential.elementa.constraints.PixelConstraint;
import gg.essential.elementa.constraints.ScaleConstraint;
import gg.essential.universal.UMatrixStack;
import me.partlysanestudios.partlysaneskies.Main;

public class PartyManagerGui extends WindowScreen{

    public HashMap<String, UIComponent> uiComponentMap = new HashMap<String, UIComponent>();

    UIComponent background = new UIBlock()
        .setX(new CenterConstraint())
        .setY(new CenterConstraint())
        .setWidth(new PixelConstraint(getWindow().getWidth()*.9f))
        .setHeight(new PixelConstraint(getWindow().getHeight()*.9f))
        .setChildOf(getWindow())
        .setColor(Main.BASE_DARK_COLOR);

    
    UIComponent list = new ScrollComponent("", 0f, Main.BASE_LIGHT_COLOR, false, true, false, false, 15f, 1f, null)
        .setWidth(new PixelConstraint(background.getWidth()))
        .setHeight(new PixelConstraint(background.getHeight()))
        .setChildOf(background);

    UIComponent loadingText = new UIText("Loading...")
        .setX(new CenterConstraint())
        .setY(new CenterConstraint())
        .setChildOf(list);

    UIComponent scrollBar = new UIBlock()
        .setColor(Main.BASE_LIGHT_COLOR)
        .setChildOf(list);

    
    public PartyManagerGui() {
        super(ElementaVersion.V2);
        ((ScrollComponent) list).setScrollBarComponent(scrollBar, false, false);
    }
    
    
    public void populateGui(List<PartyMember> partyMembers) {
        ((ScrollComponent) list).setScrollBarComponent(scrollBar, false, false);;
        float scaleFactor = (list.getWidth()-20f)/967.5f;
        float height = 180f*scaleFactor;
        UIComponent topBarBlock = new UIBlock()
            .setWidth(new PixelConstraint(list.getWidth()-20f))
            .setHeight(new ScaleConstraint(new PixelConstraint(150f), scaleFactor))
            .setColor(Main.BASE_COLOR)
            .setX(new CenterConstraint())
            .setY(new PixelConstraint(10))
            .setChildOf(list);

        createPartyManagementButtons(topBarBlock, scaleFactor, partyMembers);
        createJoinFloorButtons(topBarBlock, scaleFactor);


        for(PartyMember member : partyMembers) {
            UIComponent memberBlock = new UIBlock()
                .setWidth(new PixelConstraint(list.getWidth()-20f))
                .setHeight(new ScaleConstraint(new PixelConstraint(200f), scaleFactor))
                .setColor(Main.BASE_COLOR)
                .setX(new CenterConstraint())
                .setY(new PixelConstraint(height))
                .setChildOf(list);

                new Thread() {
                    @Override
                    public void run() {
                        if(member.isExpired()) {
                            try {
                                member.getData();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        member.createBlock(memberBlock, scaleFactor);
                        PartyManager.playerCache.put(member.username, member); 
                    }
                    
                }.start();

                loadingText.hide();

            
            height += 220f*scaleFactor;
        }

        loadingText.hide();
        this.getWindow().draw(new UMatrixStack());
    }

    public void createPartyManagementButtons(UIComponent topBarBlock, float scaleFactor, List<PartyMember> partyMembers) {
        UIComponent disbandButton = new UIRoundedRectangle(10f)
                .setX(new PixelConstraint(10f*scaleFactor))
                .setY(new PixelConstraint(10f*scaleFactor))
                .setWidth(new PixelConstraint(75f*scaleFactor))
                .setHeight(new PixelConstraint(55f * scaleFactor))
                .setColor(new Color(212, 111, 98))
                .setChildOf(topBarBlock);
            
        new UIText("Disband")
            .setTextScale(new PixelConstraint(1.5f*scaleFactor))
            .setX(new CenterConstraint())
            .setY(new CenterConstraint())
            .setColor(Color.white)
            .setChildOf(disbandButton);

        disbandButton.onMouseClickConsumer(event -> {
            Main.minecraft.thePlayer.sendChatMessage("/party disband");
        });
            

        UIComponent kickOfflineButton = new UIRoundedRectangle(10f)
            .setX(new PixelConstraint(95f*scaleFactor))
            .setY(new PixelConstraint(10f*scaleFactor))
            .setWidth(new PixelConstraint(100f*scaleFactor))
            .setHeight(new PixelConstraint(55f * scaleFactor))
            .setColor(Main.DARK_ACCENT_COLOR)
            .setChildOf(topBarBlock);
            
        new UIText("Kick Offline")
            .setTextScale(new PixelConstraint(1.5f*scaleFactor))
            .setX(new CenterConstraint())
            .setY(new CenterConstraint())
            .setColor(Color.white)
            .setChildOf(kickOfflineButton);

        kickOfflineButton.onMouseClickConsumer(event -> {
            Main.minecraft.thePlayer.sendChatMessage("/party kickoffline");
        });

        UIComponent repartyButton = new UIRoundedRectangle(10f)
            .setX(new PixelConstraint(205f*scaleFactor))
            .setY(new PixelConstraint(10f*scaleFactor))
            .setWidth(new PixelConstraint(100f*scaleFactor))
            .setHeight(new PixelConstraint(55f*scaleFactor))
            .setColor(Main.DARK_ACCENT_COLOR)
            .setChildOf(topBarBlock);
        
        new UIText("Reparty")
            .setTextScale(new PixelConstraint(1.5f*scaleFactor))
            .setX(new CenterConstraint())
            .setY(new CenterConstraint())
            .setColor(Color.white)
            .setChildOf(repartyButton);
        
            repartyButton.onMouseClickConsumer(event -> {
                PartyManager.reparty(partyMembers);
            });
    

        UIComponent readyRequestButton = new UIRoundedRectangle(10f)
            .setX(new PixelConstraint(315f*scaleFactor))
            .setY(new PixelConstraint(10f*scaleFactor))
            .setWidth(new PixelConstraint(100f*scaleFactor))
            .setHeight(new PixelConstraint(55f*scaleFactor))
            .setColor(Main.DARK_ACCENT_COLOR)
            .setChildOf(topBarBlock);
        
        new UIText("Ask if ready")
            .setTextScale(new PixelConstraint(1.25f*scaleFactor))
            .setX(new CenterConstraint())
            .setY(new CenterConstraint())
            .setColor(Color.white)
            .setChildOf(readyRequestButton);

        readyRequestButton.onMouseClickConsumer(event -> {
            Main.minecraft.thePlayer.sendChatMessage("/pc Everyone Ready?");
        });

        new UIText("Party Size: " + partyMembers.size())
            .setTextScale(new PixelConstraint(2f*scaleFactor))
            .setX(new PixelConstraint(425f*scaleFactor))
            .setY(new PixelConstraint(10f*scaleFactor))
            .setColor(Color.white)
            .setChildOf(topBarBlock);
    }


    public void createJoinFloorButtons(UIComponent topBarBlock, float scaleFactor) {
        new UIText("Join Dungeon Floor:")
            .setTextScale(new PixelConstraint(2*scaleFactor))
            .setX(new PixelConstraint(20f * scaleFactor))
            .setY(new PixelConstraint(115f * scaleFactor))
            .setChildOf(topBarBlock);
        
        UIComponent f1Button = new UIRoundedRectangle(10f)
            .setX(new PixelConstraint(265f*scaleFactor))
            .setY(new PixelConstraint(100*scaleFactor))
            .setWidth(new PixelConstraint(35f*scaleFactor))
            .setHeight(new PixelConstraint(35f * scaleFactor))
            .setColor(Main.DARK_ACCENT_COLOR)
            .setChildOf(topBarBlock);
            
        new UIText("F1")
            .setTextScale(new PixelConstraint(1.1f*scaleFactor))
            .setX(new CenterConstraint())
            .setY(new CenterConstraint())
            .setColor(Color.white)
            .setChildOf(f1Button);

        f1Button.onMouseClickConsumer(event -> {
            Main.minecraft.thePlayer.sendChatMessage("/joindungeon catacombs 0");
        });

        UIComponent f2Button = new UIRoundedRectangle(10f)
            .setX(new PixelConstraint(315f*scaleFactor))
            .setY(new PixelConstraint(100*scaleFactor))
            .setWidth(new PixelConstraint(35f*scaleFactor))
            .setHeight(new PixelConstraint(35f * scaleFactor))
            .setColor(Main.DARK_ACCENT_COLOR)
            .setChildOf(topBarBlock);
            
        new UIText("F2")
            .setTextScale(new PixelConstraint(1.1f*scaleFactor))
            .setX(new CenterConstraint())
            .setY(new CenterConstraint())
            .setColor(Color.white)
            .setChildOf(f2Button);

        f2Button.onMouseClickConsumer(event -> {
            Main.minecraft.thePlayer.sendChatMessage("/joindungeon catacombs 2");
        });

        UIComponent f3Button = new UIRoundedRectangle(10f)
            .setX(new PixelConstraint(365f*scaleFactor))
            .setY(new PixelConstraint(100*scaleFactor))
            .setWidth(new PixelConstraint(35f*scaleFactor))
            .setHeight(new PixelConstraint(35f * scaleFactor))
            .setColor(Main.DARK_ACCENT_COLOR)
            .setChildOf(topBarBlock);
            
        new UIText("F3")
            .setTextScale(new PixelConstraint(1.1f*scaleFactor))
            .setX(new CenterConstraint())
            .setY(new CenterConstraint())
            .setColor(Color.white)
            .setChildOf(f3Button);

        f3Button.onMouseClickConsumer(event -> {
            Main.minecraft.thePlayer.sendChatMessage("/joindungeon catacombs 3");
        });

        UIComponent f4Button = new UIRoundedRectangle(10f)
            .setX(new PixelConstraint(415f*scaleFactor))
            .setY(new PixelConstraint(100*scaleFactor))
            .setWidth(new PixelConstraint(35f*scaleFactor))
            .setHeight(new PixelConstraint(35f * scaleFactor))
            .setColor(Main.DARK_ACCENT_COLOR)
            .setChildOf(topBarBlock);
            
        new UIText("F4")
            .setTextScale(new PixelConstraint(1.1f*scaleFactor))
            .setX(new CenterConstraint())
            .setY(new CenterConstraint())
            .setColor(Color.white)
            .setChildOf(f4Button);

        f4Button.onMouseClickConsumer(event -> {
            Main.minecraft.thePlayer.sendChatMessage("/joindungeon catacombs 4");
        });

        UIComponent f5Button = new UIRoundedRectangle(10f)
            .setX(new PixelConstraint(465f*scaleFactor))
            .setY(new PixelConstraint(100*scaleFactor))
            .setWidth(new PixelConstraint(35f*scaleFactor))
            .setHeight(new PixelConstraint(35f * scaleFactor))
            .setColor(Main.DARK_ACCENT_COLOR)
            .setChildOf(topBarBlock);
            
        new UIText("F5")
            .setTextScale(new PixelConstraint(1.1f*scaleFactor))
            .setX(new CenterConstraint())
            .setY(new CenterConstraint())
            .setColor(Color.white)
            .setChildOf(f5Button);

        f5Button.onMouseClickConsumer(event -> {
            Main.minecraft.thePlayer.sendChatMessage("/joindungeon catacombs 5");
        });

        UIComponent f6Button = new UIRoundedRectangle(10f)
            .setX(new PixelConstraint(515f*scaleFactor))
            .setY(new PixelConstraint(100*scaleFactor))
            .setWidth(new PixelConstraint(35f*scaleFactor))
            .setHeight(new PixelConstraint(35f * scaleFactor))
            .setColor(Main.DARK_ACCENT_COLOR)
            .setChildOf(topBarBlock);
            
        new UIText("F6")
            .setTextScale(new PixelConstraint(1.1f*scaleFactor))
            .setX(new CenterConstraint())
            .setY(new CenterConstraint())
            .setColor(Color.white)
            .setChildOf(f6Button);

        f6Button.onMouseClickConsumer(event -> {
            Main.minecraft.thePlayer.sendChatMessage("/joindungeon catacombs 6");
        });

        

        UIComponent f7Button = new UIRoundedRectangle(10f)
            .setX(new PixelConstraint(565f*scaleFactor))
            .setY(new PixelConstraint(100*scaleFactor))
            .setWidth(new PixelConstraint(35f*scaleFactor))
            .setHeight(new PixelConstraint(35f * scaleFactor))
            .setColor(Main.DARK_ACCENT_COLOR)
            .setChildOf(topBarBlock);
            
        new UIText("F7")
            .setTextScale(new PixelConstraint(1.1f*scaleFactor))
            .setX(new CenterConstraint())
            .setY(new CenterConstraint())
            .setColor(Color.white)
            .setChildOf(f7Button);

        f7Button.onMouseClickConsumer(event -> {
            Main.minecraft.thePlayer.sendChatMessage("/joindungeon catacombs 7");
        });







        UIComponent m1Button = new UIRoundedRectangle(10f)
            .setX(new PixelConstraint(615f*scaleFactor))
            .setY(new PixelConstraint(100*scaleFactor))
            .setWidth(new PixelConstraint(35f*scaleFactor))
            .setHeight(new PixelConstraint(35f * scaleFactor))
            .setColor(Main.DARK_ACCENT_COLOR)
            .setChildOf(topBarBlock);
            
        new UIText("M1")
            .setTextScale(new PixelConstraint(1.1f*scaleFactor))
            .setX(new CenterConstraint())
            .setY(new CenterConstraint())
            .setColor(Color.white)
            .setChildOf(m1Button);

        m1Button.onMouseClickConsumer(event -> {
            Main.minecraft.thePlayer.sendChatMessage("/joindungeon master_catacombs 1");
        });

        UIComponent m2Button = new UIRoundedRectangle(10f)
            .setX(new PixelConstraint(665f*scaleFactor))
            .setY(new PixelConstraint(100*scaleFactor))
            .setWidth(new PixelConstraint(35f*scaleFactor))
            .setHeight(new PixelConstraint(35f * scaleFactor))
            .setColor(Main.DARK_ACCENT_COLOR)
            .setChildOf(topBarBlock);
            
        new UIText("M2")
            .setTextScale(new PixelConstraint(1.1f*scaleFactor))
            .setX(new CenterConstraint())
            .setY(new CenterConstraint())
            .setColor(Color.white)
            .setChildOf(m2Button);

        m2Button.onMouseClickConsumer(event -> {
            Main.minecraft.thePlayer.sendChatMessage("/joindungeon master_catacombs 2");
        });

        UIComponent m3Button = new UIRoundedRectangle(10f)
            .setX(new PixelConstraint(715f*scaleFactor))
            .setY(new PixelConstraint(100*scaleFactor))
            .setWidth(new PixelConstraint(35f*scaleFactor))
            .setHeight(new PixelConstraint(35f * scaleFactor))
            .setColor(Main.DARK_ACCENT_COLOR)
            .setChildOf(topBarBlock);
            
        new UIText("M3")
            .setTextScale(new PixelConstraint(1.1f*scaleFactor))
            .setX(new CenterConstraint())
            .setY(new CenterConstraint())
            .setColor(Color.white)
            .setChildOf(m3Button);

        m3Button.onMouseClickConsumer(event -> {
            Main.minecraft.thePlayer.sendChatMessage("/joindungeon master_catacombs 3");
        });

        UIComponent m4Button = new UIRoundedRectangle(10f)
            .setX(new PixelConstraint(765f*scaleFactor))
            .setY(new PixelConstraint(100*scaleFactor))
            .setWidth(new PixelConstraint(35f*scaleFactor))
            .setHeight(new PixelConstraint(35f * scaleFactor))
            .setColor(Main.DARK_ACCENT_COLOR)
            .setChildOf(topBarBlock);
            
        new UIText("M4")
            .setTextScale(new PixelConstraint(1.1f*scaleFactor))
            .setX(new CenterConstraint())
            .setY(new CenterConstraint())
            .setColor(Color.white)
            .setChildOf(m4Button);

        m4Button.onMouseClickConsumer(event -> {
            Main.minecraft.thePlayer.sendChatMessage("/joindungeon master_catacombs 4");
        });

        UIComponent m5Button = new UIRoundedRectangle(10f)
            .setX(new PixelConstraint(815f*scaleFactor))
            .setY(new PixelConstraint(100*scaleFactor))
            .setWidth(new PixelConstraint(35f*scaleFactor))
            .setHeight(new PixelConstraint(35f * scaleFactor))
            .setColor(Main.DARK_ACCENT_COLOR)
            .setChildOf(topBarBlock);
            
        new UIText("M5")
            .setTextScale(new PixelConstraint(1.1f*scaleFactor))
            .setX(new CenterConstraint())
            .setY(new CenterConstraint())
            .setColor(Color.white)
            .setChildOf(m5Button);

        m5Button.onMouseClickConsumer(event -> {
            Main.minecraft.thePlayer.sendChatMessage("/joindungeon master_catacombs 5");
        });

        UIComponent m6Button = new UIRoundedRectangle(10f)
            .setX(new PixelConstraint(865f*scaleFactor))
            .setY(new PixelConstraint(100*scaleFactor))
            .setWidth(new PixelConstraint(35f*scaleFactor))
            .setHeight(new PixelConstraint(35f * scaleFactor))
            .setColor(Main.DARK_ACCENT_COLOR)
            .setChildOf(topBarBlock);
            
        new UIText("M6")
            .setTextScale(new PixelConstraint(1.1f*scaleFactor))
            .setX(new CenterConstraint())
            .setY(new CenterConstraint())
            .setColor(Color.white)
            .setChildOf(m6Button);

        m6Button.onMouseClickConsumer(event -> {
            Main.minecraft.thePlayer.sendChatMessage("/joindungeon master_catacombs 6");
        });

        UIComponent m7Button = new UIRoundedRectangle(10f)
            .setX(new PixelConstraint(915f*scaleFactor))
            .setY(new PixelConstraint(100*scaleFactor))
            .setWidth(new PixelConstraint(35f*scaleFactor))
            .setHeight(new PixelConstraint(35f * scaleFactor))
            .setColor(Main.DARK_ACCENT_COLOR)
            .setChildOf(topBarBlock);
            
        new UIText("M7")
            .setTextScale(new PixelConstraint(1.1f*scaleFactor))
            .setX(new CenterConstraint())
            .setY(new CenterConstraint())
            .setColor(Color.white)
            .setChildOf(m7Button);

        m7Button.onMouseClickConsumer(event -> {
            Main.minecraft.thePlayer.sendChatMessage("/joindungeon master_catacombs 7");
        });
    }

    
}