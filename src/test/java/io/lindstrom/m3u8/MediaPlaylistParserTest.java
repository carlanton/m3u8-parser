package io.lindstrom.m3u8;

import org.junit.Test;

import java.nio.file.Paths;

public class MediaPlaylistParserTest {
    private final MediaPlaylistParser parser = new MediaPlaylistParser();

    @Test
    public void parse1() throws Exception {
        parser.readPlaylist(Paths.get("src/test/resources/media/apple-media.m3u8"));
    }

    @Test
    public void parse2() throws Exception {
        parser.readPlaylist(Paths.get("src/test/resources/media/iframe.m3u8"));
    }

    @Test
    public void parse3() throws Exception {
        parser.readPlaylist(Paths.get("src/test/resources/media/mp4.m3u8"));
    }

    @Test
    public void parse4() throws Exception {
        parser.readPlaylist(Paths.get("src/test/resources/media/mp4-iframe.m3u8"));
    }

    @Test
    public void parse5() throws Exception {
        parser.readPlaylist(Paths.get("src/test/resources/media/variant.m3u8"));
    }
}