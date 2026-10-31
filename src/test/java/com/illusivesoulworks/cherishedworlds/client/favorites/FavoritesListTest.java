package com.illusivesoulworks.cherishedworlds.client.favorites;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.minecraft.nbt.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import static org.junit.Assert.*;

public class FavoritesListTest {
    @Rule public TemporaryFolder directory = new TemporaryFolder();
    private Path path() { return directory.getRoot().toPath().resolve("cherishedworlds-favorites.dat"); }
    private FavoritesList load() throws Exception { FavoritesList list = new FavoritesList(path()); list.load(); return list; }

    @Test public void favoritesSurviveRestartAndToggleOff() throws Exception {
        FavoritesList list = load(); list.toggle("My World"); list.toggle("Другой мир");
        list = load(); assertTrue(list.contains("My World")); assertTrue(list.contains("Другой мир"));
        list.toggle("My World"); assertFalse(load().contains("My World")); assertTrue(load().contains("Другой мир"));
    }
    @Test public void readsUpstreamFormatAndPreservesOtherTags() throws Exception {
        NBTTagCompound root = new NBTTagCompound(); NBTTagList entries = new NBTTagList();
        entries.appendTag(new NBTTagString("World")); root.setTag("favorites", entries); root.setString("extra", "retain");
        CompressedStreamTools.write(root, path().toFile()); FavoritesList list = load(); assertTrue(list.contains("World"));
        list.toggle("Other"); assertEquals("retain", CompressedStreamTools.read(path().toFile()).getString("extra"));
    }
    @Test public void corruptFileIsNotOverwrittenByToggle() throws Exception {
        byte[] bad = {1, 2, 3}; Files.write(path(), bad); FavoritesList list = new FavoritesList(path());
        try { list.load(); fail(); } catch (java.io.IOException expected) {}
        try { list.toggle("World"); fail(); } catch (java.io.IOException expected) {}
        assertArrayEquals(bad, Files.readAllBytes(path())); assertFalse(list.contains("World"));
    }
    @Test public void wrongTypeFavoritesCannotBeSilentlyReplaced() throws Exception {
        NBTTagCompound root = new NBTTagCompound(); root.setString("favorites", "bad"); CompressedStreamTools.write(root, path().toFile());
        try { load(); fail(); } catch (java.io.IOException expected) {}
        assertEquals("bad", CompressedStreamTools.read(path().toFile()).getString("favorites"));
    }
    @Test public void previousSuccessfulFileIsBackedUp() throws Exception {
        FavoritesList list = load(); list.add("First"); byte[] previous = Files.readAllBytes(path()); list.add("Second");
        assertArrayEquals(previous, Files.readAllBytes(path().resolveSibling("cherishedworlds-favorites.dat.bak")));
    }
    @Test public void renamePersistsAndDoesNotFavoriteOrdinaryEntries() throws Exception {
        FavoritesList list = load(); list.add("Namehost:25565"); list.rename("Namehost:25565", "Newnewhost:25565"); list.rename("Other", "Another");
        list = load(); assertFalse(list.contains("Namehost:25565")); assertTrue(list.contains("Newnewhost:25565")); assertFalse(list.contains("Another"));
    }
    @Test public void failedSaveLeavesPreviousStateAndRevision() throws Exception {
        FavoritesList list = load(); list.add("First"); long revision = list.revision();
        Path backup = path().resolveSibling("cherishedworlds-favorites.dat.bak");
        Files.createDirectory(backup); Files.write(backup.resolve("keep"), new byte[]{1});
        try { list.toggle("Second"); fail(); } catch (java.io.IOException expected) {}
        assertEquals(revision, list.revision()); assertFalse(list.contains("Second")); assertTrue(load().contains("First"));
    }
    @Test public void favoritesSortFirstAndKeepSecondaryOrdering() {
        List<String> values = new ArrayList<>(Arrays.asList("c", "b", "a", "d"));
        FavoriteOrder.sort(values, s -> s, s -> s.equals("c") || s.equals("b"), Comparator.naturalOrder());
        assertEquals(Arrays.asList("b", "c", "a", "d"), values); assertEquals(2, FavoriteOrder.indexOf(values, s -> s, "a"));
        assertEquals(-1, FavoriteOrder.indexOf(values, s -> s, "absent"));
    }
    @Test public void stablePartitionPreservesManualServerOrder() {
        List<String> values = new ArrayList<>(Arrays.asList("d", "b", "a", "c"));
        FavoriteOrder.sort(values, s -> s, s -> s.equals("a") || s.equals("b"), (a, b) -> 0);
        assertEquals(Arrays.asList("b", "a", "d", "c"), values);
    }
}
