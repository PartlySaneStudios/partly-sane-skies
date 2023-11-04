package me.partlysanestudios.partlysaneskies.mixin;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(Minecraft.class)
public class StartMixin {
    @Inject(method = "startGame", at = @At(value = "HEAD"))
    private void onStartGame(CallbackInfo ci) {
        System.out.println("Partly Sane Skies Says hi!");
    }
}