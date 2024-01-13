//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.render.gui.components;

import cc.polyfrost.oneconfig.config.core.OneColor;
import gg.essential.elementa.UIComponent;
import gg.essential.elementa.components.UIBlock;
import gg.essential.elementa.components.UIImage;
import gg.essential.elementa.components.UIWrappedText;
import gg.essential.elementa.constraints.CenterConstraint;
import gg.essential.elementa.constraints.PixelConstraint;
import gg.essential.elementa.constraints.XConstraint;
import gg.essential.elementa.constraints.YConstraint;
import gg.essential.elementa.events.UIClickEvent;
import me.partlysanestudios.partlysaneskies.features.themes.ThemeManager;

import java.awt.*;
import java.util.function.Consumer;

public class PSSButton {
    UIBlock backgroundBlock;
    UIImage buttonTexture;
    UIWrappedText textComponent;

    float width;
    float height;
    XConstraint xConstraint;
    YConstraint yConstraint;
    OneColor color;
    String text;

    public PSSButton() {
        this.text = "";

        backgroundBlock = (UIBlock) new UIBlock()
            .setColor(new Color(0, 0, 0, 0));
        
        buttonTexture = (UIImage) ThemeManager.getCurrentButtonUIImage()
            .setChildOf(backgroundBlock);

        textComponent = (UIWrappedText) new UIWrappedText(text, false, new Color(0, 0, 0, 0), true)
            .setColor(new Color(255, 255, 255, 255))
            .setChildOf(buttonTexture);
        
        this.color = new OneColor(0, 0, 0, 0);
    }

    public PSSButton(Color color){
        this(new OneColor(color));
    }

    public PSSButton(OneColor color) {
        this.text = "";

        this.backgroundBlock = (UIBlock) new UIBlock()
            .setColor(color.toJavaColor());
    
        this.buttonTexture = (UIImage) ThemeManager.getCurrentButtonUIImage(color)
            .setChildOf(backgroundBlock);

        this.textComponent = (UIWrappedText) new UIWrappedText(text, false, new Color(0, 0, 0, 0), true)
            .setColor(new Color(255, 255, 255, 255))
            .setChildOf(buttonTexture);

        this.color = color;
    }


    public PSSButton setHeight(float height) {
        backgroundBlock.setHeight(new PixelConstraint(height));
        buttonTexture.setHeight(new PixelConstraint(height));

        this.height = height;

        return this;
    }

    public PSSButton setWidth(float width) {
        backgroundBlock.setWidth(new PixelConstraint(width));
        buttonTexture.setWidth(new PixelConstraint(width));
        textComponent.setWidth(new PixelConstraint(width));

        this.width = width;

        return this;
    }

    public PSSButton setX(XConstraint xPos) {
        backgroundBlock.setX(xPos);
        buttonTexture.setX(new CenterConstraint());
        textComponent.setX(new CenterConstraint());

        this.xConstraint = xPos;

        return this;
    }

    public PSSButton setY(YConstraint yPos) {
        backgroundBlock.setY(yPos);
        buttonTexture.setY(new CenterConstraint());
        textComponent.setY(new CenterConstraint());
        
        this.yConstraint = yPos;

        return this;
    }

    public PSSButton setChildOf(UIComponent parent) {
        backgroundBlock.setChildOf(parent);

        return this;
    }

    public PSSButton setColor(Color color) {
        return setColor(new OneColor(color));
    }
    public PSSButton setColor(OneColor color) {
        backgroundBlock.removeChild(buttonTexture);

        buttonTexture = (UIImage) ThemeManager.getCurrentButtonUIImage(color)
            .setWidth(new PixelConstraint(this.width))
            .setHeight(new PixelConstraint(this.height))
            .setX(new CenterConstraint())
            .setY(new CenterConstraint())
            .setChildOf(this.backgroundBlock);

        backgroundBlock.setColor(color.toJavaColor());

        return this;
    }

    public PSSButton setDefaultColor() {
        backgroundBlock.removeChild(buttonTexture);

        buttonTexture = (UIImage) ThemeManager.getCurrentButtonUIImage()
            .setWidth(new PixelConstraint(this.width))
            .setHeight(new PixelConstraint(this.height))
            .setX(new CenterConstraint())
            .setY(new CenterConstraint())
            .setChildOf(this.backgroundBlock);

        backgroundBlock.setColor(new Color(0, 0, 0, 0));

        this.color = new OneColor(0, 0, 0, 0);
        return this;
    }

    public PSSButton setText(String text) {
        this.textComponent.setText(text);
        this.text = text;

        return this;
    }

    public PSSButton setTextScale(float scale) {
        textComponent.setTextScale(new PixelConstraint(scale));

        return this;
    }

    public PSSButton onMouseClickConsumer(Consumer<UIClickEvent> method) {
        backgroundBlock.onMouseClickConsumer(method);

        return this;
    }


    public UIComponent getComponent() {
        return buttonTexture;
    }

    public PSSButton setBackgroundVisibility(boolean val) {
        if (val) {
            buttonTexture.unhide(true);
            for (UIComponent child : buttonTexture.getChildren()) {
                child.unhide(true);
            }
        } else {
            buttonTexture.hide();
            for (UIComponent child : buttonTexture.getChildren()) {
                child.unhide(true);
            }
        }

        return this;
    }

    public PSSButton insertComponentBeforeBackground(UIComponent component) {
        backgroundBlock.insertChildBefore(component, buttonTexture);

        return this;
    }
}
