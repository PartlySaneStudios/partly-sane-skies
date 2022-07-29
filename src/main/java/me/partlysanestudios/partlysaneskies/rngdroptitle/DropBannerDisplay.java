package me.partlysanestudios.partlysaneskies.rngdroptitle;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import me.partlysanestudios.partlysaneskies.Main;
import me.partlysanestudios.partlysaneskies.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DropBannerDisplay extends Gui {
	public static List<Drop> dropList;

	private FontRenderer fontRenderer;
	
	public DropBannerDisplay() {
		dropList = new ArrayList<Drop>();
	}

	private void verifyRenderer() {
		if (fontRenderer != null) return;
		fontRenderer = Main.minecraft.fontRendererObj;
	}
	
	@SubscribeEvent
	public void onChatMessage(ClientChatReceivedEvent event) {
		if(event.message.getFormattedText().startsWith("§r§6§lRARE DROP!")) {
            Utils.visPrint(Utils.removeColorCodes(event.message.getFormattedText().substring(("§r§6§lRARE DROP! ").length())));
            String unformatedMessage = event.message.getUnformattedText();
            String name = "";
            String dropCategory = unformatedMessage.substring(0, unformatedMessage.indexOf("! ") + 1);
            int magicFind = Integer.parseInt(unformatedMessage.substring(unformatedMessage.indexOf("(+")+2, unformatedMessage.indexOf("% ")));
            
            if (unformatedMessage.contains("Magic Find")) name = unformatedMessage.substring(dropCategory.length(), unformatedMessage.indexOf(" (+"));
            else name = unformatedMessage.substring(("RARE DROP! ").length());


            DropBannerDisplay.dropList.add(new Drop(name, dropCategory, 1, magicFind, Minecraft.getSystemTime(), 0xFFFFFF));
        }
	}
	@SubscribeEvent
	public void renderTopText(RenderGameOverlayEvent.Post event) {
        verifyRenderer();
		if(dropList.size() == 0) {
			String text = "empty1";
			fontRenderer.drawString(text, (Main.minecraft.displayWidth/4)-(fontRenderer.getStringWidth(text)/2), (Main.minecraft.displayHeight/4)-(fontRenderer.FONT_HEIGHT/2)-50, 0xFFFFFF);
		
		

			GL11.glColor4f(1, 1, 1, 1);
			Main.minecraft.renderEngine.bindTexture(new ResourceLocation("minecraft", "textures/gui/icons.png"));
		}
		else {
			// Drop drop = new Drop("Test", "Rare Drop!", 1, 165, (long) 357487, 0x60F542);
			Drop drop = dropList.get(dropList.size()-1);
			String text = drop.dropCategory;
			// fontRenderer.drawString(Utils.colorCodes(drop.dropCategory + "\n&rx" +drop.amount + " " + drop.name + " (+" + drop.magicFind + "% Magic Find)" ), Main.minecraft.displayWidth/4, Main.minecraft.displayHeight/8, 0xFFFFFF);
			fontRenderer.drawString(text, (Main.minecraft.displayWidth/4)-(fontRenderer.getStringWidth(text)/2), (Main.minecraft.displayHeight/4)-(fontRenderer.FONT_HEIGHT/2)-50, drop.color);
			
			
			
			GL11.glColor4f(1, 1, 1, 1);
			Main.minecraft.renderEngine.bindTexture(new ResourceLocation("minecraft", "textures/gui/icons.png"));
		}
	}

	@SubscribeEvent
	public void renderBottomText(RenderGameOverlayEvent.Post event) {
        verifyRenderer();
		if(dropList.size() == 0) {
			String text = "empty2";
			fontRenderer.drawString(text, (Main.minecraft.displayWidth/4)-(fontRenderer.getStringWidth(text)/2), (Main.minecraft.displayHeight/4)-(fontRenderer.FONT_HEIGHT/2)-30, 0xFFFFFF);



			// Without this, icons look like text
			GL11.glColor4f(1, 1, 1, 1);
			Main.minecraft.renderEngine.bindTexture(new ResourceLocation("minecraft", "textures/gui/icons.png"));
		}
		else {
			// Drop drop = new Drop("Test", "Rare Drop!", 1, 165, (long) 357487, 0xFFFFFF);
			Drop drop = dropList.get(dropList.size()-1);
			String text = "x" +drop.amount + " " + drop.name + " (+" + drop.magicFind + "% Magic Find)";
			
			// fontRenderer.drawString(Utils.colorCodes(drop.dropCategory + "\n&rx" +drop.amount + " " + drop.name + " (+" + drop.magicFind + "% Magic Find)" ), Main.minecraft.displayWidth/4, Main.minecraft.displayHeight/8, 0xFFFFFF);
			fontRenderer.drawString(text, (Main.minecraft.displayWidth/4)-(fontRenderer.getStringWidth(text)/2), (Main.minecraft.displayHeight/4)-(fontRenderer.FONT_HEIGHT/2)-30, 0xFFFFFF);
			
			

			GL11.glColor4f(1, 1, 1, 1);
			Main.minecraft.renderEngine.bindTexture(new ResourceLocation("minecraft", "textures/gui/icons.png"));
	}
	}
}
