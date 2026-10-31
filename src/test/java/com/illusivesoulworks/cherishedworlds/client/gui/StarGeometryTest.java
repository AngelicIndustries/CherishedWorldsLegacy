package com.illusivesoulworks.cherishedworlds.client.gui;

import org.junit.Test;
import static org.junit.Assert.*;

public class StarGeometryTest {
    @Test public void hitAreaMatchesVisiblePixels() {
        StarGeometry star = new StarGeometry(30, 49, 52, 200);
        assertTrue(star.visible());
        assertFalse(star.contains(30, 51));
        assertTrue(star.contains(30, 52));
        assertTrue(star.contains(38, 57));
        assertFalse(star.contains(39, 57));
        assertFalse(star.contains(30, 58));
    }
    @Test public void bottomClippingDoesNotInterceptButtons() {
        StarGeometry star = new StarGeometry(30, 197, 52, 200);
        assertTrue(star.contains(30, 199));
        assertFalse(star.contains(30, 200));
        assertEquals(200, star.bottom);
    }
    @Test public void fullyScrolledOutRowsCannotBeClicked() {
        StarGeometry above = new StarGeometry(30, 43, 52, 200);
        StarGeometry below = new StarGeometry(30, 200, 52, 200);
        assertFalse(above.visible());
        assertFalse(above.contains(30, 51));
        assertFalse(below.visible());
        assertFalse(below.contains(30, 200));
    }
}
