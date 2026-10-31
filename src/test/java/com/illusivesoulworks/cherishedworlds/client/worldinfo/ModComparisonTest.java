package com.illusivesoulworks.cherishedworlds.client.worldinfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;

public class ModComparisonTest {
    @Test public void reportsMissingChangedAndAddedBeforeUnchanged() {
        Map<String, String> saved = new HashMap<>(), current = new HashMap<>();
        saved.put("same", "1"); current.put("same", "1");
        saved.put("update", "1"); current.put("update", "2");
        saved.put("removed", "3"); current.put("added", "4");
        List<ModComparison.Entry> differences = ModComparison.compare(saved, current, false);
        assertEquals(3, differences.size());
        assertEquals(ModComparison.Status.MISSING, differences.get(0).status); assertEquals("removed", differences.get(0).id);
        assertEquals(ModComparison.Status.CHANGED, differences.get(1).status);
        assertEquals("1", differences.get(1).savedVersion); assertEquals("2", differences.get(1).currentVersion);
        assertEquals(ModComparison.Status.ADDED, differences.get(2).status);
        List<ModComparison.Entry> all = ModComparison.compare(saved, current, true);
        assertEquals(4, all.size()); assertEquals(ModComparison.Status.UNCHANGED, all.get(3).status);
        assertEquals(3, saved.size()); assertEquals(3, current.size());
    }
}
