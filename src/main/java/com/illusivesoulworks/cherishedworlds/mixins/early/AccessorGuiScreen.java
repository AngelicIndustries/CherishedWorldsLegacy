/* SPDX-License-Identifier: LGPL-3.0-or-later
 * Cherished Worlds by Illusive Soulworks; 1.7.10 backport by Angelic Industries. */
package com.illusivesoulworks.cherishedworlds.mixins.early;

import java.util.List;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.client.gui.GuiButton;

@Mixin(GuiScreen.class)
public interface AccessorGuiScreen {
    @Accessor("buttonList") List<GuiButton> cherishedworlds$buttons();
    @Invoker("func_146283_a") void cherishedworlds$tooltip(List<String> lines, int x, int y);
}
