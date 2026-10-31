/* Copyright (C) 2018-2026 Illusive Soulworks. Licensed under LGPL-3.0-or-later.
 * Adapted for Minecraft 1.7.10 by Angelic Industries. */
package com.illusivesoulworks.cherishedworlds.client.favorites;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

/** Uses upstream's uncompressed NBT format and keys. Never edits world or server data. */
public final class FavoritesList {
    private final Path file;
    private Set<String> favorites = new HashSet<>();
    private NBTTagCompound data = new NBTTagCompound();
    private boolean writable;
    private long revision;

    public FavoritesList(Path file) { this.file = file.toAbsolutePath(); }

    public void load() throws IOException {
        writable = false;
        NBTTagCompound loaded = new NBTTagCompound();
        if (Files.exists(file)) {
            try (DataInputStream input = new DataInputStream(Files.newInputStream(file))) {
                loaded = CompressedStreamTools.func_152456_a(input, new NBTSizeTracker(4 * 1024 * 1024));
            } catch (RuntimeException exception) {
                throw new IOException("Invalid favorites file: " + file, exception);
            }
            if (!loaded.hasKey("favorites", 9)) throw new IOException("Missing favorites list: " + file);
            NBTTagList raw = (NBTTagList) loaded.getTag("favorites");
            if (raw.tagCount() > 0 && raw.func_150303_d() != 8) throw new IOException("Invalid favorites list: " + file);
        }
        Set<String> values = new HashSet<>();
        NBTTagList list = loaded.getTagList("favorites", 8);
        for (int i = 0; i < list.tagCount(); i++) {
            String key = list.getStringTagAt(i);
            if (!key.isEmpty()) values.add(key);
        }
        data = loaded;
        favorites = values;
        writable = true;
        revision++;
    }

    public boolean contains(String key) { return key != null && favorites.contains(key); }
    public long revision() { return revision; }

    public void toggle(String key) throws IOException {
        if (key == null || key.isEmpty()) throw new IOException("Cannot favorite an empty entry");
        Set<String> changed = new HashSet<>(favorites);
        if (!changed.remove(key)) changed.add(key);
        save(changed);
    }

    public void add(String key) throws IOException {
        if (!contains(key)) toggle(key);
    }

    public void rename(String oldKey, String newKey) throws IOException {
        if (!contains(oldKey) || oldKey.equals(newKey)) return;
        if (newKey == null || newKey.isEmpty()) throw new IOException("Cannot rename a favorite to an empty entry");
        Set<String> changed = new HashSet<>(favorites);
        changed.remove(oldKey);
        changed.add(newKey);
        save(changed);
    }

    private void save(Set<String> changed) throws IOException {
        if (!writable) throw new IOException("Favorites were not loaded successfully; preserving " + file);
        NBTTagCompound updated = (NBTTagCompound) data.copy();
        NBTTagList list = new NBTTagList();
        for (String value : new TreeSet<>(changed)) list.appendTag(new NBTTagString(value));
        updated.setTag("favorites", list);
        Files.createDirectories(file.getParent());
        Path temporary = Files.createTempFile(file.getParent(), "cherishedworlds-", ".tmp");
        try {
            try (FileOutputStream stream = new FileOutputStream(temporary.toFile());
                 DataOutputStream output = new DataOutputStream(stream)) {
                CompressedStreamTools.write(updated, output);
                output.flush();
                stream.getFD().sync();
            }
            if (Files.exists(file)) Files.copy(file, file.resolveSibling(file.getFileName() + ".bak"), StandardCopyOption.REPLACE_EXISTING);
            try {
                Files.move(temporary, file, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            } catch (AtomicMoveNotSupportedException exception) {
                Files.move(temporary, file, StandardCopyOption.REPLACE_EXISTING);
            }
            favorites = changed;
            data = updated;
            revision++;
        } finally {
            Files.deleteIfExists(temporary);
        }
    }
}
