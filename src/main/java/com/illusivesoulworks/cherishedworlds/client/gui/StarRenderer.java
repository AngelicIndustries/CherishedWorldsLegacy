/* SPDX-License-Identifier: LGPL-3.0-or-later
 * Cherished Worlds by Illusive Soulworks; 1.7.10 backport by Angelic Industries. */
package com.illusivesoulworks.cherishedworlds.client.gui;

import com.illusivesoulworks.cherishedworlds.client.ClientFavorites;
import com.illusivesoulworks.cherishedworlds.mixins.early.AccessorGuiScreen;
import com.illusivesoulworks.cherishedworlds.mixins.early.AccessorGuiSlot;
import java.util.Arrays;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public final class StarRenderer {
    private static final ResourceLocation EMPTY = texture("empty"), FAVORITE = texture("favorite"),
        HOVER_EMPTY = texture("hovered_empty"), HOVER_FAVORITE = texture("hovered_favorite");
    private StarRenderer() {}
    private static ResourceLocation texture(String name) { return new ResourceLocation("cherishedworlds", "textures/gui/" + name + ".png"); }
    public static StarGeometry geometry(GuiSlot list, int row) {
        int x = list.left + list.width / 2 - list.getListWidth() / 2 + 2 - 16;
        int y = list.top + 4 - list.getAmountScrolled() + list.headerPadding + row * list.slotHeight + (list.slotHeight - 4 - StarGeometry.SIZE) / 2;
        return new StarGeometry(x, y, list.top, list.bottom);
    }
    public static void draw(StarGeometry star, boolean favorite, boolean hovered) {
        if (!star.visible()) return;
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT | GL11.GL_COLOR_BUFFER_BIT | GL11.GL_CURRENT_BIT);
        GL11.glDisable(GL11.GL_LIGHTING); GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND); GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1, 1, 1, 1);
        Minecraft.getMinecraft().getTextureManager().bindTexture(favorite ? (hovered ? HOVER_FAVORITE : FAVORITE) : (hovered ? HOVER_EMPTY : EMPTY));
        Gui.func_146110_a(star.x, star.top, 0, star.top - star.y, StarGeometry.SIZE, star.bottom - star.top, StarGeometry.SIZE, StarGeometry.SIZE);
        GL11.glPopAttrib();
    }
    public static void tooltip(GuiScreen screen, boolean favorite, int x, int y) {
        String action = I18n.format("selectWorld.cherishedworlds." + (favorite ? "unfavorite" : "favorite"));
        ((AccessorGuiScreen) screen).cherishedworlds$tooltip(screen instanceof FavoriteScreen
            ? Arrays.asList(action, I18n.format("selectWorld.cherishedworlds.shortcut"))
            : java.util.Collections.singletonList(action), x, y);
    }
    public static void error(GuiScreen screen) {
        if (ClientFavorites.hasError()) screen.drawCenteredString(Minecraft.getMinecraft().fontRenderer,
            I18n.format("selectWorld.cherishedworlds.saveError"), screen.width / 2, screen.height - 63, 0xFF7777);
    }
    public static void reveal(GuiSlot list, int index) {
        if (index < 0) return;
        StarGeometry star = geometry(list, index);
        if (star.y < list.top + 4) list.scrollBy(star.y - list.top - 4);
        else if (star.y + StarGeometry.SIZE > list.bottom - 4) list.scrollBy(star.y + StarGeometry.SIZE - list.bottom + 4);
        ((AccessorGuiSlot) list).cherishedworlds$lastClicked(0);
        ((AccessorGuiSlot) list).cherishedworlds$selectedElement(-1);
    }
}
