//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.mixin.minecraft;

import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.util.IChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GuiPlayerTabOverlay.class)
public interface MixinGuiPlayerTabOverlay {
    @Accessor("footer")
    IChatComponent partlySaneSkies$getFooter();

}
