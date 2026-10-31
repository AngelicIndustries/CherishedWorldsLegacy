/* SPDX-License-Identifier: LGPL-3.0-or-later
 * Cherished Worlds by Illusive Soulworks; 1.7.10 backport by Angelic Industries. */
package com.illusivesoulworks.cherishedworlds.mixins.late.advanced;

import com.illusivesoulworks.cherishedworlds.compat.EfrWorldFeatures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** EFR owns screenshot capture when enabled; AWS must not rename or overwrite its icon.png. */
@Mixin(value = sedridor.worldselectionadvanced.WorldSelectionAdvanced.class, remap = false)
public abstract class MixinAdvancedIconCapture {
    @Shadow protected static boolean keepWorldIconUpdated;

    @Redirect(method = {"onGuiOpened", "onWorldUnload"}, at = @At(value = "FIELD",
        target = "Lsedridor/worldselectionadvanced/WorldSelectionAdvanced;keepWorldIconUpdated:Z"))
    private boolean cherishedworlds$avoidCompetingFiles() {
        return !EfrWorldFeatures.enabled() && keepWorldIconUpdated;
    }

    @Inject(method = {"createWorldIcon", "resetWorldIcon"}, at = @At("HEAD"), cancellable = true)
    private void cherishedworlds$preferEfrCapture(CallbackInfo ci) {
        if (EfrWorldFeatures.enabled()) ci.cancel();
    }
}
