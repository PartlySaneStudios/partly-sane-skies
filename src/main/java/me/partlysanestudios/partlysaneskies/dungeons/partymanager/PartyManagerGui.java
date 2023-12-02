//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.dungeons.partymanager;

import gg.essential.elementa.ElementaVersion;
import gg.essential.elementa.UIComponent;
import gg.essential.elementa.WindowScreen;
import gg.essential.elementa.components.ScrollComponent;
import gg.essential.elementa.components.UIBlock;
import gg.essential.elementa.components.UIText;
import gg.essential.elementa.components.Window;
import gg.essential.elementa.constraints.CenterConstraint;
import gg.essential.elementa.constraints.PixelConstraint;
import gg.essential.elementa.constraints.ScaleConstraint;
import gg.essential.universal.UMatrixStack;
import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import me.partlysanestudios.partlysaneskies.system.ThemeManager;
import me.partlysanestudios.partlysaneskies.system.guicomponents.PSSButton;
import me.partlysanestudios.partlysaneskies.utils.ElementaUtils;

import java.awt.*;
import java.net.MalformedURLException;
import java.util.List;

public class PartyManagerGui extends WindowScreen {

    // Creates the background
    UIComponent background = new UIBlock()
            .setX(new CenterConstraint())
            .setY(new CenterConstraint())
            .setWidth(new PixelConstraint(getWindow().getWidth() * .9f))
            .setHeight(new PixelConstraint(getWindow().getHeight() * .9f))
            .setChildOf(getWindow())
            .setColor(new Color(0, 0, 0, 0));

    // Creates the scrollable list
    UIComponent list = new ScrollComponent("", 0f, ThemeManager.getPrimaryColor().toJavaColor(), false, true, false, false, 15f, 1f, null)
            .setWidth(new PixelConstraint(background.getWidth()))
            .setHeight(new PixelConstraint(background.getHeight()))
            .setChildOf(background);


    // Applies the standard PSS background the GUI
    public PartyManagerGui() {
        super(ElementaVersion.V2);
        // Utils.applyBackground(background);
    }

