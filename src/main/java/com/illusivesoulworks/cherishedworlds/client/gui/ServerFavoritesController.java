/* SPDX-License-Identifier: LGPL-3.0-or-later
 * Cherished Worlds by Illusive Soulworks; 1.7.10 backport by Angelic Industries. */
package com.illusivesoulworks.cherishedworlds.client.gui;

import com.illusivesoulworks.cherishedworlds.client.ClientFavorites;
import com.illusivesoulworks.cherishedworlds.client.favorites.FavoriteOrder;
import com.illusivesoulworks.cherishedworlds.mixins.early.AccessorGuiScreen;
import com.illusivesoulworks.cherishedworlds.mixins.early.AccessorServerSelectionList;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.*;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;

public final class ServerFavoritesController {
    public static String key(ServerData data) { return data.serverName + data.serverIP; }
    public static String key(GuiListExtended.IGuiListEntry entry) {
        if (entry instanceof ServerListEntryNormal) return key(((ServerListEntryNormal) entry).func_148296_a());
        if (entry instanceof ServerListEntryLanDetected) return ((ServerListEntryLanDetected) entry).func_148289_a().getServerIpPort();
        return null;
    }
    public static int size(ServerSelectionList list) {
        AccessorServerSelectionList access = (AccessorServerSelectionList) list;
        return access.cherishedworlds$saved().size() + 1 + access.cherishedworlds$lan().size();
    }
    public static String selected(ServerScreenAccess access) {
        ServerSelectionList list = access.cherishedworlds$list();
        if (list == null) return null;
        int index = list.func_148193_k();
        return index >= 0 && index < size(list) ? key(list.getListEntry(index)) : null;
    }
    public void refresh(GuiScreen gui, ServerScreenAccess access) {
        ServerSelectionList list = access.cherishedworlds$list();
        if (list == null) return;
        String selected = selected(access);
        AccessorServerSelectionList entries = (AccessorServerSelectionList) list;
        List<ServerListEntryNormal> saved = entries.cherishedworlds$saved();
        List<ServerListEntryNormal> before = new ArrayList<>(saved);
        FavoriteOrder.sort(saved, ServerFavoritesController::key, ClientFavorites.get()::contains, (a, b) -> 0);
        FavoriteOrder.sort(entries.cherishedworlds$lan(), ServerFavoritesController::key, ClientFavorites.get()::contains, (a, b) -> 0);
        if (!before.equals(saved)) {
            ServerList servers = access.cherishedworlds$servers();
            for (int i = 0; i < saved.size(); i++) servers.func_147413_a(i, saved.get(i).func_148296_a());
            servers.saveServerList();
        }
        int index = -1;
        if (selected != null) for (int i = 0; i < size(list); i++) {
            if (selected.equals(key(list.getListEntry(i)))) { index = i; break; }
        }
        access.cherishedworlds$select(index);
        for (GuiButton button : ((AccessorGuiScreen) gui).cherishedworlds$buttons()) {
            if (button.id == 2 && ClientFavorites.get().contains(selected)) button.enabled = false;
        }
    }
    public void draw(GuiScreen gui, ServerScreenAccess access, int x, int y) {
        refresh(gui, access);
        ServerSelectionList list = access.cherishedworlds$list();
        if (list == null) return;
        Boolean hovered = null;
        for (int i = 0; i < size(list); i++) {
            String key = key(list.getListEntry(i));
            if (key == null) continue;
            StarGeometry star = StarRenderer.geometry(list, i);
            boolean favorite = ClientFavorites.get().contains(key), over = star.contains(x, y);
            StarRenderer.draw(star, favorite, over);
            if (over) hovered = favorite;
        }
        if (hovered != null) StarRenderer.tooltip(gui, hovered, x, y);
        StarRenderer.error(gui);
    }
    public boolean click(GuiScreen gui, ServerScreenAccess access, int x, int y, int button) {
        refresh(gui, access);
        ServerSelectionList list = access.cherishedworlds$list();
        if (button != 0 || list == null) return false;
        for (int i = 0; i < size(list); i++) if (StarRenderer.geometry(list, i).contains(x, y)) return toggle(gui, access, i);
        return false;
    }
    public boolean toggle(GuiScreen gui, ServerScreenAccess access, int row) {
        ServerSelectionList list = access.cherishedworlds$list();
        if (list == null || row < 0 || row >= size(list)) return false;
        String key = key(list.getListEntry(row));
        if (key == null) return false;
        if (ClientFavorites.toggle(key)) {
            access.cherishedworlds$select(row); refresh(gui, access);
            StarRenderer.reveal(list, list.func_148193_k());
        }
        return true;
    }
}
