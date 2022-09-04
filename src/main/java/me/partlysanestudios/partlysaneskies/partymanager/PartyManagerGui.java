package me.partlysanestudios.partlysaneskies.partymanager;

import java.awt.Color;
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
import me.partlysanestudios.partlysaneskies.utils.Utils;

public class PartyManagerGui extends WindowScreen{

    public HashMap<String, UIComponent> uiComponentMap = new HashMap<String, UIComponent>();

    UIComponent background = new UIBlock()
        .setX(new CenterConstraint())
        .setY(new CenterConstraint())
        .setWidth(new PixelConstraint(getWindow().getWidth()*.9f))
        .setHeight(new PixelConstraint(getWindow().getHeight()*.9f))
        .setChildOf(getWindow())
        .setColor(new Color(32, 33, 36));

    
    UIComponent list = new ScrollComponent("", 0f, new Color(85, 85 ,88), false, true, false, false, 15f, 1f, null)
        .setWidth(new PixelConstraint(background.getWidth()))
        .setHeight(new PixelConstraint(background.getHeight()))
        .setChildOf(background);

    UIComponent loadingText = new UIText("Loading...")
        .setX(new CenterConstraint())
        .setY(new CenterConstraint())
        .setChildOf(background);

        UIComponent scrollBar = new UIBlock()
        .setColor(new Color(85, 85 ,88))
        .setChildOf(list);

    
    public PartyManagerGui() {
        super(ElementaVersion.V2);
        ((ScrollComponent) list).setScrollBarComponent(scrollBar, false, false);
        // background.onMouseEnterRunnable(new Runnable() {
        //     @Override
        //     public void run() {
        //         // Animate, set color, etc.
        //         AnimatingConstraints anim = background.makeAnimation();
        //         anim.setWidthAnimation(Animations.OUT_EXP, 0.5f, new ChildBasedSizeConstraint(2f));
        //         // anim.onCompleteRunnable(() -> {
        //         //     // Trigger new animation or anything.
        //         // });
        //         background.animateTo(anim);
        //     }
        // });
    }
    
    
    public void populateGui(List<PartyMember> partyMembers) {
        float scaleFactor = (list.getWidth()-20f)/967.5f;
        float height = 100f*scaleFactor;
        UIComponent topBarBlock = new UIBlock()
                .setWidth(new PixelConstraint(list.getWidth()-20f))
                .setHeight(new ScaleConstraint(new PixelConstraint(95f), scaleFactor))
                .setColor(new Color(42, 43, 46))
                .setX(new CenterConstraint())
                .setY(new PixelConstraint(10))
                .setChildOf(list);
        

        
        
        UIComponent disbandButton = new UIRoundedRectangle(10f)
                .setX(new PixelConstraint(10f*scaleFactor))
                .setY(new CenterConstraint())
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


        UIComponent kickOfflineButton = new UIRoundedRectangle(10f)
            .setX(new PixelConstraint(95f*scaleFactor))
            .setY(new CenterConstraint())
            .setWidth(new PixelConstraint(100f*scaleFactor))
            .setHeight(new PixelConstraint(55f * scaleFactor))
            .setColor(new Color(61, 90, 181))
            .setChildOf(topBarBlock);
            
        new UIText("Kick Offline")
            .setTextScale(new PixelConstraint(1.5f*scaleFactor))
            .setX(new CenterConstraint())
            .setY(new CenterConstraint())
            .setColor(Color.white)
            .setChildOf(kickOfflineButton);

        kickOfflineButton.onMouseClickConsumer(event -> {
            Utils.visPrint("Test");
        });


        new UIText("Join Dungeon Floor:")
            .setTextScale(new PixelConstraint(2*scaleFactor))
            .setX(new PixelConstraint(206f * scaleFactor))
            .setY(new CenterConstraint())
            .setChildOf(topBarBlock);
        
        UIComponent f1Button = new UIRoundedRectangle(10f)
            .setX(new PixelConstraint(450f*scaleFactor))
            .setY(new PixelConstraint(5))
            .setWidth(new PixelConstraint(55f*scaleFactor))
            .setHeight(new PixelConstraint(35f * scaleFactor))
            .setColor(new Color(61, 90, 181))
            .setChildOf(topBarBlock);
            
        new UIText("Floor 1")
            .setTextScale(new PixelConstraint(1.1f*scaleFactor))
            .setX(new CenterConstraint())
            .setY(new CenterConstraint())
            .setColor(Color.white)
            .setChildOf(f1Button);

        f1Button.onMouseClickConsumer(event -> {
            Utils.visPrint("Test");
        });

        UIComponent f2Button = new UIRoundedRectangle(10f)
            .setX(new PixelConstraint(515f*scaleFactor))
            .setY(new PixelConstraint(5))
            .setWidth(new PixelConstraint(55f*scaleFactor))
            .setHeight(new PixelConstraint(35f * scaleFactor))
            .setColor(new Color(61, 90, 181))
            .setChildOf(topBarBlock);
            
        new UIText("Floor 2")
            .setTextScale(new PixelConstraint(1.1f*scaleFactor))
            .setX(new CenterConstraint())
            .setY(new CenterConstraint())
            .setColor(Color.white)
            .setChildOf(f2Button);

        f2Button.onMouseClickConsumer(event -> {
            Utils.visPrint("Test");
        });

        UIComponent f3Button = new UIRoundedRectangle(10f)
            .setX(new PixelConstraint(580f*scaleFactor))
            .setY(new PixelConstraint(5))
            .setWidth(new PixelConstraint(55f*scaleFactor))
            .setHeight(new PixelConstraint(35f * scaleFactor))
            .setColor(new Color(61, 90, 181))
            .setChildOf(topBarBlock);
            
        new UIText("Floor 3")
            .setTextScale(new PixelConstraint(1.1f*scaleFactor))
            .setX(new CenterConstraint())
            .setY(new CenterConstraint())
            .setColor(Color.white)
            .setChildOf(f3Button);

        f3Button.onMouseClickConsumer(event -> {
            Utils.visPrint("Test");
        });

        UIComponent f4Button = new UIRoundedRectangle(10f)
            .setX(new PixelConstraint(645f*scaleFactor))
            .setY(new PixelConstraint(5))
            .setWidth(new PixelConstraint(55f*scaleFactor))
            .setHeight(new PixelConstraint(35f * scaleFactor))
            .setColor(new Color(61, 90, 181))
            .setChildOf(topBarBlock);
            
        new UIText("Floor 4")
            .setTextScale(new PixelConstraint(1.1f*scaleFactor))
            .setX(new CenterConstraint())
            .setY(new CenterConstraint())
            .setColor(Color.white)
            .setChildOf(f4Button);

        f4Button.onMouseClickConsumer(event -> {
            Utils.visPrint("Test");
        });

        UIComponent f5Button = new UIRoundedRectangle(10f)
            .setX(new PixelConstraint(710f*scaleFactor))
            .setY(new PixelConstraint(5))
            .setWidth(new PixelConstraint(55f*scaleFactor))
            .setHeight(new PixelConstraint(35f * scaleFactor))
            .setColor(new Color(61, 90, 181))
            .setChildOf(topBarBlock);
            
        new UIText("Floor 5")
            .setTextScale(new PixelConstraint(1.1f*scaleFactor))
            .setX(new CenterConstraint())
            .setY(new CenterConstraint())
            .setColor(Color.white)
            .setChildOf(f5Button);

        f5Button.onMouseClickConsumer(event -> {
            Utils.visPrint("Test");
        });

        UIComponent f6Button = new UIRoundedRectangle(10f)
            .setX(new PixelConstraint(775f*scaleFactor))
            .setY(new PixelConstraint(5))
            .setWidth(new PixelConstraint(55f*scaleFactor))
            .setHeight(new PixelConstraint(35f * scaleFactor))
            .setColor(new Color(61, 90, 181))
            .setChildOf(topBarBlock);
            
        new UIText("Floor 6")
            .setTextScale(new PixelConstraint(1.1f*scaleFactor))
            .setX(new CenterConstraint())
            .setY(new CenterConstraint())
            .setColor(Color.white)
            .setChildOf(f6Button);

        f6Button.onMouseClickConsumer(event -> {
            Utils.visPrint("Test");
        });

        

        UIComponent f7Button = new UIRoundedRectangle(10f)
            .setX(new PixelConstraint(840f*scaleFactor))
            .setY(new PixelConstraint(5))
            .setWidth(new PixelConstraint(55f*scaleFactor))
            .setHeight(new PixelConstraint(35f * scaleFactor))
            .setColor(new Color(61, 90, 181))
            .setChildOf(topBarBlock);
            
        new UIText("Floor 7")
            .setTextScale(new PixelConstraint(1.1f*scaleFactor))
            .setX(new CenterConstraint())
            .setY(new CenterConstraint())
            .setColor(Color.white)
            .setChildOf(f7Button);

        f7Button.onMouseClickConsumer(event -> {
            Utils.visPrint("Test");
        });







        UIComponent m1Button = new UIRoundedRectangle(10f)
            .setX(new PixelConstraint(450f*scaleFactor))
            .setY(new PixelConstraint(45))
            .setWidth(new PixelConstraint(55f*scaleFactor))
            .setHeight(new PixelConstraint(35f * scaleFactor))
            .setColor(new Color(61, 90, 181))
            .setChildOf(topBarBlock);
            
        new UIText("Master 1")
            .setTextScale(new PixelConstraint(1.1f*scaleFactor))
            .setX(new CenterConstraint())
            .setY(new CenterConstraint())
            .setColor(Color.white)
            .setChildOf(m1Button);

        m1Button.onMouseClickConsumer(event -> {
            Utils.visPrint("Test");
        });

        UIComponent m2Button = new UIRoundedRectangle(10f)
            .setX(new PixelConstraint(515f*scaleFactor))
            .setY(new PixelConstraint(45))
            .setWidth(new PixelConstraint(55f*scaleFactor))
            .setHeight(new PixelConstraint(35f * scaleFactor))
            .setColor(new Color(61, 90, 181))
            .setChildOf(topBarBlock);
            
        new UIText("Master 2")
            .setTextScale(new PixelConstraint(1.1f*scaleFactor))
            .setX(new CenterConstraint())
            .setY(new CenterConstraint())
            .setColor(Color.white)
            .setChildOf(m2Button);

        m2Button.onMouseClickConsumer(event -> {
            Utils.visPrint("Test");
        });

        UIComponent m3Button = new UIRoundedRectangle(10f)
            .setX(new PixelConstraint(580f*scaleFactor))
            .setY(new PixelConstraint(45))
            .setWidth(new PixelConstraint(55f*scaleFactor))
            .setHeight(new PixelConstraint(35f * scaleFactor))
            .setColor(new Color(61, 90, 181))
            .setChildOf(topBarBlock);
            
        new UIText("Master 3")
            .setTextScale(new PixelConstraint(1.1f*scaleFactor))
            .setX(new CenterConstraint())
            .setY(new CenterConstraint())
            .setColor(Color.white)
            .setChildOf(m3Button);

        m3Button.onMouseClickConsumer(event -> {
            Utils.visPrint("Test");
        });

        UIComponent m4Button = new UIRoundedRectangle(10f)
            .setX(new PixelConstraint(645f*scaleFactor))
            .setY(new PixelConstraint(45))
            .setWidth(new PixelConstraint(55f*scaleFactor))
            .setHeight(new PixelConstraint(35f * scaleFactor))
            .setColor(new Color(61, 90, 181))
            .setChildOf(topBarBlock);
            
        new UIText("Master 4")
            .setTextScale(new PixelConstraint(1.1f*scaleFactor))
            .setX(new CenterConstraint())
            .setY(new CenterConstraint())
            .setColor(Color.white)
            .setChildOf(m4Button);

        m4Button.onMouseClickConsumer(event -> {
            Utils.visPrint("Test");
        });

        UIComponent m5Button = new UIRoundedRectangle(10f)
            .setX(new PixelConstraint(710f*scaleFactor))
            .setY(new PixelConstraint(45))
            .setWidth(new PixelConstraint(55f*scaleFactor))
            .setHeight(new PixelConstraint(35f * scaleFactor))
            .setColor(new Color(61, 90, 181))
            .setChildOf(topBarBlock);
            
        new UIText("Master 5")
            .setTextScale(new PixelConstraint(1.1f*scaleFactor))
            .setX(new CenterConstraint())
            .setY(new CenterConstraint())
            .setColor(Color.white)
            .setChildOf(m5Button);

        m5Button.onMouseClickConsumer(event -> {
            Utils.visPrint("Test");
        });

        UIComponent m6Button = new UIRoundedRectangle(10f)
            .setX(new PixelConstraint(775f*scaleFactor))
            .setY(new PixelConstraint(45))
            .setWidth(new PixelConstraint(55f*scaleFactor))
            .setHeight(new PixelConstraint(35f * scaleFactor))
            .setColor(new Color(61, 90, 181))
            .setChildOf(topBarBlock);
            
        new UIText("Master 6")
            .setTextScale(new PixelConstraint(1.1f*scaleFactor))
            .setX(new CenterConstraint())
            .setY(new CenterConstraint())
            .setColor(Color.white)
            .setChildOf(m6Button);

        m6Button.onMouseClickConsumer(event -> {
            Utils.visPrint("Test");
        });

        

        UIComponent m7Button = new UIRoundedRectangle(10f)
            .setX(new PixelConstraint(840f*scaleFactor))
            .setY(new PixelConstraint(45))
            .setWidth(new PixelConstraint(55f*scaleFactor))
            .setHeight(new PixelConstraint(35f * scaleFactor))
            .setColor(new Color(61, 90, 181))
            .setChildOf(topBarBlock);
            
        new UIText("Master 7")
            .setTextScale(new PixelConstraint(1.1f*scaleFactor))
            .setX(new CenterConstraint())
            .setY(new CenterConstraint())
            .setColor(Color.white)
            .setChildOf(m7Button);

        m7Button.onMouseClickConsumer(event -> {
            Utils.visPrint("Test");
        });




        for(PartyMember member : partyMembers) {

            UIComponent memberBlock = new UIBlock()
                .setWidth(new PixelConstraint(list.getWidth()-20f))
                .setHeight(new ScaleConstraint(new PixelConstraint(200f), scaleFactor))
                .setColor(new Color(42, 43, 46))
                .setX(new CenterConstraint())
                .setY(new PixelConstraint(height))
                .setChildOf(list);
        
            // Name plate
            new UIText(member.username + "")
                .setTextScale(new PixelConstraint(3f*scaleFactor))
                .setX(new PixelConstraint(20f*scaleFactor))
                .setY(new PixelConstraint(20f*scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);

            new UIText("Hypixel Level: " + Utils.round(member.hypixelLevel, 1))
                .setTextScale(new PixelConstraint(1f*scaleFactor))
                .setX(new PixelConstraint(20f*scaleFactor))
                .setY(new PixelConstraint(50f*scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);

            new UIText(member.selectedDungeonClass + "")
                .setTextScale(new PixelConstraint(1f*scaleFactor))
                .setX(new PixelConstraint(150f*scaleFactor))
                .setY(new PixelConstraint(50f*scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);


// Coloumn 1
            new UIText("❤ " + Math.round(member.health))
                .setTextScale((new PixelConstraint(1.333f*scaleFactor)))
                .setX(new PixelConstraint(20f*scaleFactor))
                .setY(new PixelConstraint(75f*scaleFactor))
                .setColor(Utils.colorCodetoColor.get("&c"))
                .setChildOf(memberBlock);

            new UIText("❈ " + Math.round(member.defense))
                .setTextScale((new PixelConstraint(1.333f*scaleFactor)))
                .setX(new PixelConstraint(20f*scaleFactor))
                .setY(new PixelConstraint(90f*scaleFactor))
                .setColor(Utils.colorCodetoColor.get("&a"))
                .setChildOf(memberBlock);

            new UIText("EHP " + Math.round(member.effectHealth))
                .setTextScale((new PixelConstraint(1.3f*scaleFactor)))
                .setX(new PixelConstraint(20f*scaleFactor))
                .setY(new PixelConstraint(105f*scaleFactor))
                .setColor(new Color(45, 133, 48))
                .setChildOf(memberBlock);

            new UIText("✎ " + Math.round(member.intelligence))
                .setTextScale((new PixelConstraint(1.333f*scaleFactor)))
                .setX(new PixelConstraint(20f*scaleFactor))
                .setY(new PixelConstraint(120f*scaleFactor))
                .setColor(Utils.colorCodetoColor.get("&b"))
                .setChildOf(memberBlock);
            
         
            new UIText("Catacombs Level: " + Utils.round(member.catacombsLevel, 2))
                .setTextScale((new PixelConstraint(1.333f*scaleFactor)))
                .setX(new PixelConstraint(20f*scaleFactor))
                .setY(new PixelConstraint(135f*scaleFactor))
                .setColor(Utils.colorCodetoColor.get("&c"))
                .setChildOf(memberBlock);
            
            new UIText("Average Skill Level " + Utils.round(member.averageSkillLevel, 2))
                .setTextScale((new PixelConstraint(1.333f*scaleFactor)))
                .setX(new PixelConstraint(20f*scaleFactor))
                .setY(new PixelConstraint(150f*scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);

            new UIText("Combat Level: " + Utils.round(member.combatLevel, 2))
                .setTextScale((new PixelConstraint(1.333f*scaleFactor)))
                .setX(new PixelConstraint(20f*scaleFactor))
                .setY(new PixelConstraint(165f*scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);




            // Coloumn 2

            new UIText("Secrets: " + member.secretsCount)
                .setTextScale((new PixelConstraint(1.333f*scaleFactor)))
                .setX(new PixelConstraint(150f*scaleFactor))
                .setY(new PixelConstraint(74f*scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);

            new UIText("Secrets Per Run: " + Utils.round(member.secretsPerRun, 2))
                .setTextScale((new PixelConstraint(1.333f*scaleFactor)))
                .setX(new PixelConstraint(150f*scaleFactor))
                .setY(new PixelConstraint(90f*scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);

            new UIText("Total Weight: " + Utils.round((member.senitherWeight + member.senitherWeightOverflow), 2))
                .setTextScale((new PixelConstraint(1.2f*scaleFactor)))
                .setX(new PixelConstraint(150f*scaleFactor))
                .setY(new PixelConstraint(105f*scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);
            new UIText("Sentiher Weight: " + Utils.round((member.senitherWeight), 3))
                .setTextScale((new PixelConstraint(.9f*scaleFactor)))
                .setX(new PixelConstraint(150f*scaleFactor))
                .setY(new PixelConstraint(115f*scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);

            new UIText("Overflow Weight: " + Utils.round((member.senitherWeightOverflow), 3))
                .setTextScale((new PixelConstraint(.9f*scaleFactor)))
                .setX(new PixelConstraint(150f*scaleFactor))
                .setY(new PixelConstraint(125f*scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);


            // Column 3: Runs


            new UIText("Runs:")
                .setTextScale(new PixelConstraint(2.5f*scaleFactor))
                .setX(new PixelConstraint(390f*scaleFactor))
                .setY(new PixelConstraint(20f*scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);

            new UIText("Floor 1: " + Math.round(member.f1Runs))
                .setTextScale(new PixelConstraint(1.3f*scaleFactor))
                .setX(new PixelConstraint(340f*scaleFactor))
                .setY(new PixelConstraint(50f*scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);

            new UIText("Floor 2: " + Math.round(member.f2Runs))
                .setTextScale(new PixelConstraint(1.3f*scaleFactor))
                .setX(new PixelConstraint(340f*scaleFactor))
                .setY(new PixelConstraint(70f*scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);
            
            new UIText("Floor 3: " + Math.round(member.f3Runs))
                .setTextScale(new PixelConstraint(1.3f*scaleFactor))
                .setX(new PixelConstraint(340f*scaleFactor))
                .setY(new PixelConstraint(90f*scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);
            
            new UIText("Floor 4: " + Math.round(member.f4Runs))
                .setTextScale(new PixelConstraint(1.3f*scaleFactor))
                .setX(new PixelConstraint(340f*scaleFactor))
                .setY(new PixelConstraint(110f*scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);
            
            new UIText("Floor 5: " + Math.round(member.f5Runs))
                .setTextScale(new PixelConstraint(1.3f*scaleFactor))
                .setX(new PixelConstraint(340f*scaleFactor))
                .setY(new PixelConstraint(130f*scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);
            
            new UIText("Floor 6: " + Math.round(member.f6Runs))
                .setTextScale(new PixelConstraint(1.3f*scaleFactor))
                .setX(new PixelConstraint(340f*scaleFactor))
                .setY(new PixelConstraint(150f*scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);

            new UIText("Floor 7: " + Math.round(member.f7Runs))
                .setTextScale(new PixelConstraint(1.3f*scaleFactor))
                .setX(new PixelConstraint(340f*scaleFactor))
                .setY(new PixelConstraint(170f*scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);



            new UIText("Master 1: " + Math.round(member.m1Runs))
                .setTextScale(new PixelConstraint(1.3f*scaleFactor))
                .setX(new PixelConstraint(460f*scaleFactor))
                .setY(new PixelConstraint(50f*scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);

            new UIText("Master 2: " + Math.round(member.m2Runs))
                .setTextScale(new PixelConstraint(1.3f*scaleFactor))
                .setX(new PixelConstraint(460f*scaleFactor))
                .setY(new PixelConstraint(70f*scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);
            
            new UIText("Master 3: " + Math.round(member.m3Runs))
                .setTextScale(new PixelConstraint(1.3f*scaleFactor))
                .setX(new PixelConstraint(460f*scaleFactor))
                .setY(new PixelConstraint(90f*scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);
            
            new UIText("Master 4: " + Math.round(member.m4Runs))
                .setTextScale(new PixelConstraint(1.3f*scaleFactor))
                .setX(new PixelConstraint(460f*scaleFactor))
                .setY(new PixelConstraint(110f*scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);
            
            new UIText("Master 5: " + Math.round(member.m5Runs))
                .setTextScale(new PixelConstraint(1.3f*scaleFactor))
                .setX(new PixelConstraint(460f*scaleFactor))
                .setY(new PixelConstraint(130f*scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);
            
            new UIText("Master 6: " + Math.round(member.m6Runs))
                .setTextScale(new PixelConstraint(1.3f*scaleFactor))
                .setX(new PixelConstraint(460f*scaleFactor))
                .setY(new PixelConstraint(150f*scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);

            new UIText("Master 7: " + Math.round(member.m7Runs))
                .setTextScale(new PixelConstraint(1.3f*scaleFactor))
                .setX(new PixelConstraint(460f*scaleFactor))
                .setY(new PixelConstraint(170f*scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);



            // Coloumn 4 gear:

            new UIText("Gear:")
                .setTextScale(new PixelConstraint(2.5f*scaleFactor))
                .setX(new PixelConstraint(580f*scaleFactor))
                .setY(new PixelConstraint(20f*scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);

            new UIText("" + member.helmetName)
                
                .setTextScale(new PixelConstraint(1.15f*scaleFactor))
                .setX(new PixelConstraint(580f*scaleFactor))
                .setY(new PixelConstraint(50f*scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);
            
            new UIText("" + member.chestplateName)
                .setTextScale(new PixelConstraint(1.15f*scaleFactor))
                .setX(new PixelConstraint(580f*scaleFactor))
                .setY(new PixelConstraint(85f*scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);

            new UIText("" + member.leggingsName)
                .setTextScale(new PixelConstraint(1.15f*scaleFactor))
                .setX(new PixelConstraint(580f*scaleFactor))
                .setY(new PixelConstraint(120f*scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);

            new UIText("" + member.bootsName)
                .setTextScale(new PixelConstraint(1.15f*scaleFactor))
                .setX(new PixelConstraint(580f*scaleFactor))
                .setY(new PixelConstraint(155f*scaleFactor))
                .setColor(Color.white)
                .setChildOf(memberBlock);




            // Coloumn 5 Buttons
            UIComponent kickButton = new UIRoundedRectangle(10f)
                .setX(new PixelConstraint(800f*scaleFactor))
                .setY(new PixelConstraint(15f * scaleFactor))
                .setWidth(new PixelConstraint(125f*scaleFactor))
                .setHeight(new PixelConstraint(55f * scaleFactor))
                .setColor(new Color(61, 90, 181))
                .setChildOf(memberBlock);
            
            new UIText("Kick")
                .setTextScale(new PixelConstraint(2*scaleFactor))
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setColor(Color.white)
                .setChildOf(kickButton);

            kickButton.onMouseClickConsumer(event -> {
                Utils.visPrint("Test");
            });

        

            UIComponent promoteButton = new UIRoundedRectangle(10f)
                .setX(new PixelConstraint(800f*scaleFactor))
                .setY(new PixelConstraint(75 * scaleFactor))
                .setWidth(new PixelConstraint(125f*scaleFactor))
                .setHeight(new PixelConstraint(55f * scaleFactor))
                .setColor(new Color(61, 90, 181))
                .setChildOf(memberBlock);
            
            new UIText("Promote")

                .setTextScale(new PixelConstraint(2*scaleFactor))
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setColor(Color.white)
                .setChildOf(promoteButton);

            promoteButton.onMouseClickConsumer(event -> {
                Utils.visPrint("Test");
            });



            UIComponent transferButton = new UIRoundedRectangle(10f)
                .setX(new PixelConstraint(800f*scaleFactor))
                .setY(new PixelConstraint(135f * scaleFactor))
                .setWidth(new PixelConstraint(125f*scaleFactor))
                .setHeight(new PixelConstraint(55f * scaleFactor))
                .setColor(new Color(61, 90, 181))
                .setChildOf(memberBlock);
            
            new UIText("Transfer")
                .setTextScale(new PixelConstraint(2*scaleFactor))
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setColor(Color.white)
                .setChildOf(transferButton);

            transferButton.onMouseClickConsumer(event -> {
                Utils.visPrint("Test");
            });



            
            uiComponentMap.put(member.username, memberBlock);
            height += 220f*scaleFactor;
        }

        loadingText.hide();
        this.getWindow().draw(new UMatrixStack());
    }
}
