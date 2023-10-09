/*
 * Partly Sane Skies: A Hypixel Skyblock QOL and Economy mod
 * Created by Su386#9878 (Su386yt) and FlagMaster#1516 (FlagHater), the Partly Sane Studios team
 * Copyright (C) ©️ Su386 and FlagMaster 2023
 * This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 * 
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 * 
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package me.partlysanestudios.partlysaneskies.dungeons.partymanager;

import java.awt.Color;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;

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
import me.partlysanestudios.partlysaneskies.utils.Utils;
import me.partlysanestudios.partlysaneskies.utils.guicomponents.UIButton;
import me.partlysanestudios.partlysaneskies.utils.requests.Request;
import me.partlysanestudios.partlysaneskies.utils.requests.RequestsManager;

public class PartyManagerGui extends WindowScreen {

    public HashMap<String, UIComponent> uiComponentMap = new HashMap<String, UIComponent>();

    // Creates the background
    UIComponent background = new UIBlock()
            .setX(new CenterConstraint())
            .setY(new CenterConstraint())
            .setWidth(new PixelConstraint(getWindow().getWidth() * .9f))
            .setHeight(new PixelConstraint(getWindow().getHeight() * .9f))
            .setChildOf(getWindow())
            .setColor(new Color(0, 0, 0, 0));

    // Creates the scrollable list
    UIComponent list = new ScrollComponent("", 0f, PartlySaneSkies.BASE_LIGHT_COLOR, false, true, false, false, 15f, 1f, null)
            .setWidth(new PixelConstraint(background.getWidth()))
            .setHeight(new PixelConstraint(background.getHeight()))
            .setChildOf(background);


    // Applies the standard PSS background the the GUI
    public PartyManagerGui() {
        super(ElementaVersion.V2);
        // Utils.applyBackground(background);
    }

    public void populateGui(List<PartyMember> partyMembers) {

        // Creates a scale factor to multiply the base componenets by, based on the original creator's (Su386yt's) screensize
        float scaleFactor = (list.getWidth() - 20f) / 967.5f;
        // Sets the height of each of the the blocks
        float height = 180f * scaleFactor;
        // Creates the first top black
        UIComponent topBarBlock = new UIBlock()
                .setWidth(new PixelConstraint(list.getWidth() - 20f))
                .setHeight(new ScaleConstraint(new PixelConstraint(150f), scaleFactor))
                .setColor(new Color(0, 0, 0, 0))
                .setX(new CenterConstraint())
                .setY(new PixelConstraint(10))
                .setChildOf(list);
        
        
        Utils.applyBackground(topBarBlock);

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

            Utils.applyBackground(memberBlock);
            if (member.isExpired()) {
                try {
                    RequestsManager.newRequest(new Request("https://sky.shiiyu.moe/api/v2/profile/" + member.username, s -> {
                        try {
                            member.setSkycryptData(s.getResponse());
                            member.createBlock(memberBlock, scaleFactor);
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                        
                    }));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            } else {
                Window.Companion.enqueueRenderOperation(() -> {
                    member.createBlock(memberBlock, scaleFactor);
                });
            }

            height += 220f * scaleFactor;
        }

        this.getWindow().draw(new UMatrixStack());
    }

    public void createPartyManagementButtons(UIComponent topBarBlock, float scaleFactor,
            List<PartyMember> partyMembers) {

        new UIButton(new Color(255, 0, 0))
                .setX(new PixelConstraint(10f * scaleFactor))
                .setY(new PixelConstraint(10f * scaleFactor))
                .setWidth(75f * scaleFactor)
                .setHeight(55f * scaleFactor)
                .setChildOf(topBarBlock)
                .setText("Disband")
                .setTextScale(1.5f)
                .onMouseClickConsumer(event -> {
                    PartlySaneSkies.minecraft.thePlayer.sendChatMessage("/party disband");
                });

        new UIButton()
                .setX(new PixelConstraint(95f * scaleFactor))
                .setY(new PixelConstraint(10 * scaleFactor))
                .setWidth(75f * scaleFactor)
                .setHeight(55f * scaleFactor)
                .setChildOf(topBarBlock)
                .setText("Kick Offline")
                .setTextScale(1.25f)
                .onMouseClickConsumer(event -> {
                    PartlySaneSkies.minecraft.thePlayer.sendChatMessage("/party kickoffline");
                });

        new UIButton()
                .setX(new PixelConstraint(205f * scaleFactor))
                .setY(new PixelConstraint(10 * scaleFactor))
                .setWidth(100f * scaleFactor)
                .setHeight(55f * scaleFactor)
                .setChildOf(topBarBlock)
                .setText("Reparty")
                .setTextScale(1.5f)
                .onMouseClickConsumer(event -> {
                    PartyManager.reparty(partyMembers);
                });
        
        new UIButton()
                .setX(new PixelConstraint(315f * scaleFactor))
                .setY(new PixelConstraint(10 * scaleFactor))
                .setWidth(100f * scaleFactor)
                .setHeight(55f * scaleFactor)
                .setChildOf(topBarBlock)
                .setText("Ask if ready")
                .setTextScale(1.25f)
                .onMouseClickConsumer(event -> {
                    PartlySaneSkies.minecraft.thePlayer.sendChatMessage("/pc Ready?");
                });

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

        new UIButton()
                .setX(new PixelConstraint(265f * scaleFactor))
                .setY(new PixelConstraint(100 * scaleFactor))
                .setWidth(35f * scaleFactor)
                .setHeight(35f * scaleFactor)
                .setChildOf(topBarBlock)
                .setText("F1")
                .setTextScale(1f * scaleFactor)
                .onMouseClickConsumer(event -> {
                    PartlySaneSkies.minecraft.thePlayer.sendChatMessage("/joindungeon catacombs 1");
                });

        new UIButton()
                .setX(new PixelConstraint(315f * scaleFactor))
                .setY(new PixelConstraint(100 * scaleFactor))
                .setWidth(35f * scaleFactor)
                .setHeight(35f * scaleFactor)
                .setChildOf(topBarBlock)
                .setText("F2")
                .setTextScale(1f * scaleFactor)
                .onMouseClickConsumer(event -> {
                    PartlySaneSkies.minecraft.thePlayer.sendChatMessage("/joindungeon catacombs 2");
                });

        new UIButton()
                .setX(new PixelConstraint(365f * scaleFactor))
                .setY(new PixelConstraint(100 * scaleFactor))
                .setWidth(35f * scaleFactor)
                .setHeight(35f * scaleFactor)
                .setChildOf(topBarBlock)
                .setText("F3")
                .setTextScale(1f * scaleFactor)
                .onMouseClickConsumer(event -> {
                    PartlySaneSkies.minecraft.thePlayer.sendChatMessage("/joindungeon catacombs 3");
                });

        new UIButton()
                .setX(new PixelConstraint(415f * scaleFactor))
                .setY(new PixelConstraint(100 * scaleFactor))
                .setWidth(35f * scaleFactor)
                .setHeight(35f * scaleFactor)
                .setChildOf(topBarBlock)
                .setText("F4")
                .setTextScale(1f * scaleFactor)
                .onMouseClickConsumer(event -> {
                    PartlySaneSkies.minecraft.thePlayer.sendChatMessage("/joindungeon catacombs 4");
                });

        new UIButton()
                .setX(new PixelConstraint(465f * scaleFactor))
                .setY(new PixelConstraint(100 * scaleFactor))
                .setWidth(35f * scaleFactor)
                .setHeight(35f * scaleFactor)
                .setChildOf(topBarBlock)
                .setText("F5")
                .setTextScale(1f * scaleFactor)
                .onMouseClickConsumer(event -> {
                    PartlySaneSkies.minecraft.thePlayer.sendChatMessage("/joindungeon catacombs 5");
                });

        new UIButton()
                .setX(new PixelConstraint(515f * scaleFactor))
                .setY(new PixelConstraint(100 * scaleFactor))
                .setWidth(35f * scaleFactor)
                .setHeight(35f * scaleFactor)
                .setChildOf(topBarBlock)
                .setText("F6")
                .setTextScale(1f * scaleFactor)
                .onMouseClickConsumer(event -> {
                    PartlySaneSkies.minecraft.thePlayer.sendChatMessage("/joindungeon catacombs 6");
                });

        new UIButton()
                .setX(new PixelConstraint(565f * scaleFactor))
                .setY(new PixelConstraint(100 * scaleFactor))
                .setWidth(35f * scaleFactor)
                .setHeight(35f * scaleFactor)
                .setChildOf(topBarBlock)
                .setText("F7")
                .setTextScale(1f * scaleFactor)
                .onMouseClickConsumer(event -> {
                    PartlySaneSkies.minecraft.thePlayer.sendChatMessage("/joindungeon catacombs 7");
                });



        new UIButton()
                .setX(new PixelConstraint(615f * scaleFactor))
                .setY(new PixelConstraint(100 * scaleFactor))
                .setWidth(35f * scaleFactor)
                .setHeight(35f * scaleFactor)
                .setChildOf(topBarBlock)
                .setText("M1")
                .setTextScale(1f * scaleFactor)
                .onMouseClickConsumer(event -> {
                    PartlySaneSkies.minecraft.thePlayer.sendChatMessage("/joindungeon master_catacombs 1");
                });

        new UIButton()
                .setX(new PixelConstraint(665f * scaleFactor))
                .setY(new PixelConstraint(100 * scaleFactor))
                .setWidth(35f * scaleFactor)
                .setHeight(35f * scaleFactor)
                .setChildOf(topBarBlock)
                .setText("M2")
                .setTextScale(1f * scaleFactor)
                .onMouseClickConsumer(event -> {
                    PartlySaneSkies.minecraft.thePlayer.sendChatMessage("/joindungeon master_catacombs 2");
                });

        new UIButton()
                .setX(new PixelConstraint(715f * scaleFactor))
                .setY(new PixelConstraint(100 * scaleFactor))
                .setWidth(35f * scaleFactor)
                .setHeight(35f * scaleFactor)
                .setChildOf(topBarBlock)
                .setText("M3")
                .setTextScale(1f * scaleFactor)
                .onMouseClickConsumer(event -> {
                    PartlySaneSkies.minecraft.thePlayer.sendChatMessage("/joindungeon master_catacombs 3");
                });

        new UIButton()
                .setX(new PixelConstraint(765f * scaleFactor))
                .setY(new PixelConstraint(100 * scaleFactor))
                .setWidth(35f * scaleFactor)
                .setHeight(35f * scaleFactor)
                .setChildOf(topBarBlock)
                .setText("M4")
                .setTextScale(1f * scaleFactor)
                .onMouseClickConsumer(event -> {
                    PartlySaneSkies.minecraft.thePlayer.sendChatMessage("/joindungeon master_catacombs 4");
                });

        new UIButton()
                .setX(new PixelConstraint(815f * scaleFactor))
                .setY(new PixelConstraint(100 * scaleFactor))
                .setWidth(35f * scaleFactor)
                .setHeight(35f * scaleFactor)
                .setChildOf(topBarBlock)
                .setText("M5")
                .setTextScale(1f * scaleFactor)
                .onMouseClickConsumer(event -> {
                    PartlySaneSkies.minecraft.thePlayer.sendChatMessage("/joindungeon master_catacombs 5");
                });

        new UIButton()
                .setX(new PixelConstraint(865f * scaleFactor))
                .setY(new PixelConstraint(100 * scaleFactor))
                .setWidth(35f * scaleFactor)
                .setHeight(35f * scaleFactor)
                .setChildOf(topBarBlock)
                .setText("M6")
                .setTextScale(1f * scaleFactor)
                .onMouseClickConsumer(event -> {
                    PartlySaneSkies.minecraft.thePlayer.sendChatMessage("/joindungeon master_catacombs 6");
                });

        new UIButton()
                .setX(new PixelConstraint(915f * scaleFactor))
                .setY(new PixelConstraint(100 * scaleFactor))
                .setWidth(35f * scaleFactor)
                .setHeight(35f * scaleFactor)
                .setChildOf(topBarBlock)
                .setText("M7")
                .setTextScale(1f * scaleFactor)
                .onMouseClickConsumer(event -> {
                    PartlySaneSkies.minecraft.thePlayer.sendChatMessage("/joindungeon master_catacombs 7");
                });
    }

}