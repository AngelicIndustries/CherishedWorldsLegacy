/* SPDX-License-Identifier: LGPL-3.0-or-later
 * Cherished Worlds by Illusive Soulworks; 1.7.10 backport by Angelic Industries. */
package com.illusivesoulworks.cherishedworlds.mixins.late.advanced;

import com.illusivesoulworks.cherishedworlds.client.gui.WorldScreenAccess;
import net.minecraft.client.gui.GuiSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "sedridor.worldselectionadvanced.GuiWorldSelection$List", remap = false)
public abstract class MixinAdvancedWorldList {
    @Unique private WorldScreenAccess cherishedworlds$advancedScreen;
    @Inject(method = "<init>", at = @At("RETURN"))
    private void cherishedworlds$capture(@Coerce Object screen, CallbackInfo ci) {
        cherishedworlds$advancedScreen = (WorldScreenAccess) screen;
        cherishedworlds$advancedScreen.cherishedworlds$list((GuiSlot) (Object) this);
    }
    @Inject(method = {"elementClicked", "func_148144_a"}, at = @At("RETURN"))
    private void cherishedworlds$buttons(int row, boolean twice, int x, int y, CallbackInfo ci) {
        cherishedworlds$advancedScreen.cherishedworlds$refresh();
    }
}
