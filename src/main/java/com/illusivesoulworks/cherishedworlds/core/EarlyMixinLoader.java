/* SPDX-License-Identifier: LGPL-3.0-or-later
 * Cherished Worlds by Illusive Soulworks; 1.7.10 backport by Angelic Industries. */
package com.illusivesoulworks.cherishedworlds.core;

import com.gtnewhorizon.gtnhmixins.IEarlyMixinLoader;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import java.util.Collections;
import java.util.Arrays;
import cpw.mods.fml.relauncher.FMLLaunchHandler;
import java.util.List;
import java.util.Map;
import java.util.Set;

@IFMLLoadingPlugin.MCVersion("1.7.10")
public class EarlyMixinLoader implements IEarlyMixinLoader, IFMLLoadingPlugin {
    @Override public String getMixinConfig() { return "mixins.cherishedworlds.early.json"; }
    @Override public List<String> getMixins(Set<String> loadedCoreMods) { return FMLLaunchHandler.side().isClient() ? Arrays.asList(
        "AccessorGuiSlot", "AccessorGuiScreen", "MixinGuiScreen", "MixinGuiSelectWorld", "MixinWorldList", "MixinGuiCreateWorld", "AccessorServerSelectionList", "MixinGuiMultiplayer", "MixinServerSelectionList") : Collections.emptyList(); }
    @Override public String[] getASMTransformerClass() { return new String[0]; }
    @Override public String getModContainerClass() { return null; }
    @Override public String getSetupClass() { return null; }
    @Override public void injectData(Map<String, Object> data) {}
    @Override public String getAccessTransformerClass() { return null; }
}
