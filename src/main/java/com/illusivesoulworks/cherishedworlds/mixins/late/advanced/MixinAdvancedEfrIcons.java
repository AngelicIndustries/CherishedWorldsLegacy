/* SPDX-License-Identifier: LGPL-3.0-or-later
 * Cherished Worlds by Illusive Soulworks; 1.7.10 backport by Angelic Industries. */
package com.illusivesoulworks.cherishedworlds.mixins.late.advanced;

import com.illusivesoulworks.cherishedworlds.client.gui.WorldFavoritesController;
import com.illusivesoulworks.cherishedworlds.client.gui.WorldScreenAccess;
import com.illusivesoulworks.cherishedworlds.compat.EfrWorldFeatures;
import ganymedes01.etfuturum.client.WorldIconManager;
import java.io.File;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = sedridor.worldselectionadvanced.GuiWorldSelection.class, remap = false)
public abstract class MixinAdvancedEfrIcons {
    @Inject(method = "getWorldIcon(Ljava/io/File;I)V", at = @At("HEAD"), cancellable = true)
    private void cherishedworlds$useEfrLoader(File file, int row, CallbackInfo ci) {
        if (EfrWorldFeatures.enabled()) ci.cancel();
    }
    @Inject(method = "getWorldIcon(I)Lnet/minecraft/client/renderer/texture/DynamicTexture;", at = @At("HEAD"), cancellable = true)
    private void cherishedworlds$icon(int row, CallbackInfoReturnable<DynamicTexture> cir) {
        if (!EfrWorldFeatures.enabled()) return;
        String folder = WorldFavoritesController.key((WorldScreenAccess) this, row);
        if (folder == null) { cir.setReturnValue(null); return; }
        ResourceLocation location = WorldIconManager.getOrLoadIcon(folder);
        ITextureObject texture = Minecraft.getMinecraft().getTextureManager().getTexture(location);
        cir.setReturnValue(texture instanceof DynamicTexture ? (DynamicTexture) texture : null);
    }
    @Inject(method = "getWorldTexture", at = @At("HEAD"), cancellable = true)
    private void cherishedworlds$texture(int row, CallbackInfoReturnable<ResourceLocation> cir) {
        if (EfrWorldFeatures.enabled()) {
            String folder = WorldFavoritesController.key((WorldScreenAccess) this, row);
            if (folder != null) cir.setReturnValue(WorldIconManager.getOrLoadIcon(folder));
        }
    }
}
