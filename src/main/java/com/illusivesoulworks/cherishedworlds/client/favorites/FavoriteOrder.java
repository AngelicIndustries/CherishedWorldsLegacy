/* SPDX-License-Identifier: LGPL-3.0-or-later
 * Cherished Worlds by Illusive Soulworks; 1.7.10 backport by Angelic Industries. */
package com.illusivesoulworks.cherishedworlds.client.favorites;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public final class FavoriteOrder {
    private FavoriteOrder() {}

    public static <T> void sort(List<T> entries, Function<T, String> key, Predicate<String> favorite, Comparator<T> withinGroup) {
        entries.sort((a, b) -> {
            int group = Boolean.compare(favorite.test(key.apply(b)), favorite.test(key.apply(a)));
            return group != 0 ? group : withinGroup.compare(a, b);
        });
    }

    public static <T> int indexOf(List<T> entries, Function<T, String> key, String selected) {
        if (selected != null) for (int i = 0; i < entries.size(); i++) if (selected.equals(key.apply(entries.get(i)))) return i;
        return -1;
    }
}
