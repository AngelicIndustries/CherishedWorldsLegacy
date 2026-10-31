/* SPDX-License-Identifier: LGPL-3.0-or-later
 * Cherished Worlds by Illusive Soulworks; 1.7.10 backport by Angelic Industries. */
package com.illusivesoulworks.cherishedworlds.mixins.late.advanced;

import com.illusivesoulworks.cherishedworlds.client.ClientFavorites;
import com.illusivesoulworks.cherishedworlds.client.gui.CustomWorldScreen;
import com.illusivesoulworks.cherishedworlds.client.gui.WorldFavoritesController;
import com.illusivesoulworks.cherishedworlds.client.gui.WorldRowMetadata;
import com.illusivesoulworks.cherishedworlds.compat.EfrWorldFeatures;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.world.storage.SaveFormatComparator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sedridor.worldselectionadvanced.WorldDataTexture;
import sedridor.worldselectionadvanced.WorldDataVersion;

@Mixin(value = sedridor.worldselectionadvanced.GuiWorldSelection.class, remap = false)
public abstract class MixinAdvancedWorldSelection extends GuiSelectWorld implements CustomWorldScreen {
    @Shadow private List<SaveFormatComparator> saveList;
    @Shadow private int selectionIndex;
    @Shadow private int highlightedIndex;
    @Shadow private boolean field_146643_x;
    @Shadow private HashMap<Integer, WorldDataVersion> versionList;
    @Shadow private HashMap<Integer, WorldDataTexture> textureList;

    @Unique private final WorldFavoritesController cherishedworlds$advancedController = new WorldFavoritesController();
    @Unique private final WorldRowMetadata<WorldDataVersion> cherishedworlds$versions = new WorldRowMetadata<>();
    @Unique private final WorldRowMetadata<WorldDataTexture> cherishedworlds$textures = new WorldRowMetadata<>();
    @Unique private GuiSlot cherishedworlds$advancedList;
    @Unique private List<SaveFormatComparator> cherishedworlds$allWorlds;
    @Unique private GuiTextField cherishedworlds$search;
    @Unique private String cherishedworlds$query = "";

    protected MixinAdvancedWorldSelection(GuiScreen parent) { super(parent); }

    @Override public List<SaveFormatComparator> cherishedworlds$worlds() { return saveList; }
    @Override public void cherishedworlds$worlds(List<SaveFormatComparator> worlds) {
        saveList = worlds;
        List<String> folders = cherishedworlds$folders(worlds);
        cherishedworlds$versions.restore(folders, versionList);
        cherishedworlds$textures.restore(folders, textureList);
        highlightedIndex = -1;
    }
    @Override public int cherishedworlds$selected() { return selectionIndex; }
    @Override public void cherishedworlds$selected(int index) { selectionIndex = index; }
    @Override public GuiSlot cherishedworlds$list() { return cherishedworlds$advancedList; }
    @Override public void cherishedworlds$list(GuiSlot list) { cherishedworlds$advancedList = list; }
    @Override public void cherishedworlds$refresh() { cherishedworlds$advancedController.refresh(this, this); }
    @Override public boolean cherishedworlds$toggleSelected() {
        return cherishedworlds$advancedController.toggle(this, this, selectionIndex);
    }
    @Override public boolean cherishedworlds$click(int x, int y, int button) {
        return cherishedworlds$advancedController.click(this, this, x, y, button);
    }
    @Unique private static List<String> cherishedworlds$folders(List<SaveFormatComparator> worlds) {
        List<String> folders = new ArrayList<>(worlds.size());
        for (SaveFormatComparator world : worlds) folders.add(world.getFileName());
        return folders;
    }

