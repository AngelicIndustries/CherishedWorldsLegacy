/* SPDX-License-Identifier: LGPL-3.0-or-later
 * Cherished Worlds by Illusive Soulworks; 1.7.10 backport by Angelic Industries. */
package com.illusivesoulworks.cherishedworlds.client.worldinfo;

import com.illusivesoulworks.cherishedworlds.CherishedWorlds;
import com.illusivesoulworks.cherishedworlds.Config;
import com.illusivesoulworks.cherishedworlds.client.gui.WorldScreenAccess;
import com.illusivesoulworks.cherishedworlds.mixins.early.AccessorGuiScreen;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.resources.I18n;
import net.minecraft.world.storage.SaveFormatComparator;
import net.minecraft.world.storage.SaveFormatOld;

public final class WorldInfoTooltips {
    private final Map<String, WorldInfoData> cache = new HashMap<>();
    private final Map<String, String> currentMods = new HashMap<>();
    private String hovered;
    private long hoverStart;

    public void clear() { cache.clear(); resetHover(); }
    public void resetHover() { hovered = null; }

    public void draw(GuiScreen screen, WorldScreenAccess access, int x, int y) {
        boolean shift = GuiScreen.isShiftKeyDown(), ctrl = GuiScreen.isCtrlKeyDown();
        GuiSlot list = access.cherishedworlds$list();
        int index = y >= list.top && y < list.bottom ? list.func_148124_c(x, y) : -1;
        if (!Config.enabled || !Config.activation.active(shift, ctrl) || index < 0
            || index >= access.cherishedworlds$worlds().size()) { resetHover(); return; }
        SaveFormatComparator world = access.cherishedworlds$worlds().get(index);
        String folder = world.getFileName();
        long now = System.nanoTime();
        if (!folder.equals(hovered)) { hovered = folder; hoverStart = now; }
        if ((now - hoverStart) / 1_000_000 < Config.hoverDelay) return;
        Minecraft mc = Minecraft.getMinecraft();
        if (!(mc.getSaveLoader() instanceof SaveFormatOld)) return;
        if (!cache.containsKey(folder)) {
            Path directory = ((SaveFormatOld) mc.getSaveLoader()).savesDirectory.toPath().resolve(folder);
            WorldInfoData data = null;
            try { data = WorldInfoData.read(directory); }
            catch (IOException e) { CherishedWorlds.LOG.warn("Could not read tooltip metadata for {}", folder, e); }
            cache.put(folder, data);
        }
        List<String> lines = describe(world, cache.get(folder), shift, ctrl);
        ((AccessorGuiScreen) screen).cherishedworlds$tooltip(fit(lines, mc.fontRenderer, screen.width, screen.height, x), x, y);
    }

    private List<String> describe(SaveFormatComparator world, WorldInfoData data, boolean shift, boolean ctrl) {
        List<String> lines = new ArrayList<>();
        String name = world.getDisplayName();
        lines.add("\u00a7f" + (name.isEmpty() ? world.getFileName() : name));
        lines.add(text("folder", world.getFileName()));
        if (data == null) { lines.add(text("unavailable")); return lines; }
        if (data.backup) lines.add(text("backup"));
        if (Config.showSeed && data.seed != null) lines.add(text("seed", data.seed));
        if (Config.showWorldType && !data.generator.isEmpty()) {
            String key = "generator." + data.generator;
            lines.add(text("generator", !I18n.format(key).equals(key) ? I18n.format(key) : data.generator));
        }
        if (Config.showTime && data.time != null) {
            long ticks = Math.floorMod(data.time, 24000L);
            lines.add(text("time", Math.floorDiv(data.time, 24000L),
                String.format(Locale.ROOT, "%02d:%02d", (ticks / 1000 + 6) % 24, ticks % 1000 * 60 / 1000)));
        }
        if (Config.showPlayer && data.position != null) {
            lines.add(text("position", String.format(Locale.ROOT, "%.1f, %.1f, %.1f", data.position[0], data.position[1], data.position[2])));
            if (data.dimension != null) lines.add(text("dimension", dimension(data.dimension)));
        }
        if (Config.modDetails.active(shift, ctrl)) addMods(lines, data);
        else if (Config.modDetails == Config.Activation.CTRL) lines.add(text("holdCtrl"));
        else if (Config.modDetails == Config.Activation.SHIFT) lines.add(text("holdShift"));
        return lines;
    }

    private void addMods(List<String> lines, WorldInfoData data) {
        if (data.mods == null) { lines.add(text("modsUnavailable")); return; }
        if (currentMods.isEmpty()) {
            for (ModContainer mod : Loader.instance().getActiveModList()) currentMods.put(mod.getModId(), mod.getVersion());
        }
        lines.add(text("mods", data.mods.size(), currentMods.size()));
        List<ModComparison.Entry> entries = ModComparison.compare(data.mods, currentMods, Config.showUnchangedMods);
        if (entries.isEmpty()) lines.add(text("modsSame"));
        int limit = Math.min(entries.size(), Config.maxModLines);
        for (int i = 0; i < limit; i++) {
            ModComparison.Entry entry = entries.get(i);
            switch (entry.status) {
                case MISSING: lines.add(text("modMissing", entry.id, entry.savedVersion)); break;
                case CHANGED: lines.add(text("modChanged", entry.id, entry.savedVersion, entry.currentVersion)); break;
                case ADDED: lines.add(text("modAdded", entry.id, entry.currentVersion)); break;
                case UNCHANGED: lines.add(text("modUnchanged", entry.id, entry.currentVersion)); break;
            }
        }
        if (entries.size() > limit) lines.add(text("moreMods", entries.size() - limit));
    }

    private static String dimension(int id) {
        switch (id) {
            case -1: return I18n.format("cherishedworlds.tooltip.nether");
            case 0: return I18n.format("cherishedworlds.tooltip.overworld");
            case 1: return I18n.format("cherishedworlds.tooltip.end");
            default: return Integer.toString(id);
        }
    }
    private static String text(String key, Object... values) { return "\u00a77" + I18n.format("cherishedworlds.tooltip." + key, values); }

    // Vanilla handles cursor offsets and edge flipping. Wrap and cap text before it measures the box.
    static List<String> fit(List<String> lines, FontRenderer font, int width, int height, int mouseX) {
        int maxWidth = Math.max(20, Math.min(Config.maxWidth, Math.max(mouseX - 16, width - mouseX - 16)));
        List<String> wrapped = new ArrayList<>();
        for (String line : lines) wrapped.addAll(font.listFormattedStringToWidth(line, maxWidth));
        int maxLines = Math.max(2, (height - 14) / 10);
        if (wrapped.size() > maxLines) {
            int remaining = wrapped.size() - maxLines + 1;
            wrapped = new ArrayList<>(wrapped.subList(0, maxLines - 1));
            wrapped.add(font.trimStringToWidth(text("moreLines", remaining), maxWidth));
        }
        return wrapped;
    }
}
