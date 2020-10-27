package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.MasterPlaylist;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.runners.Parameterized.Parameter;

@RunWith(Parameterized.class)
public class MasterPlaylistReadAndWrite {
    private final MasterPlaylistParser masterPlaylistParser = new MasterPlaylistParser();

    @Parameters
    public static List<Path> data() {
        return Stream.of(
                "master/apple-master.m3u8",
                "master/master.m3u8",
                "master/master-variables.m3u8",
                "open-m3u8/masterPlaylist.m3u8",
                "open-m3u8/masterPlaylistWithAlternativeAudio.m3u8",
                "open-m3u8/masterPlaylistWithAlternativeVideo.m3u8",
                "open-m3u8/masterPlaylistWithIFrames.m3u8")
                .map(p -> Paths.get("src/test/resources/", p))
                .collect(Collectors.toList());
    }

    @Parameter
    public Path playlistPath;

    @Test
    public void readAndWrite() throws Exception {
        MasterPlaylist playlist = masterPlaylistParser.readPlaylist(playlistPath);
        assertEquals(playlist, masterPlaylistParser.readPlaylist(masterPlaylistParser.writePlaylistAsString(playlist)));
    }

    @Test
    public void readAndWriteAttributeQuoting() throws Exception {
        String original = new String(Files.readAllBytes(playlistPath), StandardCharsets.UTF_8);
        String written = masterPlaylistParser.writePlaylistAsString(masterPlaylistParser.readPlaylist(original));

        TestUtils.attributeConsistencyCheck(original, written, playlistPath);
    }

    @Test
    public void streamInfoUriTest() throws IOException {
        String original = new String(Files.readAllBytes(playlistPath), StandardCharsets.UTF_8);
        String written = masterPlaylistParser.writePlaylistAsString(masterPlaylistParser.readPlaylist(playlistPath));
        assertEquals(variantUris(original), variantUris(written));
    }

    private static List<String> variantUris(String playlist) {
        List<String> uris = new ArrayList<>();
        String[] lines = playlist.split("\n");
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            if (line.startsWith("#EXT-X-STREAM-INF:")) {
                uris.add(lines[i + 1]);
            }
        }
        return uris;
    }
}
