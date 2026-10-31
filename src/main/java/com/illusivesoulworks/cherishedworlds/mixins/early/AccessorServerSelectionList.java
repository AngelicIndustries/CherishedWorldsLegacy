/* SPDX-License-Identifier: LGPL-3.0-or-later
 * Cherished Worlds by Illusive Soulworks; 1.7.10 backport by Angelic Industries. */
package com.illusivesoulworks.cherishedworlds.mixins.early;

import java.util.List;
import net.minecraft.client.gui.ServerSelectionList;
import net.minecraft.client.gui.ServerListEntryNormal;
import net.minecraft.client.gui.ServerListEntryLanDetected;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ServerSelectionList.class)
public interface AccessorServerSelectionList {
    @Accessor("field_148198_l") List<ServerListEntryNormal> cherishedworlds$saved();
    @Accessor("field_148199_m") List<ServerListEntryLanDetected> cherishedworlds$lan();
}
