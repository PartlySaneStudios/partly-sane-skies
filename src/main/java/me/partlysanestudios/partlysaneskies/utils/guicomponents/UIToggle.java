package me.partlysanestudios.partlysaneskies.utils.guicomponents;

import gg.essential.elementa.UIComponent;
import gg.essential.elementa.components.UIBlock;
import gg.essential.elementa.components.UIImage;
import gg.essential.elementa.constraints.CenterConstraint;
import gg.essential.elementa.constraints.PixelConstraint;
import gg.essential.elementa.constraints.XConstraint;
import gg.essential.elementa.constraints.YConstraint;
import gg.essential.elementa.events.UIClickEvent;
import me.partlysanestudios.partlysaneskies.utils.Utils;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.util.function.Consumer;

public class UIToggle {
    static final ResourceLocation UNSELECTED_IMAGE_PATH = new ResourceLocation("partlysaneskies:textures/gui/base_color_button.png");
    static final ResourceLocation SELECTED_IMAGE_PATH = new ResourceLocation("partlysaneskies:textures/gui/base_color_background.png");

    float width;
    float height;

    XConstraint xConstraint;
    YConstraint yConstraint;

    boolean isSelected;

    UIBlock backgroundBlock;
    UIImage buttonTexture;

    public UIToggle() {
        this.isSelected = false;
        this.backgroundBlock = (UIBlock) new UIBlock()
                .setColor(new Color(0, 0,0,0));

        this.buttonTexture = (UIImage) Utils.uiimageFromResourceLocation(UNSELECTED_IMAGE_PATH)
                .setChildOf(backgroundBlock);

        this.backgroundBlock.onMouseClickConsumer(s -> {
            toggleState();
        });
    }

    public UIToggle toggleState() {
        return this.setState(!this.isSelected);

    }

    public UIToggle setState(boolean state) {
        this.isSelected = state;

        return this.updateState();
    }

    public UIToggle updateState() {
        backgroundBlock.removeChild(buttonTexture);

        if (this.isSelected) {
            this.buttonTexture = (UIImage) Utils.uiimageFromResourceLocation(SELECTED_IMAGE_PATH)
                    .setWidth(new PixelConstraint(this.width))
                    .setHeight(new PixelConstraint(this.height))
                    .setX(new CenterConstraint())
                    .setY(new CenterConstraint())
                    .setChildOf(this.backgroundBlock);
        }
        else {
            this.buttonTexture = (UIImage) Utils.uiimageFromResourceLocation(UNSELECTED_IMAGE_PATH)
                    .setWidth(new PixelConstraint(this.width))
                    .setHeight(new PixelConstraint(this.height))
                    .setX(new CenterConstraint())
                    .setY(new CenterConstraint())
                    .setChildOf(this.backgroundBlock);
        }

        return this;
    }

    public UIToggle setHeight(float height) {
        backgroundBlock.setHeight(new PixelConstraint(height));
        buttonTexture.setHeight(new PixelConstraint(height));

        this.height = height;

        return this;
    }

    public UIToggle setWidth(float width) {
        backgroundBlock.setWidth(new PixelConstraint(width));
        buttonTexture.setWidth(new PixelConstraint(width));

        this.width = width;

        return this;
    }

    public UIToggle setX(XConstraint xPos) {
        backgroundBlock.setX(xPos);
        buttonTexture.setX(new CenterConstraint());

        this.xConstraint = xPos;

        return this;
    }

    public UIToggle setY(YConstraint yPos) {
        backgroundBlock.setY(yPos);
        buttonTexture.setY(new CenterConstraint());

        this.yConstraint = yPos;

        return this;
    }

    public UIToggle OnMouseClickConsumer(Consumer<UIClickEvent> method) {
        backgroundBlock.onMouseClickConsumer(method);

        return this;
    }

    public UIComponent getComponent() {
        return buttonTexture;
    }

    public UIToggle insertComponentBeforeBackground(UIComponent component) {
        backgroundBlock.insertChildBefore(component, buttonTexture);

        return this;
    }
}
