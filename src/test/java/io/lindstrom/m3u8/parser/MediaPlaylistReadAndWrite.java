package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.MediaPlaylist;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.nio.charset.StandardCharsets;
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
    public static List<Path> data() {
        return Stream.of(
                "media/iframe.m3u8",
                "media/apple-media.m3u8",
                "media/variant.m3u8",
                "media/mp4-iframe.m3u8",
                "media/mp4.m3u8",
                "media/media-playlist-with-date-range.m3u8",
                "media/media-playlist-with-date-range-and-cue-out-cue-in.m3u8",
                "media/issue-17.m3u8",
                "media/ll-hls.m3u8",
                "open-m3u8/liveMediaPlaylist.m3u8",
                "open-m3u8/negativeDurationMediaPlaylist.m3u8",
                "open-m3u8/playlistWithEncryptedMediaSegments.m3u8",
                "open-m3u8/simpleMediaPlaylist.m3u8",
                "open-m3u8/withDiscontinuity.m3u8")
                .map(p -> Paths.get("src/test/resources/", p))
                .collect(Collectors.toList());
    }

    @Parameter
    public Path playlistPath;

    @Test
    public void readAndWrite() throws Exception {
        MediaPlaylist playlist = mediaPlaylistParser.readPlaylist(playlistPath);

        assertEquals(playlist, mediaPlaylistParser.readPlaylist(mediaPlaylistParser.writePlaylistAsString(playlist)));
    }

    @Test
    public void readAndWriteAttributeQuoting() throws Exception {
        String original = new String(Files.readAllBytes(playlistPath), StandardCharsets.UTF_8);
        String written = mediaPlaylistParser.writePlaylistAsString(mediaPlaylistParser.readPlaylist(original));

        TestUtils.attributeConsistencyCheck(original, written, playlistPath);
    }
}
