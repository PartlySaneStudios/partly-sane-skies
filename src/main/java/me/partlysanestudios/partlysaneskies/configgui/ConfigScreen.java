package me.partlysanestudios.partlysaneskies.configgui;

import java.io.IOException;

import me.partlysanestudios.partlysaneskies.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptionsRowList;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public final class ConfigScreen extends GuiScreen {
    /** Distance from top of the screen to the options row list's top */
    private static final int OPTIONS_LIST_TOP_HEIGHT = 24;
    /** Distance from bottom of the screen to the options row list's bottom */
    private static final int OPTIONS_LIST_BOTTOM_OFFSET = 32;
    /** Height of each item in the options row list */
    private static final int OPTIONS_LIST_ITEM_HEIGHT = 25;

    /** Width of a button */
    private static final int BUTTON_WIDTH = 200;
    /** Height of a button */
    private static final int BUTTON_HEIGHT = 20;
    /** Distance from bottom of the screen to the "Done" button's top */
    private static final int DONE_BUTTON_TOP_OFFSET = 26;

    private GuiOptionsRowList optionsRowList;
    private Minecraft minecraft;
    private GuiButton doneButton;


    public ConfigScreen() {
        
    }

    @Override
    public void initGui() {
        this.minecraft = Main.minecraft;

        this.drawCenteredString(Main.minecraft.fontRendererObj, "Test", this.width/2, 8, 0xFFFFFF);
        
        this.optionsRowList = new GuiOptionsRowList(this.minecraft, this.width, this.height, OPTIONS_LIST_TOP_HEIGHT, this.height - OPTIONS_LIST_BOTTOM_OFFSET, OPTIONS_LIST_ITEM_HEIGHT);
        

        this.doneButton = new GuiButton(10,  (this.width - BUTTON_WIDTH) / 2, this.height - DONE_BUTTON_TOP_OFFSET,BUTTON_WIDTH, BUTTON_HEIGHT,I18n.format("gui.done"));
        this.buttonList.add(this.doneButton);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // First draw the background of the screen
        this.drawBackground(10);;
        // Draw the title
        
        this.optionsRowList.drawScreen(mouseX, mouseY, partialTicks);

        // Call the super class' method to complete rendering
        super.drawScreen(mouseX, mouseY, partialTicks);


    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException{
        if(button == this.doneButton) {
            this.onGuiClosed();
        }
    }

    @Override
    public void onGuiClosed() {
        this.mc.thePlayer.closeScreen();
    }
}
