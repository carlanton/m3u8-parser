package io.lindstrom.m3u8.examples;

import io.lindstrom.m3u8.parser.MainPlaylistParser;
import io.lindstrom.m3u8.model.MainPlaylist;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class ParsePlaylists {
    private final Path mainPlaylist = Paths.get("src/test/resources/main/main.m3u8");
    private final MainPlaylistParser mainPlaylistParser = new MainPlaylistParser();

    @Test
    public void readPlaylist() throws Exception {
        Set<MainPlaylist> playlists = new HashSet<>();

        playlists.add(
                // From Path
                mainPlaylistParser.readPlaylist(mainPlaylist)
        );

        playlists.add(
                // From String
                mainPlaylistParser.readPlaylist(new String(Files.readAllBytes(mainPlaylist), UTF_8))
        );

        try (InputStream inputStream = Files.newInputStream(mainPlaylist)) {
            playlists.add(
                    // From InputStream
                    mainPlaylistParser.readPlaylist(inputStream)
            );
        }

        try (BufferedReader bufferedReader = Files.newBufferedReader(mainPlaylist, UTF_8)) {
            playlists.add(
                    // From BufferedReader
                    mainPlaylistParser.readPlaylist(bufferedReader)
            );
        }

        playlists.add(
                // From Iterator<String>
                mainPlaylistParser.readPlaylist(Files.readAllLines(mainPlaylist, UTF_8).iterator())
        );

        // All methods should produce the same playlist
        assertEquals(1, playlists.size());
    }

    @Test
    public void writePlaylist() throws Exception {
        MainPlaylist playlist = mainPlaylistParser.readPlaylist(mainPlaylist);

        byte[] bytes = mainPlaylistParser.writePlaylistAsBytes(playlist);
        String string = mainPlaylistParser.writePlaylistAsString(playlist);
        ByteBuffer byteBuffer = mainPlaylistParser.writePlaylistAsByteBuffer(playlist);

        assertArrayEquals(bytes, string.getBytes(UTF_8));
        assertArrayEquals(bytes, byteBuffer.array());
        assertArrayEquals(string.getBytes(UTF_8), byteBuffer.array());
    }
}
