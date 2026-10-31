/* SPDX-License-Identifier: LGPL-3.0-or-later
 * Cherished Worlds by Illusive Soulworks; 1.7.10 backport by Angelic Industries. */
package com.illusivesoulworks.cherishedworlds.compat;

import cpw.mods.fml.common.Loader;
import ganymedes01.etfuturum.configuration.configs.ConfigMixins;

public final class EfrWorldFeatures {
    private EfrWorldFeatures() {}
    public static boolean enabled() {
        // Keep optional EFR classes behind the loaded-mod guard.
        return Loader.isModLoaded("etfuturum") && Present.enabled();
    }
    private static final class Present {
        static boolean enabled() { return ConfigMixins.worldSaveThumbnails; }
    }
}
