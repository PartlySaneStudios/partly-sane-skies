//
// Written by J10a1n15 and Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.mixin.mods;

import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import me.partlysanestudios.partlysaneskies.features.security.PrivacyMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "gg.essential.network.connectionmanager.telemetry.TelemetryManager", remap = false)
public class MixinEssentialsTelemetryManager {

    @Inject(method = {"sendHardwareAndOSTelemetry*", "enqueue*"}, at = @At("HEAD"), cancellable = true)
    public void onTelemetry(CallbackInfo ci) {
        if (PartlySaneSkies.Companion.getConfig() == null) return;
        if (PrivacyMode.INSTANCE.shouldBlockTelemetry()) {
            ci.cancel();
        }
    }
}
