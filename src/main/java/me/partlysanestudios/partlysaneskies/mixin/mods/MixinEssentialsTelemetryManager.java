/*
 *
 * Written by J10a1n15.
 * See LICENSE for copyright and license notices.
 *
 */

package me.partlysanestudios.partlysaneskies.mixin.mods;

import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "gg.essential.network.connectionmanager.telemetry.TelemetryManager", remap = false)
public class MixinEssentialsTelemetryManager {
    @Inject(method = "sendHardwareAndOSTelemetry*", at = @At("HEAD"), cancellable = true)
    public void onSendHardwareAndOSTelemetryHead(CallbackInfo ci) {
        if (PartlySaneSkies.config == null) {
            return;
        }
        if (PartlySaneSkies.config.privacyMode) {
            ci.cancel();
        }
    }

    @Inject(method = "enqueue*", at = @At("HEAD"), cancellable = true)
    public void onEnqueueHead(CallbackInfo ci) {
        if (PartlySaneSkies.config == null) {
            return;
        }
        if (PartlySaneSkies.config.privacyMode) {
            ci.cancel();
        }
    }
}