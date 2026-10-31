/* SPDX-License-Identifier: LGPL-3.0-or-later
 * Cherished Worlds by Illusive Soulworks; 1.7.10 backport by Angelic Industries. */
package com.illusivesoulworks.cherishedworlds.core;

import com.gtnewhorizon.gtnhmixins.ILateMixinLoader;
import com.gtnewhorizon.gtnhmixins.LateMixin;
import cpw.mods.fml.relauncher.FMLLaunchHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@LateMixin
public class LateMixinLoader implements ILateMixinLoader {
    @Override public String getMixinConfig() { return "mixins.cherishedworlds.late.json"; }
    @Override public List<String> getMixins(Set<String> loadedMods) {
        List<String> mixins = new ArrayList<>();
        if (FMLLaunchHandler.side().isClient() && loadedMods.contains("WorldSelectionAdvanced")) {
            mixins.add("advanced.MixinAdvancedWorldSelection");
            mixins.add("advanced.MixinAdvancedWorldList");
            if (loadedMods.contains("etfuturum")) {
                mixins.add("advanced.MixinAdvancedEfrIcons");
                mixins.add("advanced.MixinAdvancedIconCapture");
            }
        }
        return mixins;
    }
}
