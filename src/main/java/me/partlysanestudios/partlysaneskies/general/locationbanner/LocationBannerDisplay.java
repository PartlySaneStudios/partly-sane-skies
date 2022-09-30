package me.partlysanestudios.partlysaneskies.general.locationbanner;

import java.awt.Color;

import gg.essential.elementa.ElementaVersion;
import gg.essential.elementa.UIComponent;
import gg.essential.elementa.components.UIText;
import gg.essential.elementa.components.Window;
import gg.essential.elementa.constraints.CenterConstraint;
import gg.essential.elementa.constraints.PixelConstraint;
import gg.essential.universal.UMatrixStack;
import me.partlysanestudios.partlysaneskies.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LocationBannerDisplay extends Gui {
	float TEXT_SCALE = 5f;

	String lastLocation = "";
	public long lastLocationTime;

	Window window= new Window(ElementaVersion.V2);
	String displayString = "empty";

	UIComponent displayText = new UIText(displayString)
		.setTextScale(new PixelConstraint(TEXT_SCALE))
		.setX(new CenterConstraint())
		.setY(new PixelConstraint(window.getHeight()*.333f))
		.setColor(Color.white)
		.setChildOf(window);


	public LocationBannerDisplay() {
	}


	public void checkLocation() {
		if(!Main.config.locationBannerDisplay) return;
		String regionName = Main.getRegionName();
		if (lastLocation.equals(regionName)) {
			return;
		}
		
		displayString = regionName;
		lastLocation = regionName;
		lastLocationTime = Minecraft.getSystemTime();
	}


	private boolean checkExpire() {
		return lastLocationTime+Main.config.locationBannerTime*1000 < Minecraft.getSystemTime();
	}


	@SubscribeEvent
	public void renderText(RenderGameOverlayEvent.Text event) {
		((UIText) displayText)
			.setText(displayString)
			.setX(new CenterConstraint())
			.setY(new PixelConstraint(window.getHeight()*.2f));
			window.draw(new UMatrixStack());

		if(checkExpire()) displayString = "";
	}


}
