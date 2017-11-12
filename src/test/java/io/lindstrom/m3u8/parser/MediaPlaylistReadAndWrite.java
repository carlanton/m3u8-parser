package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.MediaPlaylist;
import io.lindstrom.m3u8.parser.MediaPlaylistParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.runners.Parameterized.Parameter;

@RunWith(Parameterized.class)
public class MediaPlaylistReadAndWrite {
    private final MediaPlaylistParser mediaPlaylistParser = new MediaPlaylistParser();

    @Parameters
    public static List<Path> data() throws IOException {
        Path resources = Paths.get("src/test/resources/");
        List<Path> media = Files.list(resources.resolve("media")).collect(Collectors.toList());

        media.addAll(Stream.of(
                "liveMediaPlaylist.m3u8",
                "negativeDurationMediaPlaylist.m3u8",
                "playlistWithEncryptedMediaSegments.m3u8",
                "simpleMediaPlaylist.m3u8",
                "withDiscontinuity.m3u8")
                .map(resources.resolve("open-m3u8")::resolve)
                .collect(Collectors.toList()));

        return media;
    }

    @Parameter
    public Path playlistPath;

    @Test
    public void readAndWrite() throws Exception {
        MediaPlaylist playlist = mediaPlaylistParser.readPlaylist(playlistPath);
        assertEquals(playlist, mediaPlaylistParser.readPlaylist(mediaPlaylistParser.writePlaylistAsString(playlist)));
    }
}
