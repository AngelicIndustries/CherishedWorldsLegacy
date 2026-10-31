/* SPDX-License-Identifier: LGPL-3.0-or-later
 * Cherished Worlds by Illusive Soulworks; 1.7.10 backport by Angelic Industries. */
package com.illusivesoulworks.cherishedworlds.client.gui;

import java.util.List;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.world.storage.SaveFormatComparator;

public interface WorldScreenAccess extends FavoriteScreen {
    List<SaveFormatComparator> cherishedworlds$worlds();
    void cherishedworlds$worlds(List<SaveFormatComparator> worlds);
    int cherishedworlds$selected();
    void cherishedworlds$selected(int index);
    GuiSlot cherishedworlds$list();
    void cherishedworlds$list(GuiSlot slot);
    void cherishedworlds$refresh();
}
