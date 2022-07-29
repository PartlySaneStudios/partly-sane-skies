package me.partlysanestudios.partlysaneskies;

import org.lwjgl.opengl.GL11;

import me.partlysanestudios.partlysaneskies.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DropBannerDisplay extends Gui {
	public static Drop drop;

	private FontRenderer fontRenderer;
	
	public DropBannerDisplay() {
		
	}


	private void verifyRenderer() {
		if (fontRenderer != null) return;
		fontRenderer = Main.minecraft.fontRendererObj;
	}
	
	@SubscribeEvent
	public void onChatMessage(ClientChatReceivedEvent event) {
		if(event.message.getFormattedText().startsWith("§r§6§lRARE DROP! ") || event.message.getFormattedText().startsWith("§r§6§lPET DROP! ")) {
            String unformatedMessage = event.message.getUnformattedText();
			String formatedMessage = event.message.getFormattedText();

            String name = "";
            String dropCategory = unformatedMessage.substring(0, unformatedMessage.indexOf("! ") + 1);

			int dropNameHex = Utils.colorCodeToHex.get(formatedMessage.subSequence(formatedMessage.indexOf(dropCategory)+ dropCategory.length() + 3, formatedMessage.indexOf(dropCategory)+ dropCategory.length() + 5));
			int dropCategoryHex = Utils.colorCodeToHex.get(formatedMessage.substring(2,4));
            int magicFind = 0;
            
            if (unformatedMessage.contains("Magic Find")) {
				name = unformatedMessage.substring(dropCategory.length(), unformatedMessage.indexOf(" (+"));
				magicFind = Integer.parseInt(unformatedMessage.substring(unformatedMessage.indexOf("(+")+2, unformatedMessage.indexOf("% ")));
			}
            else {
				name = unformatedMessage.substring(dropCategory.length());
			}


            drop = new Drop(name, dropCategory, 1, magicFind, Minecraft.getSystemTime(), dropCategoryHex, dropNameHex);
        }
	}

	



	@SubscribeEvent
	public void renderTopText(RenderGameOverlayEvent.Post event) {
        verifyRenderer();
		if(drop == null) {
			String text = "_";
			fontRenderer.drawStringWithShadow(text, (Main.minecraft.displayWidth/4)-(fontRenderer.getStringWidth(text)/2), (Main.minecraft.displayHeight/4)-(fontRenderer.FONT_HEIGHT/2)-50, 0xFFFFFF);
		
		

			GL11.glColor4f(1, 1, 1, 1);
			Main.minecraft.renderEngine.bindTexture(new ResourceLocation("minecraft", "textures/gui/icons.png"));
		}
		else {
			// Drop drop = new Drop("Test", "Rare Drop!", 1, 165, (long) 357487, 0x60F542);
			String text = drop.dropCategory;
			// fontRenderer.drawString(Utils.colorCodes(drop.dropCategory + "\n&rx" +drop.amount + " " + drop.name + " (+" + drop.magicFind + "% Magic Find)" ), Main.minecraft.displayWidth/4, Main.minecraft.displayHeight/8, 0xFFFFFF);
			fontRenderer.drawStringWithShadow(text, (Main.minecraft.displayWidth/4)-(fontRenderer.getStringWidth(text)/2), (Main.minecraft.displayHeight/4)-(fontRenderer.FONT_HEIGHT/2)-50, drop.dropCategoryColor);

			
			
			GL11.glColor4f(1, 1, 1, 1);
			Main.minecraft.renderEngine.bindTexture(new ResourceLocation("minecraft", "textures/gui/icons.png"));
		
			if(drop.timeDropped+3000 < Minecraft.getSystemTime()) drop = null;
		}
		
	}



	@SubscribeEvent
	public void renderBottomText(RenderGameOverlayEvent.Post event) {
        verifyRenderer();
		if(drop == null) {
			String text = "__";
			fontRenderer.drawStringWithShadow(text, (Main.minecraft.displayWidth/4)-(fontRenderer.getStringWidth(text)/2), (Main.minecraft.displayHeight/4)-(fontRenderer.FONT_HEIGHT/2)-30, 0xFFFFFF);



			// Without this, icons look like text
			GL11.glColor4f(1, 1, 1, 1);
			Main.minecraft.renderEngine.bindTexture(new ResourceLocation("minecraft", "textures/gui/icons.png"));
		}
		else {
			// Drop drop = new Drop("Test", "Rare Drop!", 1, 165, (long) 357487, 0xFFFFFF);
			String fullText = "x" +drop.amount + " " + drop.name + " (+" + drop.magicFind + "% Magic Find)";
			String displayText = "x" +drop.amount + " " + drop.name;
			
			// fontRenderer.drawString(Utils.colorCodes(drop.dropCategory + "\n&rx" +drop.amount + " " + drop.name + " (+" + drop.magicFind + "% Magic Find)" ), Main.minecraft.displayWidth/4, Main.minecraft.displayHeight/8, 0xFFFFFF);
			fontRenderer.drawStringWithShadow(displayText, (Main.minecraft.displayWidth/4)-(fontRenderer.getStringWidth(fullText)/2), (Main.minecraft.displayHeight/4)-(fontRenderer.FONT_HEIGHT/2)-30, drop.dropNameHex);
			
			

			GL11.glColor4f(1, 1, 1, 1);
			Main.minecraft.renderEngine.bindTexture(new ResourceLocation("minecraft", "textures/gui/icons.png"));
		}
	}

	@SubscribeEvent
	public void renderBottomMagicFindText(RenderGameOverlayEvent.Post event) {
        verifyRenderer();
		if(drop == null) {
			String text = "__";
			fontRenderer.drawStringWithShadow(text, (Main.minecraft.displayWidth/4)-(fontRenderer.getStringWidth(text)/2), (Main.minecraft.displayHeight/4)-(fontRenderer.FONT_HEIGHT/2)-30, 0xFFFFFF);



			// Without this, icons look like text
			GL11.glColor4f(1, 1, 1, 1);
			Main.minecraft.renderEngine.bindTexture(new ResourceLocation("minecraft", "textures/gui/icons.png"));
		}
		else {
			// Drop drop = new Drop("Test", "Rare Drop!", 1, 165, (long) 357487, 0xFFFFFF);
			String fullText = "x" +drop.amount + " " + drop.name;
			String displayText = " (+" + drop.magicFind + "% ✯ Magic Find)";
			
			// fontRenderer.drawString(Utils.colorCodes(drop.dropCategory + "\n&rx" +drop.amount + " " + drop.name + " (+" + drop.magicFind + "% Magic Find)" ), Main.minecraft.displayWidth/4, Main.minecraft.displayHeight/8, 0xFFFFFF);
			fontRenderer.drawStringWithShadow(displayText, (Main.minecraft.displayWidth/4)-(fontRenderer.getStringWidth((fullText + displayText))/2) + fontRenderer.getStringWidth(fullText) + 5, (Main.minecraft.displayHeight/4)-(fontRenderer.FONT_HEIGHT/2)-30, 0x55FFFF);
			
			

			GL11.glColor4f(1, 1, 1, 1);
			Main.minecraft.renderEngine.bindTexture(new ResourceLocation("minecraft", "textures/gui/icons.png"));
		}
	}
}
