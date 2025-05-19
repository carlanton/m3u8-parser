package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.MultivariantPlaylist;
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
public class MultivariantPlaylistReadAndWrite {
    private final MultivariantPlaylistParser multivariantPlaylistParser = new MultivariantPlaylistParser();

    @Parameters
    public static List<Path> data() {
        return Stream.of(
                "multivariant/apple-multivariant.m3u8",
                "multivariant/multivariant.m3u8",
                "multivariant/multivariant-variables.m3u8",
                "multivariant/multivariant-alt-video.m3u8",
                "open-m3u8/multivariantPlaylist.m3u8",
                "open-m3u8/multivariantPlaylistWithAlternativeAudio.m3u8",
                "open-m3u8/multivariantPlaylistWithAlternativeVideo.m3u8",
                "open-m3u8/multivariantPlaylistWithIFrames.m3u8",
                "multivariant/content-steering.m3u8")
                .map(p -> Paths.get("src/test/resources/", p))
                .collect(Collectors.toList());
    }

    @Parameter
    public Path playlistPath;

    @Test
    public void readAndWrite() throws Exception {
        MultivariantPlaylist playlist = multivariantPlaylistParser.readPlaylist(playlistPath);
        assertEquals(playlist, multivariantPlaylistParser.readPlaylist(multivariantPlaylistParser.writePlaylistAsString(playlist)));
    }

    @Test
    public void readAndWriteAttributeQuoting() throws Exception {
        String original = new String(Files.readAllBytes(playlistPath), StandardCharsets.UTF_8);
        String written = multivariantPlaylistParser.writePlaylistAsString(multivariantPlaylistParser.readPlaylist(original));

        TestUtils.attributeConsistencyCheck(original, written, playlistPath);
    }

    @Test
    public void streamInfoUriTest() throws IOException {
        String original = new String(Files.readAllBytes(playlistPath), StandardCharsets.UTF_8);
        String written = multivariantPlaylistParser.writePlaylistAsString(multivariantPlaylistParser.readPlaylist(playlistPath));
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