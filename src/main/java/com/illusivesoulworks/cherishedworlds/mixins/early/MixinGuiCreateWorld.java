/* SPDX-License-Identifier: LGPL-3.0-or-later
 * Cherished Worlds by Illusive Soulworks; 1.7.10 backport by Angelic Industries. */
package com.illusivesoulworks.cherishedworlds.mixins.early;

import com.illusivesoulworks.cherishedworlds.client.ClientFavorites;
import com.illusivesoulworks.cherishedworlds.client.gui.StarGeometry;
import com.illusivesoulworks.cherishedworlds.client.gui.StarRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiCreateWorld.class)
public abstract class MixinGuiCreateWorld extends GuiScreen {
    @Shadow private String field_146336_i;
    @Unique private boolean cherishedworlds$favorite;
    @Unique private StarGeometry cherishedworlds$star() {
        return new StarGeometry(width / 2 - 116, 66, 0, height);
    }
    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void cherishedworlds$click(int x, int y, int button, CallbackInfo ci) {
        if (button == 0 && cherishedworlds$star().contains(x, y)) {
            cherishedworlds$favorite = !cherishedworlds$favorite; ci.cancel();
        }
    }
    @Inject(method = "drawScreen", at = @At("RETURN"))
    private void cherishedworlds$draw(int x, int y, float tick, CallbackInfo ci) {
        StarGeometry star = cherishedworlds$star();
        boolean hover = star.contains(x, y);
        StarRenderer.draw(star, cherishedworlds$favorite, hover);
        if (hover) StarRenderer.tooltip(this, cherishedworlds$favorite, x, y);
        StarRenderer.error(this);
    }
    @Inject(method = "actionPerformed", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;launchIntegratedServer(Ljava/lang/String;Ljava/lang/String;Lnet/minecraft/world/WorldSettings;)V"))
    private void cherishedworlds$create(GuiButton button, CallbackInfo ci) {
        if (cherishedworlds$favorite) ClientFavorites.add(field_146336_i);
    }
}
