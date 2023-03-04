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

package me.partlysanestudios.partlysaneskies.utils.guicomponents;

import java.awt.Color;
import java.util.function.Consumer;

import gg.essential.elementa.UIComponent;
import gg.essential.elementa.components.UIBlock;
import gg.essential.elementa.components.UIImage;
import gg.essential.elementa.components.UIWrappedText;
import gg.essential.elementa.constraints.CenterConstraint;
import gg.essential.elementa.constraints.PixelConstraint;
import gg.essential.elementa.constraints.XConstraint;
import gg.essential.elementa.constraints.YConstraint;
import gg.essential.elementa.events.UIClickEvent;
import me.partlysanestudios.partlysaneskies.utils.Utils;
import net.minecraft.util.ResourceLocation;

public class UIButton {
    UIBlock backgroundBlock;
    UIImage buttonTexture;
    UIWrappedText textComponent;

    float width;
    float height;
    XConstraint xConstraint;
    YConstraint yConstraint;
    Color color;
    String text;

    public UIButton() {
        this.text = "";

        backgroundBlock = (UIBlock) new UIBlock()
            .setColor(new Color(0, 0, 0, 0));
        
        buttonTexture = (UIImage) Utils.uiimageFromResourceLocation(new ResourceLocation("partlysaneskies:textures/gui/base_color_button.png"))
            .setChildOf(backgroundBlock);

        textComponent = (UIWrappedText) new UIWrappedText(text, false, new Color(0, 0, 0, 0), true)
            .setColor(new Color(255, 255, 255, 255))
            .setChildOf(buttonTexture);
        
        this.color = new Color(0, 0, 0, 0);
    }

    public UIButton(Color color) {
        this.text = "";

        this.backgroundBlock = (UIBlock) new UIBlock()
            .setColor(color);
    
        this.buttonTexture = (UIImage) Utils.uiimageFromResourceLocation(new ResourceLocation("partlysaneskies:textures/gui/base_color_button_transparent.png"))
            .setChildOf(backgroundBlock);

        this.textComponent = (UIWrappedText) new UIWrappedText(text, false, new Color(0, 0, 0, 0), true)
            .setColor(new Color(255, 255, 255, 255))
            .setChildOf(buttonTexture);

        this.color = color;
    }


    public UIButton setHeight(float height) {
        backgroundBlock.setHeight(new PixelConstraint(height));
        buttonTexture.setHeight(new PixelConstraint(height));

        this.height = height;

        return this;
    }

    public UIButton setWidth(float width) {
        backgroundBlock.setWidth(new PixelConstraint(width));
        buttonTexture.setWidth(new PixelConstraint(width));
        textComponent.setWidth(new PixelConstraint(width));

        this.width = width;

        return this;
    }

    public UIButton setX(XConstraint xPos) {
        backgroundBlock.setX(xPos);
        buttonTexture.setX(new CenterConstraint());
        textComponent.setX(new CenterConstraint());

        this.xConstraint = xPos;

        return this;
    }

    public UIButton setY(YConstraint yPos) {
        backgroundBlock.setY(yPos);
        buttonTexture.setY(new CenterConstraint());
        textComponent.setY(new CenterConstraint());
        
        this.yConstraint = yPos;

        return this;
    }

    public UIButton setChildOf(UIComponent parent) {
        backgroundBlock.setChildOf(parent);

        

        return this;
    }

    public UIButton setColor(Color color) {
        backgroundBlock.removeChild(buttonTexture);

        buttonTexture = (UIImage) Utils.uiimageFromResourceLocation(new ResourceLocation("partlysaneskies:textures/gui/base_color_button_transparent.png"))
            .setWidth(new PixelConstraint(this.width))
            .setHeight(new PixelConstraint(this.height))
            .setX(new CenterConstraint())
            .setY(new CenterConstraint())
            .setChildOf(this.backgroundBlock);

        backgroundBlock.setColor(color);

        return this;
    }

    public UIButton setDefaultColour() {
        backgroundBlock.removeChild(buttonTexture);

        buttonTexture = (UIImage) Utils.uiimageFromResourceLocation(new ResourceLocation("partlysaneskies:textures/gui/base_color_button.png"))
            .setWidth(new PixelConstraint(this.width))
            .setHeight(new PixelConstraint(this.height))
            .setX(new CenterConstraint())
            .setY(new CenterConstraint())
            .setChildOf(this.backgroundBlock);

        backgroundBlock.setColor(new Color(0, 0, 0, 0));

        this.color = new Color(0, 0, 0, 0);
        return this;
    }

    public UIButton setText(String text) {
        this.textComponent.setText(text);
        this.text = text;

        return this;
    }

    public UIButton setTextScale(float scale) {
        textComponent.setTextScale(new PixelConstraint(scale));

        return this;
    }

    public UIButton onMouseClickConsumer(Consumer<UIClickEvent> method) {
        backgroundBlock.onMouseClickConsumer(method);

        return this;
    }


    public UIComponent getComponent() {
        return buttonTexture;
    }
}
