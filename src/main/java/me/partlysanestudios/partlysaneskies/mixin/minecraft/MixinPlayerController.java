package me.partlysanestudios.partlysaneskies.mixin.minecraft;


import me.partlysanestudios.partlysaneskies.gui.hud.cooldown.TreecapitatorCooldown;
import me.partlysanestudios.partlysaneskies.utils.ChatUtils;
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
        ChatUtils.INSTANCE.visPrint("TEST");
        TreecapitatorCooldown.INSTANCE.checkForCooldown();
    }
}
