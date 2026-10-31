/* SPDX-License-Identifier: LGPL-3.0-or-later
 * Cherished Worlds by Illusive Soulworks; 1.7.10 backport by Angelic Industries. */
package com.illusivesoulworks.cherishedworlds.client.worldinfo;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.zip.GZIPInputStream;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

/** Read-only snapshot of vanilla and Forge save metadata. */
public final class WorldInfoData {
    public final Long seed, time;
    public final String generator;
    public final double[] position;
    public final Integer dimension;
    public final Map<String, String> mods;
    public final boolean backup;

    private WorldInfoData(NBTTagCompound root, boolean backup) throws IOException {
        if (!root.hasKey("Data", 10)) throw new IOException("Missing world Data compound");
        NBTTagCompound data = root.getCompoundTag("Data");
        seed = data.hasKey("RandomSeed", 99) ? data.getLong("RandomSeed") : null;
        time = data.hasKey("DayTime", 99) ? Long.valueOf(data.getLong("DayTime"))
            : data.hasKey("Time", 99) ? Long.valueOf(data.getLong("Time")) : null;
        generator = data.getString("generatorName");
        NBTTagCompound player = data.getCompoundTag("Player");
        NBTTagList pos = player.getTagList("Pos", 6);
        double[] coordinates = pos.tagCount() == 3
            ? new double[]{pos.func_150309_d(0), pos.func_150309_d(1), pos.func_150309_d(2)} : null;
        if (coordinates != null) for (double coordinate : coordinates) {
            if (Double.isNaN(coordinate) || Double.isInfinite(coordinate)) { coordinates = null; break; }
        }
        position = coordinates;
        dimension = player.hasKey("Dimension", 99) ? player.getInteger("Dimension") : null;
        NBTTagCompound fml = root.getCompoundTag("FML");
        Map<String, String> saved = null;
        if (fml.hasKey("ModList", 9)) {
            saved = new TreeMap<>();
            NBTTagList list = fml.getTagList("ModList", 10);
            for (int i = 0; i < list.tagCount(); i++) {
                NBTTagCompound mod = list.getCompoundTagAt(i);
                String id = mod.getString("ModId");
                if (!id.isEmpty()) saved.put(id, mod.getString("ModVersion"));
            }
        }
        mods = saved == null ? null : Collections.unmodifiableMap(saved);
        this.backup = backup;
    }
    public static WorldInfoData read(Path directory) throws IOException {
        try { return readFile(directory.resolve("level.dat"), false); }
        catch (IOException | RuntimeException primary) {
            try { return readFile(directory.resolve("level.dat_old"), true); }
            catch (IOException | RuntimeException fallback) {
                IOException failure = new IOException("Cannot read world metadata in " + directory, primary);
                failure.addSuppressed(fallback);
                throw failure;
            }
        }
    }
    private static WorldInfoData readFile(Path path, boolean backup) throws IOException {
        try (DataInputStream input = new DataInputStream(new BufferedInputStream(new GZIPInputStream(Files.newInputStream(path))))) {
            return new WorldInfoData(CompressedStreamTools.func_152456_a(input, new NBTSizeTracker(64 * 1024 * 1024)), backup);
        }
    }
}
