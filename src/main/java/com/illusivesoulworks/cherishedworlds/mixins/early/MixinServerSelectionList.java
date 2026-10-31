/* SPDX-License-Identifier: LGPL-3.0-or-later
 * Cherished Worlds by Illusive Soulworks; 1.7.10 backport by Angelic Industries. */
package com.illusivesoulworks.cherishedworlds.mixins.early;

import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.ServerSelectionList;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerSelectionList.class)
public abstract class MixinServerSelectionList extends GuiListExtended {
    protected MixinServerSelectionList(Minecraft mc, int width, int height, int top, int bottom, int slotHeight) { super(mc, width, height, top, bottom, slotHeight); }
    @Inject(method = "getListWidth", at = @At("RETURN"), cancellable = true)
    private void cherishedworlds$gutter(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(Math.min(cir.getReturnValue(), width - 40));
    }
}
