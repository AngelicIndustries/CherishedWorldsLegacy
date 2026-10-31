package com.illusivesoulworks.cherishedworlds;

import java.io.File;
import net.minecraftforge.common.config.ConfigElement;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import static org.junit.Assert.*;

public class ConfigTest {
    @Rule public TemporaryFolder directory = new TemporaryFolder();
    private Object previousHome;
    private java.lang.reflect.Field minecraftHome;
    @org.junit.Before public void initializeForgeDirectory() throws Exception {
        minecraftHome = cpw.mods.fml.relauncher.FMLInjectionData.class.getDeclaredField("minecraftHome");
        minecraftHome.setAccessible(true); previousHome = minecraftHome.get(null);
        minecraftHome.set(null, directory.getRoot());
    }
    @org.junit.After public void restoreForgeDirectory() throws Exception { minecraftHome.set(null, previousHome); }
    @Test public void configCategoryExposesSettingsAndPersistsChanges() throws Exception {
        File file = new File(directory.getRoot(), "cherishedworlds.cfg"); Config.init(file);
        assertEquals(11, new ConfigElement(Config.file.getCategory(Config.Categories.WORLD_TOOLTIPS)).getChildElements().size());
        Config.file.get(Config.Categories.WORLD_TOOLTIPS, "showSeed", true).set(false);
        Config.file.get(Config.Categories.WORLD_TOOLTIPS, "activation", "HOVER").set("SHIFT");
        Config.sync(); Config.init(file);
        assertFalse(Config.showSeed); assertEquals(Config.Activation.SHIFT, Config.activation);
        assertFalse(Config.activation.active(false, true)); assertTrue(Config.activation.active(true, false));
        Config.file.get(Config.Categories.WORLD_TOOLTIPS, "activation", "HOVER").set("invalid");
        Config.sync(); assertEquals(Config.Activation.HOVER, Config.activation);
    }
}
