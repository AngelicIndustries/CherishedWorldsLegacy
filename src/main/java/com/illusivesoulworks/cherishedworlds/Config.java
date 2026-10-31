/* SPDX-License-Identifier: LGPL-3.0-or-later
 * Cherished Worlds by Illusive Soulworks; 1.7.10 backport by Angelic Industries. */
package com.illusivesoulworks.cherishedworlds;

import java.io.File;
import java.util.Locale;
import net.minecraftforge.common.config.Configuration;

public final class Config {
    public static final class Categories {
        public static final String WORLD_TOOLTIPS = "worldtooltips";
        private Categories() {}
    }
    public enum Activation {
        HOVER, SHIFT, CTRL, NEVER;
        public boolean active(boolean shift, boolean ctrl) {
            return this == HOVER || this == SHIFT && shift || this == CTRL && ctrl;
        }
    }
    public static Configuration file;
    public static boolean enabled = true, showSeed = true, showWorldType = true, showTime = true,
        showPlayer = true, showUnchangedMods = false;
    public static Activation activation = Activation.HOVER, modDetails = Activation.CTRL;
    public static int hoverDelay = 250, maxWidth = 320, maxModLines = 8;
    private Config() {}

    public static void init(File path) { file = new Configuration(path); file.load(); sync(); }
    public static void sync() {
        enabled = bool("enabled", true, "Show a mouse-following tooltip over world entries.");
        activation = mode("activation", Activation.HOVER, "When to display world tooltips: HOVER, SHIFT, CTRL, or NEVER.");
        hoverDelay = integer("hoverDelay", 250, 0, 2000, "Milliseconds over the same world before displaying its tooltip.");
        maxWidth = integer("maxWidth", 320, 100, 600, "Maximum tooltip width in GUI pixels. Text wraps to fit the screen using the normal font size.");
        showSeed = bool("showSeed", true, "Show the world seed.");
        showWorldType = bool("showWorldType", true, "Show the world generator type.");
        showTime = bool("showTime", true, "Show the in-game day and time, with day zero as the first day.");
        showPlayer = bool("showPlayer", true, "Show the saved singleplayer position and dimension, when available.");
        modDetails = mode("modDetails", Activation.CTRL, "When to show saved/current mod differences: HOVER (always), SHIFT, CTRL, or NEVER.");
        showUnchangedMods = bool("showUnchangedMods", false, "Include mods whose version has not changed in the mod details.");
        maxModLines = integer("maxModLines", 8, 1, 50, "Maximum mod entries to display. Remaining entries are summarized; text also fits the screen height.");
        file.setCategoryPropertyOrder(Categories.WORLD_TOOLTIPS, java.util.Arrays.asList(
            "enabled", "activation", "hoverDelay", "maxWidth", "showSeed", "showWorldType", "showTime", "showPlayer",
            "modDetails", "showUnchangedMods", "maxModLines"));
        if (file.hasChanged()) file.save();
    }
    private static boolean bool(String name, boolean value, String comment) {
        return file.getBoolean(name, Categories.WORLD_TOOLTIPS, value, comment, "cherishedworlds.config." + name);
    }
    private static int integer(String name, int value, int min, int max, String comment) {
        return file.getInt(name, Categories.WORLD_TOOLTIPS, value, min, max, comment, "cherishedworlds.config." + name);
    }
    private static Activation mode(String name, Activation fallback, String comment) {
        String value = file.getString(name, Categories.WORLD_TOOLTIPS, fallback.name(), comment,
            new String[]{"HOVER", "SHIFT", "CTRL", "NEVER"}, "cherishedworlds.config." + name);
        try { return Activation.valueOf(value.toUpperCase(Locale.ROOT)); }
        catch (IllegalArgumentException e) {
            file.get(Categories.WORLD_TOOLTIPS, name, fallback.name()).set(fallback.name());
            return fallback;
        }
    }
}