    @Inject(method = "getSaveList", at = @At("HEAD"))
    private void cherishedworlds$clearOldRows(CallbackInfo ci) {
        // AWS normally retains stale index entries when a world disappears.
        versionList.clear();
        for (WorldDataTexture texture : cherishedworlds$textures.values()) mc.getTextureManager().deleteTexture(texture.getWorldTexture());
        textureList.clear();
    }
    @Inject(method = "getSaveList", at = @At("RETURN"))
    private void cherishedworlds$captureMetadata(CallbackInfo ci) {
        cherishedworlds$allWorlds = new ArrayList<>(saveList);
        List<String> folders = cherishedworlds$folders(saveList);
        cherishedworlds$versions.capture(folders, versionList);
        cherishedworlds$textures.capture(folders, textureList);
    }
    @Inject(method = {"initGui", "func_73866_w_"}, at = @At("RETURN"))
    private void cherishedworlds$initAdvanced(CallbackInfo ci) {
        if (cherishedworlds$advancedList == null || saveList == null) return;
        if (EfrWorldFeatures.enabled()) {
            cherishedworlds$search = new GuiTextField(fontRendererObj, width / 2 - 100, 33, 200, 14);
            cherishedworlds$search.setMaxStringLength(50);
            cherishedworlds$search.setText(cherishedworlds$query);
            cherishedworlds$advancedList.top = 52;
            cherishedworlds$filter(cherishedworlds$query);
        } else {
            cherishedworlds$search = null;
            cherishedworlds$query = "";
        }
        cherishedworlds$refresh();
    }
    @Unique private void cherishedworlds$filter(String query) {
        cherishedworlds$query = query;
        if (cherishedworlds$search != null && !cherishedworlds$search.getText().equals(query)) {
            cherishedworlds$search.setText(query);
        }
        String lower = query.toLowerCase(Locale.ROOT);
        List<SaveFormatComparator> visible = new ArrayList<>();
        for (SaveFormatComparator world : cherishedworlds$allWorlds) {
            String name = world.getDisplayName();
            if ((name != null && name.toLowerCase(Locale.ROOT).contains(lower))
                || world.getFileName().toLowerCase(Locale.ROOT).contains(lower)) visible.add(world);
        }
        selectionIndex = -1;
        cherishedworlds$worlds(visible);
        cherishedworlds$advancedList.scrollBy(-cherishedworlds$advancedList.getAmountScrolled());
        cherishedworlds$refresh();
    }
    @Override public void onGuiClosed() {
        for (WorldDataTexture texture : cherishedworlds$textures.values()) mc.getTextureManager().deleteTexture(texture.getWorldTexture());
        textureList.clear();
        super.onGuiClosed();
    }
    @Override protected void keyTyped(char character, int key) {
        if (cherishedworlds$search != null && cherishedworlds$search.isFocused()) {
            if (key == 1) { cherishedworlds$search.setFocused(false); return; }
            String before = cherishedworlds$search.getText();
            cherishedworlds$search.textboxKeyTyped(character, key);
            if (!before.equals(cherishedworlds$search.getText())) cherishedworlds$filter(cherishedworlds$search.getText());
            return;
        }
        super.keyTyped(character, key);
    }
    @Override protected void mouseClicked(int x, int y, int button) {
        if (cherishedworlds$click(x, y, button)) return;
        if (cherishedworlds$search != null) cherishedworlds$search.mouseClicked(x, y, button);
        super.mouseClicked(x, y, button);
    }
    @Inject(method = {"drawScreen", "func_73863_a"}, at = @At("HEAD"))
    private void cherishedworlds$beforeDraw(int x, int y, float tick, CallbackInfo ci) { cherishedworlds$refresh(); }
    @Inject(method = {"drawScreen", "func_73863_a"}, at = @At("RETURN"))
    private void cherishedworlds$afterDraw(int x, int y, float tick, CallbackInfo ci) {
        cherishedworlds$advancedController.draw(this, this, x, y);
        if (cherishedworlds$search != null) {
            cherishedworlds$search.updateCursorCounter();
            cherishedworlds$search.drawTextBox();
        }
    }
    @ModifyArg(method = {"drawScreen", "func_73863_a"}, at = @At(value = "INVOKE",
        target = "Lsedridor/worldselectionadvanced/Tooltip;drawFixedTooltip(Ljava/util/List;IIIILnet/minecraft/client/gui/FontRenderer;)V"), index = 2)
    private int cherishedworlds$tooltipBelowSearch(int top) { return cherishedworlds$search == null ? top : Math.max(top, 56); }
    @Inject(method = {"actionPerformed", "func_146284_a"}, at = @At("HEAD"), cancellable = true)
    private void cherishedworlds$protectAdvancedAction(GuiButton button, CallbackInfo ci) {
        String folder = WorldFavoritesController.key(this, selectionIndex);
        if ((button.id == 1 || button.id == 2 || button.id == 6 || button.id == 7) && folder == null) ci.cancel();
        else if (button.id == 2 && ClientFavorites.get().contains(folder)) ci.cancel();
    }
    @Inject(method = {"confirmClicked", "func_73878_a"}, at = @At("HEAD"), cancellable = true)
    private void cherishedworlds$protectAdvancedConfirmation(boolean result, int row, CallbackInfo ci) {
        if (field_146643_x && result && ClientFavorites.get().contains(WorldFavoritesController.key(this, row))) {
            field_146643_x = false;
            mc.displayGuiScreen(this);
            ci.cancel();
        }
    }
}
