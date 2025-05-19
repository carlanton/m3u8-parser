package io.lindstrom.m3u8.examples;

import io.lindstrom.m3u8.model.MultivariantPlaylist;
import io.lindstrom.m3u8.parser.MultivariantPlaylistParser;
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
    private final Path multivariantPlaylist = Paths.get("src/test/resources/multivariant/multivariant.m3u8");
    private final MultivariantPlaylistParser multivariantPlaylistParser = new MultivariantPlaylistParser();

    @Test
    public void readPlaylist() throws Exception {
        Set<MultivariantPlaylist> playlists = new HashSet<>();

        playlists.add(
                // From Path
                multivariantPlaylistParser.readPlaylist(multivariantPlaylist)
        );

        playlists.add(
                // From String
                multivariantPlaylistParser.readPlaylist(new String(Files.readAllBytes(multivariantPlaylist), UTF_8))
        );

        try (InputStream inputStream = Files.newInputStream(multivariantPlaylist)) {
            playlists.add(
                    // From InputStream
                    multivariantPlaylistParser.readPlaylist(inputStream)
            );
        }

        try (BufferedReader bufferedReader = Files.newBufferedReader(multivariantPlaylist, UTF_8)) {
            playlists.add(
                    // From BufferedReader
                    multivariantPlaylistParser.readPlaylist(bufferedReader)
            );
        }

        playlists.add(
                // From Iterator<String>
                multivariantPlaylistParser.readPlaylist(Files.readAllLines(multivariantPlaylist, UTF_8).iterator())
        );

        // All methods should produce the same playlist
        assertEquals(1, playlists.size());
    }

    @Test
    public void writePlaylist() throws Exception {
        MultivariantPlaylist playlist = multivariantPlaylistParser.readPlaylist(multivariantPlaylist);

        byte[] bytes = multivariantPlaylistParser.writePlaylistAsBytes(playlist);
        String string = multivariantPlaylistParser.writePlaylistAsString(playlist);
        ByteBuffer byteBuffer = multivariantPlaylistParser.writePlaylistAsByteBuffer(playlist);

        assertArrayEquals(bytes, string.getBytes(UTF_8));
        assertArrayEquals(bytes, byteBuffer.array());
        assertArrayEquals(string.getBytes(UTF_8), byteBuffer.array());
    }
}