/* SPDX-License-Identifier: LGPL-3.0-or-later
 * Cherished Worlds by Illusive Soulworks; 1.7.10 backport by Angelic Industries. */
package com.illusivesoulworks.cherishedworlds.mixins.early;

import net.minecraft.client.gui.GuiSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GuiSlot.class)
public interface AccessorGuiSlot {
    @Accessor("lastClicked") void cherishedworlds$lastClicked(long value);
    @Accessor("selectedElement") void cherishedworlds$selectedElement(int value);
}
