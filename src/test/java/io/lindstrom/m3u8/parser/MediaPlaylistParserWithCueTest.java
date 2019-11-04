package io.lindstrom.m3u8.parser;

import org.junit.Test;

import java.nio.file.Paths;

public class MediaPlaylistParserWithCueTest {
    private final AdSmartMediaPlaylistParser parser = new AdSmartMediaPlaylistParser();

    @Test
    public void parse1() throws Exception {
        parser.readPlaylist(Paths.get("src/test/resources/media/cue/cue1.m3u8"));
    }

    @Test
    public void parse2() throws Exception {
        parser.readPlaylist(Paths.get("src/test/resources/media/cue/cue2.m3u8"));
    }

    @Test
    public void parse3() throws Exception {
        parser.readPlaylist(Paths.get("src/test/resources/media/cue/cue3.m3u8"));
    }

    @Test
    public void parse4() throws Exception {
        parser.readPlaylist(Paths.get("src/test/resources/media/cue/cue4.m3u8"));
    }
}
