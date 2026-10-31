/* SPDX-License-Identifier: LGPL-3.0-or-later
 * Cherished Worlds by Illusive Soulworks; 1.7.10 backport by Angelic Industries. */
package com.illusivesoulworks.cherishedworlds.mixins.early;

import com.illusivesoulworks.cherishedworlds.client.ClientFavorites;
import com.illusivesoulworks.cherishedworlds.client.gui.WorldFavoritesController;
import com.illusivesoulworks.cherishedworlds.client.gui.WorldScreenAccess;
import java.util.List;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.world.storage.SaveFormatComparator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiSelectWorld.class)
public abstract class MixinGuiSelectWorld extends GuiScreen implements WorldScreenAccess {
    @Shadow private List<SaveFormatComparator> field_146639_s;
    @Shadow private int field_146640_r;
    @Shadow private boolean field_146643_x;
    @Unique private GuiSlot cherishedworlds$list;
    @Unique private final WorldFavoritesController cherishedworlds$controller = new WorldFavoritesController();
    @Override public List<SaveFormatComparator> cherishedworlds$worlds() { return field_146639_s; }
    @Override public void cherishedworlds$worlds(List<SaveFormatComparator> worlds) { field_146639_s = worlds; }
    @Override public int cherishedworlds$selected() { return field_146640_r; }
    @Override public void cherishedworlds$selected(int index) { field_146640_r = index; }
    @Override public GuiSlot cherishedworlds$list() { return cherishedworlds$list; }
    @Override public void cherishedworlds$list(GuiSlot list) { cherishedworlds$list = list; }
    @Override public void cherishedworlds$refresh() { cherishedworlds$controller.refresh(this, this); }
    @Override public boolean cherishedworlds$click(int x, int y, int button) { return cherishedworlds$controller.click(this, this, x, y, button); }
    @Override public boolean cherishedworlds$toggleSelected() { return cherishedworlds$controller.toggle(this, this, field_146640_r); }
    @Inject(method = "initGui", at = @At("RETURN"))
    private void cherishedworlds$init(CallbackInfo ci) { cherishedworlds$refresh(); }
    @Inject(method = "drawScreen", at = @At("HEAD"))
    private void cherishedworlds$refresh(int x, int y, float tick, CallbackInfo ci) { cherishedworlds$refresh(); }
    @Inject(method = "drawScreen", at = @At("RETURN"))
    private void cherishedworlds$draw(int x, int y, float tick, CallbackInfo ci) { cherishedworlds$controller.draw(this, this, x, y); }
    @Inject(method = "actionPerformed", at = @At("HEAD"), cancellable = true)
    private void cherishedworlds$protectAction(GuiButton button, CallbackInfo ci) {
        String key = WorldFavoritesController.key(this, field_146640_r);
        if ((button.id == 1 || button.id == 2 || button.id == 6 || button.id == 7) && key == null) ci.cancel();
        else if (button.id == 2 && ClientFavorites.get().contains(key)) ci.cancel();
    }
    @Inject(method = "confirmClicked", at = @At("HEAD"), cancellable = true)
    private void cherishedworlds$protectConfirmation(boolean result, int id, CallbackInfo ci) {
        if (field_146643_x && result && ClientFavorites.get().contains(WorldFavoritesController.key(this, id))) {
            field_146643_x = false; mc.displayGuiScreen(this); ci.cancel();
        }
    }
}
