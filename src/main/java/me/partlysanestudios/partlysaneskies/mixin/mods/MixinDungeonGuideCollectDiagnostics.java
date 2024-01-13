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
@Mixin(targets = "kr.syeyoung.dungeonsguide.mod.features.impl.etc.FeatureCollectDiagnostics", remap = false)
public class MixinDungeonGuideCollectDiagnostics {
    @Inject(method = "sendLogActually*", at = @At("HEAD"), cancellable = true)
    public void onSendLogActuallyHead(CallbackInfo ci) {
        if (PartlySaneSkies.Companion.getConfig() == null) {
            return;
        }
        if (PartlySaneSkies.Companion.getConfig().privacyMode) {
            ci.cancel();
        }
    }
}
