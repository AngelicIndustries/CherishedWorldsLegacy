/* SPDX-License-Identifier: LGPL-3.0-or-later
 * Cherished Worlds by Illusive Soulworks; 1.7.10 backport by Angelic Industries. */
package com.illusivesoulworks.cherishedworlds.client;

import com.illusivesoulworks.cherishedworlds.CherishedWorlds;
import com.illusivesoulworks.cherishedworlds.CommonProxy;
import com.illusivesoulworks.cherishedworlds.Config;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ClientProxy extends CommonProxy {
    @Override public void preInit(FMLPreInitializationEvent event) {
        Config.init(event.getSuggestedConfigurationFile());
        FMLCommonHandler.instance().bus().register(this);
    }
    @SubscribeEvent
    public void configChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (CherishedWorlds.MODID.equals(event.modID)) Config.sync();
    }
}
