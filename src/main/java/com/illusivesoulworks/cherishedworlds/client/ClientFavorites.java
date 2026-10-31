/* SPDX-License-Identifier: LGPL-3.0-or-later
 * Cherished Worlds by Illusive Soulworks; 1.7.10 backport by Angelic Industries. */
package com.illusivesoulworks.cherishedworlds.client;

import com.illusivesoulworks.cherishedworlds.CherishedWorlds;
import com.illusivesoulworks.cherishedworlds.client.favorites.FavoritesList;
import java.io.IOException;
import net.minecraft.client.Minecraft;

public final class ClientFavorites {
    private static FavoritesList list;
    private static long errorUntil;
    private ClientFavorites() {}
    public static FavoritesList get() {
        if (list == null) {
            list = new FavoritesList(Minecraft.getMinecraft().mcDataDir.toPath().resolve("cherishedworlds-favorites.dat"));
            try { list.load(); } catch (IOException exception) { failed(exception); }
        }
        return list;
    }
    public static boolean toggle(String key) {
        try { get().toggle(key); return true; } catch (IOException exception) { failed(exception); return false; }
    }
    public static boolean rename(String oldKey, String newKey) {
        try { get().rename(oldKey, newKey); return true; } catch (IOException exception) { failed(exception); return false; }
    }
    public static void add(String key) {
        try { get().add(key); } catch (IOException exception) { failed(exception); }
    }
    private static void failed(IOException exception) {
        errorUntil = System.currentTimeMillis() + 10000;
        CherishedWorlds.LOG.error("Could not load or save favorites. The existing file has been preserved.", exception);
    }
    public static boolean hasError() { return System.currentTimeMillis() < errorUntil; }
}
