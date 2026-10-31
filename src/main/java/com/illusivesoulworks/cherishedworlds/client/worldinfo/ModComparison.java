/* SPDX-License-Identifier: LGPL-3.0-or-later
 * Cherished Worlds by Illusive Soulworks; 1.7.10 backport by Angelic Industries. */
package com.illusivesoulworks.cherishedworlds.client.worldinfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public final class ModComparison {
    public enum Status { MISSING, CHANGED, ADDED, UNCHANGED }
    public static final class Entry {
        public final String id, savedVersion, currentVersion;
        public final Status status;
        private Entry(String id, String saved, String current, Status status) {
            this.id = id; this.savedVersion = saved; this.currentVersion = current; this.status = status;
        }
    }
    private ModComparison() {}
    public static List<Entry> compare(Map<String, String> saved, Map<String, String> current, boolean unchanged) {
        List<Entry> result = new ArrayList<>();
        TreeSet<String> ids = new TreeSet<>(saved.keySet()); ids.addAll(current.keySet());
        for (String id : ids) {
            String old = saved.get(id), now = current.get(id);
            Status status = old == null ? Status.ADDED : now == null ? Status.MISSING
                : old.equals(now) ? Status.UNCHANGED : Status.CHANGED;
            if (unchanged || status != Status.UNCHANGED) result.add(new Entry(id, old, now, status));
        }
        result.sort((a, b) -> a.status.compareTo(b.status));
        return result;
    }
}
