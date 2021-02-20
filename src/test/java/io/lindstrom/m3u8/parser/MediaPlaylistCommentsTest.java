package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MediaPlaylistCommentsTest {
    private final MediaPlaylistParser parser = new MediaPlaylistParser();

    @Test
    public void shouldAddCommentToMasterPlaylist() {
        MediaPlaylist playlist = MediaPlaylist.builder()
                .targetDuration(2)
                .addComments(
                        "Test Comment")
                .build();

        String expected = "#EXTM3U\n" +
                "#Test Comment\n" +
                "#EXT-X-TARGETDURATION:2\n" +
                "#EXT-X-MEDIA-SEQUENCE:0\n";

        assertEquals(expected, parser.writePlaylistAsString(playlist));
    }

    @Test
    public void shouldAddCommentsToMasterPlaylist() {
        MediaPlaylist playlist = MediaPlaylist.builder()
                .targetDuration(2)
                .addComments(
                        "Test Comment 1",
                        "Test Comment 2"
                )
                .build();

        String expected = "#EXTM3U\n" +
                "#Test Comment 1\n" +
                "#Test Comment 2\n" +
                "#EXT-X-TARGETDURATION:2\n" +
                "#EXT-X-MEDIA-SEQUENCE:0\n";

        assertEquals(expected, parser.writePlaylistAsString(playlist));
    }

    @Test
    public void shouldHaveCommentBetweenInitAndVersion() {
        MediaPlaylist playlist = MediaPlaylist.builder()
                .version(4)
                .targetDuration(2)
                .addComments("Test Comment")
                .build();

        String expected = "#EXTM3U\n" +
                "#Test Comment\n" +
                "#EXT-X-VERSION:4\n" +
                "#EXT-X-TARGETDURATION:2\n" +
                "#EXT-X-MEDIA-SEQUENCE:0\n";

        assertEquals(expected, parser.writePlaylistAsString(playlist));
    }

    @Test
    public void shouldOmitCommentsThatBeginWithExt() {
        MediaPlaylist playlist = MediaPlaylist.builder()
                .targetDuration(2)
                .version(4)
                .addComments(
                        "Test Comment",
                        "EXT Comment",
                        "New Comment")
                .build();

        String expected = "#EXTM3U\n" +
                "#Test Comment\n" +
                "#New Comment\n" +
                "#EXT-X-VERSION:4\n" +
                "#EXT-X-TARGETDURATION:2\n" +
                "#EXT-X-MEDIA-SEQUENCE:0\n";

        assertEquals(expected, parser.writePlaylistAsString(playlist));
    }
}
