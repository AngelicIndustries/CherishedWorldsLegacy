/* SPDX-License-Identifier: LGPL-3.0-or-later
 * Cherished Worlds by Illusive Soulworks; 1.7.10 backport by Angelic Industries. */
package com.illusivesoulworks.cherishedworlds.mixins.early;

import com.illusivesoulworks.cherishedworlds.client.gui.FavoriteScreen;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiScreen.class)
public abstract class MixinGuiScreen {
    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void cherishedworlds$click(int x, int y, int button, CallbackInfo ci) {
        if ((Object) this instanceof FavoriteScreen && ((FavoriteScreen) this).cherishedworlds$click(x, y, button)) ci.cancel();
    }
    @Inject(method = "handleKeyboardInput", at = @At("HEAD"), cancellable = true)
    private void cherishedworlds$key(CallbackInfo ci) {
        if ((Object) this instanceof FavoriteScreen && Keyboard.getEventKeyState() && !Keyboard.isRepeatEvent()
            && Keyboard.getEventKey() == Keyboard.KEY_F && GuiScreen.isCtrlKeyDown() && GuiScreen.isShiftKeyDown()
            && ((FavoriteScreen) this).cherishedworlds$toggleSelected()) ci.cancel();
    }
}
