package me.partlysanestudios.partlysaneskies.partymanager;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;

import gg.essential.elementa.ElementaVersion;
import gg.essential.elementa.UIComponent;
import gg.essential.elementa.WindowScreen;
import gg.essential.elementa.components.ScrollComponent;
import gg.essential.elementa.components.UIBlock;
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
        float height = 10f*scaleFactor;
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



            // Coloumn 3 gear:

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



            
            uiComponentMap.put(member.username, memberBlock);
            height += 220f*scaleFactor;
        }

        loadingText.hide();
        this.getWindow().draw(new UMatrixStack());
    }
}
