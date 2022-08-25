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
import gg.essential.elementa.effects.ScissorEffect;
import gg.essential.universal.UMatrixStack;

public class PartyManagerGui extends WindowScreen{

    public HashMap<String, UIComponent> uiComponentMap = new HashMap<String, UIComponent>();

    UIComponent background = new UIBlock()
        .setX(new CenterConstraint())
        .setY(new CenterConstraint())
        .setWidth(new PixelConstraint(getWindow().getWidth()*.9f))
        .setHeight(new PixelConstraint(getWindow().getHeight()*.9f))
        .setChildOf(getWindow())
        .setColor(new Color(32, 33, 36))
        .enableEffect(new ScissorEffect());

    
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
    
    float height = 10f;
    public void populateGui(List<PartyMember> partyMembers) {
        for(PartyMember member : partyMembers) {

            UIComponent memberBlock = new UIBlock()
                .setWidth(new PixelConstraint(list.getWidth()-20f))
                .setHeight(new PixelConstraint(200f))
                .setColor(new Color(42, 43, 46))
                .setX(new CenterConstraint())
                .setY(new PixelConstraint(height))
                .setChildOf(list);
            
            new UIText(member.username)
                .setX(new PixelConstraint(20f))
                .setY(new PixelConstraint(20f))
                .setColor(Color.white)
                .setChildOf(memberBlock);
            
            uiComponentMap.put(member.username, memberBlock);
            height += 220;
        }

        loadingText.hide();
        this.getWindow().draw(new UMatrixStack());
    }
}
