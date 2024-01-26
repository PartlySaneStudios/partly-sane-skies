package me.partlysanestudios.partlysaneskies.mixin.minecraft;


import me.partlysanestudios.partlysaneskies.events.EventManager;
import me.partlysanestudios.partlysaneskies.events.minecraft.player.PlayerBreakBlockEvent;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerControllerMP.class)
public class MixinPlayerController {

    @Inject(method = "onPlayerDestroyBlock", at = @At("HEAD"))
    private void onPlayerDestroyBlock(BlockPos pos, EnumFacing side, CallbackInfoReturnable<Boolean> cir) {
        PlayerBreakBlockEvent.Companion.onPlayerBreakBlock$Partly_Sane_Skies(pos, side, cir);
    }
}