    public void populateGui(List<PartyMember> partyMembers) {

        // Creates a scale factor to multiply the base components by,
        // based on the original creator's (Su386yt's) screen size
        float scaleFactor = (list.getWidth() - 20f) / 967.5f;
        // Sets the height of each of the blocks
        float height = 180f * scaleFactor;
        // Creates the first top black
        UIComponent topBarBlock = new UIBlock()
                .setWidth(new PixelConstraint(list.getWidth() - 20f))
                .setHeight(new ScaleConstraint(new PixelConstraint(150f), scaleFactor))
                .setColor(new Color(0, 0, 0, 0))
                .setX(new CenterConstraint())
                .setY(new PixelConstraint(10))
                .setChildOf(list);


        ElementaUtils.INSTANCE.applyBackground(topBarBlock);

        createPartyManagementButtons(topBarBlock, scaleFactor, partyMembers);
        createJoinFloorButtons(topBarBlock, scaleFactor);

        for (PartyMember member : partyMembers) {
            UIComponent memberBlock = new UIBlock()
                    .setWidth(new PixelConstraint(list.getWidth() - 20f))
                    .setHeight(new ScaleConstraint(new PixelConstraint(200f), scaleFactor))
                    .setColor(new Color(0, 0, 0, 0))
                    .setX(new CenterConstraint())
                    .setY(new PixelConstraint(height))
                    .setChildOf(list);

            ElementaUtils.INSTANCE.applyBackground(memberBlock);

            new Thread(() -> {
                try {
                    member.populateData();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                Window.Companion.enqueueRenderOperation(() -> member.createBlock(memberBlock, scaleFactor));
            }).start();


            height += 220f * scaleFactor;
        }

        this.getWindow().draw(new UMatrixStack());
    }

    public void createPartyManagementButtons(UIComponent topBarBlock, float scaleFactor,
            List<PartyMember> partyMembers) {

        new PSSButton(new Color(255, 0, 0))
                .setX(new PixelConstraint(10f * scaleFactor))
                .setY(new PixelConstraint(10f * scaleFactor))
                .setWidth(75f * scaleFactor)
                .setHeight(55f * scaleFactor)
                .setChildOf(topBarBlock)
                .setText("Disband")
                .setTextScale(1.5f)
                .onMouseClickConsumer(event -> PartlySaneSkies.minecraft.thePlayer.sendChatMessage("/party disband"));

        new PSSButton()
                .setX(new PixelConstraint(95f * scaleFactor))
                .setY(new PixelConstraint(10 * scaleFactor))
                .setWidth(75f * scaleFactor)
                .setHeight(55f * scaleFactor)
                .setChildOf(topBarBlock)
                .setText("Kick Offline")
                .setTextScale(1.25f)
                .onMouseClickConsumer(event -> PartlySaneSkies.minecraft.thePlayer.sendChatMessage("/party kickoffline"));

        new PSSButton()
                .setX(new PixelConstraint(205f * scaleFactor))
                .setY(new PixelConstraint(10 * scaleFactor))
                .setWidth(100f * scaleFactor)
                .setHeight(55f * scaleFactor)
                .setChildOf(topBarBlock)
                .setText("Reparty")
                .setTextScale(1.5f)
                .onMouseClickConsumer(event -> PartyManager.reparty(partyMembers));
        
        new PSSButton()
                .setX(new PixelConstraint(315f * scaleFactor))
                .setY(new PixelConstraint(10 * scaleFactor))
                .setWidth(100f * scaleFactor)
                .setHeight(55f * scaleFactor)
                .setChildOf(topBarBlock)
                .setText("Ask if ready")
                .setTextScale(1.25f)
                .onMouseClickConsumer(event -> PartlySaneSkies.minecraft.thePlayer.sendChatMessage("/pc Ready?"));

        new UIText("Party Size: " + partyMembers.size())
                .setTextScale(new PixelConstraint(2f * scaleFactor))
                .setX(new PixelConstraint(425f * scaleFactor))
                .setY(new PixelConstraint(10f * scaleFactor))
                .setColor(Color.white)
                .setChildOf(topBarBlock);
    }

    public void createJoinFloorButtons(UIComponent topBarBlock, float scaleFactor) {
        new UIText("Join Dungeon Floor:")
                .setTextScale(new PixelConstraint(2 * scaleFactor))
                .setX(new PixelConstraint(20f * scaleFactor))
                .setY(new PixelConstraint(115f * scaleFactor))
                .setChildOf(topBarBlock);

        new PSSButton()
                .setX(new PixelConstraint(265f * scaleFactor))
                .setY(new PixelConstraint(100 * scaleFactor))
                .setWidth(35f * scaleFactor)
                .setHeight(35f * scaleFactor)
                .setChildOf(topBarBlock)
                .setText("F1")
                .setTextScale(scaleFactor)
                .onMouseClickConsumer(event -> PartlySaneSkies.minecraft.thePlayer.sendChatMessage("/joindungeon CATACOMBS_FLOOR_ONE"));

        new PSSButton()
                .setX(new PixelConstraint(315f * scaleFactor))
                .setY(new PixelConstraint(100 * scaleFactor))
                .setWidth(35f * scaleFactor)
                .setHeight(35f * scaleFactor)
                .setChildOf(topBarBlock)
                .setText("F2")
                .setTextScale(scaleFactor)
                .onMouseClickConsumer(event -> PartlySaneSkies.minecraft.thePlayer.sendChatMessage("/joindungeon CATACOMBS_FLOOR_TWO"));

        new PSSButton()
                .setX(new PixelConstraint(365f * scaleFactor))
                .setY(new PixelConstraint(100 * scaleFactor))
                .setWidth(35f * scaleFactor)
                .setHeight(35f * scaleFactor)
                .setChildOf(topBarBlock)
                .setText("F3")
                .setTextScale(scaleFactor)
                .onMouseClickConsumer(event -> PartlySaneSkies.minecraft.thePlayer.sendChatMessage("/joindungeon CATACOMBS_FLOOR_THREE"));

        new PSSButton()
                .setX(new PixelConstraint(415f * scaleFactor))
                .setY(new PixelConstraint(100 * scaleFactor))
                .setWidth(35f * scaleFactor)
                .setHeight(35f * scaleFactor)
                .setChildOf(topBarBlock)
                .setText("F4")
                .setTextScale(scaleFactor)
                .onMouseClickConsumer(event -> PartlySaneSkies.minecraft.thePlayer.sendChatMessage("/joindungeon CATACOMBS_FLOOR_FOUR"));

        new PSSButton()
                .setX(new PixelConstraint(465f * scaleFactor))
                .setY(new PixelConstraint(100 * scaleFactor))
                .setWidth(35f * scaleFactor)
                .setHeight(35f * scaleFactor)
                .setChildOf(topBarBlock)
                .setText("F5")
                .setTextScale(scaleFactor)
                .onMouseClickConsumer(event -> PartlySaneSkies.minecraft.thePlayer.sendChatMessage("/joindungeon CATACOMBS_FLOOR_FIVE"));

        new PSSButton()
                .setX(new PixelConstraint(515f * scaleFactor))
                .setY(new PixelConstraint(100 * scaleFactor))
                .setWidth(35f * scaleFactor)
                .setHeight(35f * scaleFactor)
                .setChildOf(topBarBlock)
                .setText("F6")
                .setTextScale(scaleFactor)
                .onMouseClickConsumer(event -> PartlySaneSkies.minecraft.thePlayer.sendChatMessage("/joindungeon CATACOMBS_FLOOR_SIX"));

        new PSSButton()
                .setX(new PixelConstraint(565f * scaleFactor))
                .setY(new PixelConstraint(100 * scaleFactor))
                .setWidth(35f * scaleFactor)
                .setHeight(35f * scaleFactor)
                .setChildOf(topBarBlock)
                .setText("F7")
                .setTextScale(scaleFactor)
                .onMouseClickConsumer(event -> PartlySaneSkies.minecraft.thePlayer.sendChatMessage("/joindungeon CATACOMBS_FLOOR_SEVEN"));



        new PSSButton()
                .setX(new PixelConstraint(615f * scaleFactor))
                .setY(new PixelConstraint(100 * scaleFactor))
                .setWidth(35f * scaleFactor)
                .setHeight(35f * scaleFactor)
                .setChildOf(topBarBlock)
                .setText("M1")
                .setTextScale(scaleFactor)
                .onMouseClickConsumer(event -> PartlySaneSkies.minecraft.thePlayer.sendChatMessage("/joindungeon MASTER_CATACOMBS_FLOOR_ONE"));

        new PSSButton()
                .setX(new PixelConstraint(665f * scaleFactor))
                .setY(new PixelConstraint(100 * scaleFactor))
                .setWidth(35f * scaleFactor)
                .setHeight(35f * scaleFactor)
                .setChildOf(topBarBlock)
                .setText("M2")
                .setTextScale(scaleFactor)
                .onMouseClickConsumer(event -> PartlySaneSkies.minecraft.thePlayer.sendChatMessage("/joindungeon MASTER_CATACOMBS_FLOOR_TWO"));

        new PSSButton()
                .setX(new PixelConstraint(715f * scaleFactor))
                .setY(new PixelConstraint(100 * scaleFactor))
                .setWidth(35f * scaleFactor)
                .setHeight(35f * scaleFactor)
                .setChildOf(topBarBlock)
                .setText("M3")
                .setTextScale(scaleFactor)
                .onMouseClickConsumer(event -> PartlySaneSkies.minecraft.thePlayer.sendChatMessage("/joindungeon MASTER_CATACOMBS_FLOOR_THREE"));

        new PSSButton()
                .setX(new PixelConstraint(765f * scaleFactor))
                .setY(new PixelConstraint(100 * scaleFactor))
                .setWidth(35f * scaleFactor)
                .setHeight(35f * scaleFactor)
                .setChildOf(topBarBlock)
                .setText("M4")
                .setTextScale(scaleFactor)
                .onMouseClickConsumer(event -> PartlySaneSkies.minecraft.thePlayer.sendChatMessage("/joindungeon MASTER_CATACOMBS_FLOOR_FOUR"));

        new PSSButton()
                .setX(new PixelConstraint(815f * scaleFactor))
                .setY(new PixelConstraint(100 * scaleFactor))
                .setWidth(35f * scaleFactor)
                .setHeight(35f * scaleFactor)
                .setChildOf(topBarBlock)
                .setText("M5")
                .setTextScale(scaleFactor)
                .onMouseClickConsumer(event -> PartlySaneSkies.minecraft.thePlayer.sendChatMessage("/joindungeon MASTER_CATACOMBS_FLOOR_FIVE"));

        new PSSButton()
                .setX(new PixelConstraint(865f * scaleFactor))
                .setY(new PixelConstraint(100 * scaleFactor))
                .setWidth(35f * scaleFactor)
                .setHeight(35f * scaleFactor)
                .setChildOf(topBarBlock)
                .setText("M6")
                .setTextScale(scaleFactor)
                .onMouseClickConsumer(event -> PartlySaneSkies.minecraft.thePlayer.sendChatMessage("/joindungeon MASTER_CATACOMBS_FLOOR_SIX"));

        new PSSButton()
                .setX(new PixelConstraint(915f * scaleFactor))
                .setY(new PixelConstraint(100 * scaleFactor))
                .setWidth(35f * scaleFactor)
                .setHeight(35f * scaleFactor)
                .setChildOf(topBarBlock)
                .setText("M7")
                .setTextScale(scaleFactor)
                .onMouseClickConsumer(event -> PartlySaneSkies.minecraft.thePlayer.sendChatMessage("/joindungeon MASTER_CATACOMBS_FLOOR_SEVEN"));
    }

}
