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

package me.partlysanestudios.partlysaneskies;

import java.awt.Color;

import gg.essential.elementa.ElementaVersion;
import gg.essential.elementa.UIComponent;
import gg.essential.elementa.components.UIText;
import gg.essential.elementa.components.Window;
import gg.essential.elementa.constraints.CenterConstraint;
import gg.essential.elementa.constraints.PixelConstraint;
import gg.essential.universal.UMatrixStack;
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
		if (!PartlySaneSkies.config.locationBannerDisplay)
			return;

		String regionName = PartlySaneSkies.getRegionName();
		String noColorCodeRegionName = Utils.removeColorCodes(regionName);

        if (noColorCodeRegionName.equals("")) {
            return;
        }
        
        noColorCodeRegionName = Utils.stripLeading(noColorCodeRegionName);
        noColorCodeRegionName = Utils.stripTrailing(noColorCodeRegionName);
        noColorCodeRegionName = noColorCodeRegionName.replaceAll("\\P{Print}", ""); // Removes the RANDOM EMOJIS THAT ARE PRESENT IN SKYBLOCK LOCATIONS
        // LOOK AT THIS:
        // The Catac🔮ombs (F5)
        // The Catac👽ombs (F5)
        // The Catac🔮ombs (F5)
        // Dungeon H👾ub
        // Mountain⚽
        // Village⚽
        // Coal Mine⚽
        // THEY'RE NOT EVEN VISABLE IN MINECRAFT

		if (lastLocation.equals(noColorCodeRegionName)) {
			return;
		}

        if (noColorCodeRegionName.toLowerCase().contains("none")) {
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
		return getTimeSinceLastChange() > PartlySaneSkies.config.locationBannerTime * 1000;
	}

	@SubscribeEvent
	public void renderText(RenderGameOverlayEvent.Text event) {
		short alpha = getAlpha(getTimeSinceLastChange(), PartlySaneSkies.config.locationBannerTime);

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
