/* SPDX-License-Identifier: LGPL-3.0-or-later
 * Cherished Worlds by Illusive Soulworks; 1.7.10 backport by Angelic Industries. */
package com.illusivesoulworks.cherishedworlds.client.gui;

import net.minecraft.client.gui.ServerSelectionList;
import net.minecraft.client.multiplayer.ServerList;

public interface ServerScreenAccess extends FavoriteScreen {
    ServerSelectionList cherishedworlds$list();
    ServerList cherishedworlds$servers();
    void cherishedworlds$select(int index);
}
