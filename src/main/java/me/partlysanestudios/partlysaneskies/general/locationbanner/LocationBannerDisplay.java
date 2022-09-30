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
import me.partlysanestudios.partlysaneskies.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LocationBannerDisplay extends Gui {
	float TEXT_SCALE = 5f;

	String lastLocation = "";
	public long lastLocationTime = Minecraft.getSystemTime();

	Window window= new Window(ElementaVersion.V2);
	String displayString = "empty";

	Color color = Color.white;

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
		if(!regionName.equals("")) color = Utils.colorCodetoColor.get(regionName.substring(0, 2));
		regionName = Utils.removeColorCodes(regionName);
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
		short alpha = getAlpha();
		

		Utils.visPrint(alpha);
		if(color == null) color = Color.gray;
		else color = new Color(color.getRed(), color.getGreen(), color.getBlue(), (short) alpha);

		((UIText) displayText)
			.setText(displayString)
			.setX(new CenterConstraint())
			.setY(new PixelConstraint(window.getHeight()*.2f))
			.setColor(color);
			window.draw(new UMatrixStack());

		if(checkExpire()) displayString = "";
	}

	private long getTimeSinceLastChange() {
		return Minecraft.getSystemTime() - lastLocationTime;
	}

	private short getAlpha() {
		long time = getTimeSinceLastChange();
		double fadeLength = time*(1 / 6d);
		double displayLength = Main.config.locationBannerTime * 1000;
		if(0 > time) {
			return 0;
		}
		else if(0 < time && time < fadeLength) {
			return (short) Math.round(time / fadeLength*255);
		}
		else if (fadeLength < time && time < displayLength-  fadeLength){
			return 255;
		}
		else if (displayLength - fadeLength < time) {
			return (short) Math.round((-time + displayLength)/fadeLength*255);
		}
		else {
			return 0;
		}

	}
}
