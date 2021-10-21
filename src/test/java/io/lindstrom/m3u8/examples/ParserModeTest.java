package io.lindstrom.m3u8.examples;

import io.lindstrom.m3u8.parser.MainPlaylistParser;
import io.lindstrom.m3u8.parser.MediaPlaylistParser;
import io.lindstrom.m3u8.parser.ParsingMode;
import io.lindstrom.m3u8.parser.PlaylistParserException;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;

public class ParserModeTest {
    @Test(expected = PlaylistParserException.class)
    public void strictParsingMainPlaylist() throws IOException {
        MainPlaylistParser parser = new MainPlaylistParser(ParsingMode.STRICT);
        parser.readPlaylist(Paths.get("src/test/resources/main/main-lenient.m3u8"));
    }

    @Test
    public void lenientParsingMainPlaylist() throws IOException {
        MainPlaylistParser parser = new MainPlaylistParser(ParsingMode.LENIENT);
        parser.readPlaylist(Paths.get("src/test/resources/main/main-lenient.m3u8"));
    }

    @Test(expected = PlaylistParserException.class)
    public void strictParsingMediaPlaylist() throws IOException {
        MediaPlaylistParser parser = new MediaPlaylistParser(ParsingMode.STRICT);
        parser.readPlaylist(Paths.get("src/test/resources/media/media-lenient.m3u8"));
    }

    @Test
    public void lenientParsingMediaPlaylist() throws IOException {
        MediaPlaylistParser parser = new MediaPlaylistParser(ParsingMode.LENIENT);
        parser.readPlaylist(Paths.get("src/test/resources/media/media-lenient.m3u8"));
    }
}
