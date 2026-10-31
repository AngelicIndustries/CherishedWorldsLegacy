/* SPDX-License-Identifier: LGPL-3.0-or-later
 * Cherished Worlds by Illusive Soulworks; 1.7.10 backport by Angelic Industries. */
package com.illusivesoulworks.cherishedworlds.client.gui;

import com.illusivesoulworks.cherishedworlds.CherishedWorlds;
import com.illusivesoulworks.cherishedworlds.Config;
import cpw.mods.fml.client.IModGuiFactory;
import cpw.mods.fml.client.config.GuiConfig;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;

public final class GuiFactory implements IModGuiFactory {
    @Override public void initialize(Minecraft minecraft) {}
    @Override public Class<? extends GuiScreen> mainConfigGuiClass() { return Screen.class; }
    @Override public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() { return null; }
    @Override public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) { return null; }

    public static final class Screen extends GuiConfig {
        public Screen(GuiScreen parent) {
            super(parent, new ConfigElement(Config.file.getCategory(Config.Categories.WORLD_TOOLTIPS)).getChildElements(),
                CherishedWorlds.MODID, false, false, "Cherished Worlds Legacy");
        }
    }
}
