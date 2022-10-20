package me.partlysanestudios.partlysaneskies.general;

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

	Window window = new Window(ElementaVersion.V2);
	String displayString = "empty";

	Color color = Color.white;

	UIComponent displayText = new UIText(displayString)
			.setTextScale(new PixelConstraint(TEXT_SCALE))
			.setX(new CenterConstraint())
			.setY(new PixelConstraint(window.getHeight() * .125f))
			.setColor(Color.white)
			.setChildOf(window);

	public LocationBannerDisplay() {
	}

	public void checkLocation() {
		if (!Main.config.locationBannerDisplay)
			return;

		String regionName = Main.getRegionName();
		String noColorCodeRegionName = Utils.removeColorCodes(regionName);

		if (lastLocation.equals(noColorCodeRegionName)) {
			return;
		}
		if (!regionName.equals("")) {
			color = Utils.colorCodetoColor.get(regionName.substring(3, 5));
		}

		displayString = noColorCodeRegionName;
		lastLocation = noColorCodeRegionName;
		lastLocationTime = Minecraft.getSystemTime();
	}

	private boolean checkExpire() {
		return getTimeSinceLastChange() > Main.config.locationBannerTime * 1000;
	}

	@SubscribeEvent
	public void renderText(RenderGameOverlayEvent.Text event) {
		short alpha = getAlpha(getTimeSinceLastChange(), Main.config.locationBannerTime);

		if (color == null)
			color = Color.gray;
		else
			color = new Color(color.getRed(), color.getGreen(), color.getBlue(), (short) alpha);

		((UIText) displayText)
				.setText(displayString)
				.setX(new CenterConstraint())
				.setY(new PixelConstraint(window.getHeight() * .125f))
				.setColor(color);
		window.draw(new UMatrixStack());

		if (checkExpire())
			displayString = "";
	}

	private long getTimeSinceLastChange() {
		return Minecraft.getSystemTime() - lastLocationTime;
	}

	public static short getAlpha(long timeSinceLastChangeMs, double displayLenghtSeconds) {
		long time = timeSinceLastChangeMs;
		double displayLength = displayLenghtSeconds * 1000;
		double fadeLength = displayLength * (1 / 6d);

		if (0 > time) {
			return 0;
		} else if (0 < time && time < fadeLength) {
			return (short) Math.round(time / fadeLength * 255);
		} else if (fadeLength < time && time <= displayLength - fadeLength) {
			return 255;
		} else if (displayLength - fadeLength < time && time <= displayLength) {
			return (short) Math.round((-time + displayLength) / fadeLength * 255);
		} else {

			return 0;
		}

	}
}
