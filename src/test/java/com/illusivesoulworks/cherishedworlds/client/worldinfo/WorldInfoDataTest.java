package com.illusivesoulworks.cherishedworlds.client.worldinfo;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import net.minecraft.nbt.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import static org.junit.Assert.*;

public class WorldInfoDataTest {
    @Rule public TemporaryFolder directory = new TemporaryFolder();
    private Path path() { return directory.getRoot().toPath(); }
    private void write(String name, NBTTagCompound root) throws Exception {
        try (OutputStream out = Files.newOutputStream(path().resolve(name))) { CompressedStreamTools.writeCompressed(root, out); }
    }
    private NBTTagCompound root(NBTTagCompound data) {
        NBTTagCompound root = new NBTTagCompound(); root.setTag("Data", data); return root;
    }
    @Test public void readsVanillaDetailsAndForgeModsWithoutChangingSave() throws Exception {
        NBTTagCompound data = new NBTTagCompound();
        data.setLong("RandomSeed", Long.MIN_VALUE); data.setString("generatorName", "flat");
        data.setLong("DayTime", 25000); data.setLong("Time", 90000);
        NBTTagCompound player = new NBTTagCompound(); NBTTagList pos = new NBTTagList();
        pos.appendTag(new NBTTagDouble(-12.5)); pos.appendTag(new NBTTagDouble(-3)); pos.appendTag(new NBTTagDouble(123.25));
        player.setTag("Pos", pos); player.setInteger("Dimension", -1); data.setTag("Player", player);
        NBTTagCompound root = root(data), fml = new NBTTagCompound(), mod = new NBTTagCompound();
        NBTTagList mods = new NBTTagList(); mod.setString("ModId", "example"); mod.setString("ModVersion", "1.2");
        mods.appendTag(mod); fml.setTag("ModList", mods); root.setTag("FML", fml);
        write("level.dat", root); byte[] before = Files.readAllBytes(path().resolve("level.dat"));
        WorldInfoData info = WorldInfoData.read(path());
        assertEquals(Long.valueOf(Long.MIN_VALUE), info.seed); assertEquals(Long.valueOf(25000), info.time);
        assertEquals("flat", info.generator); assertEquals(Integer.valueOf(-1), info.dimension);
        assertArrayEquals(new double[]{-12.5, -3, 123.25}, info.position, 0);
        assertEquals("1.2", info.mods.get("example")); assertFalse(info.backup);
        assertArrayEquals(before, Files.readAllBytes(path().resolve("level.dat")));
    }
    @Test public void missingOptionalFieldsRemainUnknown() throws Exception {
        write("level.dat", root(new NBTTagCompound())); WorldInfoData info = WorldInfoData.read(path());
        assertNull(info.seed); assertNull(info.time); assertNull(info.position); assertNull(info.dimension); assertNull(info.mods);
    }
    @Test public void legacyTimeAndEmptyForgeListAreDistinguishedFromMissingData() throws Exception {
        NBTTagCompound data = new NBTTagCompound(); data.setLong("Time", 12000);
        NBTTagCompound root = root(data), fml = new NBTTagCompound(); fml.setTag("ModList", new NBTTagList()); root.setTag("FML", fml);
        write("level.dat", root); WorldInfoData info = WorldInfoData.read(path());
        assertEquals(Long.valueOf(12000), info.time); assertNotNull(info.mods); assertTrue(info.mods.isEmpty());
    }
    @Test public void corruptPrimaryFallsBackWithoutOverwritingEitherFile() throws Exception {
        byte[] corrupt = {1, 2, 3}; Files.write(path().resolve("level.dat"), corrupt);
        NBTTagCompound data = new NBTTagCompound(); data.setLong("RandomSeed", 42); write("level.dat_old", root(data));
        WorldInfoData info = WorldInfoData.read(path()); assertTrue(info.backup); assertEquals(Long.valueOf(42), info.seed);
        assertArrayEquals(corrupt, Files.readAllBytes(path().resolve("level.dat")));
    }
    @Test public void invalidPrimaryCompoundFallsBack() throws Exception {
        write("level.dat", new NBTTagCompound()); write("level.dat_old", root(new NBTTagCompound()));
        assertTrue(WorldInfoData.read(path()).backup);
    }
    @Test(expected = java.io.IOException.class) public void unreadableSaveFailsCleanly() throws Exception {
        Files.write(path().resolve("level.dat"), new byte[]{1, 2, 3}); WorldInfoData.read(path());
    }
    @Test public void nonfinitePlayerPositionIsNotDisplayed() throws Exception {
        NBTTagCompound data = new NBTTagCompound(), player = new NBTTagCompound(); NBTTagList pos = new NBTTagList();
        pos.appendTag(new NBTTagDouble(1)); pos.appendTag(new NBTTagDouble(Double.NaN)); pos.appendTag(new NBTTagDouble(2));
        player.setTag("Pos", pos); data.setTag("Player", player); write("level.dat", root(data));
        assertNull(WorldInfoData.read(path()).position);
    }
}
