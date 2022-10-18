package me.partlysanestudios.partlysaneskies.general.economy.AhSniper;

import me.partlysanestudios.partlysaneskies.Main;

public class AhGui {
    

    public static void clickOnSlot(int slot) {        
        Main.minecraft.playerController.windowClick(Main.minecraft.thePlayer.openContainer.windowId, slot, 0, 3, Main.minecraft.thePlayer);
    }
}
