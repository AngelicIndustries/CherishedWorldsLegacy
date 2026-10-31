/* SPDX-License-Identifier: LGPL-3.0-or-later
 * Cherished Worlds by Illusive Soulworks; 1.7.10 backport by Angelic Industries. */
package com.illusivesoulworks.cherishedworlds.client.gui;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** Keeps an index-based third-party cache associated with world folders across sorting/filtering. */
public final class WorldRowMetadata<T> {
    private final Map<String, T> byFolder = new HashMap<>();

    public void capture(List<String> folders, Map<Integer, T> indexed) {
        byFolder.clear();
        for (int i = 0; i < folders.size(); i++) {
            T value = indexed.get(i);
            if (value != null) byFolder.put(folders.get(i), value);
        }
    }

    public List<T> values() { return new ArrayList<>(byFolder.values()); }

    public void restore(List<String> folders, Map<Integer, T> indexed) {
        indexed.clear();
        for (int i = 0; i < folders.size(); i++) {
            T value = byFolder.get(folders.get(i));
            if (value != null) indexed.put(i, value);
        }
    }
}
