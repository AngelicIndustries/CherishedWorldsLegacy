/* SPDX-License-Identifier: LGPL-3.0-or-later
 * Cherished Worlds by Illusive Soulworks; 1.7.10 backport by Angelic Industries. */
package com.illusivesoulworks.cherishedworlds.mixins.early;

import com.illusivesoulworks.cherishedworlds.client.gui.WorldScreenAccess;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.GuiSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.client.gui.GuiSelectWorld$List")
public abstract class MixinWorldList {
    @Unique private WorldScreenAccess cherishedworlds$screen;
    @Inject(method = "<init>", at = @At("RETURN"))
    private void cherishedworlds$capture(GuiSelectWorld screen, CallbackInfo ci) {
        cherishedworlds$screen = (WorldScreenAccess) screen;
        cherishedworlds$screen.cherishedworlds$list((GuiSlot) (Object) this);
    }
    @Inject(method = "elementClicked", at = @At("RETURN"))
    private void cherishedworlds$buttons(int row, boolean twice, int x, int y, CallbackInfo ci) {
        cherishedworlds$screen.cherishedworlds$refresh();
    }
}
