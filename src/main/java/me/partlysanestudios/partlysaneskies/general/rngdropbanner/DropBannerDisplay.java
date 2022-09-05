package me.partlysanestudios.partlysaneskies.general.rngdropbanner;

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
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DropBannerDisplay extends Gui {
	public static Drop drop;

	public DropBannerDisplay() {
		
	}

	

	// private void verifyRenderer() {
	// 	if (fontRenderer != null) return;
	// 	fontRenderer = Main.minecraft.fontRendererObj;
	// }
	
	@SubscribeEvent
	public void onChatMessage(ClientChatReceivedEvent event) {
		if((event.message.getFormattedText().startsWith("§r§6§lRARE DROP! ") || event.message.getFormattedText().startsWith("§r§6§lPET DROP! ")) && Main.config.rareDropBannerSound) {Main.minecraft.thePlayer.playSound("partlysaneskies:rngdropjingle", 100, 1);}
		if((event.message.getFormattedText().startsWith("§r§6§lRARE DROP! ") || event.message.getFormattedText().startsWith("§r§6§lPET DROP! ")) && Main.config.rareDropBanner) {
            String unformatedMessage = event.message.getUnformattedText();
			String formatedMessage = event.message.getFormattedText();

            String name = "";
            String dropCategory = unformatedMessage.substring(0, unformatedMessage.indexOf("! ") + 1);

			Color dropNameHex = Utils.colorCodetoColor.get(formatedMessage.subSequence(formatedMessage.indexOf(dropCategory)+ dropCategory.length() + 3, formatedMessage.indexOf(dropCategory)+ dropCategory.length() + 5));
			Color dropCategoryHex = Utils.colorCodetoColor.get(formatedMessage.substring(2,4));
            int magicFind = 0;

			
            
            if (unformatedMessage.contains("Magic Find")) {
				name = unformatedMessage.substring(dropCategory.length(), unformatedMessage.indexOf(" (+"));
				magicFind = Integer.parseInt(unformatedMessage.substring(unformatedMessage.indexOf("(+")+2, unformatedMessage.indexOf("% ")));
			}
            else {
				name = unformatedMessage.substring(dropCategory.length());
			}


            DropBannerDisplay.drop = new Drop(name, dropCategory, 1, magicFind, Minecraft.getSystemTime(), dropCategoryHex, dropNameHex);
        }
	}

	float SMALL_TEXT_SCALE = 2.5f;
	float BIG_TEXT_SCALE = 5f;


	Window window= new Window(ElementaVersion.V2);
	String topString = "empty";
	String dropNameString = "empty";
	String magicFindString = "empty";

	UIComponent topText = new UIText(topString)
		.setTextScale(new PixelConstraint(BIG_TEXT_SCALE))
		.setX(new CenterConstraint())
		.setY(new PixelConstraint(window.getHeight()*.333f))
		.setColor(Color.white)
		.setChildOf(window);
		
	UIComponent magicFindText = new UIText(magicFindString)
		.setTextScale(new PixelConstraint(SMALL_TEXT_SCALE))
		.setX(new CenterConstraint())
		.setY(new PixelConstraint(window.getHeight()*.4f))
		.setColor(Color.decode("" + 0x55FFFF))
		.setChildOf(window);
		
	UIComponent dropNameText = new UIText(dropNameString)
		.setTextScale(new PixelConstraint(SMALL_TEXT_SCALE))
		.setX(new CenterConstraint())
		.setY(new PixelConstraint(window.getHeight()*.4f))
		.setColor(Color.white)
		.setChildOf(window);


	@SubscribeEvent
	public void renderText(RenderGameOverlayEvent.Text event) {
	
		Color nameColor = new Color(255, 255, 255);
		Color categoryColor = new Color(255, 255, 255);

		
		if(DropBannerDisplay.drop == null) {

			dropNameString = "";
			topString = "";
			magicFindString = "";
			categoryColor = new Color(255, 255, 255);
			nameColor = new Color(255, 255, 255);
		}
		else {
			
			dropNameString = "x" +drop.amount + " " + drop.name;
			topString = drop.dropCategory;
			magicFindString = " (+" + drop.magicFind + "% ✯ Magic Find)";
			nameColor = drop.dropNameColor;
			categoryColor = drop.dropCategoryColor;
			if(Minecraft.getSystemTime() - drop.timeDropped > (1f/3f * Main.config.rareDropBannerTime*1000) && Minecraft.getSystemTime() - drop.timeDropped < (10f/12f * Main.config.rareDropBannerTime*1000)) {
				if(Math.round((drop.timeDropped - Minecraft.getSystemTime())/1000f*4) % 2 == 0) {
					categoryColor = Color.white;
				}
				else {
					categoryColor = drop.dropCategoryColor;
				}
			}

			if(drop.timeDropped+Main.config.rareDropBannerTime*1000 < Minecraft.getSystemTime()) drop = null;
		}
		
		

		((UIText) topText)
			.setText(topString)
			.setX(new PixelConstraint(window.getWidth()/2 - ((UIText) topText).getTextWidth()*BIG_TEXT_SCALE/2))
			.setY(new PixelConstraint(window.getHeight()*.3f))
			.setColor(categoryColor);
		((UIText) dropNameText)
			.setText(dropNameString)
			.setX(new PixelConstraint(window.getWidth()/2 - (((UIText) dropNameText).getTextWidth()*SMALL_TEXT_SCALE + ((UIText) magicFindText).getTextWidth()*SMALL_TEXT_SCALE)/2f))
			.setY(new PixelConstraint(window.getHeight()*.38f))
			.setColor(nameColor);
		((UIText) magicFindText)
			.setText(magicFindString)
			.setX(new PixelConstraint(window.getWidth()/2 - (((UIText) dropNameText).getTextWidth()*SMALL_TEXT_SCALE + ((UIText) magicFindText).getTextWidth()*SMALL_TEXT_SCALE)/2f + ((UIText) dropNameText).getTextWidth()*SMALL_TEXT_SCALE))
			.setY(new PixelConstraint(window.getHeight()*.38f));
		window.draw(new UMatrixStack());
	}
}
