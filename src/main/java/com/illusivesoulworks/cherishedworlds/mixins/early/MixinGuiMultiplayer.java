/* SPDX-License-Identifier: LGPL-3.0-or-later
 * Cherished Worlds by Illusive Soulworks; 1.7.10 backport by Angelic Industries. */
package com.illusivesoulworks.cherishedworlds.mixins.early;

import com.illusivesoulworks.cherishedworlds.client.ClientFavorites;
import com.illusivesoulworks.cherishedworlds.client.gui.ServerFavoritesController;
import com.illusivesoulworks.cherishedworlds.client.gui.ServerScreenAccess;
import net.minecraft.client.gui.*;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiMultiplayer.class)
public abstract class MixinGuiMultiplayer extends GuiScreen implements ServerScreenAccess {
    @Shadow private ServerSelectionList field_146803_h;
    @Shadow private ServerList field_146804_i;
    @Shadow private boolean field_146807_u;
    @Shadow private boolean field_146805_w;
    @Shadow private ServerData field_146811_z;
    @Shadow public abstract void func_146790_a(int index);
    @Unique private final ServerFavoritesController cherishedworlds$controller = new ServerFavoritesController();
    @Override public ServerSelectionList cherishedworlds$list() { return field_146803_h; }
    @Override public ServerList cherishedworlds$servers() { return field_146804_i; }
    @Override public void cherishedworlds$select(int index) { func_146790_a(index); }
    @Override public boolean cherishedworlds$click(int x, int y, int button) { return cherishedworlds$controller.click(this, this, x, y, button); }
    @Override public boolean cherishedworlds$toggleSelected() { return cherishedworlds$controller.toggle(this, this, field_146803_h.func_148193_k()); }
    @Inject(method = "initGui", at = @At("RETURN"))
    private void cherishedworlds$init(CallbackInfo ci) { cherishedworlds$controller.refresh(this, this); }
    @Inject(method = "drawScreen", at = @At("RETURN"))
    private void cherishedworlds$draw(int x, int y, float tick, CallbackInfo ci) { cherishedworlds$controller.draw(this, this, x, y); }
    // Unlike GuiScreen, this override also forwards clicks directly to the server list.
    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void cherishedworlds$mouse(int x, int y, int button, CallbackInfo ci) {
        if (cherishedworlds$click(x, y, button)) ci.cancel();
    }
    @Inject(method = "actionPerformed", at = @At("HEAD"), cancellable = true)
    private void cherishedworlds$protectAction(GuiButton button, CallbackInfo ci) {
        if (button.id == 2 && ClientFavorites.get().contains(ServerFavoritesController.selected(this))) ci.cancel();
    }
    @Inject(method = "confirmClicked", at = @At("HEAD"), cancellable = true)
    private void cherishedworlds$confirmation(boolean result, int id, CallbackInfo ci) {
        String selected = ServerFavoritesController.selected(this);
        if (result && field_146807_u && ClientFavorites.get().contains(selected)) {
            field_146807_u = false; mc.displayGuiScreen(this); ci.cancel();
        } else if (result && field_146805_w && selected != null && field_146811_z != null) {
            // Cancel the edit if favorites cannot be persisted, preserving the old identity.
            if (!ClientFavorites.rename(selected, ServerFavoritesController.key(field_146811_z))) {
                field_146805_w = false; mc.displayGuiScreen(this); ci.cancel();
            }
        }
    }
    @Inject(method = "keyTyped", at = @At("HEAD"), cancellable = true)
    private void cherishedworlds$keepGroups(char character, int key, CallbackInfo ci) {
        if (!isShiftKeyDown() || (key != 200 && key != 208)) return;
        int selected = field_146803_h.func_148193_k();
        int target = selected + (key == 200 ? -1 : 1);
        if (selected < 0 || selected >= field_146804_i.countServers() || target < 0 || target >= field_146804_i.countServers()) return;
        boolean first = ClientFavorites.get().contains(ServerFavoritesController.key(field_146804_i.getServerData(selected)));
        boolean second = ClientFavorites.get().contains(ServerFavoritesController.key(field_146804_i.getServerData(target)));
        if (first != second) ci.cancel();
    }
}
