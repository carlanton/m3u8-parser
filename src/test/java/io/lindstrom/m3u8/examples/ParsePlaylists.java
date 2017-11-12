package io.lindstrom.m3u8.examples;

import io.lindstrom.m3u8.parser.MasterPlaylistParser;
import io.lindstrom.m3u8.model.MasterPlaylist;
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
    private final Path masterPlaylist = Paths.get("src/test/resources/master/master.m3u8");
    private final MasterPlaylistParser masterPlaylistParser  = new MasterPlaylistParser();

    @Test
    public void readPlaylist() throws Exception {
        Set<MasterPlaylist> playlists = new HashSet<>();

        playlists.add(
                // From Path
                masterPlaylistParser.readPlaylist(masterPlaylist)
        );

        playlists.add(
                // From String
                masterPlaylistParser.readPlaylist(new String(Files.readAllBytes(masterPlaylist), UTF_8))
        );

        try (InputStream inputStream = Files.newInputStream(masterPlaylist)) {
            playlists.add(
                    // From InputStream
                    masterPlaylistParser.readPlaylist(inputStream)
            );
        }

        try (BufferedReader bufferedReader = Files.newBufferedReader(masterPlaylist, UTF_8)) {
            playlists.add(
                    // From BufferedReader
                    masterPlaylistParser.readPlaylist(bufferedReader)
            );
        }

        playlists.add(
                // From Iterator<String>
                masterPlaylistParser.readPlaylist(Files.readAllLines(masterPlaylist, UTF_8).iterator())
        );

        // All methods should produce the same playlist
        assertEquals(1, playlists.size());
    }

    @Test
    public void writePlaylist() throws Exception {
        MasterPlaylist playlist = masterPlaylistParser.readPlaylist(masterPlaylist);

        byte[] bytes = masterPlaylistParser.writePlaylistAsBytes(playlist);
        String string = masterPlaylistParser.writePlaylistAsString(playlist);
        ByteBuffer byteBuffer = masterPlaylistParser.writePlaylistAsByteBuffer(playlist);

        assertArrayEquals(bytes, string.getBytes(UTF_8));
        assertArrayEquals(bytes, byteBuffer.array());
        assertArrayEquals(string.getBytes(UTF_8), byteBuffer.array());
    }
}
