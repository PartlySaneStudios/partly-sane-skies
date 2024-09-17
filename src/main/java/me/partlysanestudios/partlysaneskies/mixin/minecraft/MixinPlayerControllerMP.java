//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.mixin.minecraft;

import me.partlysanestudios.partlysaneskies.events.minecraft.player.PlayerBreakBlockEvent;
import me.partlysanestudios.partlysaneskies.utils.geometry.vectors.Point3d;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerControllerMP.class)
public class MixinPlayerControllerMP {

    @Inject(method = "onPlayerDestroyBlock", at = @At("HEAD"))
    private void onPlayerDestroyBlock(BlockPos pos, EnumFacing side, CallbackInfoReturnable<Boolean> cir) {
        new PlayerBreakBlockEvent(Point3d.Companion.toPoint3d(pos), side).post();
    }
}
