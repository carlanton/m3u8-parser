package io.lindstrom.m3u8.examples;

import io.lindstrom.m3u8.parser.MasterPlaylistParser;
import io.lindstrom.m3u8.parser.ParsingMode;
import io.lindstrom.m3u8.parser.PlaylistParserException;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class ParserModeTest {
    private final String playlist = "#EXTM3U\n" +
            "\n" +
            "#EXT-X-DEFINE:NAME=\"auth\",VALUE=\"?auth_token=/aazv/54334:pp2\"\n" +
            "\n" +
            "#EXT-X-STREAM-INF:BANDWIDTH=1156000,RESOLUTION=640x480,CODECS=\"avc1.4d001e,mp4a.40.2\",ANOTHER_ATTRIBUTE=xyz\n" +
            "bipbop_gear1/prog_index.m3u8{$auth}\n";

    @Test(expected = PlaylistParserException.class)
    public void strictParsing() throws PlaylistParserException {
        MasterPlaylistParser parser = new MasterPlaylistParser(ParsingMode.STRICT);
        parser.readPlaylist(playlist);
    }

    @Test
    public void lenientParsing() throws PlaylistParserException {
        MasterPlaylistParser parser = new MasterPlaylistParser(ParsingMode.LENIENT);
        assertNotNull(parser.readPlaylist(playlist));
    }
}
