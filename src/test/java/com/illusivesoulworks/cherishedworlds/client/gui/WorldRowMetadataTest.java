package com.illusivesoulworks.cherishedworlds.client.gui;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;

public class WorldRowMetadataTest {
    @Test public void sortingMovesMetadataWithFolder() {
        WorldRowMetadata<String> cache = new WorldRowMetadata<>();
        Map<Integer, String> rows = new HashMap<>();
        rows.put(0, "seed A"); rows.put(1, "seed B"); rows.put(2, "seed C");
        cache.capture(Arrays.asList("A", "B", "C"), rows);
        cache.restore(Arrays.asList("C", "A", "B"), rows);
        assertEquals("seed C", rows.get(0)); assertEquals("seed A", rows.get(1)); assertEquals("seed B", rows.get(2));
    }
    @Test public void clearingFilterRestoresHiddenMetadataAndMissingIconsStayMissing() {
        WorldRowMetadata<String> cache = new WorldRowMetadata<>();
        Map<Integer, String> rows = new HashMap<>();
        rows.put(0, "icon A"); rows.put(2, "icon C");
        cache.capture(Arrays.asList("A", "B", "C"), rows);
        cache.restore(Collections.singletonList("C"), rows);
        assertEquals(Collections.singletonMap(0, "icon C"), rows);
        cache.restore(Collections.emptyList(), rows); assertTrue(rows.isEmpty());
        cache.restore(Arrays.asList("B", "A", "C"), rows);
        assertFalse(rows.containsKey(0)); assertEquals("icon A", rows.get(1)); assertEquals("icon C", rows.get(2));
    }
    @Test public void reloadDropsDeletedWorldsAndStaleRowIndices() {
        WorldRowMetadata<String> cache = new WorldRowMetadata<>();
        Map<Integer, String> rows = new HashMap<>(); rows.put(0, "old A"); rows.put(7, "stale");
        cache.capture(Collections.singletonList("A"), rows);
        rows.clear(); rows.put(0, "new B"); cache.capture(Collections.singletonList("B"), rows);
        cache.restore(Arrays.asList("A", "B"), rows);
        assertFalse(rows.containsKey(0)); assertEquals("new B", rows.get(1)); assertEquals(1, rows.size());
        assertEquals(Collections.singletonList("new B"), cache.values());
    }
}
