package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.MainPlaylist;
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
public class MainPlaylistReadAndWrite {
    private final MainPlaylistParser mainPlaylistParser = new MainPlaylistParser();

    @Parameters
    public static List<Path> data() {
        return Stream.of(
                "main/apple-main.m3u8",
                "main/main.m3u8",
                "main/main-variables.m3u8",
                "open-m3u8/mainPlaylist.m3u8",
                "open-m3u8/mainPlaylistWithAlternativeAudio.m3u8",
                "open-m3u8/mainPlaylistWithAlternativeVideo.m3u8",
                "open-m3u8/mainPlaylistWithIFrames.m3u8")
                .map(p -> Paths.get("src/test/resources/", p))
                .collect(Collectors.toList());
    }

    @Parameter
    public Path playlistPath;

    @Test
    public void readAndWrite() throws Exception {
        MainPlaylist playlist = mainPlaylistParser.readPlaylist(playlistPath);
        assertEquals(playlist, mainPlaylistParser.readPlaylist(mainPlaylistParser.writePlaylistAsString(playlist)));
    }

    @Test
    public void readAndWriteAttributeQuoting() throws Exception {
        String original = new String(Files.readAllBytes(playlistPath), StandardCharsets.UTF_8);
        String written = mainPlaylistParser.writePlaylistAsString(mainPlaylistParser.readPlaylist(original));

        TestUtils.attributeConsistencyCheck(original, written, playlistPath);
    }

    @Test
    public void streamInfoUriTest() throws IOException {
        String original = new String(Files.readAllBytes(playlistPath), StandardCharsets.UTF_8);
        String written = mainPlaylistParser.writePlaylistAsString(mainPlaylistParser.readPlaylist(playlistPath));
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
