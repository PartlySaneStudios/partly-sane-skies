//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.mixin.minecraft;

import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.IInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GuiChest.class)
public interface MixinGuiChest {

    @Accessor("upperChestInventory")
    IInventory partlysaneskies$getUpperChestInventory();

    @Accessor("lowerChestInventory")
    IInventory partlysaneskies$getLowerChestInventory();
}
