/* SPDX-License-Identifier: LGPL-3.0-or-later
 * Cherished Worlds by Illusive Soulworks; 1.7.10 backport by Angelic Industries. */
package com.illusivesoulworks.cherishedworlds.client.gui;

/** Shared draw/hit geometry in scaled GUI coordinates. */
public final class StarGeometry {
    public static final int SIZE = 9;
    public final int x, y, top, bottom;
    public StarGeometry(int x, int y, int listTop, int listBottom) {
        this.x = x; this.y = y;
        top = Math.max(y, listTop); bottom = Math.min(y + SIZE, listBottom);
    }
    public boolean visible() { return bottom > top; }
    public boolean contains(int mouseX, int mouseY) {
        return visible() && mouseX >= x && mouseX < x + SIZE && mouseY >= top && mouseY < bottom;
    }
}
