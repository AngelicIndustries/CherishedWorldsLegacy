/* SPDX-License-Identifier: LGPL-3.0-or-later
 * Cherished Worlds by Illusive Soulworks; 1.7.10 backport by Angelic Industries. */
package com.illusivesoulworks.cherishedworlds;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = CherishedWorlds.MODID, name = "Cherished Worlds Legacy", version = Tags.VERSION,
    acceptedMinecraftVersions = "[1.7.10]", acceptableRemoteVersions = "*",
    dependencies = "required-after:unimixins",
    guiFactory = "com.illusivesoulworks.cherishedworlds.client.gui.GuiFactory",
    customProperties = {
        @Mod.CustomProperty(k = "license", v = "LGPL-3.0-or-later"),
        @Mod.CustomProperty(k = "issueTrackerUrl", v = "https://github.com/AngelicIndustries/CherishedWorldsLegacy/issues"),
        @Mod.CustomProperty(k = "iconFile", v = "assets/cherishedworlds/logo.png")
    })
public class CherishedWorlds {
    public static final String MODID = "cherishedworlds";
    public static final Logger LOG = LogManager.getLogger("Cherished Worlds");
    @SidedProxy(clientSide = "com.illusivesoulworks.cherishedworlds.client.ClientProxy",
        serverSide = "com.illusivesoulworks.cherishedworlds.CommonProxy")
    public static CommonProxy proxy;
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) { proxy.preInit(event); }
}
