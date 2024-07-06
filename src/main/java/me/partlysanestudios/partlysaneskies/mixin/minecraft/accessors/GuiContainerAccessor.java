//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.mixin.minecraft.accessors;

import net.minecraft.client.gui.inventory.GuiContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GuiContainer.class)
public interface GuiContainerAccessor {

    @Accessor("xSize")
    int partlysaneskies$getXSize();

    @Accessor("ySize")
    int partlysaneskies$getYSize();
}
