package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MasterPlaylistCommentsTest {
    private final MasterPlaylistParser parser = new MasterPlaylistParser();

    @Test
    public void shouldAddCommentToMasterPlaylist() {
        MasterPlaylist playlist = MasterPlaylist.builder()
                .addComments("Test Comment")
                .build();

        String expected = "#EXTM3U\n" +
                "#Test Comment\n";

        assertEquals(expected, parser.writePlaylistAsString(playlist));
    }

    @Test
    public void shouldAddCommentsToMasterPlaylist() {
        MasterPlaylist playlist = MasterPlaylist.builder()
                .addComments(
                        "Test Comment 1",
                        "Test Comment 2"
                )
                .build();

        String expected = "#EXTM3U\n" +
                "#Test Comment 1\n" +
                "#Test Comment 2\n";

        assertEquals(expected, parser.writePlaylistAsString(playlist));
    }

    @Test
    public void shouldHaveCommentBetweenInitAndVersion() {
        MasterPlaylist playlist = MasterPlaylist.builder()
                .version(4)
                .addComments("Test Comment")
                .build();

        String expected = "#EXTM3U\n" +
                "#Test Comment\n" +
                "#EXT-X-VERSION:4\n";

        assertEquals(expected, parser.writePlaylistAsString(playlist));
    }

    @Test
    public void shouldOmitCommentsThatBeginWithExt() {
        MasterPlaylist playlist = MasterPlaylist.builder()
                .version(4)
                .addComments(
                        "Test Comment",
                        "EXT Comment",
                        "New Comment")
                .build();

        String expected = "#EXTM3U\n" +
                "#Test Comment\n" +
                "#New Comment\n" +
                "#EXT-X-VERSION:4\n";

        assertEquals(expected, parser.writePlaylistAsString(playlist));
    }
}
