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

package me.partlysanestudios.partlysaneskies.rngdropbanner;

import java.awt.Color;

import gg.essential.elementa.ElementaVersion;
import gg.essential.elementa.UIComponent;
import gg.essential.elementa.components.UIText;
import gg.essential.elementa.components.Window;
import gg.essential.elementa.constraints.CenterConstraint;
import gg.essential.elementa.constraints.PixelConstraint;
import gg.essential.universal.UMatrixStack;
import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import me.partlysanestudios.partlysaneskies.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DropBannerDisplay extends Gui {
	public static Drop drop;

	public DropBannerDisplay() {
	}

    // Waits to detect the rarte drop
	@SubscribeEvent
	public void onChatMessage(ClientChatReceivedEvent event) {
        String formattedMessage = event.message.getFormattedText();
		if (isRareDrop(formattedMessage) && PartlySaneSkies.config.rareDropBannerSound) {
			PartlySaneSkies.minecraft.thePlayer.playSound("partlysaneskies:rngdropjingle", 100, 1);
		}

		if (isRareDrop(formattedMessage) && PartlySaneSkies.config.rareDropBanner) {
			String unformatedMessage = event.message.getUnformattedText();


			String name = "";
			// Gets the name of teh drop category
            String dropCategory = unformatedMessage.substring(0, unformatedMessage.indexOf("! ") + 1);

            // Gets the colour of item rarity
			Color dropNameHex = Utils.colorCodetoColor
					.get(formattedMessage.subSequence(formattedMessage.indexOf(dropCategory) + dropCategory.length() + 3,
							formattedMessage.indexOf(dropCategory) + dropCategory.length() + 5));
            // Gets the colour of the drop category
			Color dropCategoryHex = Utils.colorCodetoColor.get(formattedMessage.substring(2, 4));

            // Finds the amount of magic find
			int magicFind = 0;
            // Finds the amount of  magic find from the message
			if (unformatedMessage.contains("Magic Find")) {
				name = unformatedMessage.substring(dropCategory.length(), unformatedMessage.indexOf(" (+"));
				magicFind = Integer.parseInt(unformatedMessage.substring(unformatedMessage.indexOf("(+") + 2, unformatedMessage.indexOf("% ")));
			} else { // If no magic find, ignore and keep 0
				name = unformatedMessage.substring(dropCategory.length());
			}

			DropBannerDisplay.drop = new Drop(name, dropCategory, 1, magicFind, Minecraft.getSystemTime(), dropCategoryHex, dropNameHex);
		}
	}

    public static boolean isRareDrop(String formattedMessage) {
        return formattedMessage.startsWith("§r§6§lRARE DROP! ")
        || formattedMessage.startsWith("§r§6§lPET DROP! ");
    }

	float SMALL_TEXT_SCALE = 2.5f;
	float BIG_TEXT_SCALE = 5f;

	Window window = new Window(ElementaVersion.V2);
	String topString = "empty";
	String dropNameString = "empty";
	String magicFindString = "empty";

	UIComponent topText = new UIText(topString)
			.setTextScale(new PixelConstraint(BIG_TEXT_SCALE))
			.setX(new CenterConstraint())
			.setY(new PixelConstraint(window.getHeight() * .333f))
			.setColor(Color.white)
			.setChildOf(window);

	UIComponent magicFindText = new UIText(magicFindString)
			.setTextScale(new PixelConstraint(SMALL_TEXT_SCALE))
			.setX(new CenterConstraint())
			.setY(new PixelConstraint(window.getHeight() * .4f))
			.setColor(Color.decode("" + 0x55FFFF))
			.setChildOf(window);

	UIComponent dropNameText = new UIText(dropNameString)
			.setTextScale(new PixelConstraint(SMALL_TEXT_SCALE))
			.setX(new CenterConstraint())
			.setY(new PixelConstraint(window.getHeight() * .4f))
			.setColor(Color.white)
			.setChildOf(window);

	@SubscribeEvent
	public void renderText(RenderGameOverlayEvent.Text event) {

		Color nameColor = new Color(255, 255, 255);
		Color categoryColor = new Color(255, 255, 255);

		if (DropBannerDisplay.drop == null) {

			dropNameString = "";
			topString = "";
			magicFindString = "";
			categoryColor = new Color(255, 255, 255);
			nameColor = new Color(255, 255, 255);
		} else {

			dropNameString = "x" + drop.amount + " " + drop.name;
			topString = drop.dropCategory;
			magicFindString = " (+" + drop.magicFind + "% ✯ Magic Find)";
			nameColor = drop.dropNameColor;
			categoryColor = drop.dropCategoryColor;
			if (Minecraft.getSystemTime() - drop.timeDropped > (1f / 3f * PartlySaneSkies.config.rareDropBannerTime * 1000)
					&& Minecraft.getSystemTime()
							- drop.timeDropped < (10f / 12f * PartlySaneSkies.config.rareDropBannerTime * 1000)) {
				if (Math.round((drop.timeDropped - Minecraft.getSystemTime()) / 1000f * 4) % 2 == 0) {
					categoryColor = Color.white;
				} else {
					categoryColor = drop.dropCategoryColor;
				}
			}

			if (drop.timeDropped + PartlySaneSkies.config.rareDropBannerTime * 1000 < Minecraft.getSystemTime())
				drop = null;
		}

		((UIText) topText)
				.setText(topString)
				.setX(new PixelConstraint(
						window.getWidth() / 2 - ((UIText) topText).getTextWidth() * BIG_TEXT_SCALE / 2))
				.setY(new PixelConstraint(window.getHeight() * .3f))
				.setColor(categoryColor);
		((UIText) dropNameText)
				.setText(dropNameString)
				.setX(new PixelConstraint(
						window.getWidth() / 2 - (((UIText) dropNameText).getTextWidth() * SMALL_TEXT_SCALE
								+ ((UIText) magicFindText).getTextWidth() * SMALL_TEXT_SCALE) / 2f))
				.setY(new PixelConstraint(window.getHeight() * .38f))
				.setColor(nameColor);
		((UIText) magicFindText)
				.setText(magicFindString)
				.setX(new PixelConstraint(window.getWidth() / 2
						- (((UIText) dropNameText).getTextWidth() * SMALL_TEXT_SCALE
								+ ((UIText) magicFindText).getTextWidth() * SMALL_TEXT_SCALE) / 2f
						+ ((UIText) dropNameText).getTextWidth() * SMALL_TEXT_SCALE))
				.setY(new PixelConstraint(window.getHeight() * .38f));
		window.draw(new UMatrixStack());
	}
}
