//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.render.gui.components;

import gg.essential.elementa.UIComponent;
import gg.essential.elementa.components.UIBlock;
import gg.essential.elementa.components.UIImage;
import gg.essential.elementa.constraints.CenterConstraint;
import gg.essential.elementa.constraints.PixelConstraint;
import gg.essential.elementa.constraints.XConstraint;
import gg.essential.elementa.constraints.YConstraint;
import gg.essential.elementa.events.UIClickEvent;
import gg.essential.elementa.utils.ObservableList;
import me.partlysanestudios.partlysaneskies.features.themes.ThemeManager;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.util.function.Consumer;

public class PSSToggle {
    static final ResourceLocation UNSELECTED_IMAGE_PATH = new ResourceLocation("partlysaneskies" , "textures/gui/unselected_toggle.png");
    static final ResourceLocation SELECTED_IMAGE_PATH = new ResourceLocation("partlysaneskies" , "textures/gui/selected_toggle.png");

    float width;
    float height;

    XConstraint xConstraint;
    YConstraint yConstraint;

    boolean isSelected;

    UIBlock backgroundBlock;
    UIImage buttonTexture;

    public PSSToggle() {
        this.isSelected = false;
        this.backgroundBlock = (UIBlock) new UIBlock()
                .setColor(new Color(0, 0,0,0));

        this.buttonTexture = (UIImage) ThemeManager.getCurrentToggleUIImage(false)
                .setChildOf(backgroundBlock);

        this.buttonTexture.onMouseClickConsumer(s -> {
        });
    }

    public PSSToggle toggleState() {
        return this.setState(!this.isSelected);

    }

    public PSSToggle setState(boolean state) {
        this.isSelected = state;

        return this.updateState();
    }

    public PSSToggle updateState() {

        ObservableList<UIComponent> children = buttonTexture.getChildren();

        backgroundBlock.removeChild(buttonTexture);

        if (this.isSelected) {
            this.buttonTexture = (UIImage) ThemeManager.getCurrentToggleUIImage(true)
                    .setWidth(new PixelConstraint(this.width))
                    .setHeight(new PixelConstraint(this.height))
                    .setX(new CenterConstraint())
                    .setY(new CenterConstraint())
                    .setChildOf(this.backgroundBlock);
        }
        else {
            this.buttonTexture = (UIImage) ThemeManager.getCurrentToggleUIImage(false)
                    .setWidth(new PixelConstraint(this.width))
                    .setHeight(new PixelConstraint(this.height))
                    .setX(new CenterConstraint())
                    .setY(new CenterConstraint())
                    .setChildOf(this.backgroundBlock);
        }

        for (UIComponent child : children) {
            child.setChildOf(this.buttonTexture);
        }
        return this;
    }

    public PSSToggle setHeight(float height) {
        backgroundBlock.setHeight(new PixelConstraint(height));
        buttonTexture.setHeight(new PixelConstraint(height))
                .setX(new CenterConstraint())
                .setY(new CenterConstraint());

        this.height = height;

        return this;
    }

    public PSSToggle setWidth(float width) {
        backgroundBlock.setWidth(new PixelConstraint(width));
        buttonTexture.setWidth(new PixelConstraint(width))
                .setX(new CenterConstraint())
                .setY(new CenterConstraint());

        this.width = width;

        return this;
    }

    public PSSToggle setX(XConstraint xPos) {
        backgroundBlock.setX(xPos);
        buttonTexture.setX(new CenterConstraint());

        this.xConstraint = xPos;

        return this;
    }

    public PSSToggle setY(YConstraint yPos) {
        backgroundBlock.setY(yPos);
        buttonTexture.setY(new CenterConstraint());

        this.yConstraint = yPos;

        return this;
    }

//    This method fires when the button has been clicked
    public PSSToggle onMouseClickConsumer(Consumer<UIClickEvent> method) {
        backgroundBlock.onMouseClickConsumer(method);

        return this;
    }

    public UIComponent getComponent() {
        return buttonTexture;
    }

    public PSSToggle insertComponentBeforeBackground(UIComponent component) {
        backgroundBlock.insertChildBefore(component, buttonTexture);

        return this;
    }

    public PSSToggle setChildOf(UIComponent parent) {
        backgroundBlock.setChildOf(parent);

        return this;
    }

    public boolean getState() {
        return isSelected;
    }
}
