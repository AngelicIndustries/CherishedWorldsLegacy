/* SPDX-License-Identifier: LGPL-3.0-or-later
 * Cherished Worlds by Illusive Soulworks; 1.7.10 backport by Angelic Industries. */
package com.illusivesoulworks.cherishedworlds.client.gui;

import com.illusivesoulworks.cherishedworlds.client.ClientFavorites;
import com.illusivesoulworks.cherishedworlds.client.favorites.FavoriteOrder;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.world.storage.SaveFormatComparator;

public final class WorldFavoritesController {
    private List<SaveFormatComparator> previous;
    private int previousSize;
    private long revision = -1;

    public void refresh(GuiScreen gui, WorldScreenAccess access) {
        List<SaveFormatComparator> worlds = access.cherishedworlds$worlds();
        if (worlds == null) return;
        long current = ClientFavorites.get().revision();
        if (worlds != previous || worlds.size() != previousSize || current != revision) {
            String selected = key(access, access.cherishedworlds$selected());
            List<SaveFormatComparator> sorted = new ArrayList<>(worlds);
            FavoriteOrder.sort(sorted, SaveFormatComparator::getFileName, ClientFavorites.get()::contains, SaveFormatComparator::compareTo);
            access.cherishedworlds$worlds(sorted);
            access.cherishedworlds$selected(FavoriteOrder.indexOf(sorted, SaveFormatComparator::getFileName, selected));
            previous = sorted; previousSize = sorted.size(); revision = current;
        }
        String selected = key(access, access.cherishedworlds$selected());
        for (Object object : ((com.illusivesoulworks.cherishedworlds.mixins.early.AccessorGuiScreen) gui).cherishedworlds$buttons()) {
            GuiButton button = (GuiButton) object;
            if (button.id == 1 || button.id == 2 || button.id == 6 || button.id == 7) {
                button.enabled = selected != null && (button.id != 2 || !ClientFavorites.get().contains(selected));
            }
        }
    }
    public static String key(WorldScreenAccess access, int row) {
        List<SaveFormatComparator> worlds = access.cherishedworlds$worlds();
        return worlds != null && row >= 0 && row < worlds.size() ? worlds.get(row).getFileName() : null;
    }
    public void draw(GuiScreen gui, WorldScreenAccess access, int x, int y) {
        refresh(gui, access);
        if (access.cherishedworlds$list() == null || access.cherishedworlds$worlds() == null) return;
        Boolean hovered = null;
        for (int i = 0; i < access.cherishedworlds$worlds().size(); i++) {
            StarGeometry star = StarRenderer.geometry(access.cherishedworlds$list(), i);
            boolean favorite = ClientFavorites.get().contains(key(access, i));
            boolean over = star.contains(x, y);
            StarRenderer.draw(star, favorite, over);
            if (over) hovered = favorite;
        }
        if (hovered != null) StarRenderer.tooltip(gui, hovered, x, y);
        StarRenderer.error(gui);
    }
    public boolean click(GuiScreen gui, WorldScreenAccess access, int x, int y, int button) {
        refresh(gui, access);
        if (button != 0 || access.cherishedworlds$list() == null || access.cherishedworlds$worlds() == null) return false;
        for (int i = 0; i < access.cherishedworlds$worlds().size(); i++) {
            if (StarRenderer.geometry(access.cherishedworlds$list(), i).contains(x, y)) return toggle(gui, access, i);
        }
        return false;
    }
    public boolean toggle(GuiScreen gui, WorldScreenAccess access, int row) {
        String key = key(access, row);
        if (key == null) return false;
        if (ClientFavorites.toggle(key)) {
            access.cherishedworlds$selected(row);
            refresh(gui, access);
            StarRenderer.reveal(access.cherishedworlds$list(), access.cherishedworlds$selected());
        }
        return true;
    }
}
